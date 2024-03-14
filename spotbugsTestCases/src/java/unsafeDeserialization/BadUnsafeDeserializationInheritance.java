package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

public class BadUnsafeDeserializationInheritance extends BadUnsafeDeserialization {

    public BadUnsafeDeserializationInheritance(Date date) {
        super(date);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
