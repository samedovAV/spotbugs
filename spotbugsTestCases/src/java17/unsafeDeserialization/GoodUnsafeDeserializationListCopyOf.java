package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoodUnsafeDeserializationListCopyOf implements Serializable {

    private static final List<Object> epoch = new ArrayList<>();

    private List<Object> date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        List<Object> inDate = (List<Object>) fields.get("date", epoch);
        date = List.copyOf(inDate);
    }
}
