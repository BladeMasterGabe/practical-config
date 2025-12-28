package config.practical.data;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundData {

    private String sound;
    private float volume, pitch;

    public SoundData(Identifier identifier, float volume, float pitch) {
        this.sound = identifier.toString();
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData(SoundEvent soundEvent, float volume, float pitch) {
        this(soundEvent.id(), volume, pitch);
    }


    public void setSound(SoundEvent soundEvent) {
        if (soundEvent == null) return;
        sound = soundEvent.id().toString();
    }

    public void setSound(Identifier identifier) {
        if (identifier == null) return;
        sound = identifier.toString();
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public Identifier getSound() {
        return Identifier.of(sound);
    }
}
