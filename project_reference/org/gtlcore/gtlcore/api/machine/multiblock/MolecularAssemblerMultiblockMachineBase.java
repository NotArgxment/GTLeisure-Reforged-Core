package org.gtlcore.gtlcore.api.machine.multiblock;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMufflableMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic.Status;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.machine.trait.IMolecularAssemblerMachine;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftIOPart;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftParallelCore;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftPatternContainer;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftSpeedCore;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMolecularAssemblerHandler;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.NotifiableMAHandlerTrait;
import org.gtlcore.gtlcore.common.machine.trait.MolecularAssemblerRecipesLogic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MolecularAssemblerMultiblockMachineBase
   extends MultiblockControllerMachine
   implements IWorkableMultiController,
   IMufflableMachine,
   ParallelMachine,
   IMolecularAssemblerMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MolecularAssemblerMultiblockMachineBase.class, MultiblockControllerMachine.MANAGED_FIELD_HOLDER
   );
   private final GTRecipeType[] recipeTypes = this.getDefinition().getRecipeTypes();
   protected final List<ISubscription> traitSubscriptions;
   @Persisted
   @DescSynced
   public final RecipeLogic recipeLogic;
   @Persisted
   private int activeRecipeType = 0;
   @Nullable
   protected LongSet activeBlocks;
   @Persisted
   @DescSynced
   protected boolean isMuffled;
   protected boolean previouslyMuffled = true;
   @Nullable
   protected IMolecularAssemblerHandler molecularAssemblerHandler = null;
   protected int tickDuration = 40;
   protected int maxParallel = 0;
   protected int patternSize = 0;

   public MolecularAssemblerMultiblockMachineBase(IMachineBlockEntity holder, Object... args) {
      super(holder);
      this.recipeLogic = this.createRecipeLogic(args);
      this.traitSubscriptions = new ObjectArrayList();
   }

   protected RecipeLogic createRecipeLogic(Object... args) {
      return new MolecularAssemblerRecipesLogic(this);
   }

   @Override
   public IMolecularAssemblerHandler getMAHandler() {
      return this.molecularAssemblerHandler;
   }

   public void updateActiveBlocks(boolean active) {
      if (this.activeBlocks != null) {
         LongIterator var2 = this.activeBlocks.iterator();

         while (var2.hasNext()) {
            long pos = (Long)var2.next();
            BlockPos blockPos = BlockPos.m_122022_(pos);
            BlockState blockState = Objects.requireNonNull(this.getLevel()).m_8055_(blockPos);
            if (blockState.m_60734_() instanceof ActiveBlock activeBlock) {
               BlockState newState = activeBlock.changeActive(blockState, active);
               if (newState != blockState) {
                  this.getLevel().m_46597_(blockPos, newState);
               }
            }
         }
      }
   }

   public void onUnload() {
      super.onUnload();
      this.traitSubscriptions.forEach(ISubscription::unsubscribe);
      this.traitSubscriptions.clear();
      this.recipeLogic.inValid();
   }

   public void onPartUnload() {
      super.onPartUnload();
      this.activeBlocks = null;
      this.traitSubscriptions.forEach(ISubscription::unsubscribe);
      this.traitSubscriptions.clear();
      this.recipeLogic.updateTickSubscription();
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.activeBlocks = (LongSet)this.getMultiblockState().getMatchContext().getOrDefault("vaBlocks", LongSets.emptySet());
      this.traitSubscriptions.forEach(ISubscription::unsubscribe);
      this.traitSubscriptions.clear();
      if (!this.isRemote()) {
         this.update();
      }

      RecipeResult.of(this, null);
      RecipeResult.ofWorking(this, null);
   }

   private void update() {
      ObjectSet<BlockPos> patternInventoryPos = new ObjectArraySet();
      int speedTier = 0;
      int totalSlots = 0;
      long totalParallel = 0L;
      IMECraftIOPart meCraftIOPart = null;

      for (IMultiPart part : this.getParts()) {
         if (part instanceof IMECraftPatternContainer craftPatternContainer) {
            patternInventoryPos.add(craftPatternContainer.getBlockPos());
            totalSlots += craftPatternContainer.getSlots();
         } else if (part instanceof IMECraftParallelCore parallelCore) {
            totalParallel += parallelCore.getParallel();
         } else if (part instanceof IMECraftSpeedCore speedCore) {
            speedTier += speedCore.getSpeedTier();
         } else if (part instanceof IMECraftIOPart ioPart) {
            if (meCraftIOPart != null) {
               throw new IllegalStateException("MolecularAssemblerIOMachine already exists");
            }

            meCraftIOPart = ioPart;
         }
      }

      if (totalParallel == 0L) {
         totalParallel = 1L;
      }

      if (meCraftIOPart == null) {
         throw new IllegalStateException("MolecularAssemblerIOMachine doesn't exist");
      }

      this.patternSize = totalSlots;
      this.tickDuration = speedTier <= 39 ? 40 - speedTier : 1;
      this.maxParallel = Ints.saturatedCast(totalParallel);
      meCraftIOPart.init(patternInventoryPos);
      NotifiableMAHandlerTrait handlerTrait = meCraftIOPart.getNotifiableMAHandlerTrait();
      this.traitSubscriptions.add(handlerTrait.addChangedListener(this.recipeLogic::updateTickSubscription));
      this.molecularAssemblerHandler = handlerTrait;
      this.recipeLogic.updateTickSubscription();
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.activeBlocks = null;
      this.traitSubscriptions.forEach(ISubscription::unsubscribe);
      this.traitSubscriptions.clear();
      this.patternSize = 0;
      this.tickDuration = 40;
      this.maxParallel = 0;
      this.molecularAssemblerHandler = null;
      this.recipeLogic.resetRecipeLogic();
   }

   public void clientTick() {
      super.clientTick();
      if (this.previouslyMuffled != this.isMuffled) {
         this.previouslyMuffled = this.isMuffled;
         if (this.recipeLogic != null) {
            this.recipeLogic.updateSound();
         }
      }
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   @NotNull
   public GTRecipeType[] getRecipeTypes() {
      return new GTRecipeType[0];
   }

   @NotNull
   public GTRecipeType getRecipeType() {
      return this.recipeTypes[this.activeRecipeType];
   }

   public int getActiveRecipeType() {
      return this.activeRecipeType;
   }

   public void setActiveRecipeType(int activeRecipeType) {
      this.activeRecipeType = activeRecipeType;
   }

   @NotNull
   public RecipeLogic getRecipeLogic() {
      return this.recipeLogic;
   }

   @Nullable
   public ICleanroomProvider getCleanroom() {
      return null;
   }

   public void setCleanroom(ICleanroomProvider iCleanroomProvider) {
   }

   @NotNull
   public Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> getCapabilitiesProxy() {
      return ImmutableTable.of();
   }

   public boolean keepSubscribing() {
      return false;
   }

   public void notifyStatusChanged(Status oldStatus, Status newStatus) {
      super.notifyStatusChanged(oldStatus, newStatus);
      if (newStatus == Status.WORKING || oldStatus == Status.WORKING) {
         this.updateActiveBlocks(newStatus == Status.WORKING);
      }
   }

   public boolean isRecipeLogicAvailable() {
      return this.isFormed && !this.getMultiblockState().hasError();
   }

   public void afterWorking() {
      for (IMultiPart part : this.getParts()) {
         part.afterWorking(this);
      }

      super.afterWorking();
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      for (IMultiPart part : this.getParts()) {
         if (!part.beforeWorking(this)) {
            return false;
         }
      }

      return super.beforeWorking(recipe);
   }

   public boolean onWorking() {
      for (IMultiPart part : this.getParts()) {
         if (!part.onWorking(this)) {
            return false;
         }
      }

      return super.onWorking();
   }

   public void onWaiting() {
      for (IMultiPart part : this.getParts()) {
         part.onWaiting(this);
      }

      super.onWaiting();
   }

   public void setWorkingEnabled(boolean isWorkingAllowed) {
      if (!isWorkingAllowed) {
         for (IMultiPart part : this.getParts()) {
            part.onPaused(this);
         }
      }

      super.setWorkingEnabled(isWorkingAllowed);
   }

   public void setMuffled(boolean isMuffled) {
      this.isMuffled = isMuffled;
   }

   public boolean isMuffled() {
      return this.isMuffled;
   }

   public int getTickDuration() {
      return this.tickDuration;
   }

   @Override
   public int getMaxParallel() {
      return this.maxParallel;
   }

   public int getPatternSize() {
      return this.patternSize;
   }
}
