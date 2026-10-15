package org.gtlcore.gtlcore.mixin.ldlib;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.scene.ISceneBlockRenderHook;
import com.lowdragmc.lowdraglib.client.scene.ISceneEntityRenderHook;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer;
import com.lowdragmc.lowdraglib.client.scene.WorldSceneRenderer.VertexConsumerWrapper;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.VertexBuffer.Usage;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.gtlcore.gtlcore.utils.RenderUtil;
import org.gtlcore.gtlcore.utils.datastructure.CacheState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldSceneRenderer.class)
public abstract class WorldSceneRendererMixin {
   @Unique
   private AtomicReference<CacheState> gTLCore$cacheState = new AtomicReference<>(CacheState.UNUSED);
   @Unique
   protected List<List<RenderedBuffer>> gTLCore$compiledBufferData;
   @Unique
   protected List<List<VertexBuffer>> gTLCore$vertexBufferBatches;
   @Shadow(remap = false)
   @Final
   public Map<Collection<BlockPos>, ISceneBlockRenderHook> renderedBlocksMap;
   @Shadow(remap = false)
   @Final
   public Level world;
   @Shadow(remap = false)
   protected boolean useCache;
   @Shadow(remap = false)
   protected Thread thread;
   @Shadow(remap = false)
   protected Set<BlockPos> tileEntities;
   @Shadow(remap = false)
   protected int progress;
   @Shadow(remap = false)
   protected int maxProgress;
   @Shadow(remap = false)
   private ISceneEntityRenderHook sceneEntityRenderHook;
   @Shadow(remap = false)
   protected boolean endBatchLast;
   @Shadow(remap = false)
   private Set<BlockPos> blocked;

   @Shadow(remap = false)
   private void renderEntities(
      TrackedDummyWorld level, PoseStack poseStack, MultiBufferSource buffer, @Nullable ISceneEntityRenderHook hook, float partialTicks
   ) {
      throw new AssertionError();
   }

   @Shadow(remap = false)
   private void renderTESR(Collection<BlockPos> poses, PoseStack poseStack, BufferSource buffers, @Nullable ISceneBlockRenderHook hook, float partialTicks) {
      throw new AssertionError();
   }

   @Shadow(remap = false)
   public static void setDefaultRenderLayerState(RenderType layer) {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   public WorldSceneRenderer useCacheBuffer(boolean useCache) {
      if (!this.useCache && Minecraft.m_91087_().m_18695_()) {
         this.deleteCacheBuffer();
         if (useCache) {
            List<RenderType> layers = RenderType.m_110506_();
            this.gTLCore$vertexBufferBatches = new ObjectArrayList(layers.size());
            this.gTLCore$compiledBufferData = new ObjectArrayList(layers.size());

            for (int j = 0; j < layers.size(); j++) {
               this.gTLCore$vertexBufferBatches.add(new ObjectArrayList());
               this.gTLCore$compiledBufferData.add(new ObjectArrayList());
            }

            if (this.gTLCore$cacheState.get() == CacheState.COMPILING && this.thread != null) {
               this.thread.interrupt();
               this.thread = null;
            }

            this.gTLCore$cacheState.set(CacheState.NEED);
         }

         this.useCache = useCache;
         return (WorldSceneRenderer)this;
      } else {
         return (WorldSceneRenderer)this;
      }
   }

   @Overwrite(remap = false)
   public WorldSceneRenderer deleteCacheBuffer() {
      if (this.useCache && this.gTLCore$vertexBufferBatches != null) {
         for (List<VertexBuffer> bufferList : this.gTLCore$vertexBufferBatches) {
            if (bufferList != null) {
               for (VertexBuffer vbo : bufferList) {
                  if (vbo != null) {
                     vbo.close();
                  }
               }

               bufferList.clear();
            }
         }

         if (this.gTLCore$cacheState.get() == CacheState.COMPILING && this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
         }
      }

      this.tileEntities = null;
      this.gTLCore$vertexBufferBatches = null;
      this.useCache = false;
      this.gTLCore$cacheState.set(CacheState.UNUSED);
      return (WorldSceneRenderer)this;
   }

   @Overwrite(remap = false)
   public WorldSceneRenderer needCompileCache() {
      if (this.gTLCore$cacheState.get() == CacheState.COMPILING && this.thread != null) {
         this.thread.interrupt();
         this.thread = null;
      }

      if (this.useCache && this.gTLCore$vertexBufferBatches != null) {
         for (List<VertexBuffer> batches : this.gTLCore$vertexBufferBatches) {
            if (batches != null) {
               for (VertexBuffer vbo : batches) {
                  if (vbo != null) {
                     vbo.close();
                  }
               }

               batches.clear();
            }
         }
      }

      this.gTLCore$cacheState.set(CacheState.NEED);
      return (WorldSceneRenderer)this;
   }

   @Overwrite(remap = false)
   public boolean isCompiling() {
      return this.gTLCore$cacheState.get() == CacheState.COMPILING;
   }

   @Overwrite(remap = false)
   private void renderCacheBuffer(Minecraft mc, BufferSource buffers, float particleTicks) {
      List<RenderType> layers = RenderType.m_110506_();
      if (this.gTLCore$cacheState.get() == CacheState.NEED) {
         this.progress = 0;
         this.maxProgress = this.renderedBlocksMap.keySet().stream().map(Collection::size).reduce(0, Integer::sum) * (layers.size() + 1);
         this.thread = new Thread(
            () -> {
               this.gTLCore$cacheState.set(CacheState.COMPILING);
               BlockRenderDispatcher blockRendererDispatcher = mc.m_91289_();

               try {
                  ModelBlockRenderer.m_111000_();
                  PoseStack matrixStack = new PoseStack();

                  for (int layerIndexx = 0; layerIndexx < layers.size(); layerIndexx++) {
                     if (Thread.interrupted()) {
                        return;
                     }

                     RenderType layerx = layers.get(layerIndexx);
                     List<RenderedBuffer> compiledDataForThisLayer = this.gTLCore$compiledBufferData.get(layerIndexx);
                     BufferBuilder currentBuffer = new BufferBuilder(layerx.m_110507_());
                     currentBuffer.m_166779_(Mode.QUADS, DefaultVertexFormat.f_85811_);
                     int vertexCount = 0;

                     for (Entry<Collection<BlockPos>, ISceneBlockRenderHook> entry : this.renderedBlocksMap.entrySet()) {
                        if (Thread.interrupted()) {
                           return;
                        }

                        Collection<BlockPos> renderedBlocks = entry.getKey();
                        ISceneBlockRenderHook hook = entry.getValue();

                        for (BlockPos pos : renderedBlocks) {
                           if (this.blocked == null || !this.blocked.contains(pos)) {
                              BlockState state = this.world.m_8055_(pos);
                              FluidState fluidState = state.m_60819_();
                              Block block = state.m_60734_();
                              if (block != Blocks.f_50016_) {
                                 if (RenderUtil.shouldStartNewBatch(currentBuffer, vertexCount, 24)) {
                                    RenderedBuffer builtBuffer = currentBuffer.m_231175_();
                                    compiledDataForThisLayer.add(builtBuffer);
                                    currentBuffer = new BufferBuilder(layerx.m_110507_());
                                    currentBuffer.m_166779_(Mode.QUADS, DefaultVertexFormat.f_85811_);
                                    vertexCount = 0;
                                 }

                                 VertexConsumerWrapper wrapperBuffer = new VertexConsumerWrapper(currentBuffer);
                                 if (hook != null) {
                                    hook.applyVertexConsumerWrapper(this.world, pos, state, wrapperBuffer, layerx, 0.0F);
                                 }

                                 if (state.m_60799_() != RenderShape.INVISIBLE && WorldSceneRenderer.canRenderInLayer(state, layerx)) {
                                    matrixStack.m_85836_();
                                    matrixStack.m_252880_(pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
                                    if (Platform.isForge()) {
                                       WorldSceneRenderer.renderBlocksForge(
                                          blockRendererDispatcher, state, pos, this.world, matrixStack, wrapperBuffer, this.world.f_46441_, layerx
                                       );
                                    } else {
                                       blockRendererDispatcher.m_234355_(state, pos, this.world, matrixStack, wrapperBuffer, false, this.world.f_46441_);
                                    }

                                    matrixStack.m_85849_();
                                    vertexCount += 24;
                                 }

                                 if (!fluidState.m_76178_() && ItemBlockRenderTypes.m_109287_(fluidState) == layerx) {
                                    wrapperBuffer.addOffset(
                                       pos.m_123341_() - (pos.m_123341_() & 15),
                                       pos.m_123342_() - (pos.m_123342_() & 15),
                                       pos.m_123343_() - (pos.m_123343_() & 15)
                                    );
                                    blockRendererDispatcher.m_234363_(pos, this.world, wrapperBuffer, state, fluidState);
                                    vertexCount += 24;
                                 }

                                 wrapperBuffer.clerOffset();
                                 wrapperBuffer.clearColor();
                                 if (this.maxProgress > 0) {
                                    this.progress++;
                                 }
                              }
                           }
                        }
                     }

                     if (vertexCount > 0) {
                        RenderedBuffer builtBuffer = currentBuffer.m_231175_();
                        compiledDataForThisLayer.add(builtBuffer);
                     }
                  }
               } finally {
                  ModelBlockRenderer.m_111077_();
               }

               ObjectOpenHashSet var23 = new ObjectOpenHashSet();
               this.renderedBlocksMap.forEach((renderedBlocksx, hookx) -> {
                  for (BlockPos posx : renderedBlocksx) {
                     this.progress++;
                     if (Thread.interrupted()) {
                        return;
                     }

                     BlockEntity tile = this.world.m_7702_(posx);
                     if (tile != null && mc.m_167982_().m_112265_(tile) != null) {
                        var23.add(posx);
                     }
                  }
               });
               if (!Thread.interrupted()) {
                  this.tileEntities = var23;
                  this.gTLCore$cacheState.set(CacheState.COMPILED);
                  this.thread = null;
                  this.maxProgress = -1;
               }
            }
         );
         this.thread.start();
      } else if (this.gTLCore$cacheState.get() == CacheState.COMPILED) {
         for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
            if (this.gTLCore$vertexBufferBatches.get(layerIndex).isEmpty() && !this.gTLCore$compiledBufferData.get(layerIndex).isEmpty()) {
               List<VertexBuffer> batchList = this.gTLCore$vertexBufferBatches.get(layerIndex);

               for (RenderedBuffer data : this.gTLCore$compiledBufferData.get(layerIndex)) {
                  VertexBuffer vbo = new VertexBuffer(Usage.STATIC);
                  batchList.add(vbo);
                  VertexBuffer vboToUpload = vbo;
                  RenderedBuffer dataToUpload = data;
                  RenderSystem.recordRenderCall(() -> {
                     if (!vboToUpload.m_231230_()) {
                        vboToUpload.m_85921_();
                        vboToUpload.m_231221_(dataToUpload);
                        VertexBuffer.m_85931_();
                     }
                  });
               }

               this.gTLCore$compiledBufferData.get(layerIndex).clear();
            }
         }

         PoseStack matrixstack = new PoseStack();

         for (int layerIndex = 0; layerIndex < layers.size(); layerIndex++) {
            List<VertexBuffer> batchesForThisLayer = this.gTLCore$vertexBufferBatches.get(layerIndex);
            if (batchesForThisLayer != null && !batchesForThisLayer.isEmpty()) {
               RenderType layer = layers.get(layerIndex);
               if (layer == RenderType.m_110466_() && this.tileEntities != null) {
                  if (this.world instanceof TrackedDummyWorld level) {
                     this.renderEntities(level, matrixstack, buffers, this.sceneEntityRenderHook, particleTicks);
                  }

                  this.renderTESR(this.tileEntities, matrixstack, mc.m_91269_().m_110104_(), null, particleTicks);
                  if (!this.endBatchLast) {
                     buffers.m_109911_();
                  }
               }

               layer.m_110185_();

               for (VertexBuffer vertexbuffer : batchesForThisLayer) {
                  if (!vertexbuffer.m_231230_() && vertexbuffer.m_166892_() != null) {
                     matrixstack.m_85836_();
                     ShaderInstance shaderInstance = RenderSystem.getShader();

                     for (int j = 0; j < 12; j++) {
                        int k = RenderSystem.getShaderTexture(j);
                        shaderInstance.m_173350_("Sampler" + j, k);
                     }

                     if (shaderInstance.f_173308_ != null) {
                        shaderInstance.f_173308_.m_5679_(RenderSystem.getModelViewMatrix());
                     }

                     if (shaderInstance.f_173309_ != null) {
                        shaderInstance.f_173309_.m_5679_(RenderSystem.getProjectionMatrix());
                     }

                     if (shaderInstance.f_173312_ != null) {
                        shaderInstance.f_173312_.m_5941_(RenderSystem.getShaderColor());
                     }

                     if (shaderInstance.f_173315_ != null) {
                        shaderInstance.f_173315_.m_5985_(RenderSystem.getShaderFogStart());
                     }

                     if (shaderInstance.f_173316_ != null) {
                        shaderInstance.f_173316_.m_5985_(RenderSystem.getShaderFogEnd());
                     }

                     if (shaderInstance.f_173317_ != null) {
                        shaderInstance.f_173317_.m_5941_(RenderSystem.getShaderFogColor());
                     }

                     if (shaderInstance.f_202432_ != null) {
                        shaderInstance.f_202432_.m_142617_(RenderSystem.getShaderFogShape().m_202324_());
                     }

                     if (shaderInstance.f_173310_ != null) {
                        shaderInstance.f_173310_.m_5679_(RenderSystem.getTextureMatrix());
                     }

                     if (shaderInstance.f_173319_ != null) {
                        shaderInstance.f_173319_.m_5985_(RenderSystem.getShaderGameTime());
                     }

                     RenderSystem.setupShaderLights(shaderInstance);
                     shaderInstance.m_173363_();
                     setDefaultRenderLayerState(layer);
                     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                     vertexbuffer.m_85921_();
                     vertexbuffer.m_166882_();
                     matrixstack.m_85849_();
                     shaderInstance.m_173362_();
                     VertexBuffer.m_85931_();
                  }
               }

               layer.m_110188_();
            }
         }
      }
   }
}
