package org.gtlcore.gtlcore.api.machine.trait.MEStock;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.lowdragmc.lowdraglib.LDLib;
import org.jetbrains.annotations.Nullable;

public class ExportOnlyAEConfigureItemSlot extends ExportOnlyAEItemSlot implements IMESlot {
   private Runnable onConfigChanged;

   public ExportOnlyAEConfigureItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
      super(config, stock);
   }

   public ExportOnlyAEConfigureItemSlot() {
   }

   @Override
   public void setConfigWithoutNotify(@Nullable GenericStack config) {
      this.config = config;
   }

   public void setConfig(@Nullable GenericStack config) {
      super.setConfig(config);
      if (!LDLib.isRemote()) {
         this.onConfigChanged.run();
      }
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
