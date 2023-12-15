package edu.northeastern.csye6220.vehicleRoutePlanning.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "otp")
public class Otp extends AbstractEntity {

	@Column(name = "field")
	private String field;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "accessed_on")
	private Date accessedOn;

	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Date getAccessedOn() {
		return accessedOn;
	}
	public void setAccessedOn(Date accessedOn) {
		this.accessedOn = accessedOn;
	}
	
	@Override
	public String toString() {
		return "Otp [field=" + field + ", accessedOn=" + accessedOn + ", id=" + id + ", createdBy=" + createdBy
				+ ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + ", deleted="
				+ deleted + "]";
	}
	
}
