package org.gtlcore.gtlcore.api.machine;

public interface IPerformanceDisplayMachine extends IServerTickMachine {
   int gtlcore$getTickTime();

   void gtlcore$observe();
}
