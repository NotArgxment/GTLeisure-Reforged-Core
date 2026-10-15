package org.gtlcore.gtlcore.mixin.gtm.fix;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationProvider;
import com.gregtechceu.gtceu.api.capability.IOpticalComputationReceiver;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton.Toggle;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.research.DataBankMachine;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.HoverEvent.Action;
import org.gtlcore.gtlcore.api.machine.trait.ICheckPatternMachine;
import org.gtlcore.gtlcore.api.machine.trait.ILockRecipe;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.api.recipe.RecipeText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorkableElectricMultiblockMachine.class)
public abstract class WorkableElectricMultiblockMachineMixin extends WorkableMultiblockMachine implements IFancyUIMachine, ICheckPatternMachine {
   @Shadow(remap = false)
   protected EnergyContainerList energyContainer;
   @Shadow(remap = false)
   protected int tier;

   public WorkableElectricMultiblockMachineMixin(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Shadow(remap = false)
   public abstract EnergyContainerList getEnergyContainer();

   @Shadow(remap = false)
   public boolean isGenerator() {
      throw new AssertionError();
   }

   @Overwrite(remap = false)
   public long getOverclockVoltage() {
      if (this.energyContainer == null) {
         this.energyContainer = this.getEnergyContainer();
      }

      return Math.max(this.energyContainer.getInputVoltage(), this.energyContainer.getOutputVoltage());
   }

   @Overwrite(remap = false)
   public long getMaxVoltage() {
      if (this.energyContainer == null) {
         this.energyContainer = this.getEnergyContainer();
      }

      return this.isGenerator()
         ? this.energyContainer.getOutputVoltage()
         : (
            this.energyContainer.getNumHighestInputContainers() > 1
               ? GTValues.V[Math.min(GTUtil.getTierByVoltage(this.energyContainer.getHighestInputVoltage()) + 1, 14)]
               : this.energyContainer.getHighestInputVoltage()
         );
   }

   public void attachConfigurators(ConfiguratorPanel configuratorPanel) {
      configuratorPanel.attachConfigurators(
         new IFancyConfigurator[]{
            new Toggle(
                  GuiTextures.BUTTON_POWER.getSubTexture(0.0, 0.0, 1.0, 0.5),
                  GuiTextures.BUTTON_POWER.getSubTexture(0.0, 0.5, 1.0, 0.5),
                  this::isWorkingEnabled,
                  (clickData, pressed) -> this.setWorkingEnabled(pressed)
               )
               .setTooltipsSupplier(pressed -> List.of(Component.m_237115_(pressed ? "behaviour.soft_hammer.enabled" : "behaviour.soft_hammer.disabled")))
         }
      );
      ICheckPatternMachine.attachConfigurators(configuratorPanel, this.self());
      if (!(this.self() instanceof IOpticalComputationReceiver)
         && !(this.self() instanceof IOpticalComputationProvider)
         && !(this.self() instanceof DataBankMachine)) {
         IRecipeCapabilityMachine.attachConfigurators(configuratorPanel, (WorkableElectricMultiblockMachine)this.self());
         ILockRecipe.attachRecipeLockable(configuratorPanel, this.getRecipeLogic());
      }
   }

   @Inject(
      method = "addDisplayText",
      at = @At(
         value = "INVOKE",
         target = "Lcom/gregtechceu/gtceu/api/machine/multiblock/WorkableElectricMultiblockMachine;getDefinition()Lcom/gregtechceu/gtceu/api/machine/MultiblockMachineDefinition;"
      ),
      remap = false
   )
   public void addDisplayText(List<Component> textList, CallbackInfo ci) {
      if (this.isFormed()
         && this.getRecipeLogic() instanceof IRecipeStatus status
         && status.getRecipeStatus() != null
         && status.getRecipeStatus().reason() != null) {
         textList.add(status.getRecipeStatus().reason().m_6881_().m_130940_(ChatFormatting.RED));
         if (status.getWorkingStatus() != null && status.getWorkingStatus().reason() != null) {
            textList.add(status.getWorkingStatus().reason().m_6881_().m_130940_(ChatFormatting.RED));
         }
      }

      if (this.getRecipeLogic() instanceof ILockRecipe iLockRecipe) {
         if (iLockRecipe.isLock() && iLockRecipe.getLockRecipe() != null) {
            textList.add(
               Component.m_237115_("gui.gtlcore.recipe_lock.recipe")
                  .m_130938_(
                     style -> style.m_131144_(
                        new HoverEvent(
                           Action.f_130831_,
                           RecipeText.getRecipeInputText(iLockRecipe.getLockRecipe()).m_7220_(RecipeText.getRecipeOutputText(iLockRecipe.getLockRecipe()))
                        )
                     )
                  )
            );
         } else {
            textList.add(Component.m_237115_("gui.gtlcore.recipe_lock.no_recipe"));
         }
      }
   }

   @Override
   public boolean hasButton() {
      return true;
   }
}
