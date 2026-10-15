package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.gtlcore.gtlcore.common.machine.multiblock.part.TemperatureSensorPartMachine;
import org.gtlcore.gtlcore.common.machine.trait.AdvancedInfiniteDrillLogic;
import org.gtlcore.gtlcore.utils.MachineUtil;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AdvancedInfiniteDrillMachine extends StorageMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      AdvancedInfiniteDrillMachine.class, StorageMachine.MANAGED_FIELD_HOLDER
   );
   private static final FluidStack DISTILLED_WATER = GTMaterials.DistilledWater.getFluid(20000L);
   private static final FluidStack OXYGEN = GTMaterials.Oxygen.getFluid(FluidStorageKeys.LIQUID, 20000L);
   private static final FluidStack HELIUM = GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 20000L);
   private static final FluidStack NITROGEN = GTMaterials.Nitrogen.getFluid(20000L);
   protected static final Map<Item, Integer> DRILLS = Map.of(
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTMaterials.Neutronium).m_41720_(),
      50000,
      ChemicalHelper.get(TagPrefix.toolHeadDrill, GTLMaterials.Vibranium).m_41720_(),
      100000
   );
   protected static final Map<Material, Integer> TEMPERATURE = Map.of(
      GTLMaterials.TitanSteel,
      0,
      GTLMaterials.Adamantine,
      1,
      GTLMaterials.NaquadriaticTaranium,
      2,
      GTLMaterials.Starmetal,
      3,
      GTLMaterials.Infinity,
      4,
      GTLMaterials.Hypogen,
      6,
      GTLMaterials.Eternity,
      8
   );
   public static final double RUNNING_HEAT = 2000.0;
   public int maxHeat = -1;
   @Persisted
   protected int currentHeat = 300;
   @Persisted
   protected int process = 0;
   @Persisted
   protected boolean fast;
   protected ConditionalSubscriptionHandler heatSubs = new ConditionalSubscriptionHandler(this, this::heatUpdate, this::isFormed);
   private TemperatureSensorPartMachine sensorMachines;

   @NotNull
   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public AdvancedInfiniteDrillMachine(IMachineBlockEntity holder) {
      super(holder, 1);
   }

   protected RecipeLogic createRecipeLogic(Object... args) {
      return new AdvancedInfiniteDrillLogic(this, 5);
   }

   protected void heatUpdate() {
      if (this.fast || this.getOffsetTimer() % 5L == 0L) {
         if (!this.isEmpty() && this.maxHeat != -1) {
            int heat = 0;
            if (this.getRecipeLogic().isWorking() && this.process <= 0) {
               heat += (int)Math.floor(Math.abs(this.currentHeat - 2000.0) / 2000.0);
            }

            if (this.getRecipeLogic().isWorking() || this.process > 0) {
               if (MachineUtil.inputFluid(this, DISTILLED_WATER)) {
                  heat--;
               } else if (MachineUtil.inputFluid(this, OXYGEN)) {
                  heat -= 2;
               } else if (MachineUtil.inputFluid(this, HELIUM)) {
                  heat -= 4;
               } else if (MachineUtil.inputFluid(this, NITROGEN)) {
                  heat -= 8;
               }
            }

            if (this.inputBlast()) {
               heat++;
            }

            this.currentHeat = Math.max(0, this.currentHeat + heat);
            if (this.currentHeat > this.maxHeat) {
               this.process = this.process + (this.fast ? 1 : 5);
               if (this.process >= 300) {
                  this.process = 0;
                  this.currentHeat = 300;
                  this.machineStorage.setStackInSlot(0, ItemStack.f_41583_);
                  this.getRecipeLogic().interruptRecipe();
               }
            } else if (this.process > 0) {
               this.process--;
            }

            if (this.sensorMachines != null) {
               this.sensorMachines.update(this.currentHeat);
            }
         }
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (this.getMultiblockState().getMatchContext().get("CoilType") instanceof ICoilType coil) {
         Material material = coil.getMaterial();
         int rate = TEMPERATURE.getOrDefault(material, -1);
         if (rate < 0) {
            this.maxHeat = -1;
         } else {
            this.maxHeat = 8000 + 1000 * rate;
         }
      }

      for (IMultiPart part : this.getParts()) {
         if (part instanceof TemperatureSensorPartMachine temperatureSensorPartMachine) {
            this.sensorMachines = temperatureSensorPartMachine;
            break;
         }
      }

      this.heatSubs.initialize(this.getLevel());
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.sensorMachines = null;
   }

   @Override
   protected boolean filter(ItemStack itemStack) {
      return DRILLS.containsKey(itemStack.m_41720_());
   }

   public int getRate() {
      double heat = this.computeTempEfficiency();
      double volt = this.computeVoltageFactor();
      double overclocking = (this.maxHeat - 12000) / 1000.0 * 0.125;
      return (int)(this.getDrillHeadRate() * (1.0 + overclocking) * heat * volt);
   }

   private double computeTempEfficiency() {
      double normalizedTemp = (this.currentHeat - 2000.0) / (this.maxHeat - 2000.0);
      if (normalizedTemp < 0.4) {
         return 0.3 * Math.sqrt(normalizedTemp / 0.4);
      }

      double hyperFactor = (normalizedTemp - 0.4) / 0.6;
      return 0.3 + 0.7 * Math.pow(hyperFactor, 2.5);
   }

   private double computeVoltageFactor() {
      double voltage = (GTUtil.getTierByVoltage(this.getMaxVoltage()) - 9) / 5.0;
      double clampedVoltage = Math.max(0.0, Math.min(voltage, 1.0));
      double tempRatio = (this.currentHeat - 2000.0) / (this.maxHeat - 2000.0);
      double decayFactor = 1.0 - 0.2 * tempRatio;
      return Math.pow(clampedVoltage, 0.6 * decayFactor) * (1.0 - 0.05 * (1.0 - tempRatio));
   }

   public AdvancedInfiniteDrillLogic getRecipeLogic() {
      return (AdvancedInfiniteDrillLogic)super.getRecipeLogic();
   }

   public void onWaiting() {
      super.onWaiting();
      this.getRecipeLogic().resetRecipeLogic();
   }

   public void addDisplayText(List<Component> tooltips) {
      if (this.isFormed()) {
         MultiblockDisplayText.builder(tooltips, true)
            .setWorkingStatus(this.isWorkingEnabled(), this.isActive())
            .addIdlingLine(false)
            .addEnergyUsageLine(this.energyContainer);
         if (this.isEmpty()) {
            tooltips.add(Component.m_237115_("gtceu.machine.advanced_infinite_driller.not_fluid_head").m_130940_(ChatFormatting.RED));
         } else if (this.maxHeat == -1) {
            tooltips.add(Component.m_237115_("gtceu.machine.advanced_infinite_driller.no_coil").m_130940_(ChatFormatting.RED));
         } else {
            tooltips.add(
               Component.m_237110_("gtceu.machine.advanced_infinite_driller.range", new Object[]{Component.m_237113_("5x5").m_130940_(ChatFormatting.GOLD)})
            );
            tooltips.add(Component.m_237110_("gtceu.machine.advanced_infinite_driller.heat", new Object[]{this.maxHeat, 2000.0}));
            tooltips.add(Component.m_237110_("gtceu.machine.advanced_infinite_driller.current_heat", new Object[]{this.currentHeat}));
            tooltips.add(
               Component.m_237110_(
                  "gtceu.machine.advanced_infinite_driller.fast",
                  new Object[]{
                     ComponentPanelWidget.withButton(Component.m_237115_(this.fast ? "gui.gtlcore.status_on" : "gui.gtlcore.status_off"), "fast_mode")
                  }
               )
            );
            tooltips.add(
               Component.m_237110_(
                     "gtceu.machine.advanced_infinite_driller.process", new Object[]{FormattingUtil.formatNumber2Places(this.process / 300.0F * 100.0F)}
                  )
                  .m_130946_("%")
            );
            Map<Fluid, Long> fluids = this.getRecipeLogic().getVeinFluids();
            if (!fluids.isEmpty()) {
               fluids.forEach((fluid, produced) -> {
                  Component fluidInfo = fluid.getFluidType().getDescription().m_6881_().m_130940_(ChatFormatting.GREEN);
                  Component amountInfo = Component.m_237113_(FormattingUtil.formatNumbers(produced * this.getRate()) + " mB/s").m_130940_(ChatFormatting.BLUE);
                  tooltips.add(Component.m_237110_("gtceu.machine.advanced_infinite_driller.drilled_fluid", new Object[]{fluidInfo, amountInfo}));
               });
            } else {
               Component noFluid = Component.m_237115_("gtceu.multiblock.fluid_rig.no_fluid_in_area").m_130940_(ChatFormatting.RED);
               tooltips.add(Component.m_237110_("gtceu.multiblock.fluid_rig.drilled_fluid", new Object[]{noFluid}).m_130940_(ChatFormatting.GRAY));
            }
         }
      } else {
         Component tooltip = Component.m_237115_("gtceu.multiblock.invalid_structure.tooltip").m_130940_(ChatFormatting.GRAY);
         tooltips.add(
            Component.m_237115_("gtceu.multiblock.invalid_structure")
               .m_130948_(Style.f_131099_.m_131140_(ChatFormatting.RED).m_131144_(new HoverEvent(Action.f_130831_, tooltip)))
         );
      }
   }

   public void handleDisplayClick(String componentData, ClickData clickData) {
      if (componentData.equals("fast_mode")) {
         this.fast = !this.fast;
      }
   }

   public int getDrillHeadRate() {
      ItemStack itemStack = this.getMachineStorageItem();
      if (!itemStack.m_41619_()) {
         Item item = itemStack.m_41720_();
         return DRILLS.getOrDefault(item, 0);
      } else {
         return 0;
      }
   }

   protected boolean inputBlast() {
      return MachineUtil.inputFluid(this, GTMaterials.Blaze.getFluid(this.getFluidConsume()));
   }

   protected long getFluidConsume() {
      return (long)Math.pow(this.currentHeat, 1.3);
   }

   public boolean canRunnable() {
      return this.currentHeat >= 2000.0;
   }

   public int getCurrentHeat() {
      return this.currentHeat;
   }
}
