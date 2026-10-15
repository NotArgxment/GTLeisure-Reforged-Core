package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAdditionalRecipeIterator {
   void setAdditionalRecipes(@NotNull List<GTRecipe> var1);

   @Nullable
   List<GTRecipe> getAdditionalRecipes();

   void setUseDiveIngredientTreeFind(boolean var1);
}
