package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.machine.multiblock.part.BlockBusPartMachine;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.NotNull;

public class BlockConversionRoomMachine extends StorageMachine {
   private static final List<int[]> poses1 = new ArrayList<>();
   private static final List<int[]> poses2 = new ArrayList<>();
   private static final Map<Block, Block> covRecipe = new HashMap<>();
   private final int am;
   private final List<int[]> poses;
   private Set<BlockBusPartMachine> blockBusPartMachine = new HashSet<>();

   public BlockConversionRoomMachine(IMachineBlockEntity holder, boolean isLarge) {
      super(holder, 1);
      this.am = isLarge ? 64 : 4;
      this.poses = isLarge ? poses2 : poses1;
   }

   @Override
   protected boolean filter(@NotNull ItemStack itemStack) {
      return itemStack.m_41720_() == GTLItems.CONVERSION_SIMULATE_CARD.get() || itemStack.m_41720_() == GTLItems.FAST_CONVERSION_SIMULATE_CARD.get();
   }

   public void onStructureFormed() {
      super.onStructureFormed();

      for (IMultiPart part : this.getParts()) {
         if (part instanceof BlockBusPartMachine busPartMachine) {
            this.blockBusPartMachine.add(busPartMachine);
         }
      }
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.blockBusPartMachine = null;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L) {
         int amount = this.getTier() * this.am - 7;
         if (this.blockBusPartMachine != null && !this.getMachineStorageItem().m_41619_()) {
            int a = amount;
            if (this.getMachineStorageItem().m_150930_((Item)GTLItems.FAST_CONVERSION_SIMULATE_CARD.get())) {
               a = 15552;
            }

            for (BlockBusPartMachine busPartMachine : this.blockBusPartMachine) {
               ItemStackTransfer stackTransfer = busPartMachine.getInventory().storage;

               for (int i = 0; a > 0 && i < stackTransfer.getSlots(); i++) {
                  ItemStack itemStack = stackTransfer.getStackInSlot(i);
                  if (itemStack.m_41720_() instanceof BlockItem blockItem && covRecipe.containsKey(blockItem.m_40614_())) {
                     int count = itemStack.m_41613_();
                     a -= count;
                     stackTransfer.setStackInSlot(i, new ItemStack(covRecipe.get(blockItem.m_40614_()).m_5456_(), count));
                  }
               }
            }
         } else {
            Level level = this.getLevel();
            if (level != null) {
               int[] pos = new int[0];

               for (int i = 0; i < amount; i++) {
                  int[] pos_0 = this.poses.get((int)(Math.random() * this.poses.size()));
                  if (pos_0 != pos) {
                     pos = pos_0;
                     BlockPos blockPos = this.getPos().m_7918_(pos[0], pos[1], pos[2]);
                     Block block = level.m_8055_(blockPos).m_60734_();
                     if (covRecipe.containsKey(block)) {
                        level.m_46597_(blockPos, covRecipe.get(block).m_49966_());
                     }
                  } else {
                     i--;
                  }
               }
            }
         }
      }

      return value;
   }

   public int getConversionCount() {
      return !this.getMachineStorageItem().m_41619_() && this.getMachineStorageItem().m_150930_((Item)GTLItems.FAST_CONVERSION_SIMULATE_CARD.get())
         ? 81 * this.am
         : this.getTier() * this.am - 7;
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      textList.add(Component.m_237110_("gtceu.machine.block_conversion_room.am", new Object[]{this.getConversionCount()}));
   }

   static {
      for (int i = -2; i <= 2; i++) {
         for (int j = -1; j >= -5; j--) {
            for (int k = -2; k <= 2; k++) {
               poses1.add(new int[]{i, j, k});
            }
         }
      }

      for (int i = -4; i <= 4; i++) {
         for (int j = -1; j >= -7; j--) {
            for (int k = -4; k <= 4; k++) {
               poses2.add(new int[]{i, j, k});
            }
         }
      }

      covRecipe.put(Blocks.f_50453_, Registries.getBlock("kubejs:essence_block"));
      covRecipe.put(Blocks.f_49999_, Blocks.f_50695_);
      covRecipe.put(Blocks.f_50001_, Blocks.f_50686_);
      covRecipe.put(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.Calcium), Blocks.f_50453_);
      covRecipe.put(Blocks.f_152544_, Blocks.f_220855_);
      covRecipe.put(Blocks.f_50440_, Blocks.f_152544_);
      covRecipe.put(Registries.getBlock("kubejs:infused_obsidian"), Registries.getBlock("kubejs:draconium_block_charged"));
   }
}
