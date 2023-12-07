package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.LocationService;

@RestController
@RequestMapping("/location")
public class LocationRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationRestController.class);

	private LocationService locationService;
	
	@Autowired
	public LocationRestController(LocationService locationService) {
		this.locationService = locationService;
		LOGGER.info("loaded locationService: {}", locationService);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public LocationModel save(@RequestBody(required = false) LocationModel locationModel) {
		LOGGER.debug("saving location: {}", locationModel);
		return locationService.save(locationModel);
	}
	
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<LocationModel> saveList(@RequestBody(required = false) List<LocationModel> locationModels) {
		LOGGER.debug("saving locations of size: {}", locationModels != null ? locationModels.size() : "NULL");
		return locationService.saveList(locationModels);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<LocationModel> findByName(@RequestParam(required = false) String query) {
		LOGGER.debug("querying vehicles for: {}", query);
		return locationService.findByName(query);
	}
}
