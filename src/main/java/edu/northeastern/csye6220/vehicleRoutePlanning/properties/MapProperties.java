package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("map")
public class MapProperties {

	private double initialLatitude;
	private double initialLongitude;
	private int initialZoomLevel;

	public double getInitialLatitude() {
		return initialLatitude;
	}
	public void setInitialLatitude(double initialLatitude) {
		this.initialLatitude = initialLatitude;
	}
	public double getInitialLongitude() {
		return initialLongitude;
	}
	public void setInitialLongitude(double initialLongitude) {
		this.initialLongitude = initialLongitude;
	}
	public int getInitialZoomLevel() {
		return initialZoomLevel;
	}
	public void setInitialZoomLevel(int initialZoomLevel) {
		this.initialZoomLevel = initialZoomLevel;
	}

	@Override
	public String toString() {
		return "MapProperties [initialLatitude=" + initialLatitude + ", initialLongitude=" + initialLongitude
				+ ", initialZoomLevel=" + initialZoomLevel + "]";
	}
	
}
