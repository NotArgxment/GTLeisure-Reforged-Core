package org.gtlcore.gtlcore.mixin.gtm.api.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;
import java.util.concurrent.locks.Lock;
import net.minecraft.server.level.ServerLevel;
import org.gtlcore.gtlcore.api.machine.trait.ICheckPatternMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MultiblockControllerMachine.class)
public abstract class MultiblockControllerMachineMixin extends MetaMachine implements IMultiController, ICheckPatternMachine {
   @Unique
   private int gtlcore$time = 1;
   @Shadow(remap = false)
   protected boolean isFormed;

   @Shadow(remap = false)
   public abstract Lock getPatternLock();

   @Shadow(remap = false)
   public abstract void setFlipped(boolean var1);

   public MultiblockControllerMachineMixin(IMachineBlockEntity holder) {
      super(holder);
   }

   public boolean checkPattern() {
      if (this.gtlcore$time < 1) {
         BlockPattern pattern = this.getPattern();
         if (pattern != null && pattern.checkPatternAt(this.getMultiblockState(), false)) {
            this.gtlcore$time = 0;
            return true;
         }

         if (this.hasButton()) {
            this.gtlcore$time = 10;
         }
      } else {
         this.gtlcore$time--;
      }

      return false;
   }

   @Overwrite(remap = false)
   public void asyncCheckPattern(long periodID) {
      if ((this.getMultiblockState().hasError() || !this.isFormed)
         && (this.getHolder().getOffset() + periodID) % 4L == 0L
         && this.checkPatternWithTryLock()
         && this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().execute(() -> {
            this.getPatternLock().lock();
            this.setFlipped(this.getMultiblockState().isNeededFlip());
            this.onStructureFormed();
            MultiblockWorldSavedData mwsd = MultiblockWorldSavedData.getOrCreate(serverLevel);
            mwsd.addMapping(this.getMultiblockState());
            mwsd.removeAsyncLogic(this);
            this.getPatternLock().unlock();
         });
      }
   }

   @Override
   public void setTime(int time) {
      this.gtlcore$time = time;
   }

   @Override
   public int getTime() {
      return this.gtlcore$time;
   }
}
