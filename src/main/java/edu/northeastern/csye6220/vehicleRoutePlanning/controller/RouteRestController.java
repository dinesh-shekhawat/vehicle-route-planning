package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.Route;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingFactoryService;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.RoutingService;

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
    @ResponseBody
	public Map<String, ?> getType() {
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		return routingService.getInformation();
	}
	
	@PostMapping(value = "/construct", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Route constructRoute(@RequestBody List<LocationModel> locations) {
		RoutingService routingService = routingFactoryService.getDefaultRoutingService();
		return routingService.getRoute(locations);
	}

}
