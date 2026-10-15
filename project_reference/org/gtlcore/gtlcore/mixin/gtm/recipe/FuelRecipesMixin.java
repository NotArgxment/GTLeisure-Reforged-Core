package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.data.recipe.misc.FuelRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FuelRecipes.class)
public abstract class FuelRecipesMixin {
   @ModifyArg(
      method = "init",
      at = @At(
         value = "INVOKE",
         target = "Lcom/gregtechceu/gtceu/data/recipe/builder/GTRecipeBuilder;duration(I)Lcom/gregtechceu/gtceu/data/recipe/builder/GTRecipeBuilder;",
         ordinal = 1
      ),
      remap = false
   )
   private static int fix(int duration) {
      return duration < 0 ? 2147483647 : duration;
   }
}
