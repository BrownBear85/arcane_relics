package bonker.arcane_relics.common.item.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public abstract class CastingItem extends SwordItem {
    public CastingItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    /** checks if either hand has a CastingItem that is ready to cast (for rendering) */
    public static boolean canPlayerCast(Player player) {
        return CastingItem.canPlayerCast(player, InteractionHand.MAIN_HAND) || CastingItem.canPlayerCast(player, InteractionHand.OFF_HAND);
    }

    /** checks if the hand specified has a CastingItem with a valid cast and no cooldown (for general use) */
    public static boolean canPlayerCast(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof CastingItem castingItem) {
            return !player.getCooldowns().isOnCooldown(castingItem) && castingItem.isCastValid(player, stack);
        }

        return false;
    }

    /** checks if the cast is valid for the specific CastingItem (will be diff. for most items) */
    public boolean isCastValid(Player player, ItemStack stack) {
        return true;
    }
}
