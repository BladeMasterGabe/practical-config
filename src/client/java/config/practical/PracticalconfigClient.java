package config.practical;

import net.fabricmc.api.ClientModInitializer;

public class PracticalconfigClient implements ClientModInitializer {

    /**
     * This is the entry point for this config library.
     * The comment below is a very basic code for how you
     * can start using it
     */
    @Override
    public void onInitializeClient() {
        Config.manager.load();
        //register();
    }

    /*

      public static final ConfigManager manager = new ConfigManager("./config/test-" + Constants.NAMESPACE + ".json",
            List.of(PracticalconfigClient.class));

        @ConfigValue
    public static boolean someBoolean = false;

    private static KeyBinding openConfig;


    public void register() {
        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "opens a Config menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "some category"));

        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if (openConfig.wasPressed()) {
                client.setScreen(createScreen(null));
            }
        }));
    }

    @ConfigValue
    public static HUDComponent myComponent = new HUDComponent(0, 0, 100, 50, 1, () -> true, (component, context) -> {
        int x = component.getScaledX();
        int y = component.getScaledY();
        context.fill(x, y, x + component.getWidth(), y + component.getHeight(), 0xffff0000);
    });

    @ConfigValue
    public static HUDComponent myOtherComponent = new HUDComponent(0, 0, 100, 20, 1, () -> true, (component, context) -> {
       int x = component.getScaledX();
       int y = component.getScaledY();

       Text text = Text.literal("I");
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int centered = (component.getWidth() - textRenderer.getWidth(text)) / 2;
        context.drawText(textRenderer, text, x + centered, y + (component.getHeight() - Constants.TEXT_HEIGHT) / 2, Constants.WHITE_COLOR, true);
    });

    public static Screen createScreen(Screen parent) {
        ConfigurableScreen screen = new ConfigurableScreen(Text.literal("This is the title"), parent, manager);
        ConfigCategory category = new ConfigCategory("This is a category");
        category.add(new ConfigBool(Text.literal("This is a bool"), () -> someBoolean, bool -> someBoolean = bool));
        screen.addCategory(category);
        return screen;
    }*/
}