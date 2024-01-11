package edu.northeastern.csye6220.vehiclerouteplanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Location;
import edu.northeastern.csye6220.vehiclerouteplanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.LocationRepository;

@Service
public class LocationRepositoryImpl extends AbstractEntityRepositoryImpl<Location> implements LocationRepository {

	@Autowired
	public LocationRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), Location.class);
	}

}
