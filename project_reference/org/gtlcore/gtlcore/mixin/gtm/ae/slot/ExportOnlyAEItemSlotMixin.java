package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMETransfer;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IMETransfer.class, prefix = "gTLCore$"))
@Mixin(ExportOnlyAEItemSlot.class)
public abstract class ExportOnlyAEItemSlotMixin extends ExportOnlyAESlot {
   @Shadow(remap = false)
   public void onContentsChanged() {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   public ItemStack getStackInSlot(int slot) {
      if (slot == 0 && this.stock != null) {
         return this.stock.what() instanceof AEItemKey itemKey ? itemKey.toStack(Ints.saturatedCast(this.stock.amount())) : ItemStack.f_41583_;
      } else {
         return ItemStack.f_41583_;
      }
   }

   @Unique
   public GenericStack gTLCore$extractGenericStack(long amount, boolean simulate, boolean notifyChanges) {
      if (this.stock != null) {
         long extracted = Math.min(this.stock.amount(), amount);
         if (this.stock.what() instanceof AEItemKey itemKey) {
            GenericStack var9 = new GenericStack(itemKey, extracted);
            if (!simulate) {
               this.stock = ExportOnlyAESlot.copy(this.stock, this.stock.amount() - extracted);
               if (this.stock.amount() == 0L) {
                  this.stock = null;
               }
            }

            if (notifyChanges) {
               this.onContentsChanged();
            }

            return var9;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
