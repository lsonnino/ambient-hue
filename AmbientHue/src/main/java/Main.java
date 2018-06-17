import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.awt.*;

/**
 * Main class
 */
public class Main extends Application {
    // The factor multiplying the brightness
    public static SimpleDoubleProperty briMultiplier;
    // The properties containing the URL of the light
    public static Properties properties;

    /**
     * The start method launching the program
     * @param primaryStage the main stage given by JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        // Initializing the brightness factor and the properties
        briMultiplier = new SimpleDoubleProperty(50);
        properties = new Properties();

        // If some properties are found, take them. Otherwise, ignore
        try{
            properties = Serializer.deserialize(Path.PROPERTIES, Properties.class);
        }
        catch(Exception ignore){}

        // Create the thread setting the light's hue at a given speed
        ExtendedTimer extendedTimer = new ExtendedTimer();
        extendedTimer.schedule(() -> {
            try {
                // Get the hue, saturation and brightness from the screen
                double[] val = Utils.getColor();

                // Send those informations to the hue
                // The hue is a value between 0 and 65535
                // The saturation is a value between 0 and 254
                // The brightness is a value between 0 and 255
                //      The brightness is multiplied by the brightness factor (which goes from 0 to 100)
                Utils.send((int) (val[0] * 65535), (int) (val[1] * 254), (int) (val[2] * 255 * briMultiplier.doubleValue()/100));
            } catch (AWTException e) {
                // If an error occurs, print it for debug and pass
                e.printStackTrace();
            }
        }, 100);

        // Create the main GUI
        GUI gui = new GUI(extendedTimer);

        // Create the main window
            // Set the application icon
        primaryStage.getIcons().add(new Image(GUIUtils.getImage("ambient_hue")));
        primaryStage.setTitle(Constants.NAME); // Window's title
        primaryStage.setScene(new Scene(gui, 600, 300)); // Window's size and content
        primaryStage.setResizable(false); // The window can't be resized
        // On window's close, stop all the threads and quit
        primaryStage.setOnCloseRequest(event -> {
            extendedTimer.stop();
            System.exit(0);
        });
        // Show the window
        primaryStage.show();
    }

    /**
     * Java main
     * Launches the JavaFX start method
     * @param args the arguments array
     */
    public static void main(String[] args) {
        launch(args);
    }
}
