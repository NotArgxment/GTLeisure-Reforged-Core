package org.gtlcore.gtlcore.mixin.ae2.stacks;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "appeng.api.stacks.AEKey2LongMap$OpenHashMap", remap = false)
public abstract class AEKey2LongMapMixin extends Object2LongOpenHashMap<AEKey> {
   public long addTo(AEKey key, long incr) {
      long oldValue = this.getLong(key);
      if (incr == 0L) {
         return oldValue;
      }

      long newValue = oldValue + incr;
      if (((oldValue ^ newValue) & (incr ^ newValue)) < 0L) {
         newValue = incr > 0L ? 9223372036854775807L : -9223372036854775808L;
      }

      this.put(key, newValue);
      return oldValue;
   }
}
