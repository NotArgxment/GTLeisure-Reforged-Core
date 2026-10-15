package org.gtlcore.gtlcore.common.machine.multiblock.part;

import appeng.crafting.pattern.EncodedPatternItem;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import java.util.function.Function;
import java.util.function.IntConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.integration.ae2.widget.AEPatternViewExtendSlotWidget;
import org.jetbrains.annotations.Nullable;

public class PaginationUIManager {
   private final int uiWidth;
   private final int uiHeight;
   private final int patternsPerRow;
   private final int rowsPerPage;
   private final int maxPages;
   private final int maxPatternCount;
   private final IItemTransfer patternInventory;
   @DescSynced
   private int currentPageIndex;
   private WidgetGroup paginationUI;
   @Nullable
   private final Function<Integer, Boolean> isCached;
   private final IntConsumer onPatternChange;
   @Nullable
   private IntConsumer onMiddleClicked;

   public PaginationUIManager(
      int patternsPerRow,
      int rowsPerPage,
      int maxPages,
      int uiWidth,
      int uiHeight,
      IntConsumer onPatternChange,
      @Nullable Function<Integer, Boolean> isCached,
      IItemTransfer patternInventory
   ) {
      this.patternsPerRow = patternsPerRow;
      this.rowsPerPage = rowsPerPage;
      this.maxPages = maxPages;
      this.maxPatternCount = patternsPerRow * rowsPerPage * maxPages;
      this.uiWidth = uiWidth;
      this.uiHeight = uiHeight;
      this.onPatternChange = onPatternChange;
      this.isCached = isCached;
      this.patternInventory = patternInventory;
   }

   public PaginationUIManager(
      int patternsPerRow, int rowsPerPage, int totalCount, int uiWidth, int uiHeight, IntConsumer onPatternChange, IItemTransfer patternInventory
   ) {
      this.patternsPerRow = patternsPerRow;
      this.rowsPerPage = rowsPerPage;
      this.maxPatternCount = totalCount;
      this.maxPages = (int)Math.ceil((double)totalCount / (rowsPerPage * patternsPerRow));
      this.uiWidth = uiWidth;
      this.uiHeight = uiHeight;
      this.onPatternChange = onPatternChange;
      this.isCached = null;
      this.patternInventory = patternInventory;
   }

   public WidgetGroup createPaginationUI(@Nullable IntConsumer onMiddleClicked) {
      this.onMiddleClicked = onMiddleClicked;
      WidgetGroup basePage = new WidgetGroup(0, 0, this.uiWidth, this.uiHeight);
      this.paginationUI = this.rebuildPatternSlots(this.currentPageIndex);
      basePage.addWidget(this.paginationUI);
      this.createPageControls(basePage);
      return basePage;
   }

   protected WidgetGroup rebuildPatternSlots(int pageIndex) {
      int startY = 16;
      int patternAreaHeight = this.rowsPerPage * 18;
      int startSlot = pageIndex * this.rowsPerPage * this.patternsPerRow;
      int endSlot = Math.min(startSlot + this.rowsPerPage * this.patternsPerRow, this.maxPatternCount);
      WidgetGroup pageGroup = new WidgetGroup(0, 16, this.uiWidth, patternAreaHeight);
      this.recreatePatternSlots(pageGroup, startSlot, endSlot);
      return pageGroup;
   }

   private void recreatePatternSlots(WidgetGroup pageGroup, int startSlot, int endSlot) {
      for (int i = startSlot; i < endSlot; i++) {
         int finalI = i;
         int slotInPage = i - startSlot;
         int row = slotInPage / this.patternsPerRow;
         int col = slotInPage % this.patternsPerRow;
         int x = this.uiWidth == 106 ? (106 - this.patternsPerRow * 18) / 2 + col * 18 : 8 + col * 18;
         int y = row * 18;
         AEPatternViewExtendSlotWidget slot = (AEPatternViewExtendSlotWidget)new AEPatternViewExtendSlotWidget(this.patternInventory, i, x, y)
            .setOnPatternSlotChanged(() -> this.onPatternChange.accept(finalI))
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
            .setBackground(new IGuiTexture[]{GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY});
         if (this.onMiddleClicked != null) {
            slot.setOnMiddleClick(() -> this.onMiddleClicked.accept(finalI));
         }

         if (this.isCached != null) {
            slot.setOnAddedTooltips((s, l) -> {
               if (this.isCached.apply(finalI)) {
                  l.add(Component.m_237115_("gtceu.machine.pattern.recipe.cache"));
               }
            });
         }

         pageGroup.addWidget(slot);
      }
   }

   protected void createPageControls(WidgetGroup parentGroup) {
      int startY = 16;
      int patternAreaHeight = this.rowsPerPage * 18;
      int pageControlY = startY + patternAreaHeight + 4;
      parentGroup.addWidget(
         new ButtonWidget(8, pageControlY, 30, 12, new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture("<<")}), clickData -> {
            if (this.currentPageIndex > 0) {
               this.currentPageIndex--;
               this.refreshPage(this.currentPageIndex);
            }
         })
      );
      int pageIndicatorWidth = 12;
      int pageIndicatorX = (this.uiWidth - pageIndicatorWidth) / 2;
      parentGroup.addWidget(new LabelWidget(pageIndicatorX, pageControlY + 2, () -> this.currentPageIndex + 1 + " / " + this.maxPages));
      parentGroup.addWidget(
         new ButtonWidget(
            this.uiWidth - 38, pageControlY, 30, 12, new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture(">>")}), clickData -> {
               if (this.currentPageIndex < this.maxPages - 1) {
                  this.currentPageIndex++;
                  this.refreshPage(this.currentPageIndex);
               }
            }
         )
      );
   }

   public void refreshPage(int pageIndex) {
      this.paginationUI.clearAllWidgets();
      int startSlot = pageIndex * this.rowsPerPage * this.patternsPerRow;
      int endSlot = Math.min(startSlot + this.rowsPerPage * this.patternsPerRow, this.maxPatternCount);
      this.recreatePatternSlots(this.paginationUI, startSlot, endSlot);
   }

   public int getUiWidth() {
      return this.uiWidth;
   }

   public int getUiHeight() {
      return this.uiHeight;
   }

   public int getPatternsPerRow() {
      return this.patternsPerRow;
   }

   public int getRowsPerPage() {
      return this.rowsPerPage;
   }

   public int getMaxPages() {
      return this.maxPages;
   }

   public int getMaxPatternCount() {
      return this.maxPatternCount;
   }
}
