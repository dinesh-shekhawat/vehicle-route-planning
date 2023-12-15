package edu.northeastern.csye6220.vehicleRoutePlanning.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.northeastern.csye6220.vehicleRoutePlanning.CustomException;
import edu.northeastern.csye6220.vehicleRoutePlanning.UserContextHolder;
import edu.northeastern.csye6220.vehicleRoutePlanning.adapter.LocationAdapter;
import edu.northeastern.csye6220.vehicleRoutePlanning.constants.Constants;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Location;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.LocationModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.LocationService;

@RestController
@RequestMapping("/location-rest")
public class LocationRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationRestController.class);

	private final LocationService locationService;
	
	@Autowired
	public LocationRestController(LocationService locationService) {
		this.locationService = locationService;
		LOGGER.info("loaded locationService: {}", locationService);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public LocationModel add(@RequestBody(required = false) LocationModel location) {
		LOGGER.debug("saving location: {}", location);
		Location entity = LocationAdapter.createRequest(location);
		entity = locationService.add(entity);
		return LocationAdapter.createResponse(entity);
	}
	
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public LocationModel update(
			@RequestBody(required = false) LocationModel location,
			@PathVariable long id) {
		LOGGER.debug("updating location: {} with id: {}", location, id);
	
		Location existing = locationService.findByIdAndUserAndNotDeleted(UserContextHolder.get(), id);
		if (existing == null) {
			throw new CustomException(Constants.ERROR_LOCATION_NOT_FOUND);
		}
		
		Location entity = LocationAdapter.createRequest(location);
		entity.setId(existing.getId());
		entity.setCreatedBy(existing.getCreatedBy());
		entity.setCreatedOn(existing.getCreatedOn());
		entity = locationService.update(entity);
		return LocationAdapter.createResponse(entity);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<LocationModel> getAll() {
		List<Location> locations = locationService.getAllNotDeleted(UserContextHolder.get());	
		return LocationAdapter.createResponse(locations);
	}
	
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable long id) {
		LOGGER.debug("deleting location: with id: {}", id);
	
		Location existing = locationService.findByIdAndUserAndNotDeleted(UserContextHolder.get(), id);
		if (existing == null) {
			throw new CustomException(Constants.ERROR_LOCATION_NOT_FOUND);
		}
		
		locationService.softDelete(existing);
	}
}
