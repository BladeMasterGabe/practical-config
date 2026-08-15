package config.practical.hud;

import java.util.ArrayList;

public record HUDCategory(String name) {

    private static final ArrayList<HUDCategory> categories = new ArrayList<>();
    private static int index = 0;

    public HUDCategory(String name) {
        this.name = name;
        if (categories.contains(this)) return;
        categories.add(this);
    }


    public static HUDCategory getNext() {
        index = (index + 1) % (categories.size() + 1);
        if (index == 0) return null;
        return categories.get(index - 1);
    }

    public static void reset() {
        index = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HUDCategory other) {
            return other.name.equals(this.name);
        }

        return false;
    }
}
