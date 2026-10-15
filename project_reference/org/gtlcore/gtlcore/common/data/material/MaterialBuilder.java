package org.gtlcore.gtlcore.common.data.material;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.Material.Builder;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import org.gtlcore.gtlcore.api.data.chemical.material.info.GTLMaterialFlags;
import org.gtlcore.gtlcore.api.data.chemical.material.info.GTLMaterialIconSet;
import org.gtlcore.gtlcore.api.item.tool.GTLToolType;
import org.gtlcore.gtlcore.common.data.GTLElements;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.config.ConfigHolder;

public class MaterialBuilder {
   public static void init() {
      GTLMaterials.BarnardaAir = new Builder(GTCEu.id("barnarda_air")).gas().color(5650980).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.AlienAlgae = new Builder(GTCEu.id("alien_algae"))
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Paper, GTMaterials.Agar})
         .color(8421376)
         .iconSet(MaterialIconSet.WOOD)
         .buildAndRegister();
      GTLMaterials.Bloodstone = new Builder(GTCEu.id("bloodstone"))
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Deepslate, GTMaterials.Redstone})
         .color(14155830)
         .iconSet(MaterialIconSet.QUARTZ)
         .buildAndRegister();
      GTLMaterials.PerditioCrystal = new Builder(GTCEu.id("perditio_crystal"))
         .dust()
         .color(6645093)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.EarthCrystal = new Builder(GTCEu.id("earth_crystal"))
         .dust()
         .ore()
         .addOreByproducts(new Material[]{GTLMaterials.PerditioCrystal})
         .color(61696)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.IgnisCrystal = new Builder(GTCEu.id("ignis_crystal"))
         .dust()
         .ore()
         .addOreByproducts(new Material[]{GTLMaterials.PerditioCrystal})
         .color(14221312)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.InfusedGold = new Builder(GTCEu.id("infused_gold"))
         .dust()
         .ore()
         .color(13211156)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("Au?");
      GTLMaterials.Thaumium = new Builder(GTCEu.id("thaumium"))
         .dust()
         .components(new Object[]{GTLMaterials.InfusedGold, 1})
         .color(5526398)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AstralSilver = new Builder(GTCEu.id("astral_silver"))
         .dust()
         .fluid()
         .components(new Object[]{GTMaterials.Silver, 2, GTLMaterials.Thaumium, 1})
         .color(14277105)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.HighEnergyMixture = new Builder(GTCEu.id("highenergymixture")).dust().color(14407324).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.Luminessence = new Builder(GTCEu.id("luminessence"))
         .dust()
         .components(new Object[]{GTLMaterials.HighEnergyMixture, 1, GTMaterials.PhosphoricAcid, 1})
         .color(8620308)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Sunnarium = new Builder(GTCEu.id("sunnarium"))
         .ingot()
         .fluid()
         .color(16579584)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.PulsatingAlloy = new Builder(GTCEu.id("pulsating_alloy"))
         .ingot()
         .color(7004782)
         .iconSet(MaterialIconSet.SHINY)
         .components(new Object[]{GTMaterials.Iron, 1})
         .cableProperties(8L, 1, 0, true)
         .buildAndRegister();
      GTLMaterials.ConductiveAlloy = new Builder(GTCEu.id("conductive_alloy"))
         .ingot()
         .color(16233115)
         .iconSet(MaterialIconSet.MAGNETIC)
         .components(new Object[]{GTMaterials.Iron, 1, GTMaterials.Redstone, 1})
         .cableProperties(32L, 1, 0, true)
         .buildAndRegister();
      GTLMaterials.EnergeticAlloy = new Builder(GTCEu.id("energetic_alloy"))
         .ingot()
         .fluid()
         .color(16758085)
         .iconSet(MaterialIconSet.SHINY)
         .blastTemp(1650, GasTier.LOW, GTValues.VA[2], 700)
         .components(new Object[]{GTMaterials.Gold, 2, GTMaterials.Redstone, 1, GTMaterials.Glowstone, 1})
         .cableProperties(128L, 1, 0, true)
         .buildAndRegister();
      GTLMaterials.VibrantAlloy = new Builder(GTCEu.id("vibrant_alloy"))
         .ingot()
         .fluid()
         .color(10813296)
         .iconSet(MaterialIconSet.SHINY)
         .blastTemp(2375, GasTier.LOW, GTValues.VA[3], 1300)
         .components(new Object[]{GTLMaterials.EnergeticAlloy, 1, GTMaterials.EnderPearl, 1})
         .cableProperties(512L, 1, 0, true)
         .buildAndRegister();
      GTLMaterials.UuAmplifier = new Builder(GTCEu.id("uu_amplifier"))
         .fluid()
         .color(11152287)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.Celestine = new Builder(GTCEu.id("celestine"))
         .ore()
         .color(3950745)
         .components(new Object[]{GTMaterials.Strontium, 1, GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.EMERALD)
         .buildAndRegister();
      GTLMaterials.Zircon = new Builder(GTCEu.id("zircon")).ore().color(14587196).iconSet(MaterialIconSet.EMERALD).buildAndRegister().setFormula("ZrSiO\u2084");
      GTLMaterials.Jasper = new Builder(GTCEu.id("jasper"))
         .gem()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Talc, GTMaterials.Boron})
         .color(13127760)
         .iconSet(MaterialIconSet.EMERALD)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.BismuthTellurite = new Builder(GTCEu.id("bismuth_tellurite"))
         .dust()
         .components(new Object[]{GTMaterials.Bismuth, 2, GTMaterials.Tellurium, 3})
         .color(16930)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Prasiolite = new Builder(GTCEu.id("prasiolite"))
         .dust()
         .components(new Object[]{GTMaterials.Silicon, 5, GTMaterials.Oxygen, 10, GTMaterials.Iron, 1})
         .color(10401609)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CubicZirconia = new Builder(GTCEu.id("cubic_zirconia"))
         .gem()
         .color(15979991)
         .components(new Object[]{GTMaterials.Zirconium, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.EMERALD)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.MagnetoResonatic = new Builder(GTCEu.id("magneto_resonatic"))
         .gem()
         .color(16750591)
         .components(new Object[]{GTLMaterials.Prasiolite, 3, GTLMaterials.BismuthTellurite, 6, GTLMaterials.CubicZirconia, 1, GTMaterials.SteelMagnetic, 1})
         .iconSet(MaterialIconSet.MAGNETIC)
         .buildAndRegister();
      GTLMaterials.Adamantium = new Builder(GTCEu.id("adamantium"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(17700, GasTier.HIGHER)
         .element(GTLElements.ADAMANTIUM)
         .color(15711797)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_ROTOR, MaterialFlags.GENERATE_FRAME})
         .buildAndRegister();
      GTLMaterials.Quantanium = new Builder(GTCEu.id("quantanium"))
         .ingot()
         .fluid()
         .blastTemp(12500, GasTier.HIGHER)
         .element(GTLElements.QUANTANIUM)
         .color(917250)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_ROTOR, MaterialFlags.GENERATE_SMALL_GEAR, MaterialFlags.GENERATE_FRAME})
         .buildAndRegister();
      GTLMaterials.Vibranium = new Builder(GTCEu.id("vibranium"))
         .ingot()
         .fluid()
         .plasma()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Plutonium239, GTMaterials.Plutonium241})
         .blastTemp(18500, GasTier.HIGHER)
         .element(GTLElements.VIBRANIUM)
         .color(16711680)
         .iconSet(MaterialIconSet.METALLIC)
         .toolStats(
            com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty.Builder.of(1.0F, 1.0F, 100, 10, new GTToolType[]{GTToolType.DRILL_LV})
               .build()
         )
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_ROTOR, MaterialFlags.GENERATE_FRAME})
         .buildAndRegister();
      GTLMaterials.Indalloy140 = new Builder(GTCEu.id("indalloy_140"))
         .ingot()
         .fluid()
         .color(6970061)
         .blastTemp(2600, GasTier.LOW, GTValues.VA[4])
         .components(new Object[]{GTMaterials.Bismuth, 47, GTMaterials.Lead, 25, GTMaterials.Tin, 13, GTMaterials.Cadmium, 10, GTMaterials.Indium, 5})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ArtheriumSn = new Builder(GTCEu.id("artherium_sn"))
         .ingot()
         .fluid()
         .color(5577355)
         .blastTemp(9800, GasTier.HIGHER, GTValues.VA[5])
         .components(
            new Object[]{
               GTMaterials.Tin,
               12,
               GTMaterials.Actinium,
               7,
               GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide,
               5,
               GTMaterials.Caesium,
               4,
               GTMaterials.Osmiridium,
               3
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Tairitsu = new Builder(GTCEu.id("tairitsu"))
         .ingot()
         .fluid()
         .color(1842204)
         .blastTemp(12100, GasTier.HIGHER, GTValues.VA[7])
         .components(
            new Object[]{
               GTMaterials.Tungsten,
               8,
               GTMaterials.Naquadria,
               7,
               GTMaterials.Trinium,
               4,
               GTMaterials.Carbon,
               4,
               GTMaterials.Vanadium,
               3,
               GTMaterials.Plutonium239,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Draconium = new Builder(GTCEu.id("draconium"))
         .ingot()
         .fluid()
         .blastTemp(19200)
         .element(GTLElements.DRACONIUM)
         .color(10682572)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_ROTOR, MaterialFlags.GENERATE_FRAME, MaterialFlags.NO_SMELTING})
         .buildAndRegister();
      GTLMaterials.Chaos = new Builder(GTCEu.id("chaos"))
         .ingot()
         .liquid(new FluidBuilder().temperature(1000000).customStill())
         .plasma()
         .blastTemp(28000, GasTier.HIGHEST)
         .element(GTLElements.CHAOS)
         .iconSet(MaterialIconSet.DULL)
         .color(0)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Cosmic = new Builder(GTCEu.id("cosmic"))
         .ingot()
         .color(2965086)
         .iconSet(new MaterialIconSet("cosmic"))
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_LONG_ROD})
         .buildAndRegister();
      GTLMaterials.Hypogen = new Builder(GTCEu.id("hypogen"))
         .ingot()
         .fluid()
         .color(14324075)
         .secondaryColor(9410875)
         .blastTemp(34000, GasTier.HIGHEST)
         .element(GTLElements.HYPogen)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .cableProperties(2147483647L, 32768, 0, true)
         .buildAndRegister();
      GTLMaterials.Shirabon = new Builder(GTCEu.id("shirabon"))
         .ingot()
         .fluid()
         .color(12981089)
         .blastTemp(64000, GasTier.HIGHEST, GTValues.VA[13], 1200)
         .element(GTLElements.SHIRABON)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE})
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.Mithril = new Builder(GTCEu.id("mithril"))
         .ingot()
         .fluid()
         .plasma()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Actinium, GTMaterials.Technetium})
         .blastTemp(14800, GasTier.HIGHER)
         .element(GTLElements.MITHRIL)
         .color(5089023)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_SPRING, MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_SPRING_SMALL}
         )
         .cableProperties(GTValues.V[10], 2, 64)
         .buildAndRegister();
      GTLMaterials.Taranium = new Builder(GTCEu.id("taranium"))
         .ingot()
         .fluid()
         .blastTemp(16200, GasTier.HIGHEST, GTValues.VA[11], 1440)
         .element(GTLElements.TARANIUM)
         .color(51)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_SPRING, MaterialFlags.GENERATE_SPRING_SMALL})
         .cableProperties(GTValues.V[12], 2, 64)
         .buildAndRegister();
      GTLMaterials.Crystalmatrix = new Builder(GTCEu.id("crystalmatrix"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(19600, GasTier.HIGHEST)
         .element(GTLElements.CRYSTALMATRIX)
         .color(3407871)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_SPRING, MaterialFlags.GENERATE_SPRING_SMALL})
         .cableProperties(GTValues.V[13], 2, 128)
         .buildAndRegister();
      GTLMaterials.CosmicNeutronium = new Builder(GTCEu.id("cosmicneutronium"))
         .ingot()
         .liquid(new FluidBuilder().temperature(1000000).customStill())
         .blastTemp(24800, GasTier.HIGHEST)
         .element(GTLElements.COSMICNEUTRONIUM)
         .color(3354)
         .iconSet(new MaterialIconSet("cosmicneutronium", MaterialIconSet.BRIGHT))
         .flags(
            new MaterialFlag[]{
               GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_SPRING, MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.GENERATE_SPRING_SMALL
            }
         )
         .cableProperties(2147483647L, 2, 128)
         .buildAndRegister();
      GTLMaterials.Echoite = new Builder(GTCEu.id("echoite"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(17300, GasTier.HIGHER)
         .element(GTLElements.ECHOITE)
         .color(2519885)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_ROD, MaterialFlags.GENERATE_FINE_WIRE})
         .cableProperties(GTValues.V[11], 32, 0, true)
         .toolStats(
            com.gregtechceu.gtceu.api.data.chemical.material.properties.ToolProperty.Builder.of(6.0F, 100.0F, 64, 6, new GTToolType[]{GTLToolType.VAJRA})
               .magnetic()
               .unbreakable()
               .build()
         )
         .buildAndRegister();
      GTLMaterials.Legendarium = new Builder(GTCEu.id("legendarium"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(21400, GasTier.HIGHEST)
         .element(GTLElements.LEGENDARIUM)
         .color(65535)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE})
         .cableProperties(GTValues.V[12], 64, 0, true)
         .buildAndRegister();
      GTLMaterials.DraconiumAwakened = new Builder(GTCEu.id("draconiumawakened"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(22600, GasTier.HIGHEST)
         .element(GTLElements.DRACONIUMAWAKENED)
         .color(13395456)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE})
         .cableProperties(GTValues.V[13], 64, 0, true)
         .buildAndRegister();
      GTLMaterials.Adamantine = new Builder(GTCEu.id("adamantine"))
         .ingot()
         .fluid()
         .blastTemp(14400, GasTier.HIGHER)
         .element(GTLElements.ADAMANTINE)
         .color(15132160)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE})
         .cableProperties(GTValues.V[11], 4, 128)
         .buildAndRegister();
      GTLMaterials.Starmetal = new Builder(GTCEu.id("starmetal"))
         .ingot()
         .fluid()
         .plasma()
         .addOreByproducts(new Material[]{GTMaterials.Sapphire, GTMaterials.Polonium})
         .blastTemp(21800, GasTier.HIGHEST)
         .element(GTLElements.STARMETAL)
         .color(230)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FINE_WIRE})
         .cableProperties(GTValues.V[13], 4, 256)
         .buildAndRegister();
      GTLMaterials.Orichalcum = new Builder(GTCEu.id("orichalcum"))
         .ingot()
         .fluid()
         .plasma()
         .ore()
         .blastTemp(15300, GasTier.HIGHER)
         .element(GTLElements.ORICHALCUM)
         .color(16742601)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               GTLMaterialFlags.GENERATE_NANOSWARM,
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD
            }
         )
         .buildAndRegister();
      GTLMaterials.Infuscolium = new Builder(GTCEu.id("infuscolium"))
         .ingot()
         .fluid()
         .plasma()
         .blastTemp(17500, GasTier.HIGHER)
         .element(GTLElements.INFUSCOLIUM)
         .color(16742399)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(
            new MaterialFlag[]{
               GTLMaterialFlags.GENERATE_NANOSWARM,
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD
            }
         )
         .buildAndRegister();
      GTLMaterials.Enderium = new Builder(GTCEu.id("enderium"))
         .ingot()
         .fluid()
         .plasma()
         .ore()
         .fluidPipeProperties(100000, 100000, true, true, true, true)
         .addOreByproducts(new Material[]{GTMaterials.Endstone, GTMaterials.EnderPearl})
         .blastTemp(16800, GasTier.HIGHER)
         .element(GTLElements.ENDERIUM)
         .color(7728610)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FINE_WIRE})
         .buildAndRegister();
      GTLMaterials.Eternity = new Builder(GTCEu.id("eternity"))
         .ingot()
         .liquid(new FluidBuilder().customStill())
         .blastTemp(36000, null, GTValues.VA[14], 3600)
         .element(GTLElements.ETERNITY)
         .iconSet(new MaterialIconSet("eternity"))
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_FRAME})
         .buildAndRegister();
      GTLMaterials.Magmatter = new Builder(GTCEu.id("magmatter"))
         .ingot()
         .liquid(new FluidBuilder().customStill())
         .element(GTLElements.MAGMATTER)
         .iconSet(new MaterialIconSet("magmatter"))
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_LONG_ROD, MaterialFlags.NO_UNIFICATION})
         .buildAndRegister();
      GTLMaterials.DegenerateRhenium = new Builder(GTCEu.id("degenerate_rhenium"))
         .dust()
         .plasma()
         .fluid()
         .color(4605695)
         .element(GTLElements.DEGENERATE_REHENIUM)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.NO_UNIFICATION})
         .buildAndRegister();
      GTLMaterials.HeavyQuarkDegenerateMatter = new Builder(GTCEu.id("heavy_quark_degenerate_matter"))
         .ingot()
         .fluid()
         .plasma()
         .fluidPipeProperties(1000000, 1000000, true, true, true, true)
         .element(GTLElements.HEAVY_QUARK_DEGENERATE_MATTER)
         .blastTemp(178000, GasTier.HIGHER)
         .color(5416755)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FINE_WIRE})
         .buildAndRegister();
      GTLMaterials.MetastableHassium = new Builder(GTCEu.id("metastable_hassium"))
         .plasma()
         .fluid()
         .components(new Object[]{GTMaterials.Hassium, 1})
         .color(7894639)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Inconel625 = new Builder(GTCEu.id("inconel_625"))
         .ingot()
         .fluid()
         .color(52582)
         .blastTemp(4850, GasTier.HIGH, GTValues.VA[5])
         .components(
            new Object[]{
               GTMaterials.Nickel,
               8,
               GTMaterials.Chromium,
               6,
               GTMaterials.Molybdenum,
               4,
               GTMaterials.Niobium,
               4,
               GTMaterials.Titanium,
               3,
               GTMaterials.Iron,
               2,
               GTMaterials.Aluminium,
               2
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.DISABLE_DECOMPOSITION,
               MaterialFlags.GENERATE_PLATE,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_BOLT_SCREW
            }
         )
         .buildAndRegister();
      GTLMaterials.HastelloyN75 = new Builder(GTCEu.id("hastelloy_n_75"))
         .ingot()
         .fluid()
         .color(9136404)
         .blastTemp(4550, GasTier.HIGH, GTValues.VA[4])
         .components(new Object[]{GTMaterials.Nickel, 15, GTMaterials.Molybdenum, 9, GTMaterials.Chromium, 4, GTMaterials.Titanium, 2, GTMaterials.Erbium, 2})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.DISABLE_DECOMPOSITION,
               MaterialFlags.GENERATE_BOLT_SCREW,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_PLATE
            }
         )
         .buildAndRegister();
      GTLMaterials.MetastableOganesson = new Builder(GTCEu.id("metastable_oganesson"))
         .fluid()
         .color(9109518)
         .components(new Object[]{GTMaterials.Oganesson, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.QuantumChromodynamicallyConfinedMatter = new Builder(GTCEu.id("quantumchromodynamically_confined_matter"))
         .ingot()
         .fluid()
         .plasma()
         .element(GTLElements.QUANTUMCHROMODYNAMICALLY_CONFINED_MATTER)
         .blastTemp(13100, GasTier.HIGHER)
         .color(13667384)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.TranscendentMetal = new Builder(GTCEu.id("transcendentmetal"))
         .ingot()
         .fluid()
         .element(GTLElements.TRANSCENDENTMETAL)
         .color(16777215)
         .iconSet(GTLMaterialIconSet.CUSTOM_TRANSCENDENT_MENTAL)
         .flags(
            new MaterialFlag[]{
               GTLMaterialFlags.GENERATE_NANOSWARM,
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD
            }
         )
         .buildAndRegister();
      GTLMaterials.Uruium = new Builder(GTCEu.id("uruium"))
         .ingot()
         .fluid()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Europium})
         .blastTemp(14600, GasTier.HIGHER, GTValues.VA[11], 1200)
         .element(GTLElements.URUIUM)
         .color(8900331)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM})
         .iconSet(MaterialIconSet.METALLIC)
         .cableProperties(2147483647L, 16, 536870912)
         .buildAndRegister();
      GTLMaterials.MagnetohydrodynamicallyConstrainedStarMatter = new Builder(GTCEu.id("magnetohydrodynamicallyconstrainedstarmatter"))
         .ingot()
         .liquid(new FluidBuilder().temperature(100).customStill())
         .element(GTLElements.RAW_STAR_MATTER)
         .iconSet(new MaterialIconSet("magnetohydrodynamicallyconstrainedstarmatter"))
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_FOIL, MaterialFlags.NO_UNIFICATION})
         .buildAndRegister();
      GTLMaterials.WhiteDwarfMatter = new Builder(GTCEu.id("white_dwarf_mtter"))
         .ingot()
         .fluid()
         .element(GTLElements.STAR_MATTER)
         .color(16777215)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FINE_WIRE})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.BlackDwarfMatter = new Builder(GTCEu.id("black_dwarf_mtter"))
         .ingot()
         .fluid()
         .element(GTLElements.STAR_MATTER)
         .color(0)
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FINE_WIRE})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.AstralTitanium = new Builder(GTCEu.id("astraltitanium"))
         .ingot()
         .fluid()
         .plasma()
         .element(GTLElements.ASTRALTITANIUM)
         .color(16174070)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CelestialTungsten = new Builder(GTCEu.id("celestialtungsten"))
         .ingot()
         .fluid()
         .plasma()
         .element(GTLElements.CELESTIALTUNGSTEN)
         .color(3158064)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Enderite = new Builder(GTCEu.id("enderite"))
         .ingot()
         .fluid()
         .blastTemp(14400, GasTier.HIGHER, GTValues.VA[10], 600)
         .components(
            new Object[]{
               GTLMaterials.Enderium,
               3,
               GTMaterials.EnderPearl,
               2,
               GTMaterials.ManganesePhosphide,
               1,
               GTMaterials.MagnesiumDiboride,
               1,
               GTMaterials.MercuryBariumCalciumCuprate,
               1,
               GTMaterials.UraniumTriplatinum,
               1,
               GTMaterials.SamariumIronArsenicOxide,
               1,
               GTMaterials.IndiumTinBariumTitaniumCuprate,
               1
            }
         )
         .color(26265)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .cableProperties(GTValues.V[10], 32, 0, true)
         .buildAndRegister();
      GTLMaterials.NaquadriaticTaranium = new Builder(GTCEu.id("naquadriatictaranium"))
         .ingot()
         .fluid()
         .blastTemp(16200, GasTier.HIGHEST, GTValues.VA[12], 1400)
         .components(new Object[]{GTMaterials.Naquadria, 1, GTLMaterials.Taranium, 1})
         .color(3354)
         .iconSet(MaterialIconSet.RADIOACTIVE)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_ROD, MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .cableProperties(GTValues.V[12], 4, 128)
         .buildAndRegister();
      GTLMaterials.AbyssalAlloy = new Builder(GTCEu.id("abyssalalloy"))
         .ingot()
         .fluid()
         .blastTemp(10800, GasTier.HIGHER, GTValues.VA[8], 1800)
         .components(
            new Object[]{
               GTMaterials.StainlessSteel,
               5,
               GTMaterials.TungstenCarbide,
               5,
               GTMaterials.Nichrome,
               5,
               GTMaterials.Bronze,
               5,
               GTMaterials.IncoloyMA956,
               5,
               GTMaterials.Iodine,
               1,
               GTMaterials.Germanium,
               1,
               GTMaterials.Radon,
               1,
               GTMaterials.Hafnium,
               1,
               GTLMaterials.BarnardaAir,
               1
            }
         )
         .color(10383466)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .cableProperties(GTValues.V[9], 4, 64)
         .buildAndRegister();
      GTLMaterials.TitanSteel = new Builder(GTCEu.id("titansteel"))
         .ingot()
         .fluid()
         .blastTemp(12600, GasTier.HIGHER, GTValues.VA[9], 1200)
         .components(
            new Object[]{
               GTMaterials.TitaniumTungstenCarbide,
               4,
               GTMaterials.Plutonium241,
               1,
               GTMaterials.Einsteinium,
               2,
               GTMaterials.Rhenium,
               1,
               GTMaterials.Erbium,
               1,
               GTLMaterials.Jasper,
               3,
               GTLMaterials.UuAmplifier,
               1
            }
         )
         .color(15073280)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .cableProperties(GTValues.V[10], 4, 64)
         .buildAndRegister();
      GTLMaterials.Highurabilityompoundteel = new Builder(GTCEu.id("highurabilityompoundteel"))
         .ingot()
         .fluid()
         .blastTemp(12600, GasTier.HIGHER, GTValues.VA[8], 1600)
         .components(
            new Object[]{
               GTMaterials.TungstenSteel,
               12,
               GTMaterials.HSSS,
               9,
               GTMaterials.HSSG,
               6,
               GTMaterials.Ruridit,
               3,
               GTLMaterials.MagnetoResonatic,
               2,
               GTMaterials.Plutonium239,
               1
            }
         )
         .color(2964525)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_BOLT_SCREW, MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GermaniumTungstenNitride = new Builder(GTCEu.id("germaniumtungstennitride"))
         .ingot()
         .fluid()
         .blastTemp(8200, GasTier.HIGHER, GTValues.VA[6], 800)
         .components(new Object[]{GTMaterials.Germanium, 3, GTMaterials.Tungsten, 3, GTMaterials.Nitrogen, 10})
         .color(7368866)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.BlackTitanium = new Builder(GTCEu.id("black_titanium"))
         .ingot()
         .fluid()
         .blastTemp(18900, GasTier.HIGHEST, GTValues.VA[12], 600)
         .components(
            new Object[]{
               GTMaterials.Titanium,
               26,
               GTMaterials.Lanthanum,
               6,
               GTMaterials.Tungsten,
               4,
               GTMaterials.Cobalt,
               3,
               GTMaterials.Manganese,
               2,
               GTMaterials.Phosphorus,
               2,
               GTMaterials.Palladium,
               2,
               GTMaterials.Niobium,
               1,
               GTMaterials.Argon,
               5
            }
         )
         .color(3997729)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FRAME, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TriniumTitanium = new Builder(GTCEu.id("trinium_titanium"))
         .ingot()
         .fluid()
         .blastTemp(14400, GasTier.HIGHER, GTValues.VA[11], 800)
         .components(new Object[]{GTMaterials.Trinium, 2, GTMaterials.Titanium, 1})
         .color(8744849)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Cinobite = new Builder(GTCEu.id("cinobite"))
         .ingot()
         .fluid()
         .blastTemp(15400, GasTier.HIGHER, GTValues.VA[11], 1300)
         .components(
            new Object[]{
               GTMaterials.Zeron100,
               8,
               GTMaterials.Naquadria,
               4,
               GTMaterials.Terbium,
               3,
               GTMaterials.Aluminium,
               2,
               GTMaterials.Mercury,
               1,
               GTMaterials.Tin,
               1,
               GTMaterials.Titanium,
               6,
               GTMaterials.Osmiridium,
               1
            }
         )
         .color(0)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FINE_WIRE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HastelloyX78 = new Builder(GTCEu.id("hastelloyx_78"))
         .ingot()
         .fluid()
         .blastTemp(14400, GasTier.HIGHER, GTValues.VA[10], 1200)
         .components(
            new Object[]{
               GTMaterials.NaquadahAlloy,
               10,
               GTMaterials.Rhenium,
               5,
               GTMaterials.Naquadria,
               4,
               GTMaterials.Tritanium,
               4,
               GTMaterials.TungstenCarbide,
               1,
               GTMaterials.Promethium,
               1,
               GTMaterials.Mendelevium,
               1,
               GTMaterials.Praseodymium,
               1
            }
         )
         .color(3955583)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD,
               MaterialFlags.GENERATE_FINE_WIRE,
               MaterialFlags.DISABLE_DECOMPOSITION
            }
         )
         .buildAndRegister();
      GTLMaterials.HastelloyK243 = new Builder(GTCEu.id("hastelloyk_243"))
         .ingot()
         .fluid()
         .blastTemp(17200, GasTier.HIGHEST, GTValues.VA[12], 1500)
         .components(
            new Object[]{
               GTLMaterials.HastelloyX78,
               5,
               GTMaterials.NiobiumNitride,
               2,
               GTMaterials.Tritanium,
               4,
               GTMaterials.TungstenCarbide,
               4,
               GTMaterials.Promethium,
               1,
               GTMaterials.Mendelevium,
               1,
               GTMaterials.Praseodymium,
               1,
               GTMaterials.Holmium,
               1
            }
         )
         .color(9623897)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD,
               MaterialFlags.DISABLE_DECOMPOSITION
            }
         )
         .buildAndRegister();
      GTLMaterials.Vibramantium = new Builder(GTCEu.id("vibramantium"))
         .ingot()
         .fluid()
         .blastTemp(18800, GasTier.HIGHER, GTValues.VA[12], 1800)
         .components(new Object[]{GTLMaterials.Vibranium, 1, GTLMaterials.Adamantium, 3})
         .color(16711836)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.GENERATE_ROUND,
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_LONG_ROD
            }
         )
         .buildAndRegister();
      GTLMaterials.EglinSteel = new Builder(GTCEu.id("eglin_steel"))
         .ingot()
         .fluid()
         .blastTemp(1048, GasTier.LOW)
         .components(
            new Object[]{
               GTMaterials.Iron, 4, GTMaterials.Kanthal, 1, GTMaterials.Invar, 5, GTMaterials.Sulfur, 1, GTMaterials.Silicon, 1, GTMaterials.Carbon, 1
            }
         )
         .color(5121803)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_GEAR, MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING})
         .buildAndRegister();
      GTLMaterials.Inconel792 = new Builder(GTCEu.id("inconel_792"))
         .ingot()
         .fluid()
         .blastTemp(5200, GasTier.HIGH)
         .components(new Object[]{GTMaterials.Nickel, 2, GTMaterials.Niobium, 1, GTMaterials.Aluminium, 2, GTMaterials.Nichrome, 1})
         .color(4495178)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(
            new MaterialFlag[]{
               MaterialFlags.GENERATE_BOLT_SCREW, MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION
            }
         )
         .buildAndRegister();
      GTLMaterials.Pikyonium = new Builder(GTCEu.id("pikyonium"))
         .ingot()
         .fluid()
         .blastTemp(10400, GasTier.HIGHER, GTValues.VA[7], 800)
         .components(
            new Object[]{
               GTLMaterials.Inconel792,
               8,
               GTLMaterials.EglinSteel,
               5,
               GTMaterials.NaquadahEnriched,
               4,
               GTMaterials.Cerium,
               3,
               GTMaterials.Antimony,
               2,
               GTMaterials.Platinum,
               2,
               GTMaterials.Ytterbium,
               1,
               GTMaterials.TungstenSteel,
               4
            }
         )
         .color(2377600)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HastelloyN = new Builder(GTCEu.id("hastelloy_n"))
         .ingot()
         .fluid()
         .blastTemp(4350, GasTier.HIGHER, 1920)
         .components(new Object[]{GTMaterials.Iridium, 2, GTMaterials.Molybdenum, 4, GTMaterials.Chromium, 2, GTMaterials.Titanium, 2, GTMaterials.Nickel, 15})
         .color(11184810)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FRAME, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AluminiumBronze = new Builder(GTCEu.id("aluminium_bronze"))
         .ingot()
         .fluid()
         .blastTemp(1200, GasTier.LOW)
         .components(new Object[]{GTMaterials.Aluminium, 1, GTMaterials.Bronze, 6})
         .color(16768685)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FRAME})
         .buildAndRegister();
      GTLMaterials.Lafium = new Builder(GTCEu.id("lafium"))
         .ingot()
         .fluid()
         .blastTemp(9865, GasTier.HIGHER, 1920)
         .components(
            new Object[]{
               GTLMaterials.HastelloyN,
               8,
               GTMaterials.Naquadah,
               4,
               GTMaterials.Samarium,
               2,
               GTMaterials.Tungsten,
               4,
               GTMaterials.Aluminium,
               6,
               GTMaterials.Nickel,
               2,
               GTMaterials.Carbon,
               2
            }
         )
         .color(2315415)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Grisium = new Builder(GTCEu.id("grisium"))
         .ingot()
         .fluid()
         .blastTemp(4850, GasTier.HIGH)
         .components(
            new Object[]{
               GTMaterials.Titanium, 9, GTMaterials.Carbon, 9, GTMaterials.Potassium, 9, GTMaterials.Lithium, 9, GTMaterials.Sulfur, 9, GTMaterials.Hydrogen, 5
            }
         )
         .color(3497322)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.Stellite = new Builder(GTCEu.id("stellite"))
         .ingot()
         .fluid()
         .blastTemp(4310, GasTier.HIGH, 1920)
         .components(new Object[]{GTMaterials.Cobalt, 9, GTMaterials.Chromium, 9, GTMaterials.Manganese, 5, GTMaterials.Titanium, 2})
         .color(8946066)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR})
         .buildAndRegister();
      GTLMaterials.SiliconCarbide = new Builder(GTCEu.id("silicon_carbide"))
         .ingot()
         .fluid()
         .blastTemp(4800, GasTier.HIGH, 480)
         .components(new Object[]{GTMaterials.Silicon, 1, GTMaterials.Carbon, 1})
         .color(3452342)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.Quantum = new Builder(GTCEu.id("quantum"))
         .ingot()
         .fluid()
         .blastTemp(11400, GasTier.HIGHER, 1920)
         .components(
            new Object[]{
               GTLMaterials.Stellite,
               15,
               GTLMaterials.Quantanium,
               3,
               GTLMaterials.Jasper,
               2,
               GTMaterials.Gallium,
               5,
               GTMaterials.Americium,
               5,
               GTMaterials.Palladium,
               5,
               GTMaterials.Germanium,
               5,
               GTLMaterials.SiliconCarbide,
               5
            }
         )
         .color(855309)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.FluxedElectrum = new Builder(GTCEu.id("fluxed_electrum"))
         .ingot()
         .fluid()
         .blastTemp(10400, GasTier.HIGHER, 1920)
         .components(
            new Object[]{
               GTMaterials.SolderingAlloy,
               1,
               GTLMaterials.InfusedGold,
               1,
               GTMaterials.Naquadah,
               1,
               GTLMaterials.AstralSilver,
               1,
               GTMaterials.RedSteel,
               1,
               GTMaterials.BlueSteel,
               1,
               GTMaterials.SterlingSilver,
               1,
               GTMaterials.RoseGold,
               1
            }
         )
         .color(16316630)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Tanmolyium = new Builder(GTCEu.id("tanmolyium"))
         .ingot()
         .fluid()
         .blastTemp(4300, GasTier.HIGH, 1920)
         .components(
            new Object[]{GTMaterials.Titanium, 5, GTMaterials.Molybdenum, 5, GTMaterials.Vanadium, 2, GTMaterials.Chromium, 3, GTMaterials.Aluminium, 1}
         )
         .color(9905306)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.Dalisenite = new Builder(GTCEu.id("dalisenite"))
         .ingot()
         .fluid()
         .blastTemp(12400, GasTier.HIGHER, 7680)
         .components(
            new Object[]{
               GTMaterials.Erbium,
               3,
               GTMaterials.Tungsten,
               10,
               GTMaterials.Naquadah,
               1,
               GTMaterials.NiobiumTitanium,
               9,
               GTLMaterials.Quantanium,
               7,
               GTMaterials.RhodiumPlatedPalladium,
               14,
               GTLMaterials.Tanmolyium,
               1
            }
         )
         .color(10791953)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.ArceusAlloy2B = new Builder(GTCEu.id("arceusalloy2b"))
         .ingot()
         .fluid()
         .blastTemp(14200, GasTier.HIGHER, 122880)
         .components(
            new Object[]{
               GTMaterials.Trinium,
               3,
               GTMaterials.MaragingSteel300,
               4,
               GTLMaterials.Orichalcum,
               1,
               GTMaterials.NetherStar,
               2,
               GTMaterials.TungstenSteel,
               2,
               GTMaterials.Osmiridium,
               1,
               GTMaterials.Strontium,
               2
            }
         )
         .color(7959566)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.TitanPrecisionSteel = new Builder(GTCEu.id("titan_precision_steel"))
         .ingot()
         .fluid()
         .blastTemp(16000, GasTier.HIGHER, 491520)
         .components(
            new Object[]{
               GTLMaterials.TitanSteel,
               3,
               GTMaterials.Ytterbium,
               1,
               GTLMaterials.PerditioCrystal,
               1,
               GTLMaterials.EarthCrystal,
               1,
               GTLMaterials.IgnisCrystal,
               1
            }
         )
         .color(5853495)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Lumiium = new Builder(GTCEu.id("lumiium"))
         .ingot()
         .fluid()
         .blastTemp(5400, GasTier.HIGH)
         .components(new Object[]{GTMaterials.SterlingSilver, 2, GTMaterials.TinAlloy, 4, GTLMaterials.Luminessence, 2})
         .color(14279202)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.Hikarium = new Builder(GTCEu.id("hikarium"))
         .ingot()
         .fluid()
         .blastTemp(17800, GasTier.HIGHER, GTValues.VA[9])
         .components(new Object[]{GTLMaterials.Lumiium, 18, GTMaterials.Silver, 8, GTLMaterials.Sunnarium, 4})
         .color(14859998)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.SuperheavyLAlloy = new Builder(GTCEu.id("superheavy_l_alloy"))
         .ingot()
         .fluid()
         .blastTemp(10600, GasTier.HIGHER)
         .components(
            new Object[]{
               GTMaterials.Rutherfordium,
               1,
               GTMaterials.Dubnium,
               1,
               GTMaterials.Seaborgium,
               1,
               GTMaterials.Bohrium,
               1,
               GTMaterials.Hassium,
               1,
               GTMaterials.Meitnerium,
               1,
               GTMaterials.Darmstadtium,
               1,
               GTMaterials.Roentgenium,
               1
            }
         )
         .color(2835935)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SuperheavyHAlloy = new Builder(GTCEu.id("superheavy_h_alloy"))
         .ingot()
         .fluid()
         .blastTemp(10600, GasTier.HIGHER)
         .components(
            new Object[]{
               GTMaterials.Copernicium,
               1,
               GTMaterials.Nihonium,
               1,
               GTMaterials.Flerovium,
               1,
               GTMaterials.Moscovium,
               1,
               GTMaterials.Livermorium,
               1,
               GTMaterials.Tennessine,
               1,
               GTMaterials.Oganesson,
               1
            }
         )
         .color(15223606)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ZirconiumCarbide = new Builder(GTCEu.id("zirconium_carbide"))
         .ingot()
         .fluid()
         .blastTemp(6800, GasTier.HIGH, 1920, 1200)
         .components(new Object[]{GTMaterials.Zirconium, 1, GTMaterials.Carbon, 1})
         .color(13811626)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE})
         .buildAndRegister();
      GTLMaterials.MarM200Steel = new Builder(GTCEu.id("mar_m_200_steel"))
         .ingot()
         .fluid()
         .blastTemp(4600, GasTier.HIGH, GTValues.VA[5], 100)
         .components(
            new Object[]{
               GTMaterials.Niobium,
               2,
               GTMaterials.Chromium,
               9,
               GTMaterials.Aluminium,
               5,
               GTMaterials.Titanium,
               2,
               GTMaterials.Cobalt,
               10,
               GTMaterials.Tungsten,
               13,
               GTMaterials.Nickel,
               18
            }
         )
         .color(5329233)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Tantalloy61 = new Builder(GTCEu.id("tantalloy_61"))
         .ingot()
         .fluid()
         .blastTemp(6900, GasTier.HIGHER, GTValues.VA[6])
         .components(new Object[]{GTMaterials.Tantalum, 13, GTMaterials.Tungsten, 12, GTMaterials.Titanium, 6, GTMaterials.Yttrium, 4})
         .color(3552822)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_BOLT_SCREW, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ReactorSteel = new Builder(GTCEu.id("reactor_steel"))
         .ingot()
         .fluid()
         .blastTemp(3800, GasTier.HIGH, GTValues.VA[3], 700)
         .components(new Object[]{GTMaterials.Iron, 15, GTMaterials.Niobium, 1, GTMaterials.Vanadium, 4, GTMaterials.Carbon, 2})
         .color(11842480)
         .iconSet(MaterialIconSet.SHINY)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_DENSE, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Lanthanoids1 = new Builder(GTCEu.id("lanthanoids_1"))
         .dust()
         .color(15667507)
         .components(
            new Object[]{
               GTMaterials.Lanthanum,
               1,
               GTMaterials.Cerium,
               1,
               GTMaterials.Praseodymium,
               1,
               GTMaterials.Neodymium,
               1,
               GTMaterials.Promethium,
               1,
               GTMaterials.Samarium,
               1,
               GTMaterials.Europium,
               1,
               GTMaterials.Gadolinium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Lanthanoids2 = new Builder(GTCEu.id("lanthanoids_2"))
         .dust()
         .color(15667507)
         .components(
            new Object[]{
               GTMaterials.Terbium,
               1,
               GTMaterials.Dysprosium,
               1,
               GTMaterials.Holmium,
               1,
               GTMaterials.Erbium,
               1,
               GTMaterials.Thulium,
               1,
               GTMaterials.Ytterbium,
               1,
               GTMaterials.Lutetium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RAREEARTH = new Builder(GTCEu.id("rareearth"))
         .fluid()
         .ingot()
         .color(10824234)
         .blastTemp(12400, GasTier.HIGHER, GTValues.VA[9], 800)
         .components(new Object[]{GTMaterials.Scandium, 1, GTMaterials.Yttrium, 1, GTLMaterials.Lanthanoids1, 1, GTLMaterials.Lanthanoids2, 1})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Actinoids1 = new Builder(GTCEu.id("actinoids_1"))
         .dust()
         .color(8448819)
         .components(
            new Object[]{
               GTMaterials.Actinium,
               1,
               GTMaterials.Thorium,
               1,
               GTMaterials.Protactinium,
               1,
               GTMaterials.Uranium238,
               1,
               GTMaterials.Neptunium,
               1,
               GTMaterials.Plutonium239,
               1,
               GTMaterials.Americium,
               1,
               GTMaterials.Curium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Actinoids2 = new Builder(GTCEu.id("actinoids_2"))
         .dust()
         .color(8448819)
         .components(
            new Object[]{
               GTMaterials.Berkelium,
               1,
               GTMaterials.Californium,
               1,
               GTMaterials.Einsteinium,
               1,
               GTMaterials.Fermium,
               1,
               GTMaterials.Mendelevium,
               1,
               GTMaterials.Nobelium,
               1,
               GTMaterials.Lawrencium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Actinoids = new Builder(GTCEu.id("actinoids"))
         .dust()
         .color(7524910)
         .components(new Object[]{GTLMaterials.Actinoids1, 1, GTLMaterials.Actinoids2, 1})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Alkaline = new Builder(GTCEu.id("alkaline"))
         .dust()
         .color(14489707)
         .components(
            new Object[]{
               GTMaterials.Lithium,
               1,
               GTMaterials.Sodium,
               1,
               GTMaterials.Potassium,
               1,
               GTMaterials.Rubidium,
               1,
               GTMaterials.Caesium,
               1,
               GTMaterials.Francium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AlkalineEarth = new Builder(GTCEu.id("alkaline_earth"))
         .dust()
         .color(12653918)
         .components(
            new Object[]{
               GTMaterials.Beryllium,
               1,
               GTMaterials.Magnesium,
               1,
               GTMaterials.Calcium,
               1,
               GTMaterials.Strontium,
               1,
               GTMaterials.Barium,
               1,
               GTMaterials.Radium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Transition1 = new Builder(GTCEu.id("transition_1"))
         .dust()
         .color(10591901)
         .components(
            new Object[]{
               GTMaterials.Titanium,
               1,
               GTMaterials.Vanadium,
               1,
               GTMaterials.Chromium,
               1,
               GTMaterials.Manganese,
               1,
               GTMaterials.Iron,
               1,
               GTMaterials.Cobalt,
               1,
               GTMaterials.Nickel,
               1,
               GTMaterials.Copper,
               1,
               GTMaterials.Zinc,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Transition2 = new Builder(GTCEu.id("transition_2"))
         .dust()
         .color(9473420)
         .components(
            new Object[]{
               GTMaterials.Zirconium,
               1,
               GTMaterials.Niobium,
               1,
               GTMaterials.Molybdenum,
               1,
               GTMaterials.Technetium,
               1,
               GTMaterials.Ruthenium,
               1,
               GTMaterials.Rhodium,
               1,
               GTMaterials.Palladium,
               1,
               GTMaterials.Silver,
               1,
               GTMaterials.Cadmium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Transition3 = new Builder(GTCEu.id("transition_3"))
         .dust()
         .color(8618368)
         .components(
            new Object[]{
               GTMaterials.Hafnium,
               1,
               GTMaterials.Tantalum,
               1,
               GTMaterials.Tungsten,
               1,
               GTMaterials.Rhenium,
               1,
               GTMaterials.Osmium,
               1,
               GTMaterials.Iridium,
               1,
               GTMaterials.Platinum,
               1,
               GTMaterials.Gold,
               1,
               GTMaterials.Mercury,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Transition = new Builder(GTCEu.id("transition"))
         .fluid()
         .ingot()
         .color(9999764)
         .blastTemp(13600, GasTier.HIGHER, GTValues.VA[9], 900)
         .components(new Object[]{GTLMaterials.Transition1, 1, GTLMaterials.Transition2, 1, GTLMaterials.Transition3, 1})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Poor = new Builder(GTCEu.id("poor"))
         .dust()
         .color(9530642)
         .components(
            new Object[]{
               GTMaterials.Aluminium,
               1,
               GTMaterials.Gallium,
               1,
               GTMaterials.Indium,
               1,
               GTMaterials.Tin,
               1,
               GTMaterials.Thallium,
               1,
               GTMaterials.Lead,
               1,
               GTMaterials.Bismuth,
               1,
               GTMaterials.Polonium,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Metalloid = new Builder(GTCEu.id("metalloid"))
         .dust()
         .color(9530642)
         .components(
            new Object[]{
               GTMaterials.Boron,
               1,
               GTMaterials.Silicon,
               1,
               GTMaterials.Germanium,
               1,
               GTMaterials.Arsenic,
               1,
               GTMaterials.Antimony,
               1,
               GTMaterials.Tellurium,
               1,
               GTMaterials.Astatine,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NotFound = new Builder(GTCEu.id("not_found"))
         .fluid()
         .color(1969854)
         .components(
            new Object[]{
               GTMaterials.Hydrogen,
               1,
               GTMaterials.Carbon,
               1,
               GTMaterials.Nitrogen,
               1,
               GTMaterials.Oxygen,
               1,
               GTMaterials.Fluorine,
               1,
               GTMaterials.Phosphorus,
               1,
               GTMaterials.Sulfur,
               1,
               GTMaterials.Chlorine,
               1,
               GTMaterials.Selenium,
               1,
               GTMaterials.Bromine,
               1,
               GTMaterials.Iodine,
               1
            }
         )
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NobleGas = new Builder(GTCEu.id("noble_gas"))
         .fluid()
         .color(15568362)
         .components(
            new Object[]{GTMaterials.Helium, 1, GTMaterials.Neon, 1, GTMaterials.Argon, 1, GTMaterials.Krypton, 1, GTMaterials.Xenon, 1, GTMaterials.Radon, 1}
         )
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Periodicium = new Builder(GTCEu.id("periodicium"))
         .ingot()
         .fluid()
         .blastTemp(15200, GasTier.HIGHEST, GTValues.VA[10], 1200)
         .components(
            new Object[]{
               GTLMaterials.NotFound,
               1,
               GTLMaterials.NobleGas,
               1,
               GTLMaterials.Metalloid,
               1,
               GTLMaterials.Poor,
               1,
               GTLMaterials.Transition,
               1,
               GTLMaterials.AlkalineEarth,
               1,
               GTLMaterials.RAREEARTH,
               1,
               GTLMaterials.Alkaline,
               1,
               GTLMaterials.Actinoids,
               1
            }
         )
         .color(4017142)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.FallKing = new Builder(GTCEu.id("fall_king"))
         .ingot()
         .fluid()
         .blastTemp(5400, GasTier.HIGH)
         .components(new Object[]{GTMaterials.Helium, 1, GTMaterials.Lithium, 1, GTMaterials.Cobalt, 1, GTMaterials.Platinum, 1, GTMaterials.Erbium, 1})
         .color(16764779)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.WoodsGlass = new Builder(GTCEu.id("woods_glass"))
         .ingot()
         .fluid()
         .blastTemp(3600)
         .components(new Object[]{GTMaterials.SodaAsh, 6, GTMaterials.SiliconDioxide, 3, GTMaterials.Garnierite, 3, GTMaterials.BariumSulfide, 1})
         .color(7536793)
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Polyetheretherketone = new Builder(GTCEu.id("polyetheretherketone"))
         .polymer()
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 20, GTMaterials.Hydrogen, 12, GTMaterials.Oxygen, 3})
         .color(3355469)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE})
         .buildAndRegister();
      GTLMaterials.CarbonNanotubes = new Builder(GTCEu.id("carbon_nanotubes"))
         .polymer()
         .fluid()
         .color(0)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL, MaterialFlags.GENERATE_FINE_WIRE})
         .buildAndRegister();
      GTLMaterials.FullerenePolymerMatrixPulp = new Builder(GTCEu.id("fullerene_polymer_matrix_pulp"))
         .polymer()
         .fluid()
         .fluidPipeProperties(5000, 5000, true, true, true, true)
         .color(2302494)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Zylon = new Builder(GTCEu.id("zylon"))
         .polymer()
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Hydrogen, 6, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2})
         .color(13809664)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Kevlar = new Builder(GTCEu.id("kevlar"))
         .polymer()
         .fluid()
         .color(10461011)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL})
         .buildAndRegister();
      GTLMaterials.Radox = new Builder(GTCEu.id("radox"))
         .polymer()
         .fluid()
         .components(
            new Object[]{
               GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Oxygen, 7, GTMaterials.Silver, 3, GTMaterials.Concrete, 1, GTMaterials.Water, 1
            }
         )
         .color(6815844)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL, MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AdamantineCompounds = new Builder(GTCEu.id("adamantine_compounds"))
         .ore()
         .color(14329120)
         .components(new Object[]{GTLMaterials.Adamantine, 1, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RawTengam = new Builder(GTCEu.id("raw_tengam")).dust().color(8493646).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.CleanRawTengam = new Builder(GTCEu.id("clean_raw_tengam")).dust().color(8493646).iconSet(MaterialIconSet.SHINY).buildAndRegister();
      GTLMaterials.PurifiedTengam = new Builder(GTCEu.id("purified_tengam"))
         .dust()
         .element(GTLElements.TENGAM)
         .color(8493646)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.AttunedTengam = new Builder(GTCEu.id("attuned_tengam"))
         .ingot()
         .element(GTLElements.TENGAM)
         .color(8493646)
         .iconSet(MaterialIconSet.MAGNETIC)
         .flags(new MaterialFlag[]{MaterialFlags.IS_MAGNETIC, MaterialFlags.GENERATE_LONG_ROD})
         .buildAndRegister();
      GTLMaterials.PreZylon = new Builder(GTCEu.id("pre_zylon"))
         .dust()
         .color(5644368)
         .components(new Object[]{GTMaterials.Carbon, 20, GTMaterials.Hydrogen, 22, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Terephthalaldehyde = new Builder(GTCEu.id("terephthalaldehyde"))
         .dust()
         .color(4879444)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumOxide = new Builder(GTCEu.id("sodium_oxide"))
         .dust()
         .color(224750)
         .components(new Object[]{GTMaterials.Sodium, 2, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Bedrock = new Builder(GTCEu.id("bedrock")).dust().color(8421504).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.CompressedStone = new Builder(GTCEu.id("compressed_stone"))
         .dust()
         .color(6908265)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.GermaniumContainingPrecipitate = new Builder(GTCEu.id("germanium_containing_precipitate"))
         .dust()
         .color(6710937)
         .components(new Object[]{GTMaterials.Germanium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GermaniumAsh = new Builder(GTCEu.id("germanium_ash"))
         .dust()
         .color(7366297)
         .components(new Object[]{GTMaterials.Germanium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GermaniumDioxide = new Builder(GTCEu.id("germanium_dioxide"))
         .dust()
         .color(16777215)
         .components(new Object[]{GTMaterials.Germanium, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Durene = new Builder(GTCEu.id("durene"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 14})
         .color(7372944)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PyromelliticDianhydride = new Builder(GTCEu.id("pyromellitic_dianhydride"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 6})
         .color(7372944)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CoAcAbCatalyst = new Builder(GTCEu.id("co_ac_ab_catalyst")).dust().color(5522466).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.ZnFeAlClCatalyst = new Builder(GTCEu.id("znfealcl_catalyst")).dust().color(13586858).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.CalciumCarbide = new Builder(GTCEu.id("calcium_carbide"))
         .dust()
         .components(new Object[]{GTMaterials.Calcium, 1, GTMaterials.Carbon, 2})
         .color(4670526)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Difluorobenzophenone = new Builder(GTCEu.id("difluorobenzophenone"))
         .dust()
         .components(new Object[]{GTMaterials.Fluorine, 2, GTMaterials.Carbon, 13, GTMaterials.Hydrogen, 8, GTMaterials.Oxygen, 1})
         .color(13586862)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumFluoride = new Builder(GTCEu.id("sodium_fluoride"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Fluorine, 1})
         .color(4587537)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumSeaborgate = new Builder(GTCEu.id("sodium_seaborgate"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 2, GTMaterials.Seaborgium, 1, GTMaterials.Oxygen, 4})
         .color(5618644)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GoldDepletedMolybdenite = new Builder(GTCEu.id("gold_depleted_molybdenite"))
         .dust()
         .color(7697799)
         .components(new Object[]{GTMaterials.Molybdenum, 1, GTMaterials.Sulfur, 2})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MolybdenumTrioxide = new Builder(GTCEu.id("molybdenum_trioxide"))
         .dust()
         .components(new Object[]{GTMaterials.Molybdenum, 1, GTMaterials.Oxygen, 3})
         .color(7697799)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dichlorocyclooctadieneplatinum = new Builder(GTCEu.id("dichlorocyclooctadieneplatinium"))
         .dust()
         .color(13953410)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 12, GTMaterials.Chlorine, 2, GTMaterials.Platinum, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Diiodobiphenyl = new Builder(GTCEu.id("diiodobiphenyl"))
         .dust()
         .color(2626)
         .components(new Object[]{GTMaterials.Carbon, 12, GTMaterials.Hydrogen, 8, GTMaterials.Iodine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.MolybdenumConcentrate = new Builder(GTCEu.id("molybdenum_concentrate"))
         .dust()
         .color(4670526)
         .components(new Object[]{GTMaterials.Molybdenum, 1, GTMaterials.Sulfur, 2, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.BoronTrioxide = new Builder(GTCEu.id("boron_trioxide"))
         .dust()
         .components(new Object[]{GTMaterials.Boron, 2, GTMaterials.Oxygen, 3})
         .color(9414325)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LithiumNiobateNanoparticles = new Builder(GTCEu.id("lithium_niobate_nanoparticles"))
         .dust()
         .color(12697901)
         .components(new Object[]{GTMaterials.Lithium, 2, GTMaterials.Niobium, 1, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Hexanitrohexaaxaisowurtzitane = new Builder(GTCEu.id("hexanitrohexaaxaisowurtzitane"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 6, GTMaterials.Nitrogen, 12, GTMaterials.Oxygen, 12})
         .color(4015691)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilicaGel = new Builder(GTCEu.id("silica_gel")).dust().color(5751780).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.CrudeHexanitrohexaaxaisowurtzitane = new Builder(GTCEu.id("crude_hexanitrohexaaxaisowurtzitane"))
         .dust()
         .color(1661037)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 6, GTMaterials.Nitrogen, 12, GTMaterials.Oxygen, 12})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Tetraacetyldinitrosohexaazaisowurtzitane = new Builder(GTCEu.id("tetraacetyldinitrosohexaazaisowurtzitane"))
         .dust()
         .color(5243977)
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Hydrogen, 18, GTMaterials.Nitrogen, 8, GTMaterials.Oxygen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NitroniumTetrafluoroborate = new Builder(GTCEu.id("nitronium_tetrafluoroborate"))
         .dust()
         .color(3948352)
         .components(new Object[]{GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NitrosoniumTetrafluoroborate = new Builder(GTCEu.id("nitrosonium_tetrafluoroborate"))
         .dust()
         .color(4739156)
         .components(new Object[]{GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 1, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dibenzyltetraacetylhexaazaisowurtzitane = new Builder(GTCEu.id("dibenzyltetraacetylhexaazaisowurtzitane"))
         .dust()
         .color(6582349)
         .components(new Object[]{GTMaterials.Carbon, 28, GTMaterials.Hydrogen, 32, GTMaterials.Nitrogen, 6, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SuccinamidylAcetate = new Builder(GTCEu.id("succinimidyl_acetate"))
         .dust()
         .color(6582349)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 7, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Hexabenzylhexaazaisowurtzitane = new Builder(GTCEu.id("hexabenzylhexaazaisowurtzitane"))
         .dust()
         .color(6582349)
         .components(new Object[]{GTMaterials.Carbon, 48, GTMaterials.Hydrogen, 48, GTMaterials.Nitrogen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NHydroxysuccinimide = new Builder(GTCEu.id("n_hydroxysuccinimide"))
         .dust()
         .color(8089983)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 5, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SuccinicAnhydride = new Builder(GTCEu.id("succinic_anhydride"))
         .dust()
         .color(4198678)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SuccinicAcid = new Builder(GTCEu.id("succinic_acid"))
         .dust()
         .color(1068636)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Acetonitrile = new Builder(GTCEu.id("acetonitrile"))
         .dust()
         .color(5923169)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 3, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Hexamethylenetetramine = new Builder(GTCEu.id("hexamethylenetetramine"))
         .dust()
         .color(5923425)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RareEarthOxide = new Builder(GTCEu.id("rare_earth_oxide"))
         .dust()
         .color(8421376)
         .components(new Object[]{GTMaterials.Concrete, 1, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RareEarthMetal = new Builder(GTCEu.id("rare_earth_metal"))
         .dust()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.RareEarth, GTMaterials.Monazite})
         .color(7566195)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.NaquadahContainRareEarth = new Builder(GTCEu.id("naquadah_contain_rare_earth"))
         .dust()
         .color(16766720)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.NaquadahContainRareEarthFluoride = new Builder(GTCEu.id("naquadah_contain_rare_earth_fluoride"))
         .dust()
         .color(11776768)
         .components(new Object[]{GTMaterials.Concrete, 1, GTMaterials.Fluorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MetalResidue = new Builder(GTCEu.id("metal_residue")).dust().color(6627608).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.PalladiumFullereneMatrix = new Builder(GTCEu.id("palladium_fullerene_matrix"))
         .dust()
         .color(9876660)
         .components(new Object[]{GTMaterials.Palladium, 1, GTMaterials.Carbon, 73, GTMaterials.Hydrogen, 15, GTMaterials.Nitrogen, 1, GTMaterials.Iron, 1})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Fullerene = new Builder(GTCEu.id("fullerene"))
         .dust()
         .color(8831672)
         .components(new Object[]{GTMaterials.Carbon, 60})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.UnfoldedFullerene = new Builder(GTCEu.id("unfolded_fullerene"))
         .dust()
         .color(5799811)
         .components(new Object[]{GTMaterials.Carbon, 60, GTMaterials.Hydrogen, 30})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Methylbenzophenanthrene = new Builder(GTCEu.id("methylbenzophenanthrene"))
         .dust()
         .color(7939596)
         .components(new Object[]{GTMaterials.Carbon, 19, GTMaterials.Hydrogen, 14})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Sarcosine = new Builder(GTCEu.id("sarcosine"))
         .dust()
         .color(8426276)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 7, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DiphenylmethaneDiisocyanate = new Builder(GTCEu.id("diphenylmethane_diisocyanate"))
         .dust()
         .color(9338908)
         .components(new Object[]{GTMaterials.Carbon, 15, GTMaterials.Hydrogen, 10, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Pentaerythritol = new Builder(GTCEu.id("pentaerythritol"))
         .dust()
         .color(11316396)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 12, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.KelpSlurry = new Builder(GTCEu.id("kelp_slurry")).fluid().color(3368448).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.Quasifissioning = new Builder(GTCEu.id("quasifissioning"))
         .plasma()
         .color(13483573)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.ExcitedDtec = new Builder(GTCEu.id("exciteddtec")).fluid().color(11758187).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.ExcitedDtsc = new Builder(GTCEu.id("exciteddtsc")).fluid().color(11758187).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.DimensionallyTranscendentResplendentCatalyst = new Builder(GTCEu.id("dimensionallytranscendentresplendentcatalyst"))
         .fluid()
         .color(528400)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.DimensionallyTranscendentProsaicCatalyst = new Builder(GTCEu.id("dimensionallytranscendentprosaiccatalyst"))
         .fluid()
         .color(528400)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.DimensionallyTranscendentExoticCatalyst = new Builder(GTCEu.id("dimensionallytranscendentexoticcatalyst"))
         .fluid()
         .color(528400)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.DimensionallyTranscendentStellarCatalyst = new Builder(GTCEu.id("dimensionallytranscendentstellarcatalyst"))
         .fluid()
         .color(528400)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.DimensionallyTranscendentCrudeCatalyst = new Builder(GTCEu.id("dimensionallytranscendentcrudecatalyst"))
         .fluid()
         .color(528400)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.Ytterbium178 = new Builder(GTCEu.id("ytterbium_178"))
         .fluid()
         .element(GTLElements.YITTERBIUM178)
         .color(16777215)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Flyb = new Builder(GTCEu.id("flyb"))
         .plasma()
         .components(new Object[]{GTMaterials.Flerovium, 1, GTLMaterials.Ytterbium178, 1})
         .color(8981141)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EnrichedPotassiumIodideSlurry = new Builder(GTCEu.id("enriched_potassium_iodide_slurry"))
         .fluid()
         .color(65484)
         .components(new Object[]{GTMaterials.Potassium, 1, GTMaterials.Iodine, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.IodineContainingSlurry = new Builder(GTCEu.id("iodine_containing_slurry"))
         .fluid()
         .color(6710835)
         .components(new Object[]{GTMaterials.Iodine, 1, GTMaterials.RockSalt, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AshLeachingSolution = new Builder(GTCEu.id("ash_leaching_solution"))
         .fluid()
         .color(6710937)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.Tannic = new Builder(GTCEu.id("tannic"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 76, GTMaterials.Hydrogen, 52, GTMaterials.Oxygen, 46})
         .color(16777062)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GermaniumTetrachlorideSolution = new Builder(GTCEu.id("germanium_tetrachloride_solution"))
         .fluid()
         .color(6750156)
         .components(new Object[]{GTMaterials.Germanium, 1, GTMaterials.Chlorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Polyimide = new Builder(GTCEu.id("polyimide"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 22, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 6})
         .color(16738096)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Aniline = new Builder(GTCEu.id("aniline"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 7, GTMaterials.Nitrogen, 1})
         .color(4027671)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Oxydianiline = new Builder(GTCEu.id("oxydianiline"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 12, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 1})
         .color(16766720)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BoricAcid = new Builder(GTCEu.id("boric_acide"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .components(new Object[]{GTMaterials.Hydrogen, 3, GTMaterials.Boron, 1, GTMaterials.Oxygen, 3})
         .color(9419919)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FluoroboricAcid = new Builder(GTCEu.id("fluoroboric_acide"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4})
         .color(9419919)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BenzenediazoniumTetrafluoroborate = new Builder(GTCEu.id("benzenediazonium_tetrafluoroborate"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 5, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4, GTMaterials.Nitrogen, 2})
         .color(9419919)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FluoroBenzene = new Builder(GTCEu.id("fluoro_benzene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 5, GTMaterials.Fluorine, 1})
         .color(9419919)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Fluorotoluene = new Builder(GTCEu.id("fluorotoluene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 7, GTMaterials.Fluorine, 1})
         .color(14340998)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Hydroquinone = new Builder(GTCEu.id("hydroquinone"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .color(9315608)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Resorcinol = new Builder(GTCEu.id("resorcinol"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .color(9315608)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumNitrate = new Builder(GTCEu.id("sodium_nitrate"))
         .dust()
         .color(5122624)
         .iconSet(MaterialIconSet.SAND)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .buildAndRegister();
      GTLMaterials.SodiumNitrateSolution = new Builder(GTCEu.id("sodium_nitrate_solution"))
         .fluid()
         .components(new Object[]{GTLMaterials.SodiumNitrate, 1, GTMaterials.Water, 1})
         .color(2832510)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Acetylene = new Builder(GTCEu.id("acetylene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 2})
         .color(8357202)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumCyanide = new Builder(GTCEu.id("sodium_cyanide"))
         .fluid()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Carbon, 1, GTMaterials.Nitrogen, 1})
         .color(5203828)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GoldCyanide = new Builder(GTCEu.id("gold_cyanide"))
         .fluid()
         .components(new Object[]{GTMaterials.Gold, 1, GTMaterials.Carbon, 1, GTMaterials.Nitrogen, 1})
         .color(7565136)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.MolybdenumFlue = new Builder(GTCEu.id("molybdenum_flue"))
         .gas()
         .color(4940399)
         .components(new Object[]{GTMaterials.Sulfur, 2, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RheniumSulfuricSolution = new Builder(GTCEu.id("rhenium_sulfuric_solution"))
         .fluid()
         .color(12632298)
         .components(new Object[]{GTMaterials.Rhenium, 1, GTMaterials.Sulfur, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AmmoniumPerrhenate = new Builder(GTCEu.id("ammonium_perrhenate"))
         .fluid()
         .color(1447479)
         .components(new Object[]{GTMaterials.Ammonia, 1, GTMaterials.Rhenium, 1, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Cycloparaphenylene = new Builder(GTCEu.id("cycloparaphenylene")).fluid().color(0).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.TrimethylTinChloride = new Builder(GTCEu.id("trimethyltin_chloride"))
         .fluid()
         .color(7497823)
         .components(new Object[]{GTMaterials.Tin, 1, GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 9, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilverTetrafluoroborate = new Builder(GTCEu.id("silver_tetrafluoroborate"))
         .fluid()
         .color(7763215)
         .components(new Object[]{GTMaterials.Silver, 1, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BoronFluoride = new Builder(GTCEu.id("boron_fluoride"))
         .fluid()
         .components(new Object[]{GTMaterials.Boron, 1, GTMaterials.Fluorine, 3})
         .color(13552336)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.OneOctene = new Builder(GTCEu.id("1_octene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 16})
         .color(6712673)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Pyridine = new Builder(GTCEu.id("pyridine"))
         .fluid()
         .color(5592642)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 5, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Acetaldehyde = new Builder(GTCEu.id("acetaldehyde"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 1})
         .color(6712673)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Cyclooctadiene = new Builder(GTCEu.id("cyclooctadiene"))
         .fluid()
         .color(13953410)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 12})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SeaborgiumDopedNanotubes = new Builder(GTCEu.id("seaborgium_doped_nanotubes"))
         .fluid()
         .color(2368627)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Ethylenediamine = new Builder(GTCEu.id("ethylenediamine"))
         .fluid()
         .color(1793396)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 8, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Ethanolamine = new Builder(GTCEu.id("ethanolamine"))
         .fluid()
         .color(1793396)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 7, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.EthyleneOxide = new Builder(GTCEu.id("ethylene_oxide"))
         .fluid()
         .color(9353176)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Benzaldehyde = new Builder(GTCEu.id("benzaldehyde"))
         .fluid()
         .color(9460251)
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HydroxylamineHydrochloride = new Builder(GTCEu.id("hydroxylamine_hydrochloride"))
         .fluid()
         .color(4403735)
         .components(new Object[]{GTMaterials.Hydrogen, 4, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 1, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.MaleicAnhydride = new Builder(GTCEu.id("maleic_anhydride"))
         .fluid()
         .color(3283856)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Benzylamine = new Builder(GTCEu.id("benzylamine"))
         .fluid()
         .color(5989219)
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 9, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Glyoxal = new Builder(GTCEu.id("glyoxal"))
         .fluid()
         .color(15789389)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BenzylChloride = new Builder(GTCEu.id("benzyl_chloride"))
         .fluid()
         .color(10483451)
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 7, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Mana = new Builder(GTCEu.id("mana")).gas().color(9699539).element(GTLElements.MANA).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.RareEarthHydroxides = new Builder(GTCEu.id("rare_earth_hydroxides"))
         .fluid()
         .color(8421376)
         .components(new Object[]{GTMaterials.Concrete, 1, GTMaterials.Oxygen, 1, GTMaterials.Hydrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.QuantumDots = new Builder(GTCEu.id("quantum_dots")).fluid().color(14286848).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.StearicAcid = new Builder(GTCEu.id("stearic_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(2332561)
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 36, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Soap = new Builder(GTCEu.id("soap")).fluid().color(16751899).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.Tricotylphosphine = new Builder(GTCEu.id("tricotylphosphine"))
         .fluid()
         .color(15193360)
         .components(new Object[]{GTMaterials.Carbon, 24, GTMaterials.Hydrogen, 51, GTMaterials.Phosphorus, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.IridiumTrichlorideSolution = new Builder(GTCEu.id("iridium_trichloride_solution"))
         .fluid()
         .color(7825173)
         .components(new Object[]{GTMaterials.Iridium, 1, GTMaterials.Chlorine, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LiquidHydrogen = new Builder(GTCEu.id("liquid_hydrogen"))
         .fluid()
         .color(5227682)
         .components(new Object[]{GTMaterials.Hydrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.BedrockSmoke = new Builder(GTCEu.id("bedrock_smoke")).gas().color(11119017).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.BedrockSootSolution = new Builder(GTCEu.id("bedrock_soot_solution"))
         .fluid()
         .color(1644912)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("Nq?");
      GTLMaterials.CleanBedrockSolution = new Builder(GTCEu.id("clean_bedrock_solution"))
         .fluid()
         .color(7833753)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.BedrockGas = new Builder(GTCEu.id("bedrock_gas")).gas().color(12632256).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.VibraniumUnstable = new Builder(GTCEu.id("vibranium_unstable"))
         .fluid()
         .color(16416882)
         .components(new Object[]{GTLMaterials.Vibranium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TaraniumEnrichedLiquidHelium3 = new Builder(GTCEu.id("taranium_enriched_liquid_helium_3"))
         .fluid()
         .color(5763693)
         .components(new Object[]{GTLMaterials.Taranium, 1, GTMaterials.Helium3, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TaraniumRichLiquidHelium4 = new Builder(GTCEu.id("taranium_rich_liquid_helium_4"))
         .fluid()
         .plasma()
         .color(5763693)
         .components(new Object[]{GTLMaterials.Taranium, 1, GTMaterials.Helium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.FreeElectronGas = new Builder(GTCEu.id("free_electron_gas"))
         .gas()
         .color(212028)
         .element(GTLElements.ELECTRON)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FreeAlphaGas = new Builder(GTCEu.id("free_alpha_gas"))
         .gas()
         .color(11905798)
         .element(GTLElements.ALPHA)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FreeProtonGas = new Builder(GTCEu.id("free_proton_gas"))
         .gas()
         .color(2132394)
         .element(GTLElements.PROTON)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.QuarkGluon = new Builder(GTCEu.id("quark_gluon"))
         .plasma()
         .color(7995610)
         .element(GTLElements.QUARK_GLUON)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HeavyQuarks = new Builder(GTCEu.id("heavy_quarks"))
         .gas()
         .color(28672)
         .element(GTLElements.HEAVY_QUARKS)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LightQuarks = new Builder(GTCEu.id("light_quarks"))
         .gas()
         .color(206)
         .element(GTLElements.LIGHT_QUARKS)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Gluons = new Builder(GTCEu.id("gluons")).gas().color(16514041).element(GTLElements.GLUONS).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.OganessonBreedingBase = new Builder(GTCEu.id("oganesson_breeding_base"))
         .fluid()
         .color(12085100)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TitaniumTetrafluoride = new Builder(GTCEu.id("titanium_tetrafluoride"))
         .fluid()
         .color(14061549)
         .components(new Object[]{GTMaterials.Titanium, 1, GTMaterials.Fluorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Titanium50 = new Builder(GTCEu.id("titanium_50"))
         .ingot()
         .fluid()
         .element(GTLElements.TITANIUM50)
         .blastTemp(1942)
         .color(13995757)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.Titanium50Tetrafluoride = new Builder(GTCEu.id("titanium_50_tetrafluoride"))
         .fluid()
         .color(14061549)
         .components(new Object[]{GTLMaterials.Titanium50, 1, GTMaterials.Fluorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Titanium50Tetrachloride = new Builder(GTCEu.id("titanium_50_tetrachloride"))
         .fluid()
         .color(11529966)
         .components(new Object[]{GTLMaterials.Titanium50, 1, GTMaterials.Chlorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HotOganesson = new Builder(GTCEu.id("hot_oganesson"))
         .fluid()
         .color(4330589)
         .components(new Object[]{GTMaterials.Oganesson, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Ferrocene = new Builder(GTCEu.id("ferrocene"))
         .fluid()
         .color(7566281)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 10, GTMaterials.Iron, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ScandiumTitanium50Mixture = new Builder(GTCEu.id("scandium_titanium_50_mixture"))
         .fluid()
         .color(15413599)
         .components(new Object[]{GTMaterials.Scandium, 1, GTLMaterials.Titanium50, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DragonBreath = new Builder(GTCEu.id("dragon_breath"))
         .fluid()
         .color(16711935)
         .components(new Object[]{GTLMaterials.Draconium, 1, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EnrichedDragonBreath = new Builder(GTCEu.id("enriched_dragon_breath"))
         .fluid()
         .color(16711935)
         .components(new Object[]{GTLMaterials.Draconium, 1, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DragonBlood = new Builder(GTCEu.id("dragon_blood"))
         .fluid()
         .color(10040012)
         .components(new Object[]{GTLMaterials.Draconium, 1, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TurbidDragonBlood = new Builder(GTCEu.id("turbid_dragon_blood"))
         .fluid()
         .color(5046425)
         .components(new Object[]{GTLMaterials.Draconium, 1, GTMaterials.Concrete, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DragonElement = new Builder(GTCEu.id("dragon_element"))
         .fluid()
         .color(10040319)
         .iconSet(MaterialIconSet.DULL)
         .components(new Object[]{GTLMaterials.Draconium, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HeavyLeptonMixture = new Builder(GTCEu.id("heavy_lepton_mixture"))
         .gas()
         .color(3856689)
         .element(GTLElements.HEAVY_LEPTON_MIXTURE)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HeavyQuarkEnrichedMixture = new Builder(GTCEu.id("heavy_quark_enriched_mixture"))
         .gas()
         .color(15527148)
         .element(GTLElements.HEAVY_QUARK_ENRICHED_MIXTURE)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CosmicComputingMixture = new Builder(GTCEu.id("cosmic_computing_mixture"))
         .gas()
         .color(9144613)
         .components(new Object[]{GTLMaterials.Gluons, 1, GTLMaterials.HeavyQuarks, 1, GTLMaterials.HeavyLeptonMixture, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LiquidStarlight = new Builder(GTCEu.id("liquid_starlight"))
         .liquid(new FluidBuilder().customStill())
         .element(GTLElements.STARLIGHT)
         .buildAndRegister();
      GTLMaterials.Starlight = new Builder(GTCEu.id("starlight")).liquid(new FluidBuilder().customStill()).element(GTLElements.STARLIGHT).buildAndRegister();
      GTLMaterials.DenseNeutron = new Builder(GTCEu.id("dense_neutron"))
         .plasma()
         .color(10283164)
         .element(GTLElements.DENSE_NEUTRON)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HighEnergyQuarkGluon = new Builder(GTCEu.id("high_energy_quark_gluon"))
         .plasma()
         .color(7602382)
         .element(GTLElements.HEAVY_QUARK_ENRICHED_MIXTURE)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.NeutroniumDopedNanotubes = new Builder(GTCEu.id("neutronium_doped_nanotubes"))
         .fluid()
         .color(6026741)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.AmmoniumNitrateSolution = new Builder(GTCEu.id("ammonium_nitrate_solution"))
         .fluid()
         .color(14811135)
         .components(new Object[]{GTMaterials.Nitrogen, 2, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NaquadahSolution = new Builder(GTCEu.id("naquadah_solution")).fluid().color(65280).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.FluorineCrackedAquadah = new Builder(GTCEu.id("fluorine_cracked_aquadah"))
         .fluid()
         .color(4345163)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RadonCrackedEnrichedAquadah = new Builder(GTCEu.id("radon_cracked_enriched_aquadah"))
         .fluid()
         .color(4345163)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NaquadahFuel = new Builder(GTCEu.id("naquadah_fuel"))
         .fluid()
         .color(2697513)
         .components(new Object[]{GTMaterials.Naquadah, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EnrichedNaquadahFuel = new Builder(GTCEu.id("enriched_naquadah_fuel"))
         .fluid()
         .color(2697513)
         .components(new Object[]{GTMaterials.NaquadahEnriched, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HyperFuel1 = new Builder(GTCEu.id("hyper_fuel_1"))
         .fluid()
         .color(16383805)
         .components(new Object[]{GTMaterials.Naquadah, 1, GTMaterials.NaquadahEnriched, 1, GTMaterials.Naquadria, 1, GTMaterials.Thorium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HyperFuel2 = new Builder(GTCEu.id("hyper_fuel_2"))
         .fluid()
         .color(13751629)
         .components(new Object[]{GTLMaterials.HyperFuel1, 1, GTMaterials.Uranium235, 1, GTMaterials.Dubnium, 1, GTMaterials.Fermium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HyperFuel3 = new Builder(GTCEu.id("hyper_fuel_3"))
         .fluid()
         .color(8027196)
         .components(new Object[]{GTLMaterials.HyperFuel2, 1, GTMaterials.Plutonium241, 1, GTLMaterials.Adamantine, 1, GTMaterials.Lawrencium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HyperFuel4 = new Builder(GTCEu.id("hyper_fuel_4"))
         .fluid()
         .color(4145192)
         .components(new Object[]{GTLMaterials.HyperFuel3, 1, GTMaterials.Nobelium, 1, GTMaterials.Neutronium, 1, GTLMaterials.Taranium, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ConcentrationMixingHyperFuel1 = new Builder(GTCEu.id("concentration_mixing_hyper_fuel_1"))
         .fluid()
         .color(0)
         .components(new Object[]{GTLMaterials.HyperFuel4, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ConcentrationMixingHyperFuel2 = new Builder(GTCEu.id("concentration_mixing_hyper_fuel_2"))
         .fluid()
         .color(0)
         .components(new Object[]{GTLMaterials.HyperFuel4, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CosmicElement = new Builder(GTCEu.id("cosmic_element"))
         .gas()
         .color(10708735)
         .components(
            new Object[]{
               GTLMaterials.FreeElectronGas,
               1,
               GTLMaterials.FreeAlphaGas,
               1,
               GTLMaterials.FreeProtonGas,
               1,
               GTLMaterials.QuarkGluon,
               1,
               GTLMaterials.HeavyQuarks,
               1,
               GTLMaterials.LightQuarks,
               1,
               GTLMaterials.Gluons,
               1,
               GTLMaterials.HeavyLeptonMixture,
               1,
               GTLMaterials.HeavyQuarkEnrichedMixture,
               1,
               GTLMaterials.DenseNeutron,
               1,
               GTLMaterials.HighEnergyQuarkGluon,
               1
            }
         )
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DimensionallyTranscendentResidue = new Builder(GTCEu.id("dimensionallytranscendentresidue"))
         .liquid(new FluidBuilder().customStill())
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.PrimordialMatter = new Builder(GTCEu.id("primordialmatter")).liquid(new FluidBuilder().customStill()).buildAndRegister().setFormula("?");
      GTLMaterials.SpatialFluid = new Builder(GTCEu.id("spatialfluid"))
         .liquid(new FluidBuilder().customStill())
         .element(GTLElements.TIME)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TemporalFluid = new Builder(GTCEu.id("temporalfluid"))
         .liquid(new FluidBuilder().customStill())
         .element(GTLElements.TIME)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Isochloropropane = new Builder(GTCEu.id("isochloropropane"))
         .fluid()
         .color(13489793)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 7, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dinitrodipropanyloxybenzene = new Builder(GTCEu.id("dinitrodipropanyloxybenzene"))
         .fluid()
         .color(6977613)
         .components(new Object[]{GTMaterials.Carbon, 12, GTMaterials.Hydrogen, 16, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RadoxGas = new Builder(GTCEu.id("radox_gas"))
         .gas()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Silver, 3, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(11229099)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CrackedRadox = new Builder(GTCEu.id("crackedradox"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Silver, 3, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(11229099)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SuperLightRadox = new Builder(GTCEu.id("superlightradox"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Silver, 2, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(7077996)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LightRadox = new Builder(GTCEu.id("lightradox"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Silver, 1, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(7077996)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SuperHeavyRadox = new Builder(GTCEu.id("superheavyradox"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(7077996)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HeavyRadox = new Builder(GTCEu.id("heavyradox"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Osmium, 11, GTMaterials.Concrete, 1, GTMaterials.Water, 1})
         .color(7077996)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RawRadox = new Builder(GTCEu.id("rawradox")).fluid().color(3740985).iconSet(MaterialIconSet.BRIGHT).buildAndRegister().setFormula("?");
      GTLMaterials.Xenoxene = new Builder(GTCEu.id("xenoxene")).fluid().color(6052440).iconSet(MaterialIconSet.BRIGHT).buildAndRegister().setFormula("?");
      GTLMaterials.XenoxeneCrystal = new Builder(GTCEu.id("xenoxene_crystal"))
         .dust()
         .color(6052440)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.XenoxeneMixture = new Builder(GTCEu.id("xenoxene_mixture"))
         .fluid()
         .color(6060632)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.EnrichedXenoxene = new Builder(GTCEu.id("enriched_xenoxene"))
         .fluid()
         .color(6052440)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.PurifiedXenoxene = new Builder(GTCEu.id("purified_xenoxene"))
         .fluid()
         .color(6060632)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.DilutedXenoxene = new Builder(GTCEu.id("dilutedxenoxene"))
         .fluid()
         .color(9144196)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.Dibromomethylbenzene = new Builder(GTCEu.id("dibromomethylbenzene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 6, GTMaterials.Bromine, 2})
         .color(439684)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RawStarMatter = new Builder(GTCEu.id("raw_star_matter"))
         .element(GTLElements.RAW_STAR_MATTER)
         .plasma(new FluidBuilder().customStill())
         .buildAndRegister();
      GTLMaterials.XpJuice = new Builder(GTCEu.id("xpjuice")).liquid(new FluidBuilder().customStill()).buildAndRegister();
      GTLMaterials.BiomediumRaw = new Builder(GTCEu.id("biomediumraw")).fluid().color(4351007).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.BiohmediumSterilized = new Builder(GTCEu.id("biohmediumsterilized")).fluid().color(7516453).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.UnknowWater = new Builder(GTCEu.id("unknowwater")).fluid().color(3351210).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.UnknownNutrientAgar = new Builder(GTCEu.id("unknownnutrientagar"))
         .fluid()
         .color(9530880)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("?");
      GTLMaterials.SeaweedBroth = new Builder(GTCEu.id("seaweedbroth")).fluid().color(2921472).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("?");
      GTLMaterials.LiquidCrystalKevlar = new Builder(GTCEu.id("liquidcrystalkevlar")).fluid().color(10461008).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.PolyurethaneResin = new Builder(GTCEu.id("polyurethaneresin")).fluid().color(10132049).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.DiphenylmethanediisocyanateMixture = new Builder(GTCEu.id("diphenylmethanediisocyanatemixture"))
         .fluid()
         .color(9733405)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Phosgene = new Builder(GTCEu.id("phosgene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Oxygen, 1, GTMaterials.Chlorine, 2})
         .color(945169)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DiaminodiphenylmethanMixture = new Builder(GTCEu.id("diaminodiphenylmethanmixture"))
         .fluid()
         .color(9733405)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SiliconOil = new Builder(GTCEu.id("siliconoil")).fluid().color(11382189).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.EthyleneGlycol = new Builder(GTCEu.id("ethyleneglycol"))
         .fluid()
         .color(11382189)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PCBs = new Builder(GTCEu.id("pcbs"))
         .fluid()
         .color(7703137)
         .components(new Object[]{GTMaterials.Carbon, 80, GTMaterials.Hydrogen, 21, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DMAP = new Builder(GTCEu.id("dmap"))
         .dust()
         .color(7703137)
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 10, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PhenylPentanoicAcid = new Builder(GTCEu.id("phenylpentanoic_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(5060659)
         .components(new Object[]{GTMaterials.Carbon, 11, GTMaterials.Hydrogen, 14, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dichloromethane = new Builder(GTCEu.id("dichloromethane"))
         .fluid()
         .color(8595043)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Hydrogen, 2, GTMaterials.Chlorine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DimethylSulfide = new Builder(GTCEu.id("dimethyl_sulfide"))
         .fluid()
         .color(10497542)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 6, GTMaterials.Sulfur, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CosmicMesh = new Builder(GTCEu.id("cosmic_mesh"))
         .plasma()
         .fluid()
         .color(1579128)
         .element(GTLElements.COSMIC_MESH)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HydrobromicAcid = new Builder(GTCEu.id("hydrobromic_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Bromine, 1})
         .color(10639167)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BenzophenanthrenylAcetonitrile = new Builder(GTCEu.id("benzophenanthrenylacetonitrile"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 20, GTMaterials.Hydrogen, 13, GTMaterials.Nitrogen, 1})
         .color(9577159)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BromoSuccinamide = new Builder(GTCEu.id("bromo_succinimide"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 4, GTMaterials.Bromine, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2})
         .color(5532193)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PotassiumBromide = new Builder(GTCEu.id("potassium_bromide"))
         .dust()
         .components(new Object[]{GTMaterials.Potassium, 1, GTMaterials.Bromine, 1})
         .color(10701430)
         .iconSet(MaterialIconSet.GEM_HORIZONTAL)
         .buildAndRegister();
      GTLMaterials.Succinimide = new Builder(GTCEu.id("succinimide"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 5, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2})
         .color(2205637)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FissionedUranium235 = new Builder(GTCEu.id("fissioned_uranium_235"))
         .dust()
         .color(3787191)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.FranciumCaesiumCadmiumBromide = new Builder(GTCEu.id("francium_caesium_cadmium_bromide"))
         .dust()
         .components(new Object[]{GTMaterials.Francium, 1, GTMaterials.Caesium, 1, GTMaterials.Cadmium, 2, GTMaterials.Bromine, 6})
         .color(10702106)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.StrontiumEuropiumAluminate = new Builder(GTCEu.id("strontium_europium_aluminate"))
         .dust()
         .components(new Object[]{GTMaterials.Strontium, 1, GTMaterials.Europium, 1, GTMaterials.Aluminium, 2, GTMaterials.Oxygen, 4})
         .color(11939192)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.UraniumSulfateWasteSolution = new Builder(GTCEu.id("uranium_sulfate_waste_solution"))
         .fluid()
         .color(11645203)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DibismuthHydroborate = new Builder(GTCEu.id("dibismuthhydroborat"))
         .dust()
         .components(new Object[]{GTMaterials.Bismuth, 2, GTMaterials.Hydrogen, 1, GTMaterials.Boron, 1})
         .color(32050)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CircuitCompound = new Builder(GTCEu.id("circuit_compound"))
         .dust()
         .components(new Object[]{GTMaterials.IndiumGalliumPhosphide, 1, GTLMaterials.DibismuthHydroborate, 3, GTLMaterials.BismuthTellurite, 2})
         .color(8462)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CaesiumIodide = new Builder(GTCEu.id("caesium_iodide"))
         .dust()
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Iodine, 1})
         .color(15658722)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ThalliumThuliumDopedCaesiumIodide = new Builder(GTCEu.id("thallium_thulium_doped_caesium_iodide"))
         .dust()
         .color(15251839)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Photoresist = new Builder(GTCEu.id("photoresist")).fluid().color(3100495).iconSet(MaterialIconSet.BRIGHT).buildAndRegister();
      GTLMaterials.EuvPhotoresist = new Builder(GTCEu.id("euv_photoresist")).fluid().color(4915330).iconSet(MaterialIconSet.BRIGHT).buildAndRegister();
      GTLMaterials.GammaRaysPhotoresist = new Builder(GTCEu.id("gamma_rays_photoresist"))
         .fluid()
         .color(5597999)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.AcrylicAcid = new Builder(GTCEu.id("acrylic_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(11801944)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.EthylAcrylate = new Builder(GTCEu.id("ethyl_acrylate"))
         .fluid()
         .color(9731349)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 8, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Trichloroflerane = new Builder(GTCEu.id("trichloroflerane"))
         .fluid()
         .color(4330589)
         .components(new Object[]{GTMaterials.Flerovium, 1, GTMaterials.Chlorine, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BisethylenedithiotetraselenafulvalenePerrhenate = new Builder(GTCEu.id("bisethylenedithiotetraselenafulvalene_perrhenate"))
         .dust()
         .color(7523072)
         .components(
            new Object[]{
               GTMaterials.Rhenium, 1, GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 8, GTMaterials.Sulfur, 4, GTMaterials.Selenium, 4, GTMaterials.Oxygen, 4
            }
         )
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Bisethylenedithiotetraselenafulvalene = new Builder(GTCEu.id("bisethylenedithiotetraselenafulvalene"))
         .dust()
         .color(7523072)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 8, GTMaterials.Sulfur, 4, GTMaterials.Selenium, 4})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.LithiumThiinediselenide = new Builder(GTCEu.id("lithiumthiinediselenide"))
         .dust()
         .color(7523072)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 4, GTMaterials.Sulfur, 2, GTMaterials.Lithium, 2, GTMaterials.Selenium, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CyclopentadienylTitaniumTrichloride = new Builder(GTCEu.id("cyclopentadienyl_titanium_trichloride"))
         .dust()
         .color(11677106)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 10, GTMaterials.Chlorine, 2, GTMaterials.Titanium, 1})
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.ButylLithium = new Builder(GTCEu.id("butyl_lithium"))
         .fluid()
         .color(10447606)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 9, GTMaterials.Lithium, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Bromodihydrothiine = new Builder(GTCEu.id("bromodihydrothiine"))
         .fluid()
         .color(5293133)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 4, GTMaterials.Sulfur, 2, GTMaterials.Bromine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dibromoacrolein = new Builder(GTCEu.id("dibromoacrolein"))
         .fluid()
         .color(4079166)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 2, GTMaterials.Bromine, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumThiosulfate = new Builder(GTCEu.id("sodium_thiosulfate"))
         .dust()
         .color(1333917)
         .components(new Object[]{GTMaterials.Sodium, 2, GTMaterials.Sulfur, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LithiumFluoride = new Builder(GTCEu.id("lithium_fluoride"))
         .dust()
         .color(7171437)
         .components(new Object[]{GTMaterials.Lithium, 1, GTMaterials.Fluorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HighPurityCalciumCarbonate = new Builder(GTCEu.id("high_purity_calcium_carbonate"))
         .dust()
         .color(15658720)
         .components(new Object[]{GTMaterials.Calcium, 1, GTMaterials.Carbon, 1, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Bromobutane = new Builder(GTCEu.id("bromobutane"))
         .fluid()
         .color(16714764)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 9, GTMaterials.Bromine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Propadiene = new Builder(GTCEu.id("propadiene"))
         .fluid()
         .color(3291914)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.AstatideSolution = new Builder(GTCEu.id("astatide_solution"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(5239831)
         .components(new Object[]{GTMaterials.Astatine, 1, GTMaterials.SulfuricAcid, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.MixedAstatideSalts = new Builder(GTCEu.id("mixed_astatide_salts"))
         .dust()
         .color(6809660)
         .components(
            new Object[]{GTMaterials.Holmium, 1, GTMaterials.Thulium, 1, GTMaterials.Copernicium, 1, GTMaterials.Flerovium, 1, GTMaterials.Astatine, 3}
         )
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BoronFranciumCarbide = new Builder(GTCEu.id("boron_francium_carbide"))
         .dust()
         .color(7960953)
         .components(new Object[]{GTMaterials.Francium, 4, GTMaterials.Boron, 4, GTMaterials.Carbon, 7})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Borocarbide = new Builder(GTCEu.id("borocarbide"))
         .dust()
         .color(8289826)
         .components(new Object[]{GTLMaterials.MixedAstatideSalts, 1, GTLMaterials.BoronFranciumCarbide, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FranciumCarbide = new Builder(GTCEu.id("francium_carbide"))
         .dust()
         .color(10592673)
         .components(new Object[]{GTMaterials.Francium, 2, GTMaterials.Carbon, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BoronCarbide = new Builder(GTCEu.id("boron_carbide"))
         .dust()
         .color(1973790)
         .components(new Object[]{GTMaterials.Boron, 4, GTMaterials.Carbon, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LanthanumEmbeddedFullerene = new Builder(GTCEu.id("lanthanum_embedded_fullerene"))
         .dust()
         .color(9552128)
         .components(new Object[]{GTMaterials.Lanthanum, 1, GTLMaterials.Fullerene, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LanthanumFullereneMix = new Builder(GTCEu.id("lanthanum_fullerene_mix"))
         .dust()
         .color(13877228)
         .components(new Object[]{GTMaterials.Lanthanum, 1, GTLMaterials.UnfoldedFullerene, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CaliforniumTrichloride = new Builder(GTCEu.id("californium_trichloride"))
         .dust()
         .color(2646564)
         .components(new Object[]{GTMaterials.Californium, 1, GTMaterials.Chlorine, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.FullereneDopedNanotubes = new Builder(GTCEu.id("fullerene_doped_nanotubes"))
         .fluid()
         .color(5645142)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CaliforniumCyclopentadienide = new Builder(GTCEu.id("californium_cyclopentadienide"))
         .fluid()
         .color(7878474)
         .components(new Object[]{GTMaterials.Carbon, 15, GTMaterials.Hydrogen, 15, GTMaterials.Californium, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Cyclopentadiene = new Builder(GTCEu.id("cyclopentadiene"))
         .fluid()
         .color(2795050)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.LithiumCyclopentadienide = new Builder(GTCEu.id("lithium_cyclopentadienide"))
         .fluid()
         .color(8078935)
         .components(new Object[]{GTMaterials.Lithium, 1, GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 5})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dimethylether = new Builder(GTCEu.id("dimethylether"))
         .fluid()
         .color(12429582)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dimethoxyethane = new Builder(GTCEu.id("dimethoxyethane"))
         .fluid()
         .color(2337174)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 10, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Photopolymer = new Builder(GTCEu.id("photopolymer"))
         .fluid()
         .color(7554139)
         .components(
            new Object[]{
               GTMaterials.Carbon,
               149,
               GTMaterials.Hydrogen,
               97,
               GTMaterials.Nitrogen,
               10,
               GTMaterials.Oxygen,
               2,
               GTMaterials.Titanium,
               1,
               GTMaterials.Boron,
               1,
               GTMaterials.Fluorine,
               20
            }
         )
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilverPerchlorate = new Builder(GTCEu.id("silver_perchlorate"))
         .dust()
         .color(15592941)
         .components(new Object[]{GTMaterials.Silver, 1, GTMaterials.Chlorine, 1, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilverChloride = new Builder(GTCEu.id("silver_chloride"))
         .dust()
         .color(9276813)
         .components(new Object[]{GTMaterials.Silver, 1, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumBromide = new Builder(GTCEu.id("sodium_bromide"))
         .dust()
         .color(12945604)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Bromine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilverOxide = new Builder(GTCEu.id("silver_oxide"))
         .dust()
         .color(2829099)
         .components(new Object[]{GTMaterials.Silver, 2, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PhthalicAnhydride = new Builder(GTCEu.id("phthalic_anhydride"))
         .dust()
         .color(9211020)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 4, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumHypochlorite = new Builder(GTCEu.id("sodium_hypochlorite"))
         .dust()
         .color(6746444)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Chlorine, 1, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Ethylanthraquinone = new Builder(GTCEu.id("ethylanthraquinone"))
         .fluid()
         .color(16776996)
         .components(new Object[]{GTMaterials.Carbon, 16, GTMaterials.Hydrogen, 12, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Ethylanthrahydroquinone = new Builder(GTCEu.id("ethylanthrahydroquinone"))
         .fluid()
         .color(13553152)
         .components(new Object[]{GTLMaterials.Ethylanthraquinone, 1, GTMaterials.Hydrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Anthracene = new Builder(GTCEu.id("anthracene"))
         .fluid()
         .color(9608850)
         .components(new Object[]{GTMaterials.Carbon, 14, GTMaterials.Hydrogen, 10})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Phenylsodium = new Builder(GTCEu.id("phenylsodium"))
         .fluid()
         .color(2500267)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 5, GTMaterials.Sodium, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NDifluorophenylpyrrole = new Builder(GTCEu.id("n_difluorophenylpyrrole"))
         .fluid()
         .color(3112586)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 7, GTMaterials.Fluorine, 2, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Difluoroaniline = new Builder(GTCEu.id("difluoroaniline"))
         .fluid()
         .color(3444542)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 5, GTMaterials.Fluorine, 2, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Succinaldehyde = new Builder(GTCEu.id("succinaldehyde"))
         .fluid()
         .color(6510461)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TetraethylammoniumBromide = new Builder(GTCEu.id("tetraethylammonium_bromide"))
         .fluid()
         .color(12717311)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 20, GTMaterials.Nitrogen, 1, GTMaterials.Bromine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RhodiumRheniumNaquadahCatalyst = new Builder(GTCEu.id("rhodium_rhenium_naquadah_catalyst"))
         .dust()
         .color(9717320)
         .components(new Object[]{GTMaterials.Rhodium, 1, GTMaterials.Rhenium, 1, GTMaterials.Naquadah, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.IodineMonochloride = new Builder(GTCEu.id("iodine_monochloride"))
         .fluid()
         .color(16705)
         .components(new Object[]{GTMaterials.Iodine, 1, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dimethylnaphthalene = new Builder(GTCEu.id("dimethylnaphthalene"))
         .fluid()
         .color(14560673)
         .components(new Object[]{GTMaterials.Carbon, 12, GTMaterials.Hydrogen, 12})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dihydroiodotetracene = new Builder(GTCEu.id("dihydroiodotetracene"))
         .fluid()
         .color(14560673)
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 13, GTMaterials.Iodine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.AcetylatingReagent = new Builder(GTCEu.id("acetylating_reagent"))
         .fluid()
         .color(7490640)
         .components(new Object[]{GTMaterials.Carbon, 9, GTMaterials.Hydrogen, 12, GTMaterials.Silicon, 1, GTMaterials.Magnesium, 2, GTMaterials.Bromine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.MagnesiumChlorideBromide = new Builder(GTCEu.id("magnesium_chloride_bromide"))
         .dust()
         .color(10036045)
         .components(new Object[]{GTMaterials.Magnesium, 1, GTMaterials.Chlorine, 1, GTMaterials.Bromine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.IsopropylAlcohol = new Builder(GTCEu.id("isopropyl_alcohol"))
         .fluid()
         .color(5354271)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 8, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dichlorodicyanobenzoquinone = new Builder(GTCEu.id("dichlorodicyanobenzoquinone"))
         .fluid()
         .color(3154841)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Chlorine, 2, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Dichlorodicyanohydroquinone = new Builder(GTCEu.id("dichlorodicyanohydroquinone"))
         .fluid()
         .color(3154841)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Chlorine, 2, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2, GTMaterials.Hydrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Tetracene = new Builder(GTCEu.id("tetracene"))
         .dust()
         .color(9402136)
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 12})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PolycyclicAromaticMixture = new Builder(GTCEu.id("polycyclic_aromatic_mixture"))
         .dust()
         .color(9402136)
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 12})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf = new Builder(
            GTCEu.id("rhenium_hassium_thallium_isophtaloylbisdiethylthiourea_hexaf")
         )
         .dust()
         .color(9402136)
         .components(
            new Object[]{
               GTMaterials.Rhenium,
               1,
               GTMaterials.Hassium,
               1,
               GTMaterials.Thallium,
               1,
               GTMaterials.Carbon,
               60,
               GTMaterials.Phosphorus,
               1,
               GTMaterials.Nitrogen,
               12,
               GTMaterials.Hydrogen,
               84,
               GTMaterials.Sulfur,
               6,
               GTMaterials.Oxygen,
               12,
               GTMaterials.Fluorine,
               6
            }
         )
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ThalliumChloride = new Builder(GTCEu.id("thallium_chloride"))
         .dust()
         .color(13390672)
         .components(new Object[]{GTMaterials.Thallium, 1, GTMaterials.Chlorine, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HassiumChloride = new Builder(GTCEu.id("hassium_chloride"))
         .dust()
         .color(5777586)
         .components(new Object[]{GTMaterials.Hassium, 1, GTMaterials.Chlorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RheniumChloride = new Builder(GTCEu.id("rhenium_chloride"))
         .dust()
         .color(3745879)
         .components(new Object[]{GTMaterials.Rhenium, 1, GTMaterials.Chlorine, 5})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.IsophthaloylBis = new Builder(GTCEu.id("isophthaloylbis"))
         .fluid()
         .color(7759742)
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 26, GTMaterials.Nitrogen, 4, GTMaterials.Oxygen, 2, GTMaterials.Sulfur, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HexafluorophosphoricAcid = new Builder(GTCEu.id("hexafluorophosphoric_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(14013771)
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Phosphorus, 1, GTMaterials.Fluorine, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Diethylthiourea = new Builder(GTCEu.id("diethylthiourea"))
         .fluid()
         .color(2336391)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 2, GTMaterials.Sulfur, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ThionylChloride = new Builder(GTCEu.id("thionyl_chloride"))
         .fluid()
         .color(16315872)
         .components(new Object[]{GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 1, GTMaterials.Chlorine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PhenylenedioxydiaceticAcid = new Builder(GTCEu.id("phenylenedioxydiacetic_acid"))
         .liquid(new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(8143958)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 10, GTMaterials.Oxygen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumThiocyanate = new Builder(GTCEu.id("sodium_thiocyanate"))
         .fluid()
         .color(8143958)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Sulfur, 1, GTMaterials.Carbon, 1, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PhosphorusTrichloride = new Builder(GTCEu.id("phosphorus_trichloride"))
         .fluid()
         .color(14013771)
         .components(new Object[]{GTMaterials.Phosphorus, 1, GTMaterials.Chlorine, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.AntimonyPentafluoride = new Builder(GTCEu.id("antimony_pentafluoride"))
         .fluid()
         .color(14013885)
         .components(new Object[]{GTMaterials.Antimony, 1, GTMaterials.Fluorine, 5})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.AntimonyTrichloride = new Builder(GTCEu.id("antimony_trichloride"))
         .dust()
         .color(12369084)
         .components(new Object[]{GTMaterials.Antimony, 1, GTMaterials.Chlorine, 3})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ChargedCaesiumCeriumCobaltIndium = new Builder(GTCEu.id("charged_caesium_cerium_cobalt_indium"))
         .dust()
         .color(5088035)
         .components(
            new Object[]{GTMaterials.Caesium, 1, GTMaterials.Cerium, 1, GTMaterials.Cobalt, 2, GTMaterials.Indium, 10, GTLMaterials.CosmicComputingMixture, 1}
         )
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ActiniumSuperhydride = new Builder(GTCEu.id("actinium_superhydride"))
         .plasma()
         .dust()
         .color(5419089)
         .components(new Object[]{GTMaterials.Actinium, 1, GTMaterials.Hydrogen, 12})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CosmicSuperconductor = new Builder(GTCEu.id("cosmic_superconductor"))
         .fluid()
         .color(65535)
         .components(
            new Object[]{
               GTLMaterials.RheniumHassiumThalliumIsophtaloylbisdiethylthioureaHexaf,
               1,
               GTLMaterials.ActiniumSuperhydride,
               1,
               GTLMaterials.ChargedCaesiumCeriumCobaltIndium,
               1
            }
         )
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Ethylamine = new Builder(GTCEu.id("ethylamine"))
         .fluid()
         .color(5924205)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 5, GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TolueneDiisocyanate = new Builder(GTCEu.id("toluene_diisocyanate"))
         .fluid()
         .color(11334847)
         .components(new Object[]{GTMaterials.Carbon, 9, GTMaterials.Hydrogen, 6, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Polyurethane = new Builder(GTCEu.id("polyurethane"))
         .fluid()
         .color(11334847)
         .components(new Object[]{GTMaterials.Carbon, 17, GTMaterials.Hydrogen, 16, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ViscoelasticPolyurethane = new Builder(GTCEu.id("viscoelastic_polyurethane"))
         .fluid()
         .color(15530988)
         .components(new Object[]{GTLMaterials.Polyurethane, 1, GTLMaterials.EthyleneGlycol, 1, GTMaterials.Calcite, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ViscoelasticPolyurethaneFoam = new Builder(GTCEu.id("viscoelastic_polyurethane_foam"))
         .fluid()
         .color(15530988)
         .components(new Object[]{GTLMaterials.ViscoelasticPolyurethane, 1, GTMaterials.Air, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Glucose = new Builder(GTCEu.id("glucose"))
         .dust()
         .color(13290190)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 12, GTMaterials.Oxygen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GlucoseIronSolution = new Builder(GTCEu.id("glucose_iron_solution"))
         .fluid()
         .color(13224393)
         .components(new Object[]{GTLMaterials.Glucose, 1, GTMaterials.Iron3Chloride, 1})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GrapheneOxide = new Builder(GTCEu.id("graphene_oxide"))
         .dust()
         .color(5460819)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GrapheneGelSuspension = new Builder(GTCEu.id("graphene_gel_suspension"))
         .dust()
         .color(5460819)
         .components(new Object[]{GTMaterials.Carbon, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DryGrapheneGel = new Builder(GTCEu.id("dry_graphene_gel"))
         .dust()
         .color(2105465)
         .components(new Object[]{GTMaterials.Carbon, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SupercriticalCarbonDioxide = new Builder(GTCEu.id("supercritical_carbon_dioxide"))
         .fluid()
         .color(10143987)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PotassiumBisulfite = new Builder(GTCEu.id("potassium_bisulfite"))
         .dust()
         .color(8420722)
         .components(new Object[]{GTMaterials.Potassium, 1, GTMaterials.Sulfur, 1, GTMaterials.Hydrogen, 1, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PotassiumHydroxylaminedisulfonate = new Builder(GTCEu.id("potassium_hydroxylaminedisulfonate"))
         .dust()
         .color(5005862)
         .components(new Object[]{GTMaterials.Potassium, 2, GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 1, GTMaterials.Sulfur, 2, GTMaterials.Oxygen, 7})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HydroxylammoniumSulfate = new Builder(GTCEu.id("hydroxylammonium_sulfate"))
         .dust()
         .color(8683637)
         .components(new Object[]{GTMaterials.Nitrogen, 2, GTMaterials.Hydrogen, 8, GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 6})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BariumChloride = new Builder(GTCEu.id("barium_chloride"))
         .dust()
         .color(15298655)
         .components(new Object[]{GTMaterials.Barium, 1, GTMaterials.Chlorine, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NitrousAcid = new Builder(GTCEu.id("nitrous_acid"))
         .fluid()
         .color(10537213)
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ActiniumHydride = new Builder(GTCEu.id("atinium_hydride"))
         .dust()
         .color(12109553)
         .components(new Object[]{GTMaterials.Actinium, 1, GTMaterials.Hydrogen, 3})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater1 = new Builder(GTCEu.id("grade_1_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater2 = new Builder(GTCEu.id("grade_2_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater3 = new Builder(GTCEu.id("grade_3_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater4 = new Builder(GTCEu.id("grade_4_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater5 = new Builder(GTCEu.id("grade_5_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater6 = new Builder(GTCEu.id("grade_6_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater7 = new Builder(GTCEu.id("grade_7_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater8 = new Builder(GTCEu.id("grade_8_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater9 = new Builder(GTCEu.id("grade_9_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater10 = new Builder(GTCEu.id("grade_10_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater11 = new Builder(GTCEu.id("grade_11_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater12 = new Builder(GTCEu.id("grade_12_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater13 = new Builder(GTCEu.id("grade_13_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater14 = new Builder(GTCEu.id("grade_14_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater15 = new Builder(GTCEu.id("grade_15_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GradePurifiedWater16 = new Builder(GTCEu.id("grade_16_purified_water"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1})
         .color(22733)
         .iconSet(MaterialIconSet.FLUID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Antimatter = new Builder(GTCEu.id("antimatter")).fluid().color(10040012).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.PositiveElectron = new Builder(GTCEu.id("positive_electron"))
         .fluid()
         .color(5862988)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("e+");
      GTLMaterials.Antiproton = new Builder(GTCEu.id("antiproton")).fluid().color(4801967).iconSet(MaterialIconSet.DULL).buildAndRegister().setFormula("p-");
      GTLMaterials.Antineutron = new Builder(GTCEu.id("antineutron"))
         .fluid()
         .color(15132410)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("n-bar");
      GTLMaterials.Antihydrogen = new Builder(GTCEu.id("antihydrogen"))
         .fluid()
         .color(6970061)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("Ah");
      GTLMaterials.Kerosene = new Builder(GTCEu.id("kerosene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 12, GTMaterials.Hydrogen, 24})
         .color(13522894)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Rp1 = new Builder(GTCEu.id("rp_1")).fluid().color(16732734).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.RocketFuelRp1 = new Builder(GTCEu.id("rocket_fuel_rp_1")).fluid().color(16724507).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.Hydrazine = new Builder(GTCEu.id("hydrazine"))
         .fluid()
         .color(16777215)
         .components(new Object[]{GTMaterials.Nitrogen, 2, GTMaterials.Hydrogen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DenseHydrazineFuelMixture = new Builder(GTCEu.id("dense_hydrazine_fuel_mixture"))
         .fluid()
         .color(4858427)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Monomethylhydrazine = new Builder(GTCEu.id("monomethylhydrazine"))
         .fluid()
         .color(16777215)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Hydrogen, 6, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.RocketFuelCn3h7o3 = new Builder(GTCEu.id("rocket_fuel_cn3h7o3")).fluid().color(10892972).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.RocketFuelH8n4c2o4 = new Builder(GTCEu.id("rocket_fuel_h8n4c2o4")).fluid().color(4890907).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.StellarEnergyRocketFuel = new Builder(GTCEu.id("stellar_energy_rocket_fuel"))
         .fluid()
         .color(14628397)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ExplosiveHydrazine = new Builder(GTCEu.id("explosivehydrazine")).fluid().color(3869788).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.HmxExplosive = new Builder(GTCEu.id("hmxexplosive")).dust().color(15990747).iconSet(MaterialIconSet.BRIGHT).buildAndRegister();
      GTLMaterials.Desh = new Builder(GTCEu.id("desh"))
         .dust()
         .ore()
         .color(15900759)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES})
         .buildAndRegister();
      GTLMaterials.Ostrum = new Builder(GTCEu.id("ostrum"))
         .dust()
         .ore()
         .color(15045531)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES})
         .buildAndRegister();
      GTLMaterials.Calorite = new Builder(GTCEu.id("calorite"))
         .dust()
         .ore()
         .color(15095639)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES})
         .buildAndRegister();
      GTLMaterials.LaNdOxidesSolution = new Builder(GTCEu.id("la_nd_oxides_solution"))
         .fluid()
         .color(10281947)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("(La2O3)(Pr2O3)(Nd2O3)(Ce2O3)");
      GTLMaterials.SmGdOxidesSolution = new Builder(GTCEu.id("sm_gd_oxides_solution"))
         .fluid()
         .color(16777113)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("(Sc2O3)(Eu2O3)(Gd2O3)(Sm2O3)");
      GTLMaterials.TbHoOxidesSolution = new Builder(GTCEu.id("tb_ho_oxides_solution"))
         .fluid()
         .color(10092441)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("(Y2O3)(Tb2O3)(Dy2O3)(Ho2O3)");
      GTLMaterials.ErLuOxidesSolution = new Builder(GTCEu.id("er_lu_oxides_solution"))
         .fluid()
         .color(16757759)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("(Er2O3)(Tm2O3)(Yb2O3)(Lu2O3)");
      GTLMaterials.LanthanumOxide = new Builder(GTCEu.id("lanthanum_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Lanthanum, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PraseodymiumOxide = new Builder(GTCEu.id("praseodymium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Praseodymium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NeodymiumOxide = new Builder(GTCEu.id("neodymium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Neodymium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CeriumOxide = new Builder(GTCEu.id("cerium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Cerium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EuropiumOxide = new Builder(GTCEu.id("europium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Europium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GadoliniumOxide = new Builder(GTCEu.id("gadolinium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Gadolinium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SamariumOxide = new Builder(GTCEu.id("samarium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Samarium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TerbiumOxide = new Builder(GTCEu.id("terbium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Terbium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DysprosiumOxide = new Builder(GTCEu.id("dysprosium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Dysprosium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HolmiumOxide = new Builder(GTCEu.id("holmium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Holmium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ErbiumOxide = new Builder(GTCEu.id("erbium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Erbium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ThuliumOxide = new Builder(GTCEu.id("thulium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Thulium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YtterbiumOxide = new Builder(GTCEu.id("ytterbium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Ytterbium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LutetiumOxide = new Builder(GTCEu.id("lutetium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Lutetium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ScandiumOxide = new Builder(GTCEu.id("scandium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Scandium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YttriumOxide = new Builder(GTCEu.id("yttrium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Yttrium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PromethiumOxide = new Builder(GTCEu.id("promethium_oxide"))
         .dust()
         .color(13619151)
         .components(new Object[]{GTMaterials.Promethium, 2, GTMaterials.Oxygen, 3})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ZirconChlorinatingResidue = new Builder(GTCEu.id("zircon_chlorinating_residue"))
         .fluid()
         .color(3394099)
         .components(new Object[]{GTMaterials.Silicon, 1, GTMaterials.Chlorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.ZirconiumHafniumChloride = new Builder(GTCEu.id("zirconium_hafnium_chloride"))
         .fluid()
         .color(3394099)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("ZrHfCl\u2084");
      GTLMaterials.ZirconiumHafniumOxychloride = new Builder(GTCEu.id("zirconiu_hafnium_oxychloride"))
         .fluid()
         .color(3394099)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("Cl\u2082HfOZr");
      GTLMaterials.HafniumOxide = new Builder(GTCEu.id("hafnium_oxide"))
         .dust()
         .color(3947580)
         .components(new Object[]{GTMaterials.Hafnium, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.GLASS)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ZirconiumOxide = new Builder(GTCEu.id("zirconium_oxide"))
         .dust()
         .color(3947580)
         .components(new Object[]{GTMaterials.Zirconium, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.HafniumChloride = new Builder(GTCEu.id("hafnium_chloride"))
         .dust()
         .color(6052969)
         .components(new Object[]{GTMaterials.Hafnium, 1, GTMaterials.Chlorine, 4})
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TelluriumOxide = new Builder(GTCEu.id("tellurium_oxide"))
         .dust()
         .color(13684944)
         .components(new Object[]{GTMaterials.Tellurium, 1, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.GLASS)
         .buildAndRegister();
      GTLMaterials.SodiumEthylate = new Builder(GTCEu.id("sodium_ethylate"))
         .dust()
         .color(10210715)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 5, GTMaterials.Oxygen, 1, GTMaterials.Sodium, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumEthylxanthate = new Builder(GTCEu.id("sodium_ethylxanthate"))
         .dust()
         .color(13479168)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 5, GTMaterials.Sodium, 1, GTMaterials.Oxygen, 1, GTMaterials.Sulfur, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PotassiumEthylxanthate = new Builder(GTCEu.id("potassium_ethylxanthate"))
         .dust()
         .color(13486257)
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 5, GTMaterials.Potassium, 1, GTMaterials.Oxygen, 1, GTMaterials.Sulfur, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PotassiumEthylate = new Builder(GTCEu.id("potassium_ethylate"))
         .dust()
         .color(13461021)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 5, GTMaterials.Oxygen, 1, GTMaterials.Potassium, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NMPyrolidone = new Builder(GTCEu.id("nmethylpyrolidone"))
         .fluid()
         .color(13684944)
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 9, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.GammaButyrolactone = new Builder(GTCEu.id("gammabutyrolactone"))
         .fluid()
         .color(13421729)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Butane14Diol = new Builder(GTCEu.id("butane_1_4_diol"))
         .fluid()
         .color(12866380)
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 10, GTMaterials.Oxygen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Methylamine = new Builder(GTCEu.id("methylamine"))
         .fluid()
         .color(4540527)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Hydrogen, 5, GTMaterials.Nitrogen, 1})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PPhenylenediamine = new Builder(GTCEu.id("p_phenylenediamine"))
         .dust()
         .color(14470994)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 8, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.PNitroaniline = new Builder(GTCEu.id("p_nitroaniline"))
         .fluid()
         .color(13406263)
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 8, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TerephthalicAcid = new Builder(GTCEu.id("terephthalicacid"))
         .fluid()
         .color(14079702)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 6, GTMaterials.Hydrogen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.DimethylTerephthalate = new Builder(GTCEu.id("dimethylterephthalate"))
         .fluid()
         .color(13750737)
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 10, GTMaterials.Hydrogen, 4})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TerephthaloylChloride = new Builder(GTCEu.id("terephthaloyl_chloride"))
         .dust()
         .color(58891)
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 4, GTMaterials.Chlorine, 2, GTMaterials.Nitrogen, 2})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Rhugnor = new Builder(GTCEu.id("rhugnor"))
         .fluid()
         .color(11010274)
         .element(GTLElements.RHUGNOR)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.Force = new Builder(GTCEu.id("force"))
         .dust()
         .fluid()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Lanthanum})
         .color(14605824)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.Tartarite = new Builder(GTCEu.id("tartarite"))
         .dust()
         .fluid()
         .ore()
         .addOreByproducts(new Material[]{GTMaterials.Americium})
         .color(13853234)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.HotSodiumPotassium = new Builder(GTCEu.id("hot_sodium_potassium"))
         .fluid()
         .color(6618292)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Potassium, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SupercriticalSodiumPotassium = new Builder(GTCEu.id("supercritical_sodium_potassium"))
         .fluid()
         .color(6618292)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Potassium, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.LAPIS)
         .buildAndRegister();
      GTLMaterials.Copper76 = new Builder(GTCEu.id("copper76"))
         .dust()
         .element(GTLElements.COPPER76)
         .color(15170646)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CadmiumSulfide = new Builder(GTCEu.id("cadmium_sulfide"))
         .dust()
         .components(new Object[]{GTMaterials.Cadmium, 1, GTMaterials.Sulfur, 1})
         .color(13941273)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CadmiumTungstate = new Builder(GTCEu.id("cadmium_tungstate"))
         .dust()
         .components(new Object[]{GTMaterials.Cadmium, 1, GTMaterials.Tungsten, 1, GTMaterials.Oxygen, 4})
         .color(7698288)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BismuthGermanate = new Builder(GTCEu.id("bismuth_germanate"))
         .dust()
         .components(new Object[]{GTMaterials.Bismuth, 12, GTMaterials.Germanium, 1, GTMaterials.Oxygen, 20})
         .color(5154873)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.BismuthNitrateSolution = new Builder(GTCEu.id("bismuth_nitrate_solution"))
         .fluid()
         .components(new Object[]{GTMaterials.Water, 1, GTMaterials.Bismuth, 1, GTMaterials.Nitrogen, 3, GTMaterials.Oxygen, 9})
         .color(10790824)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Paa = new Builder(GTCEu.id("paa"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 22, GTMaterials.Hydrogen, 14, GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 7})
         .color(15388766)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilicaGelBase = new Builder(GTCEu.id("silica_gel_base")).fluid().color(3774074).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.DeglyceratedSoap = new Builder(GTCEu.id("deglycerated_soap")).fluid().color(16756736).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.Turpentine = new Builder(GTCEu.id("turpentine"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 16})
         .color(10145074)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.SteamCrackedTurpentine = new Builder(GTCEu.id("steam_cracked_turpentine"))
         .fluid()
         .color(9136404)
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.LeachedTurpentine = new Builder(GTCEu.id("leached_turpentine"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 16, GTMaterials.Concrete, 1})
         .color(13474589)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.AlmandineFront = new Builder(GTCEu.id("almandine_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Almandine, 1})
         .color(11674146)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.ChalcopyriteFront = new Builder(GTCEu.id("chalcopyrite_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Chalcopyrite, 1})
         .color(13478525)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.GrossularFront = new Builder(GTCEu.id("grossular_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Grossular, 1})
         .color(13789470)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.MonaziteFront = new Builder(GTCEu.id("monazite_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Monazite, 1})
         .color(8620931)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.NickelFront = new Builder(GTCEu.id("nickel_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Nickel, 1})
         .color(12701133)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.PlatinumFront = new Builder(GTCEu.id("platinum_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Platinum, 1})
         .color(13486501)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.PyropeFront = new Builder(GTCEu.id("pyrope_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Pyrope, 1})
         .color(9109504)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.RedstoneFront = new Builder(GTCEu.id("redstone_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Redstone, 1})
         .color(15597568)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.SpessartineFront = new Builder(GTCEu.id("spessartine_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Spessartine, 1})
         .color(15621186)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.SphaleriteFront = new Builder(GTCEu.id("sphalerite_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Sphalerite, 1})
         .color(15657449)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.PentlanditeFront = new Builder(GTCEu.id("pentlandite_front"))
         .fluid()
         .components(new Object[]{GTMaterials.Pentlandite, 1})
         .color(13478525)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.EnrichedNaquadahFront = new Builder(GTCEu.id("enriched_naquadah_front"))
         .fluid()
         .components(new Object[]{GTMaterials.NaquadahEnriched, 1})
         .color(5820431)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.CarbonDisulfide = new Builder(GTCEu.id("carbon_disulfide"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Sulfur, 2})
         .color(1068683)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.FLUID)
         .buildAndRegister();
      GTLMaterials.SpecialCeramics = new Builder(GTCEu.id("special_ceramics")).dust().color(6052105).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.HydroiodicAcid = new Builder(GTCEu.id("hydroiodic_acid"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Iodine, 1})
         .color(230114)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.Acrylonitrile = new Builder(GTCEu.id("acrylonitrile"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 3, GTMaterials.Nitrogen, 1})
         .color(10790113)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.LithiumIodide = new Builder(GTCEu.id("lithium_iodide"))
         .dust()
         .components(new Object[]{GTMaterials.Lithium, 1, GTMaterials.Iodine, 1})
         .color(12648468)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SilicaAluminaGel = new Builder(GTCEu.id("silica_alumina_gel")).dust().color(820383).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.ZeoliteSievingPellets = new Builder(GTCEu.id("zeolite_sieving_pellets"))
         .dust()
         .color(5062303)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.WetZeoliteSievingPellets = new Builder(GTCEu.id("wet_zeolite_sieving_pellets"))
         .dust()
         .color(1906492)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.TertButanol = new Builder(GTCEu.id("tert_butanol"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 10, GTMaterials.Oxygen, 1})
         .color(11318528)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.DitertbutylDicarbonate = new Builder(GTCEu.id("ditertbutyl_dicarbonate"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 10, GTMaterials.Hydrogen, 18, GTMaterials.Oxygen, 5})
         .color(8296117)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Tertbuthylcarbonylazide = new Builder(GTCEu.id("tertbuthylcarbonylazide"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 5, GTMaterials.Hydrogen, 9, GTMaterials.Nitrogen, 3, GTMaterials.Oxygen, 2})
         .color(11318528)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SodiumToluenesulfonate = new Builder(GTCEu.id("sodium_toluenesulfonate"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 7, GTMaterials.Hydrogen, 7, GTMaterials.Sulfur, 3, GTMaterials.Oxygen, 3, GTMaterials.Sodium, 1})
         .color(11908125)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SodiumAzide = new Builder(GTCEu.id("sodium_azide"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Nitrogen, 3})
         .color(1054960)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SodiumAzanide = new Builder(GTCEu.id("sodium_azanide"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 2})
         .color(2326963)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.NitrogenPentoxide = new Builder(GTCEu.id("nitrogen_pentoxide"))
         .fluid()
         .components(new Object[]{GTMaterials.Nitrogen, 2, GTMaterials.Oxygen, 5})
         .color(1452979)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.AminatedFullerene = new Builder(GTCEu.id("aminated_fullerene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 60, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 12})
         .color(3687152)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.Azafullerene = new Builder(GTCEu.id("azafullerene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 60, GTMaterials.Hydrogen, 12, GTMaterials.Nitrogen, 12})
         .color(11773184)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.AbsoluteEthanol = new Builder(GTCEu.id("absolute_ethanol"))
         .fluid()
         .color(16729344)
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 6, GTMaterials.Oxygen, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.PiranhaSolution = new Builder(GTCEu.id("piranha_solution")).fluid().color(11284479).iconSet(GTLMaterialIconSet.LIMPID).buildAndRegister();
      GTLMaterials.PotassiumPyrosulfate = new Builder(GTCEu.id("potassium_pyrosulfate"))
         .dust()
         .components(new Object[]{GTMaterials.Potassium, 2, GTMaterials.Sulfur, 2, GTMaterials.Oxygen, 7})
         .color(16750848)
         .iconSet(MaterialIconSet.METALLIC)
         .buildAndRegister();
      GTLMaterials.PlatinumSlag = new Builder(GTCEu.id("platinum_slag"))
         .dust()
         .color(3420952)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("IrOsRhRu??");
      GTLMaterials.LeachResidue = new Builder(GTCEu.id("leach_residue"))
         .dust()
         .color(6571561)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("IrOsRhRu?");
      GTLMaterials.ZincSulfate = new Builder(GTCEu.id("zinc_sulfate"))
         .dust()
         .components(new Object[]{GTMaterials.Zinc, 1, GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 4})
         .color(5454875)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.RhodiumNitrate = new Builder(GTCEu.id("rhodium_nitrate"))
         .dust()
         .color(9198092)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .components(new Object[]{GTMaterials.Rhodium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3, GTMaterials.Concrete, 1})
         .buildAndRegister();
      GTLMaterials.RoughlyRhodiumMetal = new Builder(GTCEu.id("roughly_rhodium_metal"))
         .dust()
         .color(5852186)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("?Rh?");
      GTLMaterials.ReprecipitatedRhodium = new Builder(GTCEu.id("reprecipitated_rhodium"))
         .dust()
         .color(13895753)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .components(new Object[]{GTMaterials.Rhodium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 4})
         .buildAndRegister();
      GTLMaterials.RhodiumSalt = new Builder(GTCEu.id("rhodium_salt"))
         .dust()
         .color(6365194)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("NaRhCl?");
      GTLMaterials.RhodiumSaltSolution = new Builder(GTCEu.id("rhodium_salt_solution"))
         .fluid()
         .color(6365194)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("Rh(NaCl)2Cl");
      GTLMaterials.RhodiumFilterCake = new Builder(GTCEu.id("rhodium_filter_cake"))
         .dust()
         .color(8860940)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister()
         .setFormula("?Rh?");
      GTLMaterials.RhodiumFilterCakeSolution = new Builder(GTCEu.id("rhodium_filter_cake_solution"))
         .fluid()
         .color(8860940)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister()
         .setFormula("?Rh?");
      GTLMaterials.SodiumRutheniate = new Builder(GTCEu.id("sodium_rutheniate"))
         .dust()
         .color(2632844)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .components(new Object[]{GTMaterials.Sodium, 2, GTMaterials.Oxygen, 4, GTMaterials.Ruthenium, 1})
         .buildAndRegister();
      GTLMaterials.IridiumDioxide = new Builder(GTCEu.id("iridium_dioxide"))
         .dust()
         .color(10665983)
         .iconSet(MaterialIconSet.METALLIC)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .components(new Object[]{GTMaterials.Iridium, 1, GTMaterials.Oxygen, 2})
         .buildAndRegister();
      GTLMaterials.RutheniumTetroxideLQ = new Builder(GTCEu.id("ruthenium_tetroxide_lq"))
         .fluid()
         .color(11053224)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister();
      GTLMaterials.SodiumFormate = new Builder(GTCEu.id("sodium_formate"))
         .fluid()
         .color(15831964)
         .iconSet(MaterialIconSet.ROUGH)
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Carbon, 1, GTMaterials.Oxygen, 2, GTMaterials.Hydrogen, 1})
         .buildAndRegister();
      GTLMaterials.RhodiumSulfateGas = new Builder(GTCEu.id("rhodium_sulfate_gas")).gas().color(12420931).iconSet(MaterialIconSet.ROUGH).buildAndRegister();
      GTLMaterials.AcidicIridium = new Builder(GTCEu.id("acidic_iridium"))
         .gas()
         .color(6508090)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister()
         .setFormula("???");
      GTLMaterials.RutheniumTetroxideHot = new Builder(GTCEu.id("ruthenium_tetroxide_hot"))
         .gas()
         .color(10197915)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister();
      GTLMaterials.SodiumSulfate = new Builder(GTCEu.id("sodium_sulfate"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 2, GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 4})
         .color(16381647)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.MutatedLivingSolder = new Builder(GTCEu.id("mutated_living_solder"))
         .fluid()
         .color(12685260)
         .iconSet(MaterialIconSet.METALLIC)
         .radioactiveHazard(1.0F)
         .buildAndRegister();
      GTLMaterials.SuperMutatedLivingSolder = new Builder(GTCEu.id("super_mutated_living_solder"))
         .fluid()
         .color(11952895)
         .iconSet(MaterialIconSet.METALLIC)
         .radioactiveHazard(2.0F)
         .buildAndRegister();
      GTLMaterials.HexafluorideEnrichedNaquadahSolution = new Builder(GTCEu.id("hexafluoride_enriched_naquadah_solution"))
         .fluid()
         .color(8818047)
         .components(new Object[]{GTMaterials.NaquadahEnriched, 1, GTMaterials.Fluorine, 6})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.XenonHexafluoroEnrichedNaquadate = new Builder(GTCEu.id("xenon_hexafluoro_enriched_naquadate"))
         .fluid()
         .color(2447957)
         .components(new Object[]{GTMaterials.Xenon, 1, GTMaterials.NaquadahEnriched, 1, GTMaterials.Fluorine, 6})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GoldTrifluoride = new Builder(GTCEu.id("gold_trifluoride"))
         .dust()
         .color(15254648)
         .iconSet(MaterialIconSet.BRIGHT)
         .components(new Object[]{GTMaterials.Gold, 1, GTMaterials.Fluorine, 3})
         .buildAndRegister();
      GTLMaterials.XenoauricFluoroantimonicAcid = new Builder(GTCEu.id("xenoauric_fluoroantimonic_acid"))
         .fluid(FluidStorageKeys.LIQUID, new FluidBuilder().attribute(FluidAttributes.ACID))
         .color(14728564)
         .components(new Object[]{GTMaterials.Xenon, 1, GTMaterials.Gold, 1, GTMaterials.Antimony, 1, GTMaterials.Fluorine, 6})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GoldChloride = new Builder(GTCEu.id("gold_chloride"))
         .fluid()
         .color(13421670)
         .components(new Object[]{GTMaterials.Gold, 2, GTMaterials.Chlorine, 6})
         .buildAndRegister();
      GTLMaterials.BromineTrifluoride = new Builder(GTCEu.id("bromine_trifluoride"))
         .fluid()
         .color(11046487)
         .components(new Object[]{GTMaterials.Bromine, 1, GTMaterials.Fluorine, 3})
         .buildAndRegister();
      GTLMaterials.HexafluorideNaquadriaSolution = new Builder(GTCEu.id("hexafluoride_naquadria_solution"))
         .fluid()
         .color(2474515)
         .components(new Object[]{GTMaterials.Naquadria, 1, GTMaterials.Fluorine, 6})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RadonDifluoride = new Builder(GTCEu.id("radon_difluoride"))
         .fluid()
         .color(9142015)
         .components(new Object[]{GTMaterials.Radon, 1, GTMaterials.Fluorine, 2})
         .buildAndRegister();
      GTLMaterials.RadonNaquadriaOctafluoride = new Builder(GTCEu.id("radon_naquadria_octafluoride"))
         .fluid()
         .color(8778616)
         .components(new Object[]{GTMaterials.Radon, 1, GTMaterials.Naquadria, 1, GTMaterials.Fluorine, 8})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CaesiumFluoride = new Builder(GTCEu.id("caesium_fluoride"))
         .fluid()
         .color(16743007)
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Fluorine, 1})
         .buildAndRegister();
      GTLMaterials.XenonTrioxide = new Builder(GTCEu.id("xenon_trioxide"))
         .fluid()
         .color(3514307)
         .components(new Object[]{GTMaterials.Xenon, 1, GTMaterials.Oxygen, 3})
         .buildAndRegister();
      GTLMaterials.CaesiumXenontrioxideFluoride = new Builder(GTCEu.id("caesium_xenontrioxide_fluoride"))
         .fluid()
         .color(5269463)
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Xenon, 1, GTMaterials.Oxygen, 3, GTMaterials.Fluorine, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NaquadriaCaesiumXenonnonfluoride = new Builder(GTCEu.id("naquadria_caesium_xenonnonfluoride"))
         .fluid()
         .color(5554760)
         .components(new Object[]{GTMaterials.Naquadria, 1, GTMaterials.Caesium, 1, GTMaterials.Xenon, 1, GTMaterials.Fluorine, 9})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RadonTrioxide = new Builder(GTCEu.id("radon_trioxide"))
         .fluid()
         .color(10120663)
         .components(new Object[]{GTMaterials.Radon, 1, GTMaterials.Oxygen, 3})
         .buildAndRegister();
      GTLMaterials.NaquadriaCaesiumfluoride = new Builder(GTCEu.id("naquadria_caesiumfluoride"))
         .fluid()
         .color(11201385)
         .components(new Object[]{GTMaterials.Naquadria, 1, GTMaterials.Fluorine, 3, GTMaterials.Caesium, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister()
         .setFormula("*Nq*F2CsF", true);
      GTLMaterials.NitrosoniumOctafluoroxenate = new Builder(GTCEu.id("nitrosonium_octafluoroxenate"))
         .fluid()
         .color(9780639)
         .components(new Object[]{GTMaterials.NitrogenDioxide, 2, GTMaterials.Xenon, 1, GTMaterials.Fluorine, 8})
         .buildAndRegister()
         .setFormula("(NO2)2XeF8", true);
      GTLMaterials.NitrylFluoride = new Builder(GTCEu.id("nitryl_fluoride"))
         .fluid()
         .color(9142015)
         .components(new Object[]{GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 2, GTMaterials.Fluorine, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AcidicNaquadriaCaesiumfluoride = new Builder(GTCEu.id("acidic_naquadria_caesiumfluoride"))
         .fluid()
         .color(7727872)
         .components(new Object[]{GTMaterials.Naquadria, 1, GTMaterials.Fluorine, 3, GTMaterials.Caesium, 1, GTMaterials.Sulfur, 2, GTMaterials.Oxygen, 8})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister()
         .setFormula("*Nq*F2CsF(SO4)2", true);
      GTLMaterials.SupercriticalSteam = new Builder(GTCEu.id("supercritical_steam"))
         .gas(new FluidBuilder().temperature(1000))
         .color(12895428)
         .components(new Object[]{GTMaterials.Water, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.WaterAgarMix = new Builder(GTCEu.id("water_agar_mix")).fluid().color(8978368).buildAndRegister();
      GTLMaterials.TungstenTrioxide = new Builder(GTCEu.id("tungsten_trioxide"))
         .dust()
         .components(new Object[]{GTMaterials.Tungsten, 1, GTMaterials.Oxygen, 3})
         .color(8847221)
         .iconSet(MaterialIconSet.ROUGH)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SpaceTime = new Builder(GTCEu.id("spacetime"))
         .ingot()
         .liquid(new FluidBuilder().temperature(1).customStill())
         .fluidPipeProperties(2147483647, ConfigHolder.INSTANCE.spacetimePip, true, true, true, true)
         .element(GTLElements.SPACETIME)
         .iconSet(new MaterialIconSet("spacetime"))
         .flags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.NO_UNIFICATION})
         .cableProperties(2147483647L, 524288, 0, true)
         .buildAndRegister();
      GTLMaterials.Infinity = new Builder(GTCEu.id("infinity"))
         .ingot()
         .liquid(new FluidBuilder().temperature(1000000).customStill())
         .blastTemp(32000, GasTier.HIGHEST)
         .element(GTLElements.INFINITY)
         .iconSet(new MaterialIconSet("infinity"))
         .flags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME})
         .cableProperties(2147483647L, 8192, 0, true)
         .buildAndRegister();
      GTLMaterials.CompoundTriniite = new Builder(GTCEu.id("trinium_compound"))
         .dust()
         .ore()
         .components(new Object[]{GTMaterials.Trinium, 3, GTMaterials.Actinium, 3, GTMaterials.Selenium, 4, GTMaterials.Astatine, 4})
         .color(6773129)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CrystallineNitricAcid = new Builder(GTCEu.id("crystalline_nitric_acid"))
         .dust()
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .color(13483284)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SodiumChlorate = new Builder(GTCEu.id("sodium_chlorate"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Chlorine, 1, GTMaterials.Oxygen, 3})
         .color(10855845)
         .buildAndRegister();
      GTLMaterials.SodiumPerchlorate = new Builder(GTCEu.id("sodium_perchlorate"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Chlorine, 1, GTMaterials.Oxygen, 4})
         .color(15790320)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ActiniumTriniumHydroxides = new Builder(GTCEu.id("actinium_trinium_hydroxides"))
         .dust()
         .color(11356111)
         .iconSet(MaterialIconSet.ROUGH)
         .buildAndRegister()
         .setFormula("Ke3Ac2(OH)12");
      GTLMaterials.SeleniumOxide = new Builder(GTCEu.id("selenium_oxide"))
         .dust()
         .components(new Object[]{GTMaterials.Selenium, 1, GTMaterials.Oxygen, 2})
         .color(16774940)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TriniumTetrafluoride = new Builder(GTCEu.id("trinium_tetrafluoride"))
         .dust()
         .color(5414487)
         .components(new Object[]{GTMaterials.Trinium, 1, GTMaterials.Fluorine, 4})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Fluorocarborane = new Builder(GTCEu.id("fluorocarborane"))
         .dust()
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Carbon, 1, GTMaterials.Hydrogen, 1, GTMaterials.Boron, 11, GTMaterials.Fluorine, 11})
         .color(60544)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CaesiumNitrate = new Builder(GTCEu.id("caesium_nitrate"))
         .dust()
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .color(8527559)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CesiumCarborane = new Builder(GTCEu.id("cesium_carborane"))
         .dust()
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Carbon, 1, GTMaterials.Boron, 11, GTMaterials.Hydrogen, 12})
         .color(11431642)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SilverIodide = new Builder(GTCEu.id("silver_iodide"))
         .dust()
         .components(new Object[]{GTMaterials.Silver, 1, GTMaterials.Iodine, 1})
         .color(11314538)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SilverNitrate = new Builder(GTCEu.id("silver_nitrate"))
         .dust()
         .components(new Object[]{GTMaterials.Silver, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .color(16776416)
         .iconSet(MaterialIconSet.BRIGHT)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TrifluoroaceticPhosphateEster = new Builder(GTCEu.id("trifluoroacetic_phosphate_ester"))
         .dust()
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 5, GTMaterials.Fluorine, 3, GTMaterials.Oxygen, 2, GTMaterials.Sulfur, 1})
         .color(11116357)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RadiumNitrate = new Builder(GTCEu.id("radium_nitrate"))
         .dust()
         .color(13451482)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("Ra(NO3)2");
      GTLMaterials.ActiniumNitrate = new Builder(GTCEu.id("actinium_nitrate"))
         .dust()
         .color(14344430)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("Ac(NO3)3");
      GTLMaterials.PotassiumFluoride = new Builder(GTCEu.id("potassium_fluoride"))
         .dust()
         .components(new Object[]{GTMaterials.Potassium, 1, GTMaterials.Fluorine, 1})
         .color(12171705)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.SodiumHydride = new Builder(GTCEu.id("sodium_hydride"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Hydrogen, 1})
         .color(7697525)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.CesiumCarboranePrecursor = new Builder(GTCEu.id("cesium_carborane_precursor"))
         .dust()
         .color(12213353)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister()
         .setFormula("CsB10H12CN(CH3)3Cl");
      GTLMaterials.LithiumAluminiumHydride = new Builder(GTCEu.id("lithium_aluminium_hydride"))
         .dust()
         .components(new Object[]{GTMaterials.Lithium, 1, GTMaterials.Aluminium, 1, GTMaterials.Hydrogen, 1})
         .color(11261919)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LithiumAluminiumFluoride = new Builder(GTCEu.id("lithium_aluminium_fluoride"))
         .dust()
         .components(new Object[]{GTMaterials.Aluminium, 1, GTMaterials.Fluorine, 4, GTMaterials.Lithium, 1})
         .color(11345752)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AluminiumTrifluoride = new Builder(GTCEu.id("aluminium_trifluoride"))
         .dust()
         .components(new Object[]{GTMaterials.Aluminium, 1, GTMaterials.Fluorine, 3})
         .color(12083226)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SodiumAluminiumHydride = new Builder(GTCEu.id("sodium_aluminium_hydride"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Aluminium, 1, GTMaterials.Hydrogen, 4})
         .color(8896447)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AluminiumHydride = new Builder(GTCEu.id("aluminium_hydride"))
         .dust()
         .components(new Object[]{GTMaterials.Aluminium, 1, GTMaterials.Hydrogen, 3})
         .color(3234926)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Alumina = new Builder(GTCEu.id("alumina"))
         .dust()
         .components(new Object[]{GTMaterials.Aluminium, 2, GTMaterials.Oxygen, 3})
         .color(1918809)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CaesiumHydroxide = new Builder(GTCEu.id("caesium_hydroxide"))
         .dust()
         .components(new Object[]{GTMaterials.Caesium, 1, GTMaterials.Oxygen, 1, GTMaterials.Hydrogen, 1})
         .color(14145495)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.Decaborane = new Builder(GTCEu.id("decaborane"))
         .dust()
         .components(new Object[]{GTMaterials.Boron, 10, GTMaterials.Hydrogen, 14})
         .color(9811855)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SodiumTetrafluoroborate = new Builder(GTCEu.id("sodium_tetrafluoroborate"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Boron, 1, GTMaterials.Fluorine, 4})
         .color(10904591)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SodiumBorohydride = new Builder(GTCEu.id("sodium_borohydride"))
         .dust()
         .components(new Object[]{GTMaterials.Sodium, 1, GTMaterials.Boron, 1, GTMaterials.Hydrogen, 4})
         .color(10137778)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PhosphorusPentasulfide = new Builder(GTCEu.id("phosphorus_pentasulfide"))
         .dust()
         .components(new Object[]{GTMaterials.Phosphorus, 4, GTMaterials.Sulfur, 10})
         .color(15180067)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.StoneDustResidue = new Builder(GTCEu.id("stone_dust_residue")).dust().color(5789784).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.AmmoniumBifluoride = new Builder(GTCEu.id("ammonium_bifluoride"))
         .dust()
         .components(new Object[]{GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 4, GTMaterials.Hydrogen, 1, GTMaterials.Fluorine, 2})
         .color(2541243)
         .iconSet(MaterialIconSet.DULL)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.UncommonResidues = new Builder(GTCEu.id("uncommon_residues")).dust().color(4146877).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.PartiallyOxidizedResidues = new Builder(GTCEu.id("partially_oxidized_residues"))
         .dust()
         .color(9377045)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.InertResidues = new Builder(GTCEu.id("inert_residues")).dust().color(5195875).iconSet(MaterialIconSet.BRIGHT).buildAndRegister();
      GTLMaterials.OxidizedResidues = new Builder(GTCEu.id("oxidized_residues")).dust().color(3345738).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.HeavyOxidizedResidues = new Builder(GTCEu.id("heavy_oxidized_residues"))
         .dust()
         .color(3214664)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.CleanInertResidues = new Builder(GTCEu.id("clean_inert_residues")).dust().color(1605453).iconSet(MaterialIconSet.BRIGHT).buildAndRegister();
      GTLMaterials.MetallicResidues = new Builder(GTCEu.id("metallic_residues")).dust().color(2120275).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.HeavyMetallicResidues = new Builder(GTCEu.id("heavy_metallic_residues"))
         .dust()
         .color(1837446)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.DiamagneticResidues = new Builder(GTCEu.id("diamagnetic_residues")).dust().color(3179614).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.ParamagneticResidues = new Builder(GTCEu.id("paramagnetic_residues")).dust().color(2455691).iconSet(MaterialIconSet.SAND).buildAndRegister();
      GTLMaterials.FerromagneticResidues = new Builder(GTCEu.id("ferromagnetic_residues"))
         .dust()
         .color(2055494)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.HeavyDiamagneticResidues = new Builder(GTCEu.id("heavy_diamagnetic_residues"))
         .dust()
         .color(2240874)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.HeavyParamagneticResidues = new Builder(GTCEu.id("heavy_paramagnetic_residues"))
         .dust()
         .color(1740335)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.HeavyFerromagneticResidues = new Builder(GTCEu.id("heavy_ferromagnetic_residues"))
         .dust()
         .color(2520126)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ExoticHeavyResidues = new Builder(GTCEu.id("exotic_heavy_residues"))
         .dust()
         .color(4097174)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.FumingNitricAcid = new Builder(GTCEu.id("fuming_nitric_acid"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .color(11823104)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Perfluorobenzene = new Builder(GTCEu.id("perfluorobenzene"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Fluorine, 6})
         .color(1406251)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Trimethylsilane = new Builder(GTCEu.id("trimethylsilane"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 3, GTMaterials.Hydrogen, 10, GTMaterials.Silicon, 1})
         .color(8204227)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Trimethylchlorosilane = new Builder(GTCEu.id("trimethylchlorosilane"))
         .fluid()
         .color(5837721)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(CH3)3SiCl");
      GTLMaterials.NitratedTriniiteCompoundSolution = new Builder(GTCEu.id("nitrated_triniite_compound_solution"))
         .fluid()
         .color(6198937)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ResidualTriniiteSolution = new Builder(GTCEu.id("residual_triniite_solution"))
         .fluid()
         .color(428889)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ActiniumRadiumHydroxideSolution = new Builder(GTCEu.id("actinium_radium_hydroxide_solution"))
         .fluid()
         .color(12837084)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ActiniumRadiumNitrateSolution = new Builder(GTCEu.id("actinium_radium_nitrate_solution"))
         .fluid()
         .color(9027763)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.HeavilyFluorinatedTriniumSolution = new Builder(GTCEu.id("heavily_fluorinated_trinium_solution"))
         .fluid()
         .color(1481267)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.EthyleneSulfide = new Builder(GTCEu.id("ethylene_sulfide"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 6, GTMaterials.Hydrogen, 6, GTMaterials.Sulfur, 1, GTMaterials.Oxygen, 1})
         .color(8815940)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EthylTrifluoroacetate = new Builder(GTCEu.id("ethyl_trifluoroacetate"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 4, GTMaterials.Hydrogen, 5, GTMaterials.Fluorine, 3, GTMaterials.Oxygen, 2})
         .color(9676376)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AcetylChloride = new Builder(GTCEu.id("acetyl_chloride"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 3, GTMaterials.Oxygen, 1, GTMaterials.Chlorine, 1})
         .color(13742359)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.KryptonDifluoride = new Builder(GTCEu.id("krypton_difluoride"))
         .fluid()
         .components(new Object[]{GTMaterials.Krypton, 1, GTMaterials.Fluorine, 2})
         .color(4190507)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MoltenCalciumSalts = new Builder(GTCEu.id("molten_calcium_salts")).fluid().color(8341387).iconSet(MaterialIconSet.DULL).buildAndRegister();
      GTLMaterials.Trimethylamine = new Builder(GTCEu.id("trimethylamine"))
         .fluid()
         .color(13543037)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(CH3)3N");
      GTLMaterials.BoraneDimethylSulfide = new Builder(GTCEu.id("borane_dimethyl_sulfide"))
         .fluid()
         .color(13543037)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(BH3)(CH3)2S");
      GTLMaterials.Tetrahydrofuran = new Builder(GTCEu.id("tetrahydrofuran"))
         .fluid()
         .color(8840398)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(CH2)4O");
      GTLMaterials.Diborane = new Builder(GTCEu.id("diborane"))
         .fluid()
         .color(12512205)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(BH3)2");
      GTLMaterials.DiethylEther = new Builder(GTCEu.id("diethyl_ether"))
         .fluid()
         .color(10137778)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(C2H5)2O");
      GTLMaterials.BoronTrifluorideAcetate = new Builder(GTCEu.id("boron_trifluoride_acetate"))
         .fluid()
         .color(10031202)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(BF3)(C2H5)2O");
      GTLMaterials.SodiumHexafluoroaluminate = new Builder(GTCEu.id("sodium_hexafluoroaluminate"))
         .fluid()
         .components(new Object[]{GTMaterials.Sodium, 3, GTMaterials.Aluminium, 1, GTMaterials.Fluorine, 6})
         .color(10778418)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.DirtyHexafluorosilicicAcid = new Builder(GTCEu.id("dirty_hexafluorosilicic_acid"))
         .fluid()
         .color(14301309)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("H2SiF6?");
      GTLMaterials.DiluteHexafluorosilicicAcid = new Builder(GTCEu.id("dilute_hexafluorosilicic_acid"))
         .fluid()
         .color(4841320)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(H2O)2(H2SiF6)");
      GTLMaterials.FluorosilicicAcid = new Builder(GTCEu.id("fluorosilicic_acid"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 2, GTMaterials.Silicon, 1, GTMaterials.Fluorine, 6})
         .color(4833121)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.AmmoniumFluoride = new Builder(GTCEu.id("ammonium_fluoride"))
         .fluid()
         .components(new Object[]{GTMaterials.Nitrogen, 1, GTMaterials.Hydrogen, 4, GTMaterials.Fluorine, 1})
         .color(12060966)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.AmmoniumBifluorideSolution = new Builder(GTCEu.id("ammonium_bifluoride_solution"))
         .fluid()
         .color(13354423)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(H2O)NH4FHF");
      GTLMaterials.SodiumHydroxideSolution = new Builder(GTCEu.id("sodium_hydroxide_solution"))
         .fluid()
         .color(1937)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(H2O)NaOH");
      GTLMaterials.RedMud = new Builder(GTCEu.id("red_mud")).fluid().color(9906435).iconSet(GTLMaterialIconSet.LIMPID).buildAndRegister().setFormula("HCl?");
      GTLMaterials.NeutralisedRedMud = new Builder(GTCEu.id("neutralised_red_mud"))
         .fluid()
         .color(9906435)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("Fe??");
      GTLMaterials.RedSlurry = new Builder(GTCEu.id("red_slurry"))
         .fluid()
         .color(9971970)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("TiO2?");
      GTLMaterials.FerricReeChloride = new Builder(GTCEu.id("ferric_ree_chloride"))
         .fluid()
         .color(6842381)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("Fe?");
      GTLMaterials.TitanylSulfate = new Builder(GTCEu.id("titanyl_sulfate"))
         .fluid()
         .color(16261782)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("TiO(SO4)");
      GTLMaterials.DioxygenDifluoride = new Builder(GTCEu.id("dioxygen_difluoride"))
         .fluid()
         .components(new Object[]{GTMaterials.Fluorine, 1, GTMaterials.Oxygen, 1, GTMaterials.Oxygen, 1, GTMaterials.Fluorine, 1})
         .color(1621945)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.OxidizedResidualSolution = new Builder(GTCEu.id("oxidized_residual_solution"))
         .fluid()
         .color(2937803)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.HeliumIIIHydride = new Builder(GTCEu.id("helium_iii_hydride"))
         .fluid()
         .color(10460930)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("He_3H");
      GTLMaterials.UltraacidicResidueSolution = new Builder(GTCEu.id("ultraacidic_residue_solution"))
         .fluid()
         .color(8686746)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.XenicAcid = new Builder(GTCEu.id("xenic_acid"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 2, GTMaterials.Xenon, 1, GTMaterials.Oxygen, 4})
         .color(4471414)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DiluteHydrofluoricAcid = new Builder(GTCEu.id("dilute_hydrofluoric_acid"))
         .fluid()
         .color(301089)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister()
         .setFormula("(H2O)(HF)");
      GTLMaterials.TritiumHydride = new Builder(GTCEu.id("tritium_hydride"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 1, GTMaterials.Tritium, 1})
         .color(12190210)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DustyLiquidHeliumIII = new Builder(GTCEu.id("dusty_liquid_helium_iii"))
         .fluid()
         .components(new Object[]{GTMaterials.Concrete, 1, GTMaterials.Helium3, 1})
         .color(7815264)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Ozone = new Builder(GTCEu.id("ozone"))
         .fluid()
         .components(new Object[]{GTMaterials.Oxygen, 3})
         .color(95940)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HydrogenPeroxide = new Builder(GTCEu.id("hydrogen_peroxide"))
         .fluid()
         .components(new Object[]{GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 2})
         .color(13172735)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.RareEarthChlorides = new Builder(GTCEu.id("rare_earth_chlorides"))
         .fluid()
         .components(new Object[]{GTMaterials.Concrete, 1, GTMaterials.Chlorine, 1})
         .color(12433259)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Fluorite = new Builder(GTCEu.id("fluorite"))
         .fluid()
         .dust()
         .components(new Object[]{GTMaterials.Calcium, 1, GTMaterials.Fluorine, 2})
         .color(1618944)
         .iconSet(MaterialIconSet.DULL)
         .buildAndRegister();
      GTLMaterials.SamariumRefinedPowder = new Builder(GTCEu.id("samarium_refined_powder"))
         .dust()
         .color(9071997)
         .iconSet(MaterialIconSet.BRIGHT)
         .buildAndRegister()
         .setFormula("??Sm??");
      GTLMaterials.SamariumRrareEearthTurbidLiquid = new Builder(GTCEu.id("samarium_rrare_eearth_turbid_liquid"))
         .fluid()
         .color(13617283)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.MonaziteRareEarthTurbidLiquid = new Builder(GTCEu.id("monazite_rare_earth_turbid_liquid"))
         .fluid()
         .color(8479308)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ThoritePowder = new Builder(GTCEu.id("thorite_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Thorium, 1, GTMaterials.Silicon, 1, GTMaterials.Oxygen, 4})
         .color(8224125)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.DilutedMonaziteSlurry = new Builder(GTCEu.id("diluted_monazite_slurry"))
         .fluid()
         .color(12434877)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.RedZirconPowder = new Builder(GTCEu.id("red_zircon_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Zirconium, 1, GTMaterials.Silicon, 1, GTMaterials.Oxygen, 4})
         .color(16729344)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MonaziteSulfatePowder = new Builder(GTCEu.id("monazite_sulfate_powder"))
         .dust()
         .color(13421772)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.DilutedMonaziteSulfateSolution = new Builder(GTCEu.id("diluted_monazite_sulfate_solution"))
         .fluid()
         .color(11393254)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.AcidicMonazitePowder = new Builder(GTCEu.id("acidic_monazite_powder"))
         .dust()
         .color(10145074)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ThoriumPhosphateFilterCakePowder = new Builder(GTCEu.id("thorium_phosphate_filter_cake_powder"))
         .dust()
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ThoriumPhosphateRefinedPowder = new Builder(GTCEu.id("thorium_phosphate_refined_powder"))
         .dust()
         .color(5526612)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("??ThP??", true);
      GTLMaterials.MonaziteRareEarthFilterResiduePowder = new Builder(GTCEu.id("monazite_rare_earth_filter_residue_powder"))
         .dust()
         .color(8355711)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.NeutralizedMonaziteRareEarthFilterResiduePowder = new Builder(GTCEu.id("neutralized_monazite_rare_earth_filter_residue_powder"))
         .dust()
         .color(9868950)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.UraniumFilterResiduePowder = new Builder(GTCEu.id("uranium_filter_residue_powder"))
         .dust()
         .color(5526612)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.NeutralizedUraniumFilterResiduePowder = new Builder(GTCEu.id("neutralized_uranium_filter_residue_powder"))
         .dust()
         .color(8355711)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ConcentratedMonaziteRareEarthHydroxidePowder = new Builder(GTCEu.id("concentrated_monazite_rare_earth_hydroxide_powder"))
         .dust()
         .color(12632256)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.DriedConcentratedNitricMonaziteRareEarthPowder = new Builder(GTCEu.id("dried_concentrated_nitric_monazite_rare_earth_powder"))
         .dust()
         .color(9868950)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ConcentratedNitrideMonaziteRareEarthSolution = new Builder(GTCEu.id("concentrated_nitride_monazite_rare_earth_solution"))
         .fluid()
         .color(11393254)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.CeriumRichMixturePowder = new Builder(GTCEu.id("cerium_rich_mixture_powder"))
         .dust()
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister()
         .setFormula("??Ce??");
      GTLMaterials.CeriumChloridePowder = new Builder(GTCEu.id("cerium_chloride_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Cerium, 1, GTMaterials.Chlorine, 3})
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.OxalicAcid = new Builder(GTCEu.id("oxalic_acid"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 4})
         .color(10551200)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.VanadiumPentoxidePowder = new Builder(GTCEu.id("vanadium_pentoxide_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Vanadium, 2, GTMaterials.Oxygen, 5})
         .color(9109504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.CeriumOxalatePowder = new Builder(GTCEu.id("cerium_oxalate_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Cerium, 1, GTMaterials.Carbon, 2, GTMaterials.Hydrogen, 2, GTMaterials.Oxygen, 4})
         .color(9868950)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ConcentratedCeriumChlorideSolution = new Builder(GTCEu.id("concentrated_cerium_chloride_solution"))
         .fluid()
         .components(new Object[]{GTMaterials.Cerium, 1, GTMaterials.Chlorine, 3})
         .color(65535)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.NitricLeachateFromMonazite = new Builder(GTCEu.id("nitric_leachate_from_monazite"))
         .fluid()
         .color(11393254)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ConcentratedNitricLeachateFromMonazite = new Builder(GTCEu.id("concentrated_nitric_leachate_from_monazite"))
         .fluid()
         .color(65535)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.CoolingConcentratedNitricMonaziteRareEarthPowder = new Builder(GTCEu.id("cooling_concentrated_nitric_monazite_rare_earth_powder"))
         .dust()
         .color(12632256)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.MonaziteRareEarthPrecipitatePowder = new Builder(GTCEu.id("monazite_rare_earth_precipitate_powder"))
         .dust()
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.HeterogeneousHalideMonaziteRareEarthMixturePowder = new Builder(GTCEu.id("heterogeneous_halide_monazite_rare_earth_mixture_powder"))
         .dust()
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SaturatedMonaziteRareEarthPowder = new Builder(GTCEu.id("saturated_monazite_rare_earth_powder"))
         .dust()
         .color(16777215)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SamariumPrecipitatePowder = new Builder(GTCEu.id("samarium_precipitate_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Samarium, 2, GTMaterials.Gadolinium, 1})
         .color(9868950)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.ConcentratedRareEarthChlorideSolution = new Builder(GTCEu.id("concentrated_rare_earth_chloride_solution"))
         .fluid()
         .color(49151)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.EnrichedRareEarthChlorideSolution = new Builder(GTCEu.id("enriched_rare_earth_chloride_solution"))
         .fluid()
         .color(8900346)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.DilutedRareEarthChlorideSolution = new Builder(GTCEu.id("diluted_rare_earth_chloride_solution"))
         .fluid()
         .color(11393254)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SamariumRareEarthSlurry = new Builder(GTCEu.id("samarium_rare_earth_slurry"))
         .fluid()
         .color(8421504)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.NeodymiumRareEarthConcentratePowder = new Builder(GTCEu.id("neodymium_rare_earth_concentrate_powder"))
         .dust()
         .color(9868950)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SamariumRareEarthDilutedSolution = new Builder(GTCEu.id("samarium_rare_earth_diluted_solution"))
         .fluid()
         .components(new Object[]{GTMaterials.Samarium, 1, GTMaterials.Chlorine, 1, GTMaterials.Water, 2})
         .color(11119017)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SamariumOxalateWithImpurities = new Builder(GTCEu.id("samarium_oxalate_with_impurities"))
         .dust()
         .components(new Object[]{GTMaterials.Samarium, 1, GTMaterials.Carbon, 2, GTMaterials.Oxygen, 6})
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SamariumChlorideWithImpurities = new Builder(GTCEu.id("samarium_chloride_with_impurities"))
         .dust()
         .fluid()
         .components(new Object[]{GTMaterials.Samarium, 1, GTMaterials.Chlorine, 3})
         .color(11579568)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SamariumChlorideSodiumChlorideMixturePowder = new Builder(GTCEu.id("samarium_chloride_sodium_chloride_mixture_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Samarium, 1, GTMaterials.Chlorine, 3, GTMaterials.Sodium, 1, GTMaterials.Chlorine, 1})
         .color(12632256)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.PhosphorusFreeSamariumConcentratePowder = new Builder(GTCEu.id("phosphorus_free_samarium_concentrate_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Samarium, 1})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .color(13882323)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.SamariumChlorideConcentrateSolution = new Builder(GTCEu.id("samarium_chloride_concentrate_solution"))
         .fluid()
         .components(new Object[]{GTMaterials.Samarium, 1, GTMaterials.Chlorine, 3, GTMaterials.Water, 5})
         .color(11584734)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.LanthanumChloride = new Builder(GTCEu.id("lanthanum_chloride"))
         .dust()
         .components(new Object[]{GTMaterials.Lanthanum, 1, GTMaterials.Chlorine, 3})
         .color(16777215)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.LanthanumChlorideWithImpurities = new Builder(GTCEu.id("lanthanum_chloride_with_impurities"))
         .dust()
         .color(14737632)
         .iconSet(MaterialIconSet.SAND)
         .buildAndRegister();
      GTLMaterials.FluoroCarbonLanthanideCeriumSolution = new Builder(GTCEu.id("fluoro_carbon_lanthanide_cerium_solution"))
         .fluid()
         .color(3705042)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SteamCrackedFluoroCarbonLanthanideSlurry = new Builder(GTCEu.id("steam_cracked_fluoro_carbon_lanthanide_slurry"))
         .fluid()
         .color(8355839)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ModulatedFluoroCarbonLanthanideSlurry = new Builder(GTCEu.id("modulated_fluoro_carbon_lanthanide_slurry"))
         .fluid()
         .color(5921535)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SodiumFluorosilicate = new Builder(GTCEu.id("sodium_fluorosilicate"))
         .dust()
         .fluid()
         .color(8421504)
         .iconSet(MaterialIconSet.SAND)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.DilutedFluoroCarbonLanthanideSlurry = new Builder(GTCEu.id("diluted_fluoro_carbon_lanthanide_slurry"))
         .fluid()
         .color(9869055)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.FilteredFluoroCarbonLanthanideSlurry = new Builder(GTCEu.id("filtered_fluoro_carbon_lanthanide_slurry"))
         .fluid()
         .color(10526975)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.FluoroCarbonLanthanideCeriumOxidePowder = new Builder(GTCEu.id("fluoro_carbon_lanthanide_cerium_oxide_powder"))
         .dust()
         .color(8355711)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.AcidLeachedFluoroCarbonLanthanideCeriumOxidePowder = new Builder(GTCEu.id("acid_leached_fluoro_carbon_lanthanide_cerium_oxide_powder"))
         .dust()
         .color(5592405)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CalcinedRareEarthOxidePowder = new Builder(GTCEu.id("calcined_rare_earth_oxide_powder"))
         .dust()
         .color(9868950)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.WetRareEarthOxidePowder = new Builder(GTCEu.id("wet_rare_earth_oxide_powder"))
         .dust()
         .color(6724044)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CeriumOxideRareEarthOxidePowder = new Builder(GTCEu.id("cerium_oxide_rare_earth_oxide_powder"))
         .dust()
         .components(new Object[]{GTMaterials.Cerium, 1, GTMaterials.Oxygen, 2})
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .color(13816530)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthOxidePowder = new Builder(GTCEu.id("fluoro_carbon_lanthanide_cerium_rare_earth_oxide_powder"))
         .dust()
         .color(8355711)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.NitridedFluoroCarbonLanthanideCeriumRareEarthOxideSolution = new Builder(
            GTCEu.id("nitrided_fluoro_carbon_lanthanide_cerium_rare_earth_oxide_solution")
         )
         .fluid()
         .color(10066431)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.FluoroCarbonLanthanideCeriumRareEarthSuspension = new Builder(GTCEu.id("fluoro_carbon_lanthanide_cerium_rare_earth_suspension"))
         .fluid()
         .color(12500670)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.SamariumRareEarthConcentratePowder = new Builder(GTCEu.id("samarium_rare_earth_concentrate_powder"))
         .dust()
         .color(8421504)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.FluorinatedSamariumConcentratePowder = new Builder(GTCEu.id("fluorinated_samarium_concentrate_powder"))
         .dust()
         .color(9868950)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.SamariumTerbiumMixturePowder = new Builder(GTCEu.id("samarium_terbium_mixture_powder"))
         .dust()
         .color(9868950)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.NitridedSamariumTerbiumMixturePowder = new Builder(GTCEu.id("nitrided_samarium_terbium_mixture_powder"))
         .dust()
         .color(8355711)
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.TerbiumNitratePowder = new Builder(GTCEu.id("terbium_nitrate_powder"))
         .dust()
         .color(33023)
         .components(new Object[]{GTMaterials.Terbium, 1, GTMaterials.Nitrogen, 1, GTMaterials.Oxygen, 3})
         .iconSet(GTLMaterialIconSet.BRIGHT)
         .buildAndRegister();
      GTLMaterials.CarbonTetrachloride = new Builder(GTCEu.id("carbon_tetrachloride"))
         .fluid()
         .color(954368)
         .components(new Object[]{GTMaterials.Carbon, 1, GTMaterials.Chlorine, 4})
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.ActiniumOxalate = new Builder(GTCEu.id("actinium_oxalate"))
         .dust()
         .color(13619151)
         .iconSet(MaterialIconSet.GLASS)
         .buildAndRegister()
         .setFormula("Ac(CO\u2082)\u2084");
      GTLMaterials.EthylHexanol = new Builder(GTCEu.id("ethyl_hexanol"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 8, GTMaterials.Hydrogen, 18, GTMaterials.Oxygen, 1})
         .color(16777164)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .buildAndRegister();
      GTLMaterials.P507 = new Builder(GTCEu.id("p507"))
         .fluid()
         .components(new Object[]{GTMaterials.Carbon, 18, GTMaterials.Hydrogen, 36, GTMaterials.Phosphorus, 1, GTMaterials.Oxygen, 3})
         .color(16776960)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LanthanumExtractionNanoResin = new Builder(GTCEu.id("lanthanum_extraction_nano_resin"))
         .fluid()
         .color(8421504)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CeriumExtractionNanoResin = new Builder(GTCEu.id("cerium_extraction_nano_resin"))
         .fluid()
         .color(9868950)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PraseodymiumExtractionNanoResin = new Builder(GTCEu.id("praseodymium_extraction_nano_resin"))
         .fluid()
         .color(10526880)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NeodymiumExtractionNanoResin = new Builder(GTCEu.id("neodymium_extraction_nano_resin"))
         .fluid()
         .color(11579568)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PromethiumExtractionNanoResin = new Builder(GTCEu.id("promethium_extraction_nano_resin"))
         .fluid()
         .color(12632256)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SamariumExtractionNanoResin = new Builder(GTCEu.id("samarium_extraction_nano_resin"))
         .fluid()
         .color(13684944)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EuropiumExtractionNanoResin = new Builder(GTCEu.id("europium_extraction_nano_resin"))
         .fluid()
         .color(14737632)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GadoliniumExtractionNanoResin = new Builder(GTCEu.id("gadolinium_extraction_nano_resin"))
         .fluid()
         .color(15790320)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TerbiumExtractionNanoResin = new Builder(GTCEu.id("terbium_extraction_nano_resin"))
         .fluid()
         .color(33023)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DysprosiumExtractionNanoResin = new Builder(GTCEu.id("dysprosium_extraction_nano_resin"))
         .fluid()
         .color(65535)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HolmiumExtractionNanoResin = new Builder(GTCEu.id("holmium_extraction_nano_resin"))
         .fluid()
         .color(65280)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ErbiumExtractionNanoResin = new Builder(GTCEu.id("erbium_extraction_nano_resin"))
         .fluid()
         .color(16711935)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ThuliumExtractionNanoResin = new Builder(GTCEu.id("thulium_extraction_nano_resin"))
         .fluid()
         .color(16711680)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YtterbiumExtractionNanoResin = new Builder(GTCEu.id("ytterbium_extraction_nano_resin"))
         .fluid()
         .color(16776960)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LutetiumExtractionNanoResin = new Builder(GTCEu.id("lutetium_extraction_nano_resin"))
         .fluid()
         .color(8388736)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ScandiumExtractionNanoResin = new Builder(GTCEu.id("scandium_extraction_nano_resin"))
         .fluid()
         .color(8421376)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YttriumExtractionNanoResin = new Builder(GTCEu.id("yttrium_extraction_nano_resin"))
         .fluid()
         .color(32768)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LanthanumExtractedNanoResin = new Builder(GTCEu.id("lanthanum_extracted_nano_resin"))
         .fluid()
         .color(8421504)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.CeriumExtractedNanoResin = new Builder(GTCEu.id("cerium_extracted_nano_resin"))
         .fluid()
         .color(9868950)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PraseodymiumExtractedNanoResin = new Builder(GTCEu.id("praseodymium_extracted_nano_resin"))
         .fluid()
         .color(10526880)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.NeodymiumExtractedNanoResin = new Builder(GTCEu.id("neodymium_extracted_nano_resin"))
         .fluid()
         .color(11579568)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.PromethiumExtractedNanoResin = new Builder(GTCEu.id("promethium_extracted_nano_resin"))
         .fluid()
         .color(12632256)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.SamariumExtractedNanoResin = new Builder(GTCEu.id("samarium_extracted_nano_resin"))
         .fluid()
         .color(13684944)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.EuropiumExtractedNanoResin = new Builder(GTCEu.id("europium_extracted_nano_resin"))
         .fluid()
         .color(14737632)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.GadoliniumExtractedNanoResin = new Builder(GTCEu.id("gadolinium_extracted_nano_resin"))
         .fluid()
         .color(15790320)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.TerbiumExtractedNanoResin = new Builder(GTCEu.id("terbium_extracted_nano_resin"))
         .fluid()
         .color(33023)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.DysprosiumExtractedNanoResin = new Builder(GTCEu.id("dysprosium_extracted_nano_resin"))
         .fluid()
         .color(65535)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.HolmiumExtractedNanoResin = new Builder(GTCEu.id("holmium_extracted_nano_resin"))
         .fluid()
         .color(65280)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ErbiumExtractedNanoResin = new Builder(GTCEu.id("erbium_extracted_nano_resin"))
         .fluid()
         .color(16711935)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ThuliumExtractedNanoResin = new Builder(GTCEu.id("thulium_extracted_nano_resin"))
         .fluid()
         .color(16711680)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YtterbiumExtractedNanoResin = new Builder(GTCEu.id("ytterbium_extracted_nano_resin"))
         .fluid()
         .color(16776960)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.LutetiumExtractedNanoResin = new Builder(GTCEu.id("lutetium_extracted_nano_resin"))
         .fluid()
         .color(8388736)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.ScandiumExtractedNanoResin = new Builder(GTCEu.id("scandium_extracted_nano_resin"))
         .fluid()
         .color(8421376)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.YttriumExtractedNanoResin = new Builder(GTCEu.id("yttrium_extracted_nano_resin"))
         .fluid()
         .color(32768)
         .iconSet(GTLMaterialIconSet.LIMPID)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MiracleGas = new Builder(GTCEu.id("miracle_gas"))
         .gas()
         .color(15371222)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.Miracle = new Builder(GTCEu.id("miracle"))
         .fluid()
         .color(15371222)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
      GTLMaterials.MiracleAdhesive = new Builder(GTCEu.id("miracle_adhesive"))
         .fluid()
         .color(15709681)
         .flags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION})
         .buildAndRegister();
   }
}
