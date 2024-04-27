package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class GoodUnsafeDeserializationNoReadObject implements Serializable { // not also serializable directly

    private static final Date immutable = new Date(0);

    private Date mutable = null; // Mutable component

}
