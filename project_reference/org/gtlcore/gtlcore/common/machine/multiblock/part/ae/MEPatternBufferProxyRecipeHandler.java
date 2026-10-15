package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import org.gtlcore.gtlcore.api.machine.trait.IMERecipeHandlerTrait;

public class MEPatternBufferProxyRecipeHandler<T extends Predicate<S>, S> extends NotifiableMERecipeHandlerTrait<T, S> {
   private IMERecipeHandlerTrait<T, S> handler;
   private final RecipeCapability<T> capability;

   public MEPatternBufferProxyRecipeHandler(MetaMachine machine, RecipeCapability<T> capability) {
      super(machine);
      this.capability = capability;
   }

   @Override
   public RecipeCapability<T> getCapability() {
      return this.capability;
   }

   @Override
   public IO getIo() {
      return this.handler != null ? this.handler.getIo() : IO.NONE;
   }

   @Override
   public Set<Integer> getActiveSlots() {
      return this.handler != null ? this.handler.getActiveSlots() : Collections.emptySet();
   }

   @Override
   public Int2ObjectMap<List<S>> getActiveAndUnCachedSlotsLimitContentsMap() {
      return this.handler != null ? this.handler.getActiveAndUnCachedSlotsLimitContentsMap() : Int2ObjectMaps.emptyMap();
   }

   @Override
   public Object2LongMap<S> getStackMapFromFirstAvailableSlot(IntCollection slots) {
      return this.handler != null ? this.handler.getStackMapFromFirstAvailableSlot(slots) : Object2LongMaps.emptyMap();
   }

   @Override
   public boolean meHandleRecipeInner(GTRecipe recipe, Object2LongMap<T> left, boolean simulate, int trySlot) {
      return this.handler != null ? this.handler.meHandleRecipeInner(recipe, left, simulate, trySlot) : false;
   }

   @Override
   public List<T> meHandleRecipeOutputInner(List<T> contents, boolean simulate) {
      return this.handler != null ? this.handler.meHandleRecipeOutputInner(contents, simulate) : contents;
   }

   @Override
   public void prepareMEHandleContents(GTRecipe recipe, List<T> left, boolean simulate) {
      if (this.handler != null) {
         this.handler.prepareMEHandleContents(recipe, left, simulate);
      }
   }

   @Override
   public Object2LongMap<T> getPreparedMEHandleContents() {
      return this.handler != null ? this.handler.getPreparedMEHandleContents() : Object2LongMaps.emptyMap();
   }

   public void setHandler(IMERecipeHandlerTrait<T, S> handler) {
      this.handler = handler;
   }
}
