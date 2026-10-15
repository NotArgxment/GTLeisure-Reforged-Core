package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeutronAcceleratorPartMachine extends EnergyHatchPartMachine {
   public NeutronAcceleratorPartMachine(IMachineBlockEntity holder, int tier, Object... args) {
      super(holder, tier, IO.IN, 1, args);
   }

   protected NotifiableEnergyContainer createEnergyContainer(Object... args) {
      NotifiableEnergyContainer container = NotifiableEnergyContainer.receiverContainer(this, GTValues.V[this.tier] * 2L, GTValues.V[this.tier], 1L);
      container.setSideInputCondition(s -> s == this.getFrontFacing() && this.isWorkingEnabled());
      container.setCapabilityValidator(s -> s == null || s == this.getFrontFacing());
      return container;
   }

   public long consumeEnergy() {
      return this.isWorkingEnabled() && this.energyContainer.getEnergyStored() > 0L
         ? Math.abs(this.energyContainer.changeEnergy(-this.getMaxEUConsume())) * (10 + ThreadLocalRandom.current().nextInt(11))
         : 0L;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return true;
   }

   public long getMaxEUConsume() {
      return Math.round(GTValues.V[this.tier] * 0.8);
   }
}
