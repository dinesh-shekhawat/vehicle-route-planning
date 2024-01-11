package edu.northeastern.csye6220.vehiclerouteplanning.service;

public interface RoutingFactoryService {
	
	String STRAIGHT_LINE = "straight-line";
	String OSM = "osm";

	RoutingService getDefaultRoutingService();
	
	RoutingService getRoutingService(String implementationName);
	
}
