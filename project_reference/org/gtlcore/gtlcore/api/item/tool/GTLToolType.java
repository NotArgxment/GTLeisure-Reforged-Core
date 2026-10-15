package org.gtlcore.gtlcore.api.item.tool;

import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;
import com.gregtechceu.gtceu.api.item.tool.behavior.IToolBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.BlockRotatingBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.DisableShieldBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.GrassPathBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.LogStripBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.ScrapeBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.ToolModeSwitchBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.TreeFellingBehavior;
import com.gregtechceu.gtceu.common.item.tool.behavior.WaxOffBehavior;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;

public class GTLToolType {
   public static final GTToolType VAJRA = GTToolType.builder("vajra")
      .idFormat("%s_vajra")
      .toolTag(new TagKey[]{TagUtil.createItemTag("tools/wrenches", false)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("tools/wrench", false)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("tools/wire_cutters", false)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("pickaxes", true)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("shovels", true)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("swords", true)})
      .toolTag(new TagKey[]{TagUtil.createItemTag("hoes", true)})
      .toolTag(new TagKey[]{ItemTags.f_144323_})
      .harvestTag(new TagKey[]{TagUtil.createBlockTag("mineable/hoe", true)})
      .harvestTag(new TagKey[]{TagUtil.createBlockTag("mineable/sword")})
      .harvestTag(new TagKey[]{TagUtil.createBlockTag("mineable/pickaxe", true)})
      .harvestTag(new TagKey[]{TagUtil.createBlockTag("mineable/shovel", true)})
      .toolStats(
         b -> b.blockBreaking()
            .crafting()
            .sneakBypassUse()
            .attackDamage(10.0F)
            .attackSpeed(2.0F)
            .behaviors(
               new IToolBehavior[]{
                  GrassPathBehavior.INSTANCE,
                  DisableShieldBehavior.INSTANCE,
                  TreeFellingBehavior.INSTANCE,
                  LogStripBehavior.INSTANCE,
                  ScrapeBehavior.INSTANCE,
                  WaxOffBehavior.INSTANCE,
                  BlockRotatingBehavior.INSTANCE,
                  ToolModeSwitchBehavior.INSTANCE
               }
            )
            .brokenStack(ToolHelper.SUPPLY_POWER_UNIT_IV)
      )
      .toolClasses(new GTToolType[]{GTToolType.PICKAXE, GTToolType.SHOVEL, GTToolType.AXE, GTToolType.WRENCH, GTToolType.WIRE_CUTTER})
      .build();
}
