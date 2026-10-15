package org.gtlcore.gtlcore.common.item;

import appeng.api.inventories.InternalInventory;
import appeng.blockentity.networking.CableBusBlockEntity;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory.HeldItemHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.gtlcore.gtlcore.api.item.tool.ae2.patternTool.Ae2BaseProcessingPattern;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;

public class PatternModifier implements IItemUIFactory {
   public static final PatternModifier INSTANCE = new PatternModifier();
   private int Ae2PatternGeneratorScale = 1;
   private long Ae2PatternGeneratorMaxItemStack = 1000000L;
   private long Ae2PatternGeneratorMaxFluidStack = 1000000L;
   private int Ae2PatternGeneratorDivScale = 1;
   private int Ae2PatternGeneratorAppliedNumber = 1;

   public ModularUI createUI(HeldItemHolder heldItemHolder, Player player) {
      return new ModularUI(206, 124, heldItemHolder, player)
         .widget(
            new WidgetGroup(0, 0, 206, 124)
               .addWidget(new ImageWidget(8, 8, 190, 108, GuiTextures.DISPLAY))
               .addWidget(new LabelWidget(12, 12, "AE\u6837\u677f\u500d\u4e58\u5668"))
               .addWidget(
                  new LabelWidget(12, 22, "\u8bbe\u7f6e\u500d\u6570\u540e\uff0cshift\u53f3\u952e\u6837\u677f\u4f9b\u5e94\u5668\u65b9\u5757\u4f7f\u7528")
               )
               .addWidget(
                  new LabelWidget(
                     12, 32, "\u5148\u505a\u4e58\u6cd5\uff0c\u540e\u505a\u9664\u6cd5\uff0c\u6570\u91cf\u9650\u5236\u5bf9\u6210\u54c1\u4e0d\u751f\u6548"
                  )
               )
               .addWidget(
                  new LabelWidget(
                     12,
                     42,
                     "\u82e5\u5e94\u7528\u6b21\u6570\u4e3a3\uff0c\u5219\u4ee3\u8868\u6267\u884c\u4e09\u6b21\u5e94\u7528\u6b21\u6570\u4e3a1\u7684\u64cd\u4f5c"
                  )
               )
               .addWidget(
                  new AETextInputButtonWidget(120, 50, 72, 12)
                     .setText(String.valueOf(this.Ae2PatternGeneratorScale))
                     .setOnConfirm(this::setAe2PatternGeneratorScale)
                     .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.pattern_multiplier_scale")})
               )
               .addWidget(
                  new AETextInputButtonWidget(120, 64, 72, 12)
                     .setText(String.valueOf(this.Ae2PatternGeneratorDivScale))
                     .setOnConfirm(this::setAe2PatternGeneratorDivScale)
                     .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.pattern_divider_scale")})
               )
               .addWidget(
                  new AETextInputButtonWidget(120, 78, 72, 12)
                     .setText(String.valueOf(this.Ae2PatternGeneratorMaxItemStack))
                     .setOnConfirm(this::setAe2PatternGeneratorMaxItemStack)
                     .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.pattern_max_item_stack")})
               )
               .addWidget(
                  new AETextInputButtonWidget(120, 92, 72, 12)
                     .setText(String.valueOf(this.Ae2PatternGeneratorMaxFluidStack))
                     .setOnConfirm(this::setAe2PatternGeneratorMaxFluidStack)
                     .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.pattern_max_fluid_stack")})
               )
               .addWidget(
                  new AETextInputButtonWidget(120, 106, 72, 12)
                     .setText(String.valueOf(this.Ae2PatternGeneratorAppliedNumber))
                     .setOnConfirm(this::setAe2PatternGeneratorAppliedNumber)
                     .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.pattern_applied_number")})
               )
         )
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND});
   }

   private void setAe2PatternGeneratorAppliedNumber(String s) {
      this.Ae2PatternGeneratorAppliedNumber = Math.min(16, Integer.parseInt(s));
   }

   private void setAe2PatternGeneratorMaxFluidStack(String s) {
      this.Ae2PatternGeneratorMaxFluidStack = Long.parseLong(s);
   }

   private void setAe2PatternGeneratorMaxItemStack(String s) {
      this.Ae2PatternGeneratorMaxItemStack = Long.parseLong(s);
   }

   private void setAe2PatternGeneratorDivScale(String s) {
      this.Ae2PatternGeneratorDivScale = Math.min(2147483647, Integer.parseInt(s));
   }

   private void setAe2PatternGeneratorScale(String s) {
      this.Ae2PatternGeneratorScale = Math.min(2147483647, Integer.parseInt(s));
   }

   public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
      ItemStack stack = player.m_21120_(usedHand);
      if (player instanceof ServerPlayer serverPlayer) {
         HeldItemUIFactory.INSTANCE.openUI(serverPlayer, usedHand);
      }

      return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
   }

   public InteractionResult useOn(UseOnContext context) {
      if (context.m_43723_() instanceof ServerPlayer serverPlayer) {
         if (serverPlayer.m_6144_()) {
            BlockPos pos = context.m_8083_();
            Level level = context.m_43725_();
            BlockEntity tile = level.m_7702_(pos);
            InternalInventory internalInventory;
            if (tile instanceof CableBusBlockEntity cable) {
               Vec3 hitVec = context.m_43720_();
               Vec3 hitInBlock = new Vec3(hitVec.f_82479_ - pos.m_123341_(), hitVec.f_82480_ - pos.m_123342_(), hitVec.f_82481_ - pos.m_123343_());
               internalInventory = cable.getCableBus().selectPartLocal(hitInBlock).part instanceof PatternProviderLogicHost providerPart
                  ? providerPart.getLogic().getPatternInv()
                  : null;
            } else if (tile instanceof PatternProviderLogicHost providerBlock) {
               internalInventory = providerBlock.getLogic().getPatternInv();
            } else if (tile instanceof MetaMachineBlockEntity mmbe && mmbe.getMetaMachine() instanceof MEPatternBufferPartMachine me) {
               internalInventory = me.getTerminalPatternInventory();
            } else {
               internalInventory = null;
            }

            if (internalInventory == null) {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_provider_only").m_130944_(new ChatFormatting[0]), true);
               return InteractionResult.FAIL;
            }

            for (int i = 0; i < this.Ae2PatternGeneratorAppliedNumber; i++) {
               Int2ObjectOpenHashMap<ItemStack> newItemStackHashMap = new Int2ObjectOpenHashMap();

               for (int slot = 0; slot < internalInventory.size(); slot++) {
                  ItemStack itemStack = internalInventory.getStackInSlot(slot);
                  if (!itemStack.m_41619_()) {
                     ItemStack patternItemStack = this.getNewPatternItemStack(serverPlayer, itemStack);
                     newItemStackHashMap.put(slot, patternItemStack);
                  }
               }

               newItemStackHashMap.int2ObjectEntrySet().fastForEach(entry -> {
                  int slotx = entry.getIntKey();
                  ItemStack itemStackx = (ItemStack)entry.getValue();
                  internalInventory.extractItem(slotx, 1, false);
                  internalInventory.insertItem(slotx, itemStackx, false);
               });
            }

            serverPlayer.m_5661_(Component.m_237110_("message.gtlcore.pattern_updated", new Object[]{this.Ae2PatternGeneratorAppliedNumber}), true);
         }

         if (!serverPlayer.m_6144_()) {
            serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.right_click_air_gui"), true);
         }
      }

      return InteractionResult.SUCCESS;
   }

   private ItemStack getNewPatternItemStack(ServerPlayer serverPlayer, ItemStack itemStack) {
      Ae2BaseProcessingPattern ae2BaseProcessingPattern = new Ae2BaseProcessingPattern(itemStack, serverPlayer);
      ae2BaseProcessingPattern.setScale(this.Ae2PatternGeneratorScale, false, this.Ae2PatternGeneratorMaxItemStack, this.Ae2PatternGeneratorMaxFluidStack);
      ae2BaseProcessingPattern.setScale(this.Ae2PatternGeneratorDivScale, true);
      return ae2BaseProcessingPattern.getPatternItemStack();
   }
}
