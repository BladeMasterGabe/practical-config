package config.practical.hud;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class SimpleHUDComponent extends HUDComponent {

    private final int width, height;

    private transient final ConditionSupplier conditionSupplier;
    private transient final RenderSupplier renderSupplier;
    private transient final EditSupplier editSupplier;

    /**
     * x, y goes from 0 to 1 and get scaled
     * up using the Window with getScaledWidth
     * and get getScaledHeight
     *
     * @param x                 0 to 1
     * @param y                 0 to 1
     * @param width             int
     * @param height            int
     * @param scale             a scale that's between MIN_SCALE and MAX_SCALE
     * @param info              text info that will display when its selected
     * @param conditionSupplier the condition to render
     * @param renderSupplier    the function that is used to render it
     */
    public SimpleHUDComponent(double x, double y, int width, int height, float scale, String info, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier, @NotNull EditSupplier editSupplier) {
        super(x, y, scale, info);
        this.width = width;
        this.height = height;

        this.conditionSupplier = conditionSupplier;
        this.renderSupplier = renderSupplier;
        this.editSupplier = editSupplier;
    }

    //backwards compatibility
    @SuppressWarnings("unused")
    public SimpleHUDComponent(double x, double y, int width, int height, float scale, String info, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier) {
        this(x, y, width, height, scale, info, conditionSupplier, renderSupplier, () -> true);
    }

    //backwards compatibility
    @SuppressWarnings("unused")
    public SimpleHUDComponent(double x, double y, int width, int height, float scale, @NotNull ConditionSupplier conditionSupplier, @NotNull RenderSupplier renderSupplier) {
        this(x, y, width, height, scale, "", conditionSupplier, renderSupplier, () -> true);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean editable() {
        return editSupplier.shouldBeEditable();
    }

    @Override
    public boolean shouldRender() {
        return conditionSupplier.shouldRender();
    }

    @Override
    public List<HUDCategory> categories() {
        return null;
    }


    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics) {
        renderSupplier.render(this, graphics);
    }

    public interface ConditionSupplier {
        /**
         * Will be called when the function render is called
         * Make use of it so the component only renders
         * when you want it to render
         *
         * @return true if it should render, else false
         */
        boolean shouldRender();
    }

    public interface RenderSupplier {
        /**
         * Will be called in function render
         * if ConditionSupplier returns true
         * NOTE: component is the component itself
         * and scaledX and scaledY should be used
         * for the x and y position
         *
         * @param component the component itself
         * @param graphics  GuiGraphics
         */
        void render(SimpleHUDComponent component, GuiGraphicsExtractor graphics);
    }

    public interface EditSupplier {
        /**
         * Used to determine if the component
         * should render while the user is in the
         * ComponentEditScreen
         *
         */
        boolean shouldBeEditable();
    }
}
