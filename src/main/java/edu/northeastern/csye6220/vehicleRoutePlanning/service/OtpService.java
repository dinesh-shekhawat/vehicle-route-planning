package edu.northeastern.csye6220.vehicleRoutePlanning.service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;

public interface OtpService extends AbstractEntityService<Otp> {

	Otp generateNew();
	
}
