package org.gtlcore.gtlcore.client.gui;

import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.ITooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ModifyIconButton extends Button implements ITooltip {
   private final ModifyIcon icon;
   private final Component displayName;
   private final Component displayValue;

   public ModifyIconButton(OnPress onPress, ModifyIcon icon, Component displayName, Component displayValue) {
      super(0, 0, 8, 8, Component.m_237119_(), onPress, f_252438_);
      this.icon = icon;
      this.displayName = displayName;
      this.displayValue = displayValue;
   }

   public void setVisibility(boolean vis) {
      this.f_93624_ = vis;
      this.f_93623_ = vis;
   }

   public void m_87963_(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
      if (this.f_93624_) {
         ModifyIcon icon = this.getIcon();
         Blitter blitter = icon.getBlitter();
         if (!this.f_93623_) {
            blitter.opacity(0.5F);
         }

         RenderSystem.disableDepthTest();
         RenderSystem.enableBlend();
         if (this.m_93696_()) {
            guiGraphics.m_280509_(this.m_252754_() - 1, this.m_252907_() - 1, this.m_252754_() + this.f_93618_ + 1, this.m_252907_(), -1);
            guiGraphics.m_280509_(this.m_252754_() - 1, this.m_252907_(), this.m_252754_(), this.m_252907_() + this.f_93619_, -1);
            guiGraphics.m_280509_(
               this.m_252754_() + this.f_93618_, this.m_252907_(), this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_, -1
            );
            guiGraphics.m_280509_(
               this.m_252754_() - 1, this.m_252907_() + this.f_93619_, this.m_252754_() + this.f_93618_ + 1, this.m_252907_() + this.f_93619_ + 1, -1
            );
         }

         PoseStack pose = guiGraphics.m_280168_();
         pose.m_85836_();
         pose.m_252880_(this.m_252754_(), this.m_252907_(), 0.0F);
         pose.m_85841_(0.5F, 0.5F, 1.0F);
         Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(0, 0).blit(guiGraphics);
         blitter.dest(0, 0).blit(guiGraphics);
         pose.m_85849_();
         RenderSystem.enableDepthTest();
      }
   }

   private ModifyIcon getIcon() {
      return this.icon;
   }

   public int m_5711_() {
      return this.f_93618_;
   }

   public void m_93674_(int width) {
      this.f_93618_ = width;
   }

   public void setHeight(int value) {
      this.f_93619_ = value;
   }

   public void m_93650_(float alpha) {
      this.f_93625_ = alpha;
   }

   public Rect2i getTooltipArea() {
      return new Rect2i(this.m_252754_(), this.m_252907_(), 8, 8);
   }

   public boolean isTooltipAreaVisible() {
      return this.f_93624_;
   }

   public List<Component> getTooltipMessage() {
      return Collections.singletonList(Component.m_237119_().m_7220_(this.displayName).m_130946_("\n").m_7220_(this.displayValue));
   }
}
