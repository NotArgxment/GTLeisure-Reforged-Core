package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GreenhouseMachine extends WorkableElectricMultiblockMachine {
   private int SkyLight = 15;

   public GreenhouseMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   private void getGreenhouseLight() {
      Level level = this.getLevel();
      BlockPos pos = this.getPos();
      this.SkyLight = 15;
      BlockPos[] coordinates = new BlockPos[]{
         pos.m_7918_(1, 2, 0),
         pos.m_7918_(1, 2, 1),
         pos.m_7918_(1, 2, -1),
         pos.m_7918_(0, 2, 1),
         pos.m_7918_(0, 2, -1),
         pos.m_7918_(-1, 2, 0),
         pos.m_7918_(-1, 2, 1),
         pos.m_7918_(-1, 2, -1),
         pos.m_7918_(2, 2, 0),
         pos.m_7918_(2, 2, -1),
         pos.m_7918_(2, 2, 1),
         pos.m_7918_(3, 2, 0),
         pos.m_7918_(3, 2, -1),
         pos.m_7918_(3, 2, 1),
         pos.m_7918_(-2, 2, 0),
         pos.m_7918_(-2, 2, -1),
         pos.m_7918_(-2, 2, 1),
         pos.m_7918_(-3, 2, 0),
         pos.m_7918_(-3, 2, -1),
         pos.m_7918_(-3, 2, 1),
         pos.m_7918_(-1, 2, 2),
         pos.m_7918_(0, 2, 2),
         pos.m_7918_(1, 2, 2),
         pos.m_7918_(-1, 2, 3),
         pos.m_7918_(0, 2, 3),
         pos.m_7918_(1, 2, 3),
         pos.m_7918_(-1, 2, -2),
         pos.m_7918_(0, 2, -2),
         pos.m_7918_(1, 2, -2),
         pos.m_7918_(-1, 2, -3),
         pos.m_7918_(0, 2, -3),
         pos.m_7918_(1, 2, -3)
      };

      for (BlockPos i : coordinates) {
         if (level != null && level.m_8055_(i).m_60734_() == GTBlocks.CASING_TEMPERED_GLASS.get()) {
            int l = level.m_45517_(LightLayer.SKY, i.m_7918_(0, 1, 0)) - level.m_7445_();
            if (l < this.SkyLight) {
               this.SkyLight = l;
            }
         }
      }
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      this.getGreenhouseLight();
      if (this.SkyLight == 0) {
         this.getRecipeLogic().interruptRecipe();
         RecipeResult.of(this, RecipeResult.FAIL_NO_SKYLIGHT);
         return false;
      } else {
         return super.beforeWorking(recipe);
      }
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.getOffsetTimer() % 20L == 0L) {
         this.getGreenhouseLight();
         if (this.SkyLight == 0) {
            RecipeResult.of(this, RecipeResult.FAIL_NO_SKYLIGHT);
            this.getRecipeLogic().setProgress(0);
         }

         if (this.SkyLight < 13) {
            RecipeResult.of(this, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.no.enough.skylight")));
            this.getRecipeLogic().setProgress(this.getRecipeLogic().getProgress() - 10);
         }
      }

      return value;
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         if (this.getOffsetTimer() % 10L == 0L) {
            this.getGreenhouseLight();
         }

         textList.add(Component.m_237110_("tooltip.gtlcore.current_light_level", new Object[]{this.SkyLight}));
      }
   }
}
