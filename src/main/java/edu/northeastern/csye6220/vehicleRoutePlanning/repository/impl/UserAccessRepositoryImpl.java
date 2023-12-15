package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.UserAccessRepository;

@Service
public class UserAccessRepositoryImpl extends AbstractEntityRepositoryImpl<UserAccess> implements UserAccessRepository {

	@Autowired
	public UserAccessRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), UserAccess.class);
	}

}
