package org.gtlcore.gtlcore.mixin.ae2;

import appeng.util.CowMap;
import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CowMap.class)
public abstract class MixinCowMap<K, V> {
   @Shadow(remap = false)
   private volatile Map<K, V> map;

   @Inject(method = "putIfAbsent", at = @At("HEAD"), remap = false, cancellable = true)
   private void putIfAbsent(K key, V value, CallbackInfo ci) {
      synchronized (this) {
         if (this.map.containsKey(key)) {
            ci.cancel();
         }
      }
   }
}
