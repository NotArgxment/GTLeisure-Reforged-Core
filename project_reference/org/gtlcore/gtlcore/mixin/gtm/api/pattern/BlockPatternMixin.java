package org.gtlcore.gtlcore.mixin.gtm.api.pattern;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.api.pattern.error.SinglePredicateError;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.PatternMatchContext;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.pattern.util.IMultiblockStateGet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockPattern.class)
public abstract class BlockPatternMixin {
   @Shadow(remap = false)
   @Final
   protected TraceabilityPredicate[][][] blockMatches;
   @Shadow(remap = false)
   @Final
   protected int fingerLength;
   @Shadow(remap = false)
   @Final
   protected int thumbLength;
   @Shadow(remap = false)
   @Final
   protected int palmLength;
   @Shadow(remap = false)
   @Final
   public int[][] aisleRepetitions;
   @Shadow(remap = false)
   @Final
   protected int[] centerOffset;

   @Shadow(remap = false)
   protected abstract BlockPos setActualRelativeOffset(int var1, int var2, int var3, Direction var4, Direction var5, boolean var6);

   @Shadow(remap = false)
   protected abstract void resetFacing(BlockPos var1, BlockState var2, Direction var3, BiFunction<BlockPos, Direction, Boolean> var4, Consumer<BlockState> var5);

   @Overwrite(remap = false)
   public boolean checkPatternAt(
      MultiblockState worldState, BlockPos centerPos, Direction frontFacing, Direction upwardsFacing, boolean isFlipped, boolean savePredicate
   ) {
      boolean findFirstAisle = false;
      int minZ = -this.centerOffset[4];
      if (worldState instanceof IMultiblockStateGet stateGet) {
         stateGet.cleanState();
      }

      PatternMatchContext matchContext = worldState.getMatchContext();
      Map<SimplePredicate, Integer> globalCount = worldState.getGlobalCount();
      Map<SimplePredicate, Integer> layerCount = worldState.getLayerCount();
      int c = 0;
      int z = minZ++;

      while (c < this.fingerLength) {
         int r;
         label125:
         for (r = 0; findFirstAisle ? r < this.aisleRepetitions[c][1] : z <= -this.centerOffset[3]; r++) {
            layerCount.clear();
            int b = 0;

            for (int y = -this.centerOffset[1]; b < this.thumbLength; y++) {
               int a = 0;

               for (int x = -this.centerOffset[0]; a < this.palmLength; x++) {
                  worldState.setError(null);
                  TraceabilityPredicate predicate = this.blockMatches[c][b][a];
                  if (!predicate.isAny()) {
                     BlockPos pos = this.setActualRelativeOffset(x, y, z, frontFacing, upwardsFacing, isFlipped)
                        .m_7918_(centerPos.m_123341_(), centerPos.m_123342_(), centerPos.m_123343_());
                     if (worldState instanceof IMultiblockStateGet stateGet && !stateGet.updateState(pos, predicate)) {
                        return false;
                     }

                     if (predicate.addCache()) {
                        worldState.addPosCache(pos);
                        if (savePredicate) {
                           ((Object2ObjectOpenHashMap)matchContext.getOrCreate("predicates", Object2ObjectOpenHashMap::new)).put(pos, predicate);
                        }
                     }

                     boolean canPartShared = true;
                     if (worldState.getTileEntity() instanceof IMachineBlockEntity machineBlockEntity
                        && machineBlockEntity.getMetaMachine() instanceof IMultiPart part
                        && !predicate.isAny()) {
                        if (part.isFormed() && !part.canShared() && !part.hasController(worldState.controllerPos)) {
                           canPartShared = false;
                           worldState.setError(new PatternStringError("multiblocked.pattern.error.share"));
                        } else {
                           ((ObjectOpenHashSet)matchContext.getOrCreate("parts", ObjectOpenHashSet::new)).add(part);
                        }
                     }

                     if (worldState.getBlockState().m_60734_() instanceof ActiveBlock) {
                        ((LongOpenHashSet)matchContext.getOrCreate("vaBlocks", LongOpenHashSet::new)).add(worldState.getPos().m_121878_());
                     }

                     if (!predicate.test(worldState) || !canPartShared) {
                        if (findFirstAisle) {
                           if (r < this.aisleRepetitions[c][0]) {
                              c = 0;
                              r = 0;
                              z = minZ++;
                              matchContext.reset();
                              findFirstAisle = false;
                           }
                        } else {
                           z++;
                        }
                        continue label125;
                     }

                     ((Long2ObjectOpenHashMap)matchContext.getOrCreate("ioMap", Long2ObjectOpenHashMap::new))
                        .put(worldState.getPos().m_121878_(), worldState.io);
                  }

                  a++;
               }

               b++;
            }

            findFirstAisle = true;
            z++;
            Iterator var29 = layerCount.entrySet().iterator();

            Entry<SimplePredicate, Integer> entry;
            do {
               if (!var29.hasNext()) {
                  continue label125;
               }

               entry = (Entry<SimplePredicate, Integer>)var29.next();
            } while (entry.getValue() >= entry.getKey().minLayerCount);

            worldState.setError(new SinglePredicateError(entry.getKey(), 3));
            return false;
         }

         if (r < this.aisleRepetitions[c][0] || worldState.hasError() || !findFirstAisle) {
            if (!worldState.hasError()) {
               worldState.setError(new PatternError());
            }

            return false;
         }

         c++;
      }

      for (Entry<SimplePredicate, Integer> entry : globalCount.entrySet()) {
         if (entry.getValue() < entry.getKey().minCount) {
            worldState.setError(new SinglePredicateError(entry.getKey(), 1));
            return false;
         }
      }

      worldState.setError(null);
      worldState.setNeededFlip(isFlipped);
      return true;
   }

   @Overwrite(remap = false)
   public BlockInfo[][][] getPreview(int[] repetition) {
      Object2IntOpenHashMap<SimplePredicate> cacheGlobal = new Object2IntOpenHashMap();
      Long2ObjectOpenHashMap<BlockInfo> blocks = new Long2ObjectOpenHashMap();
      int minX = 2147483647;
      int minY = 2147483647;
      int minZ = 2147483647;
      int maxX = -2147483648;
      int maxY = -2147483648;
      int maxZ = -2147483648;
      int l = 0;
      int x = 0;

      while (l < this.fingerLength) {
         for (int r = 0; r < repetition[l]; r++) {
            Object2IntOpenHashMap<SimplePredicate> cacheLayer = new Object2IntOpenHashMap();

            for (int y = 0; y < this.thumbLength; y++) {
               for (int z = 0; z < this.palmLength; z++) {
                  TraceabilityPredicate predicate = this.blockMatches[l][y][z];
                  if (!predicate.isAny()) {
                     boolean find = false;
                     BlockInfo[] infos = null;

                     for (SimplePredicate limit : predicate.limited) {
                        if (limit.minLayerCount > 0 && cacheLayer.getInt(limit) < limit.minLayerCount) {
                           cacheLayer.addTo(limit, 1);
                           if (cacheGlobal.getInt(limit) < limit.previewCount) {
                              cacheGlobal.addTo(limit, 1);
                              infos = limit.candidates == null ? null : (BlockInfo[])limit.candidates.get();
                              find = true;
                              break;
                           }
                        }
                     }

                     label166:
                     if (!find) {
                        Iterator var25 = predicate.limited.iterator();

                        SimplePredicate limit;
                        while (true) {
                           if (!var25.hasNext()) {
                              break label166;
                           }

                           limit = (SimplePredicate)var25.next();
                           if (limit.minCount != -1 || limit.previewCount != -1) {
                              if (cacheGlobal.getInt(limit) < limit.previewCount) {
                                 cacheGlobal.addTo(limit, 1);
                                 break;
                              }

                              if (limit.minCount > 0 && cacheGlobal.getInt(limit) < limit.minCount) {
                                 cacheGlobal.addTo(limit, 1);
                                 break;
                              }
                           }
                        }

                        infos = limit.candidates == null ? null : (BlockInfo[])limit.candidates.get();
                        find = true;
                     }

                     if (!find) {
                        for (SimplePredicate common : predicate.common) {
                           if (common.previewCount > 0 && cacheGlobal.getInt(common) < common.previewCount) {
                              cacheGlobal.addTo(common, 1);
                              infos = common.candidates == null ? null : (BlockInfo[])common.candidates.get();
                              find = true;
                              break;
                           }
                        }
                     }

                     if (!find) {
                        for (SimplePredicate common : predicate.common) {
                           if (common.previewCount == -1) {
                              infos = common.candidates == null ? null : (BlockInfo[])common.candidates.get();
                              find = true;
                              break;
                           }
                        }
                     }

                     label128:
                     if (!find) {
                        Iterator var28 = predicate.limited.iterator();

                        SimplePredicate limit;
                        while (true) {
                           if (!var28.hasNext()) {
                              break label128;
                           }

                           limit = (SimplePredicate)var28.next();
                           if (limit.previewCount == -1) {
                              if (limit.maxCount == -1 && limit.maxLayerCount == -1) {
                                 break;
                              }

                              if (cacheGlobal.getOrDefault(limit, 0) < limit.maxCount) {
                                 cacheGlobal.addTo(limit, 1);
                                 break;
                              }

                              if (cacheLayer.getOrDefault(limit, 0) < limit.maxLayerCount) {
                                 cacheLayer.addTo(limit, 1);
                                 break;
                              }
                           }
                        }

                        infos = limit.candidates == null ? null : (BlockInfo[])limit.candidates.get();
                     }

                     BlockInfo info = infos != null && infos.length != 0 ? infos[0] : BlockInfo.EMPTY;
                     BlockPos pos = this.setActualRelativeOffset(z, y, x, Direction.NORTH, Direction.UP, false);
                     blocks.put(pos.m_121878_(), info);
                     minX = Math.min(pos.m_123341_(), minX);
                     minY = Math.min(pos.m_123342_(), minY);
                     minZ = Math.min(pos.m_123343_(), minZ);
                     maxX = Math.max(pos.m_123341_(), maxX);
                     maxY = Math.max(pos.m_123342_(), maxY);
                     maxZ = Math.max(pos.m_123343_(), maxZ);
                  }
               }
            }

            x++;
         }

         l++;
      }

      BlockInfo[][][] result = (BlockInfo[][][])Array.newInstance(BlockInfo.class, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
      x = minX;
      int finalMinY = minY;
      int finalMinZ = minZ;
      blocks.long2ObjectEntrySet()
         .fastForEach(
            entry -> {
               long blockPos = entry.getLongKey();
               BlockPos posx = BlockPos.m_122022_(blockPos);
               BlockInfo info = (BlockInfo)entry.getValue();
               this.resetFacing(
                  posx,
                  info.getBlockState(),
                  null,
                  (p, f) -> {
                     BlockInfo blockInfo = (BlockInfo)blocks.get(p.m_121945_(f).m_121878_());
                     if (blockInfo != null && blockInfo.getBlockState().m_60734_() != Blocks.f_50016_) {
                        return false;
                     } else if (((BlockInfo)blocks.get(blockPos)).getBlockState().m_60734_() instanceof MetaMachineBlock machineBlock
                        && machineBlock.m_142194_(BlockPos.f_121853_, machineBlock.m_49966_()) instanceof IMachineBlockEntity machineBlockEntity) {
                        MetaMachine machine = machineBlockEntity.getMetaMachine();
                        return machine instanceof IMultiController ? false : machine.isFacingValid(f);
                     } else {
                        return true;
                     }
                  },
                  info::setBlockState
               );
               result[posx.m_123341_() - x][posx.m_123342_() - finalMinY][posx.m_123343_() - finalMinZ] = info;
            }
         );
      return result;
   }
}
