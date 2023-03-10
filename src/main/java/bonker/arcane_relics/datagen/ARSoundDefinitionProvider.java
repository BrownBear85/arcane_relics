package bonker.arcane_relics.datagen;

import bonker.arcane_relics.ArcaneRelics;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ARSoundDefinitionProvider extends SoundDefinitionsProvider {

    public ARSoundDefinitionProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        createSound("rumble");
        createEasyMultiSound("undead_cast", 4);
        createSound("monster_disappear");
        createSound("spooky_breath");
        createEasyMultiSound("evil_impact", 6);
        createEasyMultiSound("evil_whoosh", 5);
        createSound("evil_loop");
        createSound("evil_cast");
    }

    public void createSound(String name) {
        add(name, SoundDefinition.definition()
                .subtitle("subtitles.arcane_relics." + name)
                .with(SoundDefinition.Sound.sound(new ResourceLocation(ArcaneRelics.MODID, name), SoundDefinition.SoundType.SOUND)));
    }

    public void createSoundNoSubtitle(String name) {
        add(name, SoundDefinition.definition()
                .with(SoundDefinition.Sound.sound(new ResourceLocation(ArcaneRelics.MODID, name), SoundDefinition.SoundType.SOUND)));
    }

    public void createMultiSound(String name, String... sounds) {
        SoundDefinition definition = SoundDefinition.definition();
        for (String sound : sounds) {
            definition.with(SoundDefinition.Sound.sound(new ResourceLocation(ArcaneRelics.MODID, sound), SoundDefinition.SoundType.SOUND));
        }
        add(name, definition.subtitle("subtitles.arcane_relics." + name));
    }

    public void createEasyMultiSound(String name, int numSounds) {
        String[] sounds = new String[numSounds];
        for (int i = 0; i < numSounds; i++) {
            sounds[i] = name + "/" + name + "_" + i;
        }
        createMultiSound(name, sounds);
    }
}
