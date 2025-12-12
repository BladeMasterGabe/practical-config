package config.practical.hud;

import config.practical.utilities.Constants;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class ComponentEditScreen extends Screen {

    private static final Identifier CROSSHAIR = Identifier.of(Constants.NAMESPACE, "crosshair");

    private static final Text TITLE = Text.literal("");

    private static final int CROSSHAIR_SIZE = 4;
    private static final float SCALE_FACTOR = 0.02f;
    private static final String[] INFO = {"Press r to reset the selected component", "Use the mouse wheel to increase or decrease the scale", "Press r+ctrl+shift to reset ALL currently editable components"};

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

        if (keyCode == GLFW.GLFW_KEY_R  && ctrlIsPressed && shiftIsPressed) {
            for (HUDComponent component: components) {
                component.reset();
            }
        }

        if (keyCode == GLFW.GLFW_KEY_R && selected != null) {
            selected.reset();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        int width =  context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, CROSSHAIR, width / 2 - CROSSHAIR_SIZE, height /2  - CROSSHAIR_SIZE, CROSSHAIR_SIZE * 2,  CROSSHAIR_SIZE * 2, Constants.WHITE_COLOR);

        TextRenderer renderer = this.textRenderer;

        for (int i = 0; i < INFO.length; i++) {
            context.drawText(textRenderer, INFO[i], 1, height - ((renderer.fontHeight + 1) * (INFO.length - i)), 0xffffffff, true);
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
