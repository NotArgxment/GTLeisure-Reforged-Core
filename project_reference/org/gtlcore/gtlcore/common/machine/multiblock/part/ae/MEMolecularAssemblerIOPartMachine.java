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
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.blockentity.crafting.IMolecularAssemblerSupportedPattern;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import appeng.helpers.patternprovider.PatternContainer;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.fancyconfigurator.FancyInvConfigurator;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Pair;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftIOPart;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftPatternContainer;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.machine.multiblock.part.PaginationUIManager;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.integration.lowdragmc.misc.MutableItemTransferList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MEMolecularAssemblerIOPartMachine extends MEIOPartMachine implements PatternContainer, IMECraftIOPart {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MEMolecularAssemblerIOPartMachine.class, MEIOPartMachine.MANAGED_FIELD_HOLDER
   );
   private static final int ROWS_PER_PAGE = 8;
   private static final int PATTERNS_PER_ROW = 9;
   private final InternalInventory internalPatternInventory = new InternalInventory() {
      public int size() {
         return MEMolecularAssemblerIOPartMachine.this.mutableItemTransferList.getSlots();
      }

      public ItemStack getStackInSlot(int slotIndex) {
         return MEMolecularAssemblerIOPartMachine.this.mutableItemTransferList.getStackInSlot(slotIndex);
      }

      public void setItemDirect(int slotIndex, ItemStack stack) {
         MEMolecularAssemblerIOPartMachine.this.mutableItemTransferList.setStackInSlot(slotIndex, stack);
         MEMolecularAssemblerIOPartMachine.this.mutableItemTransferList.onContentsChanged();
         MEMolecularAssemblerIOPartMachine.this.onPatternChange(slotIndex);
      }

      public boolean isItemValid(int slot, ItemStack stack) {
         return slot <= MEMolecularAssemblerIOPartMachine.this.mutableItemTransferList.getSlots()
            && AEUtils.molecularFilter(stack, MEMolecularAssemblerIOPartMachine.this.getLevel());
      }
   };
   @DescSynced
   @Persisted
   protected String customName = "";
   private boolean needPatternSync;
   private boolean shouldOpen = false;
   protected final NotifiableMAHandlerTrait maHandler;
   private final MutableItemTransferList mutableItemTransferList;
   protected PaginationUIManager paginationUIManager;
   @Persisted
   protected final ItemStackTransfer sharedToolsInventory;
   private final BiMap<@NotNull IPatternDetails, Integer> patternSlotMap;
   private final Int2ObjectMap<@NotNull ObjectSet<@NotNull Item>> toolsSlotMap;
   private final Object2LongLinkedOpenHashMap<GenericStack> outputItems;
   private final Object2LongOpenHashMap<AEKey> buffer;
   @DescSynced
   private final Set<Long> proxies = new LongOpenHashSet();
   private final ReferenceSortedSet<IItemTransfer> proxyStackTransfers = new ReferenceLinkedOpenHashSet();
   @Nullable
   protected TickableSubscription updateSubs;

   public MEMolecularAssemblerIOPartMachine(IMachineBlockEntity holder) {
      super(holder, IO.BOTH);
      this.getMainNode().addService(IGridTickable.class, new MEMolecularAssemblerIOPartMachine.Ticker()).addService(ICraftingProvider.class, this);
      this.patternSlotMap = HashBiMap.create();
      this.toolsSlotMap = new Int2ObjectOpenHashMap();
      this.outputItems = new Object2LongLinkedOpenHashMap();
      this.buffer = new Object2LongOpenHashMap();
      this.maHandler = new MECraftHandler(this);
      this.sharedToolsInventory = new ItemStackTransfer(9);
      this.mutableItemTransferList = new MutableItemTransferList();
   }

   public boolean pushPattern(IPatternDetails iPatternDetails, KeyCounter[] keyCounters) {
      if (this.getMainNode().isActive() && this.patternSlotMap.containsKey(iPatternDetails) && iPatternDetails instanceof AEProcessingPattern processingPattern
         )
       {
         GenericStack output = processingPattern.getOutputs()[0];
         if (!(output.what() instanceof AEItemKey)) {
            return false;
         }

         int slot = (Integer)this.patternSlotMap.get(processingPattern);
         if (this.toolsSlotMap.containsKey(slot)) {
            ObjectOpenHashSet<Item> requiredTools = new ObjectOpenHashSet((ObjectCollection)this.toolsSlotMap.get(slot));

            for (int i = 0; i < this.sharedToolsInventory.getSlots(); i++) {
               ItemStack stack = this.sharedToolsInventory.getStackInSlot(i);
               if (!stack.m_41763_()) {
                  requiredTools.remove(stack.m_41720_());
               }

               if (requiredTools.isEmpty()) {
                  break;
               }
            }

            if (!requiredTools.isEmpty()) {
               AEUtils.pushInputsToMEPatternBufferInventory(keyCounters, this.buffer::addTo);
               this.meTrait.notifySelfIO();
               return true;
            }
         }

         GenericStack requireStack = processingPattern.getInputs()[0].getPossibleInputs()[0];
         long multiplier = 0L;
         KeyCounter[] var9 = keyCounters;
         int var10 = var9.length;

         for (int var11 = 0; var11 < var10; var11++) {
            for (Entry<AEKey> input : var9[var11]) {
               if (requireStack.what().equals(input.getKey())) {
                  multiplier = input.getLongValue() / (requireStack.amount() * processingPattern.getInputs()[0].getMultiplier());
                  break;
               }
            }
         }

         if (multiplier == 0L) {
            return false;
         }

         this.outputItems.addTo(output, multiplier);
         this.maHandler.notifyListeners();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void init(@NotNull Set<BlockPos> proxies) {
      this.clear();
      this.proxies.addAll(proxies.stream().mapToLong(BlockPos::m_121878_).collect(LongArrayList::new, LongArrayList::add, LongList::addAll));
      this.mutableItemTransferList.addTransfers(this.getProxies());
      if (!this.mutableItemTransferList.isEmpty()) {
         for (int i = 0; i < this.mutableItemTransferList.getSlots(); i++) {
            IPatternDetails pattern = this.getPatternDetailsAsProcessing(this.mutableItemTransferList.getStackInSlot(i), i);
            if (pattern != null) {
               this.patternSlotMap.forcePut(pattern, i);
            }
         }

         this.shouldOpen = true;
      } else {
         this.shouldOpen = false;
      }

      this.needPatternSync = true;
   }

   @NotNull
   @Override
   public NotifiableMAHandlerTrait getNotifiableMAHandlerTrait() {
      return this.maHandler;
   }

   public boolean canShared() {
      return false;
   }

   protected void clear() {
      this.mutableItemTransferList.clear();
      this.patternSlotMap.clear();
      this.toolsSlotMap.clear();
      this.proxies.clear();
      this.shouldOpen = false;
   }

   @Nullable
   private IPatternDetails getPatternDetailsAsProcessing(ItemStack stack, int slotIndex) {
      if (!(!stack.m_41619_() && stack.m_41720_() instanceof EncodedPatternItem encodedPatternItem)) {
         return null;
      }

      if (encodedPatternItem.decode(stack, this.getLevel(), false) instanceof IMolecularAssemblerSupportedPattern molecularAssemblerSupportedPattern) {
         Pair<IPatternDetails, ObjectSet<Item>> pair = AEUtils.createProcessingFromCraftPattern(molecularAssemblerSupportedPattern, this.getLevel());
         ObjectSet<Item> remainingStacks = (ObjectSet<Item>)pair.getRight();
         IPatternDetails pattern = (IPatternDetails)pair.getLeft();
         if (!(pattern instanceof AEProcessingPattern)) {
            return null;
         }

         if (remainingStacks != null && !remainingStacks.isEmpty()) {
            this.toolsSlotMap.put(slotIndex, remainingStacks);
         } else {
            this.toolsSlotMap.remove(slotIndex);
         }

         return pattern;
      } else {
         return null;
      }
   }

   protected ReferenceSortedSet<IItemTransfer> getProxies() {
      if (this.proxyStackTransfers.size() != this.proxies.size()) {
         this.proxyStackTransfers.clear();
         ObjectList<IItemTransfer> patternInventories = new ObjectArrayList();

         for (Long pos : this.proxies) {
            if (MetaMachine.getMachine(Objects.requireNonNull(this.getLevel()), BlockPos.m_122022_(pos)) instanceof MECraftPatternContainerPartMachine proxy) {
               patternInventories.add(proxy.getPatternInventory());
            }
         }

         patternInventories.sort(Comparator.comparingInt(IMECraftPatternContainer::sumNonEmpty).reversed().thenComparingInt(IItemTransfer::getSlots));
         this.proxyStackTransfers.addAll(patternInventories);
      }

      return ReferenceSortedSets.unmodifiable(this.proxyStackTransfers);
   }

   @NotNull
   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   @Override
   public void onMainNodeStateChanged(@NotNull State reason) {
      super.onMainNodeStateChanged(reason);
      this.updateSubscription();
   }

   public void onUnload() {
      super.onUnload();
      this.clear();
   }

   public void removedFromController(@NotNull IMultiController controller) {
      super.removedFromController(controller);
      this.clear();
      this.needPatternSync = true;
   }

   public void onMachineRemoved() {
      this.clearInventory(this.sharedToolsInventory);
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
         ItemStack newPattern = this.mutableItemTransferList.getStackInSlot(index);
         IPatternDetails newPatternDetails = this.getPatternDetailsAsProcessing(newPattern, index);
         if (newPatternDetails != null) {
            this.patternSlotMap.forcePut(newPatternDetails, index);
         } else {
            this.patternSlotMap.inverse().remove(index);
         }

         this.needPatternSync = true;
      }
   }

   @NotNull
   public Widget createUIWidget() {
      MutableItemTransferList tempmutableItemTransferList = new MutableItemTransferList(this.getProxies());
      int totalCount = tempmutableItemTransferList.getSlots();
      int colSize = 9;
      int uiWidth = Math.max(178, 106);
      int uiHeight = 172;
      this.paginationUIManager = new PaginationUIManager(9, 8, totalCount, uiWidth, 172, this::onPatternChange, tempmutableItemTransferList);
      WidgetGroup group = new WidgetGroup(0, 0, this.paginationUIManager.getUiWidth(), this.paginationUIManager.getUiHeight());
      group.addWidget(new LabelWidget(8, 2, () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
      group.addWidget(
         new AETextInputButtonWidget(100, 2, 70, 10)
            .setText(this.customName)
            .setOnConfirm(this::setCustomName)
            .setButtonTooltips(new Component[]{Component.m_237115_("gui.gtceu.rename.desc")})
      );
      group.addWidget(this.paginationUIManager.createPaginationUI(null));
      return group;
   }

   public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new FancyInvConfigurator(this.sharedToolsInventory, Component.m_237115_("gui.gtceu.share_inventory.title"))
               .setTooltips(List.of(Component.m_237115_("gui.gtceu.share_inventory.desc.0"), Component.m_237115_("gui.gtceu.share_inventory.desc.1")))
         }
      );
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return this.shouldOpen;
   }

   public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
      super.saveCustomPersistedData(tag, forDrop);
      if (!this.buffer.isEmpty() || !this.outputItems.isEmpty()) {
         ListTag bufferTag = AEUtils.createListTag(AEKey::toTagGeneric, this.buffer);
         if (!bufferTag.isEmpty()) {
            tag.m_128365_("buffer", bufferTag);
         }

         ListTag outputTag = AEUtils.createListTag(GenericStack::writeTag, this.outputItems);
         if (!outputTag.isEmpty()) {
            tag.m_128365_("outputItems", outputTag);
         }
      }
   }

   @Override
   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      this.buffer.clear();
      ListTag bufferTag = tag.m_128437_("buffer", 10);
      AEUtils.loadInventory(bufferTag, AEKey::fromTagGeneric, this.buffer);
      ListTag outputTag = tag.m_128437_("outputItems", 10);
      AEUtils.loadInventory(outputTag, GenericStack::readTag, this.outputItems);
   }

   public List<IPatternDetails> getAvailablePatterns() {
      return new ObjectArrayList(this.patternSlotMap.keySet());
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
         return !this.customName.isEmpty()
            ? new PatternContainerGroup(AEItemKey.of(controllerDefinition.asStack()), Component.m_237113_(this.customName), Collections.emptyList())
            : new PatternContainerGroup(
               AEItemKey.of(controllerDefinition.asStack()), Component.m_237115_(controllerDefinition.getDescriptionId()), Collections.emptyList()
            );
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

   public void setCustomName(String customName) {
      this.customName = customName;
   }

   public Object2LongLinkedOpenHashMap<GenericStack> getOutputItems() {
      return this.outputItems;
   }

   public Object2LongOpenHashMap<AEKey> getBuffer() {
      return this.buffer;
   }

   protected class Ticker implements IGridTickable {
      public TickingRequest getTickingRequest(IGridNode node) {
         return new TickingRequest(MEExtendedOutputPartMachineBase.MIN_FREQUENCY, MEExtendedOutputPartMachineBase.MAX_FREQUENCY, false, true);
      }

      public TickRateModulation tickingRequest(IGridNode node, int ticksSinceLastCall) {
         if (!MEMolecularAssemblerIOPartMachine.this.getMainNode().isActive()) {
            MEMolecularAssemblerIOPartMachine.this.isSleeping = true;
            return TickRateModulation.SLEEP;
         }

         if (MEMolecularAssemblerIOPartMachine.this.buffer.isEmpty()) {
            if (ticksSinceLastCall >= MEExtendedOutputPartMachineBase.MAX_FREQUENCY) {
               MEMolecularAssemblerIOPartMachine.this.isSleeping = true;
               return TickRateModulation.SLEEP;
            } else {
               return TickRateModulation.SLOWER;
            }
         } else {
            return AEUtils.reFunds(
                  MEMolecularAssemblerIOPartMachine.this.buffer,
                  MEMolecularAssemblerIOPartMachine.this.getMainNode().getGrid(),
                  MEMolecularAssemblerIOPartMachine.this.actionSource
               )
               ? TickRateModulation.URGENT
               : TickRateModulation.SLOWER;
         }
      }
   }
}
