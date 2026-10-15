package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.RecipeIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import org.gtlcore.gtlcore.api.recipe.IAdditionalRecipeIterator;
import org.gtlcore.gtlcore.api.recipe.IRecipeIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IAdditionalRecipeIterator.class, prefix = "gTLCore$"))
@Mixin(RecipeIterator.class)
public abstract class RecipeIteratorMixin {
   @Shadow(remap = false)
   int index;
   @Shadow(remap = false)
   List<List<AbstractMapIngredient>> ingredients;
   @Shadow(remap = false)
   @NotNull
   GTRecipeType recipeMap;
   @Shadow(remap = false)
   @NotNull
   Predicate<GTRecipe> canHandle;
   @Unique
   private List<GTRecipe> gtlCore$additionalRecipes = null;
   @Unique
   private Iterator<GTRecipe> gtlCore$presentIndexGTRecipesIterator = null;
   @Unique
   private int gtlCore$additionalIndex = 0;
   @Unique
   private boolean gtlCore$useOriginal = true;
   @Unique
   private boolean gtlCore$useDiveIngredientTreeFind = false;
   @Unique
   private GTRecipe gtlCore$singleRecipe = null;
   @Unique
   private GTRecipe gtlCore$cachedNextAdditional = null;

   @Unique
   public void gTLCore$setUseDiveIngredientTreeFind(boolean useDiveIngredientTreeFind) {
      this.gtlCore$useDiveIngredientTreeFind = useDiveIngredientTreeFind;
   }

   @Unique
   public void gTLCore$setAdditionalRecipes(@NotNull List<GTRecipe> additionalRecipes) {
      if (!additionalRecipes.isEmpty()) {
         this.gtlCore$additionalRecipes = additionalRecipes;
         this.gtlCore$additionalIndex = 0;
         this.gtlCore$useOriginal = true;
         this.gtlCore$cachedNextAdditional = null;
      }
   }

   @Unique
   @Nullable
   public List<GTRecipe> gTLCore$getAdditionalRecipes() {
      return this.gtlCore$additionalRecipes;
   }

   @Overwrite(remap = false)
   public boolean hasNext() {
      if (this.gtlCore$useOriginal) {
         if (!this.gtlCore$useDiveIngredientTreeFind && this.gtlCore$singleRecipe != null) {
            return true;
         }

         if (this.gtlCore$useDiveIngredientTreeFind
            && this.gtlCore$presentIndexGTRecipesIterator != null
            && this.gtlCore$presentIndexGTRecipesIterator.hasNext()) {
            return true;
         }

         if (this.gtlCore$findNextRecipeSet()) {
            return true;
         }

         this.gtlCore$useOriginal = false;
      }

      return this.gtlCore$hasValidAdditionalRecipe();
   }

   @Overwrite(remap = false)
   @NotNull
   public GTRecipe next() {
      if (!this.hasNext()) {
         throw new NoSuchElementException();
      }

      if (this.gtlCore$useOriginal) {
         if (!this.gtlCore$useDiveIngredientTreeFind) {
            GTRecipe recipe = this.gtlCore$singleRecipe;
            this.gtlCore$singleRecipe = null;
            return recipe;
         } else {
            return this.gtlCore$presentIndexGTRecipesIterator.next();
         }
      } else {
         return this.gtlCore$getNextValidAdditionalRecipe();
      }
   }

   @Overwrite(remap = false)
   public void reset() {
      this.index = 0;
      this.gtlCore$singleRecipe = null;
      this.gtlCore$useOriginal = true;
      this.gtlCore$presentIndexGTRecipesIterator = null;
      this.gtlCore$additionalIndex = 0;
      this.gtlCore$cachedNextAdditional = null;
   }

   @Unique
   private boolean gtlCore$findNextRecipeSet() {
      if (this.ingredients == null) {
         return false;
      }

      if (this.gtlCore$useDiveIngredientTreeFind) {
         ObjectOpenHashSet<GTRecipe> recipeSet = new ObjectOpenHashSet();

         while (this.index < this.ingredients.size()) {
            IRecipeIterator.diveIngredientTreeFindRecipeCollection(
               this.ingredients.get(this.index), this.recipeMap.getLookup().getLookup(), this.canHandle, recipeSet
            );
            this.index++;
            recipeSet.remove(null);
            if (!recipeSet.isEmpty()) {
               this.gtlCore$presentIndexGTRecipesIterator = recipeSet.iterator();
               return true;
            }
         }
      } else {
         while (this.index < this.ingredients.size()) {
            GTRecipe recipe = this.recipeMap
               .getLookup()
               .recurseIngredientTreeFindRecipe(this.ingredients, this.recipeMap.getLookup().getLookup(), this.canHandle, this.index, 0, 1L << this.index);
            this.index++;
            if (recipe != null) {
               this.gtlCore$singleRecipe = recipe;
               return true;
            }
         }
      }

      this.gtlCore$presentIndexGTRecipesIterator = null;
      return false;
   }

   @Unique
   private boolean gtlCore$hasValidAdditionalRecipe() {
      if (this.gtlCore$cachedNextAdditional != null) {
         return true;
      }

      if (this.gtlCore$additionalRecipes == null) {
         return false;
      }

      while (this.gtlCore$additionalIndex < this.gtlCore$additionalRecipes.size()) {
         GTRecipe recipe = this.gtlCore$additionalRecipes.get(this.gtlCore$additionalIndex);
         this.gtlCore$additionalIndex++;
         if (this.canHandle.test(recipe)) {
            this.gtlCore$cachedNextAdditional = recipe;
            return true;
         }
      }

      return false;
   }

   @Unique
   private GTRecipe gtlCore$getNextValidAdditionalRecipe() {
      GTRecipe recipe = this.gtlCore$cachedNextAdditional;
      this.gtlCore$cachedNextAdditional = null;
      if (recipe == null) {
         throw new NoSuchElementException();
      } else {
         return recipe;
      }
   }
}
