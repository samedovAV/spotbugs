package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GoodUnsafeDeserializationListCopyOf implements Serializable {

    private static final List<Object> immutable = new ArrayList<>();

    private List<Object> mutable = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        List<Object> inDate = (List<Object>) fields.get("mutable", immutable);
        mutable = List.copyOf(inDate);
    }
}