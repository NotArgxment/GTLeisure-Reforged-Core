package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TierCasingMachine extends WorkableElectricMultiblockMachine {
   protected final String tierType;
   protected int casingTier = 0;

   public TierCasingMachine(IMachineBlockEntity holder, String tierType, Object... args) {
      super(holder, args);
      this.tierType = tierType;
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.casingTier = (Integer)this.getMultiblockState().getMatchContext().get(this.tierType);
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.casingTier = 0;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (recipe != null && recipe.data.m_128441_(this.tierType) && recipe.data.m_128451_(this.tierType) > this.casingTier) {
         RecipeResult.of(this, RecipeResult.FAIL_NO_ENOUGH_TIER);
         this.getRecipeLogic().interruptRecipe();
         return false;
      } else {
         return super.beforeWorking(recipe);
      }
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(Component.m_237110_("gtceu.casings.tier", new Object[]{this.casingTier}));
      }
   }

   public String getTierType() {
      return this.tierType;
   }

   public int getCasingTier() {
      return this.casingTier;
   }
}
