package org.gtlcore.gtlcore.mixin.extendedae;

import appeng.api.stacks.AEKey;
import com.glodblock.github.extendedae.common.items.InfinityCell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(InfinityCell.class)
public abstract class InfinityCellMixin {
   @Overwrite(remap = false)
   public static long getAsIntMax(AEKey key) {
      return 9223372036854775807L;
   }
}
