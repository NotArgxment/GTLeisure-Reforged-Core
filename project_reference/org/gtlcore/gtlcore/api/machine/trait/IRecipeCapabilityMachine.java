package org.gtlcore.gtlcore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.IParallelHatch;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.ConfiguratorPanel;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton.Toggle;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRecipeCapabilityMachine {
   @NotNull
   List<RecipeHandlePart> getNormalRecipeHandlePart(IO var1);

   @Nullable
   RecipeHandlePart getSharedRecipeHandlePart();

   boolean emptyRecipeHandlePart();

   boolean emptyHandlePart();

   List<MEPatternRecipeHandlePart> getMEPatternRecipeHandleParts();

   List<MEIORecipeHandlePart<?>> getMEOutputRecipeHandleParts();

   void tryAddAndActiveMERhp(MEPatternRecipeHandlePart var1, GTRecipe var2, int var3);

   void sortMEOutput();

   @Nullable
   IRecipeHandlePart getActiveRecipeHandle(GTRecipe var1);

   @NotNull
   Iterator<IRecipeHandlePart> getAllCachedRecipeHandlesIter(GTRecipe var1);

   @NotNull
   ReferenceSet<IRecipeHandlePart> getAllCachedRecipeHandles(GTRecipe var1);

   void tryAddAndActiveRhp(GTRecipe var1, IRecipeHandlePart var2);

   void upDate();

   boolean isDistinct();

   void setDistinct(boolean var1);

   default boolean itemOutPutAlwaysMatch() {
      return false;
   }

   default boolean fluidOutPutAlwaysMatch() {
      return false;
   }

   default boolean isRecipeOutputAlwaysMatch(GTRecipe recipe) {
      return false;
   }

   default IParallelHatch getParallelHatch() {
      return null;
   }

   default IMaintenanceMachine getMaintenanceMachine() {
      return null;
   }

   default IDataAccessHatch getDataAccessHatch() {
      return null;
   }

   static void attachConfigurators(ConfiguratorPanel configuratorPanel, WorkableElectricMultiblockMachine machine) {
      if (machine instanceof IRecipeCapabilityMachine distinctMachine) {
         configuratorPanel.attachConfigurators(
            new IFancyConfigurator[]{
               new Toggle(
                     GuiTextures.BUTTON_DISTINCT_BUSES.getSubTexture(0.0, 0.5, 1.0, 0.5),
                     GuiTextures.BUTTON_DISTINCT_BUSES.getSubTexture(0.0, 0.0, 1.0, 0.5),
                     distinctMachine::isDistinct,
                     (clickData, pressed) -> {
                        distinctMachine.setDistinct(pressed);
                        distinctMachine.upDate();
                     }
                  )
                  .setTooltipsSupplier(
                     pressed -> List.of(
                        Component.m_237115_("gtceu.multiblock.universal.distinct.all")
                           .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.YELLOW))
                           .m_7220_(Component.m_237115_(pressed ? "gtceu.multiblock.universal.distinct.yes" : "gtceu.multiblock.universal.distinct.no"))
                     )
                  )
            }
         );
      }
   }
}
