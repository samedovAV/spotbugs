package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Clone is wrong, but we accept it
 */
public class GoodUnsafeDeserializationClone implements Serializable, Cloneable {

    private static final Date immutable = new Date(0);

    private Date mutable = null;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        Date inDate = (Date) fields.get("mutable", immutable);
        mutable = (Date) inDate.clone();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException { // clone should be final (or the whole class)
        return super.clone();
    }
}
