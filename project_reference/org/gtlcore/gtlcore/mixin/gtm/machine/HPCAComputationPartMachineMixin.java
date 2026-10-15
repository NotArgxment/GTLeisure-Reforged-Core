package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComputationPartMachine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HPCAComputationPartMachine.class)
public abstract class HPCAComputationPartMachineMixin extends HPCAComponentPartMachine {
   @Shadow(remap = false)
   @Final
   private boolean advanced;

   public HPCAComputationPartMachineMixin(IMachineBlockEntity holder) {
      super(holder);
   }

   @Overwrite(remap = false)
   public int getCWUPerTick() {
      if (this.isDamaged()) {
         return 0;
      } else {
         return this.advanced ? 32 : 8;
      }
   }
}
