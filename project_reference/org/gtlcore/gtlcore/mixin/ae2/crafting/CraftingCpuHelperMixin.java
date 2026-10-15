package org.gtlcore.gtlcore.mixin.ae2.crafting;

import appeng.api.crafting.IPatternDetails.IInput;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.execution.CraftingCpuHelper;
import appeng.crafting.execution.InputTemplate;
import appeng.crafting.inv.ICraftingInventory;
import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CraftingCpuHelper.class)
public abstract class CraftingCpuHelperMixin {
   @Overwrite(remap = false)
   public static Iterable<InputTemplate> getValidItemTemplates(ICraftingInventory inv, IInput input, Level level) {
      GenericStack[] possibleInputs = input.getPossibleInputs();
      List<InputTemplate> substitutes = new ObjectArrayList(possibleInputs.length);

      for (GenericStack stack : possibleInputs) {
         for (AEKey fuzz : inv.findFuzzyTemplates(stack.what())) {
            if (!(fuzz instanceof AEItemKey aeItemKey && !aeItemKey.matches(stack)) && !(fuzz instanceof AEFluidKey aeFluidKey && !aeFluidKey.matches(stack))) {
               substitutes.add(new InputTemplate(fuzz, stack.amount()));
            }
         }
      }

      return Iterables.filter(substitutes, stackx -> input.isValid(stackx.key(), level));
   }
}
