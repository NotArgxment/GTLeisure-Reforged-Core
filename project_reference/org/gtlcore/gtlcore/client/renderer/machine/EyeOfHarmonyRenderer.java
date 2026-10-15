package org.gtlcore.gtlcore.client.renderer.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.client.renderer.machine.IControllerRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.client.ClientUtil;
import org.gtlcore.gtlcore.common.machine.multiblock.electric.HarmonyMachine;
import org.gtlcore.gtlcore.utils.RenderUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

public class EyeOfHarmonyRenderer extends WorkableCasingMachineRenderer implements IControllerRenderer {
   private static final ResourceLocation SPACE_MODEL = GTLCore.id("obj/space");
   public static final ResourceLocation STAR_MODEL = GTLCore.id("obj/star");
   private static final List<ResourceLocation> ORBIT_OBJECTS = List.of(GTLCore.id("obj/the_nether"), GTLCore.id("obj/overworld"), GTLCore.id("obj/the_end"));

   public EyeOfHarmonyRenderer() {
      super(GTLCore.id("block/casings/dimensionally_transcendent_casing"), GTCEu.id("block/multiblock/cosmos_simulation"));
   }

   @OnlyIn(Dist.CLIENT)
   public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
      if (blockEntity instanceof IMachineBlockEntity machineBlockEntity
         && machineBlockEntity.getMetaMachine() instanceof HarmonyMachine machine
         && machine.isActive()) {
         float tick = RenderUtil.getSmoothTick(machine, partialTicks);
         double x = 0.5;
         double y = 0.5;
         double z = 0.5;
         switch (machine.getFrontFacing()) {
            case NORTH:
               z = 16.5;
               break;
            case SOUTH:
               z = -15.5;
               break;
            case WEST:
               x = 16.5;
               break;
            case EAST:
               x = -15.5;
         }

         poseStack.m_85836_();
         poseStack.m_85837_(x, y, z);
         renderStar(tick, poseStack, buffer);
         this.renderOrbitObjects(tick, poseStack, buffer, x, y, z);
         this.renderOuterSpaceShell(poseStack, buffer);
         poseStack.m_85849_();
      }
   }

   private static void renderStar(float tick, PoseStack poseStack, MultiBufferSource buffer) {
      poseStack.m_85836_();
      poseStack.m_85841_(0.02F, 0.02F, 0.02F);
      poseStack.m_252781_(new Quaternionf().fromAxisAngleDeg(0.0F, 1.0F, 1.0F, tick / 2.0F % 360.0F));
      ClientUtil.modelRenderer()
         .renderModel(
            poseStack.m_85850_(),
            buffer.m_6299_(RenderType.m_110466_()),
            null,
            ClientUtil.getBakedModel(STAR_MODEL),
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

   private void renderOrbitObjects(float tick, PoseStack poseStack, MultiBufferSource buffer, double x, double y, double z) {
      for (int a = 1; a < 4; a++) {
         float scale = 0.007F + 0.003F * a;
         poseStack.m_85836_();
         poseStack.m_85841_(scale, scale, scale);
         poseStack.m_252781_(new Quaternionf().fromAxisAngleDeg(1.0F, 0.0F, 1.0F, tick * 1.5F / a % 360.0F));
         poseStack.m_85837_(x + (a * 100 + 160) * Math.sin(tick * a / 80.0F + 0.4), y, z + (a * 100 + 160) * Math.cos(tick * a / 80.0F + 0.4));
         ClientUtil.modelRenderer()
            .renderModel(
               poseStack.m_85850_(),
               buffer.m_6299_(RenderType.m_110451_()),
               null,
               ClientUtil.getBakedModel(ORBIT_OBJECTS.get(a - 1)),
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
   }

   private void renderOuterSpaceShell(PoseStack poseStack, MultiBufferSource buffer) {
      float scale = 0.175F;
      poseStack.m_85836_();
      poseStack.m_85841_(scale, scale, scale);
      ClientUtil.modelRenderer()
         .renderModel(
            poseStack.m_85850_(),
            buffer.m_6299_(RenderType.m_110451_()),
            null,
            ClientUtil.getBakedModel(SPACE_MODEL),
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
      registry.accept(SPACE_MODEL);
      registry.accept(STAR_MODEL);
      registry.accept(ORBIT_OBJECTS.get(0));
      registry.accept(ORBIT_OBJECTS.get(1));
      registry.accept(ORBIT_OBJECTS.get(2));
   }

   @OnlyIn(Dist.CLIENT)
   public void renderPartModel(
      List<BakedQuad> list,
      IMultiController iMultiController,
      IMultiPart iMultiPart,
      Direction direction,
      @Nullable Direction direction1,
      RandomSource randomSource,
      Direction direction2,
      ModelState modelState
   ) {
      if (direction1 != null && direction2 != null) {
         list.add(FaceQuad.bakeFace(direction2, ModelFactory.getBlockSprite(GTCEu.id("block/casings/hpca/high_power_casing")), modelState));
      }
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
      return 128;
   }
}
