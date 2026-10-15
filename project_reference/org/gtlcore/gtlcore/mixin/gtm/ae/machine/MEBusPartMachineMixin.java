package org.gtlcore.gtlcore.mixin.gtm.ae.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import java.util.EnumSet;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IModifiableSyncOffset;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Implements(@Interface(iface = IModifiableSyncOffset.class, prefix = "gTLCore$"))
@Mixin(MEBusPartMachine.class)
public abstract class MEBusPartMachineMixin extends ItemBusPartMachine implements IGridConnectedMachine {
   @Unique
   @Persisted
   protected int gTLCore$syncOffset = 0;

   public MEBusPartMachineMixin(IMachineBlockEntity holder, int tier, IO io, Object... args) {
      super(holder, tier, io, args);
   }

   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      if (tag.m_128469_("ForgeData").m_128471_("isAllFacing")) {
         this.getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
      }
   }

   public boolean shouldSyncME() {
      return this.self().getOffsetTimer() % (this.gTLCore$syncOffset == 0 ? ME_UPDATE_INTERVAL : this.gTLCore$syncOffset) == 0L;
   }

   @Unique
   public int gTLCore$getOffset() {
      return this.gTLCore$syncOffset;
   }

   @Unique
   public void gTLCore$setOffset(int offset) {
      this.gTLCore$syncOffset = offset;
   }
}
