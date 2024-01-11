package edu.northeastern.csye6220.vehiclerouteplanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.UserAccess;
import edu.northeastern.csye6220.vehiclerouteplanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehiclerouteplanning.repository.UserAccessRepository;

@Service
public class UserAccessRepositoryImpl extends AbstractEntityRepositoryImpl<UserAccess> implements UserAccessRepository {

	@Autowired
	public UserAccessRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), UserAccess.class);
	}

}
