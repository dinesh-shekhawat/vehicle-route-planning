package edu.northeastern.csye6220.vehicleRoutePlanning.repository;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;

public interface OtpRepository extends AbstractEntityRepository<Otp> {
	
	Otp findLatestByEmailIdAndField(String emailId, String field);
	
}
