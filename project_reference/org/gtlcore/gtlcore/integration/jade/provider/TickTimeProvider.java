package org.gtlcore.gtlcore.integration.jade.provider;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.machine.IPerformanceDisplayMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TickTimeProvider extends CapabilityBlockProvider<IPerformanceDisplayMachine> {
   public TickTimeProvider() {
      super(GTLCore.id("tick_time_provider"));
   }

   @Nullable
   protected IPerformanceDisplayMachine getCapability(Level level, BlockPos pos, @Nullable Direction side) {
      if (MetaMachine.getMachine(level, pos) instanceof IPerformanceDisplayMachine machine) {
         machine.gtlcore$observe();
         return machine;
      } else {
         return null;
      }
   }

   protected void write(CompoundTag data, IPerformanceDisplayMachine capability) {
      if (capability != null) {
         data.m_128405_("tick_time", capability.gtlcore$getTickTime());
      }
   }

   protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
      long time = capData.m_128451_("tick_time");
      if (time != 0L) {
         tooltip.add(Component.m_237110_("tooltip.jade.delay", new Object[]{time}).m_130946_(" \u03bcs"));
      }
   }
}
