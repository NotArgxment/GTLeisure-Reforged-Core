package org.gtlcore.gtlcore.common.machine.multiblock;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.client.renderer.MultiblockInWorldPreviewRenderer;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.StorageMachine;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SimulationMachine extends StorageMachine implements IMachineModifyDrops {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(SimulationMachine.class, StorageMachine.MANAGED_FIELD_HOLDER);

   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public SimulationMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, 1);
   }

   @Override
   protected boolean filter(@NotNull ItemStack itemStack) {
      return itemStack.m_41720_() instanceof MetaMachineItem item ? item.getDefinition() instanceof MultiblockMachineDefinition : false;
   }

   public boolean checkPattern() {
      return false;
   }

   public void asyncCheckPattern(long periodID) {
   }

   public BlockPattern getPattern() {
      ItemStack stack = this.getMachineStorageItem();
      if (stack.m_41619_()) {
         return super.getPattern();
      } else {
         return stack.m_41720_() instanceof MetaMachineItem item && item.getDefinition() instanceof MultiblockMachineDefinition definition
            ? (BlockPattern)definition.getPatternFactory().get()
            : super.getPattern();
      }
   }

   public boolean isFormed() {
      return false;
   }

   private MultiblockControllerMachine getRealController() {
      ItemStack stack = this.getMachineStorageItem();
      if (stack.m_41619_()) {
         return this.self();
      }

      MetaMachineBlockEntity be = null;
      MetaMachineItem machineItem = (MetaMachineItem)stack.m_41720_();
      if (machineItem.m_40614_() instanceof IMachineBlock machineBlock) {
         BlockState bs = machineBlock.self().m_152465_(this.getBlockState());
         be = (MetaMachineBlockEntity)machineBlock.m_142194_(this.getPos(), bs);
      }

      return be == null ? this.self() : (MultiblockControllerMachine)be.getMetaMachine().self();
   }

   public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (player.m_6144_() && player.m_21120_(hand).m_41619_()) {
         if (world.m_5776_()) {
            MultiblockInWorldPreviewRenderer.showPreview(pos, this.getRealController(), ConfigHolder.INSTANCE.client.inWorldPreviewDuration * 20);
         }

         return InteractionResult.SUCCESS;
      } else {
         return super.onUse(state, world, pos, player, hand, hit);
      }
   }
}
