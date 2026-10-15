package org.gtlcore.gtlcore.api.recipe.ingredient;

import com.google.common.primitives.Ints;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntCircuitIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.NBTIngredient;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.core.mixins.IngredientAccessor;
import com.gregtechceu.gtceu.core.mixins.ItemValueAccessor;
import com.gregtechceu.gtceu.core.mixins.TagValueAccessor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Ingredient.Value;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.gtlcore.gtlcore.mixin.gtm.recipe.Ingredient.IntProviderIngredientAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LongIngredient extends SizedIngredient {
   public static final ResourceLocation TYPE = GTCEu.id("Long");
   protected long actualAmount;
   private int hashCode = 0;
   private int innerHashCode = 0;
   private boolean changed = true;
   private final boolean isEmpty;
   private final Value value;
   public static final IIngredientSerializer<LongIngredient> SERIALIZER = new IIngredientSerializer<LongIngredient>() {
      @NotNull
      public LongIngredient parse(FriendlyByteBuf buffer) {
         long amount = buffer.m_130258_();
         return new LongIngredient(Ingredient.m_43940_(buffer), amount);
      }

      @NotNull
      public LongIngredient parse(JsonObject json) {
         long amount = json.get("actualAmount").getAsLong();
         Ingredient inner = Ingredient.m_43917_(json.get("ingredient"));
         return new LongIngredient(inner, amount);
      }

      public void write(FriendlyByteBuf buffer, LongIngredient ingredient) {
         buffer.m_130103_(ingredient.getActualAmount());
         ingredient.inner.m_43923_(buffer);
      }
   };

   protected LongIngredient(Ingredient inner, long actualAmount) {
      super(inner, Ints.saturatedCast(actualAmount));
      this.actualAmount = actualAmount;
      this.isEmpty = inner.m_43947_();
      if (!this.isEmpty && inner.getClass() == Ingredient.class) {
         Value[] values = ((IngredientAccessor)inner).getValues();
         this.value = values.length == 1 ? values[0] : null;
      } else {
         this.value = null;
      }
   }

   protected LongIngredient(@NotNull TagKey<Item> tag, long actualAmount) {
      this(Ingredient.m_204132_(tag), actualAmount);
   }

   protected LongIngredient(ItemStack itemStack, long actualAmount) {
      this(itemStack.m_41782_() ? NBTIngredient.createNBTIngredient(itemStack) : Ingredient.m_43927_(new ItemStack[]{itemStack}), actualAmount);
   }

   public static LongIngredient create(Ingredient inner, long amount) {
      return new LongIngredient(inner, amount);
   }

   public static LongIngredient create(Ingredient inner) {
      return new LongIngredient(inner, 1L);
   }

   public static LongIngredient create(ItemStack inner) {
      return new LongIngredient(inner, 1L);
   }

   public static LongIngredient create(TagKey<Item> tag, long amount) {
      return new LongIngredient(tag, amount);
   }

   public static Ingredient copy(Ingredient ingredient) {
      if (ingredient instanceof LongIngredient longIngredient) {
         LongIngredient copy = create(longIngredient.inner, longIngredient.actualAmount);
         copy.hashCode = longIngredient.hashCode;
         return copy;
      } else if (ingredient instanceof SizedIngredient sizedIngredient) {
         return (Ingredient)(sizedIngredient.getInner() instanceof IntProviderIngredient intProviderIngredient
            ? copy(intProviderIngredient)
            : create(sizedIngredient.getInner(), sizedIngredient.getAmount()));
      } else if (ingredient instanceof IntCircuitIngredient circuit) {
         return circuit.copy();
      } else if (ingredient instanceof IntProviderIngredient intProviderIngredient) {
         IntProviderIngredient copied = new IntProviderIngredient(intProviderIngredient.getInner(), intProviderIngredient.getCountProvider());
         IntProviderIngredientAccessor accessor = (IntProviderIngredientAccessor)intProviderIngredient;
         if (accessor.getItemStack() != null) {
            copied.setItemStacks(Arrays.stream(accessor.getItemStack()).map(ItemStack::m_41777_).toArray(ItemStack[]::new));
         }

         if (accessor.getSampledCount() != -1) {
            copied.setSampledCount(accessor.getSampledCount());
         }

         return copied;
      } else {
         return create(ingredient);
      }
   }

   @NotNull
   public IIngredientSerializer<? extends Ingredient> getSerializer() {
      return SERIALIZER;
   }

   public static SizedIngredient fromJson(JsonObject json) {
      return (SizedIngredient)SERIALIZER.parse(json);
   }

   @NotNull
   public JsonElement m_43942_() {
      JsonObject json = new JsonObject();
      json.addProperty("type", TYPE.toString());
      json.addProperty("actualAmount", this.actualAmount);
      json.add("ingredient", this.inner.m_43942_());
      return json;
   }

   public boolean test(@Nullable ItemStack stack) {
      if (stack == null) {
         return false;
      } else if (this.isEmpty) {
         return stack.m_41619_();
      } else if (this.value instanceof TagValueAccessor tagValue) {
         return stack.m_204117_(tagValue.getTag());
      } else {
         return this.value instanceof ItemValueAccessor itemValue ? ItemStack.m_41656_(stack, itemValue.getItem()) : this.inner.test(stack);
      }
   }

   public ItemStack @NotNull [] m_43908_() {
      if (this.getInner() instanceof IntProviderIngredient intProviderIngredient) {
         return intProviderIngredient.m_43908_();
      } else {
         if (this.changed || this.itemStacks == null) {
            if (this.isEmpty) {
               return new ItemStack[0];
            }

            ObjectArrayList<ItemStack> items = new ObjectArrayList(this.inner.m_43908_().length);

            for (ItemStack item : this.inner.m_43908_()) {
               items.add(item.m_255036_(Ints.saturatedCast(this.actualAmount)));
            }

            this.itemStacks = (ItemStack[])items.toArray(new ItemStack[0]);
            this.changed = false;
         }

         return this.itemStacks;
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = Objects.hash(this.actualAmount, Objects.hashCode(this.inner));
      }

      return this.hashCode;
   }

   public int getInnerHashCode() {
      if (this.innerHashCode == 0) {
         int hash = 0;

         for (Value v : ((IngredientAccessor)this.inner).getValues()) {
            if (v instanceof TagValueAccessor tag) {
               hash += tag.getTag().hashCode();
            } else if (v instanceof ItemValueAccessor item) {
               ItemStack i = item.getItem();
               if (!i.m_41619_()) {
                  hash += i.m_41720_().hashCode() + (i.m_41783_() != null ? i.m_41783_().hashCode() : 0);
               }
            }
         }

         this.innerHashCode = hash;
      }

      return this.innerHashCode;
   }

   public void setActualAmount(long actualAmount) {
      this.actualAmount = actualAmount;
      this.changed = true;
   }

   public long getActualAmount() {
      return this.actualAmount;
   }

   public boolean m_43947_() {
      return this.isEmpty;
   }
}
