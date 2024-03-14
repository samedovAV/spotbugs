package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class BadUnsafeDeserializationArray implements Serializable {

    private static final int[] epoch = new int[0];

    private int[] date = null; // Mutable component

    public BadUnsafeDeserializationArray(int[] arr) {
        date = arr;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
