package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.MEStorage;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlotList;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMESlot;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.mixin.gtm.ae.machine.MEHatchPartMachineAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine$ExportOnlyAEStockingFluidList", remap = false)
public abstract class ExportOnlyAEStockingFluidListMixin extends ExportOnlyAEFluidList implements IConfigurableSlotList, IOptimizedMEList {
   @Unique
   protected ObjectArrayList<AEFluidKey> gTLCore$configList;
   @Unique
   protected IntArrayList gTLCore$configIndexList;
   @Unique
   private static final boolean ENABLE_ULTIMATE_ME_STOCKING = ConfigHolder.INSTANCE.enableUltimateMEStocking;
   @Shadow(remap = false)
   @Final
   MEStockingHatchPartMachine this$0;

   public ExportOnlyAEStockingFluidListMixin(MetaMachine machine, int slots) {
      super(machine, slots);
   }

   @Inject(
      method = "<init>(Lcom/gregtechceu/gtceu/integration/ae2/machine/MEStockingHatchPartMachine;Lcom/gregtechceu/gtceu/api/machine/MetaMachine;I)V",
      at = @At("TAIL")
   )
   private void gtl$onInit(MEStockingHatchPartMachine holder, MetaMachine slots, int par3, CallbackInfo ci) {
      this.gTLCore$configList = new ObjectArrayList();
      this.gTLCore$configIndexList = new IntArrayList();

      for (ExportOnlyAEFluidSlot exportOnlyAEFluidSlot : this.inventory) {
         ((IMESlot)exportOnlyAEFluidSlot).setOnConfigChanged(this::onConfigChanged);
      }
   }

   public void clearInventory(int startIndex) {
      for (int i = startIndex; i < this.getConfigurableSlots(); i++) {
         IConfigurableSlot slot = this.getConfigurableSlot(i);
         ((IMESlot)slot).setConfigWithoutNotify(null);
         slot.setStock(null);
      }
   }

   public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, @Nullable String slotName, boolean simulate) {
      if (io == IO.IN && !left.isEmpty()) {
         IGrid grid = this.this$0.getMainNode().getGrid();
         if (grid == null) {
            return left;
         }

         MEStorage aeNetwork = grid.getStorageService().getInventory();
         boolean changed = false;
         ListIterator<FluidIngredient> listIterator = left.listIterator();

         while (listIterator.hasNext()) {
            FluidIngredient ingredient = listIterator.next();
            if (ingredient.isEmpty()) {
               listIterator.remove();
            } else {
               long amount = ingredient.getAmount();
               if (amount < 1L) {
                  listIterator.remove();
               } else {
                  int i = 0;

                  for (int configListSize = this.gTLCore$configList.size(); i < configListSize; i++) {
                     AEFluidKey aeFluidKey = (AEFluidKey)this.gTLCore$configList.get(i);
                     if (AEUtils.testFluidIngredient(ingredient, aeFluidKey)) {
                        long extracted = aeNetwork.extract(
                           aeFluidKey,
                           amount,
                           simulate ? Actionable.SIMULATE : Actionable.MODULATE,
                           ((MEHatchPartMachineAccessor)this.this$0).getActionSource()
                        );
                        if (extracted > 0L) {
                           changed = true;
                           amount -= extracted;
                           if (!simulate) {
                              ExportOnlyAEFluidSlot slot = this.inventory[this.gTLCore$configIndexList.getInt(i)];
                              if (slot.getStock() != null) {
                                 long amt = slot.getStock().amount() - extracted;
                                 if (amt == 0L) {
                                    slot.setStock(null);
                                 } else {
                                    slot.setStock(new GenericStack(aeFluidKey, amt));
                                 }
                              }
                           }
                        }
                     }

                     if (amount <= 0L) {
                        listIterator.remove();
                        break;
                     }
                  }
               }
            }
         }

         if (!simulate && changed) {
            this.setChanged(true);
            this.onContentsChanged();
         }

         return left.isEmpty() ? null : left;
      } else {
         return left;
      }
   }

   @Override
   public void onConfigChanged() {
      this.gTLCore$configList.clear();
      this.gTLCore$configIndexList.clear();
      int i = 0;

      for (int inventoryLength = this.inventory.length; i < inventoryLength; i++) {
         GenericStack config = this.inventory[i].getConfig();
         if (config != null && config.what() instanceof AEFluidKey key) {
            this.gTLCore$configList.add(key);
            this.gTLCore$configIndexList.add(i);
         }
      }
   }

   @NotNull
   @Override
   public List<FluidStack> getMEFluidList() {
      List<FluidStack> fluidList = this.getFluidList();
      if (ENABLE_ULTIMATE_ME_STOCKING || this.getChanged()) {
         this.setChanged(false);
         fluidList.clear();
         MEStorage aeNetwork = Objects.requireNonNull(this.this$0.getMainNode().getGrid()).getStorageService().getInventory();
         IActionSource actionSource = ((MEHatchPartMachineAccessor)this.this$0).getActionSource();
         ObjectListIterator var4 = this.gTLCore$configList.iterator();

         while (var4.hasNext()) {
            AEFluidKey key = (AEFluidKey)var4.next();
            long extracted = aeNetwork.extract(key, 9223372036854775807L, Actionable.SIMULATE, actionSource);
            if (extracted > 0L) {
               fluidList.add(FluidStack.create(key.getFluid(), extracted));
            }
         }
      }

      return fluidList;
   }
}
