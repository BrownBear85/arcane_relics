package bonker.arcane_relics.datagen;

import bonker.arcane_relics.ArcaneRelics;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneRelics.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new ARItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new ARLanguageProvider(packOutput, ArcaneRelics.MODID, "en_us"));
        generator.addProvider(event.includeServer(), new ARSoundDefinitionProvider(packOutput, ArcaneRelics.MODID, existingFileHelper));
        generator.addProvider(event.includeServer(), new ARCraftingRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new ARLootProvider.ARGlobalLootModifierProvider(packOutput, ArcaneRelics.MODID));
    }
}
