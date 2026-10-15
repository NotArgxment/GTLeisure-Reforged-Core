package org.gtlcore.gtlcore.mixin.gtm.gui;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.CWURecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.integration.GTRecipeWidget;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.TextUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTRecipeWidget.class)
public abstract class GTRecipeWidgetMixin {
   @Shadow(remap = false)
   @Final
   private GTRecipe recipe;

   @Overwrite(remap = false)
   @NotNull
   private static List<Component> getRecipeParaText(GTRecipe recipe, int duration, long inputEUt, long outputEUt) {
      List<Component> texts = new ArrayList<>();
      if (!recipe.data.m_128471_("hide_duration")) {
         texts.add(Component.m_237110_("gtceu.recipe.duration", new Object[]{FormattingUtil.formatNumbers(duration / 20.0F)}));
      }

      long EUt = inputEUt;
      boolean isOutput = false;
      if (EUt == 0L) {
         EUt = outputEUt;
         isOutput = true;
      }

      if (EUt > 0L) {
         long euTotal = EUt * duration;
         if (recipe.data.m_128471_("duration_is_total_cwu") && recipe.tickInputs.containsKey(CWURecipeCapability.CAP)) {
            int minimumCWUt = Math.max(
               ((List)recipe.tickInputs.get(CWURecipeCapability.CAP)).stream().map(Content::getContent).mapToInt(CWURecipeCapability.CAP::of).sum(), 1
            );
            texts.add(Component.m_237110_("gtceu.recipe.max_eu", new Object[]{FormattingUtil.formatNumbers(euTotal / minimumCWUt)}));
         } else {
            texts.add(Component.m_237110_("gtceu.recipe.total", new Object[]{NumberUtils.formatLong(euTotal)}));
         }

         long absEUt = Math.abs(EUt);
         byte tier = GTUtil.getTierByVoltage(absEUt);
         MutableComponent component = Component.m_237110_(!isOutput ? "gtceu.recipe.eu" : "gtceu.recipe.eu_inverted", new Object[]{NumberUtils.formatLong(EUt)});
         if (!isOutput) {
            component.m_7220_(
               Component.m_237113_(" (")
                  .m_130940_(ChatFormatting.GREEN)
                  .m_7220_(
                     Component.m_237113_(NumberUtils.formatDouble((double)absEUt / GTValues.V[tier]) + "A")
                        .m_130938_(style -> style.m_131140_(TextUtil.GTL_CORE$VC[tier]))
                  )
                  .m_7220_(Component.m_237113_(")").m_130940_(ChatFormatting.GREEN))
            );
         }

         texts.add(component);
      }

      return texts;
   }

   @Inject(method = "setRecipeOC", at = @At("HEAD"), cancellable = true, remap = false)
   public void setRecipeOC(int button, boolean isShiftClick, CallbackInfo ci) {
      if (this.recipe.recipeType.isScanner()) {
         ci.cancel();
      }
   }
}
