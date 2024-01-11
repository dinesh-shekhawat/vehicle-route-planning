package edu.northeastern.csye6220.vehiclerouteplanning.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.csye6220.vehiclerouteplanning.model.ETA;
import edu.northeastern.csye6220.vehiclerouteplanning.model.LocationModel;
import edu.northeastern.csye6220.vehiclerouteplanning.model.Route;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehiclerouteplanning.service.RoutingService;

@RestController
@RequestMapping("/route")
public class RouteRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RouteRestController.class);

	private final RoutingFactoryService routingFactoryService;

	@Autowired
	public RouteRestController(RoutingFactoryService routingFactoryService) {
		this.routingFactoryService = routingFactoryService;
		LOGGER.info("loaded routingFactoryService: {}", routingFactoryService);
	}
	
	@GetMapping(value = "/type", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, ?> getType() {
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		return routingService.getInformation();
	}
	
	@PostMapping(value = "/construct", produces = MediaType.APPLICATION_JSON_VALUE)
	public Route constructRoute(@RequestBody List<LocationModel> locations) {
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		return routingService.getRoute(locations);
	}
	
	@GetMapping(value = "/eta", produces = MediaType.APPLICATION_JSON_VALUE)
	public ETA getEta(
			@RequestParam double sourceLatitude, 
			@RequestParam double sourceLongitude,
			@RequestParam double destinationLatitude,
			@RequestParam double destinationLongitude) {
		LOGGER.trace("getting distance between sourceLatitude: {}, sourceLongitude: {}, destinationLatitude: {}, destinationLongitude: {}", 
				sourceLatitude, 
				sourceLongitude,
				destinationLatitude,
				destinationLongitude);
		
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		ETA eta = routingService.getDistance(
				sourceLatitude, 
				sourceLongitude, 
				destinationLatitude, 
				destinationLongitude);
		return eta;
	}

}
