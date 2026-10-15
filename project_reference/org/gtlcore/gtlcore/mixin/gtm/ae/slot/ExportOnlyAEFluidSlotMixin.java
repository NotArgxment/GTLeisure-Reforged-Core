package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExportOnlyAEFluidSlot.class)
public abstract class ExportOnlyAEFluidSlotMixin extends ExportOnlyAESlot {
   @Shadow(remap = false)
   public void onContentsChanged() {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   public FluidStack drain(long maxDrain, boolean simulate, boolean notifyChanges) {
      if (this.stock != null && this.stock.what() instanceof AEFluidKey fluidKey) {
         long var9 = Math.min(this.stock.amount(), maxDrain);
         FluidStack result = FluidStack.create(fluidKey.getFluid(), var9, fluidKey.getTag());
         if (!simulate) {
            this.stock = new GenericStack(this.stock.what(), this.stock.amount() - var9);
            if (this.stock.amount() == 0L) {
               this.stock = null;
            }

            if (notifyChanges) {
               this.onContentsChanged();
            }
         }

         return result;
      } else {
         return FluidStack.empty();
      }
   }
}
