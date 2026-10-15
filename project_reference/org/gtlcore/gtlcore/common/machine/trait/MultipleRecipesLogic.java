package org.gtlcore.gtlcore.common.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.api.machine.ISuspendableMachine;
import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine;
import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;

public class MultipleRecipesLogic extends RecipeLogic implements ILockRecipe, IRecipeStatus {
   private final ParallelMachine parallel;
   private final BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck;
   private static final int MAX_THREADS = 64;
   private double reductionRatio;

   public MultipleRecipesLogic(ParallelMachine machine) {
      this(machine, null);
   }

   public MultipleRecipesLogic(ParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck) {
      this(machine, dataCheck, 1.0, 1.0);
   }

   public MultipleRecipesLogic(ParallelMachine machine, BiPredicate<CompoundTag, IRecipeLogicMachine> dataCheck, double reductionEUt, double reductionDuration) {
      super((IRecipeLogicMachine)machine);
      this.parallel = machine;
      this.dataCheck = dataCheck;
      this.reductionRatio = reductionEUt * reductionDuration;
   }

   public void setReduction(double reductionEUt, double reductionDuration) {
      this.reductionRatio = reductionEUt * reductionDuration;
   }

   public WorkableElectricMultiblockMachine getMachine() {
      return (WorkableElectricMultiblockMachine)super.getMachine();
   }

   public void findAndHandleRecipe() {
      this.lastRecipe = null;
      this.setRecipeStatus(null);
      GTRecipe match = this.getRecipe();
      if (match != null) {
         RecipeResult.of(this.machine, RecipeResult.SUCCESS);
         if (RecipeRunnerHelper.matchRecipeOutput(this.machine, match)) {
            this.setupRecipe(match);
         }
      }
   }

   protected double getTotalEuOfRecipe(GTRecipe recipe) {
      return (double)RecipeHelper.getInputEUt(recipe) * recipe.duration;
   }

   protected double getEuMultiplier() {
      IMaintenanceMachine maintenanceMachine = ((IRecipeCapabilityMachine)this.parallel).getMaintenanceMachine();
      return maintenanceMachine != null ? maintenanceMachine.getDurationMultiplier() * this.reductionRatio : this.reductionRatio;
   }

   private GTRecipe getRecipe() {
      if (!this.machine.hasProxies()) {
         return null;
      }

      long maxEUt = this.getMachine().getOverclockVoltage();
      if (maxEUt <= 0L) {
         return null;
      }

      Iterator<GTRecipe> iterator = this.lookupRecipeIterator();
      GTRecipe output = GTRecipeBuilder.ofRaw().buildRawRecipe();
      output.outputs.put(ItemRecipeCapability.CAP, new ObjectArrayList());
      output.outputs.put(FluidRecipeCapability.CAP, new ObjectArrayList());
      double totalEu = 0.0;
      long remain = this.parallel.getMaxParallel() * 64L;
      double euMultiplier = this.getEuMultiplier();

      while (remain > 0L && iterator.hasNext()) {
         GTRecipe match = iterator.next();
         if (match != null) {
            long p = IParallelLogic.getMaxParallel(this.machine, match, remain);
            if (p > 0L) {
               if (p > 1L) {
                  match = match.copy(ContentModifier.multiplier(p), false);
               }

               ((IGTRecipe)match).setRealParallels(p);
               match = IParallelLogic.getRecipeOutputChance(this.machine, match);
               if (RecipeRunnerHelper.handleRecipeInput(this.machine, match)) {
                  remain -= p;
                  totalEu += this.getTotalEuOfRecipe(match) * euMultiplier;
                  List<Content> item = (List<Content>)match.outputs.get(ItemRecipeCapability.CAP);
                  if (item != null) {
                     ((List)output.outputs.get(ItemRecipeCapability.CAP)).addAll(item);
                  }

                  List<Content> fluid = (List<Content>)match.outputs.get(FluidRecipeCapability.CAP);
                  if (fluid != null) {
                     ((List)output.outputs.get(FluidRecipeCapability.CAP)).addAll(fluid);
                  }
               }

               if (totalEu / maxEUt > 10000.0) {
                  break;
               }
            }
         }
      }

      if (((List)output.outputs.get(ItemRecipeCapability.CAP)).isEmpty() && ((List)output.outputs.get(FluidRecipeCapability.CAP)).isEmpty()) {
         if (this.getRecipeStatus() == null || this.getRecipeStatus().isSuccess()) {
            RecipeResult.of(this.machine, RecipeResult.FAIL_FIND);
         }

         return null;
      } else {
         double d = totalEu / maxEUt;
         long eut = d > 20.0 ? maxEUt : (long)(maxEUt * d / 20.0);
         output.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(eut, 10000, 10000, 0, null, null)));
         output.duration = (int)Math.max(d, 20.0);
         IGTRecipe.of(output).setHasTick(true);
         return output;
      }
   }

   private Iterator<GTRecipe> lookupRecipeIterator() {
      if (this.isLock()) {
         if (this.getLockRecipe() == null) {
            this.setLockRecipe(this.machine.getRecipeType().getLookup().find(this.machine, this::checkRecipe));
         } else if (!this.checkRecipe(this.getLockRecipe())) {
            return Collections.emptyIterator();
         }

         return Collections.singleton(this.getLockRecipe()).iterator();
      } else {
         return this.machine.getRecipeType().getLookup().getRecipeIterator(this.machine, this::checkRecipe);
      }
   }

   private boolean checkRecipe(GTRecipe recipe) {
      return RecipeRunnerHelper.matchRecipe(this.machine, recipe)
         && IGTRecipe.of(recipe).getEuTier() <= this.getMachine().getTier()
         && recipe.checkConditions(this).isSuccess()
         && (this.dataCheck == null || this.dataCheck.test(recipe.data, this.machine));
   }

   public void onRecipeFinish() {
      this.machine.afterWorking();
      if (this.lastRecipe != null) {
         RecipeRunnerHelper.handleRecipeOutput(this.machine, this.lastRecipe);
      }

      if (this.machine instanceof ISuspendableMachine suspendableMachine && suspendableMachine.gtlcore$isSuspendAfterFinish()) {
         this.setStatus(Status.SUSPEND);
         suspendableMachine.gtlcore$setSuspendAfterFinish(false);
      } else {
         GTRecipe match = this.getRecipe();
         if (match != null && RecipeRunnerHelper.matchRecipeOutput(this.machine, match)) {
            this.setupRecipe(match);
            return;
         }

         this.setStatus(Status.IDLE);
      }

      this.progress = 0;
      this.duration = 0;
   }

   public ParallelMachine getParallel() {
      return this.parallel;
   }

   public BiPredicate<CompoundTag, IRecipeLogicMachine> getDataCheck() {
      return this.dataCheck;
   }

   public double getReductionRatio() {
      return this.reductionRatio;
   }
}
