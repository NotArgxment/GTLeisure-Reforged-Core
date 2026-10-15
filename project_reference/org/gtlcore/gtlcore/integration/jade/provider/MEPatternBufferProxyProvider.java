package org.gtlcore.gtlcore.integration.jade.provider;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.utils.GradientUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class MEPatternBufferProxyProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
   public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity && blockEntity.getMetaMachine() instanceof MEPatternBufferProxyPartMachine) {
         CompoundTag serverData = blockAccessor.getServerData();
         if (!serverData.m_128471_("formed")) {
            return;
         }

         if (!serverData.m_128471_("bound")) {
            iTooltip.add(Component.m_237115_("gtceu.top.buffer_not_bound").m_130940_(ChatFormatting.RED));
            return;
         }

         int[] pos = serverData.m_128465_("pos");
         iTooltip.add(
            Component.m_237110_("gtceu.top.buffer_bound_pos", new Object[]{pos[0], pos[1], pos[2]}).m_130938_(style -> style.m_131148_(rainbowColor(1.25F)))
         );
         MEPatternBufferProvider.readBufferContents(iTooltip, serverData);
      }
   }

   public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity
         && blockEntity.getMetaMachine() instanceof MEPatternBufferProxyPartMachine proxy) {
         if (!proxy.isFormed()) {
            compoundTag.m_128379_("formed", false);
            return;
         }

         compoundTag.m_128379_("formed", true);
         MEPatternBufferPartMachine buffer = proxy.getBuffer();
         if (buffer == null) {
            compoundTag.m_128379_("bound", false);
            return;
         }

         compoundTag.m_128379_("bound", true);
         BlockPos pos = buffer.getPos();
         compoundTag.m_128385_("pos", new int[]{pos.m_123341_(), pos.m_123342_(), pos.m_123343_()});
         MEPatternBufferProvider.putTag(compoundTag, buffer);
      }
   }

   public ResourceLocation getUid() {
      return GTCEu.id("me_pattern_buffer_proxy");
   }

   static TextColor rainbowColor(float speed) {
      return TextColor.m_131266_(GradientUtil.toRGB((float)(GTValues.CLIENT_TIME & 1048575L) * speed, 95.0F, 60.0F));
   }
}
