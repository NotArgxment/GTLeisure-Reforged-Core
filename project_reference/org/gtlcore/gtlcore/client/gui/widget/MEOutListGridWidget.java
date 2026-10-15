package org.gtlcore.gtlcore.client.gui.widget;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.client.TooltipsHandler;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEConfigSlotWidget;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MEOutListGridWidget extends DraggableScrollableWidgetGroup {
   protected final Object2LongMap<AEKey> list;
   private final int slotAmountY;
   private int slotRowsAmount;
   private static final int TYPE_ITEM = 0;
   private static final int TYPE_FLUID = 1;
   protected static final int ROW_CHANGE_ID = 2;
   protected static final int CONTENT_CHANGE_ID = 3;
   protected final Object2LongMap<AEKey> changeMap = new Object2LongOpenHashMap();
   protected final Object2LongMap<AEKey> cached = new Object2LongOpenHashMap();
   protected final List<GenericStack> displayList = new ObjectArrayList();

   public MEOutListGridWidget(int x, int y, int slotsY, @NotNull Object2LongMap<AEKey> internalList) {
      super(x, y, 158, slotsY * 18);
      this.list = internalList;
      this.slotAmountY = slotsY;
   }

   public GenericStack getAt(int index) {
      return index >= 0 && index < this.displayList.size() ? this.displayList.get(index) : null;
   }

   private void addSlotRows(int amount) {
      for (int i = 0; i < amount; i++) {
         int widgetAmount = this.widgets.size();
         Widget widget = this.createDisplayWidget(0, i * 18, widgetAmount);
         this.addWidget(widget);
      }
   }

   private void removeSlotRows(int amount) {
      for (int i = 0; i < amount; i++) {
         Widget slotWidget = (Widget)this.widgets.remove(this.widgets.size() - 1);
         this.removeWidget(slotWidget);
      }
   }

   private void modifySlotRows(int delta) {
      if (delta > 0) {
         this.addSlotRows(delta);
      } else {
         this.removeSlotRows(delta);
      }
   }

   protected void writeListChange(FriendlyByteBuf buffer) {
      this.changeMap.clear();
      ObjectIterator<Entry<AEKey>> it = Object2LongMaps.fastIterator(this.cached);

      while (it.hasNext()) {
         Entry<AEKey> entry = (Entry<AEKey>)it.next();
         AEKey cachedKey = (AEKey)entry.getKey();
         if (!this.list.containsKey(cachedKey)) {
            this.changeMap.put(cachedKey, -entry.getLongValue());
            it.remove();
         }
      }

      it = Object2LongMaps.fastIterator(this.list);

      while (it.hasNext()) {
         Entry<AEKey> entry = (Entry<AEKey>)it.next();
         AEKey key = (AEKey)entry.getKey();
         long value = entry.getLongValue();
         long cacheValue = this.cached.getOrDefault(key, 0L);
         long delta = value - cacheValue;
         if (cacheValue == 0L) {
            if (value != 0L) {
               this.changeMap.put(key, value);
               this.cached.put(key, value);
            }
         } else if (delta != 0L) {
            this.changeMap.put(key, delta);
            this.cached.put(key, value);
         }
      }

      buffer.m_130130_(this.changeMap.size());
      it = Object2LongMaps.fastIterator(this.changeMap);

      while (it.hasNext()) {
         Entry<AEKey> entry = (Entry<AEKey>)it.next();
         writeAnyKey(buffer, (AEKey)entry.getKey());
         buffer.m_130103_(entry.getLongValue());
      }
   }

   protected void readListChange(FriendlyByteBuf buffer) {
      int size = buffer.m_130242_();

      for (int i = 0; i < size; i++) {
         AEKey key = readAnyKey(buffer);
         long delta = buffer.m_130258_();
         boolean found = false;
         ListIterator<GenericStack> li = this.displayList.listIterator();

         while (li.hasNext()) {
            GenericStack stack = li.next();
            if (stack.what().equals(key)) {
               long newAmount = stack.amount() + delta;
               if (newAmount > 0L) {
                  li.set(new GenericStack(key, newAmount));
               } else {
                  li.remove();
               }

               found = true;
               break;
            }
         }

         if (!found && delta > 0L) {
            this.displayList.add(new GenericStack(key, delta));
         }
      }
   }

   protected Widget createDisplayWidget(int x, int y, int index) {
      return new MEOutListGridWidget.DisplaySlot(x, y, this, index);
   }

   public void detectAndSendChanges() {
      super.detectAndSendChanges();
      int slotRowsRequired = Math.max(this.slotAmountY, this.list.size());
      if (this.slotRowsAmount != slotRowsRequired) {
         int slotsToAdd = slotRowsRequired - this.slotRowsAmount;
         this.slotRowsAmount = slotRowsRequired;
         this.writeUpdateInfo(2, buf -> buf.m_130130_(slotsToAdd));
         this.modifySlotRows(slotsToAdd);
      }

      this.writeUpdateInfo(3, this::writeListChange);
   }

   @OnlyIn(Dist.CLIENT)
   public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
      super.readUpdateInfo(id, buffer);
      if (id == 2) {
         int slotsToAdd = buffer.m_130242_();
         this.modifySlotRows(slotsToAdd);
      }

      if (id == 3) {
         this.readListChange(buffer);
      }
   }

   public void writeInitialData(FriendlyByteBuf buffer) {
      super.writeInitialData(buffer);
      int slotRowsRequired = Math.max(this.slotAmountY, this.list.size());
      int slotsToAdd = slotRowsRequired - this.slotRowsAmount;
      this.slotRowsAmount = slotRowsRequired;
      this.modifySlotRows(slotsToAdd);
      buffer.m_130130_(slotsToAdd);
      this.writeListChange(buffer);
   }

   @OnlyIn(Dist.CLIENT)
   public void readInitialData(FriendlyByteBuf buffer) {
      super.readInitialData(buffer);
      this.modifySlotRows(buffer.m_130242_());
      this.readListChange(buffer);
   }

   private static void writeAnyKey(FriendlyByteBuf buf, AEKey key) {
      if (key instanceof AEItemKey) {
         buf.m_130130_(0);
         key.writeToPacket(buf);
      } else if (key instanceof AEFluidKey) {
         buf.m_130130_(1);
         key.writeToPacket(buf);
      }
   }

   private static AEKey readAnyKey(FriendlyByteBuf buf) {
      int t = buf.m_130242_();

      return (AEKey)(switch (t) {
         case 0 -> AEItemKey.fromPacket(buf);
         case 1 -> AEFluidKey.fromPacket(buf);
         default -> throw new IllegalStateException("Unknown AEKey type id: " + t);
      });
   }

   public static class DisplaySlot extends Widget {
      private final MEOutListGridWidget gridWidget;
      private final int index;

      public DisplaySlot(int x, int y, MEOutListGridWidget gridWidget, int index) {
         super(new Position(x, y), new Size(18, 18));
         this.gridWidget = gridWidget;
         this.index = index;
      }

      public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
         super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
         Position pos = this.getPosition();
         GenericStack gs = this.gridWidget.getAt(this.index);
         if (gs != null && gs.what() instanceof AEFluidKey) {
            GuiTextures.FLUID_SLOT.draw(graphics, mouseX, mouseY, pos.x, pos.y, 18, 18);
         } else {
            GuiTextures.SLOT.draw(graphics, mouseX, mouseY, pos.x, pos.y, 18, 18);
         }

         GuiTextures.NUMBER_BACKGROUND.draw(graphics, mouseX, mouseY, pos.x + 18, pos.y, 140, 18);
         int stackX = pos.x + 1;
         int stackY = pos.y + 1;
         if (gs != null) {
            AEKey key = gs.what();
            long amt = gs.amount();
            if (key instanceof AEItemKey itemKey) {
               ItemStack stackForRender = new ItemStack(itemKey.getItem());
               DrawerHelper.drawItemStack(graphics, stackForRender, stackX, stackY, -1, null);
               String amountStr = String.format("x%,d", amt);
               DrawerHelper.drawText(graphics, amountStr, stackX + 20, stackY + 5, 1.0F, -1);
            } else if (key instanceof AEFluidKey fluidKey) {
               FluidStack fs = FluidStack.create(fluidKey.getFluid(), amt, fluidKey.getTag());
               DrawerHelper.drawFluidForGui(graphics, fs, amt, stackX, stackY, 16, 16);
               String amountStr = String.format("x%,d", amt);
               DrawerHelper.drawText(graphics, amountStr, stackX + 20, stackY + 5, 1.0F, -1);
            }
         }

         if (this.isMouseOverElement(mouseX, mouseY)) {
            AEConfigSlotWidget.drawSelectionOverlay(graphics, stackX, stackY, 16, 16);
         }
      }

      public void drawInForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
         if (this.isMouseOverElement(mouseX, mouseY)) {
            GenericStack gs = this.gridWidget.getAt(this.index);
            if (gs != null) {
               AEKey key = gs.what();
               long amt = gs.amount();
               if (key instanceof AEItemKey) {
                  graphics.m_280153_(Minecraft.m_91087_().f_91062_, GenericStack.wrapInItemStack(gs), mouseX, mouseY);
               } else if (key instanceof AEFluidKey fluidKey) {
                  FluidStack fs = FluidStack.create(fluidKey.getFluid(), amt, fluidKey.getTag());
                  List<Component> tips = new ObjectArrayList();
                  tips.add(fs.getDisplayName());
                  tips.add(Component.m_237113_(String.format("%,d mB", amt)));
                  TooltipsHandler.appendFluidTooltips(fs.getFluid(), fs.getAmount(), tips::add, TooltipFlag.f_256752_);
                  graphics.m_280677_(Minecraft.m_91087_().f_91062_, tips, Optional.empty(), mouseX, mouseY);
               }
            }
         }
      }
   }
}
