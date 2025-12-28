package config.practical;

import config.practical.category.ConfigCategory;
import config.practical.data.SoundData;
import config.practical.hud.HUDComponent;
import config.practical.manager.ConfigManager;
import config.practical.manager.ConfigValue;
import config.practical.utilities.Constants;
import config.practical.widgets.ConfigBool;
import config.practical.widgets.ConfigSection;
import config.practical.widgets.ConfigTextArea;
import config.practical.widgets.sound.ConfigSound;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * This is a test file where there is a kind of
 * prebuilt config and screen, please copy from this
 * to learn how to make a screen and save variables
 */
class TestFile {

    @ConfigValue
    public static SoundData someSound = new SoundData(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1, 1);

    @ConfigValue
    public static Identifier identifier = Identifier.of("some_identifier");

    @ConfigValue
    public static boolean someBoolean = false;

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


    public static final ConfigManager manager = new ConfigManager("./config/test-" + Constants.NAMESPACE + ".json",
            List.of(TestFile.class));

    private static KeyBinding openConfig;


    public static void register() {
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

        manager.load();
    }

    public static Screen createScreen(Screen parent) {
        ConfigurableScreen screen = new ConfigurableScreen(Text.literal("This is the title"), parent, manager);
        ConfigCategory category = new ConfigCategory("This is a category");
        ConfigSection section = new ConfigSection(Text.literal("some section"));
        section.add(new ConfigBool(Text.literal("This is a bool"), () -> someBoolean, bool -> someBoolean = bool));
        section.add(new ConfigSound(Text.literal("Some sound"), someSound));
        section.add(new ConfigTextArea("To be, or not to be, that is the question:\n" +
                "Whether 'tis nobler in the mind to suffer\n" +
                "The slings and arrows of outrageous fortune,\n" +
                "Or to take arms against a sea of troubles\n" +
                "And by opposing end them. To die—to sleep,\n" +
                "No more; and by a sleep to say we end\n" +
                "The heart-ache and the thousand natural shocks\n" +
                "That flesh is heir to: 'tis a consummation\n" +
                "Devoutly to be wish'd. To die, to sleep;\n" +
                "To sleep, perchance to dream—ay, there's the rub:\n" +
                "For in that sleep of death what dreams may come,\n" +
                "When we have shuffled off this mortal coil,\n" +
                "Must give us pause—there's the respect\n" +
                "That makes calamity of so long life.\n" +
                "For who would bear the whips and scorns of time,\n" +
                "Th'oppressor's wrong, the proud man's contumely,\n" +
                "The pangs of dispriz'd love, the law's delay,\n" +
                "The insolence of office, and the spurns\n" +
                "That patient merit of th'unworthy takes,\n" +
                "When he himself might his quietus make\n" +
                "With a bare bodkin? Who would fardels bear,\n" +
                "To grunt and sweat under a weary life,\n" +
                "But that the dread of something after death,\n" +
                "The undiscovere'd country, from whose bourn\n" +
                "No traveller returns, puzzles the will,"));
        category.add(section);
        screen.addCategory(category);
        return screen;
    }
}
