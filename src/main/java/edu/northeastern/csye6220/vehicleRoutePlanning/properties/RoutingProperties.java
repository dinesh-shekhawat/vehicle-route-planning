package edu.northeastern.csye6220.vehicleRoutePlanning.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("routing")
public class RoutingProperties {

	private int straightLineIntermediatePoints = 4;

	public int getStraightLineIntermediatePoints() {
		return straightLineIntermediatePoints;
	}

	public void setStraightLineIntermediatePoints(int straightLineIntermediatePoints) {
		this.straightLineIntermediatePoints = straightLineIntermediatePoints;
	}

	@Override
	public String toString() {
		return "RoutingProperties [straightLineIntermediatePoints=" + straightLineIntermediatePoints + "]";
	}
	
}
