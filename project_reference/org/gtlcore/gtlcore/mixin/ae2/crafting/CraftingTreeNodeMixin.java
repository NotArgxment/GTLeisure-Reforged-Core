package org.gtlcore.gtlcore.mixin.ae2.crafting;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.IPatternDetails.IInput;
import appeng.api.networking.crafting.ICraftingService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.crafting.CraftBranchFailure;
import appeng.crafting.CraftingCalculation;
import appeng.crafting.CraftingTreeNode;
import appeng.crafting.CraftingTreeProcess;
import appeng.crafting.execution.CraftingCpuHelper;
import appeng.crafting.execution.InputTemplate;
import appeng.crafting.inv.ChildCraftingSimulationState;
import appeng.crafting.inv.CraftingSimulationState;
import appeng.crafting.inv.ICraftingInventory;
import appeng.crafting.pattern.AEProcessingPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingCalculation;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingTreeNode;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingTreeProcess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingTreeNode.class)
public abstract class CraftingTreeNodeMixin implements ICraftingTreeNode {
   @Unique
   private IPatternDetails patternDetails;
   @Shadow(remap = false)
   @Final
   @Mutable
   final @Nullable IInput parentInput;
   @Shadow(remap = false)
   @Final
   @Mutable
   private final Level level;
   @Shadow(remap = false)
   @Final
   @Mutable
   private final AEKey what;
   @Shadow(remap = false)
   @Final
   private CraftingCalculation job;
   @Shadow(remap = false)
   private ArrayList<CraftingTreeProcess> nodes;
   @Shadow(remap = false)
   @Final
   private long amount;
   @Shadow(remap = false)
   @Final
   private boolean canEmit;

   public CraftingTreeNodeMixin(@Nullable IInput parentInput, Level level, AEKey what) {
      this.parentInput = parentInput;
      this.level = level;
      this.what = what;
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   private void CraftingTreeNode(ICraftingService cc, CraftingCalculation job, AEKey what, long amount, CraftingTreeProcess par, int slot, CallbackInfo ci) {
      this.patternDetails = slot == -1 ? null : ((ICraftingTreeProcess)par).getDetails();
   }

   @Shadow(remap = false)
   private void addContainerItems(AEKey template, long multiplier, @Nullable KeyCounter outputList) {
      throw new AssertionError();
   }

   @Shadow(remap = false)
   private void buildChildPatterns() {
      throw new AssertionError();
   }

   @Shadow(remap = false)
   void request(CraftingSimulationState inv, long requestedAmount, @Nullable KeyCounter containerItems) throws CraftBranchFailure, InterruptedException {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   private Iterable<InputTemplate> getValidItemTemplates(ICraftingInventory inv) {
      if (this.parentInput == null) {
         return List.of(new InputTemplate(this.what, 1L));
      } else if (this.patternDetails instanceof AEProcessingPattern) {
         GenericStack stack = this.parentInput.getPossibleInputs()[0];
         return List.of(new InputTemplate(stack.what(), stack.amount()));
      } else {
         return CraftingCpuHelper.getValidItemTemplates(inv, this.parentInput, this.level);
      }
   }

   @Unique
   @Override
   public void legacyRequest(CraftingSimulationState inv, long requestedAmount, @Nullable KeyCounter containerItems) throws CraftBranchFailure, InterruptedException {
      this.request(inv, requestedAmount, containerItems);
   }

   @Unique
   @Override
   public void fastRequest(CraftingSimulationState inv, long requestedAmount, @Nullable KeyCounter containerItems) throws CraftBranchFailure, InterruptedException {
      ((ICraftingCalculation)this.job).handlePausing();
      inv.addStackBytes(this.what, this.amount, requestedAmount);

      for (InputTemplate template : this.getValidItemTemplates(inv)) {
         long extracted = CraftingCpuHelper.extractTemplates(inv, template, requestedAmount);
         if (extracted > 0L) {
            requestedAmount -= extracted;
            this.addContainerItems(template.key(), extracted, containerItems);
            if (requestedAmount == 0L) {
               return;
            }
         }
      }

      this.addContainerItems(this.what, requestedAmount, containerItems);
      if (this.canEmit) {
         inv.emitItems(this.what, this.amount * requestedAmount);
      } else {
         this.buildChildPatterns();
         long totalRequestedItems = requestedAmount * this.amount;
         if (this.nodes.size() == 1) {
            ICraftingTreeProcess pro = (ICraftingTreeProcess)this.nodes.get(0);
            long craftedPerPattern = pro.getOutputCountTest(this.what);

            while (pro.getPossible() && totalRequestedItems > 0L) {
               long times;
               if (pro.limitsQuantityTest()) {
                  times = 1L;
               } else {
                  times = (totalRequestedItems + craftedPerPattern - 1L) / craftedPerPattern;
               }

               pro.fastRequest(inv, times);
               long available = inv.extract(this.what, totalRequestedItems, Actionable.MODULATE);
               if (available == 0L) {
                  AEItemKey pattern = pro.getDetails().getDefinition();
                  String outputs = Stream.of(pro.getDetails().getOutputs()).<CharSequence>map(GenericStack::toString).collect(Collectors.joining(", "));
                  String errorMessage = "Unexpected error in the crafting calculation: can't find created items.\nThis is an AE2 bug, please report it, with the following important information:\n\n- Found none of %s. Remaining request: %d of %d*%d.\n- Tried crafting %d times the pattern %s with tag %s.\n- Pattern outputs: %s.\n"
                     .formatted(this.what, totalRequestedItems, requestedAmount, this.amount, times, pattern, pattern.getTag(), outputs);
                  throw new UnsupportedOperationException(errorMessage);
               }

               totalRequestedItems -= available;
               if (totalRequestedItems <= 0L) {
                  return;
               }
            }
         } else if (this.nodes.size() > 1) {
            while (totalRequestedItems > 0L) {
               int processCount = this.nodes.size();
               long baseAmount = totalRequestedItems / processCount;
               long remainder = totalRequestedItems % processCount;
               boolean anySucceeded = false;

               for (int i = 0; i < this.nodes.size(); i++) {
                  ICraftingTreeProcess pro = (ICraftingTreeProcess)this.nodes.get(i);
                  if (pro.getPossible()) {
                     long targetAmount = baseAmount + (i < remainder ? 1 : 0);
                     if (targetAmount > 0L) {
                        try {
                           long craftedPerPattern = pro.getOutputCountTest(this.what);
                           long times = pro.limitsQuantityTest() ? 1L : (targetAmount + craftedPerPattern - 1L) / craftedPerPattern;
                           if (times > 0L) {
                              ChildCraftingSimulationState child = new ChildCraftingSimulationState(inv);
                              pro.fastRequest(child, times);
                              long available = child.extract(this.what, targetAmount, Actionable.MODULATE);
                              if (available != 0L) {
                                 child.applyDiff(inv);
                                 anySucceeded = true;
                                 totalRequestedItems -= available;
                                 if (totalRequestedItems <= 0L) {
                                    return;
                                 }
                              } else {
                                 pro.setPossible(false);
                              }
                           }
                        } catch (CraftBranchFailure var24) {
                        }
                     }
                  }
               }

               if (!anySucceeded) {
                  break;
               }
            }
         }

         if (this.job.isSimulation()) {
            this.job.getMissingItems().add(this.what, totalRequestedItems);
         } else {
            throw new CraftBranchFailure(this.what, totalRequestedItems);
         }
      }
   }

   @Unique
   @Override
   public void ultraFastRequest(CraftingSimulationState inv, long requestedAmount, @Nullable KeyCounter containerItems) throws CraftBranchFailure, InterruptedException {
      ((ICraftingCalculation)this.job).handlePausing();
      inv.addStackBytes(this.what, this.amount, requestedAmount);

      for (InputTemplate template : this.getValidItemTemplates(inv)) {
         long extracted = CraftingCpuHelper.extractTemplates(inv, template, requestedAmount);
         if (extracted > 0L) {
            requestedAmount -= extracted;
            this.addContainerItems(template.key(), extracted, containerItems);
            if (requestedAmount == 0L) {
               return;
            }
         }
      }

      this.addContainerItems(this.what, requestedAmount, containerItems);
      if (this.canEmit) {
         inv.emitItems(this.what, this.amount * requestedAmount);
      } else {
         this.buildChildPatterns();
         long totalRequestedItems = requestedAmount * this.amount;
         if (this.nodes.size() == 1) {
            ICraftingTreeProcess pro = (ICraftingTreeProcess)this.nodes.get(0);
            long craftedPerPattern = pro.getOutputCountTest(this.what);

            while (pro.getPossible() && totalRequestedItems > 0L) {
               long times;
               if (pro.limitsQuantityTest()) {
                  times = 1L;
               } else {
                  times = (totalRequestedItems + craftedPerPattern - 1L) / craftedPerPattern;
               }

               pro.ultraFastRequest(inv, times);
               long available = inv.extract(this.what, totalRequestedItems, Actionable.MODULATE);
               if (available == 0L) {
                  AEItemKey pattern = pro.getDetails().getDefinition();
                  String outputs = Stream.of(pro.getDetails().getOutputs()).<CharSequence>map(GenericStack::toString).collect(Collectors.joining(", "));
                  String errorMessage = "Unexpected error in the crafting calculation: can't find created items.\nThis is an AE2 bug, please report it, with the following important information:\n\n- Found none of %s. Remaining request: %d of %d*%d.\n- Tried crafting %d times the pattern %s with tag %s.\n- Pattern outputs: %s.\n"
                     .formatted(this.what, totalRequestedItems, requestedAmount, this.amount, times, pattern, pattern.getTag(), outputs);
                  throw new UnsupportedOperationException(errorMessage);
               }

               totalRequestedItems -= available;
               if (totalRequestedItems <= 0L) {
                  return;
               }
            }
         } else if (this.nodes.size() > 1) {
            for (CraftingTreeProcess node : this.nodes) {
               ICraftingTreeProcess pro = (ICraftingTreeProcess)node;

               try {
                  long craftedPerPattern = pro.getOutputCountTest(this.what);
                  long times = pro.limitsQuantityTest() ? 1L : (totalRequestedItems + craftedPerPattern - 1L) / craftedPerPattern;
                  if (times > 0L) {
                     ChildCraftingSimulationState child = new ChildCraftingSimulationState(inv);
                     pro.ultraFastRequest(child, times);
                     long available = child.extract(this.what, totalRequestedItems, Actionable.MODULATE);
                     if (available != 0L) {
                        child.applyDiff(inv);
                        totalRequestedItems -= available;
                        if (totalRequestedItems <= 0L) {
                           return;
                        }
                     }
                  }
               } catch (CraftBranchFailure var17) {
               }
            }
         }

         if (this.job.isSimulation()) {
            this.job.getMissingItems().add(this.what, totalRequestedItems);
         } else {
            throw new CraftBranchFailure(this.what, totalRequestedItems);
         }
      }
   }
}
