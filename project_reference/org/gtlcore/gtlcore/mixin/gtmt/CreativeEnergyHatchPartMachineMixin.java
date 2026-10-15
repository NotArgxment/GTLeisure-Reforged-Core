package org.gtlcore.gtlcore.mixin.gtmt;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredIOPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.pattern.MultiblockWorldSavedData;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.hepdd.gtmthings.common.block.machine.multiblock.part.CreativeEnergyHatchPartMachine;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.SelectorWidget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import java.util.Arrays;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;
import org.gtlcore.gtlcore.integration.gtmt.InfinityEnergyContainer;
import org.gtlcore.gtlcore.integration.gtmt.NewGTValues;
import org.gtlcore.gtlcore.utils.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CreativeEnergyHatchPartMachine.class)
public abstract class CreativeEnergyHatchPartMachineMixin extends TieredIOPartMachine {
   @Shadow(remap = false)
   @Final
   public NotifiableEnergyContainer energyContainer;
   @Shadow(remap = false)
   private long voltage;
   @Persisted
   @Shadow(remap = false)
   private Long maxEnergy;
   @Shadow(remap = false)
   private int setTier;
   @Shadow(remap = false)
   private int amps;

   public CreativeEnergyHatchPartMachineMixin(IMachineBlockEntity holder, int tier, IO io) {
      super(holder, tier, io);
   }

   @Overwrite(remap = false)
   protected NotifiableEnergyContainer createEnergyContainer() {
      this.setTier = 30;
      this.voltage = GTValues.VEX[this.setTier];
      this.maxEnergy = NumberUtils.saturatedMultiply(this.voltage, this.amps);
      return new InfinityEnergyContainer(this, this.maxEnergy, this.voltage, this.amps, 0L, 0L);
   }

   @Overwrite(remap = false)
   public ModularUI createUI(Player entityPlayer) {
      return new ModularUI(176, 136, this, entityPlayer)
         .background(new IGuiTexture[]{GuiTextures.BACKGROUND})
         .widget(new LabelWidget(7, 32, "gtceu.creative.energy.voltage"))
         .widget(new TextFieldWidget(9, 47, 152, 16, () -> String.valueOf(this.voltage), value -> {
            this.gTLCore$setVoltage(Long.parseLong(value));
            this.setTier = GTUtil.getTierByVoltage(this.voltage);
         }).setNumbersOnly(8L, 9223372036854775807L))
         .widget(new LabelWidget(7, 74, "gtceu.creative.energy.amperage"))
         .widget(
            new ButtonWidget(
               7,
               87,
               20,
               20,
               new GuiTextureGroup(new IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON, new TextTexture("-")}),
               cd -> this.gTLCore$setAmps(--this.amps == -1 ? 0 : this.amps)
            )
         )
         .widget(
            new TextFieldWidget(31, 89, 114, 16, () -> String.valueOf(this.amps), value -> this.gTLCore$setAmps(Integer.parseInt(value)))
               .setNumbersOnly(1, 67108864)
         )
         .widget(new ButtonWidget(149, 87, 20, 20, new GuiTextureGroup(new IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON, new TextTexture("+")}), cd -> {
            if (this.amps < 2147483647) {
               this.gTLCore$setAmps(++this.amps);
            }
         }))
         .widget(
            new SelectorWidget(7, 7, 50, 20, Arrays.stream(NewGTValues.VNF).toList(), -1)
               .setOnChanged(tier -> {
                  this.setTier = ArrayUtils.indexOf(NewGTValues.VNF, tier);
                  this.gTLCore$setVoltage(GTValues.VEX[this.setTier]);
               })
               .setSupplier(() -> NewGTValues.VNF[this.setTier])
               .setButtonBackground(new IGuiTexture[]{ResourceBorderTexture.BUTTON_COMMON})
               .setBackground(ColorPattern.BLACK.rectTexture())
               .setValue(NewGTValues.VNF[this.setTier])
         );
   }

   public void loadCustomPersistedData(@NotNull CompoundTag tag) {
      super.loadCustomPersistedData(tag);
      this.gTLCore$updateEnergyContainer();
   }

   @Unique
   private void gTLCore$setVoltage(long voltage) {
      this.voltage = voltage;
      this.maxEnergy = NumberUtils.saturatedMultiply(this.voltage, this.amps);
      this.gTLCore$updateMachine();
   }

   @Unique
   private void gTLCore$setAmps(int amps) {
      this.amps = amps;
      this.maxEnergy = NumberUtils.saturatedMultiply(this.voltage, this.amps);
      this.gTLCore$updateMachine();
   }

   @Unique
   private void gTLCore$updateEnergyContainer() {
      this.energyContainer.resetBasicInfo(this.maxEnergy, this.voltage, this.amps, 0L, 0L);
      this.energyContainer.setEnergyStored(this.maxEnergy);
   }

   @Unique
   private void gTLCore$updateMachine() {
      this.gTLCore$updateEnergyContainer();
      if (this.getLevel() instanceof ServerLevel serverLevel) {
         serverLevel.m_7654_().execute(() -> {
            for (IMultiController c : this.getControllers()) {
               if (c.isFormed()) {
                  c.getPatternLock().lock();

                  try {
                     c.onStructureInvalid();
                     MultiblockWorldSavedData mwsd = MultiblockWorldSavedData.getOrCreate(serverLevel);
                     mwsd.removeMapping(c.getMultiblockState());
                     mwsd.addAsyncLogic(c);
                  } finally {
                     c.getPatternLock().unlock();
                  }
               }
            }
         });
      }
   }

   @Overwrite(remap = false)
   protected void addEnergy() {
   }

   @Overwrite(remap = false)
   protected void InfinityEnergySubscription() {
   }

   public void onLoad() {
      super.onLoad();
   }

   public void onUnload() {
      super.onUnload();
   }
}
