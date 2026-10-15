package org.gtlcore.gtlcore.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.capability.BindCapability;
import org.gtlcore.gtlcore.common.machine.multiblock.part.WirelessOpticalDataHatchMachine;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class WirelessOpticalDataHatchProvide extends CapabilityBlockProvider<BindCapability> {
   public WirelessOpticalDataHatchProvide() {
      super(GTLCore.id("wireless_data_hatch_provider"));
   }

   @Nullable
   protected BindCapability getCapability(Level level, BlockPos pos, @Nullable Direction side) {
      return level.m_7702_(pos) instanceof MetaMachineBlockEntity metaMachineBlockEntity
            && metaMachineBlockEntity.getMetaMachine() instanceof BindCapability capability
         ? capability
         : null;
   }

   protected void write(CompoundTag data, BindCapability capability) {
      if (capability != null) {
         data.m_128379_("is_bind", capability.bind());
         data.m_128359_("pos", capability.pos());
      } else {
         data.m_128379_("is_bind", false);
         data.m_128359_("pos", "");
      }
   }

   protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
      if (blockEntity instanceof MetaMachineBlockEntity metaMachineBlockEntity) {
         if (metaMachineBlockEntity.getMetaMachine() instanceof WirelessOpticalDataHatchMachine wod) {
            if (capData.m_128471_("is_bind")) {
               if (wod.isTransmitter()) {
                  tooltip.add(Component.m_237110_("gtceu.machine.wireless_data_transmitter_hatch.bind", new Object[]{capData.m_128461_("pos")}));
               } else {
                  tooltip.add(Component.m_237110_("gtceu.machine.wireless_data_receiver_hatch.bind", new Object[]{capData.m_128461_("pos")}));
               }
            } else if (wod.isTransmitter()) {
               tooltip.add(Component.m_237115_("gtceu.machine.wireless_data_transmitter_hatch.unbind"));
            } else {
               tooltip.add(Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.unbind"));
            }
         }
      }
   }
}
