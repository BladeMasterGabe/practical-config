package config.practical.utilities;

import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import static config.practical.utilities.Constants.CORNER_RADIUS;
import static config.practical.utilities.Constants.LINE_THICKNESS;

public class DrawHelper {

    private static final int BORDER_COLOR = 0xff000000;

    private static final Identifier TOP_LEFT = Identifier.of(Constants.NAMESPACE, "top-left");
    private static final Identifier TOP_RIGHT = Identifier.of(Constants.NAMESPACE, "top-right");
    private static final Identifier BOTTOM_LEFT = Identifier.of(Constants.NAMESPACE, "bottom-left");
    private static final Identifier BOTTOM_RIGHT = Identifier.of(Constants.NAMESPACE, "bottom-right");

    private static final Identifier TOP_LEFT_FILLED = Identifier.of(Constants.NAMESPACE, "top-left-filled");
    private static final Identifier TOP_RIGHT_FILLED = Identifier.of(Constants.NAMESPACE, "top-right-filled");
    private static final Identifier BOTTOM_LEFT_FILLED = Identifier.of(Constants.NAMESPACE, "bottom-left-filled");
    private static final Identifier BOTTOM_RIGHT_FILLED = Identifier.of(Constants.NAMESPACE, "bottom-right-filled");

    public static void drawBackground(DrawContext context, int x, int y, int width, int height, int color) {
        if (width < CORNER_RADIUS * 2 || height < CORNER_RADIUS * 2) {
            return;
        }

        //corners
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TOP_LEFT, x, y, CORNER_RADIUS, CORNER_RADIUS, Constants.WHITE_COLOR);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TOP_RIGHT, x + width - CORNER_RADIUS, y, CORNER_RADIUS, CORNER_RADIUS, Constants.WHITE_COLOR);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BOTTOM_LEFT, x, y + height - CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, Constants.WHITE_COLOR);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BOTTOM_RIGHT, x + width - CORNER_RADIUS, y + height - CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, Constants.WHITE_COLOR);

        //corners filled
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TOP_LEFT_FILLED, x, y, CORNER_RADIUS, CORNER_RADIUS, color);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, TOP_RIGHT_FILLED, x + width - CORNER_RADIUS, y, CORNER_RADIUS, CORNER_RADIUS, color);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BOTTOM_LEFT_FILLED, x, y + height - CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, color);
        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, BOTTOM_RIGHT_FILLED, x + width - CORNER_RADIUS, y + height - CORNER_RADIUS, CORNER_RADIUS, CORNER_RADIUS, color);


        //border
        context.fill(x + CORNER_RADIUS, y, x + width - CORNER_RADIUS, y + LINE_THICKNESS, BORDER_COLOR);
        context.fill(x, y + CORNER_RADIUS, x + LINE_THICKNESS, y + height - CORNER_RADIUS, BORDER_COLOR);
        context.fill(x + CORNER_RADIUS, y + height - LINE_THICKNESS, x + width - CORNER_RADIUS, y + height, BORDER_COLOR);
        context.fill(x + width - LINE_THICKNESS, y + CORNER_RADIUS, x + width, y + height - CORNER_RADIUS, BORDER_COLOR);

        //background / filled color
        context.fill(x + CORNER_RADIUS, y + LINE_THICKNESS, x + width - CORNER_RADIUS, y + height - LINE_THICKNESS, color);
        context.fill(x + LINE_THICKNESS, y + CORNER_RADIUS, x + CORNER_RADIUS, y + height - CORNER_RADIUS, color);
        context.fill(x + width - CORNER_RADIUS, y + CORNER_RADIUS, x + width - LINE_THICKNESS, y + height - CORNER_RADIUS, color);
    }

    public static void drawBackground(DrawContext context, int x, int y, int width, int height) {
        drawBackground(context, x, y, width, height, Constants.BACKGROUND_COLOR);
    }

    public static void drawBorder(DrawContext context, int x, int y, int width, int height, int color) {
        context.drawHorizontalLine(x, x + width, y, color);
        context.drawHorizontalLine(x, x + width, y + height - 1, color);
        context.drawVerticalLine(x, y, y + height, color);
        context.drawVerticalLine(x + width - 1, y, y + height, color);
    }
}
