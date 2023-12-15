package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.AuthProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.OtpRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.OtpService;
import edu.northeastern.csye6220.vehicleRoutePlanning.util.OtpGenerator;

@Service
public class OtpServiceImpl extends AbstractEntityServiceImpl<Otp> implements OtpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
	
	private final AuthProperties authProperties;
	
	@Autowired
	public OtpServiceImpl(
			OtpRepository otpRepository,
			AuthProperties authProperties) {
		register(otpRepository);
		this.authProperties = authProperties;
	}
	
	@Override
	public Otp generateNew() {
		int minimumDigits = authProperties.getMinimumOtpDigits();
		LOGGER.info("generating a new OTP for minimumDigits: {}", minimumDigits);
		String field = OtpGenerator.createRandomOtp(minimumDigits);
		
		Otp otp = new Otp();
		otp.setField(field);
		return add(otp);
	}

}
