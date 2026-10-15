package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.gtlcore.gtlcore.api.recipe.IAdvancedContentModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ContentModifier.class)
public abstract class ContentModifierMixin implements IAdvancedContentModifier {
   @Unique
   private long numerator = -1L;
   @Unique
   private long denominator = -1L;
   @Unique
   private boolean useFraction;
   @Shadow(remap = false)
   @Final
   private double multiplier;
   @Shadow(remap = false)
   @Final
   private double addition;

   @Overwrite(remap = false)
   public Number apply(Number number) {
      if (number instanceof Long l) {
         return this.useFraction ? l / this.denominator * this.numerator : number.doubleValue() * this.multiplier + this.addition;
      } else if (number instanceof BigDecimal decimal) {
         return decimal.multiply(BigDecimal.valueOf(this.multiplier)).add(BigDecimal.valueOf(this.addition));
      } else {
         return number instanceof BigInteger bigInteger
            ? bigInteger.multiply(BigInteger.valueOf((long)this.multiplier)).add(BigInteger.valueOf((long)this.addition))
            : number.doubleValue() * this.multiplier + this.addition;
      }
   }

   @Unique
   @Override
   public void setDivision(long numerator, long denominator) {
      this.numerator = numerator;
      this.denominator = denominator;
      this.useFraction = true;
   }
}
