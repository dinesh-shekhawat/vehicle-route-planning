package edu.northeastern.csye6220.vehiclerouteplanning.service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;

public interface OtpService extends AbstractEntityService<Otp> {

	Otp generateNew(User user);
	
	Otp findLatestByEmailIdAndField(String emailId, String field);
	
}
