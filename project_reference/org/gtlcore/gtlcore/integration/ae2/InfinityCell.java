package org.gtlcore.gtlcore.integration.ae2;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.cells.ICellWorkbenchItem;
import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import appeng.hooks.AEToolItem;
import com.google.common.base.Preconditions;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.integration.ae2.storage.InfinityCellHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InfinityCell extends Item implements ICellWorkbenchItem, AEToolItem {
   private final AEKeyType keyType;

   public InfinityCell(AEKeyType keyType) {
      super(new Properties().m_41487_(1).m_41486_());
      this.keyType = keyType;
   }

   public boolean isEditable(ItemStack is) {
      return true;
   }

   public FuzzyMode getFuzzyMode(ItemStack is) {
      String fz = is.m_41784_().m_128461_("FuzzyMode");
      if (fz.isEmpty()) {
         return FuzzyMode.IGNORE_ALL;
      }

      try {
         return FuzzyMode.valueOf(fz);
      } catch (Throwable t) {
         return FuzzyMode.IGNORE_ALL;
      }
   }

   public void setFuzzyMode(ItemStack is, FuzzyMode fzMode) {
      is.m_41784_().m_128359_("FuzzyMode", fzMode.name());
   }

   public IUpgradeInventory getUpgrades(ItemStack is) {
      return UpgradeInventories.forItem(is, 2);
   }

   public void m_7373_(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
      Preconditions.checkArgument(stack.m_41720_() == this);
      InfinityCellHandler.INSTANCE.addCellInformationToTooltip(stack, tooltip);
   }

   public AEKeyType getKeyType() {
      return this.keyType;
   }
}
