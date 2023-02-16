package bonker.arcane_relics.common.item.custom;

import bonker.arcane_relics.client.ClientLogic;
import bonker.arcane_relics.client.particle.MagicDustParticleOptions;
import bonker.arcane_relics.common.Util;
import bonker.arcane_relics.common.networking.ARNetworking;
import bonker.arcane_relics.common.networking.EvilSkullC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class EvilSkullItem extends Item {

    public EvilSkullItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            if (!ClientLogic.usingEvilSkull) {
                ClientLogic.usingEvilSkull = true;
                ARNetworking.sendToServer(new EvilSkullC2SPacket(true));
            }
        }
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLevel.isClientSide) {
            ClientLogic.usingEvilSkull = false;
            ARNetworking.sendToServer(new EvilSkullC2SPacket(false));
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 1200;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }
}
