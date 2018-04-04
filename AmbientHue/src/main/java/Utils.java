import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static BufferedImage getScreenShot() throws AWTException {
        return new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }
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

    public static void send(int hue, int sat, int bri) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("on", "" + (bri == 0 ? "false" : "true"));
        parameters.put("hue", "" + hue);
        parameters.put("sat", "" + sat);
        parameters.put("bri", "" + bri);

        ensureParameters(parameters);

        if(Main.properties.isHttps()) {
            HttpsRequestPoster.makePostRequest(Main.properties.getUrlHTTPS(), parameters);
        }
        else {
            HttpRequestPutter.makePutRequest(Main.properties.getUrlHTTP(), parameters);
        }
    }

    public static void ensureParameters(Map<String, String> parameters){
        if (parameters.get("on") == null) {
            parameters.put("on", "true");
        }
        if (parameters.get("bri") == null) {
            parameters.put("bri", "0");
        }
    }
}
