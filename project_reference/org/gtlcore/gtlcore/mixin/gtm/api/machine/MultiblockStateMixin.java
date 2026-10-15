package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.hepdd.gtmthings.common.block.machine.multiblock.part.HugeBusPartMachine;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.pattern.util.IMultiblockStateGet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MultiblockState.class)
public abstract class MultiblockStateMixin implements IMultiblockStateGet {
   @Final
   @Shadow(remap = false)
   public Level world;
   @Final
   @Shadow(remap = false)
   public BlockPos controllerPos;
   @Shadow(remap = false)
   public IMultiController lastController;

   @Overwrite(remap = false)
   public void onBlockStateChanged(BlockPos pos, BlockState state) {
      if (this.world instanceof ServerLevel serverLevel) {
         if (pos.equals(this.controllerPos)) {
            if (this.lastController != null && !state.m_60713_(this.lastController.self().getBlockState().m_60734_())) {
               this.lastController.onStructureInvalid();
               MultiblockWorldSavedData mwsd = MultiblockWorldSavedData.getOrCreate(serverLevel);
               mwsd.removeMapping((MultiblockState)this);
            }
         } else {
            MultiblockState tempThis = (MultiblockState)this;
            IMultiController controller = tempThis.getController();
            if (controller != null) {
               boolean formed = controller.isFormed();
               if (formed) {
                  if (state.m_60734_() instanceof ActiveBlock) {
                     LongSet activeBlocks = (LongSet)tempThis.getMatchContext().getOrDefault("vaBlocks", LongSets.emptySet());
                     if (activeBlocks.contains(pos.m_121878_())) {
                        return;
                     }
                  } else if (serverLevel.m_7702_(pos) instanceof IMachineBlockEntity IMBE) {
                     MetaMachine metaMachine = IMBE.getMetaMachine();
                     if (metaMachine instanceof ItemBusPartMachine || metaMachine instanceof FluidHatchPartMachine || metaMachine instanceof HugeBusPartMachine
                        )
                      {
                        return;
                     }
                  }
               }

               if (formed && controller.checkPatternWithLock()) {
                  controller.self().setFlipped(tempThis.isNeededFlip());
                  controller.onStructureFormed();
               } else {
                  controller.self().setFlipped(false);
                  controller.onStructureInvalid();
                  MultiblockWorldSavedData mwsd = MultiblockWorldSavedData.getOrCreate(serverLevel);
                  mwsd.removeMapping(tempThis);
                  mwsd.addAsyncLogic(controller);
               }
            }
         }
      }
   }

   @Override
   public void cleanState() {
      this.clean();
   }

   @Override
   public boolean updateState(BlockPos posIn, TraceabilityPredicate predicate) {
      return this.update(posIn, predicate);
   }

   @Shadow(remap = false)
   protected abstract void clean();

   @Shadow(remap = false)
   protected abstract boolean update(BlockPos var1, TraceabilityPredicate var2);
}
