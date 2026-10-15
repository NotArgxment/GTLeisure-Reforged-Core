package org.gtlcore.gtlcore.common.item;

import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.error.PatternError;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.api.pattern.error.SinglePredicateError;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.network.GTLNetworkHandler;
import org.gtlcore.gtlcore.network.packet.SStructureDetectHighlight;
import org.jetbrains.annotations.NotNull;

public class StructureDetectBehavior extends TooltipBehavior implements IToolBehavior, IInteractionItem {
   private static final ReentrantLock LOCK = new ReentrantLock();
   public static final StructureDetectBehavior INSTANCE = new StructureDetectBehavior(lines -> {
      lines.add(Component.m_237115_("item.gtlcore.structure_detect.tooltip.0"));
      lines.add(Component.m_237115_("item.gtlcore.structure_detect.tooltip.1"));
   });

   public StructureDetectBehavior(@NotNull Consumer<List<Component>> tooltips) {
      super(tooltips);
   }

   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
      Player player = context.m_43723_();
      CompoundTag tag = stack.m_41783_();
      if (tag == null) {
         tag = new CompoundTag();
         tag.m_128379_("isFlipped", false);
         stack.m_41751_(tag);
      }

      if (player != null) {
         Level level = context.m_43725_();
         if (level.f_46443_) {
            return InteractionResult.PASS;
         }

         BlockPos blockPos = context.m_8083_();
         if (MetaMachine.getMachine(level, blockPos) instanceof IMultiController controller) {
            if (!controller.isFormed()) {
               boolean isFlipped = !tag.m_128456_() && tag.m_128471_("isFlipped");
               ((ServerLevel)level).m_7654_().execute(() -> {
                  BlockPattern pattern = controller.getPattern();
                  LOCK.lock();
                  if (LOCK.tryLock()) {
                     for (PatternError patternError : this.check(controller, pattern, isFlipped)) {
                        this.showError(player, patternError, isFlipped);
                     }

                     LOCK.unlock();
                  } else {
                     LOCK.unlock();
                  }
               });
               return InteractionResult.SUCCESS;
            }

            player.m_213846_(Component.m_237115_("message.gtlcore.structure_formed").m_130940_(ChatFormatting.GREEN));
         } else if (player instanceof ServerPlayer serverPlayer) {
            tag.m_128379_("isFlipped", !tag.m_128471_("isFlipped"));
            serverPlayer.m_5661_(
               Component.m_237115_(!tag.m_128471_("isFlipped") ? "message.gtlcore.detection_mode_normal" : "message.gtlcore.detection_mode_mirrored"), true
            );
         }
      }

      return InteractionResult.PASS;
   }

   private List<PatternError> check(IMultiController controller, BlockPattern pattern, boolean isFlipped) {
      ObjectArrayList<PatternError> errors = new ObjectArrayList();
      if (controller == null) {
         errors.add(new PatternStringError("no controller found"));
         return errors;
      }

      BlockPos centerPos = controller.self().getPos();
      Direction frontFacing = controller.self().getFrontFacing();
      Direction[] facings = controller.hasFrontFacing()
         ? new Direction[]{frontFacing}
         : new Direction[]{Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST};
      Direction upwardsFacing = controller.self().getUpwardsFacing();
      MultiblockState worldState = new MultiblockState(controller.self().getLevel(), controller.self().getPos());

      for (Direction direction : facings) {
         pattern.checkPatternAt(worldState, centerPos, direction, upwardsFacing, isFlipped, false);
         if (worldState.hasError()) {
            errors.add(worldState.error);
         }
      }

      return errors;
   }

   private void showError(Player player, PatternError error, boolean flip) {
      ObjectArrayList<Component> show = new ObjectArrayList();
      if (error instanceof PatternStringError pe) {
         player.m_213846_(pe.getErrorInfo());
      } else {
         BlockPos pos = error.getPos();
         MutableComponent posComponent = Component.m_237110_(
            "item.gtlcore.structure_detect.error.2",
            new Object[]{
               pos.m_123341_(),
               pos.m_123342_(),
               pos.m_123343_(),
               flip
                  ? Component.m_237115_("item.gtlcore.structure_detect.error.3").m_130940_(ChatFormatting.GREEN)
                  : Component.m_237115_("item.gtlcore.structure_detect.error.4").m_130940_(ChatFormatting.YELLOW)
            }
         );
         List<List<ItemStack>> candidates = error.getCandidates();
         if (error instanceof SinglePredicateError) {
            Component root = candidates.get(0).get(0).m_41786_();
            show.add(Component.m_237110_("item.gtlcore.structure_detect.error.1", new Object[]{posComponent}));
            show.add(Component.m_237113_(" - ").m_7220_(root).m_7220_(error.getErrorInfo()));
         } else {
            show.add(Component.m_237110_("item.gtlcore.structure_detect.error.0", new Object[]{posComponent}));

            for (List<ItemStack> candidate : candidates) {
               if (!candidate.isEmpty()) {
                  show.add(Component.m_237113_(" - ").m_7220_(candidate.get(0).m_41611_()));
               }
            }
         }

         show.forEach(player::m_213846_);
         GTLNetworkHandler.INSTANCE
            .sendTo(new SStructureDetectHighlight(error.getPos(), error.getWorld().m_46472_(), System.currentTimeMillis() + 15000L), (ServerPlayer)player);
      }
   }
}
