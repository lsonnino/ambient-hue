import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;

/**
 * The main GUI
 */
public class GUI extends StackPane {
    public static final String START = "Start", STOP = "Stop"; // The text displayed on the {startButton}
    // The CSS style of the {startButton}
    public static final String PRESSED_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.4);",
            START_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.2);",
            STOP_STYLE = "-fx-background-color: rgb(66, 134, 244, 0.4);";

    // Tells if the hue's color must be updated
    private boolean running;

    // Initializing the elements that changes if the hue's color is updated
    private Button startButton;
    private ImageView backgroundImageView;

    // The left-right padding of the whole GUI
    public static final int PADDING = 100; // value in px

    /**
     * Constructor of the GUI class
     * @param timer the action to perform every x milliseconds. This action is performed when the {startButton} is pressed one time.
     *              If it is pressed a second time, the action stops.
     *              In this programs, the action is simply "update the hue's color to match the screen's colors"
     */
    public GUI(ExtendedTimer timer){
        super();
        // Initialize variables
        this.running = false;

        // Set the background image
        backgroundImageView = new ImageView(new Image(getBackgroundImage()));
        backgroundImageView.fitHeightProperty().bind(heightProperty());
        backgroundImageView.fitWidthProperty().bind(widthProperty());
        backgroundImageView.setPreserveRatio(true);



        // Initialize the BorderPane containing all the GUI components except the settings button and the background image
        BorderPane borderPane = new BorderPane();

        // Initialize the start/stop button
        startButton = new Button(START);
            // Set the events of the button
        startButton.setOnMousePressed(event -> startButton.setStyle(PRESSED_STYLE));
        startButton.setOnMouseReleased(event -> startButton.setStyle(running ? STOP_STYLE : START_STYLE));
        startButton.setOnAction(event -> {
            // Change the button's text from "Start" to "Stop" or vice-versa
            startButton.setText(running ? START : STOP);
            // Invert the running variable: start or stop the hue to change
            running = !running;
            // Tells the timer the new value
            timer.setRunning(running);
            // Update the background image
            updateBackground();
        });
            // Set the button's style
        startButton.setStyle(START_STYLE);

        // Initialize the brightness factor slider and the period slider
        // Then bind it's value to the timer or to the Main.briMultiplier value
        Slider brightnessSlider = new Slider(0, 100, 50);
        Main.briMultiplier.bind(brightnessSlider.valueProperty());
        Slider periodSlider = new Slider(100, 2000, 100);
        timer.getPeriod().bind(periodSlider.valueProperty());

        // Initialize the VBox containing all the main elements of the GUI
        VBox vBox = new VBox();
        // Add the elements to the VBox
        vBox.getChildren().addAll(
                GUIUtils.getVSpacer(),
                getSliderLabel("Speed", periodSlider, "ms"), periodSlider,
                GUIUtils.vStrut(10),
                getSliderLabel("Brightness factor", brightnessSlider, "%"), brightnessSlider,
                GUIUtils.getVSpacer()
        );
        // Set the spacing between two elements of the box
        vBox.setSpacing(10); // Spacing in px
        // Set the left-right padding
        vBox.setPadding(new Insets(0, PADDING, 0, PADDING));

        // Initialize the settings label
        ImageView settingsImageView = new ImageView(new Image(GUIUtils.getImage("Settings")));
        // Set the {settingsImageView}'s listener
        settingsImageView.setOnMouseClicked(event -> {
            // Rotate the icon 100 degrees to the left
            settingsImageView.setRotate(100);
            // Open the properties panel and tell him to rotate the settings icon to normal when closed
            PropertiesWindow propertiesWindow = new PropertiesWindow(() -> settingsImageView.setRotate(0));
        });
        // Set the size of the icon
        settingsImageView.setPreserveRatio(true);
        settingsImageView.setFitHeight(20);
        // Get clicks on the bounds of the icon, not only where the alpha component of the image is not null
        settingsImageView.setPickOnBounds(true);
        // Place the {settingsImageView} to the top right of the window
        HBox settingsHBox = new HBox();
        settingsHBox.getChildren().addAll(GUIUtils.getHSpacer(), settingsImageView);
        VBox settingsVBox = new VBox();
        settingsVBox.getChildren().addAll(settingsHBox, GUIUtils.getVSpacer());
        settingsVBox.setPadding(new Insets(10, 20, 20, 20));
            // Make those boxes unclickable. So that the events passes throw the boxes
        settingsHBox.setPickOnBounds(false);
        settingsVBox.setPickOnBounds(false);

        // Fill the window
        borderPane.setCenter(vBox);
            // Set the {startButton}'s size to that it fills horizontally the window
        startButton.setPrefWidth(Double.MAX_VALUE);
        borderPane.setBottom(startButton);

        getChildren().addAll(backgroundImageView, borderPane, settingsVBox);
    }

    /**
     * Update the background image
     * Executed when {startButton} is pressed
     */
    private void updateBackground(){
        backgroundImageView.setImage(new Image(getBackgroundImage()));
    }

    /**
     * Get the background image according to {running}
     * @return the image
     */
    public InputStream getBackgroundImage(){
        return GUIUtils.getImage("Background_bulb" + (running ? "_run" : ""));
    }

    /**
     * Gives a HBox containing a label explaining what the slider is, a label containing the value of the slider in a given unit
     * and a slider
     * @param text the text explaining what the slider is
     * @param slider the slider
     * @param unit the units of the slider (for instance "ms" for milliseconds of "%" for %)
     * @return the HBox containing those components packed
     */
    private HBox getSliderLabel(String text, Slider slider, String unit){
        Label label = new Label(text);
        Label valueLabel = new Label();
        valueLabel.setText(getValueText((int) slider.getValue(), unit));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            valueLabel.setText(getValueText(value, unit));
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(label, GUIUtils.getHSpacer(), valueLabel);

        return hBox;
    }

    /**
     * Gives a string text containg the value in the given units. For example (5, "ms") returns "5 ms"
     * @param value the value
     * @param unit the unit
     * @return the String contaiing the value and the unit
     */
    private String getValueText(int value, String unit){
        return value + " " + unit;
    }
}
