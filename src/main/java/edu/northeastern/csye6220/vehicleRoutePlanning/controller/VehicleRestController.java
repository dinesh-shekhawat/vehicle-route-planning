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

import edu.northeastern.csye6220.vehicleRoutePlanning.model.VehicleModel;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRestController.class);

	private VehicleService vehicleService;
	
	@Autowired
	public VehicleRestController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
		LOGGER.info("loaded vehicleService: {}", vehicleService);
	}
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public VehicleModel save(@RequestBody(required = false) VehicleModel vehicleModel) {
		LOGGER.debug("saving vehicle: {}", vehicleModel);
		return vehicleService.save(vehicleModel);
	}
	
	@PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<VehicleModel> saveList(@RequestBody(required = false) List<VehicleModel> vehicleModels) {
		LOGGER.debug("saving vehicles of size: {}", vehicleModels != null ? vehicleModels.size() : "NULL");
		return vehicleService.saveList(vehicleModels);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<VehicleModel> findByNameOrRegistration(@RequestParam(required = false) String query) {
		LOGGER.debug("querying vehicles for: {}", query);
		return vehicleService.findByNameOrRegistration(query);
	}
}
