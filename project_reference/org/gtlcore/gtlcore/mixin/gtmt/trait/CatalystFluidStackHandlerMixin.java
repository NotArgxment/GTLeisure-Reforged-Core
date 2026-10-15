package org.gtlcore.gtlcore.mixin.gtmt.trait;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import org.gtlcore.gtlcore.api.recipe.ingredient.CacheHashStrategies;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CatalystFluidStackHandler.class)
public abstract class CatalystFluidStackHandlerMixin extends NotifiableFluidTank {
   @Unique
   private final Object2LongOpenCustomHashMap<FluidStack> gTLCore$fluidCatalystInventory = new Object2LongOpenCustomHashMap(
      FluidStackHashStrategy.comparingAllButAmount()
   );

   public CatalystFluidStackHandlerMixin(MetaMachine machine, int slots, long capacity, IO io, IO capabilityIO) {
      super(machine, slots, capacity, io, capabilityIO);
   }

   @Overwrite(remap = false)
   public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe recipe, List<FluidIngredient> left, @Nullable String slotName, boolean simulate) {
      if (!simulate) {
         return left;
      }

      ObjectSet<FluidIngredient> catalysts = new ObjectOpenCustomHashSet(CacheHashStrategies.FluidIngredientHashStrategy.INSTANCE);

      for (Content content : recipe.getInputContents(FluidRecipeCapability.CAP)) {
         FluidIngredient fluidIngredient = (FluidIngredient)content.getContent();
         if (content.chance <= 0) {
            for (FluidStack fluidStack : fluidIngredient.getStacks()) {
               if (this.gTLCore$fluidCatalystInventory.getLong(fluidStack) >= fluidStack.getAmount()) {
                  catalysts.add(fluidIngredient);
               }
            }
         } else {
            for (FluidStack fluidStack : fluidIngredient.getStacks()) {
               if (this.gTLCore$fluidCatalystInventory.containsKey(fluidStack)) {
                  return left;
               }
            }
         }
      }

      left.removeIf(catalysts::contains);
      return left.isEmpty() ? null : left;
   }

   public void onContentsChanged() {
      super.onContentsChanged();
      if (!this.machine.isRemote()) {
         this.gTLCore$rebuildMap();
      }
   }

   public void onMachineLoad() {
      super.onMachineLoad();
      if (!this.machine.isRemote()) {
         this.gTLCore$rebuildMap();
         this.notifyListeners();
      }
   }

   @Unique
   private void gTLCore$rebuildMap() {
      this.gTLCore$fluidCatalystInventory.clear();

      for (FluidStorage storage : this.getStorages()) {
         FluidStack fluidStack = storage.getFluid();
         if (!fluidStack.isEmpty()) {
            this.gTLCore$fluidCatalystInventory.addTo(fluidStack, fluidStack.getAmount());
         }
      }
   }
}
