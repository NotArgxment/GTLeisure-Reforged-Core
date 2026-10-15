package org.gtlcore.gtlcore.data.recipe;

import com.glodblock.github.extendedae.ExtendedAE;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.gtlcore.gtlcore.common.data.GTLRecipes;

public class RemoveRecipe {
   public static void init(Consumer<ResourceLocation> consumer) {
      GTLRecipes.DISASSEMBLY_RECORD.clear();
      consumer.accept(GTCEu.id("macerator/macerate_wheat"));
      consumer.accept(GTCEu.id("autoclave/agar"));
      consumer.accept(GTCEu.id("assembly_line/dynamo_hatch_uhv"));
      consumer.accept(GTCEu.id("assembly_line/energy_hatch_uhv"));
      consumer.accept(GTCEu.id("research_station/1_x_gtceu_uv_energy_input_hatch"));
      consumer.accept(GTCEu.id("research_station/1_x_gtceu_uv_energy_output_hatch"));
      consumer.accept(GTCEu.id("mixer/ender_pearl_dust"));
      consumer.accept(GTCEu.id("shapeless/integrated_circuit"));
      Material[] fluidMap = new Material[]{GTMaterials.Glue, GTMaterials.Polyethylene, GTMaterials.Polytetrafluoroethylene, GTMaterials.Polybenzimidazole};

      for (MachineDefinition machine : GTMachines.FLUID_IMPORT_HATCH) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/fluid_hatch_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      for (MachineDefinition machine : GTMachines.FLUID_EXPORT_HATCH) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/fluid_export_hatch_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      for (MachineDefinition machine : GTMachines.ITEM_IMPORT_BUS) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/item_import_bus_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      for (MachineDefinition machine : GTMachines.ITEM_EXPORT_BUS) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/item_export_bus_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      for (MachineDefinition machine : GTMachines.DUAL_IMPORT_HATCH) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/dual_import_bus_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      for (MachineDefinition machine : GTMachines.DUAL_EXPORT_HATCH) {
         if (machine != null) {
            int tier = machine.getTier();

            for (int j = Math.min(fluidMap.length - 1, tier / 2); j < fluidMap.length; j++) {
               consumer.accept(GTCEu.id("assembler/dual_export_bus_" + GTValues.VN[tier].toLowerCase() + "_" + fluidMap[j].getName()));
            }
         }
      }

      consumer.accept(ExtendedAE.id("assembler_matrix_frame"));
      consumer.accept(ExtendedAE.id("assembler_matrix_wall"));
      consumer.accept(ExtendedAE.id("assembler_matrix_glass"));
   }
}
