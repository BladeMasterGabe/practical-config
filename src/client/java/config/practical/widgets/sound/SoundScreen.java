package config.practical.widgets.sound;

import config.practical.ConfigScroll;
import config.practical.ConfigSearch;
import config.practical.data.SoundData;
import config.practical.utilities.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

class SoundScreen extends Screen {
    private final Screen parent;

    private static final int BUTTON_HEIGHT = 20;

    private final ButtonWidget confirm;
    private final ConfigScroll scroll;
    private final ConfigSearch search;
    private final TextWidget text;
    private final ArrayList<SoundWidget> soundWidgets = new ArrayList<>();

    private final SoundData soundData;
    private final ConfigSound.SoundSelector soundSelector;

    private Identifier identifier;
    private SoundInstance prevSound;

    public SoundScreen(SoundData soundData, ConfigSound.SoundSelector soundSelector) {
        super(Text.empty());
        loadWidgets();
        MinecraftClient client = MinecraftClient.getInstance();
        parent = client.currentScreen;
        Window window = client.getWindow();

        this.soundData = soundData;
        this.soundSelector = soundSelector;

        this.identifier = soundData.getSound();

        int textX = (window.getScaledWidth() - Constants.WIDGET_WIDTH) / 2;

        int scrollHeight = window.getScaledHeight() - ConfigSearch.HEIGHT - BUTTON_HEIGHT - 20;

        Text btnText = Text.literal("Select");
        TextRenderer textRenderer = client.textRenderer;
        int btnWidth = textRenderer.getWidth(btnText) * 2;

        text = new TextWidget(textX, 0, Constants.WIDGET_WIDTH, BUTTON_HEIGHT, Text.literal("Sound: " + soundData.getSound().toString()), textRenderer).alignLeft();
        confirm = ButtonWidget.builder(btnText, this::confirmSound).position(textX + Constants.WIDGET_WIDTH, 0).width(btnWidth).build();
        scroll = new ConfigScroll(0, BUTTON_HEIGHT + 10, window.getScaledWidth(), scrollHeight, Constants.WIDGET_WIDTH);
        search = new ConfigSearch(client.textRenderer, (window.getScaledWidth() - ConfigSearch.WIDTH) / 2, scrollHeight + scroll.getY() + 5, this::updateScroll);
        updateScroll("");
    }

    @Override
    protected void init() {
        this.addDrawableChild(text);
        this.addDrawableChild(confirm);
        this.addDrawableChild(search);
        this.addDrawableChild(scroll);
    }

    private void updateScroll(String searchTerm) {
        scroll.setFocused(null);
        scroll.children().clear();

        for (SoundWidget widget : soundWidgets) {
            if (widget.contains(searchTerm)) {
                scroll.add(widget);
            }
        }

        scroll.update();
        scroll.setScrollY(0);
    }

    public void setIdentifier(Identifier identifier) {
        if (identifier == null) return;
        this.identifier = identifier;
        text.setMessage(Text.literal("Sound: " + identifier));
    }

    public void playSound(Identifier identifier) {
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        stopSound();

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        prevSound = new PositionedSoundInstance(SoundEvent.of(identifier), SoundCategory.MASTER, 1, 1, SoundInstance.createRandom(), player.getBlockPos());
        soundManager.play(prevSound);
    }

    private void stopSound() {
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        if (prevSound != null) {
            soundManager.stop(prevSound);
        }
    }

    private void loadWidgets() {
        for (Identifier id : Registries.SOUND_EVENT.getIds()) {
            soundWidgets.add(new SoundWidget(id, this));
        }
    }

    private void confirmSound(ButtonWidget buttonWidget) {
        soundData.setSound(identifier);
        soundSelector.updateMessage();
        close();
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
        stopSound();

    }
}
