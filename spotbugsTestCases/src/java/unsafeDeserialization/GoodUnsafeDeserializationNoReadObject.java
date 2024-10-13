package unsafeDeserialization;

import java.io.Serializable;
import java.util.Date;

public class GoodUnsafeDeserializationNoReadObject implements Serializable {

    private static final Date immutable = new Date(0);

    private Date mutable = null; // Mutable component

}
