import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.InputStream;

/**
 * Some usefull methods to spare time making GUI's with JavaFX
 */
public class GUIUtils {
    /**
     * Get an image
     * @param name the image's name
     * @return the image as InputStream
     */
    public static InputStream getImage(String name){
        return GUIUtils.class.getClassLoader().getResourceAsStream("res/" + name + ".png");
    }

    /**
     * A Region taking all the available place
     * @return the region
     */
    public static Region getHSpacer(){
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setPickOnBounds(false);

        return spacer;
    }
    /**
     * A Region taking all the available place
     * @return the region
     */
    public static Region getVSpacer(){
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        spacer.setPickOnBounds(false);

        return spacer;
    }

    /**
     * A Region with a certain width
     * @param size the width of the region in pixels
     * @return the region
     */
    public static Region hStrut(int size){
        Region region = new Region();
        region.setPrefWidth(size);
        return region;
    }
    /**
     * A Region with a certain height
     * @param size the height of the region in pixels
     * @return the region
     */
    public static Region vStrut(int size){
        Region region = new Region();
        region.setPrefHeight(size);
        return region;
    }

    public static HBox hCenter(Node node){
        HBox hBox = new HBox();
        hBox.getChildren().addAll(getHSpacer(), node, getHSpacer());
        return hBox;
    }
    public static VBox vCenter(Node node){
        VBox vBox = new VBox();
        vBox.getChildren().addAll(getVSpacer(), node, getVSpacer());
        return vBox;
    }
}
