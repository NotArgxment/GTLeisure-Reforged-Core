package org.gtlcore.gtlcore.common.data;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.CraftingUnitBlock;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.blockentity.crafting.CraftingBlockEntity;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTModels;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.registries.GTLRegistration;
import org.gtlcore.gtlcore.common.block.BlockMap;
import org.gtlcore.gtlcore.common.block.CleanroomFilterType;
import org.gtlcore.gtlcore.common.block.CraftingUnitType;
import org.gtlcore.gtlcore.common.block.GTLFusionCasingBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GTLBlocks {
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_1M = registerCraftingUnitBlock(1, CraftingUnitType.STORAGE_1M);
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_4M = registerCraftingUnitBlock(4, CraftingUnitType.STORAGE_4M);
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_16M = registerCraftingUnitBlock(16, CraftingUnitType.STORAGE_16M);
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_64M = registerCraftingUnitBlock(64, CraftingUnitType.STORAGE_64M);
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_256M = registerCraftingUnitBlock(256, CraftingUnitType.STORAGE_256M);
   public static final BlockEntry<CraftingUnitBlock> CRAFTING_STORAGE_MAX = registerCraftingUnitBlock(-1, CraftingUnitType.STORAGE_MAX);
   public static BlockEntityEntry<CraftingBlockEntity> CRAFTING_STORAGE = ((BlockEntityBuilder)GTLRegistration.REGISTRATE
         .blockEntity("crafting_storage", CraftingBlockEntity::new)
         .validBlocks(
            new NonNullSupplier[]{
               CRAFTING_STORAGE_1M, CRAFTING_STORAGE_4M, CRAFTING_STORAGE_16M, CRAFTING_STORAGE_64M, CRAFTING_STORAGE_256M, CRAFTING_STORAGE_MAX
            }
         )
         .onRegister(type -> {
            for (CraftingUnitType craftingUnitType : CraftingUnitType.values()) {
               AEBaseBlockEntity.registerBlockEntityItem(type, craftingUnitType.getItemFromType());
               ((CraftingUnitBlock)craftingUnitType.getDefinition().get()).setBlockEntity(CraftingBlockEntity.class, type, null, null);
            }
         }))
      .register();
   public static final BlockEntry<Block> HERMETIC_CASING_UEV = createHermeticCasing(10);
   public static final BlockEntry<Block> HERMETIC_CASING_UIV = createHermeticCasing(11);
   public static final BlockEntry<Block> HERMETIC_CASING_UXV = createHermeticCasing(12);
   public static final BlockEntry<Block> HERMETIC_CASING_OpV = createHermeticCasing(13);
   public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK4 = createFusionCasing(GTLFusionCasingBlock.CasingType.FUSION_CASING_MK4);
   public static final BlockEntry<FusionCasingBlock> FUSION_CASING_MK5 = createFusionCasing(GTLFusionCasingBlock.CasingType.FUSION_CASING_MK5);
   public static final BlockEntry<ActiveBlock> ADVANCED_FUSION_COIL = createActiveCasing("advanced_fusion_coil", "block/variant/advanced_fusion_coil");
   public static final BlockEntry<ActiveBlock> FUSION_COIL_MK2 = createActiveCasing("fusion_coil_mk2", "block/variant/fusion_coil_mk2");
   public static final BlockEntry<ActiveBlock> IMPROVED_SUPERCONDUCTOR_COIL = createActiveCasing(
      "improved_superconductor_coil", "block/variant/improved_superconductor_coil"
   );
   public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL = createActiveCasing("compressed_fusion_coil", "block/variant/compressed_fusion_coil");
   public static final BlockEntry<ActiveBlock> ADVANCED_COMPRESSED_FUSION_COIL = createActiveCasing(
      "advanced_compressed_fusion_coil", "block/variant/advanced_compressed_fusion_coil"
   );
   public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2_PROTOTYPE = createActiveCasing(
      "compressed_fusion_coil_mk2_prototype", "block/variant/compressed_fusion_coil_mk2_prototype"
   );
   public static final BlockEntry<ActiveBlock> COMPRESSED_FUSION_COIL_MK2 = createActiveCasing(
      "compressed_fusion_coil_mk2", "block/variant/compressed_fusion_coil_mk2"
   );
   public static final BlockEntry<Block> FILTER_CASING_LAW = createCleanroomFilter(CleanroomFilterType.FILTER_CASING_LAW);
   public static final BlockEntry<Block> CASING_SUPERCRITICAL_TURBINE = createCasingBlock(
      "supercritical_turbine_casing", GTLCore.id("block/supercritical_turbine_casing")
   );
   public static final BlockEntry<Block> MULTI_FUNCTIONAL_CASING = createCasingBlock("multi_functional_casing", GTLCore.id("block/multi_functional_casing"));
   public static final BlockEntry<Block> CREATE_CASING = createCasingBlock("create_casing", GTLCore.id("block/create_casing"));
   public static final BlockEntry<Block> SPACE_ELEVATOR_MECHANICAL_CASING = createCasingBlock(
      "space_elevator_mechanical_casing", GTLCore.id("block/space_elevator_mechanical_casing")
   );
   public static final BlockEntry<Block> MANIPULATOR = createCasingBlock("manipulator", GTLCore.id("block/manipulator"));
   public static final BlockEntry<Block> BLAZE_BLAST_FURNACE_CASING = createCasingBlock(
      "blaze_blast_furnace_casing", GTLCore.id("block/blaze_blast_furnace_casing")
   );
   public static final BlockEntry<Block> COLD_ICE_CASING = createCasingBlock("cold_ice_casing", GTLCore.id("block/cold_ice_casing"));
   public static final BlockEntry<Block> DIMENSION_CONNECTION_CASING = createCasingBlock(
      "dimension_connection_casing", GTLCore.id("block/dimension_connection_casing")
   );
   public static final BlockEntry<Block> MOLECULAR_CASING = createCasingBlock("molecular_casing", GTLCore.id("block/molecular_casing"));
   public static final BlockEntry<Block> DIMENSION_INJECTION_CASING = createCasingBlock(
      "dimension_injection_casing", GTLCore.id("block/casings/dimension_injection_casing")
   );
   public static final BlockEntry<Block> DIMENSIONALLY_TRANSCENDENT_CASING = createCasingBlock(
      "dimensionally_transcendent_casing", GTLCore.id("block/casings/dimensionally_transcendent_casing")
   );
   public static final BlockEntry<Block> ECHO_CASING = createCasingBlock("echo_casing", GTLCore.id("block/casings/echo_casing"));
   public static final BlockEntry<Block> DRAGON_STRENGTH_TRITANIUM_CASING = createCasingBlock(
      "dragon_strength_tritanium_casing", GTLCore.id("block/casings/extreme_strength_tritanium_casing")
   );
   public static final BlockEntry<Block> ALUMINIUM_BRONZE_CASING = createCasingBlock(
      "aluminium_bronze_casing", GTLCore.id("block/casings/aluminium_bronze_casing")
   );
   public static final BlockEntry<Block> ANTIFREEZE_HEATPROOF_MACHINE_CASING = createCasingBlock(
      "antifreeze_heatproof_machine_casing", GTLCore.id("block/casings/antifreeze_heatproof_machine_casing")
   );
   public static final BlockEntry<Block> ENHANCE_HYPER_MECHANICAL_CASING = createCasingBlock(
      "enhance_hyper_mechanical_casing", GTLCore.id("block/casings/enhance_hyper_mechanical_casing")
   );
   public static final BlockEntry<Block> EXTREME_STRENGTH_TRITANIUM_CASING = createCasingBlock(
      "extreme_strength_tritanium_casing", GTLCore.id("block/casings/extreme_strength_tritanium_casing")
   );
   public static final BlockEntry<Block> GRAVITON_FIELD_CONSTRAINT_CASING = createCasingBlock(
      "graviton_field_constraint_casing", GTLCore.id("block/casings/graviton_field_constraint_casing")
   );
   public static final BlockEntry<Block> HYPER_MECHANICAL_CASING = createCasingBlock(
      "hyper_mechanical_casing", GTLCore.id("block/casings/hyper_mechanical_casing")
   );
   public static final BlockEntry<Block> IRIDIUM_CASING = createCasingBlock("iridium_casing", GTLCore.id("block/casings/iridium_casing"));
   public static final BlockEntry<Block> LAFIUM_MECHANICAL_CASING = createCasingBlock(
      "lafium_mechanical_casing", GTLCore.id("block/casings/lafium_mechanical_casing")
   );
   public static final BlockEntry<Block> OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING = createCasingBlock(
      "oxidation_resistant_hastelloy_n_mechanical_casing", GTLCore.id("block/casings/oxidation_resistant_hastelloy_n_mechanical_casing")
   );
   public static final BlockEntry<Block> PIKYONIUM_MACHINE_CASING = createCasingBlock(
      "pikyonium_machine_casing", GTLCore.id("block/casings/pikyonium_machine_casing")
   );
   public static final BlockEntry<Block> SPS_CASING = createCasingBlock("sps_casing", GTLCore.id("block/casings/sps_casing"));
   public static final BlockEntry<Block> NAQUADAH_ALLOY_CASING = createCasingBlock("naquadah_alloy_casing", GTLCore.id("block/casings/hyper_mechanical_casing"));
   public static final BlockEntry<Block> PROCESS_MACHINE_CASING = createCasingBlock(
      "process_machine_casing", GTLCore.id("block/casings/process_machine_casing")
   );
   public static final BlockEntry<Block> FISSION_REACTOR_CASING = createCasingBlock(
      "fission_reactor_casing", GTLCore.id("block/casings/fission_reactor_casing")
   );
   public static final BlockEntry<Block> DEGENERATE_RHENIUM_CONSTRAINED_CASING = createCasingBlock(
      "degenerate_rhenium_constrained_casing", GTLCore.id("block/casings/degenerate_rhenium_constrained_casing")
   );
   public static final BlockEntry<Block> INFINITY_GLASS = createGlassCasingBlock(
      "infinity_glass", GTLCore.id("block/casings/infinity_glass"), () -> RenderType::m_110457_
   );
   public static final BlockEntry<Block> RHENIUM_REINFORCED_ENERGY_GLASS = createGlassCasingBlock(
      "rhenium_reinforced_energy_glass", GTLCore.id("block/casings/rhenium_reinforced_energy_glass"), () -> RenderType::m_110457_
   );
   public static final BlockEntry<Block> HSSS_REINFORCED_BOROSILICATE_GLASS = createGlassCasingBlock(
      "hsss_reinforced_borosilicate_glass", GTLCore.id("block/casings/hsss_reinforced_borosilicate_glass"), () -> RenderType::m_110457_
   );
   public static final BlockEntry<ActiveBlock> POWER_CORE = createActiveCasing("power_core", "block/variant/hyper_core");
   public static final BlockEntry<ActiveBlock> HYPER_CORE = createActiveCasing("hyper_core", "block/variant/hyper_core");
   public static final BlockEntry<ActiveBlock> SUPER_COMPUTATION_COMPONENT = createActiveCasing(
      "super_computation_component", "block/variant/super_computation_component"
   );
   public static final BlockEntry<ActiveBlock> SUPER_COOLER_COMPONENT = createActiveCasing("super_cooler_component", "block/variant/super_cooler_component");
   public static final BlockEntry<ActiveBlock> SPACETIMECONTINUUMRIPPER = createActiveCasing(
      "spacetimecontinuumripper", "block/variant/spacetimecontinuumripper"
   );
   public static final BlockEntry<ActiveBlock> SPACETIMEBENDINGCORE = createActiveCasing("spacetimebendingcore", "block/variant/spacetimebendingcore");
   public static final BlockEntry<ActiveBlock> QFT_COIL = createActiveCasing("qft_coil", "block/variant/qft_coil");
   public static final BlockEntry<ActiveBlock> FISSION_FUEL_ASSEMBLY = createActiveCasing("fission_fuel_assembly", "block/variant/fission_fuel_assembly");
   public static final BlockEntry<ActiveBlock> COOLER = createActiveCasing("cooler", "block/variant/cooler");
   public static final BlockEntry<ActiveBlock> ADVANCED_ASSEMBLY_LINE_UNIT = createActiveCasing(
      "advanced_assembly_line_unit", "block/variant/advanced_assembly_line_unit"
   );
   public static final BlockEntry<ActiveBlock> SPACE_ELEVATOR_SUPPORT = createActiveCasing("space_elevator_support", "block/variant/space_elevator_support");
   public static final BlockEntry<Block> STELLAR_CONTAINMENT_CASING = createTierCasings(
      "stellar_containment_casing", GTLCore.id("block/stellar_containment_casing"), BlockMap.scMap, 1
   );
   public static final BlockEntry<Block> ADVANCED_STELLAR_CONTAINMENT_CASING = createTierCasings(
      "advanced_stellar_containment_casing", GTLCore.id("block/stellar_containment_casing"), BlockMap.scMap, 2
   );
   public static final BlockEntry<Block> ULTIMATE_STELLAR_CONTAINMENT_CASING = createTierCasings(
      "ultimate_stellar_containment_casing", GTLCore.id("block/stellar_containment_casing"), BlockMap.scMap, 3
   );
   public static final BlockEntry<ActiveBlock> POWER_MODULE = createActiveTierCasing("power_module", "block/variant/power_module", BlockMap.sepmMap, 1);
   public static final BlockEntry<ActiveBlock> POWER_MODULE_2 = createActiveTierCasing("power_module_2", "block/variant/power_module", BlockMap.sepmMap, 2);
   public static final BlockEntry<ActiveBlock> POWER_MODULE_3 = createActiveTierCasing("power_module_3", "block/variant/power_module", BlockMap.sepmMap, 3);
   public static final BlockEntry<ActiveBlock> POWER_MODULE_4 = createActiveTierCasing("power_module_4", "block/variant/power_module", BlockMap.sepmMap, 4);
   public static final BlockEntry<ActiveBlock> POWER_MODULE_5 = createActiveTierCasing("power_module_5", "block/variant/power_module", BlockMap.sepmMap, 5);

   public static void init() {
      BlockMap.init();

      for (int i = 1; i < 15; i++) {
         createTierCasings(
            "component_assembly_line_casing_" + GTValues.VN[i].toLowerCase(),
            GTLCore.id("block/casings/component_assembly_line/component_assembly_line_casing_" + GTValues.VN[i].toLowerCase()),
            BlockMap.calMap,
            i
         );
      }
   }

   private static BlockEntry<CraftingUnitBlock> registerCraftingUnitBlock(int tier, CraftingUnitType Type) {
      return ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(tier == -1 ? "max_storage" : tier + "m_storage", p -> new CraftingUnitBlock(Type))
            .blockstate((ctx, provider) -> {
               String formed = "block/crafting/" + ctx.getName() + "_formed";
               String unformed = "block/crafting/" + ctx.getName();
               provider.models().cubeAll(unformed, provider.modLoc("block/crafting/" + ctx.getName()));
               provider.models().getBuilder(formed);
               provider.getVariantBuilder((Block)ctx.get()).forAllStatesExcept(state -> {
                  boolean b = (Boolean)state.m_61143_(AbstractCraftingUnitBlock.FORMED);
                  return ConfiguredModel.builder().modelFile(provider.models().getExistingFile(provider.modLoc(b ? formed : unformed))).build();
               }, new Property[]{AbstractCraftingUnitBlock.POWERED});
            })
            .defaultLoot()
            .item(BlockItem::new)
            .model((ctx, provider) -> provider.withExistingParent(ctx.getName(), provider.modLoc("block/crafting/" + ctx.getName())))
            .build())
         .register();
   }

   public static BlockEntry<ActiveBlock> createActiveCasing(String name, String baseModelPath) {
      return ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(name, ActiveBlock::new)
            .initialProperties(() -> Blocks.f_50075_)
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(GTModels.createActiveModel(GTLCore.id(baseModelPath)))
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), BlockTags.f_144282_})
            .item(BlockItem::new)
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTLCore.id(baseModelPath)))
            .build())
         .register();
   }

   public static BlockEntry<Block> createTierCasings(String name, ResourceLocation texture, Int2ObjectMap<Supplier<?>> map, int tier) {
      BlockEntry<Block> Block = ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(name, p -> new Block(p) {
               public void m_5871_(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                  tooltip.add(Component.m_237110_("gtceu.casings.tier", new Object[]{tier}));
               }
            })
            .initialProperties(() -> Blocks.f_50075_)
            .properties(p -> p.m_60922_((state, level, pos, ent) -> false))
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(GTModels.cubeAllModel(name, texture))
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), BlockTags.f_144282_})
            .item(BlockItem::new)
            .build())
         .register();
      GTLRegistration.REGISTRATE.setCreativeTab(Block, GTLCreativeModeTabs.GTL_CORE);
      map.put(tier, Block);
      return Block;
   }

   public static BlockEntry<ActiveBlock> createActiveTierCasing(String name, String baseModelPath, Int2ObjectMap<Supplier<?>> map, int tier) {
      BlockEntry<ActiveBlock> Block = ((BlockBuilder)GTLRegistration.REGISTRATE
            .block("%s".formatted(name), p -> new ActiveBlock(p) {
               public void m_5871_(@NotNull ItemStack stack, @Nullable BlockGetter level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                  tooltip.add(Component.m_237110_("gtceu.casings.tier", new Object[]{tier}));
               }
            })
            .initialProperties(() -> Blocks.f_50075_)
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(GTModels.createActiveModel(GTLCore.id(baseModelPath)))
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), BlockTags.f_144282_})
            .item(BlockItem::new)
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), GTLCore.id(baseModelPath)))
            .build())
         .register();
      GTLRegistration.REGISTRATE.setCreativeTab(Block, GTLCreativeModeTabs.GTL_CORE);
      map.put(tier, Block);
      return Block;
   }

   private static BlockEntry<Block> createCleanroomFilter(IFilterType filterType) {
      BlockEntry<Block> filterBlock = ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(filterType.m_7912_(), Block::new)
            .initialProperties(() -> Blocks.f_50075_)
            .properties(
               properties -> properties.m_60913_(2.0F, 8.0F).m_60918_(SoundType.f_56743_).m_60922_((blockState, blockGetter, blockPos, entityType) -> false)
            )
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(NonNullBiConsumer.noop())
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[1]})
            .item(BlockItem::new)
            .build())
         .register();
      GTCEuAPI.CLEANROOM_FILTERS.put(filterType, filterBlock);
      return filterBlock;
   }

   private static BlockEntry<Block> createGlassCasingBlock(String name, ResourceLocation texture, Supplier<Supplier<RenderType>> type) {
      return createCasingBlock(name, GlassBlock::new, texture, () -> Blocks.f_50058_, type);
   }

   public static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture) {
      return createCasingBlock(name, Block::new, texture, () -> Blocks.f_50075_, () -> RenderType::m_110457_);
   }

   public static BlockEntry<Block> createCasingBlock(
      String name,
      NonNullFunction<Properties, Block> blockSupplier,
      ResourceLocation texture,
      NonNullSupplier<? extends Block> properties,
      Supplier<Supplier<RenderType>> type
   ) {
      return ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(name, blockSupplier)
            .initialProperties(properties)
            .properties(p -> p.m_60922_((state, level, pos, ent) -> false))
            .addLayer(type)
            .blockstate(GTModels.cubeAllModel(name, texture))
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), BlockTags.f_144282_})
            .item(BlockItem::new)
            .build())
         .register();
   }

   private static BlockEntry<FusionCasingBlock> createFusionCasing(IFusionCasingType casingType) {
      BlockEntry<FusionCasingBlock> casingBlock = ((BlockBuilder)GTLRegistration.REGISTRATE
            .block(casingType.m_7912_(), p -> new GTLFusionCasingBlock(p, casingType))
            .initialProperties(() -> Blocks.f_50075_)
            .properties(properties -> properties.m_60913_(5.0F, 10.0F).m_60918_(SoundType.f_56743_))
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(
               (ctx, prov) -> {
                  ActiveBlock block = (ActiveBlock)ctx.getEntry();
                  ModelFile inactive = prov.models().getExistingFile(GTLCore.id(casingType.m_7912_()));
                  ModelFile active = prov.models().getExistingFile(GTLCore.id(casingType.m_7912_()).m_266382_("_active"));
                  ((VariantBlockStateBuilder)prov.getVariantBuilder(block)
                        .partialState()
                        .with(ActiveBlock.ACTIVE, false)
                        .modelForState()
                        .modelFile(inactive)
                        .addModel())
                     .partialState()
                     .with(ActiveBlock.ACTIVE, true)
                     .modelForState()
                     .modelFile(active)
                     .addModel();
               }
            )
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[casingType.getHarvestLevel()]})
            .item(BlockItem::new)
            .build())
         .register();
      GTBlocks.ALL_FUSION_CASINGS.put(casingType, casingBlock);
      return casingBlock;
   }

   private static BlockEntry<Block> createHermeticCasing(int tier) {
      String tierName = GTValues.VN[tier].toLowerCase(Locale.ROOT);
      return ((BlockBuilder)GTLRegistration.REGISTRATE
            .block("%s_hermetic_casing".formatted(tierName), Block::new)
            .lang("Hermetic Casing %s".formatted(GTValues.LVT[tier]))
            .initialProperties(() -> Blocks.f_50075_)
            .properties(p -> p.m_60922_((state, level, pos, ent) -> false))
            .addLayer(() -> RenderType::m_110457_)
            .blockstate(NonNullBiConsumer.noop())
            .tag(new TagKey[]{(TagKey)GTToolType.WRENCH.harvestTags.get(0), BlockTags.f_144282_})
            .item(BlockItem::new)
            .build())
         .register();
   }

   static {
      GTLRegistration.REGISTRATE.creativeModeTab(() -> GTLCreativeModeTabs.GTL_CORE);
   }
}
