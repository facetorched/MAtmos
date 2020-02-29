package eu.ha3.matmos.core.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class NoAttenuationSound extends PositionedSoundRecord {
    public NoAttenuationSound(ResourceLocation loc, float volume, float pitch, float x, float y, float z) {
        super(loc, SoundCategory.AMBIENT, volume, pitch, false, 0, ISound.AttenuationType.NONE, x, y, z);
    }
}
