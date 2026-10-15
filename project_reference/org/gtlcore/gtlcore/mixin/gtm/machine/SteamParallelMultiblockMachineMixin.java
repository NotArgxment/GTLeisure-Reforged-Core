package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;
import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SteamParallelMultiblockMachine.class)
public abstract class SteamParallelMultiblockMachineMixin extends WorkableMultiblockMachine implements ISteamMachine {
   public SteamParallelMultiblockMachineMixin(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Inject(method = "recipeModifier", at = @At("HEAD"), remap = false, cancellable = true)
   private static void recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result, CallbackInfoReturnable<GTRecipe> cir) {
      if (machine.getDefinition() == GTMachines.STEAM_OVEN) {
         GTRecipe recipe1 = (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe, 256, false).getFirst();
         recipe1.duration = (int)Math.max(1.0, recipe1.duration * 0.5);
         cir.setReturnValue(recipe1);
      }
   }

   @ModifyConstant(method = "addDisplayText", remap = false, constant = @Constant(intValue = 8))
   private int modifyConsume(int constant) {
      return this.getDefinition() == GTMachines.STEAM_OVEN ? 256 : 8;
   }

   @Override
   public double getConversionRate() {
      return 0.5;
   }
}
