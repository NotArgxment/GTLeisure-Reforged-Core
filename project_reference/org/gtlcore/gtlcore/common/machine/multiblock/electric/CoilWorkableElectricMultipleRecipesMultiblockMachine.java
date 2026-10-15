package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.block.CoilBlock.CoilType;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CoilWorkableElectricMultipleRecipesMultiblockMachine extends WorkableElectricMultipleRecipesMachine {
   private ICoilType coilType = CoilType.CUPRONICKEL;

   public CoilWorkableElectricMultipleRecipesMultiblockMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (this.getMultiblockState().getMatchContext().get("CoilType") instanceof ICoilType coil) {
         this.coilType = coil;
      }
   }

   public int getCoilTier() {
      return this.coilType.getTier();
   }

   @Override
   public int getMaxParallel() {
      return Math.min(2147483647, (int)Math.pow(2.0, this.getCoilType().getCoilTemperature() / 900.0));
   }

   public ICoilType getCoilType() {
      return this.coilType;
   }
}
