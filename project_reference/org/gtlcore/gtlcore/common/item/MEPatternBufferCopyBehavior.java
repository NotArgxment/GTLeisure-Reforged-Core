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

public class MEPatternBufferCopyBehavior extends TooltipBehavior implements IInteractionItem {
   public static final MEPatternBufferCopyBehavior INSTANCE = new MEPatternBufferCopyBehavior(list -> {
      list.add(Component.m_237115_("tooltip.gtlcore.copy_pattern_buffer_sneak_right_click"));
      list.add(Component.m_237115_("tooltip.gtlcore.apply_pattern_buffer_right_click"));
   });

   public MEPatternBufferCopyBehavior(@NotNull Consumer<List<Component>> tooltips) {
      super(tooltips);
   }

   public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
      if (context.m_43725_().m_7702_(context.m_8083_()) instanceof MetaMachineBlockEntity machineBlock
         && machineBlock.getMetaMachine() instanceof MEPatternBufferPartMachine partMachine
         && context.m_43723_() instanceof ServerPlayer serverPlayer) {
         CompoundTag tags = ToolHelper.getBehaviorsTag(itemStack);
         if (!serverPlayer.m_6144_()) {
            if (tags.m_128456_()) {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_not_copied"), true);
            } else {
               partMachine.copyFromTag(tags.m_128469_("tag"), serverPlayer);
            }
         } else {
            tags = partMachine.copyToTag(tags);
            if (tags.m_128456_()) {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_copy_failed"), true);
            } else {
               serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.pattern_buffer_copied"), true);
            }
         }
      }

      return InteractionResult.PASS;
   }
}
