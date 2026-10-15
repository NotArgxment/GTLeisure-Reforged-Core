package org.gtlcore.gtlcore.mixin.gtm.api.misc;

import com.gregtechceu.gtceu.api.capability.ILaserContainer;
import com.gregtechceu.gtceu.api.misc.LaserContainerList;
import java.util.List;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LaserContainerList.class)
public abstract class LaserContainerListMixin {
   @Shadow(remap = false)
   @Final
   private List<? extends ILaserContainer> energyContainerList;

   @Overwrite(remap = false)
   public long getInputVoltage() {
      long inputVoltage = 0L;

      for (ILaserContainer container : this.energyContainerList) {
         inputVoltage = NumberUtils.saturatedAdd(inputVoltage, NumberUtils.saturatedMultiply(container.getInputVoltage(), container.getInputAmperage()));
      }

      return inputVoltage;
   }

   @Overwrite(remap = false)
   public long getOutputVoltage() {
      long outputVoltage = 0L;

      for (ILaserContainer container : this.energyContainerList) {
         outputVoltage = NumberUtils.saturatedAdd(outputVoltage, NumberUtils.saturatedMultiply(container.getOutputVoltage(), container.getOutputAmperage()));
      }

      return outputVoltage;
   }
}
