package org.gtlcore.gtlcore.mixin.ae2;

import appeng.client.gui.ICompositeWidget;
import appeng.client.gui.widgets.CPUSelectionList;
import appeng.menu.me.crafting.CraftingStatusMenu.CraftingCpuListEntry;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CPUSelectionList.class)
public abstract class CPUSelectionListMixin implements ICompositeWidget {
   @Inject(method = "formatStorage", at = @At("HEAD"), remap = false, cancellable = true)
   private void formatStorage(CraftingCpuListEntry cpu, CallbackInfoReturnable<String> cir) {
      cir.setReturnValue(NumberUtils.numberText(cpu.storage()).getString());
   }
}
