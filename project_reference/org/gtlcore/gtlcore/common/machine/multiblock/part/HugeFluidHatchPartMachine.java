package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HugeFluidHatchPartMachine extends FluidHatchPartMachine {
   public HugeFluidHatchPartMachine(IMachineBlockEntity holder, int tier, IO io, Object... args) {
      super(holder, tier, io, 0L, tier, args);
   }

   protected NotifiableFluidTank createTank(long initialCapacity, int slots, Object... args) {
      return new NotifiableFluidTank(this, slots, 2147483647L, this.io);
   }

   public Widget createUIWidget() {
      if (this.tier == 1) {
         return super.createSingleSlotGUI();
      }

      int rowSize = 1;
      int colSize = 1;

      for (int i = this.tier; i > 1; i--) {
         if (this.tier % i == 0) {
            rowSize = i;
            colSize = this.tier / i;
         }
      }

      WidgetGroup group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
      WidgetGroup container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * colSize + 8);
      int index = 0;

      for (int y = 0; y < colSize; y++) {
         for (int x = 0; x < rowSize && index < this.tier; x++) {
            container.addWidget(
               new TankWidget(this.tank.getStorages()[index++], 4 + x * 18, 4 + y * 18, true, this.io.support(IO.IN)).setBackground(GuiTextures.FLUID_SLOT)
            );
         }
      }

      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      group.addWidget(container);
      return group;
   }
}
