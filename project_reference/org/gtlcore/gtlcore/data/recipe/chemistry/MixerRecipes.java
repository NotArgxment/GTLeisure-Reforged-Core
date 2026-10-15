package org.gtlcore.gtlcore.data.recipe.chemistry;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLMaterials;

public class MixerRecipes {
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("absolute_ethanol", new Object[0])
         .inputFluids(GTMaterials.Ethanol.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTLMaterials.ZeoliteSievingPellets)
         .outputFluids(GTLMaterials.AbsoluteEthanol.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.WetZeoliteSievingPellets)
         .cleanroom(CleanroomType.CLEANROOM)
         .duration(100)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("piranha_solution", new Object[0])
         .inputFluids(GTLMaterials.HydrogenPeroxide.getFluid(1000L))
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .outputFluids(GTLMaterials.PiranhaSolution.getFluid(2000L))
         .cleanroom(CleanroomType.CLEANROOM)
         .duration(50)
         .EUt(30L)
         .save(provider);
   }
}
