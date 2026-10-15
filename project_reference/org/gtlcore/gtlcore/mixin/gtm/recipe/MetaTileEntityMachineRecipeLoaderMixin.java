package org.gtlcore.gtlcore.mixin.gtm.recipe;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.ResearchRecipeBuilder.ScannerRecipeBuilder;
import com.gregtechceu.gtceu.api.recipe.ResearchRecipeBuilder.StationRecipeBuilder;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityMachineRecipeLoader;
import com.hepdd.gtmthings.data.CustomMachines;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.utils.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MetaTileEntityMachineRecipeLoader.class)
public abstract class MetaTileEntityMachineRecipeLoaderMixin {
   @Shadow(remap = false)
   private static void registerLaserRecipes(Consumer<FinishedRecipe> provider) {
   }

   @Overwrite(remap = false)
   public static void init(Consumer<FinishedRecipe> provider) {
      CraftingComponent.initializeComponents();
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("reservoir_hatch", new Object[0])
         .inputItems(GTItems.COVER_INFINITE_WATER)
         .inputItems(GTMachines.FLUID_IMPORT_HATCH[4])
         .inputItems(GTItems.ELECTRIC_PUMP_EV)
         .outputItems(GTMachines.RESERVOIR_HATCH)
         .duration(300)
         .EUt(GTValues.VA[4])
         .save(provider);
      registerLaserRecipes(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "dynamo_hatch_ulv",
         GTMachines.ENERGY_OUTPUT_HATCH[0].asStack(),
         new Object[]{
            " V ",
            "SHS",
            "   ",
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.Lead),
            'V',
            GTItems.VOLTAGE_COIL_ULV.asStack(),
            'H',
            GTMachines.HULL[0].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_ulv", new Object[0])
         .inputItems(GTMachines.HULL[0])
         .inputItems(TagPrefix.spring, GTMaterials.Lead, 2)
         .inputItems(GTItems.VOLTAGE_COIL_ULV)
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[0])
         .duration(200)
         .EUt(GTValues.VA[0])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "dynamo_hatch_lv",
         GTMachines.ENERGY_OUTPUT_HATCH[1].asStack(),
         new Object[]{
            " V ",
            "SHS",
            "   ",
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.Tin),
            'V',
            GTItems.VOLTAGE_COIL_LV.asStack(),
            'H',
            GTMachines.HULL[1].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_lv", new Object[0])
         .inputItems(GTMachines.HULL[1])
         .inputItems(TagPrefix.spring, GTMaterials.Tin, 2)
         .inputItems(GTItems.VOLTAGE_COIL_LV)
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[1])
         .duration(200)
         .EUt(GTValues.VA[1])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "dynamo_hatch_mv",
         GTMachines.ENERGY_OUTPUT_HATCH[2].asStack(),
         new Object[]{
            " V ",
            "SHS",
            " P ",
            'P',
            GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT.asStack(),
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.Copper),
            'V',
            GTItems.VOLTAGE_COIL_MV.asStack(),
            'H',
            GTMachines.HULL[2].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_mv", new Object[0])
         .inputItems(GTMachines.HULL[2])
         .inputItems(TagPrefix.spring, GTMaterials.Copper, 2)
         .inputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT)
         .inputItems(GTItems.VOLTAGE_COIL_MV)
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[2])
         .duration(200)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_hv", new Object[0])
         .inputItems(GTMachines.HULL[3])
         .inputItems(TagPrefix.spring, GTMaterials.Gold, 2)
         .inputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_HV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(1000L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[3])
         .duration(200)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_ev", new Object[0])
         .inputItems(GTMachines.HULL[4])
         .inputItems(TagPrefix.spring, GTMaterials.Aluminium, 2)
         .inputItems(GTItems.POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_EV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(2000L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[4])
         .duration(200)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("dynamo_hatch_iv", new Object[0])
         .inputItems(GTMachines.HULL[5])
         .inputItems(TagPrefix.spring, GTMaterials.Tungsten, 2)
         .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_IV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(3000L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[5])
         .duration(200)
         .EUt(GTValues.VA[5])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("dynamo_hatch_luv", new Object[0])
         .inputItems(GTMachines.HULL[6])
         .inputItems(TagPrefix.spring, GTMaterials.NiobiumTitanium, 4)
         .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.LuV_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_LuV, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(6000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(720L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[6])
         .duration(400)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("dynamo_hatch_zpm", new Object[0])
         .inputItems(GTMachines.HULL[7])
         .inputItems(TagPrefix.spring, GTMaterials.VanadiumGallium, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.ZPM_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_ZPM, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(8000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[7])
         .stationResearch(b -> ((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_OUTPUT_HATCH[6].asStack())).CWUt(8))
         .duration(600)
         .EUt(GTValues.VA[7])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("dynamo_hatch_uv", new Object[0])
         .inputItems(GTMachines.HULL[8])
         .inputItems(TagPrefix.spring, GTMaterials.YttriumBariumCuprate, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.UV_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_UV, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(10000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(2880L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[8])
         .stationResearch(
            b -> (StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_OUTPUT_HATCH[7].asStack())).CWUt(64).EUt(GTValues.VA[7])
         )
         .duration(800)
         .EUt(GTValues.VA[8])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("dynamo_hatch_uhv", new Object[0])
         .inputItems(GTMachines.HULL[9])
         .inputItems(TagPrefix.spring, GTMaterials.Europium, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.UHV_CIRCUITS)
         .inputItems(TagPrefix.wireGtDouble, GTMaterials.RutheniumTriniumAmericiumNeutronate, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(12000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(5760L))
         .outputItems(GTMachines.ENERGY_OUTPUT_HATCH[9])
         .stationResearch(
            b -> (StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_OUTPUT_HATCH[8].asStack())).CWUt(128).EUt(GTValues.VA[8])
         )
         .duration(1000)
         .EUt(GTValues.VA[9])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "energy_hatch_ulv",
         GTMachines.ENERGY_INPUT_HATCH[0].asStack(),
         new Object[]{
            " V ",
            "CHC",
            "   ",
            'C',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.RedAlloy),
            'V',
            GTItems.VOLTAGE_COIL_ULV.asStack(),
            'H',
            GTMachines.HULL[0].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_ulv", new Object[0])
         .inputItems(GTMachines.HULL[0])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.RedAlloy, 2)
         .inputItems(GTItems.VOLTAGE_COIL_ULV)
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[0])
         .duration(200)
         .EUt(GTValues.VA[0])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "energy_hatch_lv",
         GTMachines.ENERGY_INPUT_HATCH[1].asStack(),
         new Object[]{
            " V ",
            "CHC",
            "   ",
            'C',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Tin),
            'V',
            GTItems.VOLTAGE_COIL_LV.asStack(),
            'H',
            GTMachines.HULL[1].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_lv", new Object[0])
         .inputItems(GTMachines.HULL[1])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Tin, 2)
         .inputItems(GTItems.VOLTAGE_COIL_LV)
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[1])
         .duration(200)
         .EUt(GTValues.VA[1])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "energy_hatch_mv",
         GTMachines.ENERGY_INPUT_HATCH[2].asStack(),
         new Object[]{
            " V ",
            "CHC",
            " P ",
            'C',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Copper),
            'P',
            GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT.asStack(),
            'V',
            GTItems.VOLTAGE_COIL_MV.asStack(),
            'H',
            GTMachines.HULL[2].asStack()
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_mv", new Object[0])
         .inputItems(GTMachines.HULL[2])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Copper, 2)
         .inputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT)
         .inputItems(GTItems.VOLTAGE_COIL_MV)
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[2])
         .duration(200)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_hv", new Object[0])
         .inputItems(GTMachines.HULL[3])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Gold, 2)
         .inputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_HV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(1000L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[3])
         .duration(200)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_ev", new Object[0])
         .inputItems(GTMachines.HULL[4])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Aluminium, 2)
         .inputItems(GTItems.POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_EV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(2000L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[4])
         .duration(200)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("energy_hatch_iv", new Object[0])
         .inputItems(GTMachines.HULL[5])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Tungsten, 2)
         .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(GTItems.VOLTAGE_COIL_IV)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(3000L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[5])
         .duration(200)
         .EUt(GTValues.VA[5])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("energy_hatch_luv", new Object[0])
         .inputItems(GTMachines.HULL[6])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.NiobiumTitanium, 4)
         .inputItems(GTItems.HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.LuV_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_LuV, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(6000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(720L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[6])
         .scannerResearch(b -> (ScannerRecipeBuilder)((ScannerRecipeBuilder)b.researchStack(GTMachines.ENERGY_INPUT_HATCH[5].asStack())).EUt(GTValues.VA[4]))
         .duration(400)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("energy_hatch_zpm", new Object[0])
         .inputItems(GTMachines.HULL[7])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.VanadiumGallium, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.ZPM_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_ZPM, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(8000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[7])
         .stationResearch(b -> ((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_INPUT_HATCH[6].asStack())).CWUt(8))
         .duration(600)
         .EUt(GTValues.VA[7])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("energy_hatch_uv", new Object[0])
         .inputItems(GTMachines.HULL[8])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.YttriumBariumCuprate, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.UV_CIRCUITS)
         .inputItems(GTItems.VOLTAGE_COIL_UV, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(10000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(2880L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[8])
         .stationResearch(
            b -> (StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_INPUT_HATCH[7].asStack())).CWUt(64).EUt(GTValues.VA[7])
         )
         .duration(800)
         .EUt(GTValues.VA[8])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("energy_hatch_uhv", new Object[0])
         .inputItems(GTMachines.HULL[9])
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Europium, 4)
         .inputItems(GTItems.ULTRA_HIGH_POWER_INTEGRATED_CIRCUIT, 2)
         .inputItems(CustomTags.UHV_CIRCUITS)
         .inputItems(TagPrefix.wireGtDouble, GTMaterials.RutheniumTriniumAmericiumNeutronate, 2)
         .inputFluids(GTMaterials.SodiumPotassium.getFluid(12000L))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(5760L))
         .outputItems(GTMachines.ENERGY_INPUT_HATCH[9])
         .stationResearch(
            b -> (StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(GTMachines.ENERGY_INPUT_HATCH[8].asStack())).CWUt(128).EUt(GTValues.VA[8])
         )
         .duration(1000)
         .EUt(GTValues.VA[9])
         .save(provider);

      for (int tier = 0; tier < GTMachines.POWER_TRANSFORMER.length; tier++) {
         MachineDefinition hatch = GTMachines.POWER_TRANSFORMER[tier];
         if (hatch != null) {
            Material materialPrime = ChemicalHelper.getMaterial(CraftingComponent.CABLE_HEX.getIngredient(tier)).material();
            Material materialSecond = ChemicalHelper.getMaterial(CraftingComponent.CABLE_TIER_UP_OCT.getIngredient(tier)).material();
            String var10001 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder(var10001.toLowerCase() + "_power_transformer", new Object[0])
               .inputItems(GTMachines.HI_AMP_TRANSFORMER_4A[tier])
               .inputItems(CraftingComponent.PUMP.getIngredient(tier / 2 + 1))
               .inputItems(CraftingComponent.CABLE_TIER_UP_OCT.getIngredient(tier))
               .inputItems(CraftingComponent.CABLE_HEX.getIngredient(tier))
               .inputItems(TagPrefix.springSmall, materialPrime)
               .inputItems(TagPrefix.spring, materialSecond)
               .inputFluids(GTMaterials.Lubricant.getFluid(2000L))
               .outputItems(hatch)
               .duration(100)
               .EUt(GTValues.VA[tier])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.ENERGY_INPUT_HATCH_4A.length; tier++) {
         MachineDefinition hatch = GTMachines.ENERGY_INPUT_HATCH_4A[tier];
         if (hatch != null) {
            String var23 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("energy_hatch_4a_" + var23.toLowerCase(), new Object[0])
               .inputItems(GTMachines.ENERGY_INPUT_HATCH[tier])
               .inputItems(CraftingComponent.WIRE_QUAD.getIngredient(tier), 2)
               .inputItems(CraftingComponent.PLATE.getIngredient(tier), 2)
               .outputItems(hatch)
               .duration(100)
               .EUt(GTValues.VA[tier])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.ENERGY_INPUT_HATCH_16A.length; tier++) {
         MachineDefinition hatch = GTMachines.ENERGY_INPUT_HATCH_16A[tier];
         if (hatch != null) {
            MachineDefinition transformer;
            if (tier == (GTCEuAPI.isHighTier() ? 14 : 9)) {
               transformer = GTMachines.HI_AMP_TRANSFORMER_4A[tier - 1];
            } else {
               transformer = GTMachines.TRANSFORMER[tier];
            }

            String var24 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("energy_hatch_16a_" + var24.toLowerCase(), new Object[0])
               .inputItems(transformer)
               .inputItems(GTMachines.ENERGY_INPUT_HATCH_4A[tier])
               .inputItems(CraftingComponent.WIRE_OCT.getIngredient(tier), 2)
               .inputItems(CraftingComponent.PLATE.getIngredient(tier), 4)
               .outputItems(hatch)
               .duration(200)
               .EUt(GTValues.VA[tier])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.SUBSTATION_ENERGY_INPUT_HATCH.length; tier++) {
         MachineDefinition hatch = GTMachines.SUBSTATION_ENERGY_INPUT_HATCH[tier];
         if (hatch != null) {
            MachineDefinition transformer;
            if (tier == (GTCEuAPI.isHighTier() ? 14 : 9)) {
               transformer = GTMachines.POWER_TRANSFORMER[tier - 1];
            } else {
               transformer = GTMachines.POWER_TRANSFORMER[tier];
            }

            String var25 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("substation_energy_hatch_" + var25.toLowerCase(), new Object[0])
               .inputItems(transformer)
               .inputItems(GTMachines.ENERGY_INPUT_HATCH_16A[tier])
               .inputItems(CraftingComponent.WIRE_HEX.getIngredient(tier), 2)
               .inputItems(CraftingComponent.PLATE.getIngredient(tier), 6)
               .outputItems(hatch)
               .duration(400)
               .EUt(GTValues.VA[tier])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.ENERGY_OUTPUT_HATCH_4A.length; tier++) {
         MachineDefinition hatch = GTMachines.ENERGY_OUTPUT_HATCH_4A[tier];
         if (hatch != null) {
            String var26 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("dynamo_hatch_4a_" + var26.toLowerCase(), new Object[0])
               .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[tier])
               .inputItems(CraftingComponent.WIRE_QUAD.getIngredient(tier), 2)
               .inputItems(CraftingComponent.PLATE.getIngredient(tier), 2)
               .outputItems(hatch)
               .duration(100)
               .EUt(GTValues.VA[tier - 1])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.ENERGY_OUTPUT_HATCH_16A.length; tier++) {
         MachineDefinition hatch = GTMachines.ENERGY_OUTPUT_HATCH_16A[tier];
         if (hatch != null) {
            MachineDefinition transformer;
            if (tier == (GTCEuAPI.isHighTier() ? 14 : 9)) {
               transformer = GTMachines.HI_AMP_TRANSFORMER_4A[tier - 1];
            } else {
               transformer = GTMachines.TRANSFORMER[tier];
            }

            String var27 = GTValues.VN[tier];
            GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("dynamo_hatch_16a_" + var27.toLowerCase(), new Object[0])
               .inputItems(transformer)
               .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_4A[tier])
               .inputItems(CraftingComponent.WIRE_OCT.getIngredient(tier), 2)
               .inputItems(CraftingComponent.PLATE.getIngredient(tier), 4)
               .outputItems(hatch)
               .duration(200)
               .EUt(GTValues.VA[tier])
               .save(provider);
         }
      }

      for (int tier = 0; tier < GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH.length; tier++) {
         MachineDefinition hatch = GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH[tier];
         if (hatch != null) {
            MachineDefinition transformer;
            if (tier == (GTCEuAPI.isHighTier() ? 14 : 9)) {
               transformer = GTMachines.POWER_TRANSFORMER[tier - 1];
            } else {
               transformer = GTMachines.POWER_TRANSFORMER[tier];
            }

            if (transformer != null) {
               String var28 = GTValues.VN[tier];
               GTRecipeTypes.ASSEMBLER_RECIPES
                  .recipeBuilder("substation_dynamo_hatch_" + var28.toLowerCase(), new Object[0])
                  .inputItems(transformer)
                  .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_16A[tier])
                  .inputItems(CraftingComponent.WIRE_HEX.getIngredient(tier), 2)
                  .inputItems(CraftingComponent.PLATE.getIngredient(tier), 6)
                  .outputItems(hatch)
                  .duration(400)
                  .EUt(GTValues.VA[tier])
                  .save(provider);
            }
         }
      }

      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("maintenance_hatch", new Object[0])
         .inputItems(GTMachines.HULL[1])
         .circuitMeta(8)
         .outputItems(GTMachines.MAINTENANCE_HATCH)
         .duration(100)
         .EUt(GTValues.VA[1])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("ev_large_miner", new Object[0])
         .inputItems(GTMachines.HULL[4])
         .inputItems(TagPrefix.frameGt, GTMaterials.Titanium, 4)
         .inputItems(CustomTags.IV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_EV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_EV, 4)
         .inputItems(GTItems.CONVEYOR_MODULE_EV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.Tungsten, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.LARGE_MINER[4])
         .duration(400)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("iv_large_miner", new Object[0])
         .inputItems(GTMachines.HULL[5])
         .inputItems(TagPrefix.frameGt, GTMaterials.TungstenSteel, 4)
         .inputItems(CustomTags.IV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_IV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_IV, 4)
         .inputItems(GTItems.CONVEYOR_MODULE_IV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.Iridium, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.LARGE_MINER[5])
         .duration(400)
         .EUt(GTValues.VA[5])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("luv_large_miner", new Object[0])
         .inputItems(GTMachines.HULL[6])
         .inputItems(TagPrefix.frameGt, GTMaterials.HSSS, 4)
         .inputItems(CustomTags.LuV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_LuV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_LuV, 4)
         .inputItems(GTItems.CONVEYOR_MODULE_LuV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.Ruridit, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.LARGE_MINER[6])
         .duration(400)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("mv_fluid_drilling_rig", new Object[0])
         .inputItems(GTMachines.HULL[2])
         .inputItems(TagPrefix.frameGt, GTMaterials.Steel, 4)
         .inputItems(CustomTags.MV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_MV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_MV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.VanadiumSteel, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.FLUID_DRILLING_RIG[2])
         .duration(400)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hv_fluid_drilling_rig", new Object[0])
         .inputItems(GTMachines.HULL[4])
         .inputItems(TagPrefix.frameGt, GTMaterials.Titanium, 4)
         .inputItems(CustomTags.EV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_EV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_EV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.TungstenCarbide, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.FLUID_DRILLING_RIG[3])
         .duration(400)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("ev_fluid_drilling_rig", new Object[0])
         .inputItems(GTMachines.HULL[6])
         .inputItems(TagPrefix.frameGt, GTMaterials.TungstenSteel, 4)
         .inputItems(CustomTags.LuV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_LuV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_LuV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.Osmiridium, 4)
         .circuitMeta(2)
         .outputItems(GTMachines.FLUID_DRILLING_RIG[4])
         .duration(400)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("long_distance_item_endpoint", new Object[0])
         .inputItems(TagPrefix.pipeLargeItem, GTMaterials.Tin, 2)
         .inputItems(TagPrefix.plate, GTMaterials.Steel, 8)
         .inputItems(TagPrefix.gear, GTMaterials.Steel, 2)
         .circuitMeta(1)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(72L))
         .outputItems(GTMachines.LONG_DIST_ITEM_ENDPOINT, 2)
         .duration(400)
         .EUt(16L)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("long_distance_fluid_endpoint", new Object[0])
         .inputItems(TagPrefix.pipeLargeFluid, GTMaterials.Bronze, 2)
         .inputItems(TagPrefix.plate, GTMaterials.Steel, 8)
         .inputItems(TagPrefix.gear, GTMaterials.Steel, 2)
         .circuitMeta(1)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(72L))
         .outputItems(GTMachines.LONG_DIST_FLUID_ENDPOINT, 2)
         .duration(400)
         .EUt(16L)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("long_distance_item_pipe", new Object[0])
         .inputItems(TagPrefix.pipeLargeItem, GTMaterials.Tin, 2)
         .inputItems(TagPrefix.plate, GTMaterials.Steel, 8)
         .circuitMeta(2)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(72L))
         .outputItems(GTBlocks.LD_ITEM_PIPE, 64)
         .duration(600)
         .EUt(24L)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("long_distance_fluid_pipe", new Object[0])
         .inputItems(TagPrefix.pipeLargeFluid, GTMaterials.Bronze, 2)
         .inputItems(TagPrefix.plate, GTMaterials.Steel, 8)
         .circuitMeta(2)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(72L))
         .outputItems(GTBlocks.LD_FLUID_PIPE, 64)
         .duration(600)
         .EUt(24L)
         .save(provider);
      if (GTCEu.isAE2Loaded()) {
         ItemStack meInterface = AEParts.INTERFACE.stack(1);
         ItemStack accelerationCard = AEItems.SPEED_CARD.stack(2);
         ItemStack capacityCard = AEItems.CAPACITY_CARD.stack(1);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_export_hatch", new Object[0])
            .inputItems(GTMachines.FLUID_EXPORT_HATCH[4])
            .inputItems(meInterface.m_41777_())
            .inputItems(accelerationCard.m_41777_())
            .outputItems(GTLMachines.GTAEMachines.FLUID_EXPORT_HATCH_ME)
            .duration(300)
            .EUt(GTValues.VA[3])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_import_hatch", new Object[0])
            .inputItems(GTMachines.FLUID_IMPORT_HATCH[4])
            .inputItems(meInterface.m_41777_())
            .inputItems(accelerationCard.m_41777_())
            .outputItems(GTLMachines.GTAEMachines.FLUID_IMPORT_HATCH_ME)
            .duration(300)
            .EUt(GTValues.VA[3])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_export_bus", new Object[0])
            .inputItems(GTMachines.ITEM_EXPORT_BUS[4])
            .inputItems(meInterface.m_41777_())
            .inputItems(accelerationCard.m_41777_())
            .outputItems(GTLMachines.GTAEMachines.ITEM_EXPORT_BUS_ME)
            .duration(300)
            .EUt(GTValues.VA[3])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_import_bus", new Object[0])
            .inputItems(GTMachines.ITEM_IMPORT_BUS[4])
            .inputItems(meInterface.m_41777_())
            .inputItems(accelerationCard.m_41777_())
            .outputItems(GTLMachines.GTAEMachines.ITEM_IMPORT_BUS_ME)
            .duration(300)
            .EUt(GTValues.VA[3])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_stocking_import_bus", new Object[0])
            .inputItems(GTMachines.ITEM_IMPORT_BUS[5])
            .inputItems(meInterface.m_41777_())
            .inputItems(GTItems.CONVEYOR_MODULE_IV)
            .inputItems(GTItems.SENSOR_IV)
            .inputItems(accelerationCard.m_255036_(4))
            .outputItems(GTLMachines.GTAEMachines.STOCKING_IMPORT_BUS_ME)
            .duration(300)
            .EUt(GTValues.VA[5])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("me_stocking_import_hatch", new Object[0])
            .inputItems(GTMachines.FLUID_IMPORT_HATCH[5])
            .inputItems(meInterface.m_41777_())
            .inputItems(GTItems.ELECTRIC_PUMP_IV)
            .inputItems(GTItems.SENSOR_IV)
            .inputItems(accelerationCard.m_255036_(4))
            .outputItems(GTLMachines.GTAEMachines.STOCKING_IMPORT_HATCH_ME)
            .duration(300)
            .EUt(GTValues.VA[5])
            .save(provider);
         GTRecipeTypes.ASSEMBLY_LINE_RECIPES
            .recipeBuilder(GTLCore.id("me_mini_pattern_buffer"), new Object[0])
            .inputItems(CustomMachines.HUGE_INPUT_DUAL_HATCH[5], 1)
            .inputItems(GTItems.EMITTER_IV, 1)
            .inputItems(CustomTags.LuV_CIRCUITS, 4)
            .inputItems(AEBlocks.PATTERN_PROVIDER.m_5456_(), 3)
            .inputItems(AEBlocks.INTERFACE.m_5456_(), 3)
            .inputItems(AEItems.SPEED_CARD.m_5456_(), 3)
            .inputItems(AEItems.CAPACITY_CARD.m_5456_(), 4)
            .inputItems(TagPrefix.plateDouble, GTMaterials.Trinium, 2)
            .inputItems(TagPrefix.wireGtSingle, GTMaterials.UraniumTriplatinum, 24)
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(576L))
            .inputFluids(GTMaterials.Lubricant.getFluid(500L))
            .outputItems(GTLMachines.GTAEMachines.ME_MINI_PATTERN_BUFFER)
            .duration(600)
            .EUt(GTValues.VA[5])
            .scannerResearch(
               b -> (ScannerRecipeBuilder)((ScannerRecipeBuilder)b.researchStack(CustomMachines.HUGE_INPUT_DUAL_HATCH[5].asStack()))
                  .duration(600)
                  .EUt(GTValues.VA[5])
            )
            .save(provider);
         GTRecipeTypes.ASSEMBLY_LINE_RECIPES
            .recipeBuilder(GTLCore.id("me_extend_pattern_buffer"), new Object[0])
            .inputItems(CustomMachines.HUGE_INPUT_DUAL_HATCH[8], 1)
            .inputItems(GTItems.EMITTER_UV, 1)
            .inputItems(CustomTags.UHV_CIRCUITS, 4)
            .inputItems(AEBlocks.PATTERN_PROVIDER.m_5456_(), 3)
            .inputItems(AEBlocks.INTERFACE.m_5456_(), 3)
            .inputItems(AEItems.SPEED_CARD.m_5456_(), 6)
            .inputItems(AEItems.CAPACITY_CARD.m_5456_(), 4)
            .inputItems(TagPrefix.plate, GTMaterials.Neutronium, 8)
            .inputItems(TagPrefix.plate, GTMaterials.Neutronium, 8)
            .inputItems(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 32)
            .inputItems(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 32)
            .inputItems(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 32)
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(576L))
            .inputFluids(GTMaterials.Lubricant.getFluid(500L))
            .inputFluids(GTLMaterials.Highurabilityompoundteel.getFluid(576L))
            .outputItems(GTLMachines.GTAEMachines.ME_EXTEND_PATTERN_BUFFER)
            .stationResearch(b -> ((StationRecipeBuilder)b.researchStack(CustomMachines.HUGE_INPUT_DUAL_HATCH[8].asStack())).CWUt(96))
            .duration(600)
            .EUt(GTValues.VA[8])
            .save(provider);
         GTRecipeTypes.ASSEMBLY_LINE_RECIPES
            .recipeBuilder(GTLCore.id("me_final_pattern_buffer"), new Object[0])
            .inputItems(CustomMachines.HUGE_INPUT_DUAL_HATCH[10], 1)
            .inputItems(GTItems.EMITTER_UEV, 2)
            .inputItems(CustomTags.UIV_CIRCUITS, 4)
            .inputItems(AEBlocks.PATTERN_PROVIDER.m_5456_(), 3)
            .inputItems(AEBlocks.INTERFACE.m_5456_(), 3)
            .inputItems(AEItems.SPEED_CARD.m_5456_(), 6)
            .inputItems(AEItems.CAPACITY_CARD.m_5456_(), 4)
            .inputItems(TagPrefix.plate, GTLMaterials.DegenerateRhenium, 12)
            .inputItems(TagPrefix.plate, GTLMaterials.DegenerateRhenium, 12)
            .inputItems(TagPrefix.wireFine, GTLMaterials.Enderite, 48)
            .inputItems(TagPrefix.wireFine, GTLMaterials.Enderite, 48)
            .inputItems(TagPrefix.wireFine, GTLMaterials.Enderite, 48)
            .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(576L))
            .inputFluids(GTMaterials.Lubricant.getFluid(500L))
            .inputFluids(GTLMaterials.Zylon.getFluid(576L))
            .outputItems(GTLMachines.GTAEMachines.ME_FINAL_PATTERN_BUFFER)
            .stationResearch(b -> ((StationRecipeBuilder)b.researchStack(CustomMachines.HUGE_INPUT_DUAL_HATCH[10].asStack())).CWUt(288))
            .duration(600)
            .EUt(GTValues.VA[10])
            .save(provider);
         GTRecipeTypes.ASSEMBLY_LINE_RECIPES
            .recipeBuilder(GTLCore.id("me_pattern_buffer_proxy"), new Object[0])
            .inputItems(GTMachines.HULL[8], 1)
            .inputItems(GTItems.SENSOR_UV, 2)
            .inputItems(CustomTags.UHV_CIRCUITS, 1)
            .inputItems(Registries.getItemStack("kubejs:entangled_singularity", 2))
            .inputItems(AEBlocks.QUANTUM_LINK.m_5456_(), 1)
            .inputItems(AEBlocks.QUANTUM_RING.m_5456_(), 8)
            .inputItems(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 32)
            .inputItems(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 32)
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(576L))
            .inputFluids(GTMaterials.Lubricant.getFluid(500L))
            .outputItems(GTLMachines.GTAEMachines.ME_PATTERN_BUFFER_PROXY)
            .stationResearch(b -> ((StationRecipeBuilder)b.researchStack(GTMachines.DUAL_IMPORT_HATCH[8].asStack())).CWUt(48))
            .duration(600)
            .EUt(GTValues.VA[8])
            .save(provider);
         GTLRecipeTypes.PRECISION_ASSEMBLER_RECIPES
            .recipeBuilder(GTLCore.id("me_extended_export_buffer"), new Object[0])
            .inputItems(CustomMachines.ME_EXPORT_BUFFER, 4)
            .inputItems(GTBlocks.MACHINE_CASING_UHV, 4)
            .inputItems(capacityCard, 64)
            .inputItems(accelerationCard, 48)
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
            .inputFluids(GTLMaterials.Mithril.getFluid(576L))
            .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 576L))
            .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(576L))
            .outputItems(GTLMachines.GTAEMachines.ME_EXTENDED_EXPORT_BUFFER)
            .EUt(GTValues.VA[8])
            .duration(400)
            .cleanroom(CleanroomType.CLEANROOM)
            .save(provider);
         GTLRecipeTypes.PRECISION_ASSEMBLER_RECIPES
            .recipeBuilder(GTLCore.id("me_extended_async_export_buffer"), new Object[0])
            .inputItems(CustomMachines.ME_EXPORT_BUFFER, 4)
            .inputItems(GTBlocks.MACHINE_CASING_UHV, 4)
            .inputItems(capacityCard, 64)
            .inputItems(accelerationCard, 48)
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
            .inputFluids(GTLMaterials.Orichalcum.getFluid(576L))
            .inputFluids(GTLMaterials.AbyssalAlloy.getFluid(576L))
            .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(576L))
            .outputItems(GTLMachines.GTAEMachines.ME_EXTENDED_ASYNC_EXPORT_BUFFER)
            .EUt(GTValues.VA[8])
            .duration(400)
            .cleanroom(CleanroomType.CLEANROOM)
            .save(provider);
      }
   }
}
