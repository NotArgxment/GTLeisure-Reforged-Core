package org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Supplier;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AutoConfigurationMaintenanceHatchPartMachine extends TieredPartMachine implements IMaintenanceMachine, IAutoConfigurationMaintenanceHatch {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      AutoConfigurationMaintenanceHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   private static final float MAX_DURATION_MULTIPLIER = 1.2F;
   private static final float MIN_DURATION_MULTIPLIER = 0.2F;
   private static final float DURATION_ACTION_AMOUNT = 0.01F;
   @Persisted
   private float durationMultiplier = 1.0F;

   public AutoConfigurationMaintenanceHatchPartMachine(IMachineBlockEntity metaTileEntityId) {
      super(metaTileEntityId, 5);
   }

   public void setTaped(boolean ignored) {
   }

   public boolean isTaped() {
      return false;
   }

   public boolean isFullAuto() {
      return true;
   }

   public byte startProblems() {
      return 63;
   }

   public byte getMaintenanceProblems() {
      return 63;
   }

   public void setMaintenanceProblems(byte problems) {
   }

   public int getTimeActive() {
      return 0;
   }

   public void setTimeActive(int time) {
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public float getTimeMultiplier() {
      float result = 1.0F;
      if (this.durationMultiplier < 1.0) {
         result = -20.0F * this.durationMultiplier + 21.0F;
      } else {
         result = -8.0F * this.durationMultiplier + 9.0F;
      }

      return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP).floatValue();
   }

   public GTRecipe modifyRecipe(GTRecipe recipe) {
      if (ConfigHolder.INSTANCE.machines.enableMaintenance) {
         if (this.hasMaintenanceProblems()) {
            return null;
         }

         float durationMultiplier = this.getDurationMultiplier();
         if (durationMultiplier != 1.0F) {
            recipe = recipe.copy();
            recipe.duration = (int)Math.max(1.0F, recipe.duration * durationMultiplier);
         }
      }

      return recipe;
   }

   private void incInternalMultiplier(int multiplier) {
      float newDurationMultiplier = this.durationMultiplier + 0.01F * multiplier;
      if (newDurationMultiplier >= 1.2F) {
         this.durationMultiplier = 1.2F;
      } else {
         this.durationMultiplier = newDurationMultiplier;
      }
   }

   private void decInternalMultiplier(int multiplier) {
      float newDurationMultiplier = this.durationMultiplier - 0.01F * multiplier;
      if (newDurationMultiplier <= 0.2F) {
         this.durationMultiplier = 0.2F;
      } else {
         this.durationMultiplier = newDurationMultiplier;
      }
   }

   public void addedToController(@NotNull IMultiController controller) {
      super.addedToController(controller);
      ICleaningRoom.addedToController(controller, null);
   }

   public void removedFromController(@NotNull IMultiController controller) {
      super.removedFromController(controller);
      ICleaningRoom.removedFromController(controller, null);
   }

   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(0, 0, 150, 70);
      group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 142, 62).setBackground(GuiTextures.DISPLAY).addWidget(new ComponentPanelWidget(4, 5, list -> {
         list.add(getTextWidgetText(this::getDurationMultiplier));
         MutableComponent buttonText = Component.m_237115_("gtceu.maintenance.configurable_duration.modify");
         buttonText.m_130946_(" ");
         buttonText.m_7220_(ComponentPanelWidget.withButton(Component.m_237113_("[-]"), "sub"));
         buttonText.m_130946_(" ");
         buttonText.m_7220_(ComponentPanelWidget.withButton(Component.m_237113_("[+]"), "add"));
         list.add(buttonText);
      }).setMaxWidthLimit(130).clickHandler((componentData, clickData) -> {
         if (!clickData.isRemote) {
            int multiplier = clickData.isCtrlClick ? 100 : (clickData.isShiftClick ? 10 : 1);
            if (componentData.equals("sub")) {
               this.decInternalMultiplier(multiplier);
            } else if (componentData.equals("add")) {
               this.incInternalMultiplier(multiplier);
            }
         }
      })));
      group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return group;
   }

   private static Component getTextWidgetText(Supplier<Float> multiplier) {
      Component tooltip;
      if (multiplier.get().floatValue() == 1.0) {
         tooltip = Component.m_237115_("gtceu.maintenance.configurable_duration.unchanged_description");
      } else {
         tooltip = Component.m_237110_("gtceu.maintenance.configurable_duration.changed_description", new Object[]{multiplier.get()});
      }

      return Component.m_237110_("gtceu.maintenance.configurable_duration", new Object[]{multiplier.get()})
         .m_6270_(Style.f_131099_.m_131144_(new HoverEvent(Action.f_130831_, tooltip)));
   }

   @Override
   public void setDurationMultiplier(float count) {
      this.durationMultiplier = Mth.m_14036_(count, 0.2F, 1.2F);
   }

   @Override
   public float getDurationMultiplier() {
      return this.durationMultiplier;
   }
}
