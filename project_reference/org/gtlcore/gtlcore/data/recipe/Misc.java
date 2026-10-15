package org.gtlcore.gtlcore.data.recipe;

import appeng.api.util.AEColor;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.glodblock.github.extendedae.common.EPPItemAndBlock;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.recipe.ResearchRecipeBuilder.StationRecipeBuilder;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.common.data.machines.AdditionalMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB;
import org.gtlcore.gtlcore.common.recipe.condition.GravityCondition;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.Registries;

public class Misc {
   public static void init(Consumer<FinishedRecipe> provider) {
      VanillaRecipeHelper.addShapelessRecipe(provider, "programmed_circuit", IntCircuitBehaviour.stack(0), new Object[]{"A", CustomTags.LV_CIRCUITS});
      VanillaRecipeHelper.addShapelessRecipe(provider, "structure_detect", GTLItems.STRUCTURE_DETECT.asStack(), new Object[]{"A", Items.f_42516_});
      VanillaRecipeHelper.addShapelessRecipe(
         provider, GTLCore.id("me_pattern_buffer_copy"), GTLItems.ME_PATTERN_BUFFER_COPY.asStack(), new Object[]{"A", AEItems.MEMORY_CARD}
      );
      VanillaRecipeHelper.addShapelessRecipe(
         provider, GTLCore.id("me_pattern_buffer_cut"), GTLItems.ME_PATTERN_BUFFER_CUT.asStack(), new Object[]{"A", GTLItems.ME_PATTERN_BUFFER_COPY.asStack()}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         GTLCore.id("heat_sensor"),
         GTLMachines.HEAT_SENSOR.asStack(),
         new Object[]{
            "FDF",
            "BAB",
            "CEC",
            'A',
            GTMachines.HULL[12].asStack(),
            'B',
            new UnificationEntry(TagPrefix.wireFine, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide),
            'C',
            GTItems.SENSOR_ZPM.asStack(),
            'D',
            Registries.getItemStack("gtceu:advanced_activity_detector_cover"),
            'E',
            Registries.getItemStack("gtceu:computer_monitor_cover"),
            'F',
            CustomTags.UV_CIRCUITS
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         GTLCore.id("fast_infinity_cell_0"),
         GTLItems.FAST_INFINITY_CELL.asStack(),
         new Object[]{
            "AAA",
            "BCD",
            "AAA",
            'A',
            AEItems.QUANTUM_ENTANGLED_SINGULARITY.stack(),
            'B',
            GTLItems.ITEM_INFINITY_CELL.asStack(),
            'C',
            GTItems.ZERO_POINT_MODULE.asStack(),
            'D',
            GTLItems.FLUID_INFINITY_CELL.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         GTLCore.id("fast_infinity_cell_1"),
         GTLItems.FAST_INFINITY_CELL.asStack(),
         new Object[]{
            "AAA",
            "DCB",
            "AAA",
            'A',
            AEItems.QUANTUM_ENTANGLED_SINGULARITY.stack(),
            'B',
            GTLItems.ITEM_INFINITY_CELL.asStack(),
            'C',
            GTItems.ZERO_POINT_MODULE.asStack(),
            'D',
            GTLItems.FLUID_INFINITY_CELL.asStack()
         }
      );
      if (ConfigHolder.INSTANCE.enablePrimitiveVoidOre) {
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "primitive_void_ore_recipes",
            MultiBlockMachineB.PRIMITIVE_VOID_ORE.asStack(),
            new Object[]{
               "DCD", "CGC", "DCD", 'D', Blocks.f_50493_.m_5456_(), 'C', Items.f_42427_.m_5456_(), 'G', new UnificationEntry(TagPrefix.block, GTMaterials.Iron)
            }
         );
         GTLRecipeTypes.PRIMITIVE_VOID_ORE_RECIPES
            .recipeBuilder("primitive_void_ore_recipes", new Object[0])
            .inputFluids(GTMaterials.Steam.getFluid(1000L))
            .duration(200)
            .save(provider);
      }

      VanillaRecipeHelper.addShapelessRecipe(
         provider, "simulation_machine", AdvancedMultiBlockMachine.SIMULATION_MACHINE.asStack(), new Object[]{"A", Blocks.f_50652_}
      );
      GTLRecipeTypes.WOOD_DISTILLATION_RECIPES
         .recipeBuilder("wood_distillation_recipes", new Object[0])
         .inputItems(ItemTags.f_13182_, 16)
         .inputFluids(GTMaterials.Nitrogen.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.DarkAsh, 8)
         .outputFluids(GTMaterials.Water.getFluid(800L))
         .outputFluids(GTMaterials.Carbon.getFluid(490L))
         .outputFluids(GTMaterials.Methanol.getFluid(480L))
         .outputFluids(GTMaterials.Benzene.getFluid(350L))
         .outputFluids(GTMaterials.CarbonMonoxide.getFluid(340L))
         .outputFluids(GTMaterials.Creosote.getFluid(300L))
         .outputFluids(GTMaterials.Dimethylbenzene.getFluid(240L))
         .outputFluids(GTMaterials.AceticAcid.getFluid(160L))
         .outputFluids(GTMaterials.Methane.getFluid(130L))
         .outputFluids(GTMaterials.Acetone.getFluid(80L))
         .outputFluids(GTMaterials.Phenol.getFluid(75L))
         .outputFluids(GTMaterials.Toluene.getFluid(75L))
         .outputFluids(GTMaterials.Ethylene.getFluid(20L))
         .outputFluids(GTMaterials.Hydrogen.getFluid(20L))
         .outputFluids(GTMaterials.MethylAcetate.getFluid(16L))
         .outputFluids(GTMaterials.Ethanol.getFluid(16L))
         .duration(200)
         .EUt(GTValues.VA[2])
         .save(provider);
      GTRecipeTypes.AUTOCLAVE_RECIPES
         .recipeBuilder("water_agar_mix", new Object[0])
         .EUt(GTValues.VA[3])
         .duration(600)
         .inputItems(TagPrefix.dust, GTMaterials.Gelatin)
         .inputFluids(GTMaterials.DistilledWater.getFluid(1000L))
         .outputFluids(GTLMaterials.WaterAgarMix.getFluid(1000L))
         .cleanroom(CleanroomType.STERILE_CLEANROOM)
         .save(provider);
      GTLRecipeTypes.DEHYDRATOR_RECIPES
         .recipeBuilder("agar", new Object[0])
         .inputFluids(GTLMaterials.WaterAgarMix.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.Agar, 1)
         .duration(420)
         .EUt(GTValues.VA[2])
         .cleanroom(CleanroomType.STERILE_CLEANROOM)
         .save(provider);
      GTRecipeTypes.CHEMICAL_RECIPES
         .recipeBuilder("soda_ash_from_carbon_dioxide", new Object[0])
         .circuitMeta(2)
         .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 6)
         .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000L))
         .outputItems(TagPrefix.dust, GTMaterials.SodaAsh, 6)
         .outputFluids(GTMaterials.Water.getFluid(1000L))
         .duration(80)
         .EUt(GTValues.VA[3])
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("engraved_crystal_chip_from_emerald", new Object[0])
         .inputItems(TagPrefix.plate, GTMaterials.Emerald)
         .inputItems(GTItems.RAW_CRYSTAL_CHIP)
         .inputFluids(GTMaterials.Helium.getFluid(1000L))
         .outputItems(GTItems.ENGRAVED_CRYSTAL_CHIP)
         .blastFurnaceTemp(5000)
         .duration(900)
         .EUt(GTValues.VA[3])
         .addCondition(new GravityCondition(true))
         .save(provider);
      GTRecipeTypes.BLAST_RECIPES
         .recipeBuilder("engraved_crystal_chip_from_olivine", new Object[0])
         .inputItems(TagPrefix.plate, GTMaterials.Olivine)
         .inputItems(GTItems.RAW_CRYSTAL_CHIP)
         .inputFluids(GTMaterials.Helium.getFluid(1000L))
         .outputItems(GTItems.ENGRAVED_CRYSTAL_CHIP)
         .blastFurnaceTemp(5000)
         .duration(900)
         .EUt(GTValues.VA[3])
         .addCondition(new GravityCondition(true))
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("quantum_star", new Object[0])
         .inputItems(TagPrefix.gem, GTMaterials.NetherStar)
         .inputFluids(GTMaterials.Radon.getFluid(1250L))
         .outputItems(GTItems.QUANTUM_STAR)
         .duration(1920)
         .EUt(GTValues.VA[3])
         .addCondition(new GravityCondition(true))
         .save(provider);
      GTRecipeTypes.AUTOCLAVE_RECIPES
         .recipeBuilder("gravi_star", new Object[0])
         .inputItems(GTItems.QUANTUM_STAR)
         .inputFluids(GTMaterials.Neutronium.getFluid(288L))
         .outputItems(GTItems.GRAVI_STAR)
         .duration(480)
         .EUt(GTValues.VA[5])
         .addCondition(new GravityCondition(true))
         .save(provider);
      GTRecipeTypes.CHEMICAL_BATH_RECIPES
         .recipeBuilder("quantum_eye", new Object[0])
         .inputItems(TagPrefix.gem, GTMaterials.EnderEye)
         .inputFluids(GTMaterials.Radon.getFluid(250L))
         .outputItems(GTItems.QUANTUM_EYE)
         .duration(480)
         .EUt(GTValues.VA[3])
         .addCondition(new GravityCondition(true))
         .save(provider);
      GTLRecipeTypes.LIGHTNING_PROCESSOR_RECIPES
         .recipeBuilder("ender_pearl_dust", new Object[0])
         .duration(400)
         .EUt(GTValues.VA[1])
         .inputItems(TagPrefix.dust, GTMaterials.Beryllium)
         .inputItems(TagPrefix.dust, GTMaterials.Potassium, 4)
         .inputFluids(GTMaterials.Nitrogen.getFluid(5000L))
         .circuitMeta(1)
         .outputItems(TagPrefix.dust, GTMaterials.EnderPearl, 10)
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("molecular_assembler_matrix", new Object[0])
         .inputItems(TagPrefix.frameGt, GTMaterials.NaquadahAlloy, 16)
         .inputItems(GTItems.FIELD_GENERATOR_UV, 8)
         .inputItems(GTItems.ULTIMATE_BATTERY)
         .inputItems(CustomTags.UHV_CIRCUITS, 16)
         .inputItems(TagPrefix.wireFine, GTMaterials.RutheniumTriniumAmericiumNeutronate, 64)
         .inputItems(TagPrefix.wireFine, GTMaterials.RutheniumTriniumAmericiumNeutronate, 64)
         .inputItems(TagPrefix.plate, GTLMaterials.AbyssalAlloy, 16)
         .inputItems(EPPItemAndBlock.EX_INTERFACE.m_5456_(), 8)
         .inputItems(EPPItemAndBlock.INGREDIENT_BUFFER.m_5456_(), 8)
         .inputItems(EPPItemAndBlock.EX_IO_PORT.m_5456_(), 8)
         .inputItems(EPPItemAndBlock.EX_PATTERN_PROVIDER.m_5456_(), 8)
         .inputItems(EPPItemAndBlock.EX_ASSEMBLER.m_5456_(), 8)
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(2304L))
         .inputFluids(GTLMaterials.Highurabilityompoundteel.getFluid(1728L))
         .inputFluids(GTLMaterials.Antimatter.getFluid(4000L))
         .inputFluids(GTLMaterials.Polyetheretherketone.getFluid(2340L))
         .outputItems(AdditionalMultiBlockMachine.MOLECULAR_ASSEMBLER_MATRIX)
         .EUt(GTValues.VA[9])
         .duration(200)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(EPPItemAndBlock.EX_ASSEMBLER.m_5456_().m_7968_()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[9]))
               .CWUt(256)
         )
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("me_molecular_assembler_io", new Object[0])
         .inputItems(TagPrefix.frameGt, GTMaterials.Neutronium, 16)
         .inputItems(GTItems.SENSOR_UV, 8)
         .inputItems(GTItems.EMITTER_UV, 8)
         .inputItems(CustomTags.UHV_CIRCUITS, 4)
         .inputItems(EPPItemAndBlock.EX_INTERFACE.m_5456_(), 32)
         .inputItems(EPPItemAndBlock.INGREDIENT_BUFFER.m_5456_(), 32)
         .inputItems(EPPItemAndBlock.EX_IO_PORT.m_5456_(), 32)
         .inputItems(AEItems.QUANTUM_ENTANGLED_SINGULARITY.m_5456_(), 2)
         .inputItems(AEItems.QUANTUM_ENTANGLED_SINGULARITY.m_5456_(), 2)
         .inputItems(AEItems.SPATIAL_128_CELL_COMPONENT.m_5456_(), 16)
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(1152L))
         .inputFluids(GTMaterials.Naquadria.getFluid(1728L))
         .inputFluids(GTMaterials.Plutonium241.getFluid(2340L))
         .inputFluids(GTLMaterials.Mithril.getFluid(2340L))
         .outputItems(GTLMachines.GTAEMachines.ME_MOLECULAR_ASSEMBLER_IO)
         .EUt(GTValues.VA[9])
         .duration(200)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(EPPItemAndBlock.EX_IO_PORT.m_5456_().m_7968_()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[9]))
               .CWUt(128)
         )
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("me_craft_speed_core", new Object[0])
         .inputItems(GTMachines.WORLD_ACCELERATOR[8], 2)
         .inputItems(GTItems.FLUID_REGULATOR_UV, 8)
         .inputItems(GTItems.CONVEYOR_MODULE_UV, 8)
         .inputItems(Registries.getItemStack("kubejs:bioware_processing_core", 4))
         .inputItems(AEItems.SPEED_CARD.m_5456_(), 64)
         .inputItems(AEItems.SINGULARITY.m_5456_(), 64)
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.RED, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.RED, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.RED, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.RED, 64))
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(1152L))
         .inputFluids(GTMaterials.Nobelium.getFluid(1728L))
         .inputFluids(GTLMaterials.Orichalcum.getFluid(2340L))
         .inputFluids(GTLMaterials.Mithril.getFluid(2340L))
         .outputItems(GTLMachines.GTAEMachines.ME_CRAFT_SPEED_CORE)
         .EUt(GTValues.VA[9])
         .duration(200)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(GTMachines.WORLD_ACCELERATOR[8].asStack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[9]))
               .CWUt(128)
         )
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("me_craft_parallel_core", new Object[0])
         .inputItems(GTItems.ELECTRIC_MOTOR_UV, 16)
         .inputItems(GTItems.FIELD_GENERATOR_UV, 4)
         .inputItems(Registries.getItemStack("kubejs:bioware_processing_core", 4))
         .inputItems(EPPItemAndBlock.EX_ASSEMBLER.m_5456_(), 64)
         .inputItems(AEItems.CRAFTING_CARD.m_5456_(), 64)
         .inputItems(AEItems.SINGULARITY.m_5456_(), 64)
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.PURPLE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.PURPLE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.PURPLE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.PURPLE, 64))
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(1152L))
         .inputFluids(GTMaterials.Neptunium.getFluid(1728L))
         .inputFluids(GTLMaterials.Orichalcum.getFluid(2340L))
         .inputFluids(GTLMaterials.Mithril.getFluid(2340L))
         .outputItems(GTLMachines.GTAEMachines.ME_CRAFT_PARALLEL_CORE)
         .EUt(GTValues.VA[9])
         .duration(200)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(AEBlocks.MOLECULAR_ASSEMBLER.stack()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[9]))
               .CWUt(128)
         )
         .save(provider);
      GTRecipeTypes.ASSEMBLY_LINE_RECIPES
         .recipeBuilder("me_craft_pattern_container", new Object[0])
         .inputItems(GTMachines.HULL[8])
         .inputItems(GTItems.FIELD_GENERATOR_UV, 4)
         .inputItems(CustomTags.UV_CIRCUITS)
         .inputItems(EPPItemAndBlock.EX_PATTERN_PROVIDER.m_5456_(), 64)
         .inputItems(AEItems.CAPACITY_CARD.m_5456_(), 64)
         .inputItems(AEItems.SINGULARITY.m_5456_(), 64)
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.BLUE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.BLUE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.BLUE, 64))
         .inputItems(AEItems.COLORED_LUMEN_PAINT_BALL.stack(AEColor.BLUE, 64))
         .inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(1152L))
         .inputFluids(GTMaterials.Mendelevium.getFluid(1728L))
         .inputFluids(GTMaterials.DamascusSteel.getFluid(2304L))
         .inputFluids(GTLMaterials.Titanium50.getFluid(2304L))
         .outputItems(GTLMachines.GTAEMachines.ME_CRAFT_PATTERN_CONTAINER)
         .EUt(GTValues.VA[9])
         .duration(200)
         .stationResearch(
            b -> ((StationRecipeBuilder)((StationRecipeBuilder)((StationRecipeBuilder)b.researchStack(EPPItemAndBlock.EX_PATTERN_PROVIDER.m_5456_().m_7968_()))
                     .dataStack(GTItems.TOOL_DATA_MODULE.asStack()))
                  .EUt(GTValues.VA[9]))
               .CWUt(128)
         )
         .save(provider);
   }
}
