package org.gtlcore.gtlcore.common.data.machines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo.ShapeInfoBuilder;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.api.registry.registrate.CompassSection;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GCyMRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;

public class GCyMMachines {
   public static final MachineDefinition[] PARALLEL_HATCH = GTMachines.registerTieredMachines(
      "parallel_hatch",
      ParallelHatchPartMachine::new,
      (tier, builder) -> builder.langValue(tier + " Parallel Control Hatch")
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.PARALLEL_HATCH})
         .workableTieredHullRenderer(GTCEu.id("block/machines/parallel_hatch_mk" + (tier - 4)))
         .tooltips(new Component[]{Component.m_237115_("gtceu.machine.parallel_hatch_mk" + (tier + 2) + ".tooltip")})
         .tooltipBuilder(GTLMachines.GTL_MODIFY)
         .compassNode("parallel_hatch")
         .register(),
      new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14}
   );
   public static final MultiblockMachineDefinition LARGE_MACERATION_TOWER = GTRegistration.REGISTRATE
      .multiblock("large_maceration_tower", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Maceration Tower")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.macerator")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.MACERATOR_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_SECURE_MACERATION)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXXXX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XGGGX", "XGGGX", "XAAAX"})
            .aisle(new String[]{"XXXXX", "XGGGX", "XGGGX", "XAAAX"})
            .aisle(new String[]{"XXXXX", "XGGGX", "XGGGX", "XAAAX"})
            .aisle(new String[]{"XXXXX", "XXXXX", "XXSXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_SECURE_MACERATION.get()})
                  .setMinGlobalLimited(55)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GCyMBlocks.CRUSHING_WHEELS.get()}))
            .where('A', Predicates.air())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/secure_maceration_casing"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_CHEMICAL_BATH = GTRegistration.REGISTRATE
      .multiblock("large_chemical_bath", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Chemical Bath")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.ore_washer"), Component.m_237115_("gtceu.chemical_bath")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.CHEMICAL_BATH_RECIPES, GTRecipeTypes.ORE_WASHER_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_WATERTIGHT)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XTTTX", "X   X"})
            .aisle(new String[]{"XXXXX", "X   X", "X   X"})
            .aisle(new String[]{"XXXXX", "X   X", "X   X"})
            .aisle(new String[]{"XXXXX", "X   X", "X   X"})
            .aisle(new String[]{"XXXXX", "XTTTX", "X   X"})
            .aisle(new String[]{"XXXXX", "XXSXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_WATERTIGHT.get()})
                  .setMinGlobalLimited(55)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where(' ', Predicates.air())
            .where('T', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TITANIUM_PIPE.get()}))
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_chemical_bath"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_CENTRIFUGE = GTRegistration.REGISTRATE
      .multiblock("large_centrifuge", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Centrifugal Unit")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.centrifuge"), Component.m_237115_("gtceu.thermal_centrifuge")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.CENTRIFUGE_RECIPES, GTRecipeTypes.THERMAL_CENTRIFUGE_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_VIBRATION_SAFE)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "XXXXX", "#XXX#"})
            .aisle(new String[]{"XXXXX", "XAPAX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XPAPX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XAPAX", "XXXXX"})
            .aisle(new String[]{"#XXX#", "XXSXX", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_VIBRATION_SAFE.get()})
                  .setMinGlobalLimited(40)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"), GTCEu.id("block/multiblock/gcym/large_centrifuge"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_MIXER = GTRegistration.REGISTRATE
      .multiblock("large_mixer", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Mixing Vessel")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.mixer")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.MIXER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_REACTION_SAFE)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "#XXX#", "#XXX#", "#XXX#", "#XXX#", "##F##"})
            .aisle(new String[]{"XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##"})
            .aisle(new String[]{"XXXXX", "XPPPX", "XAPAX", "XPPPX", "XAGAX", "FFGFF"})
            .aisle(new String[]{"XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##"})
            .aisle(new String[]{"#XXX#", "#XSX#", "#XXX#", "#XXX#", "#XXX#", "##F##"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_REACTION_SAFE.get()})
                  .setMinGlobalLimited(50)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('F', Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.HastelloyX)}))
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.get()}))
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TITANIUM_PIPE.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/reaction_safe_mixing_casing"), GTCEu.id("block/multiblock/gcym/large_mixer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_ELECTROLYZER = GTRegistration.REGISTRATE
      .multiblock("large_electrolyzer", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Electrolysis Chamber")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.electrolyzer")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.ELECTROLYZER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_NONCONDUCTING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XCCCX", "XCCCX"})
            .aisle(new String[]{"XXXXX", "XCCCX", "XCCCX"})
            .aisle(new String[]{"XXXXX", "XXSXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_NONCONDUCTING.get()})
                  .setMinGlobalLimited(30)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GCyMBlocks.ELECTROLYTIC_CELL.get()}))
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"), GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_ELECTROMAGNET = GTRegistration.REGISTRATE
      .multiblock("large_electromagnet", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Electromagnet")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.electromagnetic_separator"), Component.m_237115_("gtceu.polarizer")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES, GTRecipeTypes.POLARIZER_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_NONCONDUCTING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXXXX"})
            .aisle(new String[]{"XCXCX", "XCXCX", "XCXCX"})
            .aisle(new String[]{"XCXCX", "XCXCX", "XCXCX"})
            .aisle(new String[]{"XXXXX", "XXSXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_NONCONDUCTING.get()})
                  .setMinGlobalLimited(35)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GCyMBlocks.ELECTROLYTIC_CELL.get()}))
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"), GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_PACKER = GTRegistration.REGISTRATE
      .multiblock("large_packer", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Packaging Machine")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.packer")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.PACKER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXX", "XXX", "XXX"})
            .aisle(new String[]{"XXX", "XAX", "XXX"})
            .aisle(new String[]{"XXX", "XAX", "XXX"})
            .aisle(new String[]{"XXX", "XAX", "XXX"})
            .aisle(new String[]{"XXX", "XAX", "XXX"})
            .aisle(new String[]{"XXX", "XSX", "XXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()})
                  .setMinGlobalLimited(30)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('A', Predicates.air())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"), GTCEu.id("block/multiblock/gcym/large_packer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[3]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_ASSEMBLER = GTRegistration.REGISTRATE
      .multiblock("large_assembler", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Assembling Factory")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.assembler")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.ASSEMBLER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX"})
            .aisle(new String[]{"XXXXXXXXX", "XAAAXAAAX", "XGGGXXXXX"})
            .aisle(new String[]{"XXXXXXXXX", "XGGGXXSXX", "XGGGX###X"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING.get()})
                  .setMinGlobalLimited(40)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_CIRCUIT_ASSEMBLER = GTRegistration.REGISTRATE
      .multiblock("large_circuit_assembler", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Circuit Assembling Facility")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.circuit_assembler")})}
      )
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.exact_hatch_1.tooltip")})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.CIRCUIT_ASSEMBLER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXXXX", "XXXXXXX", "XXXXXXX"})
            .aisle(new String[]{"XXXXXXX", "XPPPPPX", "XGGGGGX"})
            .aisle(new String[]{"XXXXXXX", "XAAAAPX", "XGGGGGX"})
            .aisle(new String[]{"XXXXXXX", "XTTTTXX", "XXXXXXX"})
            .aisle(new String[]{"#####XX", "#####SX", "#####XX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING.get()})
                  .setMinGlobalLimited(55)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).setExactLimit(1))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('T', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_GRATE.get()}))
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"), GTCEu.id("block/multiblock/gcym/large_circuit_assembler"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_ARC_SMELTER = GTRegistration.REGISTRATE
      .multiblock("large_arc_smelter", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Arc Smelter")
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.large_arc_smelter.tooltip.0")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.arc_furnace"), Component.m_237115_("gtceu.lightning_processor")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.ARC_FURNACE_RECIPES)
      .recipeType(GTLRecipeTypes.LIGHTNING_PROCESSOR_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            (machine, recipe, params, result) -> {
               if (((WorkableElectricMultiblockMachine)machine).getRecipeType() == GTLRecipeTypes.LIGHTNING_PROCESSOR_RECIPES) {
                  GTRecipe recipe1 = recipe.copy();
                  recipe1.duration = recipe.duration * 4;
                  return recipe1;
               } else {
                  return recipe;
               }
            },
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "#XXX#", "#XXX#", "#XXX#"})
            .aisle(new String[]{"XXXXX", "XCACX", "XCACX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XAAAX", "XAAAX", "XXMXX"})
            .aisle(new String[]{"XXXXX", "XACAX", "XACAX", "XXXXX"})
            .aisle(new String[]{"#XXX#", "#XSX#", "#XXX#", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()})
                  .setMinGlobalLimited(45)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GCyMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()}))
            .where('M', Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/large_arc_smelter"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_ENGRAVING_LASER = GTRegistration.REGISTRATE
      .multiblock("large_engraving_laser", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Engraving Laser")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.laser_engraver")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.LASER_ENGRAVER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_LASER_SAFE_ENGRAVING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXGXX", "XXGXX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XAAAX", "XAAAX", "XKKKX"})
            .aisle(new String[]{"XXXXX", "GAAAG", "GACAG", "XKXKX"})
            .aisle(new String[]{"XXXXX", "XAAAX", "XAAAX", "XKKKX"})
            .aisle(new String[]{"XXSXX", "XXGXX", "XXGXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where('C', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()}))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_LASER_SAFE_ENGRAVING.get()})
                  .setMinGlobalLimited(45)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('K', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_GRATE.get()}))
            .where('A', Predicates.air())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/laser_safe_engraving_casing"), GTCEu.id("block/multiblock/gcym/large_engraving_laser"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_SIFTING_FUNNEL = GTRegistration.REGISTRATE
      .multiblock("large_sifting_funnel", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Sifting Funnel")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.sifter")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.SIFTER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_VIBRATION_SAFE)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#X#X#", "#X#X#", "#XXX#", "XXXXX", "#XXX#"})
            .aisle(new String[]{"XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X"})
            .aisle(new String[]{"#XXX#", "#XAX#", "XKKKX", "XKKKX", "X###X"})
            .aisle(new String[]{"XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X"})
            .aisle(new String[]{"#X#X#", "#X#X#", "#XSX#", "XXXXX", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_VIBRATION_SAFE.get()})
                  .setMinGlobalLimited(50)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('K', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_GRATE.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"), GTCEu.id("block/multiblock/gcym/large_sifting_funnel"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition BLAST_ALLOY_SMELTER = GTRegistration.REGISTRATE
      .multiblock("alloy_blast_smelter", CoilWorkableElectricMultiblockMachine::new)
      .langValue("Alloy Blast Smelter")
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.a"),
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.0"),
            Component.m_237115_("gtceu.machine.perfect_oc"),
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.2")
         }
      )
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.alloy_blast_smelter")})}
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GCyMRecipeTypes.ALLOY_BLAST_RECIPES)
      .recipeModifiers(new RecipeModifier[]{GTRecipeModifiers::ebfOverclock})
      .appearanceBlock(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#"})
            .aisle(new String[]{"XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX"})
            .aisle(new String[]{"XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXMXX"})
            .aisle(new String[]{"XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX"})
            .aisle(new String[]{"#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()})
                  .setMinGlobalLimited(30)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, false))
            )
            .where('C', Predicates.heatingCoils())
            .where('M', Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
            .where('G', Predicates.blocks(new Block[]{(Block)GCyMBlocks.HEAT_VENT.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .shapeInfos(
         definition -> {
            List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            ShapeInfoBuilder builder = ((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)MultiblockShapeInfo.builder()
                              .aisle(new String[]{"#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XMX#"}))
                           .aisle(new String[]{"IXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX"}))
                        .aisle(new String[]{"XXXXD", "CAAAC", "GAAAG", "CAAAC", "XXHXX"}))
                     .aisle(new String[]{"FXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX"}))
                  .aisle(new String[]{"#EXE#", "#CCC#", "#GGG#", "#CCC#", "#XXX#"}))
               .where('X', GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.getDefaultState())
               .where('S', definition, Direction.NORTH)
               .where('G', GCyMBlocks.HEAT_VENT.getDefaultState())
               .where('A', Blocks.f_50016_.m_49966_())
               .where('E', GTMachines.ENERGY_INPUT_HATCH[1], Direction.SOUTH)
               .where('I', GTMachines.ITEM_IMPORT_BUS[1], Direction.WEST)
               .where('F', GTMachines.FLUID_IMPORT_HATCH[1], Direction.WEST)
               .where('D', GTMachines.FLUID_EXPORT_HATCH[1], Direction.EAST)
               .where('H', GTMachines.MUFFLER_HATCH[1], Direction.UP)
               .where('M', GTMachines.MAINTENANCE_HATCH, Direction.NORTH);
            GTCEuAPI.HEATING_COILS
               .entrySet()
               .stream()
               .sorted(Comparator.comparingInt(entry -> ((ICoilType)entry.getKey()).getTier()))
               .forEach(coil -> shapeInfo.add(((ShapeInfoBuilder)builder.shallowCopy()).where('C', (Block)((Supplier)coil.getValue()).get()).build()));
            return shapeInfo;
         }
      )
      .additionalDisplay(GTLMachines.MAX_TEMPERATURE)
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/blast_alloy_smelter"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_AUTOCLAVE = GTRegistration.REGISTRATE
      .multiblock("large_autoclave", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Crystallization Chamber")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.autoclave")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.AUTOCLAVE_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_WATERTIGHT)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXX", "XXX", "XXX"})
            .aisle(new String[]{"XXX", "XTX", "XXX"})
            .aisle(new String[]{"XXX", "XTX", "XXX"})
            .aisle(new String[]{"XXX", "XTX", "XXX"})
            .aisle(new String[]{"XXX", "XSX", "XXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_WATERTIGHT.get()})
                  .setMinGlobalLimited(30)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('T', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_autoclave"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_MATERIAL_PRESS = GTRegistration.REGISTRATE
      .multiblock("large_material_press", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Material Press")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_4.tooltip",
               new Object[]{
                  Component.m_237115_("gtceu.bender"),
                  Component.m_237115_("gtceu.compressor"),
                  Component.m_237115_("gtceu.forge_hammer"),
                  Component.m_237115_("gtceu.forming_press")
               }
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(
         new GTRecipeType[]{
            GTRecipeTypes.BENDER_RECIPES, GTRecipeTypes.COMPRESSOR_RECIPES, GTRecipeTypes.FORGE_HAMMER_RECIPES, GTRecipeTypes.FORMING_PRESS_RECIPES
         }
      )
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_STRESS_PROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXXXX", "XXXXXXX", "XXXXXXX"})
            .aisle(new String[]{"XXXXXXX", "XAXGGGX", "XXXXXXX"})
            .aisle(new String[]{"XXXXXXX", "XSXCCCX", "XXXXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_STRESS_PROOF.get()})
                  .setMinGlobalLimited(40)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_GEARBOX.get()}))
            .where('C', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('A', Predicates.air())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_material_press"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_BREWER = GTRegistration.REGISTRATE
      .multiblock("large_brewer", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Brewing Vat")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_3.tooltip",
               new Object[]{Component.m_237115_("gtceu.brewery"), Component.m_237115_("gtceu.fermenter"), Component.m_237115_("gtceu.fluid_heater")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.BREWING_RECIPES, GTRecipeTypes.FERMENTING_RECIPES, GTRecipeTypes.FLUID_HEATER_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_CORROSION_PROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "#XXX#", "#XXX#", "#XXX#", "#####"})
            .aisle(new String[]{"XXXXX", "XCCCX", "XAAAX", "XXAXX", "##X##"})
            .aisle(new String[]{"XXXXX", "XCPCX", "XAPAX", "XAPAX", "#XMX#"})
            .aisle(new String[]{"XXXXX", "XCCCX", "XAAAX", "XXAXX", "##X##"})
            .aisle(new String[]{"#XXX#", "#XSX#", "#XXX#", "#XXX#", "#####"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_CORROSION_PROOF.get()})
                  .setMinGlobalLimited(50)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where('C', Predicates.blocks(new Block[]{(Block)GCyMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()}))
            .where('M', Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/corrosion_proof_casing"), GTCEu.id("block/multiblock/gcym/large_brewer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_CUTTER = GTRegistration.REGISTRATE
      .multiblock("large_cutter", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Cutting Saw")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip", new Object[]{Component.m_237115_("gtceu.cutter"), Component.m_237115_("gtceu.lathe")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.CUTTER_RECIPES, GTRecipeTypes.LATHE_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_SHOCK_PROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXXXX", "XXXXXXX", "XXXXXXX", "##XXXXX"})
            .aisle(new String[]{"XXXXXXX", "XAXCCCX", "XXXAAAX", "##XXXXX"})
            .aisle(new String[]{"XXXXXXX", "XAXCCCX", "XXXAAAX", "##XXXXX"})
            .aisle(new String[]{"XXXXXXX", "XSXGGGX", "XXXGGGX", "##XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_SHOCK_PROOF.get()})
                  .setMinGlobalLimited(65)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('C', Predicates.blocks(new Block[]{(Block)GCyMBlocks.SLICING_BLADES.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/shock_proof_cutting_casing"), GTCEu.id("block/multiblock/gcym/large_cutter"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_DISTILLERY = GTRegistration.REGISTRATE
      .multiblock("large_distillery", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Fractionating Distillery")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.distillation_tower"), Component.m_237115_("gtceu.distillery")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.NON_Y_AXIS)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.DISTILLATION_RECIPES, GTRecipeTypes.DISTILLERY_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_WATERTIGHT)
      .pattern(
         definition -> {
            TraceabilityPredicate casingPredicate = Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_WATERTIGHT.get()}).setMinGlobalLimited(40);
            return FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.BACK, RelativeDirection.UP)
               .aisle(new String[]{"#YYY#", "YYYYY", "YYYYY", "YYYYY", "#YYY#"})
               .aisle(new String[]{"#YSY#", "YAAAY", "YAAAY", "YAAAY", "#YYY#"})
               .aisle(new String[]{"##X##", "#XAX#", "XAPAX", "#XAX#", "##X##"})
               .setRepeatable(1, 12)
               .aisle(new String[]{"#####", "#ZZZ#", "#ZCZ#", "#ZZZ#", "#####"})
               .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
               .where(
                  'Y',
                  casingPredicate.or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS}))
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_FLUIDS}).setMinGlobalLimited(1))
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS}))
                     .or(Predicates.autoAbilities(true, false, true))
               )
               .where(
                  'X', casingPredicate.or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_FLUIDS_1X}).setMinLayerLimited(1).setMaxLayerLimited(1))
               )
               .where('Z', casingPredicate)
               .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
               .where('C', Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
               .where('A', Predicates.air())
               .where('#', Predicates.any())
               .build();
         }
      )
      .shapeInfos(
         definition -> {
            List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
            ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
               .where('S', definition, Direction.NORTH)
               .where('C', GCyMBlocks.CASING_WATERTIGHT.getDefaultState())
               .where('M', GTMachines.MUFFLER_HATCH[5], Direction.UP)
               .where('X', PARALLEL_HATCH[5], Direction.NORTH)
               .where('H', GTMachines.FLUID_IMPORT_HATCH[5], Direction.NORTH)
               .where('B', GTMachines.ITEM_EXPORT_BUS[5], Direction.NORTH)
               .where('N', GTMachines.MAINTENANCE_HATCH, Direction.NORTH)
               .where('P', GTBlocks.CASING_STEEL_PIPE.getDefaultState())
               .where('F', GTMachines.FLUID_EXPORT_HATCH[5], Direction.SOUTH)
               .where('E', GTMachines.ENERGY_INPUT_HATCH[5], Direction.SOUTH)
               .where('#', Blocks.f_50016_.m_49966_());
            List<String> aisle1 = new ArrayList<>(16);
            aisle1.add("#HCB#");
            aisle1.add("#NSX#");
            aisle1.add("#####");
            List<String> aisle2 = new ArrayList<>(16);
            aisle2.add("CCCCC");
            aisle2.add("C###C");
            aisle2.add("#CCC#");
            List<String> aisle3 = new ArrayList<>(16);
            aisle3.add("CCCCC");
            aisle3.add("C###C");
            aisle3.add("#CMC#");
            List<String> aisle4 = new ArrayList<>(16);
            aisle4.add("CCCCC");
            aisle4.add("C###C");
            aisle4.add("#CCC#");
            List<String> aisle5 = new ArrayList<>(16);
            aisle5.add("#CEC#");
            aisle5.add("#CCC#");
            aisle5.add("#####");

            for (int i = 1; i <= 12; i++) {
               aisle1.add(2, "##C##");
               aisle2.add(2, "#C#C#");
               aisle3.add(2, "C#P#C");
               aisle4.add(2, "#C#C#");
               aisle5.add(2, "##F##");
               ShapeInfoBuilder copy = (ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)builder.shallowCopy())
                              .aisle(aisle1.toArray(String[]::new)))
                           .aisle(aisle2.toArray(String[]::new)))
                        .aisle(aisle3.toArray(String[]::new)))
                     .aisle(aisle4.toArray(String[]::new)))
                  .aisle(aisle5.toArray(String[]::new));
               shapeInfos.add(copy.build());
            }

            return shapeInfos;
         }
      )
      .allowExtendedFacing(false)
      .partSorter(Comparator.comparingInt(a -> a.self().getPos().m_123342_()))
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_distillery"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_EXTRACTOR = GTRegistration.REGISTRATE
      .multiblock("large_extractor", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Extraction Machine")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip", new Object[]{Component.m_237115_("gtceu.extractor"), Component.m_237115_("gtceu.canner")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTRecipeTypes.EXTRACTOR_RECIPES, GTRecipeTypes.CANNER_RECIPES})
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_WATERTIGHT)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XCACX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XXSXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_WATERTIGHT.get()})
                  .setMinGlobalLimited(25)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where('A', Predicates.air())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_extractor"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_EXTRUDER = GTRegistration.REGISTRATE
      .multiblock("large_extruder", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Extrusion Machine")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.extruder")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.EXTRUDER_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_STRESS_PROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"##XXX", "##XXX", "##XXX"})
            .aisle(new String[]{"##XXX", "##XPX", "##XGX"})
            .setRepeatable(2)
            .aisle(new String[]{"XXXXX", "XXXPX", "XXXGX"})
            .aisle(new String[]{"XXXXX", "XAXPX", "XXXGX"})
            .aisle(new String[]{"XXXXX", "XSXXX", "XXXXX"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_STRESS_PROOF.get()})
                  .setMinGlobalLimited(40)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TITANIUM_PIPE.get()}))
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_extruder"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_SOLIDIFIER = GTRegistration.REGISTRATE
      .multiblock("large_solidifier", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Solidification Array")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.fluid_solidifier")})}
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_WATERTIGHT)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#XXX#", "#XXX#", "#XXX#", "#XXX#"})
            .aisle(new String[]{"XXXXX", "XCACX", "XCACX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XAAAX", "XAAAX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XCACX", "XCACX", "XXXXX"})
            .aisle(new String[]{"#XXX#", "#XSX#", "#XXX#", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_WATERTIGHT.get()})
                  .setMinGlobalLimited(45)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_solidifier"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition LARGE_WIREMILL = GTRegistration.REGISTRATE
      .multiblock("large_wiremill", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Large Wire Factory")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.wiremill")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.WIREMILL_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GCyMBlocks.CASING_STRESS_PROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXX", "XXXXX", "XXX##"})
            .aisle(new String[]{"XXXXX", "X#CCX", "XXXXX"})
            .aisle(new String[]{"XXXXX", "XSXXX", "XXX##"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_STRESS_PROOF.get()})
                  .setMinGlobalLimited(25)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('C', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TITANIUM_GEARBOX.get()}))
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_wiremill"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[5]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition MEGA_BLAST_FURNACE = GTRegistration.REGISTRATE
      .multiblock("mega_blast_furnace", CoilWorkableElectricMultiblockMachine::new)
      .langValue("Rotary Hearth Furnace")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.a"),
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.0"),
            Component.m_237115_("gtceu.machine.perfect_oc"),
            Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.2")
         }
      )
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.electric_blast_furnace")})
         }
      )
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.BLAST_RECIPES)
      .recipeModifiers(new RecipeModifier[]{GTLRecipeModifiers.GCYM_REDUCTION, GTRecipeModifiers.PARALLEL_HATCH, GTRecipeModifiers::ebfOverclock})
      .appearanceBlock(GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING)
      .pattern(
         definition -> {
            TraceabilityPredicate casing = Predicates.blocks(new Block[]{(Block)GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()}).setMinGlobalLimited(360);
            return FactoryBlockPattern.start()
               .aisle(
                  new String[]{
                     "##XXXXXXXXX##",
                     "##XXXXXXXXX##",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############"
                  }
               )
               .aisle(
                  new String[]{
                     "#XXXXXXXXXXX#",
                     "#XXXXXXXXXXX#",
                     "###F#####F###",
                     "###F#####F###",
                     "###FFFFFFF###",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "####FFFFF####",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXXXVVVVVXXXX",
                     "##F#######F##",
                     "##F#######F##",
                     "##FFFHHHFFF##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##FFFHHHFFF##",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "###TTTTTTT###"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXXXXXXXXXXXX",
                     "#F####P####F#",
                     "#F####P####F#",
                     "#FFHHHPHHHFF#",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "##FHHHPHHHF##",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXVXXXXXXXVXX",
                     "####BBPBB####",
                     "####TITIT####",
                     "#FFHHHHHHHFF#",
                     "####BITIB####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####BITIB####",
                     "#FFHHHHHHHFF#",
                     "####BITIB####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####BITIB####",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXVXXXXXXXVXX",
                     "####BAAAB####",
                     "####IAAAI####",
                     "#FHHHAAAHHHF#",
                     "####IAAAI####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####IAAAI####",
                     "#FHHHAAAHHHF#",
                     "####IAAAI####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####IAAAI####",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXVXXXXXXXVXX",
                     "###PPAAAPP###",
                     "###PTAAATP###",
                     "#FHPHAAAHPHF#",
                     "###PTAAATP###",
                     "###PCAAACP###",
                     "###PCAAACP###",
                     "###PCAAACP###",
                     "###PTAAATP###",
                     "#FHPHAAAHPHF#",
                     "###PTAAATP###",
                     "###PCAAACP###",
                     "###PCAAACP###",
                     "###PCAAACP###",
                     "###PTAAATP###",
                     "##TPPPMPPPT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXVXXXXXXXVXX",
                     "####BAAAB####",
                     "####IAAAI####",
                     "#FHHHAAAHHHF#",
                     "####IAAAI####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####IAAAI####",
                     "#FHHHAAAHHHF#",
                     "####IAAAI####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####CAAAC####",
                     "####IAAAI####",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXVXXXXXXXVXX",
                     "####BBPBB####",
                     "####TITIT####",
                     "#FFHHHHHHHFF#",
                     "####BITIB####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####BITIB####",
                     "#FFHHHHHHHFF#",
                     "####BITIB####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####CCCCC####",
                     "####BITIB####",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXXXXXXXXXXXX",
                     "#F####P####F#",
                     "#F####P####F#",
                     "#FFHHHPHHHFF#",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "##FHHHPHHHF##",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "######P######",
                     "##TTTTPTTTT##"
                  }
               )
               .aisle(
                  new String[]{
                     "XXXXXXXXXXXXX",
                     "XXXXVVVVVXXXX",
                     "##F#######F##",
                     "##F#######F##",
                     "##FFFHHHFFF##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##F#######F##",
                     "##FFFHHHFFF##",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "###TTTTTTT###"
                  }
               )
               .aisle(
                  new String[]{
                     "#XXXXXXXXXXX#",
                     "#XXXXXXXXXXX#",
                     "###F#####F###",
                     "###F#####F###",
                     "###FFFFFFF###",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "####FFFFF####",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############"
                  }
               )
               .aisle(
                  new String[]{
                     "##XXXXXXXXX##",
                     "##XXXXSXXXX##",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############",
                     "#############"
                  }
               )
               .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
               .where('X', casing.or(Predicates.autoAbilities(definition.getRecipeTypes())).or(Predicates.autoAbilities(true, false, true)))
               .where('C', Predicates.heatingCoils())
               .where('M', Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
               .where('F', Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)}))
               .where('H', casing)
               .where('T', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()}))
               .where('B', Predicates.blocks(new Block[]{(Block)GTBlocks.FIREBOX_TUNGSTENSTEEL.get()}))
               .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()}))
               .where('I', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_EXTREME_ENGINE_INTAKE.get()}))
               .where('V', Predicates.blocks(new Block[]{(Block)GCyMBlocks.HEAT_VENT.get()}))
               .where('A', Predicates.air())
               .where('#', Predicates.any())
               .build();
         }
      )
      .shapeInfos(
         definition -> {
            List<MultiblockShapeInfo> shapeInfo = new ArrayList<>();
            ShapeInfoBuilder builder = ((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)MultiblockShapeInfo.builder()
                                                      .aisle(
                                                         new String[]{
                                                            "##XODXXXQLX##",
                                                            "##XXXXSXXXX##",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############",
                                                            "#############"
                                                         }
                                                      ))
                                                   .aisle(
                                                      new String[]{
                                                         "#XXXXXXXXXXX#",
                                                         "#XXXXXXXXXXX#",
                                                         "###F#####F###",
                                                         "###F#####F###",
                                                         "###FFFFFFF###",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "####FFFFF####",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "#############",
                                                         "#############"
                                                      }
                                                   ))
                                                .aisle(
                                                   new String[]{
                                                      "XXXXXXXXXXXXX",
                                                      "XXXXVVVVVXXXX",
                                                      "##F#######F##",
                                                      "##F#######F##",
                                                      "##FFFXXXFFF##",
                                                      "##F#######F##",
                                                      "##F#######F##",
                                                      "##F#######F##",
                                                      "##F#######F##",
                                                      "##F#######F##",
                                                      "##FFFXXXFFF##",
                                                      "#############",
                                                      "#############",
                                                      "#############",
                                                      "#############",
                                                      "#############",
                                                      "###TTTTTTT###"
                                                   }
                                                ))
                                             .aisle(
                                                new String[]{
                                                   "XXXXXXXXXXXXX",
                                                   "XXXXXXXXXXXXX",
                                                   "#F####P####F#",
                                                   "#F####P####F#",
                                                   "#FFXXXPXXXFF#",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "##FXXXPXXXF##",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "######P######",
                                                   "##TTTTPTTTT##"
                                                }
                                             ))
                                          .aisle(
                                             new String[]{
                                                "XXXXXXXXXXXXX",
                                                "XXVXXXXXXXVXX",
                                                "####BBPBB####",
                                                "####TITIT####",
                                                "#FFXXXXXXXFF#",
                                                "####BITIB####",
                                                "####CCCCC####",
                                                "####CCCCC####",
                                                "####CCCCC####",
                                                "####BITIB####",
                                                "#FFXXXXXXXFF#",
                                                "####BITIB####",
                                                "####CCCCC####",
                                                "####CCCCC####",
                                                "####CCCCC####",
                                                "####BITIB####",
                                                "##TTTTPTTTT##"
                                             }
                                          ))
                                       .aisle(
                                          new String[]{
                                             "XXXXXXXXXXXXX",
                                             "XXVXXXXXXXVXX",
                                             "####BAAAB####",
                                             "####IAAAI####",
                                             "#FXXXAAAXXXF#",
                                             "####IAAAI####",
                                             "####CAAAC####",
                                             "####CAAAC####",
                                             "####CAAAC####",
                                             "####IAAAI####",
                                             "#FXXXAAAXXXF#",
                                             "####IAAAI####",
                                             "####CAAAC####",
                                             "####CAAAC####",
                                             "####CAAAC####",
                                             "####IAAAI####",
                                             "##TTTTPTTTT##"
                                          }
                                       ))
                                    .aisle(
                                       new String[]{
                                          "XXXXXXXXXXXXX",
                                          "XXVXXXXXXXVXX",
                                          "###PPAAAPP###",
                                          "###PTAAATP###",
                                          "#FXPXAAAXPXF#",
                                          "###PTAAATP###",
                                          "###PCAAACP###",
                                          "###PCAAACP###",
                                          "###PCAAACP###",
                                          "###PTAAATP###",
                                          "#FXPXAAAXPXF#",
                                          "###PTAAATP###",
                                          "###PCAAACP###",
                                          "###PCAAACP###",
                                          "###PCAAACP###",
                                          "###PTAAATP###",
                                          "##TPPPHPPPT##"
                                       }
                                    ))
                                 .aisle(
                                    new String[]{
                                       "XXXXXXXXXXXXX",
                                       "XXVXXXXXXXVXX",
                                       "####BAAAB####",
                                       "####IAAAI####",
                                       "#FXXXAAAXXXF#",
                                       "####IAAAI####",
                                       "####CAAAC####",
                                       "####CAAAC####",
                                       "####CAAAC####",
                                       "####IAAAI####",
                                       "#FXXXAAAXXXF#",
                                       "####IAAAI####",
                                       "####CAAAC####",
                                       "####CAAAC####",
                                       "####CAAAC####",
                                       "####IAAAI####",
                                       "##TTTTPTTTT##"
                                    }
                                 ))
                              .aisle(
                                 new String[]{
                                    "XXXXXXXXXXXXX",
                                    "XXVXXXXXXXVXX",
                                    "####BBPBB####",
                                    "####TITIT####",
                                    "#FFXXXXXXXFF#",
                                    "####BITIB####",
                                    "####CCCCC####",
                                    "####CCCCC####",
                                    "####CCCCC####",
                                    "####BITIB####",
                                    "#FFXXXXXXXFF#",
                                    "####BITIB####",
                                    "####CCCCC####",
                                    "####CCCCC####",
                                    "####CCCCC####",
                                    "####BITIB####",
                                    "##TTTTPTTTT##"
                                 }
                              ))
                           .aisle(
                              new String[]{
                                 "XXXXXXXXXXXXX",
                                 "XXXXXXXXXXXXX",
                                 "#F####P####F#",
                                 "#F####P####F#",
                                 "#FFXXXPXXXFF#",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "##FXXXPXXXF##",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "######P######",
                                 "##TTTTPTTTT##"
                              }
                           ))
                        .aisle(
                           new String[]{
                              "XXXXXXXXXXXXX",
                              "XXXXVVVVVXXXX",
                              "##F#######F##",
                              "##F#######F##",
                              "##FFFXXXFFF##",
                              "##F#######F##",
                              "##F#######F##",
                              "##F#######F##",
                              "##F#######F##",
                              "##F#######F##",
                              "##FFFXXXFFF##",
                              "#############",
                              "#############",
                              "#############",
                              "#############",
                              "#############",
                              "###TTTTTTT###"
                           }
                        ))
                     .aisle(
                        new String[]{
                           "#XXXXXXXXXXX#",
                           "#XXXXXXXXXXX#",
                           "###F#####F###",
                           "###F#####F###",
                           "###FFFFFFF###",
                           "#############",
                           "#############",
                           "#############",
                           "#############",
                           "#############",
                           "####FFFFF####",
                           "#############",
                           "#############",
                           "#############",
                           "#############",
                           "#############",
                           "#############"
                        }
                     ))
                  .aisle(
                     new String[]{
                        "##XXXEMEXXX##",
                        "##XXXXXXXXX##",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############",
                        "#############"
                     }
                  ))
               .where('X', GCyMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.getDefaultState())
               .where('S', definition, Direction.NORTH)
               .where('A', Blocks.f_50016_.m_49966_())
               .where('T', GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.getDefaultState())
               .where('B', GTBlocks.FIREBOX_TUNGSTENSTEEL.getDefaultState())
               .where('P', GTBlocks.CASING_TUNGSTENSTEEL_PIPE.getDefaultState())
               .where('I', GTBlocks.CASING_EXTREME_ENGINE_INTAKE.getDefaultState())
               .where('F', ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy))
               .where('V', GCyMBlocks.HEAT_VENT.getDefaultState())
               .where('E', GTMachines.ENERGY_INPUT_HATCH[1], Direction.SOUTH)
               .where('L', GTMachines.ITEM_IMPORT_BUS[1], Direction.NORTH)
               .where('O', GTMachines.ITEM_EXPORT_BUS[1], Direction.NORTH)
               .where('Q', GTMachines.FLUID_IMPORT_HATCH[1], Direction.NORTH)
               .where('D', GTMachines.FLUID_EXPORT_HATCH[1], Direction.NORTH)
               .where('H', GTMachines.MUFFLER_HATCH[1], Direction.UP)
               .where('M', GTMachines.MAINTENANCE_HATCH, Direction.SOUTH);
            GTCEuAPI.HEATING_COILS
               .entrySet()
               .stream()
               .sorted(Comparator.comparingInt(entry -> ((ICoilType)entry.getKey()).getTier()))
               .forEach(coil -> shapeInfo.add(((ShapeInfoBuilder)builder.shallowCopy()).where('C', (Block)((Supplier)coil.getValue()).get()).build()));
            return shapeInfo;
         }
      )
      .additionalDisplay(GTLMachines.MAX_TEMPERATURE)
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/mega_blast_furnace"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[6]})
      .compassNodeSelf()
      .register();
   public static final MultiblockMachineDefinition MEGA_VACUUM_FREEZER = GTRegistration.REGISTRATE
      .multiblock("mega_vacuum_freezer", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Bulk Blast Chiller")
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{0.8})})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.6})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.vacuum_freezer")})})
      .tooltipBuilder(GTLMachines.GTL_MODIFY)
      .rotationState(RotationState.ALL)
      .recipeType(GTRecipeTypes.VACUUM_RECIPES)
      .recipeModifiers(
         new RecipeModifier[]{
            GTLRecipeModifiers.GCYM_REDUCTION,
            GTRecipeModifiers.PARALLEL_HATCH,
            (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTBlocks.CASING_ALUMINIUM_FROSTPROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"XXXXXXX#KKK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KKK", "XXXXXXX####", "XXXXXXX####"})
            .aisle(new String[]{"XXXXXXX#KVK", "XPPPPPPPPPV", "XPAPAPX#VPV", "XPPPPPPPPPV", "XPAPAPX#KVK", "XPPPPPX####", "XXXXXXX####"})
            .aisle(new String[]{"XXXXXXX#KVK", "XPAPAPXAVPV", "XAAAAAX#VPV", "XPAAAPX#VPV", "XAAAAAX#KVK", "XPAPAPX####", "XXXXXXX####"})
            .aisle(new String[]{"XXXXXXX#KVK", "XPAPAPPPPPV", "XAAAAAX#VPV", "XPAAAPPPPPV", "XAAAAAX#KVK", "XPAPAPX####", "XXXXXXX####"})
            .aisle(new String[]{"XXXXXXX#KKK", "XPPPPPX#KVK", "XPA#APX#KVK", "XPAAAPX#KVK", "XPAAAPX#KKK", "XPPPPPX####", "XXXXXXX####"})
            .aisle(new String[]{"#XXXXX#####", "#XXSXX#####", "#XGGGX#####", "#XGGGX#####", "#XGGGX#####", "#XXXXX#####", "###########"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               'X',
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get()})
                  .setMinGlobalLimited(140)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('K', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where('P', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()}))
            .where('V', Predicates.blocks(new Block[]{(Block)GCyMBlocks.HEAT_VENT.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"), GTCEu.id("block/multiblock/gcym/mega_vacuum_freezer"))
      .compassSections(new CompassSection[]{GTCompassSections.TIER[6]})
      .compassNodeSelf()
      .register();

   public static void init() {
   }
}
