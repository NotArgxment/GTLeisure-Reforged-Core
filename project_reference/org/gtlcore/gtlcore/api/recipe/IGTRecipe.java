package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

public interface IGTRecipe {
   static IGTRecipe of(GTRecipe recipe) {
      return (IGTRecipe)recipe;
   }

   void setHasTick(boolean var1);

   int getEuTier();

   long getRealParallels();

   void setRealParallels(long var1);
}
