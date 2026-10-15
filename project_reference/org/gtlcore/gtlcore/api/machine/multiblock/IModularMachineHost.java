package org.gtlcore.gtlcore.api.machine.multiblock;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo;
import org.jetbrains.annotations.NotNull;

public interface IModularMachineHost<H extends IModularMachineHost<H>> {
   @NotNull
   Set<IModularMachineModule<H, ?>> getModuleSet();

   BlockPos[] getModuleScanPositions();

   BlockPos getPos();

   Level getLevel();

   boolean isFormed();

   default <M extends IModularMachineModule<H, M>> void addModule(@NotNull M module) {
      this.getModuleSet().add(module);
   }

   default <M extends IModularMachineModule<H, M>> void removeModule(@NotNull M module) {
      this.getModuleSet().remove(module);
   }

   @NotNull
   default Set<IModularMachineModule<H, ?>> getModules() {
      return Collections.unmodifiableSet(this.getModuleSet());
   }

   default void safeClearModules() {
      for (IModularMachineModule<H, ?> module : this.getModules()) {
         H self = (H)this;
         module.removeFromHost(self);
      }

      this.getModuleSet().clear();
   }

   default void scanAndConnectModules() {
      Level level = this.getLevel();
      if (level != null) {
         BlockPos[] positions = this.getModuleScanPositions();
         H self = (H)this;

         for (BlockPos pos : positions) {
            MetaMachine machine = MetaMachine.getMachine(level, pos);
            if (this.isValidModule(machine)) {
               IModularMachineModule<H, ?> module = (IModularMachineModule<H, ?>)machine;
               assert module != null;
               module.connectToHost(self);
            }
         }
      }
   }

   default boolean isValidModule(MetaMachine machine) {
      if (machine instanceof IModularMachineModule<?, ?> module) {
         if (!module.isFormed()) {
            return false;
         }

         try {
            return module.getHostType().isInstance(this);
         } catch (Exception e) {
            return false;
         }
      } else {
         return false;
      }
   }

   default int getFormedModuleCount() {
      return Ints.saturatedCast(this.getModules().stream().filter(IModularMachineModule::isFormed).count());
   }

   default <M extends IModularMachineModule<H, M>> int getFormedModuleCount(Class<M> moduleClass) {
      return Ints.saturatedCast(this.getModules().stream().filter(module -> moduleClass.isInstance(module) && module.isFormed()).count());
   }

   @NotNull
   default <M extends IModularMachineModule<H, M>> Set<M> getModulesOfType(Class<M> moduleClass) {
      return this.getModules().stream().filter(moduleClass::isInstance).map(moduleClass::cast).collect(Collectors.toSet());
   }

   default boolean exceedsModuleLimit() {
      return this.getFormedModuleCount() > this.getMaxModuleCount();
   }

   default int getMaxModuleCount() {
      return 2147483647;
   }

   @OnlyIn(Dist.CLIENT)
   @NotNull
   default List<ModuleRenderInfo> getModulesForRendering() {
      return Collections.emptyList();
   }
}
