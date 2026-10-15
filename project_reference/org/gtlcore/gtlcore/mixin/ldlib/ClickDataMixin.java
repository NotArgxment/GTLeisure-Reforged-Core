package org.gtlcore.gtlcore.mixin.ldlib;

import com.lowdragmc.lowdraglib.gui.util.ClickData;
import java.util.UUID;
import org.gtlcore.gtlcore.client.gui.widget.IExtendedClickData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(prefix = "gTLCore$", iface = IExtendedClickData.class))
@Mixin(ClickData.class)
public abstract class ClickDataMixin {
   @Unique
   @Nullable
   private UUID gTLCore$uuid;

   @Unique
   public void gTLCore$setUUID(@Nullable UUID uuid) {
      this.gTLCore$uuid = uuid;
   }

   @Nullable
   public UUID gTLCore$getUUID() {
      return this.gTLCore$uuid;
   }
}
