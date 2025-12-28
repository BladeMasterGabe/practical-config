package config.practical;


import config.practical.category.ConfigCategory;
import config.practical.category.ConfigCategoryList;
import config.practical.manager.ConfigManager;
import config.practical.utilities.Constants;
import config.practical.widgets.abstracts.ConfigParent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3x2fStack;

public class ConfigurableScreen extends Screen {

    private static final int WIDGET_X_OFFSET = 4;

    private static final int LIST_Y_OFFSET = 50;

    private static final int CATEGORY_Y_OFFSET = 10;

    private static final int TITLE_COLOR = 0xffffffff;
    private static final float TITLE_SCALAR = 1.5f;
    private static final int TITLE_Y_OFFSET = 20;

    private final ConfigSearch search;
    private final ConfigCategoryList categories;
    private final ConfigScroll scroll;
    private final ConfigHudEdit hudEdit;

    private final Screen parent;
    private final ConfigManager manager;

    /**
     * The main screen of practical-config
     * To add widgets make a ConfigCategory
     * and add widgets to the category which
     * you then register to the screen
     * @param title The title that will be displayed
     * @param parent A screen that will be the parent of this
     * @param manager The configManager that should save when the screen is closed
     */
    public ConfigurableScreen(Text title, Screen parent, @NotNull ConfigManager manager) {
        super(title);

        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();

        scroll = new ConfigScroll(0, LIST_Y_OFFSET, window.getScaledWidth(), window.getScaledHeight() - LIST_Y_OFFSET, Constants.WIDGET_WIDTH + 100);
        search = new ConfigSearch(client.textRenderer, WIDGET_X_OFFSET, (LIST_Y_OFFSET - ConfigSearch.HEIGHT) / 2, this::updateScroll);
        categories = new ConfigCategoryList(this, WIDGET_X_OFFSET, LIST_Y_OFFSET + CATEGORY_Y_OFFSET);
        hudEdit = new ConfigHudEdit(this, window.getScaledWidth() - ConfigHudEdit.WIDTH - WIDGET_X_OFFSET, (LIST_Y_OFFSET - ConfigHudEdit.HEIGHT) / 2);

        this.parent = parent;
        this.manager = manager;
    }

    @SuppressWarnings("unused")
    public ConfigurableScreen(Text title, @NotNull ConfigManager manager) {
        this(title, null, manager);
    }

    @Override
    protected void init() {
        addDrawableChild(search);
        addDrawableChild(categories);
        addDrawableChild(scroll);
        addDrawableChild(hudEdit);
        update();
    }

    @SuppressWarnings("unused")
    public void addCategory(ConfigCategory category) {
        categories.addCategory(category);
    }

    public void update() {
        updateScroll("");
    }

    public void updateScroll(String searchTerm) {
        scroll.setFocused(null);
        scroll.children().clear();

        for (ClickableWidget widget: categories.searchWidgets(searchTerm.toLowerCase())) {
            if (widget instanceof ConfigParent configParent) {
                configParent.hideAll();
                configParent.update();
            }
            scroll.add(widget);
        }

        scroll.setScrollY(0);
        scroll.hideChildComponents();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        float centerX = (MinecraftClient.getInstance().getWindow().getScaledWidth() - (this.textRenderer.getWidth(this.title) * TITLE_SCALAR)) / 2;
        Matrix3x2fStack stack = context.getMatrices();
        stack.pushMatrix();
        stack.translate(centerX, TITLE_Y_OFFSET);
        stack.scale(TITLE_SCALAR, TITLE_SCALAR);
        context.drawText(this.textRenderer, this.title, 0, 0, TITLE_COLOR, true);
        stack.popMatrix();
    }

    @Override
    public void close() {
        manager.save();
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
