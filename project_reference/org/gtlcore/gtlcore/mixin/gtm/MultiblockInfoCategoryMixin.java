package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.integration.jei.multipage.MultiblockInfoCategory;
import com.gregtechceu.gtceu.integration.jei.multipage.MultiblockInfoWrapper;
import com.lowdragmc.lowdraglib.gui.ingredient.IRecipeIngredientSlot;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.ModularUIRecipeCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.util.thread.BlockableEventLoop;
import org.gtlcore.gtlcore.integration.jei.SlotRecipeWidget;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiblockInfoCategory.class)
public abstract class MultiblockInfoCategoryMixin extends ModularUIRecipeCategory<MultiblockInfoWrapper> {
   @Inject(method = "registerRecipes", at = @At("HEAD"), cancellable = true, remap = false)
   private static void registerRecipes(IRecipeRegistration registry, @NotNull CallbackInfo ci) {
      BlockableEventLoop<?> executor = Minecraft.m_91087_();
      CompletableFuture.<List>supplyAsync(
            () -> GTRegistries.MACHINES
               .values()
               .stream()
               .filter(MultiblockMachineDefinition.class::isInstance)
               .map(MultiblockMachineDefinition.class::cast)
               .filter(MultiblockMachineDefinition::isRenderXEIPreview)
               .map(MultiblockInfoWrapper::new)
               .toList(),
            executor
         )
         .thenAcceptAsync(recipes -> registry.addRecipes(MultiblockInfoCategory.RECIPE_TYPE, recipes), executor);
      ci.cancel();
   }

   public void setRecipe(@NotNull IRecipeLayoutBuilder builder, MultiblockInfoWrapper wrapper, @NotNull IFocusGroup focuses) {
      wrapper.setRecipeWidget(0, 0);
      List<Widget> flatVisibleWidgetCollection = wrapper.modularUI.getFlatWidgetCollection();

      for (int i = 0; i < flatVisibleWidgetCollection.size(); i++) {
         Widget widget = flatVisibleWidgetCollection.get(i);
         if (widget instanceof IRecipeIngredientSlot slot) {
            RecipeIngredientRole role = mapToRole(slot.getIngredientIO());
            if (role == null) {
               gTLCore$addJEISlot(builder, slot, RecipeIngredientRole.INPUT, i);
               gTLCore$addJEISlot(builder, slot, RecipeIngredientRole.OUTPUT, i);
            } else {
               gTLCore$addJEISlot(builder, slot, role, i);
            }
         }
      }
   }

   @Unique
   private static void gTLCore$addJEISlot(IRecipeLayoutBuilder builder, IRecipeIngredientSlot slot, RecipeIngredientRole role, int index) {
      String slotName = "slot_" + index;
      IRecipeSlotBuilder slotBuilder = builder.addSlotToWidget(role, (extrasBuilder, recipe, slots) -> {
         Optional<IRecipeSlotDrawable> jeiSlot = slots.stream().filter(s -> s.getSlotName().map(name -> name.equals(slotName)).orElse(false)).findFirst();
         jeiSlot.ifPresent(drawable -> extrasBuilder.addWidget(new SlotRecipeWidget(slot, drawable)));
      });

      for (Object ingredient : slot.getXEIIngredients()) {
         if (ingredient instanceof IClickableIngredient clickableIngredient) {
            IIngredientType type = clickableIngredient.getIngredientType();
            Object ingredients = clickableIngredient.getIngredient();
            slotBuilder.addIngredient(type, ingredients);
         }
      }

      slotBuilder.setSlotName(slotName);
      slotBuilder.addRichTooltipCallback((recipeSlotView, tooltipBuilder) -> {
         if (slot instanceof SlotWidget slotWidget) {
            tooltipBuilder.addAll(slotWidget.getAdditionalToolTips(new ArrayList()));
         } else if (slot instanceof TankWidget tankWidget) {
            tooltipBuilder.addAll(tankWidget.getAdditionalToolTips(new ArrayList()));
         }
      });
   }
}
