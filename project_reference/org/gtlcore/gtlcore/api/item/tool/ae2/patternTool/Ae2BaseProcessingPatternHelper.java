package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.crafting.pattern.EncodedPatternItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;

public class Ae2BaseProcessingPatternHelper {
   public static ItemStack multiplyScale(int scale, boolean div, AEProcessingPattern patternDetail, long maxItemStack, long maxFluidStack) {
      GenericStack[] input = patternDetail.getSparseInputs();
      GenericStack[] output = patternDetail.getOutputs();
      if (checkModify(input, scale, div, maxItemStack, maxFluidStack) && checkModify(output, scale, div, maxItemStack, maxFluidStack)) {
         GenericStack[] mulInput = new GenericStack[input.length];
         GenericStack[] mulOutput = new GenericStack[output.length];
         modifyStacks(input, mulInput, scale, div);
         modifyStacks(output, mulOutput, scale, div);
         return PatternDetailsHelper.encodeProcessingPattern(mulInput, mulOutput);
      } else {
         GTLCore.LOGGER.info("\u5185\u90e8\u9519\u8bef\uff1a\u65e0\u6cd5\u6574\u9664 \u6216 \u4e58\u6570\u8fc7\u5927");
         return null;
      }
   }

   public static AEProcessingPattern decodeToAEProcessingPattern(ItemStack patternStack, ServerPlayer serverPlayer) {
      if (patternStack.m_41720_() instanceof EncodedPatternItem patternItem_1) {
         if (patternItem_1.decode(patternStack, serverPlayer.m_9236_(), false) instanceof AEProcessingPattern processStack) {
            return processStack;
         }

         GTLCore.LOGGER
            .info("Ae2BaseProcessingPattern requires a EncodedPatternItem \u610f\u5916\u4e4b\u5185\u7684\u8f93\u5165\uff1a\u975e\u5904\u7406\u6837\u677f");
      } else {
         GTLCore.LOGGER.info("Ae2BaseProcessingPattern requires a EncodedPatternItem \u610f\u5916\u4e4b\u5185\u7684\u8f93\u5165\uff1a\u975eAE\u6837\u677f");
      }

      return null;
   }

   public static List<GenericStack> transGenericStackArrayToList(GenericStack[] genericStackArray) {
      return new ArrayList<>(Arrays.asList(genericStackArray));
   }

   public static GenericStack[] transGenericStackListToArray(List<GenericStack> genericStackList) {
      GenericStack[] genericStackArrayList = new GenericStack[genericStackList.size()];
      return genericStackList.toArray(genericStackArrayList);
   }

   public static List<GenericStack> GenericStackListBlackFilter(List<GenericStack> inputGenericStacks, List<Item> matchItemList) {
      return inputGenericStacks.stream().filter(genericStack -> !itemMatch(genericStack, matchItemList)).toList();
   }

   private static boolean itemMatch(GenericStack genericStack, List<Item> matchItemList) {
      for (Item item : matchItemList) {
         if (genericStack.what().equals(AEItemKey.of(item))) {
            return true;
         }
      }

      return false;
   }

   private static boolean checkModify(GenericStack[] stacks, int scale, boolean div, long maxItemStack, long maxFluidStack) {
      if (div) {
         for (GenericStack stack : stacks) {
            if (stack != null && stack.amount() % scale != 0L) {
               return false;
            }
         }
      } else {
         for (GenericStack stack : stacks) {
            if (stack != null) {
               if (stack.what().getType().equals(AEKeyType.fluids())) {
                  long upper = maxFluidStack * stack.what().getAmountPerUnit();
                  if (stack.amount() * scale > upper) {
                     return false;
                  }
               }

               if (stack.what().getType().equals(AEKeyType.items())) {
                  long upper = maxItemStack * stack.what().getAmountPerUnit();
                  if (stack.amount() * scale > upper) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   private static void modifyStacks(GenericStack[] stacks, GenericStack[] des, int scale, boolean div) {
      for (int i = 0; i < stacks.length; i++) {
         if (stacks[i] != null) {
            long amt = div ? stacks[i].amount() / scale : stacks[i].amount() * scale;
            des[i] = new GenericStack(stacks[i].what(), amt);
         }
      }
   }
}
