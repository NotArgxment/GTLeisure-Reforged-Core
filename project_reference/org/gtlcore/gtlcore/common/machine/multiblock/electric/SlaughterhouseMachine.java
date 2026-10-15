package org.gtlcore.gtlcore.common.machine.multiblock.electric;

import appeng.util.Platform;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.MachineIO;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SlaughterhouseMachine extends WorkableElectricMultiblockMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      SlaughterhouseMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   private boolean isSpawn;
   private final String[] mobList1;
   private final String[] mobList2;

   public SlaughterhouseMachine(IMachineBlockEntity holder) {
      super(holder, new Object[0]);
      this.mobList1 = ConfigHolder.INSTANCE.mobList1;
      this.mobList2 = ConfigHolder.INSTANCE.mobList2;
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   private void getItem(ServerLevel level, BlockPos Pos) {
      Player fp = Platform.getFakePlayer(level, null);
      DamageSource Source = new DamageSource(level.m_9598_().m_175515_(Registries.f_268580_).m_246971_(DamageTypes.f_286979_), fp);

      for (Entity en : level.m_45976_(
         Entity.class, new AABB(Pos.m_123341_() - 3, Pos.m_123342_() - 1, Pos.m_123343_() - 3, Pos.m_123341_() + 3, Pos.m_123342_() + 6, Pos.m_123343_() + 3)
      )) {
         if (!(en instanceof Player)) {
            if (en instanceof LivingEntity livingEntity) {
               livingEntity.m_6469_(Source, 10000.0F);
            } else if (en instanceof ItemEntity itemEntity) {
               MachineIO.outputItem(this, itemEntity.m_32055_());
               itemEntity.m_6074_();
            }
         }

         en.m_6074_();
      }
   }

   public void afterWorking() {
      BlockPos pos = this.getPos();
      Level level = this.getLevel();
      BlockPos[][] coordinates = new BlockPos[][]{
         {pos.m_7918_(1, 0, 0), pos.m_7918_(3, 1, 0)},
         {pos.m_7918_(-1, 0, 0), pos.m_7918_(-3, 1, 0)},
         {pos.m_7918_(0, 0, 1), pos.m_7918_(0, 1, 3)},
         {pos.m_7918_(0, 0, -1), pos.m_7918_(0, 1, -3)}
      };

      for (BlockPos[] blockPos : coordinates) {
         if (level instanceof ServerLevel serverLevel
            && Objects.equals(org.gtlcore.gtlcore.utils.Registries.getBlockId(level.m_8055_(blockPos[0]).m_60734_()), "gtceu:steel_gearbox")) {
            BlockPos mobPos = blockPos[1];
            String[] mobList = MachineIO.notConsumableCircuit(this, 1) ? this.mobList1 : this.mobList2;
            if (!this.isSpawn) {
               this.getItem(serverLevel, mobPos);

               for (int am = 0; am <= (this.getTier() - 2) * 8; am++) {
                  int index = (int)(Math.random() * mobList.length);
                  ((EntityType)EntityType.m_20632_("minecraft:" + mobList[index]).get())
                     .m_262496_(serverLevel, mobPos, MobSpawnType.MOB_SUMMONED)
                     .m_146922_(serverLevel.m_213780_().m_188501_() * 360.0F);
               }
            } else {
               LootParams lootparams = new Builder(serverLevel).m_287235_(LootContextParamSets.f_81410_);
               this.getItem(serverLevel, mobPos);

               for (int am = 0; am <= (this.getTier() - 2) * 8; am++) {
                  int index = (int)(Math.random() * mobList.length);
                  ObjectArrayList<ItemStack> loottable = serverLevel.m_7654_()
                     .m_278653_()
                     .m_278676_(new ResourceLocation("minecraft:entities/" + mobList[index]))
                     .m_287195_(lootparams);
                  loottable.forEach(i -> MachineIO.outputItem(this, i));
               }
            }
         }
      }
   }

   public void addDisplayText(@NotNull List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(
            Component.m_237115_("gtceu.machine.slaughterhouse.is_spawn")
               .m_7220_(
                  ComponentPanelWidget.withButton(
                     Component.m_237113_("[")
                        .m_7220_(this.isSpawn ? Component.m_237115_("gtceu.machine.off") : Component.m_237115_("gtceu.machine.on"))
                        .m_7220_(Component.m_237113_("]")),
                     "spawn_switch"
                  )
               )
         );
      }
   }

   public void handleDisplayClick(String componentData, ClickData clickData) {
      if (!clickData.isRemote && componentData.equals("spawn_switch")) {
         this.isSpawn = !this.isSpawn;
      }
   }
}
