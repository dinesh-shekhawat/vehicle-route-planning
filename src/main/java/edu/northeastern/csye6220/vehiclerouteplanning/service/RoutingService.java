package edu.northeastern.csye6220.vehiclerouteplanning.service;

import java.util.List;
import java.util.Map;

import edu.northeastern.csye6220.vehiclerouteplanning.model.ETA;
import edu.northeastern.csye6220.vehiclerouteplanning.model.LocationModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Route;

public interface RoutingService {

	String getType();
	
	Map<String, ?> getInformation();
	
	Route getRoute(List<LocationModel> locations);
	
	ETA getDistance(
			double sourceLatitude, 
			double sourceLongitude,
			double destinationLatitude,
			double destinationLongitude);
	
}
