package unsafeDeserialization;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class GoodUnsafeDeserializationWrongMethodName implements Serializable {
	
	private ImmutableList<Object> immutable = new ImmutableList.Builder<Object>().build();
	
	private List<Object> mutable = null;
	
	private void readObjectCarefully(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ObjectInputStream.GetField fields = ois.readFields();
		List<Object> inDate = (List<Object>) fields.get("mutable", immutable);
		mutable = ImmutableList.copyOf(inDate);
	}
}
