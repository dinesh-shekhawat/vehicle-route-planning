package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Otp;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.OtpRepository;

@Service
public class OtpRepositoryImpl extends AbstractEntityRepositoryImpl<Otp> implements OtpRepository {

	@Autowired
	public OtpRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), Otp.class);
	}

}
