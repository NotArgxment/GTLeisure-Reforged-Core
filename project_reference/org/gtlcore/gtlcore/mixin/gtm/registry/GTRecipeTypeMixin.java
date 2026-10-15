package org.gtlcore.gtlcore.mixin.gtm.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.Collections;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GTRecipeType.class)
public abstract class GTRecipeTypeMixin {
   @Shadow(remap = false)
   private GTRecipeBuilder recipeBuilder;
   @Shadow(remap = false)
   @Final
   public ResourceLocation registryName;

   @Overwrite(remap = false)
   public GTRecipeType onRecipeBuild(BiConsumer<GTRecipeBuilder, Consumer<FinishedRecipe>> onBuild) {
      if (Objects.equals(this.registryName, GTCEu.id("cutter"))) {
         this.recipeBuilder
            .onSave(
               (recipeBuilder, provider) -> {
                  if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()
                     && recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {
                     if (recipeBuilder.EUt() < 524288L) {
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_water"))
                           .inputFluids(GTMaterials.Water.getFluid((int)Math.max(4L, Math.min(1000L, recipeBuilder.duration * recipeBuilder.EUt() / 320L))))
                           .duration(recipeBuilder.duration * 2)
                           .save(provider);
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_distilled_water"))
                           .inputFluids(
                              GTMaterials.DistilledWater.getFluid((int)Math.max(3L, Math.min(750L, recipeBuilder.duration * recipeBuilder.EUt() / 426L)))
                           )
                           .duration((int)(recipeBuilder.duration * 1.5))
                           .save(provider);
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_8_water"))
                           .inputFluids(
                              GTLMaterials.GradePurifiedWater8.getFluid((int)Math.max(1L, Math.min(500L, recipeBuilder.duration * recipeBuilder.EUt() / 720L)))
                           )
                           .duration((int)(recipeBuilder.duration * 0.8))
                           .save(provider);
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_16_water"))
                           .inputFluids(
                              GTLMaterials.GradePurifiedWater16
                                 .getFluid((int)Math.max(1L, Math.min(250L, recipeBuilder.duration * recipeBuilder.EUt() / 960L)))
                           )
                           .duration((int)(recipeBuilder.duration * 0.5))
                           .save(provider);
                        recipeBuilder.inputFluids(
                              GTMaterials.Lubricant.getFluid((int)Math.max(1L, Math.min(250L, recipeBuilder.duration * recipeBuilder.EUt() / 1280L)))
                           )
                           .duration(Math.max(1, recipeBuilder.duration));
                     } else if (recipeBuilder.EUt() < GTValues.VA[10]) {
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_16_water"))
                           .inputFluids(
                              GTLMaterials.GradePurifiedWater16
                                 .getFluid((int)Math.max(1L, Math.min(500L, recipeBuilder.duration * recipeBuilder.EUt() / 640L)))
                           )
                           .duration((int)(recipeBuilder.duration * 0.5))
                           .save(provider);
                        recipeBuilder.inputFluids(
                              GTLMaterials.GradePurifiedWater8
                                 .getFluid((int)Math.max(1L, Math.min(1000L, recipeBuilder.duration * recipeBuilder.EUt() / 320L)))
                           )
                           .duration(Math.max(1, recipeBuilder.duration));
                     } else {
                        recipeBuilder.inputFluids(
                              GTLMaterials.GradePurifiedWater16
                                 .getFluid((int)Math.max(1L, Math.min(1000L, recipeBuilder.duration * recipeBuilder.EUt() / 320L)))
                           )
                           .duration(Math.max(1, recipeBuilder.duration));
                     }
                  }
               }
            );
         return this.recipeBuilder.recipeType;
      } else if (Objects.equals(this.registryName, GTCEu.id("circuit_assembler"))) {
         this.recipeBuilder
            .onSave(
               (recipeBuilder, provider) -> {
                  if (recipeBuilder.input.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()
                     && recipeBuilder.tickInput.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).isEmpty()) {
                     if (recipeBuilder.EUt() < 160000L) {
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_soldering_alloy"))
                           .inputFluids(GTMaterials.SolderingAlloy.getFluid(Math.max(1, 72 * recipeBuilder.getSolderMultiplier())))
                           .save(provider);
                        recipeBuilder.inputFluids(GTMaterials.Tin.getFluid(Math.max(1, 144 * recipeBuilder.getSolderMultiplier())));
                     } else {
                        int am = GTUtil.getFloorTierByVoltage(recipeBuilder.EUt()) - 6;
                        recipeBuilder.copy(new ResourceLocation(recipeBuilder.id.toString() + "_soldering_alloy"))
                           .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(Math.max(1, 72 * am)))
                           .save(provider);
                        recipeBuilder.inputFluids(GTLMaterials.MutatedLivingSolder.getFluid(Math.max(1, 144 * am)));
                     }
                  }
               }
            );
         return this.recipeBuilder.recipeType;
      } else {
         this.recipeBuilder.onSave(onBuild);
         return this.recipeBuilder.recipeType;
      }
   }
}
