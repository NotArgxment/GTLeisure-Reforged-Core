package org.gtlreforged.api.machine.trait;

import com.google.common.collect.Iterables;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystItemStackHandler;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RecipeHandlePart implements IRecipeHandlePart {
   public static final Comparator<RecipeHandlePart> COMPARATOR = (h1, h2) -> {
      int cmp = Long.compare(h1.getPriority(), h2.getPriority());
      if (cmp != 0) {
         return cmp;
      }

      boolean b1 = h1.getTotalContentAmount() > 0.0;
      boolean b2 = h2.getTotalContentAmount() > 0.0;
      return Boolean.compare(b1, b2);
   };
   private final Reference2ObjectMap<RecipeCapability<?>, List<IRecipeHandler<?>>> handlerMap = new Reference2ObjectOpenHashMap();
   private final List<IRecipeHandler<FluidIngredient>> sharedFluidHandlers = new ObjectArrayList();

   public RecipeHandlePart(Iterable<IRecipeHandler<?>> handlers) {
      for (IRecipeHandler<?> handler : handlers) {
         ((List)this.handlerMap.computeIfAbsent(handler.getCapability(), c -> new ObjectArrayList())).add(handler);
      }
   }

   public static RecipeHandlePart of(IO io, Iterable<IRecipeHandler<?>> handlers) {
      RecipeHandlePart rhl = new RecipeHandlePart(handlers);
      if (io == IO.OUT) {
         ObjectIterator var3 = rhl.getHandlerFastIterable().iterator();

         while (var3.hasNext()) {
            Entry<RecipeCapability<?>, List<IRecipeHandler<?>>> entry = (Entry<RecipeCapability<?>, List<IRecipeHandler<?>>>)var3.next();
            ((List)entry.getValue()).sort(IRecipeHandler.ENTRY_COMPARATOR);
         }
      }

      return rhl;
   }

   public void addSharedFluidHandlers(Iterable<IRecipeHandler<FluidIngredient>> handlers) {
      for (IRecipeHandler<FluidIngredient> handler : handlers) {
         if (handler.getCapability() == FluidRecipeCapability.CAP) {
            this.sharedFluidHandlers.add(handler);
         }
      }
   }

   public ObjectIterable<Entry<RecipeCapability<?>, List<IRecipeHandler<?>>>> getHandlerFastIterable() {
      return Reference2ObjectMaps.fastIterable(this.handlerMap);
   }

   public <T extends Predicate<S>, S> Object2LongMap<S> getSelfContent(RecipeCapability<T> cap, boolean confirmMEStock) {
      if (cap == ItemRecipeCapability.CAP) {
         return (Object2LongMap<S>)createItemMap(this.getCapability(cap), new Object2LongOpenHashMap(), confirmMEStock);
      } else {
         return (Object2LongMap<S>)(cap == FluidRecipeCapability.CAP
            ? createFluidMap(this.getCapability(cap), new Object2LongOpenHashMap(), confirmMEStock)
            : Object2LongMaps.EMPTY_MAP);
      }
   }

   public <T extends Predicate<S>, S> Object2LongMap<S> getContentWithShared(RecipeCapability<T> cap, boolean confirmMEStock) {
      if (cap == ItemRecipeCapability.CAP) {
         return (Object2LongMap<S>)createItemMap(this.getCapability(ItemRecipeCapability.CAP), new Object2LongOpenHashMap(), confirmMEStock);
      } else {
         return (Object2LongMap<S>)(cap == FluidRecipeCapability.CAP
            ? createFluidMap(
               Iterables.concat(this.getCapability(FluidRecipeCapability.CAP), this.sharedFluidHandlers), new Object2LongOpenHashMap(), confirmMEStock
            )
            : Object2LongMaps.EMPTY_MAP);
      }
   }

   @NotNull
   public <T> List<IRecipeHandler<T>> getCapability(RecipeCapability<T> cap) {
      return (List<IRecipeHandler<T>>)this.handlerMap.getOrDefault(cap, Collections.emptyList());
   }

   public Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleRecipe(
      IO io, GTRecipe recipe, Map<RecipeCapability<?>, List<Object>> contents, boolean simulate
   ) {
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> copy = new Reference2ObjectOpenHashMap(contents);
      if (!this.handlerMap.isEmpty()) {
         ObjectIterator<Entry<RecipeCapability<?>, List<Object>>> it = copy.reference2ObjectEntrySet().fastIterator();

         while (it.hasNext()) {
            Entry<RecipeCapability<?>, List<Object>> entry = (Entry<RecipeCapability<?>, List<Object>>)it.next();

            for (IRecipeHandler<?> handler : this.getCapability((RecipeCapability)entry.getKey())) {
               List<?> left = handler.handleRecipe(io, recipe, (List)entry.getValue(), null, simulate);
               if (left == null) {
                  it.remove();
                  break;
               }

               entry.setValue(new ArrayList(left));
            }
         }
      }

      return copy;
   }

   @Nullable
   public List<?> handleRecipe(IO io, GTRecipe recipe, RecipeCapability<?> cap, List<?> contents, boolean simulate) {
      for (IRecipeHandler<?> handler : this.getCapability(cap)) {
         contents = handler.handleRecipe(io, recipe, contents, null, simulate);
         if (contents == null) {
            return null;
         }
      }

      return contents;
   }

   private static <T> Object2LongOpenHashMap<ItemStack> createItemMap(
      Iterable<IRecipeHandler<T>> handlers, Object2LongOpenHashMap<ItemStack> itemContent, boolean confirmME
   ) {
      ObjectOpenCustomHashSet<ItemStack> meAddedKeys = confirmME ? new ObjectOpenCustomHashSet(ItemStackHashStrategy.comparingAllButCount()) : null;

      for (IRecipeHandler<T> handler : handlers) {
         if (!(handler instanceof CatalystItemStackHandler) && !(handler instanceof NotifiableCircuitItemStackHandler)) {
            if (handler instanceof IOptimizedMEList aeItemHandler) {
               Object2LongMap<ItemStack> map = aeItemHandler.getMEItemMap();
               if (map != null) {
                  if (confirmME && aeItemHandler.isStocking()) {
                     ObjectIterator var13 = Object2LongMaps.fastIterable(map).iterator();

                     while (var13.hasNext()) {
                        it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack>)var13.next();
                        ItemStack key = (ItemStack)entry.getKey();
                        if (meAddedKeys.add(key)) {
                           itemContent.addTo(key, entry.getLongValue());
                        }
                     }
                  } else {
                     ObjectIterator var12 = Object2LongMaps.fastIterable(map).iterator();

                     while (var12.hasNext()) {
                        it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack>)var12.next();
                        itemContent.addTo((ItemStack)entry.getKey(), entry.getLongValue());
                     }
                  }
               }
            } else {
               for (Object o : handler.getContents()) {
                  if (o instanceof ItemStack stack) {
                     itemContent.addTo(stack, stack.m_41613_());
                  }
               }
            }
         }
      }

      return itemContent;
   }

   private static <T> Object2LongOpenHashMap<FluidStack> createFluidMap(
      Iterable<IRecipeHandler<T>> handlers, Object2LongOpenHashMap<FluidStack> fluidContent, boolean confirmMEStock
   ) {
      ObjectOpenCustomHashSet<FluidStack> meAddedKeys = confirmMEStock ? new ObjectOpenCustomHashSet(FluidStackHashStrategy.comparingAllButAmount()) : null;

      for (IRecipeHandler<T> fluid : handlers) {
         if (!(fluid instanceof CatalystFluidStackHandler)) {
            if (fluid instanceof IOptimizedMEList aeFluidHandler) {
               List<FluidStack> list = aeFluidHandler.getMEFluidList();
               if (confirmMEStock && aeFluidHandler.isStocking()) {
                  for (FluidStack fluidStack : list) {
                     if (meAddedKeys.add(fluidStack)) {
                        fluidContent.addTo(fluidStack, fluidStack.getAmount());
                     }
                  }
               } else {
                  for (FluidStack fluidStack : list) {
                     fluidContent.addTo(fluidStack, fluidStack.getAmount());
                  }
               }
            } else {
               for (Object o : fluid.getContents()) {
                  if (o instanceof FluidStack stack) {
                     fluidContent.addTo(stack, stack.getAmount());
                  }
               }
            }
         }
      }

      return fluidContent;
   }

   private long getPriority() {
      long priority = 0L;
      ObjectIterator var3 = this.handlerMap.values().iterator();

      while (var3.hasNext()) {
         List<IRecipeHandler<?>> list = (List<IRecipeHandler<?>>)var3.next();

         for (IRecipeHandler<?> handler : list) {
            priority += handler.getPriority();
         }
      }

      return priority;
   }

   private double getTotalContentAmount() {
      double sum = 0.0;
      ObjectIterator var3 = this.handlerMap.values().iterator();

      while (var3.hasNext()) {
         List<IRecipeHandler<?>> list = (List<IRecipeHandler<?>>)var3.next();

         for (IRecipeHandler<?> handler : list) {
            sum += handler.getTotalContentAmount();
         }
      }

      return sum;
   }
}
