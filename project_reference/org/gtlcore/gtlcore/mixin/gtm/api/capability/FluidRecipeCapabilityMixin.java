package org.gtlcore.gtlcore.mixin.gtm.api.capability;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.client.TooltipsHandler;
import com.gregtechceu.gtceu.integration.GTRecipeWidget;
import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.utils.GTLSourceTooltipHelper;
import org.gtlcore.gtlcore.utils.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRecipeCapability.class)
public abstract class FluidRecipeCapabilityMixin extends RecipeCapability<FluidIngredient> {
   protected FluidRecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<FluidIngredient> serializer) {
      super(name, color, doRenderSlot, sortIndex, serializer);
   }

   @Overwrite(remap = false)
   public FluidIngredient copyWithModifier(FluidIngredient content, ContentModifier modifier) {
      if (content.isEmpty()) {
         return content.copy();
      }

      FluidIngredient copy = content.copy();
      copy.setAmount(modifier.apply(copy.getAmount()).longValue());
      return copy;
   }

   @Overwrite(remap = false)
   public int limitParallel(GTRecipe recipe, IRecipeCapabilityHolder holder, int multiplier) {
      return Ints.saturatedCast(IParallelLogic.getOutputFluidParallel(holder, recipe, recipe.getOutputContents(FluidRecipeCapability.CAP), multiplier));
   }

   @Overwrite(remap = false)
   public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
      return Ints.saturatedCast(IParallelLogic.getInputFluidParallel(holder, recipe, parallelAmount));
   }

   @OnlyIn(Dist.CLIENT)
   @Inject(
      method = "applyWidgetInfo",
      at = @At(
         value = "INVOKE",
         target = "Lcom/lowdragmc/lowdraglib/gui/widget/TankWidget;setXEIChance(F)Lcom/lowdragmc/lowdraglib/gui/widget/TankWidget;",
         shift = Shift.AFTER
      ),
      remap = false,
      require = 0
   )
   private void hideAmountInXEI(CallbackInfo ci, @Local(name = "tank") TankWidget tank, @Local(name = "isXEI") boolean isXEI) {
      if (isXEI) {
         tank.setShowAmount(false);
      }
   }

   @OnlyIn(Dist.CLIENT)
   @ModifyArg(
      method = "applyWidgetInfo",
      at = @At(
         value = "INVOKE",
         target = "Lcom/lowdragmc/lowdraglib/gui/widget/TankWidget;setOnAddedTooltips(Ljava/util/function/BiConsumer;)Lcom/lowdragmc/lowdraglib/gui/widget/TankWidget;"
      ),
      remap = false
   )
   public BiConsumer<TankWidget, List<Component>> applyWidgetInfo(
      BiConsumer<TankWidget, List<Component>> onAddedTooltips,
      @Local(name = "content") Content content,
      @Local(name = "recipe") GTRecipe recipe,
      @Local(name = "index") int index,
      @Local(name = "io") IO io,
      @Local(name = "isXEI") boolean isXEI
   ) {
      return (w, tooltips) -> {
         FluidIngredient ingredient = (FluidIngredient)FluidRecipeCapability.CAP.of(content.content);
         if (ingredient.getStacks().length > 0) {
            FluidStack stack = ingredient.getStacks()[0];
            if (!isXEI) {
               TooltipsHandler.appendFluidTooltips(stack.getFluid(), stack.getAmount(), tooltips::add, TooltipFlag.f_256752_);
            } else {
               if (!LDLib.isEmiLoaded()) {
                  GTLSourceTooltipHelper.appendFluidTooltip(stack.getFluid(), tooltips::add);
               }

               TextUtil.appendIngotConversionTooltip(stack, tooltips, stack.getAmount());
            }
         }

         GTRecipeWidget.setConsumedChance(content, ChanceLogic.OR, tooltips);
         if (this.isTickSlot(index, io, recipe)) {
            tooltips.add(Component.m_237115_("gtceu.gui.content.per_tick"));
         }
      };
   }
}
