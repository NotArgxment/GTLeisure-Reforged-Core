package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.hepdd.gtmthings.utils.TeamUtil;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.List;
import java.util.UUID;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.multiblock.NoEnergyMultiblockMachine;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HarmonyMachine extends NoEnergyMultiblockMachine implements IMachineLife {
   public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(HarmonyMachine.class, NoEnergyMultiblockMachine.MANAGED_FIELD_HOLDER);
   @Persisted
   private int oc = 0;
   @Persisted
   private long hydrogen = 0L;
   @Persisted
   private long helium = 0L;
   @Persisted
   private UUID userid;
   protected ConditionalSubscriptionHandler StartupSubs = new ConditionalSubscriptionHandler(this, this::StartupUpdate, this::isFormed);

   public HarmonyMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
   }

   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   protected void StartupUpdate() {
      if (this.getOffsetTimer() % 20L == 0L) {
         this.oc = 0;
         if (MachineIO.inputFluid(this, GTMaterials.Hydrogen.getFluid(100000000L))) {
            this.hydrogen += 100000000L;
         }

         if (MachineIO.inputFluid(this, GTMaterials.Helium.getFluid(100000000L))) {
            this.helium += 100000000L;
         }

         if (MachineIO.notConsumableCircuit(this, 4)) {
            this.oc = 4;
         }

         if (MachineIO.notConsumableCircuit(this, 3)) {
            this.oc = 3;
         }

         if (MachineIO.notConsumableCircuit(this, 2)) {
            this.oc = 2;
         }

         if (MachineIO.notConsumableCircuit(this, 1)) {
            this.oc = 1;
         }
      }
   }

   private long getStartupEnergy() {
      return this.oc == 0 ? 0L : (long)(5.2776558108672E15 * Math.pow(8.0, this.oc - 1));
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      this.StartupSubs.initialize(this.getLevel());
   }

   public void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
      if (player != null) {
         this.userid = player.m_20148_();
      }
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof HarmonyMachine harmonyMachine
         && harmonyMachine.userid != null
         && harmonyMachine.hydrogen >= 1024000000L
         && harmonyMachine.helium >= 1024000000L
         && harmonyMachine.oc > 0) {
         harmonyMachine.hydrogen -= 1024000000L;
         harmonyMachine.helium -= 1024000000L;
         if (WirelessEnergyManager.addEUToGlobalEnergyMap(harmonyMachine.userid, -harmonyMachine.getStartupEnergy(), machine)) {
            GTRecipe recipe1 = recipe.copy();
            recipe1.duration = (int)(4800.0 / Math.pow(2.0, harmonyMachine.oc));
            return recipe1;
         }
      }

      return null;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      if (this.userid == null || !this.userid.equals(player.m_20148_())) {
         this.userid = player.m_20148_();
      }

      return true;
   }

   public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      if (player.m_21120_(hand).m_150930_(GTItems.TOOL_DATA_STICK.m_5456_())) {
         this.userid = player.m_20148_();
         if (this.isRemote()) {
            player.m_213846_(Component.m_237110_("gtmthings.machine.wireless_energy_hatch.tooltip.bind", new Object[]{TeamUtil.GetName(player)}));
         }

         return InteractionResult.m_19078_(this.isRemote());
      } else {
         return super.onUse(state, world, pos, player, hand, hit);
      }
   }

   @Override
   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         if (this.userid != null) {
            textList.add(
               Component.m_237110_("gtmthings.machine.wireless_energy_monitor.tooltip.0", new Object[]{TeamUtil.GetName(this.getLevel(), this.userid)})
            );
            textList.add(
               Component.m_237110_(
                  "gtmthings.machine.wireless_energy_monitor.tooltip.1",
                  new Object[]{FormattingUtil.formatNumbers(WirelessEnergyManager.getUserEU(this.userid))}
               )
            );
         }

         textList.add(Component.m_237110_("tooltip.gtlcore.startup_energy_cost", new Object[]{FormattingUtil.formatNumbers(this.getStartupEnergy())}));
         textList.add(Component.m_237110_("tooltip.gtlcore.hydrogen_storage", new Object[]{FormattingUtil.formatNumbers(this.hydrogen)}));
         textList.add(Component.m_237110_("tooltip.gtlcore.helium_storage", new Object[]{FormattingUtil.formatNumbers(this.helium)}));
      }
   }
}
