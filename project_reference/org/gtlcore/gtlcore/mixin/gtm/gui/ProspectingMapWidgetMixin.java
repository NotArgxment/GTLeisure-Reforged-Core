package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.api.gui.texture.ProspectingTexture;
import com.gregtechceu.gtceu.api.gui.widget.ProspectingMapWidget;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.SearchComponentWidget.IWidgetSearch;
import org.gtlcore.gtlcore.client.gui.IProspectingTextureMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProspectingMapWidget.class)
public abstract class ProspectingMapWidgetMixin extends WidgetGroup implements IWidgetSearch<Object> {
   @Shadow(remap = false)
   private ProspectingTexture texture;

   @Inject(method = "<init>", at = @At("TAIL"), remap = false)
   public void intiHooks(
      int xPosition,
      int yPosition,
      int width,
      int height,
      int chunkRadius,
      ProspectorMode<?> mode,
      int scanTick,
      CallbackInfo ci,
      @Local(ordinal = 6) int imageWidth,
      @Local(ordinal = 7) int imageHeight
   ) {
      this.widgets.remove(0);
      this.addWidget(
         0,
         new ImageWidget(0, (height - imageHeight) / 2 - 4, imageWidth + 8, imageHeight + 8, GuiTextures.BACKGROUND_INVERSE) {
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
               if (this.isMouseOverElement(mouseX, mouseY)) {
                  if (button == 0) {
                     ((IProspectingTextureMixin)ProspectingMapWidgetMixin.this.texture)
                        .gTLCore$teleportWayPoint(this.gui.entityPlayer, mouseX - this.getPositionX() - 4.0, mouseY - this.getPositionY() - 4.0);
                  } else if (button == 1) {
                     ((IProspectingTextureMixin)ProspectingMapWidgetMixin.this.texture)
                        .gTLCore$addWayPoint(this.gui.entityPlayer, mouseX - this.getPositionX() - 4.0, mouseY - this.getPositionY() - 4.0);
                  }
               }

               return false;
            }
         }
      );
   }
}
