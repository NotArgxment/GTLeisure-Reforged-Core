package org.gtlcore.gtlcore.common.data;

import com.gregtechceu.gtceu.api.sound.SoundEntry;
import com.gregtechceu.gtceu.common.registry.GTRegistration;

public class GTLSoundEntries {
   public static final SoundEntry DTPF = GTRegistration.REGISTRATE.sound("dtpf").build();
   public static final SoundEntry FUSIONLOOP = GTRegistration.REGISTRATE.sound("fusionloop").build();

   public static void init() {
   }
}
