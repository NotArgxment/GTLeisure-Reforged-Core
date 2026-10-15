package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.gregtechceu.gtceu.utils.RedstoneUtil;
import com.lowdragmc.lowdraglib.gui.widget.TextBoxWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronSensorPartMachine extends TieredPartMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      NeutronSensorPartMachine.class, TieredPartMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   @DescSynced
   private int min;
   @Persisted
   @DescSynced
   private int max;
   @Persisted
   private boolean isInverted;
   @Persisted
   protected int redstoneSignalOutput = 0;

   public NeutronSensorPartMachine(IMachineBlockEntity holder) {
      super(holder, 5);
   }

   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(Position.ORIGIN, new Size(176, 112));
      group.addWidget(new TextBoxWidget(8, 35, 65, List.of(LocalizationUtils.format("\u6700\u5c0f\u4e2d\u5b50\u52a8\u80fd\n(%s)", new Object[]{"MeV"}))));
      group.addWidget(new TextBoxWidget(8, 80, 65, List.of(LocalizationUtils.format("\u6700\u5927\u4e2d\u5b50\u52a8\u80fd\n(%s)", new Object[]{"MeV"}))));
      group.addWidget(
         new TextFieldWidget(80, 35, 85, 18, () -> this.toText(this.min), stringValue -> this.setMin(Mth.m_14045_(this.fromText(stringValue), 0, this.max)))
            .setNumbersOnly(0, this.max)
      );
      group.addWidget(
         new TextFieldWidget(
               80, 80, 85, 18, () -> this.toText(this.max), stringValue -> this.setMax(Mth.m_14045_(this.fromText(stringValue), this.min, 1200000))
            )
            .setNumbersOnly(this.min, 1200000)
      );
      group.addWidget(new ToggleButtonWidget(8, 8, 20, 20, GuiTextures.INVERT_REDSTONE_BUTTON, this::isInverted, this::setInverted) {
         public void updateScreen() {
            super.updateScreen();
            this.setHoverTooltips(List.copyOf(LangHandler.getMultiLang("gui.gtceu.neutron_sensor.invert." + (this.isPressed ? "enabled" : "disabled"))));
         }
      });
      return group;
   }

   public void update(int energy) {
      int output = RedstoneUtil.computeRedstoneBetweenValues(energy, this.max * 1000000, this.min * 1000000, this.isInverted());
      if (this.redstoneSignalOutput != output) {
         this.setRedstoneSignalOutput(output);
      }
   }

   private void setRedstoneSignalOutput(int redstoneSignalOutput) {
      this.redstoneSignalOutput = redstoneSignalOutput;
      this.updateSignal();
   }

   public int getOutputSignal(@Nullable Direction side) {
      return side == this.getFrontFacing().m_122424_() ? this.redstoneSignalOutput : 0;
   }

   public boolean canConnectRedstone(Direction side) {
      return false;
   }

   private String toText(int num) {
      return String.valueOf(num);
   }

   private int fromText(String num) {
      return Integer.parseInt(num);
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void setMin(int min) {
      this.min = min;
   }

   public void setMax(int max) {
      this.max = max;
   }

   public void setInverted(boolean isInverted) {
      this.isInverted = isInverted;
   }

   public boolean isInverted() {
      return this.isInverted;
   }

   public int getRedstoneSignalOutput() {
      return this.redstoneSignalOutput;
   }
}
