package org.gtlcore.gtlcore.api.gui;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;
import org.gtlcore.gtlcore.common.block.BlockMap;

public class BlockMapSelectorWidget extends WidgetGroup {
   public static final Component SC = Component.m_237115_("gui.gtlcore.stellar_thermal_container");
   public static final Component SEPM = Component.m_237115_("gui.gtlcore.space_elevator_module");
   public static final Component CAL = Component.m_237115_("gui.gtlcore.component_assembly_casing");
   public static final Component COIL = Component.m_237115_("gui.gtlcore.coil");
   private final BiConsumer<String, Integer> onChanged;
   private List<Block> blocks;
   private String currentType;

   public BlockMapSelectorWidget(int y, int width, BiConsumer<String, Integer> onChanged) {
      super(0, y, width, 56);
      this.onChanged = onChanged;
      this.setVisible(false);
   }

   public static Component getBlock(String string) {
      return switch (string) {
         case "sc" -> SC;
         case "sepm" -> SEPM;
         case "cal" -> CAL;
         case "coil" -> COIL;
         default -> throw new IllegalStateException("Unexpected value: " + string);
      };
   }

   public void setInit(ItemStack itemStack) {
      CompoundTag tag = itemStack.m_41784_();
      String block = tag.m_128461_("blocks");
      if (!block.isEmpty()) {
         this.blocks = Arrays.<Block>stream((Block[])((Lazy)BlockMap.tierBlockMap.get(block)).get()).toList();
         this.currentType = block;
      }
   }

   public void showType(boolean isShow) {
      if (!isShow) {
         this.setVisible(false);
      } else {
         WidgetGroup group = new WidgetGroup(0, 2, 80, 52);
         group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
         DraggableScrollableWidgetGroup blockType = new DraggableScrollableWidgetGroup(4, 4, 72, 44);
         blockType.setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1.0F)).setBackground(GuiTextures.DISPLAY);
         int y = 0;

         for (ObjectIterator var5 = BlockMap.tierBlockMap.keySet().iterator(); var5.hasNext(); y += 15) {
            String key = (String)var5.next();
            blockType.addWidget(
               new WidgetGroup(2, 2 + y, 66, 15).addWidget(new ExtendLabelWidget(0, 0, getBlock(key))).addWidget(new ButtonWidget(0, 0, 64, 15, cd -> {
                  this.showTier(Arrays.<Block>stream((Block[])((Lazy)BlockMap.tierBlockMap.get(key)).get()).toList());
                  this.currentType = key;
               }))
            );
         }

         this.addWidget(group.addWidget(blockType));
         this.setVisible(true);
         if (this.blocks != null && !this.blocks.isEmpty()) {
            this.showTier(this.blocks);
         }
      }
   }

   public void showTier(List<Block> blocks) {
      if (!blocks.isEmpty()) {
         this.blocks = blocks;
         WidgetGroup group = new WidgetGroup(80, 2, 120, 52);
         group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
         DraggableScrollableWidgetGroup blockTier = new DraggableScrollableWidgetGroup(4, 4, 112, 44);
         blockTier.setYScrollBarWidth(2).setYBarStyle(null, ColorPattern.T_WHITE.rectTexture().setRadius(1.0F)).setBackground(GuiTextures.DISPLAY);

         for (int i = 0; i < blocks.size(); i++) {
            int finalI = i;
            Block block = blocks.get(finalI);
            blockTier.addWidget(
               new WidgetGroup(2, 2 + i * 20, 110, 22)
                  .addWidget(new ImageWidget(2, 0, 18, 18, new ItemStackTexture(new ItemStack[]{block.m_5456_().m_7968_()})))
                  .addWidget(new ExtendLabelWidget(20, 4, block.m_49954_()))
                  .addWidget(new ButtonWidget(20, 0, 88, 18, cd -> {
                     if (this.onChanged != null) {
                        this.onChanged.accept(this.currentType, finalI);
                     }
                  }))
            );
         }

         this.addWidget(group.addWidget(blockTier));
      }
   }
}
