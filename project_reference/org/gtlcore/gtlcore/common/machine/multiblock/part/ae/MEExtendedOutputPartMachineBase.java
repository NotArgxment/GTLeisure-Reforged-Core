package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.stacks.AEKey;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IDataStickInteractable;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Position;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.machine.trait.IMERecipeHandlerTrait;
import org.gtlcore.gtlcore.api.machine.trait.IRecipeCapabilityMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOPartMachine;
import org.gtlcore.gtlcore.api.machine.trait.MEPart.IMEFilterIOTrait;
import org.gtlcore.gtlcore.client.gui.widget.MEOutListGridWidget;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.integration.ae2.AEUtils;
import org.jetbrains.annotations.NotNull;

public abstract class MEExtendedOutputPartMachineBase extends MEIOPartMachine implements IDataStickInteractable, IMEFilterIOPartMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MEExtendedOutputPartMachineBase.class, MEIOPartMachine.MANAGED_FIELD_HOLDER
   );
   protected static final int MIN_FREQUENCY = ConfigHolder.INSTANCE.MEPatternOutputMin;
   protected static final int MAX_FREQUENCY = ConfigHolder.INSTANCE.MEPatternOutputMax;
   protected final Object2LongOpenHashMap<AEKey> buffer = new Object2LongOpenHashMap();
   protected final NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> itemOutputHandler;
   protected final NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> fluidOutputHandler;
   @Persisted
   protected int priority = 10;

   @NotNull
   @Override
   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public MEExtendedOutputPartMachineBase(IMachineBlockEntity holder) {
      super(holder, IO.OUT);
      this.itemOutputHandler = this.createItemOutputHandler();
      this.fluidOutputHandler = this.createFluidOutputHandler();
      this.registerDefaultServices();
   }

   @NotNull
   public Widget createUIWidget() {
      WidgetGroup group = new WidgetGroup(new Position(0, 0));
      group.addWidget(new LabelWidget(0, 0, () -> this.isOnline ? "gtceu.gui.me_network.online" : "gtceu.gui.me_network.offline"));
      group.addWidget(new MEOutListGridWidget(5, 20, 7, this.buffer));
      return group;
   }

   @Override
   public Pair<IMERecipeHandlerTrait<Ingredient, ItemStack>, IMERecipeHandlerTrait<FluidIngredient, FluidStack>> getMERecipeHandlerTraits() {
      return Pair.of(this.itemOutputHandler, this.fluidOutputHandler);
   }

   @NotNull
   @Override
   public abstract IMEFilterIOTrait getMETrait();

   protected abstract NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> createItemOutputHandler();

   protected abstract NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> createFluidOutputHandler();

   protected void registerDefaultServices() {
   }

   protected void notifyHandlers() {
      this.itemOutputHandler.notifyListeners();
      this.fluidOutputHandler.notifyListeners();
   }

   protected void updatePriority() {
      for (IMultiController controller : this.getControllers()) {
         if (controller instanceof IRecipeCapabilityMachine machine) {
            machine.sortMEOutput();
         }
      }
   }

   public InteractionResult onDataStickRightClick(Player player, ItemStack dataStick) {
      CompoundTag tag = dataStick.m_41783_();
      if (tag != null && tag.m_128441_("MEExtendedExportBuffer")) {
         if (!this.isRemote()) {
            this.readConfigFromTag(tag.m_128469_("MEExtendedExportBuffer"));
            player.m_213846_(Component.m_237115_("gtceu.machine.me.import_paste_settings"));
         }

         return InteractionResult.m_19078_(this.isRemote());
      } else {
         return InteractionResult.PASS;
      }
   }

   public boolean onDataStickLeftClick(Player player, ItemStack dataStick) {
      if (!this.isRemote()) {
         CompoundTag tag = new CompoundTag();
         tag.m_128365_("MEExtendedExportBuffer", this.writeConfigToTag());
         dataStick.m_41751_(tag);
         dataStick.m_41714_(Component.m_237115_("gtceu.machine.me.me_extended_export_buffer.data_stick.name"));
         player.m_213846_(Component.m_237115_("gtceu.machine.me.import_copy_settings"));
      }

      return true;
   }

   protected CompoundTag writeConfigToTag() {
      CompoundTag tag = new CompoundTag();
      tag.m_128405_("priority", this.priority);
      return tag;
   }

   protected void readConfigFromTag(CompoundTag tag) {
      if (tag.m_128441_("priority")) {
         this.priority = tag.m_128451_("priority");
      }

      this.updatePriority();
   }

   public void saveCustomPersistedData(@NotNull CompoundTag tag, boolean forDrop) {
      super.saveCustomPersistedData(tag, forDrop);
      if (!this.buffer.isEmpty()) {
         ListTag listTag = AEUtils.createListTag(AEKey::toTagGeneric, this.buffer);
         if (!listTag.isEmpty()) {
            tag.m_128365_("buffer", listTag);
         }
      }
   }

   @Override
   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      this.buffer.clear();
      ListTag listTag = tag.m_128437_("buffer", 10);
      AEUtils.loadInventory(listTag, AEKey::fromTagGeneric, this.buffer);
   }

   public Object2LongOpenHashMap<AEKey> getBuffer() {
      return this.buffer;
   }

   public NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> getItemOutputHandler() {
      return this.itemOutputHandler;
   }

   public NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> getFluidOutputHandler() {
      return this.fluidOutputHandler;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public abstract class MEFluidOutputHandler extends NotifiableMERecipeHandlerTrait<FluidIngredient, FluidStack> {
      public MEFluidOutputHandler(MEExtendedOutputPartMachineBase machine) {
         super(machine);
      }

      public MEExtendedOutputPartMachineBase getMachine() {
         return (MEExtendedOutputPartMachineBase)this.machine;
      }

      @Override
      public RecipeCapability<FluidIngredient> getCapability() {
         return FluidRecipeCapability.CAP;
      }

      @Override
      public IO getIo() {
         return IO.OUT;
      }

      @Override
      public Set<Integer> getActiveSlots() {
         return Collections.emptySet();
      }

      @Override
      public Int2ObjectMap<List<FluidStack>> getActiveAndUnCachedSlotsLimitContentsMap() {
         return Int2ObjectMaps.emptyMap();
      }

      @Override
      public Object2LongMap<FluidStack> getStackMapFromFirstAvailableSlot(IntCollection slots) {
         return Object2LongMaps.emptyMap();
      }

      @Override
      public boolean meHandleRecipeInner(GTRecipe recipe, Object2LongMap<FluidIngredient> left, boolean simulate, int trySlot) {
         return false;
      }

      @Override
      public void prepareMEHandleContents(GTRecipe recipe, List<FluidIngredient> left, boolean simulate) {
      }

      @Override
      public Object2LongMap<FluidIngredient> getPreparedMEHandleContents() {
         return Object2LongMaps.emptyMap();
      }

      @Override
      public int getPriority() {
         return MEExtendedOutputPartMachineBase.this.priority;
      }
   }

   protected abstract class MEItemOutputHandler extends NotifiableMERecipeHandlerTrait<Ingredient, ItemStack> {
      public MEItemOutputHandler(MEExtendedOutputPartMachineBase machine) {
         super(machine);
      }

      public MEExtendedOutputPartMachineBase getMachine() {
         return (MEExtendedOutputPartMachineBase)this.machine;
      }

      @Override
      public RecipeCapability<Ingredient> getCapability() {
         return ItemRecipeCapability.CAP;
      }

      @Override
      public IO getIo() {
         return IO.OUT;
      }

      @Override
      public Set<Integer> getActiveSlots() {
         return Collections.emptySet();
      }

      @Override
      public Int2ObjectMap<List<ItemStack>> getActiveAndUnCachedSlotsLimitContentsMap() {
         return Int2ObjectMaps.emptyMap();
      }

      @Override
      public Object2LongMap<ItemStack> getStackMapFromFirstAvailableSlot(IntCollection slots) {
         return Object2LongMaps.emptyMap();
      }

      @Override
      public boolean meHandleRecipeInner(GTRecipe recipe, Object2LongMap<Ingredient> left, boolean simulate, int trySlot) {
         return false;
      }

      @Override
      public void prepareMEHandleContents(GTRecipe recipe, List<Ingredient> left, boolean simulate) {
      }

      @Override
      public Object2LongMap<Ingredient> getPreparedMEHandleContents() {
         return Object2LongMaps.emptyMap();
      }

      @Override
      public int getPriority() {
         return MEExtendedOutputPartMachineBase.this.priority;
      }
   }
}
