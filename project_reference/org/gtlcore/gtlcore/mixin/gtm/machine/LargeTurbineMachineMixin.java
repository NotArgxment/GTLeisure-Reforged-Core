package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.generator.LargeTurbineMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LargeTurbineMachine.class)
public abstract class LargeTurbineMachineMixin extends WorkableElectricMultiblockMachine {
   RotorHolderPartMachine rotorHolderPart;

   public LargeTurbineMachineMixin(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Overwrite(remap = false)
   @Nullable
   private IRotorHolderMachine getRotorHolder() {
      return this.rotorHolderPart;
   }

   public boolean onWorking() {
      return this.rotorHolderPart != null && this.rotorHolderPart.onWorking(this) ? super.onWorking() : false;
   }

   public void onStructureFormed() {
      super.onStructureFormed();

      for (IMultiPart part : this.getParts()) {
         if (part instanceof RotorHolderPartMachine rotorHatchPart) {
            this.rotorHolderPart = rotorHatchPart;
         }
      }
   }
}
