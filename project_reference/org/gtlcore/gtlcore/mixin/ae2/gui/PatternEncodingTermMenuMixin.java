package org.gtlcore.gtlcore.mixin.ae2.gui;

import appeng.api.config.Actionable;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.ITerminalHost;
import appeng.core.definitions.AEItems;
import appeng.helpers.IMenuCraftingPacket;
import appeng.helpers.IPatternTerminalMenuHost;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import appeng.menu.slot.RestrictedInputSlot;
import appeng.util.ConfigInventory;
import com.google.common.math.LongMath;
import com.gregtechceu.gtceu.common.data.GTItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.client.gui.PatterEncodingTermMenuModify;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternEncodingTermMenu.class, priority = 900)
public abstract class PatternEncodingTermMenuMixin extends MEStorageMenu implements IMenuCraftingPacket, PatterEncodingTermMenuModify {
   @Unique
   private static final ItemStack Pattern = AEItems.BLANK_PATTERN.stack();
   @Shadow(remap = false)
   @Final
   private ConfigInventory encodedInputsInv;
   @Shadow(remap = false)
   @Final
   private ConfigInventory encodedOutputsInv;
   @Shadow(remap = false)
   @Final
   private RestrictedInputSlot blankPatternSlot;
   @Shadow(remap = false)
   @Final
   private RestrictedInputSlot encodedPatternSlot;

   @Shadow(remap = false)
   @Nullable
   protected abstract ItemStack encodePattern();

   @Shadow(remap = false)
   protected abstract boolean isPattern(ItemStack var1);

   @Shadow(remap = false)
   protected abstract void clearPattern();

   public PatternEncodingTermMenuMixin(MenuType<?> menuType, int id, Inventory ip, ITerminalHost host) {
      super(menuType, id, ip, host);
   }

   @Inject(
      method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/IPatternTerminalMenuHost;Z)V",
      at = @At("TAIL"),
      remap = false
   )
   public void initHooks(MenuType<?> menuType, int id, Inventory ip, IPatternTerminalMenuHost host, boolean bindInventory, CallbackInfo ci) {
      this.registerClientAction("modifyPatter", Integer.class, this::gTLCore$modifyPatter);
   }

   @Overwrite(remap = false)
   public void encode() {
      if (this.isClientSide()) {
         this.sendClientAction("encode");
      } else {
         ItemStack encodedPattern = this.encodePattern();
         if (encodedPattern != null) {
            ItemStack encodeOutput = this.encodedPatternSlot.m_7993_();
            if (!encodeOutput.m_41619_() && !PatternDetailsHelper.isEncodedPattern(encodeOutput) && !AEItems.BLANK_PATTERN.isSameAs(encodeOutput)) {
               return;
            }

            if (encodeOutput.m_41619_()) {
               ItemStack blankPattern = this.blankPatternSlot.m_7993_();
               if (!this.isPattern(blankPattern)) {
                  return;
               }

               blankPattern.m_41774_(1);
               if (blankPattern.m_41613_() <= 0) {
                  if (this.storage != null) {
                     long extract = this.storage.extract(AEItemKey.of(Pattern), 64L, Actionable.SIMULATE, this.getActionSource());
                     if (extract > 0L) {
                        extract = this.storage.extract(AEItemKey.of(Pattern), extract, Actionable.MODULATE, this.getActionSource());
                        this.blankPatternSlot.m_5852_(Pattern.m_255036_((int)extract));
                     }
                  } else {
                     this.blankPatternSlot.m_5852_(ItemStack.f_41583_);
                  }
               }
            }

            this.encodedPatternSlot.m_5852_(encodedPattern);
         } else {
            this.clearPattern();
         }
      }
   }

   @Redirect(
      method = "encodeProcessingPattern",
      at = @At(value = "INVOKE", target = "Lappeng/util/ConfigInventory;getStack(I)Lappeng/api/stacks/GenericStack;", ordinal = 0),
      remap = false
   )
   private GenericStack filterData(ConfigInventory instance, int slot) {
      GenericStack stack = instance.getStack(slot);
      return stack == null
            || !(
               stack.what() instanceof AEItemKey aeItemKey
                  && aeItemKey.hasTag()
                  && (
                     aeItemKey.getItem() == GTItems.TOOL_DATA_STICK.m_5456_()
                        || aeItemKey.getItem() == GTItems.TOOL_DATA_ORB.m_5456_()
                        || aeItemKey.getItem() == GTItems.TOOL_DATA_MODULE.m_5456_()
                  )
            )
         ? stack
         : new GenericStack(AEItemKey.of(aeItemKey.getItem()), stack.amount());
   }

   @Override
   public void gTLCore$modifyPatter(Integer data) {
      if (this.isClientSide()) {
         this.sendClientAction("modifyPatter", data);
      } else {
         GenericStack[] output = this.gTLCore$valid(this.encodedOutputsInv, data);
         if (output == null) {
            return;
         }

         GenericStack[] input = this.gTLCore$valid(this.encodedInputsInv, data);
         if (input == null) {
            return;
         }

         for (int slot = 0; slot < output.length; slot++) {
            if (output[slot] != null) {
               this.encodedOutputsInv.setStack(slot, output[slot]);
            }
         }

         for (int slot = 0; slot < input.length; slot++) {
            if (input[slot] != null) {
               this.encodedInputsInv.setStack(slot, input[slot]);
            }
         }
      }
   }

   @Unique
   private GenericStack[] gTLCore$valid(ConfigInventory inv, int data) {
      boolean flag = data > 0;
      if (!flag) {
         data = -data;
      }

      GenericStack[] result = new GenericStack[inv.size()];

      for (int slot = 0; slot < inv.size(); slot++) {
         GenericStack stack = inv.getStack(slot);
         if (stack != null) {
            if (flag) {
               long modify = LongMath.saturatedMultiply(data, stack.amount());
               if (modify == 9223372036854775807L || modify == -9223372036854775808L) {
                  return null;
               }

               result[slot] = new GenericStack(stack.what(), modify);
            } else {
               if (stack.amount() % data != 0L) {
                  return null;
               }

               result[slot] = new GenericStack(stack.what(), stack.amount() / data);
            }
         }
      }

      return result;
   }
}
