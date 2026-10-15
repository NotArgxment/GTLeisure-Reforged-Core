package org.gtlcore.gtlcore.integration.lowdragmc.misc;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MutableItemTransferList implements IItemTransfer, ITagSerializable<CompoundTag>, IContentChangeAware {
   private Runnable onContentsChanged = () -> {};
   private final ObjectArrayList<@NotNull IItemTransfer> transfers = new ObjectArrayList();
   private volatile int totalSlots = 0;
   private volatile boolean slotsCacheDirty = true;
   protected Predicate<ItemStack> filter = item -> true;

   public MutableItemTransferList() {
   }

   public MutableItemTransferList(int initialCapacity) {
      this.transfers.ensureCapacity(initialCapacity);
   }

   public MutableItemTransferList(Set<IItemTransfer> initialTransfers) {
      if (initialTransfers != null && !initialTransfers.isEmpty()) {
         this.transfers.addAll(initialTransfers);
         this.invalidateSlotsCache();
      }
   }

   public boolean addTransfer(@Nullable IItemTransfer transfer) {
      if (transfer == null) {
         return false;
      }

      boolean added = this.transfers.add(transfer);
      if (added) {
         this.invalidateSlotsCache();
      }

      return added;
   }

   public boolean addTransfers(@Nullable Set<IItemTransfer> transfers) {
      if (transfers == null) {
         return false;
      }

      boolean added = this.transfers.addAll(transfers);
      if (added) {
         this.invalidateSlotsCache();
      }

      return added;
   }

   public void addTransfer(int index, @NotNull IItemTransfer transfer) {
      this.transfers.add(index, transfer);
      this.invalidateSlotsCache();
   }

   public boolean removeTransfer(@Nullable IItemTransfer transfer) {
      boolean removed = this.transfers.remove(transfer);
      if (removed) {
         this.invalidateSlotsCache();
      }

      return removed;
   }

   public IItemTransfer removeTransfer(int index) {
      IItemTransfer removed = (IItemTransfer)this.transfers.remove(index);
      this.invalidateSlotsCache();
      return removed;
   }

   public boolean removeTransfersIf(@NotNull Predicate<IItemTransfer> filter) {
      boolean removed = this.transfers.removeIf(filter);
      if (removed) {
         this.invalidateSlotsCache();
      }

      return removed;
   }

   public void clear() {
      if (!this.transfers.isEmpty()) {
         this.transfers.clear();
         this.totalSlots = 0;
         this.slotsCacheDirty = false;
      }
   }

   public int getTransferCount() {
      return this.transfers.size();
   }

   public boolean isEmpty() {
      return this.transfers.isEmpty();
   }

   public int getSlots() {
      if (this.slotsCacheDirty) {
         this.updateSlotsCache();
      }

      return this.totalSlots;
   }

   @NotNull
   public ItemStack getStackInSlot(int slot) {
      if (this.transfers.isEmpty()) {
         return ItemStack.f_41583_;
      }

      MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
      return pair != null ? pair.transfer.getStackInSlot(pair.localSlot) : ItemStack.f_41583_;
   }

   public void setStackInSlot(int slot, @NotNull ItemStack stack) {
      if (!this.transfers.isEmpty()) {
         MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
         if (pair != null) {
            pair.transfer.setStackInSlot(pair.localSlot, stack);
         }
      }
   }

   @NotNull
   public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChanges) {
      if (!this.transfers.isEmpty() && !stack.m_41619_() && this.filter.test(stack)) {
         MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
         return pair != null ? pair.transfer.insertItem(pair.localSlot, stack, simulate, notifyChanges) : stack;
      } else {
         return stack;
      }
   }

   @NotNull
   public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
      if (!this.transfers.isEmpty() && amount > 0) {
         MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
         return pair != null ? pair.transfer.extractItem(pair.localSlot, amount, simulate, notifyChanges) : ItemStack.f_41583_;
      } else {
         return ItemStack.f_41583_;
      }
   }

   public int getSlotLimit(int slot) {
      if (this.transfers.isEmpty()) {
         return 0;
      }

      MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
      return pair != null ? pair.transfer.getSlotLimit(pair.localSlot) : 0;
   }

   public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      if (!this.transfers.isEmpty() && this.filter.test(stack)) {
         MutableItemTransferList.TransferSlotPair pair = this.findTransferForSlot(slot);
         return pair != null && pair.transfer.isItemValid(pair.localSlot, stack);
      } else {
         return false;
      }
   }

   public void onContentsChanged() {
      ObjectListIterator var1 = this.transfers.iterator();

      while (var1.hasNext()) {
         IItemTransfer transfer = (IItemTransfer)var1.next();
         transfer.onContentsChanged();
      }
   }

   @NotNull
   public Object createSnapshot() {
      return this.transfers.stream().map(IItemTransfer::createSnapshot).toArray(Object[]::new);
   }

   public void restoreFromSnapshot(Object snapshot) {
      if (snapshot instanceof Object[] array && array.length == this.transfers.size()) {
         for (int i = 0; i < array.length; i++) {
            IItemTransfer transfer = (IItemTransfer)this.transfers.get(i);
            if (array[i] != null) {
               transfer.restoreFromSnapshot(array[i]);
            }
         }
      }
   }

   public void setFilter(@NotNull Predicate<ItemStack> filter) {
      this.filter = filter;
   }

   private void updateSlotsCache() {
      int slots = 0;
      ObjectListIterator var2 = this.transfers.iterator();

      while (var2.hasNext()) {
         IItemTransfer transfer = (IItemTransfer)var2.next();
         slots += transfer.getSlots();
      }

      this.totalSlots = slots;
      this.slotsCacheDirty = false;
   }

   private void invalidateSlotsCache() {
      this.slotsCacheDirty = true;
   }

   @Nullable
   private MutableItemTransferList.TransferSlotPair findTransferForSlot(int globalSlot) {
      int index = 0;
      ObjectListIterator var3 = this.transfers.iterator();

      while (var3.hasNext()) {
         IItemTransfer transfer = (IItemTransfer)var3.next();
         if (globalSlot - index < transfer.getSlots()) {
            return new MutableItemTransferList.TransferSlotPair(transfer, globalSlot - index);
         }

         index += transfer.getSlots();
      }

      return null;
   }

   public static MutableItemTransferList empty() {
      return new MutableItemTransferList();
   }

   public static MutableItemTransferList of(@NotNull IItemTransfer transfer) {
      MutableItemTransferList list = new MutableItemTransferList(1);
      list.addTransfer(transfer);
      return list;
   }

   public static MutableItemTransferList of(@NotNull IItemTransfer... transfers) {
      MutableItemTransferList list = new MutableItemTransferList(transfers.length);

      for (IItemTransfer transfer : transfers) {
         list.addTransfer(transfer);
      }

      return list;
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      ListTag list = new ListTag();
      ObjectListIterator var3 = this.transfers.iterator();

      while (var3.hasNext()) {
         IItemTransfer transfer = (IItemTransfer)var3.next();
         if (transfer instanceof ItemStackTransfer serializable) {
            list.add(serializable.serializeNBT());
         } else {
            LDLib.LOGGER.warn("[ItemTransferList] internal container doesn't support serialization");
         }
      }

      tag.m_128365_("slots", list);
      return tag;
   }

   public void deserializeNBT(CompoundTag nbt) {
      ListTag list = nbt.m_128437_("slots", 10);
      this.transfers.clear();

      for (Tag tag : list) {
         if (tag instanceof CompoundTag compoundTag) {
            ItemStackTransfer itemStackTransfer = new ItemStackTransfer();
            itemStackTransfer.deserializeNBT(compoundTag);
            this.addTransfer(itemStackTransfer);
         } else {
            LDLib.LOGGER.warn("[ItemTransferList] internal container doesn't support serialization");
         }
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else {
         return obj instanceof MutableItemTransferList other ? this.transfers.equals(other.transfers) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.transfers.hashCode();
   }

   public Runnable getOnContentsChanged() {
      return this.onContentsChanged;
   }

   public void setOnContentsChanged(Runnable onContentsChanged) {
      this.onContentsChanged = onContentsChanged;
   }

   public Predicate<ItemStack> getFilter() {
      return this.filter;
   }

   protected record TransferSlotPair(IItemTransfer transfer, int localSlot) {
   }
}
