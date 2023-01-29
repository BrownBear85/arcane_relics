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
        createMultiSound("undead_cast", "undead_cast_0", "undead_cast_1", "undead_cast_2", "undead_cast_3");
        createSound("monster_disappear");
    }

    public void createSound(String name) {
        add(name, SoundDefinition.definition()
                .subtitle("subtitles.arcane_relics." + name)
                .with(SoundDefinition.Sound.sound(new ResourceLocation(ArcaneRelics.MODID, name), SoundDefinition.SoundType.SOUND)));
    }

    public void createMultiSound(String name, String... sounds) {
        SoundDefinition definition = SoundDefinition.definition();
        for (String sound : sounds) {
            definition.with(SoundDefinition.Sound.sound(new ResourceLocation(ArcaneRelics.MODID, sound), SoundDefinition.SoundType.SOUND));
        }
        add(name, definition.subtitle("subtitles.arcane_relics." + name));
    }
}
