package config.practical.manager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import config.practical.utilities.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager {

    private final String filePath;
    private final List<Class<?>> classes;

    public ConfigManager(String filePath, List<Class<?>> classes) {
        this.filePath = filePath;
        this.classes = classes;
    }

    @SuppressWarnings("unused")
    public ConfigManager(String filePath, Class<?> clazz) {
        this(filePath, List.of(clazz));
    }

    public void save() {

        JsonObject obj = new JsonObject();
        Gson gson = new Gson();

        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ConfigValue.class)) continue;

                field.setAccessible(true);
                try {
                    String name = field.getName();
                    Object value = field.get(null);
                    if (value instanceof Saveable saveable) {
                        JsonObject object = new JsonObject();
                        saveable.save(object);
                        obj.add(name, object);
                    } else {
                        obj.add(name, gson.toJsonTree(value));
                    }
                } catch (IllegalAccessException ignored) {
                    Constants.LOGGER.warning("Field " + field.getName() + " is not accessible.");
                }

            }
        }

        JsonElement tree = gson.toJsonTree(obj);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(tree.toString());
        } catch (IOException ignored) {
            Constants.LOGGER.severe("Could not save config file: " + filePath);
        }
    }

    @SuppressWarnings("unused")
    public void load() {
        String jsonContent;
        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException ignored) {
            Constants.LOGGER.severe("Could not read file: " + filePath);
            return;
        }
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(jsonContent, JsonObject.class);
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                loadField(object, gson, field);
            }
        }
    }

    private void loadField(JsonObject object, Gson gson, Field field) {
        if (!field.isAnnotationPresent(ConfigValue.class)) return;

        boolean alreadyAccessible = field.canAccess(null);

        String name = field.getName();
        if (!object.has(name)) return;

        if (!alreadyAccessible) {
            field.setAccessible(true);
        }
        try {
            JsonElement jsonVal = object.get(name);
            Object fieldObj = field.get(null);
            if (fieldObj instanceof Saveable saveable && jsonVal instanceof JsonObject obj) {
                saveable.load(obj);
            } else {
                Object val = gson.fromJson(jsonVal, field.getType());
                field.set(null, val);
            }
        } catch (IllegalAccessException ignored) {
            Constants.LOGGER.warning("Could not read field: " + field.getName());
        }
        if (!alreadyAccessible) {
            field.setAccessible(false);
        }
    }
}
