package bonker.arcane_relics.common;

import bonker.arcane_relics.ArcaneRelics;
import bonker.arcane_relics.common.command.ArcaneRelicsCommand;
import bonker.arcane_relics.common.item.ARItems;
import bonker.arcane_relics.common.networking.ARNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

public class CommonEvents {

    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID)
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void rightClickEntity(PlayerInteractEvent.EntityInteract event) {
            if (event.getItemStack().is(Items.GLASS_BOTTLE) && event.getTarget() instanceof Allay) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);

                Player player = event.getEntity();
                Entity allay = event.getTarget();
                event.getLevel().playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, SoundSource.NEUTRAL, 1.0F, 1.0F);
                event.getLevel().playSound(player, allay.getX(), allay.getY(), allay.getZ(), SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 1.0F, 1.0F);
                ItemUtils.createFilledResult(event.getItemStack(), player, Util.itemStack(ARItems.ALLAY_MILK.get()));
            }
        }

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            ArcaneRelicsCommand.register(event.getDispatcher());
        }
    }

    @Mod.EventBusSubscriber(modid = ArcaneRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            ARNetworking.register();
        }

        @SubscribeEvent
        public static void registerCreativeModeTab(CreativeModeTabEvent.Register event){
            event.registerCreativeModeTab(new ResourceLocation(ArcaneRelics.MODID, "tab"), builder ->
                builder.title(Component.translatable("item_group.arcane_relics.tab"))
                    .icon(() -> new ItemStack(ARItems.ALLAY_MILK.get()))
                    .displayItems((enabledFlags, populator, hasPermissions) -> {
                        for (RegistryObject<Item> item : ARItems.ITEMS.getEntries()) {
                            populator.accept(item.get());
                        }
                    })
            );
        }
    }
}
