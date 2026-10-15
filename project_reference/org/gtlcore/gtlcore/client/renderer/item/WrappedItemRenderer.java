package org.gtlcore.gtlcore.client.renderer.item;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public abstract class WrappedItemRenderer implements IRenderer {
   @OnlyIn(Dist.CLIENT)
   protected BakedModel getVanillaModel(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
      ItemModelShaper shaper = Minecraft.m_91087_().m_91291_().m_115103_();
      BakedModel model = shaper.m_109394_(stack.m_41720_());
      if (model != null) {
         BakedModel bakedmodel = model.m_7343_().m_173464_(model, stack, level, entity, 0);
         if (bakedmodel != null) {
            return bakedmodel;
         }
      }

      return shaper.m_109393_().m_119409_();
   }

   @OnlyIn(Dist.CLIENT)
   protected void vanillaRender(
      ItemStack stack,
      ItemDisplayContext transformType,
      boolean leftHand,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int combinedLight,
      int combinedOverlay,
      BakedModel model
   ) {
      IItemRendererProvider.disabled.set(true);
      Minecraft.m_91087_().m_91291_().m_115143_(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
      IItemRendererProvider.disabled.set(false);
   }
}
