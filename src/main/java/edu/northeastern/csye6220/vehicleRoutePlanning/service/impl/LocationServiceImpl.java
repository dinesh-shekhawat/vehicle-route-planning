package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Location;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.LocationRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.LocationService;

@Service
public class LocationServiceImpl extends AbstractEntityServiceImpl<Location> implements LocationService {

	@Autowired
	public LocationServiceImpl(LocationRepository repository) {
		register(repository);
	}
	
}
