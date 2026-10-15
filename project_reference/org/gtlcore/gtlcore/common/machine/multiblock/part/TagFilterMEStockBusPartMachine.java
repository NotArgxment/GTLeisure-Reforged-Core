package org.gtlcore.gtlcore.common.machine.multiblock.part;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.storage.IStorageService;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.util.prioritylist.IPartitionList;
import com.glodblock.github.extendedae.common.me.taglist.TagPriorityList;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton.Toggle;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAESlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IModifiableSyncOffset;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.ExportOnlyAEConfigureItemSlot;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IMESlot;
import org.gtlcore.gtlcore.api.machine.trait.MEStock.IOptimizedMEList;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TagFilterMEStockBusPartMachine extends MEInputBusPartMachine implements IModifiableSyncOffset {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      TagFilterMEStockBusPartMachine.class, MEInputBusPartMachine.MANAGED_FIELD_HOLDER
   );
   private static final boolean ENABLE_ULTIMATE_ME_STOCKING = ConfigHolder.INSTANCE.enableUltimateMEStocking;
   private static final ResourceTexture TEXTURE = new ResourceTexture("gtceu:textures/gui/list.png");
   @Persisted
   protected String tagWhite = "";
   @Persisted
   protected String tagBlack = "";
   @Persisted
   private boolean isCountSort = false;

   public TagFilterMEStockBusPartMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   protected NotifiableItemStackHandler createInventory(Object... args) {
      this.aeItemHandler = new TagFilterMEStockBusPartMachine.ExportOnlyAEStockingItemList(this, 16);
      return this.aeItemHandler;
   }

   public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
      super.attachConfigurators(configuratorPanel);
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new Toggle(new TextTexture("A-Z"), new TextTexture("\u6570\u91cf\u25bc"), this::isCountSort, (clickData, pressed) -> this.setCountSort(pressed))
               .setTooltipsSupplier(pressed -> List.of(Component.m_237115_("tooltip.gtlcore.auto_pull_sort_mode")))
         }
      );
      configuratorPanel.attachConfigurators(new IFancyConfigurator[]{new TagFilterMEStockBusPartMachine.FilterIFancyConfigurator()});
   }

   public void autoIO() {
      super.autoIO();
      if (this.getOffsetTimer() % 50L == 0L) {
         this.refreshList();
         this.syncME();
      }
   }

   protected void syncME() {
      IGrid grid = this.getMainNode().getGrid();
      if (grid != null) {
         MEStorage networkInv = grid.getStorageService().getInventory();

         for (ExportOnlyAEItemSlot slot : this.aeItemHandler.getInventory()) {
            GenericStack config = slot.getConfig();
            if (config != null) {
               AEKey key = config.what();
               long extracted = networkInv.extract(key, 9223372036854775807L, Actionable.SIMULATE, this.actionSource);
               if (extracted > 0L) {
                  slot.setStock(new GenericStack(key, extracted));
                  continue;
               }
            }

            slot.setStock(null);
         }
      }
   }

   private void refreshList() {
      IGrid grid = this.getMainNode().getGrid();
      if (grid == null) {
         this.aeItemHandler.clearInventory(0);
      } else {
         IStorageService storageService = grid.getStorageService();
         MEStorage networkStorage = storageService.getInventory();
         IPartitionList filter = new TagPriorityList(this.tagWhite, this.tagBlack);
         List<GenericStack> order = new ObjectArrayList();
         ExportOnlyAEItemSlot[] inventory = this.aeItemHandler.getInventory();
         KeyCounter counter = networkStorage.getAvailableStacks();
         int index = 0;

         for (Entry<AEKey> entry : counter) {
            if (!this.isCountSort && index >= 16) {
               break;
            }

            AEKey what = (AEKey)entry.getKey();
            long amount = entry.getLongValue();
            if (amount > 0L && what instanceof AEItemKey itemKey && filter.isListed(itemKey)) {
               if (this.isCountSort) {
                  order.add(new GenericStack(itemKey, amount));
               } else {
                  long request = networkStorage.extract(what, amount, Actionable.SIMULATE, this.actionSource);
                  if (request != 0L) {
                     ExportOnlyAEItemSlot slot = inventory[index];
                     ((IMESlot)slot).setConfigWithoutNotify(new GenericStack(what, 1L));
                     slot.setStock(new GenericStack(what, request));
                     index++;
                  }
               }
            }
         }

         if (this.isCountSort) {
            order.sort((o1, o2) -> Long.compare(o2.amount(), o1.amount()));
            int len = Math.min(order.size(), 16);

            for (int i = 0; i < len; i++) {
               GenericStack stack = order.get(i);
               long request = networkStorage.extract(stack.what(), stack.amount(), Actionable.SIMULATE, this.actionSource);
               if (request != 0L) {
                  ExportOnlyAEItemSlot slot = inventory[index];
                  ((IMESlot)slot).setConfigWithoutNotify(new GenericStack(stack.what(), 1L));
                  slot.setStock(new GenericStack(stack.what(), request));
                  index++;
               }
            }
         }

         this.aeItemHandler.clearInventory(index);
         ((IOptimizedMEList)this.aeItemHandler).onConfigChanged();
      }
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().m_6937_(new TickTask(1, () -> ((IOptimizedMEList)this.aeItemHandler).onConfigChanged()));
      }
   }

   protected CompoundTag writeConfigToTag() {
      CompoundTag tag = new CompoundTag();
      tag.m_128344_("GhostCircuit", (byte)IntCircuitBehaviour.getCircuitConfiguration(this.circuitInventory.getStackInSlot(0)));
      tag.m_128359_("TagWhite", this.tagWhite);
      tag.m_128359_("TagBlack", this.tagBlack);
      tag.m_128405_("SyncOffset", this.getOffset());
      return tag;
   }

   protected void readConfigFromTag(CompoundTag tag) {
      if (tag.m_128441_("GhostCircuit")) {
         this.circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(tag.m_128445_("GhostCircuit")));
      }

      if (tag.m_128441_("TagWhite")) {
         this.tagWhite = tag.m_128461_("TagWhite");
      }

      if (tag.m_128441_("TagBlack")) {
         this.tagBlack = tag.m_128461_("TagBlack");
      }

      if (tag.m_128441_("SyncOffset")) {
         this.setOffset(tag.m_128451_("SyncOffset"));
      }
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void setTagWhite(String tagWhite) {
      this.tagWhite = tagWhite;
   }

   public void setTagBlack(String tagBlack) {
      this.tagBlack = tagBlack;
   }

   public boolean isCountSort() {
      return this.isCountSort;
   }

   public void setCountSort(boolean isCountSort) {
      this.isCountSort = isCountSort;
   }

   private class ExportOnlyAEStockingItemList extends ExportOnlyAEItemList implements IOptimizedMEList {
      protected ObjectArrayList<AEItemKey> configList = new ObjectArrayList();
      protected IntArrayList configIndexList = new IntArrayList();

      public ExportOnlyAEStockingItemList(MetaMachine holder, int slots) {
         super(holder, slots, () -> TagFilterMEStockBusPartMachine.this.new ExportOnlyAEStockingItemSlot());

         for (ExportOnlyAEItemSlot exportOnlyAEItemSlot : this.inventory) {
            ((IMESlot)exportOnlyAEItemSlot).setOnConfigChanged(this::onConfigChanged);
         }
      }

      public void clearInventory(int startIndex) {
         for (int i = startIndex; i < this.getConfigurableSlots(); i++) {
            IConfigurableSlot slot = this.getConfigurableSlot(i);
            ((IMESlot)slot).setConfigWithoutNotify(null);
            slot.setStock(null);
         }
      }

      @Override
      public void onConfigChanged() {
         this.configList.clear();
         this.configIndexList.clear();
         int i = 0;

         for (int inventoryLength = this.inventory.length; i < inventoryLength; i++) {
            GenericStack config = this.inventory[i].getConfig();
            if (config != null && config.what() instanceof AEItemKey key) {
               this.configList.add(key);
               this.configIndexList.add(i);
            }
         }
      }

      @Override
      public boolean isStocking() {
         return true;
      }

      public boolean isAutoPull() {
         return true;
      }

      @Nullable
      public List<Ingredient> handleRecipeInner(IO io, GTRecipe recipe, List<Ingredient> left, @Nullable String slotName, boolean simulate) {
         if (io == IO.IN && !left.isEmpty()) {
            IGrid grid = TagFilterMEStockBusPartMachine.this.getMainNode().getGrid();
            if (grid == null) {
               return left;
            }

            MEStorage aeNetwork = grid.getStorageService().getInventory();
            boolean changed = false;
            ListIterator<Ingredient> listIterator = left.listIterator();

            while (listIterator.hasNext()) {
               Ingredient ingredient = listIterator.next();
               if (ingredient.m_43947_()) {
                  listIterator.remove();
               } else {
                  long amount;
                  if (ingredient instanceof LongIngredient li) {
                     amount = li.getActualAmount();
                  } else if (ingredient instanceof SizedIngredient si) {
                     amount = si.getAmount();
                  } else {
                     amount = 1L;
                  }

                  if (amount < 1L) {
                     listIterator.remove();
                  } else {
                     int i = 0;

                     for (int configListSize = this.configList.size(); i < configListSize; i++) {
                        AEItemKey aeItemKey = (AEItemKey)this.configList.get(i);
                        if (aeItemKey.matches(ingredient)) {
                           long extracted = aeNetwork.extract(
                              aeItemKey, amount, simulate ? Actionable.SIMULATE : Actionable.MODULATE, TagFilterMEStockBusPartMachine.this.getActionSource()
                           );
                           if (extracted > 0L) {
                              changed = true;
                              amount -= extracted;
                              if (!simulate) {
                                 ExportOnlyAEItemSlot slot = this.inventory[this.configIndexList.getInt(i)];
                                 if (slot.getStock() != null) {
                                    long amt = slot.getStock().amount() - extracted;
                                    if (amt == 0L) {
                                       slot.setStock(null);
                                    } else {
                                       slot.setStock(new GenericStack(aeItemKey, amt));
                                    }
                                 }
                              }
                           }
                        }

                        if (amount <= 0L) {
                           listIterator.remove();
                           break;
                        }
                     }
                  }
               }
            }

            if (!simulate && changed) {
               this.setChanged(true);
               this.onContentsChanged();
            }

            return left.isEmpty() ? null : left;
         } else {
            return left;
         }
      }

      @Nullable
      @Override
      public Object2LongMap<ItemStack> getMEItemMap() {
         if (TagFilterMEStockBusPartMachine.ENABLE_ULTIMATE_ME_STOCKING || this.getChanged()) {
            this.setChanged(false);
            Object2LongOpenHashMap<ItemStack> itemMap = this.getItemMap();
            itemMap.clear();
            MEStorage aeNetwork = Objects.requireNonNull(TagFilterMEStockBusPartMachine.this.getMainNode().getGrid()).getStorageService().getInventory();
            ObjectListIterator var3 = this.configList.iterator();

            while (var3.hasNext()) {
               AEItemKey key = (AEItemKey)var3.next();
               long extracted = aeNetwork.extract(key, 9223372036854775807L, Actionable.SIMULATE, TagFilterMEStockBusPartMachine.this.getActionSource());
               if (extracted > 0L) {
                  itemMap.addTo(key.toStack(), extracted);
               }
            }
         }

         return this.getItemMap().isEmpty() ? null : this.getItemMap();
      }
   }

   private class ExportOnlyAEStockingItemSlot extends ExportOnlyAEConfigureItemSlot {
      public ExportOnlyAEStockingItemSlot() {
      }

      public ExportOnlyAEStockingItemSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
         super(config, stock);
      }

      public ItemStack extractItem(int slot, int amount, boolean simulate, boolean notifyChanges) {
         if (slot == 0 && this.stock != null && this.config != null) {
            if (!TagFilterMEStockBusPartMachine.this.isOnline()) {
               return ItemStack.f_41583_;
            }

            IGrid grid = TagFilterMEStockBusPartMachine.this.getMainNode().getGrid();
            if (grid == null) {
               return ItemStack.f_41583_;
            }

            MEStorage aeNetwork = grid.getStorageService().getInventory();
            Actionable action = simulate ? Actionable.SIMULATE : Actionable.MODULATE;
            AEKey key = this.config.what();
            long extracted = aeNetwork.extract(key, amount, action, TagFilterMEStockBusPartMachine.this.actionSource);
            if (extracted > 0L) {
               ItemStack resultStack = key instanceof AEItemKey itemKey ? itemKey.toStack((int)extracted) : ItemStack.f_41583_;
               if (!simulate) {
                  this.stock = ExportOnlyAESlot.copy(this.stock, this.stock.amount() - extracted);
                  if (this.stock.amount() == 0L) {
                     this.stock = null;
                  }

                  if (notifyChanges && this.onContentsChanged != null) {
                     this.onContentsChanged.run();
                  }
               }

               return resultStack;
            }
         }

         return ItemStack.f_41583_;
      }

      public TagFilterMEStockBusPartMachine.ExportOnlyAEStockingItemSlot copy() {
         return TagFilterMEStockBusPartMachine.this.new ExportOnlyAEStockingItemSlot(
            this.config == null ? null : copy(this.config), this.stock == null ? null : copy(this.stock)
         );
      }
   }

   private class FilterIFancyConfigurator implements IFancyConfigurator {
      public Component getTitle() {
         return Component.m_237115_("gui.gtlcore.tag_filter_config");
      }

      public IGuiTexture getIcon() {
         return TagFilterMEStockBusPartMachine.TEXTURE.scale(1.25F);
      }

      public Widget createConfigurator() {
         return new WidgetGroup(0, 0, 132, 100)
            .addWidget(new LabelWidget(9, 4, () -> Component.m_237115_("gui.gtlcore.tag_whitelist").getString()))
            .addWidget(
               new TextFieldWidget(9, 16, 114, 16, () -> TagFilterMEStockBusPartMachine.this.tagWhite, v -> TagFilterMEStockBusPartMachine.this.tagWhite = v)
            )
            .addWidget(new LabelWidget(9, 36, () -> Component.m_237115_("gui.gtlcore.tag_blacklist").getString()))
            .addWidget(
               new TextFieldWidget(9, 48, 114, 16, () -> TagFilterMEStockBusPartMachine.this.tagBlack, v -> TagFilterMEStockBusPartMachine.this.tagBlack = v)
            )
            .addWidget(new LabelWidget(0, 68, () -> Component.m_237115_("gui.gtlcore.wildcard_info").getString()))
            .addWidget(new LabelWidget(0, 84, () -> Component.m_237115_("gui.gtlcore.logic_operators").getString()));
      }
   }
}
