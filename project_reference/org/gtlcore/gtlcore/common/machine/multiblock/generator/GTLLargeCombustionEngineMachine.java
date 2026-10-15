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
public class GTLLargeCombustionEngineMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {
   private static final FluidStack OXYGEN_STACK = GTMaterials.Oxygen.getFluid(20L);
   private static final FluidStack LIQUID_OXYGEN_STACK = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 80L);
   private static final FluidStack LUBRICANT_STACK = GTMaterials.Lubricant.getFluid(1L);
   private final int tier;
   private boolean isOxygenBoosted = false;

   public GTLLargeCombustionEngineMachine(IMachineBlockEntity holder, int tier) {
      super(holder, new Object[0]);
      this.tier = tier;
   }

   private boolean isIntakesObstructed() {
      Direction facing = this.getFrontFacing();
      boolean permuteXZ = facing.m_122434_() == Axis.Z;
      BlockPos centerPos = this.getPos().m_121945_(facing);

      for (int x = -1; x < 2; x++) {
         for (int y = -1; y < 2; y++) {
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

   private boolean isExtreme() {
      return this.getTier() > 4;
   }

   public boolean isBoostAllowed() {
      return this.getMaxVoltage() >= GTValues.V[this.getTier() + 1];
   }

   public long getOverclockVoltage() {
      return this.isOxygenBoosted ? GTValues.V[this.tier] * 8L : GTValues.V[this.tier] * 4L;
   }

   protected GTRecipe getLubricantRecipe() {
      return GTRecipeBuilder.ofRaw().inputFluids(LUBRICANT_STACK).buildRawRecipe();
   }

   protected GTRecipe getBoostRecipe() {
      return GTRecipeBuilder.ofRaw().inputFluids(this.isExtreme() ? LIQUID_OXYGEN_STACK : OXYGEN_STACK).buildRawRecipe();
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof GTLLargeCombustionEngineMachine engineMachine) {
         long EUt = RecipeHelper.getOutputEUt(recipe);
         if (EUt > 0L && RecipeRunnerHelper.matchRecipe(engineMachine, engineMachine.getLubricantRecipe()) && !engineMachine.isIntakesObstructed()) {
            int maxParallel = (int)(engineMachine.getOverclockVoltage() / EUt);
            GTRecipe parallelResult = (GTRecipe)GTRecipeModifiers.fastParallel(engineMachine, recipe, maxParallel, false).getFirst();
            if (engineMachine.isOxygenBoosted) {
               parallelResult.tickOutputs
                  .put(
                     EURecipeCapability.CAP,
                     List.of(
                        new Content(
                           (long)(RecipeHelper.getOutputEUt(parallelResult) * (engineMachine.isExtreme() ? 2.0 : 1.5)),
                           ChanceLogic.getMaxChancedValue(),
                           ChanceLogic.getMaxChancedValue(),
                           0,
                           null,
                           null
                        )
                     )
                  );
            }

            return parallelResult;
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
         this.isOxygenBoosted = RecipeRunnerHelper.matchRecipe(this, boosterRecipe) && RecipeRunnerHelper.handleRecipeInput(this, boosterRecipe);
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
            if (!this.isExtreme()) {
               if (this.isOxygenBoosted) {
                  textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.oxygen_boosted"));
               } else {
                  textList.add(Component.m_237115_("gtceu.multiblock.large_combustion_engine.supply_oxygen_to_boost"));
               }
            } else if (this.isOxygenBoosted) {
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
      return this.tier;
   }
}
