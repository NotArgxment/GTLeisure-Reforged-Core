package org.gtlreforged;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@GTAddon
public class GTLAddon implements IGTAddon {

    @Override
    public GTRegistrate getRegistrate() {
        return GTLeisureReforged.GTLRegistry;
    }

    @Override
    public void initializeAddon() {
        // This thing will suck a LOT
        // take in mind its part of a GTO/GTNH ripoff modpack
        // that still uses GTCEu 1.4.4 💀
        // and from what i could read the main dev has no plans
        // to update, so im here to atleast try to do that
    }

    @Override
    public String addonModId() {
        return GTLeisureReforged.MOD_ID;
    }

    @Override
    public void registerTagPrefixes() {
        // CustomTagPrefixes.init();
    }

    @Override
    public void registerElements() {
        // CustomElements.init();
    }

}
