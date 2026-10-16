package org.gtlreforged.utils.datastructure;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeHandlePart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FirstFlagRecipePartSet {
   private final ReferenceSortedSet<@NotNull IRecipeHandlePart> handlers = new ReferenceLinkedOpenHashSet();

   public void addOrSetActive(@NotNull IRecipeHandlePart handler) {
      if (this.handlers.size() > 1) {
         this.handlers.remove(handler);
      }

      this.handlers.add(handler);
   }

   @NotNull
   public ReferenceSet<IRecipeHandlePart> getAll() {
      return this.handlers;
   }

   @Nullable
   public IRecipeHandlePart getActive() {
      return this.handlers.isEmpty() ? null : (IRecipeHandlePart)this.handlers.last();
   }

   @NotNull
   public ObjectIterator<IRecipeHandlePart> getReverseIterator() {
      return ReverseIteratorBuilder.build(this.handlers);
   }

   public boolean isEmpty() {
      return this.handlers.isEmpty();
   }

   public void clear() {
      this.handlers.clear();
   }
}
