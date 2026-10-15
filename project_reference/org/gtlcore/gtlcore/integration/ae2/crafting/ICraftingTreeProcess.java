package org.gtlcore.gtlcore.integration.ae2.crafting;

import appeng.api.crafting.IPatternDetails;
import appeng.api.stacks.AEKey;
import appeng.crafting.CraftBranchFailure;
import appeng.crafting.inv.CraftingSimulationState;

public interface ICraftingTreeProcess {
   void fastRequest(CraftingSimulationState var1, long var2) throws CraftBranchFailure, InterruptedException;

   void ultraFastRequest(CraftingSimulationState var1, long var2) throws CraftBranchFailure, InterruptedException;

   IPatternDetails getDetails();

   boolean getPossible();

   void setPossible(boolean var1);

   long getOutputCountTest(AEKey var1);

   boolean limitsQuantityTest();
}
