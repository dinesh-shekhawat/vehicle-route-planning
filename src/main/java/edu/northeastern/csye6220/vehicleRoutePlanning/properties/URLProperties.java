package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("url")
public class URLProperties {

	private List<String> protectedUrls;

	public List<String> getProtectedUrls() {
		return protectedUrls;
	}
	public void setProtectedUrls(List<String> protectedUrls) {
		this.protectedUrls = protectedUrls;
	}
	
	@Override
	public String toString() {
		return "URLProperties [protectedUrls=" + protectedUrls + "]";
	}
	
}
