package org.gtlcore.gtlcore.mixin.gtm.cover;

import com.gregtechceu.gtceu.api.capability.ICoverable;
import com.gregtechceu.gtceu.api.cover.CoverBehavior;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.common.cover.InfiniteWaterCover;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.fluid.IFluidTransfer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(InfiniteWaterCover.class)
public abstract class InfiniteWaterCoverMixin extends CoverBehavior {
   public InfiniteWaterCoverMixin(CoverDefinition definition, ICoverable coverHolder, Direction attachedSide) {
      super(definition, coverHolder, attachedSide);
   }

   @Overwrite(remap = false)
   public void update() {
      if (this.coverHolder.getOffsetTimer() % 20L == 0L) {
         IFluidTransfer fluidHandler = FluidTransferHelper.getFluidTransfer(this.coverHolder.getLevel(), this.coverHolder.getPos(), this.attachedSide);
         if (fluidHandler != null) {
            fluidHandler.fill(FluidStack.create(Fluids.f_76193_, 2147483647L), false);
         }
      }
   }
}
