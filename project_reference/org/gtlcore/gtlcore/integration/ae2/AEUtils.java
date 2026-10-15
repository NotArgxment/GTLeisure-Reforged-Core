package org.gtlcore.gtlcore.integration.ae2;

import appeng.api.config.Actionable;
import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.crafting.IPatternDetails.IInput;
import appeng.api.crafting.IPatternDetails.PatternInputSink;
import appeng.api.networking.IGrid;
import appeng.api.networking.energy.IEnergyService;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.api.storage.StorageHelper;
import appeng.blockentity.crafting.IMolecularAssemblerSupportedPattern;
import appeng.crafting.execution.CraftingCpuHelper;
import appeng.crafting.execution.InputTemplate;
import appeng.crafting.inv.ICraftingInventory;
import appeng.crafting.pattern.AECraftingPattern;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.CraftingPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.crafting.pattern.SmithingTablePatternItem;
import appeng.crafting.pattern.StonecuttingPatternItem;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.gtlcore.gtlcore.api.recipe.ingredient.CacheHashStrategies;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import org.gtlcore.gtlcore.config.AE2CalculationMode;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.jetbrains.annotations.Nullable;

public class AEUtils {
   private static final int BATCH_SIZE = 64;
   private static final int MAX_FAILED_ATTEMPTS = 5;
   public static Function<ItemStack, Boolean> PROCESS_FILTER = stack -> stack.m_41720_() instanceof ProcessingPatternItem;
   public static final AE2CalculationMode CALCULATION_MODE = ConfigHolder.INSTANCE.ae2CalculationMode;

   public static <T extends AEKey> boolean reFunds(Object2LongMap<T> buffer, @Nullable IGrid network, IActionSource actionSource) {
      if (buffer.isEmpty()) {
         return false;
      }

      if (network == null) {
         return false;
      }

      MEStorage networkInv = network.getStorageService().getInventory();
      IEnergyService energy = network.getEnergyService();
      int operationsBatched = 0;
      int consecutiveFailures = 0;
      boolean didWork = false;
      ObjectIterator<Entry<T>> it = Object2LongMaps.fastIterator(buffer);

      while (it.hasNext() && operationsBatched < 64) {
         Entry<T> entry = (Entry<T>)it.next();
         long amount = entry.getLongValue();
         if (amount <= 0L) {
            it.remove();
         } else {
            long inserted = StorageHelper.poweredInsert(energy, networkInv, (AEKey)entry.getKey(), amount, actionSource);
            operationsBatched++;
            if (inserted > 0L) {
               didWork = true;
               consecutiveFailures = 0;
               long left = amount - inserted;
               if (left <= 0L) {
                  it.remove();
               } else {
                  entry.setValue(left);
               }
            } else if (++consecutiveFailures >= 5) {
               break;
            }
         }
      }

      return didWork;
   }

   public static boolean testFluidIngredient(FluidIngredient fluidIngredient, AEFluidKey fluidKey) {
      if (fluidIngredient.isEmpty()) {
         return false;
      }

      if (fluidIngredient.getNbt() != null && !fluidIngredient.getNbt().equals(fluidKey.getTag())) {
         return false;
      }

      for (FluidStack fluidStack : fluidIngredient.getStacks()) {
         if (fluidStack.getFluid() == fluidKey.getFluid()) {
            return true;
         }
      }

      return false;
   }

   public static <T> ListTag createListTag(Function<T, CompoundTag> keySerializer, Object2LongMap<T> map) {
      ListTag tag = new ListTag();
      ObjectIterator<Entry<T>> it = Object2LongMaps.fastIterator(map);

      while (it.hasNext()) {
         Entry<T> entry = (Entry<T>)it.next();
         CompoundTag ct = keySerializer.apply((T)entry.getKey());
         ct.m_128356_("real", entry.getLongValue());
         tag.add(ct);
      }

      return tag;
   }

   public static <K> void loadInventory(ListTag tag, Function<CompoundTag, K> keyExtractor, Object2LongMap<K> targetMap) {
      for (Tag t : tag) {
         if (t instanceof CompoundTag ct) {
            K key = keyExtractor.apply(ct);
            long value = ct.m_128454_("real");
            if (key != null && value > 0L) {
               targetMap.put(key, value);
            }
         }
      }
   }

   public static <T> ListTag createListTag(Function<T, CompoundTag> keySerializer, ObjectSet<T> map) {
      ListTag tag = new ListTag();
      ObjectIterator var3 = map.iterator();

      while (var3.hasNext()) {
         T t = (T)var3.next();
         CompoundTag ct = keySerializer.apply(t);
         tag.add(ct);
      }

      return tag;
   }

   public static <K> void loadInventory(ListTag tag, Function<CompoundTag, K> keyExtractor, ObjectSet<K> targetMap) {
      for (Tag t : tag) {
         if (t instanceof CompoundTag ct) {
            K key = keyExtractor.apply(ct);
            if (key != null) {
               targetMap.add(key);
            }
         }
      }
   }

   public static CompoundTag writeTag(@Nullable GenericStack stack) {
      if (stack == null) {
         return new CompoundTag();
      }

      CompoundTag tag = stack.what().toTagGeneric().m_6426_();
      tag.m_128356_("#", stack.amount());
      return tag;
   }

   public static Pair<Object2LongOpenHashMap<Item>, Object2LongOpenHashMap<Fluid>> mergeInternalSlot(MEPatternBufferPartMachine.InternalSlot[] internalSlots) {
      Object2LongOpenHashMap<Item> items = new Object2LongOpenHashMap();
      Object2LongOpenHashMap<Fluid> fluids = new Object2LongOpenHashMap();

      for (MEPatternBufferPartMachine.InternalSlot internalSlot : Arrays.stream(internalSlots)
         .filter(MEPatternBufferPartMachine.InternalSlot::isActive)
         .toList()) {
         ObjectIterator<Entry<AEItemKey>> it = Object2LongMaps.fastIterator(internalSlot.getItemInventory());

         while (it.hasNext()) {
            Entry<AEItemKey> entry = (Entry<AEItemKey>)it.next();
            items.addTo(((AEItemKey)entry.getKey()).getItem(), entry.getLongValue());
         }

         it = Object2LongMaps.fastIterator(internalSlot.getFluidInventory());

         while (it.hasNext()) {
            Entry<AEFluidKey> entry = (Entry<AEFluidKey>)it.next();
            fluids.addTo(((AEFluidKey)entry.getKey()).getFluid(), entry.getLongValue());
         }
      }

      return new ImmutablePair(items, fluids);
   }

   public static Object2LongMap<Ingredient> ingredientsMapWithOutCircuit(List<Ingredient> ingredients, Consumer<Integer> consumer) {
      Object2LongOpenCustomHashMap<Ingredient> result = new Object2LongOpenCustomHashMap(CacheHashStrategies.IngredientHashStrategy.INSTANCE);

      for (Ingredient ingredient : ingredients) {
         ItemStack[] items = ingredient.m_43908_();
         if (items.length != 0 && !items[0].m_41619_()) {
            if (GTItems.INTEGRATED_CIRCUIT.is(items[0].m_41720_())) {
               consumer.accept(IntCircuitBehaviour.getCircuitConfiguration(items[0]));
            } else {
               result.addTo(ingredient, ingredient instanceof LongIngredient longIngredient ? longIngredient.getActualAmount() : items[0].m_41613_());
            }
         }
      }

      return result;
   }

   public static Object2LongMap<Ingredient> ingredientsMap(List<Ingredient> ingredients) {
      Object2LongOpenCustomHashMap<Ingredient> result = new Object2LongOpenCustomHashMap(CacheHashStrategies.IngredientHashStrategy.INSTANCE);

      for (Ingredient ingredient : ingredients) {
         ItemStack[] items = ingredient.m_43908_();
         if (items.length != 0 && !items[0].m_41619_()) {
            result.addTo(ingredient, ingredient instanceof LongIngredient longIngredient ? longIngredient.getActualAmount() : items[0].m_41613_());
         }
      }

      return result;
   }

   public static Object2LongMap<FluidIngredient> fluidIngredientsMap(List<FluidIngredient> ingredients) {
      Object2LongOpenCustomHashMap<FluidIngredient> result = new Object2LongOpenCustomHashMap(CacheHashStrategies.FluidIngredientHashStrategy.INSTANCE);

      for (FluidIngredient ingredient : ingredients) {
         if (!ingredient.isEmpty()) {
            result.addTo(ingredient, ingredient.getAmount());
         }
      }

      return result;
   }

   public static boolean molecularFilter(ItemStack stack, Level level) {
      Item item = stack.m_41720_();
      if (item instanceof CraftingPatternItem craftingPatternItem) {
         AECraftingPattern pattern = craftingPatternItem.decode(stack, level, false);
         return pattern != null ? !hasContainerItems(pattern) : false;
      } else {
         return item instanceof SmithingTablePatternItem || item instanceof StonecuttingPatternItem;
      }
   }

   private static boolean hasContainerItems(AECraftingPattern pattern) {
      IInput[] inputs = pattern.getInputs();

      for (IInput input : inputs) {
         AEKey key = input.getPossibleInputs()[0].what();
         AEKey remainingKey = input.getRemainingKey(key);
         if (remainingKey != null && (!(remainingKey instanceof AEItemKey itemKey) || itemKey.toStack().m_41763_())) {
            return true;
         }
      }

      return false;
   }

   public static Pair<IPatternDetails, @Nullable ObjectSet<Item>> createProcessingFromCraftPattern(
      IMolecularAssemblerSupportedPattern molecularAssemblerSupportedPattern, Level level
   ) {
      IInput[] inputs = molecularAssemblerSupportedPattern.getInputs();
      ObjectArrayList<GenericStack> normalInputs = new ObjectArrayList();
      ObjectSet<Item> remainingInputs = new ObjectArraySet();

      for (IInput input : inputs) {
         GenericStack stack = input.getPossibleInputs()[0];
         AEKey remaining = input.getRemainingKey(stack.what());
         if (remaining != null) {
            assert remaining instanceof AEItemKey;
            if (((AEItemKey)remaining).toStack().m_41763_()) {
               return ImmutablePair.of(null, null);
            }

            remainingInputs.add(((AEItemKey)remaining).getItem());
         } else {
            normalInputs.add(new GenericStack(stack.what(), stack.amount() * input.getMultiplier()));
         }
      }

      ItemStack pattern = PatternDetailsHelper.encodeProcessingPattern(
         (GenericStack[])normalInputs.toArray(new GenericStack[0]), molecularAssemblerSupportedPattern.getOutputs()
      );
      return ImmutablePair.of(PatternDetailsHelper.decodePattern(pattern, level), remainingInputs);
   }

   public static void pushInputsToMEPatternBufferInventory(KeyCounter[] inputHolder, PatternInputSink inputSink) {
      KeyCounter[] var2 = inputHolder;
      int var3 = var2.length;

      for (int var4 = 0; var4 < var3; var4++) {
         for (Entry<AEKey> input : var2[var4]) {
            inputSink.pushInput((AEKey)input.getKey(), input.getLongValue());
         }
      }
   }

   public static KeyCounter[] extractForProcessingPattern(AEProcessingPattern originDetail, ICraftingInventory sourceInv, KeyCounter expectedOutputs) {
      return extractForProcessingPattern(originDetail, sourceInv, expectedOutputs, 1L);
   }

   public static KeyCounter[] extractForProcessingPattern(
      AEProcessingPattern originDetail, ICraftingInventory sourceInv, KeyCounter expectedOutputs, long multiplier
   ) {
      IInput[] inputs = originDetail.getInputs();
      KeyCounter[] inputHolder = new KeyCounter[inputs.length];
      boolean found = true;

      for (int x = 0; x < inputs.length; x++) {
         KeyCounter list = inputHolder[x] = new KeyCounter();
         AEKey key = inputs[x].getPossibleInputs()[0].what();
         long amount = inputs[x].getMultiplier() * multiplier;
         long extracted = extractTemplates(sourceInv, key, amount);
         list.add(key, extracted);
         if (extracted < amount) {
            found = false;
            break;
         }
      }

      if (!found) {
         CraftingCpuHelper.reinjectPatternInputs(sourceInv, inputHolder);
         return null;
      }

      for (GenericStack output : originDetail.getOutputs()) {
         expectedOutputs.add(output.what(), output.amount() * multiplier);
      }

      return inputHolder;
   }

   private static long extractTemplates(ICraftingInventory inv, AEKey key, long amount) {
      if (amount == 0L) {
         return 0L;
      } else {
         long simEx = inv.extract(key, amount, Actionable.SIMULATE);
         if (simEx == 0L) {
            return 0L;
         } else {
            long extracted = inv.extract(key, simEx, Actionable.MODULATE);
            if (extracted != 0L && extracted == simEx) {
               return extracted;
            } else {
               throw new IllegalStateException("Failed to correctly extract whole number. Invalid simulation!");
            }
         }
      }
   }

   public static KeyCounter[] extractForCraftPattern(
      IPatternDetails details, ICraftingInventory sourceInv, Level level, KeyCounter expectedOutputs, KeyCounter expectedContainerItems
   ) {
      IInput[] inputs = details.getInputs();
      KeyCounter[] inputHolder = new KeyCounter[inputs.length];
      boolean found = true;

      for (int x = 0; x < inputs.length; x++) {
         KeyCounter list = inputHolder[x] = new KeyCounter();
         long remainingMultiplier = inputs[x].getMultiplier();

         for (InputTemplate template : CraftingCpuHelper.getValidItemTemplates(sourceInv, inputs[x], level)) {
            long extracted = CraftingCpuHelper.extractTemplates(sourceInv, template, remainingMultiplier);
            list.add(template.key(), extracted * template.amount());
            AEKey containerItem = inputs[x].getRemainingKey(template.key());
            if (containerItem != null) {
               expectedContainerItems.add(containerItem, extracted);
            }

            remainingMultiplier -= extracted;
            if (remainingMultiplier == 0L) {
               break;
            }
         }

         if (remainingMultiplier > 0L) {
            found = false;
            break;
         }
      }

      if (!found) {
         CraftingCpuHelper.reinjectPatternInputs(sourceInv, inputHolder);
         return null;
      }

      for (GenericStack output : details.getOutputs()) {
         expectedOutputs.add(output.what(), output.amount());
      }

      return inputHolder;
   }
}
