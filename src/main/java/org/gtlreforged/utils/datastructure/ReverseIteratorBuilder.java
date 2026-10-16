package org.gtlreforged.utils.datastructure;

import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectIterators;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class ReverseIteratorBuilder {
   @NotNull
   public static <T> ObjectIterator<T> build(@NotNull ReferenceSortedSet<T> set) {
      return (ObjectIterator<T>)(set.isEmpty()
         ? ObjectIterators.emptyIterator()
         : (set.size() == 1 ? ObjectIterators.singleton(set.first()) : new ReverseIterator<>(set)));
   }

   private static class ReverseIterator<T> implements ObjectIterator<T> {
      private final ObjectBidirectionalIterator<T> delegate;

      public ReverseIterator(ReferenceSortedSet<T> set) {
         this.delegate = set.iterator(set.last());
      }

      public boolean hasNext() {
         return this.delegate.hasPrevious();
      }

      public T next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return (T)this.delegate.previous();
         }
      }

      public void remove() {
         this.delegate.remove();
      }
   }
}
