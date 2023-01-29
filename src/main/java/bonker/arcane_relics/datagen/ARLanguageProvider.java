package bonker.arcane_relics.datagen;

import bonker.arcane_relics.common.item.ARItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class ARLanguageProvider extends LanguageProvider {
    public ARLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("item_group.arcane_relics.tab", "Arcane Relics");

        add("subtitles.arcane_relics.rumble", "Something Rumbles");
        add("subtitles.arcane_relics.undead_cast", "Spell cast");
        add("subtitles.arcane_relics.monster_disappear", "Undead creature returns");

        add("item.arcane_relics.undead_sword.fail", "Can't summon here");
        add("item.arcane_relics.undead_crystal.tooltip", "Kill undead mobs:");
        add("item.arcane_relics.charged_undead_crystal.tooltip", "Goal Complete: 30 / 30");
        add("item.arcane_relics.zombie_hand.tooltip", "A rare Zombie drop");
        add("item.arcane_relics.skeleton_skull.tooltip", "A rare Skeleton drop");

        for (RegistryObject<Item> item : ARItems.ITEMS.getEntries()) {
            addItem(item, idToName(item.get().toString()));
        }
    }

    public static String idToName(String id) {
        String[] arr = id.replace("_", " ").split("\\s");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
            if (i > 0) arr[i] = " " + arr[i];
            result.append(arr[i]);
        }
        return result.toString();
    }


}
