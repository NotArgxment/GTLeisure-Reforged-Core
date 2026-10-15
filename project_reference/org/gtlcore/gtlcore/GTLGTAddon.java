package org.gtlcore.gtlcore;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.lowdragmc.lowdraglib.Platform;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import org.gtlcore.gtlcore.api.data.tag.GTLTagPrefix;
import org.gtlcore.gtlcore.api.registries.GTLRegistration;
import org.gtlcore.gtlcore.common.data.GTLBedrockFluids;
import org.gtlcore.gtlcore.common.data.GTLBlocks;
import org.gtlcore.gtlcore.common.data.GTLCovers;
import org.gtlcore.gtlcore.common.data.GTLElements;
import org.gtlcore.gtlcore.common.data.GTLItems;
import org.gtlcore.gtlcore.common.data.GTLSoundEntries;
import org.gtlcore.gtlcore.data.recipe.CircuitRecipes;
import org.gtlcore.gtlcore.data.recipe.ElementCopying;
import org.gtlcore.gtlcore.data.recipe.FuelRecipes;
import org.gtlcore.gtlcore.data.recipe.GCyMRecipes;
import org.gtlcore.gtlcore.data.recipe.MachineRecipe;
import org.gtlcore.gtlcore.data.recipe.Misc;
import org.gtlcore.gtlcore.data.recipe.RemoveRecipe;
import org.gtlcore.gtlcore.data.recipe.chemistry.MixerRecipes;
import org.gtlcore.gtlcore.data.recipe.processing.Lanthanidetreatment;
import org.gtlcore.gtlcore.data.recipe.processing.SkyTearsAndGregHeart;
import org.gtlcore.gtlcore.data.recipe.processing.StoneDustProcess;

@GTAddon
public class GTLGTAddon implements IGTAddon {
   public String addonModId() {
      return "gtlcore";
   }

   public GTRegistrate getRegistrate() {
      return GTLRegistration.REGISTRATE;
   }

   public boolean requiresHighTier() {
      return true;
   }

   public void initializeAddon() {
      GTLItems.init();
      GTLBlocks.init();
   }

   public void registerSounds() {
      GTLSoundEntries.init();
   }

   public void registerCovers() {
      GTLCovers.init();
   }

   public void registerElements() {
      GTLElements.init();
   }

   public void registerTagPrefixes() {
      GTLTagPrefix.init();
   }

   public void addRecipes(Consumer<FinishedRecipe> provider) {
      GCyMRecipes.init(provider);
      FuelRecipes.init(provider);
      MachineRecipe.init(provider);
      Misc.init(provider);
      ElementCopying.init(provider);
      StoneDustProcess.init(provider);
      Lanthanidetreatment.init(provider);
      CircuitRecipes.init(provider);
      MixerRecipes.init(provider);
      SkyTearsAndGregHeart.init(provider);
   }

   public void removeRecipes(Consumer<ResourceLocation> consumer) {
      RemoveRecipe.init(consumer);
   }

   public void registerFluidVeins() {
      if (!Platform.isDevEnv()) {
         GTLBedrockFluids.init();
      }
   }
}
