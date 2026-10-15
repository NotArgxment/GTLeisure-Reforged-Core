package org.gtlcore.gtlcore.api.machine.trait.MEPart;

import org.jetbrains.annotations.NotNull;

public interface IMEPatternPartMachine extends IMEFilterIOPartMachine {
   @NotNull
   IMEPatternTrait getMETrait();
}
