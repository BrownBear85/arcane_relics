package bonker.arcane_relics.common.item.custom;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = ArcaneRelics.MODID)
public class SlayerItem extends Item implements IProgressBarItem {

    private final Predicate<LivingEntity> mobPredicate;
    private final int barColor;
    private final Item chargedVariant;
    private final int kills;

    private final String tooltipKey;

    public SlayerItem(Predicate<LivingEntity> mobPredicate, int barColor, Item chargedVariant, int kills, String tooltipKey, Properties pProperties) {
        super(pProperties);
        this.mobPredicate = mobPredicate;
        this.barColor = barColor;
        this.chargedVariant = chargedVariant;
        this.kills = kills;
        this.tooltipKey = tooltipKey;
    }

    @SubscribeEvent
    public static void entityKilled(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            LivingEntity entity = event.getEntity();
            Inventory inventory = player.getInventory();

            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                ItemStack stack = inventory.getItem(slot);
                if (stack.getItem() instanceof SlayerItem slayerItem) {

                    if (slayerItem.mobPredicate.test(entity)) {
                        slayerItem.incrementProgress(stack, 1);
                        if (slayerItem.getProgress(stack) >= slayerItem.getMaxProgress()) {
                            inventory.setItem(slot, Util.itemStack(slayerItem.chargedVariant));
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable(tooltipKey)
                .append(Component.literal(String.format(" %d / %d", getProgress(pStack), getMaxProgress())))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public int getMaxProgress() {
        return kills;
    }

    public boolean isBarVisible(ItemStack pStack) {
        return this.getProgress(pStack) > 0;
    }

    public int getBarWidth(ItemStack pStack) {
        return Math.round((float)this.getProgress(pStack) / (float)this.getMaxProgress() * 13.0F );
    }

    public int getBarColor(ItemStack pStack) {
        return barColor;
    }
}
