package unsafeDeserialization;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class GoodUnsafeDeserializationImmutableListCopyOf<T> implements Serializable {

    private ImmutableList<T> epoch = new ImmutableList.Builder<T>().build();

    private List<T> date = null;

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        epoch = (ImmutableList<T>) ois.readObject();
        date = ImmutableList.copyOf(epoch);
    }
}
