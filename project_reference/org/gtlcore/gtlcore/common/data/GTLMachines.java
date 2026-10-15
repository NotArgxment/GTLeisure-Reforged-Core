package org.gtlcore.gtlcore.common.data;

import com.google.common.collect.UnmodifiableIterator;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.registry.registrate.CompassSection;
import com.gregtechceu.gtceu.client.renderer.machine.MaintenanceHatchPartRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.common.registry.GTMTRegistration;
import com.hepdd.gtmthings.data.CreativeModeTabs;
import com.hepdd.gtmthings.data.WirelessMachines;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.machine.PerformanceMonitorMachine;
import org.gtlcore.gtlcore.api.machine.multiblock.GTLPartAbility;
import org.gtlcore.gtlcore.common.data.machines.AdditionalMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA;
import org.gtlcore.gtlcore.common.data.machines.ShapeInfosModify;
import org.gtlcore.gtlcore.common.data.machines.TootipsModify;
import org.gtlcore.gtlcore.common.machine.generator.LightningRodMachine;
import org.gtlcore.gtlcore.common.machine.generator.MagicEnergyMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.CoilWorkableElectricMultipleRecipesMultiblockMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.BlockBusPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.HugeFluidHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.LargeSteamHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.MEDualHatchStockPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronSensorPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.RotorHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.TagFilterMEStockBusPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.TemperatureSensorPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.WirelessOpticalDataHatchMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MECraftParallelCorePartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MECraftPatternContainerPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MECraftSpeedCorePartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEExtendedAsyncOutputPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEExtendedOutputPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEMolecularAssemblerIOPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferProxyPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.AutoConfigurationMaintenanceHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.CleaningConfigurationMaintenanceHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.GTLCleaningMaintenanceHatchPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.GravityCleaningConfigurationMaintenancePartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.ICleaningRoom;

public class GTLMachines {
   public static final BiConsumer<ItemStack, List<Component>> GTL_MODIFY = (stack, components) -> components.add(
      Component.m_237115_("gtlcore.registry.modify").m_130938_(style -> style.m_131140_(TooltipHelper.RAINBOW.getCurrent()))
   );
   public static final BiConsumer<ItemStack, List<Component>> GTL_ADD = (stack, components) -> components.add(
      Component.m_237115_("gtlcore.registry.add").m_130938_(style -> style.m_131140_(TooltipHelper.RAINBOW_SLOW.getCurrent()))
   );
   public static final BiConsumer<IMultiController, List<Component>> CHEMICAL_PLANT_DISPLAY = (controller, components) -> {
      if (controller.isFormed()) {
         double value = 1.0 - ((CoilWorkableElectricMultiblockMachine)controller).getCoilTier() * 0.05;
         components.add(Component.m_237110_("gtceu.machine.eut_multiplier.tooltip", new Object[]{FormattingUtil.formatPercent(value * 0.8)}));
         components.add(Component.m_237110_("gtceu.machine.duration_multiplier.tooltip", new Object[]{FormattingUtil.formatPercent(value * 0.6)}));
      }
   };
   public static final BiConsumer<IMultiController, List<Component>> MAX_TEMPERATURE = (controller, components) -> {
      if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
         components.add(
            Component.m_237110_(
               "gtceu.multiblock.blast_furnace.max_temperature",
               new Object[]{
                  Component.m_237115_(
                        FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature() + 100L * Math.max(0, coilMachine.getTier() - 2)) + "K"
                     )
                     .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED))
               }
            )
         );
      }
   };
   public static final BiConsumer<IMultiController, List<Component>> TEMPERATURE = (controller, components) -> {
      if (controller instanceof CoilWorkableElectricMultiblockMachine coilMachine && controller.isFormed()) {
         components.add(
            Component.m_237110_(
               "gtceu.multiblock.blast_furnace.max_temperature",
               new Object[]{
                  Component.m_237115_(FormattingUtil.formatNumbers(coilMachine.getCoilType().getCoilTemperature()) + "K")
                     .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED))
               }
            )
         );
      }
   };
   public static final BiConsumer<IMultiController, List<Component>> COIL_PARALLEL = (controller, components) -> {
      if (controller instanceof CoilWorkableElectricMultiblockMachine machine && controller.isFormed()) {
         components.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{
                     Component.m_237113_(
                           FormattingUtil.formatNumbers(Math.min(2147483647, (int)Math.pow(2.0, machine.getCoilType().getCoilTemperature() / 900.0)))
                        )
                        .m_130940_(ChatFormatting.DARK_PURPLE)
                  }
               )
               .m_130940_(ChatFormatting.GRAY)
         );
      }
   };
   public static final BiConsumer<IMultiController, List<Component>> MULTIPLERECIPES_COIL_PARALLEL = (controller, components) -> {
      if (controller instanceof CoilWorkableElectricMultipleRecipesMultiblockMachine machine && controller.isFormed()) {
         components.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{
                     Component.m_237113_(
                           FormattingUtil.formatNumbers(Math.min(2147483647, (int)Math.pow(2.0, machine.getCoilType().getCoilTemperature() / 900.0)))
                        )
                        .m_130940_(ChatFormatting.DARK_PURPLE)
                  }
               )
               .m_130940_(ChatFormatting.GRAY)
         );
      }
   };
   public static final BiConsumer<IMultiController, List<Component>> PROCESSING_PLANT_PARALLEL = (controller, components) -> {
      if (controller.isFormed() && controller instanceof WorkableElectricMultiblockMachine workableElectricMultiblockMachine) {
         components.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{
                     Component.m_237113_(FormattingUtil.formatNumbers(4 * (workableElectricMultiblockMachine.getTier() - 1)))
                        .m_130940_(ChatFormatting.DARK_PURPLE)
                  }
               )
               .m_130940_(ChatFormatting.GRAY)
         );
      }
   };
   public static final FactoryBlockPattern DTPF = FactoryBlockPattern.start()
      .aisle(
         new String[]{
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd   d     d   ddd   ddd ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbd    d d    dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         ",
            "                                 ",
            "         d   d     d   d         ",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbdddddsdsdddddbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "  s     s               s     s  ",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC   d     d   CbC   CbC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "  d     sbbbbbddsddbbbbbs     d  ",
            "  d      bCCCb     bCCCb      d  ",
            "  d      d   d     d   d      d  ",
            "   s                         s   ",
            "   s     d   d     d   d     s   ",
            "    ss   bCCCb     bCCCb   ss    ",
            "      dddbbbbbddsddbbbbbddd      ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbdddbbbd    ded    dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbdddbbbd           dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         dCCCd     dCCCd         ",
            "  s      dCCCd     dCCCd      s  ",
            "  s      dCCCd     dCCCd      s  ",
            "         bCCCb     bCCCb         ",
            "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   dd    d     d    dd   ddd ",
            "         d   d     d   d         ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s      dCCCd     dCCCd      s  ",
            "   s     d   d     d   d     s   ",
            "         d   d     d   d         ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "   d   d       ded       d   d   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   d   d                 d   d   ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "   d   d                 d   d   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   d   d                 d   d   ",
            "                                 ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s      dCCCd     dCCCd      s  ",
            "   s                         s   ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   dd    d     d    dd   ddd ",
            "         d   d     d   d         ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "         dCCCd     dCCCd         ",
            "  d      d   d     d   d      d  ",
            "         d   d     d   d         ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "dbbbdddbbbd    ded    dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbdddbbbd           dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         dCCCd     dCCCd         ",
            "         dCCCd     dCCCd         ",
            "         dCCCd     dCCCd         ",
            "         bCCCb     bCCCb         ",
            "  d      bCCCb     bCCCb      d  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbdddddsdsdddddbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "  s     s               s     s  ",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC   d     d   CbC   CbC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "  s     sbbbbbddsddbbbbbs     s  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         ",
            "                                 ",
            "         d   d     d   d         ",
            "         bCCCb     bCCCb         ",
            "  d     sbbbbbddsddbbbbbs     d  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbd    ded    dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "dddd   dddCCCb     bCCCddd   dddd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            " ddd   ddd   d     d   ddd   ddd ",
            "   d   d                 d   d   ",
            " ddd   ddd   d     d   ddd   ddd ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            " ddd   ddd   d     d   ddd   ddd "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC   d     d   CCC   CCC ",
            " CbC   CbC   d     d   CbC   CbC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            " CbC   CbC   d     d   CbC   CbC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d      ded      d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC             CCC   CCC ",
            " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d      ded      d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC             CCC   CCC ",
            " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d     dsdsd     d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbdddbbbd           dbbbdddbbbd",
            " ddd   ddd             ddd   ddd ",
            "   d   d                 d   d   ",
            " ddd   ddd             ddd   ddd ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd "
         }
      )
      .aisle(
         new String[]{
            "  d     d    deeeeed    d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            " dsdddddsddddseedeesddddsdddddsd ",
            "                d                ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  deeeeedeeeededddedeeeedeeeeed  ",
            "               ddd               ",
            "                a                ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s     s               s     s  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s     s               s     s  ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            " dsdddddsddddseedeesddddsdddddsd ",
            "                d                ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d    deeeeed    d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  d     d               d     d  ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d     dsdsd     d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbdddbbbd           dbbbdddbbbd",
            " ddd   ddd             ddd   ddd ",
            "   d   d                 d   d   ",
            " ddd   ddd             ddd   ddd ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd "
         }
      )
      .aisle(
         new String[]{
            "  d     d      ded      d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC             CCC   CCC ",
            " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "  d     d      ded      d     d  ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC             CCC   CCC ",
            " CbC   CbC             CbC   CbC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CbC   CbC             CbC   CbC ",
            " CCC   CCC             CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " CCC   CCC   d     d   CCC   CCC ",
            " CbC   CbC   d     d   CbC   CbC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            " CbC   CbC   d     d   CbC   CbC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbd    ded    dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "dddd   dddCCCb     bCCCddd   dddd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            " ddd   ddd   d     d   ddd   ddd ",
            "   d   d                 d   d   ",
            " ddd   ddd   d     d   ddd   ddd ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            " ddd   ddd   d     d   ddd   ddd "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbdddddsdsdddddbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "  s     s               s     s  ",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC   d     d   CbC   CbC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "  s     sbbbbbddsddbbbbbs     s  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         ",
            "                                 ",
            "         d   d     d   d         ",
            "         bCCCb     bCCCb         ",
            "  d     sbbbbbddsddbbbbbs     d  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbdddbbbd    ded    dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbdddbbbd           dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         dCCCd     dCCCd         ",
            "         dCCCd     dCCCd         ",
            "         dCCCd     dCCCd         ",
            "         bCCCb     bCCCb         ",
            "  d      bCCCb     bCCCb      d  ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   dd    d     d    dd   ddd ",
            "         d   d     d   d         ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "         dCCCd     dCCCd         ",
            "  d      d   d     d   d      d  ",
            "         d   d     d   d         ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "   d   d       ded       d   d   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   d   d                 d   d   ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "   d   d                 d   d   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   d   d                 d   d   ",
            "                                 ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s      dCCCd     dCCCd      s  ",
            "   s                         s   ",
            "                                 ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd     ded     ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            "   C   C                 C   C   ",
            " ddd   dd    d     d    dd   ddd ",
            "         d   d     d   d         ",
            "         dCCCd     dCCCd         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "  s      dCCCd     dCCCd      s  ",
            "   s     d   d     d   d     s   ",
            "         d   d     d   d         ",
            "                                 "
         }
      )
      .aisle(
         new String[]{
            "dbbbdddbbbd    ded    dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            "dbbbdddbbbd           dbbbdddbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbdddbbbd           dbbbdddbbbd",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC             CCCCCCCCC ",
            " CCCCCCCCC   d     d   CCCCCCCCC ",
            "dbbbdddbbdCCCb     bCCCdbbdddbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         dCCCd     dCCCd         ",
            "  s      dCCCd     dCCCd      s  ",
            "  s      dCCCd     dCCCd      s  ",
            "         bCCCb     bCCCb         ",
            "    ss   bCCCb     bCCCb   ss    ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbdddddsdsdddddbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "  s     s               s     s  ",
            " ddd   ddd             ddd   ddd ",
            " ddd   ddd             ddd   ddd ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC             CbC   CbC ",
            " CbC   CbC   d     d   CbC   CbC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "  d     sbbbbbddsddbbbbbs     d  ",
            "  d      bCCCb     bCCCb      d  ",
            "  d      d   d     d   d      d  ",
            "   s                         s   ",
            "   s     d   d     d   d     s   ",
            "    ss   bCCCb     bCCCb   ss    ",
            "      dddbbbbbddsddbbbbbddd      ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            "dbbbd dbbbd    d d    dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            "dbbbd dbbbd           dbbbd dbbbd",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "                                 ",
            "  d     d               d     d  ",
            "  d     d               d     d  ",
            "dbbbd dbbbd           dbbbd dbbbd",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC             CCC   CCC ",
            " CCC   CCC   d     d   CCC   CCC ",
            "dbbbd dbbdCCCb     bCCCdbbd dbbbd",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         ",
            "                                 ",
            "         d   d     d   d         ",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         bCCCb     bCCCb         ",
            "         d   d     d   d         "
         }
      )
      .aisle(
         new String[]{
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd             ddd   ddd ",
            "                                 ",
            "                                 ",
            "                                 ",
            " ddd   ddd   d     d   ddd   ddd ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "                                 ",
            "                                 ",
            "                                 ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "         d   d     d   d         ",
            "                                 "
         }
      );
   public static final MachineDefinition[] SEMI_FLUID_GENERATOR = GTMachines.registerSimpleGenerator(
      "semi_fluid", GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS, GTMachines.genericGeneratorTankSizeFunction, 0.1F, new int[]{1, 2, 3}
   );
   public static final MachineDefinition[] ROCKET_ENGINE_GENERATOR = GTMachines.registerSimpleGenerator(
      "rocket_engine", GTRecipeTypes.get("rocket_engine"), GTMachines.genericGeneratorTankSizeFunction, 0.1F, new int[]{4, 5, 6}
   );
   public static final MachineDefinition[] NAQUADAH_REACTOR_GENERATOR = GTMachines.registerSimpleGenerator(
      "naquadah_reactor", GTRecipeTypes.get("naquadah_reactor"), GTMachines.genericGeneratorTankSizeFunction, 0.1F, new int[]{5, 6, 7}
   );
   public static final MachineDefinition[] LIGHTNING_PROCESSOR = GTMachines.registerSimpleMachines(
      "lightning_processor", GTLRecipeTypes.LIGHTNING_PROCESSOR_RECIPES, GTMachines.defaultTankSizeFunction
   );
   public static final MachineDefinition[] DEHYDRATOR = GTMachines.registerSimpleMachines(
      "dehydrator", GTLRecipeTypes.DEHYDRATOR_RECIPES, GTMachines.defaultTankSizeFunction
   );
   public static final MachineDefinition[] WORLD_DATA_SCANNER = GTMachines.registerSimpleMachines(
      "world_data_scanner", GTLRecipeTypes.WORLD_DATA_SCANNER_RECIPES, tier -> 64000L
   );
   public static final MachineDefinition[] NEUTRON_COMPRESSOR = GTMachines.registerSimpleMachines(
      "neutron_compressor", GTLRecipeTypes.NEUTRON_COMPRESSOR_RECIPES, GTMachines.defaultTankSizeFunction, false, new int[]{14}
   );
   public static final MachineDefinition[] FRAGMENT_WORLD_COLLECTION_MACHINE = GTMachines.registerTieredMachines(
      "fragment_world_collection_machine",
      (holder, tier) -> new SimpleTieredMachine(holder, tier, GTMachines.largeTankSizeFunction, new Object[0]),
      (tier, builder) -> builder.editableUI(
            (EditableMachineUI)SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTLCore.id("fragment_world_collection"), GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION)
         )
         .rotationState(RotationState.NON_Y_AXIS)
         .recipeType(GTLRecipeTypes.FRAGMENT_WORLD_COLLECTION)
         .workableTieredHullRenderer(GTCEu.id("block/machines/fragment_world_collection_machine"))
         .tooltips(new Component[]{Component.m_237115_("gtlcore.machine.fragment_world_collection.tooltip")})
         .compassNode("fragment_world_collection")
         .register(),
      new int[]{0}
   );
   public static final MachineDefinition[] LIGHTNING_ROD = GTMachines.registerTieredMachines(
      "lightning_rod",
      (x$0, x$1) -> new LightningRodMachine(x$0, x$1),
      (tier, builder) -> builder.langValue("%s Lightning Rod %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
         .rotationState(RotationState.NON_Y_AXIS)
         .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTCEu.id("block/generators/lightning_rod")))
         .tooltips(new Component[]{Component.m_237115_("gtceu.machine.lightning_rod.tooltip.0")})
         .tooltips(new Component[]{Component.m_237115_("gtceu.machine.lightning_rod.tooltip.1")})
         .tooltips(new Component[]{Component.m_237115_("gtceu.machine.lightning_rod.tooltip.2")})
         .tooltips(new Component[]{Component.m_237110_("gtceu.universal.tooltip.ampere_out", new Object[]{512})})
         .tooltips(
            new Component[]{
               Component.m_237110_(
                  "gtceu.universal.tooltip.voltage_out", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier - 1]), GTValues.VNF[tier - 1]}
               )
            }
         )
         .tooltips(
            new Component[]{
               Component.m_237110_(
                  "gtceu.universal.tooltip.energy_storage_capacity",
                  new Object[]{FormattingUtil.formatNumbers((long)(48828.0 * Math.pow(4.0, tier.intValue())))}
               )
            }
         )
         .tooltipBuilder(GTL_ADD)
         .register(),
      new int[]{4, 5, 6}
   );
   public static final MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = GTMachines.registerTieredMachines(
      "primitive_magic_energy",
      (x$0, x$1) -> new MagicEnergyMachine(x$0, x$1),
      (tier, builder) -> builder.langValue("%s Primitive Magic Energy %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
         .rotationState(RotationState.NON_Y_AXIS)
         .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTCEu.id("block/generators/primitive_magic_energy")))
         .tooltips(new Component[]{Component.m_237115_("gtceu.machine.primitive_magic_energy.tooltip.0")})
         .tooltips(new Component[]{Component.m_237110_("gtceu.universal.tooltip.ampere_out", new Object[]{16})})
         .tooltips(
            new Component[]{
               Component.m_237110_("gtceu.universal.tooltip.voltage_out", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier]), GTValues.VNF[tier]})
            }
         )
         .tooltips(
            new Component[]{
               Component.m_237110_("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{FormattingUtil.formatNumbers(GTValues.V[tier] * 512L)})
            }
         )
         .tooltipBuilder(GTL_ADD)
         .register(),
      new int[]{0, 1}
   );
   public static final MachineDefinition LARGE_STEAM_HATCH = GTRegistration.REGISTRATE
      .machine("large_steam_input_hatch", holder -> new LargeSteamHatchPartMachine(holder, IO.IN, 8192, false))
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.STEAM})
      .overlaySteamHullRenderer("steam_hatch")
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.large_steam_input_hatch.tooltip.0"),
            Component.m_237110_("gtceu.universal.tooltip.fluid_storage_capacity", new Object[]{16384L * FluidHelper.getBucket()}),
            Component.m_237115_("gtceu.machine.steam.steam_hatch.tooltip")
         }
      )
      .tooltipBuilder(GTL_ADD)
      .compassSections(new CompassSection[]{GTCompassSections.STEAM})
      .compassNode("steam_hatch")
      .register();
   public static final MachineDefinition STERILE_CLEANING_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine("sterile_cleaning_maintenance_hatch", holder -> new GTLCleaningMaintenanceHatchPartMachine(holder, ICleaningRoom.STERILE_DUMMY_CLEANROOM))
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.STERILE_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(7, GTCEu.id("block/machine/part/maintenance.sterile_cleaning")))
      .register();
   public static final MachineDefinition LAW_CLEANING_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine("law_cleaning_maintenance_hatch", holder -> new GTLCleaningMaintenanceHatchPartMachine(holder, ICleaningRoom.LAW_DUMMY_CLEANROOM))
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.LAW_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(10, GTCEu.id("block/machine/part/maintenance.law_cleaning")))
      .register();
   public static final MachineDefinition AUTO_CONFIGURATION_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine("auto_configuration_maintenance_hatch", AutoConfigurationMaintenanceHatchPartMachine::new)
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(new Component[]{Component.m_237115_("gtceu.universal.disabled")})
      .tooltipBuilder(GTL_ADD)
      .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.full_auto")))
      .register();
   public static final MachineDefinition CLEANING_CONFIGURATION_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "cleaning_configuration_maintenance_hatch", holder -> new CleaningConfigurationMaintenanceHatchPartMachine(holder, ICleaningRoom.DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.cleaning")))
      .register();
   public static final MachineDefinition STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "sterile_configuration_cleaning_maintenance_hatch",
         holder -> new CleaningConfigurationMaintenanceHatchPartMachine(holder, ICleaningRoom.STERILE_DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.STERILE_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(9, GTCEu.id("block/machine/part/maintenance.sterile_cleaning")))
      .register();
   public static final MachineDefinition LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "law_configuration_cleaning_maintenance_hatch",
         holder -> new CleaningConfigurationMaintenanceHatchPartMachine(holder, ICleaningRoom.LAW_DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.LAW_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(12, GTCEu.id("block/machine/part/maintenance.law_cleaning")))
      .register();
   public static final MachineDefinition GRAVITY_HATCH = GTRegistration.REGISTRATE
      .machine("gravity_hatch", holder -> new GravityCleaningConfigurationMaintenancePartMachine(holder, false))
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(new Component[]{Component.m_237115_("gtceu.universal.disabled")})
      .tooltipBuilder(GTL_ADD)
      .renderer(() -> new MaintenanceHatchPartRenderer(8, GTCEu.id("block/machine/part/maintenance.full_auto")))
      .register();
   public static final MachineDefinition GRAVITY_CONFIGURATION_HATCH = GTRegistration.REGISTRATE
      .machine("gravity_configuration_hatch", GravityCleaningConfigurationMaintenancePartMachine::new)
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(new Component[]{Component.m_237115_("gtceu.universal.disabled")})
      .tooltipBuilder(GTL_ADD)
      .renderer(() -> new MaintenanceHatchPartRenderer(9, GTCEu.id("block/machine/part/maintenance.full_auto")))
      .register();
   public static final MachineDefinition CLEANING_GRAVITY_CONFIGURATION_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "cleaning_gravity_configuration_maintenance_hatch",
         holder -> new GravityCleaningConfigurationMaintenancePartMachine(holder, ICleaningRoom.DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(10, GTCEu.id("block/machine/part/maintenance.cleaning")))
      .register();
   public static final MachineDefinition STERILE_CLEANING_GRAVITY_CONFIGURATION_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "sterile_cleaning_gravity_configuration_maintenance_hatch",
         holder -> new GravityCleaningConfigurationMaintenancePartMachine(holder, ICleaningRoom.STERILE_DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.STERILE_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(11, GTCEu.id("block/machine/part/maintenance.cleaning")))
      .register();
   public static final MachineDefinition LAW_CLEANING_GRAVITY_CONFIGURATION_MAINTENANCE_HATCH = GTRegistration.REGISTRATE
      .machine(
         "law_cleaning_gravity_configuration_maintenance_hatch",
         holder -> new GravityCleaningConfigurationMaintenancePartMachine(holder, ICleaningRoom.LAW_DUMMY_CLEANROOM)
      )
      .rotationState(RotationState.ALL)
      .abilities(new PartAbility[]{PartAbility.MAINTENANCE})
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.universal.disabled"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
            Component.m_237115_("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1")
         }
      )
      .tooltipBuilder((stack, tooltips) -> {
         UnmodifiableIterator var2 = ICleaningRoom.getCleanroomTypes(ICleaningRoom.LAW_DUMMY_CLEANROOM).iterator();

         while (var2.hasNext()) {
            CleanroomType type = (CleanroomType)var2.next();
            tooltips.add(Component.m_237113_("  ").m_7220_(Component.m_237115_(type.getTranslationKey()).m_130940_(ChatFormatting.GREEN)));
         }

         GTL_ADD.accept(stack, tooltips);
      })
      .renderer(() -> new MaintenanceHatchPartRenderer(12, GTCEu.id("block/machine/part/maintenance.cleaning")))
      .register();
   public static final MachineDefinition WIRELESS_DATA_HATCH_TRANSMITTER = GTRegistration.REGISTRATE
      .machine("wireless_data_transmitter_hatch", holder -> new WirelessOpticalDataHatchMachine(holder, true))
      .rotationState(RotationState.ALL)
      .compassNodeSelf()
      .abilities(new PartAbility[]{PartAbility.OPTICAL_DATA_TRANSMISSION})
      .overlayTieredHullRenderer("optical_data_hatch")
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.wireless_data_transmitter_hatch.tooltip.1"),
            Component.m_237115_("gtceu.machine.wireless_data_transmitter_hatch.tooltip.2")
         }
      )
      .tier(6)
      .register();
   public static final MachineDefinition WIRELESS_DATA_HATCH_RECEIVER = GTRegistration.REGISTRATE
      .machine("wireless_data_receiver_hatch", holder -> new WirelessOpticalDataHatchMachine(holder, false))
      .rotationState(RotationState.ALL)
      .compassNodeSelf()
      .abilities(new PartAbility[]{PartAbility.OPTICAL_DATA_RECEPTION})
      .overlayTieredHullRenderer("optical_data_hatch")
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.tooltip.1"),
            Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.tooltip.2")
         }
      )
      .tier(6)
      .register();
   public static final MachineDefinition PERFORMANCE_MONITOR = GTRegistration.REGISTRATE
      .machine("performance_monitor", PerformanceMonitorMachine::new)
      .rotationState(RotationState.NON_Y_AXIS)
      .compassNodeSelf()
      .workableTieredHullRenderer(GTMThings.id("block/machines/wireless_energy_monitor"))
      .tier(1)
      .tooltips(new Component[]{Component.m_237115_("block.gtlcore.performance_monitor.tooltip")})
      .tooltipBuilder(GTL_ADD)
      .register();
   public static final MachineDefinition[] HUGE_FLUID_IMPORT_HATCH = registerHugeFluidHatches(
      "huge_input_hatch", "Huge Input Hatch", "fluid_hatch.import", "fluid_hatch.import", IO.IN, PartAbility.IMPORT_FLUIDS
   );
   public static final MachineDefinition[] HUGE_FLUID_EXPORT_HATCH = registerHugeFluidHatches(
      "huge_output_hatch", "Huge Output Hatch", "fluid_hatch.export", "fluid_hatch.export", IO.OUT, PartAbility.EXPORT_FLUIDS
   );
   public static final MachineDefinition[] LASER_INPUT_HATCH_16384 = GTMachines.registerLaserHatch(IO.IN, 16384, PartAbility.INPUT_LASER);
   public static final MachineDefinition[] LASER_OUTPUT_HATCH_16384 = GTMachines.registerLaserHatch(IO.OUT, 16384, PartAbility.OUTPUT_LASER);
   public static final MachineDefinition[] LASER_INPUT_HATCH_65536 = GTMachines.registerLaserHatch(IO.IN, 65536, PartAbility.INPUT_LASER);
   public static final MachineDefinition[] LASER_OUTPUT_HATCH_65536 = GTMachines.registerLaserHatch(IO.OUT, 65536, PartAbility.OUTPUT_LASER);
   public static final MachineDefinition[] LASER_INPUT_HATCH_262144 = GTMachines.registerLaserHatch(IO.IN, 262144, PartAbility.INPUT_LASER);
   public static final MachineDefinition[] LASER_OUTPUT_HATCH_262144 = GTMachines.registerLaserHatch(IO.OUT, 262144, PartAbility.OUTPUT_LASER);
   public static final MachineDefinition[] LASER_INPUT_HATCH_1048576 = GTMachines.registerLaserHatch(IO.IN, 1048576, PartAbility.INPUT_LASER);
   public static final MachineDefinition[] LASER_OUTPUT_HATCH_1048576 = GTMachines.registerLaserHatch(IO.OUT, 1048576, PartAbility.OUTPUT_LASER);
   public static final MachineDefinition[] LASER_INPUT_HATCH_4194304 = GTMachines.registerLaserHatch(IO.IN, 4194304, PartAbility.INPUT_LASER);
   public static final MachineDefinition[] LASER_OUTPUT_HATCH_4194304 = GTMachines.registerLaserHatch(IO.OUT, 4194304, PartAbility.OUTPUT_LASER);
   public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_64A = WirelessMachines.registerWirelessEnergyHatch(
      IO.IN, 64, PartAbility.INPUT_ENERGY, GTValues.tiersBetween(4, 14)
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_64A = WirelessMachines.registerWirelessEnergyHatch(
      IO.OUT, 64, PartAbility.OUTPUT_ENERGY, GTValues.tiersBetween(4, 14)
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_262144A = WirelessMachines.registerWirelessLaserHatch(
      IO.IN, 262144, PartAbility.INPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_1048576A = WirelessMachines.registerWirelessLaserHatch(
      IO.IN, 1048576, PartAbility.INPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_INPUT_HATCH_4194304A = WirelessMachines.registerWirelessLaserHatch(
      IO.IN, 4194304, PartAbility.INPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_262144A = WirelessMachines.registerWirelessLaserHatch(
      IO.OUT, 262144, PartAbility.OUTPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_1048576A = WirelessMachines.registerWirelessLaserHatch(
      IO.OUT, 1048576, PartAbility.OUTPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] WIRELESS_ENERGY_OUTPUT_HATCH_4194304A = WirelessMachines.registerWirelessLaserHatch(
      IO.OUT, 4194304, PartAbility.OUTPUT_LASER, WirelessMachines.WIRELL_ENERGY_HIGH_TIERS
   );
   public static final MachineDefinition[] NEUTRON_ACCELERATOR = GTMachines.registerTieredMachines(
      "neutron_accelerator",
      (x$0, x$1) -> new NeutronAcceleratorPartMachine(x$0, x$1),
      (tier, builder) -> builder.langValue(GTValues.VNF[tier] + "Neutron Accelerator")
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{GTLPartAbility.NEUTRON_ACCELERATOR})
         .tooltips(
            new Component[]{
               Component.m_237110_("gtceu.universal.tooltip.max_voltage_in", new Object[]{GTValues.V[tier], GTValues.VNF[tier]}),
               Component.m_237110_("gtceu.machine.neutron_accelerator.tooltip.0", new Object[]{GTValues.V[tier] * 8L / 10L}),
               Component.m_237115_("gtceu.machine.neutron_accelerator.tooltip.1"),
               Component.m_237110_("gtceu.universal.tooltip.energy_storage_capacity", new Object[]{2L * GTValues.V[tier]})
            }
         )
         .tooltipBuilder(GTL_ADD)
         .overlayTieredHullRenderer("neutron_accelerator")
         .compassNode("neutron_accelerator")
         .register(),
      GTMachines.ALL_TIERS
   );
   public static final MachineDefinition NEUTRON_SENSOR = GTRegistration.REGISTRATE
      .machine("neutron_sensor", NeutronSensorPartMachine::new)
      .langValue("Neutron Sensor")
      .tier(5)
      .rotationState(RotationState.ALL)
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.neutron_sensor.tooltip.0")})
      .tooltipBuilder(GTL_ADD)
      .overlayTieredHullRenderer("neutron_sensor")
      .register();
   public static final MachineDefinition HEAT_SENSOR = GTRegistration.REGISTRATE
      .machine("heat_sensor", TemperatureSensorPartMachine::new)
      .langValue("Temperature Sensor")
      .tier(12)
      .rotationState(RotationState.ALL)
      .tooltips(new Component[]{Component.m_237115_("gtceu.machine.heat_sensor.tooltip.0")})
      .tooltipBuilder(GTL_ADD)
      .overlayTieredHullRenderer("neutron_sensor")
      .register();
   public static final MachineDefinition ROTOR_HATCH = GTRegistration.REGISTRATE
      .machine("rotor_hatch", x$0 -> new RotorHatchPartMachine(x$0))
      .langValue("Rotor Hatch")
      .tier(4)
      .rotationState(RotationState.ALL)
      .overlayTieredHullRenderer("rotor_hatch")
      .register();
   public static final MachineDefinition BLOCK_BUS = GTRegistration.REGISTRATE
      .machine("block_bus", BlockBusPartMachine::new)
      .tier(6)
      .rotationState(RotationState.ALL)
      .renderer(() -> new OverlayTieredMachineRenderer(6, GTCEu.id("block/machine/part/item_bus.import")))
      .register();
   public static final MachineDefinition TAG_FILTER_ME_STOCK_BUS_PART_MACHINE = GTRegistration.REGISTRATE
      .machine("tag_filter_me_stock_bus_part_machine", x$0 -> new TagFilterMEStockBusPartMachine(x$0))
      .tier(6)
      .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS})
      .rotationState(RotationState.ALL)
      .renderer(() -> new OverlayTieredMachineRenderer(6, GTCEu.id("block/machine/part/me_item_bus.import")))
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.item_bus.import.tooltip"),
            Component.m_237115_("gtceu.machine.me.item_import.tooltip"),
            Component.m_237115_("gtceu.machine.me.copy_paste.tooltip"),
            Component.m_237115_("gtceu.universal.enabled")
         }
      )
      .compassNode("item_bus")
      .register();
   public static final MachineDefinition ME_DUAL_HATCH_STOCK_PART_MACHINE = GTRegistration.REGISTRATE
      .machine("me_dual_hatch_stock_part_machine", x$0 -> new MEDualHatchStockPartMachine(x$0))
      .tier(6)
      .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS})
      .rotationState(RotationState.ALL)
      .renderer(() -> new OverlayTieredMachineRenderer(6, GTCEu.id("block/machine/part/me_pattern_buffer")))
      .tooltips(
         new Component[]{
            Component.m_237115_("gtceu.machine.dual_hatch.import.tooltip"),
            Component.m_237115_("gtceu.machine.me.item_import.tooltip"),
            Component.m_237115_("gtceu.universal.enabled")
         }
      )
      .compassNode("dual_hatch")
      .register();

   public static void init() {
      TootipsModify.init();
      ShapeInfosModify.init();
      GeneratorMachine.init();
      MultiBlockMachineA.init();
      AdvancedMultiBlockMachine.init();
      AdditionalMultiBlockMachine.init();
      GTLMachines.GTAEMachines.init();
   }

   private static MachineDefinition[] registerHugeFluidHatches(String name, String displayname, String model, String tooltip, IO io, PartAbility... abilities) {
      return GTMachines.registerTieredMachines(
         name,
         (holder, tier) -> new HugeFluidHatchPartMachine(holder, tier, io),
         (tier, builder) -> {
            builder.langValue(GTValues.VNF[tier] + " " + displayname)
               .rotationState(RotationState.ALL)
               .overlayTieredHullRenderer(model)
               .abilities(abilities)
               .compassNode("fluid_hatch")
               .tooltips(new Component[]{Component.m_237115_("gtceu.machine." + tooltip + ".tooltip")});
            builder.tooltips(
                  new Component[]{
                     Component.m_237110_("gtceu.universal.tooltip.fluid_storage_capacity_mult", new Object[]{tier, FormattingUtil.formatNumbers(2147483647)})
                  }
               )
               .tooltipBuilder(GTL_ADD);
            return builder.register();
         },
         GTValues.tiersBetween(1, 13)
      );
   }

   static {
      GTRegistration.REGISTRATE.creativeModeTab(() -> GTCreativeModeTabs.MACHINE);
      GTMTRegistration.GTMTHINGS_REGISTRATE.creativeModeTab(() -> CreativeModeTabs.WIRELESS_TAB);
   }

   public static class GTAEMachines {
      public static final MachineDefinition ITEM_IMPORT_BUS_ME = GTRegistration.REGISTRATE
         .machine("me_input_bus", x$0 -> new MEInputBusPartMachine(x$0, new Object[0]))
         .langValue("ME Input Bus")
         .tier(4)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS})
         .overlayTieredHullRenderer("me_item_bus.import")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.item_bus.import.tooltip"),
               Component.m_237115_("gtceu.machine.me.item_import.tooltip"),
               Component.m_237115_("gtceu.machine.me.copy_paste.tooltip"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("item_bus")
         .register();
      public static final MachineDefinition STOCKING_IMPORT_BUS_ME = GTRegistration.REGISTRATE
         .machine("me_stocking_input_bus", x$0 -> new MEStockingBusPartMachine(x$0, new Object[0]))
         .langValue("ME Stocking Input Bus")
         .tier(6)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS})
         .overlayTieredHullRenderer("me_item_bus.import")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.item_bus.import.tooltip"),
               Component.m_237115_("gtceu.machine.me.stocking_item.tooltip.0"),
               Component.m_237115_("gtceu.machine.me_import_item_hatch.configs.tooltip"),
               Component.m_237115_("gtceu.machine.me.copy_paste.tooltip"),
               Component.m_237115_("gtceu.machine.me.stocking_item.tooltip.1"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("item_bus")
         .register();
      public static final MachineDefinition ITEM_EXPORT_BUS_ME = GTRegistration.REGISTRATE
         .machine("me_output_bus", x$0 -> new MEOutputBusPartMachine(x$0, new Object[0]))
         .langValue("ME Output Bus")
         .tier(4)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS})
         .overlayTieredHullRenderer("me_item_bus.export")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.item_bus.export.tooltip"),
               Component.m_237115_("gtceu.machine.me.item_export.tooltip"),
               Component.m_237115_("gtceu.machine.me.export.tooltip"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("item_bus")
         .register();
      public static final MachineDefinition FLUID_IMPORT_HATCH_ME = GTRegistration.REGISTRATE
         .machine("me_input_hatch", x$0 -> new MEInputHatchPartMachine(x$0, new Object[0]))
         .langValue("ME Input Hatch")
         .tier(4)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_FLUIDS})
         .overlayTieredHullRenderer("me_fluid_hatch.import")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.fluid_hatch.import.tooltip"),
               Component.m_237115_("gtceu.machine.me.fluid_import.tooltip"),
               Component.m_237115_("gtceu.machine.me.copy_paste.tooltip"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("fluid_hatch")
         .register();
      public static final MachineDefinition STOCKING_IMPORT_HATCH_ME = GTRegistration.REGISTRATE
         .machine("me_stocking_input_hatch", x$0 -> new MEStockingHatchPartMachine(x$0, new Object[0]))
         .langValue("ME Stocking Input Hatch")
         .tier(6)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_FLUIDS})
         .overlayTieredHullRenderer("me_fluid_hatch.import")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.fluid_hatch.import.tooltip"),
               Component.m_237115_("gtceu.machine.me.stocking_fluid.tooltip.0"),
               Component.m_237115_("gtceu.machine.me_import_fluid_hatch.configs.tooltip"),
               Component.m_237115_("gtceu.machine.me.copy_paste.tooltip"),
               Component.m_237115_("gtceu.machine.me.stocking_fluid.tooltip.1"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("fluid_hatch")
         .register();
      public static final MachineDefinition FLUID_EXPORT_HATCH_ME = GTRegistration.REGISTRATE
         .machine("me_output_hatch", x$0 -> new MEOutputHatchPartMachine(x$0, new Object[0]))
         .langValue("ME Output Hatch")
         .tier(4)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.EXPORT_FLUIDS})
         .overlayTieredHullRenderer("me_fluid_hatch.export")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.machine.fluid_hatch.export.tooltip"),
               Component.m_237115_("gtceu.machine.me.fluid_export.tooltip"),
               Component.m_237115_("gtceu.machine.me.export.tooltip"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .compassNode("fluid_hatch")
         .register();
      public static final MachineDefinition ME_MINI_PATTERN_BUFFER = GTRegistration.REGISTRATE
         .machine("me_mini_pattern_buffer", h -> new MEPatternBufferPartMachine(h, 9, IO.IN))
         .tier(5)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS})
         .overlayTieredHullRenderer("me_mini_pattern_buffer")
         .langValue("ME Mini Pattern Buffer")
         .tooltips(
            new Component[]{
               Component.m_237115_("block.gtceu.pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.1"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.3"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.4"),
               Component.m_237115_("gtceu.machine.me_mini_pattern_buffer.desc.0"),
               Component.m_237115_("block.gtceu.pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_EXTEND_PATTERN_BUFFER = GTRegistration.REGISTRATE
         .machine("me_extend_pattern_buffer", h -> new MEPatternBufferPartMachine(h, 36, IO.BOTH))
         .tier(8)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS})
         .overlayTieredHullRenderer("me_pattern_buffer")
         .langValue("ME Extend Pattern Buffer")
         .tooltips(
            new Component[]{
               Component.m_237115_("block.gtceu.pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.1"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.3"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.4"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.5"),
               Component.m_237115_("block.gtceu.pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_FINAL_PATTERN_BUFFER = GTRegistration.REGISTRATE
         .machine("me_final_pattern_buffer", h -> new MEPatternBufferPartMachine(h, 72, IO.BOTH))
         .tier(10)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_ITEMS, PartAbility.EXPORT_FLUIDS})
         .overlayTieredHullRenderer("me_pattern_buffer")
         .langValue("ME Final Pattern Buffer")
         .tooltips(
            new Component[]{
               Component.m_237115_("tooltip.gtlcore.bigger_stronger").m_130940_(ChatFormatting.GOLD),
               Component.m_237115_("block.gtceu.pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.0"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.1"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.3"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.4"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer.desc.5"),
               Component.m_237115_("block.gtceu.pattern_buffer.desc.2"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_PATTERN_BUFFER_PROXY = GTRegistration.REGISTRATE
         .machine("me_pattern_buffer_proxy", MEPatternBufferProxyPartMachine::new)
         .tier(8)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS, PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS})
         .overlayTieredHullRenderer("me_pattern_buffer_proxy")
         .langValue("ME Pattern Buffer Proxy")
         .tooltips(
            new Component[]{
               Component.m_237115_("block.gtceu.pattern_buffer_proxy.desc.0"),
               Component.m_237115_("block.gtceu.pattern_buffer_proxy.desc.1"),
               Component.m_237115_("block.gtceu.pattern_buffer_proxy.desc.2"),
               Component.m_237115_("gtceu.machine.me_pattern_buffer_proxy.desc.0"),
               Component.m_237115_("gtceu.universal.enabled")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_MODIFY)
         .register();
      public static final MachineDefinition ME_EXTENDED_EXPORT_BUFFER = GTRegistration.REGISTRATE
         .machine("me_extended_export_buffer", MEExtendedOutputPartMachine::new)
         .tier(9)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS})
         .overlayTieredHullRenderer("me_extended_export_buffer")
         .langValue("ME Extended Export Buffer")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtmthings.machine.me_export_buffer.tooltip"), Component.m_237115_("gtceu.machine.me_extended_export_buffer.tooltip.0")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_EXTENDED_ASYNC_EXPORT_BUFFER = GTRegistration.REGISTRATE
         .machine("me_extended_async_export_buffer", MEExtendedAsyncOutputPartMachine::new)
         .tier(9)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{PartAbility.EXPORT_FLUIDS, PartAbility.EXPORT_ITEMS})
         .overlayTieredHullRenderer("me_extended_export_buffer")
         .langValue("ME Extended Async Export Buffer")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtmthings.machine.me_export_buffer.tooltip"),
               Component.m_237115_("gtceu.machine.me_extended_async_export_buffer.tooltip.0"),
               Component.m_237115_("gtceu.machine.me_extended_async_export_buffer.tooltip.1")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_CRAFT_SPEED_CORE = GTRegistration.REGISTRATE
         .machine("me_craft_speed_core", MECraftSpeedCorePartMachine::new)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{GTLPartAbility.MOLECULAR_ASSEMBLER_MATRIX})
         .workableCasingRenderer(GTCEu.id("block/casings/speed_core_casing"), GTCEu.id("block/casings/speed_core_casing"))
         .langValue("ME CRAFT Speed Core")
         .tooltips(new Component[]{Component.m_237115_("gtceu.universal.disabled"), Component.m_237115_("gtceu.machine.me_craft_speed_core.tooltip.0")})
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_CRAFT_PARALLEL_CORE = GTRegistration.REGISTRATE
         .machine("me_craft_parallel_core", MECraftParallelCorePartMachine::new)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{GTLPartAbility.MOLECULAR_ASSEMBLER_MATRIX})
         .workableCasingRenderer(GTCEu.id("block/casings/crafter_core_casing"), GTCEu.id("block/casings/crafter_core_casing"))
         .langValue("ME CRAFT Parallel Core")
         .tooltips(new Component[]{Component.m_237115_("gtceu.universal.disabled"), Component.m_237115_("gtceu.machine.me_craft_parallel_core.tooltip.0")})
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_CRAFT_PATTERN_CONTAINER = GTRegistration.REGISTRATE
         .machine("me_craft_pattern_container", MECraftPatternContainerPartMachine::new)
         .rotationState(RotationState.ALL)
         .abilities(new PartAbility[]{GTLPartAbility.MOLECULAR_ASSEMBLER_MATRIX})
         .workableCasingRenderer(GTCEu.id("block/casings/pattern_core_casing"), GTCEu.id("block/casings/pattern_core_casing"))
         .langValue("ME Craft Pattern Container")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.universal.disabled"),
               Component.m_237110_(
                  "gtceu.machine.me_craft_pattern_core.tooltip",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(108)).m_130940_(ChatFormatting.GOLD)}
               )
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();
      public static final MachineDefinition ME_MOLECULAR_ASSEMBLER_IO = GTRegistration.REGISTRATE
         .machine("me_molecular_assembler_io", MEMolecularAssemblerIOPartMachine::new)
         .tier(8)
         .rotationState(RotationState.ALL)
         .overlayTieredHullRenderer("me_pattern_buffer")
         .langValue("ME Molecular Assembler IO")
         .tooltips(
            new Component[]{
               Component.m_237115_("gtceu.universal.disabled"),
               Component.m_237115_("gtceu.machine.me_molecular_assembler_io.tooltip.0"),
               Component.m_237115_("gtceu.machine.me_molecular_assembler_io.tooltip.1")
            }
         )
         .tooltipBuilder(GTLMachines.GTL_ADD)
         .register();

      public static void init() {
      }
   }
}
