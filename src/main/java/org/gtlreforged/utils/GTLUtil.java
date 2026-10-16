package org.gtlreforged.utils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GTLUtil {

   public static ItemStack loadItemStack(CompoundTag compoundTag) {
      try {
         ItemStack stack = Registries.getItemStack(compoundTag.getString("id"));
         if (compoundTag.contains("tag", 10)) {
            stack.setTag(compoundTag.getCompound("tag"));
            if (stack.getTag() != null) {
               stack.getItem().verifyTagAfterLoad(stack.getTag());
            }
         }

         if (stack.getItem().canBeDepleted()) {
            stack.setDamageValue(stack.getDamageValue());
         }

         return stack;
      } catch (RuntimeException var2) {
         GTCEu.LOGGER.debug("Tried to load invalid item: {}", compoundTag, var2);
         return ItemStack.EMPTY;
      }
   }

   public static Tag serializeNBT(GTRecipe recipe) {
      CompoundTag tag = new CompoundTag();
      tag.putString("id", recipe.id.toString());
      tag.put("recipe", (Tag)GTRecipeSerializer.CODEC.encodeStart(NbtOps.INSTANCE, recipe).result().orElse(new CompoundTag()));
      return tag;
   }

   @Nullable
   public static GTRecipe deserializeNBT(Tag tag) {
      if (tag instanceof CompoundTag ctag) {
         ResourceLocation id = ResourceLocation.tryParse(ctag.getString("id"));
         GTRecipe recipe = (GTRecipe)GTRecipeSerializer.CODEC.parse(NbtOps.INSTANCE, ctag.get("recipe")).result().orElse(null);
         if (recipe != null && id != null) {
            recipe.setId(id);
            return recipe;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
