package org.gtlcore.gtlcore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlag;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.DustProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IngotProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.data.recipe.generated.MaterialRecipeHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import org.gtlcore.gtlcore.common.data.GTLRecipeTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MaterialRecipeHandler.class)
public abstract class MaterialRecipeHandlerMixin {
   @Shadow(remap = false)
   private static void processEBFRecipe(Material material, BlastProperty property, ItemStack output, Consumer<FinishedRecipe> provider) {
   }

   @Overwrite(remap = false)
   public static void processDust(TagPrefix dustPrefix, Material mat, DustProperty property, Consumer<FinishedRecipe> provider) {
      String id = "%s_%s_".formatted(FormattingUtil.toLowerCaseUnder(dustPrefix.name), mat.getName().toLowerCase(Locale.ROOT));
      ItemStack dustStack = ChemicalHelper.get(dustPrefix, mat);
      OreProperty oreProperty = mat.hasProperty(PropertyKey.ORE) ? (OreProperty)mat.getProperty(PropertyKey.ORE) : null;
      if (mat.hasProperty(PropertyKey.GEM)) {
         ItemStack gemStack = ChemicalHelper.get(TagPrefix.gem, mat);
         if (mat.hasFlag(MaterialFlags.CRYSTALLIZABLE)) {
            GTRecipeTypes.AUTOCLAVE_RECIPES
               .recipeBuilder("autoclave_" + id + "_water", new Object[0])
               .inputItems(dustStack)
               .inputFluids(GTMaterials.Water.getFluid(250L))
               .chancedOutput(gemStack, 7000, 1000)
               .duration(1200)
               .EUt(24L)
               .save(provider);
            GTRecipeTypes.AUTOCLAVE_RECIPES
               .recipeBuilder("autoclave_" + id + "_distilled", new Object[0])
               .inputItems(dustStack)
               .inputFluids(GTMaterials.DistilledWater.getFluid(50L))
               .outputItems(gemStack)
               .duration(600)
               .EUt(24L)
               .save(provider);
         }

         if (!mat.hasFlag(MaterialFlags.EXPLOSIVE) && !mat.hasFlag(MaterialFlags.FLAMMABLE)) {
            GTRecipeTypes.IMPLOSION_RECIPES
               .recipeBuilder("implode_" + id + "_powderbarrel", new Object[0])
               .inputItems(GTUtil.copyAmount(4, new ItemStack[]{dustStack}))
               .outputItems(GTUtil.copyAmount(3, new ItemStack[]{gemStack}))
               .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
               .explosivesType(new ItemStack(GTBlocks.POWDERBARREL, 8))
               .save(provider);
            GTRecipeTypes.IMPLOSION_RECIPES
               .recipeBuilder("implode_" + id + "_tnt", new Object[0])
               .inputItems(GTUtil.copyAmount(4, new ItemStack[]{dustStack}))
               .outputItems(GTUtil.copyAmount(3, new ItemStack[]{gemStack}))
               .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
               .explosivesAmount(4)
               .save(provider);
            GTRecipeTypes.IMPLOSION_RECIPES
               .recipeBuilder("implode_" + id + "_dynamite", new Object[0])
               .inputItems(GTUtil.copyAmount(4, new ItemStack[]{dustStack}))
               .outputItems(GTUtil.copyAmount(3, new ItemStack[]{gemStack}))
               .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
               .explosivesType(GTItems.DYNAMITE.asStack(2))
               .save(provider);
            GTRecipeTypes.IMPLOSION_RECIPES
               .recipeBuilder("implode_" + id + "_itnt", new Object[0])
               .inputItems(GTUtil.copyAmount(4, new ItemStack[]{dustStack}))
               .outputItems(GTUtil.copyAmount(3, new ItemStack[]{gemStack}))
               .chancedOutput(TagPrefix.dust, GTMaterials.DarkAsh, 2500, 0)
               .explosivesType(new ItemStack(GTBlocks.INDUSTRIAL_TNT))
               .save(provider);
            GTLRecipeTypes.ELECTRIC_IMPLOSION_COMPRESSOR_RECIPES
               .recipeBuilder("electric_implode_" + id, new Object[0])
               .inputItems(GTUtil.copyAmount(4, new ItemStack[]{dustStack}))
               .outputItems(GTUtil.copyAmount(3, new ItemStack[]{gemStack}))
               .save(provider);
         }

         if (oreProperty != null) {
            Material smeltingResult = oreProperty.getDirectSmeltResult();
            if (smeltingResult != null) {
               VanillaRecipeHelper.addSmeltingRecipe(
                  provider, id + "_ingot", ChemicalHelper.getTag(dustPrefix, mat), ChemicalHelper.get(TagPrefix.ingot, smeltingResult)
               );
            }
         }
      } else if (mat.hasProperty(PropertyKey.INGOT)) {
         if (!mat.hasAnyOfFlags(new MaterialFlag[]{MaterialFlags.FLAMMABLE, MaterialFlags.NO_SMELTING})) {
            boolean hasHotIngot = TagPrefix.ingotHot.doGenerateItem(mat);
            ItemStack ingotStack = ChemicalHelper.get(hasHotIngot ? TagPrefix.ingotHot : TagPrefix.ingot, mat);
            if (ingotStack.m_41619_() && oreProperty != null) {
               Material smeltingResult = oreProperty.getDirectSmeltResult();
               if (smeltingResult != null) {
                  ingotStack = ChemicalHelper.get(TagPrefix.ingot, smeltingResult);
               }
            }

            int blastTemp = mat.getBlastTemperature();
            if (blastTemp <= 0) {
               if (!mat.hasFlag(MaterialFlags.IS_MAGNETIC)) {
                  VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_demagnetize_from_dust", ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
               }
            } else {
               IngotProperty ingotProperty = (IngotProperty)mat.getProperty(PropertyKey.INGOT);
               BlastProperty blastProperty = (BlastProperty)mat.getProperty(PropertyKey.BLAST);
               processEBFRecipe(mat, blastProperty, ingotStack, provider);
               if (ingotProperty.getMagneticMaterial() != null) {
                  processEBFRecipe(ingotProperty.getMagneticMaterial(), blastProperty, ingotStack, provider);
               }
            }
         }
      } else {
         if (mat.hasFlag(MaterialFlags.GENERATE_PLATE) && !mat.hasFlag(MaterialFlags.EXCLUDE_PLATE_COMPRESSOR_RECIPE)) {
            GTRecipeTypes.COMPRESSOR_RECIPES
               .recipeBuilder("compress_plate_" + id, new Object[0])
               .inputItems(dustStack)
               .outputItems(TagPrefix.plate, mat)
               .save(provider);
         }

         if (oreProperty != null) {
            Material smeltingResult = oreProperty.getDirectSmeltResult();
            if (smeltingResult != null) {
               ItemStack ingotStack = ChemicalHelper.get(TagPrefix.ingot, smeltingResult);
               if (!ingotStack.m_41619_()) {
                  VanillaRecipeHelper.addSmeltingRecipe(provider, id + "_dust_to_ingot", ChemicalHelper.getTag(dustPrefix, mat), ingotStack);
               }
            }
         }
      }
   }
}
