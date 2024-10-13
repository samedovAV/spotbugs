package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoodUnsafeDeserializationListCopyOfGenerics<T extends String> implements Serializable {
    
    private static final List<String> immutable = new ArrayList<>();

    private List<T> mutable = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        List<T> inDate = (List<T>) fields.get("mutable", immutable);
        mutable = List.copyOf(inDate);
    }
}
