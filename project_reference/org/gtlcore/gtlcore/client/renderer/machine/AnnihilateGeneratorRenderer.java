package org.gtlcore.gtlcore.client.renderer.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
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
import org.gtlcore.gtlcore.client.ClientUtil;
import org.gtlcore.gtlcore.utils.RenderUtil;
import org.joml.Quaternionf;

public class AnnihilateGeneratorRenderer extends WorkableCasingMachineRenderer {
   public AnnihilateGeneratorRenderer() {
      super(GTCEu.id("block/casings/hpca/high_power_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
   }

   @OnlyIn(Dist.CLIENT)
   public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
      if (blockEntity instanceof IMachineBlockEntity machineBlockEntity
         && machineBlockEntity.getMetaMachine() instanceof WorkableElectricMultiblockMachine machine
         && machine.isActive()) {
         float tick = RenderUtil.getSmoothTick(machine, partialTicks);
         double x = 0.5;
         double y = 36.5;
         double z = 0.5;
         switch (machine.getFrontFacing()) {
            case NORTH:
               z = 39.5;
               break;
            case SOUTH:
               z = -38.5;
               break;
            case WEST:
               x = 39.5;
               break;
            case EAST:
               x = -38.5;
         }

         poseStack.m_85836_();
         poseStack.m_85837_(x, y, z);
         renderStar(tick, poseStack, buffer);
         poseStack.m_85849_();
      }
   }

   private static void renderStar(float tick, PoseStack poseStack, MultiBufferSource buffer) {
      poseStack.m_85836_();
      poseStack.m_85841_(0.45F, 0.45F, 0.45F);
      poseStack.m_252781_(new Quaternionf().fromAxisAngleDeg(0.0F, 1.0F, 1.0F, tick % 360.0F));
      ClientUtil.modelRenderer()
         .renderModel(
            poseStack.m_85850_(),
            buffer.m_6299_(RenderType.m_110466_()),
            null,
            ClientUtil.getBakedModel(EyeOfHarmonyRenderer.STAR_MODEL),
            1.0F,
            1.0F,
            1.0F,
            15728880,
            OverlayTexture.f_118083_,
            ModelData.EMPTY,
            RenderType.m_110466_()
         );
      poseStack.m_85849_();
   }

   public void onAdditionalModel(Consumer<ResourceLocation> registry) {
      super.onAdditionalModel(registry);
      registry.accept(EyeOfHarmonyRenderer.STAR_MODEL);
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
