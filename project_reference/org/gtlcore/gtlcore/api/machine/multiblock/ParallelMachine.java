package org.gtlcore.gtlcore.api.machine.multiblock;

public interface ParallelMachine {
   int getMaxParallel();

   default boolean needConfirmMEStock() {
      return true;
   }
}
