package org.gtlcore.gtlcore.common.machine.multiblock.generator;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.ConditionalSubscriptionHandler;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.machine.multiblock.part.RotorHatchPartMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MegaTurbineMachine extends WorkableElectricMultiblockMachine implements ITieredMachine {
   public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
      MegaTurbineMachine.class, WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
   );
   public static final int MIN_DURABILITY_TO_WARN = 10;
   private final int baseEuOutput;
   private final int tier;
   private int excessVoltage;
   private Set<RotorHolderPartMachine> rotorHolderMachines;
   private RotorHatchPartMachine rotorHatchPartMachine = null;
   protected ConditionalSubscriptionHandler rotorSubs;

   public ManagedFieldHolder getFieldHolder() {
      return MANAGED_FIELD_HOLDER;
   }

   public MegaTurbineMachine(IMachineBlockEntity holder, int tier, int am) {
      super(holder, new Object[0]);
      this.tier = tier;
      this.baseEuOutput = (int)GTValues.V[tier] * am;
      this.rotorSubs = new ConditionalSubscriptionHandler(this, this::rotorUpdate, this::isFormed);
   }

   private void rotorUpdate() {
      if (this.rotorHatchPartMachine != null && this.getOffsetTimer() % 20L == 0L) {
         if (this.rotorHatchPartMachine.getInventory().isEmpty()) {
            return;
         }

         ItemStackTransfer storage = this.rotorHatchPartMachine.getInventory().storage;

         for (RotorHolderPartMachine part : this.rotorHolderMachines) {
            if (!part.hasRotor()) {
               part.setRotorStack(storage.getStackInSlot(0));
               storage.setStackInSlot(0, ItemStack.f_41583_);
            }
         }
      }
   }

   public void onStructureFormed() {
      super.onStructureFormed();

      for (IMultiPart part : this.getParts()) {
         if (part instanceof RotorHolderPartMachine rotorHolderPartMachine) {
            this.rotorHolderMachines = Objects.requireNonNullElseGet(this.rotorHolderMachines, ObjectOpenHashSet::new);
            this.rotorHolderMachines.add(rotorHolderPartMachine);
         }

         if (part instanceof RotorHatchPartMachine rotorHatchPart) {
            this.rotorHatchPartMachine = rotorHatchPart;
         }
      }

      this.rotorSubs.initialize(this.getLevel());
   }

   public void onStructureInvalid() {
      super.onStructureInvalid();
      this.rotorHolderMachines = null;
      this.rotorHatchPartMachine = null;
   }

   public boolean onWorking() {
      for (RotorHolderPartMachine part : this.rotorHolderMachines) {
         if (!part.onWorking(this)) {
            return false;
         }
      }

      return super.onWorking();
   }

   @Nullable
   private RotorHolderPartMachine getRotorHolder() {
      if (this.rotorHolderMachines != null) {
         Iterator var1 = this.rotorHolderMachines.iterator();
         if (var1.hasNext()) {
            return (RotorHolderPartMachine)var1.next();
         }
      }

      return null;
   }

   public long getOverclockVoltage() {
      RotorHolderPartMachine rotorHolder = this.getRotorHolder();
      if (rotorHolder != null && rotorHolder.hasRotor()) {
         long eu = (long)this.baseEuOutput * rotorHolder.getTotalPower() / 100L;
         if (eu < 0L) {
            RecipeResult.of(this, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.rotor_holder.tier")));
         }

         return eu;
      } else {
         return 0L;
      }
   }

   protected long boostProduction(long production) {
      RotorHolderPartMachine rotorHolder = this.getRotorHolder();
      if (rotorHolder != null && rotorHolder.hasRotor()) {
         int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
         int currentSpeed = rotorHolder.getRotorSpeed();
         return currentSpeed >= maxSpeed ? production : (long)(production * Math.pow(1.0 * currentSpeed / maxSpeed, 2.0));
      } else {
         return 0L;
      }
   }

   @Nullable
   public static GTRecipe recipeModifier(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params, @NotNull OCResult result) {
      if (machine instanceof MegaTurbineMachine turbineMachine) {
         String rotor = "";

         for (RotorHolderPartMachine part : turbineMachine.rotorHolderMachines) {
            String partRotor = part.getRotorStack().m_41786_().getString();
            if (rotor.isEmpty()) {
               rotor = partRotor;
            } else if (!rotor.equals(partRotor)) {
               RecipeResult.of(turbineMachine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.rotor.different")));
               return null;
            }
         }

         RotorHolderPartMachine rotorHolder = turbineMachine.getRotorHolder();
         long EUt = RecipeHelper.getOutputEUt(recipe);
         if (rotorHolder != null && EUt > 0L) {
            int turbineMaxVoltage = (int)turbineMachine.getOverclockVoltage();
            if (turbineMachine.excessVoltage >= turbineMaxVoltage) {
               turbineMachine.excessVoltage -= turbineMaxVoltage;
               return null;
            } else {
               double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;
               int maxParallel = (int)((turbineMaxVoltage - turbineMachine.excessVoltage) / (EUt * holderEfficiency));
               turbineMachine.excessVoltage += (int)(maxParallel * EUt * holderEfficiency - turbineMaxVoltage);
               Pair<GTRecipe, Integer> parallelResult = GTRecipeModifiers.fastParallel(turbineMachine, recipe, Math.max(1, maxParallel), false);
               recipe = parallelResult.getFirst() == recipe ? recipe.copy() : (GTRecipe)parallelResult.getFirst();
               long eut = turbineMachine.boostProduction((long)(EUt * holderEfficiency * ((Integer)parallelResult.getSecond()).intValue()));
               recipe.tickOutputs
                  .put(EURecipeCapability.CAP, List.of(new Content(eut, ChanceLogic.getMaxChancedValue(), ChanceLogic.getMaxChancedValue(), 0, null, null)));
               return recipe;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public boolean dampingWhenWaiting() {
      return false;
   }

   public boolean canVoidRecipeOutputs(RecipeCapability<?> capability) {
      return capability != EURecipeCapability.CAP;
   }

   public void addDisplayText(List<Component> textList) {
      super.addDisplayText(textList);
      if (this.isFormed()) {
         RotorHolderPartMachine rotorHolder = this.getRotorHolder();
         if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
            textList.add(
               Component.m_237110_(
                  "gtceu.multiblock.turbine.rotor_speed",
                  new Object[]{FormattingUtil.formatNumbers(rotorHolder.getRotorSpeed()), FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed())}
               )
            );
            textList.add(Component.m_237110_("gtceu.multiblock.turbine.efficiency", new Object[]{rotorHolder.getTotalEfficiency()}));
            long maxProduction = this.getOverclockVoltage();
            long currentProduction = this.isActive() ? this.boostProduction((int)maxProduction) : 0L;
            String voltageName = GTValues.VNF[GTUtil.getTierByVoltage(currentProduction)];
            if (this.isActive()) {
               textList.add(
                  3,
                  Component.m_237110_("gtceu.multiblock.turbine.energy_per_tick", new Object[]{FormattingUtil.formatNumbers(currentProduction), voltageName})
               );
            }

            int rotorDurability = rotorHolder.getRotorDurabilityPercent();
            if (rotorDurability > 10) {
               textList.add(Component.m_237110_("gtceu.multiblock.turbine.rotor_durability", new Object[]{rotorDurability}));
            } else {
               textList.add(
                  Component.m_237110_("gtceu.multiblock.turbine.rotor_durability", new Object[]{rotorDurability})
                     .m_6270_(Style.f_131099_.m_131140_(ChatFormatting.RED))
               );
            }
         }
      }
   }

   public int getTier() {
      return this.tier;
   }
}
