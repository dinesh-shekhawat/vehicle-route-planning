package edu.northeastern.csye6220.vehicleRoutePlanning.repository;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.UserAccess;

public interface UserAccessRepository {

	UserAccess add(UserAccess userAccess);
	
	UserAccess update(UserAccess userAccess);
	
}
