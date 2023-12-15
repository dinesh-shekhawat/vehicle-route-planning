package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("auth")
public class AuthProperties {

	private int minimumOtpDigits = 6;
	private String jwtSecretKey = "your-jwt-secret-key";
	private long jwtExpirationMilliseconds = 3600000;

	public int getMinimumOtpDigits() {
		return minimumOtpDigits;
	}
	public void setMinimumOtpDigits(int minimumOtpDigits) {
		this.minimumOtpDigits = minimumOtpDigits;
	}
	public String getJwtSecretKey() {
		return jwtSecretKey;
	}

	public void setJwtSecretKey(String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}

	public long getJwtExpirationMilliseconds() {
		return jwtExpirationMilliseconds;
	}

	public void setJwtExpirationMilliseconds(long jwtExpirationMilliseconds) {
		this.jwtExpirationMilliseconds = jwtExpirationMilliseconds;
	}
	
	@Override
	public String toString() {
		return "AuthProperties [minimumOtpDigits=" + minimumOtpDigits + ", jwtSecretKey=" + jwtSecretKey
				+ ", jwtExpirationMilliseconds=" + jwtExpirationMilliseconds + "]";
	}

}
