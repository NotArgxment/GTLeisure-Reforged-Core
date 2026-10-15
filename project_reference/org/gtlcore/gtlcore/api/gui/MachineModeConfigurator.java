package org.gtlcore.gtlcore.api.gui;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.network.FriendlyByteBuf;

public class MachineModeConfigurator extends WidgetGroup {
   protected IRecipeLogicMachine machine;

   public MachineModeConfigurator(int x, int y, int width, int height, IRecipeLogicMachine machine) {
      super(x, y, width, height);
      this.machine = machine;
   }

   public void writeInitialData(FriendlyByteBuf buffer) {
      buffer.m_130130_(this.machine.getActiveRecipeType());
   }

   public void readInitialData(FriendlyByteBuf buffer) {
      this.machine.setActiveRecipeType(buffer.m_130242_());
   }

   public void detectAndSendChanges() {
      this.writeUpdateInfo(0, buf -> buf.m_130130_(this.machine.getActiveRecipeType()));
   }

   public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
      if (id == 0) {
         this.machine.setActiveRecipeType(buffer.m_130242_());
      }
   }
}
