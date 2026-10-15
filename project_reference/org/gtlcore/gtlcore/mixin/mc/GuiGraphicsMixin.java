package org.gtlcore.gtlcore.mixin.mc;

import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
   @Redirect(
      method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
      at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;")
   )
   public String renderItemDecorations(int i, @Local(ordinal = 0, argsOnly = true) ItemStack stack) {
      return this.core$format(stack.m_41613_());
   }

   @Unique
   private String core$format(int number) {
      double temp = number;

      int unitIndex;
      for (unitIndex = 0; temp >= 1000.0 && unitIndex < NumberUtils.UNITS.length - 1; unitIndex++) {
         temp /= 1000.0;
      }

      return FormattingUtil.DECIMAL_FORMAT_0F.format(temp) + NumberUtils.UNITS[unitIndex];
   }
}
