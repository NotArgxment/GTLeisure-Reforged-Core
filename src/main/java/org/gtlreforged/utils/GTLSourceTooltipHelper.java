package org.gtlreforged.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class GTLSourceTooltipHelper {
   private static final Map<Item, ObjectList<Component>> ITEM_TOOLTIPS = new Reference2ReferenceOpenHashMap<>();
   private static final Map<Fluid, ObjectList<Component>> FLUID_TOOLTIPS = new Reference2ReferenceOpenHashMap<>();

   public static void appendItemTooltip(Item item, Consumer<Component> tooltips) {
      if (ITEM_TOOLTIPS.containsKey(item)) {
         ITEM_TOOLTIPS.get(item).forEach(tooltips);
      }
   }

   public static void appendFluidTooltip(Fluid fluid, Consumer<Component> tooltips) {
      if (FLUID_TOOLTIPS.containsKey(fluid)) {
         FLUID_TOOLTIPS.get(fluid).forEach(tooltips);
      }
   }

   public static void addItemTooltip(String id, List<Component> components) {
      Item item = Registries.getItem(id);
      if (item != null) {
         ITEM_TOOLTIPS.computeIfAbsent(item, k -> new ObjectArrayList<>()).addAll(components);
      }
   }

   public static void addFluidTooltip(String id, List<Component> components) {
      Fluid fluid = Registries.getFluid(id);
      if (fluid != null) {
         FLUID_TOOLTIPS.computeIfAbsent(fluid, k -> new ObjectArrayList<>()).addAll(components);
      }
   }
}
