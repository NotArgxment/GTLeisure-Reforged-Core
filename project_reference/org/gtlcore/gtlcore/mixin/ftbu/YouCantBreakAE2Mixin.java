package org.gtlcore.gtlcore.mixin.ftbu;

import dev.ftb.mods.ftbultimine.FTBUltiminePlayerData;
import dev.ftb.mods.ftbultimine.shape.BlockMatcher;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.utils.Registries;
import org.gtlcore.gtlcore.utils.TextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FTBUltiminePlayerData.class)
public abstract class YouCantBreakAE2Mixin {
   @ModifyVariable(method = "updateBlocks", at = @At(value = "STORE", ordinal = 2), remap = false, name = "matcher")
   public BlockMatcher modifyBlockMatcher(BlockMatcher matcher) {
      return ConfigHolder.INSTANCE.blackBlockList != null && ConfigHolder.INSTANCE.blackBlockList.length >= 1 ? (original, state) -> {
         boolean flag = !TextUtil.containsWithWildcard(ConfigHolder.INSTANCE.blackBlockList, Registries.getBlockId(state.m_60734_()));
         return flag && state.m_60734_() == original.m_60734_();
      } : matcher;
   }
}
