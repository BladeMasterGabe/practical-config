package config.practical.widgets;

import config.practical.utilities.Constants;
import config.practical.utilities.DrawHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
public class ConfigButton extends ClickableWidget {
    public static final int HEIGHT = 25;

    private final Runnable runnable;
    private long clickedTime = 0;
    private final long delay;

    public ConfigButton(Text message, Runnable runnable, long delay) {
        super(0, 0, Constants.WIDGET_WIDTH, HEIGHT, message);
        this.delay = delay;
        this.runnable = runnable;
    }

    @SuppressWarnings("unused")
    public ConfigButton(Text message, Runnable runnable) {
        this(message, runnable, 200);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        int x = getX();
        int y = getY();

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int centered = (width - textRenderer.getWidth(getMessage())) / 2;

        long diff = System.currentTimeMillis() - clickedTime;

        if (diff > delay) {
            DrawHelper.drawBackground(context, x, y, width, height, 0xff666666);
        } else {
            DrawHelper.drawBackground(context, x, y, width, height, 0xff333333);

        }

        context.drawText(textRenderer, getMessage(), x + centered, y + (HEIGHT - Constants.TEXT_HEIGHT) / 2, Constants.WHITE_COLOR, true);


    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        long diff = System.currentTimeMillis() - clickedTime;

        if (diff > delay) {
            this.runnable.run();
            clickedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}
