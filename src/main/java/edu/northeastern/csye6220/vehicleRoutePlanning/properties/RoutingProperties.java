package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingFactoryService;

@Component
@ConfigurationProperties("routing")
public class RoutingProperties {

	private int straightLineIntermediatePoints = 4;
	private String defaultProvider = RoutingFactoryService.STRAIGHT_LINE;
	
	private String osmHost = "localhost";
	private int osmPort = 8080;
	private String osmDirectionsApi = "ors/v2/directions/driving-car";

	public int getStraightLineIntermediatePoints() {
		return straightLineIntermediatePoints;
	}
	public void setStraightLineIntermediatePoints(int straightLineIntermediatePoints) {
		this.straightLineIntermediatePoints = straightLineIntermediatePoints;
	}
	public String getDefaultProvider() {
		return defaultProvider;
	}
	public void setDefaultProvider(String defaultProvider) {
		this.defaultProvider = defaultProvider;
	}
	public String getOsmHost() {
		return osmHost;
	}
	public void setOsmHost(String osmHost) {
		this.osmHost = osmHost;
	}
	public int getOsmPort() {
		return osmPort;
	}
	public void setOsmPort(int osmPort) {
		this.osmPort = osmPort;
	}
	public String getOsmDirectionsApi() {
		return osmDirectionsApi;
	}
	public void setOsmDirectionsApi(String osmDirectionsApi) {
		this.osmDirectionsApi = osmDirectionsApi;
	}

	@Override
	public String toString() {
		return "RoutingProperties [straightLineIntermediatePoints=" + straightLineIntermediatePoints
				+ ", defaultProvider=" + defaultProvider + ", osmHost=" + osmHost + ", osmPort=" + osmPort
				+ ", osmDirectionsApi=" + osmDirectionsApi + "]";
	}
	
}
