package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.utils.MachineUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceElevatorModuleMachine
   extends WorkableElectricMultiblockMachine
   implements IModularMachineModule<SpaceElevatorMachine, SpaceElevatorModuleMachine>,
   IMachineLife {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      SpaceElevatorModuleMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @DescSynced
   private int spaceElevatorTier = 0;
   private int moduleTier = 0;
   private final boolean sepmTier;
   @Persisted
   @Nullable
   private BlockPos hostPosition;
   @Nullable
   private SpaceElevatorMachine host;

   public SpaceElevatorModuleMachine(IMachineBlockEntity holder, boolean sepmTier, Object... args) {
      super(holder, args);
      this.sepmTier = sepmTier;
   }

   @NotNull
   @Override
   public Class<SpaceElevatorMachine> getHostType() {
      return SpaceElevatorMachine.class;
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void onConnected(@NotNull SpaceElevatorMachine host) {
      this.getSpaceElevatorTier();
      this.recipeLogic.updateTickSubscription();
   }

   @Override
   public BlockPos[] getHostScanPositions() {
      BlockPos pos = this.getPos();
      BlockPos[] powerCorePositions = new BlockPos[]{
         pos.m_7918_(8, -2, 3),
         pos.m_7918_(8, -2, -3),
         pos.m_7918_(-8, -2, 3),
         pos.m_7918_(-8, -2, -3),
         pos.m_7918_(3, -2, 8),
         pos.m_7918_(-3, -2, 8),
         pos.m_7918_(3, -2, -8),
         pos.m_7918_(-3, -2, -8)
      };
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         for (BlockPos i : powerCorePositions) {
            if (serverLevel.m_8055_(i).m_60734_() == GTLBlocks.POWER_CORE.get()) {
               return new BlockPos[]{i.m_7918_(3, 2, 0), i.m_7918_(-3, 2, 0), i.m_7918_(0, 2, 3), i.m_7918_(0, 2, -3)};
            }
         }
      }

      return MachineUtil.EMPTY_POS_ARRAY;
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (!this.findAndConnectToHost()) {
         this.removeFromHost(this.host);
      }
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.removeFromHost(this.host);
   }

   public void onPartUnload() {
      super.onPartUnload();
      this.removeFromHost(this.host);
   }

   public void onMachineRemoved() {
      this.removeFromHost(this.host);
   }

   private void getSpaceElevatorTier() {
      if (this.host != null) {
         RecipeLogic logic = this.host.getRecipeLogic();
         if (logic.isWorking() && logic.getProgress() > 80) {
            this.spaceElevatorTier = this.host.getTier() - 7;
            this.moduleTier = this.host.getCasingTier();
         } else if (!logic.isWorking()) {
            this.spaceElevatorTier = 0;
            this.moduleTier = 0;
         }
      } else {
         this.spaceElevatorTier = 0;
         this.moduleTier = 0;
      }
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof SpaceElevatorModuleMachine moduleMachine) {
         moduleMachine.getSpaceElevatorTier();
         if (moduleMachine.spaceElevatorTier < 1) {
            return null;
         }

         if (moduleMachine.sepmTier && recipe.data.m_128451_("SEPMTier") > moduleMachine.moduleTier) {
            return null;
         }

         GTRecipe recipe1 = GTLRecipeModifiers.reduction(machine, recipe, 1.0, Math.pow(0.8, moduleMachine.spaceElevatorTier - 1));
         if (recipe1 != null) {
            recipe1 = (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe1, (int)Math.pow(4.0, moduleMachine.moduleTier - 1), false).getFirst();
            if (recipe1 != null) {
               return RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1, moduleMachine.getOverclockVoltage(), params, result);
            }
         }
      }

      return null;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L) {
         this.getSpaceElevatorTier();
         if (this.spaceElevatorTier < 1) {
            this.getRecipeLogic().setProgress(0);
         }
      }

      return value;
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         if (this.getOffsetTimer() % 10L == 0L) {
            this.getSpaceElevatorTier();
         }

         textList.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(Math.pow(4.0, this.moduleTier - 1))).m_130940_(ChatFormatting.DARK_PURPLE)}
               )
               .m_130940_(ChatFormatting.GRAY)
         );
         textList.add(
            Component.m_237115_(this.spaceElevatorTier < 1 ? "tooltip.gtlcore.space_elevator_not_connected" : "tooltip.gtlcore.space_elevator_connected")
         );
         textList.add(
            Component.m_237110_(
               "gtceu.machine.duration_multiplier.tooltip", new Object[]{FormattingUtil.formatPercent(Math.pow(0.8, this.spaceElevatorTier - 1))}
            )
         );
      }
   }

   @Nullable
   @Override
   public BlockPos getHostPosition() {
      return this.hostPosition;
   }

   @Override
   public void setHostPosition(@Nullable BlockPos hostPosition) {
      this.hostPosition = hostPosition;
   }

   @Nullable
   public SpaceElevatorMachine getHost() {
      return this.host;
   }

   public void setHost(@Nullable SpaceElevatorMachine host) {
      this.host = host;
   }
}
