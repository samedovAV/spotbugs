package unsafeDeserialization;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class GoodUnsafeDeserializationImmutableListCopyOf implements Serializable {

    private static final ImmutableList<Object> immutable = new ImmutableList.Builder<>().build();

    private List<Object> mutable = null;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        List<Object> inDate = (List<Object>) fields.get("mutable", immutable);
        mutable = ImmutableList.copyOf(inDate);
    }
}
