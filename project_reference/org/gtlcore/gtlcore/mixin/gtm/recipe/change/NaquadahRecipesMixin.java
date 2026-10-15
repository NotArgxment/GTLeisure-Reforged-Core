package org.gtlcore.gtlcore.mixin.gtm.recipe.change;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.serialized.chemistry.NaquadahRecipes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(NaquadahRecipes.class)
public abstract class NaquadahRecipesMixin {
   @Overwrite(remap = false)
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("antimony_trioxide", new Object[0])
         .EUt(GTValues.VA[0])
         .duration(60)
         .inputItems(TagPrefix.dust, GTMaterials.Antimony, 2)
         .inputFluids(GTMaterials.Oxygen.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrioxide, 5)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("antimony_trifluoride", new Object[0])
         .EUt(GTValues.VA[1])
         .duration(60)
         .inputItems(TagPrefix.dust, GTMaterials.AntimonyTrioxide, 5)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(6000L))
         .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 8)
         .outputFluids(GTMaterials.Water.getFluid(3000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("fluoroantimonic_acid", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(300)
         .inputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride, 4)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(4000L))
         .outputFluids(GTMaterials.FluoroantimonicAcid.getFluid(1000L))
         .outputFluids(GTMaterials.Hydrogen.getFluid(2000L))
         .save(provider);
      GTLRecipeTypes.NEUTRON_ACTIVATOR_RECIPES
         .recipeBuilder("naquadah_separation", new Object[0])
         .inputFluids(GTMaterials.FluoroantimonicAcid.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 6)
         .outputFluids(GTMaterials.ImpureEnrichedNaquadahSolution.getFluid(4000L))
         .outputItems(TagPrefix.dust, GTMaterials.TitaniumTrifluoride, 4)
         .duration(200)
         .addData("ev_min", 220)
         .addData("ev_max", 230)
         .addData("evt", 56)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("hexafluoride_enriched_naquadah_solution", new Object[0])
         .inputFluids(GTMaterials.ImpureEnrichedNaquadahSolution.getFluid(1000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(6000L))
         .outputFluids(GTLMaterials.HexafluorideEnrichedNaquadahSolution.getFluid(1000L))
         .EUt(GTValues.VA[5])
         .duration(150)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("xenon_hexafluoro_enriched_naquadate", new Object[0])
         .inputFluids(GTLMaterials.HexafluorideEnrichedNaquadahSolution.getFluid(1000L))
         .inputFluids(GTMaterials.Xenon.getFluid(1000L))
         .outputFluids(GTLMaterials.XenonHexafluoroEnrichedNaquadate.getFluid(1000L))
         .EUt(GTValues.VA[3])
         .duration(200)
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("xenoauric_fluoroantimonic_acid", new Object[0])
         .inputFluids(GTLMaterials.XenoauricFluoroantimonicAcid.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.Gold)
         .outputItems(TagPrefix.dust, GTMaterials.AntimonyTrifluoride)
         .outputFluids(GTMaterials.Xenon.getFluid(1000L))
         .outputFluids(GTMaterials.Fluorine.getFluid(3000L))
         .EUt(GTValues.VA[2])
         .duration(800)
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("gold_chloride", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Gold, 2)
         .inputFluids(GTMaterials.Chlorine.getFluid(6000L))
         .outputFluids(GTLMaterials.GoldChloride.getFluid(1000L))
         .EUt(GTValues.VA[2])
         .duration(180)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("bromine_trifluoride", new Object[0])
         .inputFluids(GTMaterials.Bromine.getFluid(1000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(3000L))
         .outputFluids(GTLMaterials.BromineTrifluoride.getFluid(1000L))
         .EUt(GTValues.VA[1])
         .duration(120)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("gold_chloride", new Object[0])
         .inputFluids(GTLMaterials.GoldChloride.getFluid(1000L))
         .inputFluids(GTLMaterials.BromineTrifluoride.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTLMaterials.GoldTrifluoride, 8)
         .outputFluids(GTMaterials.Bromine.getFluid(2000L))
         .outputFluids(GTMaterials.Chlorine.getFluid(6000L))
         .EUt(GTValues.VA[2])
         .duration(200)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("enriched_naquadah_residue_solution", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.GoldTrifluoride, 4)
         .inputFluids(GTLMaterials.XenonHexafluoroEnrichedNaquadate.getFluid(1000L))
         .inputFluids(GTMaterials.FluoroantimonicAcid.getFluid(1000L))
         .inputFluids(GTMaterials.Hydrogen.getFluid(9000L))
         .outputFluids(GTMaterials.EnrichedNaquadahSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.XenoauricFluoroantimonicAcid.getFluid(1000L))
         .EUt(GTValues.VA[6])
         .duration(160)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("enriched_naquadah_solution_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(100)
         .inputFluids(GTMaterials.EnrichedNaquadahSolution.getFluid(1000L))
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(2000L))
         .outputFluids(GTMaterials.AcidicEnrichedNaquadahSolution.getFluid(3000L))
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("acidic_enriched_naquadah_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(100)
         .inputFluids(GTMaterials.AcidicEnrichedNaquadahSolution.getFluid(3000L))
         .outputFluids(GTMaterials.EnrichedNaquadahWaste.getFluid(2000L))
         .outputFluids(GTMaterials.Fluorine.getFluid(500L))
         .outputItems(TagPrefix.dust, GTMaterials.EnrichedNaquadahSulfate, 6)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("enriched_naquadah_sulfate_separation", new Object[0])
         .EUt(GTValues.VA[5])
         .duration(500)
         .blastFurnaceTemp(7000)
         .inputItems(TagPrefix.dust, GTMaterials.EnrichedNaquadahSulfate, 6)
         .inputFluids(GTMaterials.Hydrogen.getFluid(2000L))
         .outputItems(TagPrefix.ingotHot, GTMaterials.NaquadahEnriched)
         .outputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("enriched_naquadah_waste_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(300)
         .inputFluids(GTMaterials.EnrichedNaquadahWaste.getFluid(2000L))
         .chancedOutput(TagPrefix.dust, GTMaterials.BariumSulfide, 5000, 0)
         .outputFluids(GTMaterials.SulfuricAcid.getFluid(500L))
         .outputFluids(GTMaterials.EnrichedNaquadahSolution.getFluid(250L))
         .outputFluids(GTMaterials.NaquadriaSolution.getFluid(100L))
         .save(provider);
      GTLRecipeTypes.NEUTRON_ACTIVATOR_RECIPES
         .recipeBuilder("enriched_naquadah_separation", new Object[0])
         .inputFluids(GTMaterials.FluoroantimonicAcid.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 6)
         .outputFluids(GTMaterials.ImpureNaquadriaSolution.getFluid(4000L))
         .outputItems(TagPrefix.dust, GTMaterials.IndiumPhosphide, 1)
         .addData("ev_min", 460)
         .addData("ev_max", 480)
         .addData("evt", 124)
         .duration(200)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("hexafluoride_naquadria_solution", new Object[0])
         .inputFluids(GTMaterials.ImpureNaquadriaSolution.getFluid(1000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(6000L))
         .outputFluids(GTLMaterials.HexafluorideNaquadriaSolution.getFluid(1000L))
         .EUt(GTValues.VA[5])
         .duration(400)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("radon_naquadria", new Object[0])
         .inputFluids(GTMaterials.Radon.getFluid(1000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(2000L))
         .outputFluids(GTLMaterials.RadonDifluoride.getFluid(1000L))
         .EUt(GTValues.VA[2])
         .duration(120)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("radon_naquadria_octafluoride", new Object[0])
         .inputFluids(GTLMaterials.HexafluorideNaquadriaSolution.getFluid(1000L))
         .inputFluids(GTLMaterials.RadonDifluoride.getFluid(1000L))
         .outputFluids(GTLMaterials.RadonNaquadriaOctafluoride.getFluid(1000L))
         .outputFluids(GTMaterials.NaquadriaSolution.getFluid(1000L))
         .EUt(GTValues.VA[3])
         .duration(600)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("naquadria_solution_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(100)
         .inputFluids(GTMaterials.NaquadriaSolution.getFluid(1000L))
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(2000L))
         .outputFluids(GTMaterials.AcidicNaquadriaSolution.getFluid(3000L))
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("acidic_naquadria_solution_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(100)
         .inputFluids(GTMaterials.AcidicNaquadriaSolution.getFluid(3000L))
         .outputFluids(GTMaterials.NaquadriaWaste.getFluid(2000L))
         .outputFluids(GTMaterials.Fluorine.getFluid(500L))
         .outputItems(TagPrefix.dustTiny, GTMaterials.NaquadriaSulfate, 2)
         .chancedOutput(TagPrefix.dustTiny, GTMaterials.NaquadriaSulfate, 1, 6000, 100)
         .chancedOutput(TagPrefix.dustTiny, GTMaterials.NaquadriaSulfate, 1, 4000, 100)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("xenon_trioxide", new Object[0])
         .inputFluids(GTMaterials.Xenon.getFluid(1000L))
         .inputFluids(GTMaterials.Oxygen.getFluid(3000L))
         .outputFluids(GTLMaterials.XenonTrioxide.getFluid(1000L))
         .EUt(GTValues.VA[3])
         .duration(200)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("caesium_fluoride", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Caesium)
         .inputFluids(GTMaterials.Fluorine.getFluid(1000L))
         .outputFluids(GTLMaterials.CaesiumFluoride.getFluid(1000L))
         .EUt(7L)
         .duration(60)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("caesium_xenontrioxide_fluoride", new Object[0])
         .inputFluids(GTLMaterials.CaesiumFluoride.getFluid(1000L))
         .inputFluids(GTLMaterials.XenonTrioxide.getFluid(1000L))
         .outputFluids(GTLMaterials.CaesiumXenontrioxideFluoride.getFluid(1000L))
         .EUt(GTValues.VA[2])
         .duration(400)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("naquadria_caesium_xenonnonfluoride", new Object[0])
         .inputFluids(GTLMaterials.RadonNaquadriaOctafluoride.getFluid(1000L))
         .inputFluids(GTLMaterials.CaesiumXenontrioxideFluoride.getFluid(1000L))
         .outputFluids(GTLMaterials.NaquadriaCaesiumXenonnonfluoride.getFluid(1000L))
         .outputFluids(GTLMaterials.RadonTrioxide.getFluid(1000L))
         .EUt(GTValues.VA[5])
         .duration(400)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("nitryl_fluoride", new Object[0])
         .inputFluids(GTMaterials.NitrogenDioxide.getFluid(1000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(1000L))
         .outputFluids(GTLMaterials.NitrylFluoride.getFluid(1000L))
         .EUt(GTValues.VA[1])
         .duration(160)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("nitrosonium_octafluoroxenate", new Object[0])
         .inputFluids(GTLMaterials.NaquadriaCaesiumXenonnonfluoride.getFluid(1000L))
         .inputFluids(GTLMaterials.NitrylFluoride.getFluid(2000L))
         .outputFluids(GTLMaterials.NaquadriaCaesiumfluoride.getFluid(1000L))
         .outputFluids(GTLMaterials.NitrosoniumOctafluoroxenate.getFluid(1000L))
         .EUt(GTValues.VA[4])
         .duration(400)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("acidic_naquadria_caesiumfluoride", new Object[0])
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(2000L))
         .inputFluids(GTLMaterials.NaquadriaCaesiumfluoride.getFluid(1000L))
         .outputFluids(GTLMaterials.AcidicNaquadriaCaesiumfluoride.getFluid(3000L))
         .EUt(GTValues.VA[5])
         .duration(200)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("naquadria_sulfate_separation", new Object[0])
         .EUt(GTValues.VA[7])
         .duration(600)
         .blastFurnaceTemp(9000)
         .inputItems(TagPrefix.dust, GTMaterials.NaquadriaSulfate, 6)
         .inputFluids(GTMaterials.Hydrogen.getFluid(2000L))
         .outputItems(TagPrefix.ingotHot, GTMaterials.Naquadria)
         .outputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("naquadria_waste_separation", new Object[0])
         .EUt(GTValues.VA[4])
         .duration(400)
         .inputFluids(GTMaterials.NaquadriaWaste.getFluid(2000L))
         .chancedOutput(TagPrefix.dust, GTMaterials.TriniumSulfide, 8000, 500)
         .outputFluids(GTMaterials.NaquadriaSolution.getFluid(250L))
         .outputFluids(GTMaterials.EnrichedNaquadahSolution.getFluid(100L))
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("trinium_sulfide_separation", new Object[0])
         .duration(750)
         .EUt(GTValues.VA[6])
         .blastFurnaceTemp(GTMaterials.Trinium.getBlastTemperature())
         .inputItems(TagPrefix.dust, GTMaterials.TriniumSulfide, 2)
         .inputItems(TagPrefix.dust, GTMaterials.Zinc)
         .outputItems(TagPrefix.ingotHot, GTMaterials.Trinium)
         .outputItems(TagPrefix.dust, GTMaterials.ZincSulfide, 2)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("titanium_trifluoride_separation", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(900)
         .blastFurnaceTemp(1941)
         .inputItems(TagPrefix.dust, GTMaterials.TitaniumTrifluoride, 4)
         .inputFluids(GTMaterials.Hydrogen.getFluid(3000L))
         .outputItems(TagPrefix.ingotHot, GTMaterials.Titanium)
         .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(3000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("indium_phosphide_separation", new Object[0])
         .duration(30)
         .EUt(GTValues.VA[0])
         .inputItems(TagPrefix.dust, GTMaterials.IndiumPhosphide, 2)
         .inputItems(TagPrefix.dust, GTMaterials.Calcium)
         .outputItems(TagPrefix.dust, GTMaterials.Indium)
         .outputItems(TagPrefix.dust, GTMaterials.CalciumPhosphide, 2)
         .save(provider);
   }
}
