package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.gtlcore.gtlcore.utils.Registries;

public class GTRecipeManager {
   private List<GTRecipe> recipes = new ArrayList<>();

   public GTRecipeManager() {
      MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
      RecipeManager recipeManager = currentServer.m_129894_();
      recipeManager.m_44051_().forEach(recipe -> {
         if (recipe instanceof GTRecipe gtRecipe) {
            this.recipes.add(gtRecipe);
         }
      });
   }

   public void filterRecipesByType(GTRecipeType recipeType) {
      this.recipes = this.recipes.stream().filter(gtRecipe -> gtRecipe.getType().equals(recipeType)).toList();
   }

   public boolean isItemStackMatchByStringArray(String[] matchList, ItemStack itemStack) {
      boolean isMatch = false;

      for (String match : matchList) {
         if (Registries.getItemId(itemStack).contains(match)) {
            isMatch = true;
         }
      }

      return isMatch;
   }

   public boolean isFluidStackMatchByStringArray(String[] matchList, FluidStack fluidStack) {
      boolean isMatch = false;

      for (String match : matchList) {
         if (!match.isEmpty() && Registries.getFluidId(fluidStack).contains(match)) {
            isMatch = true;
         }
      }

      return isMatch;
   }

   public void filterRecipesByInputsIdArray(String[] matchList, boolean white) {
      this.recipes = this.recipes.stream().filter(gtRecipe -> {
         boolean isAnyoneMatch = false;

         for (ItemStack inputItem : RecipeHelper.getInputItems(gtRecipe)) {
            if (this.isItemStackMatchByStringArray(matchList, inputItem)) {
               isAnyoneMatch = true;
            }
         }

         for (FluidStack fluidStack : RecipeHelper.getInputFluids(gtRecipe)) {
            if (this.isFluidStackMatchByStringArray(matchList, fluidStack)) {
               isAnyoneMatch = true;
            }
         }

         return white ? isAnyoneMatch : !isAnyoneMatch;
      }).toList();
   }

   public void filterRecipesByOutputsIdArray(String[] matchList, boolean white) {
      this.recipes = this.recipes.stream().filter(gtRecipe -> {
         boolean isAnyoneMatch = false;

         for (ItemStack inputItem : RecipeHelper.getOutputItems(gtRecipe)) {
            if (this.isItemStackMatchByStringArray(matchList, inputItem)) {
               isAnyoneMatch = true;
            }
         }

         for (FluidStack fluidStack : RecipeHelper.getOutputFluids(gtRecipe)) {
            if (this.isFluidStackMatchByStringArray(matchList, fluidStack)) {
               isAnyoneMatch = true;
            }
         }

         return white ? isAnyoneMatch : !isAnyoneMatch;
      }).toList();
   }

   public void filterRecipesByCircuit(int circuit) {
      List<GTRecipe> filedRecipes = new ArrayList<>();

      for (GTRecipe recipe : this.recipes) {
         int circuitConfiguration = 0;

         for (Content inputContent : recipe.getInputContents(ItemRecipeCapability.CAP)) {
            ItemStack itemStack = ((Ingredient)ItemRecipeCapability.CAP.of(inputContent.getContent())).kjs$getFirst();
            if (itemStack.m_41720_().equals(GTItems.INTEGRATED_CIRCUIT.m_5456_())) {
               circuitConfiguration = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
            }
         }

         if (circuitConfiguration == circuit) {
            filedRecipes.add(recipe);
         }
      }

      this.recipes = filedRecipes;
   }

   public List<GTRecipe> getRecipes() {
      return this.recipes;
   }
}
