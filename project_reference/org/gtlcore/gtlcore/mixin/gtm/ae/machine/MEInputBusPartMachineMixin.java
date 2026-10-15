package org.gtlcore.gtlcore.mixin.gtm.ae.machine;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNodeListener.State;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.api.gui.AdvancedMEConfigurator;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IModifiableSyncOffset;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMETransfer;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEInputBusPartMachine.class)
public abstract class MEInputBusPartMachineMixin extends MEBusPartMachine implements IModifiableSyncOffset {
   @Shadow(remap = false)
   protected ExportOnlyAEItemList aeItemHandler;

   public MEInputBusPartMachineMixin(IMachineBlockEntity holder, IO io, Object... args) {
      super(holder, io, args);
   }

   @Inject(
      method = "autoIO",
      at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/integration/ae2/machine/MEInputBusPartMachine;syncME()V", shift = Shift.AFTER),
      remap = false
   )
   public void autoIO(CallbackInfo ci) {
      if (this.aeItemHandler instanceof IOptimizedMEList machine) {
         machine.setChanged(true);
      }
   }

   @Inject(method = "writeConfigToTag", at = @At("RETURN"), remap = false)
   public void writesSyncOffset(CallbackInfoReturnable<CompoundTag> cir) {
      ((CompoundTag)cir.getReturnValue()).m_128405_("SyncOffset", this.getOffset());
   }

   @Inject(method = "readConfigFromTag", at = @At("RETURN"), remap = false)
   public void readSyncOffset(CompoundTag tag, CallbackInfo ci) {
      if (tag.m_128441_("SyncOffset")) {
         this.setOffset(tag.m_128451_("SyncOffset"));
      }
   }

   public void onMainNodeStateChanged(@NotNull State reason) {
      super.onMainNodeStateChanged(reason);
      if (this.getMainNode().isOnline()) {
         this.aeItemHandler.notifyListeners();
      }
   }

   public void attachConfigurators(@NotNull ConfiguratorPanel configuratorPanel) {
      super.attachConfigurators(configuratorPanel);
      configuratorPanel.attachConfigurators(new IFancyConfigurator[]{new AdvancedMEConfigurator(this::setOffset, this::getOffset)});
   }

   @Overwrite(remap = false)
   protected void syncME() {
      MEStorage networkInv = this.getMainNode().getGrid().getStorageService().getInventory();

      for (ExportOnlyAEItemSlot aeSlot : this.aeItemHandler.getInventory()) {
         GenericStack exceedItem = aeSlot.exceedStack();
         if (exceedItem != null) {
            long total = exceedItem.amount();
            long inserted = networkInv.insert(exceedItem.what(), exceedItem.amount(), Actionable.MODULATE, this.actionSource);
            if (inserted > 0L) {
               ((IMETransfer)aeSlot).extractGenericStack(inserted, false, true);
               continue;
            }

            ((IMETransfer)aeSlot).extractGenericStack(total, false, true);
         }

         GenericStack reqItem = aeSlot.requestStack();
         if (reqItem != null) {
            long extracted = networkInv.extract(reqItem.what(), reqItem.amount(), Actionable.MODULATE, this.actionSource);
            if (extracted != 0L) {
               aeSlot.addStack(new GenericStack(reqItem.what(), extracted));
            }
         }
      }
   }
}
