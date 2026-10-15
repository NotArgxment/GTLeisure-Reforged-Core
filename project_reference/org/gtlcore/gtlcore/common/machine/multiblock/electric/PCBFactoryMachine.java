package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import java.util.List;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PCBFactoryMachine extends StorageMachine {
   private double reductionEUt = 1.0;
   private double reductionDuration = 1.0;

   public PCBFactoryMachine(IMachineBlockEntity holder) {
      super(holder, 64);
   }

   private void getPCBReduction() {
      ItemStack itemStack = this.getMachineStorageItem();
      String item = Registries.getItemId(itemStack);
      if (Objects.equals(item, "gtceu:vibranium_nanoswarm")) {
         this.reductionDuration = (100 - itemStack.m_41613_()) / 100.0;
         this.reductionEUt = 0.25;
      } else if (Objects.equals(item, "gtceu:gold_nanoswarm")) {
         this.reductionDuration = (100.0 - itemStack.m_41613_() * 0.5) / 100.0;
      }
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof PCBFactoryMachine pcbFactoryMachine) {
         pcbFactoryMachine.getPCBReduction();
         GTRecipe recipe1 = GTLRecipeModifiers.reduction(pcbFactoryMachine, recipe, pcbFactoryMachine.reductionEUt, pcbFactoryMachine.reductionDuration);
         if (recipe1 != null) {
            recipe1 = GTRecipeModifiers.hatchParallel(machine, recipe1, false, params, result);
            if (recipe1 != null) {
               return RecipeHelper.applyOverclock(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK, recipe1, pcbFactoryMachine.getOverclockVoltage(), params, result);
            }
         }
      }

      return null;
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         if (this.getOffsetTimer() % 10L == 0L) {
            this.getPCBReduction();
         }

         textList.add(Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{this.reductionEUt}));
         textList.add(Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{this.reductionDuration}));
      }
   }
}
