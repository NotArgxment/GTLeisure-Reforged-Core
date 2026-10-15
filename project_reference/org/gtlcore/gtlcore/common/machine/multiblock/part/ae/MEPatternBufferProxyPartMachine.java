package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IInteractedMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.IntConsumer;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.api.machine.trait.IMERecipeHandlerTrait;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEPatternTrait;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MEPatternBufferProxyPartMachine extends MultiblockPartMachine implements IMachineLife, IMEPatternPartMachine, IInteractedMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MEPatternBufferProxyPartMachine.class, MultiblockPartMachine.MANAGED_FIELD_HOLDER
   );
   protected final ISubscription[] handlerSubscriptions = new ISubscription[2];
   protected final IMEPatternTrait meTrait;
   protected final MEPatternBufferProxyRecipeHandler<Ingredient, ItemStack> itemProxyHandler;
   protected final MEPatternBufferProxyRecipeHandler<FluidIngredient, FluidStack> fluidProxyHandler;
   protected IntConsumer removeSlotFromMap = i -> {};
   @Persisted
   @DescSynced
   private BlockPos bufferPos;
   @Nullable
   private MEPatternBufferPartMachine buffer = null;
   @Nullable
   private IMEPatternTrait bufferTrait = null;
   private boolean bufferResolved = false;

   public MEPatternBufferProxyPartMachine(IMachineBlockEntity holder) {
      super(holder);
      this.itemProxyHandler = new MEPatternBufferProxyRecipeHandler<>(this, ItemRecipeCapability.CAP);
      this.fluidProxyHandler = new MEPatternBufferProxyRecipeHandler<>(this, FluidRecipeCapability.CAP);
      this.meTrait = new MEPatternBufferProxyPartMachine.MEPatternProxyTrait(this);
   }

   public MetaMachine self() {
      MEPatternBufferPartMachine buffer = this.getBuffer();
      return buffer != null ? buffer.self() : super.self();
   }

   public void setBuffer(@Nullable BlockPos pos) {
      this.bufferResolved = true;
      Level level = this.getLevel();
      this.releaseBuffer();
      if (level != null && pos != null && MetaMachine.getMachine(level, pos) instanceof MEPatternBufferPartMachine machine) {
         this.bufferPos = pos;
         this.buffer = machine;
         machine.addProxy(this);
         if (!this.isRemote()) {
            Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> pair = machine.getMERecipeHandlerTraits();
            this.itemProxyHandler.setHandler((IMERecipeHandlerTrait)pair.left());
            this.fluidProxyHandler.setHandler((IMERecipeHandlerTrait)pair.right());
            this.bufferTrait = machine.getMETrait();
            this.handlerSubscriptions[0] = ((IMERecipeHandlerTrait)pair.left()).addChangedListener(this.itemProxyHandler::notifyListeners);
            this.handlerSubscriptions[1] = ((IMERecipeHandlerTrait)pair.right()).addChangedListener(this.fluidProxyHandler::notifyListeners);
         }
      }

      if (!this.isRemote()) {
         this.updateIO();
      }
   }

   @Nullable
   public MEPatternBufferPartMachine getBuffer() {
      if (!this.bufferResolved) {
         this.setBuffer(this.bufferPos);
      }

      return this.buffer;
   }

   protected void releaseBuffer() {
      this.buffer = null;
      this.bufferPos = null;
      if (!this.isRemote()) {
         this.itemProxyHandler.setHandler(null);
         this.fluidProxyHandler.setHandler(null);
         this.bufferTrait = null;

         for (int i = 0; i < this.handlerSubscriptions.length; i++) {
            if (this.handlerSubscriptions[i] != null) {
               this.handlerSubscriptions[i].unsubscribe();
               this.handlerSubscriptions[i] = null;
            }
         }
      }
   }

   protected void updateIO() {
      for (IMultiController controller : this.getControllers()) {
         if (controller instanceof IRecipeCapabilityMachine machine) {
            machine.upDate();
         }
      }

      this.itemProxyHandler.notifyListeners();
      this.fluidProxyHandler.notifyListeners();
   }

   public InteractionResult onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack stack = player.m_21120_(hand);
      if (stack.m_41619_()) {
         return InteractionResult.PASS;
      }

      if (stack.m_150930_(GTItems.TOOL_DATA_STICK.m_5456_())) {
         if (!world.f_46443_) {
            com.mojang.datafixers.util.Pair<GTRecipeType, String> researchData = ResearchManager.readResearchId(stack);
            if (researchData != null) {
               return InteractionResult.PASS;
            }

            CompoundTag tag = stack.m_41783_();
            if (tag != null && tag.m_128441_("pos")) {
               int[] posArray = tag.m_128465_("pos");
               if (posArray.length == 3) {
                  BlockPos bufferPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
                  player.m_213846_(Component.m_237115_("gtceu.machine.me.import_paste_settings"));
                  this.setBuffer(bufferPos);
               }
            }
         }

         return InteractionResult.m_19078_(world.f_46443_);
      } else {
         return InteractionResult.PASS;
      }
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      return this.getBuffer() != null;
   }

   public ModularUI createUI(Player entityPlayer) {
      assert this.getBuffer() != null;
      return this.getBuffer().createUI(entityPlayer);
   }

   public void onLoad() {
      super.onLoad();
      if (this.getLevel() instanceof ServerLevel level) {
         level.m_7654_().m_6937_(new TickTask(0, () -> this.setBuffer(this.bufferPos)));
      }
   }

   public void onMachineRemoved() {
      MEPatternBufferPartMachine buf = this.getBuffer();
      if (buf != null) {
         buf.removeProxy(this);
      }

      for (int i = 0; i < this.handlerSubscriptions.length; i++) {
         if (this.handlerSubscriptions[i] != null) {
            this.handlerSubscriptions[i].unsubscribe();
            this.handlerSubscriptions[i] = null;
         }
      }
   }

   @NotNull
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   @Override
   public Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> getMERecipeHandlerTraits() {
      return Pair.of(this.itemProxyHandler, this.fluidProxyHandler);
   }

   @NotNull
   @Override
   public IMEPatternTrait getMETrait() {
      return this.meTrait;
   }

   public BlockPos getBufferPos() {
      return this.bufferPos;
   }

   protected class MEPatternProxyTrait extends MachineTrait implements IMEPatternTrait {
      protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MEPatternBufferProxyPartMachine.class);

      public MEPatternProxyTrait(MEPatternBufferProxyPartMachine machine) {
         super(machine);
      }

      public MEPatternBufferProxyPartMachine getMachine() {
         return (MEPatternBufferProxyPartMachine)this.machine;
      }

      @NotNull
      @Override
      public ObjectSet<GTRecipe> getCachedGTRecipe() {
         return MEPatternBufferProxyPartMachine.this.bufferTrait == null
            ? ObjectSets.emptySet()
            : MEPatternBufferProxyPartMachine.this.bufferTrait.getCachedGTRecipe();
      }

      @Override
      public void setSlotCacheRecipe(int index, GTRecipe recipe) {
         if (MEPatternBufferProxyPartMachine.this.bufferTrait != null) {
            MEPatternBufferProxyPartMachine.this.bufferTrait.setSlotCacheRecipe(index, recipe);
         }
      }

      @NotNull
      @Override
      public Int2ReferenceMap<ObjectSet<GTRecipe>> getSlot2RecipesCache() {
         return MEPatternBufferProxyPartMachine.this.bufferTrait == null
            ? Int2ReferenceMaps.emptyMap()
            : MEPatternBufferProxyPartMachine.this.bufferTrait.getSlot2RecipesCache();
      }

      @Override
      public void setOnPatternChange(IntConsumer removeMapOnSlot) {
         MEPatternBufferProxyPartMachine.this.removeSlotFromMap = removeMapOnSlot;
      }

      @Override
      public boolean hasCacheInSlot(int slot) {
         return MEPatternBufferProxyPartMachine.this.bufferTrait == null ? false : MEPatternBufferProxyPartMachine.this.bufferTrait.hasCacheInSlot(slot);
      }

      @Override
      public void notifySelfIO() {
         if (MEPatternBufferProxyPartMachine.this.bufferTrait != null) {
            MEPatternBufferProxyPartMachine.this.bufferTrait.notifySelfIO();
         }
      }

      @Override
      public IO getIO() {
         return MEPatternBufferProxyPartMachine.this.bufferTrait != null ? MEPatternBufferProxyPartMachine.this.bufferTrait.getIO() : IO.NONE;
      }

      public ManagedFieldHolder getFieldHolder() {
         return MANAGED_FIELD_HOLDER;
      }
   }
}
