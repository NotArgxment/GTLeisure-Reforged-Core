package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.integration.ae2.gui.widget.AmountSetWidget;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.ConfigWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AmountSetWidget.class)
public abstract class AmountSetWidgetMixin {
   @Shadow(remap = false)
   @Final
   private TextFieldWidget amountText;

   @Inject(method = "<init>", at = @At("TAIL"))
   private void a(int x, int y, ConfigWidget widget, CallbackInfo ci) {
      this.amountText.setNumbersOnly(0L, 9223372036854775807L).setMaxStringLength(19);
   }
}
