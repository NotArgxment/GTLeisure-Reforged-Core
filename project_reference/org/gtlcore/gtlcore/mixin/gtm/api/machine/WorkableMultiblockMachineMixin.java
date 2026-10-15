package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDistinctPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMufflerMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.IRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;
import com.hepdd.gtmthings.common.block.machine.multiblock.part.appeng.MEOutputPartMachine;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.machine.trait.IMERecipeHandlerTrait;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEIORecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEPatternRecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.RecipeHandlePart;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternPartMachine;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.utils.datastructure.FirstFlagRecipePartSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorkableMultiblockMachine.class)
public abstract class WorkableMultiblockMachineMixin extends MultiblockControllerMachine implements IRecipeCapabilityMachine, IRecipeLogicMachine {
   @Shadow(remap = false)
   @Final
   protected List<ISubscription> traitSubscriptions;
   @Shadow(remap = false)
   @Final
   public RecipeLogic recipeLogic;
   @Shadow(remap = false)
   @Nullable
   protected LongSet activeBlocks;
   @Persisted
   @DescSynced
   private boolean isDistinct = false;
   private boolean meOutPutBus = false;
   private boolean meOutPutHatch = false;
   private boolean meOutPutDual = false;
   private boolean meOutPutWithFilter = false;
   private boolean meItemOutPutWithFilter = false;
   private boolean meFluidOutPutWithFilter = false;
   private boolean itemOutPutAlwaysMatch = false;
   private boolean fluidOutPutAlwaysMatch = false;
   @Nullable
   private IParallelHatch parallelHatch = null;
   @Nullable
   private IMaintenanceMachine maintenanceMachine = null;
   @Nullable
   private IMufflerMachine mufflerMachine = null;
   @Nullable
   private IDataAccessHatch dataAccessHatch = null;
   @Nullable
   private RecipeHandlePart sharedInputRecipeHandlePart = null;
   private final Map<IO, List<RecipeHandlePart>> normalCapabilities = new EnumMap<>(IO.class);
   private final ObjectList<MEPatternRecipeHandlePart> mePatternRecipeHandleParts = new ObjectArrayList();
   private final ObjectList<MEIORecipeHandlePart<?>> meOutputRecipeHandleParts = new ObjectArrayList();
   private final Object2ObjectMap<GTRecipe, FirstFlagRecipePartSet> recipeHandleMap = new Object2ObjectOpenHashMap();

   public WorkableMultiblockMachineMixin(IMachineBlockEntity holder) {
      super(holder);
   }

   @Inject(method = "onStructureFormed", at = @At("TAIL"), remap = false)
   public void onStructureFormed(CallbackInfo ci) {
      if (this.getLevel() instanceof ServerLevel sl) {
         sl.m_7654_().execute(this::upDate);
      }
   }

   @Inject(method = "onStructureInvalid", at = @At("TAIL"), remap = false)
   public void onStructureInvalid(CallbackInfo ci) {
      this.clear();
      RecipeResult.of(this, null);
      RecipeResult.ofWorking(this, null);
   }

   @Inject(method = "onPartUnload", at = @At("TAIL"), remap = false)
   public void onPartUnload(CallbackInfo ci) {
      this.clear();
   }

   @Inject(method = "setActiveRecipeType", at = @At("TAIL"), remap = false)
   public void afterSetActiveRecipeType(int activeRecipeType, CallbackInfo ci) {
      if (!this.isRemote()) {
         this.recipeLogic.updateTickSubscription();
      }
   }

   @Overwrite(remap = false)
   @Nullable
   public final GTRecipe doModifyRecipe(GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (this.maintenanceMachine != null) {
         recipe = this.maintenanceMachine.modifyRecipe(recipe);
      }

      if (recipe != null && this.mufflerMachine != null) {
         recipe = this.mufflerMachine.modifyRecipe(recipe);
      }

      if (recipe != null && this.dataAccessHatch != null) {
         recipe = this.dataAccessHatch.modifyRecipe(recipe);
      }

      return recipe != null ? this.self().getDefinition().getRecipeModifier().apply(this.self(), recipe, params, result) : null;
   }

   @Overwrite(remap = false)
   public boolean onWorking() {
      return this.self().getDefinition().getOnWorking().test(this);
   }

   @Overwrite(remap = false)
   public void onWaiting() {
   }

   @Overwrite(remap = false)
   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      return this.self().getDefinition().getBeforeWorking().test(this, recipe);
   }

   @Overwrite(remap = false)
   public void afterWorking() {
      if (this.maintenanceMachine != null) {
         this.maintenanceMachine.afterWorking((IWorkableMultiController)this);
      }

      if (this.mufflerMachine != null) {
         this.mufflerMachine.afterWorking((IWorkableMultiController)this);
      }

      this.self().getDefinition().getAfterWorking().accept(this);
   }

   @Overwrite(remap = false)
   public void updateActiveBlocks(boolean active) {
      if (this.activeBlocks != null) {
         LongIterator var2 = this.activeBlocks.iterator();

         while (var2.hasNext()) {
            long pos = (Long)var2.next();
            BlockPos blockPos = BlockPos.m_122022_(pos);
            BlockState blockState = this.getLevel().m_8055_(blockPos);
            if (blockState.m_60734_() instanceof ActiveBlock block) {
               BlockState newState = block.changeActive(blockState, active);
               if (newState != blockState) {
                  this.getLevel().m_7731_(blockPos, newState, 18);
               }
            }
         }
      }
   }

   @Override
   public boolean isRecipeOutputAlwaysMatch(GTRecipe recipe) {
      if (this.meOutPutWithFilter) {
         return false;
      } else if (this.meOutPutDual) {
         return true;
      } else {
         return !this.meOutPutBus && !recipe.getOutputContents(ItemRecipeCapability.CAP).isEmpty()
            ? false
            : this.meOutPutHatch || recipe.getOutputContents(FluidRecipeCapability.CAP).isEmpty();
      }
   }

   @Override
   public boolean itemOutPutAlwaysMatch() {
      return this.itemOutPutAlwaysMatch;
   }

   @Override
   public boolean fluidOutPutAlwaysMatch() {
      return this.fluidOutPutAlwaysMatch;
   }

   @Nullable
   @Override
   public IRecipeHandlePart getActiveRecipeHandle(GTRecipe recipe) {
      FirstFlagRecipePartSet handlers = (FirstFlagRecipePartSet)this.recipeHandleMap.get(recipe);
      return handlers != null ? handlers.getActive() : null;
   }

   @NotNull
   @Override
   public Iterator<IRecipeHandlePart> getAllCachedRecipeHandlesIter(GTRecipe recipe) {
      FirstFlagRecipePartSet handlers = (FirstFlagRecipePartSet)this.recipeHandleMap.get(recipe);
      return handlers != null ? handlers.getReverseIterator() : ObjectIterators.emptyIterator();
   }

   @NotNull
   @Override
   public ReferenceSet<IRecipeHandlePart> getAllCachedRecipeHandles(GTRecipe recipe) {
      FirstFlagRecipePartSet handlers = (FirstFlagRecipePartSet)this.recipeHandleMap.get(recipe);
      return handlers != null ? handlers.getAll() : ReferenceSets.emptySet();
   }

   @Override
   public void tryAddAndActiveMERhp(MEPatternRecipeHandlePart part, GTRecipe recipe, int slot) {
      part.setLastRecipe2Slot(recipe, slot);
      this.tryAddAndActiveRhp(recipe, part);
   }

   @Override
   public void tryAddAndActiveRhp(GTRecipe recipe, IRecipeHandlePart part) {
      ((FirstFlagRecipePartSet)this.recipeHandleMap.computeIfAbsent(recipe, k -> new FirstFlagRecipePartSet())).addOrSetActive(part);
   }

   @Override
   public void sortMEOutput() {
      if (!this.meOutputRecipeHandleParts.isEmpty()) {
         this.meOutputRecipeHandleParts.sort(MEPatternRecipeHandlePart.COMPARATOR.reversed());
         boolean allHaveItemFilter = true;
         boolean allHaveFluidFilter = true;
         ObjectListIterator var3 = this.meOutputRecipeHandleParts.iterator();

         while (var3.hasNext()) {
            MEIORecipeHandlePart<?> meOutputRecipeHandlePart = (MEIORecipeHandlePart<?>)var3.next();
            if (!meOutputRecipeHandlePart.hasItemFilter()) {
               allHaveItemFilter = false;
            }

            if (!meOutputRecipeHandlePart.hasFluidFilter()) {
               allHaveFluidFilter = false;
            }

            if (!allHaveItemFilter && !allHaveFluidFilter) {
               break;
            }
         }

         this.meItemOutPutWithFilter = allHaveItemFilter;
         this.meFluidOutPutWithFilter = allHaveFluidFilter;
         this.meOutPutWithFilter = this.meItemOutPutWithFilter || this.meFluidOutPutWithFilter;
         this.itemOutPutAlwaysMatch = !this.meItemOutPutWithFilter && (this.meOutPutBus || this.meOutPutDual);
         this.fluidOutPutAlwaysMatch = !this.meFluidOutPutWithFilter && (this.meOutPutHatch || this.meOutPutDual);
      }
   }

   @Override
   public boolean emptyRecipeHandlePart() {
      return this.normalCapabilities.isEmpty() && this.mePatternRecipeHandleParts.isEmpty() && this.sharedInputRecipeHandlePart == null;
   }

   @Override
   public boolean emptyHandlePart() {
      return this.normalCapabilities.isEmpty()
         && this.mePatternRecipeHandleParts.isEmpty()
         && this.meOutputRecipeHandleParts.isEmpty()
         && this.sharedInputRecipeHandlePart == null;
   }

   @Nullable
   @Override
   public RecipeHandlePart getSharedRecipeHandlePart() {
      return this.sharedInputRecipeHandlePart;
   }

   @Override
   public List<MEPatternRecipeHandlePart> getMEPatternRecipeHandleParts() {
      return this.mePatternRecipeHandleParts;
   }

   @Override
   public List<MEIORecipeHandlePart<?>> getMEOutputRecipeHandleParts() {
      return this.meOutputRecipeHandleParts;
   }

   @NotNull
   @Override
   public List<RecipeHandlePart> getNormalRecipeHandlePart(IO io) {
      return this.normalCapabilities.getOrDefault(io, Collections.emptyList());
   }

   @Override
   public void upDate() {
      if (this.isFormed) {
         this.rebuildRecipeHandleParts(this.traitSubscriptions, this.getParts(), this.recipeLogic, this.isDistinct);
      }
   }

   @Nullable
   @Override
   public IParallelHatch getParallelHatch() {
      return this.parallelHatch;
   }

   @Nullable
   @Override
   public IMaintenanceMachine getMaintenanceMachine() {
      return this.maintenanceMachine;
   }

   @Nullable
   @Override
   public IDataAccessHatch getDataAccessHatch() {
      return this.dataAccessHatch;
   }

   private void rebuildRecipeHandleParts(List<ISubscription> traitSubscriptions, List<IMultiPart> parts, RecipeLogic recipeLogic, boolean isDistinct) {
      this.clear();
      ObjectArrayList<IRecipeHandler<?>> sharedInputHandlers = new ObjectArrayList();

      for (IMultiPart part : parts) {
         if (part instanceof IMEFilterIOPartMachine mePart) {
            this.handleMETraitPart(traitSubscriptions, recipeLogic, mePart);
         } else if (!(part instanceof FluidHatchPartMachine) && !(part instanceof IDistinctPart)) {
            this.handleSpecialPart(part);
         } else {
            this.handleNormalPart(part, isDistinct, sharedInputHandlers);
         }
      }

      if (!isDistinct && !sharedInputHandlers.isEmpty()) {
         this.sharedInputRecipeHandlePart = RecipeHandlePart.of(IO.IN, sharedInputHandlers);
         this.mergeSharedHandlePart(this.sharedInputRecipeHandlePart);
      }

      this.sortMEOutput();
   }

   private void handleMETraitPart(List<ISubscription> traitSubscriptions, RecipeLogic recipeLogic, IMEFilterIOPartMachine mePart) {
      Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> pair = mePart.getMERecipeHandlerTraits();
      traitSubscriptions.add(((IMERecipeHandlerTrait)pair.left()).addBufferChangedListener(recipeLogic::updateTickSubscription));
      traitSubscriptions.add(((IMERecipeHandlerTrait)pair.right()).addBufferChangedListener(recipeLogic::updateTickSubscription));
      IO io = mePart.getMETrait().getIO();
      if (mePart instanceof IMEPatternPartMachine mePatternPart) {
         MEPatternRecipeHandlePart me = MEPatternRecipeHandlePart.of(mePatternPart);
         me.restoreMachineCache(this::tryAddAndActiveRhp);
         this.mePatternRecipeHandleParts.add(me);
         if (io == IO.BOTH) {
            this.meOutputRecipeHandleParts.add(me);
            this.setAllMEOutputFlags();
         }
      } else if (io == IO.OUT) {
         this.meOutputRecipeHandleParts.add(MEIORecipeHandlePart.of(mePart));
         this.setAllMEOutputFlags();
      }
   }

   private void handleNormalPart(IMultiPart part, boolean isDistinct, List<IRecipeHandler<?>> sharedInputHandlers) {
      if (part instanceof MEOutputPartMachine) {
         this.setAllMEOutputFlags();
      } else if (part instanceof MEOutputBusPartMachine) {
         this.meOutPutBus = true;
      } else if (part instanceof MEOutputHatchPartMachine) {
         this.meOutPutHatch = true;
      }

      List<IRecipeHandler<?>> fluidHandlers = new ObjectArrayList();
      List<IRecipeHandler<?>> isolableHandlers = new ObjectArrayList();
      List<IRecipeHandler<?>> outputHandlers = new ObjectArrayList();
      boolean isOutput = false;

      for (IRecipeHandlerTrait handler : part.getRecipeHandlers()) {
         if (!handler.isProxy()) {
            IO io = handler.getHandlerIO();
            if (io == IO.IN) {
               if (handler.getCapability() == FluidRecipeCapability.CAP) {
                  fluidHandlers.add(handler);
               } else {
                  isolableHandlers.add(handler);
               }
            } else if (io == IO.OUT) {
               isOutput = true;
               outputHandlers.add(handler);
            }
         }
      }

      if (isOutput) {
         this.normalCapabilities.computeIfAbsent(IO.OUT, ignore -> new ReferenceArrayList()).add(RecipeHandlePart.of(IO.OUT, outputHandlers));
      } else if (isDistinct) {
         isolableHandlers.addAll(fluidHandlers);
         this.normalCapabilities.computeIfAbsent(IO.IN, ignore -> new ReferenceArrayList()).add(RecipeHandlePart.of(IO.IN, isolableHandlers));
      } else if (part instanceof IDistinctPart distinctPart && distinctPart.isDistinct()) {
         this.normalCapabilities.computeIfAbsent(IO.IN, ignore -> new ReferenceArrayList()).add(RecipeHandlePart.of(IO.IN, isolableHandlers));
         sharedInputHandlers.addAll(fluidHandlers);
      } else {
         sharedInputHandlers.addAll(isolableHandlers);
         sharedInputHandlers.addAll(fluidHandlers);
      }
   }

   private void handleSpecialPart(IMultiPart part) {
      if (part instanceof IParallelHatch parallel) {
         this.parallelHatch = parallel;
      } else if (part instanceof IMufflerMachine muffler) {
         this.mufflerMachine = muffler;
      } else if (part instanceof IMaintenanceMachine maintenance) {
         this.maintenanceMachine = maintenance;
      } else if (part instanceof IDataAccessHatch data) {
         this.dataAccessHatch = data;
      }
   }

   private void mergeSharedHandlePart(@NotNull RecipeHandlePart shared) {
      for (List<RecipeHandlePart> value : this.normalCapabilities.values()) {
         for (RecipeHandlePart part : value) {
            part.addSharedFluidHandlers(shared.getCapability(FluidRecipeCapability.CAP));
         }
      }
   }

   private void setAllMEOutputFlags() {
      this.meOutPutBus = true;
      this.meOutPutHatch = true;
      this.meOutPutDual = true;
   }

   private void clear() {
      this.meOutPutBus = false;
      this.meOutPutHatch = false;
      this.meOutPutDual = false;
      this.meOutPutWithFilter = false;
      this.meItemOutPutWithFilter = false;
      this.meFluidOutPutWithFilter = false;
      this.itemOutPutAlwaysMatch = false;
      this.fluidOutPutAlwaysMatch = false;
      this.parallelHatch = null;
      this.mufflerMachine = null;
      this.maintenanceMachine = null;
      this.dataAccessHatch = null;
      this.sharedInputRecipeHandlePart = null;
      this.normalCapabilities.clear();
      this.mePatternRecipeHandleParts.clear();
      this.meOutputRecipeHandleParts.clear();
      this.recipeHandleMap.clear();
   }

   @Override
   public boolean isDistinct() {
      return this.isDistinct;
   }

   @Override
   public void setDistinct(boolean isDistinct) {
      this.isDistinct = isDistinct;
   }
}
