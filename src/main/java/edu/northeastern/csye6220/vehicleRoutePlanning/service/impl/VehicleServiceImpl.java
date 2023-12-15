package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Vehicle;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.VehicleRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.VehicleService;

@Service
public class VehicleServiceImpl extends AbstractEntityServiceImpl<Vehicle> implements VehicleService {

	@Autowired
	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		register(vehicleRepository);
	}
	
	
}
