package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.common.recipe.condition.RockBreakerCondition;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RockBreakerCondition.class)
public abstract class RockBreakerConditionMixin extends RecipeCondition {
   @Overwrite(remap = false)
   public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
      Fluid fluidA = Registries.getFluid(recipe.data.m_128461_("fluidA"));
      Fluid fluidB = Registries.getFluid(recipe.data.m_128461_("fluidB"));
      boolean hasFluidA = false;
      boolean hasFluidB = false;
      if (recipeLogic.machine instanceof WorkableElectricMultiblockMachine MMachine) {
         List<IRecipeHandler<?>> handlers = (List<IRecipeHandler<?>>)MMachine.getCapabilitiesProxy().get(IO.IN, FluidRecipeCapability.CAP);
         if (handlers == null) {
            return false;
         }

         for (IRecipeHandler<?> handler : handlers) {
            if (handler instanceof NotifiableFluidTank tank) {
               if (tank.getFluidInTank(0).getFluid() == fluidA) {
                  hasFluidA = true;
               }

               if (tank.getFluidInTank(0).getFluid() == fluidB) {
                  hasFluidB = true;
               }

               if (hasFluidA && hasFluidB) {
                  return true;
               }

               RecipeResult.of(recipeLogic.machine, RecipeResult.FAIL_LACK_FLUID);
            }
         }
      } else {
         Level level = recipeLogic.machine.self().getLevel();
         BlockPos pos = recipeLogic.machine.self().getPos();

         for (Direction side : GTUtil.DIRECTIONS) {
            if (side.m_122434_() != Axis.Y) {
               FluidState fluid = Objects.requireNonNull(level).m_6425_(pos.m_121945_(side));
               if (fluid.m_76152_() == fluidA) {
                  hasFluidA = true;
               }

               if (fluid.m_76152_() == fluidB) {
                  hasFluidB = true;
               }

               if (hasFluidA && hasFluidB) {
                  return true;
               }

               RecipeResult.of(recipeLogic.machine, RecipeResult.FAIL_LACK_FLUID);
            }
         }
      }

      return false;
   }
}
