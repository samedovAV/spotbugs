package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GoodUnsafeDeserializationMapCopyOf implements Serializable {

    private static final Map<Object, Object> immutable = Collections.emptyMap();

    private Map<Object, Object> mutable = null;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        Map<Object, Object> inMutable = (Map<Object, Object>) fields.get("mutable", immutable);
        mutable = Map.copyOf(inMutable);
    }
}
