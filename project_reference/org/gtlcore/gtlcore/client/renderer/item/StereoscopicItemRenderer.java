package org.gtlcore.gtlcore.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

public class StereoscopicItemRenderer extends WrappedItemRenderer {
   public static final StereoscopicItemRenderer INSTANCE = new StereoscopicItemRenderer();

   @OnlyIn(Dist.CLIENT)
   public void renderItem(
      ItemStack stack,
      ItemDisplayContext transformType,
      boolean leftHand,
      PoseStack poseStack,
      MultiBufferSource buffer,
      int combinedLight,
      int combinedOverlay,
      BakedModel model
   ) {
      model = this.getVanillaModel(stack, null, null);
      poseStack.m_85836_();
      if (transformType == ItemDisplayContext.GUI) {
         poseStack.m_252781_(new Quaternionf().fromAxisAngleDeg(0.3F, 0.5F, 0.2F, (float)(System.currentTimeMillis() / 25L % 360L)));
      }

      this.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, model);
      poseStack.m_85849_();
   }
}
