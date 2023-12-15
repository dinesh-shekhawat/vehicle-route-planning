package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("auth")
public class AuthProperties {

	private int minimumOtpDigits = 6;

	public int getMinimumOtpDigits() {
		return minimumOtpDigits;
	}
	public void setMinimumOtpDigits(int minimumOtpDigits) {
		this.minimumOtpDigits = minimumOtpDigits;
	}

	@Override
	public String toString() {
		return "AuthProperties [minimumOtpDigits=" + minimumOtpDigits + "]";
	}
	
}
