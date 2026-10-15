package org.gtlcore.gtlcore.integration.ae2.async;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;

public final class AEWriteService implements AutoCloseable {
   public static final AEWriteService INSTANCE = new AEWriteService();
   public static int THREAD_PRIORITY = 4;
   public static int TIME_OUT = 10;
   private final ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4096), r -> {
      Thread t = new Thread(r, "AE-Writer");
      t.setDaemon(true);
      t.setPriority(Math.max(1, Math.min(10, THREAD_PRIORITY)));
      return t;
   }, new CallerRunsPolicy());

   public void submitIngredientLeft(WeakReference<AEAccumulator> accRef, List<Ingredient> left) {
      if (left != null && !left.isEmpty()) {
         this.executor
            .execute(
               () -> {
                  AEAccumulator acc = accRef.get();
                  if (acc != null) {
                     for (Ingredient ingredient : left) {
                        if (ingredient instanceof IntProviderIngredient intProvider) {
                           intProvider.setItemStacks(null);
                           intProvider.setSampledCount(null);
                        }

                        ItemStack[] items = ingredient.m_43908_();
                        if (items.length != 0) {
                           ItemStack output = items[0];
                           if (!output.m_41619_()) {
                              acc.add(
                                 AEItemKey.of(output),
                                 ingredient instanceof LongIngredient longIngredient ? longIngredient.getActualAmount() : output.m_41613_()
                              );
                           }
                        }
                     }
                  }
               }
            );
      }
   }

   public void submitFluidIngredientLeft(WeakReference<AEAccumulator> accRef, List<FluidIngredient> left) {
      if (left != null && !left.isEmpty()) {
         this.executor.execute(() -> {
            AEAccumulator acc = accRef.get();
            if (acc != null) {
               for (FluidIngredient fluidIngredient : left) {
                  if (!fluidIngredient.isEmpty()) {
                     FluidStack[] fluids = fluidIngredient.getStacks();
                     if (fluids.length != 0) {
                        FluidStack output = fluids[0];
                        acc.add(AEFluidKey.of(output.getFluid()), output.getAmount());
                     }
                  }
               }
            }
         });
      }
   }

   public void prepareDrainedData(WeakReference<AEAccumulator> accRef, AtomicReference<Object2LongOpenHashMap<AEKey>> targetRef, AtomicBoolean drainRequested) {
      this.executor.execute(() -> {
         AEAccumulator acc = accRef.get();
         if (acc == null) {
            drainRequested.set(false);
         } else if (targetRef.get() != null) {
            drainRequested.set(false);
         } else {
            Object2LongOpenHashMap<AEKey> drainedData = new Object2LongOpenHashMap();
            acc.drainTo(drainedData);
            if (!drainedData.isEmpty() && !targetRef.compareAndSet(null, drainedData)) {
               drainedData.object2LongEntrySet().fastForEach(e -> acc.add((AEKey)e.getKey(), e.getLongValue()));
            }

            drainRequested.set(false);
         }
      });
   }

   public void shutDownGracefully() {
      this.executor.shutdown();

      try {
         if (!this.executor.awaitTermination(TIME_OUT, TimeUnit.SECONDS)) {
            this.executor.shutdownNow();
         }
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         this.executor.shutdownNow();
      }
   }

   @Override
   public void close() {
      this.shutDownGracefully();
   }
}
