package org.gtlcore.gtlcore.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.gtlcore.gtlcore.integration.ae2.storage.InfinityCellDataStorage;
import org.jetbrains.annotations.NotNull;

public class StorageManager extends SavedData {
   private final Object2ObjectMap<UUID, InfinityCellDataStorage> disks;

   public StorageManager() {
      this.disks = new Object2ObjectOpenHashMap();
      this.m_77762_();
   }

   private StorageManager(Object2ObjectMap<UUID, InfinityCellDataStorage> disks) {
      this.disks = disks;
      this.m_77762_();
   }

   @NotNull
   public CompoundTag m_7176_(@NotNull CompoundTag nbt) {
      ListTag diskList = new ListTag();
      ObjectIterator var3 = this.disks.entrySet().iterator();

      while (var3.hasNext()) {
         Entry<UUID, InfinityCellDataStorage> entry = (Entry<UUID, InfinityCellDataStorage>)var3.next();
         UUID uuid = entry.getKey();
         InfinityCellDataStorage storage = entry.getValue();
         if (uuid != null && storage != null) {
            CompoundTag disk = new CompoundTag();
            disk.m_128362_("diskuuid", uuid);
            disk.m_128365_("diskdata", storage.toNbt());
            diskList.add(disk);
         }
      }

      nbt.m_128365_("disklist", diskList);
      return nbt;
   }

   public static StorageManager readNbt(CompoundTag nbt) {
      Object2ObjectMap<UUID, InfinityCellDataStorage> disks = new Object2ObjectOpenHashMap();
      ListTag diskList = nbt.m_128437_("disklist", 10);

      for (int i = 0; i < diskList.size(); i++) {
         CompoundTag disk = diskList.m_128728_(i);
         disks.put(disk.m_128342_("diskuuid"), InfinityCellDataStorage.fromNbt(disk.m_128469_("diskdata")));
      }

      return new StorageManager(disks);
   }

   public void updateDisk(UUID uuid, InfinityCellDataStorage infinityCellDataStorage) {
      this.disks.put(uuid, infinityCellDataStorage);
      this.m_77762_();
   }

   public void removeDisk(UUID uuid) {
      this.disks.remove(uuid);
      this.m_77762_();
   }

   public InfinityCellDataStorage getOrCreateDisk(UUID uuid, boolean isFastCell) {
      if (!this.disks.containsKey(uuid)) {
         this.updateDisk(uuid, new InfinityCellDataStorage(isFastCell));
      }

      return (InfinityCellDataStorage)this.disks.get(uuid);
   }

   public void modifyDisk(UUID diskID, ListTag stackKeys, ListTag amounts, double totalAmount, boolean isFastCell) {
      InfinityCellDataStorage diskToModify = this.getOrCreateDisk(diskID, isFastCell);
      if (stackKeys != null && amounts != null) {
         diskToModify.stackKeys = stackKeys;
         diskToModify.amounts = amounts;
      }

      diskToModify.totalAmount = totalAmount;
      this.updateDisk(diskID, diskToModify);
   }

   public static StorageManager getInstance(MinecraftServer server) {
      ServerLevel world = server.m_129880_(ServerLevel.f_46428_);
      return (StorageManager)world.m_8895_().m_164861_(StorageManager::readNbt, StorageManager::new, "disk_manager");
   }
}
