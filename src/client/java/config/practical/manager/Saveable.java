package config.practical.manager;

import com.google.gson.JsonObject;

/**
 * Used for custom saving instead of just gson parsing it
 */
public interface Saveable {

    void save(JsonObject object);

    void load(JsonObject object);

}
