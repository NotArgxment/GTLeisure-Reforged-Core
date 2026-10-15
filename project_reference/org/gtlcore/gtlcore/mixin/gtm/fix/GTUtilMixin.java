package org.gtlcore.gtlcore.mixin.gtm.fix;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.utils.GTUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTUtil.class)
public abstract class GTUtilMixin {
   @Overwrite(remap = false)
   public static byte getFloorTierByVoltage(long voltage) {
      return (byte)Math.max(0, GTUtil.nearestLesserOrEqual(GTValues.V, voltage));
   }
}
