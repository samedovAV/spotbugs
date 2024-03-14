package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoodUnsafeDeserializationSetCopyOf<T> implements Serializable {

    private Set<T> epoch = new HashSet<>();

    private Set<T> date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        epoch = (Set<T>) ois.readObject();
        date = Set.copyOf(epoch);
    }
}
