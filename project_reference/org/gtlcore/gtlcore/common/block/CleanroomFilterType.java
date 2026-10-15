package org.gtlcore.gtlcore.common.block;

import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import org.gtlcore.gtlcore.api.machine.multiblock.GTLCleanroomType;
import org.jetbrains.annotations.NotNull;

public enum CleanroomFilterType implements IFilterType {
   FILTER_CASING_LAW("law_filter_casing", GTLCleanroomType.LAW_CLEANROOM);

   private final String name;
   private final CleanroomType cleanroomType;

   CleanroomFilterType(String name, CleanroomType cleanroomType) {
      this.name = name;
      this.cleanroomType = cleanroomType;
   }

   @NotNull
   public String m_7912_() {
      return this.name;
   }

   @NotNull
   @Override
   public String toString() {
      return this.m_7912_();
   }

   public CleanroomType getCleanroomType() {
      return this.cleanroomType;
   }
}
