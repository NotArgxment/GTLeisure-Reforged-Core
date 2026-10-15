package org.gtlcore.gtlcore.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DysonSphereMachine extends WorkableElectricMultiblockMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      DysonSphereMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   private int DysonSphereData;
   @Persisted
   private int DysonSpheredamageData;
   protected ConditionalSubscriptionHandler nightSubs = new ConditionalSubscriptionHandler(this, this::nightUpdate, () -> this.DysonSphereData > 0);
   @Nullable
   private List<@NotNull BlockPos> cachedCheckPositions;

   public DysonSphereMachine(IMachineBlockEntity holder) {
      super(holder, new Object[0]);
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   protected void nightUpdate() {
      if (this.getOffsetTimer() % 10L == 0L && this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_8615_(18000L);
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.nightSubs.initialize(this.getLevel());
      this.cachedCheckPositions = this.findCheckPositions();
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.cachedCheckPositions = null;
   }

   @Nullable
   protected List<@NotNull BlockPos> findCheckPositions() {
      Level level = this.getLevel();
      if (level == null) {
         return null;
      }

      BlockPos pos = this.getPos();
      BlockPos[] coordinates = new BlockPos[]{pos.m_7918_(4, 14, 0), pos.m_7918_(-4, 14, 0), pos.m_7918_(0, 14, 4), pos.m_7918_(0, 14, -4)};

      for (BlockPos blockPos : coordinates) {
         if (Objects.equals(Registries.getBlockId(level.m_8055_(blockPos).m_60734_()), "kubejs:dyson_receiver_casing")) {
            ObjectArrayList<BlockPos> checkPositions = new ObjectArrayList(169);

            for (int i = -6; i < 7; i++) {
               for (int j = -6; j < 7; j++) {
                  if (i != 0 || j != 0) {
                     checkPositions.add(blockPos.m_7918_(i, 1, j));
                  }
               }
            }

            return checkPositions;
         }
      }

      return null;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getRecipeLogic().getProgress() == 199 && this.getDysonSphereData() < 10000 && this.getRecipeLogic().getDuration() == 200) {
         if (this.getDysonSpheredamageData() > 60) {
            this.DysonSpheredamageData = 0;
         } else {
            this.DysonSphereData++;
         }
      }

      if (this.getRecipeLogic().getDuration() == 20
         && this.getRecipeLogic().getProgress() == 19
         && Math.random() < 0.01 * (1.0 + this.getDysonSphereData() / 128.0)
         && this.getDysonSphereData() > 0) {
         if (this.getDysonSpheredamageData() > 99) {
            this.DysonSphereData--;
            this.DysonSpheredamageData = 0;
         } else {
            this.DysonSpheredamageData++;
         }
      }

      return value;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (this.cachedCheckPositions == null) {
         this.getRecipeLogic().resetRecipeLogic();
         return false;
      }

      Level level = this.getLevel();
      if (level != null) {
         for (BlockPos checkPos : this.cachedCheckPositions) {
            if (!level.m_45527_(checkPos)) {
               this.getRecipeLogic().resetRecipeLogic();
               return false;
            }
         }
      }

      return recipe != null && this.isLaunch(recipe) ? this.getDysonSphereData() <= 10000 : this.getDysonSphereData() > 0;
   }

   private boolean isLaunch(GTRecipe recipe) {
      return RecipeHelper.getOutputEUt(recipe) != GTValues.V[14];
   }

   private double getEfficiency() {
      return (50 - Math.max(0, this.getDysonSpheredamageData() - 60)) / 50.0;
   }

   public int getOutputSignal(@Nullable Direction side) {
      return (int)((1.0 - this.getEfficiency()) * 15.0);
   }

   public long getOverclockVoltage() {
      return (long)(GTValues.V[14] * this.getDysonSphereData() * this.getEfficiency());
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
      if (machine instanceof DysonSphereMachine engineMachine) {
         if (!engineMachine.isLaunch(recipe)) {
            GTRecipe recipe1 = recipe.copy();
            recipe1.tickOutputs
               .put(
                  EURecipeCapability.CAP,
                  List.of(new Content(engineMachine.getOverclockVoltage(), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
               );
            recipe1.tickInputs
               .put(
                  CWURecipeCapability.CAP,
                  List.of(new Content(engineMachine.getDysonSphereData(), ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null))
               );
            return recipe1;
         } else {
            return recipe;
         }
      } else {
         return null;
      }
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(Component.m_237110_("gtceu.machine.dyson_sphere.number", new Object[]{this.getDysonSphereData()}));
         textList.add(Component.m_237110_("gtceu.machine.dyson_sphere.voltage", new Object[]{this.getDysonSphereData() > 0 ? this.getOverclockVoltage() : 0L}));
         textList.add(Component.m_237110_("gtceu.machine.fission_reactor.damaged", new Object[]{this.getDysonSpheredamageData()}).m_130946_("%"));
      }
   }

   public int getDysonSphereData() {
      return this.DysonSphereData;
   }

   public int getDysonSpheredamageData() {
      return this.DysonSpheredamageData;
   }

   public ConditionalSubscriptionHandler getNightSubs() {
      return this.nightSubs;
   }

   @Nullable
   public List<@NotNull BlockPos> getCachedCheckPositions() {
      return this.cachedCheckPositions;
   }
}
