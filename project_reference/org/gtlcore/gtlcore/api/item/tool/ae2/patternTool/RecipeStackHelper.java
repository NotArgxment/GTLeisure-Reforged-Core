package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;

public class RecipeStackHelper {
   final GTRecipe recipe;

   protected RecipeStackHelper(GTRecipe recipe) {
      this.recipe = recipe;
   }

   public List<ItemStack> getInputItemStacks() {
      return getInputItemStacksFromRecipe(this.recipe);
   }

   public List<FluidStack> getInputFluidStacks() {
      return getInputFluidStacksFromRecipe(this.recipe);
   }

   public List<ItemStack> getOutputItemStacks() {
      return getOutputItemStacksFromRecipe(this.recipe);
   }

   public List<FluidStack> getOutputFluidStacks() {
      return getOutputFluidStacksFromRecipe(this.recipe);
   }

   public static List<ItemStack> getInputItemStacksFromRecipe(GTRecipe recipe) {
      return recipe == null ? Collections.emptyList() : recipe.getInputContents(ItemRecipeCapability.CAP).stream().map(getContentItemStackFunction()).toList();
   }

   public static List<FluidStack> getInputFluidStacksFromRecipe(GTRecipe recipe) {
      return recipe == null
         ? Collections.emptyList()
         : recipe.getInputContents(FluidRecipeCapability.CAP).stream().map(getContentFluidStackFunction()).toList();
   }

   public static List<ItemStack> getOutputItemStacksFromRecipe(GTRecipe recipe) {
      return recipe == null ? Collections.emptyList() : recipe.getOutputContents(ItemRecipeCapability.CAP).stream().map(getContentItemStackFunction()).toList();
   }

   public static List<FluidStack> getOutputFluidStacksFromRecipe(GTRecipe recipe) {
      return recipe == null
         ? Collections.emptyList()
         : recipe.getOutputContents(FluidRecipeCapability.CAP).stream().map(getContentFluidStackFunction()).toList();
   }

   public static String getItemTranslatedName(ItemStack itemStack) {
      Item item = itemStack.m_41720_();
      return item.m_7626_(itemStack).getString() + "(" + Registries.getResourceKey(item) + " " + itemStack.m_41613_() + ") ";
   }

   public static String getFluidTranslatedName(FluidStack fluidStack) {
      return fluidStack.getDisplayName().getString() + "(" + Registries.getResourceKey(fluidStack) + " " + fluidStack.getAmount() + ") ";
   }

   public static List<ItemStack> getOutputItems(GTRecipe recipe) {
      return recipe.getOutputContents(ItemRecipeCapability.CAP)
         .stream()
         .map(content -> (Ingredient)ItemRecipeCapability.CAP.of(content.getContent()))
         .map(ingredient -> ingredient.m_43908_()[0])
         .collect(Collectors.toList());
   }

   @NotNull
   private static Function<Content, FluidStack> getContentFluidStackFunction() {
      return content -> {
         if (content != null && content.getContent() != null) {
            FluidStack[] stacks = ((FluidIngredient)FluidRecipeCapability.CAP.of(content.getContent())).getStacks();
            return stacks != null && stacks.length > 0 ? stacks[0] : FluidStack.empty();
         } else {
            return FluidStack.empty();
         }
      };
   }

   @NotNull
   private static Function<Content, ItemStack> getContentItemStackFunction() {
      return content -> {
         if (content != null && content.getContent() != null) {
            ItemStack stack = ((Ingredient)ItemRecipeCapability.CAP.of(content.getContent())).kjs$getFirst();
            return stack != null ? stack : ItemStack.f_41583_;
         } else {
            return ItemStack.f_41583_;
         }
      };
   }
}
