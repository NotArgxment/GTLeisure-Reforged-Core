package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEIORecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEPatternRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.RecipeHandlePart;
import org.gtlcore.gtlcore.api.recipe.chance.LongChanceLogic;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.jetbrains.annotations.Nullable;

public final class RecipeRunner {
   private RecipeRunner() {
      throw new AssertionError("Utility class should not be instantiated");
   }

   public static RecipeResult handle(
      GTRecipe recipe,
      IO io,
      IRecipeCapabilityHolder holder,
      Map<RecipeCapability<?>, List<Content>> contents,
      Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches,
      boolean simulated,
      RecipeCacheStrategy strategy
   ) {
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent = fillContent(contents, recipe, holder, chanceCaches, simulated);
      if (recipeContent.isEmpty()) {
         return RecipeResult.SUCCESS;
      } else {
         return handleContentsInternal(io, recipe, holder, recipeContent, simulated, strategy)
            ? RecipeResult.SUCCESS
            : (io == IO.IN ? RecipeResult.FAIL_INPUT : (simulated ? generateOutputFailReason(recipeContent) : RecipeResult.fail(null)));
      }
   }

   private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> fillContent(
      Map<RecipeCapability<?>, List<Content>> entries,
      GTRecipe recipe,
      IRecipeCapabilityHolder holder,
      Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches,
      boolean simulated
   ) {
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent = new Reference2ObjectOpenHashMap();

      for (Entry<RecipeCapability<?>, List<Content>> entry : entries.entrySet()) {
         RecipeCapability<?> cap = entry.getKey();
         if (cap.doMatchInRecipe()) {
            List<Content> contents = entry.getValue();
            if (!contents.isEmpty()) {
               List<Content> chancedContents = new ObjectArrayList();
               List<Object> contentList = (List<Object>)recipeContent.computeIfAbsent(cap, c -> new ObjectArrayList());

               for (Content cont : contents) {
                  if (simulated) {
                     contentList.add(cont.content);
                  } else if (cont.chance >= cont.maxChance) {
                     contentList.add(cont.content);
                  } else if (cont.chance != 0) {
                     chancedContents.add(cont);
                  }
               }

               if (!chancedContents.isEmpty()) {
                  ChanceBoostFunction function = recipe.getType().getChanceFunction();
                  int holderTier = holder.getChanceTier();
                  Object2IntMap<?> cache = chanceCaches.get(cap);
                  chancedContents = LongChanceLogic.OR
                     .roll(chancedContents, function, ((IGTRecipe)recipe).getEuTier(), holderTier, cache, ((IGTRecipe)recipe).getRealParallels(), cap);
                  if (chancedContents != null) {
                     for (Content cont : chancedContents) {
                        contentList.add(cont.content);
                     }
                  }
               }

               if (contentList.isEmpty()) {
                  recipeContent.remove(cap);
               }
            }
         }
      }

      return recipeContent;
   }

   private static boolean handleContentsInternal(
      IO capIO,
      GTRecipe recipe,
      IRecipeCapabilityHolder holder,
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent,
      boolean simulated,
      RecipeCacheStrategy strategy
   ) {
      if (holder instanceof IRecipeCapabilityMachine machine) {
         if (machine.emptyHandlePart()) {
            return false;
         }

         if (capIO == IO.IN) {
            if (strategy == RecipeCacheStrategy.NO_CACHE) {
               return machine.isDistinct()
                  ? handleInputDistinctNocache(machine, recipe, recipeContent, simulated)
                  : handleInputNotDistinctNocache(machine, recipe, recipeContent, simulated);
            } else {
               return machine.isDistinct()
                  ? handleInputDistinct(machine, recipe, recipeContent, simulated, strategy)
                  : handleInputNotDistinct(machine, recipe, recipeContent, simulated, strategy);
            }
         } else {
            recipeContent = handleMEOutput(machine.getMEOutputRecipeHandleParts(), recipeContent, simulated);
            if (recipeContent.isEmpty()) {
               return true;
            }

            recipeContent = handleNormalOutput(machine.getNormalRecipeHandlePart(IO.OUT), recipe, recipeContent, simulated);
            return recipeContent.isEmpty();
         }
      } else {
         return false;
      }
   }

   private static boolean handleInputDistinct(
      IRecipeCapabilityMachine machine,
      GTRecipe recipe,
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent,
      boolean simulated,
      RecipeCacheStrategy strategy
   ) {
      Iterator<IRecipeHandlePart> it = machine.getAllCachedRecipeHandlesIter(recipe);

      while (it.hasNext()) {
         IRecipeHandlePart handler = it.next();
         if (handler instanceof MEPatternRecipeHandlePart cachedMEPart) {
            int slot = cachedMEPart.handleRecipe(recipe, recipeContent, simulated, strategy.cacheToMEInternal);
            if (slot != -1) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  if (slot == -2) {
                     machine.tryAddAndActiveRhp(recipe, cachedMEPart);
                  } else {
                     machine.tryAddAndActiveMERhp(cachedMEPart, recipe, slot);
                  }
               }

               return true;
            }
         } else if (handler instanceof RecipeHandlePart cachedNormalPart) {
            Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> result = cachedNormalPart.handleRecipe(IO.IN, recipe, recipeContent, simulated);
            if (result.isEmpty()) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveRhp(recipe, cachedNormalPart);
               }

               return true;
            }
         }
      }

      ReferenceSet<IRecipeHandlePart> cachedHandlers = machine.getAllCachedRecipeHandles(recipe);

      for (MEPatternRecipeHandlePart part : machine.getMEPatternRecipeHandleParts()) {
         if (!cachedHandlers.contains(part)) {
            int slot = part.handleRecipe(recipe, recipeContent, simulated, strategy.cacheToMEInternal);
            if (slot >= 0) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveMERhp(part, recipe, slot);
               }

               return true;
            }
         }
      }

      for (RecipeHandlePart part : machine.getNormalRecipeHandlePart(IO.IN)) {
         if (!cachedHandlers.contains(part)) {
            Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> result = part.handleRecipe(IO.IN, recipe, recipeContent, simulated);
            if (result.isEmpty()) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveRhp(recipe, part);
               }

               return true;
            }
         }
      }

      return false;
   }

   private static boolean handleInputNotDistinct(
      IRecipeCapabilityMachine machine,
      GTRecipe recipe,
      Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent,
      boolean simulated,
      RecipeCacheStrategy strategy
   ) {
      boolean fluidHandleResult = false;
      boolean hasFluidTry = false;
      Iterator<IRecipeHandlePart> it = machine.getAllCachedRecipeHandlesIter(recipe);

      while (it.hasNext()) {
         IRecipeHandlePart handler = it.next();
         if (handler instanceof MEPatternRecipeHandlePart cachedMEPart) {
            int slot = cachedMEPart.handleRecipe(recipe, recipeContent, simulated, strategy.cacheToMEInternal);
            if (slot != -1) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  if (slot == -2) {
                     machine.tryAddAndActiveRhp(recipe, cachedMEPart);
                  } else {
                     machine.tryAddAndActiveMERhp(cachedMEPart, recipe, slot);
                  }
               }

               return true;
            }
         } else if (handler instanceof RecipeHandlePart cachedNormalPart) {
            if (!hasFluidTry) {
               fluidHandleResult = tryNotDistinctFluid(machine.getSharedRecipeHandlePart(), recipe, recipeContent, simulated);
               hasFluidTry = true;
            }

            if (fluidHandleResult && tryNotDistinctItem(cachedNormalPart, recipe, recipeContent, simulated)) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveRhp(recipe, cachedNormalPart);
               }

               return true;
            }
         }
      }

      ReferenceSet<IRecipeHandlePart> cachedHandlers = machine.getAllCachedRecipeHandles(recipe);

      for (MEPatternRecipeHandlePart part : machine.getMEPatternRecipeHandleParts()) {
         if (!cachedHandlers.contains(part)) {
            int slot = part.handleRecipe(recipe, recipeContent, simulated, strategy.cacheToMEInternal);
            if (slot >= 0) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveMERhp(part, recipe, slot);
               }

               return true;
            }
         }
      }

      RecipeHandlePart sharedPart = machine.getSharedRecipeHandlePart();
      List<?> fluidContent = (List<?>)recipeContent.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
      if (!fluidContent.isEmpty()) {
         if (sharedPart == null) {
            return false;
         }

         fluidContent = sharedPart.handleRecipe(IO.IN, recipe, FluidRecipeCapability.CAP, fluidContent, simulated);
         if (fluidContent != null && !fluidContent.isEmpty()) {
            return false;
         }
      }

      List<?> itemContent = (List<?>)recipeContent.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
      if (itemContent.isEmpty()) {
         if (simulated && strategy.cacheToHandlePartMap) {
            machine.tryAddAndActiveRhp(recipe, sharedPart);
         }

         return true;
      } else {
         for (RecipeHandlePart part : machine.getNormalRecipeHandlePart(IO.IN)) {
            if (!cachedHandlers.contains(part)) {
               List<?> result = part.handleRecipe(IO.IN, recipe, ItemRecipeCapability.CAP, itemContent, simulated);
               if (result == null || result.isEmpty()) {
                  if (simulated && strategy.cacheToHandlePartMap) {
                     machine.tryAddAndActiveRhp(recipe, part);
                  }

                  return true;
               }
            }
         }

         if (sharedPart != null && !cachedHandlers.contains(sharedPart)) {
            List<?> result = sharedPart.handleRecipe(IO.IN, recipe, ItemRecipeCapability.CAP, itemContent, simulated);
            if (result == null || result.isEmpty()) {
               if (simulated && strategy.cacheToHandlePartMap) {
                  machine.tryAddAndActiveRhp(recipe, sharedPart);
               }

               return true;
            }
         }

         return false;
      }
   }

   private static boolean tryNotDistinctFluid(
      @Nullable RecipeHandlePart sharedPart, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      List<?> fluidContent = (List<?>)recipeContent.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
      if (fluidContent.isEmpty()) {
         return true;
      }

      if (sharedPart == null) {
         return false;
      }

      fluidContent = sharedPart.handleRecipe(IO.IN, recipe, FluidRecipeCapability.CAP, fluidContent, simulated);
      return fluidContent == null || fluidContent.isEmpty();
   }

   private static boolean tryNotDistinctItem(
      RecipeHandlePart cachedPart, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      List<?> itemContent = (List<?>)recipeContent.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
      if (itemContent.isEmpty()) {
         return true;
      }

      List<?> result = cachedPart.handleRecipe(IO.IN, recipe, ItemRecipeCapability.CAP, itemContent, simulated);
      return result == null || result.isEmpty();
   }

   private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleNormalOutput(
      List<RecipeHandlePart> handlers, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      if (handlers.isEmpty()) {
         return recipeContent;
      }

      handlers.sort(RecipeHandlePart.COMPARATOR.reversed());

      for (RecipeHandlePart handler : handlers) {
         recipeContent = handler.handleRecipe(IO.OUT, recipe, recipeContent, simulated);
         if (recipeContent.isEmpty()) {
            break;
         }
      }

      return recipeContent;
   }

   private static Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> handleMEOutput(
      List<MEIORecipeHandlePart<?>> meHandlers, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      for (MEIORecipeHandlePart<?> meHandler : meHandlers) {
         recipeContent = meHandler.meHandleOutput(recipeContent, simulated);
         if (recipeContent.isEmpty()) {
            break;
         }
      }

      return recipeContent;
   }

   private static RecipeResult generateOutputFailReason(Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent) {
      StringBuilder builder = new StringBuilder();
      ObjectIterator var2 = Reference2ObjectMaps.fastIterable(recipeContent).iterator();

      while (var2.hasNext()) {
         it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<Object>> entry = (it.unimi.dsi.fastutil.objects.Reference2ObjectMap.Entry<RecipeCapability<?>, List<Object>>)var2.next();
         RecipeCapability<?> cap = (RecipeCapability<?>)entry.getKey();

         for (Object ing : (List)entry.getValue()) {
            if (cap == ItemRecipeCapability.CAP) {
               if (ing instanceof LongIngredient li && li.m_43908_().length >= 1) {
                  builder.append(li.m_43908_()[0].m_41611_().getString()).append("x ").append(li.getActualAmount()).append(" ");
               } else if (ing instanceof SizedIngredient si && si.m_43908_().length >= 1) {
                  builder.append(si.m_43908_()[0].m_41611_().getString()).append("x ").append(si.getAmount()).append(" ");
               }
            } else if (cap == FluidRecipeCapability.CAP && ing instanceof FluidIngredient fi && fi.getStacks().length >= 1) {
               builder.append(fi.getStacks()[0].getDisplayName().getString()).append("x ").append(fi.getAmount()).append(" ");
            }
         }
      }

      return RecipeResult.fail(Component.m_237110_("gtceu.recipe.fail.Output.Content", new Object[]{builder}));
   }

   private static boolean handleInputDistinctNocache(
      IRecipeCapabilityMachine machine, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      for (RecipeHandlePart part : machine.getNormalRecipeHandlePart(IO.IN)) {
         Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> result = part.handleRecipe(IO.IN, recipe, recipeContent, simulated);
         if (result.isEmpty()) {
            return true;
         }
      }

      for (MEPatternRecipeHandlePart part : machine.getMEPatternRecipeHandleParts()) {
         int slot = part.handleRecipe(recipe, recipeContent, simulated, false);
         if (slot >= 0) {
            return true;
         }
      }

      return false;
   }

   private static boolean handleInputNotDistinctNocache(
      IRecipeCapabilityMachine machine, GTRecipe recipe, Reference2ObjectOpenHashMap<RecipeCapability<?>, List<Object>> recipeContent, boolean simulated
   ) {
      RecipeHandlePart sharedPart = machine.getSharedRecipeHandlePart();
      boolean fluidHandled = true;
      List<?> fluidContent = (List<?>)recipeContent.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList());
      if (!fluidContent.isEmpty()) {
         if (sharedPart == null) {
            fluidHandled = false;
         } else {
            fluidContent = sharedPart.handleRecipe(IO.IN, recipe, FluidRecipeCapability.CAP, fluidContent, simulated);
            if (fluidContent != null && !fluidContent.isEmpty()) {
               fluidHandled = false;
            }
         }
      }

      if (fluidHandled) {
         List<?> itemContent = (List<?>)recipeContent.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList());
         if (itemContent.isEmpty()) {
            return true;
         }

         for (RecipeHandlePart part : machine.getNormalRecipeHandlePart(IO.IN)) {
            List<?> result = part.handleRecipe(IO.IN, recipe, ItemRecipeCapability.CAP, itemContent, simulated);
            if (result == null || result.isEmpty()) {
               return true;
            }
         }

         if (sharedPart != null) {
            List<?> result = sharedPart.handleRecipe(IO.IN, recipe, ItemRecipeCapability.CAP, itemContent, simulated);
            if (result == null || result.isEmpty()) {
               return true;
            }
         }
      }

      for (MEPatternRecipeHandlePart part : machine.getMEPatternRecipeHandleParts()) {
         int slot = part.handleRecipe(recipe, recipeContent, simulated, false);
         if (slot >= 0) {
            return true;
         }
      }

      return false;
   }
}
