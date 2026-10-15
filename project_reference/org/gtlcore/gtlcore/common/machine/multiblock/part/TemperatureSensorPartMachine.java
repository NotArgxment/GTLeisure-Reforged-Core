package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
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
public class TemperatureSensorPartMachine extends TieredPartMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      TemperatureSensorPartMachine.class, TieredPartMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   @DescSynced
   protected int max;
   private static int MAX_TEMP = 16000;
   @Persisted
   protected int redStoneSignalOutput = 0;

   public TemperatureSensorPartMachine(IMachineBlockEntity holder) {
      super(holder, 11);
   }

   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(Position.ORIGIN, new Size(176, 112));
      group.addWidget(new TextBoxWidget(8, 35, 65, List.of(LocalizationUtils.format("\u6700\u5927\u6e29\u5ea6", new Object[0]))));
      group.addWidget(
         new TextFieldWidget(80, 35, 85, 18, () -> this.toText(this.max), stringValue -> this.setMax(Mth.m_14045_(this.fromText(stringValue), 1, MAX_TEMP)))
            .setNumbersOnly(1, MAX_TEMP)
      );
      return group;
   }

   public int getOutputSignal(@Nullable Direction side) {
      return side == this.getFrontFacing().m_122424_() ? this.redStoneSignalOutput : 0;
   }

   public void update(int heat) {
      int value = heat > this.max ? 15 : 0;
      if (this.redStoneSignalOutput != value) {
         this.setRedStoneSignalOutput(value);
      }
   }

   private void setRedStoneSignalOutput(int redStoneSignalOutput) {
      this.redStoneSignalOutput = redStoneSignalOutput;
      this.updateSignal();
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

   public void setMax(int max) {
      this.max = max;
   }

   public int getRedStoneSignalOutput() {
      return this.redStoneSignalOutput;
   }
}
