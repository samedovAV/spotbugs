package unsafeDeserialization.factory;

import java.io.Serializable;

public class MotorVehicle implements Serializable {
	
	private String make;
	private String model;
	
	public MotorVehicle(String make, String model) {
		this.make = make;
		this.model = model;
	}
	
	// Copy constructor for defensive copying
	public MotorVehicle(MotorVehicle other) {
		this.make = other.make;
		this.model = other.model;
	}
	
	// Getters and setters
	
	public String getMake() {
		return make;
	}
	
	public String getModel() {
		return model;
	}
	
	@Override
	public String toString() {
		return make + " " + model;
	}
}
