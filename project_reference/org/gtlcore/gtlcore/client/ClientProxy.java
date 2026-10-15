package org.gtlcore.gtlcore.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.gtlcore.gtlcore.api.event.SourceTooltipRegistrationEvent;
import org.gtlcore.gtlcore.common.CommonProxy;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
   public ClientProxy() {
      init();
   }

   public static void init() {
      CraftingUnitModelProvider.initCraftingUnitModels();
   }

   @Override
   protected void clientSetup(FMLClientSetupEvent event) {
      super.clientSetup(event);
      event.enqueueWork(() -> FMLJavaModLoadingContext.get().getModEventBus().post(new SourceTooltipRegistrationEvent()));
   }
}
