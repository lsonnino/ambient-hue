import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Some utils used to get the average color of the screen and to send the color to the lamp
 */
public class Utils {
    /**
     * Take a screenshot of the whole screen
     * @return a bBfferedImage containing a screenshot of the whole screen
     * @throws AWTException if an error occurs taking the screenshot
     */
    public static BufferedImage getScreenShot() throws AWTException {
        return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    /**
     * Get the average color of the screen. The average is found using the average RGB values of a screenshot
     * @return an array containing the average hue, saturation and brightness of the color
     * @throws AWTException if an error occurs taking the screenshot
     */
    public static double[] getColor() throws AWTException {
        BufferedImage image = getScreenShot();

        int red = 0;
        int green = 0;
        int blue = 0;
        Color color;

        for (int x = 0 ; x < image.getWidth() ; x++) {
            for (int y = 0 ; y < image.getHeight() ; y++) {
                color = new Color(image.getRGB(x, y));

                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
            }
        }

        red = red/(image.getWidth()*image.getHeight());
        green = green/(image.getWidth()*image.getHeight());
        blue = blue/(image.getWidth()*image.getHeight());


        float[] val = Color.RGBtoHSB(red, green, blue, null);

        return new double[]{val[0], val[1], val[2]};
    }

    /**
     * Send the color to the lamp
     * @param hue the hue of the color between 0 and 65535
     * @param sat the saturation between 0 and 254
     * @param bri the brightness between 0 and 255. 0 also turn the light off
     */
    public static void send(int hue, int sat, int bri) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("on", "" + (bri == 0 ? "false" : "true"));
        parameters.put("hue", "" + hue);
        parameters.put("sat", "" + sat);
        parameters.put("bri", "" + bri);

        ensureParameters(parameters);

        String url = "";

        if(Main.properties.isHttps()) {
            url = Main.properties.getUrlHTTPS();
        }
        else {
            url = Main.properties.getUrlHTTP();
        }

        String replacer = Properties.LAMP_REPLACER.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");

        if(url.contains(Properties.LAMP_REPLACER) && Main.properties.multipleLamps()){
            String[] index = Main.properties.getLamps().split(",");

            for (String i: index) {
                if(Main.properties.isHttps()) {
                    HttpsRequestPoster.makePostRequest(url.replaceAll(replacer, i), parameters);
                }
                else {
                    HttpRequestPutter.makePutRequest(url.replaceAll(replacer, i), parameters);
                }
            }
        }
        else {
            if(Main.properties.isHttps()) {
                HttpsRequestPoster.makePostRequest(url, parameters);
            }
            else {
                HttpRequestPutter.makePutRequest(url, parameters);
            }
        }
    }

    /**
     * Make sure that all the required parameters are presents. If not, ad them
     * @param parameters the parameters
     */
    public static void ensureParameters(Map<String, String> parameters){
        if (parameters.get("on") == null) {
            parameters.put("on", "true");
        }
        if (parameters.get("bri") == null) {
            parameters.put("bri", "0");
        }
    }
}
