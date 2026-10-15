package org.gtlcore.gtlcore.mixin.meRequester;

import com.almostreliable.merequester.requester.Requests.Request;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Request.class)
public abstract class RequestMixin {
   @Shadow(remap = false)
   private long batch;

   @Inject(
      method = "updateBatch",
      at = @At(value = "FIELD", target = "Lcom/almostreliable/merequester/requester/Requests$Request;batch:J", opcode = 181, shift = Shift.AFTER),
      remap = false
   )
   private void reassignBatchAfterPut(long batch, CallbackInfo ci) {
      this.batch = Math.max(1L, batch);
   }
}
