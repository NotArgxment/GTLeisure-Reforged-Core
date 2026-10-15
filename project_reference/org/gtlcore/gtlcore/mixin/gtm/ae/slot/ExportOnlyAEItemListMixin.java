package org.gtlcore.gtlcore.mixin.gtm.ae.slot;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMETransfer;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IOptimizedMEList.class, prefix = "gTLCore$"))
@Mixin(ExportOnlyAEItemList.class)
public abstract class ExportOnlyAEItemListMixin extends NotifiableItemStackHandler {
   @Unique
   protected final Object2LongOpenHashMap<ItemStack> gTLCore$itemMap = new Object2LongOpenHashMap();
   @Unique
   protected boolean gTLCore$changed = true;
   @Shadow(remap = false)
   protected ExportOnlyAEItemSlot[] inventory;

   public ExportOnlyAEItemListMixin(
      MetaMachine machine, int slots, @NotNull IO handlerIO, @NotNull IO capabilityIO, Function<Integer, ItemStackTransfer> transferFactory
   ) {
      super(machine, slots, handlerIO, capabilityIO, transferFactory);
   }

   @Unique
   public Object2LongOpenHashMap<ItemStack> gTLCore$getItemMap() {
      return this.gTLCore$itemMap;
   }

   @Unique
   public Object2LongMap<ItemStack> gTLCore$getMEItemMap() {
      if (this.gTLCore$changed) {
         this.gTLCore$changed = false;
         this.gTLCore$itemMap.clear();

         for (ExportOnlyAEItemSlot slot : this.inventory) {
            GenericStack stock = slot.getStock();
            if (stock != null && stock.amount() != 0L && stock.what() instanceof AEItemKey itemKey) {
               ItemStack stack = itemKey.toStack();
               if (!stack.m_41619_()) {
                  this.gTLCore$itemMap.addTo(stack, stock.amount());
               }
            }
         }
      }

      return this.gTLCore$itemMap.isEmpty() ? null : this.gTLCore$itemMap;
   }

   @Unique
   public boolean gTLCore$getChanged() {
      return this.gTLCore$changed;
   }

   @Unique
   public void gTLCore$setChanged(boolean gTLCore$changed) {
      this.gTLCore$changed = gTLCore$changed;
   }

   public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
      if (io == IO.IN) {
         boolean changed = false;
         ListIterator<Ingredient> listIterator = left.listIterator();

         while (listIterator.hasNext()) {
            Ingredient ingredient = listIterator.next();
            if (ingredient.m_43947_()) {
               listIterator.remove();
            } else {
               long amount = ingredient instanceof LongIngredient li ? li.getActualAmount() : (ingredient instanceof SizedIngredient si ? si.getAmount() : 1L);
               if (amount <= 0L) {
                  listIterator.remove();
               } else {
                  for (ExportOnlyAEItemSlot i : this.inventory) {
                     GenericStack stored = i.getStock();
                     if (stored != null && stored.amount() != 0L && stored.what() instanceof AEItemKey itemKey && itemKey.matches(ingredient)) {
                        GenericStack extracted = ((IMETransfer)i).extractGenericStack(amount, simulate, !simulate);
                        if (extracted != null && extracted.amount() > 0L) {
                           changed = true;
                           amount -= extracted.amount();
                           if (amount <= 0L) {
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
      Object2LongMap<ItemStack> itemMap = this.gTLCore$getMEItemMap();
      return itemMap == null ? Collections.emptyList() : Arrays.asList(itemMap.keySet().toArray());
   }
}
