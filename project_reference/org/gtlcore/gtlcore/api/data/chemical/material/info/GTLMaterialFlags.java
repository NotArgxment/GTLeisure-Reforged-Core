package org.gtlcore.gtlcore.api.data.chemical.material.info;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag.Builder;

public class GTLMaterialFlags {
   public static final MaterialFlag GENERATE_NANOSWARM = new Builder("generate_nanoswarm").build();
   public static final MaterialFlag GENERATE_MILLED = new Builder("generate_milled").build();
}
