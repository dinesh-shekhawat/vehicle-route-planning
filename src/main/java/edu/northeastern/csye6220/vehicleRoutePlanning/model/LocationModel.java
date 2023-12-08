package edu.northeastern.csye6220.vehicleRoutePlanning.model;

public class LocationModel {
    private String name;
    private double latitude;
    private double longitude;

    public LocationModel() {

    }
    
    public LocationModel(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "LocationModel [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
    
}
