package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import org.gtlcore.gtlcore.api.recipe.IAdvancedOCResult;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IAdvancedOCResult.class, prefix = "gTLCore$"))
@Mixin(OCResult.class)
public abstract class OCResultMixin {
   @Shadow(remap = false)
   private long eut;
   @Shadow(remap = false)
   private long parallelEUt;
   @Shadow(remap = false)
   private int duration;
   @Shadow(remap = false)
   private int parallel;
   @Shadow(remap = false)
   private int ocLevel;
   @Unique
   private int gTLCore$baseOCLevel;
   @Unique
   private double gTLCore$durationFactor = 0.0;
   @Unique
   private double gTLCore$voltageFactor = 0.0;

   @Overwrite(remap = false)
   public void init(long eut, int duration, int parallel, long parallelEUt, int ocLevel) {
      this.eut = eut;
      this.duration = duration;
      this.parallel = parallel;
      this.parallelEUt = parallelEUt;
      this.ocLevel = ocLevel;
      this.gTLCore$baseOCLevel = ocLevel;
      this.gTLCore$durationFactor = 0.0;
      this.gTLCore$voltageFactor = 0.0;
   }

   @Unique
   public void gTLCore$init(
      long eut, int duration, int parallel, long parallelEUt, int baseOCLevel, int totalOCLevel, double durationFactor, double voltageFactor
   ) {
      this.eut = eut;
      this.duration = duration;
      this.parallel = parallel;
      this.parallelEUt = parallelEUt;
      this.ocLevel = totalOCLevel;
      this.gTLCore$baseOCLevel = baseOCLevel;
      this.gTLCore$durationFactor = durationFactor;
      this.gTLCore$voltageFactor = voltageFactor;
   }

   @Unique
   public int gTLCore$getBaseOCLevel() {
      return this.gTLCore$baseOCLevel;
   }

   @Unique
   public double gTLCore$getDurationFactor() {
      return this.gTLCore$durationFactor;
   }

   @Unique
   public double gTLCore$getVoltageFactor() {
      return this.gTLCore$voltageFactor;
   }
}
