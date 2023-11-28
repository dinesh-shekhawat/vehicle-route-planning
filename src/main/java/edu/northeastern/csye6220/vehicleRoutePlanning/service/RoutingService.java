package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import java.util.List;
import java.util.Map;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.Location;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;

public interface RoutingService {

	String getType();
	
	Map<String, ?> getInformation();
	
	Route getRoute(List<Location> locations);
	
}
