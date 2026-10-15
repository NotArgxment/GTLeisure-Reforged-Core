package org.gtlcore.gtlcore.data.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;

public class FuelRecipes {
   public static void init(Consumer<FinishedRecipe> provider) {
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("naphtha", new Object[0])
         .inputFluids(GTMaterials.Naphtha.getFluid(1L))
         .duration(20)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("sulfuric_light_fuel", new Object[0])
         .inputFluids(GTMaterials.SulfuricLightFuel.getFluid(4L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("methanol", new Object[0])
         .inputFluids(GTMaterials.Methanol.getFluid(4L))
         .duration(16)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("ethanol", new Object[0])
         .inputFluids(GTMaterials.Ethanol.getFluid(1L))
         .duration(12)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("octane", new Object[0])
         .inputFluids(GTMaterials.Octane.getFluid(2L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("biodiesel", new Object[0])
         .inputFluids(GTMaterials.BioDiesel.getFluid(1L))
         .duration(16)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("light_fuel", new Object[0])
         .inputFluids(GTMaterials.LightFuel.getFluid(1L))
         .duration(20)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("diesel", new Object[0])
         .inputFluids(GTMaterials.Diesel.getFluid(1L))
         .duration(30)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("cetane_diesel", new Object[0])
         .inputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(2L))
         .duration(90)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("rocket_fuel", new Object[0])
         .inputFluids(GTMaterials.RocketFuel.getFluid(16L))
         .duration(250)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("gasoline", new Object[0])
         .inputFluids(GTMaterials.Gasoline.getFluid(1L))
         .duration(100)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("high_octane_gasoline", new Object[0])
         .inputFluids(GTMaterials.HighOctaneGasoline.getFluid(1L))
         .duration(200)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("toluene", new Object[0])
         .inputFluids(GTMaterials.Toluene.getFluid(1L))
         .duration(20)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("light_oil", new Object[0])
         .inputFluids(GTMaterials.OilLight.getFluid(32L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
         .recipeBuilder("raw_oil", new Object[0])
         .inputFluids(GTMaterials.RawOil.getFluid(64L))
         .duration(30)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("natural_gas", new Object[0])
         .inputFluids(GTMaterials.NaturalGas.getFluid(8L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("wood_gas", new Object[0])
         .inputFluids(GTMaterials.WoodGas.getFluid(8L))
         .duration(12)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("sulfuric_gas", new Object[0])
         .inputFluids(GTMaterials.SulfuricGas.getFluid(32L))
         .duration(50)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("sulfuric_naphtha", new Object[0])
         .inputFluids(GTMaterials.SulfuricNaphtha.getFluid(4L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("coal_gas", new Object[0])
         .inputFluids(GTMaterials.CoalGas.getFluid(1L))
         .duration(6)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("methane", new Object[0])
         .inputFluids(GTMaterials.Methane.getFluid(2L))
         .duration(14)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("ethylene", new Object[0])
         .inputFluids(GTMaterials.Ethylene.getFluid(1L))
         .duration(8)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("refinery_gas", new Object[0])
         .inputFluids(GTMaterials.RefineryGas.getFluid(1L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("ethane", new Object[0])
         .inputFluids(GTMaterials.Ethane.getFluid(4L))
         .duration(42)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("propene", new Object[0])
         .inputFluids(GTMaterials.Propene.getFluid(1L))
         .duration(12)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("butadiene", new Object[0])
         .inputFluids(GTMaterials.Butadiene.getFluid(16L))
         .duration(204)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("propane", new Object[0])
         .inputFluids(GTMaterials.Propane.getFluid(4L))
         .duration(58)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("butene", new Object[0])
         .inputFluids(GTMaterials.Butene.getFluid(1L))
         .duration(16)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("phenol", new Object[0])
         .inputFluids(GTMaterials.Phenol.getFluid(1L))
         .duration(18)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("benzene", new Object[0])
         .inputFluids(GTMaterials.Benzene.getFluid(1L))
         .duration(22)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("butane", new Object[0])
         .inputFluids(GTMaterials.Butane.getFluid(4L))
         .duration(74)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("lpg", new Object[0])
         .inputFluids(GTMaterials.LPG.getFluid(1L))
         .duration(20)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTRecipeTypes.GAS_TURBINE_FUELS
         .recipeBuilder("nitrobenzene", new Object[0])
         .inputFluids(GTMaterials.Nitrobenzene.getFluid(1L))
         .duration(80)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SUPERCRITICAL_STEAM_TURBINE_FUELS
         .recipeBuilder("supercritical_steam", new Object[0])
         .inputFluids(GTLMaterials.SupercriticalSteam.getFluid(1280L))
         .outputFluids(GTMaterials.DistilledWater.getFluid(8L))
         .duration(30)
         .EUt(-GTValues.V[2])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("seed_oil", new Object[0])
         .inputFluids(GTMaterials.SeedOil.getFluid(32L))
         .duration(8)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("fish_oil", new Object[0])
         .inputFluids(GTMaterials.FishOil.getFluid(32L))
         .duration(8)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("biomass", new Object[0])
         .inputFluids(GTMaterials.Biomass.getFluid(8L))
         .duration(8)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("oil", new Object[0])
         .inputFluids(GTMaterials.Oil.getFluid(4L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("oil_light", new Object[0])
         .inputFluids(GTMaterials.OilLight.getFluid(4L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("creosote", new Object[0])
         .inputFluids(GTMaterials.Creosote.getFluid(4L))
         .duration(12)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("oil_heavy", new Object[0])
         .inputFluids(GTMaterials.OilHeavy.getFluid(4L))
         .duration(15)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("oil_medium", new Object[0])
         .inputFluids(GTMaterials.RawOil.getFluid(4L))
         .duration(15)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("coal_tar", new Object[0])
         .inputFluids(GTMaterials.CoalTar.getFluid(8L))
         .duration(8)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("sulfuric_heavy_fuel", new Object[0])
         .inputFluids(GTMaterials.SulfuricHeavyFuel.getFluid(2L))
         .duration(10)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("glycerol", new Object[0])
         .inputFluids(GTMaterials.Glycerol.getFluid(2L))
         .duration(41)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
         .recipeBuilder("heavy_fuel", new Object[0])
         .inputFluids(GTMaterials.HeavyFuel.getFluid(2L))
         .duration(45)
         .EUt(-GTValues.V[1])
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_1", new Object[0])
         .inputFluids(GTMaterials.RocketFuel.getFluid(10L))
         .duration(5)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_2", new Object[0])
         .inputFluids(GTLMaterials.RocketFuelRp1.getFluid(10L))
         .duration(8)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_3", new Object[0])
         .inputFluids(GTLMaterials.DenseHydrazineFuelMixture.getFluid(10L))
         .duration(14)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_4", new Object[0])
         .inputFluids(GTLMaterials.RocketFuelCn3h7o3.getFluid(10L))
         .duration(20)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_5", new Object[0])
         .inputFluids(GTLMaterials.RocketFuelH8n4c2o4.getFluid(10L))
         .duration(30)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_6", new Object[0])
         .inputFluids(GTLMaterials.ExplosiveHydrazine.getFluid(10L))
         .duration(60)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.ROCKET_ENGINE_FUELS
         .recipeBuilder("rocket_engine_fuel_8", new Object[0])
         .inputFluids(GTLMaterials.StellarEnergyRocketFuel.getFluid(10L))
         .duration(240)
         .EUt(-2048L)
         .save(provider);
      GTLRecipeTypes.NAQUADAH_REACTOR
         .recipeBuilder("naquadah", new Object[0])
         .inputFluids(GTMaterials.Naquadah.getFluid(1L))
         .duration(100)
         .EUt(-32768L)
         .save(provider);
      GTLRecipeTypes.NAQUADAH_REACTOR
         .recipeBuilder("enriched_naquadah", new Object[0])
         .inputFluids(GTMaterials.NaquadahEnriched.getFluid(1L))
         .duration(150)
         .EUt(-32768L)
         .save(provider);
      GTLRecipeTypes.NAQUADAH_REACTOR
         .recipeBuilder("naquadah_fuel", new Object[0])
         .inputFluids(GTLMaterials.NaquadahFuel.getFluid(1L))
         .duration(875)
         .EUt(-32768L)
         .save(provider);
      GTLRecipeTypes.NAQUADAH_REACTOR
         .recipeBuilder("enriched_naquadah_fuel", new Object[0])
         .inputFluids(GTLMaterials.EnrichedNaquadahFuel.getFluid(1L))
         .duration(1250)
         .EUt(-32768L)
         .save(provider);
      GTLRecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES
         .recipeBuilder("naquadah_fuel", new Object[0])
         .inputFluids(GTLMaterials.NaquadahFuel.getFluid(1L))
         .inputFluids(GTMaterials.Hydrogen.getFluid(10L))
         .duration(875)
         .EUt(-131072L)
         .save(provider);
      GTLRecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES
         .recipeBuilder("enriched_naquadah_fuel", new Object[0])
         .inputFluids(GTLMaterials.EnrichedNaquadahFuel.getFluid(1L))
         .inputFluids(GTMaterials.Hydrogen.getFluid(10L))
         .duration(1250)
         .EUt(-131072L)
         .save(provider);
      GTLRecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES
         .recipeBuilder("naquadah_fuel1", new Object[0])
         .inputFluids(GTLMaterials.NaquadahFuel.getFluid(10L))
         .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 10L))
         .duration(14000)
         .EUt(-131072L)
         .save(provider);
      GTLRecipeTypes.LARGE_NAQUADAH_REACTOR_RECIPES
         .recipeBuilder("enriched_naquadah_fuel1", new Object[0])
         .inputFluids(GTLMaterials.EnrichedNaquadahFuel.getFluid(10L))
         .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 10L))
         .duration(20000)
         .EUt(-131072L)
         .save(provider);
      GTLRecipeTypes.HYPER_REACTOR_RECIPES
         .recipeBuilder("hyper_fuel_1", new Object[0])
         .inputFluids(GTLMaterials.HyperFuel1.getFluid(1L))
         .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 1L))
         .duration(200)
         .EUt(-GTValues.V[10])
         .save(provider);
      GTLRecipeTypes.HYPER_REACTOR_RECIPES
         .recipeBuilder("hyper_fuel_2", new Object[0])
         .inputFluids(GTLMaterials.HyperFuel2.getFluid(1L))
         .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 1L))
         .duration(200)
         .EUt(-GTValues.V[11])
         .save(provider);
      GTLRecipeTypes.HYPER_REACTOR_RECIPES
         .recipeBuilder("hyper_fuel_3", new Object[0])
         .inputFluids(GTLMaterials.HyperFuel3.getFluid(1L))
         .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 1L))
         .duration(200)
         .EUt(-GTValues.V[12])
         .save(provider);
      GTLRecipeTypes.HYPER_REACTOR_RECIPES
         .recipeBuilder("hyper_fuel_4", new Object[0])
         .inputFluids(GTLMaterials.HyperFuel4.getFluid(1L))
         .inputFluids(GTLMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1L))
         .duration(200)
         .EUt(-GTValues.V[13])
         .save(provider);
      GTLRecipeTypes.ADVANCED_HYPER_REACTOR_RECIPES
         .recipeBuilder("concentration_mixing_hyper_fuel_1", new Object[0])
         .inputFluids(GTLMaterials.ConcentrationMixingHyperFuel1.getFluid(1L))
         .duration(200)
         .EUt(-4L * GTValues.V[14])
         .save(provider);
      GTLRecipeTypes.ADVANCED_HYPER_REACTOR_RECIPES
         .recipeBuilder("concentration_mixing_hyper_fuel_2", new Object[0])
         .inputFluids(GTLMaterials.ConcentrationMixingHyperFuel2.getFluid(1L))
         .duration(200)
         .EUt(-16L * GTValues.V[14])
         .save(provider);
   }
}
