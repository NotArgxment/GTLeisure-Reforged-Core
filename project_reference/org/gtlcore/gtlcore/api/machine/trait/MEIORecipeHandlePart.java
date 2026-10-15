package org.gtlcore.gtlcore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.capability.IMERecipeHandler;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOTrait;
import org.jetbrains.annotations.NotNull;

public class MEIORecipeHandlePart<T extends IMEFilterIOTrait> {
   public static final Comparator<MEIORecipeHandlePart<?>> COMPARATOR = Comparator.comparingInt(MEIORecipeHandlePart::getTotalPriority);
   @NotNull
   protected final T meTrait;
   @NotNull
   protected final IMERecipeHandler<Ingredient, ItemStack> itemHandler;
   @NotNull
   protected final IMERecipeHandler<FluidIngredient, FluidStack> fluidHandler;
   protected final IMERecipeHandler<?, ?>[] handlers;

   public MEIORecipeHandlePart(
      @NotNull T meTrait, @NotNull IMERecipeHandler<Ingredient, ItemStack> itemHandler, @NotNull IMERecipeHandler<FluidIngredient, FluidStack> fluidHandler
   ) {
      this.itemHandler = itemHandler;
      this.fluidHandler = fluidHandler;
      this.handlers = new IMERecipeHandler[]{itemHandler, fluidHandler};
      this.meTrait = meTrait;
   }

   public static MEIORecipeHandlePart<IMEFilterIOTrait> of(IMEFilterIOPartMachine machine) {
      IMEFilterIOTrait meTrait = machine.getMETrait();
      Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> pair = machine.getMERecipeHandlerTraits();
      return new MEIORecipeHandlePart<>(
         meTrait, (IMERecipeHandler<Ingredient, ItemStack>)pair.left(), (IMERecipeHandler<FluidIngredient, FluidStack>)pair.right()
      );
   }

   public boolean hasItemFilter() {
      return this.meTrait.hasItemFilter();
   }

   public boolean hasFluidFilter() {
      return this.meTrait.hasFluidFilter();
   }

   @NotNull
   public <I extends Predicate<S>, S> IMERecipeHandler<I, S> getMECapability(RecipeCapability<?> cap) {
      if (cap == ItemRecipeCapability.CAP) {
         return (IMERecipeHandler<I, S>)this.itemHandler;
      } else if (cap == FluidRecipeCapability.CAP) {
         return (IMERecipeHandler<I, S>)this.fluidHandler;
      } else {
         throw new AssertionError("Invalid recipe capability");
      }
   }

   public Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> meHandleOutput(
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> contents, boolean simulate
   ) {
      boolean hasOutput = false;
      ObjectIterator<Entry<RecipeCapability<?>, List<Object>>> it = Reference2ObjectMaps.fastIterator(contents);

      while (it.hasNext()) {
         Entry<RecipeCapability<?>, List<Object>> entry = (Entry<RecipeCapability<?>, List<Object>>)it.next();
         List<Object> content = (List<Object>)entry.getValue();
         if (content.isEmpty()) {
            it.remove();
         } else {
            RecipeCapability<?> cap = (RecipeCapability<?>)entry.getKey();
            List<Object> result = this.meHandleOutput(cap, content, simulate);
            if (result.size() != content.size()) {
               hasOutput = true;
               if (result.isEmpty()) {
                  it.remove();
               } else {
                  entry.setValue(result);
               }
            }
         }
      }

      if (!simulate && hasOutput) {
         this.meTrait.notifySelfIO();
      }

      return contents;
   }

   public <I> List<Object> meHandleOutput(RecipeCapability<?> cap, List<I> content, boolean simulate) {
      IMERecipeHandler<Predicate<Object>, Object> meHandler = this.getMECapability(cap);
      return meHandler.meHandleRecipeOutput(content, simulate);
   }

   private int getTotalPriority() {
      return this.itemHandler.getPriority() + this.fluidHandler.getPriority();
   }
}
