package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.gtlcore.gtlcore.GTLCore;

public class ConflictAnalysisManager {
   public List<ConflictAnalysisObj> conflictAnalysisObjs;

   public ConflictAnalysisManager(List<GTRecipe> recipes) {
      this.conflictAnalysisObjs = new ArrayList<>();
      recipes.forEach(recipe -> this.conflictAnalysisObjs.add(new ConflictAnalysisObj(recipe)));
   }

   public ConflictAnalysisManager(List<ConflictAnalysisObj> conflictAnalysisObjs, boolean dummy) {
      this.conflictAnalysisObjs = conflictAnalysisObjs;
   }

   public static ConflictAnalysisResult findConflict(ConflictAnalysisObj conflictAnalysisObj, List<ConflictAnalysisObj> conflictAnalysisObjs) {
      boolean conflict_k_14223 = false;
      conflictAnalysisObjs = conflictAnalysisObjs.stream().filter(conflictAnalysisObj1 -> !conflictAnalysisObj1.equals(conflictAnalysisObj)).toList();
      List<String> matchPool = Stream.of(conflictAnalysisObj.inputFluidIds, conflictAnalysisObj.inputItemIds).flatMap(Collection::stream).toList();
      List<String> storagePool = new ArrayList<>();
      conflictAnalysisObjs.forEach(conflictAnalysisObj1 -> {
         storagePool.addAll(conflictAnalysisObj1.inputFluidIds);
         storagePool.addAll(conflictAnalysisObj1.inputItemIds);
      });
      if (new HashSet<>(storagePool).containsAll(matchPool)) {
         conflict_k_14223 = true;
      }

      return new ConflictAnalysisResult(conflict_k_14223, matchPool, storagePool, conflictAnalysisObj);
   }

   public List<ConflictAnalysisResult> useFindConflictForAll(Number circuit) {
      List<ConflictAnalysisResult> conflictAnalysisResultList = new ArrayList<>();
      List<ConflictAnalysisObj> conflictAnalysisObjList = this.conflictAnalysisObjs
         .stream()
         .filter(conflictAnalysisObj -> Objects.equals(conflictAnalysisObj.CIRCUIT, circuit) || conflictAnalysisObj.CIRCUIT.equals(0))
         .toList();
      conflictAnalysisObjList.forEach(conflictAnalysisObj -> {
         ConflictAnalysisResult conflictAnalysisResult = findConflict(conflictAnalysisObj, conflictAnalysisObjList);
         if (conflictAnalysisResult.conflict) {
            conflictAnalysisResultList.add(conflictAnalysisResult);
         }
      });
      return conflictAnalysisResultList;
   }

   public void printAllObject() {
      this.conflictAnalysisObjs.forEach(conflictAnalysisObj -> GTLCore.LOGGER.info(conflictAnalysisObj.toString()));
   }
}
