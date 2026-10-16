package org.gtlreforged.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlobalRenderClock {

   private static long startTimeNanos = System.nanoTime();
   private static long totalPausedNanos = 0L;
   private static long pauseStartNanos = 0L;
   private static boolean wasPaused = false;

   private GlobalRenderClock() {
      throw new AssertionError();
   }

   private static long getElapsedNanos() {
      Minecraft minecraft = Minecraft.getInstance();
      boolean isPaused = minecraft.isPaused();
      if (isPaused && !wasPaused) {
         pauseStartNanos = System.nanoTime();
         wasPaused = true;
      } else if (!isPaused && wasPaused) {
         totalPausedNanos = totalPausedNanos + (System.nanoTime() - pauseStartNanos);
         wasPaused = false;
      }

      long currentNanos = isPaused ? pauseStartNanos : System.nanoTime();
      return currentNanos - startTimeNanos - totalPausedNanos;
   }

   public static float getSmoothTick() {
      return (float)(getElapsedNanos() / 5.0E7);
   }

   public static long getElapsedMillis() {
      return getElapsedNanos() / 1000000L;
   }

   public static void reset() {
      startTimeNanos = System.nanoTime();
      totalPausedNanos = 0L;
      pauseStartNanos = 0L;
      wasPaused = false;
   }
}
