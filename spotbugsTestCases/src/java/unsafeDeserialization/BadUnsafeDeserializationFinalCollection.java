package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BadUnsafeDeserializationFinalCollection implements Serializable {

    private static final Set<Object> epoch = new HashSet<>();
    // mutable itself (unmodifiableSet is for true immutability)

    private Set<Object> date = null; // Mutable component

    public BadUnsafeDeserializationFinalCollection(Set<Object> set) {
        date = new HashSet<>(set);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
