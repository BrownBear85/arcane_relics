package bonker.arcane_relics.common.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DynamicItem extends Item {

    private final List<Component> tooltip;
    private final CraftingRemainder craftingRemainder;

    private DynamicItem(Item.Properties properties, @Nullable List<Component> tooltip, @Nullable CraftingRemainder craftingRemainder) {
        super(properties);
        this.tooltip = tooltip;
        this.craftingRemainder = craftingRemainder;

    }

    public static DynamicItemBuilder create(Item.Properties properties) {
        DynamicItemBuilder builder = new DynamicItemBuilder();
        builder.properties = properties;
        return builder;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.addAll(tooltip);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return craftingRemainder != null;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return craftingRemainder.get(stack);
    }

    public static class DynamicItemBuilder {

        private Item.Properties properties;
        private List<Component> tooltip;
        private CraftingRemainder craftingRemainder;

        private DynamicItemBuilder() {}

        public DynamicItemBuilder tooltip(String name) {
            this.tooltip = List.of(Component.translatable("item.arcane_relics." + name + ".tooltip").withStyle(ChatFormatting.GRAY));
            return this;
        }

        public DynamicItemBuilder tooltip(List<Component> tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public DynamicItemBuilder remainder(CraftingRemainder craftingRemainder) {
            this.craftingRemainder = craftingRemainder;
            return this;
        }

        public DynamicItem build() {
            return new DynamicItem(properties, tooltip, craftingRemainder);
        }
    }

    @FunctionalInterface
    public interface CraftingRemainder {
        ItemStack get(ItemStack stack);
    }
}
