package org.gtlcore.gtlcore.integration.ae2.storage;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import appeng.core.AELog;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap.Entry;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.integration.ae2.InfinityCell;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.StorageManager;

public class InfinityCellInventory implements StorageCell {
   private final ISaveProvider container;
   private final AEKeyType keyType;
   private double storedItemCount;
   private Object2ObjectOpenHashMap<AEKey, BigInteger> storedMap;
   private final ItemStack stack;
   private boolean isPersisted = true;
   private final KeyCounter lists = new KeyCounter();

   public InfinityCellInventory(AEKeyType keyType, ItemStack stack, ISaveProvider saveProvider) {
      this.stack = stack;
      this.container = saveProvider;
      this.keyType = keyType;
      this.storedMap = null;
      this.initData();
   }

   private InfinityCellDataStorage getDiskStorage() {
      return this.getDiskUUID() != null ? this.getStorageInstance().getOrCreateDisk(this.getDiskUUID(), false) : InfinityCellDataStorage.EMPTY;
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
            BigInteger count = BigInteger.ZERO;
            ObjectIterator<Entry<AEKey, BigInteger>> it = this.storedMap.object2ObjectEntrySet().fastIterator();

            while (it.hasNext()) {
               Entry<AEKey, BigInteger> entry = (Entry<AEKey, BigInteger>)it.next();
               BigInteger a = (BigInteger)entry.getValue();
               if (a.compareTo(BigInteger.ZERO) > 0) {
                  count = count.add(a);
                  keys.add(((AEKey)entry.getKey()).toTagGeneric());
                  amount.add(StringTag.m_129297_(a.toString()));
               }
            }

            if (keys.isEmpty()) {
               this.getStorageInstance().updateDisk(this.getDiskUUID(), new InfinityCellDataStorage(false));
            } else {
               this.getStorageInstance().modifyDisk(this.getDiskUUID(), keys, amount, count.doubleValue(), false);
            }

            this.storedItemCount = count.doubleValue();
            this.stack.m_41784_().m_128347_("count", this.storedItemCount);
            this.isPersisted = true;
         }
      }
   }

   public Component getDescription() {
      return null;
   }

   public static InfinityCellInventory createInventory(ItemStack stack, ISaveProvider saveProvider) {
      Objects.requireNonNull(stack, "Cannot create cell inventory for null itemstack");
      return stack.m_41720_() instanceof InfinityCell cellType ? new InfinityCellInventory(cellType.getKeyType(), stack, saveProvider) : null;
   }

   public boolean hasDiskUUID() {
      return this.stack.m_41782_() && this.stack.m_41784_().m_128441_("diskuuid");
   }

   public static boolean hasDiskUUID(ItemStack disk) {
      return !(disk.m_41720_() instanceof InfinityCell) ? false : disk.m_41782_() && disk.m_41784_().m_128441_("diskuuid");
   }

   public UUID getDiskUUID() {
      return this.hasDiskUUID() ? this.stack.m_41784_().m_128342_("diskuuid") : null;
   }

   private boolean isStorageCell(AEItemKey key) {
      InfinityCell type = getStorageCell(key);
      return type != null;
   }

   private static InfinityCell getStorageCell(AEItemKey itemKey) {
      return itemKey.getItem() instanceof InfinityCell infinityCell ? infinityCell : null;
   }

   private static boolean isCellEmpty(InfinityCellInventory inv) {
      return inv != null ? inv.getAvailableStacks().isEmpty() : true;
   }

   protected Object2ObjectOpenHashMap<AEKey, BigInteger> getCellItems() {
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
            String amount = amounts.m_128778_(i);
            AEKey key = AEKey.fromTagGeneric(stackKeys.m_128728_(i));
            if (!amount.isEmpty() && key != null) {
               BigInteger count = new BigInteger(amount);
               this.storedMap.put(key, count);
               this.lists.add(key, NumberUtils.getLongValue(count));
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
      if (amount != 0L && this.keyType.contains(what)) {
         if (what instanceof AEItemKey itemKey && this.isStorageCell(itemKey)) {
            InfinityCellInventory meInventory = createInventory(itemKey.toStack(), null);
            if (!isCellEmpty(meInventory)) {
               return 0L;
            }
         }

         if (!this.hasDiskUUID()) {
            this.stack.m_41784_().m_128362_("diskuuid", UUID.randomUUID());
            this.getStorageInstance().getOrCreateDisk(this.getDiskUUID(), false);
            this.loadCellItems();
         }

         if (mode == Actionable.MODULATE) {
            BigInteger finalAmount = BigInteger.valueOf(amount);
            this.getCellItems().compute(what, (k, v) -> v == null ? finalAmount : v.add(finalAmount));
            this.lists.add(what, amount);
            this.saveChanges(amount);
         }

         return amount;
      } else {
         return 0L;
      }
   }

   public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
      BigInteger currentAmount = (BigInteger)this.getCellItems().get(what);
      if (currentAmount == null) {
         return 0L;
      }

      if (currentAmount.signum() > 0) {
         BigInteger extractAmount = BigInteger.valueOf(amount);
         if (currentAmount.compareTo(extractAmount) < 1) {
            if (mode == Actionable.MODULATE) {
               this.storedMap.remove(what);
               this.lists.remove(what);
               this.saveChanges(-amount);
            }

            return currentAmount.longValue();
         } else {
            if (mode == Actionable.MODULATE) {
               BigInteger sub = currentAmount.subtract(extractAmount);
               this.storedMap.put(what, sub);
               this.lists.remove(what, amount);
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
