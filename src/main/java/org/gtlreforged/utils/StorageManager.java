package org.gtlreforged.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.UUID;
import java.util.Map.Entry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import org.gtlreforged.integration.ae2.storage.InfinityCellDataStorage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageManager extends SavedData {

   private final Object2ObjectMap<UUID, InfinityCellDataStorage> disks;

   public StorageManager() {
      this.disks = new Object2ObjectOpenHashMap<>();
      this.setDirty();
   }

   private StorageManager(Object2ObjectMap<UUID, InfinityCellDataStorage> disks) {
      this.disks = disks;
      this.setDirty();
   }

   @NotNull
   public CompoundTag save(@NotNull CompoundTag nbt) {
      ListTag diskList = new ListTag();

       for (Entry<UUID, InfinityCellDataStorage> entry : this.disks.entrySet()) {
           UUID uuid = entry.getKey();
           InfinityCellDataStorage storage = entry.getValue();
           if (uuid != null && storage != null) {
               CompoundTag disk = new CompoundTag();
               disk.putUUID("diskuuid", uuid);
               disk.put("diskdata", storage.toNbt());
               diskList.add(disk);
           }
       }

      nbt.put("disklist", diskList);
      return nbt;
   }

   public static StorageManager readNbt(CompoundTag nbt) {
      Object2ObjectMap<UUID, InfinityCellDataStorage> disks = new Object2ObjectOpenHashMap<>();
      ListTag diskList = nbt.getList("disklist", 10);

      for (int i = 0; i < diskList.size(); i++) {
         CompoundTag disk = diskList.getCompound(i);
         disks.put(disk.getUUID("diskuuid"), InfinityCellDataStorage.fromNbt(disk.getCompound("diskdata")));
      }

      return new StorageManager(disks);
   }

   public void updateDisk(UUID uuid, InfinityCellDataStorage infinityCellDataStorage) {
      this.disks.put(uuid, infinityCellDataStorage);
      this.setDirty();
   }

   public void removeDisk(UUID uuid) {
      this.disks.remove(uuid);
      this.setDirty();
   }

   public InfinityCellDataStorage getOrCreateDisk(UUID uuid, boolean isFastCell) {
      if (!this.disks.containsKey(uuid)) {
         this.updateDisk(uuid, new InfinityCellDataStorage(isFastCell));
      }
      return this.disks.get(uuid);
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
      ServerLevel world = server.getLevel(ServerLevel.OVERWORLD);
       assert world != null;
       return world.getDataStorage().computeIfAbsent(StorageManager::readNbt, StorageManager::new, "disk_manager");
   }
}
