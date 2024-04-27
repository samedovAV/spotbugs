package unsafeDeserialization.enums;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class BadUnsafeDeserializationSevenEnum implements Serializable {

    private static final BadUnsafeDeserializationEnum anImmutableEnum = null;
    
    private BadUnsafeDeserializationEnum mutable = null; // Mutable component

    public BadUnsafeDeserializationSevenEnum(BadUnsafeDeserializationEnum anEnum) {
        mutable = anEnum;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
