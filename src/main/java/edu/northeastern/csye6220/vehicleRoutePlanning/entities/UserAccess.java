package edu.northeastern.csye6220.vehicleRoutePlanning.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_access")
public class UserAccess extends AbstractEntity {

	@Column(name = "url")
    private String url;
	
	@Column(name = "method_type")
	private String methodType;

	@Column(name = "response_code")
    private int responseCode;

	@Column(name = "response_time")
    private long responseTime; // Assuming the response time is in milliseconds

    @Column(name = "client_ip_address")
    private String clientIpAddress;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethodType() {
		return methodType;
	}
	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	public String getClientIpAddress() {
		return clientIpAddress;
	}
	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}
	
	@Override
	public String toString() {
		return "UserAccess [url=" + url + ", methodType=" + methodType + ", responseCode=" + responseCode
				+ ", responseTime=" + responseTime + ", clientIpAddress=" + clientIpAddress + ", id=" + id
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdOn=" + createdOn + ", updatedOn="
				+ updatedOn + ", deleted=" + deleted + "]";
	}
    
}
