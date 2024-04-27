package unsafeDeserialization.factory;

public class Car implements MotorVehicle {
	
	public Car() {
	}
	
	public Car(MotorVehicle vehicle) {
		// Factory method
	}
	
	@Override
	public void build() {
		System.out.println("Building a car");
	}
}
