package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.Branch;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRecipeIterator {
   @Nullable
   static GTRecipe diveIngredientTreeFindRecipe(
      @NotNull List<AbstractMapIngredient> ingredients, @NotNull Branch branchMap, @NotNull Predicate<GTRecipe> canHandle
   ) {
      if (ingredients.isEmpty()) {
         return null;
      }

      for (AbstractMapIngredient ingredient : ingredients) {
         Map<AbstractMapIngredient, Either<GTRecipe, Branch>> targetMap = determineRootNodes(ingredient, branchMap);
         Either<GTRecipe, Branch> result = targetMap.get(ingredient);
         if (result != null) {
            GTRecipe r = (GTRecipe)result.map(
               potentialRecipe -> canHandle.test(potentialRecipe) ? potentialRecipe : null,
               potentialBranch -> diveIngredientTreeFindRecipe(ingredients, potentialBranch, canHandle)
            );
            if (r != null) {
               return r;
            }
         }
      }

      return null;
   }

   static GTRecipe diveIngredientTreeFindRecipeCollection(
      @NotNull List<AbstractMapIngredient> ingredients, @NotNull Branch branchMap, @NotNull Predicate<GTRecipe> canHandle, Set<GTRecipe> recipeSet
   ) {
      if (ingredients.isEmpty()) {
         return null;
      }

      for (AbstractMapIngredient ingredient : ingredients) {
         Map<AbstractMapIngredient, Either<GTRecipe, Branch>> targetMap = determineRootNodes(ingredient, branchMap);
         Either<GTRecipe, Branch> result = targetMap.get(ingredient);
         if (result != null) {
            GTRecipe r = (GTRecipe)result.map(
               potentialRecipe -> canHandle.test(potentialRecipe) ? potentialRecipe : null,
               potentialBranch -> diveIngredientTreeFindRecipeCollection(ingredients, potentialBranch, canHandle, recipeSet)
            );
            if (r != null) {
               recipeSet.add(r);
            }
         }
      }

      return null;
   }

   @NotNull
   static Map<AbstractMapIngredient, Either<GTRecipe, Branch>> determineRootNodes(@NotNull AbstractMapIngredient ingredient, @NotNull Branch branchMap) {
      return ingredient.isSpecialIngredient() ? branchMap.getSpecialNodes() : branchMap.getNodes();
   }
}
