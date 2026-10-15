package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineModifyDrops;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.data.GTDamageTypes;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.utils.Registries;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BallHatchPartMachine extends MultiblockPartMachine implements IMachineModifyDrops, IInteractedMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      BallHatchPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   @DescSynced
   @RequireRerender
   private boolean isWorking;
   @Persisted
   private final NotifiableItemStackHandler inventory;

   public BallHatchPartMachine(IMachineBlockEntity holder, Object... args) {
      super(holder);
      this.inventory = this.createInventoryItemHandler(args);
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   protected NotifiableItemStackHandler createInventoryItemHandler(Object... args) {
      NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.BOTH);
      storage.setFilter(i -> Registries.getItemId(i).contains("kubejs:grindball_"));
      return storage;
   }

   public static Widget createSLOTWidget(NotifiableItemStackHandler inventory) {
      WidgetGroup group = new WidgetGroup(0, 0, 34, 34);
      WidgetGroup container = new WidgetGroup(4, 4, 26, 26);
      container.addWidget(new SlotWidget(inventory.storage, 0, 4, 4, true, true).setBackground(new IGuiTexture[]{GuiTextures.SLOT}));
      group.addWidget(container);
      return group;
   }

   public Widget createUIWidget() {
      return createSLOTWidget(this.inventory);
   }

   public boolean beforeWorking(IWorkableMultiController controller) {
      this.isWorking = true;
      return super.beforeWorking(controller);
   }

   public boolean afterWorking(IWorkableMultiController controller) {
      this.isWorking = false;
      return super.afterWorking(controller);
   }

   public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (!this.isRemote() && this.isWorking && !player.m_7500_()) {
         player.m_6469_(GTDamageTypes.TURBINE.source(level), 40.0F);
         return InteractionResult.FAIL;
      } else {
         return InteractionResult.PASS;
      }
   }

   public void onDrops(List<ItemStack> list) {
      if (!this.isWorking) {
         this.clearInventory(this.inventory.storage);
      }
   }

   public boolean isWorking() {
      return this.isWorking;
   }

   public NotifiableItemStackHandler getInventory() {
      return this.inventory;
   }
}
