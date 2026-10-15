package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.crafting.IPatternDetails;
import appeng.api.implementations.blockentities.PatternContainerGroup;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener.State;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.core.definitions.AEItems;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.crafting.pattern.ProcessingPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton.Toggle;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.ButtonConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.CircuitFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyTankConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.gregtechceu.gtceu.utils.FluidStackHashStrategy;
import com.gregtechceu.gtceu.utils.ItemStackHashStrategy;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystFluidStackHandler;
import com.hepdd.gtmthings.common.block.machine.trait.CatalystItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.misc.FluidTransferList;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.LazyManaged;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import org.gtlcore.gtlcore.api.gui.MEPatternCatalystUIManager;
import org.gtlcore.gtlcore.api.machine.trait.IMERecipeHandlerTrait;
import org.gtlcore.gtlcore.api.machine.trait.NotifiableCircuitItemStackHandler;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternTrait;
import org.gtlcore.gtlcore.api.pattern.AdvancedBlockPattern;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.integration.ae2.handler.MEBufferPatternHelper;
import org.gtlcore.gtlcore.integration.ae2.handler.SlotCacheManager;
import org.gtlcore.gtlcore.integration.ae2.widget.AEPatternViewExtendSlotWidget;
import org.gtlcore.gtlcore.utils.GTLUtil;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Triplet;

public class MEPatternBufferPartMachine extends MEIOPartMachine implements IInteractedMachine, ICraftingProvider, PatternContainer, IMEPatternPartMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MEPatternBufferPartMachine.class, MEIOPartMachine.MANAGED_FIELD_HOLDER
   );
   private final InternalInventory internalPatternInventory = new InternalInventory() {
      public int size() {
         return MEPatternBufferPartMachine.this.maxPatternCount;
      }

      public ItemStack getStackInSlot(int slotIndex) {
         return MEPatternBufferPartMachine.this.patternInventory.getStackInSlot(slotIndex);
      }

      public void setItemDirect(int slotIndex, ItemStack stack) {
         MEPatternBufferPartMachine.this.patternInventory.setStackInSlot(slotIndex, stack);
         MEPatternBufferPartMachine.this.patternInventory.onContentsChanged(slotIndex);
         MEPatternBufferPartMachine.this.onPatternChange(slotIndex);
      }

      public boolean isItemValid(int slot, ItemStack stack) {
         return slot <= MEPatternBufferPartMachine.this.maxPatternCount && AEUtils.PROCESS_FILTER.apply(stack);
      }
   };
   @DescSynced
   @Persisted
   protected String customName = "";
   @Persisted
   protected boolean keepByProduct = false;
   protected final int maxPatternCount;
   private final boolean[] hasPatternArray;
   @DescSynced
   protected final boolean[] cacheRecipe;
   private boolean needPatternSync;
   @Persisted
   private final ItemStackTransfer patternInventory;
   @Persisted(key = "shareInventory")
   protected final CatalystItemStackHandler sharedCatalystInventory;
   @Persisted(key = "shareTank")
   protected final CatalystFluidStackHandler sharedCatalystTank;
   @Persisted(key = "mePatternCircuitInventory")
   protected final NotifiableItemStackHandler sharedCircuitInventory;
   @Persisted
   protected final ItemStackTransfer[] catalystItems;
   @Persisted
   @LazyManaged
   protected final FluidTransferList[] catalystFluids;
   @Persisted
   protected final MEPatternBufferPartMachine.InternalSlot[] internalInventory;
   protected final Object2LongOpenHashMap<AEKey> buffer;
   protected final MEBufferPatternHelper realPatternHelper;
   protected final MEPatternBufferRecipeHandlerTrait recipeHandler;
   protected final Int2ReferenceMap<ObjectSet<@NotNull GTRecipe>> recipeMultipleCacheMap;
   protected final byte[] cacheRecipeCount;
   private final BiMap<@NotNull IPatternDetails, Integer> patternSlotMap;
   private final Int2ObjectMap<IPatternDetails> slot2PatternMap;
   protected IntConsumer removeSlotFromMap = i -> {};
   @Persisted
   private final ObjectOpenHashSet<BlockPos> proxies;
   private final Set<MEPatternBufferProxyPartMachine> proxyMachines;
   @Nullable
   protected TickableSubscription updateSubs;

   public MEPatternBufferPartMachine(IMachineBlockEntity holder, int maxPatternCount, IO io) {
      super(holder, io);
      this.maxPatternCount = maxPatternCount;
      this.hasPatternArray = new boolean[maxPatternCount];
      this.cacheRecipe = new boolean[maxPatternCount];
      this.internalInventory = new MEPatternBufferPartMachine.InternalSlot[maxPatternCount];
      this.catalystItems = new ItemStackTransfer[maxPatternCount];
      this.catalystFluids = new FluidTransferList[maxPatternCount];
      this.cacheRecipeCount = new byte[maxPatternCount];
      this.patternSlotMap = HashBiMap.create();
      this.slot2PatternMap = new Int2ObjectOpenHashMap();
      this.recipeMultipleCacheMap = new Int2ReferenceOpenHashMap();
      this.proxies = new ObjectOpenHashSet();
      this.proxyMachines = new ReferenceOpenHashSet();
      this.buffer = new Object2LongOpenHashMap();
      this.patternInventory = new ItemStackTransfer(maxPatternCount);
      this.patternInventory.setFilter(AEUtils.PROCESS_FILTER);
      Arrays.setAll(this.internalInventory, x$0 -> new MEPatternBufferPartMachine.InternalSlot(x$0));
      Arrays.setAll(this.catalystItems, i -> {
         ItemStackTransfer transfer = new ItemStackTransfer(9);
         transfer.setFilter(stack -> !(stack.m_41720_() instanceof ProcessingPatternItem));
         return transfer;
      });
      Arrays.setAll(
         this.catalystFluids,
         i -> new FluidTransferList(Stream.<IFluidTransfer>generate(() -> new FluidStorage(16L * FluidHelper.getBucket())).limit(9L).toList())
      );
      Arrays.fill(this.cacheRecipeCount, (byte)1);
      this.sharedCircuitInventory = new NotifiableCircuitItemStackHandler(this);
      this.realPatternHelper = new MEBufferPatternHelper((NotifiableCircuitItemStackHandler)this.sharedCircuitInventory);
      this.sharedCatalystInventory = new CatalystItemStackHandler(this, 9, IO.IN, IO.NONE);
      this.sharedCatalystTank = new CatalystFluidStackHandler(this, 9, 16L * FluidHelper.getBucket(), IO.IN, IO.NONE);
      this.recipeHandler = new MEPatternBufferRecipeHandlerTrait(this, io);
      this.getMainNode().addService(ICraftingProvider.class, this);
      if (io == IO.BOTH) {
         this.getMainNode().addService(IGridTickable.class, new MEPatternBufferPartMachine.Ticker());
      }
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().execute(() -> {
            for (int i = 0; i < this.patternInventory.getSlots(); i++) {
               ItemStack pattern = this.patternInventory.getStackInSlot(i);
               IPatternDetails realPattern = this.getRealPattern(i, pattern);
               if (realPattern != null) {
                  this.slot2PatternMap.put(i, realPattern);
                  this.hasPatternArray[i] = true;
               }
            }

            this.reCalculatePatternSlotMap();
            this.needPatternSync = true;
         });

         for (int i = 0; i < this.maxPatternCount; i++) {
            int index = i;
            this.catalystItems[index].setOnContentsChanged(() -> this.reCalculateCatalystItemMap(index));

            for (IFluidTransfer transfer : this.catalystFluids[index].transfers) {
               if (transfer instanceof FluidStorage storage) {
                  storage.setOnContentsChanged(() -> this.reCalculateCatalystFluidMap(index));
               }
            }
         }
      }
   }

   @Override
   public void onMainNodeStateChanged(@NotNull State reason) {
      super.onMainNodeStateChanged(reason);
      this.updateSubscription();
   }

   protected void updateSubscription() {
      if (this.getMainNode().isOnline()) {
         this.updateSubs = this.subscribeServerTick(this.updateSubs, this::update);
      } else if (this.updateSubs != null) {
         this.updateSubs.unsubscribe();
         this.updateSubs = null;
      }
   }

   protected void update() {
      if (this.needPatternSync) {
         ICraftingProvider.requestUpdate(this.getMainNode());
         this.needPatternSync = false;
      }
   }

   protected void onPatternChange(int index) {
      if (!this.isRemote()) {
         MEPatternBufferPartMachine.InternalSlot internalInv = this.internalInventory[index];
         ItemStack newPattern = this.patternInventory.getStackInSlot(index);
         IPatternDetails newPatternDetailsWithOutCircuit = this.getRealPattern(index, newPattern);
         IPatternDetails oldPatternDetails = (IPatternDetails)this.slot2PatternMap.get(index);
         if (newPatternDetailsWithOutCircuit != null) {
            this.slot2PatternMap.put(index, newPatternDetailsWithOutCircuit);
            this.hasPatternArray[index] = true;
         } else {
            this.slot2PatternMap.remove(index);
            this.hasPatternArray[index] = false;
         }

         if (oldPatternDetails != null && !oldPatternDetails.equals(newPatternDetailsWithOutCircuit)) {
            internalInv.cacheManager.clearAllCaches();
            this.removeSlotFromGTRecipeCache(index);
            this.refundSlot(internalInv);
            AEUtils.reFunds(this.buffer, this.getMainNode().getGrid(), this.actionSource);
         }

         this.reCalculatePatternSlotMap();
         this.needPatternSync = true;
      }
   }

   public void onMachineRemoved() {
      this.clearInventory(this.patternInventory);
      this.clearInventory(this.sharedCatalystInventory);

      for (ItemStackTransfer catalystItem : this.catalystItems) {
         this.clearInventory(catalystItem);
      }

      for (MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
         proxy.setBuffer(null);
      }
   }

   private void reCalculateCatalystItemMap(int slot) {
      Object2LongMap<AEItemKey> itemCatalystInventory = this.internalInventory[slot].itemCatalystInventory;
      itemCatalystInventory.clear();
      ItemStackTransfer catalystItem = this.catalystItems[slot];

      for (int i = 0; i < catalystItem.getSlots(); i++) {
         ItemStack stack = catalystItem.getStackInSlot(i);
         if (!stack.m_41619_()) {
            itemCatalystInventory.mergeLong(AEItemKey.of(stack), stack.m_41613_(), Long::sum);
         }
      }

      this.internalInventory[slot].onContentsChanged.run();
   }

   private void reCalculateCatalystFluidMap(int slot) {
      Object2LongMap<AEFluidKey> fluidCatalystInventory = this.internalInventory[slot].fluidCatalystInventory;
      fluidCatalystInventory.clear();
      FluidTransferList catalystFluid = this.catalystFluids[slot];

      for (int i = 0; i < catalystFluid.getTanks(); i++) {
         FluidStack stack = catalystFluid.getFluidInTank(i);
         if (!stack.isEmpty()) {
            fluidCatalystInventory.mergeLong(AEFluidKey.of(stack.getFluid()), stack.getAmount(), Long::sum);
         }
      }

      this.internalInventory[slot].onContentsChanged.run();
   }

   protected void reCalculatePatternSlotMap() {
      this.patternSlotMap.clear();
      ObjectIterator var1 = Int2ObjectMaps.fastIterable(this.slot2PatternMap).iterator();

      while (var1.hasNext()) {
         Entry<IPatternDetails> entry = (Entry<IPatternDetails>)var1.next();
         int slot = entry.getIntKey();
         IPatternDetails pattern = (IPatternDetails)entry.getValue();
         if (pattern != null) {
            if (this.cacheRecipe[slot]) {
               this.patternSlotMap.forcePut(pattern, slot);
            } else {
               this.patternSlotMap.putIfAbsent(pattern, slot);
            }
         }
      }
   }

   protected void removeSlotFromGTRecipeCache(int slot) {
      this.cacheRecipe[slot] = false;
      this.recipeMultipleCacheMap.remove(slot);
      this.removeSlotFromMap.accept(slot);

      for (MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
         proxy.removeSlotFromMap.accept(slot);
      }
   }

   protected void refreshAllByProduct() {
      this.slot2PatternMap.clear();

      for (int i = 0; i < this.patternInventory.getSlots(); i++) {
         ItemStack pattern = this.patternInventory.getStackInSlot(i);
         IPatternDetails realPattern = this.getRealPattern(i, pattern);
         if (realPattern != null) {
            this.slot2PatternMap.put(i, realPattern);
         }
      }

      this.reCalculatePatternSlotMap();
      this.needPatternSync = true;
   }

   public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
      super.saveCustomPersistedData(tag, forDrop);
      if (!this.recipeMultipleCacheMap.isEmpty()) {
         CompoundTag recipeCacheTag = new CompoundTag();
         ObjectIterator var4 = Int2ReferenceMaps.fastIterable(this.recipeMultipleCacheMap).iterator();

         while (var4.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<ObjectSet<GTRecipe>> entry = (it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<ObjectSet<GTRecipe>>)var4.next();
            ObjectSet<GTRecipe> recipeSet = (ObjectSet<GTRecipe>)entry.getValue();
            if (!recipeSet.isEmpty()) {
               ListTag list = new ListTag();
               ObjectIterator var8 = recipeSet.iterator();

               while (var8.hasNext()) {
                  GTRecipe recipe = (GTRecipe)var8.next();
                  list.add(GTLUtil.serializeNBT(recipe));
               }

               recipeCacheTag.m_128365_(Integer.toString(entry.getIntKey()), list);
            }
         }

         tag.m_128365_("recipeMultipleCacheIdMap", recipeCacheTag);
      }

      tag.m_128382_("cacheRecipeCount", this.cacheRecipeCount);
      ListTag bufferTag = AEUtils.createListTag(AEKey::toTagGeneric, this.buffer);
      if (!bufferTag.isEmpty()) {
         tag.m_128365_("buffer", bufferTag);
      }
   }

   @Override
   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      byte[] byteArray = tag.m_128463_("cacheRecipeCount");
      System.arraycopy(byteArray, 0, this.cacheRecipeCount, 0, byteArray.length);
      this.recipeMultipleCacheMap.clear();
      RecipeManager recipeManager = Registries.getRecipeManager();
      if (tag.m_128441_("recipeMultipleCacheIdMap")) {
         CompoundTag recipeCacheTag = tag.m_128469_("recipeMultipleCacheIdMap");

         for (String key : recipeCacheTag.m_128431_()) {
            int slotIndex = Integer.parseInt(key);

            for (Tag recipeTag : recipeCacheTag.m_128437_(key, 10)) {
               GTRecipe recipe = GTLUtil.deserializeNBT(recipeTag);
               if (recipe != null && slotIndex >= 0 && slotIndex < this.maxPatternCount) {
                  GTRecipe real = recipe.recipeType.getRecipe(recipeManager, recipe.id);
                  if (real != null) {
                     ObjectSet<GTRecipe> set = (ObjectSet<GTRecipe>)this.recipeMultipleCacheMap.computeIfAbsent(slotIndex, integer -> new ObjectArraySet());
                     set.add(real);
                     if (set.size() >= this.cacheRecipeCount[slotIndex]) {
                        this.cacheRecipe[slotIndex] = true;
                     }
                  }
               }
            }
         }
      } else if (tag.m_128441_("gtRecipeCache")) {
         CompoundTag oldRecipeCacheTag = tag.m_128469_("gtRecipeCache");

         for (String key : oldRecipeCacheTag.m_128431_()) {
            int slotIndex = Integer.parseInt(key);
            Tag recipeTag = oldRecipeCacheTag.m_128423_(key);
            GTRecipe recipe = GTLUtil.deserializeNBT(recipeTag);
            if (recipe != null && slotIndex >= 0 && slotIndex < this.maxPatternCount) {
               GTRecipe real = recipe.recipeType.getRecipe(recipeManager, recipe.id);
               if (real != null) {
                  ObjectSet<GTRecipe> set = (ObjectSet<GTRecipe>)this.recipeMultipleCacheMap.computeIfAbsent(slotIndex, integer -> new ObjectArraySet());
                  set.add(real);
                  if (set.size() >= this.cacheRecipeCount[slotIndex]) {
                     this.cacheRecipe[slotIndex] = true;
                  }
               }
            }
         }
      }

      ListTag bufferTag = tag.m_128437_("buffer", 10);
      AEUtils.loadInventory(bufferTag, AEKey::fromTagGeneric, this.buffer);
   }

   public void copyFromTag(CompoundTag tag, ServerPlayer serverPlayer) {
      this.setCustomName(tag.m_128461_("name"));
      ListTag list = tag.m_128437_("patterns", 10);
      int listIndex = 0;

      for (int index = 0; index < this.internalPatternInventory.size() && listIndex < list.size(); index++) {
         if (this.internalPatternInventory.getStackInSlot(index).m_41619_()) {
            Triplet<ItemStack, IItemHandler, Integer> result = AdvancedBlockPattern.foundItem(
               serverPlayer, List.of(AEItems.BLANK_PATTERN.stack()), AEItems.BLANK_PATTERN.stack()::m_150930_
            );
            if (result.getA() == null) {
               break;
            }

            CompoundTag patternData = list.m_128728_(listIndex);
            CompoundTag patternTag = patternData.m_128469_("pattern");
            byte sourceCacheCount = patternData.m_128445_("cacheCount");
            if (sourceCacheCount <= 0) {
               break;
            }

            this.internalPatternInventory.setItemDirect(index, ItemStack.m_41712_(patternTag));
            this.cacheRecipeCount[index] = sourceCacheCount;
            IItemHandler handler = (IItemHandler)result.getB();
            if (handler != null) {
               handler.extractItem((Integer)result.getC(), 1, false);
            }

            listIndex++;
         }
      }
   }

   public CompoundTag copyToTag(CompoundTag tags) {
      CompoundTag tag = new CompoundTag();
      tag.m_128359_("name", this.customName);
      ListTag listPattern = new ListTag();

      for (int slotIndex : this.patternSlotMap.values()) {
         ItemStack stack = this.internalPatternInventory.getStackInSlot(slotIndex);
         if (!stack.m_41619_()) {
            CompoundTag patternData = new CompoundTag();
            patternData.m_128365_("pattern", stack.serializeNBT());
            patternData.m_128344_("cacheCount", this.cacheRecipeCount[slotIndex]);
            listPattern.add(patternData);
         }
      }

      tag.m_128365_("patterns", listPattern);
      tags.m_128365_("tag", tag);
      return tags;
   }

   public boolean pasteFromTag(CompoundTag tag) {
      this.setCustomName(tag.m_128461_("name"));
      this.keepByProduct = tag.m_128471_("keepByProduct");
      ListTag patternList = tag.m_128437_("patterns", 10);
      int usedCount = 0;

      for (ItemStack ignored : this.internalPatternInventory) {
         usedCount++;
      }

      if (this.internalPatternInventory.size() - usedCount < patternList.size()) {
         return false;
      }

      int listIndex = 0;

      for (int slotIndex = 0; slotIndex < this.internalPatternInventory.size() && listIndex < patternList.size(); slotIndex++) {
         if (this.internalPatternInventory.getStackInSlot(slotIndex).m_41619_()) {
            CompoundTag patternData = patternList.m_128728_(listIndex);
            CompoundTag patternTag = patternData.m_128469_("pattern");
            byte sourceCacheCount = patternData.m_128445_("cacheCount");
            CompoundTag catalystItems = patternData.m_128469_("catalystItems");
            CompoundTag catalystFluids = patternData.m_128469_("catalystFluids");
            this.internalPatternInventory.setItemDirect(slotIndex, ItemStack.m_41712_(patternTag));
            this.catalystItems[slotIndex].deserializeNBT(catalystItems);
            this.catalystFluids[slotIndex].deserializeNBT(catalystFluids);
            if (sourceCacheCount > 0) {
               this.cacheRecipeCount[slotIndex] = sourceCacheCount;
            }

            this.catalystItems[slotIndex].onContentsChanged();
            this.catalystFluids[slotIndex].onContentsChanged();
            listIndex++;
         }
      }

      this.sharedCatalystInventory.storage.deserializeNBT(tag.m_128469_("sharedCatalystInventory"));
      this.sharedCircuitInventory.storage.deserializeNBT(tag.m_128469_("sharedCircuitInventory"));
      ListTag catalystTanks = tag.m_128437_("sharedCatalystTank", 10);
      FluidStorage[] tankStorages = this.sharedCatalystTank.getStorages();

      for (int i = 0; i < Math.min(catalystTanks.size(), tankStorages.length); i++) {
         tankStorages[i].deserializeNBT(catalystTanks.m_128728_(i));
      }

      this.sharedCatalystTank.onContentsChanged();
      this.sharedCatalystInventory.onContentsChanged();

      for (MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
         proxy.setBuffer(null);
      }

      this.proxyMachines.clear();
      this.proxies.clear();

      for (long l : tag.m_128467_("proxies")) {
         BlockPos pos = BlockPos.m_122022_(l);
         if (MetaMachine.getMachine(Objects.requireNonNull(this.getLevel()), pos) instanceof MEPatternBufferProxyPartMachine proxy) {
            proxy.setBuffer(this.getPos());
         }
      }

      this.refreshAllByProduct();
      return true;
   }

   public CompoundTag cutToTag(CompoundTag tags) {
      CompoundTag tag = new CompoundTag();
      tag.m_128359_("name", this.customName);
      ListTag listPattern = new ListTag();

      for (int slotIndex : this.patternSlotMap.values().stream().toList()) {
         ItemStack stack = this.internalPatternInventory.getStackInSlot(slotIndex);
         if (!stack.m_41619_()) {
            CompoundTag patternData = new CompoundTag();
            patternData.m_128365_("pattern", stack.serializeNBT());
            patternData.m_128344_("cacheCount", this.cacheRecipeCount[slotIndex]);
            patternData.m_128365_("catalystItems", this.catalystItems[slotIndex].serializeNBT());
            patternData.m_128365_("catalystFluids", this.catalystFluids[slotIndex].serializeNBT());
            listPattern.add(patternData);
            this.internalPatternInventory.setItemDirect(slotIndex, ItemStack.f_41583_);

            for (int i = 0; i < this.catalystItems[slotIndex].getSlots(); i++) {
               this.catalystItems[slotIndex].setStackInSlot(i, ItemStack.f_41583_);
            }

            for (int i = 0; i < this.catalystFluids[slotIndex].getTanks(); i++) {
               this.catalystFluids[slotIndex].setFluidInTank(i, FluidStack.empty());
            }

            this.catalystItems[slotIndex].onContentsChanged();
            this.catalystFluids[slotIndex].onContentsChanged();
         }
      }

      tag.m_128365_("patterns", listPattern);
      tag.m_128365_("sharedCatalystInventory", this.sharedCatalystInventory.storage.serializeNBT());
      tag.m_128365_("sharedCircuitInventory", this.sharedCircuitInventory.storage.serializeNBT());
      tag.m_128379_("keepByProduct", this.keepByProduct);
      tag.m_128365_("proxies", new LongArrayTag(this.proxies.stream().map(BlockPos::m_121878_).toList()));
      ListTag tankList = new ListTag();
      Arrays.stream(this.sharedCatalystTank.getStorages()).map(FluidStorage::serializeNBT).forEach(tankList::add);
      tag.m_128365_("sharedCatalystTank", tankList);

      for (int i = 0; i < this.sharedCatalystInventory.getSlots(); i++) {
         this.sharedCatalystInventory.setStackInSlot(i, ItemStack.f_41583_);
      }

      for (int i = 0; i < this.sharedCatalystTank.getTanks(); i++) {
         this.sharedCatalystTank.setFluidInTank(i, FluidStack.empty());
      }

      this.sharedCatalystInventory.onContentsChanged();
      this.sharedCatalystTank.onContentsChanged();

      for (MEPatternBufferProxyPartMachine proxy : this.getProxies()) {
         proxy.setBuffer(null);
      }

      this.proxyMachines.clear();
      this.proxies.clear();
      tags.m_128365_("cut", tag);
      return tags;
   }

   @NotNull
   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void addProxy(MEPatternBufferProxyPartMachine proxy) {
      this.proxies.add(proxy.getPos());
      this.proxyMachines.add(proxy);
   }

   public void removeProxy(MEPatternBufferProxyPartMachine proxy) {
      this.proxies.remove(proxy.getPos());
      this.proxyMachines.remove(proxy);
   }

   public Set<MEPatternBufferProxyPartMachine> getProxies() {
      if (this.proxyMachines.size() != this.proxies.size()) {
         this.proxyMachines.clear();
         ObjectIterator<BlockPos> it = this.proxies.iterator();

         while (it.hasNext()) {
            BlockPos pos = (BlockPos)it.next();
            if (MetaMachine.getMachine(Objects.requireNonNull(this.getLevel()), pos) instanceof MEPatternBufferProxyPartMachine proxy) {
               this.proxyMachines.add(proxy);
            } else {
               it.remove();
            }
         }
      }

      return Collections.unmodifiableSet(this.proxyMachines);
   }

   private void refundAll(ClickData clickData) {
      if (!clickData.isRemote) {
         Arrays.stream(this.internalInventory).filter(MEPatternBufferPartMachine.InternalSlot::isActive).forEach(this::refundSlot);
         AEUtils.reFunds(this.buffer, this.getMainNode().getGrid(), this.actionSource);
      }
   }

   public void refundSlot(MEPatternBufferPartMachine.InternalSlot slot) {
      ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>> it = slot.itemInventory.object2LongEntrySet().fastIterator();

      while (it.hasNext()) {
         it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>)it.next();
         long amount = entry.getLongValue();
         if (amount > 0L) {
            this.buffer.addTo((AEKey)entry.getKey(), amount);
            it.remove();
         }
      }

      it = slot.fluidInventory.object2LongEntrySet().fastIterator();

      while (it.hasNext()) {
         it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey>)it.next();
         long amount = entry.getLongValue();
         if (amount > 0L) {
            this.buffer.addTo((AEKey)entry.getKey(), amount);
            it.remove();
         }
      }
   }

   public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack stack = player.m_21120_(hand);
      if (stack.m_41619_()) {
         return InteractionResult.PASS;
      }

      if (stack.m_150930_(GTItems.TOOL_DATA_STICK.m_5456_())) {
         if (!world.f_46443_) {
            Pair<GTRecipeType, String> researchData = ResearchManager.readResearchId(stack);
            if (researchData != null) {
               return InteractionResult.PASS;
            }

            stack.m_41784_().m_128385_("pos", new int[]{pos.m_123341_(), pos.m_123342_(), pos.m_123343_()});
            player.m_213846_(Component.m_237115_("gtceu.machine.me.import_copy_settings"));
         }

         return InteractionResult.m_19078_(world.f_46443_);
      } else {
         return InteractionResult.PASS;
      }
   }

   public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new ButtonConfigurator(new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, GuiTextures.REFUND_OVERLAY}), this::refundAll)
               .setTooltips(List.of(Component.m_237115_("gui.gtceu.refund_all.desc")))
         }
      );
      configuratorPanel.attachConfigurators(new IFancyConfigurator[]{new CircuitFancyConfigurator(this.sharedCircuitInventory.storage)});
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new FancyInvConfigurator(this.sharedCatalystInventory.storage, Component.m_237115_("gui.gtceu.share_inventory.title"))
               .setTooltips(List.of(Component.m_237115_("gui.gtceu.share_inventory.desc.0"), Component.m_237115_("gui.gtceu.share_inventory.desc.1")))
         }
      );
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new FancyTankConfigurator(this.sharedCatalystTank.getStorages(), Component.m_237115_("gui.gtceu.share_tank.title"))
               .setTooltips(List.of(Component.m_237115_("gui.gtceu.share_tank.desc.0"), Component.m_237115_("gui.gtceu.share_inventory.desc.1")))
         }
      );
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new Toggle(
                  GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture(0.0, 0.0, 1.0, 0.5),
                  GuiTextures.BUTTON_SILK_TOUCH_MODE.getSubTexture(0.0, 0.5, 1.0, 0.5),
                  () -> !this.keepByProduct,
                  (clickData, pressed) -> {
                     this.keepByProduct = !pressed;
                     this.refreshAllByProduct();
                  }
               )
               .setTooltipsSupplier(
                  pressed -> List.of(
                     Component.m_237115_("tooltip.gtlcore.disable_by_product")
                        .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.YELLOW))
                        .m_7220_(Component.m_237115_(pressed ? "gtceu.multiblock.universal.distinct.yes" : "gtceu.multiblock.universal.distinct.no"))
                  )
               )
         }
      );
   }

   @NotNull
   public Widget createUIWidget() {
      int rowSize = 9;
      int colSize = this.maxPatternCount / rowSize;
      WidgetGroup group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * colSize + 16);
      group.addWidget(new LabelWidget(8, 2, () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
      group.addWidget(
         new AETextInputButtonWidget(18 * rowSize + 8 - 70, 2, 70, 10)
            .setText(this.customName)
            .setOnConfirm(this::setCustomName)
            .setButtonTooltips(new Component[]{Component.m_237115_("gui.gtceu.rename.desc")})
      );
      MEPatternCatalystUIManager catalystUIManager = new MEPatternCatalystUIManager(
         group.getSizeWidth() + 4, this.catalystItems, this.catalystFluids, this.cacheRecipeCount, this::removeSlotFromGTRecipeCache
      );
      group.waitToAdded(catalystUIManager);
      int index = 0;

      for (int y = 0; y < colSize; y++) {
         for (int x = 0; x < rowSize; x++) {
            int finalI = index;
            Widget slot = new AEPatternViewExtendSlotWidget(this.patternInventory, index++, x * 18 + 8, y * 18 + 14)
               .setOnMiddleClick(() -> catalystUIManager.toggleFor(finalI))
               .setOnPatternSlotChanged(() -> this.onPatternChange(finalI))
               .setOccupiedTexture(new IGuiTexture[]{GuiTextures.SLOT})
               .setItemHook(stack -> {
                  if (!stack.m_41619_() && stack.m_41720_() instanceof EncodedPatternItem iep) {
                     ItemStack out = iep.getOutput(stack);
                     if (!out.m_41619_()) {
                        return out;
                     }
                  }

                  return stack;
               })
               .setOnAddedTooltips((s, l) -> {
                  if (this.cacheRecipe[finalI]) {
                     l.add(Component.m_237115_("gtceu.machine.pattern.recipe.cache"));
                  }
               })
               .setBackground(new IGuiTexture[]{GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY});
            group.addWidget(slot);
         }
      }

      return group;
   }

   private IPatternDetails getRealPattern(int slot, ItemStack stack) {
      if (!stack.m_41619_()) {
         MEPatternBufferPartMachine.InternalSlot internalSlot = this.internalInventory[slot];
         return this.realPatternHelper.processPatternWithCircuit(stack, internalSlot.cacheManager::setCircuitCache, this.getLevel(), this.keepByProduct);
      } else {
         return null;
      }
   }

   public ItemStack getCircuitForRecipe(int slotIndex) {
      return this.realPatternHelper.getCircuitForRecipe(this.internalInventory[slotIndex].cacheManager.getCircuitStack());
   }

   public List<IPatternDetails> getAvailablePatterns() {
      return this.patternSlotMap.keySet().stream().toList();
   }

   public boolean pushPattern(IPatternDetails patternDetails, KeyCounter[] inputHolder) {
      if (this.getMainNode().isActive() && this.patternSlotMap.containsKey(patternDetails)) {
         Integer slotIndex = (Integer)this.patternSlotMap.get(patternDetails);
         if (slotIndex != null && slotIndex >= 0) {
            this.internalInventory[slotIndex].pushPattern(inputHolder);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isBusy() {
      return false;
   }

   @Nullable
   public IGrid getGrid() {
      return this.getMainNode().getGrid();
   }

   public InternalInventory getTerminalPatternInventory() {
      return this.internalPatternInventory;
   }

   public PatternContainerGroup getTerminalGroup() {
      List<IMultiController> controllers = this.getControllers();
      if (!controllers.isEmpty()) {
         IMultiController controller = controllers.get(0);
         MultiblockMachineDefinition controllerDefinition = controller.self().getDefinition();
         if (!this.customName.isEmpty()) {
            return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.m_237113_(this.customName), Collections.emptyList());
         }

         ItemStack circuitStack = this.sharedCircuitInventory.storage.getStackInSlot(0);
         int circuitConfiguration = circuitStack.m_41619_() ? -1 : IntCircuitBehaviour.getCircuitConfiguration(circuitStack);
         Component groupName = circuitConfiguration != -1
            ? Component.m_237115_(controllerDefinition.getDescriptionId()).m_130946_(" - " + circuitConfiguration)
            : Component.m_237115_(controllerDefinition.getDescriptionId());
         return new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), groupName, Collections.emptyList());
      } else {
         return !this.customName.isEmpty()
            ? new PatternContainerGroup(
               AEItemKey.of(GTLMachines.GTAEMachines.ME_EXTEND_PATTERN_BUFFER.getItem()), Component.m_237113_(this.customName), Collections.emptyList()
            )
            : new PatternContainerGroup(
               AEItemKey.of(GTLMachines.GTAEMachines.ME_EXTEND_PATTERN_BUFFER.getItem()),
               GTLMachines.GTAEMachines.ME_EXTEND_PATTERN_BUFFER.get().getDefinition().getItem().m_41466_(),
               Collections.emptyList()
            );
      }
   }

   @NotNull
   protected IMEPatternTrait createMETrait() {
      return new MEPatternBufferPartMachine.MEPatternTrait(this);
   }

   @NotNull
   @Override
   public IMEPatternTrait getMETrait() {
      return (IMEPatternTrait)this.meTrait;
   }

   @Override
   public it.unimi.dsi.fastutil.Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> getMERecipeHandlerTraits() {
      return it.unimi.dsi.fastutil.Pair.of(this.recipeHandler.meItemHandler, this.recipeHandler.meFluidHandler);
   }

   public void setCustomName(String customName) {
      this.customName = customName;
   }

   public String getCustomName() {
      return this.customName;
   }

   public ItemStackTransfer getPatternInventory() {
      return this.patternInventory;
   }

   public CatalystItemStackHandler getSharedCatalystInventory() {
      return this.sharedCatalystInventory;
   }

   public CatalystFluidStackHandler getSharedCatalystTank() {
      return this.sharedCatalystTank;
   }

   public NotifiableItemStackHandler getSharedCircuitInventory() {
      return this.sharedCircuitInventory;
   }

   public MEPatternBufferPartMachine.InternalSlot[] getInternalInventory() {
      return this.internalInventory;
   }

   public Object2LongOpenHashMap<AEKey> getBuffer() {
      return this.buffer;
   }

   public MEBufferPatternHelper getRealPatternHelper() {
      return this.realPatternHelper;
   }

   public class InternalSlot implements ITagSerializable<CompoundTag>, IContentChangeAware {
      protected Runnable onContentsChanged = () -> {
         MEPatternBufferPartMachine.this.recipeHandler.getMeFluidHandler().notifyListeners();
         MEPatternBufferPartMachine.this.recipeHandler.getMeItemHandler().notifyListeners();
      };
      private final Object2LongOpenHashMap<AEItemKey> itemInventory = new Object2LongOpenHashMap();
      private final Object2LongOpenHashMap<AEFluidKey> fluidInventory = new Object2LongOpenHashMap();
      private final Object2LongMap<AEItemKey> itemCatalystInventory = new Object2LongArrayMap();
      private final Object2LongMap<AEFluidKey> fluidCatalystInventory = new Object2LongArrayMap();
      @Persisted
      private final SlotCacheManager cacheManager = new SlotCacheManager();
      private final int slotIndex;

      public InternalSlot(int slotIndex) {
         this.slotIndex = slotIndex;
         this.itemInventory.defaultReturnValue(0L);
         this.fluidInventory.defaultReturnValue(0L);
         this.itemCatalystInventory.defaultReturnValue(0L);
         this.fluidCatalystInventory.defaultReturnValue(0L);
      }

      public boolean isActive() {
         return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && (!this.itemInventory.isEmpty() || !this.fluidInventory.isEmpty());
      }

      public boolean isItemActive(boolean simulate) {
         return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && simulate
            ? !this.itemInventory.isEmpty()
               || !MEPatternBufferPartMachine.this.sharedCatalystInventory.isEmpty()
               || !MEPatternBufferPartMachine.this.realPatternHelper.getCircuitForRecipe(this.cacheManager.getCircuitStack()).m_41619_()
               || !this.itemCatalystInventory.isEmpty()
            : !this.itemInventory.isEmpty();
      }

      public boolean isFluidActive(boolean simulate) {
         return MEPatternBufferPartMachine.this.hasPatternArray[this.slotIndex] && simulate
            ? !this.fluidInventory.isEmpty() || !MEPatternBufferPartMachine.this.sharedCatalystTank.isEmpty() || !this.fluidCatalystInventory.isEmpty()
            : !this.fluidInventory.isEmpty();
      }

      private void add(AEKey what, long amount) {
         if (amount > 0L) {
            if (what instanceof AEItemKey itemKey) {
               this.itemInventory.addTo(itemKey, amount);
            } else if (what instanceof AEFluidKey fluidKey) {
               this.fluidInventory.addTo(fluidKey, amount);
            }
         }
      }

      public Object2LongMap<ItemStack> getItemStackInputMap() {
         Object2LongOpenCustomHashMap<ItemStack> itemInputMap = new Object2LongOpenCustomHashMap(ItemStackHashStrategy.comparingAllButCount());
         ObjectIterator var2 = Object2LongMaps.fastIterable(this.itemInventory).iterator();

         while (var2.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>)var2.next();
            AEItemKey key = (AEItemKey)entry.getKey();
            long amount = entry.getLongValue();
            if (amount > 0L) {
               ItemStack stack = key.toStack(1);
               itemInputMap.addTo(stack, amount);
            }
         }

         return itemInputMap;
      }

      public Object2LongMap<FluidStack> getFluidStackInputMap() {
         Object2LongOpenCustomHashMap<FluidStack> fluidInputMap = new Object2LongOpenCustomHashMap(FluidStackHashStrategy.comparingAllButAmount());
         ObjectIterator var2 = Object2LongMaps.fastIterable(this.fluidInventory).iterator();

         while (var2.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey>)var2.next();
            AEFluidKey key = (AEFluidKey)entry.getKey();
            long amount = entry.getLongValue();
            if (amount > 0L) {
               FluidStack stack = FluidStack.create(key.getFluid(), 1L);
               fluidInputMap.addTo(stack, amount);
            }
         }

         return fluidInputMap;
      }

      public ObjectList<ItemStack> getLimitItemStackInput() {
         ObjectArrayList<ItemStack> limitInput = new ObjectArrayList(this.itemInventory.size());
         ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>> it = Object2LongMaps.fastIterator(this.itemInventory);

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>)it.next();
            long amount = entry.getLongValue();
            if (amount <= 0L) {
               it.remove();
            } else {
               limitInput.add(((AEItemKey)entry.getKey()).toStack(Ints.saturatedCast(amount)));
            }
         }

         it = Object2LongMaps.fastIterable(this.itemCatalystInventory).iterator();

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEItemKey>)it.next();
            limitInput.add(((AEItemKey)entry.getKey()).toStack(Ints.saturatedCast(entry.getLongValue())));
         }

         return limitInput;
      }

      public ObjectList<FluidStack> getLimitFluidStackInput() {
         ObjectArrayList<FluidStack> limitInput = new ObjectArrayList(this.fluidInventory.size());
         ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey>> it = Object2LongMaps.fastIterator(this.fluidInventory);

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey>)it.next();
            long amount = entry.getLongValue();
            if (amount <= 0L) {
               it.remove();
            } else {
               limitInput.add(FluidStack.create(((AEFluidKey)entry.getKey()).getFluid(), amount));
            }
         }

         it = Object2LongMaps.fastIterable(this.fluidCatalystInventory).iterator();

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<AEFluidKey>)it.next();
            limitInput.add(FluidStack.create(((AEFluidKey)entry.getKey()).getFluid(), entry.getLongValue()));
         }

         return limitInput;
      }

      public void pushPattern(KeyCounter[] inputHolder) {
         AEUtils.pushInputsToMEPatternBufferInventory(inputHolder, this::add);
         this.onContentsChanged.run();
      }

      public boolean testCatalystItemInternal(GTRecipe recipe) {
         for (Content content : recipe.getInputContents(ItemRecipeCapability.CAP)) {
            if (content.chance > 0) {
               Ingredient ingredient = (Ingredient)content.getContent();

               for (ItemStack item : ingredient.m_43908_()) {
                  AEItemKey key = AEItemKey.of(item);
                  if (this.itemCatalystInventory.containsKey(key)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }

      public boolean testCatalystFluidInternal(GTRecipe recipe) {
         for (Content content : recipe.getInputContents(FluidRecipeCapability.CAP)) {
            if (content.chance > 0) {
               FluidIngredient fluidIngredient = (FluidIngredient)content.getContent();

               for (FluidStack stack : fluidIngredient.getStacks()) {
                  AEFluidKey key = AEFluidKey.of(stack.getFluid());
                  if (this.fluidCatalystInventory.containsKey(key)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }

      public boolean handleItemInternal(Object2LongMap<Ingredient> left, int leftCircuit, boolean simulate) {
         if (left.isEmpty() && leftCircuit < 0) {
            return true;
         }

         if (simulate && leftCircuit > 0 && leftCircuit != this.cacheManager.getCircuitCache()) {
            return false;
         }

         ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<Ingredient>> it = Object2LongMaps.fastIterator(left);

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<Ingredient> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<Ingredient>)it.next();
            Ingredient ingredient = (Ingredient)entry.getKey();
            long needAmount = entry.getLongValue();
            if (needAmount <= 0L) {
               it.remove();
            } else {
               AEItemKey bestMatch = simulate
                  ? this.cacheManager.getBestItemMatchSimulate(ingredient, this.itemInventory, this.itemCatalystInventory, needAmount)
                  : this.cacheManager.getBestItemMatch(ingredient, this.itemInventory, needAmount);
               if (bestMatch == null) {
                  return false;
               }
            }
         }

         if (!simulate) {
            it = Object2LongMaps.fastIterator(left);

            while (it.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<Ingredient> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<Ingredient>)it.next();
               Ingredient ingredient = (Ingredient)entry.getKey();
               long needAmount = entry.getLongValue();
               AEItemKey bestMatch = this.cacheManager.getBestItemMatch(ingredient, this.itemInventory, needAmount);
               if (bestMatch != null) {
                  long amount = this.itemInventory.getLong(bestMatch);
                  long except = amount - needAmount;
                  if (except <= 0L) {
                     this.itemInventory.removeLong(bestMatch);
                  } else {
                     this.itemInventory.put(bestMatch, except);
                  }

                  it.remove();
               }
            }
         }

         return true;
      }

      public boolean handleFluidInternal(Object2LongMap<FluidIngredient> left, boolean simulate) {
         if (left.isEmpty()) {
            return true;
         }

         ObjectIterator<it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidIngredient>> it = Object2LongMaps.fastIterator(left);

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidIngredient> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidIngredient>)it.next();
            FluidIngredient ingredient = (FluidIngredient)entry.getKey();
            long needAmount = entry.getLongValue();
            if (needAmount <= 0L) {
               it.remove();
            } else {
               AEFluidKey bestMatch = simulate
                  ? this.cacheManager.getBestFluidMatchSimulate(ingredient, this.fluidInventory, this.fluidCatalystInventory, needAmount)
                  : this.cacheManager.getBestFluidMatch(ingredient, this.fluidInventory, needAmount);
               if (bestMatch == null) {
                  return false;
               }
            }
         }

         if (!simulate) {
            it = Object2LongMaps.fastIterator(left);

            while (it.hasNext()) {
               it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidIngredient> entry = (it.unimi.dsi.fastutil.objects.Object2LongMap.Entry<FluidIngredient>)it.next();
               FluidIngredient ingredient = (FluidIngredient)entry.getKey();
               long needAmount = entry.getLongValue();
               AEFluidKey bestMatch = this.cacheManager.getBestFluidMatch(ingredient, this.fluidInventory, needAmount);
               if (bestMatch != null) {
                  long amount = this.fluidInventory.getLong(bestMatch);
                  long except = amount - needAmount;
                  if (except <= 0L) {
                     this.fluidInventory.removeLong(bestMatch);
                  } else {
                     this.fluidInventory.put(bestMatch, except);
                  }

                  it.remove();
               }
            }
         }

         return true;
      }

      public CompoundTag serializeNBT() {
         CompoundTag tag = new CompoundTag();
         ListTag itemsTag = AEUtils.createListTag(AEItemKey::toTag, this.itemInventory);
         if (!itemsTag.isEmpty()) {
            tag.m_128365_("inventory", itemsTag);
         }

         ListTag itemCatalystTag = AEUtils.createListTag(AEItemKey::toTag, this.itemCatalystInventory);
         if (!itemCatalystTag.isEmpty()) {
            tag.m_128365_("catalystInventory", itemCatalystTag);
         }

         ListTag fluidsTag = AEUtils.createListTag(AEFluidKey::toTag, this.fluidInventory);
         if (!fluidsTag.isEmpty()) {
            tag.m_128365_("fluidInventory", fluidsTag);
         }

         ListTag fluidCatalystTag = AEUtils.createListTag(AEFluidKey::toTag, this.fluidCatalystInventory);
         if (!fluidCatalystTag.isEmpty()) {
            tag.m_128365_("catalystFluidInventory", fluidCatalystTag);
         }

         return tag;
      }

      public void deserializeNBT(CompoundTag tag) {
         this.itemInventory.clear();
         this.itemCatalystInventory.clear();
         this.fluidInventory.clear();
         this.fluidCatalystInventory.clear();
         ListTag items = tag.m_128437_("inventory", 10);
         AEUtils.loadInventory(items, AEItemKey::fromTag, this.itemInventory);
         ListTag catalystItems = tag.m_128437_("catalystInventory", 10);
         AEUtils.loadInventory(catalystItems, AEItemKey::fromTag, this.itemCatalystInventory);
         ListTag fluids = tag.m_128437_("fluidInventory", 10);
         AEUtils.loadInventory(fluids, AEFluidKey::fromTag, this.fluidInventory);
         ListTag catalystFluids = tag.m_128437_("catalystFluidInventory", 10);
         AEUtils.loadInventory(catalystFluids, AEFluidKey::fromTag, this.fluidCatalystInventory);
      }

      public Runnable getOnContentsChanged() {
         return this.onContentsChanged;
      }

      public void setOnContentsChanged(Runnable onContentsChanged) {
         this.onContentsChanged = onContentsChanged;
      }

      public Object2LongOpenHashMap<AEItemKey> getItemInventory() {
         return this.itemInventory;
      }

      public Object2LongOpenHashMap<AEFluidKey> getFluidInventory() {
         return this.fluidInventory;
      }

      public SlotCacheManager getCacheManager() {
         return this.cacheManager;
      }

      public int getSlotIndex() {
         return this.slotIndex;
      }
   }

   protected class MEPatternTrait extends MEIOPartMachine.MEIOTrait implements IMEPatternTrait {
      public MEPatternTrait(MEPatternBufferPartMachine machine) {
         super(machine);
      }

      public MEPatternBufferPartMachine getMachine() {
         return (MEPatternBufferPartMachine)this.machine;
      }

      @NotNull
      @Override
      public ObjectSet<GTRecipe> getCachedGTRecipe() {
         ObjectSet<GTRecipe> recipes = new ObjectOpenHashSet();
         ObjectIterator<it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<ObjectSet<GTRecipe>>> it = Int2ReferenceMaps.fastIterator(
            MEPatternBufferPartMachine.this.recipeMultipleCacheMap
         );

         while (it.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<ObjectSet<GTRecipe>> entry = (it.unimi.dsi.fastutil.ints.Int2ReferenceMap.Entry<ObjectSet<GTRecipe>>)it.next();
            ObjectSet<GTRecipe> recipeSet = (ObjectSet<GTRecipe>)entry.getValue();
            int slot = entry.getIntKey();
            if (recipeSet.isEmpty()) {
               it.remove();
            } else if (MEPatternBufferPartMachine.this.cacheRecipe[slot] && MEPatternBufferPartMachine.this.internalInventory[slot].isActive()) {
               recipes.addAll(recipeSet);
            }
         }

         return recipes;
      }

      @Override
      public void setSlotCacheRecipe(int index, GTRecipe recipe) {
         if (recipe != null && recipe.recipeType != GTRecipeTypes.DUMMY_RECIPES) {
            ObjectSet<GTRecipe> set = (ObjectSet<GTRecipe>)MEPatternBufferPartMachine.this.recipeMultipleCacheMap
               .computeIfAbsent(index, integer -> new ObjectArraySet());
            if (set.add(recipe)) {
               MEPatternBufferPartMachine.this.cacheRecipe[index] = set.size() >= MEPatternBufferPartMachine.this.cacheRecipeCount[index];
            }
         }
      }

      @NotNull
      @Override
      public Int2ReferenceMap<ObjectSet<GTRecipe>> getSlot2RecipesCache() {
         return MEPatternBufferPartMachine.this.recipeMultipleCacheMap;
      }

      @Override
      public void setOnPatternChange(IntConsumer removeMapOnSlot) {
         MEPatternBufferPartMachine.this.removeSlotFromMap = removeMapOnSlot;
      }

      @Override
      public boolean hasCacheInSlot(int slot) {
         return MEPatternBufferPartMachine.this.cacheRecipe[slot];
      }
   }

   protected class Ticker implements IGridTickable {
      public TickingRequest getTickingRequest(IGridNode node) {
         return new TickingRequest(MEExtendedOutputPartMachineBase.MIN_FREQUENCY, MEExtendedOutputPartMachineBase.MAX_FREQUENCY, false, true);
      }

      public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
         if (!MEPatternBufferPartMachine.this.getMainNode().isActive()) {
            MEPatternBufferPartMachine.this.isSleeping = true;
            return TickRateModulation.SLEEP;
         }

         if (MEPatternBufferPartMachine.this.buffer.isEmpty()) {
            if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
               MEPatternBufferPartMachine.this.isSleeping = true;
               return TickRateModulation.SLEEP;
            } else {
               return TickRateModulation.SLOWER;
            }
         } else {
            return AEUtils.reFunds(
                  MEPatternBufferPartMachine.this.buffer, MEPatternBufferPartMachine.this.getMainNode().getGrid(), MEPatternBufferPartMachine.this.actionSource
               )
               ? TickRateModulation.URGENT
               : TickRateModulation.SLOWER;
         }
      }
   }
}
