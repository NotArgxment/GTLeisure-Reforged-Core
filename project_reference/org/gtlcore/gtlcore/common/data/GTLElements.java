package org.gtlcore.gtlcore.common.data;

import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.common.data.GTElements;

public class GTLElements {
   public static Element SPACETIME;
   public static Element INFINITY;
   public static Element ORICHALCUM;
   public static Element ADAMANTIUM;
   public static Element VIBRANIUM;
   public static Element DRACONIUM;
   public static Element CHAOS;
   public static Element MITHRIL;
   public static Element INFUSCOLIUM;
   public static Element TARANIUM;
   public static Element CRYSTALMATRIX;
   public static Element COSMICNEUTRONIUM;
   public static Element ENDERIUM;
   public static Element ADAMANTINE;
   public static Element LEGENDARIUM;
   public static Element STARMETAL;
   public static Element DRACONIUMAWAKENED;
   public static Element ECHOITE;
   public static Element ETERNITY;
   public static Element DEGENERATE_REHENIUM;
   public static Element HEAVY_QUARK_DEGENERATE_MATTER;
   public static Element QUANTUMCHROMODYNAMICALLY_CONFINED_MATTER;
   public static Element TRANSCENDENTMETAL;
   public static Element COSMIC_MESH;
   public static Element TITANIUM50;
   public static Element TENGAM;
   public static Element YITTERBIUM178;
   public static Element COPPER76;
   public static Element URUIUM;
   public static Element STARLIGHT;
   public static Element MANA;
   public static Element ELECTRON;
   public static Element ALPHA;
   public static Element PROTON;
   public static Element GLUONS;
   public static Element QUARK_GLUON;
   public static Element HEAVY_QUARKS;
   public static Element LIGHT_QUARKS;
   public static Element HEAVY_LEPTON_MIXTURE;
   public static Element HEAVY_QUARK_ENRICHED_MIXTURE;
   public static Element DENSE_NEUTRON;
   public static Element HIGH_ENERGY_QUARK_GLUON;
   public static Element TIME;
   public static Element STAR_MATTER;
   public static Element RAW_STAR_MATTER;
   public static Element RHUGNOR;
   public static Element HYPogen;
   public static Element SHIRABON;
   public static Element MAGMATTER;
   public static Element ASTRALTITANIUM;
   public static Element CELESTIALTUNGSTEN;
   public static Element QUANTANIUM;

   public static void init() {
      SPACETIME = GTElements.createAndRegister(0L, 0L, -1L, null, "spacetime", "\u00a77\u7194\u70bc\u4e3a\u6d41\u4f53\u7684\u65f6\u7a7a", false);
      INFINITY = GTElements.createAndRegister(100000L, 100000L, -1L, null, "infinity", "\u221e", false);
      ORICHALCUM = GTElements.createAndRegister(320L, 320L, -1L, null, "orichalcum", "Or", false);
      ADAMANTIUM = GTElements.createAndRegister(380L, 380L, -1L, null, "adamantium", "An", false);
      VIBRANIUM = GTElements.createAndRegister(550L, 550L, -1L, null, "vibranium", "Vi", false);
      DRACONIUM = GTElements.createAndRegister(8000L, 8000L, -1L, null, "draconium", "Dc", false);
      CHAOS = GTElements.createAndRegister(20000L, 20000L, -1L, null, "chaos", "\u00a78\u00a7kchaos", false);
      MITHRIL = GTElements.createAndRegister(330L, 330L, -1L, null, "mithril", "Mi", false);
      INFUSCOLIUM = GTElements.createAndRegister(600L, 600L, -1L, null, "infuscolium", "If", true);
      TARANIUM = GTElements.createAndRegister(440L, 440L, -1L, null, "taranium", "Tn", false);
      CRYSTALMATRIX = GTElements.createAndRegister(10000L, 0L, -1L, null, "crystalmatrix", "\u00a7b\u00a7ke\u00a7r\u00a7b\u2727\u00a7ke", true);
      COSMICNEUTRONIUM = GTElements.createAndRegister(0L, 20000L, -1L, null, "cosmicneutronium", "Cnt", false);
      ENDERIUM = GTElements.createAndRegister(350L, 350L, -1L, null, "enderium", "En", false);
      ADAMANTINE = GTElements.createAndRegister(600L, 600L, -1L, null, "adamantine", "Ad", false);
      LEGENDARIUM = GTElements.createAndRegister(640L, 640L, -1L, null, "legendarium", "Le", false);
      STARMETAL = GTElements.createAndRegister(530L, 530L, -1L, null, "starmetal", "St", false);
      DRACONIUMAWAKENED = GTElements.createAndRegister(16000L, 16000L, -1L, null, "draconiumawakened", "\u2735Dc\u2735", true);
      ECHOITE = GTElements.createAndRegister(570L, 570L, -1L, null, "echoite", "Ec", false);
      ETERNITY = GTElements.createAndRegister(1000000L, 1000000L, -1L, null, "eternity", "Et\u2743", false);
      DEGENERATE_REHENIUM = GTElements.createAndRegister(100L, 100L, -1L, null, "degenerate_rhenium", "\u00a7bRe", false);
      HEAVY_QUARK_DEGENERATE_MATTER = GTElements.createAndRegister(
         120L, 150L, -1L, null, "heavy_quark_degenerate_matter", "\u00a7b\u00a7ke\u00a7r\u00a7b(u\u2082);d\u00a7ke", true
      );
      QUANTUMCHROMODYNAMICALLY_CONFINED_MATTER = GTElements.createAndRegister(
         160L, 200L, -1L, null, "quantumchromodynamically_confined_matter", "\u00a7b\u00a7ke\u00a7r\u00a7b(u\u2082);d(c\u2082);s(t\u2082);bg\u00a7ke", true
      );
      TRANSCENDENTMETAL = GTElements.createAndRegister(100L, 100L, -1L, null, "transcendentmetal", "\u00a7kmetal", false);
      COSMIC_MESH = GTElements.createAndRegister(10000L, 10000L, -1L, null, "cosmic_mesh", "\u00a7kcm", false);
      TITANIUM50 = GTElements.createAndRegister(22L, 28L, -1L, null, "titanium50", "Ti\u2075\u2070", false);
      TENGAM = GTElements.createAndRegister(200L, 0L, -1L, null, "tengam", "M", false);
      YITTERBIUM178 = GTElements.createAndRegister(70L, 108L, -1L, null, "ytterbium178", "Yb\u00b9\u2077\u2078", true);
      COPPER76 = GTElements.createAndRegister(29L, 47L, -1L, null, "copper76", "Cu\u2077\u2076", true);
      URUIUM = GTElements.createAndRegister(480L, 480L, -1L, null, "uruium", "Ur", false);
      STARLIGHT = GTElements.createAndRegister(0L, 0L, -1L, null, "starlight", "\u00a79Sl", false);
      MANA = GTElements.createAndRegister(0L, 0L, -1L, null, "mana", "\u00a75\u00a7kemana", false);
      ELECTRON = GTElements.createAndRegister(0L, 0L, -1L, null, "electron", "\u00a7ke\u00a7re\u00a7ke", false);
      ALPHA = GTElements.createAndRegister(0L, 0L, -1L, null, "alpha", "\u00a7ke\u00a7r\u03b1\u00a7ke", false);
      PROTON = GTElements.createAndRegister(0L, 0L, -1L, null, "proton", "\u00a7ke\u00a7rp\u00a7ke", false);
      GLUONS = GTElements.createAndRegister(0L, 0L, -1L, null, "gluons", "\u00a7ke\u00a7rg\u00a7ke", false);
      QUARK_GLUON = GTElements.createAndRegister(0L, 0L, -1L, null, "quark_gluon", "\u00a7ke\u00a7r(u2);d(c2);s(t2);bg\u00a7ke", false);
      HEAVY_QUARKS = GTElements.createAndRegister(0L, 0L, -1L, null, "heavy_quarks", "\u00a7ke\u00a7r(u\u2082);d\u00a7ke", false);
      LIGHT_QUARKS = GTElements.createAndRegister(0L, 0L, -1L, null, "light_quarks", "\u00a7ke\u00a7r(c\u2082);(t\u2082);b\u00a7ke", false);
      HEAVY_LEPTON_MIXTURE = GTElements.createAndRegister(0L, 0L, -1L, null, "heavy_lepton_mixture", "\u00a7ke\u00a7r(t\u2082);u\u00a7ke", false);
      HEAVY_QUARK_ENRICHED_MIXTURE = GTElements.createAndRegister(
         0L, 0L, -1L, null, "heavy_quark_enriched_mixture", "\u00a7ke\u00a7r(u\u2082);d(c\u2082);s(t\u2082);b\u00a7ke", false
      );
      DENSE_NEUTRON = GTElements.createAndRegister(0L, 0L, -1L, null, "dense_neutron", "\u00a7ke\u00a7rn\u00a7ke", false);
      HIGH_ENERGY_QUARK_GLUON = GTElements.createAndRegister(
         0L, 0L, -1L, null, "high_energy_quark_gluon", "\u00a7ke\u00a7r(u\u2082);d(c\u2082);s(t\u2082);bg\u00a7ke", false
      );
      TIME = GTElements.createAndRegister(0L, 0L, -1L, null, "time", "\u00a7ketime", false);
      STAR_MATTER = GTElements.createAndRegister(10000L, 10000L, -1L, null, "star_matter", "\u00a7kestar_matter", true);
      RAW_STAR_MATTER = GTElements.createAndRegister(10000L, 10000L, -1L, null, "raw_star_matter", "\u00a76\u00a7kestar_matter", true);
      RHUGNOR = GTElements.createAndRegister(100000L, 100000L, -1L, null, "rhugnor", "Fs\u26b6", true);
      HYPogen = GTElements.createAndRegister(100000L, 100000L, -1L, null, "hypogen", "Hy\u26b6", true);
      SHIRABON = GTElements.createAndRegister(5000L, 5000L, -1L, null, "shirabon", "Sh\u23e7", false);
      MAGMATTER = GTElements.createAndRegister(0L, 0L, -1L, null, "magmatter", "M\u238b", false);
      ASTRALTITANIUM = GTElements.createAndRegister(0L, 0L, -1L, null, "astraltitanium", "\u2727\u25c7\u2727", false);
      CELESTIALTUNGSTEN = GTElements.createAndRegister(0L, 0L, -1L, null, "celestialtungsten", "\u2726\u25c6\u2726", false);
      QUANTANIUM = GTElements.createAndRegister(800L, 800L, -1L, null, "quantanium", "Qt", false);
   }
}
