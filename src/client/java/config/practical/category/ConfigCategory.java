package config.practical.category;

import config.practical.widgets.ConfigSection;
import config.practical.widgets.abstracts.ConfigParent;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;

public class ConfigCategory {
    public final String name;
    public final ArrayList<ClickableWidget> widgets;

    /**
     * @param name The name of the category
     */
    public ConfigCategory(String name) {
        this.name = name;
        widgets = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public void add(ClickableWidget widget) {
        if (widget == null) return;
        widgets.add(widget);
    }

    public ArrayList<ClickableWidget> searchWidgets(String term) {

        ArrayList<ClickableWidget> temp = new ArrayList<>();

        for (ClickableWidget widget : widgets) {
            if (widget instanceof ConfigSection section) {
                if (section.contains(term)) {
                    temp.addAll(section.getAllWidgets());
                    temp.add(widget);
                }
            } else {

                String message = widget.getMessage().getString().toLowerCase();
                if (message.contains(term)) {

                    if (widget instanceof ConfigParent parent) {
                        temp.addAll(parent.getAllWidgets());
                    }

                    temp.add(widget);
                }
            }

        }

        return temp;
    }
}
