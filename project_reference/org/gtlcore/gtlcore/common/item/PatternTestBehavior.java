package org.gtlcore.gtlcore.common.item;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.item.component.IItemUIFactory;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.integration.ae2.gui.widget.AETextInputButtonWidget;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory;
import com.lowdragmc.lowdraglib.gui.factory.HeldItemUIFactory.HeldItemHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.gtlcore.gtlcore.GTLCore;
import org.gtlcore.gtlcore.api.item.tool.ae2.patternTool.Ae2GtmProcessingPattern;
import org.gtlcore.gtlcore.api.item.tool.ae2.patternTool.ConflictAnalysisManager;
import org.gtlcore.gtlcore.api.item.tool.ae2.patternTool.ConflictAnalysisResult;
import org.gtlcore.gtlcore.api.item.tool.ae2.patternTool.GTRecipeManager;

public class PatternTestBehavior implements IItemUIFactory {
   public static final PatternTestBehavior INSTANCE = new PatternTestBehavior();
   private String ConflictAnalysisType = "";
   private int ConflictAnalysisCircuit = 0;
   private String Ae2PatternGeneratorType = "";
   private int Ae2PatternGeneratorCircuit = 0;
   private int Ae2PatternGeneratorScale = 1;
   private String Ae2PatternGeneratorInputsBlackKey = "";
   private String Ae2PatternGeneratorInputsWhiteKey = "";
   private String Ae2PatternGeneratorOutputsBlackKey = "";
   private String Ae2PatternGeneratorOutputsWhiteKey = "";

   public ModularUI createUI(HeldItemHolder heldItemHolder, Player player) {
      WidgetGroup containerPatternAnalysis = new WidgetGroup(8, 8, 160, 50)
         .addWidget(new ImageWidget(4, 4, 152, 42, GuiTextures.DISPLAY))
         .addWidget(new LabelWidget(6, 6, "\u914d\u65b9\u51b2\u7a81\u5206\u6790"))
         .addWidget(
            new AETextInputButtonWidget(82, 6, 72, 12)
               .setText(this.ConflictAnalysisType)
               .setOnConfirm(this::setConflictAnalysisType)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.set_recipe_type")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 20, 72, 12)
               .setText(String.valueOf(this.ConflictAnalysisCircuit))
               .setOnConfirm(s -> this.setConflictAnalysisCircuit(Integer.parseInt(s)))
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.set_programmed_circuit")})
         )
         .addWidget(
            new ButtonWidget(
                  6,
                  24,
                  64,
                  20,
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture(Component.m_237115_("gtlcore.gui.start_analysis").getString())}),
                  clickData -> this.useAnalysisRecipesBaby(heldItemHolder)
               )
               .setHoverTooltips(
                  new Component[]{
                     Component.m_237115_("tooltip.gtlcore.current_recipe_type")
                        .m_7220_(Component.m_237115_("gtceu." + this.ConflictAnalysisType))
                        .m_7220_(Component.m_237110_("gtlcore.tooltip.current_recipe_type_circuit", new Object[]{this.ConflictAnalysisCircuit}))
                  }
               )
         );
      WidgetGroup containerPatternGenerator = new WidgetGroup(8, 58, 160, 50)
         .addWidget(new ImageWidget(4, 4, 152, 42, GuiTextures.DISPLAY))
         .addWidget(new LabelWidget(6, 6, Component.m_237115_("gtlcore.gui.pattern_debug").getString()))
         .addWidget(
            new ButtonWidget(
                  6,
                  24,
                  64,
                  20,
                  new GuiTextureGroup(new IGuiTexture[]{GuiTextures.BUTTON, new TextTexture(Component.m_237115_("gtlcore.gui.get_pattern").getString())}),
                  clickData -> this.useAe2PatternGenerator(heldItemHolder)
               )
               .setHoverTooltips(
                  new Component[]{
                     Component.m_237115_("tooltip.gtlcore.current_recipe_type")
                        .m_7220_(Component.m_237115_("gtceu." + this.Ae2PatternGeneratorType))
                        .m_130946_(" \u7535\u8def\uff1a" + this.Ae2PatternGeneratorCircuit)
                        .m_130946_(" \u5c3a\u5bf8\uff1a" + this.Ae2PatternGeneratorScale)
                  }
               )
         )
         .addWidget(
            new AETextInputButtonWidget(82, 6, 72, 12)
               .setText(this.Ae2PatternGeneratorType)
               .setOnConfirm(this::setAe2PatternGeneratorType)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.set_recipe_type")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 20, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorCircuit))
               .setOnConfirm(s -> this.setAe2PatternGeneratorCircuit(Integer.parseInt(s)))
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.set_programmed_circuit")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 34, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorScale))
               .setOnConfirm(this::setAe2PatternGeneratorScale)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.set_pattern_multiplier")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 48, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorInputsWhiteKey))
               .setOnConfirm(this::setAe2PatternGeneratorInputsWhiteKey)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.input_whitelist_keywords")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 62, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorInputsBlackKey))
               .setOnConfirm(this::setAe2PatternGeneratorInputsBlackKey)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.input_blacklist_keywords")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 76, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorOutputsWhiteKey))
               .setOnConfirm(this::setAe2PatternGeneratorOutputsWhiteKey)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.output_whitelist_keywords")})
         )
         .addWidget(
            new AETextInputButtonWidget(82, 90, 72, 12)
               .setText(String.valueOf(this.Ae2PatternGeneratorOutputsBlackKey))
               .setOnConfirm(this::setAe2PatternGeneratorOutputsBlackKey)
               .setButtonTooltips(new Component[]{Component.m_237115_("tooltip.gtlcore.output_blacklist_keywords")})
         );
      return new ModularUI(176, 124, heldItemHolder, player)
         .widget(containerPatternAnalysis)
         .widget(containerPatternGenerator)
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND});
   }

   private void setAe2PatternGeneratorScale(String s) {
      this.Ae2PatternGeneratorScale = Math.min(2147483647, Integer.parseInt(s));
   }

   public void useAe2PatternGenerator(HeldItemHolder playerInventoryHolder) {
      if (playerInventoryHolder.getPlayer() instanceof ServerPlayer serverPlayer) {
         GTRecipeType recipeType = GTRecipeTypes.get("gtceu:" + this.Ae2PatternGeneratorType);
         GTRecipeManager gtRecipeManager = new GTRecipeManager();
         gtRecipeManager.filterRecipesByType(recipeType);
         gtRecipeManager.filterRecipesByCircuit(this.Ae2PatternGeneratorCircuit);
         if (!this.Ae2PatternGeneratorInputsWhiteKey.isEmpty()) {
            gtRecipeManager.filterRecipesByInputsIdArray(this.Ae2PatternGeneratorInputsWhiteKey.split(" "), true);
         }

         if (!this.Ae2PatternGeneratorInputsBlackKey.isEmpty()) {
            gtRecipeManager.filterRecipesByInputsIdArray(this.Ae2PatternGeneratorInputsBlackKey.split(" "), false);
         }

         if (!this.Ae2PatternGeneratorOutputsWhiteKey.isEmpty()) {
            gtRecipeManager.filterRecipesByOutputsIdArray(this.Ae2PatternGeneratorOutputsWhiteKey.split(" "), true);
         }

         if (!this.Ae2PatternGeneratorOutputsBlackKey.isEmpty()) {
            gtRecipeManager.filterRecipesByOutputsIdArray(this.Ae2PatternGeneratorOutputsBlackKey.split(" "), false);
         }

         for (GTRecipe recipe : gtRecipeManager.getRecipes()) {
            long inputEUt = RecipeHelper.getInputEUt(recipe);
            int recipeEUtTier = RecipeHelper.getRecipeEUtTier(recipe);
            String recipeTier = GTValues.VN[recipeEUtTier];
            Ae2GtmProcessingPattern ae2GtmProcessingPattern = Ae2GtmProcessingPattern.of(recipe, serverPlayer);
            ae2GtmProcessingPattern.setScale(this.Ae2PatternGeneratorScale, false);
            ae2GtmProcessingPattern.setDefaultFilter();
            ae2GtmProcessingPattern.setLore(
               Component.m_237115_("gui.gtlcore.machine_colon")
                  .m_7220_(
                     Component.m_237115_("gtceu." + this.Ae2PatternGeneratorType)
                        .m_7220_(Component.m_237110_("gui.gtlcore.circuit_format", new Object[]{this.Ae2PatternGeneratorCircuit}))
                  )
            );
            ae2GtmProcessingPattern.setLore(Component.m_237110_("gui.gtlcore.voltage_colon", new Object[]{recipeTier, inputEUt}));
            ItemStack patternItemStack = ae2GtmProcessingPattern.getPatternItemStack();
            serverPlayer.kjs$give(patternItemStack);
         }
      }
   }

   public void useAnalysisRecipesBaby(HeldItemHolder playerInventoryHolder) {
      if (playerInventoryHolder.getPlayer() instanceof ServerPlayer) {
         this.analysisRecipesBaby(GTRecipeTypes.get("gtceu:" + this.ConflictAnalysisType), this.ConflictAnalysisCircuit);
      }
   }

   public void analysisRecipesBaby(GTRecipeType recipeType, int CIRCUIT) {
      MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
      RecipeManager recipeManager = currentServer.m_129894_();
      Set<GTRecipe> recipes = new HashSet<>();

      for (Recipe<?> recipe : recipeManager.m_44051_()) {
         if (recipe instanceof GTRecipe && recipe.m_6671_().equals(recipeType)) {
            recipes.add((GTRecipe)recipe);
         }
      }

      ConflictAnalysisManager conflictAnalysisManager = new ConflictAnalysisManager(recipes.stream().toList());
      List<ConflictAnalysisResult> conflictAnalysisResults = conflictAnalysisManager.useFindConflictForAll(CIRCUIT);
      conflictAnalysisResults.forEach(ConflictAnalysisResult::exportToPrint);
      GTLCore.LOGGER
         .info(
            "\u53ef\u80fd\u88ab\u9519\u8bef\u5408\u6210\u914d\u65b9\u6570\u91cf{} / \u6240\u6709\u6b64\u7535\u8def\u914d\u65b9\u6570{}",
            conflictAnalysisResults.toArray().length,
            recipes.toArray().length
         );
   }

   public InteractionResultHolder<ItemStack> use(Item item, Level level, Player player, InteractionHand usedHand) {
      ItemStack stack = player.m_21120_(usedHand);
      if (player instanceof ServerPlayer serverPlayer) {
         HeldItemUIFactory.INSTANCE.openUI(serverPlayer, usedHand);
      }

      return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
   }

   public InteractionResult useOn(UseOnContext context) {
      if (context.m_43723_() instanceof ServerPlayer serverPlayer) {
         serverPlayer.m_5661_(Component.m_237115_("message.gtlcore.right_click_air_gui"), true);
      }

      return InteractionResult.SUCCESS;
   }

   public void setConflictAnalysisType(String ConflictAnalysisType) {
      this.ConflictAnalysisType = ConflictAnalysisType;
   }

   public void setConflictAnalysisCircuit(int ConflictAnalysisCircuit) {
      this.ConflictAnalysisCircuit = ConflictAnalysisCircuit;
   }

   public void setAe2PatternGeneratorType(String Ae2PatternGeneratorType) {
      this.Ae2PatternGeneratorType = Ae2PatternGeneratorType;
   }

   public void setAe2PatternGeneratorCircuit(int Ae2PatternGeneratorCircuit) {
      this.Ae2PatternGeneratorCircuit = Ae2PatternGeneratorCircuit;
   }

   public void setAe2PatternGeneratorInputsBlackKey(String Ae2PatternGeneratorInputsBlackKey) {
      this.Ae2PatternGeneratorInputsBlackKey = Ae2PatternGeneratorInputsBlackKey;
   }

   public void setAe2PatternGeneratorInputsWhiteKey(String Ae2PatternGeneratorInputsWhiteKey) {
      this.Ae2PatternGeneratorInputsWhiteKey = Ae2PatternGeneratorInputsWhiteKey;
   }

   public void setAe2PatternGeneratorOutputsBlackKey(String Ae2PatternGeneratorOutputsBlackKey) {
      this.Ae2PatternGeneratorOutputsBlackKey = Ae2PatternGeneratorOutputsBlackKey;
   }

   public void setAe2PatternGeneratorOutputsWhiteKey(String Ae2PatternGeneratorOutputsWhiteKey) {
      this.Ae2PatternGeneratorOutputsWhiteKey = Ae2PatternGeneratorOutputsWhiteKey;
   }
}
