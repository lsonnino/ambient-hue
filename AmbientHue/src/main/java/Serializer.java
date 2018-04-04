import com.sun.istack.internal.Nullable;

import java.io.*;

/**
 * Utils for serializing and deserializing an object to/from a file
 */
public class Serializer {
    /**
     * Serialize an object to a file
     * @param object the serializable object
     * @param path the path whete to save the object. If the folder doesn't exist, it will be created
     * @param name the name of the written file. If the file exists, it will be overwritten
     * @throws IOException thrown if an error occurs
     */
    public static void serialize(Serializable object, @Nullable  String path, String name) throws IOException {
        File file = null;
        if(path != null) {
            file = new File(path + System.getProperty("file.separator"));
            file.mkdirs();
        }

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                (path != null ? file.getPath() + System.getProperty("file.separator") : "") + name
        ));

        out.writeObject(object);

        out.close();
    }

    /**
     * Get an object from a serialized file
     * @param file the file containing the serialized object
     * @param classOfT the class type of the serialized object
     * @param <T> the type of the serialized object
     * @return the object contained in the file
     * @throws IOException thrown if an error occurs
     * @throws ClassNotFoundException thrown if the file doesn't contains an object of type T
     */
    public static <T> T deserialize(File file, Class<T> classOfT) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

        Object obj =  in.readObject();

        in.close();

        return classOfT.cast(obj);
    }
}