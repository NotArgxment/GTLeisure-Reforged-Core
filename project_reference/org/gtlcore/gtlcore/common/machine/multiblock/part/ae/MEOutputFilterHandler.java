package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.NumberInputWidget;
import com.gregtechceu.gtceu.api.gui.widget.ToggleButtonWidget;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.PhantomFluidWidget;
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.FluidStorage;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import com.lowdragmc.lowdraglib.utils.Position;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;

public class MEOutputFilterHandler implements ITagSerializable<CompoundTag> {
   private final ObjectOpenHashSet<AEItemKey> itemFilterHashSet = new ObjectOpenHashSet();
   private final ObjectOpenHashSet<AEFluidKey> fluidFilterHashSet = new ObjectOpenHashSet();
   private final Runnable markDirty;
   private final Runnable updatePriority;
   private final Supplier<Integer> prioritySupplier;
   private final Consumer<Integer> onPriorityChanged;
   protected boolean isItemBlackList;
   protected boolean ignoreItemNbt = true;
   protected boolean isFluidBlackList;
   protected boolean ignoreFluidNbt = true;
   private boolean hasItemFilterChange;
   private boolean hasFluidFilterChange;
   private boolean hasItemFilter;
   private boolean hasFluidFilter;
   private final int row;
   private final int col;
   private final FluidStorage[] filterTanks;
   private final ItemStackTransfer filterSlots;

   public MEOutputFilterHandler(
      int row, int col, Runnable markDirty, Runnable updatePriority, Supplier<Integer> prioritySupplier, Consumer<Integer> onPriorityChanged
   ) {
      this.row = row;
      this.col = col;
      this.markDirty = markDirty;
      this.updatePriority = updatePriority;
      this.prioritySupplier = prioritySupplier;
      this.onPriorityChanged = onPriorityChanged;
      this.filterTanks = new FluidStorage[row * col];
      this.filterSlots = new ItemStackTransfer(row * col);
      Arrays.setAll(this.filterTanks, i -> new FluidStorage(FluidHelper.getBucket()));
   }

   public WidgetGroup createMainWidgetGroup() {
      WidgetGroup itemFilter = this.openItemFilterConfigurator(20, 5, this.row, this.col);
      int height = itemFilter.getSizeHeight() + 5;
      WidgetGroup fluidFilter = this.openFluidFilterConfigurator(20, height + 5, this.row, this.col);
      height += fluidFilter.getSizeHeight() + 10;
      NumberInputWidget<Integer> priorityWidget = new IntInputWidget(new Position(20, height), this.prioritySupplier, this.onPriorityChanged)
         .setMin(10)
         .setMax(100000);
      height += priorityWidget.getSizeHeight();
      WidgetGroup widgetGroup = new WidgetGroup(0, 0, itemFilter.getSizeWidth() + 6, height);
      widgetGroup.addWidget(itemFilter);
      widgetGroup.addWidget(fluidFilter);
      widgetGroup.addWidget(priorityWidget);
      return widgetGroup;
   }

   protected WidgetGroup openItemFilterConfigurator(int x, int y, int row, int col) {
      WidgetGroup group = new WidgetGroup(x, y, 18 * col + 25, 18 * row);

      for (int i = 0; i < row; i++) {
         for (int j = 0; j < col; j++) {
            int index = i * col + j;
            Widget slot = (new PhantomSlotWidget(this.filterSlots, index, j * 18, i * 18) {
               public void updateScreen() {
                  super.updateScreen();
                  this.setMaxStackSize(1);
               }

               public void detectAndSendChanges() {
                  super.detectAndSendChanges();
                  this.setMaxStackSize(1);
               }
            }).setChangeListener(() -> this.hasItemFilterChange = true).setBackground(new IGuiTexture[]{GuiTextures.SLOT});
            group.addWidget(slot);
         }
      }

      group.addWidget(new ToggleButtonWidget(18 * col + 17, 9, 18, 18, GuiTextures.BUTTON_BLACKLIST, this::isItemBlackList, this::setItemBlackList));
      group.addWidget(new ToggleButtonWidget(18 * col + 17, 27, 18, 18, GuiTextures.BUTTON_FILTER_NBT, this::isIgnoreItemNbt, this::setIgnoreItemNbt));
      return group;
   }

   protected WidgetGroup openFluidFilterConfigurator(int x, int y, int row, int col) {
      WidgetGroup group = new WidgetGroup(x, y, 18 * col + 25, 18 * row);

      for (int i = 0; i < row; i++) {
         for (int j = 0; j < col; j++) {
            int index = i * col + j;
            TankWidget slot = (new PhantomFluidWidget(
                  this.filterTanks[index],
                  0,
                  j * 18,
                  i * 18,
                  18,
                  18,
                  () -> this.filterTanks[index].getFluid(),
                  fluidStack -> this.filterTanks[index].setFluid(fluidStack)
               ) {
                  public void updateScreen() {
                     super.updateScreen();
                     this.setShowAmount(false);
                  }

                  public void detectAndSendChanges() {
                     super.detectAndSendChanges();
                     this.setShowAmount(false);
                  }
               })
               .setChangeListener(() -> this.hasFluidFilterChange = true)
               .setBackground(GuiTextures.SLOT);
            group.addWidget(slot);
         }
      }

      group.addWidget(new ToggleButtonWidget(18 * col + 17, 9, 18, 18, GuiTextures.BUTTON_BLACKLIST, this::isFluidBlackList, this::setFluidBlackList));
      group.addWidget(new ToggleButtonWidget(18 * col + 17, 27, 18, 18, GuiTextures.BUTTON_FILTER_NBT, this::isIgnoreFluidNbt, this::setIgnoreFluidNbt));
      return group;
   }

   protected void onUIClosed() {
      if (!LDLib.isRemote()) {
         if (this.hasItemFilterChange || this.hasFluidFilterChange) {
            if (this.hasItemFilterChange) {
               this.itemFilterHashSet.clear();

               for (int i = 0; i < this.filterSlots.getSlots(); i++) {
                  ItemStack itemStack = this.filterSlots.getStackInSlot(i);
                  if (!itemStack.m_41619_()) {
                     this.itemFilterHashSet.add(this.ignoreItemNbt ? AEItemKey.of(itemStack.m_41720_()) : AEItemKey.of(itemStack));
                  }
               }

               this.hasItemFilter = !this.itemFilterHashSet.isEmpty();
               this.hasItemFilterChange = false;
            }

            if (this.hasFluidFilterChange) {
               this.fluidFilterHashSet.clear();

               for (FluidStorage filterTank : this.filterTanks) {
                  FluidStack fluidStack = filterTank.getFluid();
                  if (!fluidStack.isEmpty()) {
                     this.fluidFilterHashSet
                        .add(this.ignoreFluidNbt ? AEFluidKey.of(fluidStack.getFluid()) : AEFluidKey.of(fluidStack.getFluid(), fluidStack.getTag()));
                  }
               }

               this.hasFluidFilter = !this.fluidFilterHashSet.isEmpty();
               this.hasFluidFilterChange = false;
            }

            this.markDirty.run();
         }

         this.updatePriority.run();
      }
   }

   public List<Ingredient> testIngredient(List<Ingredient> left) {
      if (!this.hasItemFilter) {
         return List.of();
      }

      left.removeIf(ingredient -> {
         ItemStack[] items = ingredient.m_43908_();
         return items.length == 0 ? false : this.test(items[0]);
      });
      return left;
   }

   public List<FluidIngredient> testFluidIngredient(List<FluidIngredient> left) {
      if (!this.hasFluidFilter) {
         return List.of();
      }

      left.removeIf(ingredient -> {
         FluidStack[] fluids = ingredient.getStacks();
         return fluids.length == 0 ? false : this.test(fluids[0]);
      });
      return left;
   }

   protected boolean test(ItemStack itemStack) {
      if (!this.hasItemFilter) {
         return true;
      }

      AEItemKey key = this.ignoreItemNbt ? AEItemKey.of(itemStack.m_41720_()) : AEItemKey.of(itemStack);
      return this.isItemBlackList != this.itemFilterHashSet.contains(key);
   }

   protected boolean test(FluidStack fluidStack) {
      if (!this.hasFluidFilter) {
         return true;
      }

      AEFluidKey key = this.ignoreFluidNbt ? AEFluidKey.of(fluidStack.getFluid()) : AEFluidKey.of(fluidStack.getFluid(), fluidStack.getTag());
      return this.isFluidBlackList != this.fluidFilterHashSet.contains(key);
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      ListTag itemsTag = AEUtils.createListTag(AEItemKey::toTag, this.itemFilterHashSet);
      if (!itemsTag.isEmpty()) {
         tag.m_128365_("items", itemsTag);
      }

      ListTag fluidsTag = AEUtils.createListTag(AEFluidKey::toTag, this.fluidFilterHashSet);
      if (!fluidsTag.isEmpty()) {
         tag.m_128365_("fluids", fluidsTag);
      }

      tag.m_128379_("isItemBlackList", this.isItemBlackList);
      tag.m_128379_("ignoreItemNbt", this.ignoreItemNbt);
      tag.m_128379_("isFluidBlackList", this.isFluidBlackList);
      tag.m_128379_("ignoreFluidNbt", this.ignoreFluidNbt);
      tag.m_128365_("filterSlots", this.filterSlots.serializeNBT());
      ListTag list = new ListTag();
      Arrays.stream(this.filterTanks).map(FluidStorage::serializeNBT).forEach(list::add);
      tag.m_128365_("filterTanks", list);
      return (R)tag;
   }

   public void deserializeNBT(CompoundTag tag) {
      AEUtils.loadInventory(tag.m_128437_("items", 10), AEItemKey::fromTag, this.itemFilterHashSet);
      AEUtils.loadInventory(tag.m_128437_("fluids", 10), AEFluidKey::fromTag, this.fluidFilterHashSet);
      this.isItemBlackList = tag.m_128471_("isItemBlackList");
      this.isFluidBlackList = tag.m_128471_("isFluidBlackList");
      this.ignoreItemNbt = tag.m_128471_("ignoreItemNbt");
      this.ignoreFluidNbt = tag.m_128471_("ignoreFluidNbt");
      this.filterSlots.deserializeNBT(tag.m_128469_("filterSlots"));
      ListTag list = tag.m_128437_("filterTanks", 10);

      for (int i = 0; i < list.size(); i++) {
         this.filterTanks[i].deserializeNBT(list.m_128728_(i));
      }

      this.hasItemFilter = !this.itemFilterHashSet.isEmpty();
      this.hasFluidFilter = !this.fluidFilterHashSet.isEmpty();
   }

   public boolean isItemBlackList() {
      return this.isItemBlackList;
   }

   public void setItemBlackList(boolean isItemBlackList) {
      this.isItemBlackList = isItemBlackList;
   }

   public boolean isIgnoreItemNbt() {
      return this.ignoreItemNbt;
   }

   public void setIgnoreItemNbt(boolean ignoreItemNbt) {
      this.ignoreItemNbt = ignoreItemNbt;
   }

   public boolean isFluidBlackList() {
      return this.isFluidBlackList;
   }

   public void setFluidBlackList(boolean isFluidBlackList) {
      this.isFluidBlackList = isFluidBlackList;
   }

   public boolean isIgnoreFluidNbt() {
      return this.ignoreFluidNbt;
   }

   public void setIgnoreFluidNbt(boolean ignoreFluidNbt) {
      this.ignoreFluidNbt = ignoreFluidNbt;
   }

   public boolean isHasItemFilter() {
      return this.hasItemFilter;
   }

   public boolean isHasFluidFilter() {
      return this.hasFluidFilter;
   }
}
