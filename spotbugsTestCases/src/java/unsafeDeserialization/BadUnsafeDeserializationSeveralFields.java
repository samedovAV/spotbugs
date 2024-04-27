package unsafeDeserialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;

public class BadUnsafeDeserializationSeveralFields implements Serializable {
	
	private static final Date immutable = new Date(0);
	
	private Date mutable = null;
	
	private Date anotherMutable = null;
	
	public BadUnsafeDeserializationSeveralFields(Date date) {
		mutable = date;
		anotherMutable = date;
	}
	
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
	}
	
}
