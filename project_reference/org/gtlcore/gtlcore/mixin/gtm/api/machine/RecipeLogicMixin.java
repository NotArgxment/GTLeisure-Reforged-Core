package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType.ICustomRecipeLogic;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.api.recipe.lookup.RecipeIterator;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.ResearchStationMachine;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.machine.ISuspendableMachine;
import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RecipeLogic.class)
public abstract class RecipeLogicMixin implements ILockRecipe, IRecipeStatus {
   @Persisted
   private boolean isLock = false;
   @Persisted
   private GTRecipe lockRecipe;
   private RecipeResult recipeStatus;
   private RecipeResult workingStatus;
   @Shadow(remap = false)
   @Final
   public IRecipeLogicMachine machine;
   @Shadow(remap = false)
   @Final
   protected Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches;
   @Shadow(remap = false)
   public List<GTRecipe> lastFailedMatches;
   @Shadow(remap = false)
   @Nullable
   protected GTRecipe lastRecipe;
   @Shadow(remap = false)
   @Nullable
   protected GTRecipe lastOriginRecipe;
   @Shadow(remap = false)
   protected OCParams ocParams;
   @Shadow(remap = false)
   protected OCResult ocResult;
   @Shadow(remap = false)
   protected boolean recipeDirty;
   @Shadow(remap = false)
   protected int progress;
   @Shadow(remap = false)
   protected int duration;
   @Shadow(remap = false)
   private boolean isActive;
   @Shadow(remap = false)
   private Status status;
   @Shadow(remap = false)
   protected long totalContinuousRunningTime;

   @Shadow(remap = false)
   public abstract void markLastRecipeDirty();

   @Shadow(remap = false)
   public abstract void setStatus(Status var1);

   @Shadow(remap = false)
   public abstract Status getStatus();

   @Shadow(remap = false)
   public abstract void updateTickSubscription();

   @Shadow(remap = false)
   public abstract ActionResult handleTickRecipe(GTRecipe var1);

   @Shadow(remap = false)
   public abstract void interruptRecipe();

   @Shadow(remap = false)
   public abstract void setWaiting(@Nullable Component var1);

   @Shadow(remap = false)
   protected abstract void doDamping();

   @Override
   public void setLock(boolean look) {
      this.isLock = look;
      this.lockRecipe = null;
      this.updateTickSubscription();
   }

   @Overwrite(remap = false)
   protected boolean handleRecipeIO(GTRecipe recipe, IO io) {
      return this.machine.hasProxies() && io != IO.BOTH ? RecipeRunnerHelper.handleRecipeInput(this.machine, recipe) : false;
   }

   @Overwrite(remap = false)
   public void handleRecipeWorking() {
      assert this.lastRecipe != null;
      ActionResult result = this.handleTickRecipe(this.lastRecipe);
      if (result.isSuccess()) {
         this.setStatus(Status.WORKING);
         if (!this.machine.onWorking()) {
            this.interruptRecipe();
            return;
         }

         this.progress++;
         this.totalContinuousRunningTime++;
      } else {
         this.setWaiting((Component)result.reason().get());
      }

      if (this.status == Status.WAITING) {
         this.doDamping();
      }
   }

   @Overwrite(remap = false)
   public void setupRecipe(GTRecipe recipe) {
      if (!this.machine.beforeWorking(recipe)) {
         this.setStatus(Status.IDLE);
         this.progress = 0;
         this.duration = 0;
         this.isActive = false;
      } else {
         if (this.handleRecipeIO(recipe, IO.IN)) {
            if (this.lastRecipe != null && !recipe.equals(this.lastRecipe)) {
               this.chanceCaches.clear();
            }

            this.recipeDirty = false;
            this.lastRecipe = recipe;
            this.setStatus(Status.WORKING);
            this.progress = 0;
            this.duration = recipe.duration;
            this.isActive = true;
         }
      }
   }

   @Overwrite(remap = false)
   private void handleSearchingRecipes(Iterator<GTRecipe> matches) {
      while (matches != null && matches.hasNext()) {
         GTRecipe match = matches.next();
         if (match != null && this.checkMatchedRecipeAvailable(match)) {
            return;
         }
      }
   }

   @Overwrite(remap = false)
   public void findAndHandleRecipe() {
      this.lastRecipe = null;
      this.recipeStatus = null;
      this.workingStatus = null;
      if (this.isLock && this.lockRecipe != null) {
         this.lastOriginRecipe = this.lockRecipe;
         GTRecipe modified = this.machine.fullModifyRecipe(this.lastOriginRecipe.copy(), this.ocParams, this.ocResult);
         if (modified != null && this.gtlcore$checkLastRecipe(modified)) {
            this.setupRecipe(modified);
         }
      } else {
         this.lastOriginRecipe = null;
         this.handleSearchingRecipes(this.gtlcore$searchRecipe(this.machine, this.machine instanceof ResearchStationMachine ? r -> {
            if (!this.machine.hasProxies()) {
               return false;
            }

            if (((IGTRecipe)r).getEuTier() > ((ITieredMachine)this.machine).getTier()) {
               RecipeResult.of(this.machine, RecipeResult.FAIL_VOLTAGE_TIER);
               return false;
            }

            ActionResult result = r.matchRecipeContents(IO.IN, this.machine, r.inputs, false);
            if (!result.isSuccess()) {
               RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
               return false;
            }

            if (r.hasTick()) {
               result = r.matchRecipeContents(IO.IN, this.machine, r.tickInputs, true);
               if (!result.isSuccess() && result.reason() != null) {
                  String s = ((Component)result.reason().get()).toString();
                  if (s.contains("cwu")) {
                     RecipeResult.of(this.machine, RecipeResult.FAIL_NO_ENOUGH_CWU_IN);
                  } else if (s.contains("eu.name")) {
                     RecipeResult.of(this.machine, RecipeResult.FAIL_NO_ENOUGH_EU_IN);
                  }
               }

               return result.isSuccess();
            } else {
               return true;
            }
         } : this::gtlcore$checkLastRecipe));
      }

      this.recipeDirty = false;
   }

   @Overwrite(remap = false)
   public boolean checkMatchedRecipeAvailable(GTRecipe match) {
      GTRecipe modified = this.machine.fullModifyRecipe(match.copy(), this.ocParams, this.ocResult);
      if (modified != null) {
         if (this.gtlcore$checkLastRecipe(modified)) {
            this.setupRecipe(modified);
         }

         if (this.lastRecipe != null && this.getStatus() == Status.WORKING) {
            this.lastOriginRecipe = match;
            if (this.isLock) {
               this.lockRecipe = match;
            }

            return true;
         }
      }

      return false;
   }

   @Overwrite(remap = false)
   public void onRecipeFinish() {
      this.machine.afterWorking();
      if (this.lastRecipe != null) {
         RecipeRunnerHelper.handleRecipeOutput(this.machine, this.lastRecipe);
         if (this.machine instanceof ISuspendableMachine suspendableMachine && suspendableMachine.gtlcore$isSuspendAfterFinish()) {
            this.setStatus(Status.SUSPEND);
            suspendableMachine.gtlcore$setSuspendAfterFinish(false);
         } else {
            if (this.machine.alwaysTryModifyRecipe()) {
               if (this.lastOriginRecipe != null) {
                  GTRecipe modified = this.machine.fullModifyRecipe(this.lastOriginRecipe.copy(), this.ocParams, this.ocResult);
                  if (modified == null) {
                     this.markLastRecipeDirty();
                  } else {
                     this.lastRecipe = modified;
                  }
               } else {
                  this.markLastRecipeDirty();
               }
            }

            if (!this.recipeDirty && this.gtlcore$checkLastRecipe(this.lastRecipe)) {
               this.setupRecipe(this.lastRecipe);
               return;
            }

            this.setStatus(Status.IDLE);
         }

         this.progress = 0;
         this.duration = 0;
         this.isActive = false;
      }
   }

   @Unique
   public Iterator<GTRecipe> gtlcore$searchRecipe(IRecipeCapabilityHolder holder, @NotNull Predicate<GTRecipe> canHandle) {
      if (!holder.hasProxies()) {
         return null;
      }

      RecipeIterator iterator = this.machine.getRecipeType().getLookup().getRecipeIterator(holder, canHandle);
      boolean any = false;
      GTRecipe recipe = null;

      while (iterator.hasNext()) {
         recipe = iterator.next();
         if (recipe != null) {
            any = true;
            break;
         }
      }

      if (any) {
         iterator.reset();
         return Collections.singleton(recipe).iterator();
      }

      for (ICustomRecipeLogic logic : this.machine.getRecipeType().getCustomRecipeLogicRunners()) {
         recipe = logic.createCustomRecipe(holder);
         if (recipe != null) {
            return Collections.singleton(recipe).iterator();
         }
      }

      if (this.recipeStatus == null || this.recipeStatus.isSuccess()) {
         RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
      }

      return Collections.emptyIterator();
   }

   @Unique
   private boolean gtlcore$checkLastRecipe(GTRecipe lastRecipe) {
      return RecipeRunnerHelper.matchRecipe(this.machine, lastRecipe)
         && lastRecipe.matchTickRecipe(this.machine).isSuccess()
         && lastRecipe.checkConditions(this.machine.getRecipeLogic()).isSuccess();
   }

   @Override
   public boolean isLock() {
      return this.isLock;
   }

   @Override
   public GTRecipe getLockRecipe() {
      return this.lockRecipe;
   }

   @Override
   public void setLockRecipe(GTRecipe lockRecipe) {
      this.lockRecipe = lockRecipe;
   }

   @Override
   public RecipeResult getRecipeStatus() {
      return this.recipeStatus;
   }

   @Override
   public void setRecipeStatus(RecipeResult recipeStatus) {
      this.recipeStatus = recipeStatus;
   }

   @Override
   public RecipeResult getWorkingStatus() {
      return this.workingStatus;
   }

   @Override
   public void setWorkingStatus(RecipeResult workingStatus) {
      this.workingStatus = workingStatus;
   }
}
