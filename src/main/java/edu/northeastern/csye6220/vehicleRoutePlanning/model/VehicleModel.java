package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class VehicleModel {
	private String name;
	private String registrationNumber;
	private int capacity;
	private Point location;
	
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
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "VehicleModel [name=" + name + ", registrationNumber=" + registrationNumber + ", capacity=" + capacity
				+ ", location=" + location + "]";
	}

}
