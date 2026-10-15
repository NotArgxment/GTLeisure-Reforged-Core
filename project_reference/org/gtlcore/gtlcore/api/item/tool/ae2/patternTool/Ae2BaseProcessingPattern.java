package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;

public class Ae2BaseProcessingPattern {
   public ItemStack patternStack;
   public int scale;
   public ServerPlayer serverPlayer;
   public List<Item> DefaultBlackItem = new ArrayList<>(List.of(GTItems.INTEGRATED_CIRCUIT.m_5456_()));

   public void setDefaultFilter() {
      AEProcessingPattern aeProcessingPattern = Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(this.patternStack, this.serverPlayer);
      List<GenericStack> inputGenericStacks = Ae2BaseProcessingPatternHelper.transGenericStackArrayToList(aeProcessingPattern.getSparseInputs());
      List<GenericStack> outputGenericStacks = Ae2BaseProcessingPatternHelper.transGenericStackArrayToList(aeProcessingPattern.getSparseOutputs());
      inputGenericStacks = Ae2BaseProcessingPatternHelper.GenericStackListBlackFilter(inputGenericStacks, this.DefaultBlackItem);
      outputGenericStacks = Ae2BaseProcessingPatternHelper.GenericStackListBlackFilter(outputGenericStacks, this.DefaultBlackItem);
      this.patternStack = PatternDetailsHelper.encodeProcessingPattern(
         Ae2BaseProcessingPatternHelper.transGenericStackListToArray(inputGenericStacks),
         Ae2BaseProcessingPatternHelper.transGenericStackListToArray(outputGenericStacks)
      );
   }

   public void useSetScale(int newScale, boolean div, long maxItemStack, long maxFluidStack) {
      try {
         ItemStack oldPatternStack = this.patternStack;
         ItemStack newPatternStack = Ae2BaseProcessingPatternHelper.multiplyScale(
            newScale,
            div,
            Objects.requireNonNull(Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(oldPatternStack, this.serverPlayer)),
            maxItemStack,
            maxFluidStack
         );
         if (newPatternStack != null && !newPatternStack.m_41619_()) {
            this.patternStack = newPatternStack;
         }
      } catch (Exception e) {
         GTLCore.LOGGER.error(e.getMessage());
      }
   }

   public void setScale(int newScale, boolean div, long maxItemStack, long maxFluidStack) {
      this.useSetScale(newScale, div, maxItemStack, maxFluidStack);
   }

   public void setScale(int newScale, boolean div) {
      long maxItemStack = 9999999L;
      long maxFluidStack = 9999999L;
      this.useSetScale(newScale, div, maxItemStack, maxFluidStack);
   }

   public void setLore(Component component) {
      this.patternStack = this.patternStack.kjs$withLore(new Component[]{component});
   }

   public ItemStack getPatternItemStack() {
      return this.patternStack;
   }

   public Ae2BaseProcessingPattern(ItemStack patternStack, ServerPlayer serverPlayer) {
      this.patternStack = patternStack;
      this.serverPlayer = serverPlayer;
      Ae2BaseProcessingPatternHelper.decodeToAEProcessingPattern(patternStack, serverPlayer);

      try {
         for (ItemEntry<Item> shapeMold : GTItems.SHAPE_MOLDS) {
            if (shapeMold != null) {
               this.DefaultBlackItem.add(shapeMold.m_5456_());
            }
         }

         for (ItemEntry<Item> shapeMold : GTItems.SHAPE_EXTRUDERS) {
            if (shapeMold != null) {
               this.DefaultBlackItem.add(shapeMold.m_5456_());
            }
         }
      } catch (Exception e) {
         GTLCore.LOGGER.error(e.getMessage());
      }
   }
}
