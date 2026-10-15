package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.data.GTLMaterials;
import org.jetbrains.annotations.Nullable;

public class SpaceProbeSurfaceReceptionMachine extends WorkableElectricMultiblockMachine {
   @Nullable
   private BlockPos cachedCheckPos;

   public SpaceProbeSurfaceReceptionMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.cachedCheckPos = this.findTopBlock();
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.cachedCheckPos = null;
   }

   @Nullable
   protected BlockPos findTopBlock() {
      Level level = this.getLevel();
      if (level == null) {
         return null;
      }

      BlockPos pos = this.getPos();
      BlockPos[] coordinates = new BlockPos[]{pos.m_7918_(4, 8, 0), pos.m_7918_(-4, 8, 0), pos.m_7918_(0, 8, 4), pos.m_7918_(0, 8, -4)};

      for (BlockPos checkPos : coordinates) {
         if (level.m_8055_(checkPos).m_60713_(ChemicalHelper.getBlock(TagPrefix.frameGt, GTLMaterials.BlackTitanium))) {
            return checkPos.m_7918_(0, 1, 0);
         }
      }

      return null;
   }

   public boolean beforeWorking(@Nullable GTRecipe recipe) {
      if (this.cachedCheckPos == null) {
         return false;
      } else if (!Objects.requireNonNull(this.getLevel()).m_45527_(this.cachedCheckPos)) {
         RecipeResult.of(this, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.block")));
         return false;
      } else {
         return super.beforeWorking(recipe);
      }
   }
}
