import java.io.Serializable;

/**
 * Contains all the information to access the hub/server
 */
public class Properties implements Serializable {
    private String urlHTTP, urlHTTPS; // The url of a proxy server and the url of the hub
    private boolean https, postHTTP, postHTTPS; // The preferred way to access him

    /**
     * Main constructor of the class Properties
     */
    public Properties() {
        this.urlHTTP = "http://example.org";
        this.urlHTTPS = "https://example.org?user=admin&password=admin";
        https = true;
        postHTTP = false;
        postHTTPS = true;
    }

    /**
     * Duplicate a Properties object
     * @param properties
     */
    public Properties(Properties properties) {
        this.urlHTTP = new String(properties.getUrlHTTP());
        this.urlHTTPS = new String(properties.getUrlHTTPS());
        https = new Boolean(properties.isHttps());
        postHTTP = new Boolean(properties.isPostHTTP());
        postHTTPS = new Boolean(properties.isPostHTTPS());
    }

    /**
     * Set the url of the hub (or of a HTTP server) which then access the light
     * @param urlHTTP the new value for url
     */
    public void setUrlHTTP(String urlHTTP) {
        this.urlHTTP = urlHTTP;
    }

    /**
     * The url of the hub (or of an HTTP server) which then access the light
     * @return the url
     */
    public String getUrlHTTP() {
        return urlHTTP;
    }

    /**
     * Set the url of a HTTPS server which then access the light
     * @param urlHTTPS the new value for url
     */
    public void setUrlHTTPS(String urlHTTPS) {
        this.urlHTTPS = urlHTTPS;
    }
    /**
     * Get the url of a HTTPS server which then access the light
     * @return the url
     */
    public String getUrlHTTPS() {
        return urlHTTPS;
    }

    /**
     * Set whether it has to use the HTTP url or the HTTPS url
     * @param https {true} if the HTTPS url must be used. {false} otherwise
     */
    public void setHttps(boolean https) {
        this.https = https;
    }

    /**
     * Get whether the HTTP or the HTTPS url must be used
     * @return {true} if the HTTPS url must be used. {false} otherwise
     */
    public boolean isHttps() {
        return https;
    }

    /**
     * Set whether a POST request must be done to access the server
     * @param postHTTP {true} if a POST request is needed. {false} otherwise
     */
    public void setPostHTTP(boolean postHTTP) {
        this.postHTTP = postHTTP;
    }
    /**
     * Returns whether a POST request must be done
     * @return {true} if a POST request is needed. {false} otherwise
     */
    public boolean isPostHTTP() {
        return postHTTP;
    }

    /**
     * Set whether a POST request must be done to access the server
     * @param postHTTPS {true} if a POST request is needed. {false} otherwise
     */
    public void setPostHTTPS(boolean postHTTPS) {
        this.postHTTPS = postHTTPS;
    }
    /**
     * Returns whether a POST request must be done
     * @return {true} if a POST request is needed. {false} otherwise
     */
    public boolean isPostHTTPS() {
        return postHTTPS;
    }
}

