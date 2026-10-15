package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.client.gui.ModifyIcon;
import org.gtlcore.gtlcore.client.gui.ModifyIconButton;
import org.gtlcore.gtlcore.client.gui.PatterEncodingTermMenuModify;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProcessingEncodingPanel.class)
public abstract class ProcessingEncodingPanelMixin extends EncodingModePanel {
   @Unique
   private ModifyIconButton gTLCore$multipleTow;
   @Unique
   private ModifyIconButton gTLCore$multipleThree;
   @Unique
   private ModifyIconButton gTLCore$multipleFive;
   @Unique
   private ModifyIconButton gTLCore$dividingTow;
   @Unique
   private ModifyIconButton gTLCore$dividingThree;
   @Unique
   private ModifyIconButton gTLCore$dividingFive;

   public ProcessingEncodingPanelMixin(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
      super(screen, widgets);
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   public void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
      this.gTLCore$multipleTow = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(2),
         ModifyIcon.MULTIPLY_2,
         Component.m_237115_("gui.gtlcore.pattern_recipe_multiply_2"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_multiply_2")
      );
      this.gTLCore$multipleThree = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(3),
         ModifyIcon.MULTIPLY_3,
         Component.m_237115_("gui.gtlcore.pattern_recipe_multiply_3"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_multiply_3")
      );
      this.gTLCore$multipleFive = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(5),
         ModifyIcon.MULTIPLY_5,
         Component.m_237115_("gui.gtlcore.pattern_recipe_multiply_5"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_multiply_5")
      );
      this.gTLCore$dividingTow = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(-2),
         ModifyIcon.DIVISION_2,
         Component.m_237115_("gui.gtlcore.pattern_recipe_divide_2"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_divide_2")
      );
      this.gTLCore$dividingThree = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(-3),
         ModifyIcon.DIVISION_3,
         Component.m_237115_("gui.gtlcore.pattern_recipe_divide_3"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_divide_3")
      );
      this.gTLCore$dividingFive = new ModifyIconButton(
         b -> ((PatterEncodingTermMenuModify)this.menu).gTLCore$modifyPatter(-5),
         ModifyIcon.DIVISION_5,
         Component.m_237115_("gui.gtlcore.pattern_recipe_divide_5"),
         Component.m_237115_("tooltip.gtlcore.pattern_materials_divide_5")
      );
      widgets.add("modify1", this.gTLCore$multipleTow);
      widgets.add("modify2", this.gTLCore$multipleThree);
      widgets.add("modify3", this.gTLCore$multipleFive);
      widgets.add("modify4", this.gTLCore$dividingTow);
      widgets.add("modify5", this.gTLCore$dividingThree);
      widgets.add("modify6", this.gTLCore$dividingFive);
   }

   @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
   public void setVisibleHooks(boolean visible, CallbackInfo ci) {
      this.gTLCore$multipleTow.setVisibility(visible);
      this.gTLCore$multipleThree.setVisibility(visible);
      this.gTLCore$multipleFive.setVisibility(visible);
      this.gTLCore$dividingTow.setVisibility(visible);
      this.gTLCore$dividingThree.setVisibility(visible);
      this.gTLCore$dividingFive.setVisibility(visible);
   }
}
