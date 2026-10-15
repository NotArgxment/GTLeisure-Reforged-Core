package org.gtlcore.gtlcore.api.pattern;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.error.PatternStringError;
import com.gregtechceu.gtceu.api.pattern.predicates.PredicateBlocks;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.NonNull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.gtlcore.gtlcore.api.pattern.util.IValueContainer;
import org.gtlcore.gtlcore.api.pattern.util.SimpleValueContainer;

public class GTLPredicates {
   public static TraceabilityPredicate tierCasings(Int2ObjectMap<Supplier<?>> map, String tierType) {
      BlockInfo[] blockInfos = new BlockInfo[map.size()];
      int index = 0;

      for (ObjectIterator<Supplier<?>> entry = map.values().iterator(); entry.hasNext(); index++) {
         Supplier<?> blockSupplier = (Supplier<?>)entry.next();
         Block block = (Block)blockSupplier.get();
         blockInfos[index] = BlockInfo.fromBlockState(block.m_49966_());
      }

      return new TraceabilityPredicate(blockWorldState -> {
         BlockState blockState = blockWorldState.getBlockState();
         ObjectIterator var4x = map.int2ObjectEntrySet().iterator();

         while (var4x.hasNext()) {
            Entry<Supplier<?>> entryx = (Entry<Supplier<?>>)var4x.next();
            if (blockState.m_60713_((Block)((Supplier)entryx.getValue()).get())) {
               int stats = entryx.getIntKey();
               Object currentCoil = blockWorldState.getMatchContext().getOrPut(tierType, stats);
               if (!currentCoil.equals(stats)) {
                  blockWorldState.setError(new PatternStringError("gtceu.multiblock.pattern.error.tier"));
                  return false;
               }

               return true;
            }
         }

         return false;
      }, () -> blockInfos).addTooltips(new Component[]{Component.m_237115_("gtceu.multiblock.pattern.error.tier")});
   }

   public static TraceabilityPredicate countBlock(String name, Block... blocks) {
      TraceabilityPredicate inner = Predicates.blocks(blocks);
      Predicate<MultiblockState> predicate = state -> {
         if (inner.test(state)) {
            IValueContainer<?> currentContainer = (IValueContainer<?>)state.getMatchContext()
               .getOrPut(name + "Value", new SimpleValueContainer<>(0, (integer, block, tierType) -> integer + 1));
            currentContainer.operate(state.getBlockState().m_60734_(), null);
            return true;
         } else {
            return false;
         }
      };
      BlockInfo[] candidates = inner.common
         .stream()
         .map(p -> p.candidates)
         .filter(Objects::nonNull)
         .map(Supplier::get)
         .flatMap(Arrays::stream)
         .toArray(BlockInfo[]::new);
      return new TraceabilityPredicate(new SimplePredicate(predicate, () -> candidates));
   }

   public static TraceabilityPredicate RotorBlock() {
      return new TraceabilityPredicate(new PredicateBlocks(PartAbility.ROTOR_HOLDER.getAllBlocks().toArray(Block[]::new)) {
         public boolean test(MultiblockState blockWorldState) {
            if (super.test(blockWorldState)) {
               Level level = blockWorldState.getWorld();
               BlockPos pos = blockWorldState.getPos();
               MetaMachine machine = MetaMachine.getMachine(level, pos);
               if (machine instanceof ITieredMachine tieredMachine) {
                  int tier = (Integer)blockWorldState.getMatchContext().getOrPut("ReinforcedRotor", tieredMachine.getTier());
                  if (tier != tieredMachine.getTier()) {
                     return false;
                  }

                  return level.m_8055_(pos.m_121945_(machine.getFrontFacing())).m_60795_();
               }
            }

            return false;
         }
      });
   }

   public static TraceabilityPredicate diffAbilities(@NonNull Collection<PartAbility> group1, @NonNull Collection<PartAbility> group2) {
      if (group1 == null) {
         throw new NullPointerException("group1 is marked non-null but is null");
      } else if (group2 == null) {
         throw new NullPointerException("group2 is marked non-null but is null");
      } else {
         return Predicates.blocks((Block[])diffBlocks(group1, group2).toArray(Block[]::new));
      }
   }

   private static ObjectSet<Block> unionBlocks(@NonNull Collection<PartAbility> group) {
      if (group == null) {
         throw new NullPointerException("group is marked non-null but is null");
      }

      if (group.isEmpty()) {
         return ObjectSets.emptySet();
      }

      ObjectSet<Block> out = new ObjectOpenHashSet();

      for (PartAbility ability : group) {
         out.addAll(ability.getAllBlocks());
      }

      return out;
   }

   private static ObjectSet<Block> diffBlocks(Collection<PartAbility> group1, Collection<PartAbility> group2) {
      ObjectSet<Block> g1 = unionBlocks(group1);
      g1.removeAll(unionBlocks(group2));
      return g1;
   }
}
