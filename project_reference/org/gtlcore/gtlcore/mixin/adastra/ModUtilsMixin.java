package org.gtlcore.gtlcore.mixin.adastra;

import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.common.entities.vehicles.Lander;
import earth.terrarium.adastra.common.registry.ModEntityTypes;
import earth.terrarium.adastra.common.utils.ModUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModUtils.class)
public abstract class ModUtilsMixin {
   @Inject(method = "canTeleportToPlanet", at = @At("HEAD"), remap = false, cancellable = true)
   private static void canTeleportToPlanet(Player player, Planet targetPlanet, CallbackInfoReturnable<Boolean> cir) {
      if (player.m_20137_("spaceelevatorst")) {
         player.m_20049_("canTeleportToPlanet");
         cir.setReturnValue(true);
      }
   }

   @Inject(method = "land", at = @At("HEAD"), remap = false, cancellable = true)
   private static void land(ServerPlayer player, ServerLevel targetLevel, Vec3 pos, CallbackInfo ci) {
      if (player.m_20137_("canTeleportToPlanet")) {
         player.m_20219_(pos);
         Lander lander = (Lander)((EntityType)ModEntityTypes.LANDER.get()).m_20615_(targetLevel);
         if (lander == null) {
            return;
         }

         lander.m_146884_(pos);
         targetLevel.m_7967_(lander);
         ModUtils.teleportToDimension(player, targetLevel).m_20329_(lander);
         ci.cancel();
      }
   }
}
