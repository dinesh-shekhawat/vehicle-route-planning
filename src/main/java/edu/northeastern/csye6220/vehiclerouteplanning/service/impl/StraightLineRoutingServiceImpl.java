package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.model.ETA;
import edu.northeastern.csye6220.vehiclerouteplanning.model.LocationModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Point;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Route;
import edu.northeastern.csye6220.vehiclerouteplanning.properties.RoutingProperties;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingService;

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

	@Override
	public ETA getDistance(
			double sourceLatitude, 
			double sourceLongitude,
			double destinationLatitude,
			double destinationLongitude) {
		LOGGER.trace("getting distance between sourceLatitude: {}, sourceLongitude: {}, destinationLatitude: {}, destinationLongitude: {}", 
				sourceLatitude, 
				sourceLongitude,
				destinationLatitude,
				destinationLongitude);
		
		// Haversine distance, Earth radius
		final double R = 6371.0;
		
		double lat1 = Math.toRadians(sourceLatitude);
        double lon1 = Math.toRadians(sourceLongitude);
        double lat2 = Math.toRadians(destinationLatitude);
        double lon2 = Math.toRadians(destinationLongitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = R * c;
        
        ETA eta = new ETA();
        eta.setDistance(distance);
        eta.setTime(distance); // Assuming directly propotional
        return eta;
	}

}
