package bonker.arcane_relics.client.overlay;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.item.custom.CastingItem;
import bonker.arcane_relics.common.item.custom.UndeadSwordItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class CastingItemOverlay {

    private static final Font FONT = Minecraft.getInstance().font;
    private static final ResourceLocation X_TEXTURE = new ResourceLocation(ArcaneRelics.MODID, "textures/gui/cast.png");

    public static boolean hideCrosshair = false;

    public static final IGuiOverlay OVERLAY = (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight / 2;

        hideCrosshair = false;

        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) {
            return;
        }

        if (CastingItem.canPlayerCast(Minecraft.getInstance().player)) {
            hideCrosshair = true;
            RenderSystem.setShaderColor(1F, 0.1F, 0.1F, 1.0F);
            RenderSystem.setShaderTexture(0, X_TEXTURE);
            GuiComponent.blit(poseStack, x - 7, y - 7, 0, 0, 17, 17, 17, 17);
        }
    };

    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("casting_item", OVERLAY);
        }
    }
}
