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
    public static final short WINDOWS = 0;
    public static final short LINUX = 1;
    public static final short MACOS = 2;

    public static final String HOME_DIR = System.getProperty("user.home");
    public static final String SEPARATOR = System.getProperty("file.separator");

    //public static final File PROPERTIES = new File(HOME_DIR + SEPARATOR +
      //      "." + Constants.NAME_NO_SPACE + SEPARATOR + "properties");
    public static final File PROPERTIES = new File(getApplicationFolder().getPath() + SEPARATOR + "properties");

    public static File getApplicationFolder(){
        String home = System.getProperty("user.home");

        String projectName = Constants.NAME_NO_SPACE;

        switch (getOS()){
            case WINDOWS:
                return new File(
                        home + SEPARATOR + "AppData" + SEPARATOR + "Local" + SEPARATOR + "." + projectName
                );
            case MACOS:
                return new File(
                        home + SEPARATOR + "Library" + SEPARATOR + "Application Support" + SEPARATOR + "org.alfcorp." + projectName
                );
            case LINUX:
                return new File(
                        home + SEPARATOR + "." + projectName
                );
            default:
                return new File(
                        home + SEPARATOR + "AppData" + SEPARATOR + "Local" + SEPARATOR + "." + projectName
                );
        }
    }

    public static short getOS(){
        String os = System.getProperty("os.name").toLowerCase();

        if(os.contains("win")){ // Windows
            return WINDOWS;
        }
        else if(os.contains("mac")){ // macOS
            return MACOS;
        }
        else { // Linux
            return LINUX;
        }
    }
}
