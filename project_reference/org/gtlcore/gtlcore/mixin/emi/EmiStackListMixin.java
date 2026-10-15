package org.gtlcore.gtlcore.mixin.emi;

import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiStackList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiStackList.class)
public class EmiStackListMixin {
   @Inject(
      method = "reload",
      at = @At(
         value = "INVOKE_ASSIGN",
         target = "Ldev/emi/emi/api/stack/EmiStack;of(Lnet/minecraft/world/level/material/Fluid;)Ldev/emi/emi/api/stack/EmiStack;"
      ),
      remap = false
   )
   private static void mixin$reload(CallbackInfo ci, @Local(name = "fs") EmiStack fs) {
      fs.setAmount(1L);
   }
}
