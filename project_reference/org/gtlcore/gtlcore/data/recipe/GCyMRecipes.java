package org.gtlcore.gtlcore.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.machines.GCyMMachines;

public class GCyMRecipes {
   public static void init(Consumer<FinishedRecipe> provider) {
      registerPartsRecipes(provider);
      registerMultiblockControllerRecipes(provider);
   }

   private static void registerMultiblockControllerRecipes(Consumer<FinishedRecipe> provider) {
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_macerator",
         GCyMMachines.LARGE_MACERATION_TOWER.asStack(),
         new Object[]{
            "PCP",
            "BXB",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.TungstenCarbide),
            'B',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack(),
            'X',
            GTMachines.MACERATOR[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_arc_smelter",
         GCyMMachines.LARGE_ARC_SMELTER.asStack(),
         new Object[]{
            "KDK",
            "CXC",
            "PPP",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.TantalumCarbide),
            'X',
            GTMachines.ARC_FURNACE[5].asStack(),
            'D',
            new UnificationEntry(TagPrefix.dust, GTMaterials.Graphite),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_ore_washer",
         GCyMMachines.LARGE_CHEMICAL_BATH.asStack(),
         new Object[]{
            "PGP",
            "CXC",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'G',
            GTBlocks.CASING_TEMPERED_GLASS.asStack(),
            'P',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'M',
            GTItems.CONVEYOR_MODULE_IV.asStack(),
            'X',
            GTMachines.ORE_WASHER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_sifter",
         GCyMMachines.LARGE_SIFTING_FUNNEL.asStack(),
         new Object[]{
            "PCP",
            "EXE",
            "PKP",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.HSLASteel),
            'E',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'X',
            GTMachines.SIFTER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_engraver",
         GCyMMachines.LARGE_ENGRAVING_LASER.asStack(),
         new Object[]{
            "ICI",
            "EXE",
            "PKP",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plateDouble, GTMaterials.TantalumCarbide),
            'I',
            GTItems.EMITTER_IV.asStack(),
            'E',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'X',
            GTMachines.LASER_ENGRAVER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_packer",
         GCyMMachines.LARGE_PACKER.asStack(),
         new Object[]{
            "RCR",
            "PXP",
            "KPK",
            'C',
            CustomTags.EV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.HSLASteel),
            'R',
            GTItems.ROBOT_ARM_HV.asStack(),
            'K',
            GTItems.CONVEYOR_MODULE_HV.asStack(),
            'X',
            GTMachines.PACKER[3].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_mixer",
         GCyMMachines.LARGE_MIXER.asStack(),
         new Object[]{
            "FCF",
            "RXR",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'F',
            ChemicalHelper.get(TagPrefix.pipeNormalFluid, GTMaterials.Polybenzimidazole),
            'R',
            ChemicalHelper.get(TagPrefix.rotor, GTMaterials.Osmiridium),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack(),
            'X',
            GTMachines.MIXER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_centrifuge",
         GCyMMachines.LARGE_CENTRIFUGE.asStack(),
         new Object[]{
            "SFS",
            "CXC",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'F',
            ChemicalHelper.get(TagPrefix.pipeHugeFluid, GTMaterials.StainlessSteel),
            'S',
            ChemicalHelper.get(TagPrefix.spring, GTMaterials.MolybdenumDisilicide),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack(),
            'X',
            GTMachines.CENTRIFUGE[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_assembler",
         GCyMMachines.LARGE_ASSEMBLER.asStack(),
         new Object[]{
            "RKR",
            "CXC",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'R',
            GTItems.ROBOT_ARM_IV.asStack(),
            'M',
            GTItems.CONVEYOR_MODULE_IV.asStack(),
            'X',
            GTMachines.ASSEMBLER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_circuit_assembler",
         GCyMMachines.LARGE_CIRCUIT_ASSEMBLER.asStack(),
         new Object[]{
            "RKR",
            "CXC",
            "MKM",
            'C',
            CustomTags.IV_CIRCUITS,
            'R',
            GTItems.ROBOT_ARM_IV.asStack(),
            'M',
            GTItems.CONVEYOR_MODULE_IV.asStack(),
            'X',
            GTMachines.CIRCUIT_ASSEMBLER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_electrolyzer",
         GCyMMachines.LARGE_ELECTROLYZER.asStack(),
         new Object[]{
            "PCP",
            "WXW",
            "PKP",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.BlackSteel),
            'W',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum),
            'X',
            GTMachines.ELECTROLYZER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_electromagnet",
         GCyMMachines.LARGE_ELECTROMAGNET.asStack(),
         new Object[]{
            "PWP",
            "CXC",
            "PKP",
            'C',
            CustomTags.IV_CIRCUITS,
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.BlueSteel),
            'W',
            new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.Osmium),
            'X',
            GTMachines.ELECTROMAGNETIC_SEPARATOR[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "blast_alloy_smelter",
         GCyMMachines.BLAST_ALLOY_SMELTER.asStack(),
         new Object[]{
            "TCT",
            "WXW",
            "TCT",
            'C',
            CustomTags.EV_CIRCUITS,
            'T',
            new UnificationEntry(TagPrefix.plate, GTMaterials.TantalumCarbide),
            'W',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Aluminium),
            'X',
            GTMachines.ALLOY_SMELTER[4].asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "mega_blast_furnace",
         GCyMMachines.MEGA_BLAST_FURNACE.asStack(),
         new Object[]{
            "PCP",
            "FSF",
            "DWD",
            'C',
            CustomTags.ZPM_CIRCUITS,
            'S',
            GTMachines.ELECTRIC_BLAST_FURNACE.asStack(),
            'F',
            GTItems.FIELD_GENERATOR_ZPM.asStack(),
            'P',
            new UnificationEntry(TagPrefix.spring, GTMaterials.Naquadah),
            'D',
            new UnificationEntry(TagPrefix.plateDense, GTMaterials.NaquadahAlloy),
            'W',
            new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "mega_vacuum_freezer",
         GCyMMachines.MEGA_VACUUM_FREEZER.asStack(),
         new Object[]{
            "PCP",
            "FSF",
            "DWD",
            'C',
            CustomTags.ZPM_CIRCUITS,
            'S',
            GTMachines.VACUUM_FREEZER.asStack(),
            'F',
            GTItems.FIELD_GENERATOR_ZPM.asStack(),
            'P',
            new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide),
            'D',
            new UnificationEntry(TagPrefix.plateDense, GTMaterials.RhodiumPlatedPalladium),
            'W',
            new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.RutheniumTriniumAmericiumNeutronate)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_autoclave",
         GCyMMachines.LARGE_AUTOCLAVE.asStack(),
         new Object[]{
            "PCP",
            "PAP",
            "BKB",
            'C',
            CustomTags.IV_CIRCUITS,
            'A',
            GTMachines.AUTOCLAVE[5].asStack(),
            'P',
            new UnificationEntry(TagPrefix.plateDouble, GTMaterials.HSLASteel),
            'B',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_material_press",
         GCyMMachines.LARGE_MATERIAL_PRESS.asStack(),
         new Object[]{
            "PKP",
            "BZG",
            "FKH",
            'Z',
            CustomTags.IV_CIRCUITS,
            'B',
            GTMachines.BENDER[5].asStack(),
            'P',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'G',
            GTMachines.COMPRESSOR[5].asStack(),
            'F',
            GTMachines.FORMING_PRESS[5].asStack(),
            'H',
            GTMachines.FORGE_HAMMER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_brewer",
         GCyMMachines.LARGE_BREWER.asStack(),
         new Object[]{
            "SZS",
            "FBH",
            "EKE",
            'Z',
            CustomTags.IV_CIRCUITS,
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.MolybdenumDisilicide),
            'F',
            GTMachines.FERMENTER[5].asStack(),
            'E',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'B',
            GTMachines.BREWERY[5].asStack(),
            'H',
            GTMachines.FLUID_HEATER[5].asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_cutter",
         GCyMMachines.LARGE_CUTTER.asStack(),
         new Object[]{
            "SMS",
            "CZL",
            "EKE",
            'Z',
            CustomTags.IV_CIRCUITS,
            'L',
            GTMachines.LATHE[5].asStack(),
            'E',
            GTItems.ELECTRIC_MOTOR_IV.asStack(),
            'C',
            GTMachines.CUTTER[5].asStack(),
            'M',
            GTItems.CONVEYOR_MODULE_IV.asStack(),
            'S',
            new UnificationEntry(TagPrefix.toolHeadBuzzSaw, GTMaterials.TungstenCarbide),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_distillery",
         GCyMMachines.LARGE_DISTILLERY.asStack(),
         new Object[]{
            "PZP",
            "EDE",
            "PZP",
            'Z',
            CustomTags.IV_CIRCUITS,
            'D',
            GTMachines.DISTILLATION_TOWER.asStack(),
            'E',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'P',
            ChemicalHelper.get(TagPrefix.pipeLargeFluid, GTMaterials.Iridium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_extractor",
         GCyMMachines.LARGE_EXTRACTOR.asStack(),
         new Object[]{
            "PTP",
            "EZC",
            "BKB",
            'Z',
            CustomTags.IV_CIRCUITS,
            'B',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'P',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'E',
            GTMachines.EXTRACTOR[5].asStack(),
            'C',
            GTMachines.CANNER[5].asStack(),
            'T',
            GTBlocks.CASING_TEMPERED_GLASS.asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_extruder",
         GCyMMachines.LARGE_EXTRUDER.asStack(),
         new Object[]{
            "PZP",
            "SES",
            "PKP",
            'Z',
            CustomTags.IV_CIRCUITS,
            'E',
            GTMachines.EXTRUDER[5].asStack(),
            'P',
            GTItems.ELECTRIC_PISTON_IV.asStack(),
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.MolybdenumDisilicide),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_solidifier",
         GCyMMachines.LARGE_SOLIDIFIER.asStack(),
         new Object[]{
            "PZP",
            "ESE",
            "PKP",
            'Z',
            CustomTags.IV_CIRCUITS,
            'S',
            GTMachines.FLUID_SOLIDIFIER[5].asStack(),
            'E',
            GTItems.ELECTRIC_PUMP_IV.asStack(),
            'P',
            ChemicalHelper.get(TagPrefix.pipeNormalFluid, GTMaterials.Polyethylene),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         true,
         "large_wiremill",
         GCyMMachines.LARGE_WIREMILL.asStack(),
         new Object[]{
            "PZP",
            "SWS",
            "MKM",
            'Z',
            CustomTags.IV_CIRCUITS,
            'W',
            GTMachines.WIREMILL[5].asStack(),
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.HSLASteel),
            'S',
            new UnificationEntry(TagPrefix.spring, GTMaterials.HSLASteel),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack(),
            'K',
            new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Platinum)
         }
      );
   }

   private static void registerPartsRecipes(Consumer<FinishedRecipe> provider) {
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "parallel_hatch_mk1",
         GCyMMachines.PARALLEL_HATCH[5].asStack(1),
         new Object[]{
            "SZE",
            "ZHZ",
            "CZC",
            'S',
            GTItems.SENSOR_IV.asStack(),
            'E',
            GTItems.EMITTER_IV.asStack(),
            'Z',
            CustomTags.LuV_CIRCUITS,
            'H',
            GTMachines.HULL[5].asStack(),
            'C',
            new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Platinum)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "parallel_hatch_mk2",
         GCyMMachines.PARALLEL_HATCH[6].asStack(1),
         new Object[]{
            "SZE",
            "ZHZ",
            "CZC",
            'S',
            GTItems.SENSOR_LuV.asStack(),
            'E',
            GTItems.EMITTER_LuV.asStack(),
            'Z',
            CustomTags.ZPM_CIRCUITS,
            'H',
            GTMachines.HULL[6].asStack(),
            'C',
            new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.NiobiumTitanium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "parallel_hatch_mk3",
         GCyMMachines.PARALLEL_HATCH[7].asStack(1),
         new Object[]{
            "SZE",
            "ZHZ",
            "CZC",
            'S',
            GTItems.SENSOR_ZPM.asStack(),
            'E',
            GTItems.EMITTER_ZPM.asStack(),
            'Z',
            CustomTags.UV_CIRCUITS,
            'H',
            GTMachines.HULL[7].asStack(),
            'C',
            new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.VanadiumGallium)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "parallel_hatch_mk4",
         GCyMMachines.PARALLEL_HATCH[8].asStack(1),
         new Object[]{
            "SZE",
            "ZHZ",
            "CZC",
            'S',
            GTItems.SENSOR_UV.asStack(),
            'E',
            GTItems.EMITTER_UV.asStack(),
            'Z',
            CustomTags.UHV_CIRCUITS,
            'H',
            GTMachines.HULL[8].asStack(),
            'C',
            new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.YttriumBariumCuprate)
         }
      );
   }
}
