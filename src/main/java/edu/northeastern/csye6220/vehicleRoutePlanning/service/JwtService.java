package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;

public interface JwtService {

	String generateToken(User user);
	
	String validateToken(String token);
	
}
