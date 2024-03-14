package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BadUnsafeDeserializationList implements Serializable {

    private static final List<Object> epoch = new ArrayList<>();

    private List<Object> date = null; // Mutable component

    public BadUnsafeDeserializationList(List<Object> date) {
        this.date = date;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
