package org.gtlcore.gtlcore.client.renderer.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.GTRenderTypes;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.client.ClientUtil;
import org.gtlcore.gtlcore.client.renderer.RenderBufferHelper;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.SpaceElevatorMachine;
import org.gtlcore.gtlcore.utils.RenderUtil;

public class SpaceElevatorRenderer extends WorkableCasingMachineRenderer {
   private static final ResourceLocation CLIMBER_MODEL = GTLCore.id("obj/climber");

   public SpaceElevatorRenderer() {
      super(GTLCore.id("block/space_elevator_mechanical_casing"), GTCEu.id("block/multiblock/data_bank"));
   }

   @OnlyIn(Dist.CLIENT)
   public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
      if (blockEntity instanceof IMachineBlockEntity machineBlockEntity
         && machineBlockEntity.getMetaMachine() instanceof SpaceElevatorMachine machine
         && machine.isFormed()) {
         float tick = RenderUtil.getSmoothTick(machine, partialTicks);
         double x = 0.5;
         double y = 1.0;
         double z = 0.5;
         switch (machine.getFrontFacing()) {
            case NORTH:
               z = 3.5;
               break;
            case SOUTH:
               z = -2.5;
               break;
            case WEST:
               x = 3.5;
               break;
            case EAST:
               x = -2.5;
         }

         poseStack.m_85836_();
         RenderBufferHelper.renderCylinder(
            poseStack, buffer.m_6299_(GTRenderTypes.getLightRing()), (float)x, (float)(y - 2.0), (float)z, 0.3F, 360.0F, 10, 0.0F, 0.0F, 0.0F, 255.0F
         );
         poseStack.m_85837_(x, y + 180.0 + 140.0 * Math.sin(tick / 160.0F), z);
         this.renderClimber(poseStack, buffer);
         poseStack.m_85849_();
      }
   }

   private void renderClimber(PoseStack poseStack, MultiBufferSource buffer) {
      float scale = 4.0F;
      poseStack.m_85836_();
      poseStack.m_85841_(scale, scale, scale);
      ClientUtil.modelRenderer()
         .renderModel(
            poseStack.m_85850_(),
            buffer.m_6299_(RenderType.m_110451_()),
            null,
            ClientUtil.getBakedModel(CLIMBER_MODEL),
            1.0F,
            1.0F,
            1.0F,
            15728880,
            OverlayTexture.f_118083_,
            ModelData.EMPTY,
            RenderType.m_110451_()
         );
      poseStack.m_85849_();
   }

   public void onAdditionalModel(Consumer<ResourceLocation> registry) {
      super.onAdditionalModel(registry);
      registry.accept(CLIMBER_MODEL);
   }

   @OnlyIn(Dist.CLIENT)
   public boolean hasTESR(BlockEntity blockEntity) {
      return true;
   }

   @OnlyIn(Dist.CLIENT)
   public boolean isGlobalRenderer(BlockEntity blockEntity) {
      return true;
   }

   @OnlyIn(Dist.CLIENT)
   public int getViewDistance() {
      return 512;
   }
}
