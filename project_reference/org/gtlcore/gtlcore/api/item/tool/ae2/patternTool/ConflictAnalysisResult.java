package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import java.util.List;
import org.gtlcore.gtlcore.GTLCore;

public class ConflictAnalysisResult {
   boolean conflict;
   List<String> matchPool;
   List<String> storagePool;
   ConflictAnalysisObj conflictAnalysisObj;

   public ConflictAnalysisResult(boolean conflict, List<String> matchPool, List<String> storagePool, ConflictAnalysisObj conflictAnalysisObj) {
      this.conflict = conflict;
      this.matchPool = matchPool;
      this.storagePool = storagePool;
      this.conflictAnalysisObj = conflictAnalysisObj;
   }

   public void exportToPrint() {
      StringBuilder sb = new StringBuilder();
      sb.append("\n\u914d\u65b9\u7c7b\u578b\uff1a")
         .append(this.conflictAnalysisObj.recipe.getType())
         .append(" --------- \u7535\u8def\uff1a")
         .append(this.conflictAnalysisObj.CIRCUIT)
         .append("\n>>\u6750\u6599\uff1a\n");
      RecipeStackHelper.getInputItemStacksFromRecipe(this.conflictAnalysisObj.recipe)
         .forEach(itemStack -> sb.append("\u7269\u54c1\uff1a").append(RecipeStackHelper.getItemTranslatedName(itemStack)));
      RecipeStackHelper.getInputFluidStacksFromRecipe(this.conflictAnalysisObj.recipe)
         .forEach(fluidStack -> sb.append("\u6d41\u4f53\uff1a").append(RecipeStackHelper.getFluidTranslatedName(fluidStack)));
      sb.append("\n>>\u6210\u54c1\uff1a\n");
      RecipeStackHelper.getOutputItemStacksFromRecipe(this.conflictAnalysisObj.recipe)
         .forEach(itemStack -> sb.append("\u7269\u54c1\uff1a").append(RecipeStackHelper.getItemTranslatedName(itemStack)));
      RecipeStackHelper.getOutputFluidStacksFromRecipe(this.conflictAnalysisObj.recipe)
         .forEach(fluidStack -> sb.append("\u6d41\u4f53\uff1a").append(RecipeStackHelper.getFluidTranslatedName(fluidStack)));
      GTLCore.LOGGER.info(sb);
   }

   public boolean isConflict() {
      return this.conflict;
   }

   public List<String> getMatchPool() {
      return this.matchPool;
   }

   public List<String> getStoragePool() {
      return this.storagePool;
   }

   public ConflictAnalysisObj getConflictAnalysisObj() {
      return this.conflictAnalysisObj;
   }

   public void setConflict(boolean conflict) {
      this.conflict = conflict;
   }

   public void setMatchPool(List<String> matchPool) {
      this.matchPool = matchPool;
   }

   public void setStoragePool(List<String> storagePool) {
      this.storagePool = storagePool;
   }

   public void setConflictAnalysisObj(ConflictAnalysisObj conflictAnalysisObj) {
      this.conflictAnalysisObj = conflictAnalysisObj;
   }

   @Override
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof ConflictAnalysisResult other)) {
         return false;
      } else {
         if (!other.canEqual(this)) {
            return false;
         }

         if (this.isConflict() != other.isConflict()) {
            return false;
         }

         Object this$matchPool = this.getMatchPool();
         Object other$matchPool = other.getMatchPool();
         if (this$matchPool == null ? other$matchPool == null : this$matchPool.equals(other$matchPool)) {
            Object this$storagePool = this.getStoragePool();
            Object other$storagePool = other.getStoragePool();
            if (this$storagePool == null ? other$storagePool == null : this$storagePool.equals(other$storagePool)) {
               Object this$conflictAnalysisObj = this.getConflictAnalysisObj();
               Object other$conflictAnalysisObj = other.getConflictAnalysisObj();
               return this$conflictAnalysisObj == null ? other$conflictAnalysisObj == null : this$conflictAnalysisObj.equals(other$conflictAnalysisObj);
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   protected boolean canEqual(Object other) {
      return other instanceof ConflictAnalysisResult;
   }

   @Override
   public int hashCode() {
      int PRIME = 59;
      int result = 1;
      result = result * 59 + (this.isConflict() ? 79 : 97);
      Object $matchPool = this.getMatchPool();
      result = result * 59 + ($matchPool == null ? 43 : $matchPool.hashCode());
      Object $storagePool = this.getStoragePool();
      result = result * 59 + ($storagePool == null ? 43 : $storagePool.hashCode());
      Object $conflictAnalysisObj = this.getConflictAnalysisObj();
      return result * 59 + ($conflictAnalysisObj == null ? 43 : $conflictAnalysisObj.hashCode());
   }

   @Override
   public String toString() {
      return "ConflictAnalysisResult(conflict="
         + this.isConflict()
         + ", matchPool="
         + this.getMatchPool()
         + ", storagePool="
         + this.getStoragePool()
         + ", conflictAnalysisObj="
         + this.getConflictAnalysisObj()
         + ")";
   }
}
