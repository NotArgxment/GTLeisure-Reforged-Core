package org.gtlcore.gtlcore.integration.ae2.handler;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.api.machine.trait.NotifiableCircuitItemStackHandler;

public class MEBufferPatternHelper {
   private final NotifiableCircuitItemStackHandler mePatternCircuitInventory;

   public MEBufferPatternHelper(NotifiableCircuitItemStackHandler mePatternCircuitInventory) {
      this.mePatternCircuitInventory = mePatternCircuitInventory;
   }

   public boolean haveSharedPatternCircuit() {
      return !this.mePatternCircuitInventory.storage.getStackInSlot(0).m_41619_();
   }

   public IPatternDetails processPatternWithCircuit(ItemStack originalPatternStack, Consumer<Integer> storedCircuit, Level level, boolean keepByProduct) {
      if (PatternDetailsHelper.decodePattern(originalPatternStack, level) instanceof AEProcessingPattern processingPattern) {
         int extractedCircuit = this.extractCircuitFromPattern(processingPattern);
         if (extractedCircuit < 0) {
            if (keepByProduct) {
               return processingPattern;
            }
         } else {
            storedCircuit.accept(extractedCircuit);
         }

         return this.createPatternWithoutCircuit(processingPattern, level, keepByProduct);
      } else {
         return null;
      }
   }

   public ItemStack getCircuitForRecipe(ItemStack storedCircuit) {
      if (storedCircuit != ItemStack.f_41583_ && storedCircuit != null) {
         return storedCircuit;
      } else {
         return this.haveSharedPatternCircuit() ? this.mePatternCircuitInventory.storage.getStackInSlot(0) : ItemStack.f_41583_;
      }
   }

   private int extractCircuitFromPattern(AEProcessingPattern processingPattern) {
      for (GenericStack input : Arrays.stream(processingPattern.getSparseInputs()).filter(Objects::nonNull).toList()) {
         if (input.what() instanceof AEItemKey itemKey) {
            ItemStack itemStack = itemKey.toStack();
            if (IntCircuitBehaviour.isIntegratedCircuit(itemStack)) {
               return IntCircuitBehaviour.getCircuitConfiguration(itemStack);
            }
         }
      }

      return -1;
   }

   private IPatternDetails createPatternWithoutCircuit(AEProcessingPattern pattern, Level level, boolean keepByProduct) {
      GenericStack[] originalInputs = pattern.getSparseInputs();
      GenericStack[] originalOutputs = pattern.getSparseOutputs();
      ObjectArrayList<GenericStack> filteredInputs = new ObjectArrayList();
      GenericStack[] filteredOutputs = originalOutputs;

      for (GenericStack input : Arrays.stream(originalInputs).filter(Objects::nonNull).toList()) {
         if (!(input.what() instanceof AEItemKey itemKey && itemKey.getItem() == GTItems.INTEGRATED_CIRCUIT.m_5456_())) {
            filteredInputs.add(input);
         }
      }

      if (!keepByProduct) {
         Optional<GenericStack> primary = Arrays.stream(originalOutputs).filter(Objects::nonNull).findFirst();
         if (primary.isPresent()) {
            filteredOutputs = new GenericStack[]{primary.get()};
         }
      }

      return PatternDetailsHelper.decodePattern(
         PatternDetailsHelper.encodeProcessingPattern((GenericStack[])filteredInputs.toArray(new GenericStack[0]), filteredOutputs), level
      );
   }
}
