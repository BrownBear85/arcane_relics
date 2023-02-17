package bonker.arcane_relics.common.loot;

import bonker.arcane_relics.ArcaneRelics;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ARGlobalLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ArcaneRelics.MODID);

    public static final RegistryObject<Codec<AddItemModifier>> ADD_ITEM = GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("add_item", AddItemModifier.CODEC);
    public static final RegistryObject<Codec<ReplaceItemModifier>> REPLACE_ITEM = GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("replace_item", ReplaceItemModifier.CODEC);

}
