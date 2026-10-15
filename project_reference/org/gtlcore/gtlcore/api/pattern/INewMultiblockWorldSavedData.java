package org.gtlcore.gtlcore.api.pattern;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import java.util.Set;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface INewMultiblockWorldSavedData {
   Set<MultiblockState> getControllersInChunk(ChunkPos var1);
}
