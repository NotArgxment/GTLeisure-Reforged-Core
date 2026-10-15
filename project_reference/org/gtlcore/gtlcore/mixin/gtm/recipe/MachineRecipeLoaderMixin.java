package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.misc.MachineRecipeLoader;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MachineRecipeLoader.class)
public abstract class MachineRecipeLoaderMixin {
   @Overwrite(remap = false)
   private static void registerHatchConversion(Consumer<FinishedRecipe> provider) {
      for (int i = 0; i < GTMachines.FLUID_IMPORT_HATCH.length; i++) {
         if (GTMachines.FLUID_IMPORT_HATCH[i] != null && GTMachines.FLUID_EXPORT_HATCH[i] != null) {
            VanillaRecipeHelper.addShapedRecipe(
               provider,
               "fluid_hatch_output_to_input_" + GTMachines.FLUID_IMPORT_HATCH[i].getTier(),
               GTMachines.FLUID_IMPORT_HATCH[i].asStack(),
               new Object[]{"d", "B", 'B', GTMachines.FLUID_EXPORT_HATCH[i].asStack()}
            );
            VanillaRecipeHelper.addShapedRecipe(
               provider,
               "fluid_hatch_input_to_output_" + GTMachines.FLUID_EXPORT_HATCH[i].getTier(),
               GTMachines.FLUID_EXPORT_HATCH[i].asStack(),
               new Object[]{"d", "B", 'B', GTMachines.FLUID_IMPORT_HATCH[i].asStack()}
            );
         }
      }

      for (int i = 0; i < GTMachines.ITEM_IMPORT_BUS.length; i++) {
         if (GTMachines.ITEM_IMPORT_BUS[i] != null && GTMachines.ITEM_EXPORT_BUS[i] != null) {
            VanillaRecipeHelper.addShapedRecipe(
               provider,
               "item_bus_output_to_input_" + GTMachines.ITEM_IMPORT_BUS[i].getTier(),
               GTMachines.ITEM_IMPORT_BUS[i].asStack(),
               new Object[]{"d", "B", 'B', GTMachines.ITEM_EXPORT_BUS[i].asStack()}
            );
            VanillaRecipeHelper.addShapedRecipe(
               provider,
               "item_bus_input_to_output_" + GTMachines.ITEM_EXPORT_BUS[i].getTier(),
               GTMachines.ITEM_EXPORT_BUS[i].asStack(),
               new Object[]{"d", "B", 'B', GTMachines.ITEM_IMPORT_BUS[i].asStack()}
            );
         }
      }

      for (int tier : GTValues.tiersBetween(4, 14)) {
         String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
         MachineDefinition importHatch4x = GTMachines.FLUID_IMPORT_HATCH_4X[tier];
         MachineDefinition exportHatch4x = GTMachines.FLUID_EXPORT_HATCH_4X[tier];
         MachineDefinition importHatch9x = GTMachines.FLUID_IMPORT_HATCH_9X[tier];
         MachineDefinition exportHatch9x = GTMachines.FLUID_EXPORT_HATCH_9X[tier];
         VanillaRecipeHelper.addShapedRecipe(
            provider, "fluid_hatch_4x_output_to_input_" + tierName, importHatch4x.asStack(), new Object[]{"d", "B", 'B', exportHatch4x.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider, "fluid_hatch_4x_input_to_output_" + tierName, exportHatch4x.asStack(), new Object[]{"d", "B", 'B', importHatch4x.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider, "fluid_hatch_9x_output_to_input_" + tierName, importHatch9x.asStack(), new Object[]{"d", "B", 'B', exportHatch9x.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider, "fluid_hatch_9x_input_to_output_" + tierName, exportHatch9x.asStack(), new Object[]{"d", "B", 'B', importHatch9x.asStack()}
         );
      }

      for (int tier : GTValues.tiersBetween(6, 14)) {
         String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
         MachineDefinition inputBuffer = GTMachines.DUAL_IMPORT_HATCH[tier];
         MachineDefinition outputBuffer = GTMachines.DUAL_EXPORT_HATCH[tier];
         VanillaRecipeHelper.addShapedRecipe(
            provider, "dual_hatch_output_to_input_" + tierName, inputBuffer.asStack(), new Object[]{"d", "B", 'B', outputBuffer.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider, "dual_hatch_input_to_output_" + tierName, outputBuffer.asStack(), new Object[]{"d", "B", 'B', inputBuffer.asStack()}
         );
      }

      VanillaRecipeHelper.addShapedRecipe(
         provider, "steam_bus_output_to_input", GTMachines.STEAM_EXPORT_BUS.asStack(), new Object[]{"d", "B", 'B', GTMachines.STEAM_IMPORT_BUS.asStack()}
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider, "steam_bus_input_to_output", GTMachines.STEAM_IMPORT_BUS.asStack(), new Object[]{"d", "B", 'B', GTMachines.STEAM_EXPORT_BUS.asStack()}
      );
      if (GTCEu.isAE2Loaded()) {
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            "me_fluid_hatch_output_to_input",
            GTLMachines.GTAEMachines.FLUID_IMPORT_HATCH_ME.asStack(),
            new Object[]{"d", "B", 'B', GTLMachines.GTAEMachines.FLUID_EXPORT_HATCH_ME.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            "me_fluid_hatch_input_to_output",
            GTLMachines.GTAEMachines.FLUID_EXPORT_HATCH_ME.asStack(),
            new Object[]{"d", "B", 'B', GTLMachines.GTAEMachines.FLUID_IMPORT_HATCH_ME.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            "me_item_bus_output_to_input",
            GTLMachines.GTAEMachines.ITEM_IMPORT_BUS_ME.asStack(),
            new Object[]{"d", "B", 'B', GTLMachines.GTAEMachines.ITEM_EXPORT_BUS_ME.asStack()}
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            "me_item_bus_input_to_output",
            GTLMachines.GTAEMachines.ITEM_EXPORT_BUS_ME.asStack(),
            new Object[]{"d", "B", 'B', GTLMachines.GTAEMachines.ITEM_IMPORT_BUS_ME.asStack()}
         );
      }
   }
}
