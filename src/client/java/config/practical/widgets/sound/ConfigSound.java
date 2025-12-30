package config.practical.widgets.sound;

import config.practical.data.SoundData;
import config.practical.utilities.Constants;
import config.practical.utilities.DrawHelper;
import config.practical.widgets.ConfigButton;
import config.practical.widgets.ConfigSection;
import config.practical.widgets.sliders.ConfigFloat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigSound extends ConfigSection {

    static class SoundSelector extends ClickableWidget {

        private static final Identifier COG = Identifier.of(Constants.NAMESPACE, "cog");
        public static final int HEIGHT = 30;
        public static final int SPRITE_SIZE = 20;
        public static final int PADDING = 4;
        private final SoundData soundData;

        public SoundSelector(SoundData soundData) {
            super(0, 0, Constants.WIDGET_WIDTH, HEIGHT, createMessage(soundData));
            this.soundData = soundData;
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
            int x = getX();
            int y = getY();

            MinecraftClient client = MinecraftClient.getInstance();
            TextRenderer textRenderer = client.textRenderer;

            DrawHelper.drawBackground(context, x, y, width, height);
            context.drawText(textRenderer, getMessage(), x + Constants.TEXT_PADDING, y + (HEIGHT - Constants.TEXT_HEIGHT) / 2, Constants.WHITE_COLOR, true);
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, COG, x + width - SPRITE_SIZE - PADDING, y + (height - SPRITE_SIZE) / 2, SPRITE_SIZE, SPRITE_SIZE);

        }

        @Override
        public void onClick(Click click, boolean doubled) {
            super.onClick(click, doubled);
            MinecraftClient.getInstance().setScreen(new SoundScreen(soundData, this));
        }

        public static Text createMessage(SoundData soundData) {
            return Text.literal("Sound: " + soundData.getSound().toString());
        }

        public void updateMessage() {
            this.setMessage(createMessage(soundData));
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }
    }

    public ConfigSound(Text text, SoundData soundData, float maxVolume, float maxPitch, boolean includeTestButton) {
        super(text);
        SoundSelector sound = new SoundSelector(soundData);
        ConfigFloat volume = new ConfigFloat(Text.literal("Volume"), soundData::getVolume, soundData::setVolume, 0.01f, 0, maxVolume);
        ConfigFloat pitch = new ConfigFloat(Text.literal("Pitch"), soundData::getPitch, soundData::setPitch, 0.01f, 0, maxPitch);

        add(sound);
        add(volume);
        add(pitch);

        if (includeTestButton) {
            add(new ConfigButton(Text.literal("Test sound"), () -> {
                ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player == null) return;
                player.playSound(SoundEvent.of(soundData.getSound()), soundData.getVolume(), soundData.getPitch());
            }));
        }
    }

    public ConfigSound(Text text, SoundData soundData) {
        this(text, soundData, 2, 2, true);
    }
}
