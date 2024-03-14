package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

class BadUnsafeDeserializationRecord implements Serializable {

    private static final DateRecord epoch = new DateRecord(new Date(0));

    private DateRecord date = null;

    public BadUnsafeDeserializationRecord(DateRecord dateRecord) {
        date = new DateRecord(dateRecord.date());
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
    }
}

record DateRecord(Date date) {
}
