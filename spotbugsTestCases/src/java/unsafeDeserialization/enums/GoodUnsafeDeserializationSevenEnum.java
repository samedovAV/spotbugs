package unsafeDeserialization.enums;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class GoodUnsafeDeserializationSevenEnum implements Serializable {

    private static final GoodUnsafeDeserializationEnum anImmutableEnum = null;
    
    private GoodUnsafeDeserializationEnum alsoImmutable = null; // also immutable

    public GoodUnsafeDeserializationSevenEnum(GoodUnsafeDeserializationEnum anEnum) {
        alsoImmutable = anEnum;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
