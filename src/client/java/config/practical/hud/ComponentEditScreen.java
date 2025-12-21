package config.practical.hud;

import config.practical.Config;
import config.practical.utilities.Constants;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix3x2fStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class ComponentEditScreen extends Screen {

    private static final Identifier CROSSHAIR = Identifier.of(Constants.NAMESPACE, "crosshair");

    private static final Text TITLE = Text.literal("");

    private static final double MOVE_SPEED = 2;
    private static final int CROSSHAIR_SIZE = 4;
    private static final float SCALE_FACTOR = 0.02f;
    private static final int gridColor = 0xff004444;

    private static final String[] KEYS = {
            "r",
            "r + ctrl + shift",
            "g + ctrl",
            "s",
            "h + ctrl",
            "v + ctrl",
            "tab"
    };
    private static final String[] INFO = {
            "reset component",
            "reset All editable components",
            "toggle grid",
            "snap to closet grid",
            "center selected horizontally",
            "center selected vertically",
            "cycle selected component"
    };

    private static final ArrayList<HUDComponent> ALL_COMPONENTS = new ArrayList<>();

    private final ArrayList<HUDComponent> components;

    private final Screen parent;

    private HUDComponent selected;
    private boolean isDragging = false;


    public ComponentEditScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
        this.components = new ArrayList<>();

        ALL_COMPONENTS.forEach(component -> {
            if (component.editable()) {
                components.add(component);
            }
        });
    }

    public static void addComponent(HUDComponent component) {
        if (component == null) return;
        ALL_COMPONENTS.add(component);
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selected != null) {
            float newScale = selected.getScale() + (float) Math.signum(verticalAmount) * SCALE_FACTOR;
            selected.setScale(newScale);
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (int) mouseX;
        int y = (int) mouseY;

        selected = null;
        for (HUDComponent component : components) {

            if (component.inBounds(x, y)) {
                selected = component;
                isDragging = true;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging && selected != null) {
            assert client != null;
            Window window = client.getWindow();
            selected.move(deltaX / window.getScaledWidth(), deltaY / window.getScaledHeight());
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean ctrlIsPressed = (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
        boolean shiftIsPressed = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;

        if (keyCode == GLFW.GLFW_KEY_R && ctrlIsPressed && shiftIsPressed) {
            for (HUDComponent component : components) {
                component.reset();
            }
        }

        if (keyCode == GLFW.GLFW_KEY_G && ctrlIsPressed) {
            Config.gridEnabled = !Config.gridEnabled;
            Config.manager.save();
        }

        if (keyCode == GLFW.GLFW_KEY_TAB) {
            if (selected == null) selected = components.getFirst();
            else {
                int index = components.indexOf(selected);
                index = (index + 1) % components.size();
                selected = components.get(index);
            }
        }

        if (selected != null) {
            if (keyCode == GLFW.GLFW_KEY_R) {
                selected.reset();
            }

            assert client != null;
            if (keyCode == GLFW.GLFW_KEY_H && ctrlIsPressed) {
                selected.centerHorizontally(client.getWindow());
            }

            if (keyCode == GLFW.GLFW_KEY_V && ctrlIsPressed) {
                selected.centerVertically(client.getWindow());
            }

            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                selected.move(-MOVE_SPEED / client.getWindow().getScaledWidth(), 0);
            }

            if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                selected.move(MOVE_SPEED / client.getWindow().getScaledWidth(), 0);
            }

            if (keyCode == GLFW.GLFW_KEY_UP) {
                selected.move(0, -MOVE_SPEED / client.getWindow().getScaledHeight());
            }

            if (keyCode == GLFW.GLFW_KEY_DOWN) {
                selected.move(0, MOVE_SPEED / client.getWindow().getScaledHeight());
            }

            if (keyCode == GLFW.GLFW_KEY_S) {
                selected.snapToGrid(client.getWindow());

            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        TextRenderer renderer = this.textRenderer;

        if (Config.gridEnabled) {

            Matrix3x2fStack stack = context.getMatrices();
            stack.pushMatrix();
            stack.translate(-0.5f, -0.5f);
            for (int i = width / 2; i >= 0; i -= Constants.GRID_SIZE) {
                context.drawVerticalLine(i, 0, height, gridColor);
            }

            for (int i = height / 2; i >= 0; i -= Constants.GRID_SIZE) {
                context.drawHorizontalLine(0, width, i, gridColor);
            }

            for (int i = width / 2; i < width; i += Constants.GRID_SIZE) {
                context.drawVerticalLine(i, 0, height, gridColor);
            }

            for (int i = height / 2; i < height; i += Constants.GRID_SIZE) {
                context.drawHorizontalLine(0, width, i, gridColor);
            }

            stack.popMatrix();
        }


        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CROSSHAIR, width / 2 - CROSSHAIR_SIZE, height / 2 - CROSSHAIR_SIZE, CROSSHAIR_SIZE * 2, CROSSHAIR_SIZE * 2, Constants.WHITE_COLOR);

        int longest = 0;
        for (String str : INFO) {
            longest = Math.max(textRenderer.getWidth(str), longest);
        }

        for (int i = 0; i < INFO.length; i++) {
            context.drawText(textRenderer, INFO[i], 1, height - ((renderer.fontHeight + 1) * (INFO.length - i)), 0xffffffff, true);
        }

        for (int i = 0; i < KEYS.length; i++) {
            context.drawText(textRenderer, KEYS[i], 7 + longest, height - ((renderer.fontHeight + 1) * (KEYS.length - i)), 0xffffffff, true);
        }

        if (selected != null) {
            selected.renderHighlight(context);
        }

        for (HUDComponent component : components) {
            component.renderIgnoreConditions(context);
        }
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(parent);
    }
}
