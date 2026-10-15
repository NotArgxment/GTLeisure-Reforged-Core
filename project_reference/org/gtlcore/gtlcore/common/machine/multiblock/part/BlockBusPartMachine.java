package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.side.item.ItemTransferHelper;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockBusPartMachine extends TieredIOPartMachine implements IMachineLife {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BlockBusPartMachine.class, TieredIOPartMachine.MANAGED_FIELD_HOLDER);
   @Persisted
   private final NotifiableItemStackHandler inventory = this.createInventoryItemHandler();
   protected TickableSubscription autoIOSubs;

   public BlockBusPartMachine(IMachineBlockEntity holder) {
      super(holder, 6, IO.BOTH);
   }

   protected NotifiableItemStackHandler createInventoryItemHandler() {
      NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 81, IO.BOTH, IO.BOTH);
      storage.setFilter(i -> i.m_41720_() instanceof BlockItem);
      return storage;
   }

   protected void updateInventorySubscription() {
      if (this.getLevel() != null) {
         if (this.isWorkingEnabled()
            && !this.getInventory().isEmpty()
            && ItemTransferHelper.getItemTransfer(this.getLevel(), this.getPos().m_121945_(this.getFrontFacing()), this.getFrontFacing().m_122424_()) != null) {
            this.autoIOSubs = this.subscribeServerTick(this.autoIOSubs, this::autoIO);
         } else if (this.autoIOSubs != null) {
            this.autoIOSubs.unsubscribe();
            this.autoIOSubs = null;
         }
      }
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void onMachineRemoved() {
      this.clearInventory(this.getInventory().storage);
   }

   public Widget createUIWidget() {
      int rowSize = 9;
      WidgetGroup group = new WidgetGroup(0, 0, 18 * rowSize + 16, 18 * rowSize + 16);
      WidgetGroup container = new WidgetGroup(4, 4, 18 * rowSize + 8, 18 * rowSize + 8);
      int index = 0;

      for (int y = 0; y < rowSize; y++) {
         for (int x = 0; x < rowSize; x++) {
            container.addWidget(
               new SlotWidget(this.getInventory().storage, index++, 4 + x * 18, 4 + y * 18, true, this.io.support(IO.IN))
                  .setBackgroundTexture(GuiTextures.SLOT)
                  .setIngredientIO(this.io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT)
            );
         }
      }

      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      group.addWidget(container);
      return group;
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().m_6937_(new TickTask(0, this::updateInventorySubscription));
      }
   }

   public void onNeighborChanged(Block block, BlockPos fromPos, boolean isMoving) {
      super.onNeighborChanged(block, fromPos, isMoving);
      this.updateInventorySubscription();
   }

   public void onRotated(Direction oldFacing, Direction newFacing) {
      super.onRotated(oldFacing, newFacing);
      this.updateInventorySubscription();
   }

   public void setWorkingEnabled(boolean workingEnabled) {
      super.setWorkingEnabled(workingEnabled);
      this.updateInventorySubscription();
   }

   public void autoIO() {
      if (this.getOffsetTimer() % 5L == 0L) {
         if (this.isWorkingEnabled()) {
            this.getInventory().exportToNearby(new Direction[]{this.getFrontFacing()});
         }

         this.updateInventorySubscription();
      }
   }

   public NotifiableItemStackHandler getInventory() {
      return this.inventory;
   }

   public TickableSubscription getAutoIOSubs() {
      return this.autoIOSubs;
   }
}
