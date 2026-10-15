package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class Ae2GtmProcessingPattern extends Ae2BaseProcessingPattern {
   public final GTRecipe recipe;

   public Ae2GtmProcessingPattern(ItemStack patternStack, ServerPlayer serverPlayer, GTRecipe recipe) {
      super(patternStack, serverPlayer);
      this.recipe = recipe;
   }

   public static Ae2GtmProcessingPattern of(GTRecipe recipe, ServerPlayer serverPlayer) {
      List<ItemStack> inputItemStack = RecipeStackHelper.getInputItemStacksFromRecipe(recipe);
      List<FluidStack> inputFluidStack = RecipeStackHelper.getInputFluidStacksFromRecipe(recipe);
      List<ItemStack> outputItemStack = RecipeStackHelper.getOutputItemStacksFromRecipe(recipe);
      List<FluidStack> outputFluidStack = RecipeStackHelper.getOutputFluidStacksFromRecipe(recipe);
      int inputsLength = inputItemStack.size() + inputFluidStack.size();
      GenericStack[] Inputs = new GenericStack[inputsLength];
      int inputIndex = 0;

      for (ItemStack itemStack : inputItemStack) {
         Inputs[inputIndex++] = new GenericStack(AEItemKey.of(itemStack.m_41720_()), itemStack.m_41613_());
      }

      for (FluidStack fluidStack : inputFluidStack) {
         Inputs[inputIndex++] = new GenericStack(AEFluidKey.of(fluidStack.getFluid()), fluidStack.getAmount());
      }

      int outputsLength = outputItemStack.size() + outputFluidStack.size();
      GenericStack[] Outputs = new GenericStack[outputsLength];
      int ouputIndex = 0;

      for (ItemStack itemStack : outputItemStack) {
         Outputs[ouputIndex++] = new GenericStack(AEItemKey.of(itemStack.m_41720_()), itemStack.m_41613_());
      }

      for (FluidStack fluidStack : outputFluidStack) {
         Outputs[ouputIndex++] = new GenericStack(AEFluidKey.of(fluidStack.getFluid()), fluidStack.getAmount());
      }

      ItemStack patternStack = PatternDetailsHelper.encodeProcessingPattern(Inputs, Outputs);
      return new Ae2GtmProcessingPattern(patternStack, serverPlayer, recipe);
   }
}
