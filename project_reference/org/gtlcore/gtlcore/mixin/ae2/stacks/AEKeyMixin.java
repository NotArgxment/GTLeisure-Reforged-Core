package org.gtlcore.gtlcore.mixin.ae2.stacks;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AEKey.class)
public abstract class AEKeyMixin {
   @Unique
   private CompoundTag gTLCore$tagGenericCache;

   @Shadow(remap = false)
   public abstract CompoundTag toTag();

   @Shadow(remap = false)
   public abstract AEKeyType getType();

   @Overwrite(remap = false)
   public final CompoundTag toTagGeneric() {
      return this.gTLCore$tagGenericCache != null ? this.gTLCore$tagGenericCache : this.gTLCore$saveAndReturnTagGeneric();
   }

   @Unique
   private CompoundTag gTLCore$saveAndReturnTagGeneric() {
      CompoundTag tag = this.toTag();
      tag.m_128359_("#c", this.getType().getId().toString());
      this.gTLCore$tagGenericCache = tag;
      return tag;
   }
}
