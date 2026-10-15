package org.gtlcore.gtlcore.integration.jade;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.gtlcore.gtlcore.integration.jade.provider.MEMAIOProvider;
import org.gtlcore.gtlcore.integration.jade.provider.MEPatternBufferProvider;
import org.gtlcore.gtlcore.integration.jade.provider.MEPatternBufferProxyProvider;
import org.gtlcore.gtlcore.integration.jade.provider.TickTimeProvider;
import org.gtlcore.gtlcore.integration.jade.provider.WirelessOpticalDataHatchProvide;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class GTLJadePlugin implements IWailaPlugin {
   public void register(IWailaCommonRegistration registration) {
      registration.registerBlockDataProvider(new WirelessOpticalDataHatchProvide(), BlockEntity.class);
      registration.registerBlockDataProvider(new TickTimeProvider(), MetaMachineBlockEntity.class);
      registration.registerBlockDataProvider(new MEPatternBufferProvider(), BlockEntity.class);
      registration.registerBlockDataProvider(new MEPatternBufferProxyProvider(), BlockEntity.class);
      registration.registerBlockDataProvider(new MEMAIOProvider(), BlockEntity.class);
   }

   public void registerClient(IWailaClientRegistration registration) {
      registration.registerBlockComponent(new WirelessOpticalDataHatchProvide(), Block.class);
      registration.registerBlockComponent(new TickTimeProvider(), MetaMachineBlock.class);
      registration.registerBlockComponent(new MEPatternBufferProvider(), Block.class);
      registration.registerBlockComponent(new MEPatternBufferProxyProvider(), Block.class);
      registration.registerBlockComponent(new MEMAIOProvider(), Block.class);
   }
}
