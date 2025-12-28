package config.practical;

import net.fabricmc.api.ClientModInitializer;


public class PracticalconfigClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Config.manager.load();

        //uncomment to test out a prebuilt screen
        //TestFile.register();
    }
}