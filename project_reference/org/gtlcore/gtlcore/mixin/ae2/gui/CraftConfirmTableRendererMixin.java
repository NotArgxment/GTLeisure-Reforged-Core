package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.api.stacks.KeyCounter;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.crafting.AbstractTableRenderer;
import appeng.client.gui.me.crafting.CraftConfirmTableRenderer;
import appeng.menu.me.common.IClientRepo;
import appeng.menu.me.crafting.CraftingPlanSummaryEntry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.gtlcore.gtlcore.integration.ae2.common.IConfirmStartMenu;
import org.gtlcore.gtlcore.integration.ae2.crafting.ICraftingPlanSummaryEntry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CraftConfirmTableRenderer.class)
public abstract class CraftConfirmTableRendererMixin extends AbstractTableRenderer<CraftingPlanSummaryEntry> {
   @Unique
   @Nullable
   private KeyCounter gtlcore$cachedKeys = null;

   protected CraftConfirmTableRendererMixin(AEBaseScreen<?> screen, int x, int y, int rows) {
      super(screen, x, y, rows);
   }

   @Inject(
      method = "getEntryDescription(Lappeng/menu/me/crafting/CraftingPlanSummaryEntry;)Ljava/util/List;",
      at = @At("TAIL"),
      cancellable = true,
      remap = false
   )
   private void getEntryDescription(CraftingPlanSummaryEntry entry, CallbackInfoReturnable<List<Component>> cir) {
      List<Component> lines = (List<Component>)cir.getReturnValue();
      long craftTimes = ((ICraftingPlanSummaryEntry)entry).gtlcore$getCraftTimes();
      if (craftTimes > 1L) {
         ChatFormatting color = craftTimes < 500L
            ? ChatFormatting.DARK_GREEN
            : (craftTimes < 2000L ? ChatFormatting.GOLD : (craftTimes < 6000L ? ChatFormatting.RED : ChatFormatting.DARK_RED));
         MutableComponent timesText = Component.m_237113_(String.format("%d", craftTimes)).m_130940_(color);
         lines.add(Component.m_237110_("gtlcore.ae.craft.create_times", new Object[]{timesText}));
      }

      if (this.gtlcore$cachedKeys != null && entry.getMissingAmount() == 0L) {
         long storedTotal = this.gtlcore$cachedKeys.get(entry.getWhat());
         if (storedTotal > 0L) {
            float storedPercent = Math.min((float)entry.getStoredAmount() / (float)storedTotal, 1.0F);
            ChatFormatting color = storedPercent < 0.25F
               ? ChatFormatting.DARK_GREEN
               : (storedPercent < 0.5F ? ChatFormatting.GOLD : (storedPercent < 0.75F ? ChatFormatting.RED : ChatFormatting.DARK_RED));
            MutableComponent percentText = Component.m_237113_(this.gtlcore$formatNumber2Places(storedPercent * 100.0F) + "%").m_130940_(color);
            lines.add(Component.m_237110_("gtlcore.ae.craft.used_percent", new Object[]{percentText}));
         }
      }

      cir.setReturnValue(lines);
   }

   @WrapMethod(method = "getEntryDescription(Lappeng/menu/me/crafting/CraftingPlanSummaryEntry;)Ljava/util/List;", remap = false)
   private List<Component> warpGetEntryDescription(CraftingPlanSummaryEntry entry, Operation<List<Component>> original) {
      IClientRepo repo = this.screen.m_6262_() instanceof IConfirmStartMenu me ? me.gtlcore$getClientRepo() : null;
      if (repo == null) {
         return (List<Component>)original.call(new Object[]{entry});
      }

      this.gtlcore$cachedKeys = new KeyCounter();
      repo.getAllEntries()
         .stream()
         .filter(e -> e.getWhat() != null && e.getStoredAmount() > 0L)
         .forEach(e -> this.gtlcore$cachedKeys.add(e.getWhat(), e.getStoredAmount()));
      List<Component> lines = (List<Component>)original.call(new Object[]{entry});
      this.gtlcore$cachedKeys = null;
      return lines;
   }

   @Unique
   private String gtlcore$formatNumber2Places(double number) {
      return number == (long)number ? String.format("%d", (long)number) : String.format("%.2f", number);
   }
}
