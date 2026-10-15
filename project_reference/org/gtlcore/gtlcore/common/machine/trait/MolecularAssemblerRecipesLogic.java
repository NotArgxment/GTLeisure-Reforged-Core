package org.gtlcore.gtlcore.common.machine.trait;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import org.gtlcore.gtlcore.api.machine.multiblock.MolecularAssemblerMultiblockMachine;
import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMolecularAssemblerHandler;
import org.gtlcore.gtlcore.mixin.gtm.api.recipe.RecipeLogicAccessor;
import org.jetbrains.annotations.Nullable;

public class MolecularAssemblerRecipesLogic extends RecipeLogic {
   private final ParallelMachine parallel;

   public MolecularAssemblerRecipesLogic(ParallelMachine machine) {
      super((IRecipeLogicMachine)machine);
      this.parallel = machine;
   }

   public MolecularAssemblerMultiblockMachine getMachine() {
      return (MolecularAssemblerMultiblockMachine)super.getMachine();
   }

   @Nullable
   private GTRecipe getRecipe() {
      IMolecularAssemblerHandler handler = this.getMachine().getMAHandler();
      return handler != null ? handler.extractGTRecipe(this.parallel.getMaxParallel(), this.getMachine().getTickDuration()) : null;
   }

   public void updateTickSubscription() {
      if (!this.isSuspend() && this.machine.isRecipeLogicAvailable()) {
         this.subscription = this.getMachine().subscribeServerTick(this.subscription, this::serverTick);
      } else if (this.subscription != null) {
         this.subscription.unsubscribe();
         this.subscription = null;
      }
   }

   public void serverTick() {
      if (!this.isSuspend()) {
         if (!this.isIdle() && this.lastRecipe != null) {
            if (this.progress < this.duration) {
               this.handleRecipeWorking();
            }

            if (this.progress >= this.duration) {
               this.onRecipeFinish();
            }
         } else if (this.lastRecipe != null || !this.machine.keepSubscribing() || this.getMachine().getOffsetTimer() % 5L == 0L) {
            this.findAndHandleRecipe();
         }
      }

      boolean unsubscribe = false;
      if (this.isSuspend()) {
         unsubscribe = true;
      } else if (this.lastRecipe == null && this.isIdle() && !this.machine.keepSubscribing()) {
         unsubscribe = true;
      }

      if (unsubscribe && this.subscription != null) {
         this.subscription.unsubscribe();
         this.subscription = null;
      }
   }

   public void handleRecipeWorking() {
      this.progress++;
      this.totalContinuousRunningTime++;
   }

   public void findAndHandleRecipe() {
      this.lastRecipe = null;
      GTRecipe match = this.getRecipe();
      if (match != null) {
         this.setupRecipe(match);
      }
   }

   public void setupRecipe(GTRecipe recipe) {
      this.lastRecipe = recipe;
      this.setStatus(Status.WORKING);
      this.progress = 0;
      this.duration = recipe.duration;
      ((RecipeLogicAccessor)this).setIsActive(true);
   }

   public void onRecipeFinish() {
      if (this.lastRecipe != null) {
         IMolecularAssemblerHandler handler = this.getMachine().getMAHandler();
         if (handler != null) {
            handler.handleRecipeOutput(this.lastRecipe);
         }

         GTRecipe match = this.getRecipe();
         if (match != null) {
            this.setupRecipe(match);
         } else {
            this.lastRecipe = null;
            this.setStatus(Status.IDLE);
            this.progress = 0;
            this.duration = 0;
            ((RecipeLogicAccessor)this).setIsActive(false);
         }
      }
   }
}
