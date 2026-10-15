package org.gtlcore.gtlcore.common.item;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.item.component.IInteractionItem;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import java.util.Objects;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.IAutoConfiguratioGravityPart;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.IAutoConfigurationMaintenanceHatch;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.IGravityPartMachine;

public class ConfigurationCopyBehavior implements IToolBehavior, IInteractionItem {
   public static final ConfigurationCopyBehavior INSTANCE = new ConfigurationCopyBehavior();

   public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
      CompoundTag tags = ToolHelper.getBehaviorsTag(stack);
      if (context.m_43725_().m_7702_(context.m_8083_()) instanceof MetaMachineBlockEntity machineBlock) {
         MetaMachine metaMachine = machineBlock.getMetaMachine();
         if (metaMachine instanceof SimpleTieredMachine simpleTieredMachine) {
            if (Objects.requireNonNull(context.m_43723_()).m_6144_()) {
               this.getSTMCfg(tags, simpleTieredMachine);
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_copied"), true);
            } else if (tags.m_128471_("Configuration")) {
               this.setSTMCfg(tags, simpleTieredMachine);
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_pasted"), true);
            } else {
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_not_found"), true);
            }
         } else if (metaMachine instanceof ItemBusPartMachine itemBusPartMachine) {
            if (Objects.requireNonNull(context.m_43723_()).m_6144_()) {
               this.getBusCfg(tags, itemBusPartMachine);
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_copied"), true);
            } else if (tags.m_128471_("Configuration")) {
               this.setBusCfg(tags, itemBusPartMachine);
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_pasted"), true);
            } else {
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_not_found"), true);
            }
         } else if (metaMachine instanceof IAutoConfiguratioGravityPart acgp) {
            if (Objects.requireNonNull(context.m_43723_()).m_6144_()) {
               tags.m_128379_("Configuration", true);
               tags.m_128350_("durationMultiplier", acgp.getDurationMultiplier());
               tags.m_128405_("currentGravity", acgp.getCurrentGravity());
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_copied"), true);
            } else if (tags.m_128471_("Configuration")) {
               acgp.setDurationMultiplier(tags.m_128457_("durationMultiplier"));
               acgp.setCurrentGravity(tags.m_128451_("currentGravity"));
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_pasted"), true);
            } else {
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_not_found"), true);
            }
         } else if (metaMachine instanceof IAutoConfigurationMaintenanceHatch acmh) {
            if (Objects.requireNonNull(context.m_43723_()).m_6144_()) {
               tags.m_128379_("Configuration", true);
               tags.m_128350_("durationMultiplier", acmh.getDurationMultiplier());
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_copied"), true);
            } else if (tags.m_128471_("Configuration")) {
               acmh.setDurationMultiplier(tags.m_128457_("durationMultiplier"));
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_pasted"), true);
            } else {
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_not_found"), true);
            }
         } else if (metaMachine instanceof IGravityPartMachine gpm) {
            if (Objects.requireNonNull(context.m_43723_()).m_6144_()) {
               tags.m_128379_("Configuration", true);
               tags.m_128405_("currentGravity", gpm.getCurrentGravity());
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_copied"), true);
            } else if (tags.m_128471_("Configuration")) {
               gpm.setCurrentGravity(tags.m_128451_("currentGravity"));
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_pasted"), true);
            } else {
               context.m_43723_().m_5661_(Component.m_237115_("message.gtlcore.machine_data_not_found"), true);
            }
         }

         return InteractionResult.CONSUME;
      } else {
         return InteractionResult.PASS;
      }
   }

   private void getBusCfg(CompoundTag tags, ItemBusPartMachine metaMachine) {
      tags.m_128379_("Configuration", true);
      tags.m_128379_("isDistinct", metaMachine.isDistinct());
      tags.m_128379_("isWorkingEnabled", metaMachine.isWorkingEnabled());
      NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
      if (circuitInventory.handlerIO.support(IO.IN)) {
         int c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0));
         if (c > 0) {
            tags.m_128405_("circuit", c);
         }
      }
   }

   private void setBusCfg(CompoundTag tags, ItemBusPartMachine metaMachine) {
      metaMachine.setDistinct(tags.m_128471_("isDistinct"));
      metaMachine.setWorkingEnabled(tags.m_128471_("isWorkingEnabled"));
      int c = tags.m_128451_("circuit");
      NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
      if (c > 0 && circuitInventory.handlerIO.support(IO.IN)) {
         circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(c));
      }
   }

   private void getSTMCfg(CompoundTag tags, SimpleTieredMachine metaMachine) {
      tags.m_128379_("Configuration", true);
      boolean hasAutoOutputItem = metaMachine.hasAutoOutputItem();
      boolean hasAutoOutputFluid = metaMachine.hasAutoOutputFluid();
      tags.m_128379_("hasAutoOutputItem", hasAutoOutputItem);
      tags.m_128379_("hasAutoOutputFluid", hasAutoOutputFluid);
      if (hasAutoOutputItem) {
         tags.m_128359_("outputFacingItems", metaMachine.getOutputFacingItems().toString());
      }

      if (hasAutoOutputFluid) {
         tags.m_128359_("outputFacingFluids", metaMachine.getOutputFacingFluids().toString());
      }

      tags.m_128379_("allowInputFromOutputSideItems", metaMachine.isAllowInputFromOutputSideItems());
      tags.m_128379_("allowInputFromOutputSideFluids", metaMachine.isAllowInputFromOutputSideFluids());
      NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
      if (circuitInventory.handlerIO.support(IO.IN)) {
         int c = IntCircuitBehaviour.getCircuitConfiguration(circuitInventory.getStackInSlot(0));
         if (c > 0) {
            tags.m_128405_("circuit", c);
         }
      }
   }

   private void setSTMCfg(CompoundTag tags, SimpleTieredMachine metaMachine) {
      boolean hasAutoOutputItem = tags.m_128471_("hasAutoOutputItem");
      boolean hasAutoOutputFluid = tags.m_128471_("hasAutoOutputFluid");
      metaMachine.setAutoOutputItems(hasAutoOutputItem);
      metaMachine.setAutoOutputFluids(hasAutoOutputFluid);
      if (hasAutoOutputItem) {
         metaMachine.setOutputFacingItems(Direction.m_122402_(tags.m_128461_("outputFacingItems")));
      }

      if (hasAutoOutputFluid) {
         metaMachine.setOutputFacingFluids(Direction.m_122402_(tags.m_128461_("outputFacingFluids")));
      }

      metaMachine.setAllowInputFromOutputSideItems(tags.m_128471_("allowInputFromOutputSideItems"));
      metaMachine.setAllowInputFromOutputSideFluids(tags.m_128471_("allowInputFromOutputSideFluids"));
      int c = tags.m_128451_("circuit");
      NotifiableItemStackHandler circuitInventory = metaMachine.getCircuitInventory();
      if (c > 0 && circuitInventory.handlerIO.support(IO.IN)) {
         circuitInventory.setStackInSlot(0, IntCircuitBehaviour.stack(c));
      }
   }
}
