package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BadUnsafeDeserialization implements Serializable {

    private static final Date immutable = new Date(0);
    private Date mutable = null; // Mutable component

    public BadUnsafeDeserialization(Date date) {
        this.mutable = date;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

}
