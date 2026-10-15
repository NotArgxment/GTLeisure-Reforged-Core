package org.gtlcore.gtlcore.mixin.gtm.api.misc;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import java.util.List;
import org.gtlcore.gtlcore.api.capability.IInt128EnergyContainer;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.datastructure.Int128;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Implements(@Interface(iface = IInt128EnergyContainer.class, prefix = "gTLCore$"))
@Mixin(EnergyContainerList.class)
public abstract class EnergyContainerListMixin {
   @Shadow(remap = false)
   @Final
   private List<? extends IEnergyContainer> energyContainerList;
   @Mutable
   @Shadow(remap = false)
   @Final
   private long inputVoltage;
   @Mutable
   @Shadow(remap = false)
   @Final
   private long outputVoltage;
   @Mutable
   @Shadow(remap = false)
   @Final
   private long inputAmperage;
   @Mutable
   @Shadow(remap = false)
   @Final
   private long outputAmperage;

   @Inject(method = "<init>", at = @At("TAIL"))
   public void EnergyContainerList(List<? extends IEnergyContainer> energyContainerList, CallbackInfo ci) {
      long totalInputEut = 0L;
      long totalOutputEut = 0L;

      for (IEnergyContainer container : energyContainerList) {
         totalInputEut = NumberUtils.saturatedAdd(totalInputEut, NumberUtils.saturatedMultiply(container.getInputVoltage(), container.getInputAmperage()));
         totalOutputEut = NumberUtils.saturatedAdd(totalOutputEut, NumberUtils.saturatedMultiply(container.getOutputVoltage(), container.getOutputAmperage()));
      }

      this.inputVoltage = totalInputEut;
      this.inputAmperage = totalInputEut > 0L ? 1L : 0L;
      this.outputVoltage = totalOutputEut;
      this.outputAmperage = totalOutputEut > 0L ? 1L : 0L;
   }

   @Overwrite(remap = false)
   public long getEnergyStored() {
      long energyStored = 0L;

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         energyStored = NumberUtils.saturatedAdd(energyStored, iEnergyContainer.getEnergyStored());
      }

      return energyStored;
   }

   @Overwrite(remap = false)
   public long getEnergyCapacity() {
      long energyCapacity = 0L;

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         energyCapacity = NumberUtils.saturatedAdd(energyCapacity, iEnergyContainer.getEnergyCapacity());
      }

      return energyCapacity;
   }

   @Overwrite(remap = false)
   public long getInputPerSec() {
      long sum = 0L;

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum = NumberUtils.saturatedAdd(sum, iEnergyContainer.getInputPerSec());
      }

      return sum;
   }

   @Overwrite(remap = false)
   public long getOutputPerSec() {
      long sum = 0L;

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum = NumberUtils.saturatedAdd(sum, iEnergyContainer.getOutputPerSec());
      }

      return sum;
   }

   @Overwrite(remap = false)
   @NotNull
   private static long[] calculateVoltageAmperage(long voltage, long amperage) {
      return new long[]{voltage, amperage};
   }

   @Overwrite(remap = false)
   private static boolean hasPrimeFactorGreaterThanTwo(long l) {
      return l > 0L && (l & l - 1L) != 0L;
   }

   @Unique
   public Int128 gTLCore$getInt128InputPerSec() {
      Int128 sum = Int128.ZERO();

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum.add(((IInt128EnergyContainer)iEnergyContainer).getInt128InputPerSec());
      }

      return sum;
   }

   @Unique
   public Int128 gTLCore$getInt128OutputPerSec() {
      Int128 sum = Int128.ZERO();

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum.add(((IInt128EnergyContainer)iEnergyContainer).getInt128OutputPerSec());
      }

      return sum;
   }

   @Unique
   public Int128 gTLCore$getInt128EnergyStored() {
      Int128 sum = Int128.ZERO();

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum.add(iEnergyContainer.getEnergyStored());
      }

      return sum;
   }

   @Unique
   public Int128 gTLCore$getInt128EnergyCapacity() {
      Int128 sum = Int128.ZERO();

      for (IEnergyContainer iEnergyContainer : this.energyContainerList) {
         sum.add(iEnergyContainer.getEnergyCapacity());
      }

      return sum;
   }
}
