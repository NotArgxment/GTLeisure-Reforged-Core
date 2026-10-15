package org.gtlcore.gtlcore.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.hepdd.gtmthings.api.misc.WirelessEnergyManager;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.lowdragmc.lowdraglib.utils.Size;
import java.util.List;
import java.util.UUID;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GeneratorArrayMachine extends WorkableElectricMultiblockMachine implements IMachineLife {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      GeneratorArrayMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   @Persisted
   @DescSynced
   public final NotifiableItemStackHandler machineStorage;
   @Nullable
   private GTRecipeType[] recipeTypeCache;
   @Persisted
   private UUID userid;
   @Persisted
   private boolean isw;
   @Persisted
   private long eut = 0L;

   public GeneratorArrayMachine(IMachineBlockEntity holder, Object... args) {
      super(holder, args);
      this.machineStorage = this.createMachineStorage(args);
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   protected NotifiableItemStackHandler createMachineStorage(Object... args) {
      NotifiableItemStackHandler storage = new NotifiableItemStackHandler(this, 1, IO.NONE, IO.NONE, slots -> new ItemStackTransfer(1) {
         public int getSlotLimit(int slot) {
            return 16;
         }
      });
      storage.setFilter(this::isMachineStack);
      return storage;
   }

   protected boolean isMachineStack(ItemStack itemStack) {
      if (itemStack.m_41720_() instanceof MetaMachineItem metaMachineItem) {
         MachineDefinition definition = metaMachineItem.getDefinition();
         if (definition instanceof MultiblockMachineDefinition) {
            return false;
         }

         GTRecipeType[] recipeTypes = definition.getRecipeTypes();
         if (recipeTypes == null) {
            return false;
         }

         for (GTRecipeType type : recipeTypes) {
            if (type == GTRecipeTypes.STEAM_TURBINE_FUELS
               || type == GTRecipeTypes.GAS_TURBINE_FUELS
               || type == GTRecipeTypes.COMBUSTION_GENERATOR_FUELS
               || type == GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS
               || type == GTLRecipeTypes.ROCKET_ENGINE_FUELS
               || type == GTLRecipeTypes.NAQUADAH_REACTOR) {
               return true;
            }
         }
      }

      return false;
   }

   @Nullable
   public MachineDefinition getMachineDefinition() {
      return this.machineStorage.storage.getStackInSlot(0).m_41720_() instanceof MetaMachineItem metaMachineItem ? metaMachineItem.getDefinition() : null;
   }

   @NotNull
   public GTRecipeType[] getRecipeTypes() {
      if (this.recipeTypeCache == null) {
         MachineDefinition definition = this.getMachineDefinition();
         this.recipeTypeCache = definition == null ? null : definition.getRecipeTypes();
      }

      if (this.recipeTypeCache == null) {
         this.recipeTypeCache = new GTRecipeType[]{GTRecipeTypes.DUMMY_RECIPES};
      }

      return this.recipeTypeCache;
   }

   @NotNull
   public GTRecipeType getRecipeType() {
      return this.getRecipeTypes()[this.getActiveRecipeType()];
   }

   public void onLoad() {
      super.onLoad();
      if (!this.isRemote()) {
         this.machineStorage.addChangedListener(this::onMachineChanged);
      }
   }

   protected void onMachineChanged() {
      this.recipeTypeCache = null;
      if (this.isFormed) {
         if (this.getRecipeLogic().getLastRecipe() != null) {
            this.getRecipeLogic().markLastRecipeDirty();
         }

         this.getRecipeLogic().updateTickSubscription();
      }
   }

   public void onMachineRemoved() {
      this.clearInventory(this.machineStorage.storage);
   }

   public int getTier() {
      MachineDefinition machineDefinition = this.getMachineDefinition();
      return machineDefinition == null ? 0 : machineDefinition.getTier();
   }

   public int getOverclockTier() {
      MachineDefinition machineDefinition = this.getMachineDefinition();
      return machineDefinition == null ? this.getDefinition().getTier() : machineDefinition.getTier();
   }

   public int getMinOverclockTier() {
      return this.getOverclockTier();
   }

   public int getMaxOverclockTier() {
      return this.getOverclockTier();
   }

   public boolean shouldOpenUI(Player player, InteractionHand hand, BlockHitResult hit) {
      if (this.userid == null || !this.userid.equals(player.m_20148_())) {
         this.userid = player.m_20148_();
      }

      return true;
   }

   public boolean onWorking() {
      boolean value = super.onWorking();
      if (this.isw) {
         if (this.eut <= 0L) {
            return false;
         }

         WirelessEnergyManager.addEUToGlobalEnergyMap(this.userid, this.eut, this);
      }

      return value;
   }

   public void afterWorking() {
      this.eut = 0L;
      super.afterWorking();
   }

   public boolean dampingWhenWaiting() {
      return false;
   }

   public static int getAmperage(int tier) {
      return tier > 3 ? 16 - 2 * tier : 10 - 2 * tier;
   }

   public static int getEfficiency(GTRecipeType recipeType, int tier) {
      if (recipeType == GTRecipeTypes.STEAM_TURBINE_FUELS) {
         return 125 - 25 * tier;
      } else if (recipeType == GTLRecipeTypes.ROCKET_ENGINE_FUELS) {
         return 80 - 15 * (tier - 4);
      } else {
         return recipeType == GTLRecipeTypes.NAQUADAH_REACTOR ? 100 + 50 * (tier - 5) : 105 - 5 * tier;
      }
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof GeneratorArrayMachine generatorArrayMachine) {
         int a = generatorArrayMachine.machineStorage.storage.getStackInSlot(0).m_41613_();
         if (a > 0) {
            long EUt = RecipeHelper.getOutputEUt(recipe);
            if (EUt > 0L) {
               int maxParallel = (int)(GTValues.V[generatorArrayMachine.getOverclockTier()] * a * 2L * getAmperage(generatorArrayMachine.getTier()) / EUt);
               int multipliers = 0;

               for (RecipeCapability<?> cap : recipe.inputs.keySet()) {
                  if (cap instanceof FluidRecipeCapability fluidRecipeCapability) {
                     multipliers += fluidRecipeCapability.getMaxParallelRatio(generatorArrayMachine, recipe, maxParallel);
                  }
               }

               GTRecipe paraRecipe = recipe.copy(ContentModifier.multiplier(multipliers), false);
               paraRecipe.duration = paraRecipe.duration * getEfficiency(generatorArrayMachine.getRecipeType(), generatorArrayMachine.getTier()) / 100;
               if (generatorArrayMachine.isw) {
                  generatorArrayMachine.eut = RecipeHelper.getOutputEUt(paraRecipe);
                  paraRecipe.tickOutputs.remove(EURecipeCapability.CAP);
               }

               return paraRecipe;
            }
         }
      }

      return null;
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed) {
         textList.add(
            Component.m_237115_("gtceu.machine.generator_array.wireless")
               .m_7220_(
                  ComponentPanelWidget.withButton(
                     Component.m_237113_("[")
                        .m_7220_(this.isw ? Component.m_237115_("gtceu.machine.on") : Component.m_237115_("gtceu.machine.off"))
                        .m_7220_(Component.m_237113_("]")),
                     "wireless_switch"
                  )
               )
         );
         if (this.isActive() && this.isw) {
            GTRecipe r = this.getRecipeLogic().getLastRecipe();
            if (r != null) {
               textList.add(Component.m_237110_("gtceu.recipe.eu_inverted", new Object[]{FormattingUtil.formatNumbers(this.eut)}));
            }
         }
      }
   }

   public void handleDisplayClick(String componentData, ClickData clickData) {
      if (!clickData.isRemote && componentData.equals("wireless_switch")) {
         this.isw = !this.isw;
      }
   }

   public Widget createUIWidget() {
      Widget widget = super.createUIWidget();
      if (widget instanceof WidgetGroup group) {
         Size size = group.getSize();
         group.addWidget(
            new SlotWidget(this.machineStorage.storage, 0, size.width - 30, size.height - 30, true, true).setBackground(new IGuiTexture[]{GuiTextures.SLOT})
         );
      }

      return widget;
   }
}
