package org.gtlcore.gtlcore.client.forge;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.gtlcore.gtlcore.common.item.StructureWriteBehavior;

@EventBusSubscriber(modid = "gtlcore", bus = Bus.FORGE, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ForgeClientEventListener {
   @SubscribeEvent
   public static void onRenderWorldLast(RenderLevelStageEvent event) {
      Stage stage = event.getStage();
      if (stage == Stage.AFTER_TRIPWIRE_BLOCKS) {
         Minecraft mc = Minecraft.m_91087_();
         ClientLevel level = mc.f_91073_;
         LocalPlayer player = mc.f_91074_;
         if (level == null || player == null) {
            return;
         }

         PoseStack poseStack = event.getPoseStack();
         Camera camera = event.getCamera();
         ItemStack held = player.m_21205_();
         if (StructureWriteBehavior.isItemStructureWriter(held)) {
            BlockPos[] poses = StructureWriteBehavior.getPos(held);
            if (poses == null) {
               return;
            }

            Vec3 pos = camera.m_90583_();
            poseStack.m_85836_();
            poseStack.m_85837_(-pos.f_82479_, -pos.f_82480_, -pos.f_82481_);
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.disableCull();
            RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            Tesselator tesselator = Tesselator.m_85913_();
            BufferBuilder buffer = tesselator.m_85915_();
            buffer.m_166779_(Mode.QUADS, DefaultVertexFormat.f_85815_);
            RenderSystem.setShader(GameRenderer::m_172811_);
            RenderBufferUtils.renderCubeFace(
               poseStack,
               buffer,
               poses[0].m_123341_(),
               poses[0].m_123342_(),
               poses[0].m_123343_(),
               poses[1].m_123341_() + 1,
               poses[1].m_123342_() + 1,
               poses[1].m_123343_() + 1,
               0.2F,
               0.2F,
               1.0F,
               0.25F,
               true
            );
            tesselator.m_85914_();
            buffer.m_166779_(Mode.LINES, DefaultVertexFormat.f_166851_);
            RenderSystem.setShader(GameRenderer::m_172757_);
            RenderSystem.lineWidth(3.0F);
            RenderBufferUtils.drawCubeFrame(
               poseStack,
               buffer,
               poses[0].m_123341_(),
               poses[0].m_123342_(),
               poses[0].m_123343_(),
               poses[1].m_123341_() + 1,
               poses[1].m_123342_() + 1,
               poses[1].m_123343_() + 1,
               0.0F,
               0.0F,
               1.0F,
               0.5F
            );
            tesselator.m_85914_();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            poseStack.m_85849_();
         }
      }
   }
}
