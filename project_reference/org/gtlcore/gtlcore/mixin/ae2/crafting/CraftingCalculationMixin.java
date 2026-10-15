package org.gtlcore.gtlcore.mixin.ae2.crafting;

import appeng.api.networking.crafting.ICraftingPlan;
import appeng.api.stacks.KeyCounter;
import appeng.core.AELog;
import appeng.crafting.CraftBranchFailure;
import appeng.crafting.CraftingCalculation;
import appeng.crafting.CraftingTreeNode;
import appeng.crafting.inv.CraftingSimulationState;
import java.util.concurrent.atomic.AtomicBoolean;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingCalculation;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingTreeNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CraftingCalculation.class)
public abstract class CraftingCalculationMixin implements ICraftingCalculation {
   @Unique
   private final AtomicBoolean gTLCore$done = new AtomicBoolean(false);

   @Shadow(remap = false)
   private void logCraftingJob(ICraftingPlan plan) {
      throw new AssertionError();
   }

   @Shadow(remap = false)
   private ICraftingPlan computePlan() throws InterruptedException {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   public ICraftingPlan run() {
      try {
         ICraftingPlan plan = this.computePlan();
         this.logCraftingJob(plan);
         return plan;
      } catch (Exception ex) {
         AELog.info(ex, "Exception during async crafting calculation.");
         throw new RuntimeException(ex);
      } finally {
         this.finish();
      }
   }

   @Overwrite(remap = false)
   private void finish() {
      this.gTLCore$done.set(true);
   }

   @Overwrite(remap = false)
   public boolean simulateFor(int micros) {
      return !this.gTLCore$done.get();
   }

   @Overwrite(remap = false)
   @Override
   public void handlePausing() throws InterruptedException {
      if (Thread.interrupted()) {
         throw new InterruptedException();
      }
   }

   @Redirect(
      method = "runCraftAttempt",
      at = @At(
         value = "INVOKE",
         target = "Lappeng/crafting/CraftingTreeNode;request(Lappeng/crafting/inv/CraftingSimulationState;JLappeng/api/stacks/KeyCounter;)V"
      ),
      remap = false
   )
   private void redirectTreeRequest(CraftingTreeNode tree, CraftingSimulationState craftingInventory, long amount, KeyCounter containerItems) throws CraftBranchFailure, InterruptedException {
      switch (AEUtils.CALCULATION_MODE) {
         case ULTRA_FAST:
            ((ICraftingTreeNode)tree).ultraFastRequest(craftingInventory, amount, containerItems);
            break;
         case FAST:
            ((ICraftingTreeNode)tree).fastRequest(craftingInventory, amount, containerItems);
            break;
         case LEGACY:
            ((ICraftingTreeNode)tree).legacyRequest(craftingInventory, amount, containerItems);
      }
   }
}
