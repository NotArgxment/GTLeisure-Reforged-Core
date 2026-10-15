package org.gtlcore.gtlcore.common.data;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.ExistingSoundEntry;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.gtlcore.gtlcore.common.recipe.RecipeModify;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.data.recipe.GenerateDisassembly;

public class GTLRecipeTypes {
   public static final GTRecipeType PRIMITIVE_VOID_ORE_RECIPES = ConfigHolder.INSTANCE.enablePrimitiveVoidOre
      ? GTRecipeTypes.register("primitive_void_ore", "multiblock", new RecipeType[0])
         .setMaxIOSize(0, 0, 1, 0)
         .setMaxTooltips(1)
         .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, FillDirection.LEFT_TO_RIGHT)
         .setSound(GTSoundEntries.MINER)
      : null;
   public static final GTRecipeType SEMI_FLUID_GENERATOR_FUELS = GTRecipeTypes.register("semi_fluid_generator", "generator", new RecipeType[0])
      .setMaxIOSize(0, 0, 1, 0)
      .setEUIO(IO.OUT)
      .setSlotOverlay(false, true, true, GuiTextures.FURNACE_OVERLAY_2)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COMBUSTION);
   public static final GTRecipeType SUPERCRITICAL_STEAM_TURBINE_FUELS = GTRecipeTypes.register("supercritical_steam_turbine", "generator", new RecipeType[0])
      .setMaxIOSize(0, 0, 1, 1)
      .setEUIO(IO.OUT)
      .setSlotOverlay(false, true, true, GuiTextures.CENTRIFUGE_OVERLAY)
      .setProgressBar(GuiTextures.PROGRESS_BAR_GAS_COLLECTOR, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.TURBINE);
   public static final GTRecipeType ROCKET_ENGINE_FUELS = GTRecipeTypes.register("rocket_engine", "generator", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(0, 0, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.JET_ENGINE);
   public static final GTRecipeType NAQUADAH_REACTOR = GTRecipeTypes.register("naquadah_reactor", "generator", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(0, 0, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COMBUSTION);
   public static final GTRecipeType ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES = GTRecipeTypes.register(
         "electric_implosion_compressor", "multiblock", new RecipeType[0]
      )
      .setMaxIOSize(2, 1, 0, 0)
      .setEUIO(IO.IN)
      .prepareBuilder(recipeBuilder -> recipeBuilder.duration(1).EUt(GTValues.VA[8]))
      .setSlotOverlay(false, false, true, GuiTextures.IMPLOSION_OVERLAY_1)
      .setSlotOverlay(false, false, false, GuiTextures.IMPLOSION_OVERLAY_2)
      .setSlotOverlay(true, false, true, GuiTextures.DUST_OVERLAY)
      .setSound(new ExistingSoundEntry(SoundEvents.f_11913_, SoundSource.BLOCKS));
   public static final GTRecipeType DISASSEMBLY_RECIPES = GTRecipeTypes.register("disassembly", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 16, 0, 4)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ASSEMBLER);
   public static final GTRecipeType NEUTRON_ACTIVATOR_RECIPES = GTRecipeTypes.register("neutron_activator", "multiblock", new RecipeType[0])
      .setMaxIOSize(6, 3, 1, 1)
      .setSound(GTSoundEntries.COOLING)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.ev_min", new Object[]{data.m_128451_("ev_min")}))
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.ev_max", new Object[]{data.m_128451_("ev_max")}))
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.evt", new Object[]{data.m_128451_("evt")}));
   public static final GTRecipeType HEAT_EXCHANGER_RECIPES = GTRecipeTypes.register("heat_exchanger", "multiblock", new RecipeType[0])
      .setMaxIOSize(0, 0, 2, 2)
      .setProgressBar(GuiTextures.PROGRESS_BAR_MIXER, FillDirection.LEFT_TO_RIGHT)
      .setMaxTooltips(1)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType ELEMENT_COPYING_RECIPES = GTRecipeTypes.register("element_copying", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 1, 2, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType LIGHTNING_PROCESSOR_RECIPES = GTRecipeTypes.register("lightning_processor", "electric", new RecipeType[0])
      .setMaxIOSize(6, 1, 6, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW_MULTIPLE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType DEHYDRATOR_RECIPES = GTRecipeTypes.register("dehydrator", "electric", new RecipeType[0])
      .setMaxIOSize(2, 6, 2, 2)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType INTEGRATED_ORE_PROCESSOR = GTRecipeTypes.register("integrated_ore_processor", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 9, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR);
   public static final GTRecipeType FISSION_REACTOR_RECIPES = GTRecipeTypes.register("fission_reactor", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 1, 0, 0)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.frheat", new Object[]{FormattingUtil.formatNumbers(data.m_128451_("FRheat"))}));
   public static final GTRecipeType SPACE_ELEVATOR_RECIPES = GTRecipeTypes.register("space_elevator", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT);
   public static final GTRecipeType STELLAR_FORGE_RECIPES = GTRecipeTypes.register("stellar_forge", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(3, 2, 9, 2)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.stellar_containment_tier", new Object[]{getSCTier(data.m_128451_("SCTier"))}));
   public static final GTRecipeType COMPONENT_ASSEMBLY_LINE_RECIPES = GTRecipeTypes.register("component_assembly_line", "multiblock", new RecipeType[0])
      .setMaxIOSize(9, 1, 9, 0)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.ca_tier", new Object[]{GTValues.VN[data.m_128451_("CATier")]}))
      .setSound(GTSoundEntries.ASSEMBLER);
   public static final GTRecipeType SLAUGHTERHOUSE_RECIPES = GTRecipeTypes.register("slaughterhouse", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR);
   public static final GTRecipeType GREENHOUSE_RECIPES = GTRecipeTypes.register("greenhouse", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(3, 3, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES = GTRecipeTypes.register(
         "dimensionally_transcendent_plasma_forge", "multiblock", new RecipeType[0]
      )
      .setMaxIOSize(2, 2, 2, 2)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTLSoundEntries.DTPF)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(data.m_128451_("ebf_temp"))}))
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return LocalizationUtils.format(
               "gtceu.recipe.coil.tier",
               new Object[]{
                  temp > 21600 && temp <= 32000 ? "\u8d85\u7ea7\u70ed\u5bb9" : I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])
               }
            );
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            List<List<ItemStack>> items = new ArrayList<>();
            int temp = recipe.data.m_128451_("ebf_temp");
            items.add(GTCEuAPI.HEATING_COILS.entrySet().stream().filter(coil -> {
               int ctemp = ((ICoilType)coil.getKey()).getCoilTemperature();
               return ctemp == 273 ? temp <= 32000 : ctemp >= temp;
            }).map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get())).toList());
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 35, widgetGroup.getSize().height - 32, false, false)
            );
         }
      );
   public static final GTRecipeType PLASMA_CONDENSER_RECIPES = GTRecipeTypes.register("plasma_condenser", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 2, 2, 2)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType RARE_EARTH_CENTRIFUGAL_RECIPES = GTRecipeTypes.register("rare_earth_centrifugal", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 17, 1, 1)
      .setProgressBar(GuiTextures.CENTRIFUGE_OVERLAY, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.CENTRIFUGE);
   public static final GTRecipeType MAGIC_MANUFACTURER_RECIPES = GTRecipeTypes.register("magic_manufacturer", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_MASS_FAB, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType SPS_CRAFTING_RECIPES = GTRecipeTypes.register("sps_crafting", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(3, 1, 3, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType MATTER_FABRICATOR_RECIPES = GTRecipeTypes.register("matter_fabricator", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 1, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType VOID_MINER_RECIPES = GTRecipeTypes.register("void_miner", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 4, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType LARGE_VOID_MINER_RECIPES = GTRecipeTypes.register("large_void_miner", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 4, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType RANDOM_ORE_RECIPES = GTRecipeTypes.register("random_ore", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(0, 216, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType VOID_FLUID_DRILLING_RIG_RECIPES = GTRecipeTypes.register("void_fluid_drilling_rig", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.CHEMICAL);
   public static final GTRecipeType WORLD_DATA_SCANNER_RECIPES = GTRecipeTypes.register("world_data_scanner", "electric", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(3, 1, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType ANNIHILATE_GENERATOR_RECIPES = GTRecipeTypes.register("annihilate_generator", "multiblock", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(1, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType HYPER_REACTOR_RECIPES = GTRecipeTypes.register("hyper_reactor", "multiblock", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(0, 0, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType ADVANCED_HYPER_REACTOR_RECIPES = GTRecipeTypes.register("advanced_hyper_reactor", "multiblock", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(0, 0, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType LARGE_NAQUADAH_REACTOR_RECIPES = GTRecipeTypes.register("large_naquadah_reactor", "multiblock", new RecipeType[0])
      .setEUIO(IO.OUT)
      .setMaxIOSize(0, 0, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COMBUSTION);
   public static final GTRecipeType COSMOS_SIMULATION_RECIPES = GTRecipeTypes.register("cosmos_simulation", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 120, 1, 18)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType SPACE_PROBE_SURFACE_RECEPTION_RECIPES = GTRecipeTypes.register(
         "space_probe_surface_reception", "multiblock", new RecipeType[0]
      )
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(2, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType SPACE_COSMIC_PROBE_RECEIVERS_RECIPES = GTRecipeTypes.register(
         "space_cosmic_probe_receivers", "multiblock", new RecipeType[0]
      )
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(2, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType DECAY_HASTENER_RECIPES = GTRecipeTypes.register("decay_hastener", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(0, 1, 1, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType LARGE_RECYCLER_RECIPES = GTRecipeTypes.register("large_recycler", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_RECYCLER, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR);
   public static final GTRecipeType MASS_FABRICATOR_RECIPES = GTRecipeTypes.register("mass_fabricator", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 1, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_REPLICATOR, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType CIRCUIT_ASSEMBLY_LINE_RECIPES = GTRecipeTypes.register("circuit_assembly_line", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(16, 1, 4, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ASSEMBLER)
      .onRecipeBuild(GenerateDisassembly::generateDisassembly);
   public static final GTRecipeType SUPRACHRONAL_ASSEMBLY_LINE_RECIPES = GTRecipeTypes.register("suprachronal_assembly_line", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(16, 1, 4, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ASSEMBLER)
      .setHasResearchSlot(true)
      .onRecipeBuild((recipeBuilder, provider) -> {
         ResearchManager.createDefaultResearchRecipe(recipeBuilder, provider);
         GenerateDisassembly.generateDisassembly(recipeBuilder, provider);
      });
   public static final GTRecipeType PRECISION_ASSEMBLER_RECIPES = GTRecipeTypes.register("precision_assembler", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(4, 1, 4, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ASSEMBLER)
      .onRecipeBuild(GenerateDisassembly::generateDisassembly);
   public static final GTRecipeType ASSEMBLER_MODULE_RECIPES = GTRecipeTypes.register("assembler_module", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(16, 1, 4, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ASSEMBLER)
      .onRecipeBuild(GenerateDisassembly::generateDisassembly)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.sepm_tier", new Object[]{FormattingUtil.formatNumbers(data.m_128451_("SEPMTier"))}));
   public static final GTRecipeType MINER_MODULE_RECIPES = GTRecipeTypes.register("miner_module", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 6, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType DRILLING_MODULE_RECIPES = GTRecipeTypes.register("drilling_module", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 0, 1, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType FISHING_GROUND_RECIPES = GTRecipeTypes.register("fishing_ground", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 24, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);
   public static final GTRecipeType BLOCK_CONVERSIONRECIPES = GTRecipeTypes.register("block_conversion", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType INCUBATOR_RECIPES = GTRecipeTypes.register("incubator", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(6, 1, 2, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType PCB_FACTORY_RECIPES = GTRecipeTypes.register("pcb_factory", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 1, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.CHEMICAL);
   public static final GTRecipeType LAVA_FURNACE_RECIPES = GTRecipeTypes.register("lava_furnace", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARC_FURNACE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.FURNACE);
   public static final GTRecipeType LARGE_GAS_COLLECTOR_RECIPES = GTRecipeTypes.register("large_gas_collector", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 0, 0, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType AGGREGATION_DEVICE_RECIPES = GTRecipeTypes.register("aggregation_device", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(9, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType SUPER_PARTICLE_COLLIDER_RECIPES = GTRecipeTypes.register("super_particle_collider", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(0, 0, 2, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTLSoundEntries.FUSIONLOOP);
   public static final GTRecipeType DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES = GTRecipeTypes.register(
         "dimensional_focus_engraving_array", "multiblock", new RecipeType[0]
      )
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(2, 1, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC)
      .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);
   public static final GTRecipeType PRECISION_LASER_ENGRAVER_RECIPES = GTRecipeTypes.register("precision_laser_engraver", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(9, 1, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType DIMENSIONALLY_TRANSCENDENT_MIXER_RECIPES = GTRecipeTypes.register(
         "dimensionally_transcendent_mixer", "multiblock", new RecipeType[0]
      )
      .setEUIO(IO.IN)
      .setMaxIOSize(9, 1, 6, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MIXER);
   public static final GTRecipeType NEUTRON_COMPRESSOR_RECIPES = GTRecipeTypes.register("neutron_compressor", "electric", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_COMPRESS, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COMPRESSOR);
   public static final GTRecipeType QFT_RECIPES = GTRecipeTypes.register("qft", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(18, 1, 3, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType DRAGON_EGG_COPIER_RECIPES = GTRecipeTypes.register("dragon_egg_copier", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 2, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType DOOR_OF_CREATE_RECIPES = GTRecipeTypes.register("door_of_create", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxTooltips(4)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType BEDROCK_DRILLING_RIG_RECIPES = GTRecipeTypes.register("bedrock_drilling_rig", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR);
   public static final GTRecipeType CREATE_AGGREGATION_RECIPES = GTRecipeTypes.register("create_aggregation", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType GRAVITATION_SHOCKBURST_RECIPES = GTRecipeTypes.register("gravitation_shockburst", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 1, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR)
      .onRecipeBuild(ResearchManager::createDefaultResearchRecipe);
   public static final GTRecipeType ULTIMATE_MATERIAL_FORGE_RECIPES = GTRecipeTypes.register("ultimate_material_forge", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(2, 2, 2, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR);
   public static final GTRecipeType DYSON_SPHERE_RECIPES = GTRecipeTypes.register("dyson_sphere", "multiblock", new RecipeType[0])
      .setEUIO(IO.BOTH)
      .setMaxTooltips(4)
      .setMaxIOSize(1, 0, 1, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType PETROCHEMICAL_PLANT_RECIPES = GTRecipeTypes.register("petrochemical_plant", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(0, 0, 2, 12)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING);
   public static final GTRecipeType WEATHER_CONTROL_RECIPES = GTRecipeTypes.register("weather_control", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 0, 0, 0)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType DISTORT_RECIPES = GTRecipeTypes.register("distort", "multiblock", new RecipeType[0])
      .setMaxIOSize(9, 9, 9, 9)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .addDataInfo(data -> {
         int temp = data.m_128451_("ebf_temp");
         return LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(temp)});
      })
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return requiredCoil != null && requiredCoil.getMaterial() != null
               ? LocalizationUtils.format(
                  "gtceu.recipe.coil.tier", new Object[]{I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])}
               )
               : "";
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            int temp = recipe.data.m_128451_("ebf_temp");
            List<List<ItemStack>> items = new ArrayList<>();
            items.add(
               GTCEuAPI.HEATING_COILS
                  .entrySet()
                  .stream()
                  .filter(coil -> ((ICoilType)coil.getKey()).getCoilTemperature() >= temp)
                  .map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get()))
                  .toList()
            );
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false)
            );
         }
      )
      .setMaxTooltips(4)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType NANO_FORGE_RECIPES = GTRecipeTypes.register("nano_forge", "multiblock", new RecipeType[0])
      .setMaxIOSize(6, 1, 3, 0)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .addDataInfo(
         data -> LocalizationUtils.format("gtceu.recipe.nano_forge_tier", new Object[]{FormattingUtil.formatNumbers(data.m_128451_("nano_forge_tier"))})
      )
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType FUEL_REFINING_RECIPES = GTRecipeTypes.register("fuel_refining", "multiblock", new RecipeType[0])
      .setMaxIOSize(3, 0, 6, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .addDataInfo(data -> {
         int temp = data.m_128451_("ebf_temp");
         return LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(temp)});
      })
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return requiredCoil != null && requiredCoil.getMaterial() != null
               ? LocalizationUtils.format(
                  "gtceu.recipe.coil.tier", new Object[]{I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])}
               )
               : "";
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            int temp = recipe.data.m_128451_("ebf_temp");
            List<List<ItemStack>> items = new ArrayList<>();
            items.add(
               GTCEuAPI.HEATING_COILS
                  .entrySet()
                  .stream()
                  .filter(coil -> ((ICoilType)coil.getKey()).getCoilTemperature() >= temp)
                  .map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get()))
                  .toList()
            );
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false)
            );
         }
      )
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType ATOMIC_ENERGY_EXCITATION_RECIPES = GTRecipeTypes.register("atomic_energy_excitation", "multiblock", new RecipeType[0])
      .setMaxIOSize(3, 0, 6, 2)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .addDataInfo(data -> {
         int temp = data.m_128451_("ebf_temp");
         return LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(temp)});
      })
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return requiredCoil != null && requiredCoil.getMaterial() != null
               ? LocalizationUtils.format(
                  "gtceu.recipe.coil.tier", new Object[]{I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])}
               )
               : "";
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            int temp = recipe.data.m_128451_("ebf_temp");
            List<List<ItemStack>> items = new ArrayList<>();
            items.add(
               GTCEuAPI.HEATING_COILS
                  .entrySet()
                  .stream()
                  .filter(coil -> ((ICoilType)coil.getKey()).getCoilTemperature() >= temp)
                  .map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get()))
                  .toList()
            );
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false)
            );
         }
      )
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType ISA_MILL_RECIPES = GTRecipeTypes.register("isa_mill", "multiblock", new RecipeType[0])
      .setMaxIOSize(2, 1, 1, 0)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MACERATOR)
      .addDataInfo(data -> LocalizationUtils.format("gtceu.recipe.grindball", new Object[]{getGrindball(data.m_128451_("grindball"))}));
   public static final GTRecipeType FLOTATING_BENEFICIATION_RECIPES = GTRecipeTypes.register("flotating_beneficiation", "multiblock", new RecipeType[0])
      .setMaxIOSize(2, 0, 1, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_BATH, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.CHEMICAL);
   public static final GTRecipeType VACUUM_DRYING_RECIPES = GTRecipeTypes.register("vacuum_drying", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 6, 1, 2)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.COOLING)
      .addDataInfo(data -> {
         int temp = data.m_128451_("ebf_temp");
         return LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(temp)});
      })
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return requiredCoil != null && requiredCoil.getMaterial() != null
               ? LocalizationUtils.format(
                  "gtceu.recipe.coil.tier", new Object[]{I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])}
               )
               : "";
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            int temp = recipe.data.m_128451_("ebf_temp");
            List<List<ItemStack>> items = new ArrayList<>();
            items.add(
               GTCEuAPI.HEATING_COILS
                  .entrySet()
                  .stream()
                  .filter(coil -> ((ICoilType)coil.getKey()).getCoilTemperature() >= temp)
                  .map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get()))
                  .toList()
            );
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 25, widgetGroup.getSize().height - 40, false, false)
            );
         }
      );
   public static final GTRecipeType DISSOLUTION_TREATMENT = GTRecipeTypes.register("dissolution_treatment", "multiblock", new RecipeType[0])
      .setMaxIOSize(2, 2, 2, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC);
   public static final GTRecipeType DIGESTION_TREATMENT = GTRecipeTypes.register("digestion_treatment", "multiblock", new RecipeType[0])
      .setMaxIOSize(1, 1, 1, 1)
      .setEUIO(IO.IN)
      .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.ARC)
      .addDataInfo(data -> {
         int temp = data.m_128451_("ebf_temp");
         return LocalizationUtils.format("gtceu.recipe.temperature", new Object[]{FormattingUtil.formatNumbers(temp)});
      })
      .addDataInfo(
         data -> {
            int temp = data.m_128451_("ebf_temp");
            ICoilType requiredCoil = ICoilType.getMinRequiredType(temp);
            return requiredCoil != null && requiredCoil.getMaterial() != null
               ? LocalizationUtils.format(
                  "gtceu.recipe.coil.tier", new Object[]{I18n.m_118938_(requiredCoil.getMaterial().getUnlocalizedName(), new Object[0])}
               )
               : "";
         }
      )
      .setUiBuilder(
         (recipe, widgetGroup) -> {
            int temp = recipe.data.m_128451_("ebf_temp");
            List<List<ItemStack>> items = new ArrayList<>();
            items.add(
               GTCEuAPI.HEATING_COILS
                  .entrySet()
                  .stream()
                  .filter(coil -> ((ICoilType)coil.getKey()).getCoilTemperature() >= temp)
                  .map(coil -> new ItemStack((ItemLike)((Supplier)coil.getValue()).get()))
                  .toList()
            );
            widgetGroup.addWidget(
               new SlotWidget(new CycleItemStackHandler(items), 0, widgetGroup.getSize().width - 50, widgetGroup.getSize().height - 40, false, false)
            );
         }
      );
   public static final GTRecipeType WOOD_DISTILLATION_RECIPES = GTRecipeTypes.register("wood_distillation", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(1, 1, 1, 16)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.BATH);
   public static final GTRecipeType DESULFURIZER_RECIPES = GTRecipeTypes.register("desulfurizer", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(0, 1, 1, 1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MIXER);
   public static final GTRecipeType MOLECULAR_ASSEMBLER = GTRecipeTypes.register("molecular_assembler", "multiblock", new RecipeType[0])
      .setMaxIOSize(3, 12, 1, 1)
      .setMaxTooltips(1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_ARROW, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.SCIENCE);
   public static final GTRecipeType FRAGMENT_WORLD_COLLECTION = GTRecipeTypes.register("fragment_world_collection", "multiblock", new RecipeType[0])
      .setEUIO(IO.IN)
      .setMaxIOSize(3, 12, 1, 1)
      .setMaxTooltips(1)
      .setProgressBar(GuiTextures.PROGRESS_BAR_MACERATE, FillDirection.LEFT_TO_RIGHT)
      .setSound(GTSoundEntries.MINER);

   public static String getSCTier(int tier) {
      return switch (tier) {
         case 2 -> I18n.m_118938_("gtceu.tier.advanced", new Object[0]);
         case 3 -> I18n.m_118938_("gtceu.tier.ultimate", new Object[0]);
         default -> I18n.m_118938_("gtceu.tier.base", new Object[0]);
      };
   }

   private static String getGrindball(int tier) {
      return tier == 2 ? I18n.m_118938_("material.gtceu.aluminium", new Object[0]) : I18n.m_118938_("material.gtceu.soapstone", new Object[0]);
   }

   public static void init() {
      RecipeModify.init();
   }
}
