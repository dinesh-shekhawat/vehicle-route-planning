package edu.northeastern.csye6220.vehiclerouteplanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.UserAccess;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.UserAccessRepository;
import edu.northeastern.csye6220.vehiclerouteplanning.service.UserAccessService;

@Service
public class UserAccessServiceImpl extends AbstractEntityServiceImpl<UserAccess> implements UserAccessService {

	@Autowired
	public UserAccessServiceImpl(UserAccessRepository repository) {
		register(repository);
	}
	
}
