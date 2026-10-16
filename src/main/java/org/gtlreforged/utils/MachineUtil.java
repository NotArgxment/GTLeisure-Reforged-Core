package org.gtlreforged.utils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import org.gtlreforged.api.recipe.RecipeResult;
import org.gtlreforged.api.recipe.RecipeRunnerHelper;

public class MachineUtil {

   public static final BlockPos[] EMPTY_POS_ARRAY = new BlockPos[0];
   private static final Lazy<Map<Item, EquipmentSlot>> armorMap = Lazy.of(
      () -> Map.of(
         Registries.getItem("kubejs:magnetohydrodynamicallyconstrainedstarmatter_boots"),
         EquipmentSlot.FEET,
         Registries.getItem("kubejs:magnetohydrodynamicallyconstrainedstarmatter_leggings"),
         EquipmentSlot.LEGS,
         Registries.getItem("kubejs:magnetohydrodynamicallyconstrainedstarmatter_chestplate"),
         EquipmentSlot.CHEST,
         Registries.getItem("kubejs:magnetohydrodynamicallyconstrainedstarmatter_helmet"),
         EquipmentSlot.HEAD
      )
   );

   private MachineUtil() {
      throw new IllegalAccessError();
   }

   public static BlockPos getOffsetPos(int a, int b, Direction facing, BlockPos pos) {
      int x = 0;
      int z = 0;
      switch (facing) {
         case NORTH:
            z = a;
            break;
         case SOUTH:
            z = -a;
            break;
         case WEST:
            x = a;
            break;
         case EAST:
            x = -a;
      }

      return pos.offset(x, b, z);
   }

   public static boolean inputItem(WorkableMultiblockMachine machine, ItemStack item) {
      GTRecipe recipe = new GTRecipeBuilder(Registries.getResourceKey(item), GTRecipeTypes.DUMMY_RECIPES).inputItems(item).buildRawRecipe();
      if (RecipeRunnerHelper.matchRecipeInputNocache(machine, recipe)) {
         return RecipeRunnerHelper.handleRecipeInputNocache(machine, recipe);
      }

      RecipeResult.of(machine, RecipeResult.fail(Component.translatable("gtceu.recipe.fail.no.input.item", new Object[]{item.m_41611_()})));
      return false;
   }

   public static boolean outputItem(WorkableMultiblockMachine machine, ItemStack item) {
      if (!item.m_41619_()) {
         GTRecipe recipe = new GTRecipeBuilder(Registries.getResourceKey(item), GTRecipeTypes.DUMMY_RECIPES).outputItems(item).buildRawRecipe();
         if (RecipeRunnerHelper.matchRecipeOutput(machine, recipe)) {
            return RecipeRunnerHelper.handleRecipeOutput(machine, recipe);
         }
      }

      return false;
   }

   public static boolean notConsumableItem(WorkableMultiblockMachine machine, ItemStack item) {
      return new GTRecipeBuilder(Registries.getResourceKey(item), GTRecipeTypes.DUMMY_RECIPES)
         .inputItems(item)
         .buildRawRecipe()
         .matchRecipe(machine)
         .isSuccess();
   }

   public static boolean notConsumableCircuit(WorkableMultiblockMachine machine, int configuration) {
      return new GTRecipeBuilder(GTCEu.id(String.valueOf(configuration)), GTRecipeTypes.DUMMY_RECIPES)
         .inputItems(IntCircuitIngredient.circuitInput(configuration))
         .buildRawRecipe()
         .matchRecipe(machine)
         .isSuccess();
   }

   public static boolean inputFluid(WorkableMultiblockMachine machine, FluidStack fluid) {
      GTRecipe recipe = new GTRecipeBuilder(Registries.getResourceKey(fluid), GTRecipeTypes.DUMMY_RECIPES).inputFluids(fluid).buildRawRecipe();
      if (RecipeRunnerHelper.matchRecipeInputNocache(machine, recipe)) {
         return RecipeRunnerHelper.handleRecipeInputNocache(machine, recipe);
      }

      RecipeResult.of(machine, RecipeResult.fail(Component.translatable("gtceu.recipe.fail.no.input.fluid", new Object[]{fluid.getDisplayName()})));
      return false;
   }

   public static boolean outputFluid(WorkableMultiblockMachine machine, FluidStack fluid) {
      if (!fluid.isEmpty()) {
         GTRecipe recipe = new GTRecipeBuilder(Registries.getResourceKey(fluid), GTRecipeTypes.DUMMY_RECIPES).outputFluids(fluid).buildRawRecipe();
         if (RecipeRunnerHelper.matchRecipeOutput(machine, recipe)) {
            return RecipeRunnerHelper.handleRecipeOutput(machine, recipe);
         }
      }

      return false;
   }

   public static boolean inputEU(WorkableMultiblockMachine machine, long eu) {
      GTRecipe recipe = new GTRecipeBuilder(GTCEu.id(String.valueOf(eu)), GTRecipeTypes.DUMMY_RECIPES).EUt(eu).buildRawRecipe();
      return recipe.matchTickRecipe(machine).isSuccess() ? recipe.handleTickRecipeIO(IO.IN, machine, machine.recipeLogic.getChanceCaches()) : false;
   }

   public static void createItemEntity(ServerLevel level, double x, double y, double z, ItemStack itemStack) {
      ItemEntity newItem = new ItemEntity(level, x, y, z, itemStack);
      newItem.m_20334_(0.0, 0.2, 0.0);
      newItem.m_32010_(10);
      level.m_7967_(newItem);
   }

   public static boolean hasFullArmorSet(ServerPlayer player) {
      return ((Map)armorMap.get()).entrySet().stream().allMatch(entry -> player.m_6844_((EquipmentSlot)entry.getValue()).m_150930_((Item)entry.getKey()));
   }
}
