package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftSpeedCore;

public class MECraftSpeedCorePartMachine extends MultiblockPartMachine implements IMECraftSpeedCore {
   public MECraftSpeedCorePartMachine(IMachineBlockEntity holder) {
      super(holder);
   }

   @Override
   public int getSpeedTier() {
      return 8;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return false;
   }
}
