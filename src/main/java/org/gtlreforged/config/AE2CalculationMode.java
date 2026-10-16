package org.gtlreforged.config;

import lombok.Getter;

@Getter
public enum AE2CalculationMode {

   LEGACY("legacy", "Use original AE2 crafting algorithm"),
   FAST("fast", "Use fast crafting algorithm (may cause calculation failure in extreme cases)"),
   ULTRA_FAST("ultra_fast", "Use ultra-fast crafting algorithm (will not evenly distribute among different crafting paths)");

   public final String name;
   public final String description;

   AE2CalculationMode(String name, String description) {
      this.name = name;
      this.description = description;
   }

   @Override
   public String toString() {
      return this.name;
   }

}
