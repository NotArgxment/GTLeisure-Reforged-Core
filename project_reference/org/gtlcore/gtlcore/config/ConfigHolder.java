package org.gtlcore.gtlcore.config;

import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.Configurable.Comment;
import dev.toma.configuration.config.Configurable.Range;
import dev.toma.configuration.config.Configurable.Synchronized;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = "gtlcore")
public class ConfigHolder {
   public static ConfigHolder INSTANCE;
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
   @Comment(
      "\u66f4\u5927\u7684\u6570\u503c\u4f1a\u8ba9\u754c\u9762\u663e\u793a\u6709\u95ee\u9898\uff0c\u63a8\u8350\u5728\u6837\u677f\u7ba1\u7406\u7ec8\u7aef\u7ba1\u7406"
   )
   @Range(min = 36L, max = 360L)
   public int exPatternProvider = 36;
   @Configurable
   public boolean enablePrimitiveVoidOre = false;
   @Configurable
   @Comment("\u8fde\u9501\u9ed1\u540d\u5355,\u652f\u6301\u901a\u914d\u7b26*")
   @Synchronized
   public String[] blackBlockList = new String[]{"ae2:cable_bus", "minecraft:grass_block"};
   @Configurable
   @Comment("\u53ef\u80fd\u4f1a\u6781\u5c0f\u5730\u5f71\u54cd\u6027\u80fd")
   public boolean enableSmoothAnimations = true;
   @Configurable
   @Comment("ME\u6837\u677f\u603b\u6210\u8f93\u51fa\u6700\u5c0f\u95f4\u9694")
   @Range(min = 1L, max = 100L)
   public int MEPatternOutputMin = 5;
   @Configurable
   @Comment("ME\u6837\u677f\u603b\u6210\u8f93\u51fa\u6700\u5927\u95f4\u9694")
   @Range(min = 1L, max = 200L)
   public int MEPatternOutputMax = 80;
   @Configurable
   @Comment(
      "\u662f\u5426\u542f\u7528ME\u5e93\u5b58\u6781\u9650\u62c9\u53d6\u6a21\u5f0f(\u4fdd\u8bc1\u673a\u5668\u4e0d\u4f1a\u505c\u673a, \u4f46\u662f\u4f1a\u5927\u5e45\u964d\u4f4eTPS!)"
   )
   public boolean enableUltimateMEStocking = false;
   @Configurable
   @Comment(
      "AE2\u5408\u6210\u66f4\u65b0\u95f4\u9694(tick), \u503c\u8d8a\u5927\u6027\u80fd\u8d8a\u597d\u4f46\u54cd\u5e94\u8d8a\u6162, \u5fc5\u987b\u662f2\u7684\u5e42\u6b21(1,2,4,8,16)"
   )
   @Range(min = 1L, max = 16L)
   public int ae2CraftingServiceUpdateInterval = 4;
   @Configurable
   @Comment(
      "AE2\u5e93\u5b58\u66f4\u65b0\u95f4\u9694(tick), \u503c\u8d8a\u5927\u6027\u80fd\u8d8a\u597d\u4f46\u54cd\u5e94\u8d8a\u6162, \u5fc5\u987b\u662f2\u7684\u5e42\u6b21(1,2,4,8,16)"
   )
   @Range(min = 1L, max = 16L)
   public int ae2StorageServiceUpdateInterval = 8;
   @Configurable
   @Comment("AE2\u5408\u6210\u8ba1\u7b97\u6a21\u5f0f: LEGACY(\u539f\u7248), FAST(\u5feb\u901f), ULTRA_FAST(\u6700\u5feb)")
   public AE2CalculationMode ae2CalculationMode = AE2CalculationMode.ULTRA_FAST;
   @Configurable
   @Comment("\u7f16\u5199\u591a\u65b9\u5757\u7ed3\u6784\u6837\u677f\u8fc7\u6ee4\u7684\u4ed3\u5ba4")
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
   @Comment(
      {
            "\u8fde\u9501\u6316\u6398\uff08\u4e0d\u8fde\u7eed\u6a21\u5f0f\uff09\u65f6\uff0c\u68c0\u67e5\u76f8\u90bb\u65b9\u5757\u7684\u8303\u56f4",
            "The range to check adjacent blocks during chain mining (non-continuous mode)"
      }
   )
   @Range(min = 1L, max = 20L)
   @Synchronized
   public int ftbUltimineRange = 4;
   @Configurable
   public String[] mobList1 = new String[]{
      "chicken",
      "rabbit",
      "sheep",
      "cow",
      "horse",
      "pig",
      "donkey",
      "skeleton_horse",
      "iron_golem",
      "wolf",
      "goat",
      "parrot",
      "camel",
      "cat",
      "fox",
      "llama",
      "panda",
      "polar_bear"
   };
   @Configurable
   public String[] mobList2 = new String[]{
      "ghast",
      "zombie",
      "pillager",
      "zombie_villager",
      "skeleton",
      "drowned",
      "witch",
      "spider",
      "creeper",
      "husk",
      "wither_skeleton",
      "blaze",
      "zombified_piglin",
      "slime",
      "vindicator",
      "enderman"
   };

   public static void init() {
      synchronized (LOCK) {
         if (INSTANCE == null) {
            INSTANCE = (ConfigHolder)Configuration.registerConfig(ConfigHolder.class, ConfigFormats.yaml()).getConfigInstance();
         }
      }
   }
}
