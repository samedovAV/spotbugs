package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GoodUnsafeDeserializationSetCopyOf implements Serializable {

    private static final Set<Object> immutable = Collections.unmodifiableSet(new HashSet<>());

    private Set<Object> mutable = null;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        Set<Object> inMutable = (Set<Object>) fields.get("mutable", immutable);
        mutable = Set.copyOf(inMutable);
    }
}
