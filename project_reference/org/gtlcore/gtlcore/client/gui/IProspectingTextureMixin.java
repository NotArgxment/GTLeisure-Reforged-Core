package org.gtlcore.gtlcore.client.gui;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Unique;

public interface IProspectingTextureMixin {
   void gTLCore$addWayPoint(Player var1, double var2, double var4);

   @Unique
   void gTLCore$teleportWayPoint(Player var1, double var2, double var4);
}
