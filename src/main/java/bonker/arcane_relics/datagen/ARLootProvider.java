package bonker.arcane_relics.datagen;

import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.loot.AddItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ARLootProvider {

    public static class ARGlobalLootModifierProvider extends GlobalLootModifierProvider {

        public ARGlobalLootModifierProvider(PackOutput output, String modid) {
            super(output, modid);
        }

        @Override
        protected void start() {
            add("zombie", new AddItemModifier(
                    new LootItemCondition[] { LootTableIdCondition.builder(new ResourceLocation("entities/zombie")).build() },
                    ARItems.ZOMBIE_HAND.get(), 0.12
            ));

            add("skeleton", new AddItemModifier(
                    new LootItemCondition[] { LootTableIdCondition.builder(new ResourceLocation("entities/skeleton")).build() },
                    ARItems.SKELETON_SKULL.get(), 0.12
            ));
        }
    }
}
