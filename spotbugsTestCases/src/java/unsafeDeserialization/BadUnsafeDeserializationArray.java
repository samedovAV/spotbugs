package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class BadUnsafeDeserializationArray implements Serializable {

    private static final int[] immutable = new int[0];

    private int[] mutable = null;

    public BadUnsafeDeserializationArray(int[] arr) {
        mutable = arr;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
