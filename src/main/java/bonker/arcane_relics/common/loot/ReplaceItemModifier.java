package bonker.arcane_relics.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ReplaceItemModifier extends LootModifier {

    public static final Supplier<Codec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(
            inst.group(
                    ForgeRegistries.ITEMS.getCodec().fieldOf("replace").forGetter(m -> m.replace),
                    ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
                    Codec.DOUBLE.fieldOf("chance").forGetter(m -> m.chance)
            )).apply(inst, ReplaceItemModifier::new)
    ));

    private final Item replace;
    private final Item item;
    private final double chance;

    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item replace, Item item, double chance) {
        super(conditionsIn);

        this.replace = replace;
        this.item = item;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            if (generatedLoot.get(i).is(replace) && context.getRandom().nextDouble() <= chance) {
                generatedLoot.set(i, new ItemStack(item));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
