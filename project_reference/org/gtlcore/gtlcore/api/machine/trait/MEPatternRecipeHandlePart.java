package org.gtlcore.gtlcore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.capability.IMERecipeHandler;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternTrait;
import org.gtlcore.gtlcore.utils.datastructure.GTRecipe2IntBiMultiMap;
import org.jetbrains.annotations.NotNull;

public class MEPatternRecipeHandlePart extends MEIORecipeHandlePart<IMEPatternTrait> implements IRecipeHandlePart {
   private final GTRecipe2IntBiMultiMap recipes2SlotsMap = new GTRecipe2IntBiMultiMap();

   public MEPatternRecipeHandlePart(
      @NotNull IMEPatternTrait meTrait,
      @NotNull IMERecipeHandler<Ingredient, ItemStack> itemHandler,
      @NotNull IMERecipeHandler<FluidIngredient, FluidStack> fluidHandler
   ) {
      super(meTrait, itemHandler, fluidHandler);
   }

   public static MEPatternRecipeHandlePart of(IMEPatternPartMachine machine) {
      IMEPatternTrait meTrait = machine.getMETrait();
      Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> pair = machine.getMERecipeHandlerTraits();
      return new MEPatternRecipeHandlePart(
         meTrait, (IMERecipeHandler<Ingredient, ItemStack>)pair.left(), (IMERecipeHandler<FluidIngredient, FluidStack>)pair.right()
      );
   }

   public IMERecipeHandler<?, ?>[] getMERecipeHandlers() {
      return this.handlers;
   }

   @NotNull
   public ObjectSet<GTRecipe> getCachedGTRecipe() {
      return this.meTrait.getCachedGTRecipe();
   }

   public void setLastRecipe2Slot(@NotNull GTRecipe gTRecipe, int slot) {
      this.recipes2SlotsMap.put(gTRecipe, slot);
   }

   public void restoreMachineCache(BiConsumer<GTRecipe, IRecipeHandlePart> consumer) {
      this.recipes2SlotsMap.clear();
      ObjectIterator var2 = Int2ReferenceMaps.fastIterable(this.meTrait.getSlot2RecipesCache()).iterator();

      while (var2.hasNext()) {
         Entry<ObjectSet<GTRecipe>> entry = (Entry<ObjectSet<GTRecipe>>)var2.next();
         int slot = entry.getIntKey();
         ObjectIterator var5 = ((ObjectSet)entry.getValue()).iterator();

         while (var5.hasNext()) {
            GTRecipe recipe = (GTRecipe)var5.next();
            this.recipes2SlotsMap.put(recipe, slot);
         }
      }

      this.meTrait.setOnPatternChange(this.recipes2SlotsMap::removeByValue);
      var2 = this.recipes2SlotsMap.keySet().iterator();

      while (var2.hasNext()) {
         GTRecipe key = (GTRecipe)var2.next();
         consumer.accept(key, this);
      }
   }

   public <I extends Predicate<S>, S> Object2LongMap<S> getMEContent(RecipeCapability<I> cap, GTRecipe recipe) {
      return this.<I, S>getMECapability(cap).getStackMapFromFirstAvailableSlot(this.recipes2SlotsMap.getValues(recipe));
   }

   public int handleRecipe(GTRecipe recipe, Reference2ObjectMap<RecipeCapability<?>, List<Object>> contents, boolean simulate, boolean setSlotCache) {
      List<Object> itemContent = null;
      List<Object> fluidContent = null;
      ObjectIterator hasItem = Reference2ObjectMaps.fastIterable(contents).iterator();

      while (hasItem.hasNext()) {
         it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<Object>> entry = (it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<Object>>)hasItem.next();
         RecipeCapability<?> key = (RecipeCapability<?>)entry.getKey();
         if (key == ItemRecipeCapability.CAP) {
            itemContent = (List<Object>)entry.getValue();
         } else {
            if (key != FluidRecipeCapability.CAP) {
               throw new AssertionError("Invalid recipe capability entry");
            }

            fluidContent = (List<Object>)entry.getValue();
         }
      }

      boolean hasItemx = itemContent != null;
      boolean hasFluid = fluidContent != null;
      if (hasItemx) {
         this.itemHandler.initMEHandleContents(recipe, itemContent, simulate);
      }

      if (hasFluid) {
         this.fluidHandler.initMEHandleContents(recipe, fluidContent, simulate);
      }

      IntSet cachedSlots = this.recipes2SlotsMap.getValues(recipe);
      if (!cachedSlots.isEmpty()) {
         IntIterator var10 = cachedSlots.iterator();

         while (var10.hasNext()) {
            int slot = (Integer)var10.next();
            if ((!hasItemx || this.itemHandler.meHandleRecipe(recipe, simulate, slot))
               && (!hasFluid || this.fluidHandler.meHandleRecipe(recipe, simulate, slot))) {
               return slot;
            }
         }
      }

      for (int slot : this.itemHandler.getActiveSlots()) {
         if (!cachedSlots.contains(slot)
            && (!hasItemx || this.itemHandler.meHandleRecipe(recipe, simulate, slot))
            && (!hasFluid || this.fluidHandler.meHandleRecipe(recipe, simulate, slot))) {
            if (!this.meTrait.hasCacheInSlot(slot) && setSlotCache) {
               this.meTrait.setSlotCacheRecipe(slot, recipe);
            }

            return slot;
         }
      }

      return -1;
   }
}
