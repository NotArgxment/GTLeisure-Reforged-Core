package org.gtlcore.gtlcore.mixin.gtm.ae.machine;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IModifiableSyncOffset;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMESlot;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEStockingBusPartMachine.class)
public abstract class MEStockingBusPartMachineMixin extends MEInputBusPartMachine implements IModifiableSyncOffset {
   @Shadow(remap = false)
   private Predicate<GenericStack> autoPullTest;

   public MEStockingBusPartMachineMixin(IMachineBlockEntity holder, IO io, Object... args) {
      super(holder, new Object[]{io, args});
   }

   @Shadow(remap = false)
   public void setAutoPull(boolean autoPull) {
      throw new AssertionError();
   }

   @ModifyConstant(method = "autoIO", constant = @Constant(longValue = 100L), remap = false)
   private long replaceOffset(long constant) {
      return this.getOffset() == 0 ? constant : this.getOffset();
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

   @Overwrite(remap = false)
   private void refreshList() {
      IGrid grid = this.getMainNode().getGrid();
      if (grid == null) {
         this.aeItemHandler.clearInventory(0);
      } else {
         MEStorage networkStorage = grid.getStorageService().getInventory();
         KeyCounter counter = networkStorage.getAvailableStacks();
         int index = 0;

         for (Entry<AEKey> entry : counter) {
            if (index >= 16) {
               break;
            }

            AEKey what = (AEKey)entry.getKey();
            long amount = entry.getLongValue();
            if (amount > 0L && what instanceof AEItemKey itemKey) {
               long request = networkStorage.extract(what, amount, Actionable.SIMULATE, this.actionSource);
               if (request != 0L && (this.autoPullTest == null || this.autoPullTest.test(new GenericStack(itemKey, amount)))) {
                  ExportOnlyAEItemSlot slot = this.aeItemHandler.getInventory()[index];
                  ((IMESlot)slot).setConfigWithoutNotify(new GenericStack(what, 1L));
                  slot.setStock(new GenericStack(what, request));
                  index++;
               }
            }
         }

         this.aeItemHandler.clearInventory(index);
         ((IOptimizedMEList)this.aeItemHandler).onConfigChanged();
      }
   }

   @Overwrite(remap = false)
   protected void readConfigFromTag(CompoundTag tag) {
      if (tag.m_128471_("AutoPull")) {
         this.setAutoPull(true);
         this.circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.m_128445_("GhostCircuit")));
      } else {
         this.setAutoPull(false);
         if (tag.m_128441_("ConfigStacks")) {
            CompoundTag configStacks = tag.m_128469_("ConfigStacks");
            ExportOnlyAEItemSlot[] inventory = this.aeItemHandler.getInventory();

            for (int i = 0; i < 16; i++) {
               String key = Integer.toString(i);
               if (configStacks.m_128441_(key)) {
                  CompoundTag configTag = configStacks.m_128469_(key);
                  ((IMESlot)inventory[i]).setConfigWithoutNotify(GenericStack.readTag(configTag));
               } else {
                  ((IMESlot)inventory[i]).setConfigWithoutNotify(null);
               }
            }

            ((IOptimizedMEList)this.aeItemHandler).onConfigChanged();
         }

         if (tag.m_128441_("GhostCircuit")) {
            this.circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.m_128445_("GhostCircuit")));
         }
      }
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().m_6937_(new TickTask(1, () -> ((IOptimizedMEList)this.aeItemHandler).onConfigChanged()));
      }
   }
}
