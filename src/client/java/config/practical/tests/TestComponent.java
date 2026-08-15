package config.practical.tests;

import config.practical.hud.HUDCategory;
import config.practical.hud.HUDComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class TestComponent extends HUDComponent {

    public TestComponent() {
        super("This is a custom component");
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public boolean editable() {
        return true;
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public List<HUDCategory> categories() {
        return List.of(TestFile.P1);
    }

    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics) {
        int x = getScaledX();
        int y = getScaledY();

        graphics.fill(x, y, x + getWidth(), y + getHeight(), 0xff22ffff);
    }
}
