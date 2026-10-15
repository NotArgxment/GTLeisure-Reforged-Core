package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftParallelCore;

public class MECraftParallelCorePartMachine extends MultiblockPartMachine implements IMECraftParallelCore {
   public static final int PARALLEL = 4194304;

   public MECraftParallelCorePartMachine(IMachineBlockEntity holder) {
      super(holder);
   }

   @Override
   public int getParallel() {
      return 4194304;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return false;
   }
}
