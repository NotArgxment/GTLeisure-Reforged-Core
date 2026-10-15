package org.gtlcore.gtlcore.common.item;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import org.jetbrains.annotations.NotNull;

public class MEPatternBufferCutBehavior extends TooltipBehavior implements IInteractionItem {
   public static final MEPatternBufferCutBehavior INSTANCE = new MEPatternBufferCutBehavior(list -> {
      list.add(Component.m_237115_("tooltip.gtlcore.cut_pattern_buffer_sneak_right_click"));
      list.add(Component.m_237115_("tooltip.gtlcore.apply_cut_pattern_buffer_right_click"));
   });

   public MEPatternBufferCutBehavior(@NotNull Consumer<List<Component>> tooltips) {
      super(tooltips);
   }

   public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
      if (context.m_43725_().m_7702_(context.m_8083_()) instanceof MetaMachineBlockEntity machineBlock
         && machineBlock.getMetaMachine() instanceof MEPatternBufferPartMachine partMachine
         && context.m_43723_() instanceof ServerPlayer serverPlayer) {
         CompoundTag tags = ToolHelper.getBehaviorsTag(itemStack);
         if (!serverPlayer.m_6144_()) {
            if (!tags.m_128456_() && tags.m_128441_("cut")) {
               if (!partMachine.pasteFromTag(tags.m_128469_("cut"))) {
                  serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_paste_failed"), true);
                  return InteractionResult.CONSUME;
               }

               tags.m_128473_("cut");
            } else {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_not_cut"), true);
            }
         } else {
            tags = partMachine.cutToTag(tags);
            if (!tags.m_128456_() && tags.m_128441_("cut") && !tags.m_128469_("cut").m_128456_()) {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_cut"), true);
            } else {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_cut_failed"), true);
            }
         }
      }

      return InteractionResult.PASS;
   }
}
