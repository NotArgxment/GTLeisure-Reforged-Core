package org.gtlcore.gtlcore.common.data;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs.RegistrateDisplayItemsGenerator;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.item.CreativeModeTab;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.registries.GTLRegistration;

public class GTLCreativeModeTabs {
   public static RegistryEntry<CreativeModeTab> GTL_CORE = GTLRegistration.REGISTRATE
      .defaultCreativeTab(
         "gtlcore",
         builder -> builder.m_257501_(new RegistrateDisplayItemsGenerator("gtlcore", GTLRegistration.REGISTRATE))
            .m_257941_(GTLRegistration.REGISTRATE.addLang("itemGroup", GTLCore.id("creative_tab"), "GTL Core"))
            .m_257737_(GTLItems.MEGA_ULTIMATE_BATTERY::asStack)
            .m_257652_()
      )
      .register();

   public static void init() {
   }
}
