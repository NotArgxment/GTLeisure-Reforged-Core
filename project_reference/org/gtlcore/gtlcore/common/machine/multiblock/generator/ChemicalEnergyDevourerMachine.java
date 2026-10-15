package org.gtlcore.gtlcore.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyTooltip.Basic;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChemicalEnergyDevourerMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {
   private static final FluidStack DINITROGEN_TETROXIDE_STACK = GTMaterials.DinitrogenTetroxide.getFluid(80L);
   private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 120L);
   private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(2L);
   private final int tier = 5;
   private boolean isOxygenBoosted = false;
   private boolean isDinitrogenTetroxideBoosted = false;

   public ChemicalEnergyDevourerMachine(IMachineBlockEntity holder) {
      super(holder, new Object[0]);
   }

   private boolean isIntakesObstructed() {
      Direction facing = this.getFrontFacing();
      boolean permuteXZ = facing.m_122434_() == Axis.Z;
      BlockPos centerPos = this.getPos().m_121945_(facing);

      for (int x = -3; x < 4; x++) {
         for (int y = -3; y < 4; y++) {
            if (x != 0 || y != 0) {
               BlockPos blockPos = centerPos.m_7918_(permuteXZ ? x : 0, y, permuteXZ ? 0 : x);
               BlockState blockState = this.getLevel().m_8055_(blockPos);
               if (!blockState.m_60795_()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isBoostAllowed() {
      return this.getMaxVoltage() >= GTValues.V[this.getTier() + 3];
   }

   public long getOverclockVoltage() {
      if (this.isOxygenBoosted && this.isDinitrogenTetroxideBoosted) {
         return GTValues.V[5] * 128L;
      } else {
         return this.isOxygenBoosted ? GTValues.V[5] * 64L : GTValues.V[5] * 32L;
      }
   }

   protected GTRecipe getLubricantRecipe() {
      return GTRecipeBuilder.ofRaw().inputFluids(LUBRICANT_STACK).buildRawRecipe();
   }

   protected GTRecipe getBoostRecipe() {
      return GTRecipeBuilder.ofRaw().inputFluids(LIQUID_OXYGEN_STACK).buildRawRecipe();
   }

   protected GTRecipe getBoostRecipea() {
      return GTRecipeBuilder.ofRaw().inputFluids(DINITROGEN_TETROXIDE_STACK).buildRawRecipe();
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof ChemicalEnergyDevourerMachine engineMachine) {
         long EUt = RecipeHelper.getOutputEUt(recipe);
         if (EUt > 0L && RecipeRunnerHelper.matchRecipe(engineMachine, engineMachine.getLubricantRecipe()) && !engineMachine.isIntakesObstructed()) {
            int maxParallel = (int)(engineMachine.getOverclockVoltage() / EUt);
            Pair<GTRecipe, Integer> parallelResult = GTRecipeModifiers.fastParallel(engineMachine, recipe, maxParallel, false);
            if (engineMachine.isOxygenBoosted && engineMachine.isDinitrogenTetroxideBoosted) {
               recipe = parallelResult.getFirst() == recipe ? recipe.copy() : (GTRecipe)parallelResult.getFirst();
               long eut = EUt * ((Integer)parallelResult.getSecond()).intValue() * 4L;
               recipe.tickOutputs
                  .put(EURecipeCapability.CAP, List.of(new Content(eut, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            } else if (engineMachine.isOxygenBoosted) {
               recipe = parallelResult.getFirst() == recipe ? recipe.copy() : (GTRecipe)parallelResult.getFirst();
               long eut = EUt * ((Integer)parallelResult.getSecond()).intValue() * 2L;
               recipe.tickOutputs
                  .put(EURecipeCapability.CAP, List.of(new Content(eut, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
            } else {
               recipe = (GTRecipe)parallelResult.getFirst();
            }

            return recipe;
         }
      }

      return null;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      long totalContinuousRunningTime = this.recipeLogic.getTotalContinuousRunningTime();
      if ((totalContinuousRunningTime == 1L || totalContinuousRunningTime % 72L == 0L)
         && !RecipeRunnerHelper.handleRecipeInput(this, this.getLubricantRecipe())) {
         this.recipeLogic.interruptRecipe();
         return false;
      }

      if ((totalContinuousRunningTime == 1L || totalContinuousRunningTime % 20L == 0L) && this.isBoostAllowed()) {
         GTRecipe boosterRecipe = this.getBoostRecipe();
         GTRecipe boosterRecipea = this.getBoostRecipea();
         this.isOxygenBoosted = RecipeRunnerHelper.matchRecipe(this, boosterRecipe) && RecipeRunnerHelper.handleRecipeInput(this, boosterRecipe);
         this.isDinitrogenTetroxideBoosted = RecipeRunnerHelper.matchRecipe(this, boosterRecipea) && RecipeRunnerHelper.handleRecipeInput(this, boosterRecipea);
      }

      return value;
   }

   public boolean dampingWhenWaiting() {
      return false;
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         if (this.isBoostAllowed()) {
            if (this.isOxygenBoosted && this.isDinitrogenTetroxideBoosted) {
               textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.Joint_boosted"));
            } else if (this.isOxygenBoosted) {
               textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.supply_dinitrogen_tetroxide_to_boost"));
               textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.liquid_oxygen_boosted"));
            } else {
               textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.supply_liquid_oxygen_to_boost"));
            }
         } else {
            textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.boost_disallowed"));
         }
      }
   }

   public void attachTooltips(TooltipsPanel tooltipsPanel) {
      super.attachTooltips(tooltipsPanel);
      tooltipsPanel.attachTooltips(
         new IFancyTooltip[]{
            new Basic(
               () -> GuiTextures.INDICATOR_NO_STEAM.get(false),
               () -> List.of(Component.m_237115_("gtceu.multiblock.large_combustion_engine.obstructed").m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED))),
               this::isIntakesObstructed,
               () -> null
            )
         }
      );
   }

   public int getTier() {
      return 5;
   }
}
