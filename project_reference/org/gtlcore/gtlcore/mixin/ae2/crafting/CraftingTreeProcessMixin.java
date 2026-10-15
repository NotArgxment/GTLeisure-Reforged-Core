package org.gtlcore.gtlcore.mixin.ae2.crafting;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.CraftBranchFailure;
import appeng.crafting.CraftingCalculation;
import appeng.crafting.CraftingTreeNode;
import appeng.crafting.CraftingTreeProcess;
import appeng.crafting.inv.CraftingSimulationState;
import java.util.Map;
import java.util.Map.Entry;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingCalculation;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingTreeNode;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingTreeProcess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftingTreeProcess.class)
public abstract class CraftingTreeProcessMixin implements ICraftingTreeProcess {
   @Shadow(remap = false)
   @Final
   private CraftingCalculation job;
   @Shadow(remap = false)
   private boolean containerItems;
   @Shadow(remap = false)
   @Final
   private Map<CraftingTreeNode, Long> nodes;
   @Shadow(remap = false)
   @Final
   IPatternDetails details;
   @Shadow(remap = false)
   boolean possible;
   @Shadow(remap = false)
   private boolean limitQty;

   @Unique
   @Override
   public void fastRequest(CraftingSimulationState inv, long times) throws CraftBranchFailure, InterruptedException {
      ((ICraftingCalculation)this.job).handlePausing();
      KeyCounter containerItems = this.containerItems ? new KeyCounter() : null;

      for (Entry<CraftingTreeNode, Long> entry : this.nodes.entrySet()) {
         ((ICraftingTreeNode)entry.getKey()).fastRequest(inv, entry.getValue() * times, containerItems);
      }

      this.onSucceed(inv, times, containerItems);
   }

   @Unique
   @Override
   public void ultraFastRequest(CraftingSimulationState inv, long times) throws CraftBranchFailure, InterruptedException {
      ((ICraftingCalculation)this.job).handlePausing();
      KeyCounter containerItems = this.containerItems ? new KeyCounter() : null;

      for (Entry<CraftingTreeNode, Long> entry : this.nodes.entrySet()) {
         ((ICraftingTreeNode)entry.getKey()).ultraFastRequest(inv, entry.getValue() * times, containerItems);
      }

      this.onSucceed(inv, times, containerItems);
   }

   @Unique
   private void onSucceed(CraftingSimulationState inv, long times, KeyCounter containerItems) {
      if (containerItems != null) {
         for (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEKey> stack : containerItems) {
            inv.insert((AEKey)stack.getKey(), stack.getLongValue(), Actionable.MODULATE);
            inv.addStackBytes((AEKey)stack.getKey(), stack.getLongValue(), 1L);
         }
      }

      for (GenericStack out : this.details.getOutputs()) {
         inv.insert(out.what(), out.amount() * times, Actionable.MODULATE);
      }

      inv.addCrafting(this.details, times);
      inv.addBytes(times);
   }

   @Unique
   @Override
   public boolean getPossible() {
      return this.possible;
   }

   @Unique
   @Override
   public void setPossible(boolean b) {
      this.possible = b;
   }

   @Unique
   @Override
   public long getOutputCountTest(AEKey what) {
      long tot = 0L;

      for (GenericStack is : this.details.getOutputs()) {
         if (what.matches(is)) {
            tot += is.amount();
         }
      }

      return tot;
   }

   @Unique
   @Override
   public boolean limitsQuantityTest() {
      return this.limitQty;
   }

   @Override
   public IPatternDetails getDetails() {
      return this.details;
   }
}
