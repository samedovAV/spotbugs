package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BadUnsafeDeserializationList implements Serializable {

    private static final List<Object> immutable = Collections.unmodifiableList(new ArrayList<>());

    private List<Object> mutable = null;

    public BadUnsafeDeserializationList(List<Object> date) {
        mutable = new ArrayList<>(date);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
