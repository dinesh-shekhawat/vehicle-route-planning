package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class VehicleModel {
	private String name;
	private String registration;
	private int capacity;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	@Override
	public String toString() {
		return "VehicleModel [name=" + name + ", registration=" + registration + ", capacity=" + capacity + "]";
	}

}
