package edu.northeastern.csye6220.vehiclerouteplanning.service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;

public interface JwtService {

	String generateToken(User user);
	
	String validateToken(String token);
	
}
