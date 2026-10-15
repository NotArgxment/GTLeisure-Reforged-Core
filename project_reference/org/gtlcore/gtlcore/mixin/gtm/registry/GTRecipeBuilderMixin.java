package org.gtlcore.gtlcore.mixin.gtm.registry;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.nbt.CompoundTag;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTRecipeBuilder.class)
public abstract class GTRecipeBuilderMixin {
   @Shadow(remap = false)
   public GTRecipeType recipeType;
   @Shadow(remap = false)
   public int duration;
   @Shadow(remap = false)
   @NotNull
   public CompoundTag data = new CompoundTag();
   @Unique
   private long gTLCore$eut = 0L;

   @Shadow(remap = false)
   public GTRecipeBuilder duration(int duration) {
      return null;
   }

   @Inject(method = "EUt(J)Lcom/gregtechceu/gtceu/data/recipe/builder/GTRecipeBuilder;", at = @At("HEAD"), remap = false)
   private void eu(long eu, CallbackInfoReturnable<GTRecipeBuilder> cir) {
      this.gTLCore$eut = eu;
      this.data.m_128405_("euTier", GTUtil.getTierByVoltage(eu > 0L ? eu : -eu));
   }

   @Unique
   private int gTLCore$getDuration() {
      return ConfigHolder.INSTANCE.durationMultiplier != 1.0
            && this.gTLCore$eut >= 0L
            && this.recipeType != GTRecipeTypes.get("primitive_void_ore")
            && this.recipeType != GTRecipeTypes.get("large_boiler")
            && this.recipeType != GTRecipeTypes.get("steam_boiler")
            && this.recipeType != GTRecipeTypes.get("slaughterhouse")
            && this.recipeType != GTRecipeTypes.get("dyson_sphere")
            && this.recipeType != GTRecipeTypes.get("space_elevator")
            && this.recipeType != GTRecipeTypes.get("annihilate_generator")
            && this.recipeType != GTLRecipeTypes.CREATE_AGGREGATION_RECIPES
            && this.recipeType != GTLRecipeTypes.DOOR_OF_CREATE_RECIPES
         ? (int)Math.min(2.147483647E9, Math.max(1.0, Math.abs(this.duration * ConfigHolder.INSTANCE.durationMultiplier)))
         : Math.abs(this.duration);
   }

   @Inject(method = "toJson", at = @At("TAIL"), remap = false)
   public void toJson(JsonObject json, CallbackInfo ci) {
      json.addProperty("duration", this.gTLCore$getDuration());
   }
}
