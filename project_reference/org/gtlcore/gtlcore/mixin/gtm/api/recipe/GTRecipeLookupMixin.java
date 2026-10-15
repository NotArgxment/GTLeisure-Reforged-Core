package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamWorkableMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.Branch;
import com.gregtechceu.gtceu.api.recipe.lookup.GTRecipeLookup;
import com.gregtechceu.gtceu.api.recipe.lookup.RecipeIterator;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.ResearchStationMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMaps;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.capability.IMERecipeHandler;
import org.gtlcore.gtlcore.api.machine.multiblock.CoilWorkableElectricMultipleRecipesMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPatternRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.RecipeHandlePart;
import org.gtlcore.gtlcore.api.recipe.IAdditionalRecipeIterator;
import org.gtlcore.gtlcore.api.recipe.IRecipeIterator;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.WorkableElectricMultipleRecipesMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GTRecipeLookup.class)
public abstract class GTRecipeLookupMixin {
   @Shadow(remap = false)
   @Final
   private GTRecipeType recipeType;
   @Unique
   private IRecipeCapabilityHolder gtlCore$machine;
   @Shadow(remap = false)
   private static final WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> ingredientRoot = new WeakHashMap<>();
   @Unique
   private static final Comparator<AbstractMapIngredient> SPECIAL_INGREDIENT_COMPARATOR = Comparator.comparing(i -> !i.isSpecialIngredient());
   @Shadow(remap = false)
   @Final
   private Branch lookup;

   @Overwrite(remap = false)
   @Nullable
   protected List<List<AbstractMapIngredient>> prepareRecipeFind(@NotNull IRecipeCapabilityHolder holder) {
      this.gtlCore$machine = holder;
      if (!(holder instanceof PrimitiveWorkableMachine)
         && !(holder instanceof SteamWorkableMachine)
         && !(holder instanceof WorkableTieredMachine)
         && !(holder instanceof ResearchStationMachine)) {
         if (holder instanceof IRecipeCapabilityMachine machine) {
            if (machine.emptyRecipeHandlePart()) {
               return null;
            }

            List<List<AbstractMapIngredient>> list = new ObjectArrayList();
            list.addAll(this.gtlCore$fromHolder(machine));
            return list.isEmpty() ? null : list;
         } else {
            return List.of();
         }
      } else {
         int totalSize = 0;

         for (Entry<RecipeCapability<?>, List<IRecipeHandler<?>>> entries : holder.getCapabilitiesProxy().row(IO.IN).entrySet()) {
            int size = 0;
            if (entries.getKey().isRecipeSearchFilter()) {
               for (IRecipeHandler<?> entry : entries.getValue()) {
                  if (entry.getSize() != -1) {
                     size += entry.getSize();
                  }
               }

               if (size == 2147483647) {
                  return null;
               }

               totalSize += size;
            }
         }

         if (totalSize == 0) {
            return null;
         }

         List<List<AbstractMapIngredient>> list = new ObjectArrayList(totalSize);
         list.addAll(this.fromHolder(holder));
         return list.isEmpty() ? null : list;
      }
   }

   @Unique
   @NotNull
   protected List<List<AbstractMapIngredient>> gtlCore$fromHolder(@NotNull IRecipeCapabilityMachine r) {
      if (r.emptyRecipeHandlePart()) {
         return Collections.emptyList();
      }

      List<RecipeHandlePart> recipeHandleParts = r.getNormalRecipeHandlePart(IO.IN);
      List<MEPatternRecipeHandlePart> mePatternRecipeHandleParts = r.getMEPatternRecipeHandleParts();
      List<List<AbstractMapIngredient>> list = new ObjectArrayList(recipeHandleParts.size() + mePatternRecipeHandleParts.size());
      if (!mePatternRecipeHandleParts.isEmpty()) {
         for (MEPatternRecipeHandlePart part : mePatternRecipeHandleParts) {
            Int2ReferenceArrayMap<Reference2ReferenceMap<RecipeCapability<?>, List<Object>>> finalMap = new Int2ReferenceArrayMap();

            for (IMERecipeHandler<?, ?> meHandler : part.getMERecipeHandlers()) {
               ObjectIterator handler = Int2ObjectMaps.fastIterable(meHandler.getActiveAndUnCachedSlotsLimitContentsMap()).iterator();

               while (handler.hasNext()) {
                  it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<? extends List<?>> e = (it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<? extends List<?>>)handler.next();
                  int slot = e.getIntKey();
                  List<?> contents = (List<?>)e.getValue();
                  ((List)((Reference2ReferenceMap)finalMap.computeIfAbsent(slot, k -> new Reference2ReferenceArrayMap(2)))
                        .computeIfAbsent(meHandler.getCapability(), ignore -> new ObjectArrayList()))
                     .addAll(contents);
               }
            }

            ObjectIterator var24 = Int2ReferenceMaps.fastIterable(finalMap).iterator();

            while (var24.hasNext()) {
               it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<Reference2ReferenceMap<RecipeCapability<?>, List<Object>>> slotEntry = (it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<Reference2ReferenceMap<RecipeCapability<?>, List<Object>>>)var24.next();
               List<AbstractMapIngredient> ingredient = new ObjectArrayList();
               ObjectIterator var39 = Reference2ReferenceMaps.fastIterable((Reference2ReferenceMap)slotEntry.getValue()).iterator();

               while (var39.hasNext()) {
                  it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.Entry<RecipeCapability<?>, List<Object>> capEntry = (it.unimi.dsi.fastutil.objects.Reference2ReferenceMap.Entry<RecipeCapability<?>, List<Object>>)var39.next();
                  RecipeCapability<?> cap = (RecipeCapability<?>)capEntry.getKey();

                  for (Object content : cap.compressIngredients((Collection)capEntry.getValue())) {
                     ingredient.addAll(cap.convertToMapIngredient(content));
                  }
               }

               if (!ingredient.isEmpty()) {
                  ingredient.sort(SPECIAL_INGREDIENT_COMPARATOR);
                  list.add(ingredient);
               }
            }
         }
      }

      if (r.isDistinct()) {
         for (RecipeHandlePart part : recipeHandleParts) {
            List<AbstractMapIngredient> ingredients = new ObjectArrayList();
            ObjectIterator var25 = part.getHandlerFastIterable().iterator();

            while (var25.hasNext()) {
               it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<IRecipeHandler<?>>> entry = (it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<IRecipeHandler<?>>>)var25.next();
               RecipeCapability<?> cap = (RecipeCapability<?>)entry.getKey();

               for (IRecipeHandler<?> handler : (List)entry.getValue()) {
                  for (Object content : cap.compressIngredients(handler.getContents())) {
                     List<AbstractMapIngredient> converted = cap.convertToMapIngredient(content);
                     converted.sort(SPECIAL_INGREDIENT_COMPARATOR);
                     ingredients.addAll(converted);
                  }
               }
            }

            list.add(ingredients);
         }
      } else {
         RecipeHandlePart shared = r.getSharedRecipeHandlePart();
         List<AbstractMapIngredient> fluidIngredients = new ObjectArrayList();
         if (shared != null) {
            for (IRecipeHandler<FluidIngredient> fluidHandler : shared.getCapability(FluidRecipeCapability.CAP)) {
               for (Object content : FluidRecipeCapability.CAP.compressIngredients(fluidHandler.getContents())) {
                  fluidIngredients.addAll(FluidRecipeCapability.CAP.convertToMapIngredient(content));
               }
            }

            List<AbstractMapIngredient> itemIngredients = new ObjectArrayList();

            for (IRecipeHandler<Ingredient> itemHandler : shared.getCapability(ItemRecipeCapability.CAP)) {
               for (Object content : ItemRecipeCapability.CAP.compressIngredients(itemHandler.getContents())) {
                  itemIngredients.addAll(ItemRecipeCapability.CAP.convertToMapIngredient(content));
               }
            }

            itemIngredients.addAll(fluidIngredients);
            list.add(itemIngredients);
         }

         for (RecipeHandlePart otherPart : recipeHandleParts) {
            List<AbstractMapIngredient> itemIngredients = new ObjectArrayList();

            for (IRecipeHandler<Ingredient> itemHandler : otherPart.getCapability(ItemRecipeCapability.CAP)) {
               for (Object content : ItemRecipeCapability.CAP.compressIngredients(itemHandler.getContents())) {
                  itemIngredients.addAll(ItemRecipeCapability.CAP.convertToMapIngredient(content));
               }
            }

            itemIngredients.addAll(fluidIngredients);
            list.add(itemIngredients);
         }
      }

      return list;
   }

   @Overwrite(remap = false)
   @Nullable
   public GTRecipe recurseIngredientTreeFindRecipe(
      @NotNull List<List<AbstractMapIngredient>> ingredients,
      @NotNull Branch branchMap,
      @NotNull Predicate<GTRecipe> canHandle,
      int index,
      int count,
      long skip
   ) {
      if (count == ingredients.size()) {
         return null;
      }

      if (!(this.gtlCore$machine instanceof PrimitiveWorkableMachine)
         && !(this.gtlCore$machine instanceof SteamWorkableMachine)
         && !(this.gtlCore$machine instanceof WorkableTieredMachine)
         && !(this.gtlCore$machine instanceof ResearchStationMachine)) {
         if (this.gtlCore$machine instanceof IRecipeCapabilityMachine) {
            ObjectArrayList<AbstractMapIngredient> ingredient = new ObjectArrayList(ingredients.get(index));
            return IRecipeIterator.diveIngredientTreeFindRecipe(ingredient, branchMap, canHandle);
         }
      } else {
         for (AbstractMapIngredient obj : ingredients.get(index)) {
            Map<AbstractMapIngredient, Either<GTRecipe, Branch>> targetMap = determineRootNodes(obj, branchMap);
            Either<GTRecipe, Branch> result = targetMap.get(obj);
            if (result != null) {
               GTRecipe r = (GTRecipe)result.map(
                  potentialRecipe -> canHandle.test(potentialRecipe) ? potentialRecipe : null,
                  potentialBranch -> this.diveIngredientTreeFindRecipe(ingredients, potentialBranch, canHandle, index, count, skip)
               );
               if (r != null) {
                  return r;
               }
            }
         }
      }

      return null;
   }

   @Overwrite(remap = false)
   @Nullable
   private GTRecipe diveIngredientTreeFindRecipe(
      @NotNull List<List<AbstractMapIngredient>> ingredients,
      @NotNull Branch map,
      @NotNull Predicate<GTRecipe> canHandle,
      int currentIndex,
      int count,
      long skip
   ) {
      for (int i = (currentIndex + 1) % ingredients.size(); i != currentIndex; i = (i + 1) % ingredients.size()) {
         if ((skip & 1L << i) == 0L) {
            GTRecipe found = this.recurseIngredientTreeFindRecipe(ingredients, map, canHandle, i, count + 1, skip | 1L << i);
            if (found != null) {
               return found;
            }
         }
      }

      return null;
   }

   @Overwrite(remap = false)
   @NotNull
   protected List<List<AbstractMapIngredient>> fromRecipe(@NotNull GTRecipe r) {
      List<List<AbstractMapIngredient>> list = new ObjectArrayList(r.inputs.size());
      r.inputs.forEach((cap, contents) -> {
         if (cap.isRecipeSearchFilter() && !contents.isEmpty()) {
            ArrayList<Object> ingredients = new ArrayList<>();

            for (Content content : contents) {
               ingredients.add(content.getContent());
            }

            for (Object ingredient : cap.compressIngredients(ingredients)) {
               retrieveCachedIngredient(list, cap.convertToMapIngredient(ingredient), ingredientRoot);
            }
         }
      });
      return list;
   }

   @Shadow(remap = false)
   @NotNull
   protected abstract List<List<AbstractMapIngredient>> fromHolder(@NotNull IRecipeCapabilityHolder var1);

   @Shadow(remap = false)
   protected static void retrieveCachedIngredient(
      @NotNull List<List<AbstractMapIngredient>> list,
      @NotNull List<AbstractMapIngredient> ingredients,
      @NotNull WeakHashMap<AbstractMapIngredient, WeakReference<AbstractMapIngredient>> cache
   ) {
   }

   @Shadow(remap = false)
   @NotNull
   protected static Map<AbstractMapIngredient, Either<GTRecipe, Branch>> determineRootNodes(
      @NotNull AbstractMapIngredient ingredient, @NotNull Branch branchMap
   ) {
      throw new RuntimeException();
   }

   @Shadow(remap = false)
   private GTRecipe recurseIngredientTreeFindRecipe(
      @NotNull List<List<AbstractMapIngredient>> ingredients, @NotNull Branch branchRoot, @NotNull Predicate<GTRecipe> canHandle
   ) {
      throw new RuntimeException();
   }

   @Overwrite(remap = false)
   @Nullable
   public GTRecipe find(@NotNull IRecipeCapabilityHolder holder, @NotNull Predicate<GTRecipe> canHandle) {
      if (holder instanceof IRecipeCapabilityMachine rlm) {
         for (MEPatternRecipeHandlePart part : rlm.getMEPatternRecipeHandleParts()) {
            ObjectSet<GTRecipe> cachedMERecipes = part.getCachedGTRecipe();
            if (!cachedMERecipes.isEmpty()) {
               return (GTRecipe)cachedMERecipes.iterator().next();
            }
         }
      }

      List<List<AbstractMapIngredient>> list = this.prepareRecipeFind(holder);
      return list == null ? null : this.recurseIngredientTreeFindRecipe(list, this.lookup, canHandle);
   }

   @Overwrite(remap = false)
   @NotNull
   public RecipeIterator getRecipeIterator(@NotNull IRecipeCapabilityHolder holder, @NotNull Predicate<GTRecipe> canHandle) {
      List<List<AbstractMapIngredient>> list = this.prepareRecipeFind(holder);
      RecipeIterator iterator = RecipeIteratorAccessor.newRecipeIterator(this.recipeType, list, canHandle);
      if (holder instanceof IRecipeCapabilityMachine rlm) {
         List<MEPatternRecipeHandlePart> parts = rlm.getMEPatternRecipeHandleParts();
         if (!parts.isEmpty()) {
            List<GTRecipe> meRecipes = new ObjectArrayList();

            for (MEPatternRecipeHandlePart part : parts) {
               meRecipes.addAll(part.getCachedGTRecipe());
            }

            meRecipes = meRecipes.stream().filter(r -> r.recipeType == this.recipeType).toList();
            if (!meRecipes.isEmpty()) {
               ((IAdditionalRecipeIterator)iterator).setAdditionalRecipes(meRecipes);
            }
         }

         if (holder instanceof WorkableElectricMultipleRecipesMachine || holder instanceof CoilWorkableElectricMultipleRecipesMachine) {
            ((IAdditionalRecipeIterator)iterator).setUseDiveIngredientTreeFind(true);
         }
      }

      return iterator;
   }
}
