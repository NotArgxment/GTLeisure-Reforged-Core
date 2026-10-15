package org.gtlcore.gtlcore.client.gui.widget;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AmountFormat;
import appeng.api.stacks.GenericStack;
import com.google.common.collect.Lists;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.misc.IGhostFluidTarget;
import com.gregtechceu.gtceu.api.gui.misc.IGhostItemTarget;
import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;
import com.gregtechceu.gtceu.integration.ae2.utils.AEUtil;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.ingredient.Target;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.PhantomFluidWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;

public class AEDualConfigSlotWidget extends Widget implements IGhostItemTarget, IGhostFluidTarget {
   protected AEDualConfigWidget parentWidget;
   protected int index;
   protected static final int REMOVE_ID = 1000;
   protected static final int ITEM_UPDATE_ID = 1001;
   protected static final int FLUID_UPDATE_ID = 1002;
   protected boolean select = false;

   public AEDualConfigSlotWidget(int x, int y, AEDualConfigWidget widget, int index) {
      super(new Position(x, y), new Size(18, 36));
      this.parentWidget = widget;
      this.index = index;
   }

   protected int getIndex() {
      return (this.parentWidget.page - 1) * 16 + this.index;
   }

   @OnlyIn(Dist.CLIENT)
   public void drawInForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
      IConfigurableSlot slot = this.parentWidget.getDisplay(this.getIndex());
      if (slot.getConfig() == null) {
         if (this.mouseOverConfig(mouseX, mouseY)) {
            List<Component> hoverStringList = new ArrayList<>();
            hoverStringList.add(Component.m_237115_("gtceu.gui.config_slot"));
            if (this.parentWidget.isAutoPull()) {
               hoverStringList.add(Component.m_237115_("gtceu.gui.config_slot.auto_pull_managed"));
            } else {
               hoverStringList.add(Component.m_237115_("gtceu.gui.config_slot.set_only"));
               hoverStringList.add(Component.m_237115_("gtceu.gui.config_slot.remove"));
               hoverStringList.add(Component.m_237115_("gtceu.gui.config_slot.remove"));
            }

            graphics.m_280677_(Minecraft.m_91087_().f_91062_, hoverStringList, Optional.empty(), mouseX, mouseY);
         }
      } else {
         GenericStack item = null;
         if (this.mouseOverConfig(mouseX, mouseY)) {
            item = slot.getConfig();
         } else if (this.mouseOverStock(mouseX, mouseY)) {
            item = slot.getStock();
         }

         if (item != null) {
            graphics.m_280153_(Minecraft.m_91087_().f_91062_, GenericStack.wrapInItemStack(item), mouseX, mouseY);
         }
      }
   }

   protected boolean mouseOverConfig(double mouseX, double mouseY) {
      Position position = this.getPosition();
      return isMouseOver(position.x, position.y, 18, 18, mouseX, mouseY);
   }

   protected boolean mouseOverStock(double mouseX, double mouseY) {
      Position position = this.getPosition();
      return isMouseOver(position.x, position.y + 18, 18, 18, mouseX, mouseY);
   }

   @OnlyIn(Dist.CLIENT)
   public static void drawSelectionOverlay(GuiGraphics graphics, int x, int y, int width, int height) {
      RenderSystem.disableDepthTest();
      RenderSystem.colorMask(true, true, true, false);
      DrawerHelper.drawGradientRect(graphics, x, y, width, height, -2130706433, -2130706433);
      RenderSystem.colorMask(true, true, true, true);
      RenderSystem.enableDepthTest();
      RenderSystem.enableBlend();
   }

   protected boolean isStackValidForSlot(GenericStack stack) {
      return stack != null && stack.amount() >= 0L ? this.parentWidget.hasStackInConfig(stack) : false;
   }

   @OnlyIn(Dist.CLIENT)
   public void drawInBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
      super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
      Position position = this.getPosition();
      IConfigurableSlot slot = this.parentWidget.getDisplay(this.getIndex());
      GenericStack config = slot.getConfig();
      GenericStack stock = slot.getStock();
      this.drawSlots(graphics, mouseX, mouseY, position.x, position.y);
      if (this.select) {
         GuiTextures.SELECT_BOX.draw(graphics, mouseX, mouseY, position.x, position.y, 18, 18);
      }

      int stackX = position.x + 1;
      int stackY = position.y + 1;
      if (config != null) {
         if (config.what() instanceof AEItemKey itemKey) {
            ItemStack stack = new ItemStack(itemKey.getItem(), (int)config.amount());
            stack.m_41764_(1);
            DrawerHelper.drawItemStack(graphics, stack, stackX, stackY, -1, null);
         } else if (config.what() instanceof AEFluidKey) {
            FluidStack stack = AEUtil.toFluidStack(config);
            if (!stack.isEmpty()) {
               DrawerHelper.drawFluidForGui(graphics, stack, config.amount(), stackX, stackY, 16, 16);
            }
         }
      }

      if (stock != null) {
         if (stock.what() instanceof AEItemKey itemKey) {
            ItemStack stack = new ItemStack(itemKey.getItem(), (int)stock.amount());
            stack.m_41764_(1);
            DrawerHelper.drawItemStack(graphics, stack, stackX, stackY + 18, -1, null);
         } else if (stock.what() instanceof AEFluidKey) {
            FluidStack stack = AEUtil.toFluidStack(stock);
            if (!stack.isEmpty()) {
               DrawerHelper.drawFluidForGui(graphics, stack, stock.amount(), stackX, stackY + 18, 16, 16);
            }
         }

         if (stock.amount() > 0L) {
            DrawerHelper.drawStringFixedCorner(
               graphics, stock.what().formatAmount(stock.amount(), AmountFormat.SLOT), stackX + 17, stackY + 18 + 17, 16777215, true, 0.5F
            );
         }

         if (this.mouseOverConfig(mouseX, mouseY)) {
            drawSelectionOverlay(graphics, stackX, stackY, 16, 16);
         } else if (this.mouseOverStock(mouseX, mouseY)) {
            drawSelectionOverlay(graphics, stackX, stackY + 18, 16, 16);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   private void drawSlots(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {
      if (this.parentWidget.isAutoPull()) {
         GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
         GuiTextures.CONFIG_ARROW_DARK.draw(graphics, mouseX, mouseY, x, y, 18, 18);
      } else {
         GuiTextures.FLUID_SLOT.draw(graphics, mouseX, mouseY, x, y, 18, 18);
         GuiTextures.CONFIG_ARROW.draw(graphics, mouseX, mouseY, x, y, 18, 18);
      }

      GuiTextures.SLOT_DARK.draw(graphics, mouseX, mouseY, x, y + 18, 18, 18);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.mouseOverConfig(mouseX, mouseY)) {
         if (this.parentWidget.isAutoPull()) {
            return false;
         }

         if (button == 1) {
            this.writeClientAction(1000, buf -> {});
         } else if (button == 0) {
            ItemStack item = this.gui.getModularUIContainer().m_142621_();
            FluidStack fluid = FluidTransferHelper.getFluidContained(item);
            if (fluid != null) {
               this.writeClientAction(1002, fluid::writeToBuf);
            } else if (!item.m_41619_()) {
               this.writeClientAction(1001, buf -> buf.m_130055_(item));
            }
         }
      }

      return false;
   }

   public void handleClientAction(int id, FriendlyByteBuf buffer) {
      super.handleClientAction(id, buffer);
      int index = this.getIndex();
      IConfigurableSlot slot = this.parentWidget.getConfig(index);
      if (id == 1000) {
         slot.setConfig(null);
         this.writeUpdateInfo(1000, buf -> {});
      }

      if (id == 1001) {
         ItemStack item = buffer.m_130267_();
         GenericStack stack = GenericStack.fromItemStack(item);
         if (this.isStackValidForSlot(stack)) {
            return;
         }

         this.parentWidget.setItemConfig(index, stack);
         if (!item.m_41619_()) {
            this.writeUpdateInfo(1001, buf -> buf.m_130055_(item));
         }
      }

      if (id == 1002) {
         FluidStack fluid = FluidStack.readFromBuf(buffer);
         GenericStack stack = AEUtil.fromFluidStack(fluid);
         if (this.isStackValidForSlot(stack)) {
            return;
         }

         this.parentWidget.setFluidConfig(index, stack);
         if (fluid != FluidStack.empty()) {
            this.writeUpdateInfo(1002, fluid::writeToBuf);
         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
      super.readUpdateInfo(id, buffer);
      int index = this.getIndex();
      IConfigurableSlot slot = this.parentWidget.getDisplay(index);
      if (id == 1000) {
         slot.setConfig(null);
      }

      if (id == 1001) {
         ItemStack item = buffer.m_130267_();
         this.parentWidget.setItemConfig(index, new GenericStack(AEItemKey.of(item.m_41720_(), item.m_41783_()), item.m_41613_()));
      }

      if (id == 1002) {
         FluidStack fluid = FluidStack.create(Registries.getFluid(buffer.m_130281_()), buffer.m_130258_());
         this.parentWidget.setFluidConfig(index, new GenericStack(AEFluidKey.of(fluid.getFluid()), fluid.getAmount()));
      }
   }

   @OnlyIn(Dist.CLIENT)
   public Rect2i getRectangleBox() {
      Rect2i rectangle = this.toRectangleBox();
      rectangle.m_173058_(rectangle.m_110091_() / 2);
      return rectangle;
   }

   @OnlyIn(Dist.CLIENT)
   public List<Target> getPhantomTargets(Object ingredient) {
      Object result = this.convertIngredient(ingredient);
      final Rect2i rectangle = this.getRectangleBox();
      if (result instanceof ItemStack) {
         return Lists.newArrayList(new Target[]{new Target() {
            @NotNull
            public Rect2i getArea() {
               return rectangle;
            }

            public void accept(Object ingredient) {
               if (AEDualConfigSlotWidget.this.convertIngredientItem(ingredient) instanceof ItemStack stack) {
                  AEDualConfigSlotWidget.this.acceptItem(stack);
               }
            }
         }});
      } else {
         return result instanceof FluidStack && PhantomFluidWidget.drainFrom(ingredient) != null ? Lists.newArrayList(new Target[]{new Target() {
            @NotNull
            public Rect2i getArea() {
               return rectangle;
            }

            public void accept(@NotNull Object ingredient) {
               ingredient = AEDualConfigSlotWidget.this.convertIngredientFluid(ingredient);
               FluidStack ingredientStack;
               if (ingredient instanceof FluidStack fluidStack) {
                  ingredientStack = fluidStack;
               } else {
                  ingredientStack = PhantomFluidWidget.drainFrom(ingredient);
               }

               if (ingredientStack != null) {
                  AEDualConfigSlotWidget.this.acceptFluid(ingredientStack);
               }
            }
         }}) : Collections.emptyList();
      }
   }

   @OnlyIn(Dist.CLIENT)
   public Object convertIngredient(Object ingredient) {
      Object result = super.convertIngredient(ingredient);
      return result instanceof ItemStack stack && !stack.m_41619_() ? result : this.convertIngredientFluid(ingredient);
   }

   @OnlyIn(Dist.CLIENT)
   protected Object convertIngredientItem(Object ingredient) {
      return super.convertIngredient(ingredient);
   }

   @OnlyIn(Dist.CLIENT)
   protected Object convertIngredientFluid(Object ingredient) {
      return LDLib.isJeiLoaded()
            && ingredient instanceof ITypedIngredient<?> type
            && type.getIngredient() instanceof net.minecraftforge.fluids.FluidStack fluidStack
         ? FluidStack.create(fluidStack.getFluid(), fluidStack.getAmount(), fluidStack.getTag())
         : super.convertIngredient(ingredient);
   }

   @OnlyIn(Dist.CLIENT)
   public void acceptItem(ItemStack itemStack) {
      this.writeClientAction(1001, buf -> buf.m_130055_(itemStack));
   }

   @OnlyIn(Dist.CLIENT)
   public void acceptFluid(FluidStack fluidStack) {
      if (fluidStack.getRawFluid() != Fluids.f_76191_ && fluidStack.getAmount() <= 0L) {
         fluidStack.setAmount(1000L);
      }

      if (!fluidStack.isEmpty()) {
         this.writeClientAction(1002, fluidStack::writeToBuf);
      }
   }

   public void setSelect(boolean select) {
      this.select = select;
   }
}
