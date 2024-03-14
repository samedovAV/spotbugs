package unsafeDeserialization.arrays;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class GoodUnsafeDeserializationArraycopy implements Serializable {
    private static final int[] epoch = new int[]{10};

    private int[] date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        int[] inDate = (int[]) fields.get("date", epoch);
        System.arraycopy(inDate, 0, date, 0, inDate.length);
    }
}
