package org.gtlcore.gtlcore.mixin.gtm;

import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.integration.jade.provider.CapabilityBlockProvider;
import com.gregtechceu.gtceu.integration.jade.provider.RecipeOutputProvider;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;
import org.gtlcore.gtlcore.utils.GTLUtil;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.gtlcore.gtlcore.utils.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

@Mixin(RecipeOutputProvider.class)
public abstract class RecipeOutputProviderMixin extends CapabilityBlockProvider<RecipeLogic> {
   @Shadow(remap = false)
   protected abstract void addFluidTooltips(ITooltip var1, List<FluidStack> var2);

   @Shadow(remap = false)
   protected abstract Component getItemName(ItemStack var1);

   protected RecipeOutputProviderMixin(ResourceLocation uid) {
      super(uid);
   }

   protected void write(CompoundTag data, RecipeLogic recipeLogic) {
      if (recipeLogic.isWorking()) {
         data.m_128379_("Working", recipeLogic.isWorking());
         GTRecipe recipe = recipeLogic.getLastRecipe();
         if (recipe != null) {
            Map<String, CompoundTag> cache = new HashMap<>();
            ListTag itemTags = new ListTag();

            for (Ingredient outputContent : RecipeHelper.getOutputContents(recipe, ItemRecipeCapability.CAP)) {
               ItemStack stack = outputContent.m_43908_()[0];
               if (stack != null && !stack.m_41619_()) {
                  String sid = Registries.getItemId(stack);
                  if (outputContent instanceof LongIngredient longIngredient) {
                     if (cache.containsKey(sid)) {
                        CompoundTag tag = cache.get(sid);
                        if (tag != null) {
                           long amount = tag.m_128454_("Count");
                           if (amount > 0L) {
                              tag.m_128356_("Count", NumberUtils.saturatedAdd(amount, longIngredient.getActualAmount()));
                           }
                        }
                     } else {
                        CompoundTag itemTag = new CompoundTag();
                        itemTag.m_128359_("id", sid);
                        itemTag.m_128356_("Count", longIngredient.getActualAmount());
                        if (stack.m_41783_() != null) {
                           itemTag.m_128365_("tag", stack.m_41783_().m_6426_());
                        }

                        cache.put(sid, itemTag);
                        itemTags.add(itemTag);
                     }
                  } else if (cache.containsKey(sid)) {
                     CompoundTag tag = cache.get(sid);
                     if (tag != null) {
                        long amount = tag.m_128454_("Count");
                        if (amount > 0L) {
                           tag.m_128356_("Count", NumberUtils.saturatedAdd(amount, stack.m_41613_()));
                        }
                     }
                  } else {
                     CompoundTag itemTag = new CompoundTag();
                     itemTag.m_128359_("id", sid);
                     itemTag.m_128356_("Count", stack.m_41613_());
                     if (stack.m_41783_() != null) {
                        itemTag.m_128365_("tag", stack.m_41783_().m_6426_());
                     }

                     cache.put(sid, itemTag);
                     itemTags.add(itemTag);
                  }
               }
            }

            if (!itemTags.isEmpty()) {
               data.m_128365_("OutputItems", itemTags);
            }

            ListTag fluidTags = new ListTag();

            for (FluidStack stack : RecipeHelper.getOutputFluids(recipe)) {
               if (stack != null && !stack.isEmpty()) {
                  String fid = Registries.getFluidId(stack);
                  if (cache.containsKey(fid)) {
                     CompoundTag tag = cache.get(fid);
                     if (tag != null) {
                        long amount = tag.m_128454_("Amount");
                        if (amount > 0L) {
                           tag.m_128356_("Amount", NumberUtils.saturatedAdd(amount, stack.getAmount()));
                        }
                     }
                  } else {
                     CompoundTag fluidTag = new CompoundTag();
                     fluidTag.m_128359_("FluidName", fid);
                     fluidTag.m_128356_("Amount", stack.getAmount());
                     if (stack.getTag() != null) {
                        fluidTag.m_128365_("Tag", stack.getTag().m_6426_());
                     }

                     cache.put(fid, fluidTag);
                     fluidTags.add(fluidTag);
                  }
               }
            }

            if (!fluidTags.isEmpty()) {
               data.m_128365_("OutputFluids", fluidTags);
            }
         }
      }
   }

   protected void addTooltip(CompoundTag capData, ITooltip tooltip, Player player, BlockAccessor block, BlockEntity blockEntity, IPluginConfig config) {
      if (capData.m_128471_("Working")) {
         List<CompoundTag> outputItems = new ArrayList<>();
         if (capData.m_128425_("OutputItems", 9)) {
            for (Tag tag : capData.m_128437_("OutputItems", 10)) {
               if (tag instanceof CompoundTag tCompoundTag) {
                  outputItems.add(tCompoundTag);
               }
            }
         }

         List<FluidStack> outputFluids = new ArrayList<>();
         if (capData.m_128425_("OutputFluids", 9)) {
            for (Tag tag : capData.m_128437_("OutputFluids", 10)) {
               if (tag instanceof CompoundTag tCompoundTag) {
                  FluidStack stack = FluidStack.loadFromTag(tCompoundTag);
                  if (!stack.isEmpty()) {
                     outputFluids.add(stack);
                  }
               }
            }
         }

         if (!outputItems.isEmpty() || !outputFluids.isEmpty()) {
            tooltip.add(Component.m_237115_("gtceu.top.recipe_output"));
         }

         this.gTLCore$addItemTooltips(tooltip, outputItems);
         this.addFluidTooltips(tooltip, outputFluids);
      }
   }

   @Unique
   private void gTLCore$addItemTooltips(ITooltip iTooltip, List<CompoundTag> outputItems) {
      IElementHelper helper = iTooltip.getElementHelper();

      for (CompoundTag tag : outputItems) {
         if (tag != null && !tag.m_128456_()) {
            ItemStack stack = GTLUtil.loadItemStack(tag);
            long count = tag.m_128454_("Count");
            iTooltip.add(helper.smallItem(stack));
            Component text = Component.m_237113_(" ")
               .m_130946_(NumberUtils.formatLong(count))
               .m_130946_("\u00d7 ")
               .m_7220_(this.getItemName(stack))
               .m_130940_(ChatFormatting.WHITE);
            iTooltip.append(text);
         }
      }
   }
}
