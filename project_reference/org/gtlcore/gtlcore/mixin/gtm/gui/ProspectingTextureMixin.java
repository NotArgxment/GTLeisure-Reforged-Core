package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.texture.ProspectingTexture;
import com.lowdragmc.lowdraglib.LDLib;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.client.waypoint.WaypointManager;
import dev.ftb.mods.ftbchunks.net.TeleportFromMapPacket;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.client.gui.IProspectingTextureMixin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProspectingTexture.class)
public abstract class ProspectingTextureMixin extends AbstractTexture implements IProspectingTextureMixin {
   @Shadow(remap = false)
   @Final
   private int playerXGui;
   @Shadow(remap = false)
   @Final
   private int playerYGui;

   @Unique
   @Override
   public void gTLCore$addWayPoint(Player player, double mouseX, double mouseY) {
      if (LDLib.isModLoaded("ftbchunks")) {
         WaypointManager mgr = (WaypointManager)FTBChunksAPI.clientApi().getWaypointManager(player.m_9236_().m_46472_()).orElse(null);
         if (mgr != null) {
            BlockPos pos = this.gTLCore$getBlockPos(player, mouseX, mouseY);
            AtomicInteger index = new AtomicInteger();
            mgr.getAllWaypoints().forEach(waypoint -> {
               if (waypoint.getName().startsWith("prospect_point")) {
                  int i = Integer.parseInt(waypoint.getName().replace("prospect_point", ""));
                  index.set(Math.max(i, index.get()));
               }
            });
            mgr.addWaypointAt(pos, "prospect_point" + index.incrementAndGet()).setColor(200);
         }
      }
   }

   @Unique
   @Override
   public void gTLCore$teleportWayPoint(Player player, double mouseX, double mouseY) {
      if (LDLib.isModLoaded("ftbchunks")) {
         Level level = player.m_9236_();
         BlockPos pos = this.gTLCore$getBlockPos(player, mouseX, mouseY);
         new TeleportFromMapPacket(pos, true, level.m_46472_()).sendToServer();
      }
   }

   @Unique
   private BlockPos gTLCore$getBlockPos(Player player, double mouseX, double mouseY) {
      double x;
      if (this.playerXGui % 16 <= 7 && this.playerXGui % 16 != 0) {
         x = mouseX - this.playerXGui;
      } else {
         x = mouseX - (this.playerXGui - 1);
      }

      double z;
      if (this.playerYGui % 16 <= 7 && this.playerYGui % 16 != 0) {
         z = mouseY - this.playerYGui;
      } else {
         z = mouseY - (this.playerYGui - 1);
      }

      return new BlockPos((int)(player.m_20182_().f_82479_ + x), 0, (int)(player.m_20182_().f_82481_ + z));
   }
}
