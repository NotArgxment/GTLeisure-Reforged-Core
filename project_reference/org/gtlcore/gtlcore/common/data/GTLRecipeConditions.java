package org.gtlcore.gtlcore.common.data;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import org.gtlcore.gtlcore.common.recipe.condition.GravityCondition;

public final class GTLRecipeConditions {
   public static final RecipeConditionType<GravityCondition> GRAVITY = (RecipeConditionType<GravityCondition>)GTRegistries.RECIPE_CONDITIONS
      .register("gravity", new RecipeConditionType(GravityCondition::new, GravityCondition.CODEC));

   private GTLRecipeConditions() {
   }

   public static void init() {
   }
}
