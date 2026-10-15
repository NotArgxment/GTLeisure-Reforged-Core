package org.gtlcore.gtlcore.api.pattern.util;

import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;

public class SimpleValueContainer<T> implements IValueContainer<T> {
   protected T value;
   private final TriFunction<T, Block, Object, T> function;

   public SimpleValueContainer(T initialValue, TriFunction<T, Block, Object, T> function) {
      this.value = initialValue;
      this.function = function;
   }

   @Override
   public void operate(Block block, Object data) {
      this.value = (T)this.function.apply(this.value, block, data);
   }

   @Override
   public T getValue() {
      return this.value;
   }
}
