package org.gtlcore.gtlcore.config;

public enum AE2CalculationMode {
   LEGACY("legacy", "\u4f7f\u7528\u539f\u7248AE2\u5408\u6210\u7b97\u6cd5"),
   FAST("fast", "\u4f7f\u7528\u5feb\u901f\u5408\u6210\u7b97\u6cd5(\u53ef\u80fd\u5bfc\u81f4\u6781\u7aef\u60c5\u51b5\u4e0b\u8ba1\u7b97\u5931\u8d25)"),
   ULTRA_FAST("ultra_fast", "\u4f7f\u7528\u8d85\u5feb\u901f\u5408\u6210\u7b97\u6cd5(\u4e0d\u4f1a\u5728\u4e0d\u540c\u5408\u6210\u8def\u5f84\u95f4\u5747\u5206)");

   private final String name;
   private final String description;

   AE2CalculationMode(String name, String description) {
      this.name = name;
      this.description = description;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }
}
