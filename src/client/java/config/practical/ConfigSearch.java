package config.practical;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

class ConfigSearch extends TextFieldWidget {

    public static final int WIDTH = 150;
    public static final int HEIGHT = 30;

    public ConfigSearch(ConfigurableScreen screen, TextRenderer textRenderer,int x, int y) {
        //has to use a direct non null TextRenderer
        super(textRenderer, x, y, WIDTH, HEIGHT, Text.empty());
        this.setChangedListener(screen::updateScroll);
    }


}
