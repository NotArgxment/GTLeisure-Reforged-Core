package org.gtlcore.gtlcore.mixin.ae2.logic;

import appeng.api.crafting.IPatternDetails;
import appeng.crafting.CraftingLink;
import appeng.crafting.execution.ElapsedTimeTracker;
import appeng.crafting.execution.ExecutingCraftingJob;
import appeng.crafting.inv.ListCraftingInventory;
import java.util.Map;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ExecutingCraftingJob.class)
public interface ExecutingCraftingJobAccessor {
   @Accessor(value = "tasks", remap = false)
   Map<IPatternDetails, ?> getTasks();

   @Accessor(value = "waitingFor", remap = false)
   ListCraftingInventory getWaitingFor();

   @Accessor(value = "timeTracker", remap = false)
   ElapsedTimeTracker getTimeTracker();

   @Accessor(value = "link", remap = false)
   CraftingLink getLink();
}
