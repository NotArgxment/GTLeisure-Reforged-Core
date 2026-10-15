package org.gtlcore.gtlcore.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
   public static Minecraft mc() {
      return Minecraft.m_91087_();
   }

   public static BakedModel getBakedModel(ResourceLocation resourceLocation) {
      return mc().m_91304_().getModel(resourceLocation);
   }

   public static BlockRenderDispatcher blockRenderer() {
      return mc().m_91289_();
   }

   public static ModelBlockRenderer modelRenderer() {
      return blockRenderer().m_110937_();
   }
}
