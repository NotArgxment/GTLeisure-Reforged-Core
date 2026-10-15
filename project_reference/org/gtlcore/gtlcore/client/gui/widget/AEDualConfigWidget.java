package org.gtlcore.gtlcore.client.gui.widget;

import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEConfigSlotWidget;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEFluidSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemList;
import com.gregtechceu.gtceu.integration.ae2.slot.ExportOnlyAEItemSlot;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.common.machine.multiblock.part.MEDualHatchStockPartMachine;

public class AEDualConfigWidget extends WidgetGroup {
   protected final IConfigurableSlot[] config;
   protected Int2ObjectMap<IConfigurableSlot> changeMap = new Int2ObjectOpenHashMap();
   protected IConfigurableSlot[] cached;
   protected IConfigurableSlot[] displayList;
   protected ButtonWidget pageUpWidget;
   protected ButtonWidget pageDownWidget;
   private final ExportOnlyAEItemList aeItem;
   private final ExportOnlyAEFluidList aeFluid;
   private final MEDualHatchStockPartMachine machine;
   public static final int CONFIG_SIZE = 16;
   protected int page;
   protected final int MAX_PAGE;
   private final int offset;
   private static final int UPDATE_ID = 1001;

   public AEDualConfigWidget(int x, int y, ExportOnlyAEItemList aeItem, ExportOnlyAEFluidList aeFluid, MEDualHatchStockPartMachine machine, int page) {
      super(new Position(x, y), new Size(144, 93));
      this.aeItem = aeItem;
      this.aeFluid = aeFluid;
      this.machine = machine;
      this.offset = aeItem.getSize();
      this.page = page;
      this.MAX_PAGE = Math.max(1, this.offset / 16);
      this.config = this.marge();
      this.init();
      this.pageUpWidget = new ButtonWidget(3, 77, 16, 16, GuiTextures.BUTTON_LEFT, this::pageUp);
      this.pageDownWidget = new ButtonWidget(125, 77, 16, 16, GuiTextures.BUTTON_RIGHT, this::pageDown);
      this.addWidget(new LabelWidget(66, 82, () -> this.page + " / " + this.MAX_PAGE));
      this.addWidget(this.pageUpWidget);
      this.addWidget(this.pageDownWidget);
   }

   protected void pageUp(ClickData data) {
      if (this.page >= 2) {
         this.page--;
         this.machine.setPage(this.page);
      }
   }

   protected void pageDown(ClickData data) {
      if (this.page < this.MAX_PAGE) {
         this.page++;
         this.machine.setPage(this.page);
      }
   }

   protected boolean isAutoPull() {
      return this.aeFluid.isAutoPull() || this.aeItem.isAutoPull();
   }

   private IConfigurableSlot[] marge() {
      ExportOnlyAEItemSlot[] itemSlots = this.aeItem.getInventory();
      ExportOnlyAEFluidSlot[] fluidSlots = this.aeFluid.getInventory();
      IConfigurableSlot[] result = new IConfigurableSlot[itemSlots.length + fluidSlots.length];
      System.arraycopy(itemSlots, 0, result, 0, itemSlots.length);
      System.arraycopy(fluidSlots, 0, result, itemSlots.length, fluidSlots.length);
      return result;
   }

   protected final void init() {
      this.displayList = new IConfigurableSlot[this.config.length];
      this.cached = new IConfigurableSlot[this.config.length];
      int half = this.config.length / 2;

      for (int index = 0; index < this.config.length; index++) {
         this.displayList[index] = (IConfigurableSlot)(index < half ? new ExportOnlyAEItemSlot() : new ExportOnlyAEFluidSlot());
         this.cached[index] = (IConfigurableSlot)(index < half ? new ExportOnlyAEItemSlot() : new ExportOnlyAEFluidSlot());
         int line = index / 8;
         if (line < 2) {
            this.addWidget(new AEDualConfigSlotWidget((index - line * 8) * 18, line * 38, this, index));
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      for (Widget w : this.widgets) {
         if (w instanceof AEConfigSlotWidget slot) {
            slot.setSelect(false);
         }
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();
      this.changeMap.clear();

      for (int index = 0; index < this.config.length; index++) {
         IConfigurableSlot newSlot = this.config[index];
         IConfigurableSlot oldSlot = this.cached[index];
         GenericStack nConfig = newSlot.getConfig();
         GenericStack nStock = newSlot.getStock();
         GenericStack oConfig = oldSlot.getConfig();
         GenericStack oStock = oldSlot.getStock();
         if (!this.areAEStackCountsEqual(nConfig, oConfig) || !this.areAEStackCountsEqual(nStock, oStock)) {
            this.changeMap.put(index, newSlot.copy());
            this.cached[index] = this.config[index].copy();
            this.gui.holder.markAsDirty();
         }
      }

      if (!this.changeMap.isEmpty()) {
         this.writeUpdateInfo(1001, buf -> {
            buf.m_130130_(this.changeMap.size());
            IntIterator var2x = this.changeMap.keySet().iterator();

            while (var2x.hasNext()) {
               int indexx = (Integer)var2x.next();
               GenericStack sConfig = ((IConfigurableSlot)this.changeMap.get(indexx)).getConfig();
               GenericStack sStock = ((IConfigurableSlot)this.changeMap.get(indexx)).getStock();
               buf.m_130130_(indexx);
               if (sConfig != null) {
                  buf.writeBoolean(true);
                  GenericStack.writeBuffer(sConfig, buf);
               } else {
                  buf.writeBoolean(false);
               }

               if (sStock != null) {
                  buf.writeBoolean(true);
                  GenericStack.writeBuffer(sStock, buf);
               } else {
                  buf.writeBoolean(false);
               }
            }
         });
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
      super.readUpdateInfo(id, buffer);
      if (id == 1001) {
         int size = buffer.m_130242_();

         for (int i = 0; i < size; i++) {
            int index = buffer.m_130242_();
            IConfigurableSlot slot = this.displayList[index];
            if (buffer.readBoolean()) {
               slot.setConfig(GenericStack.readBuffer(buffer));
            } else {
               slot.setConfig(null);
            }

            if (buffer.readBoolean()) {
               slot.setStock(GenericStack.readBuffer(buffer));
            } else {
               slot.setStock(null);
            }
         }
      }
   }

   public boolean hasStackInConfig(GenericStack stack) {
      return this.aeItem.hasStackInConfig(stack, true) || this.aeFluid.hasStackInConfig(stack, true);
   }

   public final void setItemConfig(int index, GenericStack stack) {
      IConfigurableSlot fluidConfig = this.getFluidConfig(index);
      if (fluidConfig != null) {
         fluidConfig.setConfig(null);
      }

      this.getItemConfig(index).setConfig(stack);
   }

   public final void setFluidConfig(int index, GenericStack stack) {
      IConfigurableSlot itemConfig = this.getItemConfig(index);
      if (itemConfig != null) {
         itemConfig.setConfig(null);
      }

      this.getFluidConfig(index).setConfig(stack);
   }

   public final IConfigurableSlot getItemConfig(int index) {
      return this.config[index];
   }

   public final IConfigurableSlot getFluidConfig(int index) {
      return this.config[index + this.offset];
   }

   public final IConfigurableSlot getConfig(int index) {
      IConfigurableSlot c = this.getItemConfig(index);
      return c.getConfig() == null ? this.getFluidConfig(index) : c;
   }

   public final IConfigurableSlot getDisplay(int index) {
      IConfigurableSlot c = this.displayList[index];
      return c.getConfig() == null ? this.displayList[index + this.offset] : c;
   }

   protected final boolean areAEStackCountsEqual(GenericStack s1, GenericStack s2) {
      if (s2 == s1) {
         return true;
      } else {
         return s1 != null && s2 != null ? s1.amount() == s2.amount() && s1.what().matches(s2) : false;
      }
   }
}
