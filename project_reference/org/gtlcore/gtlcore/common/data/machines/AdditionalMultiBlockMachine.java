package org.gtlcore.gtlcore.common.data.machines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
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
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.machine.multiblock.GTLPartAbility;
import org.gtlcore.gtlcore.api.machine.multiblock.MolecularAssemblerMultiblockMachine;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.utils.Registries;

public class AdditionalMultiBlockMachine {
   public static final MultiblockMachineDefinition ADVANCED_RARE_EARTH_CENTRIFUGAL = GTRegistration.REGISTRATE
      .multiblock("advanced_rare_earth_centrifugal", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .rotationState(RotationState.NON_Y_AXIS)
      .recipeType(GTLRecipeTypes.RARE_EARTH_CENTRIFUGAL_RECIPES)
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.laser.tooltip")})
      .tooltips(
         new Component[]{
            Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.rare_earth_centrifugal")})
         }
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .recipeModifiers(
         new RecipeModifier[]{
            GTRecipeModifiers.PARALLEL_HATCH, (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTLBlocks.SPS_CASING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "   AAAAAA     AAAAAA   ",
                  "     B           B     ",
                  "     B           B     ",
                  "   CCCCC       CCCCC   ",
                  "     B           B     ",
                  "     B           B     ",
                  "   CCCCC       CCCCC   ",
                  "     B           B     ",
                  "     B           B     ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "  AADDDDA     ADDDDAA  ",
                  "                       ",
                  "                       ",
                  "  C     C     C     C  ",
                  "                       ",
                  "                       ",
                  "  C     C     C     C  ",
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
                  " AADDDDDAA   AADDDDDAA ",
                  "  B  D  B     B  D  B  ",
                  "  B  E  B     B  E  B  ",
                  " CB EEE BC   CB EEE BC ",
                  "  B EEE B     B EEE B  ",
                  "  B EEE B     B EEE B  ",
                  " CB EEE BC   CB EEE BC ",
                  "  B EEE B     B EEE B  ",
                  "  B  E  B     B  E  B  ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "AADDDDDDDAAAAADDDDDDDAA",
                  "     D           D     ",
                  "   EEEEE       EEEEE   ",
                  "C  EEEEE  C C  EEEEE  C",
                  "   E F E       E F E   ",
                  "   E F E       E F E   ",
                  "C  E F E  C C  E F E  C",
                  "   EEEEE       EEEEE   ",
                  "   EEEEE       EEEEE   ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "ADDDDDDDDDDDDDDDDDDDDDA",
                  "    DDD         DDD    ",
                  "   EEEEE       EEEEE   ",
                  "C EEGGGEE C C EEGGGEE C",
                  "  E H H E     E H H E  ",
                  "  E H H E     E H H E  ",
                  "C E H H E C C E H H E C",
                  "  EEGGGEE     EEGGGEE  ",
                  "   EEEEE       EEEEE   ",
                  "    DDD         DDD    ",
                  "     III       III     ",
                  "       IIIIIIIII       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "ADDDDDDDDDDDDDDDDDDDDDA",
                  "B DDDDDDD B B DDDDDDD B",
                  "B EEEEEEE B B EEEEEEE B",
                  "C EEGGGEE C C EEGGGEE C",
                  "B EF G FE B B EF G FE B",
                  "B EF G FE B B EF G FE B",
                  "C EF G FE C C EF G FE C",
                  "B EEGGGEE B B EEGGGEE B",
                  "B EEEGEEE B B EEEGEEE B",
                  "    D A         A D    ",
                  "     AAA       AAA     ",
                  "       AAAAIAAAA       ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "ADDDDDDDDDDDDDDDDDDDDDA",
                  "    DDD         DDD    ",
                  "   EEEEE       EEEEE   ",
                  "C EEGGGEE C C EEGGGEE C",
                  "  E H H E     E H H E  ",
                  "  E H H E     E H H E  ",
                  "C E H H E C C E H H E C",
                  "  EEGGGEE     EEGGGEE  ",
                  "   EEEEE       EEEEE   ",
                  "    DDD         DDD    ",
                  "     III       III     ",
                  "       IIIAIAIII       ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "ADDDDDDDDDDDDDDDDDDDDDA",
                  "     D           D     ",
                  "   EEEEE       EEEEE   ",
                  "C  EEEEE  C C  EEEEE  C",
                  "   E F E       E F E   ",
                  "   E F E       E F E   ",
                  "C  E F E  C C  E F E  C",
                  "   EEEEE       EEEEE   ",
                  "   EEEEE       EEEEE   ",
                  "                       ",
                  "                       ",
                  "          AIA          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "ADDDDDDDDDDDDDDDDDDDDDA",
                  "  B  D  B  A  B  D  B  ",
                  "  B     B  A  B     B  ",
                  " CB EEE BC A CB EEE BC ",
                  "  B EEE B  A  B EEE B  ",
                  "  B EEE B  A  B EEE B  ",
                  " CB EEE BC A CB EEE BC ",
                  "  B EEE B  A  B EEE B  ",
                  "  B  E  B  A  B  E  B  ",
                  "           A           ",
                  "           A           ",
                  "          AAA          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "AADDDDDDDDDDDDDDDDDDDAA",
                  "          AAA          ",
                  "          AAA          ",
                  "  C     C AAA C     C  ",
                  "          AAA          ",
                  "          AAA          ",
                  "  C     C AAA C     C  ",
                  "          AAA          ",
                  "          AAA          ",
                  "          AAA          ",
                  "          AAA          ",
                  "          AAA          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  " AAAADDDDDDDDDDDDDAAAA ",
                  "     B     A     B     ",
                  "     B     A     B     ",
                  "   CCCCC   A   CCCCC   ",
                  "     B     A     B     ",
                  "     B     A     B     ",
                  "   CCCCC   A   CCCCC   ",
                  "     B     A     B     ",
                  "     B     A     B     ",
                  "           A           ",
                  "           A           ",
                  "          IAI          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "    AADDDDDDDDDDDAA    ",
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
                  "          IAI          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "     ADDDDDDDDDDDA     ",
                  "           B           ",
                  "           B           ",
                  "         CCCCC         ",
                  "           B           ",
                  "           B           ",
                  "         CCCCC         ",
                  "           B           ",
                  "           B           ",
                  "                       ",
                  "                       ",
                  "          IAI          ",
                  "          DDD          "
               }
            )
            .aisle(
               new String[]{
                  "     AADDDDDDDDDAA     ",
                  "                       ",
                  "                       ",
                  "        C     C        ",
                  "                       ",
                  "                       ",
                  "        C     C        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "          IAI          ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "      AADDDDDDDAA      ",
                  "        B  D  B        ",
                  "        B  E  B        ",
                  "       CB EEE BC       ",
                  "        B EEE B        ",
                  "        B EEE B        ",
                  "       CB EEE BC       ",
                  "        B EEE B        ",
                  "        B  E  B        ",
                  "                       ",
                  "                       ",
                  "          IAI          ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "      ADDDDDDDDDA      ",
                  "           D           ",
                  "         EEEEE         ",
                  "      C  EEEEE  C      ",
                  "         E F E         ",
                  "         E F E         ",
                  "      C  E F E  C      ",
                  "         EEEEE         ",
                  "         EEEEE         ",
                  "                       ",
                  "          IAI          ",
                  "          IAI          ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "     AADDDDDDDDDAA     ",
                  "          DDD          ",
                  "         EEEEE         ",
                  "      C EEGGGEE C      ",
                  "        E H H E        ",
                  "        E H H E        ",
                  "      C E H H E C      ",
                  "        EEGGGEE        ",
                  "         EEEEE         ",
                  "          DAD          ",
                  "          IAI          ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "     ADDDDDDDDDDDA     ",
                  "      B DDDDDDD B      ",
                  "      B EEEEEEE B      ",
                  "      C EEGGGEE C      ",
                  "      B EF G FE B      ",
                  "      B EF G FE B      ",
                  "      C EF G FE C      ",
                  "      B EEGGGEE B      ",
                  "      B EEEGEEE B      ",
                  "          DGD          ",
                  "          IAI          ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "     ADDDDDDDDDDDA     ",
                  "          DDD          ",
                  "         EEEEE         ",
                  "      C EEGGGEE C      ",
                  "        E H H E        ",
                  "        E H H E        ",
                  "      C E H H E C      ",
                  "        EEGGGEE        ",
                  "         EEEEE         ",
                  "          DDD          ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "     ADDDDDDDDDDDA     ",
                  "           D           ",
                  "         EEEEE         ",
                  "      C  EEEEE  C      ",
                  "         E F E         ",
                  "         E F E         ",
                  "      C  E F E  C      ",
                  "         EEEEE         ",
                  "         EEEEE         ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "     AADDDDDDDDDAA     ",
                  "        B  D  B        ",
                  "        B     B        ",
                  "       CB EEE BC       ",
                  "        B EEE B        ",
                  "        B EEE B        ",
                  "       CB EEE BC       ",
                  "        B EEE B        ",
                  "        B  E  B        ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "      AAADDDDDAAA      ",
                  "                       ",
                  "                       ",
                  "        C     C        ",
                  "                       ",
                  "                       ",
                  "        C     C        ",
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
                  "        AAAAAAA        ",
                  "          ABA          ",
                  "          ABA          ",
                  "         CCCCC         ",
                  "           B           ",
                  "           B           ",
                  "         CCCCC         ",
                  "           B           ",
                  "           B           ",
                  "                       ",
                  "                       ",
                  "                       ",
                  "                       "
               }
            )
            .aisle(
               new String[]{
                  "          AAA          ",
                  "          AJA          ",
                  "          AAA          ",
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
            .where("J", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where(
               "A",
               Predicates.blocks(new Block[]{(Block)GTLBlocks.SPS_CASING.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.PARALLEL_HATCH}).setMaxGlobalLimited(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.MAINTENANCE}).setExactLimit(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_LASER}).setExactLimit(1))
            )
            .where("B", Predicates.blocks(new Block[]{(Block)GTLBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()}))
            .where("C", Predicates.blocks(new Block[]{(Block)GTLBlocks.ADVANCED_FUSION_COIL.get()}))
            .where("D", Predicates.blocks(new Block[]{(Block)GTLBlocks.HYPER_MECHANICAL_CASING.get()}))
            .where("E", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_HSSE_STURDY.get()}))
            .where("F", Predicates.blocks(new Block[]{(Block)GTLBlocks.HYPER_CORE.get()}))
            .where("G", Predicates.blocks(new Block[]{Registries.getBlock("kubejs:neutronium_gearbox")}))
            .where("H", Predicates.blocks(new Block[]{Registries.getBlock("kubejs:neutronium_pipe_casing")}))
            .where("I", Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.HastelloyX)}))
            .where(" ", Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTLCore.id("block/casings/sps_casing"), GTCEu.id("block/multiblock/gcym/large_centrifuge"))
      .register();
   public static final MultiblockMachineDefinition ADVANCED_VACUUM_DRYING_FURNACE = GTRegistration.REGISTRATE
      .multiblock("advanced_vacuum_drying_furnace", CoilWorkableElectricMultiblockMachine::new)
      .rotationState(RotationState.NON_Y_AXIS)
      .recipeType(GTLRecipeTypes.VACUUM_DRYING_RECIPES)
      .recipeType(GTLRecipeTypes.DEHYDRATOR_RECIPES)
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{0.5})})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.0")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.electric_blast_furnace.tooltip.2")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.perfect_oc")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.laser.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.coil_parallel")})
      .tooltips(
         new Component[]{
            Component.m_237110_(
               "gtceu.machine.available_recipe_map_2.tooltip",
               new Object[]{Component.m_237115_("gtceu.vacuum_drying"), Component.m_237115_("gtceu.dehydrator")}
            )
         }
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .appearanceBlock(GTBlocks.HIGH_POWER_CASING)
      .recipeModifiers(
         new RecipeModifier[]{
            (machine, recipe, params, result) -> {
               if (machine instanceof CoilWorkableElectricMultiblockMachine coilMachine) {
                  GTRecipe recipe1 = (GTRecipe)GTRecipeModifiers.accurateParallel(
                        coilMachine, recipe, (int)Math.min(2.147483647E9, Math.pow(2.0, coilMachine.getCoilType().getCoilTemperature() / 900.0)), false
                     )
                     .getFirst();
                  if (recipe1 != null) {
                     return RecipeHelper.applyOverclock(
                        OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1, coilMachine.getOverclockVoltage(), params, result
                     );
                  }
               }

               return null;
            },
            (machine, recipe, params, result) -> GTLRecipeModifiers.reduction(machine, recipe, 1.0, 0.5)
         }
      )
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "          AAAAAAA  ",
                  "   BBBBBBBBBBBBBB  ",
                  "   B         C     ",
                  "           CCCCC   ",
                  "          CCCCCCC  ",
                  "         CCCCCCCCC ",
                  "         CCCDDDCCC ",
                  "        CCCCDDDCCCC",
                  "         CCCDDDCCC ",
                  "         CCCCCCCCC ",
                  "          CCCCCCC  ",
                  "           CCCCC   ",
                  "             C     ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "  GGG       CCC B  ",
                  "  GBG      FFFFF   ",
                  "  GGG     FFFFFFF  ",
                  "  GGG    FFFFFFFFF ",
                  "  GGG   FFFFFFFFFFF",
                  "  GGG   FFFFFFFFFFF",
                  "  GGG   FFFFFFFFFFF",
                  "  GGG   FFFFFFFFFFF",
                  "   C    FFFFFFFFFFF",
                  "         FFFFFFFFF ",
                  "          FFFFFFF  ",
                  "           FFFFF   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  " GGGGG      CCC B  ",
                  " G B G     GGGGG   ",
                  " G   G    GGHIHGG  ",
                  " G   G   GG     GG ",
                  " G   G  GG       GG",
                  " G   G  GH       HG",
                  " G   G  GI       IG",
                  " G   G  GH       HG",
                  " CCCCC  GG       GG",
                  "   C     GG     GG ",
                  "          GGHIHGG  ",
                  "           GGGGG   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC B  ",
                  "G  B  G    GGGGG   ",
                  "G     G   GGHIHGG  ",
                  "G     G  GG     GG ",
                  "G     G GG       GG",
                  "G     G GH       HG",
                  "G     G GI       IG",
                  "G     G GH       HG",
                  " C   C  GG       GG",
                  "  CCC    GG     GG ",
                  "  JJJ     GGHIHGGA ",
                  "           GGGGG A ",
                  "          AAAAAAAA "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC B  ",
                  "G  B  G    GGGGG   ",
                  "G  B  G   GGHIHGG  ",
                  "G  B  G  GG     GG ",
                  "G  B  G GG       GG",
                  "G  B  G GH       HG",
                  "G  B  G GI       IG",
                  "G  B  G GH       HG",
                  "CC B CC GG       GG",
                  " CCBCC   GG     GG ",
                  "  JBJ     GGHIHGGA ",
                  "   BBBBBBBBGGGGG   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC B  ",
                  "G  B  G    GGGGG   ",
                  "G  B  G   GGHIHGG  ",
                  "G  B  G  GG     GG ",
                  "G  B  G GG       GG",
                  "G  B  G GH       HG",
                  "G  B  G GI       IG",
                  "G  B  G GH       HG",
                  "CC B CC GG       GG",
                  " CCBCC   GG     GG ",
                  "  JBJ     GGHIHGGA ",
                  "   BBBBBBBBGGGGG A ",
                  "          AAAAAAAA "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC B  ",
                  "G     G    GGGGG   ",
                  "G     G   GGHIHGG  ",
                  "G     G  GG     GG ",
                  "G     G GG       GG",
                  "G     G GH       HG",
                  "G     G GI       IG",
                  "G     G GH       HG",
                  " C   C  GG       GG",
                  "  CCC    GG     GG ",
                  "  JJJ     GGHIHGGA ",
                  "          BGGGGG   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  " GGGGG      CCC B  ",
                  " G   G     GGGGG   ",
                  " G   G    GGHIHGG  ",
                  " G   G   GG     GG ",
                  " G   G  GG       GG",
                  " G   G  GH       HG",
                  " G   G  GI       IG",
                  " G   G  GH       HG",
                  " CCCCC  GG       GG",
                  "   C     GG     GG ",
                  "          GGHIHGGA ",
                  "          BGGGGG A ",
                  "          AAAAAAAA "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "  GGG       CCC B  ",
                  "  GGG      GGGGGB  ",
                  "  GGG     GGHIHGB  ",
                  "  GGG    GG     BG ",
                  "  GGG   GG       GG",
                  "  GGG   GH       HG",
                  "  GGG   GI       IG",
                  "  GGG   GH       HG",
                  "   C    GG       GG",
                  "         GG     GG ",
                  "          GGHIHGGA ",
                  "          BGGGGG   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "          A CCC A  ",
                  "            CCC    ",
                  "           GGGGG   ",
                  "          GGHIHGG  ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGGA ",
                  "          BGGGGG A ",
                  "          AAAAAAAA "
               }
            )
            .aisle(
               new String[]{
                  "          A CCC A  ",
                  "            CCC    ",
                  "           GGGGG   ",
                  "          GGHIHGG  ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGGA ",
                  "          BGGGGG   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC    ",
                  "GGGGGGG    GGGGG   ",
                  "GGGGGGG   GGHIHGG  ",
                  "GGGGGGG  GG     GG ",
                  "GGGGGGG GG       GG",
                  "GGGGGGG HH       HH",
                  "GGGGGGG GI       IG",
                  "GGGGGGG HH       HH",
                  "GGGGGGG GG       GG",
                  "         GG     GG ",
                  "          GGHIHGGA ",
                  "          BGGGGG A ",
                  "          AAAAAAAA "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC A  ",
                  "GGGGGGG     CCC A  ",
                  "G     G    GGGGGA  ",
                  "G     G   GGHIHGA  ",
                  "G     G  GG     AG ",
                  "G     G GG       GG",
                  "G     G HH       HH",
                  "G     G GI       IG",
                  "G     G HH       HH",
                  "GGGGGGG GG       AG",
                  "  JJJ    GG     GA ",
                  "          GGHIHGGA ",
                  "          BGGGGG   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC   A CCC    ",
                  "GGGGGGG     CCC    ",
                  "G     G    GGGGG   ",
                  "G     G   GGHIHGG  ",
                  "G     G  GG     GG ",
                  "G     G GG       GG",
                  "G     G HH       HH",
                  "G     G GI       IG",
                  "G     G HH       HH",
                  "GGGAGGG GG       GG",
                  "  JAJ    GG     GG ",
                  "   A      GGHIHGG  ",
                  "   A      BGGGGG   ",
                  "   AAAAAAAA        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCCAAAA CCC    ",
                  "GGGGGGGA    CCC    ",
                  "G   AAAA   FFFFF   ",
                  "G     G   FGHIHGF  ",
                  "G     G  FG     GF ",
                  "G     G FG       GF",
                  "G     G FH       HF",
                  "G     G FI       IF",
                  "G     G FH       HF",
                  "GGGGGGG FG       GF",
                  "  JJJ    FG     GF ",
                  "          FGHIHGF  ",
                  "          BFFFFF   ",
                  "          A        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC  CCCCCCCCC ",
                  "GGGGGGG  CCCCCCCCC ",
                  "G     G  CCGGGGGCC ",
                  "G     G  CGGHIHGGC ",
                  "G     G  GG     GG ",
                  "G     G GG       GG",
                  "G     G HH       HH",
                  "G     G GI       IG",
                  "G     G HH       HH",
                  "GGGAGGG GG       GG",
                  "  JAJ    GG     GG ",
                  "   A      GGHIHGG  ",
                  "   A      BGGGGG   ",
                  "   AAAAAAAA        "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC  CCCCCCCCC ",
                  "GGGGGGG  CCCCCCCCC ",
                  "G     G  CCGGGGGCC ",
                  "G     G  CGGHIHGGC ",
                  "G     G  GG     GG ",
                  "G     G GG       GG",
                  "G     G HH       HH",
                  "G     G GI       IG",
                  "G     G HH       HH",
                  "GGGGGGG GG       GG",
                  "  JJJ    GG     GG ",
                  "          GGHIHGG  ",
                  "          BGGGGG   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "CCCCCCC  CCCCCCCCC ",
                  "GGGGGGG  CCCCCCCCC ",
                  "GGGGGGG  CCGGGGGCC ",
                  "GGGGGGG  CGGHIHGGC ",
                  "GGGGGGG  GG     GG ",
                  "GGGGGGG GG       GG",
                  "GGGGGGG HH       HH",
                  "GGGGGGG GI       IG",
                  "GGGGGGG HH       HH",
                  "GGGGGGG GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "          BGGGGG   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "         CCCCCCCCC ",
                  "         CCCCCCCCC ",
                  "         CCGGGGGCC ",
                  "         CGGHIHGGC ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "          BGGGGG   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "         CCCCCCCCC ",
                  "         CCCCCCCCC ",
                  "         CCGGGGGCC ",
                  "         CGGHIHGGC ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "          BGGGGG   ",
                  "          BBBB     "
               }
            )
            .aisle(
               new String[]{
                  "         CCCCCCCCC ",
                  "         CCCCCCCCC ",
                  "         CCGGGGGCC ",
                  "         CGGHIHGGC ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "           GGGGG   ",
                  "             B     "
               }
            )
            .aisle(
               new String[]{
                  "         CCCCCCCCC ",
                  "         CCCCCCCCC ",
                  "         CCGGGGGCC ",
                  "         CGGHIHGGC ",
                  "         GG     GG ",
                  "        GG       GG",
                  "        GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "           GGGGG   ",
                  "             B     "
               }
            )
            .aisle(
               new String[]{
                  "  CCCCC  CCCCCCCCC ",
                  "  KKKKK  CCCCCCCCC ",
                  "  KKKKK  CCGGGGGCC ",
                  "  KKKKK  CGGHIHGGC ",
                  "  KKKKK  GG     GG ",
                  "  KKKKK GG       GG",
                  "  KKKKK GH       HG",
                  "        GI       IG",
                  "        GH       HG",
                  "        GG       GG",
                  "         GG     GG ",
                  "          GGHIHGG  ",
                  "           GGGGG   ",
                  "             B     "
               }
            )
            .aisle(
               new String[]{
                  "  CCCCC  CCCCCCCCC ",
                  "  KKKKK  CCCCCCCCC ",
                  "  K   K  CCGGGGGCC ",
                  "  L   L  CGGHIHGGC ",
                  "  L   L  GG H H GG ",
                  "  L   L GG  H H  GG",
                  "  KKKKK GHHHHHHHHHG",
                  "        GI  H H  IG",
                  "        GHHHHHHHHHG",
                  "        GG  H H  GG",
                  "         GG H H GG ",
                  "          GGHIHGG  ",
                  "           GGBGG   ",
                  "             B     "
               }
            )
            .aisle(
               new String[]{
                  "  CCCCC            ",
                  "  KKKKK            ",
                  "  KMMMK    FFFFF   ",
                  "  KMNMK   FFOOOFF  ",
                  "  KMMMK  FFO   OFF ",
                  "  KMMMK FFO     OFF",
                  "  KKKKK PO       OF",
                  "        PO       OF",
                  "        PO       OF",
                  "        FFO     OFF",
                  "         FFO   OFF ",
                  "          FFOOOFF  ",
                  "           FFFFF   ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "                   ",
                  "                   ",
                  "            F F    ",
                  "           CCCCC   ",
                  "          CCCCCCC  ",
                  "         CCCCCCCCC ",
                  "        MCCCQQQCCCF",
                  "        MCCCQQQCCC ",
                  "        MCCCQQQCCCF",
                  "         CCCCCCCCC ",
                  "          CCCCCCC  ",
                  "           CCCCC   ",
                  "            F F    ",
                  "                   "
               }
            )
            .aisle(
               new String[]{
                  "                   ",
                  "                   ",
                  "                   ",
                  "                   ",
                  "                   ",
                  "                   ",
                  "        MMM        ",
                  "        MMM        ",
                  "        MMM        ",
                  "                   ",
                  "                   ",
                  "                   ",
                  "                   ",
                  "                   "
               }
            )
            .where("N", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where("A", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get()}))
            .where("B", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_TURBINE.get()}))
            .where("C", Predicates.blocks(new Block[]{(Block)GTLBlocks.IRIDIUM_CASING.get()}))
            .where("D", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_GRATE.get()}))
            .where("E", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where("F", Predicates.blocks(new Block[]{Registries.getBlock("kubejs:red_steel_casing")}))
            .where("G", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_CLEAN.get()}))
            .where("H", Predicates.heatingCoils())
            .where("I", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()}))
            .where("J", Predicates.blocks(new Block[]{ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Tungsten)}))
            .where("K", Predicates.blocks(new Block[]{(Block)GTBlocks.COMPUTER_CASING.get()}))
            .where("L", Predicates.blocks(new Block[]{(Block)GTBlocks.COMPUTER_HEAT_VENT.get()}))
            .where(
               "M",
               Predicates.blocks(new Block[]{(Block)GTBlocks.HIGH_POWER_CASING.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_LASER}).setExactLimit(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.MAINTENANCE}).setExactLimit(1))
            )
            .where("O", Predicates.blocks(new Block[]{(Block)GTBlocks.HERMETIC_CASING_LuV.get()}))
            .where("P", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()}))
            .where("Q", Predicates.blocks(new Block[]{(Block)GTBlocks.FILTER_CASING.get()}))
            .build()
      )
      .additionalDisplay(
         (controller, components) -> {
            if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
               components.add(
                  Component.m_237110_(
                        "gtceu.multiblock.parallel",
                        new Object[]{
                           Component.m_237113_(
                                 FormattingUtil.formatNumbers(Math.min(2147483647, (int)Math.pow(2.0, coilMachine.getCoilType().getCoilTemperature() / 900.0)))
                              )
                              .m_130940_(ChatFormatting.DARK_PURPLE)
                        }
                     )
                     .m_130940_(ChatFormatting.GRAY)
               );
               components.add(
                  Component.m_237110_(
                     "gtceu.multiblock.blast_furnace.max_temperature",
                     new Object[]{
                        Component.m_237115_(
                              FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() + 100L * Math.max(0, coilMachine.getTier() - 2))
                                 + "K"
                           )
                           .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED))
                     }
                  )
               );
            }
         }
      )
      .workableCasingRenderer(GTCEu.id("block/casings/hpca/high_power_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
      .register();
   public static final MultiblockMachineDefinition HUGE_INCUBATOR = GTRegistration.REGISTRATE
      .multiblock("huge_incubator", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .rotationState(RotationState.NON_Y_AXIS)
      .allowExtendedFacing(false)
      .recipeType(GTLRecipeTypes.INCUBATOR_RECIPES)
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.laser.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.perfect_oc")})
      .tooltips(new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.incubator")})})
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .recipeModifiers(
         new RecipeModifier[]{
            GTRecipeModifiers.PARALLEL_HATCH, (RecipeModifier)GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.PERFECT_OVERCLOCK_SUBTICK)
         }
      )
      .appearanceBlock(GTBlocks.CASING_PTFE_INERT)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "   ABBBBBA   CCC   ABBBBBA   ",
                  "   AAAAAAA   CCC   AAAAAAA   ",
                  "   A     A   CDC   A     A   ",
                  "             CDC             ",
                  "             CDC             ",
                  "             CCC             ",
                  "             CCC             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEEEEEEEEEEEEEEEEE ",
                  "  AAAAAAAAAEECCCEEAAAAAAAAA  ",
                  "   A     A EEC CEE A     A   ",
                  "   AAAAAAA   C C   AAAAAAA   ",
                  "   A     A   C C   A     A   ",
                  "             C C             ",
                  "             CFC             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEE E EEEEEEEEEEEE ",
                  " AAAAAAAAAAAECCCEAAAAAAAAAAA ",
                  "  GABBBBBAG EC CE GABBBBBAG  ",
                  "  GAHHHHHAG EC CE GAHHHHHAG  ",
                  "  GA     AG EC CE GA     AG  ",
                  "  GAAAAAAAG  C C  GAAAAAAAG  ",
                  "  G       G  CFC  G       G  ",
                  "  I       I   J   I       I  ",
                  "     HHH      J      HHH     ",
                  "    HHHHH     J     HHHHH    ",
                  "    HHHHH     J     HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    KKKKK           KKKKK    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "AEEEEEEEEEEEE E EEEEEEEEEEEEA",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "AAAA     AAAEC CEAAA     AAAA",
                  " AAAAAAAAAAAEC CEAAAAAAAAAAA ",
                  " AAABBBBBAAAEC CEAAABBBBBAAA ",
                  "  AAA   AAA EC CE AAA   AAA  ",
                  "            ECFCE            ",
                  "     HHH             HHH     ",
                  "    HHHHH           HHHHH    ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   KK   KK         KK   KK   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B       B EC CE B       B  ",
                  " AHA     AHAEC CEAHA     AHA ",
                  "   B     B  EC CE  B     B   ",
                  "  AA     AA EC CE AA     AA  ",
                  "     HHH    ECCCE    HHH     ",
                  "    HHHHH   ECCCE   HHHHH    ",
                  "   H     H  ECCCE  H     H   ",
                  "  HH     HH  CCC  HH     HH  ",
                  "  HH     HH   J   HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  KK     KK       KK     KK  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HHH   HHH       HHH   HHH  ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B  L L  B EC CE B  L L  B  ",
                  " AHA L L AHAEC CEAHA L L AHA ",
                  "   B L L B  EC CE  B L L B   ",
                  "  A  HHH  A EC CE A  HHH  A  ",
                  "    HHHHH   EC CE   HHHHH    ",
                  "   HHMMMHH  EC CE  HHMMMHH   ",
                  "  HH     HH EC CE HH     HH  ",
                  "  H       H  CCC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBBBH           HBBBH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEEEEEEEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B   N   B ECFCE B   N   B  ",
                  " AHA  N  AHAECFCEAHA  N  AHA ",
                  "   B  N  B  ECFCE  B  N  B   ",
                  "  A  HHH  A ECFCE A  HHH  A  ",
                  "    HHHHH   ECFCE   HHHHH    ",
                  "   HHMMMHH  ECFCE  HHMMMHH   ",
                  "  HH     HH ECFCE HH     HH  ",
                  "  H       H  CFC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBFBH           HBFBH    ",
                  "      O               O      ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B  L L  B ECCCE B  L L  B  ",
                  " AHA L L AHAECCCEAHA L L AHA ",
                  "   B L L B  ECCCE  B L L B   ",
                  "  A  HHH  A ECCCE A  HHH  A  ",
                  "    HHHHH   ECCCE   HHHHH    ",
                  "   HHMMMHH  ECCCE  HHMMMHH   ",
                  "  HH     HH ECCCE HH     HH  ",
                  "  H       H  CCC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBBBH           HBBBH    ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAEEEEEAAAAAAAAAAAA",
                  "  B       B EEEEE B       B  ",
                  " AHA     AHAEEEEEAHA     AHA ",
                  "   B     B  EEEEE  B     B   ",
                  "  AA     AA EEEEE AA     AA  ",
                  "     HHH    EEEEE    HHH     ",
                  "    HHHHH   EEEEE   HHHHH    ",
                  "   H     H  EEEEE  H     H   ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH   J   HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  KK     KK       KK     KK  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HHH   HHH       HHH   HHH  ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "AEEEEEEEEEEE  E  EEEEEEEEEEEA",
                  "AAAAAAAAAAAA     AAAAAAAAAAAA",
                  "AAAA     AAA     AAA     AAAA",
                  " AAAAAAAAAAA     AAAAAAAAAAA ",
                  " AAABBBBBAAA     AAABBBBBAAA ",
                  "  AAA   AAA       AAA   AAA  ",
                  "                             ",
                  "     HHH             HHH     ",
                  "    HHHHH           HHHHH    ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   KK   KK         KK   KK   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEEEEEEEEEEEEEEEEE ",
                  " AAAAAAAAAAA LLL AAAAAAAAAAA ",
                  "  GABBBBBAG  LLL  GABBBBBAG  ",
                  "  GAHHHHHAG  LLL  GAHHHHHAG  ",
                  "  GA     AG  LLL  GA     AG  ",
                  "  GAAAAAAAG  LLL  GAAAAAAAG  ",
                  "  G       G  LLL  G       G  ",
                  "  I       I  LLL  I       I  ",
                  "     HHH     LLL     HHH     ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    KKKKK    LLL    KKKKK    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEE   E   EEEEEEEEEE ",
                  "  AAAAAAAAA LLBLL AAAAAAAAAE ",
                  "   A     A  LLFLL  A     A E ",
                  "   AAAAAAA  LLFLL  AAAAAAA   ",
                  "   A     A  LLFLL  A     A   ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "             LLL             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEE E   E   E EEEEEEEE ",
                  " EEEEEEEE  LLBBBLL  EEEEEEEE ",
                  " EEEEEEEE  LL   LL  EEEEEEEE ",
                  "  EEEEEEE  LL   LL  EEEEEEE  ",
                  "  EEEEEEE  LL   LL  EEEEEEE  ",
                  "   EEEEEE  LL   LL  EEEEEE   ",
                  "   EEEEEE  LL   LL  EEEEEE   ",
                  "    EEEEE  LL   LL  EEEEE    ",
                  "    EEEEE  LL   LL  EEEEE    ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL F LL           ",
                  "            LLLLL            ",
                  "             KKK             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "CE    E   E   E   E   E    EC",
                  "CCCCCCCCE LLBBBBBLL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  I  LL ECCCCCCCC",
                  "    CCCCE LL  I  LL ECCCC    ",
                  "    CCCCE LL  I  LL ECCCC    ",
                  "    CCCC  LL     LL  CCCC    ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL  F  LL          ",
                  "           LLKKKLL           ",
                  "            KPPPK            ",
                  "             P P             ",
                  "             P P             ",
                  "      O      P P      O      ",
                  "             KKK             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "CEEEEEEEEEEEEEEEEEEEEEEEEEEEC",
                  "CCCCCCCCE LBBBBBBBL ECCCCCCCC",
                  "D     FCE LF NRN FL ECF     D",
                  "D     FCE LF NRN FL ECF     D",
                  "D     FCE LF NRN FL ECF     D",
                  "C     FCE LF NRN FL ECF     C",
                  "CFFFC FCE LF IRI FL ECF CFFFC",
                  "  J C FCE LF IRI FL ECF C J  ",
                  "  J C FCE LF IRI FL ECF C J  ",
                  "  J CCFC  LF  F  FL  CFCC J  ",
                  "  JJJJJJJJLF  F  FLJJJJJJJJ  ",
                  "         JLF  F  FLJ         ",
                  "         JLF  F  FLJ         ",
                  "          LF  F  FL          ",
                  "          LF  F  FL          ",
                  "          LF  F  FL          ",
                  "          LF  F  FL          ",
                  "          LF  F  FL          ",
                  "          LFFFFFFFL          ",
                  "           LLFFFLL           ",
                  "            KOPOK            ",
                  "             OPO             ",
                  "             OPO             ",
                  "      OOOOOOOOPOOOOOOOO      ",
                  "             KKK             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "CE    E   E   E   E   E    EC",
                  "CCCCCCCCE LLBBBBBLL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  N  LL ECCCCCCCC",
                  "CCCCCCCCE LL  I  LL ECCCCCCCC",
                  "    CCCCE LL  I  LL ECCCC    ",
                  "    CCCCE LL  I  LL ECCCC    ",
                  "    CCCC  LL     LL  CCCC    ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL     LL          ",
                  "          LL  F  LL          ",
                  "           LLKKKLL           ",
                  "            KPPPK            ",
                  "             P P             ",
                  "             P P             ",
                  "      O      P P      O      ",
                  "             KKK             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEE E   E   E EEEEEEEE ",
                  " EEEEEEEE  LLBBBLL  EEEEEEEE ",
                  " EEEEEEEE  LL   LL  EEEEEEEE ",
                  "  EEEEEEE  LL   LL  EEEEEEE  ",
                  "  EEEEEEE  LL   LL  EEEEEEE  ",
                  "   EEEEEE  LL   LL  EEEEEE   ",
                  "   EEEEEE  LL   LL  EEEEEE   ",
                  "    EEEEE  LL   LL  EEEEE    ",
                  "    EEEEE  LL   LL  EEEEE    ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL   LL           ",
                  "           LL F LL           ",
                  "            LLLLL            ",
                  "             KKK             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEE   E   EEEEEEEEEE ",
                  " EAAAAAAAAA LLBLL AAAAAAAAA  ",
                  " E A     A  LLFLL  A     A   ",
                  "   AAAAAAA  LLFLL  AAAAAAA   ",
                  "   A     A  LLFLL  A     A   ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "            LLFLL            ",
                  "             LLL             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEEEEEEEEEEEEEEEEE ",
                  " AAAAAAAAAAA LLL AAAAAAAAAAA ",
                  "  GABBBBBAG  LLL  GABBBBBAG  ",
                  "  GAHHHHHAG  LLL  GAHHHHHAG  ",
                  "  GA     AG  LLL  GA     AG  ",
                  "  GAAAAAAAG  LLL  GAAAAAAAG  ",
                  "  G       G  LLL  G       G  ",
                  "  I       I  LLL  I       I  ",
                  "     HHH     LLL     HHH     ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    KKKKK    LLL    KKKKK    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH    LLL    HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "AEEEEEEEEEEE  E  EEEEEEEEEEEA",
                  "AAAAAAAAAAAA     AAAAAAAAAAAA",
                  "AAAA     AAA     AAA     AAAA",
                  " AAAAAAAAAAA     AAAAAAAAAAA ",
                  " AAABBBBBAAA     AAABBBBBAAA ",
                  "  AAA   AAA       AAA   AAA  ",
                  "                             ",
                  "     HHH             HHH     ",
                  "    HHHHH           HHHHH    ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   KK   KK         KK   KK   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAEEEEEAAAAAAAAAAAA",
                  "  B       B EEEEE B       B  ",
                  " AHA     AHAEEEEEAHA     AHA ",
                  "   B     B  EEEEE  B     B   ",
                  "  AA     AA EEEEE AA     AA  ",
                  "     HHH    EEEEE    HHH     ",
                  "    HHHHH   EEEEE   HHHHH    ",
                  "   H     H  EEEEE  H     H   ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH   J   HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  KK     KK       KK     KK  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HHH   HHH       HHH   HHH  ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B  L L  B ECCCE B  L L  B  ",
                  " AHA L L AHAECCCEAHA L L AHA ",
                  "   B L L B  ECCCE  B L L B   ",
                  "  A  HHH  A ECCCE A  HHH  A  ",
                  "    HHHHH   ECCCE   HHHHH    ",
                  "   HHMMMHH  ECCCE  HHMMMHH   ",
                  "  HH     HH ECCCE HH     HH  ",
                  "  H       H  CCC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBBBH           HBBBH    ",
                  "                             ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEEEEEEEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B   N   B ECFCE B   N   B  ",
                  " AHA  N  AHAECFCEAHA  N  AHA ",
                  "   B  N  B  ECFCE  B  N  B   ",
                  "  A  HHH  A ECFCE A  HHH  A  ",
                  "    HHHHH   ECFCE   HHHHH    ",
                  "   HHMMMHH  ECFCE  HHMMMHH   ",
                  "  HH     HH ECFCE HH     HH  ",
                  "  H       H  CFC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBFBH           HBFBH    ",
                  "      O               O      ",
                  "      O               O      ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B  L L  B EC CE B  L L  B  ",
                  " AHA L L AHAEC CEAHA L L AHA ",
                  "   B L L B  EC CE  B L L B   ",
                  "  A  HHH  A EC CE A  HHH  A  ",
                  "    HHHHH   EC CE   HHHHH    ",
                  "   HHMMMHH  EC CE  HHMMMHH   ",
                  "  HH     HH EC CE HH     HH  ",
                  "  H       H  CCC  H       H  ",
                  "  H       H   J   H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  K       K       K       K  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  H       H       H       H  ",
                  "  HH     HH       HH     HH  ",
                  "   HH   HH         HH   HH   ",
                  "    HBBBH           HBBBH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "BEEEEEEEEEEEE E EEEEEEEEEEEEB",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "  B       B EC CE B       B  ",
                  " AHA     AHAEC CEAHA     AHA ",
                  "   B     B  EC CE  B     B   ",
                  "  AA     AA EC CE AA     AA  ",
                  "     HHH    ECCCE    HHH     ",
                  "    HHHHH   ECCCE   HHHHH    ",
                  "   H     H  ECCCE  H     H   ",
                  "  HH     HH  CCC  HH     HH  ",
                  "  HH     HH   J   HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  KK     KK       KK     KK  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HH     HH       HH     HH  ",
                  "  HHH   HHH       HHH   HHH  ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "AEEEEEEEEEEEE E EEEEEEEEEEEEA",
                  "AAAAAAAAAAAAECCCEAAAAAAAAAAAA",
                  "AAAA     AAAEC CEAAA     AAAA",
                  " AAAAAAAAAAAEC CEAAAAAAAAAAA ",
                  " AAABBBBBAAAEC CEAAABBBBBAAA ",
                  "  AAA   AAA EC CE AAA   AAA  ",
                  "            ECFCE            ",
                  "     HHH             HHH     ",
                  "    HHHHH           HHHHH    ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH    J    HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   KK   KK         KK   KK   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HH   HH         HH   HH   ",
                  "   HHHHHHH         HHHHHHH   ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEE E EEEEEEEEEEEE ",
                  " AAAAAAAAAAAECCCEAAAAAAAAAAA ",
                  "  GABBBBBAG EC CE GABBBBBAG  ",
                  "  GAHHHHHAG EC CE GAHHHHHAG  ",
                  "  GA     AG EC CE GA     AG  ",
                  "  GAAAAAAAG  C C  GAAAAAAAG  ",
                  "  G       G  CFC  G       G  ",
                  "  I       I   J   I       I  ",
                  "     HHH      J      HHH     ",
                  "    HHHHH     J     HHHHH    ",
                  "    HHHHH     J     HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    KKKKK           KKKKK    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "    HHHHH           HHHHH    ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  " EEEEEEEEEEEEEEEEEEEEEEEEEEE ",
                  "  AAAAAAAAAEECCCEEAAAAAAAAA  ",
                  "   A     A EEC CEE A     A   ",
                  "   AAAAAAA   C C   AAAAAAA   ",
                  "   A     A   C C   A     A   ",
                  "             C C             ",
                  "             CFC             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "   ABBBBBA   CCC   ABBBBBA   ",
                  "   AAAAAAA   CCC   AAAAAAA   ",
                  "   A     A   CDC   A     A   ",
                  "             CDC             ",
                  "             CDC             ",
                  "             CCC             ",
                  "             CCC             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .aisle(
               new String[]{
                  "             CCC             ",
                  "             C~C             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             ",
                  "                             "
               }
            )
            .where("~", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where("A", Predicates.blocks(new Block[]{(Block)GTLBlocks.IRIDIUM_CASING.get()}))
            .where("B", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:sterilizing_filter_casing")}))
            .where(
               "C",
               Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_PTFE_INERT.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.autoAbilities(true, false, true))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_LASER}).setMaxGlobalLimited(1))
            )
            .where("D", Predicates.blocks(new Block[]{(Block)GTBlocks.HIGH_POWER_CASING.get()}))
            .where("E", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_HSSE_STURDY.get()}))
            .where("F", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()}))
            .where("G", Predicates.blocks(new Block[]{(Block)GTLBlocks.MOLECULAR_CASING.get()}))
            .where("H", Predicates.blocks(new Block[]{(Block)GTBlocks.CLEANROOM_GLASS.get()}))
            .where("I", Predicates.blocks(new Block[]{Registries.getBlock("kubejs:containment_field_generator")}))
            .where("J", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_STAINLESS_TURBINE.get()}))
            .where("K", Predicates.blocks(new Block[]{(Block)GTLBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()}))
            .where("L", Predicates.blocks(new Block[]{(Block)GTBlocks.PLASTCRETE.get()}))
            .where("M", Predicates.blocks(new Block[]{Blocks.f_50056_}))
            .where("N", Predicates.blocks(new Block[]{(Block)GTLBlocks.HYPER_CORE.get()}))
            .where("O", Predicates.blocks(new Block[]{(Block)GTBlocks.CASING_TUNGSTENSTEEL_TURBINE.get()}))
            .where("P", Predicates.frames(new Material[]{GTMaterials.Naquadria}))
            .where("R", Predicates.blocks(new Block[]{Registries.getBlock("kubejs:containment_field_generator")}))
            .where(" ", Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
      .register();
   public static final MultiblockMachineDefinition ADVANCED_NEUTRON_ACTIVATOR = GTRegistration.REGISTRATE
      .multiblock("advanced_neutron_activator", x$0 -> new WorkableElectricMultiblockMachine(x$0, new Object[0]))
      .rotationState(RotationState.NON_Y_AXIS)
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.neutron_activator.tooltip.0")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.advanced_neutron_activator.tooltip.1")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.advanced_neutron_activator.tooltip.2")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.parallelizable.tooltip")})
      .tooltips(new Component[]{Component.m_237115_("gtceu.multiblock.laser.tooltip")})
      .tooltips(
         new Component[]{Component.m_237110_("gtceu.machine.available_recipe_map_1.tooltip", new Object[]{Component.m_237115_("gtceu.neutron_activator")})}
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .recipeTypes(new GTRecipeType[]{GTLRecipeTypes.NEUTRON_ACTIVATOR_RECIPES})
      .recipeModifiers(new RecipeModifier[]{(machine, recipe, params, result) -> {
         long eu = recipe.data.m_128451_("evt") * 2000L;
         recipe.tickInputs.put(EURecipeCapability.CAP, List.of(new Content(eu, 10000, 10000, 0, null, null)));
         ((IGTRecipe)recipe).setHasTick(true);
         return GTRecipeModifiers.hatchParallel(machine, recipe, false, params, result);
      }})
      .appearanceBlock(GTLBlocks.SPS_CASING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "       AAAAAAA       ",
                  "       AAAAAAA       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       AAAAAAA       ",
                  "       AAAAAAA       "
               }
            )
            .aisle(
               new String[]{
                  "     AABBBBBBBAA     ",
                  "     AA   C   AA     ",
                  "         C           ",
                  "        C            ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "            D        ",
                  "           D         ",
                  "          D          ",
                  "         D           ",
                  "        D            ",
                  "                     ",
                  "                     ",
                  "            C        ",
                  "           C         ",
                  "          C          ",
                  "         C           ",
                  "        C            ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "            D        ",
                  "           D         ",
                  "     AA   D   AA     ",
                  "     AABBBBBBBAA     "
               }
            )
            .aisle(
               new String[]{
                  "   AABBBEEEEEBBBAA   ",
                  "   AA   FFFFF   AA   ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "       CFFFFF        ",
                  "      C FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF D      ",
                  "        FFFFFD       ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "       DFFFFF C      ",
                  "      D FFFFFC       ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "       CFFFFF        ",
                  "      C FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF D      ",
                  "        FFFFFD       ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "   AA   FFFFF   AA   ",
                  "   AABBBEEEEEBBBAA   "
               }
            )
            .aisle(
               new String[]{
                  " AABBBEEEEEEEEEBBBAA ",
                  " AA   FFFGFGFFF   AA ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "     CFFFGFGFFF      ",
                  "    C FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF D    ",
                  "      FFFGFGFFFD     ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF C    ",
                  "      FFFGFGFFFC     ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "     DFFFGFGFFF      ",
                  "    D FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "     CFFFGFGFFF      ",
                  "    C FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF D    ",
                  "      FFFGFGFFFD     ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  " AA   FFFGFGFFF   AA ",
                  " AABBBEEEEEEEEEBBBAA "
               }
            )
            .aisle(
               new String[]{
                  "ABBBEEEEEEEEEEEEEBBBA",
                  "A  DFFFGFFFFFGFFF   A",
                  "A D FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A  CFFFGFFFFFGFFF D A",
                  "A C FFFGFFFFFGFFFD  A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF C A",
                  "A   FFFGFFFFFGFFFC  A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A  DFFFGFFFFFGFFF   A",
                  "A D FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A  CFFFGFFFFFGFFF D A",
                  "A C FFFGFFFFFGFFFD  A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF C A",
                  "A   FFFGFFFFFGFFFC  A",
                  "ABBBEEEEEEEEEEEEEBBBA"
               }
            )
            .aisle(
               new String[]{
                  "HBEEEEEEEEEEEEEEEEEBH",
                  "H FFFGFFIIIIIFFGFFF H",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "A FFFGFFIIIIIFFGFFF A",
                  "ABEEEEEEEEEEEEEEEEEBA"
               }
            )
            .aisle(
               new String[]{
                  "HBEEEEEEEEEEEEEEEEEBH",
                  "H FGFFFGIJIJIGFFFGF H",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " DFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFD ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " CFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFC ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " DFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFD ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " CFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFC ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "A FGFFFGIJIJIGFFFGF A",
                  "ABEEEEEEEEEEEEEEEEEBA"
               }
            )
            .aisle(
               new String[]{
                  "HBEEEEEEEEEEEEEEEEEBH",
                  "H FFFGFFIIIIIFFGFFF H",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "A FFFGFFIIIIIFFGFFF A",
                  "ABEEEEEEEEEEEEEEEEEBA"
               }
            )
            .aisle(
               new String[]{
                  "HBEEEEEEEEEEEEEEEEEBH",
                  "H FGFFFGIJIJIGFFFGF H",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFD ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " DFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFC ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " CFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFD ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " DFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGFC ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  " CFGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "  FGFFFGIJIJIGFFFGF  ",
                  "A FGFFFGIJIJIGFFFGF A",
                  "ABEEEEEEEEEEEEEEEEEBA"
               }
            )
            .aisle(
               new String[]{
                  "HBEEEEEEEEEEEEEEEEEBH",
                  "H FFFGFFIIIIIFFGFFF H",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFD ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " DFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFFC ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  " CFFFGFFIIIIIFFGFFF  ",
                  "  FFFGFFIIIIIFFGFFF  ",
                  "A FFFGFFIIIIIFFGFFF A",
                  "ABEEEEEEEEEEEEEEEEEBA"
               }
            )
            .aisle(
               new String[]{
                  "ABBBEEEEEEEEEEEEEBBBA",
                  "A   FFFGFFFFFGFFFD  A",
                  "A   FFFGFFFFFGFFF D A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A D FFFGFFFFFGFFFC  A",
                  "A  DFFFGFFFFFGFFF C A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A C FFFGFFFFFGFFF   A",
                  "A  CFFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFFD  A",
                  "A   FFFGFFFFFGFFF D A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A D FFFGFFFFFGFFFC  A",
                  "A  DFFFGFFFFFGFFF C A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A   FFFGFFFFFGFFF   A",
                  "A C FFFGFFFFFGFFF   A",
                  "A  CFFFGFFFFFGFFF   A",
                  "ABBBEEEEEEEEEEEEEBBBA"
               }
            )
            .aisle(
               new String[]{
                  " AABBBEEEEEEEEEBBBAA ",
                  " AA   FFFGFGFFF   AA ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFFC     ",
                  "      FFFGFGFFF C    ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "    D FFFGFGFFF      ",
                  "     DFFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "    C FFFGFGFFF      ",
                  "     CFFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFFD     ",
                  "      FFFGFGFFF D    ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFFC     ",
                  "      FFFGFGFFF C    ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "    D FFFGFGFFF      ",
                  "     DFFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  "      FFFGFGFFF      ",
                  " AA   FFFGFGFFF   AA ",
                  " AABBBEEEEEEEEEBBBAA "
               }
            )
            .aisle(
               new String[]{
                  "   AABBBEEEEEBBBAA   ",
                  "   AA   FFFFF   AA   ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFFC       ",
                  "        FFFFF C      ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "      D FFFFF        ",
                  "       DFFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "      C FFFFFD       ",
                  "       CFFFFF D      ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFFC       ",
                  "        FFFFF C      ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "      D FFFFF        ",
                  "       DFFFFF        ",
                  "        FFFFF        ",
                  "        FFFFF        ",
                  "   AA   FFFFF   AA   ",
                  "   AABBBEEEEEBBBAA   "
               }
            )
            .aisle(
               new String[]{
                  "     AABBBBBBBAA     ",
                  "     AA   C   AA     ",
                  "           C         ",
                  "            C        ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "        D            ",
                  "         D           ",
                  "          D          ",
                  "           D         ",
                  "            D        ",
                  "                     ",
                  "                     ",
                  "        C            ",
                  "         C           ",
                  "          C          ",
                  "           C         ",
                  "            C        ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "                     ",
                  "        D            ",
                  "         D           ",
                  "     AA   D   AA     ",
                  "     AABBBBBBBAA     "
               }
            )
            .aisle(
               new String[]{
                  "       AHHHHHA       ",
                  "       AHHKHHA       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       A     A       ",
                  "       AAAAAAA       ",
                  "       AAAAAAA       "
               }
            )
            .where('K', Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where('A', Predicates.blocks(new Block[]{(Block)GTLBlocks.SPS_CASING.get()}))
            .where('B', Predicates.blocks(new Block[]{(Block)GTLBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()}))
            .where('C', Predicates.frames(new Material[]{GTLMaterials.Quantanium}))
            .where('D', Predicates.frames(new Material[]{GTLMaterials.Vibranium}))
            .where('E', Predicates.blocks(new Block[]{(Block)GTLBlocks.HYPER_MECHANICAL_CASING.get()}))
            .where('F', Predicates.blocks(new Block[]{(Block)GTBlocks.FUSION_GLASS.get()}))
            .where('G', Predicates.blocks(new Block[]{Registries.getBlock("kubejs:speeding_pipe")}))
            .where(
               'H',
               Predicates.blocks(new Block[]{(Block)GTLBlocks.SPS_CASING.get()})
                  .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_ENERGY}).setMinGlobalLimited(1).setMaxGlobalLimited(2).setPreviewCount(1))
                  .or(Predicates.abilities(new PartAbility[]{PartAbility.INPUT_LASER}).setMinGlobalLimited(0).setMaxGlobalLimited(2))
                  .or(Predicates.autoAbilities(true, false, true))
            )
            .where('I', Predicates.blocks(new Block[]{(Block)GTLBlocks.HSSS_REINFORCED_BOROSILICATE_GLASS.get()}))
            .where('J', Predicates.blocks(new Block[]{Registries.getBlock("kubejs:neutronium_pipe_casing")}))
            .where(' ', Predicates.any())
            .build()
      )
      .workableCasingRenderer(GTLCore.id("block/casings/sps_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
      .register();
   public static final MultiblockMachineDefinition MOLECULAR_ASSEMBLER_MATRIX = GTRegistration.REGISTRATE
      .multiblock("molecular_assembler_matrix", x$0 -> new MolecularAssemblerMultiblockMachine(x$0))
      .rotationState(RotationState.ALL)
      .recipeType(GTLRecipeTypes.MOLECULAR_ASSEMBLER)
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.0"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.1"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.2"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.3"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.4"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.5"),
            Component.m_237115_("gtceu.machine.molecular_assembler_matrix.tooltip.6")
         }
      )
      .tooltipBuilder(GTLMachines.GTL_ADD)
      .appearanceBlock(GCyMBlocks.CASING_LARGE_SCALE_ASSEMBLING)
      .pattern(
         definition -> FactoryBlockPattern.start()
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "                  B                  ",
                  "                  B                  ",
                  "                  B                  ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "                  A                  ",
                  "                 ACA                 ",
                  "                 DED                 ",
                  "              DDDDEDDDD              ",
                  "                 DED                 ",
                  "                 ACA                 ",
                  "                  A                  ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "              AAAAAAAAA              ",
                  "           DDDFFFFFFFFFDDD           ",
                  "              AAAAAAAAA              ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "           AAAA       AAAA           ",
                  "         DDFFFGHHHHHHHGFFFDD         ",
                  "           AAAA       AAAA           ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "         AAA             AAA         ",
                  "        DFFGHHH       HHHGFFD        ",
                  "         AAA             AAA         ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      I                       I      ",
                  "      BAAA                 AAAB      ",
                  "      BDFGHH             HHGFDB      ",
                  "      BAAA                 AAAB      ",
                  "      I                       I      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      G                       G      ",
                  "      J                       J      ",
                  "      J                       J      ",
                  "     ICI                     ICI     ",
                  "     BEAA                   AAEB     ",
                  "     BEFGH                 HGFEB     ",
                  "     BEAA                   AAEB     ",
                  "     ICI                     ICI     ",
                  "      J                       J      ",
                  "      J                       J      ",
                  "      G                       G      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      I       KLL   LLK       I      ",
                  "     AAA                     AAA     ",
                  "     DFGH                   HGFD     ",
                  "     AAA                     AAA     ",
                  "      I       KLL   LLK       I      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "               K     K               ",
                  "     AA      LL       LL      AA     ",
                  "    DFGH    K           K    HGFD    ",
                  "     AA      LL       LL      AA     ",
                  "               K     K               ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "    AA    D               D    AA    ",
                  "   DFGH   KLLK         KLLK   HGFD   ",
                  "    AA    D               D    AA    ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "          D               D          ",
                  "          D               D          ",
                  "    A    DFD             DFD    A    ",
                  "   DFH   KFG             GFK   HFD   ",
                  "    A    DFD             DFD    A    ",
                  "          D               D          ",
                  "          D               D          ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   AA     D               D     AA   ",
                  "  DFGH   LG               GL   HGFD  ",
                  "   AA     D               D     AA   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   A                             A   ",
                  "  DFH   KL                 LK   HFD  ",
                  "   A                             A   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   A    L                   L    A   ",
                  "  DFH    K                 K    HFD  ",
                  "   A    L                   L    A   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "              D       D              ",
                  "               D     D               ",
                  "       K      M   N   M      K       ",
                  "  AA    L        ENE        L    AA  ",
                  " DFGH           NNNNN           HGFD ",
                  "  AA    L        ENE        L    AA  ",
                  "       K      M   N   M      K       ",
                  "               D     D               ",
                  "              D       D              ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "               D     D               ",
                  "              DDDNNNDDD              ",
                  "       LK      DGDDDGD      KL       ",
                  "  A            MDCCCDM            A  ",
                  " DFH           MDCCCDM           HFD ",
                  "  A            MDCCCDM            A  ",
                  "       LK      DGDDDGD      KL       ",
                  "              DDDNNNDDD              ",
                  "               D     D               ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  O                  ",
                  "               DGDDDGD               ",
                  "       L       GDPPPDG       L       ",
                  "  A            DPQQQPD            A  ",
                  " DFH          NDPQQQPDN          HFD ",
                  "  A            DPQQQPD            A  ",
                  "       L       GDPPPDG       L       ",
                  "               DGDDDGD               ",
                  "                  O                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  O                  ",
                  "                  O                  ",
                  "                 EOE                 ",
                  "               NDCCCDN               ",
                  " A             DPQQQPD             A ",
                  " DA           ECQQQQQCE           AD ",
                  " DFH          NCQQQQQCN          HFD ",
                  " DA           ECQQQQQCE           AD ",
                  " A             DPQQQPD             A ",
                  "               NDCCCDN               ",
                  "                 EOE                 ",
                  "                  O                  ",
                  "                  O                  ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                  G                  ",
                  "                  D                  ",
                  "                  D                  ",
                  "                 ODO                 ",
                  "                 ODO                 ",
                  " A              OODOO              A ",
                  " A             NDCCCDN             A ",
                  "ACA           NDPQQQPDN           ACA",
                  "BEA           NCQQQQQCN           AEB",
                  "BEFH          NCQQQQQCN          HFEB",
                  "BEA           NCQQQQQCN           AEB",
                  "ACA           NDPQQQPDN           ACA",
                  " A             NDCCCDN             A ",
                  " A              OODOO              A ",
                  "                 ODO                 ",
                  "                 ODO                 ",
                  "                  D                  ",
                  "                  D                  ",
                  "                  G                  "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  O                  ",
                  "                  O                  ",
                  "                 EOE                 ",
                  "               NDCCCDN               ",
                  " A             DPQQQPD             A ",
                  " DA           ECQQQQQCE           AD ",
                  " DFH          NCQQQQQCN          HFD ",
                  " DA           ECQQQQQCE           AD ",
                  " A             DPQQQPD             A ",
                  "               NDCCCDN               ",
                  "                 EOE                 ",
                  "                  O                  ",
                  "                  O                  ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  O                  ",
                  "               DGDDDGD               ",
                  "       L       GDPPPDG       L       ",
                  "  A            DPQQQPD            A  ",
                  " DFH          NDPQQQPDN          HFD ",
                  "  A            DPQQQPD            A  ",
                  "       L       GDPPPDG       L       ",
                  "               DGDDDGD               ",
                  "                  O                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "               D     D               ",
                  "              DDDNNNDDD              ",
                  "       LK      DGDDDGD      KL       ",
                  "  A            MDCCCDM            A  ",
                  " DFH           MDCCCDM           HFD ",
                  "  A            MDCCCDM            A  ",
                  "       LK      DGDDDGD      KL       ",
                  "              DDDNNNDDD              ",
                  "               D     D               ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "              D       D              ",
                  "               D     D               ",
                  "       K      M   N   M      K       ",
                  "  AA    L        ENE        L    AA  ",
                  " DFGH           NNRNN           HGFD ",
                  "  AA    L        ENE        L    AA  ",
                  "       K      M   N   M      K       ",
                  "               D     D               ",
                  "              D       D              ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   A    L                   L    A   ",
                  "  DFH    K                 K    HFD  ",
                  "   A    L                   L    A   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   A                             A   ",
                  "  DFH   KL                 LK   HFD  ",
                  "   A                             A   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "   AA     D               D     AA   ",
                  "  DFGH   LG               GL   HGFD  ",
                  "   AA     D               D     AA   ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "          D               D          ",
                  "          D               D          ",
                  "    A    DFD             DFD    A    ",
                  "   DFH   KFG             GFK   HFD   ",
                  "    A    DFD             DFD    A    ",
                  "          D               D          ",
                  "          D               D          ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "    AA    D               D    AA    ",
                  "   DFGH   KLLK         KLLK   HGFD   ",
                  "    AA    D               D    AA    ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "               K     K               ",
                  "     AA      LL       LL      AA     ",
                  "    DFGH    K           K    HGFD    ",
                  "     AA      LL       LL      AA     ",
                  "               K     K               ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      I       KLL   LLK       I      ",
                  "     AAA                     AAA     ",
                  "     DFGH                   HGFD     ",
                  "     AAA                     AAA     ",
                  "      I       KLL   LLK       I      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      G                       G      ",
                  "      J                       J      ",
                  "      J                       J      ",
                  "     ICI                     ICI     ",
                  "     BEAA                   AAEB     ",
                  "     BEFGH                 HGFEB     ",
                  "     BEAA                   AAEB     ",
                  "     ICI                     ICI     ",
                  "      J                       J      ",
                  "      J                       J      ",
                  "      G                       G      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "      I                       I      ",
                  "      BAAA                 AAAB      ",
                  "      BDFGHH             HHGFDB      ",
                  "      BAAA                 AAAB      ",
                  "      I                       I      ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "         AAA             AAA         ",
                  "        DFFGHHH       HHHGFFD        ",
                  "         AAA             AAA         ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "           AAAA       AAAA           ",
                  "         DDFFFGHHHHHHHGFFFDD         ",
                  "           AAAA       AAAA           ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "              AAAAAAAAA              ",
                  "           DDDFFFFFFFFFDDD           ",
                  "              AAAAAAAAA              ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "                  A                  ",
                  "                 ACA                 ",
                  "                 DED                 ",
                  "              DDDDEDDDD              ",
                  "                 DED                 ",
                  "                 ACA                 ",
                  "                  A                  ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .aisle(
               new String[]{
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                  A                  ",
                  "                  B                  ",
                  "                  B                  ",
                  "                  B                  ",
                  "                  A                  ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     ",
                  "                                     "
               }
            )
            .where("R", Predicates.controller(Predicates.blocks(new IMachineBlock[]{definition.get()})))
            .where("H", Predicates.blocks(new Block[]{(Block)GTLBlocks.MOLECULAR_CASING.get()}))
            .where(
               "Q",
               Predicates.abilities(new PartAbility[]{GTLPartAbility.MOLECULAR_ASSEMBLER_MATRIX})
                  .or(Predicates.blocks(new Block[]{(Block)GTLBlocks.MOLECULAR_CASING.get()}))
            )
            .where("D", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:high_power_casing")}))
            .where(
               "N",
               Predicates.blocks(new Block[]{Registries.getBlock("gtceu:large_scale_assembler_casing")})
                  .or(Predicates.blocks(new IMachineBlock[]{GTLMachines.GTAEMachines.ME_MOLECULAR_ASSEMBLER_IO.get()}).setExactLimit(1))
            )
            .where("K", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:naquadah_alloy_frame")}))
            .where("M", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:hsse_frame")}))
            .where("O", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:large_scale_assembler_casing")}))
            .where("I", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:assembly_line_grating")}))
            .where("P", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:europium_frame")}))
            .where("L", Predicates.blocks(new Block[]{Registries.getBlock("gtlcore:hyper_mechanical_casing")}))
            .where("B", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:fusion_glass")}))
            .where("C", Predicates.blocks(new Block[]{Registries.getBlock("gtlcore:advanced_assembly_line_unit")}))
            .where("J", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:assembly_line_casing")}))
            .where("A", Predicates.blocks(new Block[]{Registries.getBlock("gtlcore:iridium_casing")}))
            .where("F", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:superconducting_coil")}))
            .where("E", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:advanced_computer_casing")}))
            .where("G", Predicates.blocks(new Block[]{Registries.getBlock("gtceu:trinium_frame")}))
            .build()
      )
      .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"), GTCEu.id("block/multiblock/research_station"))
      .register();

   public static void init() {
   }
}
