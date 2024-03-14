package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BadUnsafeDeserializationHashMap implements Serializable {

    private static final Map<Object, Object> epoch = new HashMap<>();

    private Map<Object, Object> date; // Mutable component

    public BadUnsafeDeserializationHashMap(Map<String, Integer> map) {
        date = new HashMap<>(map);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

    /*
    public Map<String, Integer> getMutableMap() {
        // Getter to access the mutable map
        return new HashMap<>(mutableMap); // Return a defensive copy
    }
    */
}
