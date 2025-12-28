package config.practical.widgets;

import config.practical.utilities.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;

public class ConfigTextArea extends ClickableWidget {

    private final TextRenderer textRenderer;
    private final StringVisitable stringVisitable;

    public ConfigTextArea(String string) {
        super(0, 0, Constants.WIDGET_WIDTH, getHeight(string, Constants.WIDGET_WIDTH), Text.empty());
        textRenderer = MinecraftClient.getInstance().textRenderer;
        stringVisitable = StringVisitable.plain(string);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawWrappedText(textRenderer, stringVisitable, getX(), getY(), width, 0xffffffff, true);
    }



    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }


    private static int getHeight(String string, int width) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        StringVisitable visitable = StringVisitable.plain(string);
        return textRenderer.getWrappedLinesHeight(visitable, width);
    }
}
