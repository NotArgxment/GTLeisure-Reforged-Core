package org.gtlcore.gtlcore.integration.ae2.crafting;

import appeng.api.stacks.KeyCounter;
import appeng.crafting.CraftBranchFailure;
import appeng.crafting.inv.CraftingSimulationState;
import org.jetbrains.annotations.Nullable;

public interface ICraftingTreeNode {
   void ultraFastRequest(CraftingSimulationState var1, long var2, @Nullable KeyCounter var4) throws CraftBranchFailure, InterruptedException;

   void fastRequest(CraftingSimulationState var1, long var2, @Nullable KeyCounter var4) throws CraftBranchFailure, InterruptedException;

   void legacyRequest(CraftingSimulationState var1, long var2, @Nullable KeyCounter var4) throws CraftBranchFailure, InterruptedException;
}
