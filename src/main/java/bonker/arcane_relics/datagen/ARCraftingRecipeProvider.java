package bonker.arcane_relics.datagen;

import bonker.arcane_relics.common.item.ARItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class ARCraftingRecipeProvider extends RecipeProvider {
    public ARCraftingRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // azur crystal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ARItems.AZUR_CRYSTAL.get(), 4)
                .define('d', Items.DIAMOND).define('l', Items.LAPIS_LAZULI).define('a', Items.AMETHYST_SHARD)
                .pattern("ala")
                .pattern("ldl")
                .pattern("ala")
                .unlockedBy("has_azur_ingredients", item(Items.AMETHYST_SHARD, Items.LAPIS_LAZULI, Items.DIAMOND)).save(pWriter);

        // undead crystal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ARItems.UNDEAD_CRYSTAL.get(), 1)
                .define('b', ARItems.SKELETON_SKULL.get()).define('r', ARItems.ZOMBIE_HAND.get()).define('a', ARItems.AZUR_CRYSTAL.get())
                .pattern("brb")
                .pattern("rar")
                .pattern("brb")
                .unlockedBy("has_azur", item(ARItems.AZUR_CRYSTAL.get())).save(pWriter);

        // copper nugget
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ARItems.COPPER_NUGGET.get(), 9)
                .requires(Items.COPPER_INGOT, 1)
                .unlockedBy("has_copper", item(Items.COPPER_INGOT)).save(pWriter);

        // bronze ingot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ARItems.BRONZE_INGOT.get(), 1)
                .requires(ARItems.COPPER_NUGGET.get(), 5)
                .requires(Items.IRON_NUGGET, 4)
                .unlockedBy("has_iron_or_copper", item(Items.IRON_INGOT, Items.COPPER_INGOT)).save(pWriter);

        // undead ingot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ARItems.UNDEAD_INGOT.get(), 2)
                .requires(ARItems.CHARGED_UNDEAD_CRYSTAL.get())
                .requires(ARItems.BRONZE_INGOT.get(), 2)
                .unlockedBy("has_charged_undead_crystal", item(ARItems.CHARGED_UNDEAD_CRYSTAL.get())).save(pWriter);

        // bone handle
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ARItems.BONE_HANDLE.get(), 1)
                .define('/', Items.BONE)
                .pattern("  /")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy("has_bone", item(Items.BONE)).save(pWriter);

        // undead sword
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ARItems.UNDEAD_SWORD.get(), 1)
                .define('/', ARItems.BONE_HANDLE.get()).define('i', ARItems.UNDEAD_INGOT.get())
                .pattern("  i")
                .pattern(" i ")
                .pattern("/  ")
                .unlockedBy("has_undead_sword_ingredients", item(ARItems.UNDEAD_INGOT.get(), ARItems.BONE_HANDLE.get())).save(pWriter);


    }

    private static InventoryChangeTrigger.TriggerInstance item(ItemLike... items) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.item();
        Stream.of(items).forEach(builder::of);
        return inventoryTrigger(builder.build());
    }
}
