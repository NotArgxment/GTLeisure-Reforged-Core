package org.gtlcore.gtlcore.mixin.mc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.gtlcore.gtlcore.common.util.BlockStateWatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
   @Final
   @Shadow
   Level f_62776_;

   @Inject(
      method = "setBlockState",
      at = @At(
         value = "INVOKE",
         opcode = 180,
         target = "Lnet/minecraft/world/level/block/state/BlockState;onPlace(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Z)V"
      )
   )
   private void notifyBlockStateWatchers(BlockPos pos, BlockState state, boolean isMoving, CallbackInfoReturnable<BlockState> cir) {
      if (this.f_62776_ instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().execute(() -> BlockStateWatcher.notifyWatchersInternal(serverLevel, pos, state));
      }
   }
}
