package org.gtlcore.gtlcore.mixin.ae2.stacks;

import appeng.api.stacks.AEItemKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AEItemKey.class)
public abstract class AEItemKeyMixin {
   private int fuzzySearchMaxValue = -1;
   @Shadow(remap = false)
   @Final
   @Mutable
   private final Item item;

   public AEItemKeyMixin(Item item) {
      this.item = item;
   }

   @Overwrite(remap = false)
   public int getFuzzySearchMaxValue() {
      if (this.fuzzySearchMaxValue < 0) {
         this.fuzzySearchMaxValue = this.item.m_41462_();
      }

      return this.fuzzySearchMaxValue;
   }
}
