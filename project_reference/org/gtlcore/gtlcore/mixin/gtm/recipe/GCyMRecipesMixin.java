package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GCyMBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.misc.GCyMRecipes;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GCyMRecipes.class)
public abstract class GCyMRecipesMixin {
   private GCyMRecipesMixin() {
   }

   @Overwrite(remap = false)
   private static void registerMultiblockControllerRecipes(Consumer<FinishedRecipe> provider) {
   }

   @Overwrite(remap = false)
   private static void registerPartsRecipes(Consumer<FinishedRecipe> provider) {
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "crushing_wheels",
         GCyMBlocks.CRUSHING_WHEELS.asStack(2),
         new Object[]{
            "TTT",
            "UCU",
            "UMU",
            'T',
            new UnificationEntry(TagPrefix.gearSmall, GTMaterials.TungstenCarbide),
            'U',
            ChemicalHelper.get(TagPrefix.gear, GTMaterials.Ultimet),
            'C',
            GCyMBlocks.CASING_SECURE_MACERATION.asStack(),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "slicing_blades",
         GCyMBlocks.SLICING_BLADES.asStack(2),
         new Object[]{
            "PPP",
            "UCU",
            "UMU",
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.TungstenCarbide),
            'U',
            ChemicalHelper.get(TagPrefix.gear, GTMaterials.Ultimet),
            'C',
            GCyMBlocks.CASING_SHOCK_PROOF.asStack(),
            'M',
            GTItems.ELECTRIC_MOTOR_IV.asStack()
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "electrolytic_cell",
         GCyMBlocks.ELECTROLYTIC_CELL.asStack(2),
         new Object[]{
            "WWW",
            "WCW",
            "ZKZ",
            'W',
            new UnificationEntry(TagPrefix.wireGtDouble, GTMaterials.Platinum),
            'Z',
            CustomTags.IV_CIRCUITS,
            'C',
            GCyMBlocks.CASING_NONCONDUCTING.asStack(),
            'K',
            ChemicalHelper.get(TagPrefix.cableGtSingle, GTMaterials.Tungsten)
         }
      );
      VanillaRecipeHelper.addShapedRecipe(
         provider,
         "heat_vent",
         GCyMBlocks.HEAT_VENT.asStack(2),
         new Object[]{
            "PDP",
            "RLR",
            "PDP",
            'P',
            new UnificationEntry(TagPrefix.plate, GTMaterials.TantalumCarbide),
            'D',
            ChemicalHelper.get(TagPrefix.plateDouble, GTMaterials.MolybdenumDisilicide),
            'R',
            ChemicalHelper.get(TagPrefix.rotor, GTMaterials.Titanium),
            'L',
            ChemicalHelper.get(TagPrefix.rodLong, GTMaterials.MolybdenumDisilicide)
         }
      );
   }
}
