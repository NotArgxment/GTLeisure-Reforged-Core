package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Block;
import org.gtlcore.gtlcore.api.pattern.util.IValueContainer;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.gtlcore.gtlcore.utils.MachineUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FissionReactorMachine extends WorkableElectricMultiblockMachine implements IExplosionMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      FissionReactorMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   private BlockPos centrePos = null;
   @Persisted
   private int heat = 298;
   @Persisted
   private int damaged = 0;
   @Persisted
   private int parallel = 0;
   @Persisted
   private int recipeHeat = 0;
   @Persisted
   private int fuel = 0;
   @Persisted
   private int cooler = 0;
   @Persisted
   private int heatAdjacent = 1;
   @Persisted
   private int coolerAdjacent = 0;
   protected ConditionalSubscriptionHandler heatSubs = new ConditionalSubscriptionHandler(this, this::heatUpdate, this::isFormed);

   public FissionReactorMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public static int adjacent(Level level, BlockPos pos, Block block) {
      int a = 0;
      BlockPos[] coordinates = new BlockPos[]{
         pos.m_7918_(1, 0, 0), pos.m_7918_(-1, 0, 0), pos.m_7918_(0, 1, 0), pos.m_7918_(0, -1, 0), pos.m_7918_(0, 0, 1), pos.m_7918_(0, 0, -1)
      };

      for (BlockPos blockPos : coordinates) {
         if (level.m_8055_(blockPos).m_60734_() == block) {
            a++;
         }
      }

      return a;
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      BlockPos pos = this.getPos();
      Level level = this.getLevel();
      int heatA = 0;
      int coolerA = 0;
      BlockPos blockPos = MachineUtil.getOffsetPos(4, 0, this.getFrontFacing(), this.getPos());
      this.centrePos = blockPos.m_7918_(0, 4, 0);

      for (int i = -3; i < 4; i++) {
         for (int j = 0; j < 8; j++) {
            for (int k = -3; k < 4; k++) {
               BlockPos assemblyPos = blockPos.m_7918_(i, j, k);
               if (level != null && level.m_8055_(assemblyPos).m_60734_() == GTLBlocks.FISSION_FUEL_ASSEMBLY.get()) {
                  heatA += adjacent(level, assemblyPos, (Block)GTLBlocks.FISSION_FUEL_ASSEMBLY.get());
               }

               if (level != null && level.m_8055_(assemblyPos).m_60734_() == GTLBlocks.COOLER.get()) {
                  coolerA += adjacent(level, assemblyPos, (Block)GTLBlocks.COOLER.get());
               }
            }
         }
      }

      this.heatAdjacent = heatA / 2 + 1;
      this.coolerAdjacent = coolerA / 2;
      IValueContainer<?> FuelContainer = (IValueContainer<?>)this.getMultiblockState()
         .getMatchContext()
         .getOrCreate("FuelAssemblyValue", IValueContainer::noop);
      if (FuelContainer.getValue() instanceof Integer Fuel) {
         this.fuel = Fuel;
      }

      IValueContainer<?> CoolerContainer = (IValueContainer<?>)this.getMultiblockState().getMatchContext().getOrCreate("CoolerValue", IValueContainer::noop);
      if (CoolerContainer.getValue() instanceof Integer Cooler) {
         this.cooler = Cooler;
      }

      this.heatSubs.initialize(this.getLevel());
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.fuel = 0;
      this.cooler = 0;
      this.heatAdjacent = 1;
      this.coolerAdjacent = 0;
   }

   public void afterWorking() {
      this.parallel = 0;
      this.recipeHeat = 0;
      super.afterWorking();
   }

   public void onWaiting() {
      super.onWaiting();
      this.getRecipeLogic().resetRecipeLogic();
   }

   public void doExplosion(BlockPos pos, float explosionPower) {
      MultiblockControllerMachine machine = this.self();
      Level level = machine.getLevel();
      if (level != null) {
         level.m_7471_(machine.getPos(), false);
         level.m_254849_(null, pos.m_123341_() + 0.5, pos.m_123342_() + 0.5, pos.m_123343_() + 0.5, explosionPower, ExplosionInteraction.BLOCK);
      }
   }

   private boolean inputWater(long amount) {
      boolean value = MachineUtil.inputFluid(this, GTMaterials.DistilledWater.getFluid(amount * 800L));
      double steamMultiplier = this.heat > 373 ? 160.0 : 160.0 / Math.pow(1.4, 373 - this.heat);
      if (value) {
         value = MachineIO.outputFluid(this, GTMaterials.Steam.getFluid((long)(amount * 800L * steamMultiplier)));
      }

      return value;
   }

   private boolean inputSodiumPotassium(long amount) {
      boolean value = MachineUtil.inputFluid(this, GTMaterials.SodiumPotassium.getFluid(amount * 20L));
      if (this.heat > 825) {
         if (value) {
            value = MachineIO.outputFluid(this, GTLMaterials.SupercriticalSodiumPotassium.getFluid(amount * 20L));
         }
      } else if (value) {
         value = MachineIO.outputFluid(this, GTLMaterials.HotSodiumPotassium.getFluid(amount * 20L));
      }

      return value;
   }

   protected void heatUpdate() {
      if (this.getOffsetTimer() % 20L == 0L) {
         if (this.heat > 1500) {
            if (this.damaged > 99) {
               this.doExplosion(this.centrePos, this.fuel);
            } else {
               this.damaged = this.damaged + Math.max(1, this.heatAdjacent / 6);
            }
         }

         if (this.getRecipeLogic().isWorking()) {
            int required = this.recipeHeat * this.parallel * this.heat / 1500;
            int surplus = (this.cooler - this.coolerAdjacent / 3) * 8 - required;
            boolean isCooler = false;
            if (surplus >= 0) {
               if (this.inputWater(required)) {
                  while (surplus >= required && this.getProgress() < this.getMaxProgress() && this.inputWater(required)) {
                     surplus -= required;
                     this.getRecipeLogic().setProgress(this.getProgress() + 20);
                  }

                  if (this.heat > 298 && surplus >= required && this.inputWater(required)) {
                     this.heat--;
                  }

                  isCooler = true;
               } else if (this.inputSodiumPotassium(required)) {
                  while (surplus >= required && this.getProgress() < this.getMaxProgress() && this.inputSodiumPotassium(required)) {
                     surplus -= required;
                     this.getRecipeLogic().setProgress(this.getProgress() + 20);
                  }

                  if (this.heat > 298 && surplus >= required && this.inputSodiumPotassium(required)) {
                     this.heat--;
                  }

                  isCooler = true;
               }
            }

            if (!isCooler) {
               this.heat = this.heat + this.recipeHeat * this.heatAdjacent;
            }
         } else if (this.heat > 298) {
            this.heat--;
         } else if (this.damaged > 0) {
            this.damaged--;
         }
      }
   }

   public int getOutputSignal(@Nullable Direction side) {
      return 22500 / this.heat;
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
      if (machine instanceof FissionReactorMachine fissionReactorMachine) {
         Pair<GTRecipe, Integer> result = GTRecipeModifiers.accurateParallel(machine, recipe, fissionReactorMachine.fuel, false);
         GTRecipe recipe1 = (GTRecipe)result.getFirst();
         fissionReactorMachine.parallel = (Integer)result.getSecond();
         fissionReactorMachine.recipeHeat = recipe1.data.m_128451_("FRheat");
         return recipe1;
      } else {
         return null;
      }
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         textList.add(Component.m_237110_("gtceu.machine.fission_reactor.fuel", new Object[]{this.fuel, this.heatAdjacent - 1}));
         textList.add(Component.m_237110_("gtceu.machine.fission_reactor.cooler", new Object[]{this.cooler, this.coolerAdjacent}));
         textList.add(Component.m_237110_("gtceu.machine.fission_reactor.heat", new Object[]{this.heat}));
         textList.add(Component.m_237110_("gtceu.machine.fission_reactor.damaged", new Object[]{this.damaged}).m_130946_("%"));
      }
   }
}
