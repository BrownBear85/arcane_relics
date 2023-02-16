package bonker.arcane_relics.common.recipe;

import bonker.arcane_relics.ArcaneRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ARRecipes {
    public static class RecipeTypes {
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, ArcaneRelics.MODID);


        public static final RegistryObject<RecipeType<EvilSkullRecipe>> EVIL_SKULL_TYPE = RECIPE_TYPES.register("evil_skull",
                () -> RecipeType.simple(new ResourceLocation(ArcaneRelics.MODID, "evil_skull")));
    }

    public static class RecipeSerializers {
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ArcaneRelics.MODID);


        public static final RegistryObject<RecipeSerializer<EvilSkullRecipe>> EVIL_SKULL_SERIALIZER = RECIPE_SERIALIZERS.register("evil_skull",
                EvilSkullRecipeSerializer::new);
    }
}
