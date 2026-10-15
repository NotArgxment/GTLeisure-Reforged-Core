package org.gtlcore.gtlcore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.world.entity.player.Player;
import org.gtlcore.gtlcore.api.machine.trait.ICheckPatternMachine;

public class MolecularAssemblerMultiblockMachine
   extends MolecularAssemblerMultiblockMachineBase
   implements IFancyUIMachine,
   IDisplayUIMachine,
   ICheckPatternMachine,
   IInteractedMachine {
   public MolecularAssemblerMultiblockMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         textList.add(
            Component.m_237115_(this.getRecipeType().registryName.m_214298_())
               .m_6270_(
                  Style.f_131099_
                     .m_131140_(ChatFormatting.AQUA)
                     .m_131144_(new HoverEvent(Action.f_130831_, Component.m_237115_("gtceu.gui.machinemode.title")))
               )
         );
         if (!this.isWorkingEnabled()) {
            textList.add(Component.m_237115_("gtceu.multiblock.work_paused"));
         } else if (this.isActive()) {
            textList.add(Component.m_237115_("gtceu.multiblock.running"));
            int currentProgress = (int)(this.recipeLogic.getProgressPercent() * 100.0);
            textList.add(Component.m_237110_("gtceu.multiblock.progress", new Object[]{currentProgress}));
         } else {
            textList.add(Component.m_237115_("gtceu.multiblock.idling"));
         }

         if (this.recipeLogic.isWaiting()) {
            textList.add(Component.m_237115_("gtceu.multiblock.waiting").m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED)));
         }

         if (this.maxParallel > 1) {
            textList.add(
               Component.m_237110_(
                     "gtceu.multiblock.parallel",
                     new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(this.maxParallel)).m_130940_(ChatFormatting.DARK_PURPLE)}
                  )
                  .m_130940_(ChatFormatting.GRAY)
            );
         }

         textList.add(
            Component.m_237110_(
                  "gtlcore.multiblock.tick_Duration",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(this.tickDuration)).m_130940_(ChatFormatting.BLUE)}
               )
               .m_130940_(ChatFormatting.GRAY)
         );
         textList.add(
            Component.m_237110_(
                  "gtlcore.multiblock.contains_Patttern",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(this.patternSize)).m_130940_(ChatFormatting.GOLD)}
               )
               .m_130940_(ChatFormatting.GRAY)
         );
      } else {
         Component tooltip = Component.m_237115_("gtceu.multiblock.invalid_structure.tooltip").m_130940_(ChatFormatting.GRAY);
         textList.add(
            Component.m_237115_("gtceu.multiblock.invalid_structure")
               .m_130948_(Style.f_131099_.m_131140_(ChatFormatting.RED).m_131144_(new HoverEvent(Action.f_130831_, tooltip)))
         );
      }

      this.getDefinition().getAdditionalDisplay().accept(this, textList);
   }

   public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
      super.attachConfigurators(configuratorPanel);
      ICheckPatternMachine.attachConfigurators(configuratorPanel, this);
   }

   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(0, 0, 190, 125);
      group.addWidget(
         new DraggableScrollableWidgetGroup(4, 4, 182, 117)
            .setBackground(this.getScreenTexture())
            .addWidget(new LabelWidget(4, 5, this.self().getBlockState().m_60734_().m_7705_()))
            .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick))
      );
      group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return group;
   }

   public ModularUI createUI(Player entityPlayer) {
      return new ModularUI(198, 208, this, entityPlayer).widget(new FancyMachineUIWidget(this, 198, 208));
   }

   public List<IFancyUIProvider> getSubTabs() {
      return this.getParts().stream().filter(Objects::nonNull).map(IFancyUIProvider.class::cast).toList();
   }

   public void attachTooltips(TooltipsPanel tooltipsPanel) {
      for (IMultiPart part : this.getParts()) {
         part.attachFancyTooltipsToController(this, tooltipsPanel);
      }
   }

   @Override
   public boolean hasButton() {
      return true;
   }
}
