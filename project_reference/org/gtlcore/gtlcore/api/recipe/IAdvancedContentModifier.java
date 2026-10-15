package org.gtlcore.gtlcore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

public interface IAdvancedContentModifier {
   void setDivision(long var1, long var3);

   static ContentModifier preciseDivision(long numerator, long denominator) {
      ContentModifier modifier = new ContentModifier(0.0, 0.0);
      ((IAdvancedContentModifier)modifier).setDivision(numerator, denominator);
      return modifier;
   }
}
