package org.gtlcore.gtlcore.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.gtlcore.gtlcore.integration.ae2.async.AEWriteService;

@EventBusSubscriber(modid = "gtlcore", bus = Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class ForgeServerEventListener {
   @SubscribeEvent
   public static void onServerStopping(ServerStoppingEvent e) {
      AEWriteService.INSTANCE.shutDownGracefully();
   }
}
