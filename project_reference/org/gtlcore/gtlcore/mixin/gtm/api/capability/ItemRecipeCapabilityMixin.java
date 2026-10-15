package org.gtlcore.gtlcore.mixin.gtm.api.capability;

import com.google.common.primitives.Ints;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.ResearchData.ResearchEntry;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.recipe.ui.GTRecipeTypeUI.RecipeHolder;
import com.gregtechceu.gtceu.common.recipe.condition.ResearchCondition;
import com.gregtechceu.gtceu.common.valueprovider.AddedFloat;
import com.gregtechceu.gtceu.common.valueprovider.CastedFloat;
import com.gregtechceu.gtceu.common.valueprovider.FlooredInt;
import com.gregtechceu.gtceu.common.valueprovider.MultipliedFloat;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.ResearchManager;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.utils.CycleItemStackHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.api.recipe.IParallelLogic;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemRecipeCapability.class)
public abstract class ItemRecipeCapabilityMixin extends RecipeCapability<Ingredient> {
   protected ItemRecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<Ingredient> serializer) {
      super(name, color, doRenderSlot, sortIndex, serializer);
   }

   @Overwrite(remap = false)
   public int limitParallel(GTRecipe recipe, IRecipeCapabilityHolder holder, int multiplier) {
      return Ints.saturatedCast(IParallelLogic.getOutputItemParallel(holder, recipe, recipe.getOutputContents(ItemRecipeCapability.CAP), multiplier));
   }

   @Overwrite(remap = false)
   public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
      return Ints.saturatedCast(IParallelLogic.getInputItemParallel(holder, recipe, parallelAmount));
   }

   @Overwrite(remap = false)
   public Ingredient copyWithModifier(Ingredient content, ContentModifier modifier) {
      if (content instanceof LongIngredient longIngredient) {
         return LongIngredient.create(longIngredient.getInner(), modifier.apply(longIngredient.getActualAmount()).longValue());
      } else if (content instanceof SizedIngredient sizedIngredient) {
         return LongIngredient.create(sizedIngredient.getInner(), modifier.apply((long)sizedIngredient.getAmount()).longValue());
      } else {
         return (Ingredient)(content instanceof IntProviderIngredient intProviderIngredient
            ? new IntProviderIngredient(
               intProviderIngredient.getInner(),
               new FlooredInt(
                  new AddedFloat(
                     new MultipliedFloat(new CastedFloat(intProviderIngredient.getCountProvider()), ConstantFloat.m_146458_((float)modifier.getMultiplier())),
                     ConstantFloat.m_146458_((float)modifier.getAddition())
                  )
               )
            )
            : LongIngredient.create(content, modifier.apply(1).longValue()));
      }
   }

   @Overwrite(remap = false)
   public Ingredient copyInner(Ingredient content) {
      return LongIngredient.copy(content);
   }

   public void applyWidgetInfo(
      @NotNull Widget widget,
      int index,
      boolean isXEI,
      IO io,
      @UnknownNullability("null when storage == null") RecipeHolder recipeHolder,
      @NotNull GTRecipeType recipeType,
      @UnknownNullability("null when content == null") GTRecipe recipe,
      @Nullable Content content,
      @Nullable Object storage
   ) {
      if (widget instanceof SlotWidget slot) {
         if (storage instanceof IItemTransfer items) {
            if (index >= 0 && index < items.getSlots()) {
               slot.setHandlerSlot(items, index);
               slot.setIngredientIO(io == IO.IN ? IngredientIO.INPUT : IngredientIO.OUTPUT);
               slot.setCanTakeItems(!isXEI);
               slot.setCanPutItems(!isXEI && io.support(IO.IN));
            }

            if (isXEI && recipeType.isHasResearchSlot() && index == items.getSlots() && ConfigHolder.INSTANCE.machines.enableResearch) {
               ResearchCondition condition = recipeHolder.conditions()
                  .stream()
                  .filter(ResearchCondition.class::isInstance)
                  .findAny()
                  .map(ResearchCondition.class::cast)
                  .orElse(null);
               if (condition != null) {
                  List<ItemStack> dataItems = new ArrayList<>();

                  for (ResearchEntry entry : condition.data) {
                     ItemStack dataStick = entry.getDataItem().m_41777_();
                     ResearchManager.writeResearchToNBT(dataStick.m_41784_(), entry.getResearchId(), recipeType);
                     dataItems.add(dataStick);
                  }

                  CycleItemStackHandler handler = new CycleItemStackHandler(List.of(dataItems));
                  slot.setHandlerSlot(handler, 0);
                  slot.setIngredientIO(IngredientIO.INPUT);
                  slot.setCanTakeItems(false);
                  slot.setCanPutItems(false);
               }
            }
         }

         if (content != null) {
            slot.setXEIChance((float)content.chance / content.maxChance);
            slot.setOnAddedTooltips(
               (w, tooltips) -> {
                  Ingredient ingredient = (Ingredient)ItemRecipeCapability.CAP.of(content.content);
                  long amount = 1L;
                  if (ingredient instanceof SizedIngredient si) {
                     amount = si.getAmount();
                  } else if (ingredient instanceof LongIngredient li) {
                     amount = li.getActualAmount();
                  }

                  tooltips.add(
                     Component.m_237115_("gtceu.machine.quantum_chest.items_stored")
                        .m_130940_(ChatFormatting.DARK_AQUA)
                        .m_7220_(Component.m_237113_(String.valueOf(amount)))
                  );
                  gTLCore$setConsumedChance(content, ChanceLogic.OR, tooltips, io);
                  if (this.isTickSlot(index, io, recipe)) {
                     tooltips.add(Component.m_237115_("gtceu.gui.content.per_tick"));
                  }
               }
            );
         }
      }
   }

   @Unique
   private static void gTLCore$setConsumedChance(Content content, ChanceLogic logic, List<Component> tooltips, IO io) {
      int chance = content.chance;
      if (chance < ChanceLogic.getMaxChancedValue()) {
         if (chance == 0) {
            tooltips.add(Component.m_237115_("gtceu.gui.content.chance_0"));
         } else {
            float chanceFloat = 100.0F * content.chance / content.maxChance;
            if (logic != ChanceLogic.NONE && logic != ChanceLogic.OR) {
               tooltips.add(
                  Component.m_237110_(
                        io == IO.IN ? "gtceu.gui.content.chance_1_logic_in" : "gtceu.gui.content.chance_1_logic",
                        new Object[]{FormattingUtil.formatNumber2Places(chanceFloat), logic.getTranslation()}
                     )
                     .m_130940_(ChatFormatting.YELLOW)
               );
            } else {
               tooltips.add(FormattingUtil.formatPercentage2Places(io == IO.IN ? "gtceu.gui.content.chance_1_in" : "gtceu.gui.content.chance_1", chanceFloat));
            }

            if (content.tierChanceBoost != 0) {
               String formatNumber = content.tierChanceBoost > 0
                  ? "+" + FormattingUtil.formatNumber2Places(content.tierChanceBoost / 100.0F)
                  : FormattingUtil.formatNumber2Places(content.tierChanceBoost / 100.0F);
               tooltips.add(Component.m_237110_("gtceu.gui.content.tier_boost_fix", new Object[]{formatNumber}).m_130940_(ChatFormatting.YELLOW));
            }
         }
      }
   }
}
