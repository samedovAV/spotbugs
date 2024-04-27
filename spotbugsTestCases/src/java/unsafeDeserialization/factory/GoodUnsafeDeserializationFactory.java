package unsafeDeserialization.factory;

import java.io.ObjectInputStream;
import java.io.Serializable;

public class GoodUnsafeDeserializationFactory implements Serializable {
	
	private static final MotorVehicle immutable = new Car();
	
	private MotorVehicle mutable = null;
	
	public GoodUnsafeDeserializationFactory(MotorVehicle vehicle) {
		mutable = vehicle;
	}
	
	private void readObject(ObjectInputStream ois) throws java.io.IOException, ClassNotFoundException {
		ObjectInputStream.GetField fields = ois.readFields();
		MotorVehicle inDate = (MotorVehicle) fields.get("mutable", immutable);
		mutable = CarFactory.newInstance(inDate);
	}
}
