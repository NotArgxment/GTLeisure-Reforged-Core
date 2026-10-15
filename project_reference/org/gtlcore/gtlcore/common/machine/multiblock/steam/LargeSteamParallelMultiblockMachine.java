package org.gtlcore.gtlcore.common.machine.multiblock.steam;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.UITemplate;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.machine.steam.SteamEnergyRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.ComponentPanelWidget;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import java.util.Iterator;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.HoverEvent.Action;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.gtlcore.gtlcore.api.machine.ISteamMachine;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.jetbrains.annotations.NotNull;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeSteamParallelMultiblockMachine extends WorkableMultiblockMachine implements IDisplayUIMachine, ISteamMachine {
   protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      LargeSteamParallelMultiblockMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   private final int max_parallels;
   @DescSynced
   private boolean isOC;
   @Persisted
   @DescSynced
   private int amountOC;
   private static final double CONVERSION_RATE = 0.5;

   public LargeSteamParallelMultiblockMachine(IMachineBlockEntity holder, int maxParallels, Object... args) {
      super(holder, args);
      this.max_parallels = maxParallels;
   }

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public void onStructureFormed() {
      super.onStructureFormed();
      List<IRecipeHandler<?>> handlers = (List<IRecipeHandler<?>>)this.capabilitiesProxy.get(IO.IN, FluidRecipeCapability.CAP);
      if (handlers != null) {
         Iterator<IRecipeHandler<?>> itr = handlers.iterator();

         while (itr.hasNext()) {
            IRecipeHandler<?> handler = itr.next();
            if (handler instanceof NotifiableFluidTank tank && tank.getFluidInTank(0).isFluidEqual(GTMaterials.Steam.getFluid(1L))) {
               this.isOC = tank.getMachine().getDefinition() == GTLMachines.LARGE_STEAM_HATCH;
               itr.remove();
               this.capabilitiesProxy
                  .put(IO.IN, EURecipeCapability.CAP, List.of(new SteamEnergyRecipeHandler(tank, 0.5 * (this.isOC ? Math.pow(3.0, this.amountOC) : 1.0))));
               return;
            }
         }
      }
   }

   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, double reductionDuration) {
      if (machine instanceof LargeSteamParallelMultiblockMachine steamMachine) {
         if (RecipeHelper.getInputEUt(recipe) > (steamMachine.isOC ? 128 : 32)) {
            return null;
         }

         GTRecipe result = (GTRecipe)GTRecipeModifiers.accurateParallel(machine, recipe, steamMachine.max_parallels, false).getFirst();
         recipe = result == recipe ? result.copy() : result;
         recipe.duration = (int)Math.max(1.0, recipe.duration * reductionDuration / (steamMachine.isOC ? Math.pow(2.0, steamMachine.amountOC) : 1.0));
      }

      return recipe;
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         List<IRecipeHandler<?>> handlers = (List<IRecipeHandler<?>>)this.capabilitiesProxy.get(IO.IN, EURecipeCapability.CAP);
         if (handlers != null && !handlers.isEmpty() && handlers.get(0) instanceof SteamEnergyRecipeHandler steamHandler && steamHandler.getCapacity() > 0L) {
            long steamStored = steamHandler.getStored();
            textList.add(Component.m_237110_("gtceu.multiblock.steam.steam_stored", new Object[]{steamStored, steamHandler.getCapacity()}));
         }

         if (!this.isWorkingEnabled()) {
            textList.add(Component.m_237115_("gtceu.multiblock.work_paused"));
         } else if (this.isActive()) {
            textList.add(Component.m_237115_("gtceu.multiblock.running"));
            int currentProgress = (int)(this.recipeLogic.getProgressPercent() * 100.0);
            textList.add(Component.m_237110_("gtceu.multiblock.parallel", new Object[]{this.max_parallels}));
            textList.add(Component.m_237110_("gtceu.multiblock.progress", new Object[]{currentProgress}));
         } else {
            textList.add(Component.m_237115_("gtceu.multiblock.idling"));
         }

         if (this.recipeLogic.isWaiting()) {
            textList.add(Component.m_237115_("gtceu.multiblock.steam.low_steam").m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED)));
         }

         if (this.isOC) {
            textList.add(
               Component.m_237110_("gtceu.multiblock.oc_amount", new Object[]{this.amountOC})
                  .m_130948_(Style.f_131099_.m_131144_(new HoverEvent(Action.f_130831_, Component.m_237115_("gtceu.multiblock.steam_parallel_machine.oc"))))
            );
            textList.add(
               Component.m_237115_("gtceu.multiblock.steam_parallel_machine.modification_oc")
                  .m_7220_(ComponentPanelWidget.withButton(Component.m_237113_("[-] "), "ocSub"))
                  .m_7220_(ComponentPanelWidget.withButton(Component.m_237113_("[+]"), "ocAdd"))
            );
         }
      }
   }

   public void handleDisplayClick(String componentData, ClickData clickData) {
      if (!clickData.isRemote && this.isOC) {
         this.amountOC = Mth.m_14045_(this.amountOC + (componentData.equals("ocAdd") ? 1 : -1), 0, 4);
         if (this.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.m_7654_().execute(() -> {
               if (this.isFormed()) {
                  this.getPatternLock().lock();

                  try {
                     this.onStructureInvalid();
                     MultiblockWorldSavedData mwsd = MultiblockWorldSavedData.getOrCreate(serverLevel);
                     mwsd.removeMapping(this.getMultiblockState());
                     mwsd.addAsyncLogic(this);
                  } finally {
                     this.getPatternLock().unlock();
                  }
               }
            });
         }
      }
   }

   public IGuiTexture getScreenTexture() {
      return GuiTextures.DISPLAY_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks);
   }

   public ModularUI createUI(Player entityPlayer) {
      DraggableScrollableWidgetGroup screen = new DraggableScrollableWidgetGroup(7, 4, 162, 121).setBackground(this.getScreenTexture());
      screen.addWidget(new LabelWidget(4, 5, this.self().getBlockState().m_60734_().m_7705_()));
      screen.addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText).setMaxWidthLimit(150).clickHandler(this::handleDisplayClick));
      return new ModularUI(176, 216, this, entityPlayer)
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks)})
         .widget(screen)
         .widget(
            UITemplate.bindPlayerInventory(
               entityPlayer.m_150109_(), GuiTextures.SLOT_STEAM.get(ConfigHolder.INSTANCE.machines.steelSteamMultiblocks), 7, 134, true
            )
         );
   }

   @Override
   public double getConversionRate() {
      return 0.5 * (this.isOC ? Math.pow(3.0, this.amountOC) : 1.0);
   }
}
