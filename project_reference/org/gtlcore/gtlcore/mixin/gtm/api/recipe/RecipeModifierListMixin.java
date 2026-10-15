package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifierList;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
import org.gtlcore.gtlcore.api.recipe.IAdvancedOCResult;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RecipeModifierList.class)
public abstract class RecipeModifierListMixin {
   @Shadow(remap = false)
   @Final
   private RecipeModifier[] modifiers;

   @Overwrite(remap = false)
   @Nullable
   public GTRecipe apply(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      GTRecipe modifiedRecipe = recipe;

      for (RecipeModifier modifier : this.modifiers) {
         if (modifiedRecipe != null) {
            modifiedRecipe = modifier.apply(machine, modifiedRecipe, params, result);
         }
      }

      if (modifiedRecipe != null && result.getDuration() != 0) {
         modifiedRecipe.duration = result.getDuration();
         if (result.getEut() > 0L) {
            modifiedRecipe.tickInputs
               .put(
                  EURecipeCapability.CAP,
                  List.of(new Content(result.getEut(), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
               );
         } else if (result.getEut() < 0L) {
            modifiedRecipe.tickOutputs
               .put(
                  EURecipeCapability.CAP,
                  List.of(new Content(-result.getEut(), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
               );
         }

         int tryParallelMultiply = result.getParallel();
         IAdvancedOCResult advancedOCResult = (IAdvancedOCResult)result;
         if (tryParallelMultiply > 1) {
            Pair<GTRecipe, Integer> modifyResult = ParallelLogic.applyParallel(machine, modifiedRecipe, tryParallelMultiply, false);
            modifiedRecipe = (GTRecipe)modifyResult.getFirst();
            modifiedRecipe.ocTier = advancedOCResult.getBaseOCLevel();
            int actualAppliedMultiply = (Integer)modifyResult.getSecond();
            if (actualAppliedMultiply > 1) {
               double durationFactor = advancedOCResult.getDurationFactor();
               double voltageFactor = advancedOCResult.getVoltageFactor();
               if (durationFactor != 0.0 && voltageFactor != 0.0) {
                  long actualEUt;
                  if (actualAppliedMultiply == tryParallelMultiply) {
                     actualEUt = result.getParallelEUt();
                     modifiedRecipe.ocTier = result.getOcLevel();
                  } else {
                     int additionalTier = NumberUtils.getAdditionalTier(durationFactor, actualAppliedMultiply);
                     actualEUt = Math.round(Math.pow(voltageFactor, additionalTier) * result.getEut() * actualAppliedMultiply / tryParallelMultiply);
                     modifiedRecipe.ocTier += additionalTier;
                  }

                  if (result.getEut() > 0L) {
                     modifiedRecipe.tickInputs
                        .put(
                           EURecipeCapability.CAP,
                           List.of(new Content(actualEUt, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
                        );
                  } else if (result.getEut() < 0L) {
                     modifiedRecipe.tickOutputs
                        .put(
                           EURecipeCapability.CAP,
                           List.of(new Content(-actualEUt, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
                        );
                  }
               }
            }
         } else {
            modifiedRecipe.ocTier = advancedOCResult.getBaseOCLevel();
         }
      }

      result.reset();
      return modifiedRecipe;
   }
}
