package bonker.arcane_relics.mixin;

import bonker.arcane_relics.client.overlay.CastingItemOverlay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin extends GuiComponent {

    @Final
    @Shadow
    protected Minecraft minecraft;

    @Shadow
    protected int screenWidth;
    @Shadow
    protected int screenHeight;

    @Inject(method = "renderCrosshair(Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 0), cancellable = true)
    public void onRenderCrosshair(PoseStack pPoseStack, CallbackInfo ci) {
        if (CastingItemOverlay.hideCrosshair) {
            ci.cancel();

            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
                float f = this.minecraft.player.getAttackStrengthScale(0.0F);
                boolean flag = false;
                if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && f >= 1.0F) {
                    flag = this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0F;
                    flag &= this.minecraft.crosshairPickEntity.isAlive();
                    flag &= this.minecraft.player.canHit(this.minecraft.crosshairPickEntity, 0);
                }

                int j = this.screenHeight / 2 - 7 + 16;
                int k = this.screenWidth / 2 - 8;
                if (flag) {
                    this.blit(pPoseStack, k, j, 68, 94, 16, 16);
                } else if (f < 1.0F) {
                    int l = (int)(f * 17.0F);
                    this.blit(pPoseStack, k, j, 36, 94, 16, 4);
                    this.blit(pPoseStack, k, j, 52, 94, l, 4);
                }
            }
        }
    }
}
