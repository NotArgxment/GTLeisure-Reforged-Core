package org.gtlcore.gtlcore.utils;

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
         ItemStack stack = Registries.getItemStack(compoundTag.m_128461_("id"));
         if (compoundTag.m_128425_("tag", 10)) {
            stack.m_41751_(compoundTag.m_128469_("tag"));
            if (stack.m_41783_() != null) {
               stack.m_41720_().m_142312_(stack.m_41783_());
            }
         }

         if (stack.m_41720_().m_41465_()) {
            stack.m_41721_(stack.m_41773_());
         }

         return stack;
      } catch (RuntimeException var2) {
         GTCEu.LOGGER.debug("Tried to load invalid item: {}", compoundTag, var2);
         return ItemStack.f_41583_;
      }
   }

   public static Tag serializeNBT(GTRecipe recipe) {
      CompoundTag tag = new CompoundTag();
      tag.m_128359_("id", recipe.id.toString());
      tag.m_128365_("recipe", (Tag)GTRecipeSerializer.CODEC.encodeStart(NbtOps.f_128958_, recipe).result().orElse(new CompoundTag()));
      return tag;
   }

   @Nullable
   public static GTRecipe deserializeNBT(Tag tag) {
      if (tag instanceof CompoundTag ctag) {
         ResourceLocation id = ResourceLocation.m_135820_(ctag.m_128461_("id"));
         GTRecipe recipe = (GTRecipe)GTRecipeSerializer.CODEC.parse(NbtOps.f_128958_, ctag.m_128423_("recipe")).result().orElse(null);
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
