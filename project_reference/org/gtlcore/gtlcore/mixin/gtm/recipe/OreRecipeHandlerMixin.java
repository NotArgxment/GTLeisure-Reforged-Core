package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix.OreType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.data.recipe.generated.OreRecipeHandler;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(OreRecipeHandler.class)
public abstract class OreRecipeHandlerMixin {
   @Overwrite(remap = false)
   public static void init(Consumer<FinishedRecipe> provider) {
      for (TagPrefix ore : TagPrefix.ORES.keySet()) {
         Supplier<Material> material = ((OreType)TagPrefix.ORES.get(ore)).material();
         if (material != null && material.get() == GTMaterials.Stone) {
            ore.executeHandler(provider, PropertyKey.ORE, OreRecipeHandlerMixin::gtlcore$processOre);
            ore.executeHandler(provider, PropertyKey.ORE, OreRecipeHandlerMixin::gtlcore$processOreForgeHammer);
         }
      }

      TagPrefix.rawOre.executeHandler(provider, PropertyKey.ORE, OreRecipeHandlerMixin::gtlcore$processRawOre);
      TagPrefix.crushed.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedOre);
      TagPrefix.crushedPurified.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedPurified);
      TagPrefix.crushedRefined.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processCrushedCentrifuged);
      TagPrefix.dustImpure.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processDirtyDust);
      TagPrefix.dustPure.executeHandler(provider, PropertyKey.ORE, OreRecipeHandler::processPureDust);
   }

   @Unique
   private static void gtlcore$processOreForgeHammer(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {
      ItemStack crushedStack = ChemicalHelper.get(TagPrefix.crushed, material);
      int amountOfCrushedOre = property.getOreMultiplier();
      int oreTypeMultiplier = ConfigHolder.INSTANCE.oreMultiplier;
      crushedStack.m_41764_(crushedStack.m_41613_() * property.getOreMultiplier());
      GTRecipeBuilder builder = GTRecipeTypes.FORGE_HAMMER_RECIPES
         .recipeBuilder("hammer_" + material.getName() + "_ore_to_raw_ore", new Object[0])
         .inputItems(orePrefix.getItemTags(material)[0])
         .duration(10)
         .EUt(16L);
      if (material.hasProperty(PropertyKey.GEM) && !TagPrefix.gem.isIgnored(material)) {
         builder.outputItems(
            GTUtil.copyAmount(amountOfCrushedOre * oreTypeMultiplier, new ItemStack[]{ChemicalHelper.get(TagPrefix.gem, material, crushedStack.m_41613_())})
         );
      } else {
         builder.outputItems(GTUtil.copyAmount(amountOfCrushedOre * oreTypeMultiplier, new ItemStack[]{crushedStack}));
      }

      builder.save(provider);
   }

   @Unique
   private static void gtlcore$processOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {
      TagKey<Item> tag = orePrefix.getItemTags(material)[0];
      Material byproductMaterial = (Material)GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
      ItemStack byproductStack = ChemicalHelper.get(TagPrefix.gem, byproductMaterial);
      if (byproductStack.m_41619_()) {
         byproductStack = ChemicalHelper.get(TagPrefix.dust, byproductMaterial);
      }

      Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
      ItemStack crushedStack = ChemicalHelper.get(TagPrefix.crushed, material);
      int amountOfCrushedOre = property.getOreMultiplier();
      ItemStack ingotStack;
      if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
         ingotStack = ChemicalHelper.get(TagPrefix.ingot, smeltingMaterial);
      } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
         ingotStack = ChemicalHelper.get(TagPrefix.gem, smeltingMaterial);
      } else {
         ingotStack = ChemicalHelper.get(TagPrefix.dust, smeltingMaterial);
      }

      int oreTypeMultiplier = ConfigHolder.INSTANCE.oreMultiplier;
      ingotStack.m_41764_(ingotStack.m_41613_() * property.getOreMultiplier() * oreTypeMultiplier);
      crushedStack.m_41764_(crushedStack.m_41613_() * property.getOreMultiplier());
      if (!crushedStack.m_41619_()) {
         GTRecipeBuilder builder = GTRecipeTypes.MACERATOR_RECIPES
            .recipeBuilder("macerate_" + material.getName() + "_ore_to_raw_ore", new Object[0])
            .inputItems(tag)
            .outputItems(GTUtil.copyAmount(amountOfCrushedOre * 2 * oreTypeMultiplier, new ItemStack[]{crushedStack}))
            .chancedOutput(byproductStack, 1400, 850)
            .EUt(30L)
            .duration(26);

         for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
               ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial);
               builder.chancedOutput(dustStack, 6700, 800);
            }
         }

         builder.save(provider);
         int crushedAmount = amountOfCrushedOre * 2 * oreTypeMultiplier;
         GTRecipeBuilder opBuilder1 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
            .recipeBuilder("integrated_ore_processor_1_" + material.getName(), new Object[0])
            .circuitMeta(1)
            .inputItems(tag)
            .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
            .chancedOutput(byproductStack, 1400, 850)
            .chancedOutput(ChemicalHelper.get(TagPrefix.dust, byproductMaterial, property.getByProductMultiplier() * crushedAmount), 1400, 850)
            .duration((int)(26L + (26L + material.getMass() * 4L) * crushedAmount))
            .EUt(30L);

         for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
               ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
               opBuilder1.chancedOutput(dustStack, 6700, 800);
            }
         }

         Material byproduct1 = (Material)GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
         if (byproduct1.hasProperty(PropertyKey.DUST)) {
            opBuilder1.chancedOutput(TagPrefix.dust, byproduct1, crushedAmount, "1/9", 0);
         }

         opBuilder1.save(provider);
         Material byproductMaterial1 = (Material)GTUtil.selectItemInList(1, material, property.getOreByProducts(), Material.class);
         ItemStack byproductStack2 = ChemicalHelper.get(
            TagPrefix.dust, (Material)GTUtil.selectItemInList(2, material, property.getOreByProducts(), Material.class), crushedAmount
         );
         ItemStack crushedCentrifugedStack = ChemicalHelper.get(TagPrefix.crushedRefined, material);
         if (!crushedCentrifugedStack.m_41619_()) {
            GTRecipeBuilder opBuilder2 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
               .recipeBuilder("integrated_ore_processor_2_" + material.getName(), new Object[0])
               .circuitMeta(2)
               .inputItems(tag)
               .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
               .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
               .chancedOutput(byproductStack, 1400, 850)
               .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
               .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
               .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/3", 0)
               .chancedOutput(byproductStack2, 1400, 850)
               .duration(26 + 426 * crushedAmount)
               .EUt(30L);

            for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
               if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                  ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                  opBuilder2.chancedOutput(dustStack, 6700, 800);
               }
            }

            opBuilder2.save(provider);
         }

         ItemStack byproductStack1 = ChemicalHelper.get(TagPrefix.dust, byproductMaterial1, crushedAmount);
         GTRecipeBuilder opBuilder3 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
            .recipeBuilder("integrated_ore_processor_3_" + material.getName(), new Object[0])
            .circuitMeta(3)
            .inputItems(tag)
            .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
            .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
            .chancedOutput(byproductStack, 1400, 850)
            .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
            .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
            .chancedOutput(byproductStack1, 1400, 850)
            .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
            .duration(26 + 242 * crushedAmount)
            .EUt(30L);

         for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
               ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
               opBuilder3.chancedOutput(dustStack, 6700, 800);
            }
         }

         opBuilder3.save(provider);
         if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack exquisiteStack = ChemicalHelper.get(TagPrefix.gemExquisite, material);
            ItemStack flawlessStack = ChemicalHelper.get(TagPrefix.gemFlawless, material);
            ItemStack gemStack = ChemicalHelper.get(TagPrefix.gem, material);
            if (material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT)) {
               GTRecipeBuilder opBuilder4 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("integrated_ore_processor_4_" + material.getName(), new Object[0])
                  .circuitMeta(4)
                  .inputItems(tag)
                  .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1400, 850)
                  .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
                  .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
                  .chancedOutput(exquisiteStack, 500, 150)
                  .chancedOutput(flawlessStack, 1500, 200)
                  .chancedOutput(gemStack, 5000, 1000)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);

               for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                  if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                     ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                     opBuilder4.chancedOutput(dustStack, 6700, 800);
                  }
               }

               opBuilder4.save(provider);
            } else {
               GTRecipeBuilder opBuilder4 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("integrated_ore_processor_4_" + material.getName(), new Object[0])
                  .circuitMeta(4)
                  .inputItems(tag)
                  .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1400, 850)
                  .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
                  .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
                  .chancedOutput(exquisiteStack, 300, 100)
                  .chancedOutput(flawlessStack, 1000, 150)
                  .chancedOutput(gemStack, 3500, 500)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);

               for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                  if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                     ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                     opBuilder4.chancedOutput(dustStack, 6700, 800);
                  }
               }

               opBuilder4.save(provider);
            }
         }

         if (property.getWashedIn().getFirst() != null) {
            Material washingByproduct = (Material)GTUtil.selectItemInList(3, material, property.getOreByProducts(), Material.class);
            Pair<Material, Integer> washedInTuple = property.getWashedIn();
            if (!crushedCentrifugedStack.m_41619_()) {
               GTRecipeBuilder opBuilder5 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("integrated_ore_processor_5_" + material.getName(), new Object[0])
                  .circuitMeta(5)
                  .inputItems(tag)
                  .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1400, 850)
                  .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                  .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/3", 0)
                  .chancedOutput(byproductStack2, 1400, 850)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);

               for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                  if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                     ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                     opBuilder5.chancedOutput(dustStack, 6700, 800);
                  }
               }

               opBuilder5.save(provider);
            }

            GTRecipeBuilder opBuilder6 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
               .recipeBuilder("integrated_ore_processor_6_" + material.getName(), new Object[0])
               .circuitMeta(6)
               .inputItems(tag)
               .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
               .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
               .chancedOutput(byproductStack, 1400, 850)
               .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
               .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
               .chancedOutput(byproductStack1, 1400, 850)
               .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
               .duration(26 + 242 * crushedAmount)
               .EUt(30L);

            for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
               if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                  ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                  opBuilder6.chancedOutput(dustStack, 6700, 800);
               }
            }

            opBuilder6.save(provider);
            if (material.hasProperty(PropertyKey.GEM)) {
               ItemStack exquisiteStack = ChemicalHelper.get(TagPrefix.gemExquisite, material);
               ItemStack flawlessStack = ChemicalHelper.get(TagPrefix.gemFlawless, material);
               ItemStack gemStack = ChemicalHelper.get(TagPrefix.gem, material);
               if (material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT)) {
                  GTRecipeBuilder opBuilder7 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                     .recipeBuilder("integrated_ore_processor_7_" + material.getName(), new Object[0])
                     .circuitMeta(7)
                     .inputItems(tag)
                     .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                     .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                     .chancedOutput(byproductStack, 1400, 850)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                     .chancedOutput(exquisiteStack, 500, 150)
                     .chancedOutput(flawlessStack, 1500, 200)
                     .chancedOutput(gemStack, 5000, 1000)
                     .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                     .duration(26 + 426 * crushedAmount)
                     .EUt(30L);

                  for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                     if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                        ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                        opBuilder7.chancedOutput(dustStack, 6700, 800);
                     }
                  }

                  opBuilder7.save(provider);
               } else {
                  GTRecipeBuilder opBuilder7 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                     .recipeBuilder("integrated_ore_processor_7_" + material.getName(), new Object[0])
                     .circuitMeta(7)
                     .inputItems(tag)
                     .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                     .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                     .chancedOutput(byproductStack, 1400, 850)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                     .chancedOutput(exquisiteStack, 300, 100)
                     .chancedOutput(flawlessStack, 1000, 150)
                     .chancedOutput(gemStack, 3500, 500)
                     .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                     .duration(26 + 426 * crushedAmount)
                     .EUt(30L);

                  for (MaterialStack secondaryMaterial : orePrefix.secondaryMaterials()) {
                     if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
                        ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial).m_255036_(crushedAmount);
                        opBuilder7.chancedOutput(dustStack, 6700, 800);
                     }
                  }

                  opBuilder7.save(provider);
               }
            }
         }
      }

      if (!ingotStack.m_41619_() && gtlcore$doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
         float xp = Math.round(((1.0F + oreTypeMultiplier * 0.5F) * 0.5F - 0.05F) * 10.0F) / 10.0F;
         VanillaRecipeHelper.addSmeltingRecipe(provider, "smelt_" + material.getName() + "_ore_to_ingot", tag, ingotStack, xp);
         VanillaRecipeHelper.addBlastingRecipe(provider, "smelt_" + material.getName() + "_ore_to_ingot", tag, ingotStack, xp);
      }
   }

   @Unique
   private static void gtlcore$processRawOre(TagPrefix orePrefix, Material material, OreProperty property, Consumer<FinishedRecipe> provider) {
      ItemStack crushedStack = ChemicalHelper.get(
         TagPrefix.crushed, material, ((OreProperty)material.getProperty(PropertyKey.ORE)).getOreMultiplier() * ConfigHolder.INSTANCE.oreMultiplier / 2
      );
      Material smeltingMaterial = property.getDirectSmeltResult() == null ? material : property.getDirectSmeltResult();
      ItemStack ingotStack;
      if (smeltingMaterial.hasProperty(PropertyKey.INGOT)) {
         ingotStack = ChemicalHelper.get(TagPrefix.ingot, smeltingMaterial, ((OreProperty)material.getProperty(PropertyKey.ORE)).getOreMultiplier());
      } else if (smeltingMaterial.hasProperty(PropertyKey.GEM)) {
         ingotStack = ChemicalHelper.get(TagPrefix.gem, smeltingMaterial, ((OreProperty)material.getProperty(PropertyKey.ORE)).getOreMultiplier());
      } else {
         ingotStack = ChemicalHelper.get(TagPrefix.dust, smeltingMaterial, ((OreProperty)material.getProperty(PropertyKey.ORE)).getOreMultiplier());
      }

      if (!crushedStack.m_41619_()) {
         GTRecipeBuilder builder = GTRecipeTypes.FORGE_HAMMER_RECIPES
            .recipeBuilder("hammer_" + orePrefix.name + "_" + material.getName() + "_to_crushed_ore", new Object[0])
            .inputItems(orePrefix, material)
            .duration(10)
            .EUt(16L);
         if (material.hasProperty(PropertyKey.GEM) && !TagPrefix.gem.isIgnored(material)) {
            builder.outputItems(ChemicalHelper.get(TagPrefix.gem, material, crushedStack.m_41613_()));
         } else {
            builder.outputItems(crushedStack.m_41777_());
         }

         builder.save(provider);
         GTRecipeBuilder builder2 = GTRecipeTypes.MACERATOR_RECIPES
            .recipeBuilder("macerate_" + orePrefix.name + "_" + material.getName() + "_ore_to_crushed_ore", new Object[0])
            .inputItems(orePrefix, material)
            .outputItems(GTUtil.copyAmount(crushedStack.m_41613_() * 2, new ItemStack[]{crushedStack}))
            .EUt(30L)
            .duration(26);
         Material byproductMaterial = (Material)GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
         ItemStack byproductStack = ChemicalHelper.get(TagPrefix.gem, byproductMaterial);
         if (byproductStack.m_41619_()) {
            byproductStack = ChemicalHelper.get(TagPrefix.dust, byproductMaterial);
         }

         builder2.chancedOutput(byproductStack, 1000, 300);

         for (MaterialStack secondaryMaterial : TagPrefix.ore.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
               ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial);
               builder2.chancedOutput(dustStack, 500, 100);
               break;
            }
         }

         builder2.save(provider);
         int crushedAmount = crushedStack.m_41613_() * 2;
         GTRecipeBuilder opBuilder1 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
            .recipeBuilder("raw_integrated_ore_processor_1_" + material.getName(), new Object[0])
            .circuitMeta(1)
            .inputItems(orePrefix, material)
            .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
            .chancedOutput(byproductStack, 1000, 300)
            .chancedOutput(ChemicalHelper.get(TagPrefix.dust, byproductMaterial, property.getByProductMultiplier() * crushedAmount), 1400, 850)
            .duration((int)(26L + (26L + material.getMass() * 4L) * crushedAmount))
            .EUt(30L);
         Material byproduct1 = (Material)GTUtil.selectItemInList(0, material, property.getOreByProducts(), Material.class);
         if (byproduct1.hasProperty(PropertyKey.DUST)) {
            opBuilder1.chancedOutput(TagPrefix.dust, byproduct1, crushedAmount, "1/9", 0);
         }

         opBuilder1.save(provider);
         Material byproductMaterial1 = (Material)GTUtil.selectItemInList(1, material, property.getOreByProducts(), Material.class);
         ItemStack byproductStack2 = ChemicalHelper.get(
            TagPrefix.dust, (Material)GTUtil.selectItemInList(2, material, property.getOreByProducts(), Material.class), crushedAmount
         );
         ItemStack crushedCentrifugedStack = ChemicalHelper.get(TagPrefix.crushedRefined, material);
         if (!crushedCentrifugedStack.m_41619_()) {
            GTRecipeBuilder opBuilder2 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
               .recipeBuilder("raw_integrated_ore_processor_2_" + material.getName(), new Object[0])
               .circuitMeta(2)
               .inputItems(orePrefix, material)
               .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
               .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
               .chancedOutput(byproductStack, 1000, 300)
               .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
               .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
               .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/3", 0)
               .chancedOutput(byproductStack2, 1400, 850)
               .duration(26 + 426 * crushedAmount)
               .EUt(30L);
            opBuilder2.save(provider);
         }

         ItemStack byproductStack1 = ChemicalHelper.get(TagPrefix.dust, byproductMaterial1, crushedAmount);
         GTRecipeBuilder opBuilder3 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
            .recipeBuilder("raw_integrated_ore_processor_3_" + material.getName(), new Object[0])
            .circuitMeta(3)
            .inputItems(orePrefix, material)
            .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
            .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
            .chancedOutput(byproductStack, 1000, 300)
            .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
            .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
            .chancedOutput(byproductStack1, 1400, 850)
            .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
            .duration(26 + 242 * crushedAmount)
            .EUt(30L);

         for (MaterialStack secondaryMaterial : TagPrefix.ore.secondaryMaterials()) {
            if (secondaryMaterial.material().hasProperty(PropertyKey.DUST)) {
               ItemStack dustStack = ChemicalHelper.getGem(secondaryMaterial);
               opBuilder3.chancedOutput(dustStack, 500, 100);
               break;
            }
         }

         opBuilder3.save(provider);
         if (material.hasProperty(PropertyKey.GEM)) {
            ItemStack exquisiteStack = ChemicalHelper.get(TagPrefix.gemExquisite, material);
            ItemStack flawlessStack = ChemicalHelper.get(TagPrefix.gemFlawless, material);
            ItemStack gemStack = ChemicalHelper.get(TagPrefix.gem, material);
            if (material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT)) {
               GTRecipeBuilder opBuilder4 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("raw_integrated_ore_processor_4_" + material.getName(), new Object[0])
                  .circuitMeta(4)
                  .inputItems(orePrefix, material)
                  .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1000, 300)
                  .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
                  .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
                  .chancedOutput(exquisiteStack, 500, 150)
                  .chancedOutput(flawlessStack, 1500, 200)
                  .chancedOutput(gemStack, 5000, 1000)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);
               opBuilder4.save(provider);
            } else {
               GTRecipeBuilder opBuilder4 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("raw_integrated_ore_processor_4_" + material.getName(), new Object[0])
                  .circuitMeta(4)
                  .inputItems(orePrefix, material)
                  .inputFluids(GTMaterials.DistilledWater.getFluid(100L * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1000, 300)
                  .chancedOutput(TagPrefix.dust, byproductMaterial, crushedAmount, "1/3", 0)
                  .outputItems(TagPrefix.dust, GTMaterials.Stone, crushedAmount)
                  .chancedOutput(exquisiteStack, 300, 100)
                  .chancedOutput(flawlessStack, 1000, 150)
                  .chancedOutput(gemStack, 3500, 500)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);
               opBuilder4.save(provider);
            }
         }

         if (property.getWashedIn().getFirst() != null) {
            Material washingByproduct = (Material)GTUtil.selectItemInList(3, material, property.getOreByProducts(), Material.class);
            Pair<Material, Integer> washedInTuple = property.getWashedIn();
            if (!crushedCentrifugedStack.m_41619_()) {
               GTRecipeBuilder opBuilder5 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                  .recipeBuilder("raw_integrated_ore_processor_5_" + material.getName(), new Object[0])
                  .circuitMeta(5)
                  .inputItems(orePrefix, material)
                  .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                  .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                  .chancedOutput(byproductStack, 1000, 300)
                  .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                  .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                  .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/3", 0)
                  .chancedOutput(byproductStack2, 1400, 850)
                  .duration(26 + 426 * crushedAmount)
                  .EUt(30L);
               opBuilder5.save(provider);
            }

            GTRecipeBuilder opBuilder6 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
               .recipeBuilder("raw_integrated_ore_processor_6_" + material.getName(), new Object[0])
               .circuitMeta(6)
               .inputItems(orePrefix, material)
               .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
               .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
               .chancedOutput(byproductStack, 1000, 300)
               .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
               .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
               .chancedOutput(byproductStack1, 1400, 850)
               .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
               .duration(26 + 242 * crushedAmount)
               .EUt(30L);
            opBuilder6.save(provider);
            if (material.hasProperty(PropertyKey.GEM)) {
               ItemStack exquisiteStack = ChemicalHelper.get(TagPrefix.gemExquisite, material);
               ItemStack flawlessStack = ChemicalHelper.get(TagPrefix.gemFlawless, material);
               ItemStack gemStack = ChemicalHelper.get(TagPrefix.gem, material);
               if (material.hasFlag(MaterialFlags.HIGH_SIFTER_OUTPUT)) {
                  GTRecipeBuilder opBuilder7 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                     .recipeBuilder("raw_integrated_ore_processor_7_" + material.getName(), new Object[0])
                     .circuitMeta(7)
                     .inputItems(orePrefix, material)
                     .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                     .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                     .chancedOutput(byproductStack, 1000, 300)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                     .chancedOutput(exquisiteStack, 500, 150)
                     .chancedOutput(flawlessStack, 1500, 200)
                     .chancedOutput(gemStack, 5000, 1000)
                     .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                     .duration(26 + 426 * crushedAmount)
                     .EUt(30L);
                  opBuilder7.save(provider);
               } else {
                  GTRecipeBuilder opBuilder7 = GTLRecipeTypes.INTEGRATED_ORE_PROCESSOR
                     .recipeBuilder("raw_integrated_ore_processor_7_" + material.getName(), new Object[0])
                     .circuitMeta(7)
                     .inputItems(orePrefix, material)
                     .inputFluids(((Material)washedInTuple.getFirst()).getFluid((Integer)washedInTuple.getSecond() * crushedAmount))
                     .outputItems(ChemicalHelper.get(TagPrefix.dust, material, crushedAmount))
                     .chancedOutput(byproductStack, 1000, 300)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, washingByproduct, property.getByProductMultiplier() * crushedAmount), 7000, 580)
                     .chancedOutput(ChemicalHelper.get(TagPrefix.dust, GTMaterials.Stone, crushedAmount), 4000, 650)
                     .chancedOutput(exquisiteStack, 300, 100)
                     .chancedOutput(flawlessStack, 1000, 150)
                     .chancedOutput(gemStack, 3500, 500)
                     .chancedOutput(TagPrefix.dust, byproductMaterial1, crushedAmount, "1/9", 0)
                     .duration(26 + 426 * crushedAmount)
                     .EUt(30L);
                  opBuilder7.save(provider);
               }
            }
         }
      }

      if (!ingotStack.m_41619_() && gtlcore$doesMaterialUseNormalFurnace(smeltingMaterial) && !orePrefix.isIgnored(material)) {
         float xp = Math.round((1.0F + property.getOreMultiplier() * 0.33F) / 3.0F * 10.0F) / 10.0F;
         VanillaRecipeHelper.addSmeltingRecipe(
            provider,
            "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot",
            ChemicalHelper.getTag(orePrefix, material),
            GTUtil.copyAmount(ingotStack.m_41613_(), new ItemStack[]{ingotStack}),
            xp
         );
         VanillaRecipeHelper.addBlastingRecipe(
            provider,
            "smelt_" + orePrefix.name + "_" + material.getName() + "_ore_to_ingot",
            ChemicalHelper.getTag(orePrefix, material),
            GTUtil.copyAmount(ingotStack.m_41613_(), new ItemStack[]{ingotStack}),
            xp
         );
      }

      if (!com.gregtechceu.gtceu.config.ConfigHolder.INSTANCE.recipes.disableManualCompression) {
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            "compress_" + material.getName() + "_to_ore_block",
            ChemicalHelper.get(TagPrefix.rawOreBlock, material),
            new Object[]{"BBB", "BBB", "BBB", 'B', Objects.requireNonNull(ChemicalHelper.getTag(TagPrefix.rawOre, material))}
         );
         VanillaRecipeHelper.addShapelessRecipe(
            provider,
            "decompress_" + material.getName() + "_from_ore_block",
            ChemicalHelper.get(TagPrefix.rawOre, material, 9),
            new Object[]{Objects.requireNonNull(ChemicalHelper.getTag(TagPrefix.rawOreBlock, material))}
         );
      }

      GTRecipeTypes.COMPRESSOR_RECIPES
         .recipeBuilder("compress_" + material.getName() + "to_ore_block", new Object[0])
         .inputItems(TagPrefix.rawOre, material, 9)
         .outputItems(TagPrefix.rawOreBlock, material)
         .duration(300)
         .EUt(2L)
         .save(provider);
      GTRecipeTypes.FORGE_HAMMER_RECIPES
         .recipeBuilder("decompress_" + material.getName() + "to_raw_ore", new Object[0])
         .inputItems(TagPrefix.rawOreBlock, material)
         .outputItems(TagPrefix.rawOre, material, 9)
         .circuitMeta(2)
         .duration(300)
         .EUt(2L)
         .save(provider);
   }

   @Unique
   private static boolean gtlcore$doesMaterialUseNormalFurnace(Material material) {
      return !material.hasProperty(PropertyKey.BLAST) && !material.hasFlag(MaterialFlags.NO_ORE_SMELTING);
   }
}
