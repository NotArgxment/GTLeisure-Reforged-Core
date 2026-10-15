package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import com.gregtechceu.gtceu.api.gui.fancy.TabsWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.LazyManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.MEExtendedOutputFancyConfigurator;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOTrait;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;

public class MEExtendedOutputPartMachine extends MEExtendedOutputPartMachineBase {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MEExtendedOutputPartMachine.class, MEExtendedOutputPartMachineBase.MANAGED_FIELD_HOLDER
   );
   protected static final int FILTER_ROW = 3;
   protected static final int FILTER_COL = 9;
   @Persisted
   @LazyManaged
   protected final MEOutputFilterHandler filterHandler = new MEOutputFilterHandler(
      3, 9, this::onFilterChanged, this::updatePriority, this::getPriority, this::setPriority
   );

   @NotNull
   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public MEExtendedOutputPartMachine(IMachineBlockEntity holder) {
      super(holder);
   }

   private void onFilterChanged() {
      this.markDirty();
      this.notifyHandlers();
   }

   public ModularUI createUI(Player entityPlayer) {
      ModularUI ui = super.createUI(entityPlayer);
      ui.registerCloseListener(this.filterHandler::onUIClosed);
      return ui;
   }

   public void attachSideTabs(TabsWidget sideTabs) {
      super.attachSideTabs(sideTabs);
      sideTabs.attachSubTab(new MEExtendedOutputFancyConfigurator(this.filterHandler::createMainWidgetGroup));
   }

   @Override
   protected CompoundTag writeConfigToTag() {
      CompoundTag tag = super.writeConfigToTag();
      tag.m_128365_("meOutputFilterHandler", this.filterHandler.serializeNBT());
      return tag;
   }

   @Override
   protected void readConfigFromTag(CompoundTag tag) {
      super.readConfigFromTag(tag);
      if (tag.m_128441_("meOutputFilterHandler")) {
         this.filterHandler.deserializeNBT(tag.m_128469_("meOutputFilterHandler"));
         this.onFilterChanged();
      }
   }

   @Override
   protected NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> createItemOutputHandler() {
      return new MEExtendedOutputPartMachineBase.MEItemOutputHandler(this) {
         public MEExtendedOutputPartMachine getMachine() {
            return (MEExtendedOutputPartMachine)this.machine;
         }

         @Override
         public boolean outputHasFilter() {
            return MEExtendedOutputPartMachine.this.filterHandler.isHasItemFilter();
         }

         @Override
         public List<Ingredient> meHandleRecipeOutputInner(List<Ingredient> left, boolean simulate) {
            if (simulate) {
               return MEExtendedOutputPartMachine.this.filterHandler.testIngredient(left);
            }

            Iterator<Ingredient> it = left.iterator();

            while (it.hasNext()) {
               Ingredient ingredient = it.next();
               if (ingredient instanceof IntProviderIngredient intProvider) {
                  intProvider.setItemStacks(null);
                  intProvider.setSampledCount(null);
               }

               ItemStack[] items = ingredient.m_43908_();
               if (items.length != 0) {
                  ItemStack output = items[0];
                  if (output.m_41619_()) {
                     it.remove();
                  } else if (MEExtendedOutputPartMachine.this.filterHandler.test(output)) {
                     MEExtendedOutputPartMachine.this.buffer
                        .mergeLong(
                           AEItemKey.of(output),
                           ingredient instanceof LongIngredient longIngredient ? longIngredient.getActualAmount() : output.m_41613_(),
                           NumberUtils::saturatedAdd
                        );
                     it.remove();
                  }
               } else {
                  it.remove();
               }
            }

            return left;
         }
      };
   }

   @Override
   protected NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> createFluidOutputHandler() {
      return new MEExtendedOutputPartMachineBase.MEFluidOutputHandler(this) {
         public MEExtendedOutputPartMachine getMachine() {
            return (MEExtendedOutputPartMachine)this.machine;
         }

         @Override
         public boolean outputHasFilter() {
            return MEExtendedOutputPartMachine.this.filterHandler.isHasFluidFilter();
         }

         @Override
         public List<FluidIngredient> meHandleRecipeOutputInner(List<FluidIngredient> left, boolean simulate) {
            if (simulate) {
               return MEExtendedOutputPartMachine.this.filterHandler.testFluidIngredient(left);
            }

            Iterator<FluidIngredient> it = left.iterator();

            while (it.hasNext()) {
               FluidIngredient fluidIngredient = it.next();
               if (!fluidIngredient.isEmpty()) {
                  FluidStack[] fluids = fluidIngredient.getStacks();
                  if (fluids.length != 0) {
                     FluidStack output = fluids[0];
                     if (output.isEmpty()) {
                        it.remove();
                     } else if (MEExtendedOutputPartMachine.this.filterHandler.test(output)) {
                        MEExtendedOutputPartMachine.this.buffer.mergeLong(AEFluidKey.of(output.getFluid()), output.getAmount(), NumberUtils::saturatedAdd);
                        it.remove();
                     }
                  } else {
                     it.remove();
                  }
               } else {
                  it.remove();
               }
            }

            return left;
         }
      };
   }

   @NotNull
   protected IMEFilterIOTrait createMETrait() {
      return new MEExtendedOutputPartMachine.MEFilterIOTrait(this);
   }

   @NotNull
   @Override
   public IMEFilterIOTrait getMETrait() {
      return (IMEFilterIOTrait)this.meTrait;
   }

   @Override
   protected void registerDefaultServices() {
      this.getMainNode().addService(IGridTickable.class, new MEExtendedOutputPartMachine.Ticker());
   }

   protected class MEFilterIOTrait extends MEIOPartMachine.MEIOTrait implements IMEFilterIOTrait {
      public MEFilterIOTrait(MEExtendedOutputPartMachine machine) {
         super(machine);
      }

      public MEExtendedOutputPartMachine getMachine() {
         return (MEExtendedOutputPartMachine)this.machine;
      }

      @Override
      public boolean hasItemFilter() {
         return MEExtendedOutputPartMachine.this.filterHandler.isHasItemFilter();
      }

      @Override
      public boolean hasFluidFilter() {
         return MEExtendedOutputPartMachine.this.filterHandler.isHasFluidFilter();
      }
   }

   protected class Ticker implements IGridTickable {
      public TickingRequest getTickingRequest(IGridNode node) {
         return new TickingRequest(MEExtendedOutputPartMachineBase.MIN_FREQUENCY, MEExtendedOutputPartMachineBase.MAX_FREQUENCY, false, true);
      }

      public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
         if (!MEExtendedOutputPartMachine.this.getMainNode().isActive()) {
            MEExtendedOutputPartMachine.this.isSleeping = true;
            return TickRateModulation.SLEEP;
         }

         if (MEExtendedOutputPartMachine.this.buffer.isEmpty()) {
            if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
               MEExtendedOutputPartMachine.this.isSleeping = true;
               return TickRateModulation.SLEEP;
            } else {
               return TickRateModulation.SLOWER;
            }
         } else {
            return AEUtils.reFunds(
                  MEExtendedOutputPartMachine.this.buffer,
                  MEExtendedOutputPartMachine.this.getMainNode().getGrid(),
                  MEExtendedOutputPartMachine.this.actionSource
               )
               ? TickRateModulation.URGENT
               : TickRateModulation.SLOWER;
         }
      }
   }
}
