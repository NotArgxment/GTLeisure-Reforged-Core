package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.pattern.util.IValueContainer;
import org.jetbrains.annotations.NotNull;

public class AdvancedAssemblyLineMachine extends WorkableElectricMultiblockMachine {
   public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      AdvancedAssemblyLineMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   private int speed = 0;

   public AdvancedAssemblyLineMachine(IMachineBlockEntity holder) {
      super(holder, new Object[0]);
   }

   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, OCParams params, OCResult result) {
      if (machine instanceof AdvancedAssemblyLineMachine lineMachine) {
         GTRecipe recipe1 = GTRecipeModifiers.hatchParallel(machine, recipe, false, params, result);
         if (recipe1 == null) {
            return null;
         }

         recipe1.duration = Math.max(lineMachine.speed * recipe1.duration / 100, 1);
         return RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK, recipe1, lineMachine.getOverclockVoltage(), params, result);
      } else {
         return null;
      }
   }

   public void onStructureFormed() {
      IValueContainer<?> container = (IValueContainer<?>)this.getMultiblockState().getMatchContext().getOrCreate("UnitValue", IValueContainer::noop);
      if (container.getValue() instanceof Integer integer) {
         this.speed = 100 - integer;
      }

      super.onStructureFormed();
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{this.speed}).m_130946_("%"));
      }
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }
}
