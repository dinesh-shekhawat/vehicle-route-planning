package edu.northeastern.csye6220.vehiclerouteplanning.service;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;
import edu.northeastern.csye6220.vehiclerouteplanning.entities.User;

public interface EmailService {

	void sendOtpMessage(Otp otp, User user);
	
	void sendEmail(String toEmail, String subject, String body);
	
}
