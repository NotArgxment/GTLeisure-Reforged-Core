package org.gtlcore.gtlcore.api.pattern.util;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface IValueContainer<T> {
   void operate(Block var1, Object var2);

   T getValue();

   static IValueContainer<Object> noop() {
      return new IValueContainer.NOOP();
   }

   class NOOP implements IValueContainer<Object> {
      @Override
      public void operate(Block block, Object data) {
      }

      @Nullable
      @Override
      public Object getValue() {
         return null;
      }
   }
}
