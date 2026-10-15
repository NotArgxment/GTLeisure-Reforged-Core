package org.gtlcore.gtlcore.api.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.misc.FluidTransferList;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MEPatternCatalystUIManager extends WidgetGroup {
   static final int SLOT_SIZE = 18;
   static final int PAD_OUT = 8;
   static final int PAD_IN = 4;
   static final int MAX_COLS = 9;
   private static final int[] OPTIMAL_COLUMNS = new int[10];
   protected double lastDeltaX;
   protected double lastDeltaY;
   protected int dragOffsetX;
   protected int dragOffsetY;
   protected boolean isDragging;
   private int lastIndex = -1;
   private final IItemTransfer[] itemTransfers;
   private final FluidTransferList[] fluidTankTransfers;
   private final byte[] cacheRecipeCount;
   private final IntConsumer onCacheCountChange;

   public MEPatternCatalystUIManager(
      int dockX, IItemTransfer[] itemTransfers, FluidTransferList[] fluidTankTransfers, byte[] cacheRecipeCount, IntConsumer onCacheCountChange
   ) {
      super(dockX, 16, 16, 16);
      this.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND});
      this.setVisible(false).setActive(false);
      this.itemTransfers = itemTransfers;
      this.fluidTankTransfers = fluidTankTransfers;
      this.cacheRecipeCount = cacheRecipeCount;
      this.onCacheCountChange = onCacheCountChange;
   }

   public void toggleFor(int index) {
      if (this.lastIndex == index) {
         this.setVisible(!this.isVisible()).setActive(!this.isActive());
      } else {
         this.show(index, this.itemTransfers[index], this.fluidTankTransfers[index]);
      }
   }

   private void show(int index, IItemTransfer itemInventory, FluidTransferList tanks) {
      this.clearAllWidgets();
      int itemSlots = itemInventory != null ? itemInventory.getSlots() : 0;
      int fluidTanks = tanks != null ? tanks.transfers.length : 0;
      int currentY = 3;
      int maxWidth = 0;
      Widget cacheCountWidget = createCacheCountInputWidget(() -> Integer.valueOf(this.cacheRecipeCount[index]), value -> {
         if (this.cacheRecipeCount[index] != value.byteValue()) {
            this.cacheRecipeCount[index] = value.byteValue();
            this.onCacheCountChange.accept(index);
         }
      });
      cacheCountWidget.setSelfPosition(0, currentY);
      this.addWidget(cacheCountWidget);
      currentY += cacheCountWidget.getSize().height + 4;
      maxWidth = Math.max(maxWidth, cacheCountWidget.getSize().width + 8);
      if (itemSlots > 0) {
         Widget itemContainer = createInventoryContainer(itemInventory, itemSlots);
         itemContainer.setSelfPosition(0, currentY);
         this.addWidget(itemContainer);
         currentY += itemContainer.getSize().height;
         maxWidth = Math.max(maxWidth, itemContainer.getSize().width);
      }

      if (fluidTanks > 0) {
         Widget fluidContainer = createFluidContainer(tanks, fluidTanks);
         fluidContainer.setSelfPosition(0, currentY);
         this.addWidget(fluidContainer);
         currentY += fluidContainer.getSize().height;
         maxWidth = Math.max(maxWidth, fluidContainer.getSize().width);
      }

      if (maxWidth == 0) {
         maxWidth = 16;
      }

      if (currentY <= 0) {
         currentY = 16;
      }

      this.setSize(maxWidth, currentY);
      this.setVisible(true).setActive(true);
      this.lastIndex = index;
   }

   @NotNull
   private static Widget createCacheCountInputWidget(Supplier<Integer> supplier, Consumer<Integer> consumer) {
      WidgetGroup group = new WidgetGroup(new Position(0, 0));
      group.addWidget(new LabelWidget(8, 2, "\u5b9e\u9645\u914d\u65b9\u6570\u91cf"));
      group.addWidget(new IntInputWidget(new Position(8, 12), supplier, consumer).setMin(1).setMax(127));
      return group;
   }

   @NotNull
   private static Widget createInventoryContainer(IItemTransfer inventory, int slots) {
      int cols = calculateOptimalColumns(slots);
      int rows = (slots + cols - 1) / cols;
      int containerW = 8 + cols * 18;
      int containerH = 8 + rows * 18;
      int groupW = 16 + containerW;
      int groupH = 16 + containerH;
      WidgetGroup group = new WidgetGroup(0, 0, groupW, groupH);
      group.addWidget(new LabelWidget(8, 2, "\u7269\u54c1\u50ac\u5316\u5242\u69fd"));
      WidgetGroup container = new WidgetGroup(8, 12, containerW, containerH);
      int index = 0;

      for (int y = 0; y < rows && index < slots; y++) {
         for (int x = 0; x < cols && index < slots; x++) {
            int sx = 4 + x * 18;
            int sy = 4 + y * 18;
            container.addWidget(createSlotWidget(inventory, index++, sx, sy));
         }
      }

      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      group.addWidget(container);
      return group;
   }

   @NotNull
   private static Widget createFluidContainer(FluidTransferList tanks, int slots) {
      int cols = calculateOptimalColumns(slots);
      int rows = (slots + cols - 1) / cols;
      int containerW = 8 + cols * 18;
      int containerH = 8 + rows * 18;
      int groupW = 16 + containerW;
      int groupH = 16 + containerH;
      WidgetGroup group = new WidgetGroup(0, 0, groupW, groupH);
      group.addWidget(new LabelWidget(8, 2, "\u6d41\u4f53\u50ac\u5316\u5242\u69fd"));
      WidgetGroup container = new WidgetGroup(8, 12, containerW, containerH);
      int index = 0;

      for (int y = 0; y < rows && index < slots; y++) {
         for (int x = 0; x < cols && index < slots; x++) {
            int sx = 4 + x * 18;
            int sy = 4 + y * 18;
            container.addWidget(createTankWidget(tanks.transfers[index], sx, sy));
            index++;
         }
      }

      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      group.addWidget(container);
      return group;
   }

   private static int calculateOptimalColumns(int slots) {
      if (slots <= 0) {
         return 1;
      } else {
         return slots <= 9 ? OPTIMAL_COLUMNS[slots] : 9;
      }
   }

   private static int calculateOptimalColumnsInternal(int slots) {
      if (slots <= 0) {
         return 1;
      }

      int bestCols = 1;
      double bestRatio = 1.7976931348623157E308;

      for (int cols = 1; cols <= Math.min(slots, 9); cols++) {
         int rows = (slots + cols - 1) / cols;
         double ratio = Math.abs((double)cols / rows - 1.0);
         if (ratio < bestRatio) {
            bestRatio = ratio;
            bestCols = cols;
         }
      }

      return bestCols;
   }

   @NotNull
   private static Widget createSlotWidget(IItemTransfer inventory, int slotIndex, int sx, int sy) {
      return new SlotWidget(inventory, slotIndex, sx, sy, true, true).setBackgroundTexture(GuiTextures.SLOT).setIngredientIO(IngredientIO.INPUT);
   }

   @NotNull
   private static Widget createTankWidget(IFluidTransfer storage, int sx, int sy) {
      return new TankWidget(storage, 0, sx, sy, true, true).setBackground(GuiTextures.FLUID_SLOT);
   }

   private boolean isMouseover(int x, int y, int width, int height, double mouseX, double mouseY) {
      boolean b = mouseX >= x && mouseY >= y && x + width > mouseX && y + height > mouseY;
      if (!b) {
         return false;
      }

      for (Widget ui : this.widgets) {
         if (ui instanceof WidgetGroup wg) {
            for (Widget u : wg.widgets) {
               if (u instanceof WidgetGroup w) {
                  int pX = w.getPositionX();
                  int pY = w.getPositionY();
                  int pW = w.getSizeWidth();
                  int pH = w.getSizeHeight();
                  b = !(mouseX >= pX) || !(mouseY >= pY) || !(pX + pW > mouseX) || !(pY + pH > mouseY);
                  if (!b) {
                     return b;
                  }
               }
            }
         }
      }

      return b;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.lastDeltaX = 0.0;
      this.lastDeltaY = 0.0;
      this.isDragging = false;
      if (this.isMouseover(this.getPositionX(), this.getPositionY(), this.getSizeWidth(), this.getSizeHeight(), mouseX, mouseY)) {
         this.isDragging = true;
         return true;
      } else {
         return super.mouseClicked(mouseX, mouseY, button) || this.isMouseOverElement(mouseX, mouseY);
      }
   }

   @OnlyIn(Dist.CLIENT)
   public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
      double dx = dragX + this.lastDeltaX;
      double dy = dragY + this.lastDeltaY;
      dragX = (int)dx;
      dragY = (int)dy;
      this.lastDeltaX = dx - dragX;
      this.lastDeltaY = dy - dragY;
      if (this.isDragging) {
         this.dragOffsetX += (int)dragX;
         this.dragOffsetY += (int)dragY;
         this.addSelfPosition((int)dragX, (int)dragY);
      }

      return super.mouseDragged(mouseX, mouseY, button, dragX, dragY) || this.isMouseOverElement(mouseX, mouseY);
   }

   @OnlyIn(Dist.CLIENT)
   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      this.lastDeltaX = 0.0;
      this.lastDeltaY = 0.0;
      this.isDragging = false;
      return super.mouseReleased(mouseX, mouseY, button) || this.isMouseOverElement(mouseX, mouseY);
   }

   static {
      for (int slots = 1; slots <= 9; slots++) {
         OPTIMAL_COLUMNS[slots] = calculateOptimalColumnsInternal(slots);
      }
   }
}
