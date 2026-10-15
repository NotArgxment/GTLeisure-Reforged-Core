package org.gtlcore.gtlcore.api.gui;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class TurnsConfiguratorButton implements IFancyConfiguratorButton {
   protected IGuiTexture[] group;
   protected BiConsumer<ClickData, Integer> onClick;
   protected IntSupplier intSupplier;
   int mode;
   Function<Integer, List<Component>> tooltipsSupplier = isPressed -> Collections.emptyList();

   public TurnsConfiguratorButton(IntSupplier intSupplier, BiConsumer<ClickData, Integer> onClick, IGuiTexture... turns) {
      this.intSupplier = intSupplier;
      this.onClick = onClick;
      this.group = turns;
   }

   public Component getTitle() {
      return null;
   }

   public Widget createConfigurator() {
      return null;
   }

   public IGuiTexture getIcon() {
      return this.mode < this.group.length ? this.group[this.mode] : IGuiTexture.MISSING_TEXTURE;
   }

   public TurnsConfiguratorButton setTooltipsSupplier(Function<Integer, List<Component>> tooltipsSupplier) {
      this.tooltipsSupplier = tooltipsSupplier;
      return this;
   }

   public List<Component> getTooltips() {
      return this.tooltipsSupplier.apply(this.mode);
   }

   public void detectAndSendChange(BiConsumer<Integer, Consumer<FriendlyByteBuf>> sender) {
      int newValue = this.intSupplier.getAsInt();
      if (newValue != this.mode) {
         this.mode = newValue;
         sender.accept(0, buf -> buf.writeInt(this.mode));
      }
   }

   public void readUpdateInfo(int id, FriendlyByteBuf buf) {
      if (id == 0) {
         this.mode = buf.readInt();
      }
   }

   public void writeInitialData(FriendlyByteBuf buffer) {
      this.mode = this.intSupplier.getAsInt();
      buffer.writeInt(this.mode);
   }

   public void readInitialData(FriendlyByteBuf buffer) {
      this.mode = buffer.readInt();
   }

   public void onClick(ClickData clickData) {
      this.mode++;
      if (this.mode == this.group.length) {
         this.mode = 0;
      }

      this.onClick.accept(clickData, this.mode);
   }
}
