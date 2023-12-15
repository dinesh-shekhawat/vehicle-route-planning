package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;

public interface UserService extends AbstractEntityService<User> {

	User findByEmailIdAndNotDeleted(String email);
	
}
