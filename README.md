# Practical-Config

A Minecraft Fabric utility mod that seeks to make configuring more practical, both for the developer but also the user.

# Installing

to install and start using Practical-config add the given code lines below to the build.gradle file. Put it in to the
repositories scope
and then after add the next code block to the dependencies scope.

```groovy
repositories {
    ...
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    ...
}
```

```groovy
dependencies {
    ...
    include modImplementation("com.github.BladeMasterGabe:practical-config:${project.the_version_you_are_using}")
    ...
}
```

After that refresh your dependencies, and it should appear in the projects' library.

# Saving values

To make a configurable value use the annotation @ConfigValue to annotate that this value should be saved.
Make sure that the value is both static and public to ensure that it can be later on read by the ConfigManager.
<br>Note: It currently has a high chance of breaking on custom objects that gets saved. Make sure to also not repeat the
same name of 2 different variables that should be saved.

```Java

@ConfigValue
public static boolean someBoolean = false;
```

# Managing Values

To manage all the configurable values the use the ConfigManager. It's created by first giving it a path of where to save
the file
and a list of classes it should look inside to save and load values from.

```Java

@ConfigValue
public static final ConfigManager manager = new ConfigManager("./config/some-fíle-name.json",
        List.of(SomeClass.class, AnotherClass.class));
```

To now load and save the values use the save() and load() functions built in the ConfigManagers class. <br>Note that
when the screen later on closes it will automatically save all values. However, you still need to run load() at the
start of your project.

```Java

@Override
public void onInitialize() {
    Config.manager.load();
    ...
```

# Creating a HudComponent

To add moveable components to the screen use a HUDComponent

```Java
public HUDComponent(double x, double y, int width, int height, float scale, String info, @NotNull ConditionSupplier
        conditionSupplier, @NotNull RenderSupplier renderSupplier)
```

To create the component it needs both an x, y position that's a value from 0 to 1 which is a scaled position on the
screen. You also need to specify a width, height and its initial scale. You can include a string that will be shown when
selected in the editing phase to make it easier for the user to identify what the component does. <br> <br>
After that there are 2 interfaces.
The first one ConditionSupplier is just boolean supplier that will check when the component should be drawn on the
screen. The second one RenderSupplier is the one that will be called on to render the component. There you include what
is supposed to be drawn on the screen <br> Note: To draw stuff correctly use the functions getScaledX() and getScaledY()
to get the correct x and y values when drawing the component.

```Java

@ConfigValue
public static HUDComponent textOnTheScreen = new HUDComponent(0, 0, 120, 10, 1,
        () -> true,
        ((hudComponent, drawContext) -> {
            int x = hudComponent.getScaledX();
            int y = hudComponent.getScaledY();

            drawContext.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("Hello world"), x, y, 0xffffffff, true);
        })
);
```

# Making the screen

<h3>(subject to change in the future)</h3>
To create the screen use the class ConfigurableScreen. To create one give the object the arguments:
<ul>
<li>The title for the screen</li>
<li>A possible parent screen to fall back to when the user closes the screen (use null when you don't want this) </li>
<li>The ConfigManager that should auto save when the user closes the screen.</li>
</ul>

```Java
ConfigurableScreen screen = new ConfigurableScreen(TITLE, parent, manager);
```

# Adding widgets to the screen

<h3>(subject to change in the future)</h3>
To add a widget to the screen you first have to create a category in which to store the widgets.
After that you can add all the widgets you want for that specific category.
Afterward add the category to the ConfigurableScreen.

```Java
ConfigurableScreen screen = new ConfigurableScreen(TITLE, parent, manager);

ConfigCategory myCategory = new ConfigCategory("Some title");
myCategory.add(new ConfigBool(Text.literal("Some name"), ()->myVaribleToChange,newValue ->myVaribleToChange =newValue));

screen.addCategory(myCategory);
```

# Opening a Screen

There are multiple ways to let the user open a screen one of the ways is to use a keybind. Here is some boilerplate code
on how you can quickly implement that using basic built in fabric/minecraft stuff.

```Java
 private static KeyBinding openConfig;

public static void register() {

    openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "opens Config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            CATEGORY));

    ClientTickEvents.END_CLIENT_TICK.register(Keybinds::checkInputs);
}

public static void checkInputs(MinecraftClient client) {

    if (openConfig.wasPressed()) {
        client.setScreen(Config.createScreen(null));
    }
}
```

# Premade widgets

Note: You can make your own widgets and use them as long as they extend ClickableWidget
<h3>Boolean toggle</h3>
Used to toggle a boolean value

```Java
public ConfigBool(Text message, Supplier<Boolean> supplier, Consumer<Boolean> consumer)
```

<h3>Sliders</h3>
A premade slider for doubles, floats and integers. <br>Note the user can move the slider by 1 step using the left and
right arrow key

```Java
public ConfigDouble(Text message, Supplier<Double> supplier, Consumer<Double> consumer, double step, double min, double max)

public ConfigFloat(Text message, Supplier<Float> supplier, Consumer<Float> consumer, float step, float min, float max)

public ConfigInt(Text message, Supplier<Integer> supplier, Consumer<Integer> consumer, int step, int min, int max)
```

<h3>Color picker</h3>
A color picker that lets the user pick a color in the hsb format

```Java
public ConfigColor(Text message, Supplier<Integer> supplier, Consumer<Integer> consumer, String identifier, boolean transparency)
```

<h3>Enum / options selector</h3>
A built-in options selector that lets you choice between some alternatives
Give it a list of options that it will let the user choice from.
<br>Note: The objects toString() method is what gets typed into each option

```Java
public ConfigOptions(Text message, T[] options, Supplier<T> supplier, Consumer<T> consumer)
```

<h4>Example ConfigOption</h4>

```Java
public enum TimerType {
    TICK_TIME("Tick time"), DIFFRENCE("difference");

    private final String label;

    TimerType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

@ConfigValue
public static TimerType timerType = TimerType.TICK_TIME;

...

ConfigOption tickTimerType = new ConfigOptions<>(Text.literal("Tick timer type"), TimerType.values(), () -> timerType, type -> timerType = type);
```

# Adding Sections

To separate your widgets into more cohesive order use a ConfigSection. It takes it a title and then lets you populate it
with widgets that you feel goes together.

```Java
ConfigSection mySection = new ConfigSection(Text.literal("Some title"));

mySection.add(new ConfigBool(Text.literal("Some settings title"), ()->someSetting,bool ->someSetting =bool));
mySection.add(new ConfigBool(Text.literal("Another settings title"), ()->anotherSetting,bool ->anotherSetting =bool));

myCategory.add(mySection);
```