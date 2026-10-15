package org.gtlcore.gtlcore.utils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TextUtil {
   public static final ChatFormatting[] GTL_CORE$VC = new ChatFormatting[]{
      ChatFormatting.DARK_GRAY,
      ChatFormatting.GRAY,
      ChatFormatting.AQUA,
      ChatFormatting.GOLD,
      ChatFormatting.DARK_PURPLE,
      ChatFormatting.BLUE,
      ChatFormatting.LIGHT_PURPLE,
      ChatFormatting.RED,
      ChatFormatting.DARK_AQUA,
      ChatFormatting.DARK_RED,
      ChatFormatting.GREEN,
      ChatFormatting.DARK_GREEN,
      ChatFormatting.YELLOW,
      ChatFormatting.BLUE,
      ChatFormatting.RED
   };

   public static String formatting(String input, ChatFormatting[] colours, double delay) {
      StringBuilder sb = new StringBuilder(input.length() * 3);
      if (delay <= 0.0) {
         delay = 0.001;
      }

      int offset = (int)Math.floor((System.currentTimeMillis() & 16383L) / delay) % colours.length;

      for (int i = 0; i < input.length(); i++) {
         char c = input.charAt(i);
         sb.append(colours[(colours.length + i - offset) % colours.length].toString());
         sb.append(c);
      }

      return sb.toString();
   }

   public static String full_color(String input) {
      return formatting(
         input,
         new ChatFormatting[]{
            ChatFormatting.RED,
            ChatFormatting.GOLD,
            ChatFormatting.YELLOW,
            ChatFormatting.GREEN,
            ChatFormatting.AQUA,
            ChatFormatting.BLUE,
            ChatFormatting.LIGHT_PURPLE
         },
         80.0
      );
   }

   public static String dark_purplish_red(String input) {
      return formatting(input, new ChatFormatting[]{ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_RED}, 160.0);
   }

   public static String white_blue(String input) {
      return formatting(
         input,
         new ChatFormatting[]{
            ChatFormatting.BLUE,
            ChatFormatting.BLUE,
            ChatFormatting.BLUE,
            ChatFormatting.BLUE,
            ChatFormatting.WHITE,
            ChatFormatting.BLUE,
            ChatFormatting.WHITE,
            ChatFormatting.WHITE,
            ChatFormatting.BLUE,
            ChatFormatting.WHITE,
            ChatFormatting.WHITE,
            ChatFormatting.BLUE,
            ChatFormatting.RED,
            ChatFormatting.WHITE
         },
         80.0
      );
   }

   public static String purplish_red(String input) {
      return formatting(input, new ChatFormatting[]{ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE}, 160.0);
   }

   public static String golden(String input) {
      return formatting(input, new ChatFormatting[]{ChatFormatting.YELLOW, ChatFormatting.GOLD}, 160.0);
   }

   public static String dark_green(String input) {
      return formatting(input, new ChatFormatting[]{ChatFormatting.GREEN, ChatFormatting.DARK_GREEN}, 160.0);
   }

   public static boolean containsWithWildcard(String[] array, String target) {
      for (String element : array) {
         String regex = element.replace("*", ".*");
         if (Pattern.matches(regex, target)) {
            return true;
         }
      }

      return false;
   }

   @OnlyIn(Dist.CLIENT)
   public static void appendIngotConversionTooltip(FluidStack fluidStack, List<Component> tooltips, long amount) {
      Material material = ChemicalHelper.getMaterial(fluidStack.getFluid());
      if (material != null) {
         if (material.hasProperty(PropertyKey.INGOT)) {
            if (GTUtil.isShiftDown()) {
               if (amount >= 144L) {
                  long ingots = amount / 144L;
                  long remainder = amount % 144L;
                  String fluidAmount = String.format(" %,d mB = %,d * %d mB", amount, ingots, 144);
                  if (remainder != 0L) {
                     fluidAmount = fluidAmount + String.format(" + %d mB", remainder);
                  }

                  tooltips.add(Component.m_237115_("gtceu.gui.fluid_amount").m_130940_(ChatFormatting.GRAY).m_7220_(Component.m_237113_(fluidAmount)));
               }
            }
         }
      }
   }
}
