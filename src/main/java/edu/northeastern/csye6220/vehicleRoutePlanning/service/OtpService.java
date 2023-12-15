package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;

public interface OtpService extends AbstractEntityService<Otp> {

	Otp generateNew(User user);
	
	Otp findLatestByEmailIdAndField(String emailId, String field);
	
}
