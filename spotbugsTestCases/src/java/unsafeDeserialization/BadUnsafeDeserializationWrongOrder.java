package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class BadUnsafeDeserializationWrongOrder implements Serializable {
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
    
    private static final Date immutable = new Date(0);
    private Date mutable = null; // Mutable component

    public BadUnsafeDeserializationWrongOrder(Date date) {
        this.mutable = date;
    }

}
