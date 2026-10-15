package org.gtlcore.gtlcore.mixin.gtmt.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystItemStackHandler;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import java.util.function.Function;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.ingredient.CacheHashStrategies;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CatalystItemStackHandler.class)
public abstract class CatalystItemStackHandlerMixin extends NotifiableItemStackHandler {
   @Unique
   private final Object2IntOpenCustomHashMap<ItemStack> gTLCore$itemCatalystInventory = new Object2IntOpenCustomHashMap(
      ItemStackHashStrategy.comparingAllButCount()
   );

   public CatalystItemStackHandlerMixin(
      MetaMachine machine, int slots, @NotNull IO handlerIO, @NotNull IO capabilityIO, Function<Integer, ItemStackTransfer> transferFactory
   ) {
      super(machine, slots, handlerIO, capabilityIO, transferFactory);
   }

   @Overwrite(remap = false)
   public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
      if (!simulate) {
         return left;
      }

      ObjectSet<Ingredient> catalysts = new ObjectOpenCustomHashSet(CacheHashStrategies.IngredientHashStrategy.INSTANCE);

      for (Content content : recipe.getInputContents(ItemRecipeCapability.CAP)) {
         Ingredient ingredient = (Ingredient)content.getContent();
         if (content.chance <= 0) {
            for (ItemStack item : ingredient.m_43908_()) {
               if (this.gTLCore$itemCatalystInventory.getInt(item) >= item.m_41613_()) {
                  catalysts.add(ingredient);
               }
            }
         } else {
            for (ItemStack item : ingredient.m_43908_()) {
               if (this.gTLCore$itemCatalystInventory.containsKey(item)) {
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
      this.gTLCore$itemCatalystInventory.clear();

      for (int i = 0; i < this.storage.getSlots(); i++) {
         ItemStack itemStack = this.storage.getStackInSlot(i);
         if (!itemStack.m_41619_()) {
            this.gTLCore$itemCatalystInventory.addTo(itemStack, itemStack.m_41613_());
         }
      }
   }
}
