package edu.northeastern.csye6220.vehicleRoutePlanning.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.entities.User;
import edu.northeastern.csye6220.vehicleRoutePlanning.properties.AuthProperties;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.OtpRepository;
import edu.northeastern.csye6220.vehicleRoutePlanning.service.OtpService;
import edu.northeastern.csye6220.vehicleRoutePlanning.util.OtpGenerator;

@Service
public class OtpServiceImpl extends AbstractEntityServiceImpl<Otp> implements OtpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
	
	private final AuthProperties authProperties;
	private final OtpRepository otpRepository;
	
	@Autowired
	public OtpServiceImpl(
			OtpRepository otpRepository,
			AuthProperties authProperties) {
		register(otpRepository);
		this.authProperties = authProperties;
		this.otpRepository = otpRepository;
	}
	
	@Override
	public Otp generateNew(User user) {
		int minimumDigits = authProperties.getMinimumOtpDigits();
		LOGGER.info("generating a new OTP for minimumDigits: {} for user: {}", minimumDigits, user);
		String field = OtpGenerator.createRandomOtp(minimumDigits);
		
		Otp otp = new Otp();
		otp.setField(field);
		otp.setIssuingEmailId(user.getEmail());
		return add(otp);
	}

	@Override
	public Otp findLatestByEmailIdAndField(String emailId, String field) {
		LOGGER.info("finding OTP for emailId: {}, field: {}", emailId, field);
		return otpRepository.findLatestByEmailIdAndField(emailId, field);
	}

}
