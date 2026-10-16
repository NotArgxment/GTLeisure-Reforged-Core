package org.gtlreforged.api.machine.trait.MEPart;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

public interface IMEPatternTrait extends IMEFilterIOTrait {
   @NotNull
   ObjectSet<GTRecipe> getCachedGTRecipe();

   void setSlotCacheRecipe(int var1, GTRecipe var2);

   @NotNull
   Int2ReferenceMap<ObjectSet<GTRecipe>> getSlot2RecipesCache();

   void setOnPatternChange(IntConsumer var1);

   boolean hasCacheInSlot(int var1);
}
