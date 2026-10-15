package org.gtlcore.gtlcore.data.recipe;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMaterials;

public class CircuitRecipes {
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.AUTOCLAVE_RECIPES
         .recipeBuilder("sterilized_petri_dish", new Object[0])
         .inputItems(GTItems.PETRI_DISH)
         .inputFluids(GTLMaterials.AbsoluteEthanol.getFluid(100L))
         .outputItems(GTLItems.STERILIZED_PETRI_DISH)
         .cleanroom(CleanroomType.STERILE_CLEANROOM)
         .duration(25)
         .EUt(7680L)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("electricaly_wired_petri_dish", new Object[0])
         .inputItems(GTLItems.STERILIZED_PETRI_DISH)
         .inputItems(TagPrefix.wireFine, GTMaterials.Titanium)
         .inputFluids(GTMaterials.Polyethylene.getFluid(1296L))
         .outputItems(GTLItems.ELECTRICALY_WIRED_PETRI_DISH)
         .cleanroom(CleanroomType.STERILE_CLEANROOM)
         .duration(100)
         .EUt(7680L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("petri_dish", new Object[0])
         .inputItems(GTLItems.CONTAMINATED_PETRI_DISH)
         .outputItems(GTItems.PETRI_DISH)
         .inputFluids(GTLMaterials.PiranhaSolution.getFluid(100L))
         .cleanroom(CleanroomType.CLEANROOM)
         .duration(25)
         .EUt(30L)
         .save(provider);
   }
}
