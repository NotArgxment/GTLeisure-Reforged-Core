package org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance;

import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class GravityCleaningConfigurationMaintenancePartMachine extends AutoConfigurationMaintenanceHatchPartMachine implements IAutoConfiguratioGravityPart {
   @Persisted
   private int gravity = 0;
   private boolean isConfig = true;
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      GravityCleaningConfigurationMaintenancePartMachine.class, AutoConfigurationMaintenanceHatchPartMachine.MANAGED_FIELD_HOLDER
   );
   ICleanroomProvider cleanroomTypes;

   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public GravityCleaningConfigurationMaintenancePartMachine(IMachineBlockEntity blockEntity) {
      super(blockEntity);
   }

   public GravityCleaningConfigurationMaintenancePartMachine(IMachineBlockEntity blockEntity, boolean isConfig) {
      super(blockEntity);
      this.isConfig = isConfig;
   }

   public GravityCleaningConfigurationMaintenancePartMachine(IMachineBlockEntity blockEntity, ICleanroomProvider cleanroom) {
      super(blockEntity);
      this.cleanroomTypes = cleanroom;
   }

   @Override
   public void addedToController(@NotNull IMultiController controller) {
      super.addedToController(controller);
      ICleaningRoom.addedToController(controller, this.cleanroomTypes);
   }

   @Override
   public void removedFromController(@NotNull IMultiController controller) {
      super.removedFromController(controller);
      ICleaningRoom.removedFromController(controller, this.cleanroomTypes);
   }

   @NotNull
   @Override
   public Widget createUIWidget() {
      if (this.isConfig) {
         WidgetGroup group = (WidgetGroup)super.createUIWidget();
         group.addWidget(new IntInputWidget(10, 35, 80, 10, this::getCurrentGravity, this::setCurrentGravity).setMin(0).setMax(100));
         return group;
      } else {
         WidgetGroup gravityGroup = new WidgetGroup(0, 0, 100, 20);
         gravityGroup.addWidget(new IntInputWidget(this::getCurrentGravity, this::setCurrentGravity).setMin(0).setMax(100));
         return gravityGroup;
      }
   }

   public int getTier() {
      return 12;
   }

   @Override
   public int getCurrentGravity() {
      return this.gravity;
   }

   @Override
   public void setCurrentGravity(int gravity) {
      this.gravity = Mth.m_14045_(gravity, 0, 100);
   }

   @Override
   public void setDurationMultiplier(float count) {
      if (this.isConfig) {
         super.setDurationMultiplier(count);
      }
   }

   @Override
   public float getDurationMultiplier() {
      return this.isConfig ? super.getDurationMultiplier() : 1.0F;
   }
}
