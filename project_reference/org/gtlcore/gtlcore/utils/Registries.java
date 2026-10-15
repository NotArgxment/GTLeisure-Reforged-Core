package org.gtlcore.gtlcore.utils;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.gtlcore.gtlcore.GTLCore;

public class Registries {
   public static Item getItem(String s) {
      Item i = (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
      if (i == Items.f_41852_) {
         GTLCore.LOGGER.atError().log("\u672a\u627e\u5230ID\u4e3a{}\u7684\u7269\u54c1", s);
         return Items.f_42127_;
      } else {
         return i;
      }
   }

   public static ItemStack getItemStack(String s) {
      return getItemStack(s, 1);
   }

   public static ItemStack getItemStack(String s, int a) {
      return new ItemStack(getItem(s), a);
   }

   public static Block getBlock(String s) {
      Block b = (Block)ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
      if (b == Blocks.f_50016_) {
         GTLCore.LOGGER.atError().log("\u672a\u627e\u5230ID\u4e3a{}\u7684\u65b9\u5757", s);
         return Blocks.f_50375_;
      } else {
         return b;
      }
   }

   public static Fluid getFluid(String s) {
      return getFluid(new ResourceLocation(s));
   }

   public static Fluid getFluid(ResourceLocation resourceLocation) {
      Fluid f = (Fluid)ForgeRegistries.FLUIDS.getValue(resourceLocation);
      if (f == Fluids.f_76191_) {
         GTLCore.LOGGER.atError().log("\u672a\u627e\u5230ID\u4e3a{}\u7684\u6d41\u4f53", resourceLocation.toString());
         return Fluids.f_76193_;
      } else {
         return f;
      }
   }

   public static ResourceKey<Level> getDimension(String s) {
      return ResourceKey.m_135785_(net.minecraft.core.registries.Registries.f_256858_, new ResourceLocation(s));
   }

   public static RecipeManager getRecipeManager() {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      return server != null && Thread.currentThread() == server.m_6304_()
         ? server.m_129894_()
         : Objects.requireNonNull(Minecraft.m_91087_().m_91403_()).m_105141_();
   }

   public static ResourceLocation getResourceKey(ItemStack itemStack) {
      return ForgeRegistries.ITEMS.getKey(itemStack.m_41720_());
   }

   public static ResourceLocation getResourceKey(Item item) {
      return ForgeRegistries.ITEMS.getKey(item);
   }

   public static ResourceLocation getResourceKey(FluidStack fluidStack) {
      return ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid());
   }

   public static ResourceLocation getResourceKey(Fluid fluid) {
      return ForgeRegistries.FLUIDS.getKey(fluid);
   }

   public static ResourceLocation getResourceKey(Block block) {
      return ForgeRegistries.BLOCKS.getKey(block);
   }

   public static String getItemId(ItemStack itemStack) {
      return getResourceKey(itemStack).toString();
   }

   public static String getItemId(Item item) {
      return getResourceKey(item).toString();
   }

   public static String getFluidId(FluidStack fluidStack) {
      return getResourceKey(fluidStack).toString();
   }

   public static String getBlockId(Block block) {
      return getResourceKey(block).toString();
   }
}
