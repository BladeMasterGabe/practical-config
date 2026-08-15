package config.practical.data;

import com.google.gson.JsonObject;
import config.practical.manager.Saveable;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class SoundData implements Saveable {

    private String sound;
    private float volume, pitch;

    public SoundData(Identifier identifier, float volume, float pitch) {
        this.sound = identifier.toString();
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData(SoundEvent soundEvent, float volume, float pitch) {
        this(soundEvent.location(), volume, pitch);
    }


    public void setSound(SoundEvent soundEvent) {
        if (soundEvent == null) return;
        sound = soundEvent.location().toString();
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
        return Identifier.parse(sound);
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty("sound", sound);
        object.addProperty("volume", volume);
        object.addProperty("pitch", pitch);
    }

    @Override
    public void load(JsonObject object) {
       sound = object.get("sound").getAsString();
       volume = object.get("volume").getAsFloat();
       pitch = object.get("pitch").getAsFloat();
    }
}
