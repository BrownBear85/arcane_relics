package bonker.arcane_relics.datagen;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.item.ARItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class ARItemModelProvider extends ItemModelProvider {

    public ARItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneRelics.MODID, existingFileHelper);
    }

    private final Collection<RegistryObject<Item>> items = ARItems.ITEMS.getEntries();

    @Override
    protected void registerModels() {
        basicItem("allay_milk");
        basicItem("allay_cheese");
        basicItem("mystic_steel_ingot");
        largeHandheld("undead_sword");
        basicItem("undead_crystal");
        basicItem("charged_undead_crystal");
        basicItem("azur_crystal");
        basicItem("undead_ingot");
        basicItem("bronze_ingot");
        basicItem("copper_nugget");
        largeHandheld("bone_handle");
        basicItem("skeleton_skull");
        basicItem("netherrack_dust");
        basicItem("fiery_powder");
        basicItem("evil_skull");
        basicItem("fiery_skull");
        handheld("bronze_hammer");
    }

    private ItemModelBuilder basicItem(String itemId) {
        return basicItem(new ResourceLocation(ArcaneRelics.MODID, itemId));
    }

    private ItemModelBuilder handheld(String itemId) {
        return getBuilder(itemId)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", new ResourceLocation(modid, "item/" + itemId));
    }

    private ItemModelBuilder largeHandheld(String itemId) {
        return getBuilder(itemId)
                .parent(new ModelFile.UncheckedModelFile(new ResourceLocation(modid, "item/large_handheld")))
                .texture("layer0", new ResourceLocation(modid, "item/" + itemId));
    }
}
