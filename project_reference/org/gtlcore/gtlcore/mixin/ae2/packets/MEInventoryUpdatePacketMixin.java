package org.gtlcore.gtlcore.mixin.ae2.packets;

import appeng.core.AELog;
import appeng.core.sync.packets.MEInventoryUpdatePacket;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.IClientRepo;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.integration.ae2.common.IConfirmStartMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(MEInventoryUpdatePacket.class)
public class MEInventoryUpdatePacketMixin {
   @Shadow(remap = false)
   private int containerId;
   @Shadow(remap = false)
   private boolean fullUpdate;
   @Final
   @Shadow(remap = false)
   private List<GridInventoryEntry> entries;

   @Inject(method = "clientPacketData(Lnet/minecraft/world/entity/player/Player;)V", at = @At("HEAD"), remap = false, cancellable = true)
   private void clientPacketData(Player player, CallbackInfo ci) {
      if (player.f_36096_.f_38840_ == this.containerId) {
         if (player.f_36096_ instanceof IConfirmStartMenu menu) {
            IClientRepo clientRepo = menu.gtlcore$getClientRepo();
            if (clientRepo == null) {
               AELog.info("Ignoring ME inventory update packet because no client repo is available.", new Object[0]);
            } else {
               clientRepo.handleUpdate(this.fullUpdate, this.entries);
               ci.cancel();
            }
         }
      }
   }
}
