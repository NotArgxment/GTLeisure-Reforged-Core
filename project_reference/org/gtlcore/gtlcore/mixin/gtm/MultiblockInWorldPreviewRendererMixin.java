package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.client.renderer.MultiblockInWorldPreviewRenderer;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MultiblockInWorldPreviewRenderer.class)
public abstract class MultiblockInWorldPreviewRendererMixin {
   @Shadow(remap = false)
   private static BlockPos LAST_POS;
   @Shadow(remap = false)
   private static int LAST_LAYER;
   @Shadow(remap = false)
   private static TrackedDummyWorld LEVEL;

   @Shadow(remap = false)
   private static BlockPos rotateByFrontAxis(BlockPos pos, Direction front, Rotation rotation) {
      return pos;
   }

   @Shadow(remap = false)
   private static void prepareBuffers(TrackedDummyWorld level, Collection<BlockPos> renderedBlocks, int duration) {
   }

   @Overwrite(remap = false)
   public static void showPreview(BlockPos pos, MultiblockControllerMachine controller, int duration) {
      if (controller.getDefinition().isRenderWorldPreview()) {
         Direction front = controller.getFrontFacing();
         Direction up = controller.getUpwardsFacing();
         MultiblockShapeInfo shapeInfo = (MultiblockShapeInfo)controller.getDefinition().getMatchingShapes().get(0);
         Map<BlockPos, BlockInfo> blockMap = new Object2ObjectOpenHashMap();
         IMultiController controllerBase = null;
         LEVEL = new TrackedDummyWorld();
         BlockInfo[][][] blocks = shapeInfo.getBlocks();
         BlockPos controllerPatternPos = null;
         int maxY = 0;

         label145:
         for (int x = 0; x < blocks.length; x++) {
            BlockInfo[][] aisle = blocks[x];
            maxY = Math.max(maxY, aisle.length);

            for (int y = 0; y < aisle.length; y++) {
               BlockInfo[] column = aisle[y];

               for (int z = 0; z < column.length; z++) {
                  BlockInfo info = column[z];
                  if (info != null
                     && info.getBlockState().m_60734_() instanceof IMachineBlock machineBlock
                     && machineBlock.getDefinition() instanceof MultiblockMachineDefinition) {
                     controllerPatternPos = new BlockPos(x, y, z);
                     break label145;
                  }
               }
            }
         }

         if (controllerPatternPos != null) {
            if (LAST_POS != null && LAST_POS.equals(pos)) {
               LAST_LAYER++;
               if (LAST_LAYER >= maxY) {
                  LAST_LAYER = -1;
               }
            } else {
               LAST_LAYER = -1;
            }

            LAST_POS = pos;

            for (int x = 0; x < blocks.length; x++) {
               BlockInfo[][] aisle = blocks[x];

               for (int y = 0; y < aisle.length; y++) {
                  BlockInfo[] column = aisle[y];
                  if (LAST_LAYER == -1 || LAST_LAYER == y) {
                     for (int z = 0; z < column.length; z++) {
                        BlockInfo info = column[z];
                        if (info != null) {
                           BlockState blockState = info.getBlockState();
                           BlockPos offset = new BlockPos(x, y, z).m_121996_(controllerPatternPos);

                           offset = switch (front) {
                              case NORTH, UP, DOWN -> offset.m_7954_(Rotation.NONE);
                              case SOUTH -> offset.m_7954_(Rotation.CLOCKWISE_180);
                              case EAST -> offset.m_7954_(Rotation.COUNTERCLOCKWISE_90);
                              case WEST -> offset.m_7954_(Rotation.CLOCKWISE_90);
                              default -> throw new IncompatibleClassChangeError();
                           };
                           Rotation r = up == Direction.NORTH
                              ? Rotation.NONE
                              : (
                                 up == Direction.EAST
                                    ? Rotation.CLOCKWISE_90
                                    : (up == Direction.SOUTH ? Rotation.CLOCKWISE_180 : (up == Direction.WEST ? Rotation.COUNTERCLOCKWISE_90 : Rotation.NONE))
                              );
                           offset = rotateByFrontAxis(offset, front, r);
                           if (blockState.m_60734_() instanceof MetaMachineBlock machineBlock) {
                              RotationState rotationState = machineBlock.getRotationState();
                              if (rotationState != RotationState.NONE) {
                                 Direction face = (Direction)blockState.m_61143_(rotationState.property);
                                 if (face.m_122434_() != Axis.Y) {
                                    face = switch (front) {
                                       case NORTH, UP, DOWN -> front;
                                       case SOUTH -> face.m_122424_();
                                       case EAST -> face.m_122427_();
                                       case WEST -> face.m_122428_();
                                       default -> throw new IncompatibleClassChangeError();
                                    };
                                 }

                                 if (rotationState.test(face)) {
                                    blockState = (BlockState)blockState.m_61124_(rotationState.property, face);
                                 }
                              }
                           }

                           BlockPos realPos = pos.m_121955_(offset);
                           if (info.getBlockEntity(realPos) instanceof IMachineBlockEntity holder && holder.getMetaMachine() instanceof IMultiController cont) {
                              holder.getSelf().m_142339_(LEVEL);
                              controllerBase = cont;
                           } else {
                              blockMap.put(realPos, BlockInfo.fromBlockState(blockState));
                           }
                        }
                     }
                  }
               }
            }

            LEVEL.addBlocks(blockMap);
            if (controllerBase != null) {
               LEVEL.setInnerBlockEntity(controllerBase.self().holder.getSelf());
            }

            prepareBuffers(LEVEL, blockMap.keySet(), duration);
         }
      }
   }
}
