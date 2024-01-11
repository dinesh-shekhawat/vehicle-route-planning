package edu.northeastern.csye6220.vehiclerouteplanning.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehicle")
public class Vehicle extends AbstractEntity {

	@Column(name = "name")
	private String name;
	
	@Column(name = "registration_number")
	private String registrationNumber;

	@Column(name = "capacity")
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
		return "Vehicle [name=" + name + ", registrationNumber=" + registrationNumber + ", capacity=" + capacity
				+ ", id=" + id + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn
				+ ", updatedOn=" + updatedOn + ", deleted=" + deleted + "]";
	}
	
}
