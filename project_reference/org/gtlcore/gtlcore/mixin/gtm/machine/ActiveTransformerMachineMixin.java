package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.ActiveTransformerMachine;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.capability.IInt128EnergyContainer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ActiveTransformerMachine.class)
public abstract class ActiveTransformerMachineMixin extends WorkableElectricMultiblockMachine {
   @Shadow(remap = false)
   private IEnergyContainer powerOutput;
   @Shadow(remap = false)
   private IEnergyContainer powerInput;

   public ActiveTransformerMachineMixin(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Overwrite(remap = false)
   protected boolean isSubscriptionActive() {
      if (!this.isFormed()) {
         return false;
      } else if (this.powerInput == null || this.powerInput.getEnergyStored() <= 0L) {
         return false;
      } else {
         return this.powerOutput == null
            ? false
            : ((IInt128EnergyContainer)this.powerOutput)
                  .getInt128EnergyStored()
                  .compareTo(((IInt128EnergyContainer)this.powerOutput).getInt128EnergyCapacity())
               < 0;
      }
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      if (this.isFormed()) {
         if (!this.isWorkingEnabled()) {
            textList.add(Component.m_237115_("gtceu.multiblock.work_paused"));
         } else if (this.isActive()) {
            textList.add(Component.m_237115_("gtceu.multiblock.running"));
            textList.add(
               Component.m_237110_(
                  "gtceu.multiblock.active_transformer.max_input",
                  new Object[]{FormattingUtil.formatNumbers(Math.abs(this.powerInput.getInputVoltage() * this.powerInput.getInputAmperage()))}
               )
            );
            textList.add(
               Component.m_237110_(
                  "gtceu.multiblock.active_transformer.max_output",
                  new Object[]{FormattingUtil.formatNumbers(Math.abs(this.powerOutput.getOutputVoltage() * this.powerOutput.getOutputAmperage()))}
               )
            );
            textList.add(
               Component.m_237110_(
                  "gtceu.multiblock.active_transformer.average_in",
                  new Object[]{((IInt128EnergyContainer)this.powerInput).getInt128InputPerSec().divide(20L).toFormattedString()}
               )
            );
            textList.add(
               Component.m_237110_(
                  "gtceu.multiblock.active_transformer.average_out",
                  new Object[]{((IInt128EnergyContainer)this.powerOutput).getInt128OutputPerSec().divide(20L).toFormattedString()}
               )
            );
            if (!ConfigHolder.INSTANCE.machines.harmlessActiveTransformers) {
               textList.add(Component.m_237115_("gtceu.multiblock.active_transformer.danger_enabled"));
            }
         } else {
            textList.add(Component.m_237115_("gtceu.multiblock.idling"));
         }
      }
   }
}
