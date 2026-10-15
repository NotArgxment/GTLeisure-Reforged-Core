package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.integration.jade.provider.RecipeLogicProvider;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeStatus;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@Mixin(RecipeLogicProvider.class)
public abstract class RecipeLogicProviderMixin extends CapabilityBlockProvider<RecipeLogic> {
   protected RecipeLogicProviderMixin(ResourceLocation uid) {
      super(uid);
   }

   @Inject(method = "write(Lnet/minecraft/nbt/CompoundTag;Lcom/gregtechceu/gtceu/api/machine/trait/RecipeLogic;)V", at = @At("HEAD"), remap = false)
   protected void write(CompoundTag data, RecipeLogic capability, CallbackInfo ci) {
      if (capability instanceof IRecipeStatus status) {
         if (status.getRecipeStatus() != null && status.getRecipeStatus().reason() != null) {
            data.m_128359_("reason", Serializer.m_130703_(status.getRecipeStatus().reason()));
         }

         if (status.getWorkingStatus() != null && status.getWorkingStatus().reason() != null) {
            data.m_128359_("work_reason", Serializer.m_130703_(status.getWorkingStatus().reason()));
         }
      }
   }

   protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
      if (capData.m_128471_("Working")) {
         CompoundTag recipeInfo = capData.m_128469_("Recipe");
         if (!recipeInfo.m_128456_()) {
            long EUt = recipeInfo.m_128454_("EUt");
            boolean isInput = recipeInfo.m_128471_("isInput");
            boolean isSteam = false;
            if (EUt > 0L) {
               if (blockEntity instanceof MetaMachineBlockEntity mbe && mbe.getMetaMachine() instanceof ISteamMachine steamMachine) {
                  EUt = (long)Math.ceil(EUt * steamMachine.getConversionRate());
                  isSteam = true;
               }

               MutableComponent text;
               if (isSteam) {
                  text = Component.m_237113_(FormattingUtil.formatNumbers(EUt) + " mB/t").m_130940_(ChatFormatting.GREEN);
               } else {
                  long absEUt = Math.abs(EUt);
                  byte tier = GTUtil.getTierByVoltage(absEUt);
                  text = Component.m_237113_(NumberUtils.formatLong(absEUt))
                     .m_130940_(ChatFormatting.RED)
                     .m_7220_(
                        Component.m_237113_(" EU/t")
                           .m_130940_(ChatFormatting.RESET)
                           .m_7220_(
                              Component.m_237113_(" (")
                                 .m_130940_(ChatFormatting.GREEN)
                                 .m_7220_(
                                    Component.m_237110_(
                                          "gtceu.top.electricity",
                                          new Object[]{FormattingUtil.formatNumber2Places((float)absEUt / (float)GTValues.V[tier]), GTValues.VNF[tier]}
                                       )
                                       .m_130938_(style -> style.m_131140_(TextUtil.GTL_CORE$VC[tier]))
                                 )
                                 .m_7220_(Component.m_237113_(")").m_130940_(ChatFormatting.GREEN))
                           )
                     );
               }

               if (isInput) {
                  MutableComponent component = isSteam
                     ? Component.m_237115_("gtceu.jade.steam_consumption")
                     : Component.m_237115_("gtceu.top.energy_consumption");
                  tooltip.add(component.m_130946_(" ").m_7220_(text));
               } else {
                  tooltip.add(Component.m_237115_("gtceu.top.energy_production").m_130946_(" ").m_7220_(text));
               }
            }
         }

         String reason = capData.m_128461_("work_reason");
         if (reason.isEmpty()) {
            return;
         }

         Component reasonComponent = Serializer.m_130701_(reason);
         if (reasonComponent == null) {
            return;
         }

         tooltip.add(Component.m_237110_("gtceu.recipe.fail.reason", new Object[]{reasonComponent}).m_130940_(ChatFormatting.RED));
      } else {
         String reason = capData.m_128461_("reason");
         if (reason.isEmpty()) {
            return;
         }

         Component reasonComponent = Serializer.m_130701_(reason);
         if (reasonComponent == null) {
            return;
         }

         tooltip.add(Component.m_237110_("gtceu.recipe.fail.reason", new Object[]{reasonComponent}).m_130940_(ChatFormatting.RED));
      }
   }
}
