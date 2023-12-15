package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.UserAccessRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserAccessService;

@Service
public class UserAccessServiceImpl implements UserAccessService {

	private final UserAccessRepository repository;
	
	public UserAccessServiceImpl(UserAccessRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public UserAccess add(UserAccess userAccess) {
		return repository.add(userAccess);
	}
	
	@Override
	public UserAccess update(UserAccess userAccess) {
		return repository.update(userAccess);
	}
	
}
