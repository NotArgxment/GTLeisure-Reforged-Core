package org.gtlcore.gtlcore.common.item;

import com.google.common.base.Joiner;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory.HeldItemHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.pattern.DebugBlockPattern;
import org.gtlcore.gtlcore.utils.Registries;

public class StructureWriteBehavior implements IItemUIFactory {
   public static final StructureWriteBehavior INSTANCE = new StructureWriteBehavior();

   protected StructureWriteBehavior() {
   }

   public ModularUI createUI(HeldItemHolder playerInventoryHolder, Player entityPlayer) {
      WidgetGroup container = new WidgetGroup(8, 8, 160, 54);
      container.addWidget(new ImageWidget(4, 4, 152, 46, GuiTextures.DISPLAY)).addWidget(new LabelWidget(7, 7, () -> {
         int x = 0;
         int y = 0;
         int z = 0;
         if (getPos(playerInventoryHolder.getHeld()) != null) {
            BlockPos[] blockPos = getPos(playerInventoryHolder.getHeld());
            if (blockPos != null) {
               x = 1 + blockPos[1].m_123341_() - blockPos[0].m_123341_();
               y = 1 + blockPos[1].m_123342_() - blockPos[0].m_123342_();
               z = 1 + blockPos[1].m_123343_() - blockPos[0].m_123343_();
            }
         }

         return LocalizationUtils.format("structure_writer.structural_scale", new Object[]{x, y, z});
      }).setTextColor(16447990)).addWidget(new LabelWidget(7, 20, () -> {
         Direction direction = getDir(playerInventoryHolder.getHeld());
         RelativeDirection[] dirs = DebugBlockPattern.getDir(direction);
         return LocalizationUtils.format("structure_writer.export_order", new Object[]{dirs[0].name(), dirs[1].name(), dirs[2].name()});
      }).setTextColor(16447990));
      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return new ModularUI(176, 120, playerInventoryHolder, entityPlayer)
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND})
         .widget(container)
         .widget(
            new ButtonWidget(
               9,
               91,
               158,
               20,
               new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("\u5bfc\u51fa\u4e3a\u65e5\u5fd7")}),
               clickData -> this.exportLog(playerInventoryHolder)
            )
         )
         .widget(
            new ButtonWidget(
               9,
               68,
               77,
               20,
               new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("\u6cbfX\u8f74\u65cb\u8f6c")}),
               clickData -> this.changeDirX(playerInventoryHolder)
            )
         )
         .widget(
            new ButtonWidget(
               90,
               68,
               77,
               20,
               new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("\u6cbfY\u8f74\u65cb\u8f6c")}),
               clickData -> this.changeDirY(playerInventoryHolder)
            )
         );
   }

   private void exportLog(HeldItemHolder playerInventoryHolder) {
      if (getPos(playerInventoryHolder.getHeld()) != null && playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
         BlockPos[] blockPos = getPos(playerInventoryHolder.getHeld());
         Direction direction = getDir(playerInventoryHolder.getHeld());
         StringBuilder builder = new StringBuilder();
         DebugBlockPattern blockPattern = new DebugBlockPattern(
            playerInventoryHolder.getPlayer().m_9236_(),
            blockPos[0].m_123341_(),
            blockPos[0].m_123342_(),
            blockPos[0].m_123343_(),
            blockPos[1].m_123341_(),
            blockPos[1].m_123342_(),
            blockPos[1].m_123343_()
         );
         RelativeDirection[] dirs = DebugBlockPattern.getDir(direction);
         blockPattern.changeDir(dirs[0], dirs[1], dirs[2]);
         builder.append(".pattern(definition -> FactoryBlockPattern.start()\n");

         for (int i = 0; i < blockPattern.pattern.length; i++) {
            String[] strings = blockPattern.pattern[i];
            builder.append(".aisle(\"%s\")\n".formatted(Joiner.on("\", \"").join(strings)));
         }

         builder.append(".where(\"~\", Predicates.controller(Predicates.blocks(definition.get())))\n");
         blockPattern.legend.forEach((b, c) -> {
            if (!c.equals(' ')) {
               builder.append(".where(\"").append(c).append("\", Predicates.blocks(Registries.getBlock(\"").append(Registries.getBlockId(b)).append("\")))\n");
            }
         });
         GTLCore.LOGGER.info(builder.toString());
      }
   }

   private void changeDirX(HeldItemHolder playerInventoryHolder) {
      if (getPos(playerInventoryHolder.getHeld()) != null && playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
         ItemStack itemStack = playerInventoryHolder.getHeld();
         Direction direction = getDir(itemStack);
         direction = direction.m_175362_(Axis.X);
         setDir(itemStack, direction);
      }
   }

   private void changeDirY(HeldItemHolder playerInventoryHolder) {
      if (getPos(playerInventoryHolder.getHeld()) != null && playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
         ItemStack itemStack = playerInventoryHolder.getHeld();
         Direction direction = getDir(itemStack);
         direction = direction.m_175362_(Axis.Y);
         setDir(itemStack, direction);
      }
   }

   public static boolean isItemStructureWriter(ItemStack stack) {
      if (stack.m_41619_()) {
         return false;
      } else {
         return stack.m_41720_() instanceof ComponentItem item ? item.getComponents().contains(INSTANCE) : false;
      }
   }

   public static Direction getDir(ItemStack stack) {
      CompoundTag tag = stack.m_41698_("structure_writer");
      return !tag.m_128441_("dir") ? Direction.WEST : Direction.m_122402_(tag.m_128461_("dir"));
   }

   public static void setDir(ItemStack stack, Direction dir) {
      CompoundTag tag = stack.m_41698_("structure_writer");
      tag.m_128359_("dir", dir.m_122433_());
   }

   public static BlockPos[] getPos(ItemStack stack) {
      CompoundTag tag = stack.m_41698_("structure_writer");
      return !tag.m_128441_("minX")
         ? null
         : new BlockPos[]{
            new BlockPos(tag.m_128451_("minX"), tag.m_128451_("minY"), tag.m_128451_("minZ")),
            new BlockPos(tag.m_128451_("maxX"), tag.m_128451_("maxY"), tag.m_128451_("maxZ"))
         };
   }

   public static void addPos(ItemStack stack, BlockPos pos) {
      CompoundTag tag = stack.m_41698_("structure_writer");
      if (!tag.m_128441_("minX") || tag.m_128451_("minX") > pos.m_123341_()) {
         tag.m_128405_("minX", pos.m_123341_());
      }

      if (!tag.m_128441_("maxX") || tag.m_128451_("maxX") < pos.m_123341_()) {
         tag.m_128405_("maxX", pos.m_123341_());
      }

      if (!tag.m_128441_("minY") || tag.m_128451_("minY") > pos.m_123342_()) {
         tag.m_128405_("minY", pos.m_123342_());
      }

      if (!tag.m_128441_("maxY") || tag.m_128451_("maxY") < pos.m_123342_()) {
         tag.m_128405_("maxY", pos.m_123342_());
      }

      if (!tag.m_128441_("minZ") || tag.m_128451_("minZ") > pos.m_123343_()) {
         tag.m_128405_("minZ", pos.m_123343_());
      }

      if (!tag.m_128441_("maxZ") || tag.m_128451_("maxZ") < pos.m_123343_()) {
         tag.m_128405_("maxZ", pos.m_123343_());
      }
   }

   public static void removePos(ItemStack stack) {
      CompoundTag tag = stack.m_41698_("structure_writer");
      tag.m_128473_("minX");
      tag.m_128473_("maxX");
      tag.m_128473_("minY");
      tag.m_128473_("maxY");
      tag.m_128473_("minZ");
      tag.m_128473_("maxZ");
   }

   public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
      Player player = context.m_43723_();
      if (player == null) {
         return InteractionResult.SUCCESS;
      }

      ItemStack stack = player.m_21120_(context.m_43724_());
      if (!player.m_6144_()) {
         addPos(stack, context.m_8083_());
      } else {
         removePos(stack);
      }

      return InteractionResult.SUCCESS;
   }

   public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
      ItemStack stack = player.m_21120_(usedHand);
      if (player.m_6144_()) {
         removePos(stack);
      } else if (player instanceof ServerPlayer serverPlayer) {
         HeldItemUIFactory.INSTANCE.openUI(serverPlayer, usedHand);
      }

      return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
   }
}
