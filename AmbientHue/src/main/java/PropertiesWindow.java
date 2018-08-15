import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * A window containing the properties
 */
public class PropertiesWindow extends Stage {
    // Properties values
    private SimpleStringProperty httpURL, httpsURL;
    private SimpleBooleanProperty httpPost, httpsPost;

    // GUI items
    private RadioButton httpRadioButton, httpsRadioButton;

    // The action executed on stage's close
    private ActionIF onClose;
    // The test status
    private int testStatus;

    // The button's CSS
    public static final String PRESSED_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.4);",
            NORMAL_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.2);",
            FOUND_STYLE = "-fx-background-color: rgba(99,153,61,0.8);",
            ERROR_STYLE = "-fx-background-color: rgba(230,0,11,0.4);";

    /**
     * Constructor
     * @param onClose the action executed on stage's close
     */
    public PropertiesWindow(ActionIF onClose){
        super();
        this.onClose = onClose;

        // Set the stage and the main environment
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        Scene scene = new Scene(vBox, 460, 300);
        setScene(scene);
        vBox.setPadding(new Insets(30, 30, 30, 30));

        setTitle(Constants.NAME + " - Properties");
        setResizable(false);


        // Get the properties
        httpURL = new SimpleStringProperty(Main.properties.getUrlHTTP());
        httpsURL = new SimpleStringProperty(Main.properties.getUrlHTTPS());
        httpPost = new SimpleBooleanProperty(Main.properties.isPostHTTP());
        httpsPost = new SimpleBooleanProperty(Main.properties.isPostHTTPS());

        // Set the test status
        testStatus = 0;

        // Main components
            // RadioButtons and ToggleButtons
        ToggleGroup protocolToggleGroup = new ToggleGroup();

        httpRadioButton = new RadioButton("HTTP");
        httpRadioButton.setToggleGroup(protocolToggleGroup);
        httpRadioButton.setSelected(!Main.properties.isHttps());

        httpsRadioButton = new RadioButton("HTTPS (Proxy)");
        httpsRadioButton.setToggleGroup(protocolToggleGroup);
        httpsRadioButton.setSelected(!httpRadioButton.isSelected());

            // Bottom buttons
                // TEST BUTTON
        Button testButton = new Button("Test");
        testButton.prefWidthProperty().bind(scene.widthProperty().divide(2));
        testButton.setOnAction(event -> {
            try {
                // Save the current properties
                Properties clone = new Properties(Main.properties);

                // Save the new one
                save();

                // Test the lamp
                    // Set it off
                Utils.send(0, 0, 0);
                Thread.sleep(500);
                    // Set it on
                Utils.send(0, 0, 100);
                Thread.sleep(500);
                    // Set it off
                Utils.send(0, 0, 0);
                // Get the test status: success
                testStatus = 1;

                Main.properties = new Properties(clone);
            }
            catch(InterruptedException exc){
                // The Thread.sleep is done after changing the lamp status at least one time. So if this error occurs, the lamp works
                // But still, do not update the testStatus to be sure
                exc.printStackTrace();
            }
            catch(Exception exc){
                exc.printStackTrace();
                // Get the test status: failed
                testStatus = -1;
            }
        });

        testButton.setStyle(NORMAL_STYLE);
        testButton.setOnMousePressed(event -> testButton.setStyle(PRESSED_STYLE));
        testButton.setOnMouseReleased(event -> testButton.setStyle(testStatus == -1 ? ERROR_STYLE : FOUND_STYLE));
                // MULTIPLE LAMPS BUTTON
        Button lampsButton = new Button("Lamps");
        lampsButton.prefWidthProperty().bind(scene.widthProperty().divide(2));
        lampsButton.setStyle(NORMAL_STYLE);
        lampsButton.setOnMousePressed(event -> lampsButton.setStyle(PRESSED_STYLE));
        lampsButton.setOnMouseReleased(event -> {
            new LampsWindow();
            lampsButton.setStyle(NORMAL_STYLE);
        });
        lampsButton.setOnAction(event -> save() );
                // SAVE BUTTON
        Button saveButton = new Button("Save");
        saveButton.prefWidthProperty().bind(scene.widthProperty().divide(2));
        saveButton.setStyle(NORMAL_STYLE);
        saveButton.setOnMousePressed(event -> saveButton.setStyle(PRESSED_STYLE));
        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(saveButton.styleProperty(), FOUND_STYLE, Interpolator.LINEAR)),
                new KeyFrame(Duration.seconds(1), new KeyValue(saveButton.styleProperty(), NORMAL_STYLE, Interpolator.LINEAR))
        );
        saveButton.setOnMouseReleased(event -> flash.play());
        saveButton.setOnAction(event -> save() );
                // CANCEL BUTTON
        Button cancelButton = new Button("Cancel");
        cancelButton.prefWidthProperty().bind(scene.widthProperty().divide(2));
        cancelButton.setOnAction(event -> {
            onClose.run();
            close();
        });

        cancelButton.setStyle(NORMAL_STYLE);
        cancelButton.setOnMousePressed(event -> cancelButton.setStyle(PRESSED_STYLE));
        cancelButton.setOnMouseReleased(event -> cancelButton.setStyle(ERROR_STYLE));
        setOnCloseRequest(event -> onClose.run());

            // Pack
        HBox lampsHBox = new HBox();
        lampsHBox.getChildren().addAll(testButton, lampsButton);
        HBox exitHBox = new HBox();
        exitHBox.getChildren().addAll(cancelButton, saveButton);

        vBox.getChildren().addAll(
                httpRadioButton,
                getProtocolHBox(scene, false),
                GUIUtils.vStrut(20),

                httpsRadioButton,
                getProtocolHBox(scene, true),
                GUIUtils.vStrut(20),

                lampsHBox,
                exitHBox
        );

        // Show the window
        show();
    }

    /**
     * Save the properties
     */
    public void save() {
        // Set the properties from the instance variables
        Main.properties.setHttps(
                httpsRadioButton.isSelected()
        );
        Main.properties.setUrlHTTPS(
                httpsURL.get()
        );
        Main.properties.setPostHTTPS(
                httpsPost.get()
        );
        Main.properties.setUrlHTTP(
                httpURL.get()
        );
        Main.properties.setPostHTTP(
                httpPost.get()
        );

        // Save the properties
        try {
            Serializer.serialize(Main.properties, Path.PROPERTIES.getParent(), Path.PROPERTIES.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get an HBox containing a textfield and some toggle buttons. The textfield is used to get an URL and
     * the toggleButtons to chose the used request (POST and PUT, more coming soon)
     * @param scene the scene
     * @param https if this is the HTTPS part. If not, {https=false}
     * @return the HBox
     */
    private HBox getProtocolHBox(Scene scene, boolean https){
        // TextField
        TextField urlTextField = new TextField( ( https ? Main.properties.getUrlHTTPS() : Main.properties.getUrlHTTP() ) );
        (https ? httpsURL : httpURL).bind(urlTextField.textProperty());
        urlTextField.prefWidthProperty().bind(scene.widthProperty().divide(3).multiply(2));

        // ToggleButtons
        ToggleGroup requestGroup = new ToggleGroup();

        ToggleButton postToggleButton = new ToggleButton("Post");
        postToggleButton.setToggleGroup(requestGroup);
        postToggleButton.setSelected( ( https ? Main.properties.isPostHTTPS() : Main.properties.isPostHTTP() ) );

        ToggleButton putToggleButton = new ToggleButton("Put");
        putToggleButton.setToggleGroup(requestGroup);
        putToggleButton.setSelected(!postToggleButton.isSelected());

        (https ? httpsPost : httpPost).bind(postToggleButton.selectedProperty());

        // Pack
        HBox hBox = new HBox();
        hBox.getChildren().addAll(
                GUIUtils.getHSpacer(),
                urlTextField,
                GUIUtils.hStrut(10)
        );
        // Post HTTP and Put HTTPS coming up in a future update
        // TODO: add HTTP post and HTTPS put requests
        putToggleButton.selectedProperty().addListener(event -> putToggleButton.setSelected(true));
        postToggleButton.selectedProperty().addListener(event -> postToggleButton.setSelected(true));
        hBox.getChildren().add(
                ( https ? postToggleButton : putToggleButton )
        );
        // ----------------------------------------------------

        hBox.getChildren().add(
                GUIUtils.getHSpacer()
        );

        return hBox;
    }
}
