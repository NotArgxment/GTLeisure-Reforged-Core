package org.gtlcore.gtlcore.mixin.gtm.registry;

import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.mojang.datafixers.util.Pair;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTRecipeModifiers.class)
public abstract class GTRecipeModifiersMixin {
   @Overwrite(remap = false)
   public static GTRecipe ebfOverclock(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
         int blastFurnaceTemperature = coilMachine.getCoilType().getCoilTemperature() + 100 * Math.max(0, coilMachine.getTier() - 2);
         if (recipe.data.m_128441_("ebf_temp")) {
            if (recipe.data.m_128451_("ebf_temp") <= blastFurnaceTemperature) {
               return RecipeHelper.getRecipeEUtTier(recipe) > coilMachine.getTier()
                  ? null
                  : RecipeHelper.applyOverclock(
                     new OverclockingLogic(
                        (p, r, maxVoltage) -> OverclockingLogic.heatingCoilOC(
                           params, result, maxVoltage, blastFurnaceTemperature, recipe.data.m_128441_("ebf_temp") ? recipe.data.m_128451_("ebf_temp") : 0
                        )
                     ),
                     recipe,
                     coilMachine.getOverclockVoltage(),
                     params,
                     result
                  );
            }

            RecipeResult.of((IRecipeLogicMachine)machine, RecipeResult.FAIL_NO_ENOUGH_TEMPERATURE);
            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   @Overwrite(remap = false)
   public static GTRecipe hatchParallel(
      MetaMachine machine, @NotNull GTRecipe recipe, boolean modifyDuration, @NotNull OCParams params, @NotNull OCResult result
   ) {
      if (machine instanceof IMultiController controller && controller instanceof IRecipeCapabilityMachine && controller.isFormed()) {
         IParallelHatch hatch = ((IRecipeCapabilityMachine)controller).getParallelHatch();
         if (hatch != null) {
            long recipeEU = RecipeHelper.getInputEUt(recipe);
            Pair<GTRecipe, Integer> parallelRecipe = ParallelLogic.applyParallel(machine, recipe, hatch.getCurrentParallel(), modifyDuration);
            if ((Integer)parallelRecipe.getSecond() == 0) {
               return null;
            }

            result.init(recipeEU, recipe.duration, (Integer)parallelRecipe.getSecond(), params.getOcAmount());
            return (GTRecipe)parallelRecipe.getFirst();
         }
      }

      return recipe;
   }

   @Overwrite(remap = false)
   public static Pair<GTRecipe, Integer> fastParallel(MetaMachine machine, @NotNull GTRecipe recipe, int maxParallel, boolean modifyDuration) {
      if (machine instanceof IRecipeCapabilityHolder holder) {
         while (maxParallel > 0) {
            GTRecipe copied = recipe.copy(ContentModifier.multiplier(maxParallel), modifyDuration);
            if (RecipeRunnerHelper.matchRecipe(holder, copied) && copied.matchTickRecipe(holder).isSuccess()) {
               return Pair.of(copied, maxParallel);
            }

            maxParallel /= 2;
         }
      }

      return Pair.of(recipe, 1);
   }
}
