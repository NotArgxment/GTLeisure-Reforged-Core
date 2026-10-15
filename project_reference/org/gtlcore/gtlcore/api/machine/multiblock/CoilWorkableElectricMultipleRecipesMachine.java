package org.gtlcore.gtlcore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import java.util.function.BiPredicate;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.machine.trait.MultipleRecipesLogic;
import org.jetbrains.annotations.NotNull;

public class CoilWorkableElectricMultipleRecipesMachine extends CoilWorkableElectricMultiblockMachine implements ParallelMachine {
   protected static final BiPredicate<CompoundTag, IRecipeLogicMachine> EBF_CHECK = (data, machine) -> {
      CoilWorkableElectricMultiblockMachine tm = (CoilWorkableElectricMultiblockMachine)machine;
      long temp = tm.getCoilType().getCoilTemperature() + 100L * Math.max(0, tm.getTier() - 2);
      if (temp > data.m_128451_("ebf_temp")) {
         return true;
      }

      RecipeResult.of(machine, RecipeResult.FAIL_NO_ENOUGH_TEMPERATURE);
      return false;
   };

   public CoilWorkableElectricMultipleRecipesMachine(IMachineBlockEntity holder, Object @NotNull ... args) {
      super(holder);
      if (args.length == 2 && args[0] instanceof Number reductionEUt && args[1] instanceof Number reductionDuration) {
         this.getRecipeLogic().setReduction(reductionEUt.doubleValue(), reductionDuration.doubleValue());
      }
   }

   @NotNull
   protected RecipeLogic createRecipeLogic(Object @NotNull ... args) {
      return new MultipleRecipesLogic(this, EBF_CHECK);
   }

   @NotNull
   public MultipleRecipesLogic getRecipeLogic() {
      return (MultipleRecipesLogic)super.getRecipeLogic();
   }

   @Override
   public int getMaxParallel() {
      return GTLRecipeModifiers.getHatchParallel(this);
   }
}
