package org.gtlcore.gtlcore.common.item;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.hepdd.gtmthings.api.gui.widget.TerminalInputWidget;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory.HeldItemHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.SwitchWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.common.util.Lazy;
import org.gtlcore.gtlcore.api.gui.BlockMapSelectorWidget;
import org.gtlcore.gtlcore.api.gui.ExtendLabelWidget;
import org.gtlcore.gtlcore.api.pattern.AdvancedBlockPattern;
import org.gtlcore.gtlcore.common.block.BlockMap;

public class UltimateTerminalBehavior implements IItemUIFactory {
   public InteractionResult useOn(UseOnContext context) {
      if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
         Level level = context.m_43725_();
         BlockPos blockPos = context.m_8083_();
         if (!level.f_46443_ && AdvancedBlockPattern.getGridNode(level.m_7702_(blockPos)) != null) {
            ItemStack handItem = context.m_43723_().m_21205_();
            CompoundTag tag = handItem.m_41784_();
            CompoundTag AEPos = new CompoundTag();
            AEPos.m_128405_("X", blockPos.m_123341_());
            AEPos.m_128405_("Y", blockPos.m_123342_());
            AEPos.m_128405_("Z", blockPos.m_123343_());
            AEPos.m_128359_("dim", level.m_46472_().m_135782_().toString());
            tag.m_128365_("BoundAE", AEPos);
            handItem.m_41751_(tag);
            context.m_43723_().m_213846_(Component.m_237110_("gtlcore.terminal.ae_bound", new Object[]{blockPos.m_123344_()}));
            return InteractionResult.SUCCESS;
         }

         MetaMachine metaMachine = MetaMachine.getMachine(level, blockPos);
         if (context.m_43723_() != null && !level.m_5776_() && metaMachine instanceof IMultiController controller) {
            UltimateTerminalBehavior.AutoBuildSetting autoBuildSetting = this.getAutoBuildSetting(context.m_43723_().m_21205_());
            if (autoBuildSetting.isDismantleMode()) {
               this.dismantleMultiblock(controller, context.m_43723_());
            } else if (!controller.isFormed()) {
               Objects.requireNonNull(AdvancedBlockPattern.getAdvancedBlockPattern(controller.getPattern()))
                  .autoBuild(context.m_43723_(), controller.getMultiblockState(), autoBuildSetting);
            } else if (metaMachine instanceof WorkableMultiblockMachine machine && autoBuildSetting.isReplaceMode()) {
               Objects.requireNonNull(AdvancedBlockPattern.getAdvancedBlockPattern(controller.getPattern()))
                  .autoBuild(context.m_43723_(), controller.getMultiblockState(), autoBuildSetting);
               machine.onPartUnload();
            }
         }

         return InteractionResult.m_19078_(level.f_46443_);
      } else {
         return InteractionResult.PASS;
      }
   }

   private UltimateTerminalBehavior.AutoBuildSetting getAutoBuildSetting(ItemStack mainHandItem) {
      UltimateTerminalBehavior.AutoBuildSetting autoBuildSetting = new UltimateTerminalBehavior.AutoBuildSetting();
      CompoundTag tag = mainHandItem.m_41784_();
      if (!tag.m_128456_()) {
         autoBuildSetting.setTier(tag.m_128451_("Tier"));
         autoBuildSetting.setRepeatCount(tag.m_128451_("RepeatCount"));
         autoBuildSetting.setNoHatchMode(tag.m_128471_("NoHatchMode"));
         autoBuildSetting.setReplaceMode(tag.m_128471_("ReplaceMode"));
         autoBuildSetting.setFlipped(tag.m_128471_("IsFlipped"));
         autoBuildSetting.setDismantleMode(tag.m_128471_("DismantleMode"));
         autoBuildSetting.setAeMode(tag.m_128471_("AEMode"));
         if (tag.m_128441_("BoundAE")) {
            CompoundTag AEPos = tag.m_128469_("BoundAE");
            int x = AEPos.m_128451_("X");
            int y = AEPos.m_128451_("Y");
            int z = AEPos.m_128451_("Z");
            ResourceKey<Level> dimension = ResourceKey.m_135785_(Registries.f_256858_, new ResourceLocation(AEPos.m_128461_("dim")));
            autoBuildSetting.setBoundAE(GlobalPos.m_122643_(dimension, new BlockPos(x, y, z)));
         }

         String block = tag.m_128461_("blocks");
         if (!block.isEmpty()) {
            autoBuildSetting.tierBlock = (Block[])((Lazy)BlockMap.tierBlockMap.get(block)).get();
            autoBuildSetting.blocks = new ObjectOpenHashSet(autoBuildSetting.tierBlock);
         }
      }

      return autoBuildSetting;
   }

   public ModularUI createUI(HeldItemHolder heldItemHolder, Player player) {
      return new ModularUI(166, 176, heldItemHolder, player).widget(this.createWidget(player));
   }

   private Widget createWidget(Player player) {
      ItemStack handItem = player.m_21205_();
      WidgetGroup group = new WidgetGroup(0, 0, 200, 136);
      DraggableScrollableWidgetGroup contain = new DraggableScrollableWidgetGroup(4, 4, 192, 128)
         .setBackground(GuiTextures.DISPLAY)
         .setYScrollBarWidth(2)
         .setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1.0F));
      contain.addWidget(new ExtendLabelWidget(65, 8, Component.m_237115_("gui.gtlcore.ultimate_terminal_settings")))
         .addWidget(new ExtendLabelWidget(14, 26, Component.m_237115_("gui.gtlcore.tier_blocks")))
         .addWidget(new ExtendLabelWidget(14, 46, Component.m_237115_("gui.gtlcore.repeat_count")))
         .addWidget(
            new ExtendLabelWidget(14, 66, Component.m_237115_("gui.gtlcore.no_hatch_mode"))
               .setHoverTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.no_hatch_mode")})
         )
         .addWidget(
            new ExtendLabelWidget(14, 86, Component.m_237115_("gui.gtlcore.replace_mode"))
               .setHoverTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.replace_mode")})
         )
         .addWidget(
            new ExtendLabelWidget(14, 106, Component.m_237115_("gui.gtlcore.mirror_mode"))
               .setHoverTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.mirror_mode")})
         )
         .addWidget(new TerminalInputWidget(140, 45, 36, 12, () -> getRepeatCount(handItem), v -> setRepeatCount(v, handItem)).setMax(648).setMin(0))
         .addWidget(
            new SwitchWidget(140, 63, 36, 14, (c, b) -> setIsBuildHatches(!getIsBuildHatches(handItem), handItem))
               .setPressed(getIsBuildHatches(handItem))
               .setTexture(
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("OFF")}),
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("ON")})
               )
         )
         .addWidget(
            new SwitchWidget(140, 83, 36, 14, (c, b) -> setReplaceMode(!getReplaceMode(handItem), handItem))
               .setPressed(getReplaceMode(handItem))
               .setTexture(
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("OFF")}),
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("ON")})
               )
         )
         .addWidget(
            new SwitchWidget(140, 103, 36, 14, (c, b) -> setIsFlip(!getIsFlip(handItem), handItem))
               .setPressed(getIsFlip(handItem))
               .setTexture(
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("OFF")}),
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("ON")})
               )
         )
         .addWidget(new ExtendLabelWidget(14, 126, Component.m_237115_("gui.gtlcore.ae_mode")))
         .addWidget(
            new SwitchWidget(140, 123, 36, 14, (c, b) -> setAEMode(!getAEMode(handItem), handItem))
               .setPressed(getAEMode(handItem))
               .setTexture(
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("OFF")}),
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("ON")})
               )
         )
         .addWidget(new ExtendLabelWidget(14, 146, Component.m_237115_("gui.gtlcore.dismantle_mode")))
         .addWidget(
            new SwitchWidget(140, 143, 36, 14, (c, b) -> setDismantleMode(!getDismantleMode(handItem), handItem))
               .setPressed(getDismantleMode(handItem))
               .setTexture(
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("OFF")}),
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("ON")})
               )
         );
      ExtendLabelWidget blockLabel = new ExtendLabelWidget(47, 26, getBlockComponent(handItem));
      BlockMapSelectorWidget blockMap = new BlockMapSelectorWidget(
         group.getSizeHeight() + 4,
         contain.getSizeWidth(),
         (s, i) -> {
            if (s != null && i != null) {
               CompoundTag tag = handItem.m_41784_();
               tag.m_128359_("blocks", s);
               tag.m_128405_("Tier", i);
               handItem.m_41751_(tag);
               blockLabel.setComponent(
                  Component.m_237113_("(")
                     .m_7220_(BlockMapSelectorWidget.getBlock(s))
                     .m_7220_(Component.m_237113_(" : "))
                     .m_7220_(((Block[])((Lazy)BlockMap.tierBlockMap.get(s)).get())[i].m_49954_())
                     .m_7220_(Component.m_237113_(")"))
               );
            }
         }
      );
      blockMap.setInit(handItem);
      Widget open = new SwitchWidget(14, 26, 30, 16, (c, f) -> blockMap.showType(f))
         .setHoverTooltips(new Component[]{Component.m_237115_("gui.gtlcore.open.config.map")});
      contain.addWidget(open).addWidget(blockLabel);
      group.addWidget(contain).addWidget(blockMap).setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return group;
   }

   private static Component getBlockComponent(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      if (!tag.m_128456_()) {
         String block = tag.m_128461_("blocks");
         if (!block.isEmpty()) {
            int tier = tag.m_128451_("Tier");
            return Component.m_237113_("(")
               .m_7220_(BlockMapSelectorWidget.getBlock(block))
               .m_7220_(Component.m_237113_(" : "))
               .m_7220_(((Block[])((Lazy)BlockMap.tierBlockMap.get(block)).get())[tier].m_49954_())
               .m_7220_(Component.m_237113_(")"));
         }
      }

      return Component.m_237113_("");
   }

   private static int getRepeatCount(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return !tag.m_128456_() ? tag.m_128451_("RepeatCount") : 0;
   }

   private static boolean getIsFlip(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return !tag.m_128456_() && tag.m_128471_("IsFlipped");
   }

   private static boolean getDismantleMode(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return !tag.m_128456_() && tag.m_128471_("DismantleMode");
   }

   private static void setDismantleMode(boolean isDismantle, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128379_("DismantleMode", isDismantle);
      itemStack.m_41751_(tag);
   }

   private static boolean getAEMode(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return !tag.m_128456_() && tag.m_128471_("AEMode");
   }

   private static void setAEMode(boolean isAEMode, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128379_("AEMode", isAEMode);
      itemStack.m_41751_(tag);
   }

   private static void setRepeatCount(int repeatCount, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128405_("RepeatCount", repeatCount);
      itemStack.m_41751_(tag);
   }

   private static boolean getIsBuildHatches(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return tag.m_128456_() || tag.m_128471_("NoHatchMode");
   }

   private static void setIsBuildHatches(boolean isBuildHatches, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128379_("NoHatchMode", isBuildHatches);
      itemStack.m_41751_(tag);
   }

   private static boolean getReplaceMode(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      return !tag.m_128456_() && tag.m_128471_("ReplaceMode");
   }

   private static void setReplaceMode(boolean isReplace, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128379_("ReplaceMode", isReplace);
      itemStack.m_41751_(tag);
   }

   private static void setIsFlip(boolean isFlip, ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      tag.m_128379_("IsFlipped", isFlip);
      itemStack.m_41751_(tag);
   }

   private void dismantleMultiblock(IMultiController controller, Player player) {
      Level level = player.m_9236_();
      if (!level.m_5776_()) {
         BlockPattern pattern = controller.getPattern();
         if (pattern != null) {
            AdvancedBlockPattern advancedPattern = AdvancedBlockPattern.getAdvancedBlockPattern(pattern);
            if (advancedPattern != null) {
               ItemStack handItem = player.m_21205_();
               UltimateTerminalBehavior.AutoBuildSetting setting = this.getAutoBuildSetting(handItem);
               boolean isFlipped = setting.isFlipped();
               int repeatCountSetting = setting.getRepeatCount();
               boolean aeMode = setting.isAeMode();
               GlobalPos boundAEPos = setting.getBoundAE();
               advancedPattern.dismantleMultiblock(controller, player, repeatCountSetting, isFlipped, aeMode, boundAEPos);
            }
         }
      }
   }

   public static class AutoBuildSetting {
      Block[] tierBlock;
      Set<Block> blocks = Collections.emptySet();
      private int tier = 0;
      private int repeatCount = 0;
      private boolean noHatchMode = true;
      private boolean replaceMode = false;
      private boolean isFlipped = false;
      private boolean dismantleMode = false;
      private boolean aeMode = false;
      private GlobalPos boundAE;

      public List<ItemStack> apply(BlockInfo[] blockInfos) {
         List<ItemStack> candidates = new ObjectArrayList();
         if (blockInfos != null) {
            for (BlockInfo info : blockInfos) {
               if (this.tierBlock != null && this.tier >= 0 && blockInfos.length > 1 && this.blocks.contains(info.getBlockState().m_60734_())) {
                  candidates.add(this.tierBlock[Math.min(this.tier, blockInfos.length - 1)].m_5456_().m_7968_());
                  return candidates;
               }

               if (info.getBlockState().m_60734_() instanceof LiquidBlock liquidBlock) {
                  candidates.add(liquidBlock.getFluid().m_6859_().m_7968_());
               } else if (info.getBlockState().m_60734_() != Blocks.f_50016_) {
                  candidates.add(info.getItemStackForm());
               }
            }
         }

         return candidates;
      }

      public boolean isPlaceHatch(BlockInfo[] blockInfos) {
         if (!this.noHatchMode) {
            return true;
         } else if (blockInfos != null && blockInfos.length > 0) {
            BlockInfo blockInfo = blockInfos[0];
            return !(
               blockInfo.getBlockState().m_60734_() instanceof MetaMachineBlock machineBlock
                  && UltimateTerminalBehavior.AutoBuildSetting.Hatch.Set.contains(machineBlock)
            );
         } else {
            return true;
         }
      }

      public void setTierBlock(Block[] tierBlock) {
         this.tierBlock = tierBlock;
      }

      public void setBlocks(Set<Block> blocks) {
         this.blocks = blocks;
      }

      public void setTier(int tier) {
         this.tier = tier;
      }

      public void setRepeatCount(int repeatCount) {
         this.repeatCount = repeatCount;
      }

      public void setNoHatchMode(boolean noHatchMode) {
         this.noHatchMode = noHatchMode;
      }

      public void setReplaceMode(boolean replaceMode) {
         this.replaceMode = replaceMode;
      }

      public void setFlipped(boolean isFlipped) {
         this.isFlipped = isFlipped;
      }

      public void setDismantleMode(boolean dismantleMode) {
         this.dismantleMode = dismantleMode;
      }

      public void setAeMode(boolean aeMode) {
         this.aeMode = aeMode;
      }

      public void setBoundAE(GlobalPos boundAE) {
         this.boundAE = boundAE;
      }

      public Block[] getTierBlock() {
         return this.tierBlock;
      }

      public Set<Block> getBlocks() {
         return this.blocks;
      }

      public int getTier() {
         return this.tier;
      }

      public int getRepeatCount() {
         return this.repeatCount;
      }

      public boolean isNoHatchMode() {
         return this.noHatchMode;
      }

      public boolean isReplaceMode() {
         return this.replaceMode;
      }

      public boolean isFlipped() {
         return this.isFlipped;
      }

      public boolean isDismantleMode() {
         return this.dismantleMode;
      }

      public boolean isAeMode() {
         return this.aeMode;
      }

      public GlobalPos getBoundAE() {
         return this.boundAE;
      }

      private static final class Hatch {
         public static final Set<Block> Set = new ObjectOpenHashSet();

         static {
            GTRegistries.MACHINES
               .forEach(
                  d -> {
                     if (d.getRecipeTypes() == null && !(d instanceof MultiblockMachineDefinition)) {
                        Block block = d.getBlock();
                        if (d.createMetaMachine((IMachineBlockEntity)d.getBlockEntityType().m_155264_(BlockPos.f_121853_, block.m_49966_())) instanceof MultiblockPartMachine
                           )
                         {
                           Set.add(block);
                        }
                     }
                  }
               );
         }
      }
   }
}
