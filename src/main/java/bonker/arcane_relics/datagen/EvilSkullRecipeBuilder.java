package bonker.arcane_relics.datagen;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.recipe.ARRecipes;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class EvilSkullRecipeBuilder {

    private ResourceLocation id;
    private Ingredient input;
    private ItemStack output;

    public static EvilSkullRecipeBuilder name(String name) {
        return new EvilSkullRecipeBuilder(new ResourceLocation(ArcaneRelics.MODID, name));
    }

    public EvilSkullRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public EvilSkullRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public void save(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        finishedRecipeConsumer.accept(new EvilSkullFinishedRecipe(id, input, output));
    }

    private EvilSkullRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public static class EvilSkullFinishedRecipe implements FinishedRecipe {

        private ResourceLocation id;
        private Ingredient input;
        private ItemStack output;

        private EvilSkullFinishedRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
            this.id = id;
            this.input = input;
            this.output = output;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("input", input.toJson());
            pJson.add("output", Util.ITEM_STACK_CODEC.encodeStart(JsonOps.INSTANCE, output).getOrThrow(false, ArcaneRelics.LOGGER::error));
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ARRecipes.RecipeSerializers.EVIL_SKULL_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
