package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class GoodUnsafeDeserializationClone implements Serializable, Cloneable {

    private static Date epoch = new Date(0); // todo should be final always?

    private Date date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        epoch = (Date) ois.readObject();
        date = (Date) epoch.clone();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException { // clone should be final (or the whole class)
        return super.clone();
    }
}
