package org.gtlcore.gtlcore.mixin.gtm.recipe.change;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.serialized.chemistry.PlatGroupMetalsRecipes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlatGroupMetalsRecipes.class)
public abstract class PlatGroupMetalsRecipesMixin {
   @Overwrite(remap = false)
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("pgs_from_chalcocite", new Object[0])
         .duration(50)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.crushedPurified, GTMaterials.Chalcocite)
         .inputFluids(GTMaterials.NitricAcid.getFluid(100L))
         .outputItems(TagPrefix.dust, GTMaterials.PlatinumGroupSludge, 8)
         .outputFluids(GTMaterials.SulfuricCopperSolution.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("pgs_from_bornite", new Object[0])
         .duration(50)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.crushedPurified, GTMaterials.Bornite)
         .inputFluids(GTMaterials.NitricAcid.getFluid(100L))
         .outputItems(TagPrefix.dust, GTMaterials.PlatinumGroupSludge, 8)
         .outputFluids(GTMaterials.SulfuricCopperSolution.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("pgs_from_tetrahedrite", new Object[0])
         .duration(50)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.crushedPurified, GTMaterials.Tetrahedrite)
         .inputFluids(GTMaterials.NitricAcid.getFluid(100L))
         .outputItems(TagPrefix.dust, GTMaterials.PlatinumGroupSludge, 8)
         .outputFluids(GTMaterials.SulfuricCopperSolution.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("pgs_from_cooperite", new Object[0])
         .duration(50)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.crushedPurified, GTMaterials.Cooperite)
         .inputFluids(GTMaterials.NitricAcid.getFluid(100L))
         .outputItems(TagPrefix.dust, GTMaterials.PlatinumGroupSludge, 16)
         .outputFluids(GTMaterials.SulfuricNickelSolution.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("aqua_regia", new Object[0])
         .duration(30)
         .EUt(GTValues.VA[1])
         .inputFluids(GTMaterials.NitricAcid.getFluid(1000L))
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(2000L))
         .outputFluids(GTMaterials.AquaRegia.getFluid(3000L))
         .save(provider);
      GTRecipeTypes.CENTRIFUGE_RECIPES
         .recipeBuilder("pgs_separation", new Object[0])
         .duration(20)
         .EUt(GTValues.VA[3])
         .inputItems(TagPrefix.dust, GTMaterials.PlatinumGroupSludge, 6)
         .inputFluids(GTMaterials.AquaRegia.getFluid(1200L))
         .chancedOutput(TagPrefix.dust, GTLMaterials.PlatinumSlag, 6, 7500, 500)
         .chancedOutput(TagPrefix.dust, GTMaterials.PlatinumRaw, 4, 8000, 500)
         .chancedOutput(TagPrefix.dust, GTMaterials.PalladiumRaw, 4, 8000, 500)
         .chancedOutput(TagPrefix.dust, GTMaterials.PlatinumSludgeResidue, 2, 8500, 500)
         .chancedOutput(TagPrefix.dustTiny, GTMaterials.InertMetalMixture, 4, 9000, 500)
         .chancedOutput(TagPrefix.dustTiny, GTMaterials.RarestMetalMixture, 1, 9000, 100)
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("leach_residue", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.PlatinumSlag, 6)
         .inputItems(TagPrefix.dust, GTLMaterials.PotassiumPyrosulfate, 11)
         .inputFluids(GTMaterials.Oxygen.getFluid(1000L))
         .outputFluids(GTLMaterials.RhodiumSulfateGas.getFluid(500L))
         .outputItems(TagPrefix.dust, GTLMaterials.LeachResidue, 5)
         .outputItems(TagPrefix.dust, GTMaterials.PotassiumSulfate, 7)
         .blastFurnaceTemp(775)
         .duration(20)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("rarest_metal_mixture", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.LeachResidue, 40)
         .inputItems(TagPrefix.dust, GTMaterials.SodaAsh, 18)
         .inputFluids(GTMaterials.Oxygen.getFluid(3000L))
         .outputFluids(GTMaterials.CarbonMonoxide.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTMaterials.RarestMetalMixture, 20)
         .outputItems(TagPrefix.dust, GTLMaterials.SodiumRutheniate, 21)
         .blastFurnaceTemp(775)
         .duration(20)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("sodium_formate", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 3)
         .inputFluids(GTMaterials.CarbonMonoxide.getFluid(1000L))
         .outputFluids(GTLMaterials.SodiumFormate.getFluid(1000L))
         .duration(15)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("formic_acid_a", new Object[0])
         .inputFluids(GTLMaterials.SodiumFormate.getFluid(2000L))
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .circuitMeta(1)
         .outputFluids(GTMaterials.FormicAcid.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SodiumSulfate, 7)
         .duration(15)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("raw_platinum_separation", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.PlatinumRaw, 3)
         .inputItems(TagPrefix.dust, GTMaterials.Calcium, 1)
         .outputItems(TagPrefix.dust, GTMaterials.Platinum, 1)
         .outputItems(TagPrefix.dust, GTMaterials.CalciumChloride, 3)
         .duration(30)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.LARGE_CHEMICAL_RECIPES
         .recipeBuilder("raw_palladium_separation", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.PalladiumRaw, 4)
         .inputFluids(GTMaterials.FormicAcid.getFluid(4000L))
         .outputItems(TagPrefix.dust, GTMaterials.Palladium, 1)
         .outputFluids(GTMaterials.Ammonia.getFluid(4000L))
         .outputFluids(GTMaterials.Ethylene.getFluid(1000L))
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("leach_residue_one", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.InertMetalMixture, 6)
         .inputItems(TagPrefix.dust, GTLMaterials.SodiumNitrate, 10)
         .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000L))
         .outputFluids(GTLMaterials.RhodiumSulfateGas.getFluid(500L))
         .outputItems(TagPrefix.dust, GTLMaterials.SodiumRutheniate, 7)
         .blastFurnaceTemp(775)
         .duration(50)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("ruthenium_tetroxide", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SodiumRutheniate, 7)
         .inputFluids(GTMaterials.Chlorine.getFluid(3000L))
         .outputFluids(GTMaterials.RutheniumTetroxide.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTMaterials.Salt, 12)
         .duration(30)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CRACKING_RECIPES
         .recipeBuilder("hot_ruthenium_tetroxide", new Object[0])
         .inputFluids(GTMaterials.RutheniumTetroxide.getFluid(1000L))
         .inputFluids(GTMaterials.Steam.getFluid(1000L))
         .circuitMeta(1)
         .outputFluids(GTLMaterials.RutheniumTetroxideHot.getFluid(1000L))
         .duration(40)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("hot_ruthenium_tetroxide_distill", new Object[0])
         .inputFluids(GTLMaterials.RutheniumTetroxideHot.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTMaterials.Salt, 2)
         .outputFluids(GTMaterials.Water.getFluid(1800L))
         .outputFluids(GTLMaterials.RutheniumTetroxideLQ.getFluid(1000L))
         .duration(30)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES
         .recipeBuilder("ruthenium_tetroxide_dust", new Object[0])
         .inputFluids(GTLMaterials.RutheniumTetroxideLQ.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.RutheniumTetroxide, 5)
         .duration(20)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("ruthenium_tetroxide_separation", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.RutheniumTetroxide, 5)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(6000L))
         .outputItems(TagPrefix.dust, GTMaterials.Ruthenium, 1)
         .outputFluids(GTMaterials.Water.getFluid(2000L))
         .outputFluids(GTMaterials.Chlorine.getFluid(6000L))
         .duration(60)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("rhodium_sulfate_to_solution", new Object[0])
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .inputFluids(GTLMaterials.RhodiumSulfateGas.getFluid(1000L))
         .outputFluids(GTMaterials.RhodiumSulfate.getFluid(1000L))
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTLRecipeTypes.DEHYDRATOR_RECIPES
         .recipeBuilder("crude_rhodium_metallic_powder", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Zinc, 1)
         .inputFluids(GTMaterials.RhodiumSulfate.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.ZincSulfate, 6)
         .outputItems(TagPrefix.dust, GTLMaterials.RoughlyRhodiumMetal, 1)
         .duration(80)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("rhodium_salt_ebf", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.RoughlyRhodiumMetal, 1)
         .inputItems(TagPrefix.dust, GTMaterials.Salt, 2)
         .inputFluids(GTMaterials.Chlorine.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.RhodiumSalt, 3)
         .blastFurnaceTemp(600)
         .duration(70)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("rhodium_salt_solution", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.RhodiumSalt, 4)
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .outputFluids(GTLMaterials.RhodiumSaltSolution.getFluid(1000L))
         .duration(60)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("sodium_nitrate", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.Sodium)
         .inputFluids(GTMaterials.NitricAcid.getFluid(1000L))
         .outputFluids(GTMaterials.Hydrogen.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.SodiumNitrate, 5)
         .duration(60)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("rhodium_nitrate", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.SodiumNitrate, 5)
         .inputFluids(GTMaterials.Oxygen.getFluid(2000L))
         .inputFluids(GTMaterials.NitrogenDioxide.getFluid(2000L))
         .inputFluids(GTLMaterials.RhodiumSaltSolution.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTLMaterials.RhodiumNitrate, 13)
         .outputItems(TagPrefix.dust, GTMaterials.Salt, 4)
         .duration(60)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.SIFTER_RECIPES
         .recipeBuilder("rhodium_filter_cake", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.RhodiumNitrate, 13)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 8000, 200)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 8000, 200)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 6000, 100)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 6000, 100)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 4000, 50)
         .chancedOutput(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 4000, 50)
         .duration(50)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.MIXER_RECIPES
         .recipeBuilder("rhodium_filter_cake_solution", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.RhodiumFilterCake, 6)
         .inputFluids(GTMaterials.Water.getFluid(1000L))
         .outputFluids(GTLMaterials.RhodiumFilterCakeSolution.getFluid(1000L))
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.DISTILLERY_RECIPES
         .recipeBuilder("reprecipitated_rhodium", new Object[0])
         .inputFluids(GTLMaterials.RhodiumFilterCakeSolution.getFluid(1000L))
         .circuitMeta(1)
         .outputItems(TagPrefix.dust, GTLMaterials.ReprecipitatedRhodium, 7)
         .duration(50)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("rhodium_dust", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.ReprecipitatedRhodium, 7)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.Rhodium, 1)
         .outputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 6)
         .outputFluids(GTMaterials.Hydrogen.getFluid(1000L))
         .duration(70)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("rarest_metal_residue_ebf", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.RarestMetalMixture, 7)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(4000L))
         .outputItems(TagPrefix.dust, GTMaterials.IridiumMetalResidue, 5)
         .outputFluids(GTMaterials.AcidicOsmiumSolution.getFluid(1600L))
         .blastFurnaceTemp(775)
         .duration(20)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("acidic_osmium_solution_separation", new Object[0])
         .duration(80)
         .EUt(GTValues.VA[1])
         .inputFluids(GTMaterials.AcidicOsmiumSolution.getFluid(2000L))
         .outputItems(TagPrefix.dust, GTMaterials.OsmiumTetroxide, 5)
         .outputFluids(GTMaterials.HydrochloricAcid.getFluid(1000L))
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("iridium_dioxide_dissolving", new Object[0])
         .inputItems(TagPrefix.dust, GTLMaterials.IridiumDioxide, 3)
         .inputFluids(GTMaterials.HydrochloricAcid.getFluid(1000L))
         .outputFluids(GTLMaterials.AcidicIridium.getFluid(1000L))
         .duration(90)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("iridium_chloride", new Object[0])
         .inputItems(TagPrefix.dust, GTMaterials.AmmoniumChloride, 18)
         .inputFluids(GTLMaterials.AcidicIridium.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.IridiumChloride, 4)
         .outputFluids(GTMaterials.Ammonia.getFluid(3000L))
         .duration(40)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("osmium_tetroxide_separation", new Object[0])
         .duration(70)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.dust, GTMaterials.OsmiumTetroxide, 5)
         .inputFluids(GTMaterials.Hydrogen.getFluid(8000L))
         .outputItems(TagPrefix.dust, GTMaterials.Osmium)
         .outputFluids(GTMaterials.Water.getFluid(4000L))
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("iridium_chloride_separation", new Object[0])
         .duration(100)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.dust, GTMaterials.IridiumChloride, 4)
         .inputFluids(GTMaterials.Hydrogen.getFluid(3000L))
         .outputItems(TagPrefix.dust, GTMaterials.Iridium)
         .outputFluids(GTMaterials.HydrochloricAcid.getFluid(3000L))
         .save(provider);
   }
}
