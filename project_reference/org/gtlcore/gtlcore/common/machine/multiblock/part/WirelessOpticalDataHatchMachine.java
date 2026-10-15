package org.gtlcore.gtlcore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.capability.IDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.IOpticalDataAccessHatch;
import com.gregtechceu.gtceu.api.capability.IWorkable;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.capability.BindCapability;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class WirelessOpticalDataHatchMachine extends MultiblockPartMachine implements IOpticalDataAccessHatch, IInteractedMachine, BindCapability {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      WirelessOpticalDataHatchMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   private final boolean transmitter;
   @Persisted
   private BlockPos transmitterPos;
   @Persisted
   private BlockPos receiverPos;

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public WirelessOpticalDataHatchMachine(IMachineBlockEntity holder, boolean transmitter) {
      super(holder);
      this.transmitter = transmitter;
   }

   public boolean isTransmitter() {
      return this.transmitter;
   }

   public boolean isRecipeAvailable(GTRecipe recipe, Collection<IDataAccessHatch> seen) {
      seen.add(this);
      if (!this.getControllers().isEmpty()) {
         if (this.isTransmitter()) {
            IMultiController controller = (IMultiController)this.getControllers().get(0);
            if (controller instanceof IWorkable workable && workable.isActive()) {
               List<IDataAccessHatch> dataAccesses = new ArrayList<>();
               List<IDataAccessHatch> transmitters = new ArrayList<>();

               for (IMultiPart part : controller.getParts()) {
                  Block block = part.self().getBlockState().m_60734_();
                  if (part instanceof IDataAccessHatch hatch && PartAbility.DATA_ACCESS.isApplicable(block)) {
                     dataAccesses.add(hatch);
                  }

                  if (part instanceof IDataAccessHatch hatch && PartAbility.OPTICAL_DATA_RECEPTION.isApplicable(block)) {
                     transmitters.add(hatch);
                  }
               }

               return isRecipeAvailable(dataAccesses, seen, recipe) || isRecipeAvailable(transmitters, seen, recipe);
            }

            return false;
         }

         Level level = this.getLevel();
         if (level == null || this.transmitterPos == null) {
            RecipeResult.of(
               (IRecipeLogicMachine)this.getControllers().get(0), RecipeResult.fail(Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.unbind"))
            );
            return false;
         }

         if (MetaMachine.getMachine(level, this.transmitterPos) instanceof WirelessOpticalDataHatchMachine machine) {
            if (machine.isRecipeAvailable(recipe, seen)) {
               return true;
            }

            RecipeResult.of((IRecipeLogicMachine)this.getControllers().get(0), RecipeResult.FAIL_NO_FIND_RESEARCHED);
         }
      }

      return false;
   }

   private static boolean isRecipeAvailable(
      @NotNull Iterable<? extends IDataAccessHatch> hatches, @NotNull Collection<IDataAccessHatch> seen, @NotNull GTRecipe recipe
   ) {
      for (IDataAccessHatch hatch : hatches) {
         if (hatch.isCreative()) {
            return true;
         }

         if (!seen.contains(hatch) && hatch.isRecipeAvailable(recipe, seen)) {
            return true;
         }
      }

      return false;
   }

   private CompoundTag createPos(BlockPos pos) {
      CompoundTag posTag = new CompoundTag();
      posTag.m_128405_("x", pos.m_123341_());
      posTag.m_128405_("y", pos.m_123342_());
      posTag.m_128405_("z", pos.m_123343_());
      return posTag;
   }

   private BlockPos getPos(CompoundTag tag) {
      return new BlockPos(tag.m_128451_("x"), tag.m_128451_("y"), tag.m_128451_("z"));
   }

   public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack is = player.m_21120_(hand);
      if (is.m_41619_()) {
         return InteractionResult.PASS;
      }

      if (is.m_150930_(GTItems.TOOL_DATA_STICK.m_5456_())) {
         Level level = this.getLevel();
         if (level == null) {
            return InteractionResult.PASS;
         }

         if (this.transmitter) {
            CompoundTag tag = is.m_41783_();
            if (this.transmitterPos == null) {
               this.transmitterPos = pos;
            }

            if (tag != null) {
               tag.m_128365_("transmitterPos", this.createPos(pos));
               CompoundTag bindPos = (CompoundTag)tag.m_128423_("receiverPos");
               if (bindPos != null) {
                  BlockPos recPos = this.getPos(bindPos);
                  if (MetaMachine.getMachine(level, recPos) instanceof WirelessOpticalDataHatchMachine wod && !wod.transmitter) {
                     wod.setTransmitterPos(this.transmitterPos);
                     this.receiverPos = pos;
                     tag.m_128473_("transmitterPos");
                     tag.m_128473_("receiverPos");
                     if (level.m_5776_()) {
                        player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_hatch.bind"));
                     }
                  }
               } else if (level.m_5776_()) {
                  player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_transmitter_hatch.to_bind"));
               }

               is.m_41751_(tag);
            } else {
               tag = new CompoundTag();
               tag.m_128365_("transmitterPos", this.createPos(this.transmitterPos));
               is.m_41751_(tag);
               if (level.m_5776_()) {
                  player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_transmitter_hatch.to_bind"));
               }
            }

            return InteractionResult.SUCCESS;
         } else {
            if (this.receiverPos == null) {
               this.receiverPos = pos;
            }

            CompoundTag tag = is.m_41783_();
            if (tag != null) {
               tag.m_128365_("receiverPos", this.createPos(pos));
               CompoundTag bindPos = (CompoundTag)tag.m_128423_("transmitterPos");
               if (bindPos != null) {
                  BlockPos tranPos = new BlockPos(bindPos.m_128451_("x"), bindPos.m_128451_("y"), bindPos.m_128451_("z"));
                  if (MetaMachine.getMachine(level, tranPos) instanceof WirelessOpticalDataHatchMachine wod && wod.transmitter) {
                     wod.setReceiverPos(this.receiverPos);
                     this.transmitterPos = tranPos;
                     tag.m_128473_("transmitterPos");
                     tag.m_128473_("receiverPos");
                     if (level.m_5776_()) {
                        player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_hatch.bind"));
                     }
                  }
               } else if (level.m_5776_()) {
                  player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.to_bind"));
               }

               is.m_41751_(tag);
            } else {
               tag = new CompoundTag();
               tag.m_128365_("receiverPos", this.createPos(this.receiverPos));
               is.m_41751_(tag);
               if (level.m_5776_()) {
                  player.m_213846_(Component.m_237115_("gtceu.machine.wireless_data_receiver_hatch.to_bind"));
               }
            }

            return InteractionResult.SUCCESS;
         }
      } else {
         return InteractionResult.PASS;
      }
   }

   public boolean isCreative() {
      return false;
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return false;
   }

   public boolean canShared() {
      return false;
   }

   public GTRecipe modifyRecipe(GTRecipe recipe) {
      return super.modifyRecipe(recipe);
   }

   @Override
   public boolean bind() {
      return this.transmitterPos != null || this.receiverPos != null;
   }

   @Override
   public String pos() {
      if (this.isTransmitter() && this.receiverPos != null) {
         return this.receiverPos.m_123344_();
      } else {
         return !this.isTransmitter() && this.transmitterPos != null ? this.transmitterPos.m_123344_() : "";
      }
   }

   public BlockPos getTransmitterPos() {
      return this.transmitterPos;
   }

   public void setTransmitterPos(BlockPos transmitterPos) {
      this.transmitterPos = transmitterPos;
   }

   public BlockPos getReceiverPos() {
      return this.receiverPos;
   }

   public void setReceiverPos(BlockPos receiverPos) {
      this.receiverPos = receiverPos;
   }
}
