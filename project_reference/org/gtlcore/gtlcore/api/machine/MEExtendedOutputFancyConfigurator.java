package org.gtlcore.gtlcore.api.machine;

import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class MEExtendedOutputFancyConfigurator implements IFancyUIProvider {
   protected final Supplier<Widget> widgetSupplier;

   public MEExtendedOutputFancyConfigurator(Supplier<Widget> widgetSupplier) {
      this.widgetSupplier = widgetSupplier;
   }

   public Widget createMainPage(FancyMachineUIWidget fancyMachineUIWidget) {
      return this.widgetSupplier.get();
   }

   public IGuiTexture getTabIcon() {
      return new ItemStackTexture(new ItemStack[]{GTItems.ITEM_FILTER.asStack()});
   }

   public Component getTitle() {
      return Component.m_237115_("gtlcore.gui.filter_config.title");
   }

   public List<Component> getTabTooltips() {
      return List.of(Component.m_237115_("tooltip.gtlcore.change_item_fluid_filter_priority"));
   }
}
