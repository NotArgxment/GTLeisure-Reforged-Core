package org.gtlcore.gtlcore.integration.ae2.storage;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.core.AELog;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.integration.ae2.FastInfinityCell;
import org.gtlcore.gtlcore.utils.StorageManager;
import org.gtlcore.gtlcore.utils.datastructure.Int128;

public class FastInfinityCellInventory implements StorageCell {
   private final ISaveProvider container;
   private double storedItemCount;
   private Object2ObjectOpenHashMap<AEKey, Int128> storedMap;
   private final ItemStack stack;
   private boolean isPersisted = true;
   private final KeyCounter lists = new KeyCounter();
   private final Int128 tempInt128 = Int128.ZERO();
   private final Int128 sumInt128 = Int128.ZERO();

   public FastInfinityCellInventory(ItemStack stack, ISaveProvider saveProvider) {
      this.stack = stack;
      this.container = saveProvider;
      this.storedMap = null;
      this.initData();
   }

   private InfinityCellDataStorage getDiskStorage() {
      return this.getDiskUUID() != null ? this.getStorageInstance().getOrCreateDisk(this.getDiskUUID(), true) : InfinityCellDataStorage.FAST_EMPTY;
   }

   private void initData() {
      if (this.hasDiskUUID()) {
         this.storedItemCount = this.getDiskStorage().totalAmount;
      } else {
         this.storedItemCount = 0.0;
         this.getCellItems();
      }
   }

   public CellState getStatus() {
      return this.storedItemCount == 0.0 ? CellState.EMPTY : CellState.NOT_EMPTY;
   }

   public double getIdleDrain() {
      return 2.147483647E9;
   }

   public void persist() {
      if (!this.isPersisted) {
         if (this.storedItemCount == 0.0) {
            if (this.hasDiskUUID()) {
               this.getStorageInstance().removeDisk(this.getDiskUUID());
               if (this.stack.m_41783_() != null) {
                  this.stack.m_41783_().m_128473_("diskuuid");
                  this.stack.m_41783_().m_128473_("count");
               }

               this.initData();
            }
         } else {
            ListTag keys = new ListTag();
            ListTag amount = new ListTag();
            this.sumInt128.set(0L, 0L);
            ObjectIterator<Entry<AEKey, Int128>> it = this.storedMap.object2ObjectEntrySet().fastIterator();

            while (it.hasNext()) {
               Entry<AEKey, Int128> entry = (Entry<AEKey, Int128>)it.next();
               Int128 a = (Int128)entry.getValue();
               if (a.isPositive()) {
                  this.sumInt128.add(a);
                  keys.add(((AEKey)entry.getKey()).toTagGeneric());
                  amount.add(new LongArrayTag(new long[]{a.getHigh(), a.getLow()}));
               }
            }

            if (keys.isEmpty()) {
               this.getStorageInstance().updateDisk(this.getDiskUUID(), new InfinityCellDataStorage(true));
            } else {
               this.getStorageInstance().modifyDisk(this.getDiskUUID(), keys, amount, this.sumInt128.doubleValue(), true);
            }

            this.storedItemCount = this.sumInt128.doubleValue();
            this.stack.m_41784_().m_128347_("count", this.storedItemCount);
            this.isPersisted = true;
         }
      }
   }

   public Component getDescription() {
      return null;
   }

   public static FastInfinityCellInventory createInventory(ItemStack stack, ISaveProvider saveProvider) {
      Objects.requireNonNull(stack, "Cannot create cell inventory for null itemstack");
      return !(stack.m_41720_() instanceof FastInfinityCell) ? null : new FastInfinityCellInventory(stack, saveProvider);
   }

   public boolean hasDiskUUID() {
      return this.stack.m_41782_() && this.stack.m_41784_().m_128441_("diskuuid");
   }

   public static boolean hasDiskUUID(ItemStack disk) {
      return !(disk.m_41720_() instanceof FastInfinityCell) ? false : disk.m_41782_() && disk.m_41784_().m_128441_("diskuuid");
   }

   public UUID getDiskUUID() {
      return this.hasDiskUUID() ? this.stack.m_41784_().m_128342_("diskuuid") : null;
   }

   private boolean isStorageCell(AEItemKey key) {
      FastInfinityCell type = getStorageCell(key);
      return type != null;
   }

   private static FastInfinityCell getStorageCell(AEItemKey itemKey) {
      return itemKey.getItem() instanceof FastInfinityCell fastInfinityCell ? fastInfinityCell : null;
   }

   private static boolean isCellEmpty(FastInfinityCellInventory inv) {
      return inv != null ? inv.getAvailableStacks().isEmpty() : true;
   }

   protected Object2ObjectOpenHashMap<AEKey, Int128> getCellItems() {
      if (this.storedMap == null) {
         this.storedMap = new Object2ObjectOpenHashMap();
         this.loadCellItems();
      }

      return this.storedMap;
   }

   public void getAvailableStacks(KeyCounter out) {
      this.getCellItems();
      out.addAll(this.lists);
   }

   private void loadCellItems() {
      boolean corruptedTag = false;
      if (this.stack.m_41782_()) {
         ListTag amounts = this.getDiskStorage().amounts;
         ListTag stackKeys = this.getDiskStorage().stackKeys;
         if (amounts.size() != stackKeys.size()) {
            AELog.warn("Loading storage cell with mismatched amounts/tags: %d != %d", new Object[]{amounts.size(), stackKeys.size()});
         }

         for (int i = 0; i < amounts.size(); i++) {
            Tag amountTag = amounts.get(i);
            AEKey key = AEKey.fromTagGeneric(stackKeys.m_128728_(i));
            if (key != null && amountTag instanceof LongArrayTag longArrayTag) {
               long[] longArray = longArrayTag.m_128851_();
               Int128 count = new Int128(longArray[0], longArray[1]);
               this.storedMap.put(key, count);
               this.lists.add(key, count.longValue());
               this.storedItemCount = this.storedItemCount + count.doubleValue();
            } else {
               corruptedTag = true;
            }
         }

         if (corruptedTag) {
            this.saveChanges(0.0);
         }
      }
   }

   protected void saveChanges(double incur) {
      this.storedItemCount += incur;
      this.isPersisted = false;
      if (this.container != null) {
         this.container.saveChanges();
      } else {
         this.persist();
      }
   }

   private StorageManager getStorageInstance() {
      return GTLCore.STORAGE_INSTANCE;
   }

   public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
      if (amount != 0L && what != null) {
         if (what instanceof AEItemKey itemKey && this.isStorageCell(itemKey)) {
            FastInfinityCellInventory meInventory = createInventory(itemKey.toStack(), null);
            if (!isCellEmpty(meInventory)) {
               return 0L;
            }
         }

         if (!this.hasDiskUUID()) {
            this.stack.m_41784_().m_128362_("diskuuid", UUID.randomUUID());
            this.getStorageInstance().getOrCreateDisk(this.getDiskUUID(), true);
            this.loadCellItems();
         }

         if (mode == Actionable.MODULATE) {
            this.tempInt128.set(0L, amount);
            Int128 newValue = (Int128)this.getCellItems().compute(what, (k, v) -> v == null ? new Int128(0L, amount) : v.add(this.tempInt128));
            this.lists.set(what, newValue.longValue());
            this.saveChanges(amount);
         }

         return amount;
      } else {
         return 0L;
      }
   }

   public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
      Int128 currentAmount = (Int128)this.getCellItems().get(what);
      if (currentAmount == null) {
         return 0L;
      }

      if (currentAmount.isPositive()) {
         this.tempInt128.set(0L, amount);
         if (currentAmount.compareTo(this.tempInt128) <= 0) {
            if (mode == Actionable.MODULATE) {
               this.storedMap.remove(what);
               this.lists.remove(what);
               this.saveChanges(-currentAmount.longValue());
            }

            return currentAmount.longValue();
         } else {
            if (mode == Actionable.MODULATE) {
               currentAmount.subtract(this.tempInt128);
               this.lists.set(what, currentAmount.longValue());
               this.saveChanges(-amount);
            }

            return amount;
         }
      } else {
         return 0L;
      }
   }

   public double getNbtItemCount() {
      return this.hasDiskUUID() && this.stack.m_41783_() != null ? this.stack.m_41783_().m_128459_("count") : 0.0;
   }
}
