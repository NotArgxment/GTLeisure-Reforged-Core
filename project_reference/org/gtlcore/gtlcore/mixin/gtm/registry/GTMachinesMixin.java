package org.gtlcore.gtlcore.mixin.gtm.registry;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.GTCEuAPI.RegisterEvent;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.gui.editor.EditableMachineUI;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.api.registry.registrate.CompassSection;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.RotorHolderMachineRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.SimpleGeneratorMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTCompassSections;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;
import com.gregtechceu.gtceu.common.machine.storage.BufferMachine;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.utils.BlockInfo;
import com.tterrag.registrate.util.entry.ItemEntry;
import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModLoader;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.common.data.machines.GCyMMachines;
import org.gtlcore.gtlcore.common.machine.multiblock.generator.GTLLargeCombustionEngineMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.generator.GeneratorArrayMachine;
import org.gtlcore.gtlcore.common.machine.multiblock.generator.MegaTurbineMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTMachines.class)
public abstract class GTMachinesMixin {
   @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
   private static void init(CallbackInfo ci) {
      GCyMMachines.init();
      GTResearchMachines.init();
      ModLoader.get().postEvent(new RegisterEvent(GTRegistries.MACHINES, MachineDefinition.class));
      GTRegistries.MACHINES.freeze();
      ci.cancel();
   }

   @Unique
   private static MachineDefinition[] gTLCore$registerTieredMachines(
      String name,
      BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
      BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
      int... tiers
   ) {
      MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];

      for (int tier : tiers) {
         MachineBuilder<MachineDefinition> register = GTRegistration.REGISTRATE
            .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name, holder -> factory.apply(holder, tier))
            .tier(tier);
         definitions[tier] = builder.apply(tier, register);
      }

      return definitions;
   }

   @Inject(method = "registerTieredMachines", at = @At("HEAD"), remap = false, cancellable = true)
   private static void registerTieredMachines(
      String name,
      BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
      BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
      int[] tiers,
      CallbackInfoReturnable<MachineDefinition[]> cir
   ) {
      switch (name) {
         case "input_bus":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "input_bus",
                  (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.IN, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " Input Bus")
                     .rotationState(RotationState.ALL)
                     .abilities(
                        tier == 0 ? new PartAbility[]{PartAbility.IMPORT_ITEMS, PartAbility.STEAM_IMPORT_ITEMS} : new PartAbility[]{PartAbility.IMPORT_ITEMS}
                     )
                     .overlayTieredHullRenderer("item_bus.import")
                     .tooltips(
                        new Component[]{
                           Component.m_237115_("gtceu.machine.item_bus.import.tooltip"),
                           Component.m_237110_("gtceu.universal.tooltip.item_storage_capacity", new Object[]{(1 + tier) * (1 + tier)})
                        }
                     )
                     .compassNode("item_bus")
                     .register(),
                  GTValues.ALL_TIERS
               )
            );
            break;
         case "output_bus":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "output_bus",
                  (holder, tier) -> new ItemBusPartMachine(holder, tier, IO.OUT, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " Output Bus")
                     .rotationState(RotationState.ALL)
                     .abilities(
                        tier == 0 ? new PartAbility[]{PartAbility.EXPORT_ITEMS, PartAbility.STEAM_EXPORT_ITEMS} : new PartAbility[]{PartAbility.EXPORT_ITEMS}
                     )
                     .overlayTieredHullRenderer("item_bus.export")
                     .tooltips(
                        new Component[]{
                           Component.m_237115_("gtceu.machine.item_bus.export.tooltip"),
                           Component.m_237110_("gtceu.universal.tooltip.item_storage_capacity", new Object[]{(1 + tier) * (1 + tier)})
                        }
                     )
                     .compassNode("item_bus")
                     .register(),
                  GTValues.ALL_TIERS
               )
            );
            break;
         case "energy_input_hatch_4a":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "energy_input_hatch_4a",
                  (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 4, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " 4A Energy Hatch")
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.INPUT_ENERGY})
                     .tooltips(new Component[]{Component.m_237115_("gtceu.machine.energy_hatch.input_hi_amp.tooltip")})
                     .overlayTieredHullRenderer("energy_hatch.input_4a")
                     .compassNode("energy_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "energy_output_hatch_4a":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "energy_output_hatch_4a",
                  (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 4, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " 4A Dynamo Hatch")
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY})
                     .tooltips(new Component[]{Component.m_237115_("gtceu.machine.energy_hatch.output_hi_amp.tooltip")})
                     .overlayTieredHullRenderer("energy_hatch.output_4a")
                     .compassNode("energy_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "energy_input_hatch_16a":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "energy_input_hatch_16a",
                  (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 16, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " 16A Energy Hatch")
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.INPUT_ENERGY})
                     .tooltips(new Component[]{Component.m_237115_("gtceu.machine.energy_hatch.input_hi_amp.tooltip")})
                     .overlayTieredHullRenderer("energy_hatch.input_16a")
                     .compassNode("energy_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "energy_output_hatch_16a":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "energy_output_hatch_16a",
                  (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 16, new Object[0]),
                  (tier, builder1) -> builder1.langValue(GTValues.VNF[tier] + " 16A Dynamo Hatch")
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY})
                     .tooltips(new Component[]{Component.m_237115_("gtceu.machine.energy_hatch.output_hi_amp.tooltip")})
                     .overlayTieredHullRenderer("energy_hatch.output_16a")
                     .compassNode("energy_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "dual_input_hatch":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "dual_input_hatch",
                  (holder, tier) -> new DualHatchPartMachine(holder, tier, IO.IN, new Object[0]),
                  (tier, builder1) -> builder1.langValue("%s Dual Input Hatch".formatted(GTValues.VNF[tier]))
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.IMPORT_ITEMS})
                     .overlayTieredHullRenderer("dual_hatch.import")
                     .tooltips(
                        new Component[]{
                           Component.m_237115_("gtceu.machine.dual_hatch.import.tooltip"),
                           Component.m_237110_("gtceu.universal.tooltip.item_storage_capacity", new Object[]{(1 + tier) * (1 + tier)}),
                           Component.m_237110_(
                              "gtceu.universal.tooltip.fluid_storage_capacity_mult",
                              new Object[]{1 + tier, FluidHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)}
                           ),
                           Component.m_237115_("gtceu.universal.enabled")
                        }
                     )
                     .tooltipBuilder(GTLMachines.GTL_MODIFY)
                     .compassNode("dual_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "dual_output_hatch":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "dual_output_hatch",
                  (holder, tier) -> new DualHatchPartMachine(holder, tier, IO.OUT, new Object[0]),
                  (tier, builder1) -> builder1.langValue("%s Dual Output Hatch".formatted(GTValues.VNF[tier]))
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.EXPORT_ITEMS})
                     .overlayTieredHullRenderer("dual_hatch.export")
                     .tooltips(
                        new Component[]{
                           Component.m_237115_("gtceu.machine.dual_hatch.export.tooltip"),
                           Component.m_237110_("gtceu.universal.tooltip.item_storage_capacity", new Object[]{(1 + tier) * (1 + tier)}),
                           Component.m_237110_(
                              "gtceu.universal.tooltip.fluid_storage_capacity_mult",
                              new Object[]{1 + tier, FluidHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)}
                           ),
                           Component.m_237115_("gtceu.universal.enabled")
                        }
                     )
                     .tooltipBuilder(GTLMachines.GTL_MODIFY)
                     .compassNode("dual_hatch")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "buffer":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "buffer",
                  (x$0, x$1) -> new BufferMachine(x$0, x$1, new Object[0]),
                  (tier, builder1) -> builder1.langValue("%s Buffer %s".formatted(GTValues.VLVH[tier], GTValues.VLVT[tier]))
                     .rotationState(RotationState.NONE)
                     .tieredHullRenderer(GTCEu.id("block/machine/buffer"))
                     .tooltips(
                        new Component[]{
                           Component.m_237115_("gtceu.machine.buffer.tooltip"),
                           Component.m_237110_("gtceu.universal.tooltip.item_storage_capacity", new Object[]{BufferMachine.getInventorySize(tier)}),
                           Component.m_237110_(
                              "gtceu.universal.tooltip.fluid_storage_capacity_mult",
                              new Object[]{
                                 BufferMachine.getTankSize(tier), FluidHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)
                              }
                           )
                        }
                     )
                     .tooltipBuilder(GTLMachines.GTL_MODIFY)
                     .compassNode("buffer")
                     .register(),
                  GTValues.tiersBetween(1, GTCEuAPI.isHighTier() ? 14 : 9)
               )
            );
            break;
         case "rotor_holder":
            cir.setReturnValue(
               gTLCore$registerTieredMachines(
                  "rotor_holder",
                  RotorHolderPartMachine::new,
                  (tier, builder1) -> builder1.langValue("%s Rotor Holder".formatted(GTValues.VNF[tier]))
                     .rotationState(RotationState.ALL)
                     .abilities(new PartAbility[]{PartAbility.ROTOR_HOLDER})
                     .renderer(() -> new RotorHolderMachineRenderer(tier))
                     .tooltips(
                        new Component[]{
                           LangHandler.getFromMultiLang("gtceu.machine.rotor_holder.tooltip", 0),
                           LangHandler.getFromMultiLang("gtceu.machine.rotor_holder.tooltip", 1),
                           Component.m_237115_("gtceu.universal.disabled")
                        }
                     )
                     .compassNode("rotor_holder")
                     .register(),
                  3,
                  4,
                  5,
                  6,
                  7,
                  8,
                  9,
                  10
               )
            );
      }
   }

   @Inject(method = "registerLaserHatch", at = @At("HEAD"), remap = false, cancellable = true)
   private static void registerLaserHatch(IO io, int amperage, PartAbility ability, CallbackInfoReturnable<MachineDefinition[]> cir) {
      String name = io == IO.IN ? "target" : "source";
      cir.setReturnValue(
         gTLCore$registerTieredMachines(
            amperage + "a_laser_" + name + "_hatch",
            (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage),
            (tier, builder) -> builder.langValue(
                  GTValues.VNF[tier] + " " + FormattingUtil.formatNumbers(amperage) + "A Laser " + FormattingUtil.toEnglishName(name) + " Hatch"
               )
               .rotationState(RotationState.ALL)
               .tooltips(
                  new Component[]{
                     Component.m_237115_("gtceu.machine.laser_hatch." + name + ".tooltip"),
                     Component.m_237115_("gtceu.machine.laser_hatch.both.tooltip"),
                     Component.m_237115_("gtceu.universal.disabled")
                  }
               )
               .abilities(new PartAbility[]{ability})
               .overlayTieredHullRenderer("laser_hatch." + name)
               .register(),
            GTValues.tiersBetween(5, GTCEuAPI.isHighTier() ? 14 : 9)
         )
      );
   }

   @Inject(method = "registerSimpleGenerator", at = @At("HEAD"), remap = false, cancellable = true)
   private static void registerSimpleGenerator(
      String name,
      GTRecipeType recipeType,
      Int2LongFunction tankScalingFunction,
      float hazardStrengthPerOperation,
      int[] tiers,
      CallbackInfoReturnable<MachineDefinition[]> cir
   ) {
      cir.setReturnValue(
         gTLCore$registerTieredMachines(
            name,
            (holder, tier) -> new SimpleGeneratorMachine(holder, tier, hazardStrengthPerOperation * tier.intValue(), tankScalingFunction, new Object[0]),
            (tier, builder) -> builder.langValue("%s %s Generator %s".formatted(GTValues.VLVH[tier], FormattingUtil.toEnglishName(name), GTValues.VLVT[tier]))
               .editableUI((EditableMachineUI)SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
               .rotationState(RotationState.ALL)
               .recipeType(recipeType)
               .recipeModifier(SimpleGeneratorMachine::recipeModifier, true)
               .addOutputLimit(ItemRecipeCapability.CAP, 0)
               .addOutputLimit(FluidRecipeCapability.CAP, 0)
               .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTCEu.id("block/generators/" + name)))
               .tooltips(
                  new Component[]{
                     Component.m_237110_("gtceu.machine.efficiency.tooltip", new Object[]{GeneratorArrayMachine.getEfficiency(recipeType, tier)})
                        .m_130946_("%")
                  }
               )
               .tooltips(new Component[]{Component.m_237110_("gtceu.universal.tooltip.ampere_out", new Object[]{GeneratorArrayMachine.getAmperage(tier)})})
               .tooltips(
                  GTMachines.workableTiered(
                     tier,
                     GTValues.V[tier],
                     GTValues.V[tier] * 64L * GeneratorArrayMachine.getAmperage(tier),
                     recipeType,
                     (Long)tankScalingFunction.apply(tier),
                     false
                  )
               )
               .tooltipBuilder(GTLMachines.GTL_MODIFY)
               .compassNode(name)
               .register(),
            tiers
         )
      );
   }

   @Inject(method = "registerLargeCombustionEngine", at = @At("HEAD"), remap = false, cancellable = true)
   private static void registerLargeCombustionEngine(
      String name,
      int tier,
      Supplier<? extends Block> casing,
      Supplier<? extends Block> gear,
      Supplier<? extends Block> intake,
      ResourceLocation casingTexture,
      ResourceLocation overlayModel,
      CallbackInfoReturnable<MultiblockMachineDefinition> cir
   ) {
      cir.setReturnValue(
         GTRegistration.REGISTRATE
            .multiblock(name, holder -> new GTLLargeCombustionEngineMachine(holder, tier))
            .rotationState(RotationState.ALL)
            .recipeType(GTRecipeTypes.COMBUSTION_GENERATOR_FUELS)
            .generator(true)
            .recipeModifier(GTLLargeCombustionEngineMachine::recipeModifier, true)
            .appearanceBlock(casing)
            .pattern(
               definition -> FactoryBlockPattern.start()
                  .aisle(new String[]{"XXX", "XDX", "XXX"})
                  .aisle(new String[]{"XCX", "CGC", "XCX"})
                  .aisle(new String[]{"XCX", "CGC", "XCX"})
                  .aisle(new String[]{"AAA", "AYA", "AAA"})
                  .where('X', Predicates.blocks(new Block[]{casing.get()}))
                  .where('G', Predicates.blocks(new Block[]{gear.get()}))
                  .where(
                     'C',
                     Predicates.blocks(new Block[]{casing.get()})
                        .setMinGlobalLimited(3)
                        .or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true))
                        .or(Predicates.autoAbilities(true, true, false))
                  )
                  .where(
                     'D',
                     Predicates.ability(
                           PartAbility.OUTPUT_ENERGY, Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).filter(t -> t >= tier).mapToInt(Integer::intValue).toArray()
                        )
                        .addTooltips(new Component[]{Component.m_237110_("gtceu.multiblock.pattern.error.limited.1", new Object[]{GTValues.VN[tier]})})
                  )
                  .where(
                     'A',
                     Predicates.blocks(new Block[]{intake.get()}).addTooltips(new Component[]{Component.m_237115_("gtceu.multiblock.pattern.clear_amount_1")})
                  )
                  .where('Y', Predicates.controller(Predicates.blocks(new Block[]{definition.getBlock()})))
                  .build()
            )
            .recoveryItems(() -> new ItemLike[]{(ItemLike)((ItemEntry)GTItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash)).get()})
            .workableCasingRenderer(casingTexture, overlayModel)
            .tooltips(
               new Component[]{
                  Component.m_237110_("gtceu.universal.tooltip.base_production_eut", new Object[]{GTValues.V[tier] * 4L}),
                  Component.m_237110_("gtceu.universal.tooltip.uses_per_hour_lubricant", new Object[]{FluidHelper.getBucket()}),
                  tier > 4
                     ? Component.m_237110_("gtceu.machine.large_combustion_engine.tooltip.boost_extreme", new Object[]{GTValues.V[tier] * 16L})
                     : Component.m_237110_("gtceu.machine.large_combustion_engine.tooltip.boost_regular", new Object[]{GTValues.V[tier] * 12L})
               }
            )
            .tooltipBuilder(GTLMachines.GTL_MODIFY)
            .compassSections(new CompassSection[]{GTCompassSections.TIER[4]})
            .compassNode("large_combustion")
            .register()
      );
   }

   @Inject(method = "registerLargeTurbine", at = @At("HEAD"), remap = false, cancellable = true)
   private static void registerLargeTurbine(
      String name,
      int tier,
      GTRecipeType recipeType,
      Supplier<? extends Block> casing,
      Supplier<? extends Block> gear,
      ResourceLocation casingTexture,
      ResourceLocation overlayModel,
      CallbackInfoReturnable<MultiblockMachineDefinition> cir
   ) {
      int finalAm = switch (name) {
         case "rocket_large_turbine" -> 12;
         case "supercritical_steam_turbine" -> 16;
         default -> 8;
      };
      cir.setReturnValue(
         GTRegistration.REGISTRATE
            .multiblock(name, holder -> new MegaTurbineMachine(holder, tier, finalAm))
            .rotationState(RotationState.ALL)
            .recipeType(recipeType)
            .generator(true)
            .recipeModifier(MegaTurbineMachine::recipeModifier, true)
            .appearanceBlock(casing)
            .pattern(
               definition -> FactoryBlockPattern.start()
                  .aisle(new String[]{"CCCC", "CHHC", "CCCC"})
                  .aisle(new String[]{"CHHC", "RGGR", "CHHC"})
                  .aisle(new String[]{"CCCC", "CSHC", "CCCC"})
                  .where('S', Predicates.controller(Predicates.blocks(new Block[]{definition.getBlock()})))
                  .where('G', Predicates.blocks(new Block[]{gear.get()}))
                  .where('C', Predicates.blocks(new Block[]{casing.get()}))
                  .where(
                     'R',
                     new TraceabilityPredicate(
                           new SimplePredicate(
                              state -> MetaMachine.getMachine(state.getWorld(), state.getPos()) instanceof IRotorHolderMachine rotorHolder
                                 && state.getWorld().m_8055_(state.getPos().m_121945_(rotorHolder.self().getFrontFacing())).m_60795_(),
                              () -> PartAbility.ROTOR_HOLDER.getAllBlocks().stream().map(BlockInfo::fromBlock).toArray(BlockInfo[]::new)
                           )
                        )
                        .addTooltips(new Component[]{Component.m_237115_("gtceu.multiblock.pattern.clear_amount_3")})
                        .addTooltips(new Component[]{Component.m_237110_("gtceu.multiblock.pattern.error.limited.1", new Object[]{GTValues.VN[tier]})})
                        .setExactLimit(1)
                        .or(Predicates.abilities(new PartAbility[]{PartAbility.OUTPUT_ENERGY}))
                        .setExactLimit(1)
                  )
                  .where(
                     'H',
                     Predicates.blocks(new Block[]{casing.get()})
                        .or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true))
                        .or(Predicates.autoAbilities(true, true, false))
                  )
                  .build()
            )
            .recoveryItems(() -> new ItemLike[]{(ItemLike)((ItemEntry)GTItems.MATERIAL_ITEMS.get(TagPrefix.dustTiny, GTMaterials.Ash)).get()})
            .workableCasingRenderer(casingTexture, overlayModel)
            .tooltips(
               new Component[]{
                  Component.m_237110_("gtceu.universal.tooltip.base_production_eut", new Object[]{GTValues.V[tier] * finalAm}),
                  Component.m_237110_("gtceu.multiblock.turbine.efficiency_tooltip", new Object[]{GTValues.VNF[tier]})
               }
            )
            .tooltipBuilder(GTLMachines.GTL_MODIFY)
            .compassSections(new CompassSection[]{GTCompassSections.TIER[3]})
            .compassNode("large_turbine")
            .register()
      );
   }
}
