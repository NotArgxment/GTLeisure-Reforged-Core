package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.gtlcore.gtlcore.api.pattern.INewMultiblockWorldSavedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = INewMultiblockWorldSavedData.class, prefix = "gTLCore$"))
@Mixin(MultiblockWorldSavedData.class)
public abstract class MultiblockWorldSavedDataMixin {
   @Shadow(remap = false)
   @Final
   public Map<BlockPos, MultiblockState> mapping;
   @Shadow(remap = false)
   @Final
   public Map<ChunkPos, Set<MultiblockState>> chunkPosMapping;

   @Overwrite(remap = false)
   public void addMapping(MultiblockState state) {
      this.mapping.put(state.controllerPos, state);

      for (BlockPos blockPos : state.getCache()) {
         this.chunkPosMapping.computeIfAbsent(new ChunkPos(blockPos), c -> ConcurrentHashMap.newKeySet()).add(state);
      }
   }

   @Overwrite(remap = false)
   public void removeMapping(MultiblockState state) {
      this.mapping.remove(state.controllerPos);

      for (Set<MultiblockState> set : this.chunkPosMapping.values()) {
         set.remove(state);
      }
   }

   @Overwrite(remap = false)
   public MultiblockState[] getControllerInChunk(ChunkPos chunkPos) {
      return new MultiblockState[0];
   }

   @Unique
   public Set<MultiblockState> gTLCore$getControllersInChunk(ChunkPos chunkPos) {
      return this.chunkPosMapping.getOrDefault(chunkPos, Collections.emptySet());
   }
}
