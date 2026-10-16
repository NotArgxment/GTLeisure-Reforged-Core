package org.gtlreforged.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraftforge.fluids.FluidStack;

public class MEPatternRecipeHandlePart extends MEIORecipeHandlePart<IMEPatternTrait> implements IRecipeHandlePart {

   private final GTRecipe2IntBiMultiMap recipes2SlotsMap = new GTRecipe2IntBiMultiMap();

   public MEPatternRecipeHandlePart(
           IMEPatternTrait meTrait,
           IMERecipeHandler<Ingredient, ItemStack> itemHandler,
           IMERecipeHandler<FluidIngredient, FluidStack> fluidHandler
   ) {
      super(meTrait, itemHandler, fluidHandler);
   }

   public static MEPatternRecipeHandlePart of(IMEPatternPartMachine machine) {
      IMEPatternTrait meTrait = machine.getMETrait();
      Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> handlerPair =
              machine.getMERecipeHandlerTraits();
      return new MEPatternRecipeHandlePart(
              meTrait,
              (IMERecipeHandler<Ingredient, ItemStack>) handlerPair.left(),
              (IMERecipeHandler<FluidIngredient, FluidStack>) handlerPair.right()
      );
   }

   public IMERecipeHandler<?, ?>[] getMERecipeHandlers() {
      return this.handlers;
   }

   public ObjectSet<GTRecipe> getCachedGTRecipe() {
      return this.meTrait.getCachedGTRecipe();
   }

   public void setLastRecipe2Slot(GTRecipe gTRecipe, int slot) {
      this.recipes2SlotsMap.put(gTRecipe, slot);
   }

   public void restoreMachineCache(BiConsumer<GTRecipe, IRecipeHandlePart> consumer) {

      this.recipes2SlotsMap.clear();

      for (Entry<ObjectSet<GTRecipe>> slotEntry : Int2ReferenceMaps.fastIterable(this.meTrait.getSlot2RecipesCache())) {
         int slot = slotEntry.getIntKey();
         for (GTRecipe cachedRecipe : slotEntry.getValue()) {
            this.recipes2SlotsMap.put(cachedRecipe, slot);
         }
      }

      this.meTrait.setOnPatternChange(this.recipes2SlotsMap::removeByValue);

      for (GTRecipe restoredRecipe : this.recipes2SlotsMap.keySet()) {
         consumer.accept(restoredRecipe, this);
      }
   }

   public <I extends Predicate<S>, S> Object2LongMap<S> getMEContent(RecipeCapability<I> cap, GTRecipe recipe) {
      return this.<I, S>getHandler(cap)
              .getStackMapFromFirstAvailableSlot(this.recipes2SlotsMap.getValues(recipe));
   }

   public int handleRecipe(GTRecipe recipe, Reference2ObjectMap<RecipeCapability<?>, List<Object>> contents, boolean simulate, boolean setSlotCache) {
      List<Object> itemContent = null;
      List<Object> fluidContent = null;

      for (Reference2ObjectMap.Entry<RecipeCapability<?>, List<Object>> contentEntry : Reference2ObjectMaps.fastIterable(contents)) {
         RecipeCapability<?> capability = contentEntry.getKey();
         if (capability == ItemRecipeCapability.CAP) {
            itemContent = contentEntry.getValue();
         } else {
            if (capability != FluidRecipeCapability.CAP) {
               throw new AssertionError("Invalid recipe capability entry");
            }
            fluidContent = contentEntry.getValue();
         }
      }

      boolean hasItems = itemContent != null;
      boolean hasFluids = fluidContent != null;

      if (hasItems) {
         this.itemHandler.initMEHandleContents(recipe, itemContent, simulate);
      }
      if (hasFluids) {
         this.fluidHandler.initMEHandleContents(recipe, fluidContent, simulate);
      }

      IntSet cachedSlots = this.recipes2SlotsMap.getValues(recipe);
      for (int cachedSlot : cachedSlots) {
         if ((!hasItems || this.itemHandler.meHandleRecipe(recipe, simulate, cachedSlot))
                 && (!hasFluids || this.fluidHandler.meHandleRecipe(recipe, simulate, cachedSlot))) {
            return cachedSlot;
         }
      }

      for (int activeSlot : this.itemHandler.getActiveSlots()) {
         if (!cachedSlots.contains(activeSlot)
                 && (!hasItems || this.itemHandler.meHandleRecipe(recipe, simulate, activeSlot))
                 && (!hasFluids || this.fluidHandler.meHandleRecipe(recipe, simulate, activeSlot))) {
            if (!this.meTrait.hasCacheInSlot(activeSlot) && setSlotCache) {
               this.meTrait.setSlotCacheRecipe(activeSlot, recipe);
            }
            return activeSlot;
         }
      }

      return -1;
   }
}