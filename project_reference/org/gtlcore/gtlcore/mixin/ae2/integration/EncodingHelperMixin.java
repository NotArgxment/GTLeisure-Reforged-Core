package org.gtlcore.gtlcore.mixin.ae2.integration;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.InventoryActionPacket;
import appeng.helpers.InventoryAction;
import appeng.integration.modules.jeirei.EncodingHelper;
import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.FakeSlot;
import appeng.parts.encoding.EncodingMode;
import appeng.util.CraftingRecipeUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.apache.commons.lang3.tuple.Pair;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EncodingHelper.class)
public abstract class EncodingHelperMixin {
   @Shadow(remap = false)
   @Final
   static Comparator<GridInventoryEntry> ENTRY_COMPARATOR;

   @Shadow(remap = false)
   public static Map<AEKey, Integer> getIngredientPriorities(MEStorageMenu menu, Comparator<GridInventoryEntry> comparator) {
      return null;
   }

   @Overwrite(remap = false)
   private static GenericStack findBestIngredient(Map<AEKey, Integer> ingredientPriorities, List<GenericStack> possibleIngredients) {
      List<GenericStack> list = possibleIngredients.stream()
         .map(gi -> Pair.of(gi, ingredientPriorities.getOrDefault(gi.what(), -2147483648)))
         .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
         .<GenericStack>map(Pair::getLeft)
         .toList();
      return list.stream()
         .filter(gi -> gi.what() instanceof AEItemKey aeItemKey && Registries.getItemId(aeItemKey.getItem()).contains("universal_circuit"))
         .findFirst()
         .orElseGet(() -> list.stream().findFirst().orElseThrow());
   }

   @Overwrite(remap = false)
   public static void encodeCraftingRecipe(
      PatternEncodingTermMenu menu, @Nullable Recipe<?> recipe, List<List<GenericStack>> genericIngredients, Predicate<ItemStack> visiblePredicate
   ) {
      if (recipe != null && recipe.m_6671_().equals(RecipeType.f_44112_)) {
         menu.setMode(EncodingMode.STONECUTTING);
         menu.setStonecuttingRecipeId(recipe.m_6423_());
      } else if (recipe != null && recipe.m_6671_().equals(RecipeType.f_44113_)) {
         menu.setMode(EncodingMode.SMITHING_TABLE);
      } else {
         menu.setMode(EncodingMode.CRAFTING);
      }

      Map<AEKey, Integer> prioritizedNetworkInv = getIngredientPriorities(menu, ENTRY_COMPARATOR);
      NonNullList<ItemStack> encodedInputs = NonNullList.m_122780_(menu.getCraftingGridSlots().length, ItemStack.f_41583_);
      if (recipe != null) {
         NonNullList<Ingredient> ingredients3x3 = CraftingRecipeUtil.ensure3by3CraftingMatrix(recipe);

         for (int slot = 0; slot < ingredients3x3.size(); slot++) {
            Ingredient ingredient = (Ingredient)ingredients3x3.get(slot);
            if (!ingredient.m_43947_()) {
               List<ItemStack> bestNetworkIngredient = prioritizedNetworkInv.entrySet()
                  .stream()
                  .filter(ni -> ni.getKey() instanceof AEItemKey itemKey && itemKey.matches(ingredient))
                  .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                  .map(entry -> entry.getKey() instanceof AEItemKey itemKey ? itemKey.toStack() : null)
                  .toList();
               ItemStack bestIngredient = bestNetworkIngredient.stream()
                  .filter(itemStack -> Registries.getItemId(itemStack).contains("universal_circuit"))
                  .findFirst()
                  .orElseGet(() -> bestNetworkIngredient.stream().findFirst().orElseGet(() -> {
                     for (ItemStack stack : ingredient.m_43908_()) {
                        if (visiblePredicate.test(stack)) {
                           return stack;
                        }
                     }

                     return ingredient.m_43908_()[0];
                  }));
               encodedInputs.set(slot, bestIngredient);
            }
         }
      } else {
         for (int slot = 0; slot < genericIngredients.size(); slot++) {
            List<GenericStack> genericIngredient = genericIngredients.get(slot);
            if (!genericIngredient.isEmpty()) {
               AEKey bestIngredient = findBestIngredient(prioritizedNetworkInv, genericIngredient).what();
               if (bestIngredient instanceof AEItemKey itemKey) {
                  encodedInputs.set(slot, itemKey.toStack());
               } else {
                  encodedInputs.set(slot, GenericStack.wrapInItemStack(bestIngredient, 1L));
               }
            }
         }
      }

      for (int i = 0; i < encodedInputs.size(); i++) {
         ItemStack encodedInput = (ItemStack)encodedInputs.get(i);
         NetworkHandler.instance().sendToServer(new InventoryActionPacket(InventoryAction.SET_FILTER, menu.getCraftingGridSlots()[i].f_40219_, encodedInput));
      }

      for (FakeSlot outputSlot : menu.getProcessingOutputSlots()) {
         NetworkHandler.instance().sendToServer(new InventoryActionPacket(InventoryAction.SET_FILTER, outputSlot.f_40219_, ItemStack.f_41583_));
      }
   }
}
