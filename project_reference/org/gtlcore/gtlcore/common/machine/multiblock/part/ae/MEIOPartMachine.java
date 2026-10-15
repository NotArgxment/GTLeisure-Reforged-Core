package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.IGridNodeListener.State;
import appeng.api.networking.security.IActionSource;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider.PageGroupingData;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.trait.GridNodeHolder;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.EnumSet;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEIOPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEIOTrait;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MEIOPartMachine extends MultiblockPartMachine implements IMachineLife, IGridConnectedMachine, IMEIOPartMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEIOPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER);
   @Persisted
   protected final GridNodeHolder nodeHolder;
   @DescSynced
   protected boolean isOnline;
   protected final IMEIOTrait meTrait;
   protected final IActionSource actionSource;
   protected final IO io;
   protected boolean isSleeping = true;

   public MEIOPartMachine(IMachineBlockEntity holder, IO io) {
      super(holder);
      this.nodeHolder = this.createNodeHolder();
      this.actionSource = IActionSource.ofMachine(this.nodeHolder.getMainNode()::getNode);
      this.io = io;
      this.meTrait = this.createMETrait();
   }

   @NotNull
   protected IMEIOTrait createMETrait() {
      return new MEIOPartMachine.MEIOTrait(this);
   }

   @NotNull
   @Override
   public IMEIOTrait getMETrait() {
      return this.meTrait;
   }

   @Nullable
   public PageGroupingData getPageGroupingData() {
      return switch (this.io) {
         case IN -> new PageGroupingData("gtceu.multiblock.page_switcher.io.import", 1);
         case OUT -> new PageGroupingData("gtceu.multiblock.page_switcher.io.export", 2);
         case BOTH -> new PageGroupingData("gtceu.multiblock.page_switcher.io.both", 3);
         case NONE -> null;
         default -> throw new IncompatibleClassChangeError();
      };
   }

   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      if (tag.m_128469_("ForgeData").m_128471_("isAllFacing")) {
         this.getMainNode().setExposedOnSides(EnumSet.allOf(Direction.class));
      }
   }

   public IManagedGridNode getMainNode() {
      return this.nodeHolder.getMainNode();
   }

   public void onRotated(@NotNull Direction oldFacing, @NotNull Direction newFacing) {
      super.onRotated(oldFacing, newFacing);
      this.getMainNode().setExposedOnSides(EnumSet.of(newFacing));
   }

   public void onMainNodeStateChanged(@NotNull State reason) {
      super.onMainNodeStateChanged(reason);
      if (this.isOnline()) {
         this.meTrait.notifySelfIO();
      }
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   protected GridNodeHolder createNodeHolder() {
      return new GridNodeHolder(this);
   }

   public GridNodeHolder getNodeHolder() {
      return this.nodeHolder;
   }

   public boolean isOnline() {
      return this.isOnline;
   }

   public void setOnline(boolean isOnline) {
      this.isOnline = isOnline;
   }

   public class MEIOTrait extends MachineTrait implements IMEIOTrait {
      protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEIOPartMachine.class);

      public MEIOTrait(MEIOPartMachine machine) {
         super(machine);
      }

      public MEIOPartMachine getMachine() {
         return (MEIOPartMachine)this.machine;
      }

      @Override
      public void notifySelfIO() {
         if (MEIOPartMachine.this.isSleeping && MEIOPartMachine.this.getMainNode().isActive()) {
            MEIOPartMachine.this.getMainNode().ifPresent((grid, node) -> {
               grid.getTickManager().wakeDevice(node);
               MEIOPartMachine.this.isSleeping = false;
            });
         }
      }

      @Override
      public IO getIO() {
         return MEIOPartMachine.this.io;
      }

      public ManagedFieldHolder getFieldHolder() {
         return MANAGED_FIELD_HOLDER;
      }
   }
}
