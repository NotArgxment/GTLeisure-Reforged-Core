package org.gtlreforged.utils.datastructure;

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
   @NotNull MultiblockMachineDefinition moduleDefinition) {

   private static final Direction[][] BLOCK_ROTATION_MAP = buildBlockRotationMap();

   public ModuleRenderInfo(
      @NotNull BlockPos worldOffset, @NotNull Direction moduleFront, @NotNull Direction moduleUp, @NotNull MultiblockMachineDefinition moduleDefinition) {
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

      BlockPos frontVec = new BlockPos(moduleFront.getStepX(), moduleFront.getStepY(), moduleFront.getStepZ());
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
         case NORTH, UP, DOWN -> offset.rotate(Rotation.NONE);
         case SOUTH -> offset.rotate(Rotation.CLOCKWISE_180);
         case EAST -> offset.rotate(Rotation.COUNTERCLOCKWISE_90);
         case WEST -> offset.rotate(Rotation.CLOCKWISE_90);
         default -> throw new IncompatibleClassChangeError();
      };
   }

   private static BlockPos reverseHorizontalRotation(BlockPos offset, Direction front) {
      return switch (front) {
         case NORTH, UP, DOWN -> offset.rotate(Rotation.NONE);
         case SOUTH -> offset.rotate(Rotation.CLOCKWISE_180);
         case EAST -> offset.rotate(Rotation.CLOCKWISE_90);
         case WEST -> offset.rotate(Rotation.COUNTERCLOCKWISE_90);
         default -> throw new IncompatibleClassChangeError();
      };
   }

   private static Rotation calculateUpRotation(Direction front, Direction up) {
      if (front.getAxis() == Axis.Y) {
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
      if (front.getAxis() == Axis.X) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               -pos.getX(), -front.getAxisDirection().getStep() * pos.getZ(), front.getAxisDirection().getStep() * -pos.getY()
            );
            case CLOCKWISE_180 -> new BlockPos(-pos.getX(), -pos.getY(), pos.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -pos.getX(), front.getAxisDirection().getStep() * pos.getZ(), front.getAxisDirection().getStep() * pos.getY()
            );
            default -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());
         };
      } else if (front.getAxis() == Axis.Y) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               pos.getY(), -front.getAxisDirection().getStep() * pos.getZ(), -front.getAxisDirection().getStep() * pos.getX()
            );
            case CLOCKWISE_180 -> new BlockPos(
               front.getAxisDirection().getStep() * pos.getX(), -front.getAxisDirection().getStep() * pos.getZ(), pos.getY()
            );
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -pos.getY(), -front.getAxisDirection().getStep() * pos.getZ(), front.getAxisDirection().getStep() * pos.getX()
            );
            default -> new BlockPos(-front.getAxisDirection().getStep() * pos.getX(), -front.getAxisDirection().getStep() * pos.getZ(), -pos.getY());
         };
      } else if (front.getAxis() == Axis.Z) {
         return switch (rotation) {
            case CLOCKWISE_90 -> new BlockPos(
               front.getAxisDirection().getStep() * pos.getY(), -front.getAxisDirection().getStep() * pos.getX(), pos.getZ()
            );
            case CLOCKWISE_180 -> new BlockPos(-pos.getX(), -pos.getY(), pos.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(
               -front.getAxisDirection().getStep() * pos.getY(), front.getAxisDirection().getStep() * pos.getX(), pos.getZ()
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
      if (vec.getX() > 0) {
         return Direction.EAST;
      } else if (vec.getX() < 0) {
         return Direction.WEST;
      } else if (vec.getY() > 0) {
         return Direction.UP;
      } else if (vec.getY() < 0) {
         return Direction.DOWN;
      } else if (vec.getZ() > 0) {
         return Direction.SOUTH;
      } else {
         return Direction.NORTH;
      }
   }

   public static Direction calculateBlockFacing(Direction originalFace, Direction newModuleFront) {
      return originalFace.getAxis() == Axis.Y ? originalFace : applyBlockRotation(originalFace, newModuleFront);
   }

   private static Direction applyBlockRotation(Direction face, Direction targetFront) {
      return targetFront != Direction.NORTH && targetFront.getAxis() != Axis.Y ? BLOCK_ROTATION_MAP[targetFront.ordinal()][face.ordinal()] : face;
   }

   public static BlockPos applyGTTransform(BlockPos localOffset, Direction moduleFront, Direction moduleUp) {
      BlockPos offsetRotated = switch (moduleFront) {
         case NORTH, UP, DOWN -> localOffset.rotate(Rotation.NONE);
         case SOUTH -> localOffset.rotate(Rotation.CLOCKWISE_180);
         case EAST -> localOffset.rotate(Rotation.COUNTERCLOCKWISE_90);
         case WEST -> localOffset.rotate(Rotation.CLOCKWISE_90);
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
            if (originalFace.getAxis() == Axis.Y) {
               map[targetFront.ordinal()][originalFace.ordinal()] = originalFace;
            } else if (targetFront.getAxis() != Axis.Y && targetFront != Direction.NORTH) {
               Direction newFace = switch (targetFront) {
                  case SOUTH -> originalFace.getOpposite();
                  case EAST -> originalFace.getCounterClockWise();
                  case WEST -> originalFace.getClockWise();
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
