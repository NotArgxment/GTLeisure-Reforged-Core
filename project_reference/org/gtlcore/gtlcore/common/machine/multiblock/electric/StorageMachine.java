package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Size;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StorageMachine extends WorkableElectricMultiblockMachine implements IMachineModifyDrops {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      StorageMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   public final NotifiableItemStackHandler machineStorage;

   public StorageMachine(IMachineBlockEntity holder, int slot) {
      super(holder, new Object[0]);
      this.machineStorage = this.createMachineStorage(slot);
   }

   protected NotifiableItemStackHandler createMachineStorage(int value) {
      NotifiableItemStackHandler handler = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH, slots -> new ItemStackTransfer(1) {
         public int getSlotLimit(int slot) {
            return value;
         }
      });
      handler.setFilter(this::filter);
      return handler;
   }

   public void onDrops(List<ItemStack> drops) {
      this.clearInventory(this.machineStorage.storage);
   }

   protected boolean filter(@NotNull ItemStack itemStack) {
      return true;
   }

   @NotNull
   public Widget createUIWidget() {
      Widget widget = super.createUIWidget();
      if (widget instanceof WidgetGroup group) {
         Size size = group.getSize();
         group.addWidget(
            new SlotWidget(this.machineStorage.storage, 0, size.width - 30, size.height - 30, true, true).setBackground(new IGuiTexture[]{GuiTextures.SLOT})
         );
      }

      return widget;
   }

   public ItemStack getMachineStorageItem() {
      return this.machineStorage.getStackInSlot(0);
   }

   public void setMachineStorageItem(ItemStack item) {
      this.machineStorage.storage.setStackInSlot(0, item);
   }

   public boolean isEmpty() {
      return this.machineStorage.isEmpty();
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }
}
