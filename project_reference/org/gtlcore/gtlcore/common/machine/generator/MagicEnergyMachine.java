package org.gtlcore.gtlcore.common.machine.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.mojang.blaze3d.MethodsReturnNonnullByDefault;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicEnergyMachine extends TieredEnergyMachine {
   @Nullable
   protected TickableSubscription energySubs;

   public MagicEnergyMachine(IMachineBlockEntity holder, int tier, Object... args) {
      super(holder, tier, args);
      this.energySubs = this.subscribeServerTick(this.energySubs, this::checkEnergy);
   }

   protected void checkEnergy() {
      if (this.getOffsetTimer() % 20L == 0L
         && !Objects.requireNonNull(this.getLevel())
            .m_6443_(
               EndCrystal.class,
               AABB.m_165882_(new Vec3(this.getPos().m_123341_(), this.getPos().m_123342_() + 1, this.getPos().m_123343_()), 1.0, 1.0, 1.0),
               e -> true
            )
            .isEmpty()) {
         this.energyContainer.addEnergy(GTValues.V[this.getTier() + 4]);
      }
   }

   protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
      long tierVoltage = GTValues.V[this.getTier()];
      return NotifiableEnergyContainer.emitterContainer(this, tierVoltage * 512L, tierVoltage, this.getMaxInputOutputAmperage());
   }

   protected boolean isEnergyEmitter() {
      return true;
   }

   protected long getMaxInputOutputAmperage() {
      return 16L;
   }
}
