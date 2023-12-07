package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Point;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.RoutingProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingService;

@Service
public class StraightLineRoutingServiceImpl implements RoutingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StraightLineRoutingServiceImpl.class);

	private final RoutingProperties routingProperties;

	@Autowired
	public StraightLineRoutingServiceImpl(RoutingProperties routingProperties) {
		this.routingProperties = routingProperties;
		LOGGER.info("loaded routingProperties: {}", routingProperties);
	}

	@Override
	public String getType() {
		return RoutingFactoryService.STRAIGHT_LINE;
	}

	@Override
	public String toString() {
		return "StraightLineRoutingServiceImpl []";
	}

	@Override
	public Route getRoute(List<LocationModel> locations) {
		LOGGER.trace("forming routes for locations: {}", locations);

		Route route = new Route();
		route.setStops(locations);

		List<Point> polyline = new ArrayList<>();

		int intermediatePoints = routingProperties.getStraightLineIntermediatePoints();
		
		// Iterate through locations to create intermediate points in the polyline
		for (int i = 0; i < locations.size() - 1; i++) {
			LocationModel start = locations.get(i);
			LocationModel end = locations.get(i + 1);
			double deltaLatitde = end.getLatitude() - start.getLatitude();
			double deltaLongitude = end.getLongitude() - start.getLongitude();

			// Add start point to the polyline
			polyline.add(new Point(start.getLatitude(), start.getLongitude()));

			// Calculate intermediate points
			for (int step = 1; step <= intermediatePoints; step++) {
				double fraction = (double) step / (intermediatePoints + 1);
				double intermediateLatitude = start.getLatitude() + fraction * deltaLatitde;
				double intermediateLongitude = start.getLongitude() + fraction * deltaLongitude;

				polyline.add(new Point(intermediateLatitude, intermediateLongitude));
			}
		}

		// Add the last location as the final point in the polyline
		LocationModel lastLocation = locations.get(locations.size() - 1);
		polyline.add(new Point(lastLocation.getLatitude(), lastLocation.getLongitude()));
		
		route.setPolyline(polyline);
		LOGGER.debug("sending polyline size: {} for locations size: {}", polyline.size(), locations.size());
		
		return route;
	}

	@Override
	public Map<String, ?> getInformation() {
		Map<String, Object> information = new HashMap<>();
		information.put("type", getType());
		information.put("routingProperties", routingProperties);
		return information;
	}

}
