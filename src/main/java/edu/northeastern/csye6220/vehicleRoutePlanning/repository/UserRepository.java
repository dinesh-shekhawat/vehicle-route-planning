package edu.northeastern.csye6220.vehicleRoutePlanning.repository;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;

public interface UserRepository extends AbstractEntityRepository<User> {
	
	User findByEmailIdAndNotDeleted(String email);
	
}
