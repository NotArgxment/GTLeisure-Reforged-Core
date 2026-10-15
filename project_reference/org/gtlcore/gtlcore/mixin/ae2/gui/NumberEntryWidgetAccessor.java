package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.client.gui.widgets.ConfirmableTextField;
import appeng.client.gui.widgets.NumberEntryWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NumberEntryWidget.class)
public interface NumberEntryWidgetAccessor {
   @Accessor(value = "textField", remap = false)
   ConfirmableTextField getTextField();
}
