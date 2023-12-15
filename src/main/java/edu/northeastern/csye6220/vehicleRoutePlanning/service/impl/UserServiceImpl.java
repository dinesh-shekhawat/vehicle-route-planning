package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.UserRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.UserService;

@Service
public class UserServiceImpl extends AbstractEntityServiceImpl<User> implements UserService {

	private final UserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		register(userRepository);
		this.userRepository = userRepository;
	}
	
	@Override
	public User findByEmailIdAndNotDeleted(String email) {
		return userRepository.findByEmailIdAndNotDeleted(email);
	}

}
