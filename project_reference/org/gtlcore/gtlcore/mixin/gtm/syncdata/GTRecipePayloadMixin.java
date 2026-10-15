package org.gtlcore.gtlcore.mixin.gtm.syncdata;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeSerializer;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.syncdata.GTRecipePayload;
import com.lowdragmc.lowdraglib.syncdata.payload.ObjectTypedPayload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.gtlcore.gtlcore.api.recipe.IGTRecipe;
import org.gtlcore.gtlcore.utils.Registries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GTRecipePayload.class)
public abstract class GTRecipePayloadMixin extends ObjectTypedPayload<GTRecipe> {
   @Nullable
   public Tag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.m_128359_("id", ((GTRecipe)this.payload).id.toString());
      tag.m_128365_("recipe", (Tag)GTRecipeSerializer.CODEC.encodeStart(NbtOps.f_128958_, (GTRecipe)this.payload).result().orElse(new CompoundTag()));
      tag.m_128356_("realParallels", IGTRecipe.of((GTRecipe)this.payload).getRealParallels());
      tag.m_128405_("ocTier", ((GTRecipe)this.payload).ocTier);
      return tag;
   }

   public void deserializeNBT(Tag tag) {
      if (tag instanceof CompoundTag compoundTag) {
         this.payload = GTRecipeSerializer.CODEC.parse(NbtOps.f_128958_, compoundTag.m_128423_("recipe")).result().orElse(null);
         if (this.payload != null) {
            ((GTRecipe)this.payload).id = new ResourceLocation(compoundTag.m_128461_("id"));
            IGTRecipe.of((GTRecipe)this.payload).setRealParallels(compoundTag.m_128441_("realParallels") ? compoundTag.m_128454_("realParallels") : 1L);
            ((GTRecipe)this.payload).ocTier = compoundTag.m_128451_("ocTier");
         }
      } else if (tag instanceof StringTag stringTag) {
         Recipe<?> recipe = (Recipe<?>)Registries.getRecipeManager().m_44043_(new ResourceLocation(stringTag.m_7916_())).orElse(null);
         if (recipe instanceof GTRecipe gtRecipe) {
            this.payload = gtRecipe;
         } else if (recipe instanceof SmeltingRecipe smeltingRecipe) {
            this.payload = GTRecipeTypes.FURNACE_RECIPES.toGTrecipe(new ResourceLocation(stringTag.m_7916_()), smeltingRecipe);
         } else {
            this.payload = null;
         }
      } else if (tag instanceof ByteArrayTag byteArray) {
         ByteBuf copiedDataBuffer = Unpooled.copiedBuffer(byteArray.m_128227_());
         FriendlyByteBuf buf = new FriendlyByteBuf(copiedDataBuffer);
         this.payload = (GTRecipe)Registries.getRecipeManager().m_44043_(buf.m_130281_()).orElse(null);
         buf.release();
      }
   }

   public void writePayload(FriendlyByteBuf buf) {
      buf.m_130085_(((GTRecipe)this.payload).id);
      GTRecipeSerializer.SERIALIZER.toNetwork(buf, (GTRecipe)this.payload);
      buf.writeLong(IGTRecipe.of((GTRecipe)this.payload).getRealParallels());
      buf.writeInt(((GTRecipe)this.payload).ocTier);
   }

   public void readPayload(FriendlyByteBuf buf) {
      ResourceLocation id = buf.m_130281_();
      if (buf.isReadable()) {
         this.payload = GTRecipeSerializer.SERIALIZER.fromNetwork(id, buf);
         if (buf.isReadable()) {
            IGTRecipe.of((GTRecipe)this.payload).setRealParallels(buf.readLong());
            ((GTRecipe)this.payload).ocTier = buf.readInt();
         }
      } else {
         RecipeManager recipeManager = Registries.getRecipeManager();
         this.payload = (GTRecipe)recipeManager.m_44043_(id).orElse(null);
      }
   }
}
