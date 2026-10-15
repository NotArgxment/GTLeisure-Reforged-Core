package org.gtlcore.gtlcore.integration.jei;

import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.utils.Position;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IIngredientConsumer;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.inputs.RecipeSlotUnderMouse;
import mezz.jei.api.gui.widgets.ISlottedRecipeWidget;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SlotRecipeWidget implements ISlottedRecipeWidget {
   public final IRecipeIngredientSlot slot;
   public final IRecipeSlotDrawable jeiSlot;
   public final Position position;

   public SlotRecipeWidget(IRecipeIngredientSlot slot, IRecipeSlotDrawable jeiSlot) {
      this.slot = slot;
      this.jeiSlot = jeiSlot;
      this.position = new Position(slot.self().getPositionX(), slot.self().getPositionY());
   }

   public Optional<RecipeSlotUnderMouse> getSlotUnderMouse(double mouseX, double mouseY) {
      return this.slot.self().isMouseOverElement(mouseX + this.position.x, mouseY + this.position.y)
         ? Optional.of(new RecipeSlotUnderMouse(new SlotRecipeWidget.RecipeSlotDrawable(), this.getPosition()))
         : Optional.empty();
   }

   public ScreenPosition getPosition() {
      return new ScreenPosition(this.position.x, this.position.y);
   }

   public class RecipeSlotDrawable implements IRecipeSlotDrawable {
      public void draw(GuiGraphics guiGraphics) {
      }

      public void drawHoverOverlays(GuiGraphics guiGraphics) {
      }

      public List<Component> getTooltip() {
         return SlotRecipeWidget.this.jeiSlot.getTooltip();
      }

      public void getTooltip(ITooltipBuilder builder) {
         SlotRecipeWidget.this.jeiSlot.getTooltip(builder);
      }

      public boolean isMouseOver(double mouseX, double mouseY) {
         return SlotRecipeWidget.this.slot.self().isMouseOverElement(mouseX + SlotRecipeWidget.this.position.x, mouseY + SlotRecipeWidget.this.position.y);
      }

      public void setPosition(int x, int y) {
      }

      public IIngredientConsumer createDisplayOverrides() {
         return SlotRecipeWidget.this.jeiSlot.createDisplayOverrides();
      }

      public void clearDisplayOverrides() {
         SlotRecipeWidget.this.jeiSlot.clearDisplayOverrides();
      }

      public Rect2i getRect() {
         return new Rect2i(0, 0, SlotRecipeWidget.this.slot.self().getSizeWidth(), SlotRecipeWidget.this.slot.self().getSizeHeight());
      }

      public Rect2i getAreaIncludingBackground() {
         return null;
      }

      public Stream<ITypedIngredient<?>> getAllIngredients() {
         return SlotRecipeWidget.this.slot
            .getXEIIngredients()
            .stream()
            .filter(IClickableIngredient.class::isInstance)
            .map(IClickableIngredient.class::cast)
            .map(IClickableIngredient::getTypedIngredient);
      }

      public Optional<ITypedIngredient<?>> getDisplayedIngredient() {
         List<Object> ingredients = SlotRecipeWidget.this.slot.getXEIIngredients();
         int index = ingredients.size() > 1 ? Math.abs((int)(System.currentTimeMillis() / 1000L) % ingredients.size()) : 0;
         if (ingredients.isEmpty()) {
            return Optional.empty();
         } else {
            return ingredients.get(index) instanceof IClickableIngredient<?> clickableIngredient
               ? Optional.of(clickableIngredient.getTypedIngredient())
               : Optional.empty();
         }
      }

      public RecipeIngredientRole getRole() {
         return SlotRecipeWidget.this.jeiSlot.getRole();
      }

      public void drawHighlight(GuiGraphics guiGraphics, int color) {
         SlotRecipeWidget.this.jeiSlot.drawHighlight(guiGraphics, color);
      }

      public Optional<String> getSlotName() {
         return SlotRecipeWidget.this.jeiSlot.getSlotName();
      }
   }
}
