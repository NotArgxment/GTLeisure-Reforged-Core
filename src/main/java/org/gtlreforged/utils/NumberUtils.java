package org.gtlreforged.utils;

import com.google.common.math.LongMath;
import java.math.BigInteger;
import java.text.DecimalFormat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class NumberUtils {
   private static final double LN_095 = Math.log(0.95);
   private static final double[] TABLE = new double[128];
   public static final String[] UNITS = new String[]{"", "K", "M", "G", "T", "P", "E", "Z", "Y", "B", "N", "D"};
   public static final int[] NEAREST = new int[]{1, 2, 4, 4, 4, 8, 8, 8, 8, 8, 8, 16, 16, 16, 16, 16};
   public static final BigInteger BIG_INTEGER_MAX_LONG = BigInteger.valueOf(9223372036854775807L);

   public static double pow95(int n) {
      return n < TABLE.length ? TABLE[n] : Math.exp(n * LN_095);
   }

   public static String formatLong(long number) {
      DecimalFormat df = new DecimalFormat("#.##");
      double temp = number;

      int unitIndex;
      for (unitIndex = 0; temp >= 1000.0 && unitIndex < UNITS.length - 1; unitIndex++) {
         temp /= 1000.0;
      }

      return df.format(temp) + UNITS[unitIndex];
   }

   public static String formatDouble(double number) {
      DecimalFormat df = new DecimalFormat("#.##");
      double temp = number;

      int unitIndex;
      for (unitIndex = 0; temp >= 1000.0 && unitIndex < UNITS.length - 1; unitIndex++) {
         temp /= 1000.0;
      }

      return df.format(temp) + UNITS[unitIndex];
   }

   public static MutableComponent numberText(double number) {
      return Component.m_237113_(formatDouble(number));
   }

   public static MutableComponent numberText(long number) {
      return Component.m_237113_(formatLong(number));
   }

   public static long getLongValue(BigInteger bigInt) {
      return bigInt.compareTo(BIG_INTEGER_MAX_LONG) > 0 ? 9223372036854775807L : bigInt.longValue();
   }

   public static int getFakeVoltageTier(long voltage) {
      return voltage < 32L ? 0 : 1 + (63 - Long.numberOfLeadingZeros(voltage >> 5) >> 1);
   }

   public static long getVoltageFromFakeTier(int tier) {
      return LongMath.pow(4L, tier + 1) * 2L;
   }

   public static int nearestPow2Lookup(int x) {
      if (x < 1) {
         return 1;
      } else {
         return x > 16 ? 16 : NEAREST[x - 1];
      }
   }

   public static int getAdditionalTier(double durationFactor, double actualConsumeParallel) {
      return (int)Math.ceil(Math.log(actualConsumeParallel) / -Math.log(durationFactor));
   }

   public static long saturatedAdd(long a, long b) {
      long naiveSum = a + b;
      return (a ^ b) < 0L | (a ^ naiveSum) >= 0L ? naiveSum : 9223372036854775807L + (naiveSum >>> 63 ^ 1L);
   }

   public static long saturatedMultiply(long a, long b) {
      int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
      if (leadingZeros > 65) {
         return a * b;
      }

      long limit = 9223372036854775807L + ((a ^ b) >>> 63);
      if (leadingZeros < 64 | a < 0L & b == -9223372036854775808L) {
         return limit;
      }

      long result = a * b;
      return a != 0L && result / a != b ? limit : result;
   }

   static {
      double p = 1.0;

      for (int i = 0; i < TABLE.length; i++) {
         TABLE[i] = p;
         p *= 0.95;
      }
   }
}
