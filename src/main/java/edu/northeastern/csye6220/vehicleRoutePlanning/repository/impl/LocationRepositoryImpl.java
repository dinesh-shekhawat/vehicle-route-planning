package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Location;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.LocationRepository;

@Service
public class LocationRepositoryImpl extends AbstractEntityRepositoryImpl<Location> implements LocationRepository {

	@Autowired
	public LocationRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), Location.class);
	}

}
