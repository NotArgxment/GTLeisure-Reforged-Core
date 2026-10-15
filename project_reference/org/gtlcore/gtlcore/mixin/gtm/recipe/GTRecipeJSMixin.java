package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.GTRecipeJS;
import dev.latvian.mods.kubejs.item.InputItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.utils.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GTRecipeJS.class)
public abstract class GTRecipeJSMixin {
   @Shadow(remap = false)
   public int chance;

   @ModifyArg(
      method = "notConsumable(Ldev/latvian/mods/kubejs/item/InputItem;)Lcom/gregtechceu/gtceu/integration/kjs/recipe/GTRecipeSchema$GTRecipeJS;",
      at = @At(
         value = "INVOKE",
         target = "Lcom/gregtechceu/gtceu/integration/kjs/recipe/GTRecipeSchema$GTRecipeJS;inputItems([Ldev/latvian/mods/kubejs/item/InputItem;)Lcom/gregtechceu/gtceu/integration/kjs/recipe/GTRecipeSchema$GTRecipeJS;"
      ),
      index = 0,
      remap = false
   )
   private InputItem[] modifyInputItemInNotConsumable(InputItem[] inputs) {
      if (inputs.length == 1) {
         InputItem itemStack = inputs[0];
         if (itemStack.count == 64) {
            Ingredient ingredient = itemStack.ingredient;
            ItemStack[] items = ingredient.m_43908_();
            if (items.length > 0) {
               String itemId = Registries.getItemId(items[0]);
               if ("kubejs:ingot_field_shape".equals(itemId)) {
                  return new InputItem[]{InputItem.of(ingredient, 8)};
               }
            }
         }
      }

      return inputs;
   }
}
