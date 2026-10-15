package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.lowdragmc.lowdraglib.LDLib;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMESlot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine$ExportOnlyAEStockingItemSlot", remap = false)
public abstract class ExportOnlyAEStockingItemSlotMixin extends ExportOnlyAEItemSlot implements IMESlot {
   private Runnable onConfigChanged;

   public void setConfig(@Nullable GenericStack config) {
      super.setConfig(config);
      if (!LDLib.isRemote()) {
         this.onConfigChanged.run();
      }
   }

   @Override
   public void setConfigWithoutNotify(@Nullable GenericStack config) {
      this.config = config;
   }

   @Override
   public void setOnConfigChanged(Runnable onConfigChanged) {
      this.onConfigChanged = onConfigChanged;
   }

   @Override
   public Runnable getOnConfigChanged() {
      return this.onConfigChanged;
   }
}
