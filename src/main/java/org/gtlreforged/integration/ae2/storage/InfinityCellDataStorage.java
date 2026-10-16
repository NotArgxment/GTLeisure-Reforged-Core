package org.gtlreforged.integration.ae2.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class InfinityCellDataStorage {

   public static final InfinityCellDataStorage EMPTY = new InfinityCellDataStorage(false);
   public static final InfinityCellDataStorage FAST_EMPTY = new InfinityCellDataStorage(true);
   public ListTag stackKeys;
   public ListTag amounts;
   public double totalAmount;
   public boolean isFastCell;

   public InfinityCellDataStorage(boolean isFastCell) {
      this.stackKeys = new ListTag();
      this.amounts = new ListTag();
      this.totalAmount = 0.0;
      this.isFastCell = isFastCell;
   }

   public InfinityCellDataStorage(ListTag stackKeys, ListTag amounts, double totalAmount, boolean isFastCell) {
      this.stackKeys = stackKeys;
      this.amounts = amounts;
      this.totalAmount = totalAmount;
      this.isFastCell = isFastCell;
   }

   public CompoundTag toNbt() {
      CompoundTag nbt = new CompoundTag();
      nbt.putBoolean("isFastCell", this.isFastCell);
      nbt.put("keys", this.stackKeys);
      nbt.put("amounts", this.amounts);
      if (this.totalAmount != 0.0) {
         nbt.putDouble("totalAmount", this.totalAmount);
      }

      return nbt;
   }

   public static InfinityCellDataStorage fromNbt(CompoundTag nbt) {
      double totalAmount = 0.0;
      boolean isFastCell = nbt.getBoolean("isFastCell");
      ListTag stackKeys = nbt.getList("keys", 10);
      ListTag amounts = nbt.getList("amounts", isFastCell ? 12 : 8);
      if (nbt.contains("totalAmount")) {
         totalAmount = nbt.getDouble("totalAmount");
      }

      return new InfinityCellDataStorage(stackKeys, amounts, totalAmount, isFastCell);
   }
}
