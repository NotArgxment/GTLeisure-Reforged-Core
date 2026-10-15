package org.gtlcore.gtlcore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NotifiableCircuitItemStackHandler extends NotifiableItemStackHandler {
   public NotifiableCircuitItemStackHandler(MetaMachine machine) {
      super(machine, 1, IO.IN, IO.IN);
   }

   @NotNull
   public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate, boolean notifyChange) {
      if (GTItems.INTEGRATED_CIRCUIT.isIn(stack)) {
         this.storage.setStackInSlot(slot, stack);
         return ItemStack.f_41583_;
      } else {
         return stack;
      }
   }

   public int getSlotLimit(int slot) {
      return 1;
   }

   public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
      if (!simulate) {
         return left;
      }

      Iterator<Ingredient> it = left.iterator();

      while (it.hasNext()) {
         ItemStack[] items = it.next().m_43908_();
         if (items.length != 0
            && !items[0].m_41619_()
            && GTItems.INTEGRATED_CIRCUIT.is(items[0].m_41720_())
            && IntCircuitBehaviour.getCircuitConfiguration(items[0]) == IntCircuitBehaviour.getCircuitConfiguration(this.storage.getStackInSlot(0))) {
            it.remove();
            break;
         }
      }

      return left.isEmpty() ? null : left;
   }
}
