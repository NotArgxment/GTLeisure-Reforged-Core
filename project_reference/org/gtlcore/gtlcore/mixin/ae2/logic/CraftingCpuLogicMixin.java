package org.gtlcore.gtlcore.mixin.ae2.logic;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.execution.CraftingCpuHelper;
import appeng.crafting.execution.CraftingCpuLogic;
import appeng.crafting.execution.ExecutingCraftingJob;
import appeng.crafting.inv.ListCraftingInventory;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import appeng.me.service.CraftingService;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftIOPart;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternPartMachine;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CraftingCpuLogic.class, priority = 1100)
public abstract class CraftingCpuLogicMixin {
   @Shadow(remap = false)
   private ExecutingCraftingJob job;
   @Shadow(remap = false)
   private boolean cantStoreItems = false;
   @Shadow(remap = false)
   @Final
   CraftingCPUCluster cluster;
   @Shadow(remap = false)
   @Final
   private ListCraftingInventory inventory;
   @Shadow(remap = false)
   @Final
   private int[] usedOps;

   @Shadow(remap = false)
   public abstract void storeItems();

   @Shadow(remap = false)
   public abstract void cancel();

   @Shadow(remap = false)
   @Nullable
   public abstract GenericStack getFinalJobOutput();

   @Shadow(remap = false)
   protected abstract void finishJob(boolean var1);

   @Unique
   private boolean core$matchOutput(GenericStack g) {
      return g != null && g.what() instanceof AEItemKey i && i.getItem() == Items.f_42615_ && i.hasTag() && i.getTag().m_128441_("display");
   }

   @Overwrite(remap = false)
   public void tickCraftingLogic(IEnergyService eg, CraftingService cc) {
      if (this.cluster.isActive()) {
         this.cantStoreItems = false;
         if (this.job == null) {
            this.storeItems();
            if (!this.inventory.list.isEmpty()) {
               this.cantStoreItems = true;
            }
         } else if (((ExecutingCraftingJobAccessor)this.job).getLink().isCanceled()) {
            this.cancel();
         } else {
            int remainingOperations = this.cluster.getCoProcessors() + 1 - (this.usedOps[0] + this.usedOps[1] + this.usedOps[2]);
            int started = remainingOperations;
            if (remainingOperations > 0) {
               do {
                  int pushedPatterns = this.executeCrafting(remainingOperations, cc, eg, this.cluster.getLevel());
                  if (pushedPatterns <= 0) {
                     if (this.job != null && ((ExecutingCraftingJobAccessor)this.job).getTasks().isEmpty() && this.core$matchOutput(this.getFinalJobOutput())) {
                        this.finishJob(true);
                     }
                     break;
                  }

                  remainingOperations -= pushedPatterns;
               } while (remainingOperations > 0);
            }

            this.usedOps[2] = this.usedOps[1];
            this.usedOps[1] = this.usedOps[0];
            this.usedOps[0] = started - remainingOperations;
         }
      }
   }

   @Overwrite(remap = false)
   public int executeCrafting(int maxPatterns, CraftingService craftingService, IEnergyService energyService, Level level) {
      ExecutingCraftingJobAccessor job = (ExecutingCraftingJobAccessor)this.job;
      if (job == null) {
         return 0;
      }

      int pushedPatterns = 0;
      Iterator<? extends Entry<IPatternDetails, ?>> it = job.getTasks().entrySet().iterator();

      while (it.hasNext()) {
         Entry<IPatternDetails, ?> task = (Entry<IPatternDetails, ?>)it.next();
         ExecutingCraftingJobTaskProgressAccessor taskProgress = (ExecutingCraftingJobTaskProgressAccessor)task.getValue();
         if (taskProgress.getValue() <= 0L) {
            it.remove();
         } else {
            IPatternDetails details = task.getKey();
            boolean isProcessing = details instanceof AEProcessingPattern;
            KeyCounter expectedOutputs = new KeyCounter();
            KeyCounter expectedContainerItems = new KeyCounter();
            KeyCounter[] craftingContainer = null;
            boolean needExtract = true;
            Iterator var16 = craftingService.getProviders(details).iterator();

            while (true) {
               label95:
               if (var16.hasNext()) {
                  ICraftingProvider provider = (ICraftingProvider)var16.next();
                  boolean autoExpand = isProcessing && (provider instanceof IMEPatternPartMachine || provider instanceof IMECraftIOPart);
                  if (needExtract) {
                     craftingContainer = isProcessing
                        ? (
                           autoExpand
                              ? AEUtils.extractForProcessingPattern((AEProcessingPattern)details, this.inventory, expectedOutputs, taskProgress.getValue())
                              : AEUtils.extractForProcessingPattern((AEProcessingPattern)details, this.inventory, expectedOutputs)
                        )
                        : AEUtils.extractForCraftPattern(details, this.inventory, level, expectedOutputs, expectedContainerItems);
                     needExtract = false;
                     if (craftingContainer == null) {
                        break label95;
                     }
                  }

                  if (provider.isBusy()) {
                     continue;
                  }

                  double patternPower = CraftingCpuHelper.calculatePatternPower(craftingContainer);
                  if (!(energyService.extractAEPower(patternPower, Actionable.SIMULATE, PowerMultiplier.CONFIG) < patternPower - 0.01)) {
                     if (!provider.pushPattern(details, craftingContainer)) {
                        continue;
                     }

                     energyService.extractAEPower(patternPower, Actionable.MODULATE, PowerMultiplier.CONFIG);
                     pushedPatterns++;

                     for (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEKey> expectedOutput : expectedOutputs) {
                        job.getWaitingFor().insert((AEKey)expectedOutput.getKey(), expectedOutput.getLongValue(), Actionable.MODULATE);
                     }

                     for (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEKey> expectedContainerItem : expectedContainerItems) {
                        job.getWaitingFor().insert((AEKey)expectedContainerItem.getKey(), expectedContainerItem.getLongValue(), Actionable.MODULATE);
                        ((ElapsedTimeTrackerAccessor)job.getTimeTracker())
                           .invokeAddMaxItems(expectedContainerItem.getLongValue(), ((AEKey)expectedContainerItem.getKey()).getType());
                     }

                     this.cluster.markDirty();
                     if (autoExpand) {
                        taskProgress.setValue(0L);
                        it.remove();
                        break;
                     }

                     taskProgress.setValue(taskProgress.getValue() - 1L);
                     if (taskProgress.getValue() <= 0L) {
                        it.remove();
                        break;
                     }

                     if (pushedPatterns == maxPatterns) {
                        return pushedPatterns;
                     }

                     expectedOutputs.reset();
                     expectedContainerItems.reset();
                     craftingContainer = null;
                     needExtract = true;
                     continue;
                  }
               }

               if (craftingContainer != null) {
                  CraftingCpuHelper.reinjectPatternInputs(this.inventory, craftingContainer);
               }
               break;
            }
         }
      }

      return pushedPatterns;
   }
}
