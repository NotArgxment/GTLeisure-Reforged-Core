package org.gtlcore.gtlcore.api.recipe;

public interface IAdvancedOCResult {
   void init(long var1, int var3, int var4, long var5, int var7, int var8, double var9, double var11);

   int getBaseOCLevel();

   double getDurationFactor();

   double getVoltageFactor();
}
