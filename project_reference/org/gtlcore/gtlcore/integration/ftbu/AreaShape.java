package org.gtlcore.gtlcore.integration.ftbu;

import dev.ftb.mods.ftbultimine.EntityDistanceComparator;
import dev.ftb.mods.ftbultimine.shape.BlockMatcher;
import dev.ftb.mods.ftbultimine.shape.Shape;
import dev.ftb.mods.ftbultimine.shape.ShapeContext;
import dev.ftb.mods.ftbultimine.shape.ShapeRegistry;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import org.antlr.v4.runtime.misc.OrderedHashSet;
import org.gtlcore.gtlcore.config.ConfigHolder;

public class AreaShape implements Shape {
   private static final AreaShape.Neighbor NEIGHBOR_POSITIONS = new AreaShape.Neighbor(0);

   public static void register() {
      ShapeRegistry.register(new AreaShape());
   }

   public String getName() {
      return "area";
   }

   public List<BlockPos> getBlocks(ShapeContext context) {
      if (NEIGHBOR_POSITIONS.range != ConfigHolder.INSTANCE.ftbUltimineRange) {
         NEIGHBOR_POSITIONS.update(ConfigHolder.INSTANCE.ftbUltimineRange);
      }

      Set<BlockPos> known = new OrderedHashSet();
      walk(context, known);
      List<BlockPos> list = new ArrayList<>(known);
      list.sort(new EntityDistanceComparator(context.pos()));
      if (list.size() > context.maxBlocks()) {
         list.subList(context.maxBlocks(), list.size()).clear();
      }

      return list;
   }

   private static void walk(ShapeContext context, Set<BlockPos> known) {
      LongOpenHashSet traversed = new LongOpenHashSet();
      Deque<BlockPos> openSet = new ArrayDeque<>();
      openSet.add(context.pos());
      traversed.add(context.pos().m_121878_());

      while (!openSet.isEmpty()) {
         BlockPos ptr = openSet.pop();
         if (context.check(ptr) && known.add(ptr)) {
            if (known.size() >= context.maxBlocks()) {
               return;
            }

            for (BlockPos side : NEIGHBOR_POSITIONS.neighbors) {
               BlockPos offset = ptr.m_121955_(side);
               if (traversed.add(offset.m_121878_())) {
                  openSet.add(offset);
               }
            }
         }
      }
   }

   public BlockMatcher getTagMatcher() {
      return BlockMatcher.TAGS_MATCH_SHAPELESS;
   }

   private static final class Neighbor {
      private final ArrayList<BlockPos> neighbors = new ArrayList<>();
      private int range;

      private Neighbor(int r) {
         this.update(r);
      }

      private void update(int r) {
         this.range = r;
         this.neighbors.clear();
         int length = (this.range << 1) + 1;
         this.neighbors.ensureCapacity(length * length * length - 1);

         for (int x = -this.range; x <= this.range; x++) {
            for (int y = -this.range; y <= this.range; y++) {
               for (int z = -this.range; z <= this.range; z++) {
                  if (x != 0 || y != 0 || z != 0) {
                     this.neighbors.add(new BlockPos(x, y, z));
                  }
               }
            }
         }
      }
   }
}
