package org.gtlcore.gtlcore.utils.datastructure;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Rotation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public record ModuleRenderInfo(
   @NotNull BlockPos worldOffset,
   @NotNull Direction hostFront,
   @NotNull Direction hostUp,
   @NotNull Direction moduleFront,
   @NotNull Direction moduleUp,
   @NotNull MultiblockMachineDefinition moduleDefinition
) {
   private static final Direction[][] BLOCK_ROTATION_MAP = buildBlockRotationMap();

   public ModuleRenderInfo(
      @NotNull BlockPos worldOffset, @NotNull Direction moduleFront, @NotNull Direction moduleUp, @NotNull MultiblockMachineDefinition moduleDefinition
   ) {
      this(worldOffset, Direction.NORTH, Direction.UP, moduleFront, moduleUp, moduleDefinition);
   }

   public ModuleRenderInfo(@NotNull BlockPos worldOffset, @NotNull Direction moduleFront, @NotNull MultiblockMachineDefinition moduleDefinition) {
      this(worldOffset, Direction.NORTH, Direction.UP, moduleFront, Direction.UP, moduleDefinition);
   }

   public BlockPos calculateModuleOffset(@NotNull Direction newHostFront, @NotNull Direction newHostUp) {
      if (newHostFront == this.hostFront && newHostUp == this.hostUp) {
         return this.worldOffset;
      }

      BlockPos offset = reverseHorizontalRotation(this.worldOffset, this.hostFront);
      Rotation hostUpRotation = calculateUpRotation(this.hostFront, this.hostUp);
      offset = reverseRotateByFrontAxis(offset, this.hostFront, hostUpRotation);
      offset = applyHorizontalRotation(offset, newHostFront);
      Rotation newUpRotation = calculateUpRotation(newHostFront, newHostUp);
      return rotateByFrontAxis(offset, newHostFront, newUpRotation);
   }

   public Direction calculateModuleFront(@NotNull Direction newHostFront, @NotNull Direction newHostUp) {
      return this.convertDirection(newHostFront, newHostUp, this.moduleFront);
   }

   public Direction calculateModuleUp(@NotNull Direction newHostFront, @NotNull Direction newHostUp) {
      return this.convertDirection(newHostFront, newHostUp, this.moduleUp);
   }

   private Direction convertDirection(@NotNull Direction newHostFront, @NotNull Direction newHostUp, Direction moduleFront) {
      if (newHostFront == this.hostFront && newHostUp == this.hostUp) {
         return moduleFront;
      }

      BlockPos frontVec = new BlockPos(moduleFront.m_122429_(), moduleFront.m_122430_(), moduleFront.m_122431_());
      frontVec = reverseHorizontalRotation(frontVec, this.hostFront);
      Rotation hostUpRotation = calculateUpRotation(this.hostFront, this.hostUp);
      frontVec = reverseRotateByFrontAxis(frontVec, this.hostFront, hostUpRotation);
      frontVec = applyHorizontalRotation(frontVec, newHostFront);
      Rotation newUpRotation = calculateUpRotation(newHostFront, newHostUp);
      frontVec = rotateByFrontAxis(frontVec, newHostFront, newUpRotation);
      return vectorToDirection(frontVec);
   }

   private static BlockPos applyHorizontalRotation(BlockPos offset, Direction front) {
      return switch (front) {
         case NORTH, UP, DOWN -> offset.m_7954_(Rotation.NONE);
         case SOUTH -> offset.m_7954_(Rotation.CLOCKWISE_180);
         case EAST -> offset.m_7954_(Rotation.COUNTERCLOCKWISE_90);
         case WEST -> offset.m_7954_(Rotation.CLOCKWISE_90);
         default -> throw new IncompatibleClassChangeError();
      };
   }

   private static BlockPos reverseHorizontalRotation(BlockPos offset, Direction front) {
      return switch (front) {
         case NORTH, UP, DOWN -> offset.m_7954_(Rotation.NONE);
         case SOUTH -> offset.m_7954_(Rotation.CLOCKWISE_180);
         case EAST -> offset.m_7954_(Rotation.CLOCKWISE_90);
         case WEST -> offset.m_7954_(Rotation.COUNTERCLOCKWISE_90);
         default -> throw new IncompatibleClassChangeError();
      };
   }

   private static Rotation calculateUpRotation(Direction front, Direction up) {
      if (front.m_122434_() == Axis.Y) {
         return switch (up) {
            case NORTH -> Rotation.NONE;
            default -> Rotation.NONE;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
         };
      } else {
         return Rotation.NONE;
      }
   }

   private static BlockPos rotateByFrontAxis(BlockPos pos, Direction front, Rotation rotation) {
      if (front.m_122434_() == Axis.X) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               -pos.m_123341_(), -front.m_122421_().m_122540_() * pos.m_123343_(), front.m_122421_().m_122540_() * -pos.m_123342_()
            );
            case CLOCKWISE_180 -> new BlockPos(-pos.m_123341_(), -pos.m_123342_(), pos.m_123343_());
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -pos.m_123341_(), front.m_122421_().m_122540_() * pos.m_123343_(), front.m_122421_().m_122540_() * pos.m_123342_()
            );
            default -> new BlockPos(-pos.m_123341_(), pos.m_123342_(), -pos.m_123343_());
         };
      } else if (front.m_122434_() == Axis.Y) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               pos.m_123342_(), -front.m_122421_().m_122540_() * pos.m_123343_(), -front.m_122421_().m_122540_() * pos.m_123341_()
            );
            case CLOCKWISE_180 -> new BlockPos(
               front.m_122421_().m_122540_() * pos.m_123341_(), -front.m_122421_().m_122540_() * pos.m_123343_(), pos.m_123342_()
            );
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -pos.m_123342_(), -front.m_122421_().m_122540_() * pos.m_123343_(), front.m_122421_().m_122540_() * pos.m_123341_()
            );
            default -> new BlockPos(-front.m_122421_().m_122540_() * pos.m_123341_(), -front.m_122421_().m_122540_() * pos.m_123343_(), -pos.m_123342_());
         };
      } else if (front.m_122434_() == Axis.Z) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               front.m_122421_().m_122540_() * pos.m_123342_(), -front.m_122421_().m_122540_() * pos.m_123341_(), pos.m_123343_()
            );
            case CLOCKWISE_180 -> new BlockPos(-pos.m_123341_(), -pos.m_123342_(), pos.m_123343_());
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -front.m_122421_().m_122540_() * pos.m_123342_(), front.m_122421_().m_122540_() * pos.m_123341_(), pos.m_123343_()
            );
            default -> pos;
         };
      } else {
         return pos;
      }
   }

   private static BlockPos reverseRotateByFrontAxis(BlockPos pos, Direction front, Rotation rotation) {
      Rotation reverseRotation = switch (rotation) {
         case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
         case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
         case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
         default -> Rotation.NONE;
      };
      return rotateByFrontAxis(pos, front, reverseRotation);
   }

   private static Direction vectorToDirection(BlockPos vec) {
      if (vec.m_123341_() > 0) {
         return Direction.EAST;
      } else if (vec.m_123341_() < 0) {
         return Direction.WEST;
      } else if (vec.m_123342_() > 0) {
         return Direction.UP;
      } else if (vec.m_123342_() < 0) {
         return Direction.DOWN;
      } else if (vec.m_123343_() > 0) {
         return Direction.SOUTH;
      } else {
         return vec.m_123343_() < 0 ? Direction.NORTH : Direction.NORTH;
      }
   }

   public static Direction calculateBlockFacing(Direction originalFace, Direction newModuleFront) {
      return originalFace.m_122434_() == Axis.Y ? originalFace : applyBlockRotation(originalFace, newModuleFront);
   }

   private static Direction applyBlockRotation(Direction face, Direction targetFront) {
      return targetFront != Direction.NORTH && targetFront.m_122434_() != Axis.Y ? BLOCK_ROTATION_MAP[targetFront.ordinal()][face.ordinal()] : face;
   }

   public static BlockPos applyGTTransform(BlockPos localOffset, Direction moduleFront, Direction moduleUp) {
      BlockPos offsetRotated = switch (moduleFront) {
         case NORTH, UP, DOWN -> localOffset.m_7954_(Rotation.NONE);
         case SOUTH -> localOffset.m_7954_(Rotation.CLOCKWISE_180);
         case EAST -> localOffset.m_7954_(Rotation.COUNTERCLOCKWISE_90);
         case WEST -> localOffset.m_7954_(Rotation.CLOCKWISE_90);
         default -> throw new IncompatibleClassChangeError();
      };

      Rotation rotation = switch (moduleUp) {
         case NORTH -> Rotation.NONE;
         default -> Rotation.NONE;
         case SOUTH -> Rotation.CLOCKWISE_180;
         case EAST -> Rotation.CLOCKWISE_90;
         case WEST -> Rotation.COUNTERCLOCKWISE_90;
      };
      return rotateByFrontAxis(offsetRotated, moduleFront, rotation);
   }

   private static Direction[][] buildBlockRotationMap() {
      Direction[][] map = new Direction[6][6];

      for (Direction targetFront : Direction.values()) {
         for (Direction originalFace : Direction.values()) {
            if (originalFace.m_122434_() == Axis.Y) {
               map[targetFront.ordinal()][originalFace.ordinal()] = originalFace;
            } else if (targetFront.m_122434_() != Axis.Y && targetFront != Direction.NORTH) {
               Direction newFace = switch (targetFront) {
                  case SOUTH -> originalFace.m_122424_();
                  case EAST -> originalFace.m_122428_();
                  case WEST -> originalFace.m_122427_();
                  default -> originalFace;
               };
               map[targetFront.ordinal()][originalFace.ordinal()] = newFace;
            } else {
               map[targetFront.ordinal()][originalFace.ordinal()] = originalFace;
            }
         }
      }

      return map;
   }
}
