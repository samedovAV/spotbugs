package unsafeDeserialization.factory;

public class CarFactory {
	
	public static MotorVehicle newInstance(MotorVehicle vehicle) {
		if (vehicle instanceof Car) {
			// Create a defensive copy of the Car
			return new Car((Car) vehicle);
		} else if (vehicle != null) {
			// Create a defensive copy of MotorVehicle
			return new MotorVehicle(vehicle);
		}
		throw new IllegalArgumentException("Unknown vehicle type");
	}
}
