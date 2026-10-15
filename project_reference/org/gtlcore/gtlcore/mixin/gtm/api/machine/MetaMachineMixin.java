package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.block.BlockProperties;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.mojang.datafixers.util.Pair;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.IPerformanceDisplayMachine;
import org.gtlcore.gtlcore.api.machine.ISuspendableMachine;
import org.gtlcore.gtlcore.api.machine.PerformanceMonitorMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MetaMachine.class)
public abstract class MetaMachineMixin implements IPerformanceDisplayMachine, ISuspendableMachine {
   @Unique
   private long gtlcore$lastExecutionTime;
   @Unique
   private int gtlcore$averageTickTime;
   @Unique
   private long gtlcore$totaTtickCount;
   @Unique
   private boolean gtlcore$observe;
   @Unique
   private boolean gtlcore$suspendAfterFinish;
   @Shadow(remap = false)
   @Final
   public IMachineBlockEntity holder;
   @Shadow(remap = false)
   @Final
   private List<TickableSubscription> serverTicks;
   @Shadow(remap = false)
   @Final
   private List<TickableSubscription> waitingToAdd;

   @Shadow(remap = false)
   public abstract boolean isRemote();

   @Shadow(remap = false)
   protected abstract void executeTick();

   @Shadow(remap = false)
   @Nullable
   public abstract Level getLevel();

   @Shadow(remap = false)
   public abstract BlockPos getPos();

   @Shadow(remap = false)
   public abstract BlockState getBlockState();

   @Shadow(remap = false)
   public abstract long getOffsetTimer();

   @Shadow(remap = false)
   public abstract boolean isInValid();

   @Shadow(remap = false)
   public abstract void onChanged();

   @Override
   public int gtlcore$getTickTime() {
      return this.gtlcore$averageTickTime;
   }

   @Override
   public void gtlcore$observe() {
      this.gtlcore$observe = true;
   }

   @Override
   public void runTick() {
      this.executeTick();
   }

   @Overwrite(remap = false)
   public final void serverTick() {
      if (!this.cancelTick()) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.gtlcore$lastExecutionTime >= 40L) {
            this.gtlcore$lastExecutionTime = currentTime;
            boolean observe = PerformanceMonitorMachine.observe || this.gtlcore$observe;
            if (observe) {
               currentTime = System.nanoTime();
            }

            this.runTick();
            if (observe) {
               this.gtlcore$totaTtickCount = this.gtlcore$totaTtickCount + (System.nanoTime() - currentTime);
               if (this.getOffsetTimer() % 40L == 0L) {
                  this.gtlcore$observe = false;
                  this.gtlcore$averageTickTime = (int)(this.gtlcore$totaTtickCount / 40000L);
                  this.gtlcore$totaTtickCount = 0L;
               }

               if (PerformanceMonitorMachine.observe) {
                  PerformanceMonitorMachine.PERFORMANCE_MAP.put((MetaMachine)this, this.gtlcore$averageTickTime);
               }
            } else if (!this.keepTick() && this.serverTicks.isEmpty() && this.waitingToAdd.isEmpty() && !this.isInValid()) {
               this.gtlcore$averageTickTime = 0;
               this.gtlcore$totaTtickCount = 0L;
               Objects.requireNonNull(this.getLevel()).m_46597_(this.getPos(), (BlockState)this.getBlockState().m_61124_(BlockProperties.SERVER_TICK, false));
            }
         }
      }
   }

   @Inject(method = "onToolClick", at = @At("RETURN"), remap = false, cancellable = true)
   private void onToolClick(
      Set<@NotNull GTToolType> toolType, ItemStack itemStack, UseOnContext context, CallbackInfoReturnable<Pair<GTToolType, InteractionResult>> cir
   ) {
      if (((Pair)cir.getReturnValue()).getSecond() == InteractionResult.PASS
         && toolType.contains(GTToolType.WIRE_CUTTER)
         && context.m_43723_() instanceof ServerPlayer serverPlayer
         && this.holder.getMetaMachine() instanceof IGridConnectedMachine gridConnectedMachine) {
         cir.setReturnValue(Pair.of(GTToolType.WIRE_CUTTER, this.gTLCore$onWireCutterClick(serverPlayer, context.m_43724_(), gridConnectedMachine)));
      }
   }

   @Inject(method = "shouldRenderGrid", at = @At("HEAD"), remap = false, cancellable = true)
   private void shouldRenderGrid(Player player, BlockPos pos, BlockState state, ItemStack held, Set<GTToolType> toolTypes, CallbackInfoReturnable<Boolean> cir) {
      if (toolTypes.contains(GTToolType.WIRE_CUTTER)) {
         MetaMachine metaMachine = this.holder.getMetaMachine();
         if (metaMachine instanceof IGridConnectedMachine) {
            cir.setReturnValue(true);
         }
      }
   }

   @Unique
   private InteractionResult gTLCore$onWireCutterClick(ServerPlayer serverPlayer, InteractionHand hand, IGridConnectedMachine machine) {
      serverPlayer.m_6674_(hand);
      if (this.holder.self().getPersistentData().m_128471_("isAllFacing")) {
         machine.getMainNode().setExposedOnSides(EnumSet.of(((MetaMachine)machine).getFrontFacing()));
         serverPlayer.m_240418_(Component.m_237115_("gtlcore.me_front"), true);
         this.holder.self().getPersistentData().m_128379_("isAllFacing", false);
      } else {
         machine.getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
         serverPlayer.m_240418_(Component.m_237115_("gtlcore.me_any"), true);
         this.holder.self().getPersistentData().m_128379_("isAllFacing", true);
      }

      return InteractionResult.CONSUME;
   }

   @Inject(method = "onSoftMalletClick", at = @At("HEAD"), cancellable = true, remap = false)
   public void onSoftMalletClick(
      Player playerIn, InteractionHand hand, Direction gridSide, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir
   ) {
      if (!this.isRemote()) {
         if (!playerIn.m_6144_()) {
            this.gtlcore$setSuspendAfterFinish(false);
         } else {
            IControllable controllable = GTCapabilityHelper.getControllable(this.getLevel(), this.getPos(), gridSide);
            if (controllable != null) {
               if (controllable.isWorkingEnabled() && !this.gtlcore$isSuspendAfterFinish()) {
                  this.gtlcore$setSuspendAfterFinish(true);
                  playerIn.m_213846_(Component.m_237115_("gtlcore.behaviour.soft_hammer.idle_after_cycle"));
                  playerIn.m_6674_(hand);
                  cir.cancel();
                  cir.setReturnValue(InteractionResult.CONSUME);
               } else {
                  cir.cancel();
                  cir.setReturnValue(InteractionResult.FAIL);
               }
            }
         }
      }
   }

   @Unique
   @Override
   public boolean gtlcore$isSuspendAfterFinish() {
      return this.gtlcore$suspendAfterFinish;
   }

   @Unique
   @Override
   public void gtlcore$setSuspendAfterFinish(boolean suspendAfterFinish) {
      this.gtlcore$suspendAfterFinish = suspendAfterFinish;
      if (!this.isRemote()) {
         this.onChanged();
      }
   }
}
