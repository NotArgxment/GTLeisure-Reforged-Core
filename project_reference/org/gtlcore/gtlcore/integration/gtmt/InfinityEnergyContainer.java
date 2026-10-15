package org.gtlcore.gtlcore.integration.gtmt;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.List;
import net.minecraft.core.Direction;
import org.gtlcore.gtlcore.api.capability.IInt128EnergyContainer;
import org.jetbrains.annotations.Nullable;

public class InfinityEnergyContainer extends NotifiableEnergyContainer implements IInt128EnergyContainer {
   public InfinityEnergyContainer(
      MetaMachine machine, long maxCapacity, long maxInputVoltage, long maxInputAmperage, long maxOutputVoltage, long maxOutputAmperage
   ) {
      super(machine, maxCapacity, maxInputVoltage, maxInputAmperage, maxOutputVoltage, maxOutputAmperage);
   }

   public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, @Nullable String slotName, boolean simulate) {
      return null;
   }

   public long changeEnergy(long energyToAdd) {
      long oldEnergyStored = this.getEnergyStored();
      long newEnergyStored = this.getEnergyCapacity() - oldEnergyStored < energyToAdd ? this.getEnergyCapacity() : oldEnergyStored + energyToAdd;
      if (newEnergyStored < 0L) {
         newEnergyStored = 0L;
      }

      long change = newEnergyStored - oldEnergyStored;
      this.addEnergyPerSec(change);
      return change;
   }

   public void checkOutputSubscription() {
   }

   public void serverTick() {
   }

   public long acceptEnergyFromNetwork(Direction side, long voltage, long amperage) {
      return 0L;
   }

   public boolean outputsEnergy(Direction side) {
      return false;
   }

   public boolean inputsEnergy(Direction side) {
      return false;
   }

   public long getEnergyStored() {
      return this.getEnergyCapacity();
   }
}
