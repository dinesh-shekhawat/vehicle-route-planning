package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class DeliveryModel {
	private String name;
	private Point location;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		return "DeliveryModel [name=" + name + ", location=" + location + "]";
	}
}
