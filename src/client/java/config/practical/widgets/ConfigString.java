package config.practical.widgets;

import config.practical.utilities.Constants;
import config.practical.utilities.DrawHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConfigString extends TextFieldWidget {

    private static final int HEIGHT = 40;
    private static final int INPUT_PADDING = 2;
    private static final int TEXT_OFFSET = 13;
    private static final int INPUT_HEIGHT = 14;
    private static final int INPUT_COLOR = 0xff222222;

    private int maxLength;
    private int selectionEnd;
    private Consumer<String> changedListener;

    public ConfigString(Text message, Supplier<String> supplier, Consumer<String> consumer, boolean formatText) {
        super(MinecraftClient.getInstance().textRenderer, Constants.WIDGET_WIDTH, HEIGHT, message);
        this.setMaxLength(100);
        this.setText(supplier.get());
        this.setChangedListener(string -> {
            if (formatText) {
                consumer.accept(string.replace('&', '§'));
            } else {
                consumer.accept(string);
            }
        });
    }

    public ConfigString(Text message, Supplier<String> supplier, Consumer<String> consumer) {
        this(message, supplier, consumer, true);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        DrawHelper.drawBackground(context, getX(), super.getY(), width, getMainBackgroundHeight());
        context.drawText(MinecraftClient.getInstance().textRenderer, getMessage(), getX() + Constants.TEXT_PADDING, super.getY() + (getMainBackgroundHeight() - Constants.TEXT_HEIGHT) / 2, Constants.WHITE_COLOR, true);
        DrawHelper.drawBackground(context, getX(), super.getY() + (height - INPUT_HEIGHT), width, INPUT_HEIGHT, INPUT_COLOR);
        super.renderWidget(context, mouseX, mouseY, deltaTicks);

    }

    @Override
    public boolean charTyped(CharInput input) {
        if (!this.isActive()) {
            return false;
        } else if (input.isValidChar() || isValid(input)) {
            this.write(input.asString());
            return true;
        } else {
            return false;
        }
    }


    private boolean isValid(CharInput input) {
        return input.asString().contains("§");
    }

    /**
     * A slightly modified version of the TextFieldWidget write(String)
     * because it removes chars like §
     * @param text The text to add
     */
    @Override
    public void write(String text) {
        int i = Math.min(getCursor(), this.selectionEnd);
        int j = Math.max(getCursor(), this.selectionEnd);
        int k = this.maxLength - getText().length() - (i - j);
        if (k > 0) {
            //String string = StringHelper.stripInvalidChars(text);
            String string = text;
            int l = string.length();
            if (k < l) {
                if (Character.isHighSurrogate(string.charAt(k - 1))) {
                    k--;
                }

                string = string.substring(0, k);
                l = k;
            }
            String string2 = new StringBuilder(getText()).replace(i, j, string).toString();
            setText(string2);
            this.setSelectionStart(i + l);
            this.setSelectionEnd(getCursor());
            changedListener.accept(getText());
        }
    }

    @Override
    public void setSelectionEnd(int index) {
        this.selectionEnd = MathHelper.clamp(index, 0, getText().length());
        super.setSelectionEnd(index);
    }

    @Override
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        super.setMaxLength(maxLength);
    }

    @Override
    public void setChangedListener(Consumer<String> changedListener) {
        this.changedListener = changedListener;
        super.setChangedListener(changedListener);
    }

    @Override
    public int getY() {
        return super.getY() + TEXT_OFFSET;
    }

    private int getMainBackgroundHeight() {
        return height - INPUT_HEIGHT - 1;
    }

    @Override
    public int getInnerWidth() {
        return width - INPUT_PADDING * 2 - 4;
    }

    @Override
    public boolean drawsBackground() {
        return false;
    }
}
