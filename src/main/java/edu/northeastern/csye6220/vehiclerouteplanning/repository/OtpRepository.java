package edu.northeastern.csye6220.vehiclerouteplanning.repository;

import edu.northeastern.csye6220.vehiclerouteplanning.entities.Otp;

public interface OtpRepository extends AbstractEntityRepository<Otp> {
	
	Otp findLatestByEmailIdAndField(String emailId, String field);
	
}
