package org.gtlcore.gtlcore.mixin.ae2.logic;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.helpers.patternprovider.PatternProviderLogic;
import com.gregtechceu.gtceu.common.data.GTItems;
import java.util.Set;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderLogic.class)
public abstract class PatternProviderLogicMixin {
   @Shadow(remap = false)
   @Final
   private Set<AEKey> patternInputs;

   @Inject(method = "updatePatterns", at = @At("TAIL"), remap = false)
   public void updatePatternsHook(CallbackInfo ci) {
      this.patternInputs.remove(AEItemKey.of((ItemLike)GTItems.INTEGRATED_CIRCUIT.get()));
   }
}
