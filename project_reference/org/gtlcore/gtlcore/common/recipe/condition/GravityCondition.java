package org.gtlcore.gtlcore.common.recipe.condition;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import org.gtlcore.gtlcore.api.recipe.RecipeResult;
import org.gtlcore.gtlcore.common.data.GTLRecipeConditions;
import org.gtlcore.gtlcore.common.machine.multiblock.part.maintenance.IGravityPartMachine;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.jetbrains.annotations.NotNull;

public class GravityCondition extends RecipeCondition {
   public static final Codec<GravityCondition> CODEC = RecordCodecBuilder.create(
      instance -> RecipeCondition.isReverse(instance).and(Codec.BOOL.fieldOf("gravity").forGetter(val -> val.zero)).apply(instance, GravityCondition::new)
   );
   public static final GravityCondition INSTANCE = new GravityCondition();
   private boolean zero = false;
   private int heightThreshold = 313;

   public GravityCondition(boolean zero) {
      this.zero = zero;
   }

   public GravityCondition(boolean isReverse, boolean zero) {
      super(isReverse);
      this.zero = zero;
   }

   public GravityCondition(boolean zero, int heightThreshold) {
      this.zero = zero;
      this.heightThreshold = heightThreshold;
   }

   public GravityCondition(boolean isReverse, boolean zero, int heightThreshold) {
      super(isReverse);
      this.zero = zero;
      this.heightThreshold = heightThreshold;
   }

   public RecipeConditionType<?> getType() {
      return GTLRecipeConditions.GRAVITY;
   }

   public Component getTooltips() {
      return Component.m_237115_("gtlcore.condition." + (this.zero ? "zero_" : "") + "gravity");
   }

   public boolean test(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
      MetaMachine machine = recipeLogic.getMachine();
      if (machine instanceof MultiblockControllerMachine controllerMachine) {
         for (IMultiPart part : controllerMachine.self().getParts()) {
            if (part instanceof IGravityPartMachine gravityPart) {
               if (gravityPart.getCurrentGravity() == (this.zero ? 0 : 100)) {
                  return true;
               }

               RecipeResult.of(
                  (IRecipeLogicMachine)machine, RecipeResult.fail(Component.m_237115_("gtceu.recipe.fail.gravity" + (this.zero ? ".low" : ".power")))
               );
            }
         }
      }

      Level level = Objects.requireNonNull(machine.getLevel(), "Machine level cannot be null");
      BlockPos machinePos = machine.getPos();
      String dimensionName = level.m_46472_().m_135782_().toString();
      boolean isInOrbitDimension = dimensionName.contains("_orbit");
      if (isInOrbitDimension && this.zero) {
         return true;
      }

      if (ConfigHolder.INSTANCE.enableSkyBlokeMode) {
         boolean isInOverworld = dimensionName.equals("minecraft:overworld");
         boolean exceedsHeightThreshold = machinePos.m_123342_() >= this.heightThreshold;
         if (isInOverworld && exceedsHeightThreshold && this.zero) {
            return true;
         }
      }

      return false;
   }

   public RecipeCondition createTemplate() {
      return new GravityCondition(false, 313);
   }

   @NotNull
   public JsonObject serialize() {
      JsonObject config = super.serialize();
      config.addProperty("gravity", this.zero);
      config.addProperty("height_threshold", this.heightThreshold);
      return config;
   }

   public RecipeCondition deserialize(@NotNull JsonObject config) {
      super.deserialize(config);
      this.zero = GsonHelper.m_13855_(config, "gravity", false);
      this.heightThreshold = GsonHelper.m_13824_(config, "height_threshold", 313);
      return this;
   }

   public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
      super.fromNetwork(buf);
      this.zero = buf.readBoolean();
      this.heightThreshold = buf.readInt();
      return this;
   }

   public void toNetwork(FriendlyByteBuf buf) {
      super.toNetwork(buf);
      buf.writeBoolean(this.zero);
      buf.writeInt(this.heightThreshold);
   }

   public GravityCondition() {
   }
}
