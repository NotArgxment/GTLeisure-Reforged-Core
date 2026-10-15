package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineHost;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule;
import org.gtlcore.gtlcore.common.data.machines.AdvancedMultiBlockMachine;
import org.gtlcore.gtlcore.utils.datastructure.ModuleRenderInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SuprachronalAssemblyLineMachine extends WorkableElectricMultiblockMachine implements IModularMachineHost<SuprachronalAssemblyLineMachine> {
   private final Set<IModularMachineModule<SuprachronalAssemblyLineMachine, ?>> modules = new ReferenceOpenHashSet();
   private int mam = 0;

   public SuprachronalAssemblyLineMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   private int getMAM() {
      if (this.getOffsetTimer() % 20L == 0L) {
         this.mam = this.getFormedModuleCount();
      }

      return this.mam;
   }

   @NotNull
   @Override
   public Set<IModularMachineModule<SuprachronalAssemblyLineMachine, ?>> getModuleSet() {
      return this.modules;
   }

   @Override
   public BlockPos[] getModuleScanPositions() {
      BlockPos pos = this.getPos();
      return new BlockPos[]{pos.m_7918_(3, 0, 0), pos.m_7918_(-3, 0, 0), pos.m_7918_(0, 0, 3), pos.m_7918_(0, 0, -3)};
   }

   @OnlyIn(Dist.CLIENT)
   @NotNull
   @Override
   public List<ModuleRenderInfo> getModulesForRendering() {
      return List.of(
         new ModuleRenderInfo(
            new BlockPos(0, 0, 3), Direction.WEST, Direction.UP, Direction.WEST, Direction.UP, AdvancedMultiBlockMachine.SUPRACHRONAL_ASSEMBLY_LINE_MODULE
         ),
         new ModuleRenderInfo(
            new BlockPos(0, 0, -3), Direction.WEST, Direction.UP, Direction.WEST, Direction.UP, AdvancedMultiBlockMachine.SUPRACHRONAL_ASSEMBLY_LINE_MODULE
         )
      );
   }

   @Override
   public int getMaxModuleCount() {
      return 2;
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.safeClearModules();
      this.scanAndConnectModules();
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.safeClearModules();
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L && this.exceedsModuleLimit()) {
         this.getRecipeLogic().setProgress(0);
      }

      return value;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (this.exceedsModuleLimit()) {
         this.getRecipeLogic().interruptRecipe();
         return false;
      } else {
         return super.beforeWorking(recipe);
      }
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(Component.m_237110_("tooltip.gtlcore.installed_module_count", new Object[]{this.getMAM()}));
      }
   }
}
