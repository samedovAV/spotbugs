package unsafeDeserialization.factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import unsafeDeserialization.GoodUnsafeDeserializationCopyConstructor.CopyClass;

public class GoodUnsafeDeserializationFactory implements Serializable {

    private static ClassFactory classFactory = new ClassFactoryItem();

    private final FactoryInterface epoch = classFactory.createFactoryInstance();

    private FactoryInterface date = null; // Mutable component

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = ois.readFields();
        FactoryInterface inDate = (FactoryInterface) fields.get("date", epoch);
        date = ClassFactory.createNewInstance(inDate);
    }

}
