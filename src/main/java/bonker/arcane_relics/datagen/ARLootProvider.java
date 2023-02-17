package bonker.arcane_relics.datagen;

import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.loot.AddItemModifier;
import bonker.arcane_relics.common.loot.ReplaceItemModifier;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
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

            add("netherrack", new ReplaceItemModifier(
                    new LootItemCondition[] { new MatchTool(ItemPredicate.Builder.item().of(ARItems.BRONZE_HAMMER.get()).build()) },
                    Items.NETHERRACK, ARItems.NETHERRACK_DUST.get(), 0.4
            ));
        }
    }
}
