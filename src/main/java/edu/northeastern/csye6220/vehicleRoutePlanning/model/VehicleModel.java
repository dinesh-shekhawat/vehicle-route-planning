package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class VehicleModel {
	private String name;
	private String registrationNumber;
	private int capacity;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	@Override
	public String toString() {
		return "VehicleModel [name=" + name + ", registrationNumber=" + registrationNumber + ", capacity=" + capacity
				+ "]";
	}

}
