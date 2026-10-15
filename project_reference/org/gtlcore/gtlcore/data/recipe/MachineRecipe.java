package org.gtlcore.gtlcore.data.recipe;

import appeng.core.definitions.AEItems;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.ResearchRecipeBuilder.StationRecipeBuilder;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.misc.MetaTileEntityLoader;
import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.data.CustomItems;
import com.hepdd.gtmthings.data.WirelessMachines;
import com.tterrag.registrate.util.entry.ItemEntry;
import earth.terrarium.adastra.common.registry.ModItems;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.Tags.Items;
import org.apache.commons.lang3.ArrayUtils;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.common.data.machines.AdditionalMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.GCyMMachines;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB;
import org.gtlcore.gtlcore.utils.Registries;

public class MachineRecipe {
   public static void init(Consumer<FinishedRecipe> provider) {
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "casing_uev",
         GTBlocks.MACHINE_CASING_UEV.asStack(),
         new Object[]{"PPP", "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTLMaterials.Quantanium)}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "casing_uiv",
         GTBlocks.MACHINE_CASING_UIV.asStack(),
         new Object[]{"PPP", "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTLMaterials.Adamantium)}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "casing_uxv",
         GTBlocks.MACHINE_CASING_UXV.asStack(),
         new Object[]{"PPP", "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTLMaterials.Vibranium)}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "casing_opv",
         GTBlocks.MACHINE_CASING_OpV.asStack(),
         new Object[]{"PPP", "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTLMaterials.Draconium)}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "casing_max",
         GTBlocks.MACHINE_CASING_MAX.asStack(),
         new Object[]{"PPP", "PwP", "PPP", 'P', new UnificationEntry(TagPrefix.plate, GTLMaterials.Chaos)}
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("casing_uev", new Object[0])
         .EUt(16L)
         .inputItems(TagPrefix.plate, GTLMaterials.Quantanium, 8)
         .outputItems(GTBlocks.MACHINE_CASING_UEV.asStack())
         .circuitMeta(8)
         .duration(50)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("casing_uiv", new Object[0])
         .EUt(16L)
         .inputItems(TagPrefix.plate, GTLMaterials.Adamantium, 8)
         .outputItems(GTBlocks.MACHINE_CASING_UIV.asStack())
         .circuitMeta(8)
         .duration(50)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("casing_uxv", new Object[0])
         .EUt(16L)
         .inputItems(TagPrefix.plate, GTLMaterials.Vibranium, 8)
         .outputItems(GTBlocks.MACHINE_CASING_UXV.asStack())
         .circuitMeta(8)
         .duration(50)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("casing_opv", new Object[0])
         .EUt(16L)
         .inputItems(TagPrefix.plate, GTLMaterials.Draconium, 8)
         .outputItems(GTBlocks.MACHINE_CASING_OpV.asStack())
         .circuitMeta(8)
         .duration(50)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("casing_max", new Object[0])
         .EUt(16L)
         .inputItems(TagPrefix.plate, GTLMaterials.Chaos, 8)
         .outputItems(GTBlocks.MACHINE_CASING_MAX.asStack())
         .circuitMeta(8)
         .duration(50)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("rotor_hatch_a", new Object[0])
         .inputItems(GTMachines.ITEM_IMPORT_BUS[4], 1)
         .inputItems(GTItems.COVER_ITEM_DETECTOR_ADVANCED)
         .inputItems(TagPrefix.pipeLargeRestrictive, GTMaterials.SterlingSilver, 4)
         .inputItems(GTItems.CONVEYOR_MODULE_EV, 16)
         .inputFluids(GTMaterials.Polyethylene.getFluid(144L))
         .outputItems(GTLMachines.ROTOR_HATCH)
         .duration(400)
         .EUt(480L)
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("adv", new Object[0])
         .inputItems(AdvancedMultiBlockMachine.FLUID_DRILLING_RIG[7])
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(10), 4)
         .inputItems(GTItems.CONVEYOR_MODULE_UHV, 4)
         .inputItems(GTItems.FLUID_REGULATOR_UHV, 4)
         .outputItems(AdvancedMultiBlockMachine.ADVANCED_INFINITE_DRILLER)
         .duration(400)
         .EUt(GTValues.V[10])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("block_bus", new Object[0])
         .inputItems(GTItems.CONVEYOR_MODULE_LuV, 2)
         .inputItems(GTItems.ROBOT_ARM_LuV, 2)
         .inputItems(GTItems.EMITTER_LuV, 2)
         .inputItems(GTMachines.ITEM_IMPORT_BUS[6], 1)
         .inputItems(GTItems.ITEM_FILTER)
         .inputItems(Registries.getBlock("kubejs:essence_block"))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(GTLMachines.BLOCK_BUS)
         .duration(200)
         .EUt(GTValues.V[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("wireless_data_hatch_transmitter", new Object[0])
         .inputItems(GTResearchMachines.DATA_HATCH_TRANSMITTER)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(6), 1)
         .inputItems(GTItems.EMITTER_LuV)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(288L))
         .outputItems(GTLMachines.WIRELESS_DATA_HATCH_TRANSMITTER)
         .duration(200)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("wireless_data_hatch_receiver", new Object[0])
         .inputItems(GTResearchMachines.DATA_HATCH_RECEIVER)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(6), 1)
         .inputItems(GTItems.EMITTER_LuV)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(288L))
         .outputItems(GTLMachines.WIRELESS_DATA_HATCH_RECEIVER)
         .duration(200)
         .EUt(GTValues.VA[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("conversion_simulate_card", new Object[0])
         .inputItems(GTItems.FIELD_GENERATOR_LuV)
         .inputItems(AEItems.SPEED_CARD, 2)
         .inputItems(AdvancedMultiBlockMachine.BLOCK_CONVERSION_ROOM, 1)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(6), 2)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(GTLItems.CONVERSION_SIMULATE_CARD)
         .duration(200)
         .EUt(GTValues.V[6])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_uhv", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_UHV)
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Europium, 2)
         .inputFluids(GTLMaterials.Polyetheretherketone.getFluid(288L))
         .outputItems(GTMachines.HULL[9])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_uev", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_UEV.asStack())
         .inputItems(TagPrefix.cableGtSingle, GTLMaterials.Mithril, 2)
         .inputFluids(GTLMaterials.Polyetheretherketone.getFluid(288L))
         .outputItems(GTMachines.HULL[10])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_uiv", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_UIV.asStack())
         .inputItems(TagPrefix.cableGtSingle, GTMaterials.Neutronium, 2)
         .inputFluids(GTLMaterials.Zylon.getFluid(288L))
         .outputItems(GTMachines.HULL[11])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_uxv", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_UXV.asStack())
         .inputItems(TagPrefix.cableGtSingle, GTLMaterials.Taranium, 2)
         .inputFluids(GTLMaterials.Zylon.getFluid(288L))
         .outputItems(GTMachines.HULL[12])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_opv", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_OpV.asStack())
         .inputItems(TagPrefix.cableGtSingle, GTLMaterials.Crystalmatrix, 2)
         .inputFluids(GTLMaterials.FullerenePolymerMatrixPulp.getFluid(288L))
         .outputItems(GTMachines.HULL[13])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("hull_max", new Object[0])
         .duration(50)
         .EUt(16L)
         .inputItems(GTBlocks.MACHINE_CASING_MAX.asStack())
         .inputItems(TagPrefix.cableGtSingle, GTLMaterials.CosmicNeutronium, 2)
         .inputFluids(GTLMaterials.Radox.getFluid(288L))
         .outputItems(GTMachines.HULL[14])
         .save(provider);
      Material[] multiHatchMaterials = new Material[]{
         GTMaterials.Neutronium, GTLMaterials.Enderium, GTLMaterials.Enderium, GTLMaterials.HeavyQuarkDegenerateMatter, GTLMaterials.HeavyQuarkDegenerateMatter
      };

      for (int i = 0; i < multiHatchMaterials.length; i++) {
         int tier = GTMachines.MULTI_HATCH_TIERS[i + 6];
         String tierName = GTValues.VN[tier].toLowerCase();
         Material material = multiHatchMaterials[i];
         MachineDefinition importHatch = GTMachines.FLUID_IMPORT_HATCH[tier];
         MachineDefinition exportHatch = GTMachines.FLUID_EXPORT_HATCH[tier];
         MachineDefinition importHatch4x = GTMachines.FLUID_IMPORT_HATCH_4X[tier];
         MachineDefinition exportHatch4x = GTMachines.FLUID_EXPORT_HATCH_4X[tier];
         MachineDefinition importHatch9x = GTMachines.FLUID_IMPORT_HATCH_9X[tier];
         MachineDefinition exportHatch9x = GTMachines.FLUID_EXPORT_HATCH_9X[tier];
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "fluid_import_hatch_4x_" + tierName,
            importHatch4x.asStack(),
            new Object[]{"P", "M", 'M', importHatch.asStack(), 'P', new UnificationEntry(TagPrefix.pipeQuadrupleFluid, material)}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "fluid_export_hatch_4x_" + tierName,
            exportHatch4x.asStack(),
            new Object[]{"M", "P", 'M', exportHatch.asStack(), 'P', new UnificationEntry(TagPrefix.pipeQuadrupleFluid, material)}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "fluid_import_hatch_9x_" + tierName,
            importHatch9x.asStack(),
            new Object[]{"P", "M", 'M', importHatch.asStack(), 'P', new UnificationEntry(TagPrefix.pipeNonupleFluid, material)}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "fluid_export_hatch_9x_" + tierName,
            exportHatch9x.asStack(),
            new Object[]{"M", "P", 'M', exportHatch.asStack(), 'P', new UnificationEntry(TagPrefix.pipeNonupleFluid, material)}
         );
      }

      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "rotor_holder_uhv",
         GTMachines.ROTOR_HOLDER[9].asStack(),
         new Object[]{
            "SGS",
            "GHG",
            "SGS",
            'H',
            GTMachines.HULL[9].asStack(),
            'G',
            new UnificationEntry(TagPrefix.gear, GTLMaterials.Orichalcum),
            'S',
            new UnificationEntry(TagPrefix.gearSmall, GTMaterials.Neutronium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "rotor_holder_uev",
         GTMachines.ROTOR_HOLDER[10].asStack(),
         new Object[]{
            "SGS",
            "GHG",
            "SGS",
            'H',
            GTMachines.HULL[10].asStack(),
            'G',
            new UnificationEntry(TagPrefix.gear, GTLMaterials.AstralTitanium),
            'S',
            new UnificationEntry(TagPrefix.gearSmall, GTLMaterials.Quantanium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         GTLCore.id("ultimate_terminal"),
         GTLItems.ULTIMATE_TERMINAL.asStack(),
         new Object[]{
            "ABA",
            "CDC",
            "CEC",
            'A',
            new UnificationEntry(TagPrefix.screw, GTMaterials.BlackBronze),
            'B',
            Items.GLASS_PANES,
            'C',
            new UnificationEntry(TagPrefix.plate, GTMaterials.Silicon),
            'D',
            CustomItems.ADVANCED_TERMINAL.asStack(),
            'E',
            CustomTags.MV_CIRCUITS
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         (MachineDefinition[])ArrayUtils.subarray(GTMachines.TRANSFORMER, 9, 14),
         new Object[]{
            "WCC",
            "TH ",
            "WCC",
            'W',
            CraftingComponent.POWER_COMPONENT,
            'C',
            CraftingComponent.CABLE,
            'T',
            CraftingComponent.CABLE_TIER_UP,
            'H',
            CraftingComponent.HULL
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         (MachineDefinition[])ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_2A, 9, 14),
         new Object[]{
            "WCC",
            "TH ",
            "WCC",
            'W',
            CraftingComponent.POWER_COMPONENT,
            'C',
            CraftingComponent.CABLE_DOUBLE,
            'T',
            CraftingComponent.CABLE_TIER_UP_DOUBLE,
            'H',
            CraftingComponent.HULL
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         (MachineDefinition[])ArrayUtils.subarray(GTMachines.HI_AMP_TRANSFORMER_4A, 9, 14),
         new Object[]{
            "WCC",
            "TH ",
            "WCC",
            'W',
            CraftingComponent.POWER_COMPONENT,
            'C',
            CraftingComponent.CABLE_QUAD,
            'T',
            CraftingComponent.CABLE_TIER_UP_QUAD,
            'H',
            CraftingComponent.HULL
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         GTLMachines.DEHYDRATOR,
         new Object[]{
            "WCW",
            "AMA",
            "PRP",
            'M',
            CraftingComponent.HULL,
            'P',
            CraftingComponent.PLATE,
            'C',
            CraftingComponent.CIRCUIT,
            'W',
            CraftingComponent.WIRE_QUAD,
            'R',
            CraftingComponent.ROBOT_ARM,
            'A',
            CraftingComponent.CABLE_QUAD
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         GTLMachines.LIGHTNING_PROCESSOR,
         new Object[]{
            "WEW",
            "AMA",
            "WSW",
            'M',
            CraftingComponent.HULL,
            'E',
            CraftingComponent.EMITTER,
            'W',
            CraftingComponent.WIRE_HEX,
            'S',
            CraftingComponent.SENSOR,
            'A',
            CraftingComponent.CABLE_TIER_UP
         }
      );
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("zpm_fluid_drilling_rig", new Object[0])
         .inputItems(GTMachines.HULL[8])
         .inputItems(TagPrefix.frameGt, GTMaterials.Ruridit, 4)
         .inputItems(CustomTags.UV_CIRCUITS, 4)
         .inputItems(GTItems.ELECTRIC_MOTOR_UV, 4)
         .inputItems(GTItems.ELECTRIC_PUMP_UV, 4)
         .inputItems(TagPrefix.gear, GTMaterials.Neutronium, 4)
         .circuitMeta(2)
         .outputItems(AdvancedMultiBlockMachine.FLUID_DRILLING_RIG[7])
         .duration(400)
         .EUt(GTValues.VA[8])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder("wood_distillation", new Object[0])
         .inputItems(MultiBlockMachineA.LARGE_PYROLYSE_OVEN, 2)
         .inputItems(GCyMMachines.LARGE_DISTILLERY, 4)
         .inputItems(CustomTags.LuV_CIRCUITS, 16)
         .inputItems(GTItems.EMITTER_LuV, 4)
         .inputItems(TagPrefix.pipeHugeFluid, GTMaterials.StainlessSteel, 8)
         .inputItems(GTItems.ELECTRIC_PUMP_IV, 8)
         .inputItems(GTMaterials.WatertightSteel, 16)
         .inputItems(TagPrefix.plateDouble, GTMaterials.StainlessSteel, 32)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(1296L))
         .outputItems(MultiBlockMachineB.WOOD_DISTILLATION)
         .duration(400)
         .EUt(GTValues.VA[6])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "generator_array",
         GeneratorMachine.GENERATOR_ARRAY.asStack(),
         new Object[]{
            "ABA", "BCB", "ABA", 'A', new UnificationEntry(TagPrefix.plate, GTMaterials.Steel), 'B', CustomTags.LV_CIRCUITS, 'C', GTItems.EMITTER_LV.asStack()
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider, GTMachines.FLUID_IMPORT_HATCH, new Object[]{" G", " M", 'M', CraftingComponent.HULL, 'G', CraftingComponent.GLASS}
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider, GTMachines.FLUID_EXPORT_HATCH, new Object[]{" M", " G", 'M', CraftingComponent.HULL, 'G', CraftingComponent.GLASS}
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider, GTMachines.ITEM_IMPORT_BUS, new Object[]{" C", " M", 'M', CraftingComponent.HULL, 'C', TagUtil.createItemTag("chests/wooden")}
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider, GTMachines.ITEM_EXPORT_BUS, new Object[]{" M", " C", 'M', CraftingComponent.HULL, 'C', TagUtil.createItemTag("chests/wooden")}
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         GTMachines.DUAL_IMPORT_HATCH,
         new Object[]{
            "PG", "CM", 'P', CraftingComponent.PIPE_NONUPLE, 'M', CraftingComponent.HULL, 'G', CraftingComponent.GLASS, 'C', CraftingComponentAddition.BUFFER
         }
      );
      MetaTileEntityLoader.registerMachineRecipe(
         provider,
         GTMachines.DUAL_EXPORT_HATCH,
         new Object[]{
            "MG", "CP", 'P', CraftingComponent.PIPE_NONUPLE, 'M', CraftingComponent.HULL, 'G', CraftingComponent.GLASS, 'C', CraftingComponentAddition.BUFFER
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "hermetic_casing_uev",
         GTLBlocks.HERMETIC_CASING_UEV.asStack(),
         new Object[]{
            "PPP",
            "PFP",
            "PPP",
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Quantanium),
            'F',
            new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "hermetic_casing_uiv",
         GTLBlocks.HERMETIC_CASING_UIV.asStack(),
         new Object[]{
            "PPP",
            "PFP",
            "PPP",
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Adamantium),
            'F',
            new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "hermetic_casing_uxv",
         GTLBlocks.HERMETIC_CASING_UXV.asStack(),
         new Object[]{
            "PPP",
            "PFP",
            "PPP",
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Vibranium),
            'F',
            new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.Enderium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "hermetic_casing_opv",
         GTLBlocks.HERMETIC_CASING_OpV.asStack(),
         new Object[]{
            "PPP",
            "PFP",
            "PPP",
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Draconium),
            'F',
            new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.HeavyQuarkDegenerateMatter)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_tank_uev",
         GTMachines.QUANTUM_TANK[10].asStack(),
         new Object[]{
            "CGC",
            "PHP",
            "CUC",
            'C',
            CustomTags.UEV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Quantanium),
            'U',
            GTItems.ELECTRIC_PUMP_UHV.asStack(),
            'G',
            GTItems.FIELD_GENERATOR_UV.asStack(),
            'H',
            GTLBlocks.HERMETIC_CASING_UEV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_tank_uiv",
         GTMachines.QUANTUM_TANK[11].asStack(),
         new Object[]{
            "CGC",
            "PHP",
            "CUC",
            'C',
            CustomTags.UIV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Adamantium),
            'U',
            GTItems.ELECTRIC_PUMP_UEV.asStack(),
            'G',
            GTItems.FIELD_GENERATOR_UHV.asStack(),
            'H',
            GTLBlocks.HERMETIC_CASING_UIV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_tank_uxv",
         GTMachines.QUANTUM_TANK[12].asStack(),
         new Object[]{
            "CGC",
            "PHP",
            "CUC",
            'C',
            CustomTags.UXV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Vibranium),
            'U',
            GTItems.ELECTRIC_PUMP_UIV.asStack(),
            'G',
            GTItems.FIELD_GENERATOR_UEV.asStack(),
            'H',
            GTLBlocks.HERMETIC_CASING_UXV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_tank_opv",
         GTMachines.QUANTUM_TANK[13].asStack(),
         new Object[]{
            "CGC",
            "PHP",
            "CUC",
            'C',
            CustomTags.OpV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Draconium),
            'U',
            GTItems.ELECTRIC_PUMP_UXV.asStack(),
            'G',
            GTItems.FIELD_GENERATOR_UIV.asStack(),
            'H',
            GTLBlocks.HERMETIC_CASING_OpV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_chest_uev",
         GTMachines.QUANTUM_CHEST[10].asStack(),
         new Object[]{
            "CPC",
            "PHP",
            "CFC",
            'C',
            CustomTags.UEV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Quantanium),
            'F',
            GTItems.FIELD_GENERATOR_UV.asStack(),
            'H',
            GTMachines.HULL[10].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_chest_uiv",
         GTMachines.QUANTUM_CHEST[11].asStack(),
         new Object[]{
            "CPC",
            "PHP",
            "CFC",
            'C',
            CustomTags.UIV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Adamantium),
            'F',
            GTItems.FIELD_GENERATOR_UHV.asStack(),
            'H',
            GTMachines.HULL[11].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_chest_uxv",
         GTMachines.QUANTUM_CHEST[12].asStack(),
         new Object[]{
            "CPC",
            "PHP",
            "CFC",
            'C',
            CustomTags.UXV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Vibranium),
            'F',
            GTItems.FIELD_GENERATOR_UEV.asStack(),
            'H',
            GTMachines.HULL[12].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "quantum_chest_opv",
         GTMachines.QUANTUM_CHEST[13].asStack(),
         new Object[]{
            "CPC",
            "PHP",
            "CFC",
            'C',
            CustomTags.OpV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTLMaterials.Draconium),
            'F',
            GTItems.FIELD_GENERATOR_UIV.asStack(),
            'H',
            GTMachines.HULL[13].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_block_conversion_room",
         AdvancedMultiBlockMachine.LARGE_BLOCK_CONVERSION_ROOM.asStack(),
         new Object[]{
            "SES",
            "EHE",
            "SES",
            'S',
            GTItems.SENSOR_ZPM.asStack(),
            'E',
            GTItems.EMITTER_ZPM.asStack(),
            'H',
            AdvancedMultiBlockMachine.BLOCK_CONVERSION_ROOM.asStack()
         }
      );
      List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER = List.of(
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV,
         GTLItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX
      );
      List<ItemEntry<ComponentItem>> WIRELESS_ENERGY_RECEIVE_COVER_4A = List.of(
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_MV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_HV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_EV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_IV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_LUV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_ZPM_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UHV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UEV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UIV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_UXV_4A,
         CustomItems.WIRELESS_ENERGY_RECEIVE_COVER_OPV_4A,
         GTLItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A
      );

      for (int tier : GTValues.tiersBetween(1, 3)) {
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_4a"), new Object[0])
            .inputItems(GTMachines.ENERGY_INPUT_HATCH_4A[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack(2))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_4A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_16a"), new Object[0])
            .inputItems(GTMachines.ENERGY_INPUT_HATCH_16A[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_16A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_4a"), new Object[0])
            .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_4A[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(tier - 1).asStack(2))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_16a"), new Object[0])
            .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_16A[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(2))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_16A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
      }

      for (int tier : GTValues.tiersBetween(4, 14)) {
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[tier].toLowerCase() + "_64a"), new Object[0])
            .inputItems(GTMachines.SUBSTATION_ENERGY_INPUT_HATCH[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(4))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(GTLMachines.WIRELESS_ENERGY_INPUT_HATCH_64A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
         GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[tier].toLowerCase() + "_64a"), new Object[0])
            .inputItems(GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH[tier].asStack())
            .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(tier - 1).asStack(4))
            .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
            .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
            .outputItems(GTLMachines.WIRELESS_ENERGY_OUTPUT_HATCH_64A[tier].asStack())
            .duration(200)
            .EUt(GTValues.VA[tier])
            .save(provider);
      }

      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase()), new Object[0])
         .inputItems(GTMachines.ENERGY_INPUT_HATCH[14].asStack())
         .inputItems(GTLItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase()), new Object[0])
         .inputItems(GTMachines.ENERGY_OUTPUT_HATCH[14].asStack())
         .inputItems(GTLItems.WIRELESS_ENERGY_RECEIVE_COVER_MAX.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack())
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase() + "_4a"), new Object[0])
         .inputItems(GTMachines.ENERGY_INPUT_HATCH_4A[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(13).asStack(2))
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_4A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase() + "_16a"), new Object[0])
         .inputItems(GTMachines.ENERGY_INPUT_HATCH_16A[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(2))
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_16A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase() + "_4a"), new Object[0])
         .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_4A[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER.get(13).asStack(2))
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase() + "_16a"), new Object[0])
         .inputItems(GTMachines.ENERGY_OUTPUT_HATCH_16A[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(2))
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_16A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase() + "_256a"), new Object[0])
         .inputItems(GTMachines.LASER_INPUT_HATCH_256[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(4))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_256A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase() + "_1024a"), new Object[0])
         .inputItems(GTMachines.LASER_INPUT_HATCH_1024[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(8))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_1024A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_input_hatch_" + GTValues.VN[14].toLowerCase() + "_4096a"), new Object[0])
         .inputItems(GTMachines.LASER_INPUT_HATCH_4096[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(16))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_INPUT_HATCH_4096A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase() + "_256a"), new Object[0])
         .inputItems(GTMachines.LASER_OUTPUT_HATCH_256[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(4))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_256A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase() + "_1024a"), new Object[0])
         .inputItems(GTMachines.LASER_OUTPUT_HATCH_1024[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(8))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_1024A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTMThings.id("wireless_energy_output_hatch_" + GTValues.VN[14].toLowerCase() + "_4096a"), new Object[0])
         .inputItems(GTMachines.LASER_OUTPUT_HATCH_4096[14].asStack())
         .inputItems(WIRELESS_ENERGY_RECEIVE_COVER_4A.get(13).asStack(16))
         .inputItems(GTMachines.ACTIVE_TRANSFORMER.asStack())
         .inputItems(GTBlocks.SUPERCONDUCTING_COIL.asStack())
         .inputItems(GTItems.COVER_ENERGY_DETECTOR_ADVANCED.asStack(1))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(144L))
         .outputItems(WirelessMachines.WIRELESS_ENERGY_OUTPUT_HATCH_4096A[14].asStack())
         .duration(200)
         .EUt(GTValues.VA[14])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTLCore.id("heat_sensor"), new Object[0])
         .inputItems(GTItems.SENSOR_UXV, 4)
         .inputItems(GTItems.EMITTER_UXV, 4)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(12), 16)
         .inputItems(CraftingComponent.HULL.getIngredient(12), 1)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
         .outputItems(GTLMachines.HEAT_SENSOR)
         .duration(200)
         .EUt(GTValues.VA[12])
         .save(provider);
      GTRecipeTypes.ASSEMBLER_RECIPES
         .recipeBuilder(GTLCore.id("heat_sensor"), new Object[0])
         .inputItems(GTLMachines.GTAEMachines.STOCKING_IMPORT_BUS_ME, 2)
         .inputItems(GTLMachines.GTAEMachines.STOCKING_IMPORT_HATCH_ME, 2)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(5))
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(1440L))
         .outputItems(GTLMachines.ME_DUAL_HATCH_STOCK_PART_MACHINE)
         .duration(200)
         .EUt(GTValues.VA[4])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder(GTLCore.id("space_cosmic_probe_receivers"), new Object[0])
         .inputItems(GTItems.EMITTER_OpV, 16)
         .inputItems(GTItems.SENSOR_OpV, 16)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(14), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.rod, GTLMaterials.Infinity), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDouble, GTLMaterials.Starmetal), 32)
         .inputItems(AdvancedMultiBlockMachine.SPACE_PROBE_SURFACE_RECEPTION, 8)
         .inputItems(GTLBlocks.ADVANCED_FUSION_COIL, 16)
         .inputItems(Registries.getItem("kubejs:awakened_core"), 16)
         .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.BlackDwarfMatter), 64)
         .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.WhiteDwarfMatter), 64)
         .inputItems(GTLItems.INSANELY_ULTIMATE_BATTERY)
         .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(32000L))
         .inputFluids(GTLMaterials.Periodicium.getFluid(16000L))
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(
                        AdvancedMultiBlockMachine.SPACE_PROBE_SURFACE_RECEPTION.asStack()
                     ))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[12]))
               .CWUt(2048)
         )
         .outputItems(AdvancedMultiBlockMachine.SPACE_COSMIC_PROBE_RECEIVERS)
         .duration(2000)
         .EUt(GTValues.VA[13])
         .save(provider);
      GTLRecipeTypes.ASSEMBLER_MODULE_RECIPES
         .recipeBuilder("fast_conversion_simulate_card", new Object[0])
         .inputItems(GTItems.FIELD_GENERATOR_UIV, 4)
         .inputItems(GTItems.EMITTER_UIV, 4)
         .inputItems(GTItems.SENSOR_UIV, 4)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(11), 8)
         .inputItems(GTLItems.CONVERSION_SIMULATE_CARD)
         .inputFluids(GTLMaterials.CosmicSuperconductor.getFluid(16000L))
         .inputFluids(GTLMaterials.Periodicium.getFluid(16000L))
         .inputFluids(GTLMaterials.HeavyLeptonMixture.getFluid(FluidStorageKeys.GAS, 8000L))
         .inputFluids(GTLMaterials.Legendarium.getFluid(8000L))
         .outputItems(GTLItems.FAST_CONVERSION_SIMULATE_CARD)
         .addData("SEPMTier", 5)
         .duration(200)
         .EUt(GTValues.V[12])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("advanced_rare_earth_centrifugal", new Object[0])
         .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTMaterials.Neutronium), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDense, GTMaterials.TungstenSteel), 9)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDouble, GTLMaterials.Enderium), 9)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDouble, GTLMaterials.Echoite), 16)
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(12), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDense, GTMaterials.NaquadahAlloy), 9)
         .inputItems(ChemicalHelper.get(TagPrefix.pipeLargeItem, GTMaterials.Osmiridium), 8)
         .inputItems(ChemicalHelper.get(TagPrefix.rod, GTLMaterials.AttunedTengam), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.cableGtHex, GTLMaterials.Mithril), 16)
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(32000L))
         .inputFluids(GTLMaterials.FallKing.getFluid(24000L))
         .inputFluids(GTLMaterials.Quantum.getFluid(32000L))
         .outputItems(AdditionalMultiBlockMachine.ADVANCED_RARE_EARTH_CENTRIFUGAL)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(MultiBlockMachineA.RARE_EARTH_CENTRIFUGAL.asStack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[12]))
               .CWUt(2048)
         )
         .addData("SEPMTier", 5)
         .duration(200)
         .EUt(GTValues.V[12])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("advanced_vacuum_drying_furnace", new Object[0])
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(11), 32)
         .inputItems(GTBlocks.HIGH_POWER_CASING, 16)
         .inputItems(ModItems.CALORITE_PLATE, 16)
         .inputItems(MultiBlockMachineA.VACUUM_DRYING_FURNACE, 64)
         .inputItems(ChemicalHelper.get(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium), 4)
         .inputItems(GTItems.ELECTRIC_PUMP_UEV, 64)
         .inputItems(GTItems.FIELD_GENERATOR_UEV, 64)
         .inputItems(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTMaterials.Iron), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDouble, GTMaterials.RedSteel), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDense, GTMaterials.Steel), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.plateDense, GTMaterials.RhodiumPlatedPalladium), 16)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(16000L))
         .inputFluids(GTLMaterials.HastelloyX78.getFluid(8000L))
         .inputFluids(GTLMaterials.Quantum.getFluid(16000L))
         .outputItems(AdditionalMultiBlockMachine.ADVANCED_VACUUM_DRYING_FURNACE)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(MultiBlockMachineA.VACUUM_DRYING_FURNACE.asStack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[11]))
               .CWUt(256)
         )
         .addData("SEPMTier", 2)
         .duration(200)
         .EUt(GTValues.V[11])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("huge_incubator", new Object[0])
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(11), 32)
         .inputItems(MultiBlockMachineA.LARGE_INCUBATOR, 4)
         .inputItems(GTLItems.STERILIZED_PETRI_DISH, 16)
         .inputItems(ChemicalHelper.get(TagPrefix.gear, GTMaterials.Neutronium), 64)
         .inputItems(GTItems.ROBOT_ARM_UIV, 16)
         .inputItems(GTItems.FLUID_REGULATOR_UIV, 16)
         .inputItems(GTItems.FIELD_GENERATOR_UIV, 16)
         .inputItems(GTItems.EMITTER_UIV, 16)
         .inputItems(GTItems.SENSOR_UIV, 16)
         .inputItems(ChemicalHelper.get(TagPrefix.plate, GTLMaterials.DegenerateRhenium), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.gearSmall, GTLMaterials.Vibramantium), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.rod, GTLMaterials.Vibramantium), 16)
         .inputItems(ChemicalHelper.get(TagPrefix.screw, GTLMaterials.Vibramantium), 16)
         .inputItems(Registries.getItem("kubejs:x_ray_laser"), 1)
         .inputFluids(GTMaterials.SolderingAlloy.getFluid(16000L))
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(16000L))
         .inputFluids(GTLMaterials.Photopolymer.getFluid(8000L))
         .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(8000L))
         .outputItems(AdditionalMultiBlockMachine.HUGE_INCUBATOR)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(MultiBlockMachineA.LARGE_INCUBATOR.asStack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[11]))
               .CWUt(256)
         )
         .duration(200)
         .EUt(GTValues.V[11])
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("advanced_neutron_activator", new Object[0])
         .inputItems(CraftingComponent.CIRCUIT.getIngredient(12), 32)
         .inputItems(ChemicalHelper.get(TagPrefix.frameGt, GTLMaterials.BlackTitanium), 32)
         .inputItems(AdvancedMultiBlockMachine.NEUTRON_ACTIVATOR, 16)
         .inputItems(GTLMachines.NEUTRON_COMPRESSOR, 16)
         .inputItems(GTLMachines.NEUTRON_ACCELERATOR[9], 8)
         .inputItems(GTLMachines.NEUTRON_ACCELERATOR[10], 8)
         .inputItems(GTLMachines.NEUTRON_ACCELERATOR[11], 8)
         .inputItems(GTLItems.EXTREMELY_ULTIMATE_BATTERY)
         .inputItems(Registries.getItem("kubejs:wyvern_core"), 4)
         .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(16000L))
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(16000L))
         .inputFluids(GTMaterials.Oganesson.getFluid(1296L))
         .inputFluids(GTLMaterials.Vibranium.getFluid(1296L))
         .outputItems(AdditionalMultiBlockMachine.ADVANCED_NEUTRON_ACTIVATOR)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(AdvancedMultiBlockMachine.NEUTRON_ACTIVATOR.asStack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[12]))
               .CWUt(512)
         )
         .duration(400)
         .EUt(GTValues.V[12])
         .save(provider);
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         GTLCore.id("performance_monitor"),
         GTLMachines.PERFORMANCE_MONITOR.asStack(),
         new Object[]{"AAA", "ABA", "AAA", 'B', GTItems.PORTABLE_SCANNER.asStack(), 'A', TagPrefix.plate, GTMaterials.Steel}
      );
      space_probe(GTLMaterials.Starlight, 2, 1000, 2, provider);
      space_probe(GTLMaterials.Starlight, 3, 10000, 2, provider);
      space_probe(GTLMaterials.HeavyLeptonMixture, 1, 100, 1, provider);
      space_probe(GTLMaterials.HeavyLeptonMixture, 2, 1000, 1, provider);
      space_probe(GTLMaterials.HeavyLeptonMixture, 3, 10000, 1, provider);
      space_probe(GTLMaterials.CosmicElement, 3, 10000, 3, provider);
   }

   private static void space_probe(Material material, int grade, int amount, int circuit, Consumer<FinishedRecipe> provider) {
      GTLRecipeTypes.SPACE_COSMIC_PROBE_RECEIVERS_RECIPES
         .recipeBuilder(GTLCore.id("space_cosmic_probe_receivers_" + material.getName()), new Object[0])
         .notConsumable(Registries.getItem("kubejs:space_probe_mk" + grade))
         .outputFluids(material.getFluid(amount))
         .EUt((long)(GTValues.VA[10] * Math.pow(4.0, grade)))
         .duration(200)
         .circuitMeta(circuit)
         .CWUt(grade)
         .save(provider);
   }
}
