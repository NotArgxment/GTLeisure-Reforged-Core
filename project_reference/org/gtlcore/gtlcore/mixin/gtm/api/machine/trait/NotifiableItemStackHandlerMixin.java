package org.gtlcore.gtlcore.mixin.gtm.api.machine.trait;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler.KJSCallWrapper;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NotifiableItemStackHandler.class)
public abstract class NotifiableItemStackHandlerMixin {
   @Shadow(remap = false)
   @Final
   public IO handlerIO;
   @Shadow(remap = false)
   @Final
   public ItemStackTransfer storage;

   @Overwrite(remap = false)
   public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
      return handleLongIngredient(io, recipe, left, simulate, this.handlerIO, this.storage);
   }

   @Unique
   @Nullable
   private static List<Ingredient> handleLongIngredient(
      IO io, GTRecipe recipe, List<Ingredient> left, boolean simulate, IO handlerIO, ItemStackTransfer storage
   ) {
      if (io != handlerIO) {
         return left;
      }

      if (io != IO.IN && io != IO.OUT) {
         return left.isEmpty() ? null : left;
      }

      Runnable listener = storage.getOnContentsChanged();
      storage.setOnContentsChanged(() -> {});
      boolean changed = false;
      ItemStack[] visited = new ItemStack[storage.getSlots()];
      ListIterator<Ingredient> it = left.listIterator();

      label119:
      while (it.hasNext()) {
         Ingredient ingredient = it.next();
         if (ingredient.m_43947_()) {
            it.remove();
         } else {
            if (io == IO.OUT && ingredient instanceof IntProviderIngredient provider) {
               provider.setItemStacks(null);
               provider.setSampledCount(-1);
            }

            ItemStack[] items = ingredient.m_43908_();
            if (items.length != 0 && !items[0].m_41619_()) {
               long amount;
               if (ingredient instanceof LongIngredient li) {
                  amount = li.getActualAmount();
               } else {
                  amount = items[0].m_41613_();
               }

               for (int slot = 0; slot < storage.getSlots(); slot++) {
                  ItemStack current = visited[slot] == null ? storage.getStackInSlot(slot) : visited[slot];
                  int count = current.m_41613_();
                  if (io == IO.IN) {
                     if (current.m_41619_()) {
                        continue;
                     }

                     if (ingredient.test(current)) {
                        ItemStack extracted = getActioned(storage, slot, recipe.ingredientActions);
                        if (extracted == null) {
                           extracted = storage.extractItem(slot, Ints.saturatedCast(Math.min(count, amount)), simulate);
                        }

                        if (!extracted.m_41619_()) {
                           changed = true;
                           visited[slot] = extracted.m_255036_(count - extracted.m_41613_());
                        }

                        amount -= extracted.m_41613_();
                     }
                  } else {
                     ItemStack template = items[0];
                     if (visited[slot] == null || ItemStack.m_150942_(visited[slot], template)) {
                        int slotLimit = storage.getSlotLimit(slot);
                        int canHold = slotLimit - count;
                        if (canHold > 0) {
                           int tryPut = Ints.saturatedCast(Math.min(amount, canHold));
                           ItemStack remainder = getActioned(storage, slot, recipe.ingredientActions);
                           if (remainder == null) {
                              remainder = storage.insertItem(slot, template.m_255036_(tryPut), simulate);
                           }

                           int actuallyPut = tryPut - remainder.m_41613_();
                           if (actuallyPut > 0) {
                              changed = true;
                              visited[slot] = template.m_255036_(count + actuallyPut);
                              amount -= actuallyPut;
                           }
                        }
                     }
                  }

                  if (amount <= 0L) {
                     it.remove();
                     if (amount > 0L) {
                        if (ingredient instanceof LongIngredient li) {
                           li.setActualAmount(amount);
                        } else {
                           items[0].m_41764_(Ints.saturatedCast(amount));
                        }
                     }
                     continue label119;
                  }
               }
               break;
            } else {
               it.remove();
            }
         }
      }

      storage.setOnContentsChanged(listener);
      if (changed && !simulate) {
         listener.run();
      }

      return left.isEmpty() ? null : left;
   }

   @Unique
   @Nullable
   private static ItemStack getActioned(ItemStackTransfer storage, int index, List<?> actions) {
      if (!GTCEu.isKubeJSLoaded()) {
         return null;
      }

      ItemStack actioned = KJSCallWrapper.applyIngredientAction(storage, index, actions);
      return !actioned.m_41619_() ? actioned : null;
   }
}
