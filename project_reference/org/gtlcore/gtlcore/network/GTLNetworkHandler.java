package org.gtlcore.gtlcore.network;

import com.glodblock.github.glodium.network.NetworkHandler;
import org.gtlcore.gtlcore.network.packet.SStructureDetectHighlight;

public class GTLNetworkHandler extends NetworkHandler {
   public static final GTLNetworkHandler INSTANCE = new GTLNetworkHandler();

   public GTLNetworkHandler() {
      super("gtlcore");
   }

   public void init() {
      this.registerPacket(SStructureDetectHighlight.class, SStructureDetectHighlight::new);
   }
}
