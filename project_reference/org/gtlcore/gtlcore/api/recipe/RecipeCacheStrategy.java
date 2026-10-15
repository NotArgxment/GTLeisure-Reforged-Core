package org.gtlcore.gtlcore.api.recipe;

public enum RecipeCacheStrategy {
   FULL_CACHE(true, true),
   HANDLE_PART_CACHE_ONLY(true, false),
   NO_CACHE(false, false);

   public final boolean cacheToHandlePartMap;
   public final boolean cacheToMEInternal;

   RecipeCacheStrategy(boolean cacheToHandlePartMap, boolean cacheToMEInternal) {
      this.cacheToHandlePartMap = cacheToHandlePartMap;
      this.cacheToMEInternal = cacheToMEInternal;
   }
}
