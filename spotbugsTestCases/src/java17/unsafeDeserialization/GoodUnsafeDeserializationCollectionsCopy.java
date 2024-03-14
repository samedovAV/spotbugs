package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoodUnsafeDeserializationCollectionsCopy implements Serializable {

    private static final List<String> epoch = new ArrayList<>();

    private List<String> date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        List<String> inDate = (List<String>) fields.get("date", epoch);
        Collections.copy(date, inDate);
    }
}
