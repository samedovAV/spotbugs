package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BadUnsafeDeserializationFinalCollection implements Serializable {

    private static final Set<Object> immutable = Collections.unmodifiableSet(new HashSet<>());
    // mutable itself (unmodifiableSet is for true immutability)

    private Set<Object> mutable = null;

    public BadUnsafeDeserializationFinalCollection(Set<Object> set) {
        mutable = new HashSet<>(set);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
