package org.gtlreforged.api.machine.trait;

import org.gtlreforged.api.recipe.RecipeResult;

public interface IRecipeStatus {

   default void setRecipeStatus(RecipeResult result) {
   }

   default RecipeResult getRecipeStatus() {
      return null;
   }

   default void setWorkingStatus(RecipeResult result) {
   }

   default RecipeResult getWorkingStatus() {
      return null;
   }
}
