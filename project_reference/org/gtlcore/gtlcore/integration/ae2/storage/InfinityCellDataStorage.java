package org.gtlcore.gtlcore.integration.ae2.storage;

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
      nbt.m_128379_("isFastCell", this.isFastCell);
      nbt.m_128365_("keys", this.stackKeys);
      nbt.m_128365_("amounts", this.amounts);
      if (this.totalAmount != 0.0) {
         nbt.m_128347_("totalAmount", this.totalAmount);
      }

      return nbt;
   }

   public static InfinityCellDataStorage fromNbt(CompoundTag nbt) {
      double totalAmount = 0.0;
      boolean isFastCell = nbt.m_128471_("isFastCell");
      ListTag stackKeys = nbt.m_128437_("keys", 10);
      ListTag amounts = nbt.m_128437_("amounts", isFastCell ? 12 : 8);
      if (nbt.m_128441_("totalAmount")) {
         totalAmount = nbt.m_128459_("totalAmount");
      }

      return new InfinityCellDataStorage(stackKeys, amounts, totalAmount, isFastCell);
   }
}
