package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class GoodUnsafeDeserializationSeveralFields implements Serializable {
	
	private static final Date immutable = new Date(0);
	
	private Date mutable = null;
	
	private Date anotherMutable = null;
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ObjectInputStream.GetField fields = ois.readFields();
		Date inDate = (Date) fields.get("immutable", immutable);
		// Defensively copy the mutable component
		mutable = new Date(inDate.getTime());
		anotherMutable = new Date(inDate.getTime());
		// Perform validation if necessary
	}
	
}
