package org.gtlcore.gtlcore.common.block;

import appeng.block.crafting.CraftingUnitBlock;
import appeng.block.crafting.ICraftingUnitType;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.Item;
import org.gtlcore.gtlcore.common.data.GTLBlocks;

public enum CraftingUnitType implements ICraftingUnitType {
   STORAGE_1M(1, "1m_storage"),
   STORAGE_4M(4, "4m_storage"),
   STORAGE_16M(16, "16m_storage"),
   STORAGE_64M(64, "64m_storage"),
   STORAGE_256M(256, "256m_storage"),
   STORAGE_MAX(-1, "max_storage");

   private final int storageMb;
   private final String affix;

   CraftingUnitType(int storageMb, String affix) {
      this.storageMb = storageMb;
      this.affix = affix;
   }

   public long getStorageBytes() {
      return this.storageMb == -1 ? 9223372036854775807L : 1048576L * this.storageMb;
   }

   public int getAcceleratorThreads() {
      return 0;
   }

   public BlockEntry<CraftingUnitBlock> getDefinition() {
      return switch (this) {
         case STORAGE_1M -> GTLBlocks.CRAFTING_STORAGE_1M;
         case STORAGE_4M -> GTLBlocks.CRAFTING_STORAGE_4M;
         case STORAGE_16M -> GTLBlocks.CRAFTING_STORAGE_16M;
         case STORAGE_64M -> GTLBlocks.CRAFTING_STORAGE_64M;
         case STORAGE_256M -> GTLBlocks.CRAFTING_STORAGE_256M;
         case STORAGE_MAX -> GTLBlocks.CRAFTING_STORAGE_MAX;
      };
   }

   public Item getItemFromType() {
      return this.getDefinition().m_5456_();
   }

   public String getAffix() {
      return this.affix;
   }
}
