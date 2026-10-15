package org.gtlcore.gtlcore.common.machine.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LightningRodMachine extends TieredEnergyMachine {
   @Nullable
   protected TickableSubscription energySubs;

   public LightningRodMachine(IMachineBlockEntity holder, int tier, Object... args) {
      super(holder, tier, args);
      this.energySubs = this.subscribeServerTick(this.energySubs, this::checkEnergy);
   }

   protected void checkEnergy() {
      if (this.getOffsetTimer() % 10L == 0L) {
         BlockState state = Objects.requireNonNull(this.getLevel()).m_8055_(this.getPos().m_7918_(0, 1, 0));
         if (state.m_60734_() == Blocks.f_152587_
            && state.m_61143_(BlockStateProperties.f_61372_) == Direction.UP
            && (Boolean)state.m_61143_(LightningRodBlock.f_153703_)) {
            if (this.energyContainer.getEnergyStored() == this.getCharge()) {
               this.doExplosion(this.getTier());
            } else {
               this.energyContainer.addEnergy(this.getCharge() / 4L);
            }

            if (Math.random() > 0.9) {
               this.getLevel().m_46597_(this.getPos().m_7918_(0, 1, 0), Blocks.f_50016_.m_49966_());
            }
         }
      }
   }

   protected boolean isEnergyEmitter() {
      return true;
   }

   protected long getMaxInputOutputAmperage() {
      return 512L;
   }

   private long getCharge() {
      return (long)(48828.0 * Math.pow(4.0, this.getTier()));
   }

   protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
      return NotifiableEnergyContainer.emitterContainer(this, this.getCharge(), GTValues.V[this.getTier() - 1], this.getMaxInputOutputAmperage());
   }

   public int tintColor(int index) {
      return index == 2 ? GTValues.VC[this.getTier()] : super.tintColor(index);
   }
}
