import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Make a PUT request to a server giving him a JSON
 */
public class HttpRequestPutter {
    /**
     * Make a PUT request to a certain URL giving him some informations
     * @param urlStr the url of the server
     * @param param the body of the request. Ihe body will be converted in JSON so that:
     *              {"a", "b"}
     *              is formatted as
     *              " "a":b "
     */
    public static void makePutRequest(String urlStr, Map<String, String> param) {
        MapToJSon mapToJSon = new MapToJSon();
        param.forEach(mapToJSon);
        mapToJSon.afterForEach();
        String json = mapToJSon.getJson();

        makePutRequest(urlStr, json);
    }

    /**
     * Make a PUT request to a certain URL giving him a JSON object
     * @param url the url of the server
     * @param json the body of the request
     * @return a message if the server respons 201. {null} otherwise
     */
    public static String makePutRequest(String url, String json) {
        HttpURLConnection connection = null;
        try {

            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("PUT");

            //set the sending type and receiving type to json
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setAllowUserInteraction(false);

            if (json != null) {
                //set the content length of the body
                connection.setRequestProperty("Content-length", json.getBytes().length + "");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                //send the json as body of the request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    //return received string
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * An object converting a Map to a JSON
     */
    private static class MapToJSon implements BiConsumer<String, String>{
        private String json;

        /**
         * Constructor of the object
         */
        public MapToJSon(){
            json = "{";
        }

        /**
         * Method to give to the map as paramether of map.foreach()
         *
         * @param s the name of a map element
         * @param s2 the value of that map element
         *
         * The result is added in the {json} variabme
         */
        @Override
        public void accept(String s, String s2) {
            json = json + "\"" + s + "\":" + s2 + ",";
        }

        /**
         * To execute at the end of the foreach method. It ends the json String
         */
        public void afterForEach(){
            json = json.substring(0, json.length() - 1) + "}";
        }

        /**
         * Get the result as a JSON String
         * @return the JSON
         */
        public String getJson() {
            return json;
        }
    }
}