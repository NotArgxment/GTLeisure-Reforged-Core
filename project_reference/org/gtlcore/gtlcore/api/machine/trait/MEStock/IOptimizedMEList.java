package org.gtlcore.gtlcore.api.machine.trait.MEStock;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IOptimizedMEList {
   @Nullable
   default Object2LongMap<ItemStack> getMEItemMap() {
      return null;
   }

   @NotNull
   default List<FluidStack> getMEFluidList() {
      return List.of();
   }

   default Object2LongOpenHashMap<ItemStack> getItemMap() {
      return null;
   }

   @NotNull
   default List<FluidStack> getFluidList() {
      return List.of();
   }

   default void setChanged(boolean value) {
   }

   default boolean getChanged() {
      return false;
   }

   default void onConfigChanged() {
   }

   default boolean isStocking() {
      return false;
   }
}
