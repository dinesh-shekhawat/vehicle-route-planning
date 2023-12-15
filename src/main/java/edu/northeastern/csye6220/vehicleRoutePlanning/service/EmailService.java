package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;

public interface EmailService {

	void sendOtpMessage(Otp otp, User user);
	
	void sendEmail(String toEmail, String subject, String body);
	
}
