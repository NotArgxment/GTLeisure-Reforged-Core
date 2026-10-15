package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import org.gtlcore.gtlcore.api.recipe.IAdvancedOCResult;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverclockingLogic.class)
public abstract class OverclockingLogicMixin {
   @Overwrite(remap = false)
   public static void heatingCoilOC(@NotNull OCParams params, @NotNull OCResult result, long maxVoltage, int providedTemp, int requiredTemp) {
      double duration = params.getDuration() * Math.max(0.5, (double)requiredTemp / providedTemp);
      double eut = params.getEut();
      int ocAmount = params.getOcAmount();
      double parallel = 1.0;
      boolean shouldParallel = false;
      int baseOCLevel = 0;
      double vfPowParallel = 1.0;

      int ocLevel;
      for (ocLevel = 0; ocAmount-- > 0; ocLevel++) {
         double potentialVoltage = eut * 4.0;
         if (potentialVoltage > maxVoltage) {
            break;
         }

         eut = potentialVoltage;
         if (shouldParallel) {
            parallel *= 4.0;
            vfPowParallel *= 4.0;
         } else {
            double potentialDuration = duration * 0.25;
            if (potentialDuration < 1.0) {
               parallel *= 4.0;
               vfPowParallel *= 4.0;
               shouldParallel = true;
            } else {
               duration = potentialDuration;
               baseOCLevel++;
            }
         }
      }

      eut *= Math.min(1.0, NumberUtils.pow95(Math.max(0, (providedTemp - requiredTemp) / 900)));
      ((IAdvancedOCResult)result).init((long)(eut / vfPowParallel), (int)duration, (int)parallel, (long)eut, baseOCLevel, ocLevel, 0.25, 4.0);
   }

   @Inject(method = "getOverclockForTier", at = @At("HEAD"), remap = false, cancellable = true)
   protected void getOverclockForTier(long voltage, CallbackInfoReturnable<Integer> cir) {
      cir.setReturnValue(NumberUtils.getFakeVoltageTier(voltage));
   }

   @Overwrite(remap = false)
   public static void subTickParallelOC(@NotNull OCParams params, @NotNull OCResult result, long maxVoltage, double durationFactor, double voltageFactor) {
      double duration = params.getDuration();
      double eut = params.getEut();
      int ocAmount = params.getOcAmount();
      double parallel = 1.0;
      boolean shouldParallel = false;
      int baseOCLevel = 0;
      double vfPowParallel = 1.0;

      int ocLevel;
      for (ocLevel = 0; ocAmount-- > 0; ocLevel++) {
         double potentialVoltage = eut * voltageFactor;
         if (potentialVoltage > maxVoltage) {
            break;
         }

         eut = potentialVoltage;
         if (shouldParallel) {
            parallel /= durationFactor;
            vfPowParallel *= voltageFactor;
         } else {
            double potentialDuration = duration * durationFactor;
            if (potentialDuration < 1.0) {
               parallel /= durationFactor;
               vfPowParallel *= voltageFactor;
               shouldParallel = true;
            } else {
               duration = potentialDuration;
               baseOCLevel++;
            }
         }
      }

      ((IAdvancedOCResult)result)
         .init((long)(eut / vfPowParallel), (int)duration, (int)parallel, (long)eut, baseOCLevel, ocLevel, durationFactor, voltageFactor);
   }
}
