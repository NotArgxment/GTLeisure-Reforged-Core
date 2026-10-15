package org.gtlcore.gtlcore.data.recipe.processing;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;

public class Lanthanidetreatment {
   public static void init(Consumer<FinishedRecipe> provider) {
      GTLRecipeTypes.DIGESTION_TREATMENT
         .recipeBuilder("monazite_rare_earth_turbid_liquid1", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Monazite, 2)
         .inputFluids(GTMaterials.NitricAcid.getFluid(700L))
         .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 1)
         .outputFluids(GTLMaterials.MonaziteRareEarthTurbidLiquid.getFluid(400L))
         .duration(200)
         .EUt(1920L)
         .blastFurnaceTemp(1440)
         .save(provider);
      GTLRecipeTypes.DISSOLUTION_TREATMENT
         .recipeBuilder("diluted_monazite_slurry2", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Saltpeter, 9)
         .inputFluids(GTLMaterials.MonaziteRareEarthTurbidLiquid.getFluid(9000L))
         .inputFluids(GTMaterials.Water.getFluid(90000L))
         .outputItems(TagPrefix.dust, GTLMaterials.ThoritePowder, 9)
         .outputItems(TagPrefix.dust, GTMaterials.Monazite, 2)
         .outputFluids(GTLMaterials.DilutedMonaziteSlurry.getFluid(100000L))
         .duration(200)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("diluted_monazite_slurry3", new Object[0])
         .inputFluids(GTLMaterials.DilutedMonaziteSlurry.getFluid(1000L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.MonaziteSulfatePowder, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.SiliconDioxide, 1, 7500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Rutile, 1, 2000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RedZirconPowder, 1, 500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Ilmenite, 1, 2000, 0)
         .duration(400)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("red_zircon_powder4", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.RedZirconPowder, 4)
         .outputItems(TagPrefix.dust, GTLMaterials.Zircon, 3)
         .outputItems(TagPrefix.dust, GTMaterials.GraniteRed, 2)
         .duration(320)
         .EUt(90L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("monazite_sulfate_powder5", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.MonaziteSulfatePowder, 1)
         .inputFluids(GTMaterials.Water.getFluid(6000L))
         .outputFluids(GTLMaterials.DilutedMonaziteSulfateSolution.getFluid(7000L))
         .duration(240)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("diluted_monazite_sulfate_solution6", new Object[0])
         .inputFluids(GTLMaterials.DilutedMonaziteSulfateSolution.getFluid(9000L))
         .inputFluids(GTLMaterials.AmmoniumNitrateSolution.getFluid(1800L))
         .outputItems(TagPrefix.dust, GTLMaterials.AcidicMonazitePowder, 3)
         .duration(160)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("acidic_monazite_powder7", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.AcidicMonazitePowder, 1)
         .chancedOutput(TagPrefix.dust, GTLMaterials.MonaziteRareEarthFilterResiduePowder, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.ThoriumPhosphateFilterCakePowder, 1, 7000, 0)
         .duration(300)
         .EUt(256L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("thorium_phosphate_filter_cake_powder8", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ThoriumPhosphateFilterCakePowder, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.ThoriumPhosphateRefinedPowder, 1)
         .duration(100)
         .EUt(120L)
         .blastFurnaceTemp(1200)
         .save(provider);
      GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES
         .recipeBuilder("thorium_phosphate_refined_powder9", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ThoriumPhosphateRefinedPowder, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Monazite, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Phosphate, 1)
         .duration(200)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("monazite_rare_earth_filter_residue_powder10", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.MonaziteRareEarthFilterResiduePowder, 1)
         .inputFluids(GTLMaterials.AmmoniumNitrateSolution.getFluid(320L))
         .outputItems(TagPrefix.dust, GTLMaterials.NeutralizedMonaziteRareEarthFilterResiduePowder, 1)
         .duration(120)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("neutralized_monazite_rare_earth_filter_residue_powder11", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.NeutralizedMonaziteRareEarthFilterResiduePowder, 1)
         .chancedOutput(TagPrefix.dust, GTLMaterials.ConcentratedMonaziteRareEarthHydroxidePowder, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.UraniumFilterResiduePowder, 1, 5000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.UraniumFilterResiduePowder, 1, 4000, 0)
         .duration(160)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("uranium_filter_residue_powder12", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.UraniumFilterResiduePowder, 1)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(100L))
         .outputItems(TagPrefix.dust, GTLMaterials.NeutralizedUraniumFilterResiduePowder, 1)
         .duration(180)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("neutralized_uranium_filter_residue_powder13", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.NeutralizedUraniumFilterResiduePowder, 1)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 4500, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 4000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 3000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 3000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Uranium235, 1, 2000, 0)
         .duration(240)
         .EUt(30L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("concentrated_monazite_rare_earth_hydroxide_powder14", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ConcentratedMonaziteRareEarthHydroxidePowder, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.DriedConcentratedNitricMonaziteRareEarthPowder, 1)
         .duration(300)
         .EUt(120L)
         .blastFurnaceTemp(1200)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("dried_concentrated_nitric_monazite_rare_earth_powder15", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.DriedConcentratedNitricMonaziteRareEarthPowder, 1)
         .inputFluids(GTMaterials.NitricAcid.getFluid(500L))
         .outputFluids(GTLMaterials.ConcentratedNitrideMonaziteRareEarthSolution.getFluid(500L))
         .duration(240)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("concentrated_nitride_monazite_rare_earth_solution16", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumRichMixturePowder, 3)
         .inputFluids(GTLMaterials.ConcentratedNitrideMonaziteRareEarthSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.NitricLeachateFromMonazite.getFluid(2000L))
         .duration(240)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("concentrated_nitride_monazite_rare_earth_solution17", new Object[0])
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .inputFluids(GTLMaterials.ConcentratedNitrideMonaziteRareEarthSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.NitricLeachateFromMonazite.getFluid(1000L))
         .duration(200)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("cerium_rich_mixture_powder18", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Bastnasite, 6)
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumRichMixturePowder, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Carbon, 1)
         .outputFluids(GTMaterials.Oxygen.getFluid(3000L))
         .outputFluids(GTMaterials.Fluorine.getFluid(1000L))
         .duration(120)
         .EUt(90L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("cerium_chloride_powder19", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumRichMixturePowder, 15)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(750L))
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumChloridePowder, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Monazite, 1)
         .outputFluids(GTMaterials.Water.getFluid(750L))
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("cerium_oxalate_powder20", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumChloridePowder, 15)
         .inputFluids(GTLMaterials.OxalicAcid.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumOxalatePowder, 1)
         .outputFluids(GTMaterials.HydrochloricAcid.getFluid(6000L))
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("oxalic_acid21", new Object[0])
         .notConsumable(TagPrefix.dust, GTLMaterials.VanadiumPentoxidePowder, 1)
         .inputFluids(GTMaterials.Oxygen.getFluid(40000L))
         .inputFluids(GTMaterials.Ethanol.getFluid(9000L))
         .outputFluids(GTLMaterials.OxalicAcid.getFluid(9000L))
         .outputFluids(GTMaterials.Water.getFluid(20000L))
         .duration(400)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("oxalic_acid22", new Object[0])
         .notConsumable(TagPrefix.dust, GTLMaterials.VanadiumPentoxidePowder, 1)
         .inputFluids(GTMaterials.Oxygen.getFluid(27000L))
         .inputFluids(GTMaterials.Methanol.getFluid(9000L))
         .inputFluids(GTMaterials.CarbonMonoxide.getFluid(9000L))
         .outputFluids(GTLMaterials.OxalicAcid.getFluid(9000L))
         .outputFluids(GTMaterials.Water.getFluid(20000L))
         .duration(400)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("oxalic_acid23", new Object[0])
         .notConsumable(TagPrefix.dust, GTLMaterials.VanadiumPentoxidePowder, 1)
         .inputItems(TagPrefix.dust, GTMaterials.Sugar, 24)
         .inputFluids(GTMaterials.NitricAcid.getFluid(6000L))
         .outputFluids(GTLMaterials.OxalicAcid.getFluid(3000L))
         .outputFluids(GTMaterials.NitricOxide.getFluid(6000L))
         .duration(300)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("vanadium_pentoxide_powder24", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Vanadium, 2)
         .inputFluids(GTMaterials.Oxygen.getFluid(5000L))
         .outputItems(TagPrefix.dust, GTLMaterials.VanadiumPentoxidePowder, 7)
         .duration(200)
         .EUt(120L)
         .blastFurnaceTemp(2500)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("cerium_oxalate_powder25", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumOxalatePowder, 5)
         .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumOxide, 5)
         .outputFluids(GTMaterials.CarbonMonoxide.getFluid(9000L))
         .duration(200)
         .EUt(480L)
         .blastFurnaceTemp(800)
         .save(provider);
      GTLRecipeTypes.DIGESTION_TREATMENT
         .recipeBuilder("concentrated_cerium_chloride_solution26", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumRichMixturePowder, 1)
         .inputFluids(GTMaterials.Chlorine.getFluid(10000L))
         .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 1)
         .outputFluids(GTLMaterials.ConcentratedCeriumChlorideSolution.getFluid(1000L))
         .duration(40)
         .EUt(122880L)
         .blastFurnaceTemp(3820)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("concentrated_nitric_leachate_from_monazite27", new Object[0])
         .inputFluids(GTLMaterials.NitricLeachateFromMonazite.getFluid(1000L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.CeriumOxide, 1, 1000, 100)
         .outputFluids(GTLMaterials.ConcentratedNitricLeachateFromMonazite.getFluid(1000L))
         .duration(200)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.VACUUM_RECIPES
         .recipeBuilder("cooling_concentrated_nitric_monazite_rare_earth_powder28", new Object[0])
         .inputFluids(GTLMaterials.ConcentratedNitricLeachateFromMonazite.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.CoolingConcentratedNitricMonaziteRareEarthPowder, 1)
         .duration(100)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES
         .recipeBuilder("monazite_rare_earth_precipitate_powder29", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CoolingConcentratedNitricMonaziteRareEarthPowder, 1)
         .chancedOutput(TagPrefix.dust, GTLMaterials.MonaziteRareEarthPrecipitatePowder, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.EuropiumOxide, 1, 500, 0)
         .duration(100)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("heterogeneous_halide_monazite_rare_earth_mixture_powder30", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.MonaziteRareEarthPrecipitatePowder, 1)
         .inputFluids(GTMaterials.Chlorine.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
         .duration(250)
         .EUt(480L)
         .blastFurnaceTemp(1200)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("saturated_monazite_rare_earth_powder30", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumRefinedPowder, 2)
         .inputFluids(GTMaterials.Acetone.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SaturatedMonaziteRareEarthPowder, 3)
         .duration(200)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("saturated_monazite_rare_earth_powder32", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.HeterogeneousHalideMonaziteRareEarthMixturePowder, 1)
         .inputItems(TagPrefix.dust, GTMaterials.Salt, 1)
         .inputFluids(GTMaterials.Acetone.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SaturatedMonaziteRareEarthPowder, 2)
         .duration(200)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("samarium_precipitate_powder33", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SaturatedMonaziteRareEarthPowder, 4)
         .outputItems(TagPrefix.dust, GTMaterials.RareEarth, 5)
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumPrecipitatePowder, 3)
         .outputFluids(GTMaterials.Chloromethane.getFluid(400L))
         .duration(160)
         .EUt(1920L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("samarium_precipitate_powder34", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumPrecipitatePowder, 3)
         .outputItems(TagPrefix.dust, GTMaterials.Samarium, 2)
         .chancedOutput(TagPrefix.dust, GTMaterials.Gadolinium, 1, 2000, 0)
         .duration(160)
         .EUt(1920L)
         .save(provider);
      GTLRecipeTypes.DIGESTION_TREATMENT
         .recipeBuilder("samarium_rrare_eearth_turbid_liquid35", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumRefinedPowder, 16)
         .inputFluids(GTMaterials.NitricAcid.getFluid(200L))
         .outputItems(TagPrefix.dust, GTLMaterials.ThoriumPhosphateRefinedPowder, 1)
         .outputFluids(GTLMaterials.SamariumRrareEearthTurbidLiquid.getFluid(800L))
         .duration(200)
         .EUt(1920L)
         .blastFurnaceTemp(2680)
         .save(provider);
      GTLRecipeTypes.DISSOLUTION_TREATMENT
         .recipeBuilder("samarium_rrare_eearth_turbid_liquid36", new Object[0])
         .inputFluids(GTLMaterials.SamariumRrareEearthTurbidLiquid.getFluid(1000L))
         .inputFluids(GTMaterials.NitricAcid.getFluid(1000L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.CeriumRichMixturePowder, 1, 6000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.CeriumOxide, 1, 8000, 0)
         .outputFluids(GTLMaterials.SamariumRareEarthSlurry.getFluid(2000L))
         .duration(80)
         .EUt(1920L)
         .save(provider);
      GTLRecipeTypes.DISSOLUTION_TREATMENT
         .recipeBuilder("samarium_rare_earth_slurry37", new Object[0])
         .inputFluids(GTLMaterials.SamariumRareEarthSlurry.getFluid(1000L))
         .inputFluids(GTMaterials.Water.getFluid(9000L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.NeodymiumRareEarthConcentratePowder, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.NeodymiumRareEarthConcentratePowder, 1, 6000, 0)
         .outputFluids(GTLMaterials.SamariumRareEarthDilutedSolution.getFluid(10000L))
         .duration(120)
         .EUt(1920L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("lanthanum_chloride38", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.NeodymiumRareEarthConcentratePowder, 1)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTLMaterials.LanthanumChloride, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.NeodymiumOxide, 1)
         .duration(450)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("samarium_oxalate_with_impurities39", new Object[0])
         .inputFluids(GTLMaterials.SamariumRareEarthDilutedSolution.getFluid(2000L))
         .inputFluids(GTLMaterials.OxalicAcid.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumOxalateWithImpurities, 5)
         .chancedOutput(TagPrefix.dust, GTLMaterials.PhosphorusFreeSamariumConcentratePowder, 3, 1000, 0)
         .outputFluids(GTLMaterials.SamariumRrareEearthTurbidLiquid.getFluid(50L))
         .duration(280)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("Samarium40", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.PhosphorusFreeSamariumConcentratePowder, 1)
         .chancedOutput(TagPrefix.dust, GTMaterials.Samarium, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.ThoritePowder, 2, 8000, 0)
         .duration(100)
         .EUt(1920L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("samarium_chloride_concentrate_solution41", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumOxalateWithImpurities, 5)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(6000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumChlorideWithImpurities, 8)
         .outputFluids(GTMaterials.CarbonMonoxide.getFluid(6000L))
         .duration(100)
         .EUt(960L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("samarium_chloride_sodium_chloride_mixture_powder42", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Salt, 1)
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumChlorideWithImpurities, 2)
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumChlorideSodiumChlorideMixturePowder, 3)
         .duration(80)
         .EUt(30L)
         .save(provider);
      GTRecipeTypes.ELECTROLYZER_RECIPES
         .recipeBuilder("Samarium43", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumChlorideSodiumChlorideMixturePowder, 6)
         .outputItems(TagPrefix.dust, GTMaterials.Samarium, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Sodium, 1)
         .outputFluids(GTMaterials.Chlorine.getFluid(4000L))
         .duration(20)
         .EUt(7680L)
         .save(provider);
      GTLRecipeTypes.DIGESTION_TREATMENT
         .recipeBuilder("monazite_rare_earth_turbid_liquid_output44", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumRefinedPowder, 1)
         .inputFluids(GTMaterials.Chlorine.getFluid(10000L))
         .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 1)
         .outputFluids(GTLMaterials.SamariumChlorideConcentrateSolution.getFluid(1000L))
         .duration(40)
         .EUt(122880L)
         .blastFurnaceTemp(3820)
         .save(provider);
      GTRecipeTypes.DISTILLERY_RECIPES
         .recipeBuilder("lanthanum_chloride_with_impurities45", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Lanthanum, 9)
         .inputFluids(GTLMaterials.SamariumChlorideWithImpurities.getFluid(5184L))
         .outputItems(TagPrefix.dust, GTLMaterials.LanthanumChlorideWithImpurities, 36)
         .outputFluids(GTMaterials.Samarium.getFluid(2304L))
         .duration(100)
         .EUt(122880L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("lanthanum_chloride_with_impurities46", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.LanthanumChlorideWithImpurities, 36)
         .outputItems(TagPrefix.dust, GTLMaterials.LanthanumChloride, 36)
         .duration(100)
         .EUt(1920L)
         .save(provider);
      GTLRecipeTypes.DIGESTION_TREATMENT
         .recipeBuilder("fluoro_carbon_lanthanide_cerium_solution47", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Bastnasite, 2)
         .inputFluids(GTMaterials.NitricAcid.getFluid(700L))
         .outputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 1)
         .outputFluids(GTLMaterials.FluoroCarbonLanthanideCeriumSolution.getFluid(400L))
         .duration(200)
         .EUt(1920L)
         .blastFurnaceTemp(1440)
         .save(provider);
      GTRecipeTypes.CRACKING_RECIPES
         .recipeBuilder("steam_cracked_fluoro_carbon_lanthanide_slurry48", new Object[0])
         .inputFluids(GTLMaterials.FluoroCarbonLanthanideCeriumSolution.getFluid(1000L))
         .inputFluids(GTMaterials.Steam.getFluid(1000L))
         .outputFluids(GTLMaterials.SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(2000L))
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("fluoro_carbon_lanthanide_cerium_solution49", new Object[0])
         .inputFluids(GTLMaterials.SteamCrackedFluoroCarbonLanthanideSlurry.getFluid(1000L))
         .inputFluids(GTMaterials.NitricAcid.getFluid(700L))
         .outputFluids(GTLMaterials.ModulatedFluoroCarbonLanthanideSlurry.getFluid(1320L))
         .duration(200)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("sodium_fluorosilicate50", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Salt, 4)
         .inputFluids(GTLMaterials.FluorosilicicAcid.getFluid(1000L))
         .outputFluids(GTMaterials.HydrochloricAcid.getFluid(2000L))
         .outputFluids(GTLMaterials.SodiumFluorosilicate.getFluid(1000L))
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTLRecipeTypes.DISSOLUTION_TREATMENT
         .recipeBuilder("diluted_fluoro_carbon_lanthanide_slurry51", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Saltpeter, 1)
         .inputFluids(GTLMaterials.ModulatedFluoroCarbonLanthanideSlurry.getFluid(1000L))
         .inputFluids(GTMaterials.Water.getFluid(10000L))
         .outputItems(TagPrefix.dust, GTMaterials.Stone, 1)
         .outputFluids(GTLMaterials.DilutedFluoroCarbonLanthanideSlurry.getFluid(10000L))
         .duration(160)
         .EUt(1920L)
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("filtered_fluoro_carbon_lanthanide_slurry52", new Object[0])
         .inputFluids(GTLMaterials.DilutedFluoroCarbonLanthanideSlurry.getFluid(1000L))
         .chancedOutput(TagPrefix.dust, GTMaterials.SiliconDioxide, 1, 9000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Rutile, 1, 7500, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RedZirconPowder, 1, 1000, 0)
         .chancedOutput(TagPrefix.dust, GTMaterials.Ilmenite, 1, 500, 0)
         .outputFluids(GTLMaterials.FilteredFluoroCarbonLanthanideSlurry.getFluid(400L))
         .duration(200)
         .EUt(240L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("fluoro_carbon_lanthanide_cerium_oxide_powder53", new Object[0])
         .inputFluids(GTLMaterials.FilteredFluoroCarbonLanthanideSlurry.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.FluoroCarbonLanthanideCeriumOxidePowder, 1)
         .duration(240)
         .EUt(480L)
         .blastFurnaceTemp(1400)
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("acid_leached_fluoro_carbon_lanthanide_cerium_oxide_powder54", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.FluoroCarbonLanthanideCeriumOxidePowder, 1)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(500L))
         .outputItems(TagPrefix.dust, GTLMaterials.AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder, 1)
         .duration(200)
         .EUt(30L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("calcined_rare_earth_oxide_powder55", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder, 1)
         .inputFluids(GTMaterials.Oxygen.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.CalcinedRareEarthOxidePowder, 1)
         .outputFluids(GTMaterials.Fluorine.getFluid(13L))
         .duration(240)
         .EUt(120L)
         .blastFurnaceTemp(1200)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("wet_rare_earth_oxide_powder56", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CalcinedRareEarthOxidePowder, 1)
         .inputFluids(GTMaterials.Water.getFluid(200L))
         .outputItems(TagPrefix.dust, GTLMaterials.WetRareEarthOxidePowder, 1)
         .duration(100)
         .EUt(30L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("cerium_oxide_rare_earth_oxide_powder57", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.WetRareEarthOxidePowder, 1)
         .inputFluids(GTMaterials.Fluorine.getFluid(4000L))
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumOxideRareEarthOxidePowder, 1)
         .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(4000L))
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("fluoro_carbon_lanthanide_cerium_rare_earth_oxide_powder58", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.CeriumOxideRareEarthOxidePowder, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthOxidePowder, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumOxide, 1)
         .duration(320)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("nitrided_fluoro_carbon_lanthanide_cerium_rare_earth_oxide_solution59", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthOxidePowder, 1)
         .inputFluids(GTMaterials.NitricAcid.getFluid(400L))
         .outputFluids(GTLMaterials.NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution.getFluid(1000L))
         .duration(150)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("fluoro_carbon_lanthanide_cerium_rare_earth_suspension60", new Object[0])
         .inputFluids(GTMaterials.Acetone.getFluid(1000L))
         .inputFluids(GTLMaterials.NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000L))
         .duration(350)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("samarium_rare_earth_concentrate_powder61", new Object[0])
         .inputFluids(GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthSuspension.getFluid(1000L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.NeodymiumRareEarthConcentratePowder, 1, 8000, 0)
         .chancedOutput(TagPrefix.dust, GTLMaterials.SamariumRareEarthConcentratePowder, 1, 5000, 0)
         .outputFluids(GTMaterials.Acetone.getFluid(450L))
         .duration(450)
         .EUt(120L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("fluorinated_samarium_concentrate_powder62", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumRareEarthConcentratePowder, 1)
         .inputFluids(GTMaterials.HydrofluoricAcid.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTMaterials.RareEarth, 4)
         .outputItems(TagPrefix.dust, GTLMaterials.FluorinatedSamariumConcentratePowder, 1)
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("samarium_terbium_mixture_powder63", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.FluorinatedSamariumConcentratePowder, 4)
         .inputItems(TagPrefix.dust, GTMaterials.Calcium, 4)
         .outputItems(TagPrefix.dust, GTMaterials.Holmium, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumTerbiumMixturePowder, 4)
         .outputItems(TagPrefix.dust, GTLMaterials.Fluorite, 12)
         .duration(200)
         .EUt(1920L)
         .blastFurnaceTemp(1200)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("nitrided_samarium_terbium_mixture_powder64", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SamariumTerbiumMixturePowder, 1)
         .inputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 9)
         .outputItems(TagPrefix.dust, GTLMaterials.NitridedSamariumTerbiumMixturePowder, 1)
         .duration(300)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("terbium_nitrate_powder65", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.NitridedSamariumTerbiumMixturePowder, 4)
         .inputItems(TagPrefix.dust, GTMaterials.Copper, 1)
         .outputItems(TagPrefix.dust, GTLMaterials.TerbiumNitratePowder, 2)
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumPrecipitatePowder, 2)
         .duration(320)
         .EUt(1920L)
         .save(provider);
      GTLRecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES
         .recipeBuilder("concentrated_cerium_chloride_solution66", new Object[0])
         .inputFluids(GTLMaterials.ConcentratedCeriumChlorideSolution.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.CeriumOxide, 4)
         .outputFluids(GTLMaterials.ConcentratedRareEarthChlorideSolution.getFluid(1000L))
         .duration(20)
         .EUt(491520L)
         .save(provider);
      GTLRecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES
         .recipeBuilder("samarium_chloride_concentrate_solution67", new Object[0])
         .inputFluids(GTLMaterials.SamariumChlorideConcentrateSolution.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SamariumOxide, 4)
         .outputFluids(GTLMaterials.ConcentratedRareEarthChlorideSolution.getFluid(1000L))
         .duration(20)
         .EUt(491520L)
         .save(provider);
      GTLRecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES
         .recipeBuilder("samarium_chloride_concentrate_solution69", new Object[0])
         .inputFluids(GTLMaterials.EnrichedRareEarthChlorideSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.DilutedRareEarthChlorideSolution.getFluid(1000L))
         .outputItems(TagPrefix.dustSmall, GTLMaterials.LanthanumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.PraseodymiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.NeodymiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.CeriumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.EuropiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.GadoliniumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.SamariumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.TerbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.DysprosiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.HolmiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ErbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ThuliumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.YtterbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.LutetiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ScandiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.YttriumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.PromethiumOxide, 1)
         .duration(20)
         .EUt(491520L)
         .save(provider);
      GTLRecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES
         .recipeBuilder("samarium_chloride_concentrate_solution70", new Object[0])
         .inputFluids(GTLMaterials.DilutedRareEarthChlorideSolution.getFluid(1000L))
         .outputFluids(GTLMaterials.RedMud.getFluid(1000L))
         .outputItems(TagPrefix.dustSmall, GTLMaterials.LanthanumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.PraseodymiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.NeodymiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.CeriumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.EuropiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.GadoliniumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.SamariumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.TerbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.DysprosiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.HolmiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ErbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ThuliumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.YtterbiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.LutetiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.ScandiumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.YttriumOxide, 1)
         .outputItems(TagPrefix.dustSmall, GTLMaterials.PromethiumOxide, 1)
         .duration(20)
         .EUt(491520L)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("samarium_chloride_concentrate_solution71", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.PromethiumOxide, 10)
         .inputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
         .outputItems(TagPrefix.dust, GTMaterials.Promethium, 4)
         .outputFluids(GTMaterials.CarbonDioxide.getFluid(3000L))
         .duration(200)
         .EUt(480L)
         .blastFurnaceTemp(2500)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("make_p507_1", new Object[0])
         .notConsumable(TagPrefix.plate, GTMaterials.Copper, 1)
         .inputFluids(GTMaterials.SeedOil.getFluid(3000L))
         .inputFluids(GTMaterials.Hydrogen.getFluid(8000L))
         .outputFluids(GTLMaterials.EthylHexanol.getFluid(1000L))
         .duration(400)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("make_p507_2", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Sodium, 2)
         .inputFluids(GTLMaterials.EthylHexanol.getFluid(2000L))
         .inputFluids(GTMaterials.PhosphoricAcid.getFluid(2000L))
         .inputFluids(GTMaterials.Ethanol.getFluid(2000L))
         .outputFluids(GTLMaterials.P507.getFluid(1000L))
         .duration(1200)
         .EUt(1920L)
         .save(provider);
      Material[] rareEarthElements = new Material[]{
         GTMaterials.Lanthanum,
         GTMaterials.Cerium,
         GTMaterials.Praseodymium,
         GTMaterials.Neodymium,
         GTMaterials.Promethium,
         GTMaterials.Samarium,
         GTMaterials.Europium,
         GTMaterials.Gadolinium,
         GTMaterials.Terbium,
         GTMaterials.Dysprosium,
         GTMaterials.Holmium,
         GTMaterials.Erbium,
         GTMaterials.Thulium,
         GTMaterials.Ytterbium,
         GTMaterials.Lutetium,
         GTMaterials.Scandium,
         GTMaterials.Yttrium
      };
      Material[] extractionNanoResins = new Material[]{
         GTLMaterials.LanthanumExtractionNanoResin,
         GTLMaterials.CeriumExtractionNanoResin,
         GTLMaterials.PraseodymiumExtractionNanoResin,
         GTLMaterials.NeodymiumExtractionNanoResin,
         GTLMaterials.PromethiumExtractionNanoResin,
         GTLMaterials.SamariumExtractionNanoResin,
         GTLMaterials.EuropiumExtractionNanoResin,
         GTLMaterials.GadoliniumExtractionNanoResin,
         GTLMaterials.TerbiumExtractionNanoResin,
         GTLMaterials.DysprosiumExtractionNanoResin,
         GTLMaterials.HolmiumExtractionNanoResin,
         GTLMaterials.ErbiumExtractionNanoResin,
         GTLMaterials.ThuliumExtractionNanoResin,
         GTLMaterials.YtterbiumExtractionNanoResin,
         GTLMaterials.LutetiumExtractionNanoResin,
         GTLMaterials.ScandiumExtractionNanoResin,
         GTLMaterials.YttriumExtractionNanoResin
      };
      Material[] extractedNanoResins = new Material[]{
         GTLMaterials.LanthanumExtractedNanoResin,
         GTLMaterials.CeriumExtractedNanoResin,
         GTLMaterials.PraseodymiumExtractedNanoResin,
         GTLMaterials.NeodymiumExtractedNanoResin,
         GTLMaterials.PromethiumExtractedNanoResin,
         GTLMaterials.SamariumExtractedNanoResin,
         GTLMaterials.EuropiumExtractedNanoResin,
         GTLMaterials.GadoliniumExtractedNanoResin,
         GTLMaterials.TerbiumExtractedNanoResin,
         GTLMaterials.DysprosiumExtractedNanoResin,
         GTLMaterials.HolmiumExtractedNanoResin,
         GTLMaterials.ErbiumExtractedNanoResin,
         GTLMaterials.ThuliumExtractedNanoResin,
         GTLMaterials.YtterbiumExtractedNanoResin,
         GTLMaterials.LutetiumExtractedNanoResin,
         GTLMaterials.ScandiumExtractedNanoResin,
         GTLMaterials.YttriumExtractedNanoResin
      };

      for (int i = 0; i < rareEarthElements.length; i++) {
         Material rareEarthElement = rareEarthElements[i];
         Material nanoResin = extractionNanoResins[i];
         Material extractedNanoResin = extractedNanoResins[i];
         GTRecipeTypes.LASER_ENGRAVER_RECIPES
            .recipeBuilder("make_extraction_nano_resin_" + i, new Object[0])
            .inputItems(GTLTagPrefix.nanoswarm, GTMaterials.Carbon, 1)
            .notConsumable(TagPrefix.lens, GTMaterials.NetherStar, 1)
            .inputFluids(rareEarthElement.getFluid(4000L))
            .inputFluids(GTLMaterials.P507.getFluid(4000L))
            .outputFluids(nanoResin.getFluid(1000L))
            .duration(1200)
            .EUt(491520L)
            .save(provider);
         GTRecipeTypes.LARGE_CHEMICAL_RECIPES
            .recipeBuilder("extraction_nano_resin_a_" + i, new Object[0])
            .inputFluids(nanoResin.getFluid(1000L))
            .inputFluids(GTLMaterials.ConcentratedRareEarthChlorideSolution.getFluid(1000L))
            .outputFluids(GTLMaterials.EnrichedRareEarthChlorideSolution.getFluid(1000L))
            .outputFluids(extractedNanoResin.getFluid(1000L))
            .duration(20)
            .EUt(491520L)
            .save(provider);
         GTRecipeTypes.LARGE_CHEMICAL_RECIPES
            .recipeBuilder("extraction_nano_resin_b_" + i, new Object[0])
            .inputFluids(nanoResin.getFluid(1000L))
            .inputFluids(GTLMaterials.EnrichedRareEarthChlorideSolution.getFluid(1000L))
            .outputFluids(GTLMaterials.DilutedRareEarthChlorideSolution.getFluid(1000L))
            .outputFluids(extractedNanoResin.getFluid(1000L))
            .duration(80)
            .EUt(491520L)
            .save(provider);
         GTRecipeTypes.LARGE_CHEMICAL_RECIPES
            .recipeBuilder("extraction_nano_resin_c_" + i, new Object[0])
            .inputFluids(nanoResin.getFluid(1000L))
            .inputFluids(GTLMaterials.DilutedRareEarthChlorideSolution.getFluid(1000L))
            .outputFluids(extractedNanoResin.getFluid(1000L))
            .duration(320)
            .EUt(491520L)
            .save(provider);
         GTRecipeTypes.ELECTROLYZER_RECIPES
            .recipeBuilder("break_down_extracted_nano_resin_" + i, new Object[0])
            .inputFluids(extractedNanoResin.getFluid(1000L))
            .outputItems(TagPrefix.dust, rareEarthElement, 1)
            .outputFluids(nanoResin.getFluid(1000L))
            .outputFluids(GTMaterials.Chlorine.getFluid(3000L))
            .duration(100)
            .EUt(122880L)
            .save(provider);
      }
   }
}
