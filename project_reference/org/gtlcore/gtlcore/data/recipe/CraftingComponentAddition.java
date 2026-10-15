package org.gtlcore.gtlcore.data.recipe;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent.Component;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.utils.Registries;

public final class CraftingComponentAddition {
   public static Component BUFFER;

   public static void init() {
      CraftingComponent.PUMP
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.ELECTRIC_PUMP_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CONVEYOR
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.CONVEYOR_MODULE_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.MOTOR
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.ELECTRIC_MOTOR_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PISTON
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.ELECTRIC_PISTON_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.EMITTER
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.EMITTER_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.SENSOR
         .appendIngredients(Stream.of(new Object[][]{{14, GTLItems.SENSOR_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1])));
      CraftingComponent.FIELD_GENERATOR
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.FIELD_GENERATOR_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.ROBOT_ARM
         .appendIngredients(
            Stream.of(new Object[][]{{14, GTLItems.ROBOT_ARM_MAX.asStack()}}).collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.WIRE_ELECTRIC
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtSingle, GTMaterials.Mendelevium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtSingle, GTLMaterials.Uruium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtSingle, GTLMaterials.Uruium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.WIRE_QUAD
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.WIRE_OCT
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtOctal, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.WIRE_HEX
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtHex, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_DOUBLE
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_QUAD
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.WIRE_ELECTRIC
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_OCT
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_HEX
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_TIER_UP
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Taranium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.Crystalmatrix)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.CosmicNeutronium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtSingle, GTLMaterials.SpaceTime)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_TIER_UP_DOUBLE
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtDouble, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Taranium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.Crystalmatrix)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtDouble, GTLMaterials.CosmicNeutronium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.SpaceTime)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_TIER_UP_QUAD
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtQuadruple, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Taranium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.Crystalmatrix)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtQuadruple, GTLMaterials.CosmicNeutronium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.SpaceTime)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_TIER_UP_OCT
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtOctal, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Taranium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.Crystalmatrix)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtOctal, GTLMaterials.CosmicNeutronium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.SpaceTime)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.CABLE_TIER_UP_HEX
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.cableGtHex, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Taranium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.Crystalmatrix)},
                  new Object[]{13, new UnificationEntry(TagPrefix.cableGtHex, GTLMaterials.CosmicNeutronium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.SpaceTime)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PIPE_NORMAL
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium)},
                  new Object[]{10, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.pipeNormalFluid, GTLMaterials.Enderium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.pipeNormalFluid, GTLMaterials.Enderium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.pipeNormalFluid, GTLMaterials.HeavyQuarkDegenerateMatter)},
                  new Object[]{14, new UnificationEntry(TagPrefix.pipeNormalFluid, GTLMaterials.HeavyQuarkDegenerateMatter)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PIPE_LARGE
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium)},
                  new Object[]{10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.Enderium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.Enderium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.HeavyQuarkDegenerateMatter)},
                  new Object[]{14, new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.HeavyQuarkDegenerateMatter)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PIPE_NONUPLE
         .appendIngredients(
            Stream.of(
                  new Object[]{0, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze)},
                  new Object[]{1, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Bronze)},
                  new Object[]{2, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Steel)},
                  new Object[]{3, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.StainlessSteel)},
                  new Object[]{9, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium)},
                  new Object[]{10, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTMaterials.Neutronium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTLMaterials.Enderium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTLMaterials.Enderium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTLMaterials.HeavyQuarkDegenerateMatter)},
                  new Object[]{14, new UnificationEntry(TagPrefix.pipeNonupleFluid, GTLMaterials.HeavyQuarkDegenerateMatter)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.GLASS
         .appendIngredients(
            Stream.of(
                  new Object[]{9, GTBlocks.FUSION_GLASS.asStack()},
                  new Object[]{10, GTBlocks.FUSION_GLASS.asStack()},
                  new Object[]{11, Registries.getItem("kubejs:force_field_glass")},
                  new Object[]{12, Registries.getItem("kubejs:force_field_glass")},
                  new Object[]{13, Registries.getItem("kubejs:force_field_glass")},
                  new Object[]{14, Registries.getItem("kubejs:force_field_glass")}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PLATE
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.plate, GTLMaterials.Quantanium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.plate, GTLMaterials.Adamantium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.plate, GTLMaterials.Vibranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.plate, GTLMaterials.Draconium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.plate, GTLMaterials.Chaos)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.HULL_PLATE
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.plate, GTLMaterials.Polyetheretherketone)},
                  new Object[]{10, new UnificationEntry(TagPrefix.plate, GTLMaterials.Polyetheretherketone)},
                  new Object[]{11, new UnificationEntry(TagPrefix.plate, GTLMaterials.Zylon)},
                  new Object[]{12, new UnificationEntry(TagPrefix.plate, GTLMaterials.Zylon)},
                  new Object[]{13, new UnificationEntry(TagPrefix.plate, GTLMaterials.FullerenePolymerMatrixPulp)},
                  new Object[]{14, new UnificationEntry(TagPrefix.plate, GTLMaterials.Radox)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.ROTOR
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.rotor, GTMaterials.Neutronium)},
                  new Object[]{10, new UnificationEntry(TagPrefix.rotor, GTLMaterials.Quantanium)},
                  new Object[]{11, new UnificationEntry(TagPrefix.rotor, GTLMaterials.Adamantium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.rotor, GTLMaterials.Vibranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.rotor, GTLMaterials.Draconium)},
                  new Object[]{14, new UnificationEntry(TagPrefix.rotor, GTLMaterials.TranscendentMetal)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.COIL_HEATING
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.AbyssalAlloy)},
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.TitanSteel)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.Adamantine)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.NaquadriaticTaranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.Starmetal)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtDouble, GTLMaterials.Hypogen)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.COIL_HEATING_DOUBLE
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.AbyssalAlloy)},
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.TitanSteel)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Adamantine)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.NaquadriaticTaranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Starmetal)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtQuadruple, GTLMaterials.Hypogen)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.COIL_ELECTRIC
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Mithril)},
                  new Object[]{10, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Mithril)},
                  new Object[]{12, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Mithril)},
                  new Object[]{13, new UnificationEntry(TagPrefix.wireGtOctal, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.wireGtHex, GTLMaterials.Crystalmatrix)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.STICK_DISTILLATION
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.spring, GTMaterials.Europium)},
                  new Object[]{10, new UnificationEntry(TagPrefix.spring, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.spring, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.spring, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.spring, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.STICK_ELECTROMAGNETIC
         .appendIngredients(
            Stream.of(
                  new Object[]{5, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium)},
                  new Object[]{6, new UnificationEntry(TagPrefix.rod, GTMaterials.VanadiumGallium)},
                  new Object[]{7, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium)},
                  new Object[]{8, new UnificationEntry(TagPrefix.rod, GTMaterials.NiobiumTitanium)},
                  new Object[]{9, Registries.getItem("kubejs:netherite_rod")},
                  new Object[]{10, Registries.getItem("kubejs:netherite_rod")},
                  new Object[]{11, new UnificationEntry(TagPrefix.rod, GTLMaterials.Mithril)},
                  new Object[]{12, new UnificationEntry(TagPrefix.rod, GTLMaterials.Mithril)},
                  new Object[]{13, new UnificationEntry(TagPrefix.rod, GTLMaterials.Echoite)},
                  new Object[]{14, new UnificationEntry(TagPrefix.rod, GTLMaterials.Echoite)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.PIPE_REACTOR
         .appendIngredients(
            Stream.of(
                  new Object[]{9, new UnificationEntry(TagPrefix.pipeNormalFluid, GTMaterials.Polybenzimidazole)},
                  new Object[]{10, new UnificationEntry(TagPrefix.pipeLargeFluid, GTMaterials.Polybenzimidazole)},
                  new Object[]{11, new UnificationEntry(TagPrefix.pipeHugeFluid, GTMaterials.Polybenzimidazole)},
                  new Object[]{12, new UnificationEntry(TagPrefix.pipeNormalFluid, GTLMaterials.FullerenePolymerMatrixPulp)},
                  new Object[]{13, new UnificationEntry(TagPrefix.pipeLargeFluid, GTLMaterials.FullerenePolymerMatrixPulp)},
                  new Object[]{14, new UnificationEntry(TagPrefix.pipeHugeFluid, GTLMaterials.FullerenePolymerMatrixPulp)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.POWER_COMPONENT
         .appendIngredients(
            Stream.of(
                  new Object[]{10, Registries.getItem("kubejs:nm_chip")},
                  new Object[]{11, Registries.getItem("kubejs:pm_chip")},
                  new Object[]{12, Registries.getItem("kubejs:pm_chip")},
                  new Object[]{13, Registries.getItem("kubejs:fm_chip")},
                  new Object[]{14, Registries.getItem("kubejs:fm_chip")}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.VOLTAGE_COIL
         .appendIngredients(
            Stream.of(
                  new Object[]{9, Registries.getItem("kubejs:uhv_voltage_coil")},
                  new Object[]{10, Registries.getItem("kubejs:uev_voltage_coil")},
                  new Object[]{11, Registries.getItem("kubejs:uiv_voltage_coil")},
                  new Object[]{12, Registries.getItem("kubejs:uxv_voltage_coil")},
                  new Object[]{13, Registries.getItem("kubejs:opv_voltage_coil")},
                  new Object[]{14, Registries.getItem("kubejs:max_voltage_coil")}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      CraftingComponent.SPRING
         .appendIngredients(
            Stream.of(
                  new Object[]{10, new UnificationEntry(TagPrefix.spring, GTLMaterials.Mithril)},
                  new Object[]{11, new UnificationEntry(TagPrefix.spring, GTMaterials.Neutronium)},
                  new Object[]{12, new UnificationEntry(TagPrefix.spring, GTLMaterials.Taranium)},
                  new Object[]{13, new UnificationEntry(TagPrefix.spring, GTLMaterials.Crystalmatrix)},
                  new Object[]{14, new UnificationEntry(TagPrefix.spring, GTLMaterials.CosmicNeutronium)}
               )
               .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
         );
      BUFFER = new Component(
         Stream.of(
               new Object[]{1, GTMachines.BUFFER[1].asStack()},
               new Object[]{2, GTMachines.BUFFER[2].asStack()},
               new Object[]{3, GTMachines.BUFFER[3].asStack()},
               new Object[]{4, GTMachines.BUFFER[4].asStack()},
               new Object[]{5, GTMachines.BUFFER[5].asStack()},
               new Object[]{6, GTMachines.BUFFER[6].asStack()},
               new Object[]{7, GTMachines.BUFFER[7].asStack()},
               new Object[]{8, GTMachines.BUFFER[8].asStack()},
               new Object[]{9, GTMachines.BUFFER[9].asStack()},
               new Object[]{10, GTMachines.BUFFER[10].asStack()},
               new Object[]{11, GTMachines.BUFFER[11].asStack()},
               new Object[]{12, GTMachines.BUFFER[12].asStack()},
               new Object[]{13, GTMachines.BUFFER[13].asStack()},
               new Object[]{14, GTMachines.BUFFER[14].asStack()}
            )
            .collect(Collectors.toMap(data -> (Integer)data[0], data -> data[1]))
      );
   }
}
