package config.practical.utilities;

import java.text.DecimalFormat;
import java.util.logging.Logger;

public class Constants {
    public static final String NAMESPACE = "practical-config";

    public static final Logger LOGGER = Logger.getLogger(NAMESPACE);

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static final int WIDGET_WIDTH = 300;
    public static final int TEXT_HEIGHT = 8;
    public static final int TEXT_PADDING = 6;

    public static final int LINE_THICKNESS = 2;
    public static final int CORNER_RADIUS = 7;

    public static final int WHITE_COLOR = 0xffffffff;

    public static final int BACKGROUND_COLOR = 0xff999999;

    public static final int GRID_SIZE = 20;
}
