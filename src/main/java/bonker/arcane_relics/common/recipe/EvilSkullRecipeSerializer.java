package bonker.arcane_relics.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class EvilSkullRecipeSerializer implements RecipeSerializer<EvilSkullRecipe> {

    @Override
    public EvilSkullRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
        ItemStack output = CraftingHelper.getItemStack(pSerializedRecipe.getAsJsonObject("output"), false);
        return new EvilSkullRecipe(pRecipeId, input, output);
    }

    @Override
    public @Nullable EvilSkullRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        Ingredient input = Ingredient.fromNetwork(pBuffer);
        ItemStack output = pBuffer.readItem();
        return new EvilSkullRecipe(pRecipeId, input, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, EvilSkullRecipe pRecipe) {
        pRecipe.input().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.output());
    }
}
