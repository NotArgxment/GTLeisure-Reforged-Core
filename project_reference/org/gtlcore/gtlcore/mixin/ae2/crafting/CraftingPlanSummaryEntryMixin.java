package org.gtlcore.gtlcore.mixin.ae2.crafting;

import appeng.menu.me.crafting.CraftingPlanSummaryEntry;
import net.minecraft.network.FriendlyByteBuf;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingPlanSummaryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftingPlanSummaryEntry.class)
public class CraftingPlanSummaryEntryMixin implements ICraftingPlanSummaryEntry {
   @Unique
   private long gtlcore$craftTimes = 0L;

   @Override
   public long gtlcore$getCraftTimes() {
      return this.gtlcore$craftTimes;
   }

   @Override
   public void gtlcore$setCraftTimes(long craftTimes) {
      this.gtlcore$craftTimes = craftTimes;
   }

   @Inject(at = @At("TAIL"), method = "write", remap = false)
   private void write(FriendlyByteBuf buffer, CallbackInfo ci) {
      buffer.m_130103_(this.gtlcore$craftTimes);
   }

   @Inject(at = @At("TAIL"), method = "read", cancellable = true, remap = false)
   private static void read(FriendlyByteBuf buffer, CallbackInfoReturnable<CraftingPlanSummaryEntry> cir) {
      CraftingPlanSummaryEntry entry = (CraftingPlanSummaryEntry)cir.getReturnValue();
      ((ICraftingPlanSummaryEntry)entry).gtlcore$setCraftTimes(buffer.m_130258_());
      cir.setReturnValue(entry);
   }
}
