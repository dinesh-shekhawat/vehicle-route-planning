package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingService;

@Service
public class RoutingFactoryServiceImpl implements RoutingFactoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoutingFactoryServiceImpl.class);
	
	private final Map<String, RoutingService> routingServiceMap;
	
	@Autowired
    public RoutingFactoryServiceImpl(List<RoutingService> routingServices) {
		LOGGER.info("registering routingServices: {}", routingServices);
        this.routingServiceMap = routingServices
        		.stream()
        		.collect(Collectors.toMap(RoutingService::getType, Function.identity()));
    }
	
	@Override
	public RoutingService getRoutingService(String implementationName) {
		LOGGER.trace("get routing service for: {}", implementationName);
		RoutingService service = routingServiceMap.get(implementationName);
		LOGGER.trace("returning service: {}", service);
		return service;
	}

	@Override
	public String toString() {
		return "RoutingFactoryServiceImpl [routingServiceMap=" + routingServiceMap + "]";
	}

}
