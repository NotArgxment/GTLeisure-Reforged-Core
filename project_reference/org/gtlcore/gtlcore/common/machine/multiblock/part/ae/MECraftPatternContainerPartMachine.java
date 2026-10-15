package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.crafting.pattern.EncodedPatternItem;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.trait.AECraft.IMECraftPatternContainer;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.integration.ae2.widget.AEPatternViewExtendSlotWidget;
import org.jetbrains.annotations.NotNull;

public class MECraftPatternContainerPartMachine extends MultiblockPartMachine implements IMECraftPatternContainer, IMachineLife {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MECraftPatternContainerPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   private static final int PATTERNS_PER_ROW = 9;
   @Persisted
   protected final ItemStackTransfer patternInventory;
   @DescSynced
   @Persisted
   private boolean shouldOpen = true;

   public MECraftPatternContainerPartMachine(IMachineBlockEntity holder) {
      super(holder);
      this.patternInventory = new ItemStackTransfer(108);
      this.patternInventory.setFilter(itemStack -> AEUtils.molecularFilter(itemStack, this.getLevel()));
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   @Override
   public BlockPos getBlockPos() {
      return this.getPos();
   }

   @Override
   public int getSlots() {
      return this.patternInventory.getSlots();
   }

   public void onMachineRemoved() {
      this.clearInventory(this.patternInventory);
   }

   public void removedFromController(@NotNull IMultiController controller) {
      super.removedFromController(controller);
      this.shouldOpen = true;
   }

   public void addedToController(@NotNull IMultiController controller) {
      super.addedToController(controller);
      this.shouldOpen = false;
   }

   @NotNull
   public Widget createUIWidget() {
      int cowSize = 14;
      int totalCount = this.patternInventory.getSlots();
      int rowSize = (int)Math.ceil(totalCount / 14.0);
      WidgetGroup group = new WidgetGroup(0, 0, 268, 18 * rowSize + 16);
      int index = 0;

      for (int y = 0; y < rowSize; y++) {
         for (int x = 0; x < 14 && index < totalCount; x++) {
            Widget slot = new AEPatternViewExtendSlotWidget(this.patternInventory, index++, x * 18 + 8, y * 18 + 14)
               .setOccupiedTexture(new IGuiTexture[]{GuiTextures.SLOT})
               .setItemHook(stack -> {
                  if (!stack.m_41619_() && stack.m_41720_() instanceof EncodedPatternItem iep) {
                     ItemStack out = iep.getOutput(stack);
                     if (!out.m_41619_()) {
                        return out;
                     }
                  }

                  return stack;
               })
               .setBackground(new IGuiTexture[]{GuiTextures.SLOT, GuiTextures.PATTERN_OVERLAY});
            group.addWidget(slot);
         }
      }

      return group;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return this.shouldOpen;
   }

   public boolean canShared() {
      return false;
   }

   public ItemStackTransfer getPatternInventory() {
      return this.patternInventory;
   }
}
