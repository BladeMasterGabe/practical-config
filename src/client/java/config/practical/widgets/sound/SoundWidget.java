package config.practical.widgets.sound;

import config.practical.utilities.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

class SoundWidget extends ClickableWidget {

    private static final Identifier BUTTON_TEXTURE = Identifier.ofVanilla("widget/button");
    private static final Identifier MUSIC_NOTE = Identifier.of(Constants.NAMESPACE, "music_note");
    private static final Identifier CHECKMARK = Identifier.of(Constants.NAMESPACE, "checkmark");
    private static final int HEIGHT = 30;
    private static final int BUTTON_SIZE = 20;
    private static final int PADDING = 4;

    private final Identifier identifier;
    private final SoundScreen screen;

    public SoundWidget(Identifier identifier, SoundScreen screen) {
        super(0, 0, Constants.WIDGET_WIDTH, HEIGHT, Text.literal(identifier.toString()));
        this.identifier = identifier;
        this.screen = screen;

    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int x = getX();
        int y = getY();
        context.drawText(textRenderer, getMessage(), x + Constants.TEXT_PADDING, y + (HEIGHT - Constants.TEXT_HEIGHT) / 2, Constants.WHITE_COLOR, true);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BUTTON_TEXTURE, buttonX(), buttonY(), BUTTON_SIZE, BUTTON_SIZE);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CHECKMARK, buttonX(), buttonY(), BUTTON_SIZE, BUTTON_SIZE);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BUTTON_TEXTURE, soundX(), soundY(), BUTTON_SIZE, BUTTON_SIZE);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, MUSIC_NOTE, soundX(), soundY(), BUTTON_SIZE, BUTTON_SIZE);
    }

    public boolean contains(String searchTerm) {
        return identifier.toString().contains(searchTerm);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        soundClick(mouseX, mouseY);
        buttonClick(mouseX, mouseY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    private int buttonX() {
        return getX() + Constants.WIDGET_WIDTH - BUTTON_SIZE - PADDING;
    }

    private int buttonY() {
        return getY();
    }

    private int soundX() {
        return getX() + Constants.WIDGET_WIDTH - (BUTTON_SIZE + PADDING) * 2;
    }

    private int soundY() {
        return getY();
    }

    private void soundClick(double mouseX, double mouseY) {
        if (soundX() <= mouseX && mouseX <= soundX() + BUTTON_SIZE && soundY() <= mouseY && mouseY <= soundY() + BUTTON_SIZE) {
            screen.playSound(identifier);
        }
    }

    private void buttonClick(double mouseX, double mouseY) {
        if (buttonX() <= mouseX && mouseX <= buttonX() + BUTTON_SIZE && buttonY() <= mouseY && mouseY <= buttonY() + BUTTON_SIZE) {
            screen.setIdentifier(identifier);
        }
    }

}
