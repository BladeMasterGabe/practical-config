package config.practical.hud;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Window;
import config.practical.Practicalconfig;
import config.practical.manager.Saveable;
import config.practical.utilities.Constants;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class HUDComponent implements Saveable, HudElement {

    private static int componentCount = 0;

    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 5f;

    private static final int HIGHLIGHT_COLOR = 0x99ffffff;
    private static final int HIGHLIGHT_MARGIN = 2;

    private static final Identifier AFTER_IDENTIFIER = Identifier.parse("boss_bar");
    private static final Minecraft client = Minecraft.getInstance();

    private double x, y;
    private float scale;

    private final String info;

    public HUDComponent(String info) {
        this(0, 0, 1, info);
    }

    public HUDComponent(double x, double y, float scale, String info) {
        this.x = x;
        this.y = y;
        setScale(scale);
        this.info = info;

        HudElementRegistry.attachElementAfter(AFTER_IDENTIFIER, Identifier.fromNamespaceAndPath(Practicalconfig.MOD_ID, "component-" + componentCount), this);
        componentCount++;
        ComponentEditScreen.addComponent(this);

        init();
    }

    public void init() {}

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean editable();

    public abstract boolean shouldRender();

    /**
     * a list of categories for it to display the components
     * @return List of HUDCategory or null if it should ignore categories
     */
    public abstract List<HUDCategory> categories();

    public abstract void render(@NonNull GuiGraphicsExtractor graphics);

    public final void scaleAndRender(@NonNull GuiGraphicsExtractor graphics) {
        Matrix3x2fStack stack = graphics.pose();
        stack.pushMatrix();
        stack.scale(scale, scale);
        render(graphics);
        stack.popMatrix();
    }

    /**
     * Used by the ComponentEditScreen to render the component
     * Override it if you want to do a specific template for when its edited
     * @param graphics the graphics interface
     */
    public void renderEditTemplate(@NonNull GuiGraphicsExtractor graphics) {
        render(graphics);
    }

    public final void scaleAndRenderEditTemplate(@NonNull GuiGraphicsExtractor graphics) {
        Matrix3x2fStack stack = graphics.pose();
        stack.pushMatrix();
        stack.scale(scale, scale);
        renderEditTemplate(graphics);
        stack.popMatrix();
    }

    public void renderHighlight(GuiGraphicsExtractor graphics) {

        int x = getScaledX();
        int y = getScaledY();
        int width = getWidth();
        int height = getHeight();

        Matrix3x2fStack stack = graphics.pose();
        stack.pushMatrix();
        stack.scale(scale, scale);
        Font textRenderer = Minecraft.getInstance().font;
        graphics.fill(x - HIGHLIGHT_MARGIN, y - HIGHLIGHT_MARGIN, x + width + HIGHLIGHT_MARGIN, y + height + HIGHLIGHT_MARGIN, HIGHLIGHT_COLOR);
        graphics.text(textRenderer, info, x + (width - textRenderer.width(info)) / 2, y - textRenderer.lineHeight - 2, 0xffffffff, true);
        stack.popMatrix();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        if (!editable() || !shouldRender()) return;
        scaleAndRender(graphics);
    }

    public void reset() {
        x = 0;
        y = 0;
        scale = 1;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void setScale(float scale) {
        this.scale = Mth.clamp(scale, MIN_SCALE, MAX_SCALE);
    }

    public float getScale() {
        return scale;
    }

    public int getScaledX() {
        int screenWidth = client.getWindow().getGuiScaledWidth();
        return (int) (x * screenWidth / scale);
    }

    public int getScaledY() {
        int screenHeight = client.getWindow().getGuiScaledHeight();
        return (int) (y * screenHeight / scale);
    }

    public void centerHorizontally(Window window) {
        int windowWidth = window.getGuiScaledWidth();
        x = (windowWidth - getWidth() * scale) / (windowWidth * 2.0);
    }

    public void centerVertically(Window window) {
        int windowHeight = window.getGuiScaledHeight();
        y = (windowHeight - getHeight() * scale) / (windowHeight * 2.0);
    }

    public double calcXSnap(double scaledX, Window window) {
        int windowWidth = window.getGuiScaledWidth();

        double diff = scaledX - (windowWidth / 2.0);

        double mod = (Math.abs(diff) % Constants.GRID_SIZE);
        if (mod > Constants.GRID_SIZE / 2.0) {
            mod -= Constants.GRID_SIZE;
        }

        if (diff >= 0) {
            return (scaledX - mod) / windowWidth;
        } else {
            return (scaledX + mod) / windowWidth;
        }
    }

    public double calcYSnap(double scaledY, Window window) {
        int windowHeight = window.getGuiScaledHeight();

        double diff = scaledY - (windowHeight / 2.0);

        double mod = (Math.abs(diff) % Constants.GRID_SIZE);
        if (mod > Constants.GRID_SIZE / 2.0) {
            mod -= Constants.GRID_SIZE;
        }

        if (diff >= 0) {
            return (scaledY - mod) / windowHeight;
        } else {
            return (scaledY + mod) / windowHeight;
        }
    }

    public void snapToGrid(Window window) {
        int windowWidth = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();
        double scaledX = x * windowWidth;
        double scaledY = y * windowHeight;

        x = calcXSnap(scaledX, window);
        y = calcYSnap(scaledY, window);

    }

    public boolean inBounds(int mouseX, int mouseY) {
        double screenX = x * client.getWindow().getGuiScaledWidth();
        double screenY = y * client.getWindow().getGuiScaledHeight();

        return screenX <= mouseX && mouseX <= screenX + (getWidth() * scale)
                && screenY <= mouseY && mouseY <= screenY + (getHeight() * scale);
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty("x", x);
        object.addProperty("y", y);
        object.addProperty("scale", scale);
    }

    @Override
    public void load(JsonObject object) {
        x = object.get("x").getAsDouble();
        y = object.get("y").getAsDouble();
        scale = object.get("scale").getAsFloat();
    }
}
