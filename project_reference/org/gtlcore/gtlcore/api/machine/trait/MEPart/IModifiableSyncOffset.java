package org.gtlcore.gtlcore.api.machine.trait.MEPart;

public interface IModifiableSyncOffset {
   default int getOffset() {
      return 0;
   }

   default void setOffset(int offset) {
   }
}
