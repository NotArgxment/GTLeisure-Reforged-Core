package org.gtlcore.gtlcore.api.machine.trait.AECraft;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

public interface IMolecularAssemblerHandler {
   void handleRecipeOutput(GTRecipe var1);

   GTRecipe extractGTRecipe(long var1, int var3);
}
