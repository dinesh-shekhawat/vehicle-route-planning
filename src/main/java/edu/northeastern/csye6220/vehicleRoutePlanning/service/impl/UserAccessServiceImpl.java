package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.UserAccessRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserAccessService;

@Service
public class UserAccessServiceImpl extends AbstractEntityServiceImpl<UserAccess> implements UserAccessService {

	@Autowired
	public UserAccessServiceImpl(UserAccessRepository repository) {
		register(repository);
	}
	
}
