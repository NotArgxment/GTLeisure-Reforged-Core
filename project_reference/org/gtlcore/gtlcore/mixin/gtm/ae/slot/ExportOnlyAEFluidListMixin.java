package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.ListIterator;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IOptimizedMEList.class, prefix = "gTLCore$"))
@Mixin(ExportOnlyAEFluidList.class)
public abstract class ExportOnlyAEFluidListMixin extends NotifiableFluidTank implements IOptimizedMEList {
   @Unique
   protected final ObjectArrayList<FluidStack> gTLCore$fluidList = new ObjectArrayList();
   @Unique
   private boolean gTLCore$changed = true;
   @Shadow(remap = false)
   protected ExportOnlyAEFluidSlot[] inventory;

   public ExportOnlyAEFluidListMixin(MetaMachine machine, int slots, long capacity, IO io, IO capabilityIO) {
      super(machine, slots, capacity, io, capabilityIO);
   }

   @Unique
   public boolean gTLCore$getChanged() {
      return this.gTLCore$changed;
   }

   @Unique
   public void gTLCore$setChanged(boolean value) {
      this.gTLCore$changed = value;
   }

   @Unique
   @NotNull
   public List<FluidStack> gTLCore$getFluidList() {
      return this.gTLCore$fluidList;
   }

   @Unique
   @NotNull
   public List<FluidStack> gTLCore$getMEFluidList() {
      if (this.gTLCore$changed) {
         this.gTLCore$changed = false;
         this.gTLCore$fluidList.clear();

         for (ExportOnlyAEFluidSlot slot : this.inventory) {
            GenericStack stock = slot.getStock();
            if (stock != null && stock.amount() != 0L) {
               FluidStack stack = slot.getFluid();
               if (!stack.isEmpty()) {
                  this.gTLCore$fluidList.add(stack);
               }
            }
         }
      }

      return this.gTLCore$fluidList;
   }

   public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, @Nullable String slotName, boolean simulate) {
      if (io == IO.IN) {
         boolean changed = false;
         ListIterator<FluidIngredient> listIterator = left.listIterator();

         while (listIterator.hasNext()) {
            FluidIngredient ingredient = listIterator.next();
            if (ingredient.isEmpty()) {
               listIterator.remove();
            } else {
               long count = ingredient.getAmount();
               if (count < 1L) {
                  listIterator.remove();
               } else {
                  for (ExportOnlyAEFluidSlot i : this.inventory) {
                     GenericStack stored = i.getStock();
                     if (stored != null) {
                        long amount = stored.amount();
                        if (amount != 0L) {
                           if (ingredient.test(i.getFluid())) {
                              FluidStack drained = i.drain(count, simulate, !simulate);
                              if (drained.getAmount() > 0L) {
                                 changed = true;
                                 count -= drained.getAmount();
                                 ingredient.setAmount(count);
                              }
                           }

                           if (count <= 0L) {
                              listIterator.remove();
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         if (!simulate && changed) {
            this.gTLCore$changed = true;
            this.onContentsChanged();
         }
      }

      return left.isEmpty() ? null : left;
   }

   public void onContentsChanged() {
      super.onContentsChanged();
      this.gTLCore$changed = true;
   }

   public List<Object> getContents() {
      return this.gTLCore$getMEFluidList();
   }
}
