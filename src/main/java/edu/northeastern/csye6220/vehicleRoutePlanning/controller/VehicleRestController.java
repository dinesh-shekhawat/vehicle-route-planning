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
import edu.northeastern.csye6220.vehicleRoutePlanning.adapter.VehicleAdapter;
import edu.northeastern.csye6220.vehicleRoutePlanning.constants.Constants;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Vehicle;
import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleService;

@RestController
@RequestMapping("/vehicle-rest")
public class VehicleRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRestController.class);

	private final VehicleService vehicleService;
	
	@Autowired
	public VehicleRestController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
		LOGGER.info("loaded vehicleService: {}", vehicleService);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public VehicleModel add(@RequestBody(required = false) VehicleModel vehicle) {
		LOGGER.debug("saving vehicle: {}", vehicle);
		Vehicle entity = VehicleAdapter.createRequest(vehicle);
		entity = vehicleService.add(entity);
		return VehicleAdapter.createResponse(entity);
	}
	
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public VehicleModel update(
			@RequestBody(required = false) VehicleModel vehicle,
			@PathVariable long id) {
		LOGGER.debug("updating vehicle: {} with id: {}", vehicle, id);
	
		Vehicle existing = vehicleService.findByIdAndUserAndNotDeleted(UserContextHolder.get(), id);
		if (existing == null) {
			throw new CustomException(Constants.ERROR_VEHICLE_NOT_FOUND);
		}
		
		Vehicle entity = VehicleAdapter.createRequest(vehicle);
		entity.setId(existing.getId());
		entity.setCreatedBy(existing.getCreatedBy());
		entity.setCreatedOn(existing.getCreatedOn());
		entity = vehicleService.update(entity);
		return VehicleAdapter.createResponse(entity);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VehicleModel> getAll() {
		List<Vehicle> vehicle = vehicleService.getAllNotDeleted(UserContextHolder.get());	
		return VehicleAdapter.createResponse(vehicle);
	}
	
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable long id) {
		LOGGER.debug("updating vehicle: with id: {}", id);
	
		Vehicle existing = vehicleService.findByIdAndUserAndNotDeleted(UserContextHolder.get(), id);
		if (existing == null) {
			throw new CustomException(Constants.ERROR_VEHICLE_NOT_FOUND);
		}
		
		vehicleService.softDelete(existing);
	}
	
}
