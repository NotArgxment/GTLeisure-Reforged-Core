package org.gtlreforged.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.ActionResult;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeRunner;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import it.unimi.dsi.fastutil.objects.Object2IntMap;

import org.gtlreforged.api.machine.trait.IRecipeCapabilityMachine;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RecipeRunnerHelper {

    public static boolean matchRecipe(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return matchRecipeInput(holder, recipe) && matchRecipeOutput(holder, recipe);
    }

    public static boolean matchRecipeInput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return recipe.inputs.isEmpty()
                || handleRecipe(IO.IN, holder, recipe.inputs, Collections.emptyMap(), false, recipe, true).isSuccess();
    }

    public static boolean matchRecipeOutput(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        if (recipe.outputs.isEmpty()) {
            return true;
        }
        return holder instanceof IRecipeCapabilityMachine machine && machine.isRecipeOutputAlwaysMatch(recipe)
                || handleRecipe(IO.OUT, holder, recipe.outputs, Collections.emptyMap(), false, recipe, true).isSuccess();
    }

    public static boolean handleRecipeInput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipe(IO.IN, holder, recipe.inputs, holder.getRecipeLogic().getChanceCaches(), true, recipe, false).isSuccess();
    }

    public static boolean handleRecipeOutput(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipe(IO.OUT, holder, recipe.outputs, holder.getRecipeLogic().getChanceCaches(), true, recipe, false).isSuccess();
    }

    public static RecipeResult handleRecipe(
            IO io, IRecipeCapabilityHolder holder,
            Map<RecipeCapability<?>, List<Content>> contents, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches,
            boolean isTick, GTRecipe recipe, boolean isSimulate
    ) {
        return handleRecipe(io, holder, contents, chanceCaches, isTick, recipe, isSimulate, RecipeCacheStrategy.FULL_CACHE);
    }

    public static RecipeResult handleRecipe(
            IO io, IRecipeCapabilityHolder holder,
            Map<RecipeCapability<?>, List<Content>> contents, Map<RecipeCapability<?>, Object2IntMap<?>> chanceCaches,
            boolean isTick, GTRecipe recipe,
            boolean isSimulate, RecipeCacheStrategy cacheStrategy
    ) {
        RecipeRunner runner = new RecipeRunner(recipe, io, isTick, holder, chanceCaches, isSimulate);
        ActionResult actionResult = runner.handle(contents);

        RecipeResult result = actionResult.isSuccess()
                ? RecipeResult.SUCCESS
                : (io == IO.IN ? RecipeResult.FAIL_INPUT : RecipeResult.FAIL_OUTPUT);

        if (holder instanceof IRecipeLogicMachine machine) {
            RecipeResult.of(machine, result);
        }
        return result;
    }

    public static boolean matchRecipeInputNoMEInnerCache(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return recipe.inputs.isEmpty()
                || handleRecipe(IO.IN, holder, recipe.inputs, Collections.emptyMap(), false, recipe, true, RecipeCacheStrategy.HANDLE_PART_CACHE_ONLY).isSuccess();
    }

    public static boolean matchRecipeInputNocache(IRecipeCapabilityHolder holder, GTRecipe recipe) {
        return recipe.inputs.isEmpty()
                || handleRecipe(IO.IN, holder, recipe.inputs, Collections.emptyMap(), false, recipe, true, RecipeCacheStrategy.NO_CACHE).isSuccess();
    }

    public static boolean handleRecipeInputNocache(IRecipeLogicMachine holder, GTRecipe recipe) {
        return handleRecipe(IO.IN, holder, recipe.inputs, holder.getRecipeLogic().getChanceCaches(), true, recipe, false, RecipeCacheStrategy.NO_CACHE)
                .isSuccess();
    }
}