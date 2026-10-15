package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RotorHatchPartMachine extends MultiblockPartMachine implements IMachineModifyDrops, IInteractedMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      RotorHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   private final NotifiableItemStackHandler inventory;

   public RotorHatchPartMachine(IMachineBlockEntity holder, Object... args) {
      super(holder);
      this.inventory = this.createMachineStorage(args);
   }

   protected NotifiableItemStackHandler createMachineStorage(Object... args) {
      NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
      storage.setFilter(i -> TurbineRotorBehaviour.getBehaviour(i) != null);
      return storage;
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public Widget createUIWidget() {
      return BallHatchPartMachine.createSLOTWidget(this.inventory);
   }

   public void onDrops(List<ItemStack> list) {
      this.clearInventory(this.inventory.storage);
   }

   public NotifiableItemStackHandler getInventory() {
      return this.inventory;
   }
}
