package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.TankWidget;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Player;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeSteamHatchPartMachine extends FluidHatchPartMachine {
   public final boolean is_steel;

   public LargeSteamHatchPartMachine(IMachineBlockEntity holder, IO io, int tank, boolean steel, Object... args) {
      super(holder, steel ? 0 : 1, io, tank * FluidHelper.getBucket(), 1, args);
      this.is_steel = steel;
   }

   protected NotifiableFluidTank createTank(long initialCapacity, int slots, Object... args) {
      return super.createTank(initialCapacity, slots, new Object[0]).setFilter(fluidStack -> fluidStack.getFluid().m_205067_(GTMaterials.Steam.getFluidTag()));
   }

   public ModularUI createUI(Player entityPlayer) {
      return new ModularUI(176, 166, this, entityPlayer)
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND_STEAM.get(this.is_steel)})
         .widget(new ImageWidget(7, 16, 81, 55, GuiTextures.DISPLAY_STEAM.get(this.is_steel)))
         .widget(new LabelWidget(11, 20, "gtceu.gui.fluid_amount"))
         .widget(new LabelWidget(11, 30, () -> this.tank.getFluidInTank(0).getAmount() + "").setTextColor(-1).setDropShadow(true))
         .widget(new LabelWidget(6, 6, this.getBlockState().m_60734_().m_7705_()))
         .widget(new TankWidget(this.tank.getStorages()[0], 90, 35, true, true).setBackground(GuiTextures.FLUID_SLOT))
         .widget(UITemplate.bindPlayerInventory(entityPlayer.m_150109_(), GuiTextures.SLOT_STEAM.get(this.is_steel), 7, 84, true));
   }
}
