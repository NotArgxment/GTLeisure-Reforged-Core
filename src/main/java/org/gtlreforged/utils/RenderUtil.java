package org.gtlreforged.utils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlreforged.config.GTLConfigHolder;
import org.gtlreforged.mixin.minecraft.BufferBuilderAccessor;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

   private static final int MAX_BATCH_BYTES = 1610612736;
   private static final int MAX_VERTICES_PER_BATCH = 2097152;

   public static int getBufferByteSize(BufferBuilder builder) {
      return ((BufferBuilderAccessor)builder).getNextElementByte();
   }

   public static boolean shouldStartNewBatch(BufferBuilder builder, int currentVertexCount, int estimatedAdditionalVertices) {
      int byteSize = getBufferByteSize(builder);
      if (byteSize >= 0) {
         int estimatedBytes = estimatedAdditionalVertices * 40;
         return byteSize + estimatedBytes > 1610612736;
      } else {
         return currentVertexCount + estimatedAdditionalVertices > 2097152;
      }
   }

   public static float getSmoothTick(MetaMachine machine, float partialTick) {
      return GTLConfigHolder.INSTANCE.enableSmoothAnimations ? GlobalRenderClock.getSmoothTick() : (float)machine.getOffsetTimer() + partialTick;
   }
}
