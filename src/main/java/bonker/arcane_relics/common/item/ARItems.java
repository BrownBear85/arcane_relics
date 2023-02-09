package bonker.arcane_relics.common.item;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.item.custom.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ARItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArcaneRelics.MODID);


    /* allay */

    public static final RegistryObject<Item> ALLAY_MILK = ITEMS.register("allay_milk",
            () -> new AllayMilkItem(properties().craftRemainder(Items.GLASS_BOTTLE).food(Foods.ALLAY_MILK).stacksTo(16)));

    public static final RegistryObject<Item> ALLAY_CHEESE = ITEMS.register("allay_cheese",
            () -> new Item(properties().food(Foods.ALLAY_CHEESE)));

    /* undead */

    public static final RegistryObject<UndeadSwordItem> UNDEAD_SWORD = ITEMS.register("undead_sword",
            () -> new UndeadSwordItem(Tiers.UNDEAD_SWORD, 4, -3.0F, properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> CHARGED_UNDEAD_CRYSTAL = ITEMS.register("charged_undead_crystal",
            () -> DynamicItem.create(properties().stacksTo(1).rarity(Rarity.UNCOMMON))
                    .tooltip(List.of(Component.translatable("item.arcane_relics.charged_undead_crystal.tooltip").withStyle(ChatFormatting.GRAY)))
                    .remainder((stack) -> Util.itemStack(ARItems.UNDEAD_CRYSTAL.get())).build());

    public static final RegistryObject<SlayerItem> UNDEAD_CRYSTAL = ITEMS.register("undead_crystal",
            () -> new SlayerItem(livingEntity -> livingEntity.getMobType() == MobType.UNDEAD,
                    11318916, CHARGED_UNDEAD_CRYSTAL.get(), 30, "item.arcane_relics.undead_crystal.tooltip", properties().stacksTo(1)));

    /*  */

    public static final RegistryObject<Item> BRONZE_HAMMER = ITEMS.register("bronze_hammer",
            () -> new PickaxeItem(Tiers.BRONZE, 1, -2.8F, properties()));

    /* crafting ingredients */

    public static final RegistryObject<Item> MYSTIC_STEEL_INGOT = ITEMS.register("mystic_steel_ingot", () -> new Item(properties()));
    public static final RegistryObject<Item> AZUR_CRYSTAL = ITEMS.register("azur_crystal", () -> new Item(properties()));
    public static final RegistryObject<Item> UNDEAD_INGOT = ITEMS.register("undead_ingot", () -> new Item(properties()));
    public static final RegistryObject<Item> BONE_HANDLE = ITEMS.register("bone_handle", () -> new Item(properties()));
    public static final RegistryObject<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot", () -> new Item(properties()));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(properties()));
    public static final RegistryObject<Item> NETHERRACK_DUST = ITEMS.register("netherrack_dust", () -> new Item(properties()));
    public static final RegistryObject<Item> FIERY_POWDER = ITEMS.register("fiery_powder", () -> new Item(properties()));
    public static final RegistryObject<Item> FIERY_SKULL = ITEMS.register("fiery_skull", () -> new Item(properties()));

    /* mob drops */

    public static final RegistryObject<Item> SKELETON_SKULL = ITEMS.register("skeleton_skull", () -> DynamicItem.create(properties()).tooltip("skeleton_skull").build());
    public static final RegistryObject<Item> ZOMBIE_HAND = ITEMS.register("zombie_hand", () -> DynamicItem.create(properties()).tooltip("zombie_hand").build());

    /* evil */

    public static final RegistryObject<Item> EVIL_SKULL = ITEMS.register("evil_skull", () -> new EvilSkullItem(properties().stacksTo(1)));


    public static Item.Properties properties() {
        return new Item.Properties();
    }


    public static class Foods {
        public static final FoodProperties ALLAY_MILK = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.0F)
                .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 65, 0), 1.0F)
                .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 100, 1), 1.0F).build();

        public static final FoodProperties ALLAY_CHEESE = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.1F)
                .effect(() -> new MobEffectInstance(MobEffects.DARKNESS, 120, 0), 0.6F)
                .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 160, 1), 0.2F).build();
    }

    public static class Tiers {
        public static final Tier UNDEAD_SWORD = new ForgeTier(2, 250, 6.0F, 2.0F, 14, null, Ingredient::of);

        public static final Tier BRONZE = new ForgeTier(2, 250, 3.5F, 2.0F, 14, null, () -> Ingredient.of(BRONZE_INGOT.get()));
    }
}
