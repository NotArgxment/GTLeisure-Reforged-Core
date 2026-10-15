package org.gtlcore.gtlcore.integration.ae2.storage;

import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.integration.ae2.InfinityCell;
import org.gtlcore.gtlcore.utils.NumberUtils;

public class InfinityCellHandler implements ICellHandler {
   public static final InfinityCellHandler INSTANCE = new InfinityCellHandler();

   public boolean isCell(ItemStack is) {
      return is.m_41720_() instanceof InfinityCell;
   }

   public InfinityCellInventory getCellInventory(ItemStack is, ISaveProvider container) {
      return InfinityCellInventory.createInventory(is, container);
   }

   public void addCellInformationToTooltip(ItemStack stack, List<Component> lines) {
      InfinityCellInventory handler = this.getCellInventory(stack, null);
      if (handler != null && handler.hasDiskUUID()) {
         lines.add(
            Component.m_237113_("UUID: ")
               .m_130940_(ChatFormatting.GRAY)
               .m_7220_(Component.m_237113_(handler.getDiskUUID().toString()).m_130940_(ChatFormatting.AQUA))
         );
         lines.add(
            Component.m_237113_("Byte: ")
               .m_130940_(ChatFormatting.GRAY)
               .m_7220_(NumberUtils.numberText(handler.getNbtItemCount()).m_130940_(ChatFormatting.GREEN))
         );
      }
   }
}
