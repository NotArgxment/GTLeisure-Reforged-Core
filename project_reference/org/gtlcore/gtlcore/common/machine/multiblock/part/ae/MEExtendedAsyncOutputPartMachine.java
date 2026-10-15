package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.stacks.AEKey;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOTrait;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.integration.ae2.async.AEAccumulator;
import org.gtlcore.gtlcore.integration.ae2.async.AEWriteService;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;

public class MEExtendedAsyncOutputPartMachine extends MEExtendedOutputPartMachineBase {
   private final AEAccumulator accumulator = new AEAccumulator();
   private final WeakReference<AEAccumulator> accRef = new WeakReference<>(this.accumulator);
   private final AtomicReference<Object2LongOpenHashMap<AEKey>> pendingData = new AtomicReference<>();
   private final AtomicBoolean drainRequested = new AtomicBoolean(false);

   public MEExtendedAsyncOutputPartMachine(IMachineBlockEntity holder) {
      super(holder);
   }

   private void requestAsyncDrain() {
      if (this.pendingData.get() == null && this.drainRequested.compareAndSet(false, true)) {
         AEWriteService.INSTANCE.prepareDrainedData(this.accRef, this.pendingData, this.drainRequested);
      }
   }

   private boolean mergeFromPendingData() {
      Object2LongOpenHashMap<AEKey> data = this.pendingData.getAndSet(null);
      if (data != null && !data.isEmpty()) {
         data.object2LongEntrySet().fastForEach(e -> this.buffer.mergeLong((AEKey)e.getKey(), e.getLongValue(), NumberUtils::saturatedAdd));
         return true;
      } else {
         return false;
      }
   }

   public void onMachineRemoved() {
      this.accumulator.clear();
      super.onMachineRemoved();
   }

   public ModularUI createUI(Player entityPlayer) {
      ModularUI ui = super.createUI(entityPlayer);
      ui.registerCloseListener(this::updatePriority);
      return ui;
   }

   @NotNull
   @Override
   public Widget createUIWidget() {
      WidgetGroup group = (WidgetGroup)super.createUIWidget();
      group.addWidget(new IntInputWidget(90, 0, 80, 10, this::getPriority, this::setPriority).setMin(10).setMax(100000));
      return group;
   }

   @Override
   protected NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> createItemOutputHandler() {
      return new MEExtendedOutputPartMachineBase.MEItemOutputHandler(this) {
         public MEExtendedAsyncOutputPartMachine getMachine() {
            return (MEExtendedAsyncOutputPartMachine)this.machine;
         }

         @Override
         public List<Ingredient> meHandleRecipeOutputInner(List<Ingredient> left, boolean simulate) {
            if (simulate) {
               return List.of();
            }

            AEWriteService.INSTANCE.submitIngredientLeft(MEExtendedAsyncOutputPartMachine.this.accRef, left);
            return List.of();
         }
      };
   }

   @Override
   protected NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> createFluidOutputHandler() {
      return new MEExtendedOutputPartMachineBase.MEFluidOutputHandler(this) {
         public MEExtendedAsyncOutputPartMachine getMachine() {
            return (MEExtendedAsyncOutputPartMachine)this.machine;
         }

         @Override
         public List<FluidIngredient> meHandleRecipeOutputInner(List<FluidIngredient> left, boolean simulate) {
            if (simulate) {
               return List.of();
            }

            AEWriteService.INSTANCE.submitFluidIngredientLeft(MEExtendedAsyncOutputPartMachine.this.accRef, left);
            return List.of();
         }
      };
   }

   @NotNull
   protected IMEFilterIOTrait createMETrait() {
      return new MEExtendedAsyncOutputPartMachine.MEAsyncFilterIOTrait(this);
   }

   @NotNull
   @Override
   public IMEFilterIOTrait getMETrait() {
      return (IMEFilterIOTrait)this.meTrait;
   }

   @Override
   protected void registerDefaultServices() {
      this.getMainNode().addService(IGridTickable.class, new MEExtendedAsyncOutputPartMachine.Ticker());
   }

   protected class MEAsyncFilterIOTrait extends MEIOPartMachine.MEIOTrait implements IMEFilterIOTrait {
      public MEAsyncFilterIOTrait(MEExtendedAsyncOutputPartMachine machine) {
         super(machine);
      }

      public MEExtendedAsyncOutputPartMachine getMachine() {
         return (MEExtendedAsyncOutputPartMachine)this.machine;
      }
   }

   protected class Ticker implements IGridTickable {
      public TickingRequest getTickingRequest(IGridNode node) {
         return new TickingRequest(MEExtendedOutputPartMachineBase.MIN_FREQUENCY, MEExtendedOutputPartMachineBase.MAX_FREQUENCY, false, true);
      }

      public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
         boolean isActive = MEExtendedAsyncOutputPartMachine.this.getMainNode().isActive();
         boolean dataMerged = MEExtendedAsyncOutputPartMachine.this.mergeFromPendingData();
         boolean hasPendingWork = MEExtendedAsyncOutputPartMachine.this.pendingData.get() != null
            || !MEExtendedAsyncOutputPartMachine.this.accumulator.isEmpty();
         if (hasPendingWork) {
            MEExtendedAsyncOutputPartMachine.this.requestAsyncDrain();
         }

         if (!isActive) {
            if (hasPendingWork) {
               return TickRateModulation.FASTER;
            } else if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
               MEExtendedAsyncOutputPartMachine.this.isSleeping = true;
               return TickRateModulation.SLEEP;
            } else {
               return TickRateModulation.SLOWER;
            }
         } else if (MEExtendedAsyncOutputPartMachine.this.buffer.isEmpty()) {
            if (hasPendingWork) {
               return TickRateModulation.FASTER;
            } else if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
               MEExtendedAsyncOutputPartMachine.this.isSleeping = true;
               return TickRateModulation.SLEEP;
            } else {
               return TickRateModulation.SLOWER;
            }
         } else {
            return !AEUtils.reFunds(
                     MEExtendedAsyncOutputPartMachine.this.buffer,
                     MEExtendedAsyncOutputPartMachine.this.getMainNode().getGrid(),
                     MEExtendedAsyncOutputPartMachine.this.actionSource
                  )
                  && !dataMerged
               ? TickRateModulation.SLOWER
               : TickRateModulation.URGENT;
         }
      }
   }
}
