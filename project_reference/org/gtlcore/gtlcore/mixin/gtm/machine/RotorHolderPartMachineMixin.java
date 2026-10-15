package org.gtlcore.gtlcore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotorHolderPartMachine.class)
public abstract class RotorHolderPartMachineMixin implements IRotorHolderMachine {
   @Mutable
   @Final
   @Shadow(remap = false)
   public final NotifiableItemStackHandler inventory;

   protected RotorHolderPartMachineMixin(NotifiableItemStackHandler inventory) {
      this.inventory = inventory;
   }

   @Inject(method = "<init>", at = @At("TAIL"))
   public void RotorHolderPartMachine(IMachineBlockEntity holder, int tier, CallbackInfo ci) {
      this.inventory.setFilter(itemstack -> itemstack.m_150930_(GTItems.TURBINE_ROTOR.m_5456_()));
   }

   public GTRecipe modifyRecipe(GTRecipe recipe) {
      GTRecipe modifiedRecipe = this.isFrontFaceFree() && this.hasRotor() ? recipe : null;
      if (modifiedRecipe != null) {
         return modifiedRecipe;
      }

      if (this.getRotorStack().m_41619_()) {
         RecipeResult.of((IRecipeLogicMachine)this.getControllers().get(0), RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.rotor.isEmpty")));
      } else {
         RecipeResult.of(
            (IRecipeLogicMachine)this.getControllers().get(0), RecipeResult.fail(Component.m_237115_("gtceu.multiblock.universal.rotor_obstructed"))
         );
      }

      return null;
   }

   @Overwrite(remap = false)
   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(0, 0, 34, 34);
      WidgetGroup container = new WidgetGroup(4, 4, 26, 26);
      container.addWidget(
         new SlotWidget(this.inventory.storage, 0, 4, 4, true, true)
            .setBackground(new IGuiTexture[]{GuiTextures.SLOT, GuiTextures.TURBINE_OVERLAY})
            .setHoverTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.turbine_rotor_only")})
      );
      container.setBackground(new IGuiTexture[]{GuiTextures.BACKGROUND_INVERSE});
      group.addWidget(container);
      return group;
   }
}
