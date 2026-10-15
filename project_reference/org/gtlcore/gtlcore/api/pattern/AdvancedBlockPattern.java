package org.gtlcore.gtlcore.api.pattern;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.gtlcore.gtlcore.common.item.UltimateTerminalBehavior;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

public class AdvancedBlockPattern extends BlockPattern {
   static Direction[] FACINGS = new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN};
   static Direction[] FACINGS_H = new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST};
   protected final int[][] aisleRepetitions;
   protected final RelativeDirection[] structureDir;
   protected final TraceabilityPredicate[][][] blockMatches;
   protected final int fingerLength;
   protected final int thumbLength;
   protected final int palmLength;
   protected final int[] centerOffset;

   public AdvancedBlockPattern(TraceabilityPredicate[][][] predicatesIn, RelativeDirection[] structureDir, int[][] aisleRepetitions, int[] centerOffset) {
      super(predicatesIn, structureDir, aisleRepetitions, centerOffset);
      this.blockMatches = predicatesIn;
      this.fingerLength = predicatesIn.length;
      this.structureDir = structureDir;
      this.aisleRepetitions = aisleRepetitions;
      if (this.fingerLength > 0) {
         this.thumbLength = predicatesIn[0].length;
         if (this.thumbLength > 0) {
            this.palmLength = predicatesIn[0][0].length;
         } else {
            this.palmLength = 0;
         }
      } else {
         this.thumbLength = 0;
         this.palmLength = 0;
      }

      this.centerOffset = centerOffset;
   }

   public static AdvancedBlockPattern getAdvancedBlockPattern(BlockPattern blockPattern) {
      try {
         Class<?> clazz = BlockPattern.class;
         Field blockMatchesField = clazz.getDeclaredField("blockMatches");
         blockMatchesField.setAccessible(true);
         TraceabilityPredicate[][][] blockMatches = (TraceabilityPredicate[][][])blockMatchesField.get(blockPattern);
         Field structureDirField = clazz.getDeclaredField("structureDir");
         structureDirField.setAccessible(true);
         RelativeDirection[] structureDir = (RelativeDirection[])structureDirField.get(blockPattern);
         Field aisleRepetitionsField = clazz.getDeclaredField("aisleRepetitions");
         aisleRepetitionsField.setAccessible(true);
         int[][] aisleRepetitions = (int[][])aisleRepetitionsField.get(blockPattern);
         Field centerOffsetField = clazz.getDeclaredField("centerOffset");
         centerOffsetField.setAccessible(true);
         int[] centerOffset = (int[])centerOffsetField.get(blockPattern);
         return new AdvancedBlockPattern(blockMatches, structureDir, aisleRepetitions, centerOffset);
      } catch (Exception e) {
         return null;
      }
   }

   private Fluid getFluid(ItemStack stack) {
      if (stack.m_41720_() instanceof BlockItem blockItem && blockItem.m_40614_() instanceof LiquidBlock liquidBlock) {
         return liquidBlock.getFluid();
      } else {
         return stack.m_41720_() instanceof BucketItem bucketItem ? bucketItem.getFluid() : null;
      }
   }

   public void autoBuild(Player player, MultiblockState worldState, UltimateTerminalBehavior.AutoBuildSetting autoBuildSetting) {
      Level world = player.m_9236_();
      int minZ = -this.centerOffset[4];
      this.clearWorldState(worldState);
      IMultiController controller = worldState.getController();
      BlockPos centerPos = controller.self().getPos();
      Direction facing = controller.self().getFrontFacing();
      Direction upwardsFacing = controller.self().getUpwardsFacing();
      boolean isFlipped = autoBuildSetting.isFlipped();
      boolean aeMode = autoBuildSetting.isAeMode();
      GlobalPos boundCoord = autoBuildSetting.getBoundAE();
      IGrid grid = aeMode ? this.findBestGrid(world, boundCoord) : null;
      MEStorage aeInventory = grid != null ? grid.getStorageService().getInventory() : null;
      IActionSource source = IActionSource.ofPlayer(player);
      Object2IntOpenHashMap<SimplePredicate> cacheGlobal = new Object2IntOpenHashMap(worldState.getGlobalCount());
      Object2IntOpenHashMap<SimplePredicate> cacheLayer = new Object2IntOpenHashMap(worldState.getLayerCount());
      Object2ObjectOpenHashMap<BlockPos, Object> blocks = new Object2ObjectOpenHashMap();
      ObjectOpenHashSet<BlockPos> placeBlockPos = new ObjectOpenHashSet();
      blocks.put(centerPos, controller);
      if (controller.isFormed() && autoBuildSetting.isReplaceMode()) {
         controller.onStructureInvalid();
      }

      int[] repeat = new int[this.fingerLength];

      for (int h = 0; h < this.fingerLength; h++) {
         int minH = this.aisleRepetitions[h][0];
         int maxH = this.aisleRepetitions[h][1];
         if (minH != maxH) {
            repeat[h] = Math.max(minH, Math.min(maxH, autoBuildSetting.getRepeatCount()));
         } else {
            repeat[h] = minH;
         }
      }

      int c = 0;
      int z = minZ;

      while (c < this.fingerLength) {
         for (int r = 0; r < repeat[c]; r++) {
            cacheLayer.clear();
            int b = 0;

            for (int y = -this.centerOffset[1]; b < this.thumbLength; y++) {
               int a = 0;

               for (int x = -this.centerOffset[0]; a < this.palmLength; x++) {
                  TraceabilityPredicate predicate = this.blockMatches[c][b][a];
                  label199:
                  if (!predicate.isAny()) {
                     BlockPos pos = this.setActualRelativeOffset(x, y, z, facing, upwardsFacing, isFlipped)
                        .m_7918_(centerPos.m_123341_(), centerPos.m_123342_(), centerPos.m_123343_());
                     this.updateWorldState(worldState, pos, predicate);
                     ItemStack itemStack = null;
                     if (!world.m_46859_(pos)) {
                        Block block = world.m_8055_(pos).m_60734_();
                        if (!autoBuildSetting.getBlocks().contains(block) || !autoBuildSetting.isReplaceMode()) {
                           blocks.put(pos, world.m_8055_(pos));

                           for (SimplePredicate limit : predicate.limited) {
                              limit.testLimited(worldState);
                           }
                           break label199;
                        }

                        itemStack = block.m_5456_().m_7968_();
                     }

                     boolean find = false;
                     BlockInfo[] infos = new BlockInfo[0];

                     for (SimplePredicate limit : predicate.limited) {
                        if ((limit.candidates == null || autoBuildSetting.isPlaceHatch((BlockInfo[])limit.candidates.get())) && limit.minLayerCount > 0) {
                           int curr = cacheLayer.getInt(limit);
                           if (curr < limit.minLayerCount && (limit.maxLayerCount == -1 || curr < limit.maxLayerCount)) {
                              cacheLayer.addTo(limit, 1);
                              infos = limit.candidates == null ? null : (BlockInfo[])limit.candidates.get();
                              find = true;
                              break;
                           }
                        }
                     }

                     if (!find) {
                        for (SimplePredicate limit : predicate.limited) {
                           if ((limit.candidates == null || autoBuildSetting.isPlaceHatch((BlockInfo[])limit.candidates.get())) && limit.minCount > 0) {
                              int curr = cacheGlobal.getInt(limit);
                              if (curr < limit.minCount && (limit.maxCount == -1 || curr < limit.maxCount)) {
                                 cacheGlobal.addTo(limit, 1);
                                 infos = limit.candidates == null ? null : (BlockInfo[])limit.candidates.get();
                                 find = true;
                                 break;
                              }
                           }
                        }
                     }

                     if (!find) {
                        for (SimplePredicate limit : predicate.limited) {
                           if ((limit.candidates == null || autoBuildSetting.isPlaceHatch((BlockInfo[])limit.candidates.get()))
                              && (limit.maxLayerCount == -1 || cacheLayer.getOrDefault(limit, 2147483647) != limit.maxLayerCount)
                              && (limit.maxCount == -1 || cacheGlobal.getOrDefault(limit, 2147483647) != limit.maxCount)) {
                              cacheLayer.addTo(limit, 1);
                              cacheGlobal.addTo(limit, 1);
                              infos = (BlockInfo[])ArrayUtils.addAll(infos, limit.candidates == null ? null : (BlockInfo[])limit.candidates.get());
                           }
                        }

                        for (SimplePredicate common : predicate.common) {
                           if (common.candidates == null || predicate.common.size() <= 1 || autoBuildSetting.isPlaceHatch((BlockInfo[])common.candidates.get())
                              )
                            {
                              infos = (BlockInfo[])ArrayUtils.addAll(infos, common.candidates == null ? null : (BlockInfo[])common.candidates.get());
                           }
                        }
                     }

                     List<ItemStack> candidates = autoBuildSetting.apply(infos);
                     if (autoBuildSetting.isReplaceMode() && itemStack != null) {
                        ItemStack finalItemStack = itemStack;
                        if (candidates.stream().anyMatch(cand -> ItemStack.m_41656_(cand, finalItemStack))) {
                           break label199;
                        }
                     }

                     ItemStack found = null;
                     Fluid fluidToPlace = null;
                     boolean fromAE = false;
                     IItemHandler handler = null;
                     int foundSlot = -1;

                     for (ItemStack candidate : candidates) {
                        Fluid fluid = this.getFluid(candidate);
                        if (fluid != null) {
                           if (aeInventory != null && aeInventory.extract(AEFluidKey.of(fluid), 1000L, Actionable.MODULATE, source) >= 1000L) {
                              fluidToPlace = fluid;
                              fromAE = true;
                              break;
                           }

                           if (fluidToPlace == null && this.extractFluidFromPlayerRecursively(player, fluid)) {
                              fluidToPlace = fluid;
                              break;
                           }
                        } else {
                           if (aeInventory != null && aeInventory.extract(AEItemKey.of(candidate), 1L, Actionable.MODULATE, source) > 0L) {
                              found = candidate.m_41777_();
                              fromAE = true;
                              break;
                           }

                           if (found == null) {
                              Triplet<ItemStack, IItemHandler, Integer> result = foundItem(player, List.of(candidate), item -> true);
                              if (result.getA() != null) {
                                 found = (ItemStack)result.getA();
                                 handler = (IItemHandler)result.getB();
                                 foundSlot = (Integer)result.getC();
                                 break;
                              }
                           }
                        }
                     }

                     label193:
                     if (found != null || fluidToPlace != null) {
                        IItemHandler holderHandler = null;
                        int holderSlot = -1;
                        if (autoBuildSetting.isReplaceMode() && itemStack != null) {
                           ObjectIntPair<IItemHandler> holderResult = this.foundHolderSlot(player, itemStack);
                           holderHandler = (IItemHandler)holderResult.first();
                           holderSlot = holderResult.rightInt();
                           if (holderHandler != null && holderSlot < 0) {
                              break label193;
                           }
                        }

                        if (autoBuildSetting.isReplaceMode() && itemStack != null) {
                           world.m_7471_(pos, true);
                           if (holderHandler != null) {
                              holderHandler.insertItem(holderSlot, itemStack, false);
                           }
                        }

                        if (fluidToPlace != null) {
                           BlockState state = fluidToPlace.m_76145_().m_76188_();
                           if (!state.m_60795_()) {
                              world.m_7731_(pos, state, 3);
                              placeBlockPos.add(pos);
                              blocks.put(pos, state);
                           }
                        } else if (found != null && found.m_41720_() instanceof BlockItem itemBlock) {
                           BlockPlaceContext context = new BlockPlaceContext(
                              world, player, InteractionHand.MAIN_HAND, found, BlockHitResult.m_82426_(player.m_20299_(0.0F), Direction.UP, pos)
                           );
                           InteractionResult interactionResult = itemBlock.m_40576_(context);
                           if (interactionResult != InteractionResult.FAIL) {
                              placeBlockPos.add(pos);
                              if (handler != null && !fromAE) {
                                 handler.extractItem(foundSlot, 1, false);
                              }
                           }

                           if (world.m_7702_(pos) instanceof IMachineBlockEntity machineBlockEntity) {
                              blocks.put(pos, machineBlockEntity.getMetaMachine());
                           } else {
                              blocks.put(pos, world.m_8055_(pos));
                           }
                        }
                     }
                  }

                  a++;
               }

               b++;
            }

            z++;
         }

         c++;
      }

      Direction frontFacing = controller.self().getFrontFacing();
      blocks.object2ObjectEntrySet().fastForEach(entry -> {
         BlockPos posx = (BlockPos)entry.getKey();
         Object block = entry.getValue();
         if (!(block instanceof IMultiController)) {
            if (block instanceof BlockState && placeBlockPos.contains(posx)) {
               this.resetFacing(posx, (BlockState)block, frontFacing, (p, f) -> {
                  Object object = blocks.get(p.m_121945_(f));
                  return object == null || object instanceof BlockState && ((BlockState)object).m_60734_() == Blocks.f_50016_;
               }, state -> world.m_7731_(posx, state, 3));
            } else if (block instanceof MetaMachine machine) {
               this.resetFacing(posx, machine.getBlockState(), frontFacing, (p, f) -> {
                  Object object = blocks.get(p.m_121945_(f));
                  return object != null && !(object instanceof BlockState blockState && blockState.m_60795_()) ? false : machine.isFacingValid(f);
               }, state -> world.m_7731_(posx, state, 3));
            }
         }
      });
   }

   public void dismantleMultiblock(
      IMultiController controller, Player player, int repeatCountSetting, boolean isFlipped, boolean aeMode, @Nullable GlobalPos boundAE
   ) {
      Level level = player.m_9236_();
      if (!level.m_5776_()) {
         BlockPos centerPos = controller.self().getPos();
         Direction facing = controller.self().getFrontFacing();
         Direction upwardsFacing = controller.self().getUpwardsFacing();
         IGrid grid = aeMode ? this.findBestGrid(level, boundAE) : null;
         MEStorage aeInventory = grid != null ? grid.getStorageService().getInventory() : null;
         IActionSource source = IActionSource.ofPlayer(player);
         MultiblockState worldState = controller.getMultiblockState();
         int[] repeat = new int[this.fingerLength];

         for (int h = 0; h < this.fingerLength; h++) {
            int minH = this.aisleRepetitions[h][0];
            int maxH = this.aisleRepetitions[h][1];
            if (minH != maxH) {
               repeat[h] = Math.max(minH, Math.min(maxH, repeatCountSetting));
            } else {
               repeat[h] = minH;
            }
         }

         int minZ = -this.centerOffset[4];
         int c = 0;
         int z = minZ;

         while (c < this.fingerLength) {
            for (int r = 0; r < repeat[c]; r++) {
               int b = 0;

               for (int y = -this.centerOffset[1]; b < this.thumbLength; y++) {
                  int a = 0;

                  for (int x = -this.centerOffset[0]; a < this.palmLength; x++) {
                     TraceabilityPredicate predicate = this.blockMatches[c][b][a];
                     if (!predicate.isAny()) {
                        BlockPos pos = this.setActualRelativeOffset(x, y, z, facing, upwardsFacing, isFlipped)
                           .m_7918_(centerPos.m_123341_(), centerPos.m_123342_(), centerPos.m_123343_());
                        if (!pos.equals(centerPos)) {
                           BlockState blockState = level.m_8055_(pos);
                           if (!blockState.m_60795_()
                              && this.isBlockValid(blockState, pos, predicate, worldState)
                              && !blockState.m_60795_()
                              && level instanceof ServerLevel serverLevel) {
                              if (blockState.m_60734_() instanceof LiquidBlock liquidBlock) {
                                 if (aeInventory != null) {
                                    aeInventory.insert(AEFluidKey.of(liquidBlock.getFluid()), 1000L, Actionable.MODULATE, source);
                                 }

                                 level.m_7731_(pos, Blocks.f_50016_.m_49966_(), 3);
                              } else {
                                 List<ItemStack> drops = Block.m_49874_(blockState, serverLevel, pos, serverLevel.m_7702_(pos), player, player.m_21205_());
                                 level.m_7731_(pos, Blocks.f_50016_.m_49966_(), 3);

                                 for (ItemStack drop : drops) {
                                    ItemStack remainder = drop;
                                    if (aeInventory != null) {
                                       long inserted = aeInventory.insert(AEItemKey.of(remainder), remainder.m_41613_(), Actionable.MODULATE, source);
                                       if (inserted == remainder.m_41613_()) {
                                          remainder = ItemStack.f_41583_;
                                       } else {
                                          remainder.m_41774_((int)inserted);
                                       }
                                    }

                                    if (!remainder.m_41619_()) {
                                       remainder = this.insertIntoNestedItemHandler(player, remainder);
                                       if (!remainder.m_41619_() && player.m_150109_().m_36054_(remainder)) {
                                          remainder = ItemStack.f_41583_;
                                       }

                                       if (!remainder.m_41619_()) {
                                          player.m_36176_(remainder, false);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }

                     a++;
                  }

                  b++;
               }

               z++;
            }

            c++;
         }

         if (controller instanceof WorkableMultiblockMachine machine) {
            machine.onPartUnload();
         }
      }
   }

   private boolean isBlockValid(BlockState current, BlockPos pos, TraceabilityPredicate predicate, MultiblockState tempState) {
      List<SimplePredicate> allPreds = new ArrayList<>();
      if (predicate.common != null) {
         allPreds.addAll(predicate.common);
      }

      if (predicate.limited != null) {
         allPreds.addAll(predicate.limited);
      }

      for (SimplePredicate p : allPreds) {
         BlockInfo[] infos = p.candidates == null ? null : (BlockInfo[])p.candidates.get();
         if (infos != null && infos.length > 0) {
            for (BlockInfo info : infos) {
               if (info.getBlockState().m_60713_(current.m_60734_())) {
                  return true;
               }
            }
         } else {
            this.clearWorldState(tempState);
            this.updateWorldState(tempState, pos, predicate);
            if (p.test(tempState)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean extractFluidFromPlayerRecursively(Player player, Fluid fluid) {
      if (player.m_7500_()) {
         return true;
      }

      LazyOptional<IItemHandler> cap = player.getCapability(ForgeCapabilities.ITEM_HANDLER);
      return !cap.isPresent() ? false : this.extractFluidRecursive((IItemHandler)cap.resolve().orElse(null), fluid);
   }

   private boolean extractFluidRecursive(IItemHandler handler, Fluid fluid) {
      if (handler == null) {
         return false;
      }

      for (int i = 0; i < handler.getSlots(); i++) {
         ItemStack stack = handler.getStackInSlot(i);
         if (!stack.m_41619_()) {
            LazyOptional<IFluidHandlerItem> fluidCap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            if (fluidCap.isPresent()) {
               IFluidHandlerItem fluidHandler = (IFluidHandlerItem)fluidCap.resolve().orElse(null);
               if (fluidHandler != null) {
                  FluidStack simulated = fluidHandler.drain(new FluidStack(fluid, 1000), FluidAction.SIMULATE);
                  if (simulated.getAmount() == 1000) {
                     fluidHandler.drain(new FluidStack(fluid, 1000), FluidAction.EXECUTE);
                     if (handler instanceof IItemHandlerModifiable modifiable) {
                        modifiable.setStackInSlot(i, fluidHandler.getContainer());
                     }

                     return true;
                  }
               }
            }

            LazyOptional<IItemHandler> subItemCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (subItemCap.isPresent() && this.extractFluidRecursive((IItemHandler)subItemCap.resolve().orElse(null), fluid)) {
               return true;
            }
         }
      }

      return false;
   }

   private IGrid findBestGrid(Level level, @Nullable GlobalPos boundCoord) {
      if (boundCoord != null && boundCoord.m_122640_().equals(level.m_46472_())) {
         BlockEntity be = level.m_7702_(boundCoord.m_122646_());
         IGridNode node = getGridNode(be);
         if (node != null && node.getGrid() != null) {
            return node.getGrid();
         }
      }

      return null;
   }

   public static IGridNode getGridNode(BlockEntity be) {
      if (be == null) {
         return null;
      }

      try {
         Method m = be.getClass().getMethod("getGridNode", Direction.class);

         for (Direction direction : Direction.values()) {
            IGridNode node = (IGridNode)m.invoke(be, direction);
            if (node != null) {
               return node;
            }
         }

         return null;
      } catch (Exception e) {
         return null;
      }
   }

   private ItemStack insertIntoNestedItemHandler(Player player, ItemStack stack) {
      LazyOptional<IItemHandler> cap = player.getCapability(ForgeCapabilities.ITEM_HANDLER);
      return cap.isPresent() ? this.insertIntoNestedItemHandlerRecursion((IItemHandler)cap.resolve().orElse(null), stack) : stack;
   }

   private ItemStack insertIntoNestedItemHandlerRecursion(IItemHandler handler, ItemStack stack) {
      if (handler == null) {
         return stack;
      }

      for (int i = 0; i < handler.getSlots(); i++) {
         if (stack.m_41619_()) {
            return stack;
         }

         ItemStack inSlot = handler.getStackInSlot(i);
         if (!inSlot.m_41619_()) {
            LazyOptional<IItemHandler> subCap = inSlot.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (subCap.isPresent()) {
               IItemHandler subHandler = (IItemHandler)subCap.resolve().orElse(null);
               if (subHandler != null) {
                  stack = this.insertIntoNestedItemHandlerRecursion(subHandler, stack);
                  if (stack.m_41619_()) {
                     return stack;
                  }

                  stack = ItemHandlerHelper.insertItemStacked(subHandler, stack, false);
               }
            }
         }
      }

      return stack;
   }

   public static Triplet<ItemStack, IItemHandler, Integer> foundItem(Player player, List<ItemStack> candidates, Predicate<Item> test) {
      ItemStack found = null;
      IItemHandler handler = null;
      int foundSlot = -1;
      if (!player.m_7500_()) {
         IntObjectPair<IItemHandler> foundHandler = getMatchStackWithHandler(candidates, player.getCapability(ForgeCapabilities.ITEM_HANDLER), test);
         if (foundHandler != null) {
            foundSlot = foundHandler.firstInt();
            handler = (IItemHandler)foundHandler.second();
            found = handler.getStackInSlot(foundSlot).m_41777_();
         }
      } else {
         for (ItemStack candidate : candidates) {
            found = candidate.m_41777_();
            if (!found.m_41619_() && test.test(found.m_41720_())) {
               break;
            }

            found = null;
         }
      }

      return new Triplet(found, handler, foundSlot);
   }

   private ObjectIntPair<IItemHandler> foundHolderSlot(Player player, ItemStack coilItemStack) {
      if (!player.m_7500_()) {
         ObjectIntPair<IItemHandler> r = this.findFirstSlot(player.getCapability(ForgeCapabilities.ITEM_HANDLER), coilItemStack);
         if (r != null) {
            return r;
         }
      }

      return ObjectIntPair.of(null, -1);
   }

   private ObjectIntPair<IItemHandler> findFirstSlot(LazyOptional<IItemHandler> cap, ItemStack stack) {
      IItemHandler handler = (IItemHandler)cap.resolve().orElse(null);
      if (handler == null) {
         return null;
      }

      int foundSlot = -1;

      for (int i = 0; i < handler.getSlots(); i++) {
         ItemStack inSlot = handler.getStackInSlot(i);
         LazyOptional<IItemHandler> stackCap = inSlot.getCapability(ForgeCapabilities.ITEM_HANDLER);
         if (stackCap.isPresent()) {
            ObjectIntPair<IItemHandler> rt = this.findFirstSlot(stackCap, stack);
            if (rt != null) {
               return rt;
            }
         } else {
            if (inSlot.m_41619_() && (!(handler instanceof PlayerInvWrapper) || i < 36)) {
               if (foundSlot < 0) {
                  foundSlot = i;
               }
            } else if (ItemStack.m_150942_(stack, inSlot) && inSlot.m_41613_() + 1 <= inSlot.m_41741_()) {
               foundSlot = i;
            }

            if (foundSlot > 0) {
               break;
            }
         }
      }

      return ObjectIntPair.of(handler, foundSlot);
   }

   private void clearWorldState(MultiblockState worldState) {
      try {
         Class<?> clazz = Class.forName("com.gregtechceu.gtceu.api.pattern.MultiblockState");
         Method method = clazz.getDeclaredMethod("clean");
         method.setAccessible(true);
         method.invoke(worldState);
      } catch (Exception var4) {
      }
   }

   private void updateWorldState(MultiblockState worldState, BlockPos posIn, TraceabilityPredicate predicate) {
      try {
         Class<?> clazz = Class.forName("com.gregtechceu.gtceu.api.pattern.MultiblockState");
         Method method = clazz.getDeclaredMethod("update", BlockPos.class, TraceabilityPredicate.class);
         method.setAccessible(true);
         method.invoke(worldState, posIn, predicate);
      } catch (Exception var6) {
      }
   }

   private BlockPos setActualRelativeOffset(int x, int y, int z, Direction facing, Direction upwardsFacing, boolean isFlipped) {
      int[] c0 = new int[]{x, y, z};
      int[] c1 = new int[3];
      if (facing != Direction.UP && facing != Direction.DOWN) {
         for (int i = 0; i < 3; i++) {
            switch (this.structureDir[i].getActualFacing(facing)) {
               case UP:
                  c1[1] = c0[i];
                  break;
               case DOWN:
                  c1[1] = -c0[i];
                  break;
               case WEST:
                  c1[0] = -c0[i];
                  break;
               case EAST:
                  c1[0] = c0[i];
                  break;
               case NORTH:
                  c1[2] = -c0[i];
                  break;
               case SOUTH:
                  c1[2] = c0[i];
            }
         }

         if (upwardsFacing != Direction.WEST && upwardsFacing != Direction.EAST) {
            if (upwardsFacing == Direction.SOUTH) {
               c1[1] = -c1[1];
               if (facing.m_122429_() == 0) {
                  c1[0] = -c1[0];
               } else {
                  c1[2] = -c1[2];
               }
            }
         } else {
            int xOffset = upwardsFacing == Direction.EAST ? facing.m_122427_().m_122429_() : facing.m_122427_().m_122424_().m_122429_();
            int zOffset = upwardsFacing == Direction.EAST ? facing.m_122427_().m_122431_() : facing.m_122427_().m_122424_().m_122431_();
            if (xOffset == 0) {
               int tmp = c1[2];
               c1[2] = zOffset > 0 ? -c1[1] : c1[1];
               c1[1] = zOffset > 0 ? tmp : -tmp;
            } else {
               int tmp = c1[0];
               c1[0] = xOffset > 0 ? -c1[1] : c1[1];
               c1[1] = xOffset > 0 ? tmp : -tmp;
            }
         }

         if (isFlipped) {
            if (upwardsFacing != Direction.NORTH && upwardsFacing != Direction.SOUTH) {
               c1[1] = -c1[1];
            } else if (facing != Direction.NORTH && facing != Direction.SOUTH) {
               c1[2] = -c1[2];
            } else {
               c1[0] = -c1[0];
            }
         }
      } else {
         Direction of = facing == Direction.DOWN ? upwardsFacing : upwardsFacing.m_122424_();

         for (int i = 0; i < 3; i++) {
            switch (this.structureDir[i].getActualFacing(of)) {
               case UP:
                  c1[1] = c0[i];
                  break;
               case DOWN:
                  c1[1] = -c0[i];
                  break;
               case WEST:
                  c1[0] = -c0[i];
                  break;
               case EAST:
                  c1[0] = c0[i];
                  break;
               case NORTH:
                  c1[2] = -c0[i];
                  break;
               case SOUTH:
                  c1[2] = c0[i];
            }
         }

         int xOffset = upwardsFacing.m_122429_();
         int zOffset = upwardsFacing.m_122431_();
         if (xOffset == 0) {
            int tmp = c1[2];
            c1[2] = zOffset > 0 ? c1[1] : -c1[1];
            c1[1] = zOffset > 0 ? -tmp : tmp;
         } else {
            int tmp = c1[0];
            c1[0] = xOffset > 0 ? c1[1] : -c1[1];
            c1[1] = xOffset > 0 ? -tmp : tmp;
         }

         if (isFlipped) {
            if (upwardsFacing != Direction.NORTH && upwardsFacing != Direction.SOUTH) {
               c1[2] = -c1[2];
            } else {
               c1[0] = -c1[0];
            }
         }
      }

      return new BlockPos(c1[0], c1[1], c1[2]);
   }

   @Nullable
   private static IntObjectPair<IItemHandler> getMatchStackWithHandler(List<ItemStack> candidates, LazyOptional<IItemHandler> cap, Predicate<Item> test) {
      IItemHandler handler = (IItemHandler)cap.resolve().orElse(null);
      if (handler == null) {
         return null;
      }

      for (int i = 0; i < handler.getSlots(); i++) {
         ItemStack stack = handler.getStackInSlot(i);
         if (!stack.m_41619_()) {
            LazyOptional<IItemHandler> stackCap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (stackCap.isPresent()) {
               IntObjectPair<IItemHandler> rt = getMatchStackWithHandler(candidates, stackCap, test);
               if (rt != null) {
                  return rt;
               }
            } else if (candidates.stream().anyMatch(candidate -> ItemStack.m_150942_(candidate, stack)) && !stack.m_41619_() && test.test(stack.m_41720_())) {
               return IntObjectPair.of(i, handler);
            }
         }
      }

      return null;
   }

   private void resetFacing(BlockPos pos, BlockState blockState, Direction facing, BiPredicate<BlockPos, Direction> checker, Consumer<BlockState> consumer) {
      if (blockState.m_61138_(BlockStateProperties.f_61372_)) {
         this.tryFacings(
            blockState,
            pos,
            checker,
            consumer,
            BlockStateProperties.f_61372_,
            facing == null ? FACINGS : (Direction[])ArrayUtils.addAll(new Direction[]{facing}, FACINGS)
         );
      } else if (blockState.m_61138_(BlockStateProperties.f_61374_)) {
         this.tryFacings(
            blockState,
            pos,
            checker,
            consumer,
            BlockStateProperties.f_61374_,
            facing != null && facing.m_122434_() != Axis.Y ? (Direction[])ArrayUtils.addAll(new Direction[]{facing}, FACINGS_H) : FACINGS_H
         );
      }
   }

   private void tryFacings(
      BlockState blockState,
      BlockPos pos,
      BiPredicate<BlockPos, Direction> checker,
      Consumer<BlockState> consumer,
      Property<Direction> property,
      Direction[] facings
   ) {
      Direction found = null;

      for (Direction facing : facings) {
         if (checker.test(pos, facing)) {
            found = facing;
            break;
         }
      }

      if (found == null) {
         found = Direction.NORTH;
      }

      consumer.accept((BlockState)blockState.m_61124_(property, found));
   }
}
