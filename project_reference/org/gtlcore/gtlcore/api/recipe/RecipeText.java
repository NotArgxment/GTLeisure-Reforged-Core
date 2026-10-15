package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.utils.NumberUtils;

public class RecipeText {
   public static MutableComponent getRecipeInputText(GTRecipe recipe) {
      return getIngredientText(recipe, true);
   }

   public static MutableComponent getRecipeOutputText(GTRecipe recipe) {
      return getIngredientText(recipe, false);
   }

   private static MutableComponent getIngredientText(GTRecipe recipe, boolean io) {
      if (recipe != null) {
         MutableComponent buffer = io
            ? Component.m_237115_("gtceu.top.recipe_input").m_130940_(ChatFormatting.WHITE)
            : Component.m_237115_("gtceu.top.recipe_output").m_130940_(ChatFormatting.WHITE);
         List<Content> items = io ? recipe.getInputContents(ItemRecipeCapability.CAP) : recipe.getOutputContents(ItemRecipeCapability.CAP);
         List<Content> fluids = io ? recipe.getInputContents(FluidRecipeCapability.CAP) : recipe.getOutputContents(FluidRecipeCapability.CAP);

         for (Content item : items) {
            ItemStack[] stacks = ((Ingredient)ItemRecipeCapability.CAP.of(item.content)).m_43908_();
            if (stacks.length != 0) {
               ItemStack stack = stacks[0];
               int count = stack.m_41613_();
               float chance = 100.0F * item.chance / item.maxChance;
               String percent = FormattingUtil.formatPercent(chance);
               if (chance == 100.0F) {
                  buffer.m_7220_(Component.m_237110_("gtceu.machine.lockrecipe.line.0", new Object[]{stack.m_41786_(), count}).m_130940_(ChatFormatting.GRAY));
               } else if (chance == 0.0F) {
                  buffer.m_7220_(Component.m_237110_("gtceu.machine.lockrecipe.line.2", new Object[]{stack.m_41786_(), count}).m_130940_(ChatFormatting.GRAY));
               } else {
                  buffer.m_7220_(
                     Component.m_237110_("gtceu.machine.lockrecipe.line.1", new Object[]{stack.m_41786_(), count, percent}).m_130940_(ChatFormatting.GRAY)
                  );
               }
            }
         }

         for (Content fluid : fluids) {
            FluidStack[] stacks = ((FluidIngredient)FluidRecipeCapability.CAP.of(fluid.content)).getStacks();
            if (stacks.length != 0) {
               FluidStack stack = stacks[0];
               long amount = stack.getAmount();
               String s;
               if (amount < 1000L) {
                  s = amount + "mB";
               } else {
                  s = NumberUtils.formatLong(amount / 1000L) + "B";
               }

               float chance = 100.0F * fluid.chance / fluid.maxChance;
               String percent = FormattingUtil.formatPercent(chance);
               if (chance == 100.0F) {
                  buffer.m_7220_(Component.m_237110_("gtceu.machine.lockrecipe.line.0", new Object[]{stack.getDisplayName(), s}).m_130940_(ChatFormatting.GRAY));
               } else if (chance == 0.0F) {
                  buffer.m_7220_(Component.m_237110_("gtceu.machine.lockrecipe.line.2", new Object[]{stack.getDisplayName(), s}).m_130940_(ChatFormatting.GRAY));
               } else {
                  buffer.m_7220_(
                     Component.m_237110_("gtceu.machine.lockrecipe.line.1", new Object[]{stack.getDisplayName(), s, percent}).m_130940_(ChatFormatting.GRAY)
                  );
               }
            }
         }

         return buffer;
      } else {
         return null;
      }
   }
}
