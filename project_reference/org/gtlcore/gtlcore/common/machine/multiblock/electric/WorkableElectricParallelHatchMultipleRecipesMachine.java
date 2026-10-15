package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;

public class WorkableElectricParallelHatchMultipleRecipesMachine extends WorkableElectricMultipleRecipesMachine {
   public WorkableElectricParallelHatchMultipleRecipesMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Override
   public int getMaxParallel() {
      return GTLRecipeModifiers.getHatchParallel(this);
   }
}
