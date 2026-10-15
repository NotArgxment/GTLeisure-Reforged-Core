package org.gtlcore.gtlcore.common.machine.trait;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.INFFluidDrillMachine;
import org.jetbrains.annotations.Nullable;

public class INFFluidDrillLogic extends RecipeLogic {
   public static final int MAX_PROGRESS = 20;
   @Nullable
   private Fluid veinFluid;

   public INFFluidDrillLogic(INFFluidDrillMachine machine) {
      super(machine);
   }

   public INFFluidDrillMachine getMachine() {
      return (INFFluidDrillMachine)super.getMachine();
   }

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

   @Nullable
   private GTRecipe getFluidDrillRecipe() {
      if (this.getMachine().getLevel() instanceof ServerLevel serverLevel && this.veinFluid != null) {
         BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
         GTRecipe recipe = GTRecipeBuilder.ofRaw()
            .duration(20)
            .EUt(GTValues.VA[this.getMachine().getEnergyTier()])
            .outputFluids(FluidStack.create(this.veinFluid, this.getFluidToProduce(data.getFluidVeinWorldEntry(this.getChunkX(), this.getChunkZ()))))
            .buildRawRecipe();
         if (RecipeRunnerHelper.matchRecipeOutput(this.getMachine(), recipe) && recipe.matchTickRecipe(this.getMachine()).isSuccess()) {
            return recipe;
         }
      }

      return null;
   }

   public long getFluidToProduce() {
      if (this.getMachine().getLevel() instanceof ServerLevel serverLevel && this.veinFluid != null) {
         BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
         return this.getFluidToProduce(data.getFluidVeinWorldEntry(this.getChunkX(), this.getChunkZ()));
      } else {
         return 0L;
      }
   }

   private long getFluidToProduce(FluidVeinWorldEntry entry) {
      BedrockFluidDefinition definition = entry.getDefinition();
      if (definition != null) {
         int depletedYield = definition.getDepletedYield();
         int regularYield = entry.getFluidYield();
         int remainingOperations = entry.getOperationsRemaining();
         int produced = Math.max(depletedYield, regularYield * remainingOperations / 100000);
         produced *= INFFluidDrillMachine.getRigMultiplier(this.getMachine().getTier());
         if (this.isOverclocked()) {
            produced = produced * 3 / 2;
         }

         return produced * FluidHelper.getBucket() / 1000L;
      } else {
         return 0L;
      }
   }

   public void onRecipeFinish() {
      this.machine.afterWorking();
      if (this.lastRecipe != null) {
         RecipeRunnerHelper.handleRecipeOutput(this.getMachine(), this.lastRecipe);
      }

      GTRecipe match = this.getFluidDrillRecipe();
      if (match != null) {
         this.setupRecipe(match);
      } else {
         this.setStatus(Status.IDLE);
         this.progress = 0;
         this.duration = 0;
      }
   }

   protected boolean isOverclocked() {
      return this.getMachine().getEnergyTier() > this.getMachine().getTier();
   }

   private int getChunkX() {
      return SectionPos.m_123171_(this.getMachine().getPos().m_123341_());
   }

   private int getChunkZ() {
      return SectionPos.m_123171_(this.getMachine().getPos().m_123343_());
   }

   @Nullable
   public Fluid getVeinFluid() {
      return this.veinFluid;
   }
}
