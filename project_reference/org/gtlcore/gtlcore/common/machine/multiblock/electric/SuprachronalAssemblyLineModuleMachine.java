package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.machine.multiblock.IModularMachineModule;
import org.gtlcore.gtlcore.common.data.GTLRecipeModifiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SuprachronalAssemblyLineModuleMachine
   extends WorkableElectricMultiblockMachine
   implements IModularMachineModule<SuprachronalAssemblyLineMachine, SuprachronalAssemblyLineModuleMachine>,
   IMachineLife {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      SuprachronalAssemblyLineModuleMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   @Nullable
   private BlockPos hostPosition;
   @Nullable
   private SuprachronalAssemblyLineMachine host;

   public SuprachronalAssemblyLineModuleMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @NotNull
   @Override
   public Class<SuprachronalAssemblyLineMachine> getHostType() {
      return SuprachronalAssemblyLineMachine.class;
   }

   @Override
   public BlockPos[] getHostScanPositions() {
      BlockPos pos = this.getPos();
      return new BlockPos[]{pos.m_7918_(3, 0, 0), pos.m_7918_(-3, 0, 0), pos.m_7918_(0, 0, 3), pos.m_7918_(0, 0, -3)};
   }

   public void onConnected(@NotNull SuprachronalAssemblyLineMachine host) {
      this.getRecipeLogic().updateTickSubscription();
   }

   public int getParallel() {
      return this.host != null ? GTLRecipeModifiers.getHatchParallel(this.host) : 0;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L && !this.isConnectedToHost()) {
         this.getRecipeLogic().setProgress(0);
      }

      return value;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (!this.isConnectedToHost()) {
         this.getRecipeLogic().interruptRecipe();
         return false;
      } else {
         return super.beforeWorking(recipe);
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      if (!this.findAndConnectToHost()) {
         this.removeFromHost(this.host);
      }
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.removeFromHost(this.host);
   }

   public void onPartUnload() {
      super.onPartUnload();
      this.removeFromHost(this.host);
   }

   public void onMachineRemoved() {
      this.removeFromHost(this.host);
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(
            Component.m_237110_(
                  "gtceu.multiblock.parallel",
                  new Object[]{Component.m_237113_(FormattingUtil.formatNumbers(this.getParallel())).m_130940_(ChatFormatting.DARK_PURPLE)}
               )
               .m_130940_(ChatFormatting.GRAY)
         );
         textList.add(Component.m_237115_(this.isConnectedToHost() ? "tooltip.gtlcore.module_installed" : "tooltip.gtlcore.module_not_installed"));
      }
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   @Nullable
   @Override
   public BlockPos getHostPosition() {
      return this.hostPosition;
   }

   @Override
   public void setHostPosition(@Nullable BlockPos hostPosition) {
      this.hostPosition = hostPosition;
   }

   @Nullable
   public SuprachronalAssemblyLineMachine getHost() {
      return this.host;
   }

   public void setHost(@Nullable SuprachronalAssemblyLineMachine host) {
      this.host = host;
   }
}
