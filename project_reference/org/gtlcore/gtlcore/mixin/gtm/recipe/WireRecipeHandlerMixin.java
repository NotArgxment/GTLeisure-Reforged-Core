package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.WireRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WireRecipeHandler.class)
public abstract class WireRecipeHandlerMixin {
   @Shadow(remap = false)
   @Final
   private static Map<TagPrefix, Integer> INSULATION_AMOUNT;

   @Shadow(remap = false)
   private static void generateManualRecipe(TagPrefix wirePrefix, Material material, TagPrefix cablePrefix, int cableAmount, Consumer<FinishedRecipe> provider) {
   }

   @Overwrite(remap = false)
   public static void generateCableCovering(TagPrefix wirePrefix, Material material, WireProperties property, Consumer<FinishedRecipe> provider) {
      if (!property.isSuperconductor()) {
         int cableAmount = (int)(wirePrefix.getMaterialAmount(material) * 2L / 3628800L);
         TagPrefix cablePrefix = TagPrefix.get("cable" + wirePrefix.name().substring(4));
         int voltageTier = GTUtil.getTierByVoltage(property.getVoltage());
         int insulationAmount = INSULATION_AMOUNT.get(cablePrefix);
         if (voltageTier <= 1) {
            generateManualRecipe(wirePrefix, material, cablePrefix, cableAmount, provider);
         }

         if (voltageTier <= 4) {
            GTRecipeBuilder builder = GTRecipeTypes.ASSEMBLER_RECIPES
               .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_rubber", new Object[0])
               .EUt(GTValues.VA[0])
               .duration(100)
               .inputItems(wirePrefix, material)
               .outputItems(cablePrefix, material)
               .inputFluids(GTMaterials.Rubber.getFluid(144L * insulationAmount));
            if (voltageTier == 4) {
               builder.inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, insulationAmount);
            }

            builder.save(provider);
         }

         GTRecipeBuilder builder = GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_silicone", new Object[0])
            .EUt(GTValues.VA[0])
            .duration(100)
            .inputItems(wirePrefix, material)
            .outputItems(cablePrefix, material);
         if (voltageTier > 10) {
            builder.inputItems(GTLItems.HIGHLY_INSULATING_FOIL.asStack(insulationAmount));
         }

         if (voltageTier >= 6) {
            builder.inputItems(TagPrefix.foil, GTMaterials.PolyphenyleneSulfide, insulationAmount);
         }

         if (voltageTier >= 4) {
            builder.inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, insulationAmount);
         }

         builder.inputFluids(GTMaterials.SiliconeRubber.getFluid(144L * insulationAmount / 2L)).save(provider);
         builder = GTRecipeTypes.ASSEMBLER_RECIPES
            .recipeBuilder("cover_" + material.getName() + "_" + wirePrefix + "_styrene_butadiene", new Object[0])
            .EUt(GTValues.VA[0])
            .duration(100)
            .inputItems(wirePrefix, material)
            .outputItems(cablePrefix, material);
         if (voltageTier > 10) {
            builder.inputItems(GTLItems.HIGHLY_INSULATING_FOIL.asStack(insulationAmount));
         }

         if (voltageTier >= 6) {
            builder.inputItems(TagPrefix.foil, GTMaterials.PolyphenyleneSulfide, insulationAmount);
         }

         if (voltageTier >= 4) {
            builder.inputItems(TagPrefix.foil, GTMaterials.PolyvinylChloride, insulationAmount);
         }

         builder.inputFluids(GTMaterials.StyreneButadieneRubber.getFluid(144L * insulationAmount / 4L)).save(provider);
      }
   }
}
