package org.gtlcore.gtlcore.mixin.meRequester;

import appeng.api.stacks.AEKey;
import com.almostreliable.merequester.client.widgets.NumberField;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NumberField.class)
public abstract class NumberFieldMixin extends EditBox {
   @Shadow(remap = false)
   private boolean isFluid;

   public NumberFieldMixin(Font font, int x, int y, int width, int height, Component message) {
      super(font, x, y, width, height, message);
   }

   @ModifyConstant(method = "<init>", constant = @Constant(intValue = 7))
   private int replaceMaxLength(int seven) {
      return 15;
   }

   @Inject(method = "adjustToType", at = @At("TAIL"), remap = false)
   private void adjustMaxLength(AEKey key, CallbackInfo ci) {
      if (this.isFluid) {
         this.m_94199_(16);
      } else {
         this.m_94199_(19);
      }
   }
}
