package org.gtlcore.gtlcore.api.machine.trait.MEStock;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.lowdragmc.lowdraglib.LDLib;
import org.jetbrains.annotations.Nullable;

public class ExportOnlyAEConfigureFluidSlot extends ExportOnlyAEFluidSlot implements IMESlot {
   private Runnable onConfigChanged;

   public ExportOnlyAEConfigureFluidSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
      super(config, stock);
   }

   public ExportOnlyAEConfigureFluidSlot() {
   }

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
   public Runnable getOnConfigChanged() {
      return this.onConfigChanged;
   }

   @Override
   public void setOnConfigChanged(Runnable onConfigChanged) {
      this.onConfigChanged = onConfigChanged;
   }
}
