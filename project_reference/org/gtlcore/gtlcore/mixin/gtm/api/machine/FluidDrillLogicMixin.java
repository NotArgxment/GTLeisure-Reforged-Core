package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FluidDrillMachine;
import com.gregtechceu.gtceu.common.machine.trait.FluidDrillLogic;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FluidDrillLogic.class)
public abstract class FluidDrillLogicMixin extends RecipeLogic {
   @Shadow(remap = false)
   @Nullable
   private Fluid veinFluid;

   @Shadow(remap = false)
   protected abstract int getChunkX();

   @Shadow(remap = false)
   protected abstract int getChunkZ();

   @Shadow(remap = false)
   public abstract FluidDrillMachine getMachine();

   @Shadow(remap = false)
   protected abstract long getFluidToProduce(FluidVeinWorldEntry var1);

   @Shadow(remap = false)
   protected abstract void depleteVein();

   public FluidDrillLogicMixin(IRecipeLogicMachine machine) {
      super(machine);
   }

   @Overwrite(remap = false)
   public void findAndHandleRecipe() {
      if (this.getMachine().getLevel() instanceof ServerLevel serverLevel) {
         this.lastRecipe = null;
         BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
         if (this.veinFluid == null) {
            this.veinFluid = data.getFluidInChunk(this.getChunkX(), this.getChunkZ());
            if (this.veinFluid == null) {
               if (this.subscription != null) {
                  this.subscription.unsubscribe();
                  this.subscription = null;
               }

               return;
            }
         }

         GTRecipe match = this.getFluidDrillRecipe();
         if (match != null) {
            this.setupRecipe(match);
         }
      }
   }

   @Overwrite(remap = false)
   @Nullable
   private GTRecipe getFluidDrillRecipe() {
      if (this.getMachine().getLevel() instanceof ServerLevel serverLevel && this.veinFluid != null) {
         BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
         GTRecipe recipe = GTRecipeBuilder.ofRaw()
            .duration(20)
            .EUt(GTValues.VA[this.getMachine().getEnergyTier()])
            .outputFluids(FluidStack.create(this.veinFluid, this.getFluidToProduce(data.getFluidVeinWorldEntry(this.getChunkX(), this.getChunkZ()))))
            .buildRawRecipe();
         if (RecipeRunnerHelper.matchRecipe(this.getMachine(), recipe) && recipe.matchTickRecipe(this.getMachine()).isSuccess()) {
            return recipe;
         }
      }

      return null;
   }

   @Overwrite(remap = false)
   public void onRecipeFinish() {
      this.machine.afterWorking();
      if (this.lastRecipe != null) {
         RecipeRunnerHelper.handleRecipeOutput(this.machine, this.lastRecipe);
      }

      this.depleteVein();
      GTRecipe match = this.getFluidDrillRecipe();
      if (match != null) {
         this.setupRecipe(match);
      } else {
         this.setStatus(Status.IDLE);
         this.progress = 0;
         this.duration = 0;
      }
   }
}
