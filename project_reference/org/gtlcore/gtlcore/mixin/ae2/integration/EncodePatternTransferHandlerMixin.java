package org.gtlcore.gtlcore.mixin.ae2.integration;

import appeng.api.stacks.GenericStack;
import appeng.integration.modules.jei.transfer.EncodePatternTransferHandler;
import com.gregtechceu.gtceu.integration.jei.multipage.MultiblockInfoWrapper;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EncodePatternTransferHandler.class)
public class EncodePatternTransferHandlerMixin {
   @ModifyArg(
      method = "transferRecipe(Lappeng/menu/me/items/PatternEncodingTermMenu;Ljava/lang/Object;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/world/entity/player/Player;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
      at = @At(
         value = "INVOKE",
         target = "Lappeng/integration/modules/jeirei/EncodingHelper;encodeProcessingRecipe(Lappeng/menu/me/items/PatternEncodingTermMenu;Ljava/util/List;Ljava/util/List;)V"
      ),
      index = 1,
      remap = false
   )
   public List<List<GenericStack>> multiBlockInputFilter(List<List<GenericStack>> genericIngredients, @Local(name = "recipeBase") Object recipeBase) {
      if (recipeBase instanceof MultiblockInfoWrapper && ConfigHolder.INSTANCE.filterHatch.length != 0) {
         ObjectArrayList<List<GenericStack>> newList = new ObjectArrayList();

         for (List<GenericStack> l : genericIngredients) {
            List<GenericStack> list = l.stream()
               .filter(g -> Arrays.stream(ConfigHolder.INSTANCE.filterHatch).noneMatch(s -> g.what().getId().toString().contains(s)))
               .toList();
            if (!list.isEmpty()) {
               newList.add(l);
            }
         }

         return newList;
      } else {
         return genericIngredients;
      }
   }

   @ModifyArg(
      method = "transferRecipe(Lappeng/menu/me/items/PatternEncodingTermMenu;Ljava/lang/Object;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/world/entity/player/Player;ZZ)Lmezz/jei/api/recipe/transfer/IRecipeTransferError;",
      at = @At(
         value = "INVOKE",
         target = "Lappeng/integration/modules/jeirei/EncodingHelper;encodeProcessingRecipe(Lappeng/menu/me/items/PatternEncodingTermMenu;Ljava/util/List;Ljava/util/List;)V"
      ),
      index = 2,
      remap = false
   )
   public List<GenericStack> multiBlockOutputImport(List<GenericStack> genericIngredients, @Local(name = "recipeBase") Object recipeBase) {
      if (recipeBase instanceof MultiblockInfoWrapper miw) {
         GenericStack g = GenericStack.fromItemStack(
            Items.f_42615_.m_7968_().kjs$withName(Component.m_237115_(miw.definition.getId().m_214296_("block")).m_130938_(style -> style.m_178520_(16536828)))
         );
         return g == null ? List.of() : List.of(g);
      } else {
         return genericIngredients;
      }
   }
}
