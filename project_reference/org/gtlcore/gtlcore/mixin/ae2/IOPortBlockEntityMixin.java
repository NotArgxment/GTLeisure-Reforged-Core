package org.gtlcore.gtlcore.mixin.ae2;

import appeng.api.config.Actionable;
import appeng.api.config.OperationMode;
import appeng.api.config.Settings;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.api.storage.cells.StorageCell;
import appeng.blockentity.storage.IOPortBlockEntity;
import appeng.util.ConfigManager;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IOPortBlockEntity.class)
public abstract class IOPortBlockEntityMixin {
   @Shadow(remap = false)
   @Final
   private ConfigManager manager;
   @Shadow(remap = false)
   @Final
   private IActionSource mySrc;
   private static final int MAX_TRANSFER_LOOPS = 8192;

   @Overwrite(remap = false)
   private long transferContents(IGrid grid, StorageCell cellInv, long itemsToMove) {
      MEStorage networkInv = grid.getStorageService().getInventory();
      KeyCounter srcList;
      MEStorage src;
      MEStorage destination;
      if (this.manager.getSetting(Settings.OPERATION_MODE) == OperationMode.EMPTY) {
         src = cellInv;
         srcList = cellInv.getAvailableStacks();
         destination = networkInv;
      } else {
         src = networkInv;
         srcList = grid.getStorageService().getCachedInventory();
         destination = cellInv;
      }

      IEnergyService energy = grid.getEnergyService();
      int loopBudget = 8192;

      while (loopBudget-- > 0) {
         boolean didStuff = false;

         for (Entry<AEKey> srcEntry : srcList) {
            long totalStackSize = srcEntry.getLongValue();
            if (totalStackSize > 0L) {
               AEKey what = (AEKey)srcEntry.getKey();
               long possible = destination.insert(what, totalStackSize, Actionable.SIMULATE, this.mySrc);
               if (possible > 0L) {
                  possible = Math.min(possible, itemsToMove * what.getAmountPerOperation());
                  possible = src.extract(what, possible, Actionable.MODULATE, this.mySrc);
                  if (possible > 0L) {
                     long inserted = StorageHelper.poweredInsert(energy, destination, what, possible, this.mySrc);
                     if (inserted < possible) {
                        src.insert(what, possible - inserted, Actionable.MODULATE, this.mySrc);
                     }

                     if (inserted > 0L) {
                        itemsToMove -= Math.max(1L, inserted / what.getAmountPerOperation());
                        didStuff = true;
                     }
                     break;
                  }
               }
            }
         }

         if (itemsToMove <= 0L || !didStuff) {
            return itemsToMove;
         }
      }

      return itemsToMove;
   }
}
