package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine;
import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;
import org.jetbrains.annotations.NotNull;

public class WorkableElectricMultipleRecipesMachine extends WorkableElectricMultiblockMachine implements ParallelMachine {
   public WorkableElectricMultipleRecipesMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @NotNull
   protected RecipeLogic createRecipeLogic(Object @NotNull ... args) {
      return new MultipleRecipesLogic(this);
   }

   @NotNull
   public MultipleRecipesLogic getRecipeLogic() {
      return (MultipleRecipesLogic)super.getRecipeLogic();
   }

   @Override
   public int getMaxParallel() {
      return 2147483647;
   }
}
