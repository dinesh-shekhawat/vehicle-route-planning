package edu.northeastern.csye6220.vehiclerouteplanning.service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;

public interface UserService extends AbstractEntityService<User> {

	User findByEmailIdAndNotDeleted(String email);
	
}
