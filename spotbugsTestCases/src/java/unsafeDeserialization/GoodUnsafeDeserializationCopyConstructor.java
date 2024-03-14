package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

public class GoodUnsafeDeserializationCopyConstructor implements Serializable {

    private CopyClass epoch = new CopyClass(1);

    private CopyClass copyClass = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Copy constructor
        epoch = (CopyClass) ois.readObject();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        copyClass = new CopyClass(epoch);
    }

    public static class CopyClass {
        private final int value;

        public CopyClass(int value) {
            this.value = value;
        }

        // Copy constructor
        public CopyClass(CopyClass original) {
            this.value = original.value;
        }
    }
}
