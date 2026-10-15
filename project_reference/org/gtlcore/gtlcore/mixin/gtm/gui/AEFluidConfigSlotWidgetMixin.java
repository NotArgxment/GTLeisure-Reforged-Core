package org.gtlcore.gtlcore.mixin.gtm.gui;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEFluidConfigSlotWidget;
import com.llamalad7.mixinextras.sugar.Local;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AEFluidConfigSlotWidget.class)
public abstract class AEFluidConfigSlotWidgetMixin {
   @ModifyArg(
      method = "drawInBackground",
      at = @At(
         value = "INVOKE",
         target = "Lcom/lowdragmc/lowdraglib/gui/util/DrawerHelper;drawStringFixedCorner(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/String;FFIZF)V",
         ordinal = 0
      ),
      index = 1,
      remap = false
   )
   public String configFormat(String str, @Local(ordinal = 0) GenericStack stack) {
      long amount = stack.amount();
      return amount < 1000L ? amount + "mB" : NumberUtils.formatLong(amount / 1000L) + "B";
   }

   @ModifyArg(
      method = "drawInBackground",
      at = @At(
         value = "INVOKE",
         target = "Lcom/lowdragmc/lowdraglib/gui/util/DrawerHelper;drawStringFixedCorner(Lnet/minecraft/client/gui/GuiGraphics;Ljava/lang/String;FFIZF)V",
         ordinal = 1
      ),
      index = 1,
      remap = false
   )
   public String stockFormat(String str, @Local(ordinal = 1) GenericStack stack) {
      long amount = stack.amount();
      return amount < 1000L ? amount + "mB" : NumberUtils.formatLong(amount / 1000L) + "B";
   }
}
