package org.gtlcore.gtlcore.integration.ae2;

import appeng.api.implementations.blockentities.IChestOrDrive;
import appeng.api.storage.cells.ICellGuiHandler;
import appeng.api.storage.cells.ICellHandler;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import appeng.menu.me.common.MEStorageMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InfinityCellGuiHandler implements ICellGuiHandler {
   public boolean isSpecializedFor(ItemStack cell) {
      return cell.m_41720_() instanceof InfinityCell || cell.m_41720_() instanceof FastInfinityCell;
   }

   public void openChestGui(Player player, IChestOrDrive chest, ICellHandler cellHandler, ItemStack cell) {
      MenuOpener.open(MEStorageMenu.TYPE, player, MenuLocators.forBlockEntity((BlockEntity)chest));
   }
}
