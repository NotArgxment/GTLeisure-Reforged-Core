package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.recipe.condition.VentCondition;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SimpleSteamMachine.class)
public abstract class SimpleSteamMachineMixin extends SteamMachine implements ISteamMachine {
   public SimpleSteamMachineMixin(IMachineBlockEntity holder, boolean isHighPressure, Object... args) {
      super(holder, isHighPressure, args);
   }

   @Overwrite(remap = false)
   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof SimpleSteamMachine steamMachine) {
         if (RecipeHelper.getRecipeEUtTier(recipe) <= 1 && steamMachine.checkVenting()) {
            GTRecipe modified = recipe.copy();
            modified.conditions.add(VentCondition.INSTANCE);
            if (steamMachine.isHighPressure) {
               result.init(RecipeHelper.getInputEUt(recipe) * 2L, modified.duration, params.getOcAmount());
            } else {
               result.init(RecipeHelper.getInputEUt(recipe), modified.duration * 2, params.getOcAmount());
            }

            return modified;
         }

         if (!steamMachine.checkVenting()) {
            RecipeResult.of(steamMachine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.venting")));
         } else if (RecipeHelper.getRecipeEUtTier(recipe) > 1) {
            RecipeResult.of(steamMachine, RecipeResult.FAIL_VOLTAGE_TIER);
         }
      }

      return null;
   }

   @Override
   public double getConversionRate() {
      return (float)FluidHelper.getBucket() / 1000.0F;
   }
}
