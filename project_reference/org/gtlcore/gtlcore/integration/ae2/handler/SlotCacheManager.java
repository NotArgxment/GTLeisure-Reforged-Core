package org.gtlcore.gtlcore.integration.ae2.handler;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.ingredient.CacheHashStrategies;

public class SlotCacheManager implements ITagSerializable<CompoundTag> {
   private final Object2ObjectMap<Ingredient, AEItemKey> itemBestMatchCache = new Object2ObjectOpenCustomHashMap(
      CacheHashStrategies.IngredientHashStrategy.INSTANCE
   );
   private final Object2ObjectMap<FluidIngredient, AEFluidKey> fluidBestMatchCache = new Object2ObjectOpenCustomHashMap(
      CacheHashStrategies.FluidIngredientHashStrategy.INSTANCE
   );
   private int circuitCache = -1;
   private ItemStack circuitStack = ItemStack.f_41583_;

   public void setCircuitCache(int circuitCache) {
      this.circuitCache = circuitCache;
      this.circuitStack = IntCircuitBehaviour.stack(circuitCache);
   }

   public AEItemKey getBestItemMatch(Ingredient ingredient, Object2LongMap<AEItemKey> inventory, long needAmount) {
      AEItemKey cached = (AEItemKey)this.itemBestMatchCache.get(ingredient);
      if (cached != null && inventory.getLong(cached) >= needAmount) {
         return cached;
      }

      AEItemKey bestMatch = findBestItemMatch(ingredient, inventory, needAmount);
      if (bestMatch != null) {
         this.itemBestMatchCache.put(ingredient, bestMatch);
      }

      return bestMatch;
   }

   public AEFluidKey getBestFluidMatch(FluidIngredient ingredient, Object2LongMap<AEFluidKey> inventory, long needAmount) {
      AEFluidKey cached = (AEFluidKey)this.fluidBestMatchCache.get(ingredient);
      if (cached != null && inventory.getLong(cached) >= needAmount) {
         return cached;
      }

      AEFluidKey bestMatch = findBestFluidMatch(ingredient, inventory, needAmount);
      if (bestMatch != null) {
         this.fluidBestMatchCache.put(ingredient, bestMatch);
      }

      return bestMatch;
   }

   private static AEItemKey findBestItemMatch(Ingredient ingredient, Object2LongMap<AEItemKey> inventory, long needAmount) {
      ItemStack[] items = ingredient.m_43908_();

      for (ItemStack item : items) {
         if (!item.m_41619_()) {
            AEItemKey aeKey = AEItemKey.of(item);
            if (inventory.getLong(aeKey) >= needAmount) {
               return aeKey;
            }
         }
      }

      return null;
   }

   private static AEFluidKey findBestFluidMatch(FluidIngredient ingredient, Object2LongMap<AEFluidKey> inventory, long needAmount) {
      FluidStack[] stacks = ingredient.getStacks();

      for (FluidStack stack : stacks) {
         if (!stack.isEmpty()) {
            AEFluidKey aeKey = AEFluidKey.of(stack.getFluid());
            if (inventory.getLong(aeKey) >= needAmount) {
               return aeKey;
            }
         }
      }

      return null;
   }

   public AEItemKey getBestItemMatchSimulate(
      Ingredient ingredient, Object2LongMap<AEItemKey> inventory, Object2LongMap<AEItemKey> catalystInventory, long needAmount
   ) {
      AEItemKey cached = (AEItemKey)this.itemBestMatchCache.get(ingredient);
      if (cached == null || inventory.getLong(cached) < needAmount && catalystInventory.getLong(cached) < needAmount) {
         AEItemKey bestMatch = findBestItemMatchSimulate(ingredient, inventory, catalystInventory, needAmount);
         if (bestMatch != null) {
            this.itemBestMatchCache.put(ingredient, bestMatch);
         }

         return bestMatch;
      } else {
         return cached;
      }
   }

   public AEFluidKey getBestFluidMatchSimulate(
      FluidIngredient ingredient, Object2LongMap<AEFluidKey> inventory, Object2LongMap<AEFluidKey> catalystInventory, long needAmount
   ) {
      AEFluidKey cached = (AEFluidKey)this.fluidBestMatchCache.get(ingredient);
      if (cached == null || inventory.getLong(cached) < needAmount && catalystInventory.getLong(cached) < needAmount) {
         AEFluidKey bestMatch = findBestFluidMatchSimulate(ingredient, inventory, catalystInventory, needAmount);
         if (bestMatch != null) {
            this.fluidBestMatchCache.put(ingredient, bestMatch);
         }

         return bestMatch;
      } else {
         return cached;
      }
   }

   private static AEItemKey findBestItemMatchSimulate(
      Ingredient ingredient, Object2LongMap<AEItemKey> inventory, Object2LongMap<AEItemKey> catalystInventory, long needAmount
   ) {
      ItemStack[] items = ingredient.m_43908_();

      for (ItemStack item : items) {
         if (!item.m_41619_()) {
            AEItemKey aeKey = AEItemKey.of(item);
            if (inventory.getLong(aeKey) >= needAmount || catalystInventory.getLong(aeKey) >= needAmount) {
               return aeKey;
            }
         }
      }

      return null;
   }

   private static AEFluidKey findBestFluidMatchSimulate(
      FluidIngredient ingredient, Object2LongMap<AEFluidKey> inventory, Object2LongMap<AEFluidKey> catalystInventory, long needAmount
   ) {
      FluidStack[] stacks = ingredient.getStacks();

      for (FluidStack stack : stacks) {
         if (!stack.isEmpty()) {
            AEFluidKey aeKey = AEFluidKey.of(stack.getFluid());
            if (inventory.getLong(aeKey) >= needAmount || catalystInventory.getLong(aeKey) >= needAmount) {
               return aeKey;
            }
         }
      }

      return null;
   }

   public void clearAllCaches() {
      this.itemBestMatchCache.clear();
      this.fluidBestMatchCache.clear();
      this.circuitCache = -1;
      this.circuitStack = ItemStack.f_41583_;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      if (!this.itemBestMatchCache.isEmpty()) {
         ListTag itemCacheList = new ListTag();
         ObjectIterator var3 = Object2ObjectMaps.fastIterable(this.itemBestMatchCache).iterator();

         while (var3.hasNext()) {
            Entry<Ingredient, AEItemKey> entry = (Entry<Ingredient, AEItemKey>)var3.next();
            CompoundTag entryTag = new CompoundTag();
            entryTag.m_128365_("ingredient", serializeIngredient((Ingredient)entry.getKey()));
            entryTag.m_128365_("aekey", ((AEItemKey)entry.getValue()).toTag());
            itemCacheList.add(entryTag);
         }

         tag.m_128365_("itemCache", itemCacheList);
      }

      if (!this.fluidBestMatchCache.isEmpty()) {
         ListTag fluidCacheList = new ListTag();
         ObjectIterator var7 = Object2ObjectMaps.fastIterable(this.fluidBestMatchCache).iterator();

         while (var7.hasNext()) {
            Entry<FluidIngredient, AEFluidKey> entry = (Entry<FluidIngredient, AEFluidKey>)var7.next();
            CompoundTag entryTag = new CompoundTag();
            entryTag.m_128365_("ingredient", serializeFluidIngredient((FluidIngredient)entry.getKey()));
            entryTag.m_128365_("aekey", ((AEFluidKey)entry.getValue()).toTag());
            fluidCacheList.add(entryTag);
         }

         tag.m_128365_("fluidCache", fluidCacheList);
      }

      return tag;
   }

   public void deserializeNBT(CompoundTag tag) {
      this.clearAllCaches();
      if (tag.m_128425_("itemCache", 9)) {
         for (Tag t : tag.m_128437_("itemCache", 10)) {
            if (t instanceof CompoundTag entryTag) {
               Ingredient ingredient = deserializeIngredient(entryTag.m_128469_("ingredient"));
               AEItemKey aeKey = AEItemKey.fromTag(entryTag.m_128469_("aekey"));
               if (ingredient != null && aeKey != null) {
                  this.itemBestMatchCache.put(ingredient, aeKey);
               }
            }
         }
      }

      if (tag.m_128425_("fluidCache", 9)) {
         for (Tag t : tag.m_128437_("fluidCache", 10)) {
            if (t instanceof CompoundTag entryTag) {
               FluidIngredient ingredient = deserializeFluidIngredient(entryTag.m_128469_("ingredient"));
               AEFluidKey aeKey = AEFluidKey.fromTag(entryTag.m_128469_("aekey"));
               if (ingredient != null && aeKey != null) {
                  this.fluidBestMatchCache.put(ingredient, aeKey);
               }
            }
         }
      }
   }

   private static CompoundTag serializeIngredient(Ingredient ingredient) {
      CompoundTag tag = new CompoundTag();
      JsonElement json = ingredient.m_43942_();
      tag.m_128359_("json", json.toString());
      return tag;
   }

   private static CompoundTag serializeFluidIngredient(FluidIngredient ingredient) {
      CompoundTag tag = new CompoundTag();
      JsonElement json = ingredient.toJson();
      tag.m_128359_("json", json.toString());
      return tag;
   }

   private static Ingredient deserializeIngredient(CompoundTag tag) {
      try {
         String jsonStr = tag.m_128461_("json");
         if (jsonStr.isEmpty()) {
            return null;
         }

         JsonElement json = JsonParser.parseString(jsonStr);
         return Ingredient.m_288218_(json, false);
      } catch (Exception e) {
         return null;
      }
   }

   private static FluidIngredient deserializeFluidIngredient(CompoundTag tag) {
      try {
         String jsonStr = tag.m_128461_("json");
         if (jsonStr.isEmpty()) {
            return null;
         }

         JsonElement json = JsonParser.parseString(jsonStr);
         return FluidIngredient.fromJson(json);
      } catch (Exception e) {
         return null;
      }
   }

   public int getCircuitCache() {
      return this.circuitCache;
   }

   public ItemStack getCircuitStack() {
      return this.circuitStack;
   }
}
