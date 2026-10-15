package org.gtlcore.gtlcore.integration.jade.provider;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.integration.jade.GTElementHelper;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.gtlcore.gtlcore.common.machine.multiblock.part.ae.MEMolecularAssemblerIOPartMachine;
import org.gtlcore.gtlcore.utils.NumberUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.fluid.JadeFluidObject;

public class MEMAIOProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
   public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity
         && blockEntity.getMetaMachine() instanceof MEMolecularAssemblerIOPartMachine) {
         CompoundTag serverData = blockAccessor.getServerData();
         readBufferContents(iTooltip, serverData);
      }
   }

   public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
      if (blockAccessor.getBlockEntity() instanceof IMachineBlockEntity blockEntity
         && blockEntity.getMetaMachine() instanceof MEMolecularAssemblerIOPartMachine ioPartMachine) {
         putTag(compoundTag, ioPartMachine);
      }
   }

   public static void readBufferContents(ITooltip iTooltip, CompoundTag serverData) {
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

   public static void putTag(CompoundTag compoundTag, MEMolecularAssemblerIOPartMachine maMachine) {
      ListTag listTag = new ListTag();
      ObjectIterator<Entry<AEKey>> it = maMachine.getBuffer().object2LongEntrySet().fastIterator();

      while (it.hasNext()) {
         Entry<AEKey> entry = (Entry<AEKey>)it.next();
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
      return GTCEu.id("me_molecular_assembler_io");
   }
}
