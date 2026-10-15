package org.gtlcore.gtlcore.mixin.ae2.stacks;

import appeng.api.stacks.GenericStack;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GenericStack.class)
public abstract class GenericStackMixin {
   @Overwrite(remap = false)
   public static CompoundTag writeTag(@Nullable GenericStack stack) {
      if (stack == null) {
         return new CompoundTag();
      }

      CompoundTag tag = stack.what().toTagGeneric().m_6426_();
      tag.m_128356_("#", stack.amount());
      return tag;
   }
}
