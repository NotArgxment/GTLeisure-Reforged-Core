package org.gtlcore.gtlcore.api.recipe.chance;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.boost.ChanceBoostFunction;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.recipe.IAdvancedContentModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public abstract class LongChanceLogic extends ChanceLogic {
   public static final LongChanceLogic OR = new LongChanceLogic("longOr") {
      @Nullable
      @Override
      public @Unmodifiable List<@NotNull Content> roll(
         @NotNull List<Content> chancedEntries,
         @NotNull ChanceBoostFunction boostFunction,
         int baseTier,
         int machineTier,
         @Nullable Object2IntMap<?> cache,
         long times,
         RecipeCapability<?> cap
      ) {
         List<Content> out = new ObjectArrayList(chancedEntries.size());

         for (Content entry : chancedEntries) {
            int maxChance = entry.maxChance;
            int newChance = getChance(entry, boostFunction, baseTier, machineTier);
            modifyByChanceSafe(cache, times, cap, out, entry, maxChance, newChance);
         }

         return out.isEmpty() ? null : out;
      }

      @Nullable
      public @Unmodifiable List<@NotNull Content> roll(
         @NotNull List<Content> chancedEntries,
         @NotNull ChanceBoostFunction boostFunction,
         int baseTier,
         int machineTier,
         @Nullable Object2IntMap<?> cache,
         int times,
         RecipeCapability<?> cap
      ) {
         List<Content> out = new ObjectArrayList(chancedEntries.size());

         for (Content entry : chancedEntries) {
            int maxChance = entry.maxChance;
            int newChance = getChance(entry, boostFunction, baseTier, machineTier);
            modifyByChanceSafe(cache, times, cap, out, entry, maxChance, newChance);
         }

         return out.isEmpty() ? null : out;
      }

      @NotNull
      public Component getTranslation() {
         return Component.m_237115_("gtceu.chance_logic.or");
      }

      public String toString() {
         return "LongChanceLogic{OR}";
      }
   };

   public LongChanceLogic(String id) {
      super(id);
   }

   public static int getChance(@NotNull Content entry, @NotNull ChanceBoostFunction boostFunction, int baseTier, int machineTier) {
      return boostFunction.getBoostedChance(entry, baseTier, machineTier);
   }

   public static int getCachedChance(Content entry, @Nullable Object2IntMap<?> cache) {
      return cache == null ? GTValues.RNG.m_188503_(entry.maxChance) : cache.getOrDefault(entry.content, GTValues.RNG.m_188503_(entry.maxChance));
   }

   public static void updateCachedChance(Object ingredient, @Nullable Object2IntMap<?> cache, int chance) {
      if (cache != null) {
         cache.put(ingredient, chance);
      }
   }

   public static void modifyByChanceSafe(
      @Nullable Object2IntMap<?> cache, long times, RecipeCapability<?> cap, List<Content> out, Content entry, int maxChance, int chance
   ) {
      long timesQuotient = times / maxChance;
      long timesRemainder = times % maxChance;
      long guaranteed = timesQuotient * chance + timesRemainder * chance / maxChance;
      if (guaranteed > 0L) {
         out.add(entry.copy(cap, IAdvancedContentModifier.preciseDivision(guaranteed, times)));
      }

      int newChance = (int)(timesRemainder * chance % maxChance);
      int cached = getCachedChance(entry, cache);
      int chanceSum = newChance + cached;
      if (chanceSum >= maxChance) {
         int bonusCount = chanceSum / maxChance;
         out.add(entry.copy(cap, IAdvancedContentModifier.preciseDivision(bonusCount, times)));
         newChance -= bonusCount * maxChance;
      }

      updateCachedChance(entry.content, cache, newChance / 2 + cached);
   }

   @Nullable
   public abstract @Unmodifiable List<@NotNull Content> roll(
      @NotNull @Unmodifiable List<Content> var1,
      @NotNull ChanceBoostFunction var2,
      int var3,
      int var4,
      @Nullable Object2IntMap<?> var5,
      long var6,
      RecipeCapability<?> var8
   );

   static {
      GTRegistries.CHANCE_LOGICS.unfreeze();
      GTRegistries.CHANCE_LOGICS.freeze();
   }
}
