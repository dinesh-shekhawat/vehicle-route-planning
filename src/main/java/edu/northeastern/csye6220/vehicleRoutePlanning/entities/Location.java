package edu.northeastern.csye6220.vehicleRoutePlanning.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "location")
public class Location extends AbstractEntity {

	@Column(name = "name")
	private String name;
	
	@Column(name = "latitude")
	private double latitude;
	
	@Column(name = "longitude")
	private double longitude;

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
		return "Location [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + ", id=" + id
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn="
				+ updatedOn + ", deleted=" + deleted + "]";
	}
	
}
