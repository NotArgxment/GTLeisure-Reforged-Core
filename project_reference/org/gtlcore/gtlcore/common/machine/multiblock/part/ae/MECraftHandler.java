package org.gtlcore.gtlcore.common.machine.multiblock.part.ae;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.Collections;
import java.util.List;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.gtlcore.gtlcore.api.recipe.ingredient.LongIngredient;

public class MECraftHandler extends NotifiableMAHandlerTrait {
   public MECraftHandler(MEMolecularAssemblerIOPartMachine machine) {
      super(machine);
   }

   public MEMolecularAssemblerIOPartMachine getMachine() {
      return (MEMolecularAssemblerIOPartMachine)this.machine;
   }

   @Override
   public void handleRecipeOutput(GTRecipe recipe) {
      Object2LongOpenHashMap<AEKey> buffer = this.getMachine().getBuffer();

      for (Content content : recipe.outputs.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList())) {
         if (content.content instanceof LongIngredient longIngredient) {
            buffer.addTo(AEItemKey.of(longIngredient.m_43908_()[0]), longIngredient.getActualAmount());
         }
      }

      this.getMachine().getMETrait().notifySelfIO();
   }

   @Override
   public GTRecipe extractGTRecipe(long parallelAmount, int tickDuration) {
      GTRecipe output = GTRecipeBuilder.ofRaw().buildRawRecipe();
      List<Content> outputList = output.outputs.computeIfAbsent(ItemRecipeCapability.CAP, cap -> new ObjectArrayList());
      long remain = parallelAmount;
      ObjectIterator<Entry<GenericStack>> it = Object2LongMaps.fastIterator(this.getMachine().getOutputItems());

      while (it.hasNext() && remain > 0L) {
         Entry<GenericStack> entry = (Entry<GenericStack>)it.next();
         GenericStack key = (GenericStack)entry.getKey();
         if (key.what() instanceof AEItemKey aeItemKey) {
            Item var18 = aeItemKey.getItem();
            long multiply = entry.getLongValue();
            long extract = Math.min(multiply, remain);
            Content cont = new Content(
               LongIngredient.create(Ingredient.m_43929_(new ItemLike[]{var18}), extract * key.amount()),
               ChanceLogic.getMaxChancedValue(),
               ChanceLogic.getMaxChancedValue(),
               0,
               null,
               null
            );
            outputList.add(cont);
            remain -= extract;
            multiply -= extract;
            if (multiply == 0L) {
               it.remove();
            } else {
               entry.setValue(multiply);
            }
         } else {
            it.remove();
         }
      }

      if (outputList.isEmpty()) {
         return null;
      }

      output.duration = tickDuration;
      return output;
   }
}
