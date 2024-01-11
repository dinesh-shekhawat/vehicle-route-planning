package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Location;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.LocationRepository;
import edu.northeastern.csye6220.vehiclerouteplanning.service.LocationService;

@Service
public class LocationServiceImpl extends AbstractEntityServiceImpl<Location> implements LocationService {

	@Autowired
	public LocationServiceImpl(LocationRepository repository) {
		register(repository);
	}
	
}
