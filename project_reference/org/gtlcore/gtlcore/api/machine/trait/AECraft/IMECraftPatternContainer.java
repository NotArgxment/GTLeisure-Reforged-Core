package org.gtlcore.gtlcore.api.machine.trait.AECraft;

import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public interface IMECraftPatternContainer {
   static int sumNonEmpty(IItemTransfer itemTransfer) {
      int sum = 0;
      int i = 0;

      for (int n = itemTransfer.getSlots(); i < n; i++) {
         ItemStack stack = itemTransfer.getStackInSlot(i);
         if (!stack.m_41619_()) {
            sum++;
         }
      }

      return sum;
   }

   BlockPos getBlockPos();

   int getSlots();
}
