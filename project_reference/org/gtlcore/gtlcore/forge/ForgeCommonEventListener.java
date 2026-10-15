package org.gtlcore.gtlcore.forge;

import dev.latvian.mods.kubejs.KubeJS;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.level.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.event.level.BlockEvent.PortalSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.MissingMappingsEvent;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.Registries;

@EventBusSubscriber(modid = "gtlcore", bus = Bus.FORGE)
public class ForgeCommonEventListener {
   @SubscribeEvent
   public static void onPortalSpawnEvent(PortalSpawnEvent event) {
      event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onRightClickBlock(RightClickBlock event) {
      if (event.getEntity().m_9236_().m_8055_(event.getPos()).m_60734_() == Blocks.f_50258_
         && event.getEntity().m_21120_(event.getHand()).m_41720_() == Items.f_42545_) {
         if (event.getEntity() instanceof ServerPlayer player && Objects.equals(Registries.getItemId(player.m_21206_()), "kubejs:end_data")) {
            player.m_21206_().m_41764_(player.m_21206_().m_41613_() - 1);
            return;
         }

         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onFarmlandTrampleEvent(FarmlandTrampleEvent event) {
      event.setCanceled(true);
   }

   @SubscribeEvent
   public static void onEnderTeleportEvent(EnderEntity event) {
      if (event.getEntity() instanceof EnderMan || event.getEntity() instanceof Shulker) {
         event.setCanceled(true);
      }
   }

   @SubscribeEvent
   public static void onPlayerTickEvent(PlayerTickEvent event) {
      if (ConfigHolder.INSTANCE.disableDrift
         && event.phase == Phase.END
         && event.side == LogicalSide.CLIENT
         && event.player.f_20900_ == 0.0F
         && event.player.f_20902_ == 0.0F) {
         event.player.m_20256_(event.player.m_20184_().m_82542_(0.5, 1.0, 0.5));
      }
   }

   @SubscribeEvent
   public static void remapIds(MissingMappingsEvent event) {
      event.getMappings(net.minecraft.core.registries.Registries.f_256747_, "kubejs").forEach(mapping -> {
         if (mapping.getKey().equals(KubeJS.id("multi_functional_casing"))) {
            mapping.remap((Block)GTLBlocks.MULTI_FUNCTIONAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("create_casing"))) {
            mapping.remap((Block)GTLBlocks.CREATE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("space_elevator_mechanical_casing"))) {
            mapping.remap((Block)GTLBlocks.SPACE_ELEVATOR_MECHANICAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("manipulator"))) {
            mapping.remap((Block)GTLBlocks.MANIPULATOR.get());
         }

         if (mapping.getKey().equals(KubeJS.id("blaze_blast_furnace_casing"))) {
            mapping.remap((Block)GTLBlocks.BLAZE_BLAST_FURNACE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("cold_ice_casing"))) {
            mapping.remap((Block)GTLBlocks.COLD_ICE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("dimension_connection_casing"))) {
            mapping.remap((Block)GTLBlocks.DIMENSION_CONNECTION_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("molecular_casing"))) {
            mapping.remap((Block)GTLBlocks.MOLECULAR_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("dimension_injection_casing"))) {
            mapping.remap((Block)GTLBlocks.DIMENSION_INJECTION_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("dimensionally_transcendent_casing"))) {
            mapping.remap((Block)GTLBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("echo_casing"))) {
            mapping.remap((Block)GTLBlocks.ECHO_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("dragon_strength_tritanium_casing"))) {
            mapping.remap((Block)GTLBlocks.DRAGON_STRENGTH_TRITANIUM_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("aluminium_bronze_casing"))) {
            mapping.remap((Block)GTLBlocks.ALUMINIUM_BRONZE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("antifreeze_heatproof_machine_casing"))) {
            mapping.remap((Block)GTLBlocks.ANTIFREEZE_HEATPROOF_MACHINE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("enhance_hyper_mechanical_casing"))) {
            mapping.remap((Block)GTLBlocks.ENHANCE_HYPER_MECHANICAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("extreme_strength_tritanium_casing"))) {
            mapping.remap((Block)GTLBlocks.EXTREME_STRENGTH_TRITANIUM_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("graviton_field_constraint_casing"))) {
            mapping.remap((Block)GTLBlocks.GRAVITON_FIELD_CONSTRAINT_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("hyper_mechanical_casing"))) {
            mapping.remap((Block)GTLBlocks.HYPER_MECHANICAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("iridium_casing"))) {
            mapping.remap((Block)GTLBlocks.IRIDIUM_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("lafium_mechanical_casing"))) {
            mapping.remap((Block)GTLBlocks.LAFIUM_MECHANICAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("oxidation_resistant_hastelloy_n_mechanical_casing"))) {
            mapping.remap((Block)GTLBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("pikyonium_machine_casing"))) {
            mapping.remap((Block)GTLBlocks.PIKYONIUM_MACHINE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("sps_casing"))) {
            mapping.remap((Block)GTLBlocks.SPS_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("naquadah_alloy_casing"))) {
            mapping.remap((Block)GTLBlocks.NAQUADAH_ALLOY_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("process_machine_casing"))) {
            mapping.remap((Block)GTLBlocks.PROCESS_MACHINE_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("fission_reactor_casing"))) {
            mapping.remap((Block)GTLBlocks.FISSION_REACTOR_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("degenerate_rhenium_constrained_casing"))) {
            mapping.remap((Block)GTLBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get());
         }

         if (mapping.getKey().equals(KubeJS.id("infinity_glass"))) {
            mapping.remap((Block)GTLBlocks.INFINITY_GLASS.get());
         }

         if (mapping.getKey().equals(KubeJS.id("rhenium_reinforced_energy_glass"))) {
            mapping.remap((Block)GTLBlocks.RHENIUM_REINFORCED_ENERGY_GLASS.get());
         }

         if (mapping.getKey().equals(KubeJS.id("hsss_reinforced_borosilicate_glass"))) {
            mapping.remap((Block)GTLBlocks.HSSS_REINFORCED_BOROSILICATE_GLASS.get());
         }
      });
      event.getMappings(net.minecraft.core.registries.Registries.f_256913_, "infinitycells").forEach(mapping -> {
         if (mapping.getKey().equals(new ResourceLocation("infinitycells:infinity_cell"))) {
            mapping.remap((Item)GTLItems.ITEM_INFINITY_CELL.get());
         }

         if (mapping.getKey().equals(new ResourceLocation("infinitycells:infinity_fluid_cell"))) {
            mapping.remap((Item)GTLItems.FLUID_INFINITY_CELL.get());
         }
      });
   }
}
