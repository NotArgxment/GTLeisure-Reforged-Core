package org.gtlcore.gtlcore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.jetbrains.annotations.NotNull;

public class HeatExchangerMachine extends NoEnergyMultiblockMachine {
   public HeatExchangerMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
      if (machine instanceof HeatExchangerMachine hMachine) {
         if (((FluidIngredient)FluidRecipeCapability.CAP.of(((Content)((List)recipe.inputs.get(FluidRecipeCapability.CAP)).get(1)).getContent())).getStacks()[0]
               .getFluid()
            == GTMaterials.Water.getFluid()) {
            return (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe, 2147483647, false).getFirst();
         }

         FluidIngredient fluidIngredient = (FluidIngredient)FluidRecipeCapability.CAP
            .of(((Content)((List)recipe.inputs.get(FluidRecipeCapability.CAP)).get(0)).getContent());
         String prefix = Objects.requireNonNull(
               ((IForgeRegistry)ForgeRegistries.FLUID_TYPES.get()).getKey(fluidIngredient.getStacks()[0].getFluid().getFluidType())
            )
            .m_135815_();
         GTRecipe plasmaRecipe = new GTRecipeBuilder(GTLCore.id("heat_exchanger_" + prefix), GTRecipeTypes.DUMMY_RECIPES)
            .inputFluids(new FluidIngredient[]{fluidIngredient})
            .outputFluids(
               ((List)recipe.outputs.get(FluidRecipeCapability.CAP))
                  .stream()
                  .map(c -> (FluidIngredient)FluidRecipeCapability.CAP.of(c.getContent()))
                  .toArray(FluidIngredient[]::new)
            )
            .duration(200)
            .buildRawRecipe();
         if (!RecipeRunnerHelper.matchRecipeInputNoMEInnerCache(hMachine, plasmaRecipe)) {
            return null;
         }

         Pair<GTRecipe, Integer> result = GTRecipeModifiers.accurateParallel(machine, plasmaRecipe, 2147483647, false);
         long count = ((Integer)result.getSecond()).intValue()
            * ((FluidIngredient)FluidRecipeCapability.CAP.of(((Content)((List)recipe.inputs.get(FluidRecipeCapability.CAP)).get(1)).getContent())).getStacks()[0]
               .getAmount();
         if (MachineIO.inputFluid(hMachine, GTMaterials.DistilledWater.getFluid(count))) {
            return (GTRecipe)result.getFirst();
         }

         RecipeResult.of((IRecipeLogicMachine)machine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.enough.distilledwater")));
      }

      return null;
   }
}
