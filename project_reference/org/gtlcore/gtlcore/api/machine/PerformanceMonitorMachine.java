package org.gtlcore.gtlcore.api.machine;

import com.glodblock.github.extendedae.client.render.EAEHighlightHandler;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class PerformanceMonitorMachine extends MetaMachine implements IFancyUIMachine {
   public static boolean observe;
   public static final Map<MetaMachine, Integer> PERFORMANCE_MAP = new WeakHashMap<>();
   private List<Component> textListCache;

   public PerformanceMonitorMachine(IMachineBlockEntity holder) {
      super(holder);
   }

   public static ResourceKey<Level> getDimensionKey(ResourceLocation resourceLocation) {
      return ResourceKey.m_135785_(Registries.f_256858_, resourceLocation);
   }

   private static void handleDisplayClick(String componentData, ClickData clickData) {
      if (clickData.isRemote) {
         String[] parts = componentData.split(", ");
         BlockPos pos = new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
         EAEHighlightHandler.highlight(pos, getDimensionKey(new ResourceLocation(parts[3])), System.currentTimeMillis() + 15000L);
      }
   }

   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(0, 0, 190, 125);
      group.addWidget(
         new DraggableScrollableWidgetGroup(4, 4, 182, 117)
            .setBackground(GuiTextures.DISPLAY)
            .addWidget(new LabelWidget(4, 5, this.self().getBlockState().m_60734_().m_7705_()))
            .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(PerformanceMonitorMachine::handleDisplayClick))
      );
      group.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      return group;
   }

   private void addDisplayText(@NotNull List<Component> textList) {
      if (!this.isRemote()) {
         observe = true;
         if (this.textListCache == null || this.getOffsetTimer() % 40L == 0L) {
            this.textListCache = new ArrayList<>();
            Map<MetaMachine, Integer> sortedMap = new TreeMap<>((mm1, mm2) -> PERFORMANCE_MAP.get(mm2).compareTo(PERFORMANCE_MAP.get(mm1)));
            sortedMap.putAll(PERFORMANCE_MAP);
            PERFORMANCE_MAP.clear();

            for (Entry<MetaMachine, Integer> entry : sortedMap.entrySet()) {
               MetaMachine machine = entry.getKey();
               String pos = machine.getPos().m_123344_();
               Level level = machine.getLevel();
               if (level != null) {
                  this.textListCache
                     .add(
                        Component.m_237115_(machine.getBlockState().m_60734_().m_7705_())
                           .m_130946_(" ")
                           .m_130948_(
                              Style.f_131099_
                                 .m_131144_(
                                    new HoverEvent(
                                       Action.f_130831_,
                                       Component.m_237110_("recipe.condition.dimension.tooltip", new Object[]{level.m_46472_().m_135782_()})
                                          .m_130946_(" [")
                                          .m_130946_(pos)
                                          .m_130946_("] ")
                                    )
                                 )
                           )
                           .m_7220_(Component.m_237110_("tooltip.jade.delay", new Object[]{entry.getValue()}).m_130946_(" \u03bcs"))
                           .m_7220_(ComponentPanelWidget.withButton(Component.m_237113_(" [ ] "), pos + ", " + level.m_46472_().m_135782_()))
                     );
               }
            }
         }

         textList.addAll(this.textListCache);
      }
   }
}
