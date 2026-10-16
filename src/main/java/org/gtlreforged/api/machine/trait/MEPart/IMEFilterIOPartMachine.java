package org.gtlreforged.api.machine.trait.MEPart;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public interface IMEFilterIOPartMachine extends IMEIOPartMachine {
   Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> getMERecipeHandlerTraits();

   @NotNull
   IMEFilterIOTrait getMETrait();
}
