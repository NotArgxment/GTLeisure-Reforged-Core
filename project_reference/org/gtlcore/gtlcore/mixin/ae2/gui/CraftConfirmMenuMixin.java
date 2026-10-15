package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.config.TypeFilter;
import appeng.api.config.ViewItems;
import appeng.api.networking.IGrid;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.ISubMenuHost;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.widgets.ISortSource;
import appeng.core.sync.packets.MEInventoryUpdatePacket;
import appeng.core.sync.packets.MEInventoryUpdatePacket.Builder;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.common.IncrementalUpdateHelper;
import appeng.menu.me.crafting.CraftConfirmMenu;
import java.util.Set;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.gtlcore.gtlcore.integration.ae2.common.IConfirmStartMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftConfirmMenu.class)
public abstract class CraftConfirmMenuMixin extends AEBaseMenu implements IConfirmStartMenu {
   @Unique
   private IClientRepo gtlcore$repo;
   @Unique
   private boolean gtlcore$sent = false;

   protected CraftConfirmMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
      super(menuType, id, playerInventory, host);
   }

   @Shadow(remap = false)
   protected abstract IGrid getGrid();

   @Inject(method = "<init>", at = @At("RETURN"), remap = false)
   private void onConstructed(int id, Inventory ip, ISubMenuHost host, CallbackInfo ci) {
      this.gtlcore$repo = new Repo(() -> 0, new ISortSource() {
         public SortOrder getSortBy() {
            return SortOrder.AMOUNT;
         }

         public SortDir getSortDir() {
            return SortDir.ASCENDING;
         }

         public ViewItems getSortDisplay() {
            return ViewItems.ALL;
         }

         public TypeFilter getTypeFilter() {
            return TypeFilter.ALL;
         }
      });
   }

   @Override
   public IClientRepo gtlcore$getClientRepo() {
      return this.gtlcore$repo;
   }

   @Inject(method = "broadcastChanges", at = @At("RETURN"))
   private void onBroadcastChanges(CallbackInfo ci) {
      if (!this.gtlcore$sent) {
         Builder builder = MEInventoryUpdatePacket.builder(this.f_38840_, true);
         builder.addFull(new IncrementalUpdateHelper(), this.getGrid().getStorageService().getInventory().getAvailableStacks(), Set.of(), new KeyCounter());
         builder.buildAndSend(x$0 -> this.sendPacketToClient(x$0));
         this.gtlcore$sent = true;
      }
   }
}
