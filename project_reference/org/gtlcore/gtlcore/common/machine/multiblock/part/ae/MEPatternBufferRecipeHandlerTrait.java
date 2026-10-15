package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;

public class MEPatternBufferRecipeHandlerTrait extends MachineTrait {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEPatternBufferPartMachine.class);
   protected final MEPatternBufferRecipeHandlerTrait.MEItemInputHandler meItemHandler;
   protected final MEPatternBufferRecipeHandlerTrait.MEFluidHandler meFluidHandler;

   public MEPatternBufferRecipeHandlerTrait(MEPatternBufferPartMachine ioBuffer, IO io) {
      super(ioBuffer);
      this.meItemHandler = new MEPatternBufferRecipeHandlerTrait.MEItemInputHandler(ioBuffer, io);
      this.meFluidHandler = new MEPatternBufferRecipeHandlerTrait.MEFluidHandler(ioBuffer, io);
   }

   public MEPatternBufferPartMachine getMachine() {
      return (MEPatternBufferPartMachine)super.getMachine();
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void onChanged() {
   }

   private boolean handleItemInner(GTRecipe recipe, Object2LongMap<Ingredient> left, int circuit, boolean simulate, int trySlot) {
      MEPatternBufferPartMachine.InternalSlot internalSlot = this.getMachine().getInternalInventory()[trySlot];
      if (internalSlot.isItemActive(simulate)) {
         return simulate && !internalSlot.testCatalystItemInternal(recipe) ? false : internalSlot.handleItemInternal(left, circuit, simulate);
      } else {
         return left.isEmpty() && circuit < 0;
      }
   }

   private boolean handleFluidInner(GTRecipe recipe, Object2LongMap<FluidIngredient> left, boolean simulate, int trySlot) {
      MEPatternBufferPartMachine.InternalSlot internalSlot = this.getMachine().getInternalInventory()[trySlot];
      if (internalSlot.isFluidActive(simulate)) {
         return simulate && !internalSlot.testCatalystFluidInternal(recipe) ? false : internalSlot.handleFluidInternal(left, simulate);
      } else {
         return left.isEmpty();
      }
   }

   private Set<Integer> getActiveSlots(MEPatternBufferPartMachine.InternalSlot[] slots) {
      Set<Integer> activeSlots = new IntOpenHashSet();

      for (int i = 0; i < slots.length; i++) {
         if (slots[i].isActive()) {
            activeSlots.add(i);
         }
      }

      return activeSlots;
   }

   private int[] getActiveAndUnCachedSlots(MEPatternBufferPartMachine.InternalSlot[] slots) {
      MEPatternBufferPartMachine machine = this.getMachine();
      return IntStream.range(0, slots.length).filter(i -> slots[i].isActive() && !machine.cacheRecipe[i]).toArray();
   }

   public MEPatternBufferRecipeHandlerTrait.MEItemInputHandler getMeItemHandler() {
      return this.meItemHandler;
   }

   public MEPatternBufferRecipeHandlerTrait.MEFluidHandler getMeFluidHandler() {
      return this.meFluidHandler;
   }

   public class MEFluidHandler extends NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> {
      private final IO io;
      private Object2LongMap<FluidIngredient> preparedMEHandleContents = new Object2LongOpenHashMap();

      public MEFluidHandler(MEPatternBufferPartMachine machine, IO io) {
         super(machine);
         this.io = io;
      }

      public MEPatternBufferPartMachine getMachine() {
         return (MEPatternBufferPartMachine)this.machine;
      }

      @Override
      public RecipeCapability<FluidIngredient> getCapability() {
         return FluidRecipeCapability.CAP;
      }

      @Override
      public Set<Integer> getActiveSlots() {
         return MEPatternBufferRecipeHandlerTrait.this.getActiveSlots(this.getMachine().getInternalInventory());
      }

      @Override
      public Int2ObjectMap<List<FluidStack>> getActiveAndUnCachedSlotsLimitContentsMap() {
         Int2ObjectArrayMap<List<FluidStack>> map = new Int2ObjectArrayMap();
         MEPatternBufferPartMachine machine = this.getMachine();
         List<FluidStack> shared = machine.getSharedCatalystTank().getContents();

         for (int slot : MEPatternBufferRecipeHandlerTrait.this.getActiveAndUnCachedSlots(this.getMachine().getInternalInventory())) {
            ObjectList<FluidStack> inputs = machine.getInternalInventory()[slot].getLimitFluidStackInput();
            inputs.addAll(shared);
            map.put(slot, inputs);
         }

         return map;
      }

      @Override
      public Object2LongMap<FluidStack> getStackMapFromFirstAvailableSlot(IntCollection slots) {
         MEPatternBufferPartMachine.InternalSlot[] inventory = this.getMachine().getInternalInventory();
         IntIterator var3 = slots.iterator();

         while (var3.hasNext()) {
            int slot = (Integer)var3.next();
            if (inventory[slot].isActive()) {
               Object2LongOpenHashMap<FluidStack> map = new Object2LongOpenHashMap();
               ObjectIterator var6 = Object2LongMaps.fastIterable(inventory[slot].getFluidStackInputMap()).iterator();

               while (var6.hasNext()) {
                  Entry<FluidStack> entry = (Entry<FluidStack>)var6.next();
                  map.addTo((FluidStack)entry.getKey(), entry.getLongValue());
               }

               return map;
            }
         }

         return Object2LongMaps.emptyMap();
      }

      @Override
      public boolean meHandleRecipeInner(GTRecipe recipe, Object2LongMap<FluidIngredient> left, boolean simulate, int trySlot) {
         return MEPatternBufferRecipeHandlerTrait.this.handleFluidInner(recipe, left, simulate, trySlot);
      }

      @Override
      public void prepareMEHandleContents(GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
         if (simulate) {
            this.getMachine().getSharedCatalystTank().handleRecipeInner(IO.IN, recipe, left, null, true);
         }

         this.setPreparedMEHandleContents(AEUtils.fluidIngredientsMap(left));
      }

      @Override
      public List<FluidIngredient> meHandleRecipeOutputInner(List<FluidIngredient> left, boolean simulate) {
         if (simulate) {
            return List.of();
         }

         Object2LongOpenHashMap<AEKey> buffer = this.getMachine().buffer;

         for (FluidIngredient fluidIngredient : left) {
            if (!fluidIngredient.isEmpty()) {
               FluidStack[] fluids = fluidIngredient.getStacks();
               if (fluids.length != 0) {
                  FluidStack output = fluids[0];
                  buffer.addTo(AEFluidKey.of(output.getFluid()), output.getAmount());
               }
            }
         }

         return List.of();
      }

      @Override
      public IO getIo() {
         return this.io;
      }

      @Override
      public Object2LongMap<FluidIngredient> getPreparedMEHandleContents() {
         return this.preparedMEHandleContents;
      }

      public void setPreparedMEHandleContents(Object2LongMap<FluidIngredient> preparedMEHandleContents) {
         this.preparedMEHandleContents = preparedMEHandleContents;
      }
   }

   public class MEItemInputHandler extends NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> {
      private final IO io;
      private Object2LongMap<Ingredient> preparedMEHandleContents = new Object2LongOpenHashMap();
      private int preparedCircuitConfig = -1;

      public MEItemInputHandler(MEPatternBufferPartMachine machine, IO io) {
         super(machine);
         this.io = io;
      }

      public MEPatternBufferPartMachine getMachine() {
         return (MEPatternBufferPartMachine)this.machine;
      }

      @Override
      public RecipeCapability<Ingredient> getCapability() {
         return ItemRecipeCapability.CAP;
      }

      @Override
      public Set<Integer> getActiveSlots() {
         return MEPatternBufferRecipeHandlerTrait.this.getActiveSlots(this.getMachine().getInternalInventory());
      }

      @Override
      public Int2ObjectMap<List<ItemStack>> getActiveAndUnCachedSlotsLimitContentsMap() {
         Int2ObjectArrayMap<List<ItemStack>> map = new Int2ObjectArrayMap();
         MEPatternBufferPartMachine machine = this.getMachine();
         List<ItemStack> shared = machine.getSharedCatalystInventory().getContents();

         for (int slot : MEPatternBufferRecipeHandlerTrait.this.getActiveAndUnCachedSlots(this.getMachine().getInternalInventory())) {
            ObjectList<ItemStack> inputs = machine.getInternalInventory()[slot].getLimitItemStackInput();
            ItemStack circuitForRecipe = machine.getCircuitForRecipe(slot);
            if (!circuitForRecipe.m_41619_()) {
               inputs.add(circuitForRecipe);
            }

            inputs.addAll(shared);
            map.put(slot, inputs);
         }

         return map;
      }

      @Override
      public Object2LongMap<ItemStack> getStackMapFromFirstAvailableSlot(IntCollection slots) {
         MEPatternBufferPartMachine.InternalSlot[] inventory = this.getMachine().getInternalInventory();
         IntIterator var3 = slots.iterator();

         while (var3.hasNext()) {
            int slot = (Integer)var3.next();
            if (inventory[slot].isActive()) {
               Object2LongOpenHashMap<ItemStack> map = new Object2LongOpenHashMap();
               ObjectIterator var6 = Object2LongMaps.fastIterable(inventory[slot].getItemStackInputMap()).iterator();

               while (var6.hasNext()) {
                  Entry<ItemStack> entry = (Entry<ItemStack>)var6.next();
                  map.addTo((ItemStack)entry.getKey(), entry.getLongValue());
               }

               return map;
            }
         }

         return Object2LongMaps.emptyMap();
      }

      @Override
      public boolean meHandleRecipeInner(GTRecipe recipe, Object2LongMap<Ingredient> left, boolean simulate, int trySlot) {
         return MEPatternBufferRecipeHandlerTrait.this.handleItemInner(recipe, left, this.preparedCircuitConfig, simulate, trySlot);
      }

      @Override
      public void prepareMEHandleContents(GTRecipe recipe, List<Ingredient> left, boolean simulate) {
         this.preparedCircuitConfig = -1;
         if (simulate) {
            this.getMachine().getSharedCircuitInventory().handleRecipeInner(IO.IN, recipe, left, null, true);
            this.getMachine().getSharedCatalystInventory().handleRecipeInner(IO.IN, recipe, left, null, true);
            this.setPreparedMEHandleContents(AEUtils.ingredientsMapWithOutCircuit(left, this::setPreparedCircuitConfig));
         } else {
            this.setPreparedMEHandleContents(AEUtils.ingredientsMap(left));
         }
      }

      @Override
      public List<Ingredient> meHandleRecipeOutputInner(List<Ingredient> left, boolean simulate) {
         if (simulate) {
            return List.of();
         }

         Object2LongOpenHashMap<AEKey> buffer = this.getMachine().buffer;

         for (Ingredient ingredient : left) {
            if (ingredient instanceof IntProviderIngredient intProvider) {
               intProvider.setItemStacks(null);
               intProvider.setSampledCount(null);
            }

            ItemStack[] items = ingredient.m_43908_();
            if (items.length != 0) {
               ItemStack output = items[0];
               if (!output.m_41619_()) {
                  buffer.addTo(AEItemKey.of(output), ingredient instanceof LongIngredient longIngredient ? longIngredient.getActualAmount() : output.m_41613_());
               }
            }
         }

         return List.of();
      }

      @Override
      public IO getIo() {
         return this.io;
      }

      @Override
      public Object2LongMap<Ingredient> getPreparedMEHandleContents() {
         return this.preparedMEHandleContents;
      }

      public void setPreparedMEHandleContents(Object2LongMap<Ingredient> preparedMEHandleContents) {
         this.preparedMEHandleContents = preparedMEHandleContents;
      }

      public void setPreparedCircuitConfig(int preparedCircuitConfig) {
         this.preparedCircuitConfig = preparedCircuitConfig;
      }
   }
}
