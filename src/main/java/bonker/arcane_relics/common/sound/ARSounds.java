package bonker.arcane_relics.common.sound;

import bonker.arcane_relics.ArcaneRelics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ARSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ArcaneRelics.MODID);


    public static final RegistryObject<SoundEvent> RUMBLE = registerSoundEvent("rumble");
    public static final RegistryObject<SoundEvent> UNDEAD_CAST = registerSoundEvent("undead_cast");
    public static final RegistryObject<SoundEvent> MONSTER_DISAPPEAR = registerSoundEvent("monster_disappear");
    public static final RegistryObject<SoundEvent> SPOOKY_BREATH = registerSoundEvent("spooky_breath");
    public static final RegistryObject<SoundEvent> EVIL_IMPACT = registerSoundEvent("evil_impact");
    public static final RegistryObject<SoundEvent> EVIL_WHOOSH = registerSoundEvent("evil_whoosh");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ArcaneRelics.MODID, name)));
    }
}
