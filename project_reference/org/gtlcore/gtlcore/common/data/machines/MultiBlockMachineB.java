package org.gtlcore.gtlcore.common.data.machines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.WorkableElectricParallelHatchMultipleRecipesMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.noenergy.PrimitiveOreMachine;
import org.gtlcore.gtlcore.config.ConfigHolder;

public class MultiBlockMachineB {
   public static final MultiblockMachineDefinition GRAVITATION_SHOCKBURST = GTRegistration.REGISTRATE
      .multiblock("gravitation_shockburst", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .rotationState(RotationState.ALL)
      .recipeType(GTLRecipeTypes.GRAVITATION_SHOCKBURST_RECIPES)
      .recipeType(GTLRecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES)
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.only.laser.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.perfect_oc")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.gravitation_shockburst"), Component.m_237115_("gtceu.electric_implosion_compressor")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .recipeModifiers(
         new RecipeModifier[]{
            GTRecipeModifiers.PARALLEL_HATCH, (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTLBlocks.CREATE_CASING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"aaaaaaaaa", "         ", "         ", "         ", "         ", "         ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " abbbbba ", " abbbbba ", " abbbbba ", " abbbbba ", " abbbbba ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " b ccc b ", " bcccccb ", " bcccccb ", " bcccccb ", " b ccc b ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " bcccccb ", " bcccccb ", " bcc ccb ", " bcccccb ", " bcccccb ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " bcccccb ", " bcc ccb ", " bc   cb ", " bcc ccb ", " bcccccb ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " bcccccb ", " bcccccb ", " bcc ccb ", " bcccccb ", " bcccccb ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " b ccc b ", " bcccccb ", " bcccccb ", " bcccccb ", " b ccc b ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaaaaaaa", " abbbbba ", " abbbbba ", " abbbbba ", " abbbbba ", " abbbbba ", "aaaaaaaaa"})
            .aisle(new String[]{"aaaa~aaaa", "         ", "         ", "         ", "         ", "         ", "aaaaaaaaa"})
            .where(
               "a",
               Predicates.blocks(new Block[]{(Block)GTLBlocks.CREATE_CASING.get()})
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS}).setMaxGlobalLimited(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS}).setMaxGlobalLimited(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.PARALLEL_HATCH}).setExactLimit(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_LASER}).setMaxGlobalLimited(1))
            )
            .where("b", Predicates.blocks(new Block[]{(Block)GTLBlocks.INFINITY_GLASS.get()}))
            .where("c", Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.block, GTLMaterials.Infinity)}))
            .where("~", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(" ", Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTLCore.id("block/create_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
      .register();
   public static final MultiblockMachineDefinition DISSOLVING_TANK = GTRegistration.REGISTRATE
      .multiblock("dissolving_tank", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .langValue("Dissolving Tank")
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.dissolving_tank.tooltip.0")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("tooltip.gtlcore.structure.source", new Object[]{"GTNH"})})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.dissolution_treatment")})}
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTLRecipeTypes.DISSOLUTION_TREATMENT})
      .recipeModifier(GTLRecipeModifiers::dissolvingTankOverclock)
      .appearanceBlock(GTBlocks.CASING_STAINLESS_CLEAN)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"X###X", "OOOOO", "XGGGX", "XGGGX", "#XXX#"})
            .aisle(new String[]{"#####", "OKKKO", "GAAAG", "GAAAG", "XXXXX"})
            .aisle(new String[]{"#####", "OKKKO", "GAAAG", "GAAAG", "XXXXX"})
            .aisle(new String[]{"#####", "OKKKO", "GAAAG", "GAAAG", "XXXXX"})
            .aisle(new String[]{"X###X", "OOSOO", "XGGGX", "XGGGX", "#XXX#"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where('X', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where('K', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_INVAR_HEATPROOF.get()}))
            .where(
               'O',
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('G', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TEMPERED_GLASS.get()}))
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/generator/large_gas_turbine"))
      .register();
   public static final MultiblockMachineDefinition DIGESTION_TANK = GTRegistration.REGISTRATE
      .multiblock("digestion_tank", CoilWorkableElectricMultiblockMachine::new)
      .langValue("Digestion Tank")
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("tooltip.gtlcore.structure.source", new Object[]{"GTNH"})})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.digestion_treatment")})}
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .rotationState(RotationState.ALL)
      .recipeTypes(new GTRecipeType[]{GTLRecipeTypes.DIGESTION_TREATMENT})
      .recipeModifiers(
         new RecipeModifier[]{
            GTRecipeModifiers.PARALLEL_HATCH, (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"#OOOOO#", "#YMMMY#", "##YYY##", "#######"})
            .aisle(new String[]{"OXXXXXO", "YMAAAMY", "#YAAAY#", "#YYYYY#"})
            .aisle(new String[]{"OXKKKXO", "MAAAAAM", "YAAAAAY", "#YAAAY#"})
            .aisle(new String[]{"OXKKKXO", "MAAAAAM", "YAAAAAY", "#YAAAY#"})
            .aisle(new String[]{"OXKKKXO", "MAAAAAM", "YAAAAAY", "#YAAAY#"})
            .aisle(new String[]{"OXXXXXO", "YMAAAMY", "#YAAAY#", "#YYYYY#"})
            .aisle(new String[]{"#OOSOO#", "#YMMMY#", "##YYY##", "#######"})
            .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where('X', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where('K', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_INVAR_HEATPROOF.get()}))
            .where('Y', Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()}))
            .where('M', Predicates.heatingCoils())
            .where(
               'O',
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('A', Predicates.air())
            .where('#', Predicates.any())
            .build()
      )
      .beforeWorking(
         (machine, recipe) -> machine instanceof CoilWorkableElectricMultiblockMachine coilMachine
            && coilMachine.getCoilType().getCoilTemperature() >= recipe.data.m_128451_("ebf_temp")
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
      .register();
   public static final MultiblockMachineDefinition WOOD_DISTILLATION = GTRegistration.REGISTRATE
      .multiblock("wood_distillation", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .rotationState(RotationState.ALL)
      .recipeType(GTLRecipeTypes.WOOD_DISTILLATION_RECIPES)
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.wood_distillation")})}
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .recipeModifiers(
         new RecipeModifier[]{
            GTRecipeModifiers.PARALLEL_HATCH, (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK)
         }
      )
      .appearanceBlock(GTBlocks.CASING_INVAR_HEATPROOF)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "ABBBA IIIII IIIII ABBBA",
                  "AAAAA IIIII IIIII AAAAA",
                  "CAAAC CIIIC CIIIC CAAAC",
                  "C   C C   C C   C CCCCC",
                  "CCCCC C   C C   C CAAAC",
                  "CAAAC C   C CCCCC C   C",
                  "C   C CCCCC CJJJC CAAAC",
                  "CAAAC CJJJC CJJJC C   C",
                  "C   C CJJJC CJJJC CAAAC",
                  "CAAAC CJJJC CBBBC C   C",
                  "C   C CBBBC C   C CBBBC",
                  "CAAAC CCCCC CCCCC CDDDC",
                  "C   C C   C CDDDC CDDDC",
                  "CBBBC CCCCC CDDDC CDDDC",
                  "CDDDC CDDDC CDDDC CCCCC",
                  "CDDDC CDDDC CCCCC      ",
                  "CCCCC CDDDC            ",
                  "      CDDDC            ",
                  "      CCCCC            ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "BBBBB IKKKI IKKKI BBBBB",
                  "AAAAA IKKKI IKKKI AAAAA",
                  "ADDDA IDJDI IDJDI ADDDA",
                  " DDD   DJD   DJD  CDDDC",
                  "CDDDC  DJD   DJDC A   A",
                  "A   A  DJD  CDJDC  EEE ",
                  " EEE  CDJDC J   J A   A",
                  "A   A J   J J   J  EEE ",
                  " EEE  J   J J   J A   A",
                  "A   A J   J B   B  EEE ",
                  " EEE  B   B  DDD  B   B",
                  "A   A CDDDC CDDDC D   D",
                  " EEE   DDD  D   D D   D",
                  "B   B CEEEC D   D D   D",
                  "D   D D   D D   D CDDDC",
                  "D   D D   D CDDDC  DDD ",
                  "CDDDC D   D  DDD       ",
                  " DDD  D   D            ",
                  "      CDDDC            ",
                  "       DDD             "
               }
            )
            .aisle(
               new String[]{
                  "BBBBB IKKKI IKKKI BBBBB",
                  "AAGAA IKGKI IKGKI AAGAA",
                  "ADGDA IJGJI IJGJI ADGDA",
                  " DGD   JGJ   JGJ  CDGDC",
                  "CDGDC  JGJ   JGJC A G A",
                  "A G A  JGJ  CJGJC  EGE ",
                  " EGE  CJGJC J   J A G A",
                  "A G A J   J J   J  EGE ",
                  " EGE  J   J J   J A G A",
                  "A G A J   J B   B  EGE ",
                  " EGE  B   B  DGD  B   B",
                  "A G A CDGDC CDGDC D   D",
                  " EEE   DGD  D   GGD   D",
                  "B   B CEEEC D   D D   D",
                  "D   GGG   D D   D CD DC",
                  "D   D D   D CD DC  DDD ",
                  "CD DC D   D  DDD       ",
                  " DDD  D   D            ",
                  "      CD DC            ",
                  "       DDD             "
               }
            )
            .aisle(
               new String[]{
                  "BBBBB IKKKI IKKKI BBBBB",
                  "AAAAA IKGKI IKGKI AAAAA",
                  "ADDDA IDJDI IDJDI ADDDA",
                  " DDD   DJD   DGD  CDDDC",
                  "CDDDC  DJD   DJDC A   A",
                  "A   A  DJD  CDJDC  EEE ",
                  " EEE  CDJDC J   J A   A",
                  "A   A J   J J   J  EEE ",
                  " EEE  J   J J   J A   A",
                  "A   A J   J B   B  EEE ",
                  " EEE  B   B  DDD  B   B",
                  "A   A CDDDC CDDDC D   D",
                  " EEE   DDD  D   D D   D",
                  "B   B CEEEC D   D D   D",
                  "D   D D   D D   D CDDDC",
                  "D   D D   D CDDDC  DDD ",
                  "CDDDC D   D  DDD       ",
                  " DDD  D   D            ",
                  "      CDDDC            ",
                  "       DDD             "
               }
            )
            .aisle(
               new String[]{
                  "ABBBA IKKKI IKKKI ABBBA",
                  "AAAAA IIGII IIGII AAAAA",
                  "CAAAC CIIIC CIIIC CAAAC",
                  "C   C C   C C   C CCCCC",
                  "CCCCC C   C C   C CAAAC",
                  "CAAAC C   C CCCCC C   C",
                  "C   C CCCCC CJJJC CAAAC",
                  "CAAAC CJJJC CJJJC C   C",
                  "C   C CJJJC CJJJC CAAAC",
                  "CAAAC CJJJC CBBBC C   C",
                  "C   C CBBBC C   C CBBBC",
                  "CAAAC CCCCC CCCCC CDDDC",
                  "C   C C   C CDDDC CDDDC",
                  "CBBBC CCCCC CDDDC CDDDC",
                  "CDDDC CDDDC CDDDC CCCCC",
                  "CDDDC CDDDC CCCCC      ",
                  "CCCCC CDDDC            ",
                  "      CDDDC            ",
                  "      CCCCC            ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "                       ",
                  "        G     G        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "                       ",
                  "        G     G        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "                       ",
                  "        G     G        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "C   C  IIIIIIIII       ",
                  "CCCCC  IGIIIIIGI       ",
                  "CDDDC  IIIIIIIII       ",
                  "CDDDC  IIIIIIIII       ",
                  "CDDDC  IIIIIIIII       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "       IKKKKKKKI       ",
                  "CDDDC  I       I       ",
                  "D   D  I       I       ",
                  "D   D  I       I       ",
                  "D   D  IIIIIIIII       ",
                  " DDD     I   I         ",
                  "         I   I         ",
                  "         I   I         ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "       IKKKKKKKI       ",
                  "CDDDC  I       I       ",
                  "D   D  I       I       ",
                  "D   D  I       I       ",
                  "D   D  IIEIIIEII       ",
                  " DDD    IEI IEI        ",
                  "        IEI IEI        ",
                  "        IHI IHI        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "       IKKKKKKKI       ",
                  "CDDDC  I       I       ",
                  "D   D  I       I       ",
                  "D   D  I       I       ",
                  "D   D  IIIIIIIII       ",
                  " DDD     I   I         ",
                  "         I   I         ",
                  "         I   I         ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "       IILLLLLII       ",
                  "CDDDC  IILLMLLII       ",
                  "D   D  IILLLLLII       ",
                  "D   D  IIIIIIIII       ",
                  "D   D  IIIIIIIII       ",
                  " DDD                   ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "                       ",
                  "CDDDC                  ",
                  "D   D                  ",
                  "D   D                  ",
                  "D   D                  ",
                  " DDD                   ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "C   C                  ",
                  "CCCCC                  ",
                  "CDNDC                  ",
                  "CDNDC                  ",
                  "CDDDC                  ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .where("M", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where("I", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_INVAR_HEATPROOF.get()}))
            .where("N", Predicates.blocks(new Block[]{(Block)GTBlocks.HERMETIC_CASING_HV.get()}))
            .where("J", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_EVAPORATION.get()}))
            .where("E", Predicates.blocks(new Block[]{(Block)GTBlocks.FILTER_CASING.get()}))
            .where(
               "L",
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_INVAR_HEATPROOF.get()})
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).setMaxGlobalLimited(2).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_FLUIDS}).setMaxGlobalLimited(2).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS}).setMaxGlobalLimited(2).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_FLUIDS}).setMaxGlobalLimited(6).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS}).setMaxGlobalLimited(2).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.PARALLEL_HATCH}).setMaxGlobalLimited(1).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.MAINTENANCE}).setExactLimit(1))
            )
            .where("B", Predicates.blocks(new Block[]{(Block)GCyMBlocks.HEAT_VENT.get()}))
            .where("H", Predicates.abilities(new PartAbility[]{PartAbility.MUFFLER}))
            .where("D", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where("G", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where("K", Predicates.blocks(new Block[]{(Block)GTBlocks.FIREBOX_STEEL.get()}))
            .where("C", Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)}))
            .where("A", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get()}))
            .where(" ", Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_heatproof"), GTCEu.id("block/multiblock/electric_blast_furnace"))
      .register();
   public static final MultiblockMachineDefinition DESULFURIZER = GTRegistration.REGISTRATE
      .multiblock("desulfurizer", x$0 -> new WorkableElectricParallelHatchMultipleRecipesMachine(x$0))
      .rotationState(RotationState.ALL)
      .recipeType(GTLRecipeTypes.DESULFURIZER_RECIPES)
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.multiple_recipes.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.desulfurizer")})})
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .appearanceBlock(GTBlocks.CASING_STAINLESS_CLEAN)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(new String[]{"CCCCCIIIIIII", "CCCCCILILILI", "CCCCCILILILI", "            ", "            ", "            "})
            .aisle(new String[]{"CCCCCIIIIIII", "CGCCCXXXXXXI", "CCCCCILILILI", "  CCCILILILI", "  CCCIIIIIII", "            "})
            .aisle(new String[]{"CCCCCIIIIIII", "CGGGGILILILI", "CCCCCILILILI", "  CXXXXXXXXI", "  CCCIIIIIII", "            "})
            .aisle(new String[]{"CCDDDIIIIIII", "CGDPDXXXXXXI", "CCDDDILILILI", "  DDDILILILI", "  DDDIIIIIII", "  DDD       "})
            .aisle(new String[]{"CCCCCIIIIIII", "CCDPDILILILI", "CCDPDILILILI", "  DPD       ", "  DPD       ", "  DDD       "})
            .aisle(new String[]{"  DDD       ", "  D~D       ", "  DDD       ", "  DDD       ", "  DDD       ", "  DDD       "})
            .where("~", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where("I", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_INVAR_HEATPROOF.get()}))
            .where("X", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_PIPE.get()}))
            .where("P", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_BRONZE_PIPE.get()}))
            .where("G", Predicates.blocks(new Block[]{(Block)GTBlocks.HERMETIC_CASING_MV.get()}))
            .where(
               "D",
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()})
                  .setMinGlobalLimited(36)
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where("L", Predicates.blocks(new Block[]{(Block)GTBlocks.COIL_KANTHAL.get()}))
            .where("C", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STEEL_SOLID.get()}))
            .where(" ", Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/large_chemical_reactor"))
      .register();
   public static final MultiblockMachineDefinition PRIMITIVE_VOID_ORE = ConfigHolder.INSTANCE.enablePrimitiveVoidOre
      ? GTRegistration.REGISTRATE
         .multiblock("primitive_void_ore", x$0 -> new PrimitiveOreMachine(x$0))
         .langValue("Primitive Void Ore")
         .tooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.primitive_void_ore_random_output")})
         .tooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.supports_dimensions")})
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .rotationState(RotationState.ALL)
         .recipeType(GTLRecipeTypes.PRIMITIVE_VOID_ORE_RECIPES)
         .appearanceBlock(() -> Blocks.f_50493_)
         .pattern(
            definition -> FactoryBlockPattern.start()
               .aisle(new String[]{"XXX", "XXX", "XXX"})
               .aisle(new String[]{"XXX", "XAX", "XXX"})
               .aisle(new String[]{"XXX", "XSX", "XXX"})
               .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
               .where(
                  'X',
                  Predicates.blocks(new Block[]{Blocks.f_50493_})
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS}))
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_FLUIDS}))
               )
               .where('A', Predicates.air())
               .build()
         )
         .workableCasingRenderer(new ResourceLocation("minecraft:block/dirt"), GTCEu.id("block/multiblock/gcym/large_extractor"))
         .register()
      : null;
   public static final MultiblockMachineDefinition LARGE_FRAGMENT_WORLD_COLLECTION_MACHINE = ConfigHolder.INSTANCE.enableSkyBlokeMode
      ? GTRegistration.REGISTRATE
         .multiblock("large_fragment_world_collection_machine", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
         .langValue("Large fragment world collection machine")
         .tooltips(new Component[]{Component.m_237115_("gtlcore.machine.large_fragment_world_collection.sky_block_mode")})
         .tooltips(new Component[]{Component.m_237115_("gtlcore.machine.large_fragment_world_collection.energy_multiplier")})
         .tooltips(new Component[]{Component.m_237115_("gtlcore.machine.large_fragment_world_collection.duration_multiplier")})
         .tooltips(new Component[]{Component.m_237115_("gtlcore.machine.large_fragment_world_collection.max_parallel")})
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .recipeModifiers(
            new RecipeModifier[]{
               (machine, recipe, params, result) -> GTLRecipeModifiers.reduction(machine, recipe, 256.0, 0.25),
               (machine, recipe, params, result) -> (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe, 64, false).getFirst()
            }
         )
         .rotationState(RotationState.ALL)
         .recipeType(GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION)
         .appearanceBlock(GTBlocks.CASING_TITANIUM_STABLE)
         .pattern(
            definition -> FactoryBlockPattern.start()
               .aisle(new String[]{"AAA", "AXA", "XXX", "XXX", "XXX", "AXA", "AAA"})
               .aisle(new String[]{"AOA", "XXX", "XXX", "XXX", "XXX", "XXX", "AIA"})
               .aisle(new String[]{"AAA", "AXA", "XXX", "XSX", "XXX", "AXA", "AAA"})
               .where('S', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
               .where(
                  'X',
                  Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TITANIUM_STABLE.get()})
                     .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).setExactLimit(1))
               )
               .where('I', Predicates.abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS}))
               .where('O', Predicates.abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS}))
               .where('A', Predicates.any())
               .build()
         )
         .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_stable_titanium"), GTCEu.id("block/multiblock/gcym/large_extractor"))
         .register()
      : null;

   public static void init() {
   }
}
