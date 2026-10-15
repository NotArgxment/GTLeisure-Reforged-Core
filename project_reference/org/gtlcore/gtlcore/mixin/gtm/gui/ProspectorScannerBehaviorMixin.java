package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.common.item.ProspectorScannerBehavior;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory.HeldItemHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbchunks.api.client.waypoint.Waypoint;
import dev.ftb.mods.ftbchunks.api.client.waypoint.WaypointManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProspectorScannerBehavior.class)
public abstract class ProspectorScannerBehaviorMixin {
   @Inject(method = "createUI", at = @At("RETURN"), remap = false)
   public void createUIHook(HeldItemHolder holder, Player entityPlayer, CallbackInfoReturnable<ModularUI> cir) {
      ((ModularUI)cir.getReturnValue())
         .widget(
            new ButtonWidget(-20, 26, 18, 18, data -> this.gTLCore$removeAllPoint(entityPlayer))
               .setButtonTexture(new IGuiTexture[]{GuiTextures.BUTTON, GuiTextures.CLOSE_ICON.copy().scale(0.8F)})
         );
   }

   @Unique
   public void gTLCore$removeAllPoint(Player player) {
      if (LDLib.isModLoaded("ftbchunks")) {
         WaypointManager mgr = (WaypointManager)FTBChunksAPI.clientApi().getWaypointManager(player.m_9236_().m_46472_()).orElse(null);
         if (mgr != null) {
            List<Waypoint> tempPoint = new ArrayList<>();
            mgr.getAllWaypoints().stream().filter(way -> way.getName().contains("prospect_point")).forEach(tempPoint::add);
            tempPoint.forEach(mgr::removeWaypoint);
         }
      }
   }
}
