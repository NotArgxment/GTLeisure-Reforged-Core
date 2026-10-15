package org.gtlcore.gtlcore.network.packet;

import com.glodblock.github.glodium.network.packet.IMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.client.renderer.BlockHighlightHandler;

public class SStructureDetectHighlight implements IMessage<SStructureDetectHighlight> {
   private BlockPos pos;
   private ResourceKey<Level> dim;
   private long time;

   public SStructureDetectHighlight() {
   }

   public SStructureDetectHighlight(BlockPos pos, ResourceKey<Level> dim, long time) {
      this.pos = pos;
      this.dim = dim;
      this.time = time;
   }

   public void toBytes(FriendlyByteBuf buf) {
      buf.m_130103_(this.pos.m_121878_());
      buf.m_236858_(this.dim);
      buf.m_130103_(this.time);
   }

   public void fromBytes(FriendlyByteBuf buf) {
      this.pos = BlockPos.m_122022_(buf.m_130258_());
      this.dim = buf.m_236801_(Registries.f_256858_);
      this.time = buf.m_130258_();
   }

   public void onMessage(Player player) {
      BlockHighlightHandler.highlight(this.pos, this.dim, this.time);
   }

   public Class<SStructureDetectHighlight> getPacketClass() {
      return SStructureDetectHighlight.class;
   }

   public boolean isClient() {
      return true;
   }
}
