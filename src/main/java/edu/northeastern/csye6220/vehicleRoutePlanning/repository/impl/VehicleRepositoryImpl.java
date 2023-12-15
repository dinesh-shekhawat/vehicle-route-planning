package edu.northeastern.csye6220.vehicleRoutePlanning.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.csye6220.vehicleRoutePlanning.entities.Vehicle;
import edu.northeastern.csye6220.vehicleRoutePlanning.hibernate.HibernateConnectionService;
import edu.northeastern.csye6220.vehicleRoutePlanning.repository.VehicleRepository;

@Service
public class VehicleRepositoryImpl extends AbstractEntityRepositoryImpl<Vehicle> implements VehicleRepository {

	@Autowired
	public VehicleRepositoryImpl(HibernateConnectionService hibernateConnectionService) {
		register(hibernateConnectionService.getSessionFactory(), Vehicle.class);
	}

}
