package org.gtlcore.gtlcore.mixin.gtm.api.item.tool;

import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.IGTToolDefinition;
import com.gregtechceu.gtceu.api.item.tool.ToolDefinitionBuilder;
import java.lang.reflect.Field;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTToolType.class)
public class GTToolTypeMixin {
   @Inject(method = "<clinit>", at = @At("TAIL"))
   private static void modifySoftMallet(CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {
      Field softMalletField = GTToolType.class.getDeclaredField("SOFT_MALLET");
      GTToolType softMallet = (GTToolType)softMalletField.get(null);
      IGTToolDefinition definition = new ToolDefinitionBuilder().crafting().cannotAttack().attackSpeed(-2.4F).sneakBypassUse().build();
      Field definitionField = GTToolType.class.getDeclaredField("toolDefinition");
      definitionField.setAccessible(true);
      definitionField.set(softMallet, definition);
   }
}
