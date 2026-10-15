package org.gtlcore.gtlcore.mixin.ae2.service;

import appeng.api.networking.storage.IStorageWatcherNode;
import appeng.hooks.ticking.TickHandler;
import appeng.me.helpers.InterestManager;
import appeng.me.helpers.StackWatcher;
import appeng.me.service.StorageService;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StorageService.class)
public abstract class StorageServiceMixin {
   @Shadow(remap = false)
   @Final
   @Mutable
   private final InterestManager<StackWatcher<IStorageWatcherNode>> interestManager;
   @Shadow(remap = false)
   private boolean cachedStacksNeedUpdate;
   @Unique
   private static final int STORAGE_MASK = NumberUtils.nearestPow2Lookup(ConfigHolder.INSTANCE.ae2StorageServiceUpdateInterval) - 1;

   @Shadow(remap = false)
   protected abstract void updateCachedStacks();

   public StorageServiceMixin(InterestManager<StackWatcher<IStorageWatcherNode>> interestManager) {
      this.interestManager = interestManager;
   }

   @Overwrite(remap = false)
   public void onServerEndTick() {
      if (this.interestManager.isEmpty()) {
         this.cachedStacksNeedUpdate = true;
      } else if ((TickHandler.instance().getCurrentTick() & STORAGE_MASK) == 0L) {
         this.updateCachedStacks();
      }
   }
}
