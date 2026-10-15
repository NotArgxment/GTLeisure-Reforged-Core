package org.gtlcore.gtlcore.mixin.gtm.api.capability;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableComputationContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CWURecipeCapability.class)
public abstract class CWURecipeCapabilityMixin extends RecipeCapability<Integer> {
   protected CWURecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<Integer> serializer) {
      super(name, color, doRenderSlot, sortIndex, serializer);
   }

   public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int maxParallel) {
      int recipeCWU = (Integer)CWURecipeCapability.CAP.of(((Content)((List)recipe.tickInputs.get(CWURecipeCapability.CAP)).get(0)).getContent());
      if (recipeCWU == 0) {
         return 2147483647;
      }

      long maxCWU = 0L;

      for (IRecipeHandler<?> container : ((List)Objects.requireNonNullElseGet(
            (List)holder.getCapabilitiesProxy().get(IO.IN, CWURecipeCapability.CAP), Collections::emptyList
         ))
         .stream()
         .filter(handler -> !handler.isProxy())
         .toList()) {
         if (container instanceof NotifiableComputationContainer nc) {
            maxCWU += nc.requestCWUt(2147483647, true);
         }
      }

      return Math.min(maxParallel, Math.abs(Ints.saturatedCast(maxCWU / recipeCWU)));
   }
}
