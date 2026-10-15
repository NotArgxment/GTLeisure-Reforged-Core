package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FusionReactorMachine.class)
public abstract class FusionReactorMachineMixin extends WorkableElectricMultiblockMachine {
   @Shadow(remap = false)
   protected long heat = 0L;
   @Mutable
   @Final
   @Shadow(remap = false)
   protected final NotifiableEnergyContainer energyContainer;

   @Shadow(remap = false)
   protected void updatePreHeatSubscription() {
   }

   public FusionReactorMachineMixin(IMachineBlockEntity holder, NotifiableEnergyContainer energyContainer, Object... args) {
      super(holder, args);
      this.energyContainer = energyContainer;
   }

   @Overwrite(remap = false)
   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof FusionReactorMachineMixin fm && RecipeHelper.getRecipeEUtTier(recipe) <= fm.getTier() && recipe.data.m_128441_("eu_to_start")) {
         if (recipe.data.m_128454_("eu_to_start") <= fm.energyContainer.getEnergyCapacity()) {
            long heatDiff = recipe.data.m_128454_("eu_to_start") - fm.heat;
            if (heatDiff <= 0L) {
               return RecipeHelper.applyOverclock(new OverclockingLogic(0.5, 2.0, false), recipe, fm.getMaxVoltage(), params, result);
            }

            if (fm.energyContainer.getEnergyStored() < heatDiff) {
               return null;
            }

            fm.energyContainer.removeEnergy(heatDiff);
            fm.heat += heatDiff;
            fm.updatePreHeatSubscription();
            return RecipeHelper.applyOverclock(new OverclockingLogic(0.5, 2.0, false), recipe, fm.getMaxVoltage(), params, result);
         }

         RecipeResult.of(fm, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.enough.cache.energy")));
      }

      return null;
   }

   @Overwrite(remap = false)
   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         textList.add(
            Component.m_237110_(
               "gtceu.multiblock.fusion_reactor_energy",
               new Object[]{this.energyContainer.getEnergyStored() / 1000000L, this.energyContainer.getEnergyCapacity() / 1000000L}
            )
         );
         textList.add(Component.m_237110_("gtceu.multiblock.fusion_reactor.heat", new Object[]{this.heat}));
      }
   }
}
