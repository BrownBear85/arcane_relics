package bonker.arcane_relics.common.item.custom;

import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.networking.ARNetworking;
import bonker.arcane_relics.common.networking.UndeadSwordC2SPacket;
import bonker.arcane_relics.common.worldevent.SummonUndeadWorldEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

public class UndeadSwordItem extends CastingItem implements ICastOnEntityItem, IProgressBarItem {

    public final int range;
    public final double assist;

    public UndeadSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.range = 50;
        this.assist = 0.4;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide && canPlayerCast(pPlayer, pUsedHand)) {
            LivingEntity target = getTargetedEntity(pPlayer, range, assist);
            if (target != null) {
                ARNetworking.sendToServer(new UndeadSwordC2SPacket(target, pUsedHand));
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        super.hurtEnemy(pStack, pTarget, pAttacker);
        if (pTarget.isDeadOrDying() && pTarget.getMobType() == MobType.UNDEAD && !pTarget.getTags().contains(SummonUndeadWorldEvent.SUMMONED_TAG)) {
            ARItems.UNDEAD_SWORD.get().incrementProgress(pStack, 1);
        }
        return true;
    }

    @Override
    public int getMaxProgress() {
        return 18;
    }

    @Override
    public boolean isCastValid(Player player, ItemStack stack) {
        return getTargetedEntity(player, range, assist) != null && (player.getAbilities().instabuild || getProgress(stack) >= 6);
    }

    public boolean isBarVisible(ItemStack pStack) {
        return this.getProgress(pStack) > 0;
    }

    public int getBarWidth(ItemStack pStack) {
        return Math.round((float)this.getProgress(pStack) / (float)this.getMaxProgress() * 13.0F );
    }

    public static final int WHITE = 16773596;
    public static final int RED = 16718121;
    public static final int GREEN = 4188713;

    public int getBarColor(ItemStack pStack) {
        int progress = getProgress(pStack);
        if (progress < 6)
            return WHITE;
        else if (progress == getMaxProgress())
            return System.currentTimeMillis() % 500 <= 250 ? WHITE : RED;
        else
            return System.currentTimeMillis() % 2000 <= 500 ? WHITE : GREEN;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return super.onDroppedByPlayer(item, player);
    }
}
