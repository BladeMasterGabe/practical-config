package config.practical;

import config.practical.manager.ConfigManager;
import config.practical.manager.ConfigValue;
import config.practical.utilities.Constants;

import java.util.List;

public class Config {

    public static final ConfigManager manager = new ConfigManager("./config/" + Constants.NAMESPACE + ".json",
            List.of(Config.class));

    @ConfigValue
    public static boolean gridEnabled = true;
}
