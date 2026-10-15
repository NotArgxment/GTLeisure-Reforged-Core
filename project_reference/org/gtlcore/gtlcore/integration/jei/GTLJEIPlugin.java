package org.gtlcore.gtlcore.integration.jei;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.LDLib;
import java.util.ArrayList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.common.data.machines.MultiBlockMachineA;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class GTLJEIPlugin implements IModPlugin {
   @NotNull
   public ResourceLocation getPluginUid() {
      return GTLCore.id("jei_plugin");
   }

   public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
      if (!LDLib.isReiLoaded() && !LDLib.isEmiLoaded()) {
         registration.addRecipeCatalyst(MultiBlockMachineA.ADVANCED_MULTI_SMELTER.asStack(), new RecipeType[]{RecipeTypes.SMELTING});
         registration.addRecipeCatalyst(MultiBlockMachineA.DIMENSIONALLY_TRANSCENDENT_STEAM_OVEN.asStack(), new RecipeType[]{RecipeTypes.SMELTING});
      }
   }

   public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
      if (!LDLib.isReiLoaded() && !LDLib.isEmiLoaded()) {
         registration.useNbtForSubtypes(new Item[]{GTItems.TURBINE_ROTOR.m_5456_()});
         registration.useNbtForSubtypes(new Item[]{GTItems.INTEGRATED_CIRCUIT.m_5456_()});
      }
   }

   public void onRuntimeAvailable(@NotNull IJeiRuntime runtime) {
      ArrayList<ItemStack> list = new ArrayList<>();

      for (int i = 1; i <= 32; i++) {
         list.add(IntCircuitBehaviour.stack(i));
      }

      runtime.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, list);
   }
}
