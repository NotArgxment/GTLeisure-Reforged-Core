package org.gtlcore.gtlcore.integration.jade.provider;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.integration.jade.GTElementHelper;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEPatternBufferPartMachine;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.Registries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;

public class MEPatternBufferProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
   public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity && blockEntity.getMetaMachine() instanceof MEPatternBufferPartMachine) {
         CompoundTag serverData = blockAccessor.getServerData();
         readBufferContents(iTooltip, serverData);
      }
   }

   public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity
         && blockEntity.getMetaMachine() instanceof MEPatternBufferPartMachine buffer) {
         putTag(compoundTag, buffer);
      }
   }

   public static void readBufferContents(ITooltip iTooltip, CompoundTag serverData) {
      iTooltip.add(Component.m_237110_("gtceu.top.proxies_bound", new Object[]{serverData.m_128451_("proxies")}).m_130940_(ChatFormatting.LIGHT_PURPLE));
      ListTag itemTags = serverData.m_128437_("items", 10);
      ListTag fluidTags = serverData.m_128437_("fluids", 10);

      for (Tag t : itemTags) {
         if (t instanceof CompoundTag itemTag) {
            Item item = Registries.getItem(itemTag.m_128461_("item"));
            long count = itemTag.m_128454_("real");
            if (item != null && count > 0L) {
               ItemStack stack = new ItemStack(item);
               iTooltip.add(iTooltip.getElementHelper().smallItem(new ItemStack(item)));
               Component text = Component.m_237113_(" ")
                  .m_7220_(Component.m_237113_(String.valueOf(count)).m_130940_(ChatFormatting.DARK_PURPLE))
                  .m_7220_(Component.m_237113_(" \u00d7 ").m_130940_(ChatFormatting.WHITE))
                  .m_7220_(stack.m_41786_().m_6881_().m_130940_(ChatFormatting.GOLD));
               iTooltip.append(text);
            }
         }
      }

      for (Tag t : fluidTags) {
         if (t instanceof CompoundTag fluidTag) {
            Fluid fluid = Registries.getFluid(fluidTag.m_128461_("fluid"));
            long amount = fluidTag.m_128454_("real");
            if (fluid != null && amount > 0L) {
               iTooltip.add(GTElementHelper.smallFluid(JadeFluidObject.of(fluid)));
               Component text = Component.m_237113_(" ")
                  .m_130946_(amount < 1000L ? amount + "mB" : NumberUtils.formatLong(amount / 1000L) + "B")
                  .m_130940_(ChatFormatting.DARK_PURPLE)
                  .m_7220_(Component.m_237113_(" ").m_130940_(ChatFormatting.WHITE))
                  .m_7220_(fluid.getFluidType().getDescription().m_6881_().m_130940_(ChatFormatting.DARK_AQUA));
               iTooltip.append(text);
            }
         }
      }

      ListTag pending = serverData.m_128437_("pending", 10);
      if (!pending.isEmpty()) {
         iTooltip.add(Component.m_237115_("gtceu.top.pending_refunds").m_130944_(new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.BOLD}));

         for (Tag tag : pending) {
            if (tag instanceof CompoundTag keyTag && !keyTag.m_128456_()) {
               AEKey key = AEKey.fromTagGeneric((CompoundTag)keyTag.m_128423_("key"));
               if (key instanceof AEItemKey itemKey) {
                  ItemStack stack = itemKey.toStack();
                  long count = keyTag.m_128454_("count");
                  iTooltip.add(iTooltip.getElementHelper().smallItem(stack));
                  Component text = Component.m_237113_(" ")
                     .m_130946_(String.valueOf(count))
                     .m_130946_(" \u00d7 ")
                     .m_7220_(ComponentUtils.m_130748_(stack.m_41720_().m_41466_()).m_130940_(ChatFormatting.WHITE))
                     .m_130940_(ChatFormatting.WHITE);
                  iTooltip.append(text);
               } else if (key instanceof AEFluidKey fluidKey) {
                  long count = keyTag.m_128454_("count");
                  FluidStack fluid = fluidKey.toStack(Ints.saturatedCast(count));
                  iTooltip.add(GTElementHelper.smallFluid(JadeFluidObject.of(fluid.getFluid(), count)));
                  Component text = Component.m_237113_(" ")
                     .m_130946_(count < 1000L ? count + "mB" : NumberUtils.formatLong(count / 1000L) + "B")
                     .m_130946_(" ")
                     .m_7220_(ComponentUtils.m_130748_(fluid.getDisplayName()).m_130940_(ChatFormatting.WHITE))
                     .m_130940_(ChatFormatting.WHITE);
                  iTooltip.append(text);
               }
            }
         }
      }
   }

   public static void putTag(CompoundTag compoundTag, MEPatternBufferPartMachine buffer) {
      compoundTag.m_128405_("proxies", buffer.getProxies().size());
      Pair<Object2LongOpenHashMap<Item>, Object2LongOpenHashMap<Fluid>> merged = AEUtils.mergeInternalSlot(buffer.getInternalInventory());
      Object2LongOpenHashMap<Item> items = (Object2LongOpenHashMap<Item>)merged.getLeft();
      Object2LongOpenHashMap<Fluid> fluids = (Object2LongOpenHashMap<Fluid>)merged.getRight();
      ListTag itemTags = new ListTag();
      ObjectIterator<Entry<Item>> it = items.object2LongEntrySet().fastIterator();

      while (it.hasNext()) {
         Entry<Item> entry = (Entry<Item>)it.next();
         Item item = (Item)entry.getKey();
         ResourceLocation key = Registries.getResourceKey(item);
         if (key != null) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.m_128359_("item", key.toString());
            itemTag.m_128356_("real", entry.getLongValue());
            itemTags.add(itemTag);
         }
      }

      compoundTag.m_128365_("items", itemTags);
      ListTag fluidTags = new ListTag();
      ObjectIterator<Entry<Fluid>> itx = fluids.object2LongEntrySet().fastIterator();

      while (itx.hasNext()) {
         Entry<Fluid> entry = (Entry<Fluid>)itx.next();
         Fluid fluid = (Fluid)entry.getKey();
         ResourceLocation key = Registries.getResourceKey(fluid);
         if (key != null) {
            CompoundTag fluidTag = new CompoundTag();
            fluidTag.m_128359_("fluid", key.toString());
            fluidTag.m_128356_("real", entry.getLongValue());
            fluidTags.add(fluidTag);
         }
      }

      compoundTag.m_128365_("fluids", fluidTags);
      ListTag listTag = new ListTag();
      ObjectIterator<Entry<AEKey>> itxx = buffer.getBuffer().object2LongEntrySet().fastIterator();

      while (itxx.hasNext()) {
         Entry<AEKey> entry = (Entry<AEKey>)itxx.next();
         long count = entry.getLongValue();
         if (count > 0L) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.m_128365_("key", ((AEKey)entry.getKey()).toTagGeneric());
            itemTag.m_128356_("count", count);
            listTag.add(itemTag);
         }
      }

      compoundTag.m_128365_("pending", listTag);
   }

   public ResourceLocation getUid() {
      return GTCEu.id("me_pattern_buffer");
   }
}
