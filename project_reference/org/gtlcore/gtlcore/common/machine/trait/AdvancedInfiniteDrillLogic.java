package org.gtlcore.gtlcore.common.machine.trait;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidVeinSavedData;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.FluidVeinWorldEntry;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Fluid;
import org.gtlcore.gtlcore.api.recipe.RecipeRunnerHelper;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.AdvancedInfiniteDrillMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedInfiniteDrillLogic extends RecipeLogic {
   public static final int MAX_PROGRESS = 20;
   @NotNull
   private final Map<Fluid, Long> veinFluids = new HashMap<>();
   private int range;

   public AdvancedInfiniteDrillLogic(IRecipeLogicMachine machine, int range) {
      super(machine);
      this.range = range;
   }

   public AdvancedInfiniteDrillMachine getMachine() {
      return (AdvancedInfiniteDrillMachine)super.getMachine();
   }

   public void findAndHandleRecipe() {
      if (this.getMachine().getLevel() instanceof ServerLevel serverLevel) {
         this.lastRecipe = null;
         BedrockFluidVeinSavedData data = BedrockFluidVeinSavedData.getOrCreate(serverLevel);
         if (this.veinFluids.isEmpty()) {
            this.getGridFluid(data);
            if (this.veinFluids.isEmpty() && this.subscription != null) {
               this.subscription.unsubscribe();
               this.subscription = null;
            }
         }

         if (this.getMachine().isEmpty() || !this.getMachine().canRunnable()) {
            return;
         }

         GTRecipe match = this.getFluidDrillRecipe();
         if (match != null) {
            GTRecipe copied = match.copy(new ContentModifier(match.duration, 0.0));
            if (RecipeRunnerHelper.matchRecipe(this.machine, match) && copied.matchTickRecipe(this.machine).isSuccess()) {
               this.setupRecipe(match);
            }
         }
      }
   }

   @Nullable
   private GTRecipe getFluidDrillRecipe() {
      if (!this.veinFluids.isEmpty()) {
         long total = this.veinFluids.values().stream().mapToLong(Long::longValue).sum();
         GTRecipe recipe = GTRecipeBuilder.ofRaw()
            .duration(20)
            .EUt(GTValues.V[9] + total)
            .outputFluids(this.veinFluids.entrySet().stream().map(entry -> FluidStack.create(entry.getKey(), entry.getValue())).toArray(FluidStack[]::new))
            .buildRawRecipe();
         recipe = recipe.copy(ContentModifier.multiplier(this.getMachine().getRate()), false);
         if (RecipeRunnerHelper.matchRecipe(this.getMachine(), recipe) && recipe.matchTickRecipe(this.getMachine()).isSuccess()) {
            return recipe;
         }
      }

      return null;
   }

   private long getFluidToProduce(FluidVeinWorldEntry entry) {
      BedrockFluidDefinition definition = entry.getDefinition();
      if (definition != null) {
         int depletedYield = definition.getDepletedYield();
         int regularYield = entry.getFluidYield();
         int remainingOperations = entry.getOperationsRemaining();
         int produced = Math.max(depletedYield, regularYield * remainingOperations / 100000);
         return produced * FluidHelper.getBucket() / 1000L;
      } else {
         return 0L;
      }
   }

   protected void getGridFluid(BedrockFluidVeinSavedData data) {
      int x = this.getChunkX();
      int z = this.getChunkZ();
      int mid = this.range / 2;

      for (int i = -mid; i <= mid; i++) {
         for (int j = -mid; j <= mid; j++) {
            Fluid fluid = data.getFluidInChunk(x + i, z + j);
            if (fluid != null) {
               long produced = this.getFluidToProduce(data.getFluidVeinWorldEntry(x + i, z + j));
               if (produced > 0L) {
                  long value = this.veinFluids.getOrDefault(fluid, 0L) + produced * 10L;
                  this.veinFluids.put(fluid, value);
               }
            }
         }
      }
   }

   @NotNull
   public Map<Fluid, Long> getVeinFluids() {
      return this.veinFluids;
   }

   public void onRecipeFinish() {
      if (this.lastRecipe != null) {
         RecipeRunnerHelper.handleRecipeOutput(this.machine, this.lastRecipe);
      }

      GTRecipe match = this.getFluidDrillRecipe();
      if (match != null) {
         GTRecipe copied = match.copy(new ContentModifier(match.duration, 0.0));
         if (RecipeRunnerHelper.matchRecipe(this.machine, match) && copied.matchTickRecipe(this.machine).isSuccess()) {
            this.setupRecipe(match);
            return;
         }
      }

      this.setStatus(Status.IDLE);
      this.progress = 0;
      this.duration = 0;
   }

   private int getChunkX() {
      return SectionPos.m_123171_(this.getMachine().getPos().m_123341_());
   }

   private int getChunkZ() {
      return SectionPos.m_123171_(this.getMachine().getPos().m_123343_());
   }

   public int getRange() {
      return this.range;
   }

   public void setRange(int range) {
      this.range = range;
   }
}
