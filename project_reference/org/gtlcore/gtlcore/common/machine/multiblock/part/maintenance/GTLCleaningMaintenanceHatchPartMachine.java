package org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance;

import com.gregtechceu.gtceu.api.capability.ICleanroomReceiver;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ICleanroomProvider;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GTLCleaningMaintenanceHatchPartMachine extends AutoMaintenanceHatchPartMachine {
   ICleanroomProvider cleanroomTypes;

   public GTLCleaningMaintenanceHatchPartMachine(IMachineBlockEntity metaTileEntityId, ICleanroomProvider cleanroomTypes) {
      super(metaTileEntityId);
      this.cleanroomTypes = cleanroomTypes;
   }

   public void addedToController(IMultiController controller) {
      super.addedToController(controller);
      if (controller instanceof ICleanroomReceiver receiver) {
         receiver.setCleanroom(this.cleanroomTypes);
      }
   }

   public void removedFromController(IMultiController controller) {
      super.removedFromController(controller);
      if (controller instanceof ICleanroomReceiver receiver && receiver.getCleanroom() == this.cleanroomTypes) {
         receiver.setCleanroom(null);
      }
   }

   public int getTier() {
      if (this.cleanroomTypes == ICleaningRoom.STERILE_DUMMY_CLEANROOM) {
         return 7;
      } else {
         return this.cleanroomTypes == ICleaningRoom.LAW_DUMMY_CLEANROOM ? 10 : 3;
      }
   }
}
