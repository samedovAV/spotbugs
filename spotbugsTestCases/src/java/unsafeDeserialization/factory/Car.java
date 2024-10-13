package unsafeDeserialization.factory;

public class Car extends MotorVehicle {
	
	private int year;
	
	public Car(String make, String model, int year) {
		super(make, model);
		this.year = year;
	}
	
	// Copy constructor for deep copying
	public Car(Car other) {
		super(other);
		this.year = other.year;
	}
	
	public int getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + year;
	}
}
