package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.machine.trait.INFFluidDrillLogic;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class INFFluidDrillMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {
   private final int tier;

   public INFFluidDrillMachine(IMachineBlockEntity holder, int tier) {
      super(holder, new Object[0]);
      this.tier = tier;
   }

   protected RecipeLogic createRecipeLogic(Object... args) {
      return new INFFluidDrillLogic(this);
   }

   @NotNull
   public INFFluidDrillLogic getRecipeLogic() {
      return (INFFluidDrillLogic)super.getRecipeLogic();
   }

   public int getEnergyTier() {
      List<IRecipeHandler<?>> energyContainer = (List<IRecipeHandler<?>>)this.getCapabilitiesProxy().get(IO.IN, EURecipeCapability.CAP);
      if (energyContainer == null) {
         return this.tier;
      }

      EnergyContainerList energyCont = new EnergyContainerList(
         energyContainer.stream().filter(IEnergyContainer.class::isInstance).map(IEnergyContainer.class::cast).toList()
      );
      return Math.min(this.tier + 1, Math.max(this.tier, GTUtil.getFloorTierByVoltage(energyCont.getInputVoltage())));
   }

   public void addDisplayText(List<Component> textList) {
      if (this.isFormed()) {
         int energyContainer = this.getEnergyTier();
         long maxVoltage = GTValues.V[energyContainer];
         String voltageName = GTValues.VNF[energyContainer];
         textList.add(Component.m_237110_("gtceu.multiblock.max_energy_per_tick", new Object[]{maxVoltage, voltageName}));
         if (this.getRecipeLogic().getVeinFluid() != null) {
            Fluid drilledFluid = this.getRecipeLogic().getVeinFluid();
            Component fluidInfo = drilledFluid.getFluidType().getDescription().m_6881_().m_130940_(ChatFormatting.GREEN);
            textList.add(Component.m_237110_("gtceu.multiblock.fluid_rig.drilled_fluid", new Object[]{fluidInfo}).m_130940_(ChatFormatting.GRAY));
            Component amountInfo = Component.m_237113_(FormattingUtil.formatNumbers(this.getRecipeLogic().getFluidToProduce() * 20L / 20L) + " mB/s")
               .m_130940_(ChatFormatting.BLUE);
            textList.add(Component.m_237110_("gtceu.multiblock.fluid_rig.fluid_amount", new Object[]{amountInfo}).m_130940_(ChatFormatting.GRAY));
         } else {
            Component noFluid = Component.m_237115_("gtceu.multiblock.fluid_rig.no_fluid_in_area").m_130940_(ChatFormatting.RED);
            textList.add(Component.m_237110_("gtceu.multiblock.fluid_rig.drilled_fluid", new Object[]{noFluid}).m_130940_(ChatFormatting.GRAY));
         }
      } else {
         Component tooltip = Component.m_237115_("gtceu.multiblock.invalid_structure.tooltip").m_130940_(ChatFormatting.GRAY);
         textList.add(
            Component.m_237115_("gtceu.multiblock.invalid_structure")
               .m_130948_(Style.f_131099_.m_131140_(ChatFormatting.RED).m_131144_(new HoverEvent(Action.f_130831_, tooltip)))
         );
      }
   }

   public static int getRigMultiplier(int tier) {
      return 256;
   }

   public static Block getCasingState(int tier) {
      return (Block)GTLBlocks.IRIDIUM_CASING.get();
   }

   public int getTier() {
      return this.tier;
   }
}
