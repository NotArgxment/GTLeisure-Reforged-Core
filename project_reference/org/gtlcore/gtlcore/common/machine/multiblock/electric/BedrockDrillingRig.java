package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.common.util.BlockStateWatcher;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.Nullable;

public class BedrockDrillingRig extends WorkableElectricMultiblockMachine implements IMachineLife {
   protected BlockStateWatcher.WatcherHandle watcherHandle;
   protected BlockPos targetPos = this.getPos().m_7918_(0, -9, 0);
   protected boolean hasBedrockAtTarget;

   public BedrockDrillingRig(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public void onUnload() {
      super.onUnload();
      if (!this.isRemote()) {
         this.unregisterBlockWatcher();
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (this.getLevel() instanceof ServerLevel level) {
         this.registerBlockWatcher(level);
      }
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      if (!this.isRemote()) {
         this.unregisterBlockWatcher();
      }
   }

   public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
      this.targetPos = this.getPos().m_7918_(0, -9, 0);
   }

   public void onMachineRemoved() {
      if (!this.isRemote()) {
         this.unregisterBlockWatcher();
      }
   }

   protected void registerBlockWatcher(ServerLevel level) {
      this.unregisterBlockWatcher();
      this.watcherHandle = BlockStateWatcher.addWatcher(this.getLevel(), this.targetPos, this::onBlockStateChanged);
      BlockState currentState = level.m_8055_(this.targetPos);
      this.hasBedrockAtTarget = Registries.getBlockId(currentState.m_60734_()).equals("minecraft:bedrock");
      if (this.hasBedrockAtTarget) {
         this.recipeLogic.updateTickSubscription();
      }
   }

   protected void unregisterBlockWatcher() {
      if (this.watcherHandle != null) {
         this.watcherHandle.remove();
         this.watcherHandle = null;
      }
   }

   protected void onBlockStateChanged(BlockState newState) {
      this.hasBedrockAtTarget = newState != null && Registries.getBlockId(newState.m_60734_()).equals("minecraft:bedrock");
      if (this.hasBedrockAtTarget) {
         this.recipeLogic.updateTickSubscription();
      }
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      return this.hasBedrockAtTarget;
   }

   public void afterWorking() {
      super.afterWorking();
      Level level = this.getLevel();
      if (level != null && ThreadLocalRandom.current().nextInt(10) == 0) {
         level.m_46597_(this.targetPos, Blocks.f_50016_.m_49966_());
      }
   }
}
