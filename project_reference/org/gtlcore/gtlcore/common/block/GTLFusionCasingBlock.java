package org.gtlcore.gtlcore.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.IFusionCasingType;
import com.gregtechceu.gtceu.api.block.MaterialBlock;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.block.FusionCasingBlock;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.jetbrains.annotations.NotNull;

public class GTLFusionCasingBlock extends FusionCasingBlock {
   public GTLFusionCasingBlock(Properties properties, IFusionCasingType casingType) {
      super(properties, casingType);
   }

   public static Block getCompressedCoilState(int tier) {
      return switch (tier) {
         case 6 -> (ActiveBlock)GTLBlocks.IMPROVED_SUPERCONDUCTOR_COIL.get();
         case 7 -> (ActiveBlock)GTLBlocks.COMPRESSED_FUSION_COIL.get();
         case 8 -> (ActiveBlock)GTLBlocks.ADVANCED_COMPRESSED_FUSION_COIL.get();
         case 9 -> (ActiveBlock)GTLBlocks.COMPRESSED_FUSION_COIL_MK2_PROTOTYPE.get();
         default -> (ActiveBlock)GTLBlocks.COMPRESSED_FUSION_COIL_MK2.get();
      };
   }

   public static Block getCoilState(int tier) {
      return tier == 9 ? (Block)GTLBlocks.ADVANCED_FUSION_COIL.get() : (Block)GTLBlocks.FUSION_COIL_MK2.get();
   }

   public static Block getFrameState(int tier) {
      return switch (tier) {
         case 6 -> (MaterialBlock)((BlockEntry)GTBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)).get();
         case 7 -> (MaterialBlock)((BlockEntry)GTBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Duranium)).get();
         case 8 -> (MaterialBlock)((BlockEntry)GTBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Naquadria)).get();
         case 9 -> (MaterialBlock)((BlockEntry)GTBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Trinium)).get();
         default -> (MaterialBlock)((BlockEntry)GTBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt, GTMaterials.Neutronium)).get();
      };
   }

   public static Block getCasingState(int tier) {
      return switch (tier) {
         case 6 -> (FusionCasingBlock)GTBlocks.FUSION_CASING.get();
         case 7 -> (FusionCasingBlock)GTBlocks.FUSION_CASING_MK2.get();
         case 8 -> (FusionCasingBlock)GTBlocks.FUSION_CASING_MK3.get();
         case 9 -> (FusionCasingBlock)GTLBlocks.FUSION_CASING_MK4.get();
         default -> (FusionCasingBlock)GTLBlocks.FUSION_CASING_MK5.get();
      };
   }

   public static IFusionCasingType getCasingType(int tier) {
      return (IFusionCasingType)(switch (tier) {
         case 6 -> com.gregtechceu.gtceu.common.block.FusionCasingBlock.CasingType.FUSION_CASING;
         case 7 -> com.gregtechceu.gtceu.common.block.FusionCasingBlock.CasingType.FUSION_CASING_MK2;
         case 8 -> com.gregtechceu.gtceu.common.block.FusionCasingBlock.CasingType.FUSION_CASING_MK3;
         case 9 -> GTLFusionCasingBlock.CasingType.FUSION_CASING_MK4;
         default -> GTLFusionCasingBlock.CasingType.FUSION_CASING_MK5;
      });
   }

   public enum CasingType implements IFusionCasingType, StringRepresentable {
      FUSION_CASING_MK4("fusion_casing_mk4", 3),
      FUSION_CASING_MK5("fusion_casing_mk5", 3);

      private final String name;
      private final int harvestLevel;

      CasingType(String name, int harvestLevel) {
         this.name = name;
         this.harvestLevel = harvestLevel;
      }

      @NotNull
      public String m_7912_() {
         return this.name;
      }

      public ResourceLocation getTexture() {
         return GTLCore.id("block/casings/fusion/%s".formatted(this.name));
      }

      public int getHarvestLevel() {
         return this.harvestLevel;
      }
   }
}
