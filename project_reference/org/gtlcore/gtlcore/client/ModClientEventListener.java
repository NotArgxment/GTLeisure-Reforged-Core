package org.gtlcore.gtlcore.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.gtlcore.gtlcore.api.event.SourceTooltipRegistrationEvent;
import org.gtlcore.gtlcore.common.data.GTLSourceTooltips;

@EventBusSubscriber(modid = "gtlcore", bus = Bus.MOD, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ModClientEventListener {
   @SubscribeEvent
   public static void onSourceTooltipRegistration(SourceTooltipRegistrationEvent event) {
      GTLSourceTooltips.init(event);
   }
}
