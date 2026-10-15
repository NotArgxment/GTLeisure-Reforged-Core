package org.gtlcore.gtlcore.mixin.gtm.api.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import org.gtlcore.gtlcore.api.capability.IInt128EnergyContainer;
import org.gtlcore.gtlcore.utils.datastructure.Int128;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IInt128EnergyContainer.class, prefix = "gTLCore$"))
@Mixin(NotifiableEnergyContainer.class)
public abstract class NotifiableEnergyContainerMixin extends NotifiableRecipeHandlerTrait<Long> {
   @Shadow(remap = false)
   protected long energyStored;
   @Shadow(remap = false)
   private long energyCapacity;
   @Unique
   protected Int128 gTLCore$lastEnergyInputPerSec = Int128.ZERO();
   @Unique
   protected Int128 gTLCore$lastEnergyOutputPerSec = Int128.ZERO();
   @Unique
   protected Int128 gTLCore$energyInputPerSec = Int128.ZERO();
   @Unique
   protected Int128 gTLCore$energyOutputPerSec = Int128.ZERO();

   @Shadow(remap = false)
   public void checkOutputSubscription() {
      throw new AssertionError();
   }

   public NotifiableEnergyContainerMixin(MetaMachine machine) {
      super(machine);
   }

   @Overwrite(remap = false)
   public void setEnergyStored(long energyStored) {
      if (this.energyStored != energyStored) {
         this.gTLCore$addEnergyPerSec(energyStored - this.energyStored);
         this.energyStored = energyStored;
         this.checkOutputSubscription();
         this.notifyListeners();
      }
   }

   @Overwrite(remap = false)
   public void updateTick() {
      if (this.getMachine().getOffsetTimer() % 20L == 0L) {
         this.gTLCore$lastEnergyOutputPerSec = this.gTLCore$energyOutputPerSec.copy();
         this.gTLCore$lastEnergyInputPerSec = this.gTLCore$energyOutputPerSec.copy();
         this.gTLCore$energyOutputPerSec.set(0L, 0L);
         this.gTLCore$energyOutputPerSec.set(0L, 0L);
      }
   }

   @Overwrite(remap = false)
   public long getInputPerSec() {
      return this.gTLCore$lastEnergyInputPerSec.longValue();
   }

   @Overwrite(remap = false)
   public long getOutputPerSec() {
      return this.gTLCore$lastEnergyOutputPerSec.longValue();
   }

   @Unique
   public Int128 gTLCore$getInt128InputPerSec() {
      return this.gTLCore$lastEnergyInputPerSec.copy();
   }

   @Unique
   public Int128 gTLCore$getInt128OutputPerSec() {
      return this.gTLCore$lastEnergyOutputPerSec.copy();
   }

   @Unique
   public void gTLCore$addEnergyPerSec(long energy) {
      if (energy > 0L) {
         this.gTLCore$energyInputPerSec.add(energy);
      } else if (energy < 0L) {
         this.gTLCore$energyOutputPerSec.add(-energy);
      }
   }

   @Unique
   public Int128 gTLCore$getInt128EnergyStored() {
      return new Int128(this.energyStored);
   }

   @Unique
   public Int128 gTLCore$getInt128EnergyCapacity() {
      return new Int128(this.energyCapacity);
   }
}
