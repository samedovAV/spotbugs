package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class BadUnsafeDeserialization implements Serializable {

    private static final Date epoch = new Date(0);
    private Date date = null; // Mutable component

    public BadUnsafeDeserialization(Date date) {
        this.date = date;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }

}
