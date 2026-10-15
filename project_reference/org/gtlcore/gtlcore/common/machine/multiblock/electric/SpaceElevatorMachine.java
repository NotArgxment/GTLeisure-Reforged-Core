package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import earth.terrarium.adastra.common.menus.base.PlanetsMenuProvider;
import earth.terrarium.botarium.common.menu.MenuHooks;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineHost;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.client.gui.widget.IExtendedClickData;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.utils.MachineUtil;
import org.gtlcore.gtlcore.utils.Registries;
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo;
import org.jetbrains.annotations.NotNull;

public class SpaceElevatorMachine extends TierCasingMachine implements IModularMachineHost<SpaceElevatorMachine>, IMachineLife {
   private final Set<IModularMachineModule<SpaceElevatorMachine, ?>> modules = new ReferenceOpenHashSet();
   private int mam = 0;

   public SpaceElevatorMachine(IMachineBlockEntity holder) {
      super(holder, "SEPMTier");
   }

   @NotNull
   @Override
   public Set<IModularMachineModule<SpaceElevatorMachine, ?>> getModuleSet() {
      return this.modules;
   }

   @Override
   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.safeClearModules();
   }

   public void onMachineRemoved() {
      this.safeClearModules();
   }

   @Override
   public void onStructureFormed() {
      super.onStructureFormed();
      this.safeClearModules();
      this.scanAndConnectModules();
   }

   @Override
   public BlockPos[] getModuleScanPositions() {
      Level level = this.getLevel();
      BlockPos powerCore = this.getPowerCore(this.getPos(), level);
      return powerCore != null
         ? new BlockPos[]{
            powerCore.m_7918_(8, 2, 3),
            powerCore.m_7918_(8, 2, -3),
            powerCore.m_7918_(-8, 2, 3),
            powerCore.m_7918_(-8, 2, -3),
            powerCore.m_7918_(3, 2, 8),
            powerCore.m_7918_(-3, 2, 8),
            powerCore.m_7918_(3, 2, -8),
            powerCore.m_7918_(-3, 2, -8)
         }
         : MachineUtil.EMPTY_POS_ARRAY;
   }

   @NotNull
   @Override
   public List<ModuleRenderInfo> getModulesForRendering() {
      return List.of(
         new ModuleRenderInfo(
            new BlockPos(-3, 0, -5), Direction.NORTH, Direction.UP, Direction.NORTH, Direction.UP, AdvancedMultiBlockMachine.RESOURCE_COLLECTION
         ),
         new ModuleRenderInfo(new BlockPos(-8, 0, 0), Direction.NORTH, Direction.UP, Direction.WEST, Direction.UP, AdvancedMultiBlockMachine.ASSEMBLER_MODULE),
         new ModuleRenderInfo(
            new BlockPos(-8, 0, 6), Direction.NORTH, Direction.UP, Direction.WEST, Direction.UP, AdvancedMultiBlockMachine.RESOURCE_COLLECTION
         ),
         new ModuleRenderInfo(new BlockPos(-3, 0, 11), Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.UP, AdvancedMultiBlockMachine.ASSEMBLER_MODULE),
         new ModuleRenderInfo(
            new BlockPos(3, 0, 11), Direction.NORTH, Direction.UP, Direction.SOUTH, Direction.UP, AdvancedMultiBlockMachine.RESOURCE_COLLECTION
         ),
         new ModuleRenderInfo(new BlockPos(8, 0, 6), Direction.NORTH, Direction.UP, Direction.EAST, Direction.UP, AdvancedMultiBlockMachine.ASSEMBLER_MODULE),
         new ModuleRenderInfo(new BlockPos(8, 0, 0), Direction.NORTH, Direction.UP, Direction.EAST, Direction.UP, AdvancedMultiBlockMachine.RESOURCE_COLLECTION),
         new ModuleRenderInfo(new BlockPos(3, 0, -5), Direction.NORTH, Direction.UP, Direction.NORTH, Direction.UP, AdvancedMultiBlockMachine.ASSEMBLER_MODULE)
      );
   }

   private BlockPos getPowerCore(BlockPos pos, Level level) {
      BlockPos[] coordinates = new BlockPos[]{pos.m_7918_(3, -2, 0), pos.m_7918_(-3, -2, 0), pos.m_7918_(0, -2, 3), pos.m_7918_(0, -2, -3)};

      for (BlockPos blockPos : coordinates) {
         if (Objects.equals(Registries.getBlockId(level.m_8055_(blockPos).m_60734_()), "gtlcore:power_core")) {
            return blockPos;
         }
      }

      return null;
   }

   private int getMAM() {
      if (this.getOffsetTimer() % 20L == 0L) {
         this.mam = this.getFormedModuleCount();
      }

      return this.mam;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L && this.getRecipeLogic().getProgress() > 240) {
         RecipeResult.of(this, RecipeResult.SUCCESS);
         this.getRecipeLogic().setProgress(120);
      }

      return value;
   }

   @NotNull
   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(0, 0, 190, 125);
      group.addWidget(
         new DraggableScrollableWidgetGroup(4, 4, 182, 117)
            .setBackground(this.getScreenTexture())
            .addWidget(new LabelWidget(4, 5, this.self().getBlockState().m_60734_().m_7705_()))
            .addWidget(
               (new ComponentPanelWidget(4, 17, this::addDisplayText) {
                     @OnlyIn(Dist.CLIENT)
                     public boolean mouseClicked(double mouseX, double mouseY, int button) {
                        Style style = this.getStyleUnderMouse(mouseX, mouseY);
                        if (style != null && style.m_131182_() != null) {
                           ClickEvent clickEvent = style.m_131182_();
                           String componentText = clickEvent.m_130623_();
                           if (clickEvent.m_130622_() == Action.OPEN_URL) {
                              if (componentText.startsWith("@!")) {
                                 String rawText = componentText.substring(2);
                                 ClickData clickData = new ClickData();
                                 if (this.clickHandler != null) {
                                    this.clickHandler.accept(rawText, clickData);
                                 }

                                 this.writeClientAction(1, buf -> {
                                    clickData.writeToBuf(buf);
                                    buf.m_130070_(rawText);
                                    if (Minecraft.m_91087_().f_91074_ != null) {
                                       buf.m_130077_(Minecraft.m_91087_().f_91074_.m_20148_());
                                    }
                                 });
                              } else if (componentText.startsWith("@#")) {
                                 String rawText = componentText.substring(2);
                                 Util.m_137581_().m_137646_(rawText);
                              }

                              playButtonClickSound();
                              return true;
                           }
                        }

                        return super.mouseClicked(mouseX, mouseY, button);
                     }

                     public void handleClientAction(int id, FriendlyByteBuf buffer) {
                        if (id == 1) {
                           ClickData clickData = ClickData.readFromBuf(buffer);
                           String componentData = buffer.m_130277_();
                           ((IExtendedClickData)clickData).setUUID(buffer.m_130259_());
                           if (this.clickHandler != null) {
                              this.clickHandler.accept(componentData, clickData);
                           }
                        } else {
                           super.handleClientAction(id, buffer);
                        }
                     }
                  })
                  .textSupplier(Objects.requireNonNull(this.getLevel()).f_46443_ ? null : this::addDisplayText)
                  .setMaxWidthLimit(150)
                  .clickHandler(this::handleDisplayClick)
            )
      );
      group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return group;
   }

   @Override
   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(Component.m_237110_("gtceu.machine.module", new Object[]{this.getMAM()}));
         textList.add(ComponentPanelWidget.withButton(Component.m_237115_("gtceu.machine.space_elevator.set_out"), "set_out"));
      }
   }

   public void handleDisplayClick(String componentData, ClickData clickData) {
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         if (componentData.equals("set_out") && this.getRecipeLogic().isWorking()) {
            UUID playerUUID = ((IExtendedClickData)clickData).getUUID();
            if (playerUUID != null) {
               ServerPlayer player = serverLevel.m_7654_().m_6846_().m_11259_(playerUUID);
               if (player != null) {
                  player.m_20049_("spaceelevatorst");
                  MenuHooks.openMenu(player, new PlanetsMenuProvider());
               }
            }
         }
      }
   }
}
