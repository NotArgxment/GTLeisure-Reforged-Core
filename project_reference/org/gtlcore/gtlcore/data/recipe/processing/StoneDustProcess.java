package org.gtlcore.gtlcore.data.recipe.processing;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;

public class StoneDustProcess {
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("dirty_hexafluorosilicic_acid_output", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Stone, 24)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(6000L))
         .outputFluids(GTLMaterials.DirtyHexafluorosilicicAcid.getFluid(3000L))
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("stone_dust_residue_output", new Object[0])
         .inputFluids(GTLMaterials.DirtyHexafluorosilicicAcid.getFluid(3000L))
         .outputFluids(GTLMaterials.DiluteHexafluorosilicicAcid.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTLMaterials.StoneDustResidue, 12)
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("dilute_hexafluorosilicic_acid", new Object[0])
         .inputFluids(GTLMaterials.DiluteHexafluorosilicicAcid.getFluid(3000L))
         .outputFluids(GTMaterials.Water.getFluid(2000L))
         .outputFluids(GTLMaterials.FluorosilicicAcid.getFluid(1000L))
         .duration(160)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("ammonium_fluoride_output", new Object[0])
         .inputFluids(GTMaterials.Water.getFluid(2000L))
         .inputFluids(GTMaterials.Ammonia.getFluid(6000L))
         .inputFluids(GTLMaterials.FluorosilicicAcid.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 3)
         .outputFluids(GTLMaterials.AmmoniumFluoride.getFluid(6000L))
         .duration(320)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("ammonium_bifluoride_output", new Object[0])
         .inputFluids(GTLMaterials.AmmoniumFluoride.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTLMaterials.AmmoniumBifluoride, 8)
         .outputFluids(GTMaterials.Ammonia.getFluid(1000L))
         .duration(340)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("ammonium_bifluoride_soluition_output", new Object[0])
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTLMaterials.AmmoniumBifluoride, 8)
         .outputFluids(GTLMaterials.AmmoniumBifluorideSolution.getFluid(2000L))
         .duration(140)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("ammonium_bifluoride_solution", new Object[0])
         .inputFluids(GTLMaterials.AmmoniumBifluorideSolution.getFluid(2000L))
         .outputFluids(GTMaterials.Ammonia.getFluid(1000L))
         .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(2000L))
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .duration(260)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("uncommon_residues_output", new Object[0])
         .inputFluids(GTLMaterials.SodiumHydroxideSolution.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTLMaterials.StoneDustResidue, 24)
         .outputItems(TagPrefix.dust, GTLMaterials.UncommonResidues, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Magnetite, 1)
         .outputFluids(GTLMaterials.SodiumHydroxideSolution.getFluid(925L))
         .outputFluids(GTLMaterials.RedMud.getFluid(75L))
         .duration(40)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("sodium_hydroxide_solution_output", new Object[0])
         .circuitMeta(2)
         .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .outputFluids(GTLMaterials.SodiumHydroxideSolution.getFluid(1000L))
         .duration(60)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("partially_oxidized_residues_output", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.UncommonResidues, 1)
         .inputFluids(GTLMaterials.DioxygenDifluoride.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.PartiallyOxidizedResidues, 1)
         .duration(80)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("neutralised_red_mud_output", new Object[0])
         .inputFluids(GTLMaterials.RedMud.getFluid(1000L))
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000L))
         .outputFluids(GTLMaterials.NeutralisedRedMud.getFluid(2000L))
         .duration(100)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("red_slurry_output", new Object[0])
         .inputFluids(GTLMaterials.NeutralisedRedMud.getFluid(2000L))
         .outputFluids(GTLMaterials.RedSlurry.getFluid(1000L))
         .outputFluids(GTLMaterials.FerricReeChloride.getFluid(1000L))
         .outputFluids(GTMaterials.Hydrogen.getFluid(4000L))
         .duration(100)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("titanyl_sulfate_output", new Object[0])
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(2000L))
         .inputFluids(GTLMaterials.RedSlurry.getFluid(2000L))
         .outputFluids(GTLMaterials.TitanylSulfate.getFluid(2000L))
         .outputFluids(GTMaterials.Water.getFluid(2000L))
         .duration(160)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("titanyl_sulfate", new Object[0])
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(4000L))
         .inputFluids(GTLMaterials.TitanylSulfate.getFluid(1000L))
         .outputFluids(GTMaterials.TitaniumTetrachloride.getFluid(1000L))
         .outputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .duration(160)
         .EUt(960L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("ferric_ree_chloride", new Object[0])
         .inputFluids(GTLMaterials.FerricReeChloride.getFluid(2000L))
         .outputFluids(GTLMaterials.RareEarthChlorides.getFluid(1000L))
         .outputFluids(GTMaterials.Iron3Chloride.getFluid(1000L))
         .outputFluids(GTMaterials.Water.getFluid(3000L))
         .duration(320)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("dioxygen_difluoride_output", new Object[0])
         .notConsumable(GTLItems.MICROFOCUS_X_RAY_TUBE)
         .inputFluids(GTMaterials.Oxygen.getFluid(2000L))
         .inputFluids(GTMaterials.Fluorine.getFluid(2000L))
         .outputFluids(GTLMaterials.DioxygenDifluoride.getFluid(1000L))
         .duration(80)
         .EUt(200L)
         .save(provider);
      GTLRecipeTypes.DEHYDRATOR_RECIPES
         .recipeBuilder("oxidized_residues_output", new Object[0])
         .inputFluids(GTLMaterials.OxidizedResidualSolution.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTLMaterials.OxidizedResidues, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.HeavyOxidizedResidues, 1)
         .duration(80)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("metallic_residues_output", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.OxidizedResidues, 10)
         .inputFluids(GTMaterials.Hydrogen.getFluid(60000L))
         .outputItems(TagPrefix.dust, GTLMaterials.MetallicResidues, 1)
         .outputFluids(GTLMaterials.DiluteHydrofluoricAcid.getFluid(40000L))
         .duration(1600)
         .EUt(GTValues.VA[4])
         .blastFurnaceTemp(3500)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("heavy_metallic_residues_output", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeavyOxidizedResidues, 10)
         .inputFluids(GTMaterials.Hydrogen.getFluid(60000L))
         .outputItems(TagPrefix.dust, GTLMaterials.HeavyMetallicResidues, 1)
         .outputFluids(GTLMaterials.DiluteHydrofluoricAcid.getFluid(40000L))
         .duration(1600)
         .EUt(GTValues.VA[4])
         .blastFurnaceTemp(3500)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("ultraacidic_residue_solution_output", new Object[0])
         .inputFluids(GTLMaterials.HeliumIIIHydride.getFluid(1000L))
         .inputItems(TagPrefix.dust, GTLMaterials.CleanInertResidues, 1)
         .outputFluids(GTLMaterials.UltraacidicResidueSolution.getFluid(1000L))
         .duration(160)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("helium_iii_hydride_output", new Object[0])
         .inputFluids(GTLMaterials.TritiumHydride.getFluid(10000L))
         .outputFluids(GTLMaterials.HeliumIIIHydride.getFluid(100L))
         .outputFluids(GTLMaterials.TritiumHydride.getFluid(9900L))
         .duration(800)
         .EUt(200L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("tritium_hydride_output", new Object[0])
         .inputFluids(GTMaterials.Hydrogen.getFluid(1000L))
         .inputFluids(GTMaterials.Tritium.getFluid(1000L))
         .outputFluids(GTLMaterials.TritiumHydride.getFluid(1000L))
         .duration(160)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("xenic_acid_output", new Object[0])
         .inputFluids(GTMaterials.Xenon.getFluid(1000L))
         .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 4000L))
         .inputFluids(GTLMaterials.UltraacidicResidueSolution.getFluid(2000L))
         .outputFluids(GTLMaterials.XenicAcid.getFluid(1000L))
         .outputFluids(GTLMaterials.DustyLiquidHeliumIII.getFluid(2000L))
         .duration(120)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("ozone_output_1", new Object[0])
         .inputFluids(GTLMaterials.XenicAcid.getFluid(2000L))
         .outputFluids(GTMaterials.Xenon.getFluid(2000L))
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .outputFluids(GTLMaterials.Ozone.getFluid(1000L))
         .outputFluids(GTLMaterials.HydrogenPeroxide.getFluid(1000L))
         .outputFluids(GTMaterials.Oxygen.getFluid(2000L))
         .duration(120)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("hydrofluoric_acid_output", new Object[0])
         .inputFluids(GTLMaterials.DiluteHydrofluoricAcid.getFluid(2000L))
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(1000L))
         .duration(80)
         .EUt(200L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("metallic_residues_input", new Object[0])
         .notConsumable(GTLItems.SEPARATION_ELECTROMAGNET)
         .inputItems(TagPrefix.dust, GTLMaterials.MetallicResidues, 10)
         .outputItems(TagPrefix.dust, GTLMaterials.DiamagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.ParamagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.FerromagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.UncommonResidues, 1)
         .duration(80)
         .EUt(GTValues.VA[5])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("heavy_metallic_residues_input", new Object[0])
         .notConsumable(GTLItems.SEPARATION_ELECTROMAGNET)
         .inputItems(TagPrefix.dust, GTLMaterials.HeavyMetallicResidues, 10)
         .outputItems(TagPrefix.dust, GTLMaterials.HeavyDiamagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.HeavyParamagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.HeavyFerromagneticResidues, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.ExoticHeavyResidues, 1)
         .duration(80)
         .EUt(GTValues.VA[5])
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("diamagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.DiamagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Calcium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Zinc, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Copper, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Gallium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Beryllium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Tin, 1, 2500, 0)
         .duration(100)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("paramagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ParamagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Sodium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Potassium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Magnesium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Titanium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Vanadium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Manganese, 1, 2500, 0)
         .duration(100)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("ferromagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.FerromagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Iron, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Nickel, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Cobalt, 1, 2500, 0)
         .duration(100)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("heavy_diamagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeavyDiamagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Lead, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Cadmium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Indium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Gold, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Bismuth, 1, 2500, 0)
         .outputFluids(GTMaterials.Mercury.getFluid(36L))
         .duration(120)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("heavy_paramagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeavyParamagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Thorium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Tungsten, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Hafnium, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Tantalum, 1, 2500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Thallium, 1, 2500, 0)
         .duration(120)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("heavy_ferromagnetic_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeavyFerromagneticResidues, 6)
         .chancedOutput(TagPrefix.dust, GTMaterials.Dysprosium, 1, 2500, 0)
         .duration(120)
         .EUt(3000L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("exotic_heavy_residues_input", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ExoticHeavyResidues, 16)
         .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
         .inputItems(GTLItems.PROTONATED_FULLERENE_SIEVING_MATRIX)
         .inputFluids(GTMaterials.Water.getFluid(2000L))
         .outputItems(GTLItems.SATURATED_FULLERENE_SIEVING_MATRIX)
         .outputFluids(GTLMaterials.SodiumHydroxideSolution.getFluid(1000L))
         .duration(40)
         .EUt(GTValues.VA[9])
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("nitrogen_pentoxide", new Object[0])
         .inputFluids(GTMaterials.Oxygen.getFluid(6000L))
         .outputFluids(GTLMaterials.Ozone.getFluid(2000L))
         .duration(120)
         .EUt(GTValues.VA[3])
         .save(provider);
   }
}
