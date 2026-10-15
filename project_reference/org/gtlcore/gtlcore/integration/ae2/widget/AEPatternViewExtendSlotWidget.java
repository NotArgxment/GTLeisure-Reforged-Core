package org.gtlcore.gtlcore.integration.ae2.widget;

import com.gregtechceu.gtceu.integration.ae2.gui.widget.slot.AEPatternViewSlotWidget;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget.WidgetSlotItemTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.mojang.blaze3d.platform.InputConstants.Type;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AEPatternViewExtendSlotWidget extends AEPatternViewSlotWidget {
   @Nullable
   private Runnable onMiddleClick;
   @Nullable
   private Runnable onPatternSlotChanged;

   public AEPatternViewExtendSlotWidget(IItemTransfer inventory, int slotIndex, int xPosition, int yPosition) {
      super(inventory, slotIndex, xPosition, yPosition);
   }

   public AEPatternViewExtendSlotWidget setOnMiddleClick(@Nullable Runnable runnable) {
      this.onMiddleClick = runnable;
      return this;
   }

   public AEPatternViewExtendSlotWidget setOnPatternSlotChanged(@Nullable Runnable runnable) {
      this.onPatternSlotChanged = runnable;
      return this;
   }

   protected Slot createSlot(IItemTransfer itemHandler, int index) {
      return new AEPatternViewExtendSlotWidget.ExtendWidgetSlotItemTransfer(itemHandler, index, 0, 0);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.slotReference != null && this.isMouseOverElement(mouseX, mouseY) && this.gui != null) {
         if (button == 2 && this.onMiddleClick != null) {
            this.writeClientAction(10, writer -> writer.writeBoolean(true));
            this.onMiddleClick.run();
            return true;
         }

         ItemStack stack = this.slotReference.m_7993_();
         if (this.canPutItems && stack.m_41619_() || this.canTakeItems && !stack.m_41619_()) {
            ModularUIGuiContainer modularUIGui = this.gui.getModularUIGui();
            boolean last = modularUIGui.getQuickCrafting();
            Key mouseKey = Type.MOUSE.m_84895_(button);
            HOVER_SLOT = this.slotReference;
            this.gui.getModularUIGui().superMouseClicked(mouseX, mouseY, button);
            HOVER_SLOT = null;
            if (last != modularUIGui.getQuickCrafting()) {
               modularUIGui.dragSplittingButton = button;
               if (button == 0) {
                  modularUIGui.dragSplittingLimit = 0;
               } else if (button == 1) {
                  modularUIGui.dragSplittingLimit = 1;
               } else if (Minecraft.m_91087_().f_91066_.f_92097_.m_90830_(mouseKey.m_84873_())) {
                  modularUIGui.dragSplittingLimit = 2;
               }
            }

            return true;
         }
      }

      return false;
   }

   public void handleClientAction(int id, FriendlyByteBuf buffer) {
      super.handleClientAction(id, buffer);
      if (id == 10 && this.onMiddleClick != null) {
         this.onMiddleClick.run();
      }
   }

   protected class ExtendWidgetSlotItemTransfer extends WidgetSlotItemTransfer {
      public ExtendWidgetSlotItemTransfer(IItemTransfer itemHandler, int index, int xPosition, int yPosition) {
         super(AEPatternViewExtendSlotWidget.this, itemHandler, index, xPosition, yPosition);
      }

      public void m_5852_(@Nonnull ItemStack stack) {
         super.m_5852_(stack);
         if (AEPatternViewExtendSlotWidget.this.onPatternSlotChanged != null) {
            AEPatternViewExtendSlotWidget.this.onPatternSlotChanged.run();
         }
      }
   }
}
