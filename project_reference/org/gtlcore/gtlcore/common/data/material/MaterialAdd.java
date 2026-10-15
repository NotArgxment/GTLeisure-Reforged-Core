package org.gtlcore.gtlcore.common.data.material;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.DustProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IngotProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.WireProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.fluids.FluidBuilder;
import com.gregtechceu.gtceu.api.fluids.FluidState;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import org.gtlcore.gtlcore.api.data.chemical.material.info.GTLMaterialFlags;

public class MaterialAdd {
   public static void init() {
      GTMaterials.Monazite.addFlags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION});
      GTMaterials.Bastnasite.addFlags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION});
      GTMaterials.PotassiumSulfate
         .setComponents(
            new MaterialStack[]{
               new MaterialStack(GTMaterials.Potassium, 2L), new MaterialStack(GTMaterials.Sulfur, 1L), new MaterialStack(GTMaterials.Oxygen, 4L)
            }
         );
      GTMaterials.AmmoniumChloride
         .setComponents(
            new MaterialStack[]{
               new MaterialStack(GTMaterials.Nitrogen, 1L), new MaterialStack(GTMaterials.Hydrogen, 4L), new MaterialStack(GTMaterials.Chlorine, 1L)
            }
         );
      GTMaterials.Duranium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME});
      GTMaterials.Naquadria.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME});
      GTMaterials.Trinium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME});
      ((FluidProperty)GTMaterials.Silver.getProperty(PropertyKey.FLUID))
         .enqueueRegistration(FluidStorageKeys.PLASMA, new FluidBuilder().state(FluidState.PLASMA));
      ((BlastProperty)GTMaterials.Europium.getProperty(PropertyKey.BLAST)).setBlastTemperature(9600);
      GTMaterials.Neutronium
         .addFlags(
            new MaterialFlag[]{
               MaterialFlags.GENERATE_ROTOR,
               MaterialFlags.GENERATE_SPRING,
               MaterialFlags.GENERATE_GEAR,
               MaterialFlags.GENERATE_SMALL_GEAR,
               MaterialFlags.GENERATE_SPRING_SMALL
            }
         );
      GTMaterials.Neutronium.setProperty(PropertyKey.WIRE, new WireProperties((int)GTValues.V[11], 2, 64));
      GTMaterials.Mendelevium.setProperty(PropertyKey.WIRE, new WireProperties((int)GTValues.V[9], 4, 16));
      GTMaterials.RutheniumTetroxide.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.RutheniumTetroxide.addFlags(new MaterialFlag[]{MaterialFlags.NO_UNIFICATION});
      GTMaterials.Actinium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Actinium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Selenium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Aluminium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_ROTOR});
      GTMaterials.Neutronium.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Carbon.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.RadAway.addFlags(new MaterialFlag[]{MaterialFlags.DISABLE_DECOMPOSITION});
      GTMaterials.Platinum.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_SPRING});
      GTMaterials.Chromium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR});
      GTMaterials.Naquadah.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM, MaterialFlags.GENERATE_FRAME});
      GTMaterials.NaquadahAlloy.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE});
      GTMaterials.RutheniumTriniumAmericiumNeutronate.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE});
      GTMaterials.TitaniumTungstenCarbide.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_GEAR});
      GTMaterials.Europium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_SPRING_SMALL});
      GTMaterials.Germanium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Germanium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Germanium.setProperty(PropertyKey.BLAST, new BlastProperty(3800, GasTier.MID, GTValues.VA[3], 700));
      GTMaterials.Sodium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Seaborgium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Seaborgium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Seaborgium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Dubnium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Dubnium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Dubnium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE});
      GTMaterials.Rhenium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Rhenium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Rhenium.setProperty(PropertyKey.BLAST, new BlastProperty(7400, GasTier.MID, GTValues.VA[4], 900));
      GTMaterials.Calcium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Calcium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Praseodymium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Praseodymium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Gadolinium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Gadolinium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Terbium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Terbium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Dysprosium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Dysprosium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Holmium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Holmium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Erbium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Erbium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Thulium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Thulium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Ytterbium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Ytterbium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Scandium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Scandium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.NaquadahEnriched.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME});
      GTMaterials.NickelZincFerrite.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FOIL});
      GTMaterials.Promethium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Promethium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Polonium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Polonium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Polonium.setProperty(PropertyKey.BLAST, new BlastProperty(3700, GasTier.MID, GTValues.VA[5], 400));
      GTMaterials.Hafnium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Hafnium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Hafnium.setProperty(PropertyKey.BLAST, new BlastProperty(6800, GasTier.MID, GTValues.VA[3], 1100));
      GTMaterials.EnderEye.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.EnderPearl.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Protactinium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Protactinium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Neptunium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Neptunium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Curium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Curium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Berkelium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Berkelium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Einsteinium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Einsteinium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Californium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Californium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Fermium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Fermium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Mendelevium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Mendelevium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Oganesson.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Oganesson.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Oganesson.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Flerovium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Flerovium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Nobelium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Lawrencium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Nobelium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Lawrencium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Hassium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Hassium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Hassium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Rubidium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Rubidium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Technetium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Technetium.setProperty(PropertyKey.BLAST, new BlastProperty(5600, GasTier.MID, GTValues.VA[3], 800));
      GTMaterials.Technetium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_PLATE});
      GTMaterials.Francium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Strontium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Zirconium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Radium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Radium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Tellurium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Rutherfordium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Astatine.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Astatine.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Bohrium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Bohrium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Thallium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Meitnerium.setProperty(PropertyKey.DUST, new DustProperty());
      GTMaterials.Meitnerium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Roentgenium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Copernicium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Nihonium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Moscovium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Livermorium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Tennessine.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Roentgenium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Copernicium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Nihonium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Moscovium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Livermorium.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Tennessine.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.ReinforcedEpoxyResin.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_ROD});
      GTMaterials.Phosphorus.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Tellurium.setProperty(PropertyKey.ORE, new OreProperty());
      GTMaterials.Rubidium.setProperty(PropertyKey.ORE, new OreProperty());
      ((OreProperty)GTMaterials.Rubidium.getProperty(PropertyKey.ORE))
         .setOreByProducts(new Material[]{GTMaterials.Caesium, GTMaterials.Potassium, GTMaterials.Sodium});
      GTMaterials.Titanium.setProperty(PropertyKey.ORE, new OreProperty());
      ((OreProperty)GTMaterials.Titanium.getProperty(PropertyKey.ORE)).setOreByProducts(new Material[]{GTMaterials.Aluminium, GTMaterials.Iron});
      GTMaterials.Tungsten.setProperty(PropertyKey.ORE, new OreProperty());
      ((OreProperty)GTMaterials.Tungsten.getProperty(PropertyKey.ORE)).setOreByProducts(new Material[]{GTMaterials.Lithium, GTMaterials.Calcium});
      GTMaterials.Indium.setProperty(PropertyKey.ORE, new OreProperty());
      ((OreProperty)GTMaterials.Indium.getProperty(PropertyKey.ORE)).setOreByProducts(new Material[]{GTMaterials.Aluminium, GTMaterials.Zinc});
      GTMaterials.NaquadahEnriched.setProperty(PropertyKey.ORE, new OreProperty());
      ((OreProperty)GTMaterials.NaquadahEnriched.getProperty(PropertyKey.ORE)).setOreByProducts(new Material[]{GTMaterials.Naquadah, GTMaterials.Sulfur});
      GTMaterials.EchoShard.setProperty(PropertyKey.FLUID, new FluidProperty(FluidStorageKeys.LIQUID, new FluidBuilder()));
      GTMaterials.Graphite.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.VanadiumSteel.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FRAME});
      GTMaterials.Lutetium.setProperty(PropertyKey.INGOT, new IngotProperty());
      GTMaterials.Carbon.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Iron.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Glowstone.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Copper.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Silver.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Gold.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Iridium.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Osmium.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Rhenium.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_NANOSWARM});
      GTMaterials.Nickel.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Platinum.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Almandine.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Grossular.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Pyrope.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Spessartine.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Chalcopyrite.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Pentlandite.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Sphalerite.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Monazite.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Redstone.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.NaquadahEnriched.addFlags(new MaterialFlag[]{GTLMaterialFlags.GENERATE_MILLED});
      GTMaterials.Titanium.addFlags(new MaterialFlag[]{MaterialFlags.GENERATE_FINE_WIRE});
   }
}
