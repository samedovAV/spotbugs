package unsafeDeserialization.enums;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class BadUnsafeDeserializationSevenEnum implements Serializable {

    private BadUnsafeDeserializationEnum anEnum = null;

    public BadUnsafeDeserializationSevenEnum(BadUnsafeDeserializationEnum anEnum) {
        this.anEnum = anEnum;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}
