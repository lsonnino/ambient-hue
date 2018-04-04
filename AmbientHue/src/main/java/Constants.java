/**
 * Contains all the basic constants of the program:
 *      NAME: the program's name
 *      NAME_NO_SPACE: the program's name without spaces
 *
 *      VESION: the current version
 *      MINOR: the current minor version
 *      PATCH: the current patch
 *      -> The complete version can be written as
 *          VERSION.MINOR.PATCH
 *          for instance: 1.0.0
 */
public class Constants {
    public static final String NAME = "Ambient Hue";
    public static final String NAME_NO_SPACE = NAME.replaceAll("\\s", "-");

    public static final int VERSION = 1;
    public static final int MINOR = 0;
    public static final int PATCH = 0;
}
