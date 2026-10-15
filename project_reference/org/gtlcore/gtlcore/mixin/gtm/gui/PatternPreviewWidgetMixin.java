package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.widget.PatternPreviewWidget;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PatternPreviewWidget.class)
public abstract class PatternPreviewWidgetMixin {
   @Overwrite(remap = false)
   public static PatternPreviewWidget getPatternWidget(MultiblockMachineDefinition controllerDefinition) {
      return null;
   }
}
