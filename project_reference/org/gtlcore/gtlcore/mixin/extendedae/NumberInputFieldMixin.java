package org.gtlcore.gtlcore.mixin.extendedae;

import appeng.client.gui.NumberEntryType;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.NumberEntryWidget;
import com.glodblock.github.extendedae.client.gui.widget.NumberInputField;
import org.gtlcore.gtlcore.mixin.ae2.gui.NumberEntryWidgetAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NumberInputField.class)
public abstract class NumberInputFieldMixin extends NumberEntryWidget {
   public NumberInputFieldMixin(ScreenStyle style, NumberEntryType type) {
      super(style, type);
   }

   @Inject(method = "<init>", at = @At("TAIL"), remap = false)
   private void changeMaxLength(ScreenStyle style, NumberEntryType type, CallbackInfo ci) {
      ((NumberEntryWidgetAccessor)this).getTextField().m_94199_(19);
   }

   public void setType(NumberEntryType type) {
      super.setType(type);
      ((NumberEntryWidgetAccessor)this).getTextField().m_94199_(type.amountPerUnit() == 1 ? 19 : 16);
   }
}
