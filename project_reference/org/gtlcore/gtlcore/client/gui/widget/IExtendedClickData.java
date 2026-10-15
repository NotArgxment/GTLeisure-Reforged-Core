package org.gtlcore.gtlcore.client.gui.widget;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public interface IExtendedClickData {
   void setUUID(@Nullable UUID var1);

   @Nullable
   UUID getUUID();
}
