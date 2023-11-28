package edu.northeastern.csye6220.vehicleRoutePlanning.service;

public interface RoutingFactoryService {
	
	String STRAIGHT_LINE = "straight-line";
	
	RoutingService getRoutingService(String implementationName);
	
}
