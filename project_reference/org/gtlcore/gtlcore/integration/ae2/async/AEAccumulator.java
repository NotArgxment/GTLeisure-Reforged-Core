package org.gtlcore.gtlcore.integration.ae2.async;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public final class AEAccumulator {
   private final ConcurrentHashMap<AEKey, LongAdder> acc = new ConcurrentHashMap<>();

   public void add(AEKey key, long delta) {
      if (key != null && delta != 0L) {
         this.acc.computeIfAbsent(key, k -> new LongAdder()).add(delta);
      }
   }

   public void drainTo(Object2LongOpenHashMap<AEKey> buffer) {
      Iterator<Entry<AEKey, LongAdder>> it = this.acc.entrySet().iterator();

      while (it.hasNext()) {
         Entry<AEKey, LongAdder> e = it.next();
         long d = e.getValue().sumThenReset();
         if (d != 0L) {
            buffer.addTo(e.getKey(), d);
         }

         if (e.getValue().sum() == 0L) {
            it.remove();
         }
      }
   }

   public void clear() {
      this.acc.clear();
   }

   public boolean isEmpty() {
      return this.acc.isEmpty();
   }

   public ConcurrentHashMap<AEKey, LongAdder> getAcc() {
      return this.acc;
   }
}
