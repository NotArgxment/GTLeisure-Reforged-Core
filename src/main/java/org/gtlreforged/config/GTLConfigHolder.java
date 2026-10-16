package org.gtlreforged.config;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.Configurable.Comment;
import dev.toma.configuration.config.Configurable.Range;
import dev.toma.configuration.config.Configurable.Synchronized;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = "gtlreforged")
public class GTLConfigHolder {

   public static GTLConfigHolder INSTANCE;
   private static final Object LOCK = new Object();

   @Configurable
   public boolean disableDrift = true;

   @Configurable
   public boolean enableSkyBlokeMode = false;

   @Configurable
   @Range(min = 1L)
   public int oreMultiplier = 4;

   @Configurable
   @Range(min = 1L)
   public int cellType = 4;

   @Configurable
   @Range(min = 1L)
   public int spacetimePip = 2147483647;

   @Configurable
   @Range(min = 0L)
   public double durationMultiplier = 1.0;

   @Configurable
   @Range(min = 1L)
   public int travelStaffCD = 2;

   @Configurable
   @Comment("Larger values will cause UI display issues, it is recommended to manage them in the Pattern Management Terminal")
   @Range(min = 36L, max = 360L)
   public int exPatternProvider = 36;

   @Configurable
   public boolean enablePrimitiveVoidOre = false;

   @Configurable
   @Comment("Chain blacklist, supports wildcard *")
   @Synchronized
   public String[] blackBlockList = new String[]{"ae2:cable_bus", "minecraft:grass_block"};

   @Configurable
   @Comment("May have a very small impact on performance")
   public boolean enableSmoothAnimations = true;

   @Configurable
   @Comment("Minimum output interval of the ME pattern assembly")
   @Range(min = 1L, max = 100L)
   public int MEPatternOutputMin = 5;

   @Configurable
   @Comment("Maximum output interval of the ME pattern assembly")
   @Range(min = 1L, max = 200L)
   public int MEPatternOutputMax = 80;

   @Configurable
   @Comment("Enable ME inventory ultimate stocking mode (guarantees machines never stall, but greatly reduces TPS!)")
   public boolean enableUltimateMEStocking = false;

   @Configurable
   @Comment("AE2 crafting update interval (ticks), higher values improve performance but slow down response, must be a power of 2 (1, 2, 4, 8, 16)")
   @Range(min = 1L, max = 16L)
   public int ae2CraftingServiceUpdateInterval = 4;

   @Configurable
   @Comment("AE2 storage update interval (ticks), higher values improve performance but slow down response, must be a power of 2 (1, 2, 4, 8, 16)")
   @Range(min = 1L, max = 16L)
   public int ae2StorageServiceUpdateInterval = 8;

   @Configurable
   @Comment("AE2 Calculation Mode: LEGACY (original), FAST, ULTRA_FAST (fastest)")
   public AE2CalculationMode ae2CalculationMode = AE2CalculationMode.ULTRA_FAST;

   @Configurable
   @Comment("Hatches filtered out when writing multiblock structure patterns")
   public String[] filterHatch = new String[]{
           "input_bus",
           "output_bus",
           "item_import_bus",
           "item_export_bus",
           "input_hatch",
           "output_hatch",
           "energy_input_hatch",
           "energy_output_hatch",
           "laser_target_hatch",
           "laser_source_hatch",
           "computation_transmitter_hatch",
           "computation_receiver_hatch",
           "data_transmitter_hatch",
           "data_receiver_hatch",
           "maintenance",
           "muffler",
           "rotor_holder"
   };

   @Configurable
   @Comment({ "The range to check adjacent blocks during chain mining (non-continuous mode)" })
   @Range(min = 1L, max = 20L)
   @Synchronized
   public int ftbUltimineRange = 4;

   @Configurable
   public String[] mobList1 = new String[]{
           "chicken", "rabbit", "sheep", "cow", "horse", "pig", "donkey", "skeleton_horse",
           "iron_golem", "wolf", "goat", "parrot", "camel", "cat", "fox", "llama", "panda", "polar_bear"
   };

   @Configurable
   public String[] mobList2 = new String[]{
           "ghast", "zombie", "pillager", "zombie_villager", "skeleton", "drowned", "witch", "spider",
           "creeper", "husk", "wither_skeleton", "blaze", "zombified_piglin", "slime", "vindicator", "enderman"
   };

   public static void init() {
      synchronized (LOCK) {
         if (INSTANCE == null) {
            INSTANCE = Configuration.registerConfig(GTLConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
         }
      }
   }
}