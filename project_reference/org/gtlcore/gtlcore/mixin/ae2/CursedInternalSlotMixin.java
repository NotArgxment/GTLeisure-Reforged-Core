package org.gtlcore.gtlcore.mixin.ae2;

import java.util.UUID;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.integration.ae2.storage.FastInfinityCellInventory;
import org.gtlcore.gtlcore.integration.ae2.storage.InfinityCellDataStorage;
import org.gtlcore.gtlcore.integration.ae2.storage.InfinityCellInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class CursedInternalSlotMixin {
   @Shadow
   @Final
   public NonNullList<Slot> f_38839_;

   @Shadow
   public abstract void m_142503_(ItemStack var1);

   @Inject(
      method = "doClick",
      at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.copyWithCount (I)Lnet/minecraft/world/item/ItemStack;"),
      slice = @Slice(from = @At(value = "INVOKE", target = "net/minecraft/world/inventory/Slot.hasItem()Z", ordinal = 1)),
      cancellable = true
   )
   public void doClick(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci) {
      Slot i = (Slot)this.f_38839_.get(slotIndex);
      if (InfinityCellInventory.hasDiskUUID(i.m_7993_())) {
         InfinityCellDataStorage storage = GTLCore.STORAGE_INSTANCE.getOrCreateDisk(i.m_7993_().m_41784_().m_128342_("diskuuid"), false);
         ItemStack newStack = new ItemStack(i.m_7993_().m_41720_());
         UUID id = UUID.randomUUID();
         newStack.m_41784_().m_128362_("diskuuid", id);
         newStack.m_41784_().m_128356_("count", Double.doubleToLongBits(storage.totalAmount));
         GTLCore.STORAGE_INSTANCE.updateDisk(id, storage);
         newStack.m_41764_(newStack.m_41741_());
         this.m_142503_(newStack);
         ci.cancel();
      } else if (FastInfinityCellInventory.hasDiskUUID(i.m_7993_())) {
         InfinityCellDataStorage storage = GTLCore.STORAGE_INSTANCE.getOrCreateDisk(i.m_7993_().m_41784_().m_128342_("diskuuid"), true);
         ItemStack newStack = new ItemStack(i.m_7993_().m_41720_());
         UUID id = UUID.randomUUID();
         newStack.m_41784_().m_128362_("diskuuid", id);
         newStack.m_41784_().m_128356_("count", Double.doubleToLongBits(storage.totalAmount));
         GTLCore.STORAGE_INSTANCE.updateDisk(id, storage);
         newStack.m_41764_(newStack.m_41741_());
         this.m_142503_(newStack);
         ci.cancel();
      }
   }
}
