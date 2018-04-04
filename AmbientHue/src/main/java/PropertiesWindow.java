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

public class PropertiesWindow extends Stage {
    private SimpleStringProperty httpURL, httpsURL;
    private SimpleBooleanProperty httpPost, httpsPost;

    private RadioButton httpRadioButton, httpsRadioButton;

    private ActionIF onClose;
    private int testStatus;

    public static final String PRESSED_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.4);",
            NORMAL_STYLE = "-fx-background-color: rgb(0, 0, 0, 0.2);",
            FOUND_STYLE = "-fx-background-color: rgba(99,153,61,0.8);",
            ERROR_STYLE = "-fx-background-color: rgba(230,0,11,0.4);";

    public PropertiesWindow(ActionIF onClose){
        super();
        this.onClose = onClose;

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        Scene scene = new Scene(vBox, 460, 300);
        setScene(scene);
        vBox.setPadding(new Insets(30, 30, 30, 30));

        setTitle(Constants.NAME + " - Properties");
        setResizable(false);


        httpURL = new SimpleStringProperty(Main.properties.getUrlHTTP());
        httpsURL = new SimpleStringProperty(Main.properties.getUrlHTTPS());
        httpPost = new SimpleBooleanProperty(Main.properties.isPostHTTP());
        httpsPost = new SimpleBooleanProperty(Main.properties.isPostHTTPS());


        testStatus = 0;
        ToggleGroup protocolToggleGroup = new ToggleGroup();

        httpRadioButton = new RadioButton("HTTP");
        httpRadioButton.setToggleGroup(protocolToggleGroup);
        httpRadioButton.setSelected(!Main.properties.isHttps());

        httpsRadioButton = new RadioButton("HTTPS (Proxy)");
        httpsRadioButton.setToggleGroup(protocolToggleGroup);
        httpsRadioButton.setSelected(!httpRadioButton.isSelected());

        Button testButton = new Button("Test");
        testButton.prefWidthProperty().bind(scene.widthProperty());
        testButton.setOnAction(event -> {
            try {
                Properties clone = new Properties(Main.properties);

                save();

                Utils.send(0, 0, 0);
                Thread.sleep(500);
                Utils.send(0, 0, 100);
                Thread.sleep(500);
                Utils.send(0, 0, 0);
                testStatus = 1;

                Main.properties = new Properties(clone);
            }
            catch(InterruptedException exc){
                exc.printStackTrace();
            }
            catch(Exception exc){
                exc.printStackTrace();
                testStatus = -1;
            }
        });

        testButton.setStyle(NORMAL_STYLE);
        testButton.setOnMousePressed(event -> testButton.setStyle(PRESSED_STYLE));
        testButton.setOnMouseReleased(event -> testButton.setStyle(testStatus == -1 ? ERROR_STYLE : FOUND_STYLE));
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

        HBox buttonHBox = new HBox();
        buttonHBox.getChildren().addAll(cancelButton, saveButton);

        vBox.getChildren().addAll(
                httpRadioButton,
                getProtocolHBox(scene, false),
                GUIUtils.vStrut(20),

                httpsRadioButton,
                getProtocolHBox(scene, true),
                GUIUtils.vStrut(20),

                testButton,
                buttonHBox
        );

        show();
    }

    public void save() {
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

        try {
            Serializer.serialize(Main.properties, Path.PROPERTIES.getParent(), Path.PROPERTIES.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private HBox getProtocolHBox(Scene scene, boolean https){
        TextField urlTextField = new TextField( ( https ? Main.properties.getUrlHTTPS() : Main.properties.getUrlHTTP() ) );
        (https ? httpsURL : httpURL).bind(urlTextField.textProperty());
        urlTextField.prefWidthProperty().bind(scene.widthProperty().divide(3).multiply(2));

        ToggleGroup requestGroup = new ToggleGroup();

        ToggleButton postToggleButton = new ToggleButton("Post");
        postToggleButton.setToggleGroup(requestGroup);
        postToggleButton.setSelected( ( https ? Main.properties.isPostHTTPS() : Main.properties.isPostHTTP() ) );

        ToggleButton putToggleButton = new ToggleButton("Put");
        putToggleButton.setToggleGroup(requestGroup);
        putToggleButton.setSelected(!postToggleButton.isSelected());

        (https ? httpsPost : httpPost).bind(postToggleButton.selectedProperty());

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
