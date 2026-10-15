package org.gtlcore.gtlcore.api.item.tool.ae2.patternTool;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.gtlcore.gtlcore.utils.Registries;

public class ConflictAnalysisObj {
   public GTRecipe recipe;
   public List<ItemStack> inputItemStacks = new ArrayList<>();
   public List<FluidStack> inputFluidStacks = new ArrayList<>();
   public List<ItemStack> outputItemStacks = new ArrayList<>();
   public List<FluidStack> outputFluidStacks = new ArrayList<>();
   public List<String> inputItemIds = new ArrayList<>();
   public List<String> inputFluidIds = new ArrayList<>();
   public List<String> outputItemIds = new ArrayList<>();
   public List<String> outputFluidIds = new ArrayList<>();
   public Map<String, Long> inputItemIdsMap = new HashMap<>();
   public Map<String, Long> inputFluidIdsMap = new HashMap<>();
   public Map<String, Long> outputItemIdsMap = new HashMap<>();
   public Map<String, Long> outputFluidIdsMap = new HashMap<>();
   public Number CIRCUIT;
   private boolean hasINTEGRATED_CIRCUIT = false;

   public ConflictAnalysisObj(GTRecipe recipe) {
      this.recipe = recipe;
      this.populateItemStacks(recipe.getInputContents(ItemRecipeCapability.CAP), this.inputItemStacks, this.inputItemIds, this.inputItemIdsMap);
      this.populateFluidStacks(recipe.getInputContents(FluidRecipeCapability.CAP), this.inputFluidStacks, this.inputFluidIds, this.inputFluidIdsMap);
      this.populateItemStacks(recipe.getOutputContents(ItemRecipeCapability.CAP), this.outputItemStacks, this.outputItemIds, this.outputItemIdsMap);
      this.populateFluidStacks(recipe.getOutputContents(FluidRecipeCapability.CAP), this.outputFluidStacks, this.outputFluidIds, this.outputFluidIdsMap);
   }

   private void populateItemStacks(List<Content> contentList, List<ItemStack> stackList, List<String> itemIdList, Map<String, Long> itemIdsMap) {
      contentList.forEach(content -> {
         ItemStack itemStack = ((Ingredient)ItemRecipeCapability.CAP.of(content.getContent())).kjs$getFirst();
         if (itemStack.m_41720_().equals(GTItems.INTEGRATED_CIRCUIT.m_5456_())) {
            this.CIRCUIT = IntCircuitBehaviour.getCircuitConfiguration(itemStack);
            this.hasINTEGRATED_CIRCUIT = true;
         } else {
            String itemId = Registries.getItemId(itemStack);
            stackList.add(itemStack);
            itemIdList.add(itemId);
            itemIdsMap.put(itemId, (long)itemStack.m_41613_());
         }
      });
      if (!this.hasINTEGRATED_CIRCUIT) {
         this.CIRCUIT = 0;
      }
   }

   private void populateFluidStacks(List<Content> contentList, List<FluidStack> stackList, List<String> fluidIdList, Map<String, Long> FluidIdsMap) {
      contentList.forEach(content -> {
         FluidStack[] fluidStackList = ((FluidIngredient)FluidRecipeCapability.CAP.of(content.getContent())).getStacks();
         FluidStack fluidStack = fluidStackList.length > 0 ? fluidStackList[0] : FluidStack.empty();
         stackList.add(fluidStack);
         if (!fluidStack.isEmpty()) {
            String key = Registries.getFluidId(fluidStack);
            fluidIdList.add(key);
            FluidIdsMap.put(key, fluidStack.getAmount());
         }
      });
   }

   @Override
   public String toString() {
      return "// ---------- start ---------- //\nCIRCUIT: %S\ninputs:\nList<ItemStack>: %s\nList<FluidStack> : %s\ninputItemIds : %s\ninputFluidIds : %s\ninputItemIdsMap : %s\ninputFluidIdsMap : %s\noutputs:\nList<ItemStack>: %s\nList<FluidStack> : %s\noutputItemIds : %s\noutputFluidIds : %s\noutputItemIdsMap : %s\noutputFluidIdsMap : %s\n// ---------- stopA ---------- //\n"
         .formatted(
            this.CIRCUIT,
            this.inputItemStacks,
            this.inputFluidStacks,
            this.inputItemIds,
            this.inputFluidIds,
            this.inputItemIdsMap,
            this.inputFluidIdsMap,
            this.outputItemStacks,
            this.outputFluidStacks,
            this.outputItemIds,
            this.outputFluidIds,
            this.outputItemIdsMap,
            this.outputFluidIdsMap
         );
   }
}
