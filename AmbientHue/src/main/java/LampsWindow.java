import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LampsWindow extends Stage {
    public LampsWindow(){
        super();

        Label hint1Label = new Label("Use " + Properties.LAMP_REPLACER + " in the URL to use the lamps.");
        Label hint2Label = new Label("Separate the lamps index by \',\' in the text field below.");

        TextField lampField = new TextField(Main.properties.getLamps());
        lampField.setPromptText("ex: 1,5,6,10");
        lampField.prefWidthProperty().bind(this.widthProperty().multiply(2).divide(3));

        Button button = new Button("Close");
        button.prefWidthProperty().bind(this.widthProperty());
        button.requestFocus();
        button.setOnAction(event -> {
            Main.properties.setLamps(lampField.getText());
            close();
        });
        button.setStyle(PropertiesWindow.NORMAL_STYLE);
        button.setOnMousePressed(event -> button.setStyle(PropertiesWindow.PRESSED_STYLE));
        button.setOnMouseReleased(event -> button.setStyle(PropertiesWindow.NORMAL_STYLE));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                GUIUtils.getVSpacer(),
                GUIUtils.hCenter(hint1Label),
                GUIUtils.hCenter(hint2Label),
                GUIUtils.vStrut(10),
                GUIUtils.hCenter(lampField),
                GUIUtils.getVSpacer(),
                button
        );

        vBox.setSpacing(10);
        Scene scene = new Scene(vBox, 460, 200);
        setScene(scene);

        // Show the window
        show();
    }
}
