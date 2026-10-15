package org.gtlcore.gtlcore.common;

import appeng.api.storage.StorageCells;
import appeng.core.AELog;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTCEuAPI.RegisterEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.gtlcore.gtlcore.api.registries.GTLRegistration;
import org.gtlcore.gtlcore.common.data.*;
import org.gtlcore.gtlcore.common.data.machines.GeneratorMachine;
import org.gtlcore.gtlcore.config.ConfigHolder;
import org.gtlcore.gtlcore.integration.ae2.InfinityCellGuiHandler;
import org.gtlcore.gtlcore.integration.ae2.storage.FastInfinityCellHandler;
import org.gtlcore.gtlcore.integration.ae2.storage.InfinityCellHandler;
import org.gtlcore.gtlcore.integration.ftbu.AreaShape;
import org.gtlcore.gtlcore.network.GTLNetworkHandler;

public class CommonProxy {
    public CommonProxy() {
        init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GTLRegistration.REGISTRATE.registerEventListeners(eventBus);
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::addMaterialRegistries);
        eventBus.addListener(this::addMaterials);
        eventBus.addListener(this::modifyMaterials);
        eventBus.addGenericListener(RecipeConditionType.class, this::registerRecipeConditions);
        eventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        eventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
    }

    public static void init() {
        GTLCreativeModeTabs.init();
        ConfigHolder.init();
        AreaShape.register();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        StorageCells.addCellHandler(InfinityCellHandler.INSTANCE);
        StorageCells.addCellHandler(FastInfinityCellHandler.INSTANCE);
        StorageCells.addCellGuiHandler(new InfinityCellGuiHandler());
        event.enqueueWork(this::postRegistrationInitialization).whenComplete((res, err) -> {
            if (err != null) {
                AELog.warn(err);
            }
        });
    }

    public void postRegistrationInitialization() {
        GTLItems.InitUpgrades();
        GTLNetworkHandler.INSTANCE.init();
    }

    protected void clientSetup(FMLClientSetupEvent event) {
    }

    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry("gtlcore");
    }

    private void addMaterials(MaterialEvent event) {
        GTLMaterials.init();
    }

    private void modifyMaterials(PostMaterialEvent event) {
    }

    private void registerRecipeTypes(RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GTLRecipeTypes.init();
    }

    private void registerRecipeConditions(RegisterEvent<ResourceLocation, RecipeConditionType<?>> event) {
        GTLRecipeConditions.init();
    }

    private void registerMachines(RegisterEvent<ResourceLocation, MachineDefinition> event) {
        GTLMachines.init();
        GeneratorMachine.LARGE_SEMI_FLUID_GENERATOR.setRecipeTypes(new GTRecipeType[]{GTLRecipeTypes.SEMI_FLUID_GENERATOR_FUELS});
    }
}
