package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("auth")
public class AuthProperties {

	private int minimumOtpDigits = 6;
	private String jwtSecretKey = "your-jwt-secret-key";
	private long jwtExpirationMilliseconds = 3600000;
	
	private String defaultUserInfo = "adming@drive-sync.com";
	private String authBypassFilePath = "bypassAuth";

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
	public String getDefaultUserInfo() {
		return defaultUserInfo;
	}
	public void setDefaultUserInfo(String defaultUserInfo) {
		this.defaultUserInfo = defaultUserInfo;
	}
	public String getAuthBypassFilePath() {
		return authBypassFilePath;
	}
	public void setAuthBypassFilePath(String authBypassFilePath) {
		this.authBypassFilePath = authBypassFilePath;
	}

	@Override
	public String toString() {
		return "AuthProperties [minimumOtpDigits=" + minimumOtpDigits + ", jwtSecretKey=" + jwtSecretKey
				+ ", jwtExpirationMilliseconds=" + jwtExpirationMilliseconds + ", defaultUserInfo=" + defaultUserInfo
				+ ", authBypassFilePath=" + authBypassFilePath + "]";
	}
}
