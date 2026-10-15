package org.gtlcore.gtlcore.api.registries;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.resources.ResourceKey;

public class GTLRegistration {
   public static final GTRegistrate REGISTRATE = GTRegistrate.create("gtlcore");

   private GTLRegistration() {
   }

   static {
      REGISTRATE.defaultCreativeTab((ResourceKey)null);
   }
}
