package org.gtlcore.gtlcore.data.recipe.processing;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import earth.terrarium.adastra.common.registry.ModItems;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineB;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.Registries;

public class SkyTearsAndGregHeart {
   static final ItemStack[] skyFragments = new ItemStack[]{
      GTLItems.WORLD_FRAGMENTS_OVERWORLD.asStack(1),
      GTLItems.WORLD_FRAGMENTS_NETHER.asStack(1),
      GTLItems.WORLD_FRAGMENTS_END.asStack(1),
      GTLItems.WORLD_FRAGMENTS_REACTOR.asStack(1),
      GTLItems.WORLD_FRAGMENTS_MOON.asStack(1),
      GTLItems.WORLD_FRAGMENTS_MARS.asStack(1),
      GTLItems.WORLD_FRAGMENTS_VENUS.asStack(1),
      GTLItems.WORLD_FRAGMENTS_MERCURY.asStack(1),
      GTLItems.WORLD_FRAGMENTS_CERES.asStack(1),
      GTLItems.WORLD_FRAGMENTS_IO.asStack(1),
      GTLItems.WORLD_FRAGMENTS_GANYMEDE.asStack(1),
      GTLItems.WORLD_FRAGMENTS_PLUTO.asStack(1),
      GTLItems.WORLD_FRAGMENTS_ENCELADUS.asStack(1),
      GTLItems.WORLD_FRAGMENTS_TITAN.asStack(1),
      GTLItems.WORLD_FRAGMENTS_GLACIO.asStack(1),
      GTLItems.WORLD_FRAGMENTS_BARNARDA.asStack(1)
   };
   static final Material[][] skyOres = new Material[][]{
      {GTMaterials.Goethite, GTMaterials.YellowLimonite, GTMaterials.Hematite, GTMaterials.Malachite},
      {GTMaterials.Soapstone, GTMaterials.Talc, GTMaterials.GlauconiteSand, GTMaterials.Pentlandite},
      {GTMaterials.Grossular, GTMaterials.Spessartine, GTMaterials.Pyrolusite, GTMaterials.Tantalite},
      {GTMaterials.Chalcopyrite, GTMaterials.Zeolite, GTMaterials.Cassiterite, GTMaterials.Realgar},
      {GTMaterials.Chalcopyrite, GTMaterials.Iron, GTMaterials.Pyrite, GTMaterials.Copper},
      {GTMaterials.Galena, GTMaterials.Silver, GTMaterials.Lead, GTMaterials.Lead},
      {GTMaterials.Tin, GTMaterials.Tin, GTMaterials.Cassiterite, GTMaterials.Cassiterite},
      {GTMaterials.Redstone, GTMaterials.Ruby, GTMaterials.Cinnabar, GTMaterials.Cinnabar},
      {GTMaterials.Apatite, GTMaterials.Apatite, GTMaterials.TricalciumPhosphate, GTMaterials.TricalciumPhosphate},
      {GTMaterials.Graphite, GTMaterials.Diamond, GTMaterials.Coal, GTMaterials.Coal},
      {GTMaterials.Garnierite, GTMaterials.Nickel, GTMaterials.Cobaltite, GTMaterials.Pentlandite},
      {GTMaterials.Bentonite, GTMaterials.Magnetite, GTMaterials.Olivine, GTMaterials.GlauconiteSand},
      {GTMaterials.Almandine, GTMaterials.Pyrope, GTMaterials.Sapphire, GTMaterials.GreenSapphire},
      {GTMaterials.Coal, GTMaterials.Coal, GTMaterials.Coal, GTMaterials.Coal},
      {GTMaterials.Magnetite, GTMaterials.VanadiumMagnetite, GTMaterials.Gold, GTMaterials.Gold},
      {GTMaterials.Lazurite, GTMaterials.Sodalite, GTMaterials.Lapis, GTMaterials.Calcite},
      {GTMaterials.Kyanite, GTMaterials.Mica, GTMaterials.Pollucite, GTMaterials.Pollucite},
      {GTMaterials.GarnetRed, GTMaterials.GarnetYellow, GTMaterials.Amethyst, GTMaterials.Opal},
      {GTMaterials.BasalticMineralSand, GTMaterials.GraniticMineralSand, GTMaterials.FullersEarth, GTMaterials.Gypsum},
      {GTMaterials.RockSalt, GTMaterials.Salt, GTMaterials.Lepidolite, GTMaterials.Spodumene},
      {GTMaterials.CassiteriteSand, GTMaterials.GarnetSand, GTMaterials.Asbestos, GTMaterials.Diatomite},
      {GTMaterials.Oilsands, GTMaterials.Oilsands, GTMaterials.Oilsands, GTMaterials.Oilsands},
      {GTMaterials.Bastnasite, GTMaterials.Bastnasite, GTMaterials.Monazite, GTMaterials.Neodymium},
      {GTMaterials.Saltpeter, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Alunite},
      {GTMaterials.Beryllium, GTMaterials.Beryllium, GTMaterials.Emerald, GTMaterials.Emerald},
      {GTMaterials.Grossular, GTMaterials.Pyrolusite, GTMaterials.Tantalite, GTMaterials.Tantalite},
      {GTMaterials.Wulfenite, GTMaterials.Molybdenite, GTMaterials.Molybdenum, GTMaterials.Powellite},
      {GTMaterials.Quartzite, GTMaterials.CertusQuartz, GTMaterials.Barite, GTMaterials.Barite},
      {GTMaterials.Tetrahedrite, GTMaterials.Tetrahedrite, GTMaterials.Copper, GTMaterials.Stibnite},
      {GTMaterials.Goethite, GTMaterials.YellowLimonite, GTMaterials.Hematite, GTMaterials.Gold},
      {GTMaterials.BlueTopaz, GTMaterials.Topaz, GTMaterials.Chalcocite, GTMaterials.Bornite},
      {GTMaterials.NetherQuartz, GTMaterials.NetherQuartz, GTMaterials.Quartzite, GTMaterials.Quartzite},
      {GTMaterials.Sulfur, GTMaterials.Pyrite, GTMaterials.Sphalerite, GTMaterials.Sphalerite},
      {GTMaterials.Naquadah, GTMaterials.Naquadah, GTMaterials.Plutonium239, GTMaterials.Plutonium239},
      {GTMaterials.Scheelite, GTMaterials.Tungstate, GTMaterials.Lithium, GTMaterials.Lithium},
      {GTMaterials.Bauxite, GTMaterials.Ilmenite, GTMaterials.Aluminium, GTMaterials.Aluminium},
      {GTMaterials.Bornite, GTMaterials.Cooperite, GTMaterials.Platinum, GTMaterials.Palladium},
      {GTMaterials.Pitchblende, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Uraninite},
      {GTMaterials.NetherQuartz, GTMaterials.Barite, GTMaterials.Quartzite, GTMaterials.Quartzite},
      {GTMaterials.BlueTopaz, GTMaterials.BlueTopaz, GTMaterials.Topaz, GTMaterials.Topaz},
      {GTMaterials.Copper, GTMaterials.Copper, GTMaterials.Stibnite, GTMaterials.Stibnite},
      {GTMaterials.Uraninite, GTMaterials.Thorium, GTMaterials.Plutonium239, GTMaterials.Plutonium239},
      {GTMaterials.Uraninite, GTMaterials.Pitchblende, GTMaterials.Thorium, GTMaterials.Thorium},
      {GTMaterials.Apatite, GTMaterials.TricalciumPhosphate, GTMaterials.Pyrochlore, GTMaterials.Pyrochlore},
      {GTMaterials.Bentonite, GTMaterials.Magnetite, GTMaterials.Olivine, GTMaterials.GlauconiteSand},
      {GTMaterials.Magnesite, GTMaterials.Magnesite, GTLMaterials.Desh, GTLMaterials.Desh},
      {GTMaterials.Cobalt, GTMaterials.Cobalt, GTLMaterials.Calorite, GTMaterials.Magnesite},
      {GTMaterials.Gold, GTMaterials.Gold, GTLMaterials.Ostrum, GTLMaterials.Ostrum},
      {GTMaterials.Trona, GTMaterials.Trona, GTMaterials.Cooperite, GTLMaterials.Celestine},
      {GTLMaterials.Zircon, GTMaterials.Grossular, GTMaterials.Pyrolusite, GTMaterials.Tantalite}
   };
   static final int[][] skyOres_n = new int[][]{
      {64, 24, 24, 16, 800},
      {48, 32, 32, 16, 100},
      {48, 32, 32, 16, 150},
      {64, 24, 24, 16, 500},
      {64, 24, 24, 16, 800},
      {64, 48, 8, 8, 100},
      {64, 16, 32, 16, 800},
      {64, 48, 8, 8, 120},
      {64, 16, 32, 16, 100},
      {64, 48, 8, 8, 100},
      {48, 32, 32, 16, 100},
      {48, 32, 32, 16, 50},
      {48, 32, 32, 16, 150},
      {32, 32, 32, 32, 200},
      {64, 48, 8, 8, 120},
      {48, 32, 32, 16, 300},
      {64, 48, 8, 8, 50},
      {48, 32, 32, 16, 300},
      {48, 32, 32, 16, 160},
      {48, 32, 32, 16, 100},
      {48, 32, 32, 16, 320},
      {64, 48, 8, 8, 120},
      {36, 36, 28, 28, 100},
      {36, 36, 36, 20, 300},
      {38, 38, 26, 26, 250},
      {64, 48, 8, 8, 150},
      {64, 32, 16, 16, 50},
      {64, 48, 8, 8, 100},
      {36, 36, 36, 20, 800},
      {48, 32, 32, 16, 300},
      {48, 32, 32, 16, 175},
      {48, 48, 16, 16, 160},
      {64, 48, 8, 8, 500},
      {48, 48, 16, 16, 100},
      {64, 48, 8, 8, 140},
      {48, 48, 16, 16, 120},
      {48, 32, 32, 16, 60},
      {64, 16, 32, 16, 75},
      {52, 38, 22, 16, 120},
      {32, 32, 32, 32, 100},
      {32, 32, 32, 32, 100},
      {64, 48, 8, 8, 400},
      {64, 48, 8, 8, 400},
      {54, 54, 8, 8, 100},
      {26, 26, 50, 26, 75},
      {42, 22, 42, 22, 60},
      {32, 32, 32, 32, 120},
      {42, 22, 42, 22, 100},
      {32, 32, 32, 32, 200},
      {48, 32, 24, 24, 100}
   };
   static final Material[] skyFluid = new Material[]{
      GTMaterials.SaltWater,
      GTMaterials.OilHeavy,
      GTMaterials.RawOil,
      GTMaterials.Oil,
      GTMaterials.OilLight,
      GTMaterials.NaturalGas,
      GTMaterials.Lava,
      GTMaterials.NaturalGas,
      GTMaterials.Helium3,
      GTMaterials.Helium,
      GTMaterials.Radon,
      GTMaterials.SulfuricAcid,
      GTMaterials.Deuterium,
      GTMaterials.Neon,
      GTMaterials.Krypton,
      GTMaterials.Radon,
      GTMaterials.Xenon,
      GTMaterials.CoalGas,
      GTMaterials.HydrochloricAcid,
      GTMaterials.NitricAcid,
      GTMaterials.Chlorine,
      GTMaterials.Fluorine,
      GTMaterials.Benzene,
      GTMaterials.Methane,
      GTMaterials.CharcoalByproducts,
      GTLMaterials.UnknowWater
   };
   static final int[] skyFluid_n = new int[]{
      1000, 2000, 3000, 3000, 3000, 1750, 2500, 3000, 1800, 3000, 800, 2500, 3000, 2500, 2500, 2500, 2500, 3000, 3500, 3000, 4200, 3200, 1600, 2500, 2600, 600
   };
   static final int[][] skyDNumber = new int[][]{
      {22, 6}, {12, 2}, {6, 0}, {8, 0}, {4, 2}, {3, 1}, {3, 1}, {2, 1}, {4, 4}, {4, 1}, {5, 1}, {5, 1}, {3, 2}, {4, 3}, {9, 0}, {7, 1}
   };
   static final int[][] skyDMap = new int[][]{
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 0, 1, 2, 3, 4, 5},
      {22, 7, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 6, 7},
      {33, 14, 34, 35, 36, 37},
      {39, 38, 24, 23, 27, 32, 40, 26},
      {41, 35, 22, 42, 8, 9},
      {36, 34, 43, 10},
      {44, 32, 45, 11},
      {46, 10, 12},
      {27, 26, 22, 47, 13, 14, 15, 16},
      {48, 33, 44, 32, 17},
      {39, 38, 35, 32, 49, 18},
      {41, 33, 40, 10, 42, 19},
      {36, 44, 43, 20, 21},
      {23, 49, 45, 42, 22, 23, 24},
      {23, 32, 26, 48, 22, 36, 34, 46, 47},
      {41, 39, 27, 33, 34, 32, 43, 25}
   };
   static ItemStack[] skyDrill = new ItemStack[]{
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTMaterials.Steel, 1),
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTMaterials.Titanium, 1),
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTMaterials.NaquadahAlloy, 1),
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTMaterials.Neutronium, 1),
      new ItemStack(Registries.getBlock("kubejs:machine_casing_grinding_head"), 1)
   };
   static int[] skyDrill_n = new int[]{1, 16, 128, 1024};

   public static void init(Consumer<FinishedRecipe> provider) {
      if (ConfigHolder.INSTANCE.enableSkyBlokeMode) {
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            GTLCore.id("make_fragment_world_collection_machine"),
            GTLMachines.FRAGMENT_WORLD_COLLECTION_MACHINE[0].asStack(),
            new Object[]{
               "WNW",
               "PCQ",
               "XNX",
               'W',
               new UnificationEntry(TagPrefix.cableGtSingle, GTMaterials.Lead),
               'N',
               GTBlocks.RUBBER_LOG,
               'P',
               GTLItems.TREASURES_CRYSTAL,
               'Q',
               GTLItems.MINING_CRYSTAL,
               'C',
               GTMachines.HULL[1].asStack(),
               'X',
               Blocks.f_50322_
            }
         );
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            true,
            "make_large_fragment_world_collection_machine",
            MultiBlockMachineB.LARGE_FRAGMENT_WORLD_COLLECTION_MACHINE.asStack(),
            new Object[]{
               "WNW",
               "PCQ",
               "XNX",
               'W',
               CustomTags.IV_CIRCUITS,
               'N',
               GTItems.FIELD_GENERATOR_EV,
               'P',
               GTLItems.TREASURES_CRYSTAL,
               'Q',
               GTLItems.MINING_CRYSTAL,
               'C',
               GTLMachines.FRAGMENT_WORLD_COLLECTION_MACHINE[0].asStack(),
               'X',
               GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.asStack()
            }
         );

         for (int i = 0; i < 16; i++) {
            for (int k = skyDNumber[i][0]; k < skyDNumber[i][0] + skyDNumber[i][1]; k++) {
               int circuit = i == 0 ? k + 2 : k + 1;

               for (int m = 0; m < 5; m++) {
                  int fluid_output = m == 4 ? 2147483647 : skyFluid_n[skyDMap[i][k]] * skyDrill_n[m];
                  GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
                     .recipeBuilder("sky_block_digging_" + (i + 1) + "_" + (k + 1) + "_" + (m + 1), new Object[0])
                     .notConsumable(skyFragments[i])
                     .chancedInput(skyDrill[m], 100 - m * 10, 0)
                     .chancedOutput(skyFragments[i], 50, 0)
                     .chancedOutput(GTLItems.MINING_CRYSTAL.asStack(1), 5, 5)
                     .outputFluids(skyFluid[skyDMap[i][k]].getFluid(fluid_output))
                     .circuitMeta(circuit)
                     .duration(200)
                     .EUt(8L)
                     .save(provider);
               }
            }
         }

         for (int i = 0; i < skyDNumber.length; i++) {
            for (int j = 0; j < skyDNumber[i][0]; j++) {
               GTRecipeBuilder build = GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION.recipeBuilder("sky_block_digging_" + (i + 1) + "_" + (j + 1), new Object[0]);
               build.notConsumable(skyFragments[i])
                  .chancedOutput(skyFragments[i], 50, 0)
                  .chancedOutput(GTLItems.MINING_CRYSTAL.asStack(1), 5, 5)
                  .outputItems(TagPrefix.rawOre, skyOres[skyDMap[i][j]][0], skyOres_n[skyDMap[i][j]][0])
                  .outputItems(TagPrefix.rawOre, skyOres[skyDMap[i][j]][1], skyOres_n[skyDMap[i][j]][1])
                  .outputItems(TagPrefix.rawOre, skyOres[skyDMap[i][j]][2], skyOres_n[skyDMap[i][j]][2])
                  .outputItems(TagPrefix.rawOre, skyOres[skyDMap[i][j]][3], skyOres_n[skyDMap[i][j]][3])
                  .EUt(8L)
                  .duration(24000 / skyOres_n[skyDMap[i][j]][4])
                  .circuitMeta(i == 0 ? j + 2 : j + 1);
               switch (i) {
                  case 0:
                     build.outputItems(Blocks.f_50069_.m_5456_(), 32)
                        .outputItems(TagPrefix.dust, GTMaterials.Stone, 32)
                        .outputItems(Blocks.f_152550_.m_5456_(), 32)
                        .outputItems(TagPrefix.dust, GTMaterials.Deepslate, 32);
                     break;
                  case 1:
                     build.outputItems(Blocks.f_50134_.m_5456_(), 32)
                        .outputItems(TagPrefix.dust, GTMaterials.Netherrack, 32)
                        .outputItems(Blocks.f_50137_.m_5456_(), 32)
                        .outputItems(TagPrefix.dust, GTMaterials.Basalt, 32);
                     break;
                  case 2:
                     build.outputItems(Blocks.f_50259_.m_5456_(), 64).outputItems(TagPrefix.dust, GTMaterials.Endstone, 64);
                     break;
                  case 3:
                     build.outputItems(Blocks.f_50228_.m_5456_(), 64).outputItems(TagPrefix.dust, GTMaterials.Diorite, 64);
                     break;
                  case 4:
                     build.outputItems(ModItems.MOON_STONE, 64);
                     break;
                  case 5:
                     build.outputItems(ModItems.MARS_STONE, 64);
                     break;
                  case 6:
                     build.outputItems(ModItems.VENUS_STONE, 64);
                     break;
                  case 7:
                     build.outputItems(ModItems.MERCURY_STONE, 64);
                     break;
                  case 8:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:ceresstone"), 64));
                     break;
                  case 9:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:iostone"), 64));
                     break;
                  case 10:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:ganymedestone"), 64));
                     break;
                  case 11:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:plutostone"), 64));
                     break;
                  case 12:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:enceladusstone"), 64));
                     break;
                  case 13:
                     build.outputItems(new ItemStack(Registries.getBlock("kubejs:titanstone"), 64));
                     break;
                  case 14:
                     build.outputItems(ModItems.GLACIO_STONE, 64);
                     break;
                  case 15:
                     build.outputItems(Blocks.f_50069_.m_5456_(), 64);
               }

               build.save(provider);
            }
         }

         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_1", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_OVERWORLD.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_OVERWORLD.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Blocks.f_50493_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_49994_, 16), 4000, 0)
            .chancedOutput(new ItemStack(Blocks.f_49992_, 16), 3000, 0)
            .chancedOutput(new ItemStack(Items.f_42461_, 64), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_42799_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_42801_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_42800_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_41826_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_271375_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_220175_, 8), 2000, 0)
            .chancedOutput(GTMaterials.Lava.getFluid(1000L), 500, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_2", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_OVERWORLD.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_OVERWORLD.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Items.f_41909_, 8), 2000, 0)
            .chancedOutput(new ItemStack(GTBlocks.RUBBER_SAPLING, 4), 1000, 0)
            .chancedOutput(new ItemStack(Items.f_42454_, 4), 500, 0)
            .chancedOutput(new ItemStack(Items.f_42401_, 8), 500, 0)
            .chancedOutput(new ItemStack(Items.f_42784_, 1), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_41910_, 1), 2000, 0)
            .chancedOutput(new ItemStack(Blocks.f_220858_, 2), 100, 0)
            .chancedOutput(new ItemStack(Blocks.f_152500_, 2), 100, 0)
            .chancedOutput(new ItemStack(Blocks.f_50135_, 4), 5, 0)
            .chancedOutput(new ItemStack(Items.f_42747_, 1), 10, 0)
            .chancedOutput(GTMaterials.RawOil.getFluid(1000L), 2000, 0)
            .circuitMeta(1)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_3", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_REACTOR.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_REACTOR.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Blocks.f_50493_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_50228_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_50334_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_50122_, 16), 6000, 0)
            .chancedOutput(new ItemStack(GTBlocks.RED_GRANITE, 16), 6000, 0)
            .chancedOutput(new ItemStack(GTBlocks.MARBLE, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_271439_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_276445_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Registries.getItem("ae2:mysterious_cube"), 1), 100, 0)
            .chancedOutput(new ItemStack(Registries.getItem("ae2:sky_stone_block"), 16), 500, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_4", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_NETHER.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_NETHER.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Blocks.f_50135_, 16), 6000, 0)
            .chancedOutput(new ItemStack(Blocks.f_50136_, 16), 3000, 0)
            .chancedOutput(new ItemStack(Blocks.f_50722_, 4), 500, 0)
            .chancedOutput(new ItemStack(Items.f_42588_, 12), 200, 0)
            .chancedOutput(new ItemStack(Items.f_41954_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_41955_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_42585_, 8), 500, 0)
            .chancedOutput(GTMaterials.Lava.getFluid(8000L), 5000, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_5", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_END.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_END.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Blocks.f_50260_, 1), 5, 0)
            .chancedOutput(new ItemStack(Blocks.f_50320_, 1), 5, 0)
            .chancedOutput(new ItemStack(Items.f_42735_, 1), 500, 0)
            .chancedOutput(new ItemStack(Items.f_42748_, 8), 2000, 0)
            .chancedOutput(new ItemStack(Items.f_42730_, 16), 4000, 0)
            .chancedOutput(new ItemStack(Items.f_42003_, 1), 500, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_6", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_GLACIO.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_GLACIO.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Registries.getItem("kubejs:glacio_spirit"), 1), 500, 0)
            .chancedOutput(new ItemStack((ItemLike)ModItems.ICE_SHARD.get(), 1), 9500, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("sky_block_digging_special_7", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_BARNARDA.asStack(1))
            .chancedOutput(GTLItems.WORLD_FRAGMENTS_BARNARDA.asStack(1), 50, 0)
            .chancedOutput(GTLItems.TREASURES_CRYSTAL.asStack(), 5, 5)
            .chancedOutput(new ItemStack(Registries.getBlock("kubejs:barnarda_log"), 1), 500, 0)
            .chancedOutput(new ItemStack(Registries.getBlock("kubejs:barnarda_leaves"), 1), 9500, 0)
            .chancedOutput(GTLMaterials.BarnardaAir.getFluid(16000L), 2000, 0)
            .circuitMeta(0)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.WORLD_DATA_SCANNER_RECIPES
            .recipeBuilder("sky_block_make_overworld_data", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_OVERWORLD)
            .inputItems(GTItems.TOOL_DATA_STICK.asStack())
            .inputItems(TagPrefix.dust, GTMaterials.Stone, 64)
            .inputFluids(GTMaterials.PCBCoolant.getFluid(100L))
            .inputFluids(GTMaterials.Air.getFluid(64000L))
            .outputItems(Registries.getItem("kubejs:overworld_data"))
            .duration(4000)
            .EUt(32L)
            .save(provider);
         GTLRecipeTypes.WORLD_DATA_SCANNER_RECIPES
            .recipeBuilder("sky_block_make_nether_data", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_VENUS)
            .inputItems(GTItems.TOOL_DATA_STICK.asStack(2))
            .inputItems(TagPrefix.dust, GTMaterials.Netherrack, 64)
            .inputFluids(GTMaterials.PCBCoolant.getFluid(200L))
            .inputFluids(GTMaterials.NetherAir.getFluid(64000L))
            .outputItems(Registries.getItem("kubejs:nether_data"))
            .duration(4000)
            .EUt(128L)
            .save(provider);
         GTLRecipeTypes.WORLD_DATA_SCANNER_RECIPES
            .recipeBuilder("sky_block_make_end_data", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_PLUTO)
            .inputItems(GTItems.TOOL_DATA_STICK.asStack(4))
            .inputItems(TagPrefix.dust, GTMaterials.Endstone, 64)
            .inputFluids(GTMaterials.PCBCoolant.getFluid(400L))
            .inputFluids(GTMaterials.EnderAir.getFluid(64000L))
            .outputItems(Registries.getItem("kubejs:end_data"))
            .duration(4000)
            .EUt(512L)
            .save(provider);
         GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
            .recipeBuilder("make_damascus_steel_dust", new Object[0])
            .notConsumable(GTLItems.WORLD_FRAGMENTS_REACTOR)
            .inputItems(TagPrefix.dust, GTMaterials.Steel, 1)
            .inputFluids(GTMaterials.Lubricant.getFluid(100L))
            .outputItems(TagPrefix.dust, GTMaterials.DamascusSteel, 1)
            .circuitMeta(9)
            .duration(200)
            .EUt(8L)
            .save(provider);
         GTLRecipeTypes.QFT_RECIPES
            .recipeBuilder("make_miracle_crystal", new Object[0])
            .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_NETHER, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_END, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_REACTOR, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MOON, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MARS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_VENUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MERCURY, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_CERES, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_IO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GANYMEDE, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_PLUTO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_ENCELADUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_TITAN, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GLACIO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_BARNARDA, 1)
            .inputItems(GTLItems.MINING_CRYSTAL, 1)
            .inputItems(GTLItems.TREASURES_CRYSTAL, 1)
            .inputFluids(GTMaterials.UUMatter.getFluid(100000000L))
            .inputFluids(GTLMaterials.Eternity.getFluid(100000L))
            .inputFluids(GTLMaterials.Magmatter.getFluid(100000L))
            .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 3000, 0)
            .duration(2000)
            .EUt(GTValues.VEX[19])
            .save(provider);
         GTLRecipeTypes.QFT_RECIPES
            .recipeBuilder("make_miracle_crystal2", new Object[0])
            .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_NETHER, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_END, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_REACTOR, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MOON, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MARS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_VENUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MERCURY, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_CERES, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_IO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GANYMEDE, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_PLUTO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_ENCELADUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_TITAN, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GLACIO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_BARNARDA, 1)
            .inputItems(GTLItems.MINING_CRYSTAL, 1)
            .inputItems(GTLItems.TREASURES_CRYSTAL, 1)
            .inputFluids(GTLMaterials.Miracle.getFluid(1000L))
            .inputFluids(GTLMaterials.Eternity.getFluid(100000L))
            .inputFluids(GTLMaterials.Magmatter.getFluid(100000L))
            .outputItems(GTLItems.MIRACLE_CRYSTAL, 1)
            .duration(2000)
            .EUt(GTValues.VEX[20])
            .save(provider);
      }

      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_1", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(Registries.getItem("kubejs:reactor_core"))
         .inputItems(TagPrefix.block, GTMaterials.Steel, 4)
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_REACTOR, 1)
         .dimension(new ResourceLocation("overworld"))
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_2", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(ModItems.TIER_1_ROCKET)
         .inputFluids(GTMaterials.RocketFuel.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_MOON, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_3", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(ModItems.TIER_2_ROCKET)
         .inputFluids(GTLMaterials.RocketFuelRp1.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_MARS, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_4", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(ModItems.TIER_3_ROCKET)
         .inputFluids(GTLMaterials.DenseHydrazineFuelMixture.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_VENUS, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_5", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(ModItems.TIER_3_ROCKET)
         .inputFluids(GTLMaterials.DenseHydrazineFuelMixture.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_MERCURY, 1)
         .circuitMeta(31)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_6", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_VENUS, 1)
         .notConsumable(Registries.getItem("kubejs:nether_data"))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_NETHER, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_7", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(ModItems.TIER_4_ROCKET)
         .inputFluids(GTLMaterials.RocketFuelCn3h7o3.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_CERES, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_8", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(Registries.getItem("ad_astra_rocketed:tier_5_rocket"))
         .inputFluids(GTLMaterials.RocketFuelH8n4c2o4.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_IO, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_9", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(Registries.getItem("ad_astra_rocketed:tier_5_rocket"))
         .inputFluids(GTLMaterials.RocketFuelH8n4c2o4.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_GANYMEDE, 1)
         .circuitMeta(31)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_13", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_PLUTO, 1)
         .notConsumable(new ItemStack(Registries.getItem("kubejs:end_data"), 16))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_END, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_14", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(Registries.getItem("ad_astra_rocketed:tier_7_rocket"))
         .inputFluids(GTLMaterials.StellarEnergyRocketFuel.getFluid(16000L))
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_GLACIO, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);
      GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION
         .recipeBuilder("make_world_fragments_15", new Object[0])
         .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
         .notConsumable(AdvancedMultiBlockMachine.SPACE_ELEVATOR.getItem())
         .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 1, 0)
         .outputItems(GTLItems.WORLD_FRAGMENTS_BARNARDA, 1)
         .circuitMeta(32)
         .duration(200)
         .EUt(8L)
         .save(provider);

      for (int i = 0; i < 16; i++) {
         GTLRecipeTypes.ELEMENT_COPYING_RECIPES
            .recipeBuilder("copy_world_fragments_" + i, new Object[0])
            .notConsumable(skyFragments[i])
            .inputFluids(GTMaterials.UUMatter.getFluid(1000000L))
            .outputItems(skyFragments[i])
            .duration(200)
            .EUt(GTValues.VEX[18])
            .save(provider);
      }

      GTLRecipeTypes.MASS_FABRICATOR_RECIPES
         .recipeBuilder("make_miracle_gas", new Object[0])
         .inputItems(GTLItems.MIRACLE_CRYSTAL, 1)
         .inputFluids(GTLMaterials.PrimordialMatter.getFluid(100000L))
         .outputFluids(GTLMaterials.MiracleGas.getFluid(90000L))
         .duration(2000)
         .EUt(GTValues.VEX[21])
         .save(provider);
      GTRecipeTypes.DISTILLATION_RECIPES
         .recipeBuilder("make_miracle", new Object[0])
         .inputFluids(GTLMaterials.MiracleGas.getFluid(100000L))
         .outputItems(GTLItems.MIRACLE_CRYSTAL, 1)
         .outputFluids(GTLMaterials.PrimordialMatter.getFluid(90000L))
         .outputFluids(GTLMaterials.Miracle.getFluid(1000L))
         .duration(6000)
         .EUt(GTValues.VEX[22])
         .save(provider);
      GTLRecipeTypes.PRECISION_ASSEMBLER_RECIPES
         .recipeBuilder("make_super_glue", new Object[0])
         .inputItems(new ItemStack(Registries.getItem("kubejs:hyper_stable_self_healing_adhesive"), 64))
         .inputItems(new ItemStack(Registries.getItem("kubejs:cosmic_fabric"), 32))
         .inputItems(TagPrefix.foil, GTLMaterials.Shirabon, 64)
         .inputItems(new ItemStack(Registries.getItem("kubejs:chain_command_block_core"), 8))
         .inputFluids(GTLMaterials.Miracle.getFluid(1000000L))
         .inputFluids(GTLMaterials.Infinity.getFluid(1000000L))
         .inputFluids(GTLMaterials.PrimordialMatter.getFluid(1000000L))
         .inputFluids(GTLMaterials.SuperMutatedLivingSolder.getFluid(1000000L))
         .outputItems(GTLItems.SUPER_GLUE)
         .duration(10000)
         .EUt(GTValues.VEX[23])
         .save(provider);
      GTLRecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES
         .recipeBuilder("make_big_fish", new Object[0])
         .inputItems(Items.f_42529_, 2000000000)
         .inputItems(TagPrefix.block, GTLMaterials.MagnetohydrodynamicallyConstrainedStarMatter, 1000000)
         .outputItems(GTLItems.COMPRESSED_PUFFERFISH)
         .duration(200)
         .EUt(GTValues.VEX[26])
         .save(provider);
      if (!ConfigHolder.INSTANCE.enableSkyBlokeMode) {
         VanillaRecipeHelper.addShapedRecipe(
            provider,
            GTLCore.id("make_fragment_world_collection_machine"),
            GTLMachines.FRAGMENT_WORLD_COLLECTION_MACHINE[0].asStack(),
            new Object[]{
               "WNW",
               "PCP",
               "XNX",
               'X',
               new UnificationEntry(TagPrefix.cableGtSingle, GTLMaterials.CosmicNeutronium),
               'N',
               CustomTags.MAX_CIRCUITS,
               'P',
               GTLItems.SENSOR_MAX,
               'C',
               GTMachines.HULL[14].asStack(),
               'W',
               GTLItems.FIELD_GENERATOR_MAX
            }
         );
         GTLRecipeTypes.QFT_RECIPES
            .recipeBuilder("make_miracle_crystal", new Object[0])
            .notConsumable(new ItemStack(Registries.getItem("kubejs:giga_chad"), 64))
            .chancedInput(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.CosmicNeutronium, 4), 1000, 0)
            .chancedInput(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.SpaceTime, 4), 1000, 0)
            .chancedInput(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.TranscendentMetal, 4), 1000, 0)
            .chancedInput(ChemicalHelper.get(GTLTagPrefix.nanoswarm, GTLMaterials.Eternity, 4), 1000, 0)
            .inputItems(new ItemStack(Registries.getItem("kubejs:chaotic_core"), 16))
            .inputItems(new ItemStack(Registries.getItem("kubejs:quantum_anomaly"), 16))
            .inputFluids(GTLMaterials.ExcitedDtec.getFluid(1000000L))
            .inputFluids(GTLMaterials.TranscendentMetal.getFluid(100000L))
            .inputFluids(GTLMaterials.Shirabon.getFluid(100000L))
            .chancedOutput(GTLItems.MIRACLE_CRYSTAL.asStack(), 6000, 0)
            .duration(1000)
            .EUt(GTValues.VEX[18])
            .save(provider);
         GTLRecipeTypes.QFT_RECIPES
            .recipeBuilder("make_miracle_crystal2", new Object[0])
            .chancedInput(new ItemStack(Registries.getItem("kubejs:chaotic_core"), 16), 1000, 0)
            .chancedInput(new ItemStack(Registries.getItem("kubejs:quantum_anomaly"), 16), 1000, 0)
            .inputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_NETHER, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_END, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_REACTOR, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MOON, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MARS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_VENUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_MERCURY, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_CERES, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_IO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GANYMEDE, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_PLUTO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_ENCELADUS, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_TITAN, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_GLACIO, 1)
            .inputItems(GTLItems.WORLD_FRAGMENTS_BARNARDA, 1)
            .inputFluids(GTLMaterials.Miracle.getFluid(1000L))
            .inputFluids(GTLMaterials.TranscendentMetal.getFluid(100000L))
            .inputFluids(GTLMaterials.Shirabon.getFluid(100000L))
            .outputItems(GTLItems.MIRACLE_CRYSTAL, 1)
            .duration(1000)
            .EUt(GTValues.VEX[19])
            .save(provider);
         GTLRecipeTypes.GRAVITATION_SHOCKBURST_RECIPES
            .recipeBuilder("make_world_fragments_0", new Object[0])
            .inputItems(Blocks.f_50440_.m_5456_(), 64)
            .inputItems(GTLItems.MIRACLE_CRYSTAL, 1)
            .outputItems(GTLItems.WORLD_FRAGMENTS_OVERWORLD, 1)
            .duration(200)
            .EUt(GTValues.VEX[17])
            .dimension(new ResourceLocation("overworld"))
            .save(provider);
      }
   }
}
