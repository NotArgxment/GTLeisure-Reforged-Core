package org.gtlcore.gtlcore.api.capability;

import com.gregtechceu.gtceu.api.capability.recipe.IFilteredHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface IMERecipeHandler<T extends Predicate<S>, S> extends IFilteredHandler<T> {
   RecipeCapability<T> getCapability();

   Set<Integer> getActiveSlots();

   Int2ObjectMap<List<S>> getActiveAndUnCachedSlotsLimitContentsMap();

   Object2LongMap<S> getStackMapFromFirstAvailableSlot(IntCollection var1);

   default Object2LongMap<S> getSingleSlotStackMap(int slot) {
      return this.getStackMapFromFirstAvailableSlot(IntList.of(slot));
   }

   default T copyContent(Object content) {
      return (T)this.getCapability().copyInner((Predicate)content);
   }

   default boolean meHandleRecipe(GTRecipe recipe, boolean simulate, int trySlot) {
      return this.meHandleRecipeInner(recipe, this.getPreparedMEHandleContents(), simulate, trySlot);
   }

   boolean meHandleRecipeInner(GTRecipe var1, Object2LongMap<T> var2, boolean var3, int var4);

   List<T> meHandleRecipeOutputInner(List<T> var1, boolean var2);

   default boolean outputHasFilter() {
      return false;
   }

   default void initMEHandleContents(GTRecipe recipe, List<?> leftContents, boolean simulate) {
      if (!leftContents.isEmpty()) {
         List<T> typedContents = new ObjectArrayList(leftContents.size());

         for (Object leftObj : leftContents) {
            typedContents.add(this.copyContent(leftObj));
         }

         this.prepareMEHandleContents(recipe, typedContents, simulate);
      }
   }

   default List<T> meHandleRecipeOutput(List<?> leftContents, boolean simulate) {
      if (!leftContents.isEmpty() && (!simulate || this.outputHasFilter())) {
         List<T> typedContents = new ObjectArrayList(leftContents.size());

         for (Object leftObj : leftContents) {
            typedContents.add(this.copyContent(leftObj));
         }

         return this.meHandleRecipeOutputInner(typedContents, simulate);
      } else {
         return List.of();
      }
   }

   void prepareMEHandleContents(GTRecipe var1, List<T> var2, boolean var3);

   Object2LongMap<T> getPreparedMEHandleContents();
}
