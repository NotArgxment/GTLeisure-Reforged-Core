package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExportOnlyAESlot.class)
public interface ExportOnlyAESlotAccessor {
   @Accessor(value = "config", remap = false)
   @Nullable
   GenericStack getConfig();

   @Accessor(value = "onContentsChanged", remap = false)
   Runnable getOnContentsChanged();
}
