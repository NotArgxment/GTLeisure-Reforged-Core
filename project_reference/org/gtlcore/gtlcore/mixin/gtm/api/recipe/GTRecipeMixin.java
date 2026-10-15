package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.GTRecipe.ActionResult;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTRecipe.class)
public abstract class GTRecipeMixin implements IGTRecipe {
   @Unique
   private int tier = -1;
   @Unique
   private long realParallels = 1L;
   @Unique
   private boolean hasTick;
   @Unique
   private IO io;
   @Shadow(remap = false)
   public int parallels;
   @Shadow(remap = false)
   public ResourceLocation id;
   @Shadow(remap = false)
   @NotNull
   public CompoundTag data;
   @Shadow(remap = false)
   @Final
   public GTRecipeType recipeType;

   @Inject(
      method = "<init>(Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;Lnet/minecraft/resources/ResourceLocation;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/Map;Ljava/util/List;Ljava/util/List;Lnet/minecraft/nbt/CompoundTag;IZ)V",
      at = @At("RETURN"),
      remap = false
   )
   public void GTRecipe(
      GTRecipeType recipeType,
      ResourceLocation id,
      Map inputs,
      Map outputs,
      Map tickInputs,
      Map tickOutputs,
      Map inputChanceLogics,
      Map outputChanceLogics,
      Map tickInputChanceLogics,
      Map tickOutputChanceLogics,
      List conditions,
      List ingredientActions,
      CompoundTag data,
      int duration,
      boolean isFuel,
      CallbackInfo ci
   ) {
      this.tier = this.getEuTier();
      this.hasTick = !tickInputs.isEmpty() || !tickOutputs.isEmpty();
      this.io = tickInputs.isEmpty() ? (tickOutputs.isEmpty() ? IO.NONE : IO.OUT) : IO.IN;
   }

   @Overwrite(remap = false)
   public boolean hasTick() {
      return this.hasTick;
   }

   @Overwrite(remap = false)
   public ActionResult matchTickRecipe(IRecipeCapabilityHolder holder) {
      if (!this.hasTick()) {
         return ActionResult.SUCCESS;
      }

      IRecipeLogicMachine rlm = (IRecipeLogicMachine)holder;
      if (holder instanceof WorkableElectricMultiblockMachine machine && this.io == IO.IN) {
         GTRecipe lastRecipe = machine.getRecipeLogic().getLastOriginRecipe();
         if ((lastRecipe == null || !this.id.equals(lastRecipe.id)) && this.getEuTier() > GTUtil.getFloorTierByVoltage(machine.getMaxVoltage())) {
            RecipeResult.of(rlm, RecipeResult.FAIL_VOLTAGE_TIER);
            return ActionResult.fail(() -> null);
         }
      }

      ActionResult result = this.matchRecipe(holder, true);
      if (!result.isSuccess() && result.reason() != null) {
         String s = ((Component)result.reason().get()).toString();
         if (s.contains("_in")) {
            if (s.contains("cwu")) {
               RecipeResult.of(rlm, RecipeResult.FAIL_NO_ENOUGH_CWU_IN);
            } else if (s.contains("eu.name")) {
               RecipeResult.of(
                  rlm,
                  holder instanceof ISteamMachine
                     ? RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.steam.enough"))
                     : RecipeResult.FAIL_NO_ENOUGH_EU_IN
               );
            }
         } else if (s.contains("_out") && s.contains("eu.name")) {
            RecipeResult.of(rlm, RecipeResult.FAIL_NO_ENOUGH_EU_OUT);
         }
      }

      return result;
   }

   @Shadow(remap = false)
   protected abstract ActionResult matchRecipe(IRecipeCapabilityHolder var1, boolean var2);

   @Override
   public int getEuTier() {
      return this.tier = this.tier == -1 ? this.data.m_128451_("euTier") : this.tier;
   }

   @Override
   public void setRealParallels(long realParallel) {
      this.realParallels = realParallel;
      this.parallels = Ints.saturatedCast(realParallel);
   }

   @Override
   public long getRealParallels() {
      return this.realParallels;
   }

   @Override
   public void setHasTick(boolean hasTick) {
      this.hasTick = hasTick;
   }
}
