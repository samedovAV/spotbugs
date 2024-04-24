package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BadUnsafeDeserializationHashMap implements Serializable {

    private static final Map<Object, Object> immutable = new HashMap<>();

    private Map<Object, Object> mutable; // Mutable component

    public BadUnsafeDeserializationHashMap(Map<String, Integer> map) {
        mutable = new HashMap<>(map);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

}
