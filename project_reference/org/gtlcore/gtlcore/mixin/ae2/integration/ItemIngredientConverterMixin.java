package org.gtlcore.gtlcore.mixin.ae2.integration;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jei.ItemIngredientConverter;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.tterrag.registrate.util.entry.RegistryEntry;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemIngredientConverter.class)
public abstract class ItemIngredientConverterMixin {
   @Overwrite(remap = false)
   @Nullable
   public GenericStack getStackFromIngredient(ItemStack itemStack) {
      if (!itemStack.m_41619_()) {
         Item item = itemStack.m_41720_();
         boolean b1 = Arrays.stream(GTItems.SHAPE_MOLDS).<Item>map(RegistryEntry::get).anyMatch(i -> i.equals(item));
         boolean b2 = Arrays.stream(GTItems.SHAPE_EXTRUDERS).filter(Objects::nonNull).<Item>map(RegistryEntry::get).anyMatch(i -> i.equals(item));
         boolean b3 = itemStack.m_41783_() != null && itemStack.m_41783_().m_128441_("assembly_line_research");
         if (b1 || b2 || b3) {
            return null;
         }
      }

      return GenericStack.fromItemStack(itemStack);
   }
}
