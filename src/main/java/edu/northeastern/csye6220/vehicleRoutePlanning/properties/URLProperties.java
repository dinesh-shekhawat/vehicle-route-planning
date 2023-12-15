package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("url")
public class URLProperties {

	private List<String> nonProtectedUrls;
	private List<String> nonProtectedExtensions;

	public List<String> getNonProtectedUrls() {
		return nonProtectedUrls;
	}
	public void setNonProtectedUrls(List<String> nonProtectedUrls) {
		this.nonProtectedUrls = nonProtectedUrls;
	}
	public List<String> getNonProtectedExtensions() {
		return nonProtectedExtensions;
	}
	public void setNonProtectedExtensions(List<String> nonProtectedExtensions) {
		this.nonProtectedExtensions = nonProtectedExtensions;
	}
	
	@Override
	public String toString() {
		return "URLProperties [nonProtectedUrls=" + nonProtectedUrls + ", nonProtectedExtensions="
				+ nonProtectedExtensions + "]";
	}
	
}
