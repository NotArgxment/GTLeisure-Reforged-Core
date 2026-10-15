package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IControllable;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import java.util.Collection;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.gtlcore.gtlcore.utils.Registries;
import org.gtlcore.gtlcore.utils.TextUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ComputationProviderMachine extends WorkableElectricMultiblockMachine implements IOpticalComputationProvider, IControllable {
   public int allocatedCWUt = 0;
   @Persisted
   public long totalCWU = 0L;
   public int maxCWUt = 0;
   @Nullable
   protected TickableSubscription tickSubs;
   boolean canProvideCWUt = true;
   private boolean inf = false;
   private static final ItemStack OPTICAL_MAINFRAME = Registries.getItemStack("kubejs:optical_mainframe", 8);
   private static final ItemStack EXOTIC_MAINFRAME = Registries.getItemStack("kubejs:exotic_mainframe", 8);
   private static final ItemStack COSMIC_MAINFRAME = Registries.getItemStack("kubejs:cosmic_mainframe", 8);
   private static final ItemStack SUPRACAUSAL_MAINFRAME = Registries.getItemStack("kubejs:supracausal_mainframe", 8);

   public ComputationProviderMachine(IMachineBlockEntity holder, boolean inf, Object... args) {
      super(holder, args);
      this.inf = inf;
   }

   public int requestCWUt(int cwut, boolean simulate, @NotNull Collection<IOpticalComputationProvider> seen) {
      seen.add(this);
      return !this.canProvideCWUt ? 0 : this.allocatedCWUt(cwut, simulate);
   }

   private int allocatedCWUt(int cwut, boolean simulate) {
      if (this.totalCWU < this.getMaxCWUt()) {
         if (this.inf && MachineIO.inputEU(this, 2147483647L)) {
            return 2147483647;
         }

         if (MachineIO.inputEU(this, GTValues.VA[this.getTier()])) {
            this.totalCWU = this.totalCWU + (long)Math.pow(2.0, this.getTier());
         }

         this.maxCWUt = 0;
      }

      int maxCWUt = this.getMaxCWUt();
      int availableCWUt = maxCWUt - this.allocatedCWUt;
      int toAllocate = Math.min(cwut, (int)Math.min(availableCWUt, this.totalCWU));
      if (!simulate) {
         this.allocatedCWUt += toAllocate;
      }

      return toAllocate;
   }

   public int getMaxCWUt(@NotNull Collection<IOpticalComputationProvider> seen) {
      seen.add(this);
      if (this.inf) {
         return 2147483647;
      }

      if (this.maxCWUt == 0) {
         switch (this.getTier()) {
            case 11:
               if (MachineIO.notConsumableItem(this, OPTICAL_MAINFRAME)) {
                  return 1024;
               }
               break;
            case 12:
               if (MachineIO.notConsumableItem(this, EXOTIC_MAINFRAME)) {
                  return 2048;
               }
               break;
            case 13:
               if (MachineIO.notConsumableItem(this, COSMIC_MAINFRAME)) {
                  return 4096;
               }
               break;
            case 14:
               if (MachineIO.notConsumableItem(this, SUPRACAUSAL_MAINFRAME)) {
                  return 8192;
               }
         }

         return 0;
      } else {
         return this.maxCWUt;
      }
   }

   public boolean canBridge(@NotNull Collection<IOpticalComputationProvider> seen) {
      seen.add(this);
      return true;
   }

   public void tick() {
      this.totalCWU = this.totalCWU - this.allocatedCWUt;
      if (this.getRecipeLogic().isSuspend()) {
         this.allocatedCWUt = 0;
         this.canProvideCWUt = false;
      } else {
         this.canProvideCWUt = true;
         if (this.allocatedCWUt != 0) {
            this.getRecipeLogic().setStatus(Status.WORKING);
            this.allocatedCWUt = 0;
         } else {
            this.getRecipeLogic().setStatus(Status.IDLE);
         }
      }
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().m_6937_(new TickTask(0, this::updateTickSubscription));
      }
   }

   public void onUnload() {
      super.onUnload();
      if (this.tickSubs != null) {
         this.tickSubs.unsubscribe();
         this.tickSubs = null;
      }
   }

   protected void updateTickSubscription() {
      if (this.isFormed) {
         this.tickSubs = this.subscribeServerTick(this.tickSubs, this::tick);
      } else if (this.tickSubs != null) {
         this.tickSubs.unsubscribe();
         this.tickSubs = null;
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().m_6937_(new TickTask(0, this::updateTickSubscription));
      }
   }

   public void onChanged() {
      this.maxCWUt = 0;
   }

   public void addDisplayText(List<Component> textList) {
      MultiblockDisplayText.builder(textList, this.isFormed())
         .setWorkingStatus(true, this.allocatedCWUt > 0)
         .setWorkingStatusKeys("gtceu.multiblock.idling", "gtceu.multiblock.idling", "gtceu.multiblock.data_bank.providing")
         .addCustom(
            tl -> {
               if (this.isFormed()) {
                  Component cwutInfo = Component.m_237113_(this.allocatedCWUt + " / " + (this.inf ? TextUtil.full_color("\u221e") : this.getMaxCWUt()))
                     .m_7220_(Component.m_237113_(" CWU/t"))
                     .m_130940_(ChatFormatting.AQUA);
                  tl.add(Component.m_237110_("gtceu.multiblock.hpca.computation", new Object[]{cwutInfo}).m_130940_(ChatFormatting.GRAY));
               }
            }
         )
         .addWorkingStatusLine();
   }
}
