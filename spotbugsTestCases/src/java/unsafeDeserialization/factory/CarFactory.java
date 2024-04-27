package unsafeDeserialization.factory;

public class CarFactory extends MotorVehicleFactory {
	
	@Override
	protected MotorVehicle createMotorVehicle() {
		return new Car();
	}
	
	public static MotorVehicle newInstance(MotorVehicle vehicle) {
		return new Car(vehicle);
	}
}
