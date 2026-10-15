package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.gregtechceu.gtceu.utils.IngredientEquality.IngredientHashStrategy;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.multiblock.ParallelMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEIORecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEPatternRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.RecipeHandlePart;
import org.gtlcore.gtlcore.api.recipe.chance.LongChanceLogic;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.utils.NumberUtils;

public interface IParallelLogic {
   static GTRecipe getRecipeOutputChance(IRecipeCapabilityHolder holder, GTRecipe recipe) {
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Content>> recipeContents = new Reference2ObjectOpenHashMap();

      for (Entry<RecipeCapability<?>, List<Content>> entry : recipe.outputs.entrySet()) {
         RecipeCapability<?> cap = entry.getKey();
         List<Content> chancedContents = new ObjectArrayList();
         List<Content> contentList = (List<Content>)recipeContents.computeIfAbsent(cap, c -> new ObjectArrayList());

         for (Content cont : entry.getValue()) {
            if (cont.chance >= cont.maxChance) {
               contentList.add(cont);
            } else {
               chancedContents.add(cont);
            }
         }

         if (!chancedContents.isEmpty()) {
            ChanceBoostFunction function = recipe.getType().getChanceFunction();
            int holderTier = holder.getChanceTier();
            Object2IntMap<?> cache = (Object2IntMap<?>)((IRecipeLogicMachine)holder).getRecipeLogic().getChanceCaches().get(cap);
            chancedContents = LongChanceLogic.OR
               .roll(chancedContents, function, ((IGTRecipe)recipe).getEuTier(), holderTier, cache, ((IGTRecipe)recipe).getRealParallels(), cap);
            if (chancedContents != null) {
               for (Content cont : chancedContents) {
                  contentList.add(new Content(cont.content, 10000, 10000, 0, null, null));
               }
            }
         }

         if (contentList.isEmpty()) {
            recipeContents.remove(cap);
         }
      }

      GTRecipe copy = new GTRecipe(
         recipe.recipeType,
         recipe.id,
         recipe.inputs,
         recipeContents,
         recipe.tickInputs,
         recipe.tickOutputs,
         recipe.inputChanceLogics,
         recipe.outputChanceLogics,
         recipe.tickInputChanceLogics,
         recipe.tickOutputChanceLogics,
         recipe.conditions,
         recipe.ingredientActions,
         recipe.data,
         recipe.duration,
         recipe.isFuel
      );
      ((IGTRecipe)copy).setRealParallels(((IGTRecipe)recipe).getRealParallels());
      copy.ocTier = recipe.ocTier;
      return copy;
   }

   static long getParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, long parallelAmount) {
      if (parallelAmount <= 1L) {
         return parallelAmount;
      }

      long maxParallel = getMaxParallel(holder, recipe, parallelAmount);
      return maxParallel == 0L ? 0L : getMinParallel(holder, recipe, maxParallel);
   }

   static long getMaxParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, long parallelAmount) {
      for (RecipeCapability<?> cap : recipe.inputs.keySet()) {
         if (cap == ItemRecipeCapability.CAP) {
            parallelAmount = Math.min(parallelAmount, getInputItemParallel(holder, recipe, parallelAmount));
            if (parallelAmount == 0L) {
               break;
            }
         } else if (cap == FluidRecipeCapability.CAP) {
            parallelAmount = Math.min(parallelAmount, getInputFluidParallel(holder, recipe, parallelAmount));
            if (parallelAmount == 0L) {
               break;
            }
         }
      }

      return parallelAmount;
   }

   static long getMinParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, long parallelAmount) {
      for (Entry<RecipeCapability<?>, List<Content>> entry : recipe.outputs.entrySet()) {
         if (entry.getKey() == ItemRecipeCapability.CAP
            && holder instanceof IRecipeLogicMachine machine
            && !machine.canVoidRecipeOutputs(ItemRecipeCapability.CAP)) {
            parallelAmount = Math.min(parallelAmount, getOutputItemParallel(holder, recipe, entry.getValue(), parallelAmount));
            if (parallelAmount == 0L) {
               break;
            }
         } else if (entry.getKey() == FluidRecipeCapability.CAP
            && holder instanceof IRecipeLogicMachine machine
            && !machine.canVoidRecipeOutputs(FluidRecipeCapability.CAP)) {
            parallelAmount = Math.min(parallelAmount, getOutputFluidParallel(holder, recipe, entry.getValue(), parallelAmount));
            if (parallelAmount == 0L) {
               break;
            }
         }
      }

      return parallelAmount;
   }

   static long getInputItemParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, long parallelAmount) {
      if (parallelAmount <= 1L) {
         return parallelAmount;
      }

      if (holder instanceof IRecipeCapabilityMachine machine) {
         if (machine.emptyRecipeHandlePart()) {
            return 0L;
         }

         Object2LongOpenCustomHashMap<Ingredient> countableMap = new Object2LongOpenCustomHashMap(IngredientHashStrategy.INSTANCE);
         boolean confirmMEStock = machine instanceof ParallelMachine para && para.needConfirmMEStock();

         for (Content content : recipe.getInputContents(ItemRecipeCapability.CAP)) {
            if (content.chance > 0) {
               Ingredient ingredient = (Ingredient)ItemRecipeCapability.CAP.of(content.content);
               long ingredientCount;
               if (ingredient instanceof LongIngredient longIngredient) {
                  ingredientCount = longIngredient.getAmount();
               } else if (ingredient instanceof SizedIngredient sizedIngredient) {
                  ingredientCount = sizedIngredient.getAmount();
               } else {
                  ingredientCount = 1L;
               }

               countableMap.addTo(ingredient, ingredientCount);
            }
         }

         if (countableMap.isEmpty()) {
            return parallelAmount;
         }

         Object2LongOpenCustomHashMap<ItemStack> ingredientStacks = new Object2LongOpenCustomHashMap(ItemStackHashStrategy.comparingAllButCount());
         IRecipeHandlePart handle = machine.getActiveRecipeHandle(recipe);
         if (handle instanceof MEPatternRecipeHandlePart mePatternRecipeHandlePart) {
            ObjectIterator sharedRecipeHandlePart = Object2LongMaps.fastIterable(mePatternRecipeHandlePart.getMEContent(ItemRecipeCapability.CAP, recipe))
               .iterator();

            while (sharedRecipeHandlePart.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack>)sharedRecipeHandlePart.next();
               ingredientStacks.mergeLong((ItemStack)entry.getKey(), entry.getLongValue(), NumberUtils::saturatedAdd);
            }
         } else if (handle instanceof RecipeHandlePart recipeHandlePart) {
            ObjectIterator var19 = Object2LongMaps.fastIterable(recipeHandlePart.getSelfContent(ItemRecipeCapability.CAP, confirmMEStock)).iterator();

            while (var19.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack>)var19.next();
               ingredientStacks.mergeLong((ItemStack)entry.getKey(), entry.getLongValue(), NumberUtils::saturatedAdd);
            }
         } else {
            RecipeHandlePart sharedRecipeHandlePart = machine.getSharedRecipeHandlePart();
            if (sharedRecipeHandlePart != null) {
               ObjectIterator var23 = Object2LongMaps.fastIterable(sharedRecipeHandlePart.getSelfContent(ItemRecipeCapability.CAP, confirmMEStock)).iterator();

               while (var23.hasNext()) {
                  it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<ItemStack>)var23.next();
                  ingredientStacks.mergeLong((ItemStack)entry.getKey(), entry.getLongValue(), NumberUtils::saturatedAdd);
               }
            }
         }

         return calculate(parallelAmount, countableMap, ingredientStacks);
      } else {
         return 1L;
      }
   }

   static long getInputFluidParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, long parallelAmount) {
      if (parallelAmount <= 1L) {
         return parallelAmount;
      }

      if (holder instanceof IRecipeCapabilityMachine machine) {
         if (machine.emptyRecipeHandlePart()) {
            return 0L;
         }

         Object2LongOpenHashMap<FluidIngredient> fluidCountMap = new Object2LongOpenHashMap();
         boolean confirmMEStock = machine instanceof ParallelMachine para && para.needConfirmMEStock();

         for (Content content : recipe.getInputContents(FluidRecipeCapability.CAP)) {
            FluidIngredient fluidInput = (FluidIngredient)FluidRecipeCapability.CAP.of(content.content);
            if (content.chance > 0) {
               fluidCountMap.addTo(fluidInput, fluidInput.getAmount());
            }
         }

         if (fluidCountMap.isEmpty()) {
            return parallelAmount;
         }

         Object2LongOpenCustomHashMap<FluidStack> ingredientStacks = new Object2LongOpenCustomHashMap(FluidStackHashStrategy.comparingAllButAmount());
         IRecipeHandlePart handle = machine.getActiveRecipeHandle(recipe);
         if (handle instanceof MEPatternRecipeHandlePart mePatternRecipeHandlePart) {
            ObjectIterator sharedRecipeHandlePart = Object2LongMaps.fastIterable(mePatternRecipeHandlePart.getMEContent(FluidRecipeCapability.CAP, recipe))
               .iterator();

            while (sharedRecipeHandlePart.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidStack>)sharedRecipeHandlePart.next();
               ingredientStacks.mergeLong((FluidStack)entry.getKey(), entry.getLongValue(), NumberUtils::saturatedAdd);
            }
         } else if (handle instanceof RecipeHandlePart recipeHandlePart) {
            ObjectIterator var21 = Object2LongMaps.fastIterable(
                  machine.isDistinct()
                     ? recipeHandlePart.getSelfContent(FluidRecipeCapability.CAP, confirmMEStock)
                     : recipeHandlePart.getContentWithShared(FluidRecipeCapability.CAP, confirmMEStock)
               )
               .iterator();

            while (var21.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidStack> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidStack>)var21.next();
               ingredientStacks.mergeLong((FluidStack)entry.getKey(), entry.getLongValue(), NumberUtils::saturatedAdd);
            }
         } else {
            RecipeHandlePart sharedRecipeHandlePart = machine.getSharedRecipeHandlePart();
            if (sharedRecipeHandlePart != null) {
               for (IRecipeHandler<FluidIngredient> handler : sharedRecipeHandlePart.getCapability(FluidRecipeCapability.CAP)) {
                  if (!(handler instanceof CatalystFluidStackHandler)) {
                     for (Object object : handler.getContents()) {
                        if (object instanceof FluidStack fs) {
                           ingredientStacks.mergeLong(fs, fs.getAmount(), NumberUtils::saturatedAdd);
                        }
                     }
                  }
               }
            }
         }

         return calculate(parallelAmount, fluidCountMap, ingredientStacks);
      } else {
         return 1L;
      }
   }

   private static <I extends Predicate<S>, S> long calculate(long parallelLimit, Object2LongMap<I> countableMap, Object2LongMap<S> ingredientStacks) {
      if (ingredientStacks.isEmpty()) {
         return 0L;
      }

      ObjectIterator var4 = Object2LongMaps.fastIterable(countableMap).iterator();

      while (var4.hasNext()) {
         it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<I> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<I>)var4.next();
         I ingredient = (I)entry.getKey();
         long needed = entry.getLongValue();
         long available = 0L;
         ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<S>> it = Object2LongMaps.fastIterator(ingredientStacks);

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<S> input = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<S>)it.next();
            if (ingredient.test((S)input.getKey())) {
               available = input.getLongValue();
               it.remove();
               break;
            }
         }

         if (available < needed) {
            return 0L;
         }

         parallelLimit = Math.min(parallelLimit, available / needed);
      }

      return parallelLimit;
   }

   static long getOutputItemParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, List<Content> contents, long multiplier) {
      if (multiplier > 1L && !contents.isEmpty()) {
         if (!(holder instanceof IRecipeCapabilityMachine machine)) {
            return 1L;
         } else {
            if (machine.itemOutPutAlwaysMatch()) {
               return multiplier;
            }

            List<Object> inners = new ObjectArrayList(contents.size());

            for (Content content : contents) {
               inners.add(content.content);
            }

            for (MEIORecipeHandlePart<?> meIOHandler : machine.getMEOutputRecipeHandleParts()) {
               inners = meIOHandler.meHandleOutput(ItemRecipeCapability.CAP, inners, true);
               if (inners.isEmpty()) {
                  return multiplier;
               }
            }

            List<Ingredient> ingredients = new ObjectArrayList(inners.size());

            for (Object inner : inners) {
               Ingredient ingredient = (Ingredient)ItemRecipeCapability.CAP.of(inner);
               if (ingredient instanceof LongIngredient longIngredient) {
                  if (longIngredient.getAmount() > 0) {
                     ingredients.add(ingredient);
                  }
               } else if (ingredient instanceof SizedIngredient sizedIngredient) {
                  if (sizedIngredient.getAmount() > 0) {
                     ingredients.add(ingredient);
                  }
               } else {
                  ingredients.add(ingredient);
               }
            }

            return ingredients.isEmpty() ? multiplier : binarySearchMaxParallel(machine, recipe, ItemRecipeCapability.CAP, ingredients, multiplier);
         }
      } else {
         return multiplier;
      }
   }

   static long getOutputFluidParallel(IRecipeCapabilityHolder holder, GTRecipe recipe, List<Content> contents, long multiplier) {
      if (multiplier > 1L && !contents.isEmpty()) {
         if (!(holder instanceof IRecipeCapabilityMachine machine)) {
            return 1L;
         } else {
            if (machine.fluidOutPutAlwaysMatch()) {
               return multiplier;
            }

            List<Object> inners = new ObjectArrayList(contents.size());

            for (Content content : contents) {
               inners.add(content.content);
            }

            for (MEIORecipeHandlePart<?> meIOHandler : machine.getMEOutputRecipeHandleParts()) {
               inners = meIOHandler.meHandleOutput(FluidRecipeCapability.CAP, inners, true);
               if (inners.isEmpty()) {
                  return multiplier;
               }
            }

            List<FluidIngredient> ingredients = new ObjectArrayList(inners.size());

            for (Object inner : inners) {
               FluidIngredient ingredient = (FluidIngredient)FluidRecipeCapability.CAP.of(inner);
               if (ingredient.getAmount() > 0L) {
                  ingredients.add(ingredient);
               }
            }

            return ingredients.isEmpty() ? multiplier : binarySearchMaxParallel(machine, recipe, FluidRecipeCapability.CAP, ingredients, multiplier);
         }
      } else {
         return multiplier;
      }
   }

   private static <T> long binarySearchMaxParallel(
      IRecipeCapabilityMachine machine, GTRecipe recipe, RecipeCapability<T> capability, List<T> ingredients, long initialMultiplier
   ) {
      List<RecipeHandlePart> handlers = machine.getNormalRecipeHandlePart(IO.OUT);
      if (handlers.isEmpty()) {
         return 0L;
      }

      long left = 1L;
      long right = initialMultiplier;

      while (left < right) {
         long mid = left + (right - left + 1L) / 2L;
         List<T> copied = new ObjectArrayList(ingredients.size());

         for (T ing : ingredients) {
            copied.add((T)capability.copyWithModifier(ing, ContentModifier.multiplier(mid)));
         }

         boolean canHandle = false;

         for (RecipeHandlePart handler : handlers) {
            copied = (List<T>)handler.handleRecipe(IO.OUT, recipe, capability, copied, true);
            if (copied == null || copied.isEmpty()) {
               canHandle = true;
               break;
            }
         }

         if (canHandle) {
            left = mid;
         } else {
            right = mid - 1L;
         }
      }

      return left;
   }
}
