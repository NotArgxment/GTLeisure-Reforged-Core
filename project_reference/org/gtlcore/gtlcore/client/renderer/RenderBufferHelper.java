package org.gtlcore.gtlcore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderBufferHelper {
   public static void renderCylinder(
      PoseStack poseStack,
      VertexConsumer buffer,
      float x,
      float y,
      float z,
      float radius,
      float height,
      int sides,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      Matrix4f mat = poseStack.m_85850_().m_252922_();
      float angleStep = (float)(6.283185307179586 / sides);

      for (int i = 0; i < sides; i++) {
         float angle1 = i * angleStep;
         float angle2 = (i + 1) * angleStep;
         float cosAngle1 = Mth.m_14089_(angle1);
         float sinAngle1 = Mth.m_14031_(angle1);
         float cosAngle2 = Mth.m_14089_(angle2);
         float sinAngle2 = Mth.m_14031_(angle2);
         buffer.m_252986_(mat, x + cosAngle1 * radius, y, z + sinAngle1 * radius).m_85950_(red, green, blue, alpha).m_5752_();
         buffer.m_252986_(mat, x + cosAngle2 * radius, y, z + sinAngle2 * radius).m_85950_(red, green, blue, alpha).m_5752_();
         buffer.m_252986_(mat, x + cosAngle2 * radius, y + height, z + sinAngle2 * radius).m_85950_(red, green, blue, alpha).m_5752_();
         buffer.m_252986_(mat, x + cosAngle1 * radius, y, z + sinAngle1 * radius).m_85950_(red, green, blue, alpha).m_5752_();
         buffer.m_252986_(mat, x + cosAngle2 * radius, y + height, z + sinAngle2 * radius).m_85950_(red, green, blue, alpha).m_5752_();
         buffer.m_252986_(mat, x + cosAngle1 * radius, y + height, z + sinAngle1 * radius).m_85950_(red, green, blue, alpha).m_5752_();
      }
   }
}
