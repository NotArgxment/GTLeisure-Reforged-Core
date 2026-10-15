package org.gtlcore.gtlcore.api.gui;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.lowdragmc.lowdraglib.client.utils.RenderUtils;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture.TextType;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.SceneWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup.ScrollWheelDirection;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.lowdragmc.lowdraglib.utils.BlockPosFace;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.ItemStackKey;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineHost;
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class PatternPreviewWidget extends WidgetGroup {
   private static TrackedDummyWorld LEVEL;
   private static final int REGION_SIZE = 512;
   private static int LAST_OFFSET_INDEX = 0;
   private static final Map<MultiblockMachineDefinition, PatternPreviewWidget.MBPattern[]> CACHE = new Object2ObjectOpenHashMap();
   private final SceneWidget sceneWidget;
   private DraggableScrollableWidgetGroup scrollableWidgetGroup;
   private final MultiblockMachineDefinition controllerDefinition;
   private final PatternPreviewWidget.MBPattern[] patterns;
   private int index;
   private int layer;
   private final List<SlotWidget> slotWidgets;
   private final List<SlotWidget> candidates;
   private boolean showModules = false;

   protected PatternPreviewWidget(MultiblockMachineDefinition controllerDefinition) {
      super(0, 0, 160, 160);

      try {
         this.setClientSideWidget();
         this.controllerDefinition = controllerDefinition;
         this.layer = -1;
         this.slotWidgets = new ObjectArrayList();
         this.candidates = new ObjectArrayList();
         this.addWidget(this.sceneWidget = (new SceneWidget(3, 3, 150, 150, LEVEL) {
            public void renderBlockOverLay(WorldSceneRenderer renderer) {
               PoseStack poseStack = new PoseStack();
               this.hoverPosFace = null;
               this.hoverItem = null;
               if (this.isMouseOverElement(this.currentMouseX, this.currentMouseY)) {
                  BlockHitResult hit = renderer.getLastTraceResult();
                  if (hit != null) {
                     if (this.core.contains(hit.m_82425_())) {
                        this.hoverPosFace = new BlockPosFace(hit.m_82425_(), hit.m_82434_());
                     } else if (!this.useOrtho) {
                        Vector3f hitPos = hit.m_82450_().m_252839_();
                        Level world = renderer.world;
                        Vec3 eyePos = new Vec3(renderer.getEyePos());
                        hitPos.mul(2.0F);
                        Vec3 endPos = new Vec3(hitPos.x - eyePos.f_82479_, hitPos.y - eyePos.f_82480_, hitPos.z - eyePos.f_82481_);
                        double min = 3.4028234663852886E38;

                        for (BlockPos pos : this.core) {
                           BlockState blockState = world.m_8055_(pos);
                           if (blockState.m_60734_() != Blocks.f_50016_) {
                              hit = world.m_45558_(eyePos, endPos, pos, blockState.m_60808_(world, pos), blockState);
                              if (hit != null && hit.m_6662_() != Type.MISS) {
                                 double dist = eyePos.m_82557_(hit.m_82450_());
                                 if (dist < min) {
                                    min = dist;
                                    this.hoverPosFace = new BlockPosFace(hit.m_82425_(), hit.m_82434_());
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if (this.hoverPosFace != null) {
                  BlockState state = this.getDummyWorld().m_8055_(this.hoverPosFace.pos);
                  this.hoverItem = state.m_60734_().m_7397_(this.getDummyWorld(), this.hoverPosFace.pos, state);
               }

               BlockPosFace tmp = this.dragging ? this.clickPosFace : this.hoverPosFace;
               if (this.selectedPosFace != null || tmp != null) {
                  if (this.selectedPosFace != null && this.renderFacing) {
                     this.drawFacingBorder(poseStack, this.selectedPosFace, -16711936);
                  }

                  if (tmp != null && !tmp.equals(this.selectedPosFace) && this.renderFacing) {
                     this.drawFacingBorder(poseStack, tmp, -1);
                  }
               }

               if (this.selectedPosFace != null && this.renderSelect) {
                  RenderUtils.renderBlockOverLay(poseStack, this.selectedPosFace.pos, 0.6F, 0.0F, 0.0F, 1.03F);
               }

               if (this.afterWorldRender != null) {
                  this.afterWorldRender.accept(this);
               }
            }
         }).setOnSelected(this::onPosSelected).setRenderFacing(false).setRenderFacing(false));
         this.refreshScrollableWidget();
         if (ConfigHolder.INSTANCE.client.useVBO) {
            if (!RenderSystem.isOnRenderThread()) {
               RenderSystem.recordRenderCall(this.sceneWidget::useCacheBuffer);
            } else {
               this.sceneWidget.useCacheBuffer();
            }
         }

         this.addWidget(
            new ImageWidget(
               3, 3, 160, 10, new TextTexture(controllerDefinition.getDescriptionId(), -1).setType(TextType.ROLL).setWidth(170).setDropShadow(true)
            )
         );
         this.patterns = CACHE.computeIfAbsent(
            controllerDefinition,
            definition -> definition.getMatchingShapes()
               .stream()
               .map(this::initializePattern)
               .filter(Objects::nonNull)
               .toArray(PatternPreviewWidget.MBPattern[]::new)
         );
         this.addWidget(
            new ButtonWidget(
                  138,
                  30,
                  18,
                  18,
                  new GuiTextureGroup(new IGuiTexture[]{ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> "P:" + this.index)}),
                  x -> this.setPage(this.index + 1 >= this.patterns.length ? 0 : this.index + 1, x)
               )
               .setHoverBorderTexture(1, -1)
         );
         this.addWidget(
            new ButtonWidget(
                  138,
                  50,
                  18,
                  18,
                  new GuiTextureGroup(
                     new IGuiTexture[]{ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> this.layer >= 0 ? "L:" + this.layer : "ALL")}
                  ),
                  this::updateLayer
               )
               .setHoverBorderTexture(1, -1)
         );
         if (this.patterns.length != 0 && this.patterns[0].hasModule) {
            this.addWidget(
               new ButtonWidget(
                     138,
                     70,
                     18,
                     18,
                     new GuiTextureGroup(
                        new IGuiTexture[]{ColorPattern.T_GRAY.rectTexture(), new TextTexture("1").setSupplier(() -> this.showModules ? "M:ON" : "M:OFF")}
                     ),
                     x -> this.toggleModules()
                  )
                  .setHoverBorderTexture(1, -1)
                  .setHoverTooltips(new Component[]{Component.m_237115_("gui.gtlcore.module.show")})
            );
         }

         this.setPage(0, null);
      } catch (Exception e) {
         throw new IllegalStateException("The jei preview creation for the Multi Block Machine [" + controllerDefinition.getId().toString() + "] failed! ");
      }
   }

   private void updateLayer(ClickData cd) {
      PatternPreviewWidget.MBPattern pattern = this.patterns[this.index];
      if (this.layer + 1 >= -1 && this.layer + 1 <= pattern.maxY - pattern.minY && !cd.isShiftClick) {
         this.layer++;
         if (pattern.controllerBase.isFormed()) {
            this.onFormedSwitch(false);
         }
      } else {
         this.layer = -1;
         if (!pattern.controllerBase.isFormed()) {
            this.onFormedSwitch(true);
         }
      }

      this.setupScene(pattern);
   }

   private void setupScene(PatternPreviewWidget.MBPattern pattern) {
      Stream<BlockPos> stream = pattern.blockMap
         .keySet()
         .stream()
         .filter(pos -> (this.layer == -1 || this.layer + pattern.minY == pos.m_123342_()) && (this.showModules || !pattern.moduleOnlyBlocks.contains(pos)));
      if (pattern.controllerBase.isFormed()) {
         LongSet modelDisabled = (LongSet)pattern.controllerBase.getMultiblockState().getMatchContext().getOrDefault("renderMask", LongSets.EMPTY_SET);
         if (!modelDisabled.isEmpty()) {
            stream = stream.filter(pos -> !modelDisabled.contains(pos.m_121878_()));
         }
      }

      this.sceneWidget.setRenderedCore(stream.toList(), null);
   }

   public static PatternPreviewWidget getPatternWidget(MultiblockMachineDefinition controllerDefinition) {
      if (LEVEL == null) {
         if (Minecraft.m_91087_().f_91073_ == null) {
            GTCEu.LOGGER.error("Try to init pattern previews before level load");
            throw new IllegalStateException();
         }

         LEVEL = new TrackedDummyWorld();
      }

      return new PatternPreviewWidget(controllerDefinition);
   }

   public void setPage(int index, ClickData x) {
      if (x != null) {
         if (x.isShiftClick) {
            index = 0;
         } else if (x.isCtrlClick) {
            index = this.patterns.length - 1;
         }
      }

      if (index < this.patterns.length && index >= 0) {
         this.index = index;
         this.layer = -1;
         PatternPreviewWidget.MBPattern pattern = this.patterns[index];
         this.setupScene(pattern);
         this.refreshScrollableWidget();
         this.slotWidgets.clear();
         CycleItemStackHandler itemHandler = new CycleItemStackHandler(pattern.parts);

         for (int i = 0; i < pattern.parts.size(); i++) {
            ItemStack itemStack = pattern.parts.get(i).get(0);
            SlotWidget widget = new SlotWidget(itemHandler, i, 4 + i * 18, 0, false, false)
               .setBackgroundTexture(ColorPattern.T_GRAY.rectTexture())
               .setIngredientIO(IngredientIO.INPUT)
               .setOnAddedTooltips(
                  (w, tips) -> tips.add(
                     Component.m_237115_("gtceu.machine.quantum_chest.items_stored")
                        .m_130940_(ChatFormatting.DARK_AQUA)
                        .m_7220_(Component.m_237113_(String.valueOf(itemStack.m_41613_())))
                  )
               );
            this.slotWidgets.add(widget);
            this.scrollableWidgetGroup.addWidget(widget);
         }
      }
   }

   private void refreshScrollableWidget() {
      if (this.scrollableWidgetGroup != null) {
         this.removeWidget(this.scrollableWidgetGroup);
      }

      DraggableScrollableWidgetGroup result = new DraggableScrollableWidgetGroup(3, 132, 154, 22)
         .setXScrollBarHeight(4)
         .setXBarStyle(GuiTextures.SLIDER_BACKGROUND, GuiTextures.BUTTON)
         .setScrollable(true)
         .setDraggable(true);
      result.setScrollWheelDirection(ScrollWheelDirection.HORIZONTAL);
      result.setScrollYOffset(0);
      this.scrollableWidgetGroup = result;
      this.addWidget(this.scrollableWidgetGroup);
   }

   private void toggleModules() {
      this.showModules = !this.showModules;
      this.setPage(this.index, null);
   }

   private void onFormedSwitch(boolean isFormed) {
      PatternPreviewWidget.MBPattern pattern = this.patterns[this.index];
      IMultiController controllerBase = pattern.controllerBase;
      if (isFormed) {
         this.layer = -1;
         this.loadControllerFormed(pattern.blockMap.keySet(), controllerBase);
      } else {
         this.sceneWidget.setRenderedCore(pattern.blockMap.keySet(), null);
         controllerBase.onStructureInvalid();
      }
   }

   private void onPosSelected(BlockPos pos, Direction facing) {
      if (this.index < this.patterns.length && this.index >= 0) {
         PatternPreviewWidget.MBPattern pattern = this.patterns[this.index];
         TraceabilityPredicate predicate = pattern.predicateMap.get(pos);
         if (predicate != null) {
            ObjectOpenHashSet<SimplePredicate> predicates = new ObjectOpenHashSet();
            predicates.addAll(predicate.common);
            predicates.addAll(predicate.limited);
            predicates.remove(null);
            predicates.removeIf(p -> p.candidates == null);

            for (SlotWidget candidate : this.candidates) {
               this.removeWidget(candidate);
            }

            this.candidates.clear();
            List<List<ItemStack>> candidateStacks = new ObjectArrayList();
            List<List<Component>> predicateTips = new ObjectArrayList();
            ObjectIterator itemHandler = predicates.iterator();

            while (itemHandler.hasNext()) {
               SimplePredicate simplePredicate = (SimplePredicate)itemHandler.next();
               List<ItemStack> itemStacks = simplePredicate.getCandidates();
               if (!itemStacks.isEmpty()) {
                  candidateStacks.add(itemStacks);
                  predicateTips.add(simplePredicate.getToolTips(predicate));
               }
            }

            CycleItemStackHandler itemHandlerx = new CycleItemStackHandler(candidateStacks);
            int maxCol = (160 - ((this.slotWidgets.size() - 1) / 9 + 1) * 18 - 35) % 18;

            for (int i = 0; i < candidateStacks.size(); i++) {
               int finalI = i;
               SlotWidget widget = new SlotWidget(itemHandlerx, i, 3 + i / maxCol * 18, 3 + i % maxCol * 18, false, false)
                  .setIngredientIO(IngredientIO.INPUT)
                  .setBackgroundTexture(new ColorRectTexture(1342177279))
                  .setOnAddedTooltips((slot, list) -> list.addAll(predicateTips.get(finalI)));
               this.candidates.add(widget);
               this.addWidget(widget);
            }
         }
      }
   }

   public static BlockPos locateNextRegion() {
      int currentIndex = LAST_OFFSET_INDEX++;
      int x = 0;
      int z = 0;
      if (currentIndex > 0) {
         int v = (int)(Mth.m_14116_(currentIndex + 0.25F) - 0.5F);
         int nextV = v + 1;
         int spiralBaseIndex = v * nextV;
         int flipFlop = (v & 1) * 2 - 1;
         int offset = flipFlop * nextV / 2;
         x += offset;
         z += offset;
         int cornerIndex = spiralBaseIndex + nextV;
         if (currentIndex < cornerIndex) {
            x -= flipFlop * (currentIndex - spiralBaseIndex + 1);
         } else {
            x -= flipFlop * nextV;
            z -= flipFlop * (currentIndex - cornerIndex + 1);
         }
      }

      return new BlockPos(x * 512, 50, z * 512);
   }

   public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      RenderSystem.enableBlend();
      super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
   }

   private PatternPreviewWidget.MBPattern initializePattern(MultiblockShapeInfo shapeInfo) {
      if (shapeInfo == null) {
         return null;
      }

      Map<BlockPos, BlockInfo> blockMap = new Object2ObjectOpenHashMap();
      Set<BlockPos> moduleOnlyBlocks = new ObjectOpenHashSet();
      IMultiController controllerBase = null;
      BlockPos controllerPosInShape = null;
      BlockPos multiPos = locateNextRegion();
      BlockInfo[][][] blocks = shapeInfo.getBlocks();

      for (int x = 0; x < blocks.length; x++) {
         BlockInfo[][] aisle = blocks[x];

         for (int y = 0; y < aisle.length; y++) {
            BlockInfo[] column = aisle[y];

            for (int z = 0; z < column.length; z++) {
               BlockInfo block = column[z];
               if (block != null) {
                  BlockState blockState = block.getBlockState();
                  BlockPos pos = multiPos.m_7918_(x, y, z);
                  if (block.getBlockEntity(pos) instanceof IMachineBlockEntity holder
                     && holder.getMetaMachine() instanceof IMultiController controller
                     && this.controllerDefinition == controller.self().getDefinition()) {
                     holder.getSelf().m_142339_(LEVEL);
                     controllerBase = controller;
                     controllerPosInShape = new BlockPos(x, y, z);
                  }

                  blockMap.put(pos, BlockInfo.fromBlockState(blockState));
               }
            }
         }
      }

      LEVEL.addBlocks(blockMap);
      Map<BlockPos, BlockInfo> hostBlockMap = new Object2ObjectOpenHashMap(blockMap);
      BlockPos controllerWorldPos = multiPos;
      Map<BlockPos, TraceabilityPredicate> predicateMap = new Object2ObjectOpenHashMap();
      Map<ItemStackKey, PatternPreviewWidget.PartInfo> partsMap = new Object2ObjectOpenHashMap();
      if (controllerBase != null) {
         LEVEL.setInnerBlockEntity(controllerBase.self().holder.getSelf());
         if (controllerPosInShape != null) {
            controllerWorldPos = multiPos.m_121955_(controllerPosInShape);
         }

         this.loadControllerFormed(blockMap.keySet(), controllerBase);
         Map<BlockPos, TraceabilityPredicate> hostPredicates = (Map<BlockPos, TraceabilityPredicate>)controllerBase.getMultiblockState()
            .getMatchContext()
            .get("predicates");
         if (hostPredicates != null) {
            predicateMap = new Object2ObjectOpenHashMap(hostPredicates);
         }

         partsMap = this.gatherBlockDrops(blockMap);
         if (controllerBase instanceof IModularMachineHost<?> host) {
            List<IMultiController> moduleControllers = initializeModules(host, controllerWorldPos, blockMap, hostBlockMap, moduleOnlyBlocks);
            Set<BlockPos> modulePos = new ObjectOpenHashSet();

            for (IMultiController moduleController : moduleControllers) {
               try {
                  modulePos.add(moduleController.self().getPos());
                  this.mergeModulePredicates(moduleController, predicateMap);
               } catch (Exception e) {
                  GTCEu.LOGGER.warn("Failed to merge module predicates in pattern preview", e);
               }
            }

            hostBlockMap.forEach((pos, block) -> {
               if (!modulePos.contains(pos)) {
                  LEVEL.addBlock(pos, block);
               }
            });
         }
      }

      List<List<ItemStack>> parts = partsMap.values().stream().sorted((one, two) -> {
         if (one.isController) {
            return -1;
         } else if (two.isController) {
            return 1;
         } else if (one.isTile && !two.isTile) {
            return -1;
         } else if (two.isTile && !one.isTile) {
            return 1;
         } else {
            return one.blockId != two.blockId ? two.blockId - one.blockId : two.amount - one.amount;
         }
      }).map(PatternPreviewWidget.PartInfo::getItemStack).filter(list -> !list.isEmpty()).collect(Collectors.toList());
      return controllerBase == null ? null : new PatternPreviewWidget.MBPattern(blockMap, moduleOnlyBlocks, parts, predicateMap, controllerBase);
   }

   private void loadControllerFormed(Collection<BlockPos> positions, IMultiController controllerBase) {
      BlockPattern pattern = controllerBase.getPattern();
      if (pattern != null && pattern.checkPatternAt(controllerBase.getMultiblockState(), true)) {
         controllerBase.onStructureFormed();
      }

      if (controllerBase.isFormed()) {
         LongSet modelDisabled = (LongSet)controllerBase.getMultiblockState().getMatchContext().getOrDefault("renderMask", LongSets.EMPTY_SET);
         if (!modelDisabled.isEmpty()) {
            positions = new HashSet<>(positions);
            positions.removeIf(pos -> modelDisabled.contains(pos.m_121878_()));
         }

         this.sceneWidget.setRenderedCore(positions, null);
      }
   }

   private static void loadModuleFormed(IMultiController moduleController) {
      BlockPattern modulePattern = moduleController.getPattern();
      BlockState controllerState = LEVEL.m_8055_(moduleController.self().getPos());
      Direction controllerFacing = Direction.NORTH;
      Direction controllerUp = Direction.UP;
      if (controllerState.m_60734_() instanceof MetaMachineBlock machineBlock) {
         controllerFacing = machineBlock.getFrontFacing(controllerState);
         if (controllerState.m_61138_(IMachineBlock.UPWARDS_FACING_PROPERTY)) {
            controllerUp = (Direction)controllerState.m_61143_(IMachineBlock.UPWARDS_FACING_PROPERTY);
         }
      }

      if (modulePattern != null
         && modulePattern.checkPatternAt(moduleController.getMultiblockState(), moduleController.self().getPos(), controllerFacing, controllerUp, false, true)) {
         moduleController.onStructureFormed();
      }
   }

   private static List<IMultiController> initializeModules(
      IModularMachineHost<?> host,
      BlockPos hostControllerPos,
      Map<BlockPos, BlockInfo> blockMap,
      Map<BlockPos, BlockInfo> hostBlockMap,
      Set<BlockPos> moduleBlocks
   ) {
      List<ModuleRenderInfo> modules = host.getModulesForRendering();
      if (modules.isEmpty()) {
         return List.of();
      }

      List<IMultiController> moduleControllers = new ObjectArrayList();
      Direction hostFront = Direction.NORTH;
      Direction hostUp = Direction.UP;
      if (LEVEL.m_7702_(hostControllerPos) instanceof IMachineBlockEntity IMBE
         && IMBE.getMetaMachine() instanceof MultiblockControllerMachine controllerMachine) {
         hostFront = controllerMachine.getFrontFacing();
         hostUp = controllerMachine.getUpwardsFacing();
      }

      for (ModuleRenderInfo moduleInfo : modules) {
         try {
            MultiblockMachineDefinition moduleDef = moduleInfo.moduleDefinition();
            List<MultiblockShapeInfo> moduleShapes = moduleDef.getMatchingShapes();
            if (!moduleShapes.isEmpty()) {
               MultiblockShapeInfo moduleShapeInfo = moduleShapes.get(0);
               BlockPos moduleOffset = moduleInfo.calculateModuleOffset(hostFront, hostUp);
               BlockPos moduleWorldPos = hostControllerPos.m_121955_(moduleOffset);
               Direction moduleFront = moduleInfo.calculateModuleFront(hostFront, hostUp);
               Direction moduleUp = moduleInfo.calculateModuleUp(hostFront, hostUp);
               IMultiController result = initializeModule(
                  moduleDef, moduleShapeInfo, moduleWorldPos, moduleFront, moduleUp, blockMap, hostBlockMap, moduleBlocks
               );
               if (result != null) {
                  moduleControllers.add(result);
               }
            }
         } catch (Exception e) {
            GTCEu.LOGGER.warn("Failed to render module in pattern preview", e);
         }
      }

      return moduleControllers;
   }

   @Nullable
   private static IMultiController initializeModule(
      MultiblockMachineDefinition moduleDefinition,
      MultiblockShapeInfo moduleShapeInfo,
      BlockPos moduleControllerWorldPos,
      Direction moduleFront,
      Direction moduleUp,
      Map<BlockPos, BlockInfo> blockMap,
      Map<BlockPos, BlockInfo> hostBlockMap,
      Set<BlockPos> moduleOnlyBlocks
   ) {
      BlockInfo[][][] blocks = moduleShapeInfo.getBlocks();
      BlockPos moduleControllerPosInShape = null;
      List<Pair<BlockPos, BlockInfo>> pairs = new ObjectArrayList();

      for (int x = 0; x < blocks.length; x++) {
         BlockInfo[][] aisle = blocks[x];

         for (int y = 0; y < aisle.length; y++) {
            BlockInfo[] column = aisle[y];

            for (int z = 0; z < column.length; z++) {
               BlockInfo info = column[z];
               if (info != null) {
                  BlockPos localPos = new BlockPos(x, y, z);
                  pairs.add(Pair.of(localPos, info));
                  if (info.getBlockState().m_60734_() instanceof MetaMachineBlock machineBlock && machineBlock.definition == moduleDefinition) {
                     moduleControllerPosInShape = new BlockPos(x, y, z);
                  }
               }
            }
         }
      }

      if (moduleControllerPosInShape == null) {
         return null;
      }

      IMultiController moduleController = null;
      Map<BlockPos, BlockInfo> moduleBlockMap = new Object2ObjectOpenHashMap();

      for (Pair<BlockPos, BlockInfo> pair : pairs) {
         BlockPos localPos = (BlockPos)pair.left();
         BlockInfo info = (BlockInfo)pair.right();
         BlockState blockState = info.getBlockState();
         BlockPos offsetLocal = localPos.m_121996_(moduleControllerPosInShape);
         BlockPos offsetRotated = ModuleRenderInfo.applyGTTransform(offsetLocal, moduleFront, moduleUp);
         BlockPos worldPos = moduleControllerWorldPos.m_121955_(offsetRotated);
         if (blockState.m_60734_() instanceof MetaMachineBlock machineBlock) {
            RotationState rotationState = machineBlock.getRotationState();
            if (rotationState != RotationState.NONE) {
               Direction originalFace = (Direction)blockState.m_61143_(rotationState.property);
               Direction newFace;
               if (localPos.equals(moduleControllerPosInShape)) {
                  newFace = moduleFront;
               } else {
                  newFace = ModuleRenderInfo.calculateBlockFacing(originalFace, moduleFront);
               }

               if (rotationState.test(newFace)) {
                  blockState = (BlockState)blockState.m_61124_(rotationState.property, newFace);
               }
            }
         }

         if (info.getBlockEntity(worldPos) instanceof IMachineBlockEntity holder) {
            holder.getSelf().m_142339_(LEVEL);
            if (localPos.equals(moduleControllerPosInShape) && holder.getMetaMachine() instanceof IMultiController controller) {
               moduleController = controller;
            }
         }

         BlockInfo blockInfo = BlockInfo.fromBlockState(blockState);
         moduleBlockMap.put(worldPos, blockInfo);
         if (!hostBlockMap.containsKey(worldPos)) {
            moduleOnlyBlocks.add(worldPos);
            blockMap.put(worldPos, blockInfo);
         }
      }

      LEVEL.addBlocks(moduleBlockMap);
      if (moduleController != null) {
         LEVEL.setInnerBlockEntity(moduleController.self().holder.getSelf());
         loadModuleFormed(moduleController);
      }

      return moduleController;
   }

   private void mergeModulePredicates(IMultiController moduleController, Map<BlockPos, TraceabilityPredicate> hostPredicateMap) {
      Map<BlockPos, TraceabilityPredicate> modulePredicateMap = (Map<BlockPos, TraceabilityPredicate>)moduleController.getMultiblockState()
         .getMatchContext()
         .get("predicates");
      if (modulePredicateMap != null && !modulePredicateMap.isEmpty()) {
         for (Entry<BlockPos, TraceabilityPredicate> entry : modulePredicateMap.entrySet()) {
            hostPredicateMap.putIfAbsent(entry.getKey(), entry.getValue());
         }
      }
   }

   private Map<ItemStackKey, PatternPreviewWidget.PartInfo> gatherBlockDrops(Map<BlockPos, BlockInfo> blocks) {
      Map<ItemStackKey, PatternPreviewWidget.PartInfo> partsMap = new Object2ObjectOpenHashMap();

      for (Entry<BlockPos, BlockInfo> entry : blocks.entrySet()) {
         BlockPos pos = entry.getKey();
         BlockState blockState = LEVEL.m_8055_(pos);
         ItemStack itemStack = blockState.m_60734_().m_7397_(LEVEL, pos, blockState);
         if (itemStack.m_41619_() && !blockState.m_60819_().m_76178_()) {
            Fluid fluid = blockState.m_60819_().m_76152_();
            itemStack = fluid.m_6859_().m_7968_();
         }

         ItemStackKey itemStackKey = new ItemStackKey(new ItemStack[]{itemStack});
         partsMap.computeIfAbsent(itemStackKey, key -> new PatternPreviewWidget.PartInfo(key, entry.getValue())).amount++;
      }

      return partsMap;
   }

   private static class MBPattern {
      @NotNull
      final List<List<ItemStack>> parts;
      @NotNull
      final Map<BlockPos, TraceabilityPredicate> predicateMap;
      @NotNull
      final Map<BlockPos, BlockInfo> blockMap;
      @NotNull
      final Set<BlockPos> moduleOnlyBlocks;
      @NotNull
      final IMultiController controllerBase;
      final int maxY;
      final int minY;
      final boolean hasModule;

      public MBPattern(
         @NotNull Map<BlockPos, BlockInfo> blockMap,
         @NotNull Set<BlockPos> moduleOnlyBlocks,
         @NotNull List<List<ItemStack>> parts,
         @NotNull Map<BlockPos, TraceabilityPredicate> predicateMap,
         @NotNull IMultiController controllerBase
      ) {
         this.parts = parts;
         this.blockMap = blockMap;
         this.predicateMap = predicateMap;
         this.controllerBase = controllerBase;
         this.moduleOnlyBlocks = moduleOnlyBlocks;
         int min = 2147483647;
         int max = -2147483648;

         for (BlockPos pos : blockMap.keySet()) {
            min = Math.min(min, pos.m_123342_());
            max = Math.max(max, pos.m_123342_());
         }

         this.minY = min;
         this.maxY = max;
         this.hasModule = controllerBase instanceof IModularMachineHost;
      }
   }

   private static class PartInfo {
      final ItemStackKey itemStackKey;
      boolean isController = false;
      boolean isTile;
      final int blockId;
      int amount = 0;

      PartInfo(ItemStackKey itemStackKey, BlockInfo blockInfo) {
         this.itemStackKey = itemStackKey;
         this.blockId = Block.m_49956_(blockInfo.getBlockState());
         this.isTile = blockInfo.hasBlockEntity();
         if (blockInfo.getBlockState().m_60734_() instanceof MetaMachineBlock block && block.definition instanceof MultiblockMachineDefinition) {
            this.isController = true;
         }
      }

      public List<ItemStack> getItemStack() {
         return Arrays.stream(this.itemStackKey.getItemStack()).map(itemStack -> {
            ItemStack item = itemStack.m_41777_();
            item.m_41764_(this.amount);
            return item;
         }).filter(item -> !item.m_41619_()).toList();
      }
   }
}
