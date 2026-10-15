package org.gtlcore.gtlcore.mixin.gtmt;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine;
import com.gregtechceu.gtceu.common.machine.electric.HullMachine;
import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.common.cover.WirelessEnergyReceiveCover;
import java.util.UUID;
import net.minecraft.core.Direction;
import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WirelessEnergyReceiveCover.class)
public abstract class WirelessEnergyReceiveCoverMixin extends CoverBehavior {
   @Shadow(remap = false)
   @Final
   private int tier;
   @Shadow(remap = false)
   private UUID uuid;
   @Shadow(remap = false)
   @Final
   private int amperage;
   @Shadow(remap = false)
   @Final
   private long energyPerTick;
   @Shadow(remap = false)
   private long machineMaxEnergy;

   @Shadow(remap = false)
   protected abstract void updateCoverSub();

   public WirelessEnergyReceiveCoverMixin(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
      super(definition, coverHolder, attachedSide);
   }

   @Inject(method = "canAttach", at = @At("HEAD"), remap = false, cancellable = true)
   public void canAttach(CallbackInfoReturnable<Boolean> cir) {
      if (MetaMachine.getMachine(this.coverHolder.getLevel(), this.coverHolder.getPos()) instanceof NeutronAcceleratorPartMachine neutronAcceleratorPartMachine
         )
       {
         cir.setReturnValue(neutronAcceleratorPartMachine.getTier() >= this.tier);
      }
   }

   @Overwrite(remap = false)
   private void updateEnergy() {
      if (this.uuid != null) {
         IEnergyContainer energyContainer = GTCapabilityHelper.getEnergyContainer(this.coverHolder.getLevel(), this.coverHolder.getPos(), this.attachedSide);
         if (energyContainer != null) {
            MetaMachine machine = MetaMachine.getMachine(this.coverHolder.getLevel(), this.coverHolder.getPos());
            if (machine instanceof BatteryBufferMachine
               || machine instanceof HullMachine
               || machine instanceof NeutronAcceleratorPartMachine neutronAcceleratorPartMachine && neutronAcceleratorPartMachine.isWorkingEnabled()) {
               long changeStored = Math.min(energyContainer.getEnergyCapacity() - energyContainer.getEnergyStored(), this.energyPerTick);
               if (changeStored <= 0L) {
                  return;
               }

               if (!WirelessEnergyManager.addEUToGlobalEnergyMap(this.uuid, -changeStored, machine)) {
                  return;
               }

               energyContainer.acceptEnergyFromNetwork(null, GTValues.V[this.tier], this.amperage);
            } else {
               long changeStored = Math.min(this.machineMaxEnergy - energyContainer.getEnergyStored(), this.energyPerTick);
               if (changeStored <= 0L) {
                  return;
               }

               if (!WirelessEnergyManager.addEUToGlobalEnergyMap(this.uuid, -changeStored, machine)) {
                  return;
               }

               energyContainer.addEnergy(changeStored);
            }
         }

         this.updateCoverSub();
      }
   }
}
