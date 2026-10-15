package org.gtlcore.gtlcore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Content.class)
public abstract class ContentMixin {
   @Shadow(remap = false)
   public int chance;
   @Shadow(remap = false)
   public int maxChance;

   @OnlyIn(Dist.CLIENT)
   @Overwrite(remap = false)
   public void drawChance(GuiGraphics graphics, float x, float y, int width, int height) {
      if (this.chance != ChanceLogic.getMaxChancedValue()) {
         graphics.m_280168_().m_85836_();
         graphics.m_280168_().m_252880_(0.0F, 0.0F, 400.0F);
         graphics.m_280168_().m_85841_(0.5F, 0.5F, 1.0F);
         float chance = 100.0F * this.chance / this.maxChance;
         String percent = FormattingUtil.formatPercent(chance);
         String s = chance == 0.0F ? LocalizationUtils.format("gtceu.gui.content.chance_0_short", new Object[0]) : percent + "%";
         int color = chance == 0.0F ? 16711680 : 16776960;
         Font fontRenderer = Minecraft.m_91087_().f_91062_;
         int xDraw = (int)((x + width / 3.0F) * 2.0F - fontRenderer.m_92895_(s) + 23.0F) - (chance == 0.0F ? 10 : 0);
         int yDraw = (int)((y + height / 3.0F + 6.0F) * 2.0F - height) - (chance == 0.0F ? 3 : 0);
         graphics.m_280056_(fontRenderer, s, xDraw, yDraw, color, true);
         graphics.m_280168_().m_85849_();
      }
   }
}
