package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Vehicle;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.VehicleRepository;
import edu.northeastern.csye6220.vehiclerouteplanning.service.VehicleService;

@Service
public class VehicleServiceImpl extends AbstractEntityServiceImpl<Vehicle> implements VehicleService {

	@Autowired
	public VehicleServiceImpl(VehicleRepository vehicleRepository) {
		register(vehicleRepository);
	}
	
	
}
