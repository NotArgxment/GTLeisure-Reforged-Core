package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.api.stacks.GenericStack;
import appeng.client.gui.me.items.SetProcessingPatternAmountScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SetProcessingPatternAmountScreen.class)
public abstract class SetProcessingPatternAmountScreenMixin {
   @Shadow(remap = false)
   @Final
   private GenericStack currentStack;

   @Overwrite(remap = false)
   private long getMaxAmount() {
      return 2147483647L * this.currentStack.what().getAmountPerUnit();
   }
}
