package org.gtlcore.gtlcore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IModularMachineModule<H extends IModularMachineHost<H>, M extends IModularMachineModule<H, M>> {
   @Nullable
   BlockPos getHostPosition();

   void setHostPosition(@Nullable BlockPos var1);

   @Nullable
   H getHost();

   void setHost(@Nullable H var1);

   @NotNull
   Class<H> getHostType();

   BlockPos[] getHostScanPositions();

   Level getLevel();

   boolean isFormed();

   default void connectToHost(@NotNull H host) {
      this.setHost(host);
      this.setHostPosition(host.getPos());
      M self = (M)this;
      host.addModule(self);
      this.onConnected(host);
   }

   default void removeFromHost(@Nullable H host) {
      this.setHostPosition(null);
      this.setHost(null);
      if (host != null) {
         M self = (M)this;
         host.removeModule(self);
      }

      this.onDisconnected();
   }

   default boolean findAndConnectToHost() {
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         BlockPos savedPos = this.getHostPosition();
         if (savedPos != null && serverLevel.m_7702_(savedPos) instanceof IMachineBlockEntity imbe) {
            MetaMachine machine = imbe.getMetaMachine();
            if (this.isValidHost(machine)) {
               H host = (H)machine;
               this.connectToHost(host);
               return true;
            }
         }

         BlockPos[] positions = this.getHostScanPositions();

         for (BlockPos pos : positions) {
            if (serverLevel.m_7702_(pos) instanceof IMachineBlockEntity imbe) {
               MetaMachine machine = imbe.getMetaMachine();
               if (this.isValidHost(machine)) {
                  H host = (H)machine;
                  this.connectToHost(host);
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   default boolean isValidHost(MetaMachine machine) {
      if (machine == null) {
         return false;
      } else if (!this.getHostType().isInstance(machine)) {
         return false;
      } else {
         return machine instanceof IModularMachineHost<?> host ? host.isFormed() : false;
      }
   }

   default boolean isConnectedToHost() {
      return this.getHost() != null;
   }

   default void onConnected(@NotNull H host) {
   }

   default void onDisconnected() {
   }
}
