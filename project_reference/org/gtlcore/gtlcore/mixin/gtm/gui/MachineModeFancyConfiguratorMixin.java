package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.MachineModeFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture.TextType;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.gtlcore.gtlcore.api.gui.MachineModeConfigurator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MachineModeFancyConfigurator.class)
public abstract class MachineModeFancyConfiguratorMixin {
   @Shadow(remap = false)
   protected IRecipeLogicMachine machine;

   @Overwrite(remap = false)
   public Widget createMainPage(FancyMachineUIWidget widget) {
      int length = this.machine.getRecipeTypes().length;
      MachineModeConfigurator group = new MachineModeConfigurator(0, 0, 140, 20 * Math.min(length, 6) + 4, this.machine);
      group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      if (length > 6) {
         DraggableScrollableWidgetGroup widgetGroup = new DraggableScrollableWidgetGroup(2, 2, 136, 120);
         this.addWidgets(widgetGroup, length);
         group.addWidget(widgetGroup);
      } else {
         this.addWidgets(group, length);
      }

      return group;
   }

   private void addWidgets(WidgetGroup group, int length) {
      for (int i = 0; i < length; i++) {
         int finalI = i;
         group.addWidget(
            new ButtonWidget(length < 7 ? 2 : 0, (length < 7 ? 2 : 0) + i * 20, 136, 20, IGuiTexture.EMPTY, cd -> this.machine.setActiveRecipeType(finalI))
         );
         group.addWidget(
            new ImageWidget(
               length < 7 ? 2 : 0,
               (length < 7 ? 2 : 0) + i * 20,
               136,
               20,
               () -> new GuiTextureGroup(
                  new IGuiTexture[]{
                     ResourceBorderTexture.BUTTON_COMMON.copy().setColor(this.machine.getActiveRecipeType() == finalI ? ColorPattern.CYAN.color : -1),
                     new TextTexture(this.machine.getRecipeTypes()[finalI].registryName.m_214298_()).setWidth(136).setType(TextType.ROLL)
                  }
               )
            )
         );
      }
   }
}
