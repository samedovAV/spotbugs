package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class GoodUnsafeDeserialization implements Serializable { // not also serializable directly

    private static final Date epoch = new Date(0);

    private Date date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        Date inDate = (Date) fields.get("date", epoch);
        // Defensively copy the mutable component
        date = new Date(inDate.getTime());
        // Perform validation if necessary
    }
}
