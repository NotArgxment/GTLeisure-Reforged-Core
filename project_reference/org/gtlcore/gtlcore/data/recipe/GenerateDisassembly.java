package org.gtlcore.gtlcore.data.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.common.data.GTLRecipes;
import org.gtlcore.gtlcore.utils.Registries;

public class GenerateDisassembly {
   private static final String[] inputItem = new String[]{
      "suprachronal_", "_circuit", "_processor", "_assembly", "_computer", "_mainframe", "circuit_resonatic_"
   };
   private static final String[] outputItem = new String[]{
      "_cable",
      "_frame",
      "_fence",
      "_electric_motor",
      "_electric_pump",
      "_conveyor_module",
      "_electric_piston",
      "_robot_arm",
      "_field_generator",
      "_emitter",
      "_sensor",
      "smd_",
      "_lamp",
      "_crate",
      "_drum",
      "_machine_casing",
      "ae2:blank_pattern",
      "gtceu:carbon_nanoswarm"
   };

   private static boolean isExcludeItems(String id, String[] excludeItems) {
      for (String pattern : excludeItems) {
         if (id.contains(pattern)) {
            return true;
         }
      }

      return false;
   }

   public static void generateDisassembly(GTRecipeBuilder r, Consumer<FinishedRecipe> p) {
      List<Content> c = (List<Content>)r.output.getOrDefault(ItemRecipeCapability.CAP, null);
      if (c == null) {
         GTLCore.LOGGER.atError().log("\u914d\u65b9{}\u6ca1\u6709\u8f93\u51fa", r.id);
      } else {
         ItemStack[] outputs = ((Ingredient)ItemRecipeCapability.CAP.of(c.get(0).getContent())).m_43908_();
         if (outputs.length != 0) {
            ItemStack output = outputs[0];
            String id = Registries.getItemId(output);
            boolean cal = r.recipeType == GTRecipeTypes.get("circuit_assembly_line");
            if (!isExcludeItems(id, outputItem)) {
               GTRecipeBuilder builder = GTLRecipeTypes.DISASSEMBLY_RECIPES
                  .recipeBuilder(new ResourceLocation(id), new Object[0])
                  .inputItems(output)
                  .duration(1)
                  .EUt(1L);
               if (GTLRecipes.DISASSEMBLY_RECORD.remove(id) && !cal) {
                  GTLRecipes.DISASSEMBLY_RECORD.add(id);
               } else {
                  GTLRecipes.DISASSEMBLY_RECORD.add(id);
                  List<Content> itemList = (List<Content>)r.input.getOrDefault(ItemRecipeCapability.CAP, null);
                  List<Content> fluidList = (List<Content>)r.input.getOrDefault(FluidRecipeCapability.CAP, null);
                  if (itemList != null) {
                     itemList.forEach(
                        content -> {
                           ItemStack[] items = ((Ingredient)ItemRecipeCapability.CAP.of(content.getContent())).m_43908_();
                           if (items.length != 0) {
                              ItemStack item = items[0];
                              if (content.chance == ChanceLogic.getMaxChancedValue()
                                 && !item.m_41619_()
                                 && !item.m_41782_()
                                 && (cal || !isExcludeItems(Registries.getItemId(item), inputItem))) {
                                 builder.outputItems(item);
                              }
                           }
                        }
                     );
                  }

                  if (fluidList != null) {
                     fluidList.forEach(content -> {
                        FluidIngredient fluid = (FluidIngredient)FluidRecipeCapability.CAP.of(content.getContent());
                        if (content.chance == ChanceLogic.getMaxChancedValue() && !fluid.isEmpty()) {
                           builder.outputFluids(new FluidIngredient[]{fluid});
                        }
                     });
                  }
               }

               builder.save(p);
            }
         }
      }
   }
}
