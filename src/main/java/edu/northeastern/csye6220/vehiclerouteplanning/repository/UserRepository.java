package edu.northeastern.csye6220.vehiclerouteplanning.repository;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;

public interface UserRepository extends AbstractEntityRepository<User> {
	
	User findByEmailIdAndNotDeleted(String email);
	
}
