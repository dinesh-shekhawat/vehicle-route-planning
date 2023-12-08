package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class ShipmentModel {
	private String name;
	private Point sourceLocation;
	private Point destinationLocation;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Point getSourceLocation() {
		return sourceLocation;
	}
	public void setSourceLocation(Point sourceLocation) {
		this.sourceLocation = sourceLocation;
	}
	public Point getDestinationLocation() {
		return destinationLocation;
	}
	public void setDestinationLocation(Point destinationLocation) {
		this.destinationLocation = destinationLocation;
	}
	
	@Override
	public String toString() {
		return "ShipmentModel [name=" + name + ", sourceLocation=" + sourceLocation + ", destinationLocation="
				+ destinationLocation + "]";
	}
}
