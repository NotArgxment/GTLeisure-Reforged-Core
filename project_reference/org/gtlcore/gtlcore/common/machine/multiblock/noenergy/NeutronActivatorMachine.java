package org.gtlcore.gtlcore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.api.pattern.util.IValueContainer;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronAcceleratorPartMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.part.NeutronSensorPartMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronActivatorMachine extends NoEnergyMultiblockMachine {
   public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      NeutronActivatorMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   private int height = 0;
   @Persisted
   @DescSynced
   private int eV;
   private static final Item dustBeryllium = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Beryllium).m_41720_();
   private static final Item dustGraphite = ChemicalHelper.get(TagPrefix.dust, GTMaterials.Graphite).m_41720_();
   protected ConditionalSubscriptionHandler neutronEnergySubs = new ConditionalSubscriptionHandler(this, this::neutronEnergyUpdate, this::isFormed);
   private Set<NeutronSensorPartMachine> sensorMachines;
   private Set<ItemBusPartMachine> busMachines;
   private Set<NeutronAcceleratorPartMachine> acceleratorMachines;

   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public NeutronActivatorMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      IValueContainer<?> container = (IValueContainer<?>)this.getMultiblockState().getMatchContext().getOrCreate("SpeedPipeValue", IValueContainer::noop);
      if (container.getValue() instanceof Integer integer) {
         this.height = integer;
      }

      for (IMultiPart part : this.getParts()) {
         if (part instanceof ItemBusPartMachine itemBusPartMachine) {
            this.busMachines = Objects.requireNonNullElseGet(this.busMachines, HashSet::new);
            this.busMachines.add(itemBusPartMachine);
         }

         if (part instanceof NeutronSensorPartMachine neutronSensorMachine) {
            this.sensorMachines = Objects.requireNonNullElseGet(this.sensorMachines, HashSet::new);
            this.sensorMachines.add(neutronSensorMachine);
         }

         if (part instanceof NeutronAcceleratorPartMachine neutronAccelerator) {
            this.acceleratorMachines = Objects.requireNonNullElseGet(this.acceleratorMachines, HashSet::new);
            this.acceleratorMachines.add(neutronAccelerator);
         }
      }

      this.neutronEnergySubs.initialize(this.getLevel());
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.height = 0;
      this.sensorMachines = null;
      this.busMachines = null;
      this.acceleratorMachines = null;
      this.neutronEnergySubs.unsubscribe();
   }

   private double getEfficiencyFactor() {
      return Math.pow(0.95, Math.max(this.height - 4, 0));
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe) {
      if (machine instanceof NeutronActivatorMachine nMachine) {
         if (nMachine.eV > recipe.data.m_128451_("ev_min") * 1000000 && nMachine.eV < recipe.data.m_128451_("ev_max") * 1000000) {
            GTRecipe recipe1 = (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe, GTLRecipeModifiers.getHatchParallel(nMachine), false).getFirst();
            recipe1.duration = (int)Math.round(Math.max(recipe1.duration * nMachine.getEfficiencyFactor(), 1.0));
            return recipe1;
         }

         if (nMachine.eV < recipe.data.m_128451_("ev_min") * 1000000) {
            RecipeResult.of((IRecipeLogicMachine)machine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.enough.neutron")));
         } else if (nMachine.eV > recipe.data.m_128451_("ev_max") * 1000000) {
            RecipeResult.of((IRecipeLogicMachine)machine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.too.much.neutron")));
         }
      }

      return null;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (recipe != null) {
         if (this.eV >= recipe.data.m_128451_("evt") * 1000 * this.getEVtMultiplier(recipe)) {
            return true;
         }

         RecipeResult.of(this, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.enough.eV.consumed")));
         return false;
      } else {
         return false;
      }
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      GTRecipe lastRecipe = this.getRecipeLogic().getLastRecipe();
      if (lastRecipe != null) {
         int evt = (int)(lastRecipe.data.m_128451_("evt") * 1000 * this.getEVtMultiplier(lastRecipe));
         if (this.eV < evt) {
            this.getRecipeLogic().setProgress(0);
         } else {
            this.eV -= evt;
         }
      }

      return value;
   }

   private double getEVtMultiplier(GTRecipe recipe) {
      return Math.max(1.0, Math.pow(IGTRecipe.of(recipe).getRealParallels(), 1.5) * this.getEfficiencyFactor());
   }

   protected void neutronEnergyUpdate() {
      if (this.acceleratorMachines != null) {
         for (NeutronAcceleratorPartMachine accelerator : this.acceleratorMachines) {
            long increase = accelerator.consumeEnergy();
            if (increase > 0L) {
               this.eV = this.eV + (int)Math.round(Math.max(increase * this.getEfficiencyFactor(), 1.0));
            }
         }

         if (this.eV > 1200000000) {
            this.eV = 0;
         }

         this.moderateUpdate();
         this.absorptionUpdate();
      }
   }

   protected void moderateUpdate() {
      if (this.getOffsetTimer() % 20L == 0L) {
         this.eV = Math.max(this.eV - 72000, 0);
      }

      if (this.eV < 0) {
         this.eV = 0;
      }

      if (this.isFormed() && this.sensorMachines != null) {
         this.sensorMachines.forEach(sensor -> sensor.update(this.eV));
      }
   }

   protected void absorptionUpdate() {
      if (this.busMachines != null && (this.eV > 0 || this.getOffsetTimer() % 10L == 0L)) {
         for (ItemBusPartMachine bus : this.busMachines) {
            NotifiableItemStackHandler inv = bus.getInventory();
            IO io = inv.getHandlerIO();
            if (io == IO.IN || io == IO.BOTH) {
               for (int i = 0; i < inv.getSlots(); i++) {
                  ItemStack stack = inv.getStackInSlot(i);
                  if (stack.m_150930_(dustBeryllium) || stack.m_150930_(dustGraphite)) {
                     int consume = Math.min(Math.max(this.eV / 10000000, 1), stack.m_41613_());
                     inv.extractItemInternal(i, consume, false);
                     this.eV -= 10000000 * consume;
                  }
               }
            }
         }
      }
   }

   @Override
   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         int numParallels = GTLRecipeModifiers.getHatchParallel(this);
         textList.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(numParallels)).m_130940_(ChatFormatting.DARK_PURPLE)}
               )
               .m_130940_(ChatFormatting.GRAY)
         );
         textList.add(Component.m_237110_("gtceu.machine.neutron_activator.ev", new Object[]{this.processNumber(this.eV)}));
         textList.add(
            Component.m_237110_(
               "gtceu.machine.neutron_activator.efficiency",
               new Object[]{FormattingUtil.formatNumbers(Math.max(1.0, Math.pow(numParallels, 1.5) * this.getEfficiencyFactor()))}
            )
         );
         textList.add(Component.m_237110_("gtceu.machine.neutron_activator.height", new Object[]{this.height}));
         textList.add(
            Component.m_237110_("gtceu.machine.neutron_activator.time", new Object[]{FormattingUtil.formatNumbers(this.getEfficiencyFactor() * 100.0)})
               .m_130946_("%")
         );
      }
   }

   private String processNumber(int num) {
      float num2 = num / 1000.0F;
      if (num2 <= 0.0F) {
         return String.format("%d", num);
      }

      if (num2 < 1000.0) {
         return String.format("%.1fK", num2);
      }

      num2 /= 1000.0F;
      return String.format("%.1fM", num2);
   }

   public int getEV() {
      return this.eV;
   }
}
