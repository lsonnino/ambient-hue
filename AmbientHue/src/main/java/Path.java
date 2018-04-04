import java.io.File;

/**
 * A class containing the path to some important folders and to the saved properties:
 *      HOME_DIR: the user's home directory
 *      SEPARATOR: the separator used by the system to separate files (for instance, "/" for Unix and MAC OS systems)
 *
 *      PROPERTIES: the saved properties representing a serialized object containing the server's url or the hub's url and
 *          the preferred method to access him
 */
public class Path {
    public static final String HOME_DIR = System.getProperty("user.home");
    public static final String SEPARATOR = System.getProperty("file.separator");

    public static final File PROPERTIES = new File(HOME_DIR + SEPARATOR +
            "." + Constants.NAME_NO_SPACE + SEPARATOR + "properties");
}
