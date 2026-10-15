package org.gtlcore.gtlcore.mixin.ldlib;

import com.lowdragmc.lowdraglib.client.bakedpipeline.Quad;
import com.lowdragmc.lowdraglib.client.bakedpipeline.Submap;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.model.custommodel.Connections;
import com.lowdragmc.lowdraglib.client.model.custommodel.CustomBakedModel;
import com.lowdragmc.lowdraglib.client.model.custommodel.LDLMetadataSection;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomBakedModel.class)
public abstract class CustomBakedModelFixed implements BakedModel {
   @Mutable
   @Final
   @Shadow(remap = false)
   private final BakedModel parent;
   protected final ConcurrentMap<Direction, ConcurrentMap<Connections, List<BakedQuad>>> sideCache = new ConcurrentHashMap<>();
   protected final List<BakedQuad> noSideCache = new ObjectArrayList();

   protected CustomBakedModelFixed(BakedModel parent) {
      this.parent = parent;
   }

   @Overwrite(remap = false)
   @Nonnull
   @Deprecated
   public List<BakedQuad> m_213637_(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
      return this.parent.m_213637_(state, side, rand);
   }

   @Overwrite(remap = false)
   @Nonnull
   public List<BakedQuad> getCustomQuads(BlockAndTintGetter level, BlockPos pos, @Nonnull BlockState state, @Nullable Direction side, RandomSource rand) {
      Connections connections = Connections.checkConnections(level, pos, state, side);
      if (side == null) {
         if (this.noSideCache.isEmpty()) {
            synchronized (this.noSideCache) {
               if (this.noSideCache.isEmpty()) {
                  this.noSideCache.addAll(buildCustomQuads(connections, this.parent.m_213637_(state, null, rand), 0.0F));
               }
            }
         }

         return this.noSideCache;
      } else {
         return this.sideCache
            .computeIfAbsent(side, key -> new ConcurrentHashMap<>())
            .computeIfAbsent(connections, key -> buildCustomQuads(key, this.parent.m_213637_(state, side, rand), 0.0F));
      }
   }

   @Overwrite(remap = false)
   public static List<BakedQuad> reBakeCustomQuads(
      List<BakedQuad> quads, BlockAndTintGetter level, BlockPos pos, @Nonnull BlockState state, @Nullable Direction side, float offset
   ) {
      return buildCustomQuads(Connections.checkConnections(level, pos, state, side), quads, offset);
   }

   @Overwrite(remap = false)
   public static List<BakedQuad> buildCustomQuads(Connections connections, List<BakedQuad> base, float offset) {
      List<BakedQuad> result = new LinkedList<>();

      for (BakedQuad bakedQuad : base) {
         LDLMetadataSection section = LDLMetadataSection.getMetadata(bakedQuad.m_173410_());
         TextureAtlasSprite connection = section.connection == null ? null : ModelFactory.getBlockSprite(section.connection);
         if (connection == null) {
            result.add(makeQuad(bakedQuad, section, offset).rebake());
         } else {
            Quad quad = makeQuad(bakedQuad, section, offset).derotate();
            Quad[] quads = quad.subdivide(4);
            int[] ctm = connections.getSubmapIndices();

            for (int j = 0; j < quads.length; j++) {
               Quad q = quads[j];
               if (q != null) {
                  int ctmid = q.getUvs().normalize().getQuadrant();
                  quads[j] = q.grow().transformUVs(ctm[ctmid] > 15 ? bakedQuad.m_173410_() : connection, Submap.uvs[ctm[ctmid]]);
               }
            }

            result.addAll(Arrays.stream(quads).filter(Objects::nonNull).map(Quad::rebake).toList());
         }
      }

      return result;
   }

   @Overwrite(remap = false)
   protected static Quad makeQuad(BakedQuad bq, LDLMetadataSection section, float offset) {
      Quad q = Quad.from(bq, offset);
      if (section.emissive) {
         q = q.setLight(15, 15);
      }

      return q;
   }
}
