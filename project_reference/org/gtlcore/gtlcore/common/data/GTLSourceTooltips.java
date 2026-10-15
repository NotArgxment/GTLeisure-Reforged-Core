package org.gtlcore.gtlcore.common.data;

import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.api.event.SourceTooltipRegistrationEvent;
import org.gtlcore.gtlcore.config.ConfigHolder;

@OnlyIn(Dist.CLIENT)
public class GTLSourceTooltips {
   public static void init(SourceTooltipRegistrationEvent event) {
      registerWorldFragmentCollectionTooltip(event);
      registerSpaceElevatorTooltip(event);
      registerSeriesTooltips(event);
   }

   private static Component ofWFC(MutableComponent wf, String cs) {
      return Component.m_237110_(
         "gtlcore.source_tooltip.world_fragment_collection",
         new Object[]{wf.m_130940_(ChatFormatting.DARK_GREEN), Component.m_237113_(cs).m_130940_(ChatFormatting.GRAY)}
      );
   }

   private static void registerWorldFragmentCollectionTooltip(SourceTooltipRegistrationEvent event) {
      if (ConfigHolder.INSTANCE.enableSkyBlokeMode) {
         MutableComponent barnarda = Component.m_237115_("item.gtlcore.world_fragments_barnarda");
         MutableComponent ceres = Component.m_237115_("item.gtlcore.world_fragments_ceres");
         MutableComponent enceladus = Component.m_237115_("item.gtlcore.world_fragments_enceladus");
         MutableComponent end = Component.m_237115_("item.gtlcore.world_fragments_end");
         MutableComponent ganymede = Component.m_237115_("item.gtlcore.world_fragments_ganymede");
         MutableComponent glacio = Component.m_237115_("item.gtlcore.world_fragments_glacio");
         MutableComponent io = Component.m_237115_("item.gtlcore.world_fragments_io");
         MutableComponent mars = Component.m_237115_("item.gtlcore.world_fragments_mars");
         MutableComponent mercury = Component.m_237115_("item.gtlcore.world_fragments_mercury");
         MutableComponent moon = Component.m_237115_("item.gtlcore.world_fragments_moon");
         MutableComponent nether = Component.m_237115_("item.gtlcore.world_fragments_nether");
         MutableComponent overworld = Component.m_237115_("item.gtlcore.world_fragments_overworld");
         MutableComponent pluto = Component.m_237115_("item.gtlcore.world_fragments_pluto");
         MutableComponent reactor = Component.m_237115_("item.gtlcore.world_fragments_reactor");
         MutableComponent titan = Component.m_237115_("item.gtlcore.world_fragments_titan");
         MutableComponent venus = Component.m_237115_("item.gtlcore.world_fragments_venus");
         event.addItemTooltip("gtceu:raw_almandine", ofWFC(overworld, "[ 14 ]"));
         event.addItemTooltip("gtceu:raw_aluminium", ofWFC(moon, "[ 2 ]"), ofWFC(ganymede, "[ 3 ]"), ofWFC(end, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_alunite", ofWFC(reactor, "[ 4 ]"), ofWFC(nether, "[ 3 ]"), ofWFC(titan, "[ 1 ]"), ofWFC(glacio, "[ 1 ]"));
         event.addItemTooltip("gtceu:raw_amethyst", ofWFC(overworld, "[ 19 ]"));
         event.addItemTooltip("gtceu:raw_apatite", ofWFC(overworld, "[ 10 ]"), ofWFC(mars, "[ 3 ]"), ofWFC(enceladus, "[ 3 ]"), ofWFC(barnarda, "[ 7 ]"));
         event.addItemTooltip("gtceu:raw_asbestos", ofWFC(overworld, "[ 22 ]"));
         event.addItemTooltip(
            "gtceu:raw_barite", ofWFC(reactor, "[ 2, 5 ]"), ofWFC(nether, "[ 7 ]"), ofWFC(ceres, "[ 1 ]"), ofWFC(ganymede, "[ 2 ]"), ofWFC(barnarda, "[ 3 ]")
         );
         event.addItemTooltip("gtceu:raw_basaltic_mineral_sand", ofWFC(overworld, "[ 20 ]"));
         event.addItemTooltip("gtceu:raw_bastnasite", ofWFC(moon, "[ 3 ]"), ofWFC(nether, "[ 1 ]"), ofWFC(ceres, "[ 3 ]"), ofWFC(glacio, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_bauxite", ofWFC(moon, "[ 2 ]"), ofWFC(ganymede, "[ 3 ]"), ofWFC(end, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_bentonite", ofWFC(overworld, "[ 13 ]"), ofWFC(venus, "[ 1 ]"), ofWFC(io, "[ 3 ]"), ofWFC(enceladus, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_beryllium", ofWFC(reactor, "[ 3 ]"), ofWFC(nether, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_blue_topaz", ofWFC(reactor, "[ 1 ]"), ofWFC(nether, "[ 10 ]"), ofWFC(ganymede, "[ 1 ]"), ofWFC(barnarda, "[ 2 ]"));
         event.addItemTooltip(
            "gtceu:raw_bornite", ofWFC(mars, "[ 1 ]"), ofWFC(nether, "[ 10 ]"), ofWFC(enceladus, "[ 1 ]"), ofWFC(end, "[ 5 ]"), ofWFC(glacio, "[ 6 ]")
         );
         event.addItemTooltip("gtceu:raw_calcite", ofWFC(overworld, "[ 17 ]"));
         event.addItemTooltip("gtceu:raw_calorite", ofWFC(mercury, "[ 1 ]"), ofWFC(glacio, "[ 8 ]"));
         event.addItemTooltip("gtceu:raw_cassiterite_sand", ofWFC(overworld, "[ 22 ]"));
         event.addItemTooltip("gtceu:raw_cassiterite", ofWFC(overworld, "[ 5, 8 ]"));
         event.addItemTooltip("gtceu:raw_celestine", ofWFC(io, "[ 1 ]"), ofWFC(glacio, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_certus_quartz", ofWFC(reactor, "[ 5 ]"), ofWFC(nether, "[ 7 ]"), ofWFC(ceres, "[ 1 ]"), ofWFC(barnarda, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_chalcocite", ofWFC(nether, "[ 10 ]"));
         event.addItemTooltip("gtceu:raw_chalcopyrite", ofWFC(overworld, "[ 5, 6 ]"));
         event.addItemTooltip("gtceu:raw_cinnabar", ofWFC(overworld, "[ 9 ]"), ofWFC(nether, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_coal", ofWFC(overworld, "[ 11, 15 ]"));
         event.addItemTooltip("gtceu:raw_cobalt", ofWFC(mercury, "[ 1 ]"), ofWFC(glacio, "[ 8 ]"));
         event.addItemTooltip("gtceu:raw_cobaltite", ofWFC(overworld, "[ 12 ]"), ofWFC(mercury, "[ 2 ]"), ofWFC(pluto, "[ 4 ]"));
         event.addItemTooltip(
            "gtceu:raw_cooperite", ofWFC(mars, "[ 1 ]"), ofWFC(io, "[ 1 ]"), ofWFC(enceladus, "[ 1 ]"), ofWFC(end, "[ 5 ]"), ofWFC(glacio, "[ 4, 6 ]")
         );
         event.addItemTooltip("gtceu:raw_desh", ofWFC(venus, "[ 3 ]"), ofWFC(titan, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_diamond", ofWFC(overworld, "[ 11 ]"));
         event.addItemTooltip(
            "gtceu:raw_diatomite", ofWFC(overworld, "[ 22 ]"), ofWFC(reactor, "[ 4 ]"), ofWFC(nether, "[ 3 ]"), ofWFC(titan, "[ 1 ]"), ofWFC(glacio, "[ 1 ]")
         );
         event.addItemTooltip("gtceu:raw_electrotine", ofWFC(reactor, "[ 4 ]"), ofWFC(nether, "[ 3 ]"), ofWFC(titan, "[ 1 ]"), ofWFC(glacio, "[ 1 ]"));
         event.addItemTooltip("gtceu:raw_emerald", ofWFC(reactor, "[ 3 ]"), ofWFC(nether, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_fullers_earth", ofWFC(overworld, "[ 20 ]"));
         event.addItemTooltip("gtceu:raw_galena", ofWFC(overworld, "[ 7 ]"));
         event.addItemTooltip("gtceu:raw_garnet_sand", ofWFC(overworld, "[ 22 ]"));
         event.addItemTooltip("gtceu:raw_garnierite", ofWFC(overworld, "[ 12 ]"), ofWFC(mercury, "[ 2 ]"), ofWFC(pluto, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_glauconite_sand", ofWFC(overworld, "[ 13, 3 ]"), ofWFC(venus, "[ 1 ]"), ofWFC(io, "[ 3 ]"), ofWFC(enceladus, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_goethite", ofWFC(overworld, "[ 2 ]"), ofWFC(nether, "[ 9 ]"));
         event.addItemTooltip("gtceu:raw_granitic_mineral_sand", ofWFC(overworld, "[ 20 ]"));
         event.addItemTooltip("gtceu:raw_graphite", ofWFC(overworld, "[ 11 ]"));
         event.addItemTooltip("gtceu:raw_green_sapphire", ofWFC(overworld, "[ 14 ]"));
         event.addItemTooltip("gtceu:raw_grossular", ofWFC(overworld, "[ 4 ]"), ofWFC(nether, "[ 5 ]"), ofWFC(ganymede, "[ 5 ]"), ofWFC(titan, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_gypsum", ofWFC(overworld, "[ 20 ]"));
         event.addItemTooltip("gtceu:raw_hematite", ofWFC(overworld, "[ 2 ]"), ofWFC(nether, "[ 9 ]"));
         event.addItemTooltip("gtceu:raw_ilmenite", ofWFC(moon, "[ 2 ]"), ofWFC(ganymede, "[ 3 ]"), ofWFC(end, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_kyanite", ofWFC(overworld, "[ 18 ]"));
         event.addItemTooltip("gtceu:raw_lapis", ofWFC(overworld, "[ 17 ]"));
         event.addItemTooltip("gtceu:raw_lazurite", ofWFC(overworld, "[ 17 ]"));
         event.addItemTooltip("gtceu:raw_lead", ofWFC(overworld, "[ 7 ]"));
         event.addItemTooltip("gtceu:raw_lepidolite", ofWFC(overworld, "[ 21 ]"));
         event.addItemTooltip("gtceu:raw_lithium", ofWFC(mars, "[ 2 ]"), ofWFC(end, "[ 3 ]"), ofWFC(glacio, "[ 7 ]"), ofWFC(barnarda, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_magnesite", ofWFC(venus, "[ 3 ]"), ofWFC(mercury, "[ 1 ]"), ofWFC(titan, "[ 3 ]"), ofWFC(glacio, "[ 8 ]"));
         event.addItemTooltip(
            "gtceu:raw_magnetite", ofWFC(overworld, "[ 13, 16 ]"), ofWFC(venus, "[ 1 ]"), ofWFC(io, "[ 3 ]"), ofWFC(enceladus, "[ 2 ]"), ofWFC(end, "[ 2 ]")
         );
         event.addItemTooltip("gtceu:raw_malachite", ofWFC(overworld, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_mica", ofWFC(overworld, "[ 18 ]"));
         event.addItemTooltip("gtceu:raw_molybdenite", ofWFC(reactor, "[ 8 ]"), ofWFC(nether, "[ 6 ]"), ofWFC(ceres, "[ 2 ]"), ofWFC(glacio, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_molybdenum", ofWFC(reactor, "[ 8 ]"), ofWFC(nether, "[ 6 ]"), ofWFC(ceres, "[ 2 ]"), ofWFC(glacio, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_monazite", ofWFC(moon, "[ 3 ]"), ofWFC(nether, "[ 1 ]"), ofWFC(ceres, "[ 3 ]"), ofWFC(glacio, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_naquadah", ofWFC(io, "[ 2 ]"), ofWFC(pluto, "[ 2 ]"), ofWFC(end, "[ 1 ]"), ofWFC(barnarda, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_neodymium", ofWFC(moon, "[ 3 ]"), ofWFC(nether, "[ 1 ]"), ofWFC(ceres, "[ 3 ]"), ofWFC(glacio, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_nether_quartz", ofWFC(reactor, "[ 2 ]"), ofWFC(nether, "[ 11 ]"), ofWFC(ganymede, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_nickel", ofWFC(overworld, "[ 12 ]"), ofWFC(mercury, "[ 2 ]"), ofWFC(pluto, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_oilsands", ofWFC(overworld, "[ 23 ]"));
         event.addItemTooltip("gtceu:raw_olivine", ofWFC(overworld, "[ 13 ]"), ofWFC(venus, "[ 1 ]"), ofWFC(io, "[ 3 ]"), ofWFC(enceladus, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_opal", ofWFC(overworld, "[ 19 ]"));
         event.addItemTooltip("gtceu:raw_ostrum", ofWFC(ceres, "[ 4 ]"), ofWFC(glacio, "[ 9 ]"));
         event.addItemTooltip("gtceu:raw_palladium", ofWFC(mars, "[ 1 ]"), ofWFC(enceladus, "[ 1 ]"), ofWFC(end, "[ 5 ]"), ofWFC(glacio, "[ 6 ]"));
         event.addItemTooltip("gtceu:raw_pentlandite", ofWFC(overworld, "[ 12, 3 ]"), ofWFC(mercury, "[ 2 ]"), ofWFC(pluto, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_pitchblende", ofWFC(moon, "[ 4 ]"), ofWFC(pluto, "[ 5 ]"), ofWFC(titan, "[ 4 ]"), ofWFC(end, "[ 6 ]"));
         event.addItemTooltip("gtceu:raw_platinum", ofWFC(mars, "[ 1 ]"), ofWFC(enceladus, "[ 1 ]"), ofWFC(end, "[ 5 ]"), ofWFC(glacio, "[ 6 ]"));
         event.addItemTooltip(
            "gtceu:raw_plutonium", ofWFC(moon, "[ 1 ]"), ofWFC(io, "[ 2 ]"), ofWFC(pluto, "[ 1, 2 ]"), ofWFC(end, "[ 1 ]"), ofWFC(barnarda, "[ 1, 4 ]")
         );
         event.addItemTooltip("gtceu:raw_pollucite", ofWFC(overworld, "[ 18 ]"));
         event.addItemTooltip("gtceu:raw_powellite", ofWFC(reactor, "[ 8 ]"), ofWFC(nether, "[ 6 ]"), ofWFC(ceres, "[ 2 ]"), ofWFC(glacio, "[ 3 ]"));
         event.addItemTooltip(
            "gtceu:raw_pyrite",
            ofWFC(overworld, "[ 6 ]"),
            ofWFC(reactor, "[ 6 ]"),
            ofWFC(venus, "[ 2 ]"),
            ofWFC(nether, "[ 12 ]"),
            ofWFC(io, "[ 4 ]"),
            ofWFC(ganymede, "[ 4 ]"),
            ofWFC(glacio, "[ 2 ]"),
            ofWFC(barnarda, "[ 6 ]")
         );
         event.addItemTooltip("gtceu:raw_pyrochlore", ofWFC(mars, "[ 3 ]"), ofWFC(enceladus, "[ 3 ]"), ofWFC(barnarda, "[ 7 ]"));
         event.addItemTooltip("gtceu:raw_pyrolusite", ofWFC(overworld, "[ 4 ]"), ofWFC(nether, "[ 5 ]"), ofWFC(ganymede, "[ 5 ]"), ofWFC(titan, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_pyrope", ofWFC(overworld, "[ 14 ]"));
         event.addItemTooltip(
            "gtceu:raw_quartzite",
            ofWFC(reactor, "[ 2, 5 ]"),
            ofWFC(nether, "[ 11, 7 ]"),
            ofWFC(ceres, "[ 1 ]"),
            ofWFC(ganymede, "[ 2 ]"),
            ofWFC(barnarda, "[ 3 ]")
         );
         event.addItemTooltip("gtceu:raw_realgar", ofWFC(overworld, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_red_garnet", ofWFC(overworld, "[ 19 ]"));
         event.addItemTooltip("gtceu:raw_redstone", ofWFC(overworld, "[ 9 ]"), ofWFC(nether, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_rock_salt", ofWFC(overworld, "[ 21 ]"));
         event.addItemTooltip("gtceu:raw_ruby", ofWFC(overworld, "[ 9 ]"), ofWFC(nether, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_salt", ofWFC(overworld, "[ 21 ]"));
         event.addItemTooltip("gtceu:raw_saltpeter", ofWFC(reactor, "[ 4 ]"), ofWFC(nether, "[ 3 ]"), ofWFC(titan, "[ 1 ]"), ofWFC(glacio, "[ 1 ]"));
         event.addItemTooltip("gtceu:raw_sapphire", ofWFC(overworld, "[ 14 ]"));
         event.addItemTooltip("gtceu:raw_scheelite", ofWFC(mars, "[ 2 ]"), ofWFC(end, "[ 3 ]"), ofWFC(glacio, "[ 7 ]"), ofWFC(barnarda, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_silver", ofWFC(overworld, "[ 7 ]"));
         event.addItemTooltip("gtceu:raw_soapstone", ofWFC(overworld, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_sodalite", ofWFC(overworld, "[ 17 ]"));
         event.addItemTooltip("gtceu:raw_spessartine", ofWFC(overworld, "[ 4 ]"));
         event.addItemTooltip(
            "gtceu:raw_sphalerite",
            ofWFC(reactor, "[ 6 ]"),
            ofWFC(venus, "[ 2 ]"),
            ofWFC(nether, "[ 12 ]"),
            ofWFC(io, "[ 4 ]"),
            ofWFC(ganymede, "[ 4 ]"),
            ofWFC(glacio, "[ 2 ]"),
            ofWFC(barnarda, "[ 6 ]")
         );
         event.addItemTooltip("gtceu:raw_spodumene", ofWFC(overworld, "[ 21 ]"));
         event.addItemTooltip("gtceu:raw_stibnite", ofWFC(reactor, "[ 7 ]"), ofWFC(nether, "[ 8 ]"), ofWFC(pluto, "[ 3 ]"));
         event.addItemTooltip(
            "gtceu:raw_sulfur",
            ofWFC(reactor, "[ 6 ]"),
            ofWFC(venus, "[ 2 ]"),
            ofWFC(nether, "[ 12 ]"),
            ofWFC(io, "[ 4 ]"),
            ofWFC(ganymede, "[ 4 ]"),
            ofWFC(glacio, "[ 2 ]"),
            ofWFC(barnarda, "[ 6 ]")
         );
         event.addItemTooltip("gtceu:raw_talc", ofWFC(overworld, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_tantalite", ofWFC(overworld, "[ 4 ]"), ofWFC(nether, "[ 5 ]"), ofWFC(ganymede, "[ 5 ]"), ofWFC(titan, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_tetrahedrite", ofWFC(nether, "[ 8 ]"));
         event.addItemTooltip("gtceu:raw_thorium", ofWFC(moon, "[ 1, 4 ]"), ofWFC(pluto, "[ 1, 5 ]"), ofWFC(titan, "[ 4 ]"), ofWFC(barnarda, "[ 1 ]"));
         event.addItemTooltip("gtceu:raw_tin", ofWFC(overworld, "[ 8 ]"));
         event.addItemTooltip("gtceu:raw_topaz", ofWFC(reactor, "[ 1 ]"), ofWFC(nether, "[ 10 ]"), ofWFC(ganymede, "[ 1 ]"), ofWFC(barnarda, "[ 2 ]"));
         event.addItemTooltip(
            "gtceu:raw_tricalcium_phosphate", ofWFC(overworld, "[ 10 ]"), ofWFC(mars, "[ 3 ]"), ofWFC(enceladus, "[ 3 ]"), ofWFC(barnarda, "[ 7 ]")
         );
         event.addItemTooltip("gtceu:raw_trona", ofWFC(io, "[ 1 ]"), ofWFC(glacio, "[ 4 ]"));
         event.addItemTooltip("gtceu:raw_tungstate", ofWFC(mars, "[ 2 ]"), ofWFC(end, "[ 3 ]"), ofWFC(glacio, "[ 7 ]"), ofWFC(barnarda, "[ 5 ]"));
         event.addItemTooltip(
            "gtceu:raw_uraninite", ofWFC(moon, "[ 1, 4 ]"), ofWFC(pluto, "[ 1, 5 ]"), ofWFC(titan, "[ 4 ]"), ofWFC(end, "[ 6 ]"), ofWFC(barnarda, "[ 1 ]")
         );
         event.addItemTooltip("gtceu:raw_vanadium_magnetite", ofWFC(overworld, "[ 16 ]"), ofWFC(end, "[ 2 ]"));
         event.addItemTooltip("gtceu:raw_wulfenite", ofWFC(reactor, "[ 8 ]"), ofWFC(nether, "[ 6 ]"), ofWFC(ceres, "[ 2 ]"), ofWFC(glacio, "[ 3 ]"));
         event.addItemTooltip("gtceu:raw_yellow_garnet", ofWFC(overworld, "[ 19 ]"));
         event.addItemTooltip("gtceu:raw_yellow_limonite", ofWFC(overworld, "[ 2 ]"), ofWFC(nether, "[ 9 ]"));
         event.addItemTooltip("gtceu:raw_zeolite", ofWFC(overworld, "[ 5 ]"));
         event.addItemTooltip("gtceu:raw_zircon", ofWFC(ganymede, "[ 5 ]"), ofWFC(titan, "[ 2 ]"));
         event.addItemTooltip("minecraft:raw_copper", ofWFC(overworld, "[ 6 ]"), ofWFC(reactor, "[ 7 ]"), ofWFC(nether, "[ 8 ]"), ofWFC(pluto, "[ 3 ]"));
         event.addItemTooltip(
            "minecraft:raw_gold", ofWFC(overworld, "[ 16 ]"), ofWFC(nether, "[ 9 ]"), ofWFC(ceres, "[ 4 ]"), ofWFC(end, "[ 2 ]"), ofWFC(glacio, "[ 9 ]")
         );
         event.addItemTooltip("minecraft:raw_iron", ofWFC(overworld, "[ 6 ]"));
         event.addFluidTooltip("gtceu:benzene", ofWFC(titan, "[ 5 ]"), ofWFC(barnarda, "[ 0 ]"));
         event.addFluidTooltip("gtceu:chlorine", ofWFC(enceladus, "[ 4 ]"));
         event.addFluidTooltip("gtceu:coal_gas", ofWFC(io, "[ 5 ]"));
         event.addFluidTooltip("gtceu:deuterium", ofWFC(mercury, "[ 3 ]"));
         event.addFluidTooltip("gtceu:fluorine", ofWFC(enceladus, "[ 5 ]"), ofWFC(titan, "[ 7 ]"));
         event.addFluidTooltip("gtceu:helium_3", ofWFC(moon, "[ 5 ]"));
         event.addFluidTooltip("gtceu:helium", ofWFC(moon, "[ 6 ]"));
         event.addFluidTooltip("gtceu:hydrochloric_acid", ofWFC(ganymede, "[ 6 ]"));
         event.addFluidTooltip("gtceu:krypton", ofWFC(ceres, "[ 6 ]"));
         event.addFluidTooltip("gtceu:methane", ofWFC(titan, "[ 6 ]"));
         event.addFluidTooltip("gtceu:natural_gas", ofWFC(overworld, "[ 29 ]"), ofWFC(nether, "[ 14 ]"));
         event.addFluidTooltip("gtceu:neon", ofWFC(ceres, "[ 5 ]"));
         event.addFluidTooltip("gtceu:nitric_acid", ofWFC(pluto, "[ 6 ]"));
         event.addFluidTooltip("gtceu:oil_heavy", ofWFC(overworld, "[ 25 ]"));
         event.addFluidTooltip("gtceu:oil_light", ofWFC(overworld, "[ 28 ]"));
         event.addFluidTooltip("gtceu:oil_medium", ofWFC(overworld, "[ 26 ]"));
         event.addFluidTooltip("gtceu:oil", ofWFC(overworld, "[ 27 ]"));
         event.addFluidTooltip("gtceu:radon", ofWFC(mars, "[ 4 ]"), ofWFC(ceres, "[ 7 ]"));
         event.addFluidTooltip("gtceu:salt_water", ofWFC(overworld, "[ 24 ]"));
         event.addFluidTooltip("gtceu:sulfuric_acid", ofWFC(venus, "[ 4 ]"));
         event.addFluidTooltip("gtceu:unknowwater", ofWFC(barnarda, "[ 8 ]"));
         event.addFluidTooltip("gtceu:xenon", ofWFC(ceres, "[ 8 ]"));
         event.addFluidTooltip("minecraft:lava", ofWFC(nether, "[ 13 ]"));
      }
   }

   private static Component ofSE(Component mk, String cs) {
      return Component.m_237110_("gtlcore.source_tooltip.space_elevator", new Object[]{mk, Component.m_237113_(cs).m_130940_(ChatFormatting.GRAY)});
   }

   private static void registerSpaceElevatorTooltip(SourceTooltipRegistrationEvent event) {
      Component[] sd = Stream.of(0, 1, 2, 3, 4, 5, 6)
         .map(i -> Component.m_237113_(String.format("MK%s+", i)).m_130940_(ChatFormatting.DARK_AQUA))
         .toArray(Component[]::new);
      event.addItemTooltip("ae2:sky_stone_block", ofSE(sd[1], "[ 6 ]"));
      event.addItemTooltip("gtceu:adamantine_compounds_ore", ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:alien_algae_ore", ofSE(sd[1], "[ 14 ]"));
      event.addItemTooltip("gtceu:almandine_ore", ofSE(sd[1], "[ 2 ]"), ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:aluminium_ore", ofSE(sd[1], "[ 20 ]"), ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:alunite_ore", ofSE(sd[1], "[ 7 ]"));
      event.addItemTooltip("gtceu:amethyst_ore", ofSE(sd[1], "[ 14 ]"), ofSE(sd[2], "[ 25 ]"));
      event.addItemTooltip("gtceu:andesite_platinum_ore", ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:apatite_ore", ofSE(sd[1], "[ 18 ]"));
      event.addItemTooltip("gtceu:asbestos_ore", ofSE(sd[1], "[ 19 ]"));
      event.addItemTooltip("gtceu:barite_ore", ofSE(sd[1], "[ 14 ]"));
      event.addItemTooltip("gtceu:basaltic_mineral_sand_ore", ofSE(sd[1], "[ 15 ]"));
      event.addItemTooltip("gtceu:bastnasite_ore", ofSE(sd[1], "[ 3 ]"), ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:bauxite_ore", ofSE(sd[1], "[ 12, 20 ]"), ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:bentonite_ore", ofSE(sd[1], "[ 1 ]"));
      event.addItemTooltip("gtceu:beryllium_ore", ofSE(sd[1], "[ 8 ]"));
      event.addItemTooltip("gtceu:bloodstone_ore", ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:blue_topaz_ore", ofSE(sd[1], "[ 15 ]"));
      event.addItemTooltip("gtceu:bornite_ore", ofSE(sd[1], "[ 16, 21 ]"));
      event.addItemTooltip("gtceu:calcite_ore", ofSE(sd[1], "[ 10 ]"));
      event.addItemTooltip("gtceu:calorite_ore", ofSE(sd[1], "[ 10 ]"));
      event.addItemTooltip("gtceu:cassiterite_ore", ofSE(sd[1], "[ 6, 13 ]"));
      event.addItemTooltip("gtceu:cassiterite_sand_ore", ofSE(sd[1], "[ 19 ]"));
      event.addItemTooltip("gtceu:celestine_ore", ofSE(sd[2], "[ 25 ]"), ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:certus_quartz_ore", ofSE(sd[1], "[ 13 ]"));
      event.addItemTooltip("gtceu:chalcocite_ore", ofSE(sd[1], "[ 16 ]"));
      event.addItemTooltip("gtceu:chalcopyrite_ore", ofSE(sd[1], "[ 6, 8 ]"));
      event.addItemTooltip("gtceu:chromite_ore", ofSE(sd[1], "[ 24 ]"));
      event.addItemTooltip("gtceu:cinnabar_ore", ofSE(sd[1], "[ 6, 17 ]"));
      event.addItemTooltip("gtceu:coal_ore", ofSE(sd[1], "[ 7, 21 ]"));
      event.addItemTooltip("gtceu:cobalt_ore", ofSE(sd[3], "[ 26 ]"));
      event.addItemTooltip("gtceu:cobaltite_ore", ofSE(sd[1], "[ 22 ]"));
      event.addItemTooltip("gtceu:cooperite_ore", ofSE(sd[1], "[ 21 ]"));
      event.addItemTooltip("gtceu:copper_ore", ofSE(sd[1], "[ 1, 8 ]"));
      event.addItemTooltip("gtceu:diamond_ore", ofSE(sd[1], "[ 21 ]"));
      event.addItemTooltip("gtceu:diatomite_ore", ofSE(sd[1], "[ 7, 19 ]"));
      event.addItemTooltip("gtceu:earth_crystal_ore", ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:electrotine_ore", ofSE(sd[1], "[ 7 ]"));
      event.addItemTooltip("gtceu:emerald_ore", ofSE(sd[1], "[ 8 ]"), ofSE(sd[2], "[ 25 ]"));
      event.addItemTooltip("gtceu:enderium_ore", ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:enriched_naquadah_ore", ofSE(sd[1], "[ 24 ]"), ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:force_ore", ofSE(sd[3], "[ 26 ]"));
      event.addItemTooltip("gtceu:fullers_earth_ore", ofSE(sd[1], "[ 15 ]"));
      event.addItemTooltip("gtceu:galena_ore", ofSE(sd[1], "[ 11 ]"));
      event.addItemTooltip("gtceu:garnet_sand_ore", ofSE(sd[1], "[ 19 ]"));
      event.addItemTooltip("gtceu:garnierite_ore", ofSE(sd[1], "[ 22 ]"));
      event.addItemTooltip("gtceu:glauconite_sand_ore", ofSE(sd[1], "[ 1, 4 ]"));
      event.addItemTooltip("gtceu:goethite_ore", ofSE(sd[1], "[ 3, 12 ]"));
      event.addItemTooltip("gtceu:gold_ore", ofSE(sd[1], "[ 9, 13, 20 ]"));
      event.addItemTooltip("gtceu:granitic_mineral_sand_ore", ofSE(sd[1], "[ 15 ]"));
      event.addItemTooltip("gtceu:graphite_ore", ofSE(sd[1], "[ 21 ]"));
      event.addItemTooltip("gtceu:gravel_ruby_ore", ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:green_sapphire_ore", ofSE(sd[1], "[ 2 ]"), ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:grossular_ore", ofSE(sd[1], "[ 5, 9 ]"));
      event.addItemTooltip("gtceu:gypsum_ore", ofSE(sd[1], "[ 15 ]"));
      event.addItemTooltip("gtceu:hematite_ore", ofSE(sd[1], "[ 3, 13 ]"));
      event.addItemTooltip("gtceu:ignis_crystal_ore", ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:ilmenite_ore", ofSE(sd[1], "[ 20 ]"));
      event.addItemTooltip("gtceu:indium_ore", ofSE(sd[1], "[ 24 ]"));
      event.addItemTooltip("gtceu:infused_gold_ore", ofSE(sd[1], "[ 20 ]"));
      event.addItemTooltip("gtceu:iron_ore", ofSE(sd[1], "[ 8 ]"), ofSE(sd[3], "[ 26 ]"), ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:jasper_ore", ofSE(sd[2], "[ 25 ]"));
      event.addItemTooltip("gtceu:kyanite_ore", ofSE(sd[1], "[ 12 ]"));
      event.addItemTooltip("gtceu:lapis_ore", ofSE(sd[1], "[ 10 ]"), ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:lazurite_ore", ofSE(sd[1], "[ 10 ]"), ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:lead_ore", ofSE(sd[1], "[ 11 ]"));
      event.addItemTooltip("gtceu:lepidolite_ore", ofSE(sd[1], "[ 16 ]"));
      event.addItemTooltip("gtceu:lithium_ore", ofSE(sd[1], "[ 23 ]"));
      event.addItemTooltip("gtceu:magnetite_ore", ofSE(sd[1], "[ 1, 9, 19 ]"));
      event.addItemTooltip("gtceu:malachite_ore", ofSE(sd[1], "[ 3 ]"));
      event.addItemTooltip("gtceu:mica_ore", ofSE(sd[1], "[ 12 ]"));
      event.addItemTooltip("gtceu:mithril_ore", ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:molybdenite_ore", ofSE(sd[1], "[ 11 ]"));
      event.addItemTooltip("gtceu:molybdenum_ore", ofSE(sd[1], "[ 3, 11 ]"));
      event.addItemTooltip("gtceu:monazite_ore", ofSE(sd[1], "[ 4 ]"), ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:naquadah_ore", ofSE(sd[1], "[ 24 ]"), ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:neodymium_ore", ofSE(sd[1], "[ 4 ]"));
      event.addItemTooltip("gtceu:nether_quartz_ore", ofSE(sd[1], "[ 17 ]"));
      event.addItemTooltip("gtceu:nickel_ore", ofSE(sd[1], "[ 22 ]"), ofSE(sd[3], "[ 26 ]"));
      event.addItemTooltip("gtceu:oilsands_ore", ofSE(sd[1], "[ 20 ]"));
      event.addItemTooltip("gtceu:olivine_ore", ofSE(sd[1], "[ 1 ]"));
      event.addItemTooltip("gtceu:opal_ore", ofSE(sd[1], "[ 14 ]"));
      event.addItemTooltip("gtceu:orichalcum_ore", ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:palladium_ore", ofSE(sd[1], "[ 22 ]"));
      event.addItemTooltip("gtceu:pentlandite_ore", ofSE(sd[1], "[ 4, 22 ]"));
      event.addItemTooltip("gtceu:pitchblende_ore", ofSE(sd[1], "[ 23 ]"), ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:platinum_ore", ofSE(sd[1], "[ 22 ]"));
      event.addItemTooltip("gtceu:plutonium_ore", ofSE(sd[1], "[ 24 ]"));
      event.addItemTooltip("gtceu:pollucite_ore", ofSE(sd[1], "[ 12 ]"), ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:powellite_ore", ofSE(sd[1], "[ 11 ]"));
      event.addItemTooltip("gtceu:pyrite_ore", ofSE(sd[1], "[ 8, 18 ]"));
      event.addItemTooltip("gtceu:pyrochlore_ore", ofSE(sd[1], "[ 18 ]"));
      event.addItemTooltip("gtceu:pyrolusite_ore", ofSE(sd[1], "[ 5, 9 ]"));
      event.addItemTooltip("gtceu:pyrope_ore", ofSE(sd[1], "[ 2 ]"), ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:quartzite_ore", ofSE(sd[1], "[ 13, 17 ]"));
      event.addItemTooltip("gtceu:rare_earth_metal_ore", ofSE(sd[4], "[ 28 ]"));
      event.addItemTooltip("gtceu:realgar_ore", ofSE(sd[1], "[ 6 ]"));
      event.addItemTooltip("gtceu:red_garnet_ore", ofSE(sd[1], "[ 14 ]"), ofSE(sd[2], "[ 25 ]"), ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:redstone_ore", ofSE(sd[1], "[ 5, 17 ]"), ofSE(sd[3], "[ 27 ]"));
      event.addItemTooltip("gtceu:rock_salt_ore", ofSE(sd[1], "[ 16 ]"));
      event.addItemTooltip("gtceu:rubidium_ore", ofSE(sd[1], "[ 7 ]"));
      event.addItemTooltip("gtceu:ruby_ore", ofSE(sd[1], "[ 5, 17 ]"));
      event.addItemTooltip("gtceu:salt_ore", ofSE(sd[1], "[ 16 ]"), ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:saltpeter_ore", ofSE(sd[1], "[ 7 ]"));
      event.addItemTooltip("gtceu:sapphire_ore", ofSE(sd[1], "[ 2 ]"), ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:scheelite_ore", ofSE(sd[1], "[ 23 ]"));
      event.addItemTooltip("gtceu:silver_ore", ofSE(sd[1], "[ 11 ]"), ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:soapstone_ore", ofSE(sd[1], "[ 4 ]"));
      event.addItemTooltip("gtceu:sodalite_ore", ofSE(sd[1], "[ 10 ]"), ofSE(sd[5], "[ 30 ]"));
      event.addItemTooltip("gtceu:spessartine_ore", ofSE(sd[1], "[ 5 ]"));
      event.addItemTooltip("gtceu:sphalerite_ore", ofSE(sd[1], "[ 18 ]"));
      event.addItemTooltip("gtceu:spodumene_ore", ofSE(sd[1], "[ 16 ]"));
      event.addItemTooltip("gtceu:starmetal_ore", ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:stibnite_ore", ofSE(sd[1], "[ 2 ]"));
      event.addItemTooltip("gtceu:sulfur_ore", ofSE(sd[1], "[ 18 ]"));
      event.addItemTooltip("gtceu:talc_ore", ofSE(sd[1], "[ 4 ]"));
      event.addItemTooltip("gtceu:tantalite_ore", ofSE(sd[1], "[ 5, 9 ]"));
      event.addItemTooltip("gtceu:tartarite_ore", ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:tellurium_ore", ofSE(sd[1], "[ 23 ]"));
      event.addItemTooltip("gtceu:tetrahedrite_ore", ofSE(sd[1], "[ 1 ]"));
      event.addItemTooltip("gtceu:tin_ore", ofSE(sd[3], "[ 26 ]"));
      event.addItemTooltip("gtceu:titanium_ore", ofSE(sd[1], "[ 21 ]"));
      event.addItemTooltip("gtceu:topaz_ore", ofSE(sd[1], "[ 15 ]"), ofSE(sd[2], "[ 25 ]"));
      event.addItemTooltip("gtceu:tricalcium_phosphate_ore", ofSE(sd[1], "[ 18 ]"));
      event.addItemTooltip("gtceu:trinium_compound_ore", ofSE(sd[1], "[ 24 ]"));
      event.addItemTooltip("gtceu:tungstate_ore", ofSE(sd[1], "[ 23 ]"));
      event.addItemTooltip("gtceu:tungsten_ore", ofSE(sd[1], "[ 23 ]"));
      event.addItemTooltip("gtceu:uraninite_ore", ofSE(sd[1], "[ 2 ]"), ofSE(sd[4], "[ 29 ]"));
      event.addItemTooltip("gtceu:uruium_ore", ofSE(sd[3], "[ 26 ]"));
      event.addItemTooltip("gtceu:vanadium_magnetite_ore", ofSE(sd[1], "[ 9, 19 ]"));
      event.addItemTooltip("gtceu:vibranium_ore", ofSE(sd[5], "[ 31 ]"));
      event.addItemTooltip("gtceu:wulfenite_ore", ofSE(sd[1], "[ 10 ]"));
      event.addItemTooltip("gtceu:yellow_garnet_ore", ofSE(sd[1], "[ 14 ]"), ofSE(sd[6], "[ 32 ]"));
      event.addItemTooltip("gtceu:yellow_limonite_ore", ofSE(sd[1], "[ 3, 12 ]"));
      event.addItemTooltip("gtceu:zeolite_ore", ofSE(sd[1], "[ 6 ]"));
      event.addItemTooltip("gtceu:zircon_ore", ofSE(sd[1], "[ 13 ]"));
      event.addItemTooltip("minecraft:ancient_debris", ofSE(sd[1], "[ 17 ]"));
      event.addFluidTooltip("gtceu:ammonia", ofSE(sd[1], "[ 8 ]"));
      event.addFluidTooltip("gtceu:argon", ofSE(sd[2], "[ 15 ]"));
      event.addFluidTooltip("gtceu:barnarda_air", ofSE(sd[3], "[ 28 ]"));
      event.addFluidTooltip("gtceu:black_dwarf_mtter", ofSE(sd[6], "[ 30 ]"));
      event.addFluidTooltip("gtceu:bromine", ofSE(sd[3], "[ 27 ]"));
      event.addFluidTooltip("gtceu:carbon_dioxide", ofSE(sd[1], "[ 6 ]"));
      event.addFluidTooltip("gtceu:carbon_monoxide", ofSE(sd[1], "[ 11 ]"));
      event.addFluidTooltip("gtceu:chlorine", ofSE(sd[1], "[ 9 ]"));
      event.addFluidTooltip("gtceu:coal_gas", ofSE(sd[3], "[ 26 ]"));
      event.addFluidTooltip("gtceu:deuterium", ofSE(sd[3], "[ 20 ]"));
      event.addFluidTooltip("gtceu:fluorine", ofSE(sd[1], "[ 10 ]"));
      event.addFluidTooltip("gtceu:heavy_fuel", ofSE(sd[3], "[ 22 ]"));
      event.addFluidTooltip("gtceu:helium_3", ofSE(sd[2], "[ 19 ]"));
      event.addFluidTooltip("gtceu:helium", ofSE(sd[1], "[ 2 ]"));
      event.addFluidTooltip("gtceu:hydrogen", ofSE(sd[1], "[ 1 ]"));
      event.addFluidTooltip("gtceu:krypton", ofSE(sd[2], "[ 16 ]"));
      event.addFluidTooltip("gtceu:light_fuel", ofSE(sd[3], "[ 23 ]"));
      event.addFluidTooltip("gtceu:methane", ofSE(sd[1], "[ 4 ]"));
      event.addFluidTooltip("gtceu:naphtha", ofSE(sd[3], "[ 24 ]"));
      event.addFluidTooltip("gtceu:neon", ofSE(sd[2], "[ 14 ]"));
      event.addFluidTooltip("gtceu:nitrogen_dioxide", ofSE(sd[1], "[ 7 ]"));
      event.addFluidTooltip("gtceu:nitrogen", ofSE(sd[1], "[ 3 ]"));
      event.addFluidTooltip("gtceu:oxygen", ofSE(sd[1], "[ 12 ]"));
      event.addFluidTooltip("gtceu:radon", ofSE(sd[2], "[ 18 ]"));
      event.addFluidTooltip("gtceu:refinery_gas", ofSE(sd[3], "[ 25 ]"));
      event.addFluidTooltip("gtceu:sulfur_dioxide", ofSE(sd[1], "[ 5 ]"));
      event.addFluidTooltip("gtceu:tritium", ofSE(sd[3], "[ 21 ]"));
      event.addFluidTooltip("gtceu:unknowwater", ofSE(sd[2], "[ 13 ]"));
      event.addFluidTooltip("gtceu:white_dwarf_mtter", ofSE(sd[6], "[ 29 ]"));
      event.addFluidTooltip("gtceu:xenon", ofSE(sd[2], "[ 17 ]"));
   }

   private static Component ofSeries(Component component) {
      return Component.m_237110_("gtlcore.source_tooltip.series", new Object[]{component}).m_130940_(ChatFormatting.LIGHT_PURPLE);
   }

   private static void registerSeriesTooltips(SourceTooltipRegistrationEvent event) {
      String[] rare_earth_centrifugal_materials = new String[]{
         "lanthanum",
         "cerium",
         "neodymium",
         "promethium",
         "samarium",
         "europium",
         "praseodymium",
         "gadolinium",
         "terbium",
         "dysprosium",
         "holmium",
         "erbium",
         "thulium",
         "ytterbium",
         "scandium",
         "lutetium",
         "yttrium"
      };
      Component rare_earth_centrifugal_text = ofSeries(Component.m_237115_("gtceu.rare_earth_centrifugal"));

      for (String material : rare_earth_centrifugal_materials) {
         event.addItemTooltip(String.format("gtceu:small_%s_dust", material), rare_earth_centrifugal_text);
         event.addItemTooltip(String.format("gtceu:%s_dust", material), rare_earth_centrifugal_text);
         event.addItemTooltip(String.format("gtceu:small_%s_oxide_dust", material), rare_earth_centrifugal_text);
         event.addItemTooltip(String.format("gtceu:%s_oxide_dust", material), rare_earth_centrifugal_text);
      }

      String[] platinum_group = new String[]{
         "gtceu:platinum_dust", "gtceu:palladium_dust", "gtceu:ruthenium_dust", "gtceu:iridium_dust", "gtceu:rhodium_dust", "gtceu:osmium_dust"
      };
      Component platinum_group_precess_text = ofSeries(Component.m_237115_("gtlcore.source_tooltip.platinum_group_precess"));

      for (String e : platinum_group) {
         event.addItemTooltip(e, platinum_group_precess_text);
      }

      String[][] fusion_reactor_productions = new String[][]{
         {
               "gtceu:helium_plasma",
               "gtceu:plutonium",
               "gtceu:uranium",
               "gtceu:uranium_235",
               "gtceu:europium",
               "gtceu:chromium",
               "gtceu:duranium",
               "gtceu:osmium",
               "gtceu:lutetium"
         },
         {
               "gtceu:radon",
               "gtceu:oxygen_plasma",
               "gtceu:argon_plasma",
               "gtceu:nitrogen_plasma",
               "gtceu:tritanium",
               "gtceu:plutonium_241",
               "gtceu:indium",
               "gtceu:americium",
               "gtceu:darmstadtium"
         },
         {
               "gtceu:orichalcum_plasma",
               "gtceu:neutronium",
               "gtceu:naquadria",
               "gtceu:mithril_plasma",
               "gtceu:iron_plasma",
               "gtceu:silver_plasma",
               "gtceu:nickel_plasma"
         },
         {
               "gtceu:metastable_hassium_plasma",
               "gtceu:taranium_rich_liquid_helium_4_plasma",
               "gtceu:moscovium",
               "gtceu:tennessine",
               "gtceu:livermorium",
               "gtceu:dubnium",
               "gtceu:seaborgium",
               "gtceu:hot_oganesson",
               "gtceu:plutonium_241_plasma"
         },
         {"gtceu:vibranium_plasma", "gtceu:infinity", "gtceu:draconiumawakened_plasma"}
      };
      MutableComponent fusion_reactor_text = Component.m_237115_("gtceu.fusion_reactor");

      for (int i = 0; i < fusion_reactor_productions.length; i++) {
         Component fusion_reactor_mk_text = ofSeries(fusion_reactor_text.m_6881_().m_7220_(Component.m_237113_(String.format(" MK%s+", i + 1))));

         for (String e : fusion_reactor_productions[i]) {
            event.addFluidTooltip(e, fusion_reactor_mk_text);
         }
      }

      String[] super_particle_collider_productions = new String[]{
         "gtceu:antineutron",
         "gtceu:positive_electron",
         "gtceu:antiproton",
         "gtceu:astatine",
         "gtceu:copernicium",
         "gtceu:mendelevium",
         "gtceu:plutonium",
         "gtceu:uranium",
         "gtceu:lawrencium",
         "gtceu:californium",
         "gtceu:curium",
         "gtceu:nobelium",
         "gtceu:berkelium",
         "gtceu:einsteinium",
         "gtceu:fermium",
         "gtceu:neptunium",
         "gtceu:nihonium",
         "gtceu:roentgenium",
         "gtceu:bohrium",
         "gtceu:antimatter"
      };
      Component super_particle_collider_text = ofSeries(Component.m_237115_("gtceu.super_particle_collider"));

      for (String e : super_particle_collider_productions) {
         event.addFluidTooltip(e, super_particle_collider_text);
      }

      String[] alloy_smelter_ingots = new String[]{
         "gtceu:bronze_ingot",
         "gtceu:cupronickel_ingot",
         "gtceu:tin_alloy_ingot",
         "gtceu:invar_ingot",
         "gtceu:brass_ingot",
         "gtceu:electrum_ingot",
         "gtceu:battery_alloy_ingot",
         "gtceu:red_alloy_ingot",
         "gtceu:blue_alloy_ingot",
         "gtceu:magnalium_ingot",
         "gtceu:pulsating_alloy_ingot",
         "gtceu:conductive_alloy_ingot",
         "ad_astra:calorite_ingot"
      };
      Component alloy_smelter_text = ofSeries(Component.m_237115_("gtceu.alloy_smelter"));

      for (String e : alloy_smelter_ingots) {
         event.addItemTooltip(e, alloy_smelter_text);
      }
   }
}
