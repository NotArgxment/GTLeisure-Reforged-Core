package org.gtlcore.gtlcore.mixin.ae2.client;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
   @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
   private void loadModelHook(ResourceLocation id, CallbackInfo ci) {
      UnbakedModel model = this.gtceu$getUnbakedModel(id);
      if (model != null) {
         this.m_119352_(id, model);
         ci.cancel();
      }
   }

   @Unique
   private UnbakedModel gtceu$getUnbakedModel(ResourceLocation variantId) {
      if (!variantId.m_135827_().equals("gtlcore")) {
         return null;
      }

      if (variantId instanceof ModelResourceLocation modelId) {
         if ("inventory".equals(modelId.m_119448_())) {
            ResourceLocation itemModelId = new ResourceLocation(modelId.m_135827_(), "item/" + modelId.m_135815_());
            return BuiltInModelHooksAccessor.getBuiltInModels().get(itemModelId);
         } else {
            return null;
         }
      } else {
         return BuiltInModelHooksAccessor.getBuiltInModels().get(variantId);
      }
   }

   @Shadow
   protected abstract void m_119352_(ResourceLocation var1, UnbakedModel var2);
}
