package org.gtlcore.gtlcore.integration.emi;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.Comparison;
import dev.emi.emi.api.stack.EmiStack;

@EmiEntrypoint
public class GTLEMIPlugin implements EmiPlugin {
   public void register(EmiRegistry registry) {
      registry.setDefaultComparison(EmiStack.of(GTItems.TURBINE_ROTOR.m_5456_()), Comparison.compareNbt());
      registry.setDefaultComparison(EmiStack.of(GTItems.INTEGRATED_CIRCUIT.m_5456_()), Comparison.compareNbt());

      for (int i = 0; i <= 32; i++) {
         registry.addEmiStack(EmiStack.of(IntCircuitBehaviour.stack(i)));
      }
   }
}
